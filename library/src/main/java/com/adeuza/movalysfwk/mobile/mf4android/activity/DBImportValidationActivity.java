package com.adeuza.movalysfwk.mobile.mf4android.activity;

import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.dialog_confirmPasswordForImportDatabase_content_edit;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.dialog_confirmPasswordForImportDatabase_error_text;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.dialog_confirmPasswordForImportDatabase_ko_button;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.dialog_confirmPasswordForImportDatabase_ok_button;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.fwk_password_dialog_to_import_database;

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
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 */
public class DBImportValidationActivity extends AbstractMMActivity implements OnClickListener {

	private MMEditText passfield;
	private MMButton okBtn;
	private MMButton koBtn;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		setContentView(this.getAndroidApplication().getAndroidIdByRKey(fwk_password_dialog_to_import_database));
		
		passfield=(MMEditText) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(dialog_confirmPasswordForImportDatabase_content_edit));
		okBtn=(MMButton) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(dialog_confirmPasswordForImportDatabase_ok_button));
		okBtn.setOnClickListener(this);
		koBtn=(MMButton) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(dialog_confirmPasswordForImportDatabase_ko_button));
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
				passfield.setError(this.getAndroidApplication().getStringResource(dialog_confirmPasswordForImportDatabase_error_text));
				passfield.setText("");
			}
		}
		else if (p_oView.getId()==koBtn.getId()) {
			finish();
		}
	}
	
	/**
	 * @param p_oCurrentActivity
	 * @param p_oApplication
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
