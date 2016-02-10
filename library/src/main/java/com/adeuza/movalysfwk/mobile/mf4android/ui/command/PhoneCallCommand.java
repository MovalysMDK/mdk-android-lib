package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.AlertMessage;

/**
 * <p>Command patern to use the phone capacity</p>
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author dmaurange
 *
 */

public final class PhoneCallCommand {
	
	/** instance of handler for singleton patern */
	private static PhoneCallCommand instance = null;

	/**
	 * Get the singleton instance
	 * @return the instance of {@link #PhoneCallCommand()}
	 */
	public static PhoneCallCommand getInstance() {
		if (PhoneCallCommand.instance == null) {
			PhoneCallCommand.instance = new PhoneCallCommand();
		}
		return PhoneCallCommand.instance;
	}
	
	/** private constructor for singleton pattern*/
	private PhoneCallCommand(){
		
	}
	
	/**
	 * 
	 * Dial the phone number
	 * @param p_sNumero the phone number to dial. Format tested on the Android platform : <ul>
	 * <li>+33251896689</li>
	 * <li>0251896689</li>
	 * </ul>
	 * @param p_oContext 
	 * 
	 */
	public void dial(String p_sNumero, Context p_oContext){
		String sToDial = StringUtils.concat("tel:", p_sNumero);

		TelephonyManager oTelephonyManager=(TelephonyManager) p_oContext.getSystemService(Context.TELEPHONY_SERVICE);
		//si le telephone est disponible
		if (TelephonyManager.CALL_STATE_IDLE==oTelephonyManager.getCallState()){
			((AndroidApplication) AndroidApplication.getInstance()).startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(sToDial)));
		}
		else{
			(Application.getInstance().getController()).doDisplayMessage(new AlertMessage(AndroidApplicationR.alert_phonecall_notiddle, AlertMessage.SHORT));
		}
	}
}
