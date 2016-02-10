package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

import java.security.Key;

/**
 * Crypto Provider
 *
 */
public interface CryptoProvider {

	/**
	 * encryption method
	 * @param p_oKey the encryption key
	 * @param p_sText the test to encrypt
	 * @return the encrypted string
	 */
	public String encrypt( Key p_oKey, String p_sText);
	
	/**
	 * decryption method
	 * @param p_oKey the decryption key
	 * @param p_sText the encrypted text
	 * @return the decrypted text
	 */
	public String decrypt( Key p_oKey, String p_sText);
	
	/**
	 * Return the encryption algorithm name
	 * @return the algorithm name
	 */
	public String getAlgoName();
	
	/**
	 * Return if use Android KeyStore
	 * @return true if it use Android KeyStore, false otherwise
	 */
	public boolean useKeyStore();

	/**
	 * Return the decryption key (and generate/store it if nessecary)
	 * @param p_oKeyStoreHelper the keystore (do not use if useKeyStore return false)
	 * @return the decryption key
	 */
	public Key getDecryptionKey(KeyStoreHelper p_oKeyStoreHelper);

	/**
	 * Return the encryption key (and generate/store it if nessecary)
	 * @param p_oKeyStoreHelper the keystore (do not use if useKeyStore return false)
	 * @return the encryption key
	 */
	public Key getEncryptionKey(KeyStoreHelper p_oKeyStoreHelper);
}
