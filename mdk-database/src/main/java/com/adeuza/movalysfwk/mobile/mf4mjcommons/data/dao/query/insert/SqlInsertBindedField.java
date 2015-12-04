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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;

/**
 * <p>Représente un champs bindé d'un preparedStatement d'une requête Insert</p>
 * <p>INSERT INTO TABLE(...CHAMPS1...) VALUES (...,?,...)</p>
 *
 * <p>Cette classe ne s'utilise pas directement. Il faut passer par SqlInsert.</p>
 * <pre>
 * {@code
 * SqlInsert oSqlInsert = new SqlInsert();
 * oSqlInsert.addBindedField(RequestField.CODE);
 * }</pre>
 * <p>
 *
 * @since 2.5
 * @see AbstractSqlInsertField
 */
public class SqlInsertBindedField extends AbstractSqlInsertField implements Cloneable {

	/**
	 * Constructeur avec champs de la valeur bindée
	 *
	 * @param p_oField champs
	 */
	public SqlInsertBindedField(Field p_oField) {
		setFieldName(p_oField.name());
	}
	
	/**
	 * Constructeur avec champs de la valeur bindée
	 *
	 * @param p_sFieldName champs
	 */
	public SqlInsertBindedField(String p_sFieldName) {
		setFieldName(p_sFieldName);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField#appendToColumnClause(java.lang.StringBuilder, com.adeuza.movalysfwk.mobile.mf4javacommons.core.service.context.MContext)
	 */
	@Override
	public void appendToColumnClause(StringBuilder p_oSql, MContext p_oContext) {
		super.appendToColumnClause(p_oSql, p_oContext);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Genere le Sql de la valeur bindée
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField#valueToSql()
	 */
	@Override
	public void appendToValueClause(StringBuilder p_oSql, MContext p_oContext ) {
		p_oSql.append('?');
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlInsertBindedField
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField#clone()
	 */
	@Override
	public AbstractSqlInsertField clone() {
		return new SqlInsertBindedField(this.getFieldName());
	}
}
