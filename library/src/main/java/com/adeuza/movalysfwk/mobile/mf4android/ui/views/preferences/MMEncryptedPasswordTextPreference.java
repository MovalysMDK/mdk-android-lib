/**
 * 
 */
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.utils.security.LocalAuthHelper;

/**
 * @author dmaurange
 *
 */
public class MMEncryptedPasswordTextPreference extends MMEditTextPreference {
	private static final String TAG = "MMEncryptedPasswordTextPreference";

	/**
	 * @param p_oContext
	 */
	public MMEncryptedPasswordTextPreference(Context p_oContext) {
		super(p_oContext);
	}
	/**
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMEncryptedPasswordTextPreference(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * @param p_oContext
	 * @param p_oAttrs
	 * @param p_oDefStyle
	 */
	public MMEncryptedPasswordTextPreference(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
	}

	@Override
	protected void onDialogClosed(boolean p_bPositiveResult) {
		try {
			super.getEditText().setText( LocalAuthHelper.getInstance().encrypt(this.getContext(), getEditText().getText().toString()) );
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
				sPassword = LocalAuthHelper.getInstance().decrypt(this.getContext(), sEncryptedPassword);
			}
			this.getEditText().setText(sPassword);
			this.getEditText().setSelection(sPassword.length());
		} catch (Exception e) {
			throw new RuntimeException(TAG+" :Decryption probleme detected", e);
		}
	}	
}
