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

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.security.LocalAuthHelperImpl;
import com.adeuza.movalysfwk.mobile.mf4android.utils.security.KeyStoreHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>Activity to display settings</p>
 *
 *
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
		if (LocalAuthHelperImpl.getInstance().useKeyStore()) {
			// unlock
			KeyStoreHelper oKSHelper = new KeyStoreHelper(this);
			oKSHelper.unlock();
		}
	}


}
