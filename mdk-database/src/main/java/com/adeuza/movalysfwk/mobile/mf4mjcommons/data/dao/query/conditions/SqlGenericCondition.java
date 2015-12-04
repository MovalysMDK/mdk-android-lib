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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions;

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.SelectionArgsBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlBindValue;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition générique : permet d'insérer librement tout type de sql dans la requête.</p>
 *
 * <p>Exemple :</p>
 * <p>Pour le sql suivant : </p>
 * <pre>
 * {@code
 * intervention1.code = lower(?) and intervention1.description like upper(?)
 * }</pre>
 * <p>Le code Java correspondant est : </p>
 * <pre>
 * {@code
 * oSqlQuery.addToWhere( new SqlGenericCondition("intervention1.code = lower(?) and intervention1.description like upper(?)",
 *  new SqlBindValue("CODE001", SqlType.VARCHAR),
 *	new SqlBindValue("%computer%", SqlType.VARCHAR)));
 * }</pre>
 * @see AbstractSqlCondition
 */
public class SqlGenericCondition extends AbstractSqlCondition implements Cloneable {

	/**
	 * SQL de la condition
	 */
	private String sqlText ; 
	
	/**
	 * Liste des valeurs bindées 
	 */
	private List<SqlBindValue> values = new ArrayList<>();
	
	/**
	 * Constructeur avec le SQL et les valeurs bindées
	 *
	 * @param p_sSql SQL de la condition
	 * @param p_oValue liste des valeurs bindées
	 */
	public SqlGenericCondition( String p_sSql, SqlBindValue...p_oValue ) {
		this.sqlText = p_sSql ;
		for( SqlBindValue oValue: p_oValue ) {
			values.add(oValue);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Retourne le SQL de la condition générique
	 */
	@Override
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext ) {
		p_oSql.append(this.sqlText);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind les valeurs de la condition générique
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#bindValues(StatementBinder)
	 *
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder ) throws DaoException {
		for( SqlBindValue oBindValue: this.values ) {
			p_oStatementBinder.bind(oBindValue.getValue(), oBindValue.getSqlType());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone la condition générique
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#clone()
	 */
	@Override
	public AbstractSqlCondition clone() {
		return new SqlGenericCondition(this.sqlText);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append(this.sqlText);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
		for( SqlBindValue oBindValue: this.values ) {
			p_oSqlArgsBuilder.addValue(oBindValue.getValue(), oBindValue.getSqlType());
		}
	}
}
