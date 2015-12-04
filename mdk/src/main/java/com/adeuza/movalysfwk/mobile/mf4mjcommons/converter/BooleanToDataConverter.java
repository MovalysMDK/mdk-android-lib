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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.BooleanUtils;

/**
 * <p>Allows to convert a boolean/Boolean value to any another compatible type.</p>
 */
public class BooleanToDataConverter {
	
	/**
	 * <p>StringFromBooleanValue.</p>
	 *
	 * @param p_bValue a boolean.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromBooleanValue(boolean p_bValue) {
		return String.valueOf(p_bValue);
	}
	
	/**
	 * <p>StringFromBooleanValue.</p>
	 *
	 * @param p_bValue a {@link java.lang.Boolean} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromBooleanValue(Boolean p_bValue) {
		return String.valueOf(booleanFromBooleanValue(p_bValue));
	}
	
	/**
	 * <p>booleanFromBooleanValue.</p>
	 *
	 * @param p_bValue a {@link java.lang.Boolean} object.
	 * @return a boolean.
	 */
	public static boolean booleanFromBooleanValue(Boolean p_bValue) {
		return BooleanUtils.toBoolean(p_bValue);
	}
}
