package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

import java.security.Key;

import android.content.Context;

/**
 * Local authentification helper
 * <p>Simplify access to autentification methods</p>
 *
 */
public class LocalAuthHelper {

	/** key name in keystore */
	public static final String KEY_NAME = "ANDROID_PASSWORD_KEY";
	
	/** singleton */
	private static final LocalAuthHelper mLocalAuthHelper = new LocalAuthHelper();
	
	/** cripto provider */
	private CryptoProvider mCryptoProvider = new AESCBSCryptoProvider();
	
	/**
	 * Singleton getter
	 * @return this
	 */
	public static LocalAuthHelper getInstance() {
		return mLocalAuthHelper;
	}

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
