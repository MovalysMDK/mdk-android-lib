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
package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.io.File;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.application.MFAndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.database.DBImportExportHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMCustomDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMButton;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEditText;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;


/**
 * <p>Dialog to write the password to launch an database import.</p>
 *
 *
 */
public class DBImportValidationActivity extends AbstractMMActivity implements OnClickListener {

	/** edittext field */
	private MMEditText passfield;
	
	/** ok button */
	private MMButton okBtn;
	
	/** ko button */
	private MMButton koBtn;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		setContentView(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.fwk_password_dialog_to_import_database));
		
		passfield=(MMEditText) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.dialog_confirmPasswordForImportDatabase_content_edit));
		okBtn=(MMButton) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.dialog_confirmPasswordForImportDatabase_ok_button));
		okBtn.setOnClickListener(this);
		koBtn=(MMButton) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.dialog_confirmPasswordForImportDatabase_ko_button));
		koBtn.setOnClickListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oView) {
		if(p_oView.getId()==okBtn.getId()){
			String sValidPwd = Application.getInstance().getStringResource(FwkPropertyName.dbcrypt_keystorepass);
			Log.i("DBImportValidation", "pass_field.getText() = "+passfield.getText()+" # sValidPwd = "+sValidPwd);
			if ( sValidPwd.equals(passfield.getText().toString())) {
				
				AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
				AbstractMMActivity oCurrentActivity = (AbstractMMActivity) oApplication.getCurrentVisibleActivity();
				File oFileToImport = (File) getIntent().getExtras().getSerializable("importFile");
				DBImportExportHelper.importDatabase(oFileToImport, this);
				showExitApplicationDialog(oCurrentActivity, oApplication);
			}
			else{
				passfield.setError(this.getAndroidApplication().getStringResource(AndroidApplicationR.dialog_confirmPasswordForImportDatabase_error_text));
				passfield.setText("");
			}
		}
		else if (p_oView.getId()==koBtn.getId()) {
			finish();
		}
	}
	
	/**
	 * Shows the exit application dialog
	 * @param p_oCurrentActivity the currently displayed activity
	 * @param p_oApplication the application object
	 */
	public void showExitApplicationDialog( AbstractMMActivity p_oCurrentActivity, AndroidApplication p_oApplication ) {
		
		MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder(p_oCurrentActivity);
		oBuilder.setTitle(AndroidApplicationR.import_database_popup_title);
		oBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		oBuilder.setMessage(AndroidApplicationR.import_database_exit_application);
		oBuilder.setPositiveButton(AndroidApplicationR.import_database_popup_OK_button,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface p_oDialog, int p_iId) {
						p_oDialog.dismiss();						
						((MFAndroidApplication) getApplication()).launchStopApplication();
					}
				});
		MMCustomDialogFragment oAlert = oBuilder.create();
		oAlert.setCancelable(false);
		oAlert.show(getSupportFragmentManager(), oAlert.getFragmentDialogTag());

	}
}
