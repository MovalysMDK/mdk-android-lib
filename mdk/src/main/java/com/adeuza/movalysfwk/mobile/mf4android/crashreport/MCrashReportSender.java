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
package com.adeuza.movalysfwk.mobile.mf4android.crashreport;

import static org.acra.ACRA.LOG_TAG;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.crypto.Cipher;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.utils.ResourceUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;

/**
 *         <p>
 *         This class have the less number of dependencies to Movalys Framework
 *         as possible.
 *         </p>
 */
public class MCrashReportSender implements ReportSender {

	/**
	 * Mail mimetype
	 */
	private static final String MAIL_MIMETYPE = "text/plain";

	/**
	 * 
	 */
	private static final String DATETIME_FORMAT = "yyyyMMdd-HH'h'mm'm'ss";

	/**
	 * Android Context
	 */
	private final Context context;

	/**
	 * Crash report constructor
	 * @param p_oContext the android context
	 */
	public MCrashReportSender(Context p_oContext) {
		this.context = p_oContext;
	}

	/**
	 * (non-Javadoc)
	 * @param p_oCrashReportData the crash report
	 * @see org.acra.sender.ReportSender#send(org.acra.CrashReportData)
	 * @throws ReportSenderException exeption on report
	 */
	@Override
	public void send(Context p_oContext, CrashReportData p_oCrashReportData)
			throws ReportSenderException {

		String sReportContent = this.buildReportContent(p_oCrashReportData);

		Log.i(LOG_TAG,
				"crashreport_file_enabled: "
						+ this.isFileReportEnabled());
		
		Log.i(LOG_TAG,
				"crashreport_file_dbexport: "
						+ this.isDBReportEnabled());

		Log.i(LOG_TAG,
				"crashreport_mail_enabled: "
						+ ResourceUtils.getStringResourceByName(
								FwkPropertyName.crashreport_mail_enabled.name(),
								this.context));

		if (this.isFileReportEnabled()
				|| this.isDBReportEnabled()) {
			this.doSDReports(sReportContent);
		}

		if (ResourceUtils.getStringResourceByName(
				FwkPropertyName.crashreport_mail_enabled.name(), this.context)
				.equalsIgnoreCase("true")) {
			this.doMailReport(sReportContent);
		}
	}

	/**
	 * Write report on SD card
	 * @param p_sReportContent the report to write
	 */
	private void doSDReports(String p_sReportContent) {
		try {
						
			String sState = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(sState)) {

				SimpleDateFormat oSdf = new SimpleDateFormat(DATETIME_FORMAT,
						Locale.US);
				String sDate = oSdf.format(System.currentTimeMillis());

				File oReportDir = this.context.getExternalFilesDir("crashreports/"
						+ sDate);
				Log.i(LOG_TAG, oReportDir.getAbsolutePath());

				Log.i(LOG_TAG,
						"keystore default type: " + KeyStore.getDefaultType());
				
				Log.i(LOG_TAG, "aes provider: "
						+ Cipher.getInstance("AES").getProvider().getName());

				if (this.isFileReportEnabled()) {
					this.writeCrashReportToFile(p_sReportContent, oReportDir);
				}

				if (this.isDBReportEnabled()) {

					Class<?> oDbImportHelper = Class.forName("com.adeuza.movalysfwk.mobile.mf4android.database.DBImportExportHelper");
					Method oExport = oDbImportHelper.getMethod("exportDatabase", File.class, Context.class);
					oExport.invoke(null, oReportDir, this.context);
					Log.i(LOG_TAG, "database successfully exported");
				}
				
			} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(sState)) {
				Log.e(LOG_TAG,
						"fileReport: can't save report because sdcard is mounted read-only");
			} else {
				Log.e(LOG_TAG,
						"fileReport: can't save report because no sdcard is mounted");
			}
		} catch (Exception oException) {
			Log.e(LOG_TAG, "fileReport: generation failed", oException);
		}
	}

	/**
	 * Send report by mail
	 * @param p_sReportContent the report to send
	 */
	private void doMailReport(String p_sReportContent) {
		try {
			Intent oEmailIntent = new Intent(android.content.Intent.ACTION_SEND);
			oEmailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			oEmailIntent.setType(MAIL_MIMETYPE);
			oEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,this.context.getPackageName() + " Crash Report");
			oEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT,p_sReportContent);
			oEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { ResourceUtils.getStringResourceByName(
							FwkPropertyName.crashreport_mail_sender.name(),
							this.context) });
			this.context.startActivity(oEmailIntent);
		} catch (Exception oException) {
			Log.e(LOG_TAG, "mailReport: generation failed", oException);
		}
	}

	/**
	 * Write the file
	 * @param p_sReportContent the String representing the crash
	 * @param p_oReportDir the directory to put the file
	 * @throws IOException if IOExeption on write report
	 */
	private void writeCrashReportToFile(String p_sReportContent, File p_oReportDir)	throws IOException {
		FileOutputStream oOutputStream = new FileOutputStream(new File(p_oReportDir, "crashreport.txt"));
		try {
			OutputStreamWriter oWriter = new OutputStreamWriter(oOutputStream);
			try {
				// Write the string to the file
				oWriter.write(p_sReportContent);
				oWriter.flush();
			} finally {
				oWriter.close();
			}
		} finally {
			oOutputStream.close();
		}
	}
	/**
	 * Is the file report enable
	 * @return true if the file report is enable, false otherwise
	 */
	private boolean isFileReportEnabled() {
		return	ResourceUtils.getStringResourceByName(
					FwkPropertyName.crashreport_file_enabled.name(),
					this.context).equalsIgnoreCase("true");
	}	
	/**
	 * Is the database in the report
	 * @return true if the database is exported with the crash report, false otherwise
	 */
	private boolean isDBReportEnabled() {
		return ResourceUtils.getStringResourceByName(
					FwkPropertyName.crashreport_file_dbexport.name(),
					this.context).equalsIgnoreCase("true");
	}
	/**
	 * Build the report content
	 * @param p_oErrorContent the crash report object
	 * @return a string corresponding to the error logged
	 */
	private String buildReportContent(CrashReportData p_oErrorContent) {

		ReportField[] sFields = ACRA.getConfig().customReportContent();
		if (sFields.length == 0) {
			sFields = ACRAConstants.DEFAULT_REPORT_FIELDS;
		}

		final StringBuilder builder = new StringBuilder();
		for (ReportField oField : sFields) {
			builder.append(oField.toString()).append('=');
			builder.append(p_oErrorContent.get(oField));
			builder.append('\n');
		}
		return builder.toString();
	}
}
