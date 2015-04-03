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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing;

import java.util.Collection;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.camera.CameraManager;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

/**
 * <p>Class </p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public final class CaptureActivityHandler extends Handler {

	private static final String TAG = CaptureActivityHandler.class.getSimpleName();

	private final CaptureActivity activity;
	private final DecodeThread decodeThread;
	private State state;
	private final CameraManager cameraManager;

	private enum State {
		PREVIEW,
		SUCCESS,
		DONE
	}
	/**
	 * Constructor to prepare the handler of the scanning
	 * @param p_oActivity component scanner
	 * @param p_oDecodeFormats decode format use to decode bitmap
	 * @param p_oBaseHints hint use to decode
	 * @param p_sCharacterSet 
	 * @param p_oCameraManager manager of camera
	 */
	public CaptureActivityHandler(CaptureActivity p_oActivity, Collection<BarcodeFormat> p_oDecodeFormats,
			Map<DecodeHintType,?> p_oBaseHints, String p_sCharacterSet, CameraManager p_oCameraManager) {
		this.activity = p_oActivity;
		this.decodeThread = new DecodeThread(p_oActivity, p_oDecodeFormats, p_oBaseHints, p_sCharacterSet,
				new ViewfinderResultPointCallback(p_oActivity.getViewfinderView()));
		this.decodeThread.start();
		this.state = State.SUCCESS;

		// Start ourselves capturing previews and decoding.
		this.cameraManager = p_oCameraManager;
		p_oCameraManager.startPreview();
		restartPreviewAndDecode();
	}
	/**
	 * Method to handle the message of the preview
	 * @param p_oMessage message sent by preview scanning
	 */
	@Override
	public void handleMessage(Message p_oMessage) {
		if (p_oMessage.what == R.id.restart_preview) {
			Log.d(TAG, "Got restart preview message");
			this.restartPreviewAndDecode();
		} else if (p_oMessage.what == R.id.decode_succeeded) {
			Log.d(TAG, "Got decode succeeded message");
			state = State.SUCCESS;
			Bundle bundle = p_oMessage.getData();
			Bitmap barcode = null;
			float scaleFactor = 1.0f;
			if (bundle != null) {
				byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
				if (compressedBitmap != null) {
					barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
					// Mutable copy:
					barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
				}
				scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);          
			}
			activity.handleDecode((Result) p_oMessage.obj, barcode, scaleFactor);
		} else if (p_oMessage.what == R.id.decode_failed) {
			// We're decoding as fast as possible, so when one decode fails, start another.
			state = State.PREVIEW;
			cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
		} else if (p_oMessage.what == R.id.return_scan_result) {
			Log.d(TAG, "Got return scan result message");
			//        activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			//        activity.finish();
		} else if (p_oMessage.what == R.id.launch_product_query) {
			Log.d(TAG, "Got product query message");
			String url = (String) p_oMessage.obj;

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.setData(Uri.parse(url));

			//        ResolveInfo resolveInfo =
			//            activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
			//        String browserPackageName = null;
			//        if (resolveInfo.activityInfo != null) {
			//          browserPackageName = resolveInfo.activityInfo.packageName;
			//          Log.d(TAG, "Using browser in package " + browserPackageName);
			//        }
			//
			//        // Needed for default Android browser / Chrome only apparently
			//        if ("com.android.browser".equals(browserPackageName) || "com.android.chrome".equals(browserPackageName)) {
			//          intent.setPackage(browserPackageName);
			//          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//          intent.putExtra(Browser.EXTRA_APPLICATION_ID, browserPackageName);
			//        }
			//
			//        try {
			//          activity.startActivity(intent);
			//        } catch (ActivityNotFoundException ignored) {
			//          Log.w(TAG, "Can't find anything to handle VIEW of URI " + url);
			//        }
		}
	}

	/**
	 * Stop to handling the decode process : stop the camera manager and remove message in the callback queue
	 */
	public void quitSynchronously() {
		this.state = State.DONE;
		this.cameraManager.stopPreview();
		Message oQuit = Message.obtain(decodeThread.getHandler(), R.id.quit);
		oQuit.sendToTarget();
		try {
			// Wait at most half a second; should be enough time, and onPause() will timeout quickly
			decodeThread.join(NumericConstants.MILLISEC_500);
		} catch (InterruptedException e) {
			// continue
	    	  Log.v(TAG, "[quitSynchronously] "+e);
		}

		// Be absolutely sure we don't send any queued up messages
		this.removeMessages(R.id.decode_succeeded);
		this.removeMessages(R.id.decode_failed);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
			activity.drawViewfinder();
		}
	}

}
