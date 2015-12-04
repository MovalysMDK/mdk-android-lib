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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Représente un champs avec valeur d'une requête Insert</p>
 * <p>INSERT INTO TABLE(...CHAMPS1...) VALUES (...,'valeur',...)
 *
 *
 * @since 2.5
 * @see AbstractSqlInsertField
 */
public class SqlInsertValueField extends AbstractSqlInsertField implements Cloneable {

	/**
	 * Valeur du champs
	 */
	private String value ;
	
	/**
	 * Constructeur avec nom du champs et la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sValue valeur
	 */
	public SqlInsertValueField(String p_sFieldName, String p_sValue) {
		setFieldName(p_sFieldName);
		this.value = p_sValue ;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Génère le sql de la valeur de SqlInsertValueField
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField#valueToSql()
	 */
	@Override
	public void appendToValueClause(StringBuilder p_oSql, MContext p_oContext ) {
		p_oSql.append(this.value);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlInsertValueField
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField#clone()
	 */
	@Override
	public AbstractSqlInsertField clone() {
		return new SqlInsertValueField(this.getFieldName(), this.value);
	}
}
