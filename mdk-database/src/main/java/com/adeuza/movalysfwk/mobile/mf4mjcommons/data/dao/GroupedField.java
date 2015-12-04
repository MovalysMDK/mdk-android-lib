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
 * <p>GroupedField class.</p>
 */
public class GroupedField {

	/**
	 * Field.
	 */
	private String field;
	
	/**
	 * Value.
	 */
	private String value;
	
	/**
	 * Constructor
	 * @param p_oKey field
	 * @param p_oValue value
	 */
	private GroupedField(Field p_oKey, String p_oValue) {
		this(p_oKey.name(), p_oValue);
	}

	/**
	 * Constructor
	 * @param p_sKey key
	 * @param p_oValue value
	 */
	private GroupedField(String p_sKey, String p_oValue) {
		this.field = p_sKey ;
		this.value = p_oValue;
	}

	/**
	 * <p>of.</p>
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 * @param p_sAlias a {@link java.lang.String} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.GroupedField} object.
	 */
	public static GroupedField of(Field p_oField, String p_sAlias) {
		return new GroupedField(p_oField, p_sAlias);
	}

	/**
	 * Adds the group to the sql command given as a string builder
	 *
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public void appendToSQL( StringBuilder p_oSql, ToSqlContext p_oToSqlContext ) {
		StringBuilder sGroupedField = new StringBuilder();

		final String sTableAliasName = this.value;
		if (sTableAliasName != null) {
			sGroupedField.append(sTableAliasName);
			sGroupedField.append('.');
		}
		sGroupedField.append(this.field);

		String sFieldAlias = p_oToSqlContext.getFieldAlias(sGroupedField.toString());
		if ( sFieldAlias != null ) {
			p_oSql.append(sFieldAlias);
		}
		else {
			p_oSql.append(sGroupedField.toString());
		}
	}

	/**
	 * Clones a GroupedField
	 *
	 * @return the cloned GroupedField
	 */
	@Override
	public GroupedField clone() {
		return new GroupedField(this.field, this.value);
	}
}
