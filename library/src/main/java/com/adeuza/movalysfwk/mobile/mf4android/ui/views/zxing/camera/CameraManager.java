/*
 * Copyright (C) 2008 ZXing authors
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

import java.io.IOException;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.client.android.camera.open.OpenCameraManager;

/**
 * This object wraps the Camera service object and expects to be the only one talking to it. The implementation encapsulates the steps
 * needed to take preview-sized images, which are used for both preview and decoding.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CameraManager {

	private static final String TAG = CameraManager.class.getSimpleName();

	private static final int MIN_FRAME_WIDTH = 240;
	private static final int MIN_FRAME_HEIGHT = 240;
	private static final int MAX_FRAME_WIDTH = 960; // = 1920/2
	private static final int MAX_FRAME_HEIGHT = 540; // = 1080/2

	private final Context context;
	private final CameraConfigurationManager configManager;
	private Camera camera;
	private AutoFocusManager autoFocusManager;
	private Rect framingRect;
	private Rect framingRectInPreview;
	private boolean initialized;
	private boolean previewing;
	private int requestedFramingRectWidth;
	private int requestedFramingRectHeight;
	/**
	 * Preview frames are delivered here, which we pass on to the registered handler. Make sure to clear the handler so it will only receive
	 * one message.
	 */
	private final PreviewCallback previewCallback;

	/**
	 * Construct object and create callback with the camera preview, and configuration manager
	 * 
	 * @param p_oContext context usage
	 */
	public CameraManager(Context p_oContext) {
		this.context = p_oContext;
		this.configManager = new CameraConfigurationManager(p_oContext);
		this.previewCallback = new PreviewCallback(configManager);
	}

	/**
	 * Opens the camera driver and initializes the hardware parameters.
	 * 
	 * @param p_oHolder The surface object which the camera will draw preview frames into.
	 * @throws IOException Indicates the camera driver failed to open.
	 */
	public synchronized void openDriver(SurfaceHolder p_oHolder) throws IOException {
		Camera theCamera = camera;
		if (theCamera == null) {
			theCamera = new OpenCameraManager().build().open();
			if (theCamera == null) {
				throw new IOException();
			}
			camera = theCamera;
		}
		theCamera.setPreviewDisplay(p_oHolder);
		// [ROTATION]
		this.defineCameraOrientation();

		if (!initialized) {
			initialized = true;
			final Rect oSize = p_oHolder.getSurfaceFrame();
			// define the size of the view
			configManager.initFromCameraParameters(theCamera, Math.abs(oSize.right - oSize.left), Math.abs(oSize.bottom - oSize.top));
			if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
				setManualFramingRect(requestedFramingRectWidth, requestedFramingRectHeight);
				requestedFramingRectWidth = 0;
				requestedFramingRectHeight = 0;
			}
		}

		Camera.Parameters parameters = theCamera.getParameters();
		String parametersFlattened = null;
		if(parameters != null){
			parametersFlattened = parameters.flatten(); // Save these, temporarily
		}
		try {
			configManager.setDesiredCameraParameters(theCamera, false);
		} catch (RuntimeException re) {
			// Driver failed
			Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
			Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
			// Reset:
			if (parametersFlattened != null) {
				parameters = theCamera.getParameters();
				parameters.unflatten(parametersFlattened);
				try {
					theCamera.setParameters(parameters);
					configManager.setDesiredCameraParameters(theCamera, true);
				} catch (RuntimeException re2) {
					// Well, darn. Give up
					Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
				}
			}
		}
	}
	/**
	 * Define the good orientation according to the display and camera 
	 */
	private void defineCameraOrientation() {
		if (camera != null) {
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(0, info);
			WindowManager manager = (WindowManager) configManager.getContext().getSystemService(Context.WINDOW_SERVICE);

			int rotation = manager.getDefaultDisplay().getRotation();
			int degrees = 0;
			switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = NumericConstants.DEGREES_QUARTER_ROTATION;
				break;
			case Surface.ROTATION_180:
				degrees = NumericConstants.DEGREES_HALF_ROTATION;
				break;
			case Surface.ROTATION_270:
				degrees = NumericConstants.DEGREES_270;
				break;
			default:
				throw new MobileFwkException("defineCameraOrientation", "unknown rotation degree");
			}

			int result;
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				result = (info.orientation + degrees) % NumericConstants.DEGREES_FULL_ROTATION;
				result = (NumericConstants.DEGREES_FULL_ROTATION - result) % NumericConstants.DEGREES_FULL_ROTATION; // compensate the mirror
			} else { // back-facing
				result = (info.orientation - degrees + NumericConstants.DEGREES_FULL_ROTATION) % NumericConstants.DEGREES_FULL_ROTATION;
			}
			camera.setDisplayOrientation(result);
			camera.getParameters().set("rotation", degrees);
		}
	}
	/**
	 * Verify the existence of the camera
	 * @return true if the camera exist
	 */
	public synchronized boolean isOpen() {
		return camera != null;
	}

	/**
	 * Closes the camera driver if still in use and nullify the camera
	 */
	public synchronized void closeDriver() {
		if (camera != null) {
			camera.release();
			camera = null;
			// Make sure to clear these each time we close the camera, so that any scanning rect
			// requested by intent is forgotten.
			framingRect = null;
			framingRectInPreview = null;
		}
	}

	/**
	 * Asks the camera hardware to begin drawing preview frames to the screen.
	 */
	public synchronized void startPreview() {
		Camera theCamera = camera;
		if (theCamera != null && !previewing) {
			theCamera.startPreview();
			previewing = true;
			autoFocusManager = new AutoFocusManager(context, camera);
		}
	}

	/**
	 * Tells the camera to stop drawing preview frames.
	 */
	public synchronized void stopPreview() {
		if (autoFocusManager != null) {
			autoFocusManager.stop();
			autoFocusManager = null;
		}
		if (camera != null && previewing) {
			camera.stopPreview();
			previewCallback.setHandler(null, 0);
			previewing = false;
		}
	}

	/**
	 * Convenience method for {@link com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.CaptureActivity}
	 */
	public synchronized void setTorch(boolean p_bNewSetting) {
		if (p_bNewSetting != configManager.getTorchState(camera)) {
			if (camera != null) {
				if (autoFocusManager != null) {
					autoFocusManager.stop();
				}
				configManager.setTorch(camera);
				if (autoFocusManager != null) {
					autoFocusManager.start();
				}
			}
		}
	}

	/**
	 * A single preview frame will be returned to the handler supplied. The data will arrive as byte[] in the message.obj field, with width
	 * and height encoded as message.arg1 and message.arg2, respectively.
	 * 
	 * @param p_oHandler The handler to send the message to.
	 * @param p_iMessage The what field of the message to be sent.
	 */
	public synchronized void requestPreviewFrame(Handler p_oHandler, int p_iMessage) {
		Camera theCamera = camera;
		if (theCamera != null && previewing) {
			previewCallback.setHandler(p_oHandler, p_iMessage);
			theCamera.setOneShotPreviewCallback(previewCallback);
		}
	}

	/**
	 * Calculates the framing rect which the UI should draw to show the user where to place the barcode. This target helps with alignment as
	 * well as forces the user to hold the device far enough away to ensure the image will be in focus.
	 * 
	 * @return The rectangle to draw on screen in window coordinates.
	 */
	public synchronized Rect getFramingRect() {
		if (framingRect == null) {
			Point screenResolution = configManager.getScreenResolution();
			if (camera == null || screenResolution == null) {
				return null;
			}

			// [ORIENTATION]
			int width = screenResolution.x /* 3 / 4 */;
			if (width < MIN_FRAME_WIDTH) {
				width = MIN_FRAME_WIDTH;
			} else if (width > MAX_FRAME_WIDTH) {
				width = MAX_FRAME_WIDTH;
			}
			int height = screenResolution.y / NumericConstants.NO_3;
			if (height < MIN_FRAME_HEIGHT) {
				height = MIN_FRAME_HEIGHT;
			} else if (height > MAX_FRAME_HEIGHT) {
				height = MAX_FRAME_HEIGHT;
			}
			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
			Log.d(TAG, "Calculated framing rect: " + framingRect);
		}
		return framingRect;
	}

	/**
	 * Like {@link #getFramingRect} but coordinates are in terms of the preview frame, not UI / screen.
	 */
	public synchronized Rect getFramingRectInPreview() {
		if (framingRectInPreview == null) {
			Rect oFramingRect = getFramingRect();
			Point cameraResolution = configManager.getCameraResolution();
			Point screenResolution = configManager.getScreenResolution();
			if (oFramingRect == null || cameraResolution == null || screenResolution == null) {
				return null;
			}
			Rect rect = new Rect(oFramingRect);
			// [ORIENTATION]
			WindowManager manager = (WindowManager) configManager.getContext().getSystemService(Context.WINDOW_SERVICE);
			int rotation = manager.getDefaultDisplay().getRotation();
			switch (rotation) {
			case Surface.ROTATION_0:
			case Surface.ROTATION_180:
				rect.left = rect.left * cameraResolution.y / screenResolution.x;
				rect.right = rect.right * cameraResolution.y / screenResolution.x;
				rect.top = rect.top * cameraResolution.x / screenResolution.y;
				rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
				break;
			case Surface.ROTATION_90:
			case Surface.ROTATION_270:
				rect.left = rect.left * cameraResolution.x / screenResolution.x;
				rect.right = rect.right * cameraResolution.x / screenResolution.x;
				rect.top = rect.top * cameraResolution.y / screenResolution.y;
				rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
				break;
			default:
				throw new MobileFwkException("getFramingRectInPreview", "unknown rotation degree");
			}

			framingRectInPreview = rect;
		}
		return framingRectInPreview;
	}

	/**
	 * Allows third party apps to specify the scanning rectangle dimensions, rather than determine them automatically based on screen
	 * resolution.
	 * 
	 * @param p_iWidth The width in pixels to scan.
	 * @param p_iHeight The height in pixels to scan.
	 */
	public synchronized void setManualFramingRect(int p_iWidth, int p_iHeight) {
		if (initialized) {
			Point screenResolution = configManager.getScreenResolution();
			if (p_iWidth > screenResolution.x) {
				p_iWidth = screenResolution.x;
			}
			if (p_iHeight > screenResolution.y) {
				p_iHeight = screenResolution.y;
			}
			int leftOffset = (screenResolution.x - p_iWidth) / 2;
			int topOffset = (screenResolution.y - p_iHeight) / 2;
			framingRect = new Rect(leftOffset, topOffset, leftOffset + p_iWidth, topOffset + p_iHeight);
			Log.d(TAG, "Calculated manual framing rect: " + framingRect);
			framingRectInPreview = null;
		} else {
			requestedFramingRectWidth = p_iWidth;
			requestedFramingRectHeight = p_iHeight;
		}
	}

	/**
	 * A factory method to build the appropriate LuminanceSource object based on the format of the preview buffers, as described by
	 * Camera.Parameters.
	 * 
	 * @param p_oData A preview frame.
	 * @param p_iWidth The width of the image.
	 * @param p_iHeight The height of the image.
	 * @return A PlanarYUVLuminanceSource instance.
	 */
	public PlanarYUVLuminanceSource buildLuminanceSource(byte[] p_oData, int p_iWidth, int p_iHeight) {
		Rect rect = getFramingRectInPreview();
		if (rect == null) {
			return null;
		}
		if( Log.isLoggable(TAG,Log.DEBUG ) ) {
			Log.d(TAG, "buildLuminanceSource > width:'" + p_iWidth +"'; height:'" + p_iHeight + "'; rect.left:'" + rect.left + "';  rect.top:'" +rect.top + "' rect.width():'" + rect.width() + "'; rect.height():'" +  rect.height() +"'.");
		}
		// Go ahead and assume it's YUV rather than die.
		return new PlanarYUVLuminanceSource(p_oData, p_iWidth, p_iHeight, rect.left, rect.top, rect.width(), rect.height(), false);
	}

	/**
	 * Accessor of the camera
	 * 
	 * @return the camera managed
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Accessor of the camera config manager
	 * 
	 * @return the camera config manager
	 */
	public CameraConfigurationManager getConfigManager() {
		return configManager;
	}
}
