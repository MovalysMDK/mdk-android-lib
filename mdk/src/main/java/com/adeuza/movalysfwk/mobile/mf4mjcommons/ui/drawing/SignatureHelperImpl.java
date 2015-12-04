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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>
 * 	Class helping signature management for interventions.
 * 	In this class we find methods to convert a String list of points
 * 	to allow the sending of the signature during the synchronization.
 * </p>
 *
 *
 * @since Barcelone (8 déc. 2010)
 */
public class SignatureHelperImpl implements SignatureHelper {

	/** regex used to split the string value of signature to get each trait. */
	protected static final String STRING_SIGNATURE_LINE_REGEX = "\\[";
	/** regex used to split the string value of signature to get each negative numbers. */
	protected static final String STRING_SIGNATURE_NEGATIVE_NUMBER_REGEX = "-";
	/** regex used to split the string value of signature to get the coordinate of each points. */
	protected static final String STRING_SIGNATURE_COORDINATE_REGEX = "\\D";
	/** magic string that represent a brace. */
	protected static final String STRING_SIGNATURE_MAGIC_BRACE = "{";
	/** magic string that represent a closing brace. */
	protected static final String STRING_SIGNATURE_MAGIC_CLOSING_BRACE = "}";
	/** magic string that represent an opening bracket. */
	protected static final String STRING_SIGNATURE_MAGIC_OPENING_BRACKET = "[";
	/** magic string that represent a closing bracket. */
	protected static final String STRING_SIGNATURE_MAGIC_CLOSING_BRACKET = "]";
	/** magic string that represent an opening parentesi. */
	protected static final String STRING_SIGNATURE_MAGIC_OPENING_PARENTESI = "(";
	/** magic string that represent an closing parentesi. */
	protected static final String STRING_SIGNATURE_MAGIC_CLOSING_PARENTESI = ")";
	/** magic string that represent a coma. */
	protected static final String STRING_SIGNATURE_MAGIC_COMMA = ",";
	/** magic string that represent a 0 */
	protected static final String STRING_SIGNATURE_MAGIC_ZERO_COORDINATE = "0";

	/** Constant <code>LINE_PATTERN</code> */
	protected static final Pattern LINE_PATTERN = Pattern.compile("\\[[^\\[\\]]+\\]");
	/** Constant <code>POINT_PATTERN</code> */
	protected static final Pattern POINT_PATTERN = Pattern.compile("\\(([-0-9]+),([-0-9]+)\\)");
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * 	Transforms an array of list of points as a string.
	 * </p>
	 */
	@Override
	public String convertFromPointsToString(List<List<MMPoint>> p_oPointsList) {
		StringBuilder r_sSignature = new StringBuilder();

		if (p_oPointsList != null && !p_oPointsList.isEmpty()) {
			r_sSignature.append(STRING_SIGNATURE_MAGIC_BRACE);

			for (List<MMPoint> oPoints : p_oPointsList){
				r_sSignature.append(STRING_SIGNATURE_MAGIC_OPENING_BRACKET);
				for (MMPoint oPoint : oPoints){
					r_sSignature.append(STRING_SIGNATURE_MAGIC_OPENING_PARENTESI);
					r_sSignature.append((int)oPoint.getX());
					r_sSignature.append(STRING_SIGNATURE_MAGIC_COMMA);
					r_sSignature.append((int)oPoint.getY());
					r_sSignature.append(STRING_SIGNATURE_MAGIC_CLOSING_PARENTESI);
				}
				r_sSignature.append(STRING_SIGNATURE_MAGIC_CLOSING_BRACKET);
			}

			r_sSignature.append(STRING_SIGNATURE_MAGIC_CLOSING_BRACE);
		}

		return r_sSignature.toString();
	}

	/** {@inheritDoc} */
	@Override
	public List<List<MMPoint>> convertFromStringToPoints(String p_sSignature) {

		List<List<MMPoint>> r_oResult = new ArrayList<List<MMPoint>>();
		if (p_sSignature != null) {
			final Matcher oLineMatcher = LINE_PATTERN.matcher(p_sSignature);
			Matcher oPointMatcher;
			List<MMPoint> oPoints;
			MMPoint oPoint;
			while (oLineMatcher.find()) {
				oPoints = new ArrayList<MMPoint>();

				oPointMatcher = POINT_PATTERN.matcher(oLineMatcher.group());
				while (oPointMatcher.find()) {
					try {
						oPoint = new MMPoint(Float.valueOf(oPointMatcher.group(1)),Float.valueOf(oPointMatcher.group(2)));
					}
					catch (NumberFormatException e) {
						Application.getInstance().getLog().error(this.getClass().getSimpleName(), "sCoord = "+oPointMatcher.group(1)+" :::: oCoordinates[i+1] = "+oPointMatcher.group(2));
						oPoint = new MMPoint(0,0);
					}
					oPoints.add(oPoint);
				}

				if (!oPoints.isEmpty()) {
					r_oResult.add(oPoints);
				}
			}
		}

		return r_oResult;
	}
}
