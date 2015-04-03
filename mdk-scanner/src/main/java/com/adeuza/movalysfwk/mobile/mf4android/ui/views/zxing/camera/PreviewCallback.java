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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
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