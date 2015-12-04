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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans;

/**
 * <p>Interface implemented by all the enums generated
 * each of these enums is an integer value in database returned by getBaseId()</p>
 * <b> the framework use a FWK_NONE value represented by id 0 as a default value</b>
 */
public interface Enum {
	
	/** constant for value 0 */
	public static final int VALUE_0 = 0;
	
	/** constant for value 1 */
	public static final int VALUE_1 = 1;
	
	/** constant for value 2 */
	public static final int VALUE_2 = 2;
	
	/** constant for value 3 */
	public static final int VALUE_3 = 3;
	
	/** constant for value 4 */
	public static final int VALUE_4 = 4;
	
	/** constant for value 5 */
	public static final int VALUE_5 = 5;
	
	/** constant for value 6 */
	public static final int VALUE_6 = 6;
	
	/** constant for value 7 */
	public static final int VALUE_7 = 7;
	
	/** constant for value 8 */
	public static final int VALUE_8 = 8;
	
	/** constant for value 9 */
	public static final int VALUE_9 = 9;
	
	/** constant for value 10 */
	public static final int VALUE_10 = 10;
	
	/** constant for value 11 */
	public static final int VALUE_11 = 11;
	
	/** constant for value 12 */
	public static final int VALUE_12 = 12;
	
	/** constant for value 13 */
	public static final int VALUE_13 = 13;
	
	/** constant for value 14 */
	public static final int VALUE_14 = 14;
	
	/** constant for value 15 */
	public static final int VALUE_15 = 15;
	
	/** constant for value 16 */
	public static final int VALUE_16 = 16;
	
	/** constant for value 17 */
	public static final int VALUE_17 = 17;
	
	/** constant for value 18 */
	public static final int VALUE_18 = 18;
	
	/** constant for value 19 */
	public static final int VALUE_19 = 19;
	
	/** constant for value 20 */
	public static final int VALUE_20 = 20;
	
	/**
	 * Identifiant de l'item en base
	 *
	 * @return Identifiant en base
	 */
	public int getBaseId();
	
	
	/**
	 * <p>fromBaseId.</p>
	 *
	 * @param p_iBaseId a int.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum} object.
	 */
	public Enum fromBaseId(int p_iBaseId );
}
