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
 * <p>Allows to convert a int/Integer value to any another compatible type.</p>
 *
 *
 */
public class IntegerToDataConverter {
	
	/**
	 * <p>StringFromIntegerValue.</p>
	 *
	 * @param p_iValue a {@link java.lang.Integer} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromIntegerValue(Integer p_iValue) {
		if (p_iValue == null) {
			return null;
		} else {
			return String.valueOf((int)p_iValue);
		}
	}
	
	/**
	 * <p>StringFormIntValue.</p>
	 *
	 * @param p_iValue a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromIntValue(int p_iValue) {
		return String.valueOf(p_iValue);
	}
	
	/**
	 * <p>DoubleFromIntegerValue.</p>
	 *
	 * @param p_iValue a {@link java.lang.Integer} object.
	 * @return a {@link java.lang.Double} object.
	 */
	public static Double doubleObjectFromIntegerValue(Integer p_iValue) {
		if (p_iValue == null) {
			return null;
		} else {
			return Double.valueOf((int)p_iValue);
		}
	}
	
	/**
	 * <p>DoubleFromIntValue.</p>
	 *
	 * @param p_iValue a int.
	 * @return a {@link java.lang.Double} object.
	 */
	public static Double doubleObjectFromIntValue(int p_iValue) {
		return Double.valueOf(p_iValue);
	}
	
	/**
	 * <p>doubleFromIntValue.</p>
	 *
	 * @param p_iValue a {@link java.lang.Integer} object.
	 * @return a double.
	 */
	public static double doubleFromIntValue(Integer p_iValue) {
		if (p_iValue == null) {
			return -1.0d;
		} else {
			return (double)((int)p_iValue);
		}
	}
	
	/**
	 * <p>doubleFromIntValue.</p>
	 *
	 * @param p_iValue a int.
	 * @return a double.
	 */
	public static double doubleFromIntValue(int p_iValue) {
		return (double)p_iValue;
	}
}
