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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Implémentation des requêtes pour la couche Dao avec gestion des spécificités Movalys : blocs histo + références</p>
 *
 *
 * @since 2.5
 * @see DaoQuery
 */
public class DaoQueryImpl implements DaoQuery {

	/**
	 * Query
	 */
	private SqlQuery sqlQuery;

	/**
	 * Pour compléter la lecture du resultset (pour les champs ajoutés à la main dans le select
	 */
	private List<EntityRowReaderCallBack> rowReaderCallBack = new ArrayList<EntityRowReaderCallBack>(); 

	/**
	 * Constructeur avec la SqlQuery
	 * <p>
	 * Utilisation des blocs d'histo par défaut
	 * </p>
	 *
	 * @param p_oSqlQuery
	 *            SqlQuery
	 * @param p_sEntityName Nom de l'enité
	 */
	public DaoQueryImpl(SqlQuery p_oSqlQuery, String p_sEntityName) {
		this.sqlQuery = p_oSqlQuery;
	}

	/**
	 * Constructeur avec SqlQuery et p_oOrderBy
	 * <p>
	 * Utilisation des blocs d'histo par défaut
	 * </p>
	 *
	 * @param p_oSqlQuery
	 *            SqlQuery
	 * @param p_sEntityName Nom de l'enité
	 * @param p_oOrderBy
	 *            order by de la requête
	 */
	public DaoQueryImpl(SqlQuery p_oSqlQuery, String p_sEntityName, OrderSet p_oOrderBy) {
		this(p_oSqlQuery,p_sEntityName);
		this.sqlQuery.setOrderBy(p_oOrderBy);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoQuery#prepareStatement(com.adeuza.movalysfwk.mobile.mf4javacommons.core.service.context.MContext)
	 */
	@Override
	public PreparedStatement prepareStatement(MContext p_oContext) throws DaoException {
		PreparedStatement r_oStatement = null ;
		try {
			Connection oConnection = ((ItfTransactionalContext)p_oContext).getTransaction().getConnection();
			String sQuery = buildQuery( p_oContext );
			try {
				r_oStatement = oConnection.prepareStatement(sQuery);
			}
			catch( SQLException oSQLException ) {
				throw new DaoException("Failed to execute query : " + sQuery, oSQLException );
			}
		}
		catch( SQLException oSQLException ) {
			throw new DaoException("Failed to get jdbc connection", oSQLException );
		}
		return r_oStatement ;
	}

	/**
	 * Construit la requête au format SQL
	 *
	 * @param p_oMContext transactional context
	 * @return la requête SQL
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException échec de construction de la requête
	 */
	protected String buildQuery(MContext p_oMContext) throws DaoException {
		return this.sqlQuery.toSql(new ToSqlContext(p_oMContext));
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoQuery#bindValues(java.sql.PreparedStatement)
	 */
	@Override
	public void bindValues(PreparedStatement p_oStatement) throws SQLException {
		StatementBinder oStatementBinder = new StatementBinder(p_oStatement);
		this.sqlQuery.bindValues(oStatementBinder);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalys.fwk.data.dao.DaoQuery#getSqlQuery()
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlQuery} object.
	 */
	@Override
	public SqlQuery getSqlQuery() {
		return this.sqlQuery;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.data.dao.DaoQuery#addRowReaderCallBack(com.adeuza.movalys.fwk.data.dao.EntityRowReaderCallBack)
	 */
	@Override
	public void addRowReaderCallBack(EntityRowReaderCallBack p_oRowReaderCallBack) {
		this.rowReaderCallBack.add(p_oRowReaderCallBack);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.data.dao.DaoQuery#doResultSetCustomRead(com.adeuza.movalys.fwk.core.beans.MIEntity, java.sql.ResultSet, int, com.adeuza.movalys.fwk.data.dao.DaoSession, com.adeuza.movalys.fwk.core.beans.CascadeSet)
	 */
	@Override
	public void doResultSetCustomRead( MEntity p_oEntity, ResultSetReader p_oResultSet, DaoSession p_oDaoSession, CascadeSet p_oCascadeSet ) throws SQLException {
		for( EntityRowReaderCallBack oEntityRowReaderCallBack : this.rowReaderCallBack ) {
			oEntityRowReaderCallBack.doReadRow(p_oEntity, p_oResultSet, p_oDaoSession, p_oCascadeSet);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.data.dao.DaoQuery#getFetchSize()
	 */
	@Override
	public int getFetchSize() {
		return this.sqlQuery.getFetchSize();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.data.dao.DaoQuery#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(int p_iFetchSize) {
		this.sqlQuery.setFetchSize(p_iFetchSize);
	}
}
