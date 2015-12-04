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
package com.adeuza.movalysfwk.mobile.mf4android.ui.drawing;

import java.util.List;

import android.graphics.Path;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing.MMPoint;

/**
 * <p>
 * 	Class helping signature management for interventions on Android.
 * 	In this class we find methods to convert a String list of points 
 * 	to allow the sending of the signature during the synchronization.
 * </p>
 *
 *
 * @since Barcelone (9 déc. 2010)
 */
public interface AndroidSignatureHelper {

	/**
	 * <p>
	 * 	Transforms a String format signature in a Path object, usable by Android for re-draws it.
	 * </p>
	 * 
	 * @param p_sString
	 * 		The signature to transform.
	 * 
	 * @return A <em>Path</em> object corresponding to the signature.
	 */
	public Path convertFromStringToPath(String p_sString);
	
	/**
	 * <p>
	 * 	Transforms a Signature (lists of points) in a Path object, usable by Android for re-draws it.
	 * </p>
	 * 
	 * @param p_oListsPoints
	 *		The list of points that define a Signature.
	 * 
	 * @return A <em>Path</em> object corresponding to the signature.
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.helper.MMPoint
	 */
	public Path convertFromMMPointsToPath(List<List<MMPoint>> p_oListsPoints);
	
}
