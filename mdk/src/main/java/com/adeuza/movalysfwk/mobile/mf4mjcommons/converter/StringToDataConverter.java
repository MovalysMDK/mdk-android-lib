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

import java.sql.Timestamp;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.DurationUtils;

/**
 * <p>Allows to convert a String value to any another compatible type.</p>
 *
 *
 */
public class StringToDataConverter {
	
	/**
	 * <p>DoubleFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a {@link java.lang.Double} object.
	 */
	public static Double doubleObjectFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return null;
		}
		else {
			return Double.valueOf(p_sValue);
		}
	}
	
	/**
	 * <p>doubleFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a double.
	 */
	public static double doubleFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return -1.0d;
		}
		else {
			return (double)Double.valueOf(p_sValue);
		}
	}
	
	/**
	 * <p>intFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a int.
	 */
	public static int intFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return -1;
		}
		else {
			return (int)Integer.valueOf(p_sValue);
		}
	}
	
	/**
	 * <p>IntegerFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a int.
	 */
	public static Integer integerObjectFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return -1;
		}
		else {
			return Integer.valueOf(p_sValue);
		}
	}
	
	/**
	 * <p>longFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a long.
	 */
	public static long longFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return -1l;
		}
		else {
			return (long)Long.valueOf(p_sValue);
		}
	}
	
	/**
	 * <p>LongFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a {@link java.lang.Long} object.
	 */
	public static Long longObjectFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return null;
		}
		else {
			return Long.valueOf(p_sValue);
		}
	}
	
	/**
	 * <p>floatFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a float.
	 */
	public static float floatFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return -1.0f;
		}
		else {
			return (float)Float.valueOf(p_sValue);
		}
	}
	
	/**
	 * <p>FloatFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a {@link java.lang.Float} object.
	 */
	public static Float floatObjectFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return null;
		}
		else {
			return Float.valueOf(p_sValue);
		}
	}

	/**
	 * <p>TimestampFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a {@link java.sql.Timestamp} object.
	 */
	public static Timestamp timestampFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return null;
		}
		else {
			return DurationUtils.convertStringToTimestamp(p_sValue);
		}
	}

	/**
	 * <p>booleanFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean booleanFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return Boolean.FALSE;
		}
		else {
			return Boolean.parseBoolean(p_sValue);
		}
	}
	
	/**
	 * <p>shortFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a short.
	 */
	public static short shortFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return (short)-1;
		}
		else {
			return (short)Short.valueOf(p_sValue);
		}
	}

	/**
	 * <p>ShortFromStringValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a {@link java.lang.Short} object.
	 */
	public static Short shortObjectFromStringValue(String p_sValue) {
		if (p_sValue == null || p_sValue.isEmpty()) {
			return null;
		}
		else {
			return Short.valueOf(p_sValue);
		}
	}
}
