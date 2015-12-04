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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from;

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin;

/**
 * <p>
 * Partie From représentant une requete SQL
 * </p>
 * <p>
 * ex: SELECT ... FROM ( SELECT * FROM TABLE ) alias ...
 * </p>
 * <p>Exemple :</p>
 * <p>Pour le Sql suivant :</p>
 * <pre>
 * {@code
 * SELECT intervention1.code
 * FROM (
 * 	 SELECT intervention1.code
 * 	 FROM intervention1
 * ) intervention1 }</pre>
 *
 * <p>Le code java correspondant est le suivant : </p>
 * <pre>
 * {@code
 * SqlQuery oFromQuery = new SqlQuery();
 * oFromQuery.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionField.CODE);
 * oFromQuery.addToFrom(InterventionDao.TABLE_NAME, InterventionDao.ALIAS_NAME);
 *
 * SqlQuery oSqlSelect = new SqlQuery();
 * oSqlSelect.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionField.CODE);
 * oSqlSelect.addToFrom( new SqlSelectFromPart(oFromQuery, InterventionDao.ALIAS_NAME)); }</pre>
 *
 * @since 2.5
 * @see AbstractSqlFromPart
 */
public class SqlSelectFromPart extends AbstractSqlFromPart implements Cloneable {

	/**
	 * Query du SqlSelectFromPart
	 */
	private SqlQuery sqlQuery;

	/**
	 * Contructeur avec la requête et l'alias
	 *
	 * @param p_oSqlQuery
	 *            requête du SqlSelectFromPart
	 * @param p_sAlias
	 *            alias pour la requête
	 */
	public SqlSelectFromPart(SqlQuery p_oSqlQuery, String p_sAlias) {
		this.sqlQuery = p_oSqlQuery;
		this.setAlias(p_sAlias);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.data.dao.query.from.AbstractSqlFromPart#toFirstSql(com.adeuza.movalys.fwk.core.historisation.map.QueryHistorisationBlocMap)
	 */
	@Override
	public String toFirstSql(ToSqlContext p_oSqlContext) throws DaoException {
		throw new DaoException(" La méthode toFirstSql n'est pas supporté pour SqlSelectFromPart.");
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.data.dao.query.from.AbstractSqlFromPart#toOtherSql(QueryHistorisationBlocMap)
	 */
	@Override
	public List<String> toOtherSql(ToSqlContext p_oSqlContext) throws DaoException {
		List<String> r_list = new ArrayList<String>();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("(");
		sBuilder.append(this.sqlQuery.toSql(p_oSqlContext));
		sBuilder.append(") ");
		sBuilder.append(this.getAlias());
		this.joinToSql(sBuilder, p_oSqlContext);
		r_list.add(sBuilder.toString());
		return r_list;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind les valeurs du SqlSelectFromPart
	 * @see com.adeuza.movalys.fwk.data.dao.query.from.AbstractSqlFromPart#bindValues(com.adeuza.movalys.fwk.data.dao.query.StatementBinder)
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		this.sqlQuery.bindValues(p_oStatementBinder);
		super.bindValues(p_oStatementBinder);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlSelectFromPart
	 * @see com.adeuza.movalys.fwk.data.dao.query.from.AbstractSqlFromPart#clone()
	 */
	@Override
	public AbstractSqlFromPart clone() {
		AbstractSqlFromPart r_oSqlFromPart = new SqlSelectFromPart(this.sqlQuery.clone(), this.getAlias());
		for (AbstractSqlJoin oJoin : this.getJoins()) {
			r_oSqlFromPart.addSqlJoin(oJoin.clone());
		}
		return r_oSqlFromPart;
	}
}
