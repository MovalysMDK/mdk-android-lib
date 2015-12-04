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
 * <p>Allows to convert a byte/Byte value to any another compatible type.</p>
 *
 *
 */
public class ByteToDataConverter {
	
	/**
	 * <p>StringFromByteValue.</p>
	 *
	 * @param p_bValue a {@link java.lang.Byte} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromByteValue(Byte p_bValue) {
		if (p_bValue == null) {
			return null;
		} else {
			return String.valueOf((byte)p_bValue);
		}
	}
	
	/**
	 * <p>StringFromByteValue.</p>
	 *
	 * @param p_bValue a byte.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromByteValue(byte p_bValue) {
		return String.valueOf(p_bValue);
	}
	
	/**
	 * <p>ByteFromByteValue.</p>
	 *
	 * @param p_bValue a byte.
	 * @return a {@link java.lang.Byte} object.
	 */
	public static Byte byteObjectFromByteValue(byte p_bValue) {
		return Byte.valueOf(p_bValue);
	}
	
	/**
	 * <p>ByteFromByteValue.</p>
	 *
	 * @param p_bValue a {@link java.lang.Byte} object.
	 * @return a byte.
	 */
	public static byte byteFromByteValue(Byte p_bValue) {
		if (p_bValue == null) {
			return (byte)0;
		} else {
			return (byte)p_bValue;
		}
	}
}
