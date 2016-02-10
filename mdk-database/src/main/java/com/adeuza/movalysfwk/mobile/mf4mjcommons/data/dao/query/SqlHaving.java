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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;

/**
 * <p>Describe having part of a sql query</p>
 */
public class SqlHaving implements Cloneable {

	/**
	 * Liste des conditions composant le Where
	 */
	private List<AbstractSqlCondition> conditions = new ArrayList<AbstractSqlCondition>();
	
	/**
	 * Ajoute une condition au Where
	 *
	 * @param p_oSqlCondition condition à rajouter dans le where
	 */
	public void addSqlCondition( AbstractSqlCondition p_oSqlCondition ) {
		this.conditions.add(p_oSqlCondition);
	}
	
	/**
	 * <p>toSql.</p>
	 *
	 * @param p_oSqlBuilder a {@link java.lang.StringBuilder} object.
	 * @param p_oSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public void toSql( StringBuilder p_oSqlBuilder, ToSqlContext p_oSqlContext ) throws DaoException {
		if ( !this.conditions.isEmpty()) {
			p_oSqlBuilder.append(' ');
			p_oSqlBuilder.append(SqlKeywords.HAVING);
			p_oSqlBuilder.append(' ');
			boolean bFirst = true ;
			for( AbstractSqlCondition oSqlCondition : this.conditions ) {
				if ( bFirst ) {
					bFirst = false ;
				} else {
					p_oSqlBuilder.append(' ');
					p_oSqlBuilder.append(oSqlCondition.getLink());
					p_oSqlBuilder.append(' ');
				}
				oSqlCondition.toSql(p_oSqlBuilder, p_oSqlContext);
			}
		}
	}

	/**
	 * Bind les parametres contenus dans le where
	 *
	 * @param p_oStatementBinder StatementBinder à utiliser pour binder les valeurs
	 * @throws java.sql.SQLException échec du binding
	 */
	public void bindValues(StatementBinder p_oStatementBinder) throws SQLException {
		for( AbstractSqlCondition oSqlCondition : conditions ) {
			oSqlCondition.bindValues(p_oStatementBinder);
		}	
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SqlHaving clone() {
		SqlHaving r_oSqlHaving = new SqlHaving();
		for( AbstractSqlCondition oCondition : this.conditions ) {
			r_oSqlHaving.addSqlCondition(oCondition.clone());
		}
		return r_oSqlHaving ;
	}
}
