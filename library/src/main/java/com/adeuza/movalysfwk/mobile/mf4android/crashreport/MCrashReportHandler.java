package com.adeuza.movalysfwk.mobile.mf4android.crashreport;

import static org.acra.ACRA.LOG_TAG;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.Thread.UncaughtExceptionHandler;

import org.acra.ACRA;
import org.acra.ErrorReporter;

import android.content.Context;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.application.MFApplicationHolder;

/**
 * @author lmichenaud
 *
 */
public final class MCrashReportHandler {
	
	private MCrashReportHandler() {
		
	}
	/** Extension du nom de fichier généré */
	public static final String REPORTFILE_EXTENSION = ".stacktrace";
	/** Statut d'erreur en cas de sortie */

	/**
	 * Exception handler
	 */
	private static UncaughtExceptionHandler sUncaughtExceptionHandler = new UncaughtExceptionHandler() {
		/**
		 * 
		 * {@inheritDoc}
		 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
		 */
		@Override
		public void uncaughtException(Thread p_oThread, Throwable p_oThrowable) {

			Log.e(LOG_TAG, "UncaughtException handler", p_oThrowable );
			MFApplicationHolder.getInstance().getApplication().destroy();
		}
	};

	/**
	 * @param p_oContext
	 */
	public static void deleteOldReports( Context p_oContext ) {
		File oFileDir = p_oContext.getFilesDir();
        if (oFileDir != null) {
            // Filter for ".stacktrace" files
            FilenameFilter oFilter = new FilenameFilter() {
                @Override
				public boolean accept(File p_oDir, String p_oName) {
                    return p_oName.endsWith(REPORTFILE_EXTENSION);
                }
            };
            final File[] aFiles = oFileDir.listFiles(oFilter);
            boolean bDeleted ;
            for( File oFile: aFiles) {
            	bDeleted = oFile.delete();
                if (!bDeleted) {
                    Log.w(ACRA.LOG_TAG, "Could not deleted error report : " + oFile.getName());
                }
            }
        }
	}
	
	/**
	 * Initialize crash report handler
	 * @param p_oApplication
	 */
	public static void initialize(android.app.Application p_oApplication) {
		
		Log.i(LOG_TAG, "crash report handler initialization");
		
		deleteOldReports(p_oApplication);
		
		Thread.setDefaultUncaughtExceptionHandler(sUncaughtExceptionHandler);
		ACRA.init(p_oApplication);
		MCrashReportSender oMCrashReportSender = new MCrashReportSender(p_oApplication.getApplicationContext());
		ErrorReporter.getInstance().setReportSender(oMCrashReportSender);
	}
}
