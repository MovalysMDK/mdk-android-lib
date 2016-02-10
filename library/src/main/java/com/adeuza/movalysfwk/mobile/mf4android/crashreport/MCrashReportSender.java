package com.adeuza.movalysfwk.mobile.mf4android.crashreport;

import static org.acra.ACRA.LOG_TAG;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

import com.adeuza.movalysfwk.mobile.mf4android.database.DBImportExportHelper;
import com.adeuza.movalysfwk.mobile.mf4android.utils.ResourceUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;

/**
 * @author lmichenaud
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
	 * @param p_oContext
	 */
	public MCrashReportSender(Context p_oContext) {
		this.context = p_oContext;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.acra.sender.ReportSender#send(org.acra.CrashReportData)
	 */
	@Override
	public void send(CrashReportData p_oCrashReportData)
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
	 * @param p_sReportContent
	 * @throws FileNotFoundException
	 * @throws IOException
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

					DBImportExportHelper.exportDatabase(oReportDir, this.context);
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
	 * @param p_sReportContent
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
	 * @param p_oCrashReportData
	 * @param p_oReportDir
	 * @throws FileNotFoundException
	 * @throws IOException
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
	 * @return
	 */
	private boolean isFileReportEnabled() {
		return	ResourceUtils.getStringResourceByName(
					FwkPropertyName.crashreport_file_enabled.name(),
					this.context).equalsIgnoreCase("true");
	}	
	/**
	 * @return
	 */
	private boolean isDBReportEnabled() {
		return ResourceUtils.getStringResourceByName(
					FwkPropertyName.crashreport_file_dbexport.name(),
					this.context).equalsIgnoreCase("true");
	}
	/**
	 * @param errorContent
	 * @return
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
