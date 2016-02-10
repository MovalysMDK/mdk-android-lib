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
		Key r_oSecretKey = p_oKeyStoreHelper.loadKey(LocalAuthHelper.KEY_NAME, ALGO_NAME);
		if (r_oSecretKey == null) {
			r_oSecretKey = AESUtil2.generateKey();
			p_oKeyStoreHelper.storeKey(LocalAuthHelper.KEY_NAME, r_oSecretKey);
		}
		return r_oSecretKey;
	}
}
