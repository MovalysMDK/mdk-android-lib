package com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.camera.CameraManager;
import com.google.zxing.ResultPoint;

/**
 * Interface to implement to replace the layer above the view on the camera for bar code scanner
 * @author spacreau
 */
public interface ViewfinderViewable {

	/**
	 * Modifier of camera manager attribute
	 * 
	 * @param p_oCameraManager new manager camera
	 */
	public void setCameraManager(CameraManager p_oCameraManager) ;
	/**
	 * Add a point decoded by the scanner
	 * 
	 * @param p_oPoint new point found by the scanner
	 */
	public void addPossibleResultPoint(ResultPoint p_oPoint) ;
	/**
	 * Redraw the zone view and cancel the old bitmap result
	 */
	public void drawViewfinder() ;
	/**
	 * Set the enabled state of this view.
	 */
	public void setVisibility(int p_iVisibility);
}
