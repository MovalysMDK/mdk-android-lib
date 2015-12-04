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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.DateUtils;

/**
 * <p>Allows to convert a Timestamp value to any another compatible type.</p>
 *
 *
 */
public class TimestampToDataConverter {
	
	/**
	 * <p>LongFromTimestampValue.</p>
	 *
	 * @param p_oValue a java$sql$Timestamp object.
	 * @return a {@link java.lang.Long} object.
	 */
	public static Long longObjectFromTimestampValue(Timestamp p_oValue) {
		if (p_oValue == null) {
			return null;
		} else {
			return DateUtils.getTime(p_oValue);
		}
	}
}
