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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;

/**
 * <p>
 * Représente une jointure de type LEFT OUTER JOIN sur un SELECT.
 * </p>
 * <p>Exemple : <p>
 * <p>Pour la requête sql suivante :</p>
 * <pre>
 * {@code
 * SELECT request1.CODE, intervention1.CODE
 * FROM MIT_REQUEST request1
 * LEFT OUTER JOIN (
 * 	 SELECT ID, CODE
 *   FROM MIT_INTERVENTION intervention1
 * ) intervention1
 * ON intervention1.requestid = request1.id ;
 * }</pre>
 * <p>Le code Java correspondant est : </p>
 * <pre>
 * {@code
 * oSqlQuery oSubQuery = new SqlQuery();
 * oSubQuery.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionDao.InterventionField.ID, InterventionDao.InterventionField.CODE);
 * oSubQuery.addToFrom(InterventionDao.TABLE_NAME, InterventionDao.ALIAS_NAME);
 *
 * SqlQuery oSqlSelect = new SqlQuery();
 * oSqlSelect.addFieldToSelect(RequestDao.ALIAS_NAME, RequestDao.RequestField.CODE);
 * oSqlSelect.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionDao.InterventionField.CODE);
 * oSqlSelect.addToFrom(RequestDao.TABLE_NAME, RequestDao.ALIAS_NAME);
 * oSqlSelect.getFirstFromPart().addSqlJoin( new SqlSelectLeftOuterJoin(
 *   oSubQuery, InterventionDao.ALIAS_NAME,
 *   new SqlEqualsFieldCondition(InterventionDao.InterventionField.REQUESTID, InterventionDao.ALIAS_NAME,
 *   RequestDao.RequestField.ID, RequestDao.ALIAS_NAME)));
 * }</pre>
 *
 * @since 2.5
 * @see AbstractSqlJoin
 */
public class SqlSelectLeftOuterJoin extends AbstractSqlJoin implements Cloneable {

	/**
	 * Requêt du left outer join
	 */
	private SqlQuery sqlQuery;

	/**
	 * Constructeur avec query, alias, liste des conditions de jointure
	 *
	 * @param p_oSqlQuery
	 *            query de la jointure
	 * @param p_sAlias
	 *            alias de la jointure
	 * @param p_oSqlConditions
	 *            liste des conditions
	 */
	public SqlSelectLeftOuterJoin(SqlQuery p_oSqlQuery, String p_sAlias, AbstractSqlCondition... p_oSqlConditions) {
		this.sqlQuery = p_oSqlQuery;
		this.setAlias(p_sAlias);
		for (AbstractSqlCondition oSqlCondition : p_oSqlConditions) {
			this.addOnCondition(oSqlCondition);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Retourne le type de jointure
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#getJoinType()
	 */
	@Override
	protected String getJoinType() {
		return SqlKeywords.LEFT_OUTER_JOIN.toString();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#getJoinWith(com.adeuza.movalysfwk.mobile.mf4javacommons.core.historisation.map.QueryHistorisationBlocMap)
	 */
	@Override
	protected String getJoinWith(ToSqlContext p_oToSqlContext) throws DaoException {
		return this.sqlQuery.toSql(p_oToSqlContext);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind les valeurs de la jointure
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#bindValues(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder)
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		this.sqlQuery.bindValues(p_oStatementBinder);
		super.bindValues(p_oStatementBinder);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone du SqlSelectLeftOuterJoin
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#clone()
	 */
	@Override
	public AbstractSqlJoin clone() {
		AbstractSqlJoin r_oSqlJoin = new SqlSelectLeftOuterJoin(this.sqlQuery.clone(), this.getAlias());
		r_oSqlJoin.onConditions = this.onConditions.clone();
		return r_oSqlJoin;
	}
}
