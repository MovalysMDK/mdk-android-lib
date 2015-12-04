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
package com.adeuza.movalysfwk.mf4jcommons.crypt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * Android keystore helper
 *
 */
public final class KeyStoreHelper {

	/**
	 * BKS (keystore format on android)
	 */
	private static final String BKS = "BKS";

	/**
	 * Empty constructor
	 * cannot be instanciate
	 */
	private KeyStoreHelper() {
	}
	
	/**
	 * Creates a keystore for Android
	 *
	 * @param p_sKeyStoreFile a {@link java.lang.String} object.
	 * @param p_sKeyStorePassword the password of the keystore
	 * @param p_sAlias the alias of the keystore
	 * @param p_oKey the public key for the certficate
	 * @throws java.security.KeyStoreException exception
	 * @throws java.security.NoSuchProviderException exception
	 * @throws java.security.NoSuchAlgorithmException exception
	 * @throws java.security.cert.CertificateException exception
	 * @throws java.io.IOException exception
	 */
	public static void createKeyStoreForAndroid(String p_sKeyStoreFile, String p_sKeyStorePassword,
			String p_sAlias, Key p_oKey) throws KeyStoreException, NoSuchProviderException,
			NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore oKeyStore = KeyStore.getInstance(BKS);
		oKeyStore.load(null, null);
		oKeyStore.setKeyEntry(p_sAlias, p_oKey, null, null);
		FileOutputStream fos = new FileOutputStream(p_sKeyStoreFile);
		try {
			oKeyStore.store(fos, p_sKeyStorePassword.toCharArray());
			fos.flush();
		} finally {
			fos.close();
		}
	}

	/**
	 * loads the key for an Android keystore
	 *
	 * @param p_sKeyStoreFile the file name of the keystore
	 * @param p_sKeyStorePassword the password of the keystore
	 * @param p_sAlias the alias of the keystore
	 * @param p_sProvider a {@link java.lang.String} object.
	 * @return the key
	 * @throws java.security.KeyStoreException exception
	 * @throws java.security.NoSuchProviderException exception
	 * @throws java.security.NoSuchAlgorithmException exception
	 * @throws java.security.cert.CertificateException exception
	 * @throws java.io.IOException exception
	 * @throws java.security.UnrecoverableKeyException exception
	 */
	public static Key loadKeyForAndroidFromKeyStore(String p_sKeyStoreFile, String p_sKeyStorePassword,
			String p_sAlias, String p_sProvider) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException,
			CertificateException, IOException, UnrecoverableKeyException {
		Key r_oKey = null ;
		FileInputStream oIs = new FileInputStream(p_sKeyStoreFile);
		try {
			r_oKey = loadKeyForAndroidFromKeyStore(oIs, p_sKeyStorePassword, p_sAlias, p_sProvider);
		} finally {
			oIs.close();
		}
		return r_oKey ;
	}
	
	/**
	 * loads a keystore from an inpustream
	 *
	 * @param p_oIs the inputstream
	 * @param p_sKeyStorePassword the keystore password
	 * @return the decrypted keystore
	 * @throws java.security.KeyStoreException exception
	 * @throws java.security.NoSuchProviderException exception
	 * @throws java.security.NoSuchAlgorithmException exception
	 * @throws java.security.cert.CertificateException exception
	 * @throws java.io.IOException exception
	 * @throws java.security.UnrecoverableKeyException exception
	 */
	public static KeyStore loadKeyStore(InputStream p_oIs, String p_sKeyStorePassword)
			throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException,
					CertificateException, IOException, UnrecoverableKeyException {

		return loadKeyStore(p_oIs, p_sKeyStorePassword, null);
	}

	/**
	 * Loads a keystore from an inpustream using a given provider
	 *
	 * @param p_oIs the inputstream
	 * @param p_sKeyStorePassword the keystore password
	 * @param p_sProvider the provider
	 * @return the decrypted keystore
	 * @throws java.security.KeyStoreException exception
	 * @throws java.security.NoSuchProviderException exception
	 * @throws java.security.NoSuchAlgorithmException exception
	 * @throws java.security.cert.CertificateException exception
	 * @throws java.io.IOException exception
	 */
	public static KeyStore loadKeyStore(InputStream p_oIs, String p_sKeyStorePassword, String p_sProvider)
			throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException,
					CertificateException, IOException {

		KeyStore r_oKeyStore = null;
		if ( p_sProvider != null ) {
			r_oKeyStore = KeyStore.getInstance(BKS, p_sProvider);
		} else {
			r_oKeyStore = KeyStore.getInstance(BKS);
		}
		r_oKeyStore.load(p_oIs, p_sKeyStorePassword.toCharArray());

		return r_oKeyStore;
	}
	
	/**
	 * Loads a key from an android keystore
	 *
	 * @param p_oIs the inputstream of the keystore
	 * @param p_sKeyStorePassword the password of the keystore
	 * @param p_sAlias the alias of the keystore
	 * @param p_sProvider a {@link java.lang.String} object.
	 * @return the key
	 * @throws java.security.KeyStoreException exception
	 * @throws java.security.NoSuchProviderException exception
	 * @throws java.security.NoSuchAlgorithmException exception
	 * @throws java.security.cert.CertificateException exception
	 * @throws java.io.IOException exception
	 * @throws java.security.UnrecoverableKeyException exception
	 */
	public static Key loadKeyForAndroidFromKeyStore( InputStream p_oIs, String p_sKeyStorePassword,
			String p_sAlias, String p_sProvider) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException,
			CertificateException, IOException, UnrecoverableKeyException {
		Key r_oKey = null;
		KeyStore oKeyStore = null;
		if ( p_sProvider != null ) {
			oKeyStore = KeyStore.getInstance(BKS, p_sProvider);
		} else {
			oKeyStore = KeyStore.getInstance(BKS);
		}
		oKeyStore.load(p_oIs, p_sKeyStorePassword.toCharArray());
		if ( oKeyStore.isKeyEntry(p_sAlias)) {
			r_oKey = oKeyStore.getKey(p_sAlias, null);
		}
		else {
			throw new IllegalArgumentException("Can't find key '"+ p_sAlias + "' in keystore.");
		}
		return r_oKey;
	}
}
