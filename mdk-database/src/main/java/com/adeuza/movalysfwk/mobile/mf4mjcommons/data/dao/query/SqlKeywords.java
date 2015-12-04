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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.StringDef;

/**
 * <p>Enumération des mots clés SQL</p>
 *
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({SqlKeywords.COUNT, SqlKeywords.NOT_IN, SqlKeywords.IN, SqlKeywords.FROM, SqlKeywords.ON, 
	SqlKeywords.LEFT_OUTER_JOIN, SqlKeywords.INNER_JOIN, SqlKeywords.DISTINCT, SqlKeywords.SELECT, 
	SqlKeywords.WHERE, SqlKeywords.HAVING, SqlKeywords.INSERT, SqlKeywords.UPDATE, SqlKeywords.INTO, 
	SqlKeywords.VALUES, SqlKeywords.SET, SqlKeywords.BETWEEN, SqlKeywords.LIKE, SqlKeywords.DELETE, 
	SqlKeywords.NULL,SqlKeywords.AS,SqlKeywords.UNION,SqlKeywords.UNION_ALL})
public @interface SqlKeywords {
	
	/**
	 * Mot clé Count
	 */
	public static final String COUNT = "COUNT";
	
	/**
	 * Mot clé NOT IN
	 */
	public static final String NOT_IN = "NOT IN";
	
	/**
	 * Mot clé IN
	 */
	public static final String IN = "IN";
	
	/**
	 * Mot clé FROM
	 */
	public static final String FROM = "FROM";
	
	/**
	 * Mot clé ON
	 */
	public static final String ON = "ON";
	
	/**
	 * Mot clé LEFT OUTER JOIN
	 */
	public static final String LEFT_OUTER_JOIN = "LEFT OUTER JOIN";
	
	/**
	 * Mot clé INNER JOIN
	 */
	public static final String INNER_JOIN = "INNER JOIN";
	
	/**
	 * Mot clé DISTINCT
	 */
	public static final String DISTINCT = "DISTINCT";
	
	/**
	 * Mot clé SELECT
	 */
	public static final String SELECT = "SELECT";
	
	/**
	 * Mot clé WHERE
	 */
	public static final String WHERE = "WHERE";
	
	/**
	 * Mot clé WHERE
	 */
	public static final String HAVING = "HAVING";
	
	/**
	 * Mot clé INSERT
	 */
	public static final String INSERT = "INSERT";
	
	/**
	 * Mot clé UPDATE
	 */
	public static final String UPDATE = "UPDATE";
	
	/**
	 * Mot clé INTO
	 */
	public static final String INTO = "INTO";
	
	/**
	 * Mot clé VALUES
	 */
	public static final String VALUES = "VALUES";
	
	/**
	 * Mot clé SET
	 */
	public static final String SET = "SET";
	
	/**
	 * Mot clé BETWEEN
	 */
	public static final String BETWEEN = "BETWEEN";
	
	/**
	 * Mot clé LIKE
	 */
	public static final String LIKE = "LIKE";
	
	/**
	 * Mot clé DELETE
	 */
	public static final String DELETE = "DELETE"; 
	
	/**
	 * Mot clé NULL
	 */
	public static final String NULL = "NULL";
	
	/**
	 * Mot clé AS
	 */
	public static final String AS = "AS";
	
	/**
	 * Mot clé UNION
	 */
	public static final String UNION = "UNION";
	
	/**
	 * Mot clé UNION ALL
	 */
	public static final String UNION_ALL = "UNION ALL";
}
