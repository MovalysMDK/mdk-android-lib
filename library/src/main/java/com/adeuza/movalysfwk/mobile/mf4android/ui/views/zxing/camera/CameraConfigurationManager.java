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

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
final class CameraConfigurationManager {

  private static final String TAG = "CameraConfiguration";

  // This is bigger than the size of a small screen, which is still supported. The routine
  // below will still select the default (presumably 320x240) size for these. This prevents
  // accidental selection of very low resolution on some devices.
  //private static final int MIN_PREVIEW_PIXELS = 470 * 320; // normal screen
  //private static final int MAX_PREVIEW_PIXELS = 1280 * 800;

  private final Context context;
  private Point screenResolution;
  private Point cameraResolution;
  /**
   * Construct the object in the new context
   * @param p_oContext context using
   */
  public CameraConfigurationManager(Context p_oContext) {
    this.context = p_oContext;
  }

	/**
	 * Compute the resolution of the component and camera according to the dimensions gave
	 * @param p_oCamera camera parameter
	 * @param p_iWidth available width
	 * @param p_iHeight available height
	 */
	public void initFromCameraParameters(final Camera p_oCamera, final int p_iWidth, final int p_iHeight) {
		this.screenResolution = new Point(p_iWidth, p_iHeight);
		Camera.Parameters parameters = p_oCamera.getParameters();
	    List<Size> sizes = parameters.getSupportedPreviewSizes();
	    Size optimalSize = getOptimalPreviewSize(sizes, p_iWidth, p_iHeight);
		this.cameraResolution = new Point(optimalSize.width, optimalSize.height);
		Log.v(TAG, "Camera resolution: '" + cameraResolution + "'.\nScreen resolution: '" + screenResolution+"'");
	}
	/**
	 * Defined the parameters of the camera according to the material 
	 * @param p_oCamera camera to parameterize
	 * @param p_oSafeMode minimum for using camera
	 */
  public void setDesiredCameraParameters(Camera p_oCamera, boolean p_oSafeMode) {
    Camera.Parameters parameters = p_oCamera.getParameters();

    if (parameters == null) {
      Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
      return;
    }
    if ( Log.isLoggable(TAG, Log.VERBOSE)){
    	Log.v(TAG, "Initial camera parameters: " + parameters.flatten());
    }
    if (p_oSafeMode) {
      Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
    }

    //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

    initializeTorch(parameters, p_oSafeMode);

    String focusMode = null;

    if (p_oSafeMode ) {
        focusMode = findSettableValue(parameters.getSupportedFocusModes(),
                                      Camera.Parameters.FOCUS_MODE_AUTO);
    } else {
        focusMode = findSettableValue(parameters.getSupportedFocusModes(),
                                      "continuous-picture", // Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE in 4.0+
                                      "continuous-video",   // Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO in 4.0+
                                      Camera.Parameters.FOCUS_MODE_AUTO);
    }
    
    // Maybe selected auto-focus but not available, so fall through here:
    if (!p_oSafeMode && focusMode == null) {
      focusMode = findSettableValue(parameters.getSupportedFocusModes(),
                                    Camera.Parameters.FOCUS_MODE_MACRO,
                                    "edof"); // Camera.Parameters.FOCUS_MODE_EDOF in 2.2+
    }
    if (focusMode != null) {
    	parameters.setFocusMode(focusMode);
    }

    // verify bar code mode exist before setting it
    String sceneMode = findSettableValue(parameters.getSupportedSceneModes(), Camera.Parameters.SCENE_MODE_BARCODE);
    if (sceneMode != null) {
    	parameters.setSceneMode(sceneMode);
    }else {
    	sceneMode = findSettableValue(parameters.getSupportedSceneModes(), Camera.Parameters.SCENE_MODE_AUTO);
    	if (sceneMode != null) {
        	parameters.setSceneMode(sceneMode);
        }
    }
    
    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
    Log.d("CameraConfigurationManager", "setPreviewSize cameraResolution.x " + cameraResolution.x + " cameraResolution.y "+ cameraResolution.y) ;
    p_oCamera.setParameters(parameters);
  }
  /**
   * Return the computed attribute camera resolution
   * @return the computed attribute camera resolution
   */
  public Point getCameraResolution() {
    return cameraResolution;
  }
  /**
   * Return the computed attribute screen resolution
   * @return the computed attribute screen resolution
   */
  public Point getScreenResolution() {
    return screenResolution;
  }

