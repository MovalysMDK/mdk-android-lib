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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.importdatabase;

import java.io.File;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.DBImportValidationActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMCustomDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.importdatabase.ImportDatabaseAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * import database action
 *
 */
public class ImportDatabaseActionImpl implements ImportDatabaseAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6586202590869381915L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, NullActionParameterImpl p_oParameterIn) {
		
		Log.i(Application.LOG_TAG, "Import database action");
		
		Context oAndroidContext = ((AndroidContextImpl)p_oContext).getAndroidNativeContext();
		
		AbstractMMActivity oCurrentActivity = (AbstractMMActivity) ((AndroidApplication) Application.getInstance()).getCurrentVisibleActivity();

		String sState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sState) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(sState)) {
		
			File oImportDir = oAndroidContext.getExternalFilesDir("importdb");
			Log.i(Application.LOG_TAG, "  dir:" + oImportDir.getAbsolutePath());
			
			File[] t_sFiles = oImportDir.listFiles();
			if ( t_sFiles == null || t_sFiles.length == 0 ) {
				this.showImportErrorDialog(oCurrentActivity, AndroidApplicationR.import_database_filenotfound);
			}
			else {
				File oImportFile = t_sFiles[0];
				Log.i(Application.LOG_TAG, "  found file:" + oImportFile.getAbsolutePath());
				this.showConfirmImportDialog(oImportFile, oCurrentActivity, p_oContext);
			}
		} else {
			this.showImportErrorDialog(oCurrentActivity, AndroidApplicationR.import_database_nosdcard);
		}
		
		return null;
	}
	
	/**
	 * show confirm import dialog
	 * @param p_oCurrentActivity current activity
	 * @param p_oImportFile import file
	 * @param p_oContext context 
	 */
	protected void showConfirmImportDialog( final File p_oImportFile, final AbstractMMActivity p_oCurrentActivity, final MContext p_oContext) {
		final MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder(p_oCurrentActivity);

		oBuilder.setTitle(AndroidApplicationR.import_database_popup_title);
		oBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		oBuilder.setMessage(AndroidApplicationR.import_database_confirm_import);
		oBuilder.setPositiveButton(AndroidApplicationR.import_database_popup_OK_button,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface p_oDialog, int p_iId) {
						// on ferme la popup
						p_oDialog.dismiss();
						
						Intent oIntent= new Intent(((AndroidContextImpl)p_oContext).getAndroidNativeContext(), DBImportValidationActivity.class);
						oIntent.putExtra("importFile", p_oImportFile);
						((AndroidApplication) Application.getInstance()).startActivity(oIntent);
					}
				});
		oBuilder.setNegativeButton(AndroidApplicationR.import_database_popup_KO_button,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface p_oDialog, int p_iId) {
						// fermeture de la popup
						p_oDialog.cancel();
					}
				});

		final MMCustomDialogFragment oAlert = oBuilder.create();
		oAlert.setCancelable(false);
		oAlert.show(p_oCurrentActivity.getSupportFragmentManager(), MMCustomDialogFragment.CUSTOM_DIALOG_TAG);
	}

	/**
	 * Show database not found dialog
	 * @param p_oCurrentActivity current activity
	 * @param p_oMessage android application R
	 */
	public void showImportErrorDialog(final AbstractMMActivity p_oCurrentActivity, final AndroidApplicationR p_oMessage ) {
		final MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder(p_oCurrentActivity);

		oBuilder.setTitle(AndroidApplicationR.import_database_popup_title);
		oBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		oBuilder.setMessage(p_oMessage);
		oBuilder.setPositiveButton(AndroidApplicationR.import_database_popup_OK_button,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface p_oDialog, int p_iId) {
						p_oDialog.dismiss();
					}
				});

		final MMCustomDialogFragment oAlert = oBuilder.create();
		oAlert.setCancelable(false);
		oAlert.show(p_oCurrentActivity.getSupportFragmentManager(), MMCustomDialogFragment.CUSTOM_DIALOG_TAG);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn,NullActionParameterImpl p_oResultOut) {
		//do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		// nothing to do
		return p_oResultOut;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(NullActionParameterImpl p_oNullActionParameterImpl, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oParameterOut)
			throws ActionException {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESS[])
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void... p_oProgressInformations) {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritableDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConcurrentAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl getEmptyInParameter() {
		return new NullActionParameterImpl();
	}
}
