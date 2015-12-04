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

import java.sql.Date;

/**
 * <p>Allows to convert a long/Long value to any another compatible type.</p>
 *
 *
 */
public class LongToDataConverter {
	
	/**
	 * <p>StringFromLongValue.</p>
	 *
	 * @param p_lValue a {@link java.lang.Long} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromLongValue(Long p_lValue) {
		if (p_lValue == null) {
			return null;
		} else {
			return String.valueOf((long)p_lValue);
		}
	}
	
	/**
	 * <p>StringFromLongValue.</p>
	 *
	 * @param p_lValue a long.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromLongValue(long p_lValue) {
		return String.valueOf(p_lValue);
	}
	
	/**
	 * <p>LongFromLongValue.</p>
	 *
	 * @param p_lValue a long.
	 * @return a {@link java.lang.Long} object.
	 */
	public static Long longObjectFromLongValue(long p_lValue) {
		return Long.valueOf(p_lValue);
	}
	
	/**
	 * <p>LongFromLongValue.</p>
	 *
	 * @param p_lValue a {@link java.lang.Long} object.
	 * @return a long.
	 */
	public static long longFromLongValue(Long p_lValue) {
		if (p_lValue == null) {
			return -1l;
		} else {
			return (long)p_lValue;
		}
	}
	
	/**
	 * Computes a Date from a long value.
	 * @param p_lValue the value to convert
	 * @return the converted Date
	 */
	public static Date dateFromLongValue(Long p_lValue) {
		return new Date(p_lValue);
	}
	
	/**
	 * Computes a long from a Date value.
	 * @param p_oDate the Date to convert
	 * @return the converted long
	 */
	public static Long longFromDateValue(Date p_oDate) {
		return p_oDate.getTime();
	}
}
