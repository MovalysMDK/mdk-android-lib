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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.converter;

/**
 * <p>Allows to convert a double/Double value to any another compatible type.</p>
 *
 *
 */
public class DoubleToDataConverter {
	
	/**
	 * <p>StringFromDoubleValue.</p>
	 *
	 * @param p_dValue a {@link java.lang.Double} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromDoubleValue(Double p_dValue) {
		if (p_dValue == null || p_dValue.isNaN()) {
			return null;
		} else {
			return String.valueOf((double)p_dValue);
		}
	}
	
	/**
	 * <p>StringFromDoubleValue.</p>
	 *
	 * @param p_dValue a double.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromDoubleValue(double p_dValue) {
		return String.valueOf(p_dValue);
	}
	
	/**
	 * <p>DoubleFromDoubleValue.</p>
	 *
	 * @param p_dValue a double.
	 * @return a {@link java.lang.Double} object.
	 */
	public static Double doubleObjectFromDoubleValue(double p_dValue) {
		return Double.valueOf(p_dValue);
	}
}
