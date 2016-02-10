/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.camera;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
/**
 * Class who react to the change of camera preview
 * and give the data to the decoding handler by a messaging process.
 */
final class PreviewCallback implements Camera.PreviewCallback {

	private static final String TAG = PreviewCallback.class.getSimpleName();

	private final CameraConfigurationManager configManager;
	private Handler previewHandler;
	private int previewMessage;
	/**
	 * Constructor of the callback when the preview is modified
	 * @param p_oConfigManager config manager of the camera 
	 */
	public PreviewCallback(CameraConfigurationManager p_oConfigManager) {
		this.configManager = p_oConfigManager;
	}
	/**
	 * Define the handler who will react to the message
	 * @param p_oPreviewHandler handler of the preview action
	 * @param p_oPreviewMessage message gave to the handler
	 */
	public void setHandler(Handler p_oPreviewHandler, int p_oPreviewMessage) {
		this.previewHandler = p_oPreviewHandler;
		this.previewMessage = p_oPreviewMessage;
	}
	/**
	 * {@inheritDoc}
	 * Rotate the data in portrait orientation
	 * @see Camera.PreviewCallback#onPreviewFrame(byte[], Camera)
	 */
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Point cameraResolution = configManager.getCameraResolution();
		Handler thePreviewHandler = previewHandler;
		if (data != null && cameraResolution != null && thePreviewHandler != null) {

			// [ORIENTATION]
			WindowManager manager = (WindowManager) configManager.getContext().getSystemService(Context.WINDOW_SERVICE);
			int rotation = manager.getDefaultDisplay().getRotation();
			int heightFrame = 0;
			int widthFrame =0 ;
			switch (rotation) {
				case Surface.ROTATION_0:
				case Surface.ROTATION_180:
					rotateDataCounterClockwise90(data, cameraResolution.x, cameraResolution.y);
					widthFrame = cameraResolution.y ;
					heightFrame = cameraResolution.x ;
					break;
				case Surface.ROTATION_90:
				case Surface.ROTATION_270:
					widthFrame = cameraResolution.x ;
					heightFrame = cameraResolution.y ;
					break;
				default:
					throw new MobileFwkException("onPreviewFrame", "unknown rotation degree");
			}
			
			Message message = thePreviewHandler.obtainMessage(previewMessage, widthFrame ,heightFrame, data);
			message.sendToTarget();
			previewHandler = null;
		} else {
			Log.d(TAG, "Got preview callback, but no handler or resolution available");
		}
		data = null ;
	}
	/**
	 * Rotate the data for 1D bar code
	 * @param p_bSource data source modified 
	 * @param p_iWidth count of data to rotate in width
	 * @param p_iHeight count of data to rotate in height
	 */
	private static void rotateDataCounterClockwise90(final byte[] p_bSource, final int p_iWidth, final int p_iHeight) {
		final byte[] target = new byte[p_bSource.length];

		for (int y = 0; y < p_iHeight; y++) {
			for (int x = 0; x < p_iWidth; x++) {
				target[(x + 1) * p_iHeight - (y + 1)] = p_bSource[x + y * p_iWidth];
			}
		}
		System.arraycopy(target, 0, p_bSource, 0, p_bSource.length);
	}
}