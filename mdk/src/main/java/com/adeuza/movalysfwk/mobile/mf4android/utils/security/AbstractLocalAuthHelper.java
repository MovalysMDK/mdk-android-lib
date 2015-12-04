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
package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

import java.security.Key;

import android.content.Context;

import com.adeuza.movalysfwk.mf4jcommons.crypt.LocalAuthHelper;

/**
 * Local authentication helper
 * <p>Simplify access to authentication methods</p>
 *
 */
public abstract class AbstractLocalAuthHelper implements LocalAuthHelper {

	/** key name in keystore */
	public static final String KEY_NAME = "ANDROID_PASSWORD_KEY";
	
	/** crypto provider */
	private CryptoProvider mCryptoProvider = new AESCBSCryptoProvider();

	/**
	 * decryption method
	 * @param p_oContext the android context
	 * @param p_sEncryptedString the encrypted string
	 * @return decrypted string
	 */
	public String decrypt(Context p_oContext, String p_sEncryptedString) {
		Key oDecryptKey = this.mCryptoProvider.getDecryptionKey(new KeyStoreHelper(p_oContext));
		return this.mCryptoProvider.decrypt(oDecryptKey, p_sEncryptedString);
	}

	/**
	 * encryption method
	 * @param p_oContext the android context
	 * @param p_sPassphrase the passphrase to encrypt
	 * @return the encrypted string
	 */
	public String encrypt(Context p_oContext, String p_sPassphrase) {
		Key oEncryptKey = this.mCryptoProvider.getEncryptionKey(new KeyStoreHelper(p_oContext));
		return this.mCryptoProvider.encrypt(oEncryptKey, p_sPassphrase);
	}

	/**
	 * Setter for a new CryptoProvider
	 * @param p_oCryptoProvider the new CryptoProvider
	 */
	public void setCryptoProvider(CryptoProvider p_oCryptoProvider) {
		this.mCryptoProvider = p_oCryptoProvider;
	}
	
	/**
	 * Return if the CryptoProvider uses the android keystore
	 * @return true if it use the keystore, false otherwise
	 */
	public boolean useKeyStore() {
		return this.mCryptoProvider.useKeyStore();
	}
}
