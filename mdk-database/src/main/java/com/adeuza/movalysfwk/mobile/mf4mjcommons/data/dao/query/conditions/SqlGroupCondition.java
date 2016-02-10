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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
	private List<AbstractSqlCondition> conditions = new ArrayList<AbstractSqlCondition>();
	
	/**
	 * 
	 */
	private SqlClauseLink innerClauseLink = SqlClauseLink.AND;
	
	/**
	 * Constructeur avec les conditions(une à une) à grouper
	 *
	 * @param p_oCondition La liste de conditions à grouper
	 */
	public SqlGroupCondition(AbstractSqlCondition... p_oCondition) {
		for (AbstractSqlCondition oCondition : p_oCondition) {
			this.conditions.add(oCondition);
		}
	}
	
	/**
	 * Constructeur avec les conditions(une à une) à grouper, type de liaison
	 *
	 * @param p_oSqlClauseLink Lien du groupe avec le reste
	 * @param p_oInnerSqlClauseLink Lien entre les conditions du groupe
	 * @param p_oCondition La liste de conditions à grouper
	 */
	public SqlGroupCondition(SqlClauseLink p_oSqlClauseLink, SqlClauseLink p_oInnerSqlClauseLink, AbstractSqlCondition... p_oCondition) {
		for (AbstractSqlCondition oCondition : p_oCondition) {
			this.conditions.add(oCondition);
		}
		this.innerClauseLink = p_oInnerSqlClauseLink ;
		setLink(p_oSqlClauseLink);
	}
	
	/**
	 * Constructeur avec les conditions(liste) à grouper
	 *
	 * @param p_listConditions La liste de conditions à grouper
	 */
	public SqlGroupCondition(List<AbstractSqlCondition> p_listConditions) {
		for (AbstractSqlCondition oCondition : p_listConditions) {
			this.conditions.add(oCondition);
		}
	}
	
	/**
	 * Constructeur avec les conditions(liste) à grouper, type de liaison
	 *
	 * @param p_listConditions La liste de conditions à grouper
	 * @param p_oSqlClauseLink Lien du groupe avec le reste
	 * @param p_oInnerSqlClauseLink Lien entre les conditions du groupe
	 */
	public SqlGroupCondition(List<AbstractSqlCondition> p_listConditions, SqlClauseLink p_oSqlClauseLink, SqlClauseLink p_oInnerSqlClauseLink) {
		for (AbstractSqlCondition oCondition : p_listConditions) {
			this.conditions.add(oCondition);
		}
		this.setLink(p_oSqlClauseLink);
		this.innerClauseLink = p_oInnerSqlClauseLink ;
	}
	
	/** {@inheritDoc} */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws SQLException {
		for (AbstractSqlCondition oSqlCondition : this.conditions) {
			oSqlCondition.bindValues(p_oStatementBinder);
		}
	}

	/** {@inheritDoc} */
	@Override
	public AbstractSqlCondition clone() {
		return new SqlGroupCondition(this.conditions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) throws DaoException {
		p_oSql.append('(');
		
		for ( Iterator<AbstractSqlCondition> oIterator = this.conditions.iterator(); oIterator.hasNext();) {
			AbstractSqlCondition oSqlCondition = (AbstractSqlCondition) oIterator.next();
			oSqlCondition.toSql(p_oSql, p_oToSqlContext);
			if(oIterator.hasNext()){
				p_oSql.append(' ').append(this.innerClauseLink.name()).append(' ');
			}
		}
		p_oSql.append(')');
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append('(');
		
		for ( Iterator<AbstractSqlCondition> oIterator = this.conditions.iterator(); oIterator.hasNext();) {
			AbstractSqlCondition oSqlCondition = (AbstractSqlCondition) oIterator.next();
			oSqlCondition.computeContentResolverSelection(p_oSql);
			if(oIterator.hasNext()){
				p_oSql.append(' ').append(this.innerClauseLink.name()).append(' ');
			}
		}
		p_oSql.append(')');
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelectionArgs(
			SelectionArgsBuilder p_oSqlArgsBuilder) {
		for (AbstractSqlCondition oSqlCondition : this.conditions) {
			oSqlCondition.computeContentResolverSelectionArgs(p_oSqlArgsBuilder);
		}
	}
}
