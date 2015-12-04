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
 * <p>Allows to convert a char/Character value to any another compatible type.</p>
 *
 *
 */
public class CharacterToDataConverter {

	/**
	 * <p>StringFromCharValue.</p>
	 *
	 * @param p_iValue a char.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromCharValue(char p_iValue) {
		if (p_iValue == '\u0000') {
			return null;
		} else {
			return String.valueOf(p_iValue);
		}
	}

	/**
	 * <p>StringFromCharacterValue.</p>
	 *
	 * @param p_iValue a {@link java.lang.Character} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String stringFromCharacterValue(Character p_iValue) {
		if (p_iValue == null)
			return null;
		else {
			return Character.toString((char)p_iValue);
		}
	}

	/**
	 * <p>CharacterFromCharValue.</p>
	 *
	 * @param p_iValue a char.
	 * @return a {@link java.lang.Character} object.
	 */
	public static Character characterObjectFromCharValue(char p_iValue) {
		if (p_iValue == '\u0000') {
			return null;
		} else {
			return Character.valueOf(p_iValue);
		}
	}

	/**
	 * <p>CharFromCharacterValue.</p>
	 *
	 * @param p_iValue a {@link java.lang.Character} object.
	 * @return a char.
	 */
	public static char charFromCharacterValue(Character p_iValue) {
		if (p_iValue == null)
			return '\u0000';
		else {
			return (char)p_iValue;
		}
	}
}
