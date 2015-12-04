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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing;

import java.util.List;

/**
 * <p>
 * 	Interface helping signature management for interventions.
 * 	In this class we find methods to convert a String list of points
 * 	to allow the sending of the signature during the synchronization.
 * </p>
 *
 *
 * @since Barcelone (8 déc. 2010)
 */
public interface SignatureHelper {


	/**
	 * <p>
	 * 	Transforms an array of list of points defining a signature in its representation as a string.
	 * </p>
	 *
	 * @param p_oPointsList
	 * 				The point list table to convert as a string.
	 * @return The string corresponding to the signature
	 */
	public String convertFromPointsToString(List<List<MMPoint>> p_oPointsList);
	
	
	/**
	 * <p>
	 * 	Transforms a string, in case it meets the definition of a signature, in a table list of points.
	 * 	If the parameter is not corresponding at a signature, it return null.
	 * </p>
	 *
	 * @param p_sSignature
	 * 				The string corresponding to the signature.
	 * @return Table list of points corresponding to the chain.
	 */
	public List<List<MMPoint>> convertFromStringToPoints(String p_sSignature);

}
