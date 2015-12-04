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
package com.adeuza.movalysfwk.mobile.mf4android.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.crypt.AESUtil;
import com.adeuza.movalysfwk.mf4jcommons.crypt.KeyStoreHelper;
import com.adeuza.movalysfwk.mobile.mf4android.utils.ResourceUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * This class is an helper for importing / exporting databases on the Android platform.
 * It should be used in a static way only.
 */
public final class DBImportExportHelper {

	/**
	 * private constructor
	 */
	private DBImportExportHelper() {
		// nothing to do
	}
	
	/**
	 * Export the current database to a "database" directory in the installation dir of the application.
	 * Should there be an error, it will be written to a file in the directory given in parameter.
	 * @param p_oCrashReportDir directory in which the report should be written
	 * @param p_oContext context to use
	 * @throws Exception if any
	 */
	public static void exportDatabase(File p_oCrashReportDir, Context p_oContext) throws Exception {

		String sDatabaseName = ResourceUtils.getStringResourceByName(
				FwkPropertyName.database_name.name(), p_oContext);

		String sCurrentDBPath = "/data/" + p_oContext.getPackageName()
				+ "/databases/" + sDatabaseName;
		File oCurrentDB = new File(Environment.getDataDirectory(),
				sCurrentDBPath);
		if (oCurrentDB.exists()) {
			File oBackupDB = new File(p_oCrashReportDir, sDatabaseName);
			AESUtil.encrypt(oCurrentDB, oBackupDB, loadKey(p_oContext));
		} else {
			Log.i(Application.LOG_TAG, "FileReport: no database to backup");
		}
	}
	
	/**
	 * Import database file into application
	 * @param p_oImportFile database file to import
	 * @param p_oContext context to use
	 */
	public static void importDatabase( File p_oImportFile, Context p_oContext ) {
		
		try {
			String sDatabaseName = Application.getInstance().getStringResource(FwkPropertyName.database_name);
			String sCurrentDBPath = "/data/" + p_oContext.getPackageName() + "/databases/" + sDatabaseName;
			File oCurrentDB = new File(Environment.getDataDirectory(), sCurrentDBPath);
			
			AESUtil.decrypt(p_oImportFile, oCurrentDB, loadKey(p_oContext));
		} catch( Exception oException ) {
			throw new MobileFwkException(oException);
		}
	}
	
	/**
	 * Loads an encryption key from the keystore
	 * @param p_oContext context to use
	 * @return the encryption key
	 * @throws NoSuchProviderException if any
	 * @throws IOException if any
	 * @throws KeyStoreException if any
	 * @throws CertificateException if any
	 * @throws NoSuchAlgorithmException if any
	 * @throws UnrecoverableKeyException if any
	 */
	public static byte[] loadKey( Context p_oContext ) throws UnrecoverableKeyException, KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException   {
		
		String sDbKeyStoreFile = ResourceUtils.getStringResourceByName("dbcrypt_keystorefile", p_oContext);
		String sDbKeyStorePass = ResourceUtils.getStringResourceByName("dbcrypt_keystorepass", p_oContext);
		String sDbKeyAlias = ResourceUtils.getStringResourceByName("dbcrypt_keyalias", p_oContext);
					
		int iRawId = p_oContext.getResources().getIdentifier(sDbKeyStoreFile,
				"raw", p_oContext.getPackageName());
		InputStream oIs = p_oContext.getResources().openRawResource(iRawId);
		
		return KeyStoreHelper.loadKeyForAndroidFromKeyStore(oIs, sDbKeyStorePass, sDbKeyAlias, null).getEncoded();
	}
}
