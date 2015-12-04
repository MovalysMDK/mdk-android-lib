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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;

/**
 * <p>Représente un champs de valeur null d'une requête Insert</p>
 * <p>INSERT INTO TABLE(...CHAMPS1...) VALUES (...,NULL,...)</p>
 *
 * <pre>
 * {@code
 * SqlInsert oSqlInsert = new SqlInsert();
 * oSqlInsert.addNullField(RequestField.MODIFDATE);
 * }</pre>
 *
 *
 * @since 2.5
 * @see AbstractSqlInsertField
 */
public class SqlInsertNullField extends AbstractSqlInsertField implements Cloneable {

	/**
	 * Constructeur avec le champs
	 *
	 * @param p_oField champs
	 */
	public SqlInsertNullField(Field p_oField) {
		setFieldName(p_oField.name());
	}

	/**
	 * Constructeur avec nom du champs
	 *
	 * @param p_sFieldName nom du champs
	 */
	public SqlInsertNullField(String p_sFieldName) {
		setFieldName(p_sFieldName);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Retourne le sql de la valeur du SqlInsertNullField
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField#valueToSql()
	 */
	@Override
	public void appendToValueClause(StringBuilder p_oSql, MContext p_oContext ) {
		p_oSql.append(SqlKeywords.NULL.toString());
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlInsertField
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField#clone()
	 */
	@Override
	public AbstractSqlInsertField clone() {
		return new SqlInsertNullField(this.getFieldName());
	}
}
