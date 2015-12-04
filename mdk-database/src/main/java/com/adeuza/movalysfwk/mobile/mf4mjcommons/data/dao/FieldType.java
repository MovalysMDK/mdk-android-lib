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

/**
 * Identify the type of a column.
 *
 */
public class FieldType {

	/**
	 * Field.
	 */
	private final Field field;
	
	/**
	 * Type.
	 */
	private final @SqlType int type;
	
	/**
	 * Field type.
	 * @param p_oField field
	 * @param p_iColumnType type of field
	 */
	public FieldType(Field p_oField, @SqlType int p_iColumnType ) {
		this.field = p_oField ;
		this.type = p_iColumnType ;
	}

	/**
	 * Get field.
	 * @return field.
	 */
	public Field getField() {
		return this.field;
	}

	/**
	 * Get type.
	 * @return type.
	 */
	public int getType() {
		return this.type;
	}
}