  /**
   * Verify the state of the camera according to the parameters
   * @param p_oCamera camera tested
   * @return true if the flash mode is on or torch 
   */
  public boolean getTorchState(Camera p_oCamera) {
    if (p_oCamera != null) {
      Camera.Parameters parameters = p_oCamera.getParameters();
      if (parameters != null) {
        String flashMode = p_oCamera.getParameters().getFlashMode();
        return flashMode != null && (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) || Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
      }
    }
    return false;
  }

  /**
   * Define the torch parameter for the camera
   * @param p_oCamera camera parameterized
   */
  public void setTorch(Camera p_oCamera) {
    Camera.Parameters parameters = p_oCamera.getParameters();
    this.doSetTorch(parameters, false);
    p_oCamera.setParameters(parameters);
  }

  private void initializeTorch(Camera.Parameters p_oParameters, boolean p_bSafeMode) {
    this.doSetTorch(p_oParameters, p_bSafeMode);
  }

  private void doSetTorch(Camera.Parameters p_oParameters, boolean p_bSafeMode) {
    String flashMode = findSettableValue(p_oParameters.getSupportedFlashModes(),
                                    Camera.Parameters.FLASH_MODE_AUTO);
    if ( flashMode == null ) {
        flashMode = findSettableValue(p_oParameters.getSupportedFlashModes(),
                Camera.Parameters.FLASH_MODE_TORCH,
                Camera.Parameters.FLASH_MODE_ON,
                Camera.Parameters.FLASH_MODE_OFF);
    }
    
    if (flashMode != null) {
      p_oParameters.setFlashMode(flashMode);
    }  
  }
  /*SPA
  private Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution) {

    List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
    if (rawSupportedSizes == null) {
      Log.w(TAG, "Device returned no supported preview sizes; using default");
      Camera.Size defaultSize = parameters.getPreviewSize();
      return new Point(defaultSize.width, defaultSize.height);
    }

    // Sort by size, descending
    List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
    Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
      @Override
      public int compare(Camera.Size a, Camera.Size b) {
        int aPixels = a.height * a.width;
        int bPixels = b.height * b.width;
        if (bPixels < aPixels) {
          return -1;
        }
        if (bPixels > aPixels) {
          return 1;
        }
        return 0;
      }
    });

    if (Log.isLoggable(TAG, Log.INFO)) {
      StringBuilder previewSizesString = new StringBuilder();
      for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
        previewSizesString.append(supportedPreviewSize.width).append('x')
            .append(supportedPreviewSize.height).append(' ');
      }
      Log.i(TAG, "Supported preview sizes: " + previewSizesString);
    }

    Point bestSize = null;
    float screenAspectRatio = (float) screenResolution.x / (float) screenResolution.y;

    float diff = Float.POSITIVE_INFINITY;
    for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
      int realWidth = supportedPreviewSize.width;
      int realHeight = supportedPreviewSize.height;
      int pixels = realWidth * realHeight;
      if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
        continue;
      }
      boolean isCandidatePortrait = realWidth < realHeight;
      int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
      int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
      if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
        Point exactPoint = new Point(realWidth, realHeight);
        Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
        return exactPoint;
      }
      float aspectRatio = (float) maybeFlippedWidth / (float) maybeFlippedHeight;
      float newDiff = Math.abs(aspectRatio - screenAspectRatio);
      if (newDiff < diff) {
        bestSize = new Point(realWidth, realHeight);
        diff = newDiff;
      }
    }

    if (bestSize == null) {
      Camera.Size defaultSize = parameters.getPreviewSize();
      bestSize = new Point(defaultSize.width, defaultSize.height);
      Log.i(TAG, "No suitable preview sizes, using default: " + bestSize);
    }

    Log.i(TAG, "Found best approximate preview size: " + bestSize);
    return bestSize;
  }
*/
  private static String findSettableValue(Collection<String> p_sSupportedValues,
                                          String... p_sDesiredValues) {
    Log.d(TAG, "Supported values: " + p_sSupportedValues);
    String result = null;
    if (p_sSupportedValues != null) {
      for (String desiredValue : p_sDesiredValues) {
        if (p_sSupportedValues.contains(desiredValue)) {
          result = desiredValue;
          break;
        }
      }
    }
    Log.d(TAG, "Settable value: " + result);
    return result;
  }
  /**
   * Getter of context
   * @return context of camera manager
   */
  public Context getContext() {
	return context;
  }
  
  private static final double ASPECT_TOLERANCE = 0.05;
  private Size getOptimalPreviewSize(List<Size> p_oSizes, int p_iWidth, int p_iHeight) {
		double targetRatio = (double) p_iWidth / p_iHeight;
		if (p_oSizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = p_iHeight;

		// Try to find an size match aspect ratio and size
		for (Size size : p_oSizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : p_oSizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
  	}
}
