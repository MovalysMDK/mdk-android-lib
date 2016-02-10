package com.adeuza.movalysfwk.mobile.mf4android.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.crypt.KeyStoreHelper;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.auth.UserKeyStoreHelper;

/**
 * Helper for user KeyStore, to be created in res/raw folder<br/>
 * Aimed at storing certificates for SSL connections
 */
public class UserKeyStoreHelperImpl implements UserKeyStoreHelper {

	/** Log Tag */
	private static final String TAG = UserKeyStoreHelperImpl.class.getName();
	
	/**
	 * loads a keystore with its name from the res/raw directory
	 * @param p_sKeyStoreFile the full name of the file (including extension) 
	 * @param p_sKeyStorePass the password protecting the file
	 * @return the decrypted keystore
	 */
	@Override
	public final KeyStore loadKeyStoreFromRaw(String  p_sKeyStoreFile, String p_sKeyStorePass) {
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		InputStream oKeyStoreStream;
		
		try {
			String sKeyStore = p_sKeyStoreFile.substring(0, p_sKeyStoreFile.indexOf("."));
			int iId = oApplication.getApplicationContext().getResources().getIdentifier("raw/" + sKeyStore, "raw", oApplication.getApplicationContext().getPackageName());
			oKeyStoreStream = oApplication.getApplicationContext().getResources().openRawResource(iId);
			return KeyStoreHelper.loadKeyStore(oKeyStoreStream, p_sKeyStorePass);
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | CertificateException | IOException e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads a keystore from resources with its name
	 *  @param p_sKeyStoreFile the full name of the file (including extension) 
	 * @param p_sKeyStorePass the password protecting the file
	 * @return the decrypted keystore
	 */
	@Override
	public final KeyStore loadKeyStore(String  p_sKeyStoreFile, String p_sKeyStorePass) {
		try {
			return KeyStoreHelper.loadKeyStore(this.openKeyStoreStream(Application.getInstance()
					.getStringResource(p_sKeyStoreFile)), Application.getInstance()
					.getStringResource(p_sKeyStorePass));
		}
		catch (UnrecoverableKeyException | IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException e) {
			Log.e(TAG, e.getMessage());
		}
		
		return null;
	}

	/**
	 * creates an InputStream from the given keystore file name
	 * @param p_sKeyStoreFile the name of the keystore
	 * @return the inputstream for the file
	 */
	private InputStream openKeyStoreStream(String p_sKeyStoreFile) {
		try {
			return new FileInputStream(p_sKeyStoreFile);
		}
		catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		
		return null;
	}
}