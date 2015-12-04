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
 * <p>Allows to convert a short/Short value to any another compatible type.</p>
 *
 *
 */
public class ShortToDataConverter {
	
	/**
	 * <p>StringFromShortValue.</p>
	 *
	 * @param p_sValue a short.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromShortValue(short p_sValue) {
		return String.valueOf(p_sValue);
	}
	
	/**
	 * <p>StringFromShortValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.Short} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromShortValue(Short p_sValue) {
		if (p_sValue == null) {
			return null;
		} else {
			return p_sValue.toString();
		}
	}
	
	/**
	 * <p>shortFromShortValue.</p>
	 *
	 * @param p_sValue a {@link java.lang.Short} object.
	 * @return a short.
	 */
	public static short shortFromShortValue(Short p_sValue) {
		if (p_sValue == null) {
			return (short)-1;
		} else {
			return (short)p_sValue;
		}
	}

	/**
	 * <p>ShortFromShortValue.</p>
	 *
	 * @param p_sValue a short.
	 * @return a {@link java.lang.Short} object.
	 */
	public static Short shortObjectFromShortValue(short p_sValue) {
		return Short.valueOf(p_sValue);
	}
}
