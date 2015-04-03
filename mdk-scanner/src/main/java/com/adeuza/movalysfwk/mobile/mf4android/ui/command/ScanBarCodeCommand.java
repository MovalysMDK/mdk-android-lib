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
package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMCustomDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.AlertMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.BarCodeResult;

/**
 * <p>
 * A utility class which helps ease integration with Barcode Scanner via {@link Intent}s. This is a simple way to invoke barcode scanning and receive
 * the result, without any need to integrate, modify, or learn the project's source code.
 * </p>
 * 
 * <h2>Initiating a barcode scan</h2>
 * 
 * <p>
 * Integration is essentially as easy as calling {@link #initiateScan(Activity)} and waiting for the result in your app.
 * </p>
 * 
 * <p>
 * It does require that the Barcode Scanner application is installed. The {@link #initiateScan(Activity)} method will prompt the user to download the
 * application, if needed.
 * </p>
 * 
 * <p>
 * There are a few steps to using this integration. First, your {@link Activity} must implement the method
 * {@link Activity#onActivityResult(int, int, Intent)} and include a line of code like this:
 * </p>
 * 
 * <p>
 * {@code public void onActivityResult(int resultCode, Intent intent) //before calling, verify that you received the good request_code IntentResult
 * scanResult = ScanBarCode.parseActivityResult(resultCode, intent); if (scanResult != null) { // handle scan result } // else continue with any other
 * code you need in the method ... } }
 * </p>
 * 
 * <p>
 * This is where you will handle a scan result. Second, just call this in response to a user action somewhere to begin the scan process:
 * </p>
 * 
 * <p>
 * {@code IntentIntegrator.initiateScan(yourActivity, yourrequest_code);}
 * </p>
 * 
 * 
 * <p>
 * Note that {@link #initiateScan(Activity)} returns an AlertDialo which is non-null if the user was prompted to download the application.
 * This lets the calling app potentially manage the dialog. In particular, ideally, the app dismisses the dialog if it's still active in its
 * {@link Activity#onPause()} method.
 * </p>
 * 
 * <h2>Sharing text via barcode</h2>
 * 
 * <p>
 * To share text, encoded as a QR Code on-screen, similarly, see {@link #shareText(Activity, CharSequence)}.
 * </p>
 * 
 * <p>
 * Some code, particularly download integration, was contributed from the Anobiit application.
 * </p>
 * this class come from http://zxing.googlecode.com/ and modified to be Movalys compliant
 */
public final class ScanBarCodeCommand {

	/** urls of the intent to encode URL */
	private static final String SCANNER_INTENT_ENCODE_URL = "com.google.zxing.client.android.ENCODE";
	/** urls of the intent to scan URL */
	private static final String SCANNER_INTENT_SCAN_URL = "com.google.zxing.client.android.SCAN";
	/** parameter names of the intent */
	private static final String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";
	/** scan result parameter for the intent */
	private static final String SCAN_RESULT = "SCAN_RESULT";
	/** scan formats parameter for the intent */
	private static final String SCAN_FORMATS = "SCAN_FORMATS";
	/** encode type parameter for the intent */
	private static final String ENCODE_TYPE = "ENCODE_TYPE";
	/** encode data parameter for the intent */
	private static final String ENCODE_DATA = "ENCODE_DATA";

	/** supported barcode formats */
	
	/** product code types */
	public static final String PRODUCT_CODE_TYPES = "UPC_A,UPC_E,EAN_8,EAN_13";
	/** one d code types */
	public static final String ONE_D_CODE_TYPES = PRODUCT_CODE_TYPES + ",CODE_39,CODE_93,CODE_128";
	/** qrcode types */
	public static final String QR_CODE_TYPES = "QR_CODE";
	/** other code types */
	public static final String ALL_CODE_TYPES = null;
	/** parameter value */
	private static final String TEXT_TYPE = "TEXT_TYPE";

	/** instance of handler for singleton patern */
	private static ScanBarCodeCommand instance = null;

	/** reference to the application */
	private AndroidApplication application = null;
	
	/**
	 * Returns the singleton instance of ScanBarCodeCommand
	 * @return the instance
	 */
	public static ScanBarCodeCommand getInstance() {
		if (ScanBarCodeCommand.instance == null) {
			ScanBarCodeCommand.instance = new ScanBarCodeCommand();
		}
		return ScanBarCodeCommand.instance;
	}
	
	/**
	 * Constructor for the class
	 */
	private ScanBarCodeCommand() {
		super();
		this.application = (AndroidApplication)(Application.getInstance());
	}

