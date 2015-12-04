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
 * <p>Représente un champs valué par une séquence d'un preparedStatement d'une requête Insert</p>
 * <p>INSERT INTO TABLE(...CHAMPS1...) VALUES (...,SEQ.NEXTVAL,...)</p>
 *
 * <p>Cette classe ne s'utilise pas directement. Il faut passer par SqlInsert.</p>
 * <pre>
 * {@code
 * SqlInsert oSqlInsert = new SqlInsert();
 * oSqlInsert.addNextValField(RequestDao.RequestField.ID, "SEQ_REQUEST");
 * }</pre>
 * <p>
 *
 *
 * @since 2.5
 * @see AbstractSqlInsertField
 */
public class SqlInsertAutoIncrementField extends AbstractSqlInsertField implements Cloneable {

	/**
	 * Constructeur avec champs, nom de la séquence
	 *
	 * @param p_oField champs
	 */
	public SqlInsertAutoIncrementField(Field p_oField) {
		this.setFieldName(p_oField.name());
	}

	/**
	 * Constructeur avec nom du champs, nom de la séquence
	 *
	 * @param p_sFieldName nom du champs
	 */
	public SqlInsertAutoIncrementField(String p_sFieldName) {
		this.setFieldName(p_sFieldName);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone du SqlInsertNextValField
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart#clone()
	 */
	@Override
	public AbstractSqlInsertField clone() {
		return new SqlInsertAutoIncrementField(this.getFieldName());
	}

	/**
	 * {@inheritDoc}
	 *
	 * Génère le sql de la valeur du SqlInsertNextValField
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField#valueToSql()
	 */
	@Override
	public void appendToValueClause(StringBuilder p_oSql, MContext p_oContext ) {
		p_oSql.append("null");
	}
}
