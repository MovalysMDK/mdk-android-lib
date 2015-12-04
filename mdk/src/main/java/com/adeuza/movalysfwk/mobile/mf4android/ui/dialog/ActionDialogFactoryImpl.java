/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.support.v7.app.ActionBarActivity;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4android.actiontask.ActionASyncTask;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.application.MFApplicationHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ActionDialogFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ProgressDialog;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Factory for ProgressDialog of actions.</p>
 */
@Scope(ScopePolicy.SINGLETON)
public class ActionDialogFactoryImpl implements ActionDialogFactory, OnDismissListener {

	/**
	 * Tag for the default progress dialog
	 */
	public static final String DEFAULT_PROGRESS_DIALOG_TAG = "defaultProgressDialog";
	
	/**
	 * Tag for the synchronization dialog
	 */
	public static final String SYNCHRO_PROGRESS_DIALOG_TAG = "synchroProgressDialog";
	
	/**
	 * List of action classes with progress dialog disabled
	 */
	protected Set<Class<? extends Action<?,?,?,?>>> actionsWithoutProgressDialog = new HashSet<>();
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ActionDialogFactory#createProgessDialog(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen, com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask)
	 */
	@Override
	public ProgressDialog createProgressDialog(MContext p_oContext, MMActionTask<?, ?, ?, ?> p_oActionTask) {
		MMDialogFragment r_oProgressDialog = null ;
		
		if ( !isProgressDialogDisabled(p_oActionTask)) {
		
			// Create dialog only if display active
			ActionBarActivity oActivity = (ActionBarActivity) Application.getInstance().getScreenObjectFromName(p_oActionTask.getScreenIdentifier());
			if ( oActivity != null ) {
				
				String sTag = getDialogTag(p_oActionTask);
				if ( !Application.getInstance().hasDialogOfType(sTag, p_oActionTask.getScreenIdentifier())) {
					try {
						r_oProgressDialog = this.createDialogInstance((MMActionTask<?, ?, ?, ?>) p_oActionTask);
						r_oProgressDialog.show(oActivity.getSupportFragmentManager(), sTag);
						Application.getInstance().registerProgressDialog(sTag, p_oActionTask.getScreenIdentifier());
					} catch(IllegalStateException e) {
						r_oProgressDialog = null;
					}
				}
			}
		}
		
		return (ProgressDialog) r_oProgressDialog;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ActionDialogFactory#createPreExecuteActionDialog(com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen, com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask)
	 */
	@Override
	public Dialog createPreExecuteDialog(MContext p_oContext, Screen p_oScreen, MMActionTask<?, ?, ?, ?> p_oActionTask) {
		if (p_oActionTask != null && SynchronizationAction.class.isAssignableFrom(p_oActionTask.getAction().getClass())) {
			final MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder((Activity)p_oScreen);

			oBuilder.setTitle(AndroidApplicationR.screen_synchro_activity_name);
			oBuilder.setIcon(android.R.drawable.ic_popup_sync);
			oBuilder.setMessage(AndroidApplicationR.screen_synchronisation_request);
			oBuilder.setPositiveButton(AndroidApplicationR.generic_message_yes, (ActionASyncTask<?, ?, ?, ?>) p_oActionTask);
			oBuilder.setNegativeButton(AndroidApplicationR.generic_message_no, new OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog, int p_iInt) {
					p_oDialog.dismiss();
					MFApplicationHolder.getInstance().getApplication().launchStopApplication();
				}
			});
			
			final MMCustomDialogFragment oAlert = oBuilder.create();
			oAlert.setCancelable(false);

			return (Dialog) oAlert.getDialog();
		}
		return null;
	}
	
	@Override
	public void onDismiss(DialogInterface p_oDialog) {
		// Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroyProgressDialog(MMActionTask<?, ?, ?, ?> p_oActionTask) {
		
		if ( !isProgressDialogDisabled(p_oActionTask)) {
		
			String sDialogTag = getDialogTag(p_oActionTask);
			
			// Don't destroy dialog if another task exists (running or pending) with the same dialog type and on the same screen
			if ( !Application.getInstance().hasRunningOrPendingActionWithDialogType(sDialogTag, p_oActionTask.getScreenIdentifier())) {
				
				// Close dialog
				ActionBarActivity oActivity = (ActionBarActivity) Application.getInstance().getScreenObjectFromName(p_oActionTask.getScreenIdentifier());
				if ( oActivity != null ) {
					ProgressDialog oDialog = (ProgressDialog) oActivity.getSupportFragmentManager().findFragmentByTag(sDialogTag);
					if ( oDialog != null ) {
						oDialog.close();
					}
				}
	
				// Unregister it
				Application.getInstance().unregisterProgressDialog(sDialogTag, p_oActionTask.getScreenIdentifier());
			}
		}
	}
	
	/**
	 * Get tag to use for the dialog of the action task
	 * @param p_oActionTask action task
	 * @return tag
	 */
	public String getDialogTag( MMActionTask<?, ?, ?, ?> p_oActionTask ) {
		String r_sDialogTag = null;
		if (p_oActionTask != null && SynchronizationAction.class.isAssignableFrom(p_oActionTask.getAction().getClass())) {
			r_sDialogTag = SYNCHRO_PROGRESS_DIALOG_TAG;
		}
		else {
			r_sDialogTag = DEFAULT_PROGRESS_DIALOG_TAG;
		}
		return r_sDialogTag;
	}
	
	/**
	 * Create the dialog instance
	 * @param p_oActionTask action task
	 * @return dialog instance
	 */
	protected MMDialogFragment createDialogInstance( MMActionTask<?, ?, ?, ?> p_oActionTask ) {
		MMDialogFragment r_oDialog = null;
		if (p_oActionTask != null && SynchronizationAction.class.isAssignableFrom(p_oActionTask.getAction().getClass())) {
			r_oDialog = createSynchroDialogInstance();
		}
		else {
			r_oDialog = createDefaultDialogInstance();
		}
		return r_oDialog;
	}
	
	/**
	 * Create the instance of synchronization dialog
	 * @return instance of synchronization dialog
	 */
	protected MMDialogFragment createSynchroDialogInstance() {
		return SynchroProgressDialogFragment.newInstance(this);
	}
	
	/**
	 * Create the instance of the default dialog
	 * @return instance of the default dialog
	 */
	protected MMDialogFragment createDefaultDialogInstance() {
		return DefaultProgressDialogFragment.newInstance(this);
	}
	
	/**
	 * Return true if progress dialog is disabled
	 * @param p_oActionTask action task
	 * @return true if progress dialog is disabled
	 */
	protected boolean isProgressDialogDisabled( MMActionTask<?, ?, ?, ?> p_oActionTask ) {
		return this.actionsWithoutProgressDialog.contains(p_oActionTask.getAction().getClass()) || p_oActionTask.isProgressDialogDisabled();
	}
}
