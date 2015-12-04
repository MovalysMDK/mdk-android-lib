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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.controller;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.RedirectActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.deleteconfiguration.DeleteConfigurationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.AlertMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.DisplayMessageAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayparameterdialog.DisplayParameterDialogAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayresetsettingsandexitdialog.DisplayResetSettingsAndExitDialogAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaysettings.DisplaySettingAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.BarCodeResult;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.DoAfterBarCodeScanAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dobeforephonecall.DoBeforePhoneCallAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dobeforephonecall.PhoneNumber;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.DoOpenMapAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.DoWriteMailAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.importdatabase.ImportDatabaseAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.resetdatabase.ResetDataBaseAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.resetsettings.ResetSettingAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.restartapplicationstartup.RestartApplicationStartupAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.sendreport.SendReportAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.stopapplicationstartup.StopApplicationStartupAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;


/**
 * <p>Default controller.</p>
 *
 *
 */
public abstract class SubControllerTransverse extends SubController {
	
	/**
	 * Constructs a new SubController for resource method
	 *
	 * @param p_oParent the parent
	 */
	protected SubControllerTransverse(FwkController p_oParent) {
		super(p_oParent);
	}

	/**
	 * Action that display the application's settings.
	 */
	protected void doDisplaySetting() {
		this.getController().launchAction(this.getController().getAction(DisplaySettingAction.class), null);
	}
	
	//A2A_DEV homgéniériser les noms setting/parameter
	/**
	 * <p>
	 * 	Method that launch the action to display the activity that asks the user to configure the Movalys application.
	 * </p>
	 */
	protected void doDisplayParameterDialog() {
		this.getController().launchAction(this.getController().getAction(DisplayParameterDialogAction.class), null);
	}
	
	/**
	 * <p>doDisplayResetSettingsAndExitDialog.</p>
	 */
	protected void doDisplayResetSettingsAndExitDialog() {
		this.getController().launchAction(this.getController().getAction(DisplayResetSettingsAndExitDialogAction.class), null);
	}
	
	/**
	 * Action that reset the database.
	 *
	 * @param screen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public abstract void doResetDataBase( Screen screen);
	
	/**
	 * Action that reset the database.
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doSendReport(Screen p_oScreen) {
		this.getController().launchAction(this.getController().getAction(SendReportAction.class), null);
	}
	
	/**
	 * Action that reset the database.
	 */
	public void doImportDatabase() {
		this.getController().launchAction(this.getController().getAction(ImportDatabaseAction.class), null);
	}
	
	/**
	 * Action that reset the application's settings.
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doResetSetting( Screen p_oScreen ) {
		RedirectActionParameterImpl oInParameter = new RedirectActionParameterImpl();
//		oInParameter.redirectTo(this.getController().getAction(DisplaySettingAction.class));
		//on supprime la base car on peux choisir de changer d'utilisateur et donc changer les inters en base.
		oInParameter.redirectTo(this.getController().getAction(ResetDataBaseAction.class));
		this.getController().launchAction(this.getController().getAction(ResetSettingAction.class), oInParameter);
		Application.getInstance().getController().doDisplayResetSettingsAndExitDialog();

		
	}
	
	/**
	 * Launch a PhoneCall Action {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dobeforephonecall.DoBeforePhoneCallAction}
	 *
	 * @param p_oPhoneNumber the Phone Number {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dobeforephonecall.PhoneNumber}
	 */
	public void doPhoneCall(PhoneNumber p_oPhoneNumber) {
		this.getController().launchAction(this.getController().getAction(DoBeforePhoneCallAction.class), p_oPhoneNumber);
	}
	
	/**
	 * Launch a  {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.DisplayMessageAction}
	 *
	 * @param p_oAlertMessage the Alert message {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.AlertMessage}
	 */
	public void doDisplayMessage(AlertMessage p_oAlertMessage) {
		this.getController().launchAction(this.getController().getAction(DisplayMessageAction.class), p_oAlertMessage);
	}
	
	/**
	 * Launch a  {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.DoWriteMailAction}
	 *
	 * @param p_oEMail the fields to populate the "Compose mail" native GUI {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail}
	 */
	public void doWriteEmail(EMail p_oEMail) {
		this.getController().launchAction(this.getController().getAction(DoWriteMailAction.class), p_oEMail);
	}
	
	/**
	 * Launch a  {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.DoOpenMapAction}
	 *
	 * @param p_oAddressLocation the coordinates and/or address to locate de target {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation}
	 */
	public void doOpenMap(AddressLocation p_oAddressLocation) {
		this.getController().launchAction(this.getController().getAction(DoOpenMapAction.class), p_oAddressLocation);
	}
	
	/**
	 * Launch a  {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.DoAfterBarCodeScanAction}
	 *
	 * @param p_oInOutBarCode the type of barcode to scan {@link InOutBarCode}
	 * @return InOutBarCode populated with value of the barcode and type of barcode scanned
	 */
	public BarCodeResult doAfterBarCodeScan(BarCodeResult p_oInOutBarCode) {
		return (BarCodeResult) this.getController().launchAction(this.getController().getAction(DoAfterBarCodeScanAction.class), p_oInOutBarCode);
	}	
	
	/**
	 * Action to stop the application startup.
	 */
	public void doStopApplicationStartup() {
		this.getController().launchAction(this.getController().getAction(StopApplicationStartupAction.class), null);
	}
	
	/**
	 * Action to restart the application startup.
	 */
	public void doRestartApplicationStartup() {
		this.getController().launchAction(this.getController().getAction(RestartApplicationStartupAction.class), null);
	}
	
	/**
	 * Action to delete the application's configuration.
	 */
	public void doDeleteSerializedConfiguration() {
		this.getController().launchAction(this.getController().getAction(DeleteConfigurationAction.class), null);
	}
	
	/**
	 * Launch the action to display the exit dialog.
	 *
	 * @param screen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public abstract void doDisplayExitApplicationDialog( Screen screen );
	
	/**
	 * Action to display the Main screen.
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public abstract void doDisplayMain( Screen p_oScreen );
}
