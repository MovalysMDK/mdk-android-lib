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

import android.util.Log;

/**
 * AES Crypto Provider
 *
 */
public class AESCBSCryptoProvider implements CryptoProvider {

	/** algorithm name */
	private static final String ALGO_NAME = "AES";
	/** key generation algorithm */
	private static final String SR_ALGO_NAME = "SHA1PRNG";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAlgoName() {
		return ALGO_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encrypt(Key p_oKey, String p_sText) {
		Log.d("AESCBSCryptoProvider", "encrypt #################"+AESUtil2.encryptAesCbc(p_sText, p_oKey, SR_ALGO_NAME));
		return AESUtil2.encryptAesCbc(p_sText, p_oKey, SR_ALGO_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String decrypt(Key p_oKey, String p_sText) {
		Log.d("AESCBSCryptoProvider", "decrypt #################"+AESUtil2.decryptAesCbc(p_sText, p_oKey));
		return AESUtil2.decryptAesCbc(p_sText, p_oKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean useKeyStore() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Key getDecryptionKey(KeyStoreHelper p_oKeyStoreHelper) {
		return getSecretKey(p_oKeyStoreHelper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Key getEncryptionKey(KeyStoreHelper p_oKeyStoreHelper) {
		return getSecretKey(p_oKeyStoreHelper);
	}

	/**
	 * Genere/Store/Get SecretKey
	 * @param p_oKeyStoreHelper the KeyStore to use
	 * @return the AES SecretKey
	 */
	private Key getSecretKey(KeyStoreHelper p_oKeyStoreHelper) {
		Key r_oSecretKey = p_oKeyStoreHelper.loadKey(AbstractLocalAuthHelper.KEY_NAME, ALGO_NAME);
		if (r_oSecretKey == null) {
			r_oSecretKey = AESUtil2.generateKey();
			p_oKeyStoreHelper.storeKey(AbstractLocalAuthHelper.KEY_NAME, r_oSecretKey);
		}
		return r_oSecretKey;
	}
}
