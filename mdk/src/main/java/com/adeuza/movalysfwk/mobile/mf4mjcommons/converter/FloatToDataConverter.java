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
 * <p>Allows to convert a float/Float value to any another compatible type.</p>
 *
 *
 */
public class FloatToDataConverter {
	
	/**
	 * <p>StringFromFloatValue.</p>
	 *
	 * @param p_fValue a {@link java.lang.Float} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromFloatValue(Float p_fValue) {
		if (p_fValue == null || p_fValue.isNaN()) {
			return null;
		} else {
			return String.valueOf((float)p_fValue);
		}
	}
	
	/**
	 * <p>StringFromFloatValue.</p>
	 *
	 * @param p_fValue a float.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromFloatValue(float p_fValue) {
		return String.valueOf(p_fValue);
	}
	
	/**
	 * <p>FloatFromFloatValue.</p>
	 *
	 * @param p_fValue a float.
	 * @return a {@link java.lang.Float} object.
	 */
	public static Float floatObjectFromFloatValue(float p_fValue) {
		return Float.valueOf(p_fValue);
	}
	
	/**
	 * <p>FloatFromFloatValue.</p>
	 *
	 * @param p_fValue a {@link java.lang.Float} object.
	 * @return a float.
	 */
	public static float floatFromFloatValue(Float p_fValue) {
		if (p_fValue == null || p_fValue.isNaN()) {
			return -1.0f;
		} else {
			return (float)p_fValue;
		}
	}
}
