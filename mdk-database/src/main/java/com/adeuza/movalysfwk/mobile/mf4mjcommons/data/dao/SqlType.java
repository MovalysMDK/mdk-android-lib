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

import java.sql.Types;

/**
 * <p>Encapsulation des types de java.sql.Types dans une enumeration</p>
 *
 *
 */
public enum SqlType {

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>INTEGER</code>.
	 */
	INTEGER(Types.INTEGER),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>FLOAT</code>.
	 */
	FLOAT(Types.FLOAT),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>REAL</code>.
	 */
	REAL(Types.FLOAT),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>DOUBLE</code>.
	 */
	DOUBLE(Types.DOUBLE),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>NUMERIC</code>.
	 */
	NUMERIC(Types.NUMERIC),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>DECIMAL</code>.
	 */
	DECIMAL(Types.DECIMAL),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>CHAR</code>.
	 */
	CHAR(Types.CHAR),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>VARCHAR</code>.
	 */
	VARCHAR(Types.VARCHAR),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>DATE</code>.
	 */
	DATE(Types.DATE),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>TIME</code>.
	 */
	TIME(Types.TIME),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>TIMESTAMP</code>.
	 */
	TIMESTAMP(Types.TIMESTAMP),

	/**
	 * <P>
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>BINARY</code>.
	 */
	BINARY(Types.BINARY),

	/**
	 * <P>
	 * The constant in the Java programming language that identifies the generic
	 * SQL value <code>NULL</code>.
	 */
	NULL(Types.NULL),

	/**
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>BLOB</code>.
	 * 
	 * @since 1.2
	 */
	BLOB(Types.BLOB),

	/**
	 * The constant in the Java programming language, sometimes referred to as a
	 * type code, that identifies the generic SQL type <code>CLOB</code>.
	 * 
	 * @since 1.2
	 */
	CLOB(Types.CLOB),

	/**
	 * The constant in the Java programming language, somtimes referred to as a
	 * type code, that identifies the generic SQL type <code>BOOLEAN</code>.
	 * 
	 * @since 1.4
	 */
	BOOLEAN(Types.BOOLEAN);

	/**
	 * Valeur de l'enumeration correspondante dans java.sql.Types 
	 */
	private int sqlType ;
	
	/**
	 * Constructeur avec la valeur correspondante de java.sql.Types
	 * @param p_iNum valeur correspondante de java.sql.Types
	 */
	private SqlType(int p_iNum) {
		sqlType = p_iNum;
	}

	/**
	 * Retourne la valeur correspondante de java.sql.Types
	 *
	 * @return la valeur correspondante de java.sql.Types
	 */
	public int intValue() {
		return this.sqlType ;
	}
}
