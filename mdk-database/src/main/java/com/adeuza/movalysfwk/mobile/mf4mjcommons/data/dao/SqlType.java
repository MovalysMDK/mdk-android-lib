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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.IntDef;

/**
 * Sql types.
 *
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({SqlType.INTEGER, SqlType.FLOAT, SqlType.REAL, SqlType.DOUBLE, SqlType.CHAR, SqlType.VARCHAR, SqlType.DATE, SqlType.TIME, 
	SqlType.TIMESTAMP, SqlType.BINARY, SqlType.NULL, SqlType.BOOLEAN })
public @interface SqlType {
	
	/**
	 * Integer sql type.
	 */
	public static final int INTEGER = 4;
	
	/**
	 * Integer sql type.
	 */
	public static final int FLOAT = 6;
	
	/**
	 * Real sql type.
	 */
	public static final int REAL = 7;
	
	/**
	 * Double sql type.
	 */
	public static final int DOUBLE = 8;
	
	/**
	 * Char sql type.
	 */
	public static final int CHAR = 1;
	
	/**
	 * Varchar sql type.
	 */
	public static final int VARCHAR = 12;
	
	/**
	 * Date sql type.
	 */
	public static final int DATE = 91;
	
	/**
	 * Time sql type.
	 */
	public static final int TIME = 92;
	
	/**
	 * Timestamp sql type.
	 */
	public static final int TIMESTAMP = 93;
	
	/**
	 * Binary sql type.
	 */
	public static final int BINARY = -2;
	
	/**
	 * Null sql type.
	 */
	public static final int NULL = 0;
	
	/**
	 * Boolean sql type.
	 */
	public static final int BOOLEAN = 16;
}

