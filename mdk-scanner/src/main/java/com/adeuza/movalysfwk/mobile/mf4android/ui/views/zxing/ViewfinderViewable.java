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
