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

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.SelectionArgsBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlClauseLink;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Permet de regrouper des conditions (parenthésage)</p>
 *
 * <p>Exemple :</p>
 * <p>Pour le sql suivant : </p>
 * <pre>
 * {@code
 * WHERE ( INTERVENTIONSTATEID = 5 AND INTERVENTIONTYPEID = 4 ) OR INTERVENTIONTYPEOLDID = 1
 * }</pre>
 *
 * <p>Le code Java correspondant est : </p>
 * <pre>
 * {@code
 * oSqlQuery.addToWhere( new SqlGroupCondition(
 *   new SqlEqualsValueCondition(
 *     InterventionField.INTERVENTIONSTATEID, InterventionDao.ALIAS_NAME, 5, SqlType.INTEGER ),
 *   new SqlEqualsValueCondition(
 *     InterventionField.INTERVENTIONTYPEID, InterventionDao.ALIAS_NAME, 4, SqlType.INTEGER )));
 * oSqlQuery.addToWhere(new SqlEqualsValueCondition(
 *   InterventionField.INTERVENTIONTYPEOLDID, InterventionDao.ALIAS_NAME, 1, SqlType.INTEGER, SqlClauseLink.OR));
 * }</pre>
 */
public class SqlGroupCondition extends AbstractSqlCondition implements Cloneable {

	/**
	 * Conditions du groupe
	 */
	private SqlConditionList conditions = new SqlConditionList();
	
	/**
	 * Constructeur avec les conditions(une à une) à grouper
	 *
	 * @param p_oCondition La liste de conditions à grouper
	 */
	public SqlGroupCondition(AbstractSqlCondition... p_oCondition) {
		for (AbstractSqlCondition oCondition : p_oCondition) {
			this.conditions.addSqlCondition(oCondition);
		}
	}
	
	/**
	 * Constructeur avec les conditions(une à une) à grouper, type de liaison
	 *
	 * @param p_sSqlClauseLink Lien du groupe avec le reste
	 * @param p_oInnerSqlClauseLink Lien entre les conditions du groupe
	 * @param p_oCondition La liste de conditions à grouper
	 */
	public SqlGroupCondition( @SqlClauseLink String p_sInnerSqlClauseLink, AbstractSqlCondition... p_oCondition) {
		for (AbstractSqlCondition oCondition : p_oCondition) {
			this.conditions.addSqlCondition(oCondition, p_sInnerSqlClauseLink);
		}
	}
	
	/**
	 * Constructeur avec les conditions(liste) à grouper
	 *
	 * @param p_listConditions La liste de conditions à grouper
	 */
	public SqlGroupCondition(SqlConditionList p_listConditions) {
		this.conditions = p_listConditions;
	}
	
	/**
	 * Constructeur avec les conditions(liste) à grouper, type de liaison
	 *
	 * @param p_listConditions La liste de conditions à grouper
	 * @param p_sSqlClauseLink Lien du groupe avec le reste
	 * @param p_sInnerSqlClauseLink Lien entre les conditions du groupe
	 */
	public SqlGroupCondition(List<AbstractSqlCondition> p_listConditions, @SqlClauseLink String p_sInnerSqlClauseLink) {
		for (AbstractSqlCondition oCondition : p_listConditions) {
			this.conditions.addSqlCondition(oCondition, p_sInnerSqlClauseLink);
		}
	}
	
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		this.conditions.bindValues(p_oStatementBinder);
	}

	@Override
	public AbstractSqlCondition clone() {
		SqlGroupCondition r_oSqlGroupCondition = new SqlGroupCondition();
		r_oSqlGroupCondition.conditions = this.conditions.clone();
		return r_oSqlGroupCondition;
	}

	@Override
	public void toSql( StringBuilder p_oSqlBuilder, ToSqlContext p_oToSqlContext) throws DaoException {
		p_oSqlBuilder.append('(');
		this.conditions.toSql(p_oSqlBuilder, p_oToSqlContext);
		p_oSqlBuilder.append(')');
	}

	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append('(');
		p_oSql.append(this.conditions.computeContentResolverSelection());
		p_oSql.append(')');
	}
	
	@Override
	public void computeContentResolverSelectionArgs(
			SelectionArgsBuilder p_oSqlArgsBuilder) {
		this.conditions.computeContentResolverSelectionArgs(p_oSqlArgsBuilder);
	}
}