	/**
	 * Invokes scanning.
	 * @param p_oActivity the calling activity
	 * @param p_iRequestcode the request code to return
	 * @param p_sDesiredBarcodeFormats a comma separated list of codes you would like to scan for. ALL_CODE_TYPES is apply if ""
	 * @param p_oTargetComponent the component launching the command
	 * @return an AlertDialog if the user was prompted to download the app, null otherwise
	 */
	public MMCustomDialogFragment initiateScanCommand(Activity p_oActivity, int p_iRequestcode, CharSequence p_sDesiredBarcodeFormats, MMComplexeComponent p_oTargetComponent) {
		Intent oIntentScan = new Intent(SCANNER_INTENT_SCAN_URL);
		oIntentScan.addCategory(Intent.CATEGORY_DEFAULT);
		String sDesireBarCodeFormat=(String) p_sDesiredBarcodeFormats;
		
		// check which types of codes to scan for
		if ("".equals(p_sDesiredBarcodeFormats)) {
			sDesireBarCodeFormat = ScanBarCodeCommand.ALL_CODE_TYPES;
		}
		// set the desired barcode types
		oIntentScan.putExtra(SCAN_FORMATS, sDesireBarCodeFormat);

		try {
			if (p_oActivity instanceof AbstractMMActivity) {
				((AbstractMMActivity)p_oActivity).startActivityForResult(oIntentScan,p_oTargetComponent,p_iRequestcode);
			}
			return null;
		} catch (ActivityNotFoundException e) {
			return showDownloadDialog(p_oActivity);
		}
	}

	/**
	 * Shows the download dialog
	 * @param p_oActivity the calling activity
	 * @return the download dialog
	 */
	private MMCustomDialogFragment showDownloadDialog(final Activity p_oActivity) {
		final MMCustomDialogFragment.Builder oDownloadDialog = new MMCustomDialogFragment.Builder(p_oActivity);

		oDownloadDialog.setTitle(AndroidApplicationR.alert_scancodebar_install);
		oDownloadDialog.setMessage(AndroidApplicationR.alert_scancodebar_missing);
		oDownloadDialog.setPositiveButton(AndroidApplicationR.generic_message_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p_oDialogInterface, int p_iNumber) {
				try {
					Uri oUri = Uri.parse("market://search?q=pname:com.google.zxing.client.android");
					
					p_oActivity.startActivity(new Intent(Intent.ACTION_VIEW, oUri));
				} catch (ActivityNotFoundException e) {
					application.getController().doDisplayMessage(new AlertMessage(AndroidApplicationR.alert_market_noapp, AlertMessage.LONG));
				}
			}
		});
		oDownloadDialog.setNegativeButton(AndroidApplicationR.generic_message_no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p_oDialogInterface, int p_iNumber) {
				p_oDialogInterface.dismiss();
			}
		});

		final MMCustomDialogFragment oAlert = oDownloadDialog.create();
//		oAlert.show();
		return oAlert;
	}

	/**
	 * <p>
	 * Call this from your {@link Activity}'s {@link Activity#onActivityResult(int, int, Intent)} method.
	 * </p>
	 * @param p_iResultCode the activity call result code
	 * @param p_oIntent the result intent
	 * @return null if the event handled here was not related to {@link ScanBarCodeCommand}, or else an {@link IntentResult} containing the result of
	 *         the scan. If the user cancelled scanning, the fields will be null.
	 */
	public BarCodeResult parseActivityResult(int p_iResultCode, Intent p_oIntent) {
		if (p_iResultCode == Activity.RESULT_OK) {
			String sContents = p_oIntent.getStringExtra(SCAN_RESULT);
			String sFormatName = p_oIntent.getStringExtra(SCAN_RESULT_FORMAT);
			return new BarCodeResult(sContents, sFormatName);
		} else {
			return new BarCodeResult(null, null);
		}
	}

	/**
	 * Shares the given text by encoding it as a barcode, such that another user can scan the text off the screen of the device.
	 * @param p_oActivity the calling activity
	 * @param p_sText
	 *            the text string to encode as a barcode
	 */
	public void shareText(Activity p_oActivity, CharSequence p_sText) {

		Intent oIntent = new Intent();
		oIntent.setAction(SCANNER_INTENT_ENCODE_URL);
		oIntent.putExtra(ENCODE_TYPE, TEXT_TYPE);
		oIntent.putExtra(ENCODE_DATA, p_sText);
		try {
			p_oActivity.startActivity(oIntent);
		} catch (ActivityNotFoundException e) {
			showDownloadDialog(p_oActivity);
		}
	}

}
