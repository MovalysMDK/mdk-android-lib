package com.adeuza.movalysfwk.mobile.mf4android.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.utils.security.KeyStoreHelper;
import com.adeuza.movalysfwk.mobile.mf4android.utils.security.LocalAuthHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>Activity to display settings</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author smaitre
 */
public class ParameterActivity extends PreferenceActivity { //A2A_DEV retrouver l'héritage MMactivity ?

	/**
	 * Request code to the activity
	 */
	public static final int PREFERENCE_ACTIVITY_REQUEST_CODE = Math.abs(ParameterActivity.class.hashCode()) & NumericConstants.HEXADECIMAL_MASK;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		addPreferencesFromResource(((AndroidApplication)Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.screen_parameters));
	}

	@Override
	protected void onResume() {
		super.onResume();
		// check use of keystore
		if (LocalAuthHelper.getInstance().useKeyStore()) {
			// unlock
			KeyStoreHelper oKSHelper = new KeyStoreHelper(this);
			oKSHelper.unlock();
		}
	}


}
