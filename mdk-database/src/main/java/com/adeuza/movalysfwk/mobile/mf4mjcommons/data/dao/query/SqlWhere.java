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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlConditionList;

/**
 * <p>Décrit le Where d'une SQLQuery</p>
 */
public class SqlWhere implements Cloneable {

	/**
	 * Liste des conditions composant le Where
	 */
	private SqlConditionList conditions = new SqlConditionList();
	
	/**
	 * Ajoute une condition au Where
	 *
	 * @param p_oSqlCondition condition à rajouter dans le where
	 */
	public void addSqlCondition( AbstractSqlCondition p_oSqlCondition ) {
		this.conditions.addSqlCondition(p_oSqlCondition);
	}
	
	/**
	 * <p>toSql.</p>
	 *
	 * @param p_oSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 * @param p_oSqlBuilder a {@link java.lang.StringBuilder} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public void toSql( StringBuilder p_oSqlBuilder, ToSqlContext p_oSqlContext ) throws DaoException {
		if ( !this.conditions.isEmpty()) {
			p_oSqlBuilder.append(' ');
			p_oSqlBuilder.append(SqlKeywords.WHERE);
			p_oSqlBuilder.append(' ');
			this.conditions.toSql(p_oSqlBuilder, p_oSqlContext);
		}
	}

	/**
	 * Bind les parametres contenus dans le where
	 *
	 * @param p_oStatementBinder StatementBinder à utiliser pour binder les valeurs
	 * @throws DaoException échec du binding
	 */
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		this.conditions.bindValues(p_oStatementBinder);	
	}
	
	/**
	 * Compute selection of ContentResolver
	 * @return selection of ContentResolver
	 */
	public String computeContentResolverSelection() {
		return this.conditions.computeContentResolverSelection();
	}

	/**
	 * Compute selection args of ContentResolver
	 * @return selection args of ContentResolver
	 */
	public String[] computeContentResolverSelectionArgs() {
		return this.conditions.computeContentResolverSelectionArgs();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Clone le Where
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SqlWhere clone() {
		SqlWhere r_oSqlWhere = new SqlWhere();
		r_oSqlWhere.conditions = this.conditions.clone();
		return r_oSqlWhere ;
	}
}
