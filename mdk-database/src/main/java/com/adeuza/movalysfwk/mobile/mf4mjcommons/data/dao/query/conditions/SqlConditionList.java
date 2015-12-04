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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlClauseLink;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

public class SqlConditionList {

	/**
	 * Liste des conditions composant le Where
	 */
	private List<AbstractSqlCondition> conditions = new ArrayList<AbstractSqlCondition>();
	
	public void addSqlCondition( AbstractSqlCondition p_oSqlCondition ) {
		if ( !conditions.isEmpty() && p_oSqlCondition != SqlClauseLinks.AND && p_oSqlCondition != SqlClauseLinks.OR ) {
			AbstractSqlCondition prevCondition = this.conditions.get(conditions.size()-1);
			if ( prevCondition != SqlClauseLinks.AND && prevCondition != SqlClauseLinks.OR ) {
				this.conditions.add(SqlClauseLinks.AND);
			}
		}
		this.conditions.add(p_oSqlCondition);
	}
	
	public void addSqlCondition( AbstractSqlCondition p_oSqlCondition, @SqlClauseLink String p_sClauseLink ) {
		if ( !conditions.isEmpty()) {
			this.conditions.add(p_sClauseLink.equals(SqlClauseLink.OR) ? SqlClauseLinks.OR : SqlClauseLinks.AND);
		}
		this.conditions.add(p_oSqlCondition);
	}
	
	public void toSql( StringBuilder p_oSqlBuilder, ToSqlContext p_oSqlContext ) throws DaoException {
		for( AbstractSqlCondition oSqlCondition : this.conditions ) {
			oSqlCondition.toSql(p_oSqlBuilder, p_oSqlContext);
		}
	}
	
	/**
	 * Bind les parametres contenus dans le where
	 *
	 * @param p_oStatementBinder StatementBinder à utiliser pour binder les valeurs
	 * @throws DaoException échec du binding
	 */
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		for( AbstractSqlCondition oSqlCondition : conditions ) {
			oSqlCondition.bindValues(p_oStatementBinder);
		}	
	}
	
	/**
	 * Compute selection of ContentResolver
	 * @return selection of ContentResolver
	 */
	public String computeContentResolverSelection() {
		String r_sResult = null;
		if ( !this.conditions.isEmpty()) {
			StringBuilder oSelectBuilder = new StringBuilder();
			for( AbstractSqlCondition oSqlCondition : this.conditions ) {
				oSqlCondition.computeContentResolverSelection(oSelectBuilder);
			}
			r_sResult = oSelectBuilder.toString();
		}
		return r_sResult;
	}
	
	/**
	 * Compute selection args of ContentResolver
	 * @return selection args of ContentResolver
	 */
	public String[] computeContentResolverSelectionArgs() {
		String[] r_t_sResult = null;
		if ( !this.conditions.isEmpty()) {
			SelectionArgsBuilder oSelectionArgsBuilder = new SelectionArgsBuilder();
			computeContentResolverSelectionArgs(oSelectionArgsBuilder);
			r_t_sResult = oSelectionArgsBuilder.getSelectionArgs();
		}
		return r_t_sResult;
	}
	
	/**
	 * Compute resolver args.
	 * @param p_oSqlArgsBuilder args builder.
	 */
	public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
		for( AbstractSqlCondition oSqlCondition : this.conditions ) {
			oSqlCondition.computeContentResolverSelectionArgs(p_oSqlArgsBuilder);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Clone le ConditionList
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SqlConditionList clone() {
		SqlConditionList r_oConditionList = new SqlConditionList();
		for( AbstractSqlCondition oCondition : this.conditions ) {
			r_oConditionList.addSqlCondition(oCondition.clone());
		}
		return r_oConditionList ;
	}

	public boolean isEmpty() {
		return this.conditions.isEmpty();
	}
	
	public List<AbstractSqlCondition> getList() {
		return this.conditions;
	}
}
