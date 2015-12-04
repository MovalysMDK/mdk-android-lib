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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * Defines an attribute to apply as a collate to an order by on a field
 *
 * @since 3.1.1
 */
public class Collate {
	
	/** Unicode collate */
	public static final Collate UNICODE = new Collate();
	
	/** value to apply to the collate */
	private String value = "UNICODE";	

	/**
	 * Default constructor
	 */
	public Collate() {
		// default constructor
	}
	
	/**
	 * Constructor with mode in parameter
	 *
	 * @param p_sValue the mode of collate to apply
	 */
	public Collate(String p_sValue) {
		this.value = p_sValue;
	}
	
	/**
	 * Returns the value of the mode
	 *
	 * @return the value of the mode
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the mode
	 *
	 * @param p_sValue the value of the mode
	 */
	public void setValue(String p_sValue) {
		this.value = p_sValue;
	}
	
	/**
	 * Generates the sql of the collate
	 *
	 * @return collate in sql format
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public StringBuilder appendToSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext ) {
		if (this.value != null) {
			p_oSql.append(" COLLATE ");
			p_oSql.append(this.value);
		}
		return p_oSql ;
	}
}
