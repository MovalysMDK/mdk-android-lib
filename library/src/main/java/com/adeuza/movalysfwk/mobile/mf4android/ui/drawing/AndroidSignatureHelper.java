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
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author fbourlieux
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
