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

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlHaving;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;

/**
 * <p>GroupBy class.</p>
 */
public final class GroupBy implements Cloneable {

	/**
	 * Empty cascade
	 */
	public static final GroupBy NONE = new GroupBy();

	/**
	 * Fields list
	 */
	private List<GroupedField> fields;
	
	/**
	 * Supplementary "Having" condition  
	 */
	private SqlHaving having;
	
	/**
	 * Constructor
	 * @param p_oFields fields list
	 * @param p_oSqlHaving having clause
	 */
	private GroupBy(List<GroupedField> p_oFields, SqlHaving p_oSqlHaving) {
		this.fields = p_oFields;
		this.having = p_oSqlHaving;
	}

	/**
	 * Constructor
	 * @param p_oFields fields list
	 */
	private GroupBy(List<GroupedField> p_oFields) {
		this.fields = p_oFields;
		this.having = null;
	}

	/**
	 * Empty constructor 
	 * <p>Constructeur private car l'utilisateur doit passer par la méthode of pour l'instancier</p>
	 */
	private GroupBy() {
		this.fields = new ArrayList<GroupedField>();
		this.having = null;
	}

	/**
	 * Return the GroupBy with the specified fields list
	 *
	 * @param p_oFields a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.GroupedField} object.
	 * @return GroupBy the built object
	 */
	public static GroupBy of(GroupedField... p_oFields) {
		List<GroupedField> listFields = new ArrayList<GroupedField>();
		for (GroupedField oField : p_oFields) {
			listFields.add(oField);
		}

		return new GroupBy(listFields);
	}

	/**
	 * <p>of.</p>
	 *
	 * @param p_sAliasName a {@link java.lang.String} object.
	 * @param p_oFields a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.GroupBy} object.
	 */
	public static GroupBy of(String p_sAliasName, Field... p_oFields) {
		List<GroupedField> listFields = new ArrayList<GroupedField>();
		for (Field oField : p_oFields) {
			listFields.add(GroupedField.of(oField, p_sAliasName));
		}
		return new GroupBy(listFields);
	}

	/**
	 * Sets the "Having" clause on the object
	 *
	 * @param p_oSqlCondition condition to add
	 */
	public void setHavingToGroupBy(AbstractSqlCondition p_oSqlCondition) {
		this.having = new SqlHaving();
		addToHavingOfGroupBy(p_oSqlCondition);
	}
	
	/**
	 * Adds an "Having" clause to the object
	 *
	 * @param p_oSqlCondition condition to add
	 */
	public void addToHavingOfGroupBy( AbstractSqlCondition p_oSqlCondition) {
		this.having.addSqlCondition(p_oSqlCondition);
	}
	
	/**
	 * Generates the sql GROUP BY command
	 *
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 * @return sql GROUP BY command
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public StringBuilder appendToSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext ) throws DaoException {
		if (!this.fields.isEmpty()) {
			p_oSql.append(" GROUP BY");
			char sSeparator = ' ';
			for (GroupedField oField : this.fields) {
				p_oSql.append(sSeparator);
				oField.appendToSQL(p_oSql, p_oToSqlContext);
				sSeparator = ',';
			}
			if ( having != null ) {
				having.toSql(p_oSql,p_oToSqlContext);
			}
		}
		return p_oSql ;
	}
	
	/**
	 * Clones the given object
	 *
	 * @return the clone
	 * @see java.lang.Object#clone()
	 */
	@Override
	public GroupBy clone() {
		List<GroupedField> listFields = new ArrayList<GroupedField>();
		for (GroupedField oField : this.fields ) {
			listFields.add(oField.clone());
		}
		if ( having != null ) {
			return new GroupBy(listFields, having);
		}
		return new GroupBy(listFields);
		
	}
}
