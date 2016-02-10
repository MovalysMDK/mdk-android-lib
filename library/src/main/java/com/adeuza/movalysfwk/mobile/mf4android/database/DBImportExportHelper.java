package com.adeuza.movalysfwk.mobile.mf4android.database;

import static org.acra.ACRA.LOG_TAG;

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

public final class DBImportExportHelper {

	private DBImportExportHelper() {
		
	}
	/**
	 * @param p_oCrashReportDir
	 * @throws IOException
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
			Log.i(LOG_TAG, "FileReport: no database to backup");
		}
	}
	
	/**
	 * Import database file into application
	 * @param p_oContext
	 * @throws IOException
	 */
	public static final void importDatabase( File p_oImportFile, Context p_oContext ) {
		
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
	 * @return
	 * @throws NoSuchProviderException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
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
