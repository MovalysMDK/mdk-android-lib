package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4android.actiontask.ActionASyncTask;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.application.MFApplicationHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ActionDialogFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ProgressDialog;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>TODO Décrire la classe ProgressDialogFactoryImpl</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */
@Scope(ScopePolicy.SINGLETON)
public class ActionDialogFactoryImpl implements ActionDialogFactory, OnDismissListener {

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ActionDialogFactory#createProgessDialog(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen, com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask)
	 */
	@Override
	public ProgressDialog createProgressDialog(MContext p_oContext, Screen p_oScreen, MMActionTask<?, ?, ?, ?> p_oActionTask) {
		MMDialogFragment r_oProgressDialog = null ;
		
		ActionBarActivity oActivity = (ActionBarActivity) p_oScreen;
		
		if (p_oActionTask != null && SynchronizationAction.class.isAssignableFrom(p_oActionTask.getAction().getClass())) {
			r_oProgressDialog = (SynchroProgressDialogFragment) 
					oActivity.getSupportFragmentManager().findFragmentByTag("synchroProgressDialog");
			if ( r_oProgressDialog == null ) {
				r_oProgressDialog = SynchroProgressDialogFragment.newInstance(this);
				((DialogFragment) r_oProgressDialog).show(oActivity.getSupportFragmentManager(), "synchroProgressDialog");
			}
		}
		else {
			r_oProgressDialog = DefaultProgressDialogFragment.newInstance(this);
			 r_oProgressDialog.show(oActivity.getSupportFragmentManager(), r_oProgressDialog.getFragmentDialogTag());

		}
		return (ProgressDialog)r_oProgressDialog;

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

			AndroidApplication oApp = (AndroidApplication)Application.getInstance();
			
			
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
}
