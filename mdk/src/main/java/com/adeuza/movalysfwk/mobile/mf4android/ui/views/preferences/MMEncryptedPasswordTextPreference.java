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
/**
 * 
 */
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.security.LocalAuthHelperImpl;

/**
 * Encrypted password input component
 */
public class MMEncryptedPasswordTextPreference extends MMEditTextPreference {
	private static final String TAG = "MMEncryptedPasswordTextPreference";

	/**
	 * Constructor
	 * @param p_oContext context to use
	 */
	public MMEncryptedPasswordTextPreference(Context p_oContext) {
		super(p_oContext);
	}
	
	/**
	 * Constructor
	 * @param p_oContext context to use
	 * @param p_oAttrs XML attributes to use
	 */
	public MMEncryptedPasswordTextPreference(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * @param p_oContext context to use
	 * @param p_oAttrs XML attributes to use
	 * @param p_oDefStyle style to apply
	 */
	public MMEncryptedPasswordTextPreference(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
	}

	@Override
	protected void onDialogClosed(boolean p_bPositiveResult) {
		try {
			String sPassword = getEditText().getText().toString();
			String sEncryptedPassword = "";
			if (sPassword.length() != 0) {
				sEncryptedPassword = LocalAuthHelperImpl.getInstance().encrypt(this.getContext(), sPassword);
			}
			super.getEditText().setText(sEncryptedPassword);
		} catch (Exception e) {
			throw new RuntimeException(TAG+" :Encryption probleme detected", e);
		}
		super.onDialogClosed(p_bPositiveResult);
	}

	@Override
	protected void onBindDialogView(View p_oView) {
		super.onBindDialogView(p_oView);
		try {
			String sEncryptedPassword = getEditText().getText().toString();
			String sPassword = "";
			if (sEncryptedPassword.length() != 0) {
				sPassword = LocalAuthHelperImpl.getInstance().decrypt(this.getContext(), sEncryptedPassword);
			}
			this.getEditText().setText(sPassword);
			this.getEditText().setSelection(sPassword.length());
		} catch (Exception e) {
			throw new RuntimeException(TAG+" :Decryption probleme detected", e);
		}
	}	
}
