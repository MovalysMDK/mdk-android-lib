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

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4android.database.sqlite.MDKSQLiteStatement;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteConnection;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLitePreparedStatement;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteResultSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlInsert;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlUpdate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;

/**
 *
 * <p>
 * AbstractDao
 * </p>
 * @see Dao
 */
public abstract class AbstractDao implements Dao {
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Dao#genericDelete(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete,
	 *      com.adeuza.movalysfwk.mobile.mf4javacommons.core.service.context.ItfTransactionalContext)
	 */
	@Override
	public void genericDelete(SqlDelete p_oSqlDelete, MContext p_oContext) throws DaoException {
		AndroidSQLiteConnection oConnection = ((ItfTransactionalContext) p_oContext).getConnection();
		AndroidSQLitePreparedStatement oStatement = oConnection.prepareStatement(p_oSqlDelete.toSql(p_oContext));
		try {
			p_oSqlDelete.bindValues(oStatement);
			oStatement.executeUpdate();
		} finally {
			oStatement.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Dao#genericUpdate(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlUpdate,
	 *      com.adeuza.movalysfwk.mobile.mf4javacommons.core.service.context.ItfTransactionalContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.StatementBinderCallBack)
	 */
	@Override
	public void genericUpdate(SqlUpdate p_oSqlUpdate, MContext p_oContext, StatementBinderCallBack p_oStatementBinderCallBack)
			throws DaoException {

		AndroidSQLiteConnection oConnection = ((ItfTransactionalContext) p_oContext).getConnection();
		MDKSQLiteStatement oStatement = oConnection.compileStatement(p_oSqlUpdate.toSql(p_oContext));
		try {
			p_oStatementBinderCallBack.doBind(oStatement);
			oStatement.executeUpdate();
		} finally {
			oStatement.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Dao#genericInsert(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlInsert,
	 *      com.adeuza.movalysfwk.mobile.mf4javacommons.core.service.context.ItfTransactionalContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.StatementBinderCallBack)
	 */
	@Override
	public void genericInsert(SqlInsert p_oSqlInsert, MContext p_oContext, StatementBinderCallBack p_oStatementBinderCallBack)
			throws DaoException {
		AndroidSQLiteConnection oConnection = ((ItfTransactionalContext) p_oContext).getConnection();
		MDKSQLiteStatement oStatement = oConnection.compileStatement(p_oSqlInsert.toSql(p_oContext));
		try {
			p_oStatementBinderCallBack.doBind(oStatement);
			oStatement.executeInsert();
		} finally {
			oStatement.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Dao#genericSelect(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoQuery,
	 *      com.adeuza.movalysfwk.mobile.mf4javacommons.core.service.context.ItfTransactionalContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.ResultSetReaderCallBack)
	 */
	@Override
	public Object genericSelect(DaoQuery p_oSqlQuery, MContext p_oContext, ResultSetReaderCallBack p_oResultSetReader)
			throws DaoException {
		return this.genericSelect(p_oSqlQuery, p_oContext, new DaoSession(), p_oResultSetReader);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Dao#genericSelect(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoQuery,
	 *      com.adeuza.movalysfwk.mobile.mf4javacommons.core.service.context.ItfTransactionalContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoSession,
	 *      com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.ResultSetReaderCallBack)
	 */
	@Override
	public Object genericSelect(DaoQuery p_oDaoQuery, MContext p_oContext, DaoSession p_oDaoSession,
			ResultSetReaderCallBack p_oResultSetReader) throws DaoException {
		Object r_oResult = null;
		boolean bIsComplete = this.completeQueryForList(p_oDaoQuery);
		if (bIsComplete) {
			AndroidSQLitePreparedStatement oStatement = p_oDaoQuery.prepareStatement(p_oContext);
			try {
				StatementBinder oBinder = new StatementBinder(oStatement);
				p_oDaoQuery.getSqlQuery().bindValues(oBinder);

				AndroidSQLiteResultSet oResulSet = oStatement.executeQuery();
				try {
					r_oResult = p_oResultSetReader.doRead(oResulSet);
				} finally {
					oResulSet.close();
				}
			} finally {
				oStatement.close();
			}
		}
		return r_oResult;
	}

	/**
	 * Retourne le nom de la table correspondant au Dao
	 *
	 * @return le nom de la table
	 */
	protected abstract String getTableName();

	/**
	 * Retourne un booléen indiquant si la mise à jour de la map d'historisation de la requête en fonction de la requête de sélection a bien été
	 * effectuée. (false est retourné dans le cas ou l'entité n'existe pas en base) Cette méthode est appelée lorque l'on souhaite obtenir une entité
	 * en fonction de ses identifiants.
	 *
	 * @param p_oDaoQuery
	 *            la requête du DAO
	 * @param p_oLevelDaoQuery
	 *            la requête de selection pour obtenir le niveau d'historisation de l'entité
	 * @param p_oTransactionalContext
	 *            le contexte transactionnel
	 * @return true si la mise à jour de la map d'historisation de la requête a bien été effectuée, false si la map n'a pas put être mise à jour.
	 *         (false est retourné dans le cas ou l'entité n'existe pas en base)
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	protected boolean completeQueryForEntityByPK(DaoQuery p_oDaoQuery, DaoQuery p_oLevelDaoQuery, MContext p_oTransactionalContext)
			throws DaoException {
		return true;
	}

	/**
	 * Retourne un booléen indiquant si la mise à jour de la map d'historisation de la requête en fonction des map d'historisation des entités du
	 * tableau a bien été effectuée. Cette méthode est appelée lorque l'on souhaite obtenir une entité en fonction d'autres entités.
	 *
	 * @param p_oDaoQuery
	 *            la requête du DAO
	 * @param p_t_oMIEntity
	 *            le tableau d'entité movalys
	 * @return true si la mise à jour de la map d'historisation de la requête en fonction des map d'historisation des entités du tableau a bien été
	 *         effectuée, false si la map n'a pas put être mise à jour.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	protected boolean completeQueryForBy(DaoQuery p_oDaoQuery, MEntity... p_t_oMIEntity) throws DaoException {
		return true;
	}

	/**
	 * Retourne un booléen indiquant si la mise à jour de la map d'historisation de la requête a bien été effectuée. Cette méthode est appelée lorque
	 * l'on souhaite obtenir une liste d'entité.
	 *
	 * @param p_oDaoQuery
	 *            la requête du DAO
	 * @return true si la mise à jour de la map d'historisation de la requête a bien été effectuée, false si la map n'a pas put être mise à jour.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	protected boolean completeQueryForList(DaoQuery p_oDaoQuery) throws DaoException {
		return true;
	}
}
