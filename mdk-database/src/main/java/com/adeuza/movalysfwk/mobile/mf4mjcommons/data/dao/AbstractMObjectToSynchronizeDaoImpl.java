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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Initializable;
import com.adeuza.movalysfwk.mobile.mf4android.database.sqlite.MDKSQLiteStatement;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteConnection;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLitePreparedStatement;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteResultSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlFunction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlInsert;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlUpdate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsValueCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlFunctionSelectPart;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.factory.MObjectToSynchronizeFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;

/**
 *
 * <p>DAO class : AbstractMObjectToSynchronizeDaoImpl</p>
 *
 */
public abstract class AbstractMObjectToSynchronizeDaoImpl extends AbstractEntityDao<MObjectToSynchronize> implements Initializable {
	
	/**
	 * Requête de sélection des entités
	 */
	private static SqlQuery selectQuery;

	/**
	 * Requête de comptage
	 */
	private static SqlQuery countQuery;

	/**
	 * Requête d'insertion
	 */
	private static SqlInsert insertQuery;

	/**
	 * Requête d'update
	 */
	private static SqlUpdate updateQuery;

	/**
	 * Requête de suppression.
	 */
	private static SqlDelete deleteQuery;

	/**
	 * Factory MObjectToSynchronizeFactory
	 */
	protected MObjectToSynchronizeFactory MObjectToSynchronizeFactory;

	/**
	 * Définition des optimisations des cascades
	 */
	protected CascadeOptimDefinition cascadeOptimDefinition;

	/**
	 * Compute the next identifier
	 *
	 * @return a long.
	 */
	@Override
	public long nextId() {
		synchronized (this.lastNewId) {
			return --this.lastNewId;
		}
	}

	/**
	 * Initializes the private attributes of this DAO: factories and daos use by this dao.
	 */
	@Override
	public void initialize() {
		this.MObjectToSynchronizeFactory = BeanLoader.getInstance().getBean(MObjectToSynchronizeFactory.class);

		init();

		cascadeOptimDefinition = new CascadeOptimDefinition(MObjectToSynchronizeDao.PK_FIELDS, MObjectToSynchronizeDao.ALIAS_NAME);

	}

	/**
	 * initializes static objects
	 */
	private static void init() {
		selectQuery = new SqlQuery();
		selectQuery.addFieldToSelect(MObjectToSynchronizeDao.ALIAS_NAME, MObjectToSynchronizeField.ID, MObjectToSynchronizeField.OBJECTID,
				MObjectToSynchronizeField.OBJECTNAME );
		selectQuery.addToFrom(MObjectToSynchronizeDao.TABLE_NAME, MObjectToSynchronizeDao.ALIAS_NAME);

		countQuery = new SqlQuery();
		countQuery.addCountToSelect(MObjectToSynchronizeField.ID, MObjectToSynchronizeDao.ALIAS_NAME);
		countQuery.addToFrom(MObjectToSynchronizeDao.TABLE_NAME, MObjectToSynchronizeDao.ALIAS_NAME);

		insertQuery = new SqlInsert(MObjectToSynchronizeDao.TABLE_NAME);

		insertQuery.addBindedField(MObjectToSynchronizeField.ID);
		insertQuery.addBindedField(MObjectToSynchronizeField.OBJECTID);
		insertQuery.addBindedField(MObjectToSynchronizeField.OBJECTNAME);

		updateQuery = new SqlUpdate(MObjectToSynchronizeDao.TABLE_NAME);

		updateQuery.addBindedField(MObjectToSynchronizeField.ID);
		updateQuery.addBindedField(MObjectToSynchronizeField.OBJECTID);
		updateQuery.addBindedField(MObjectToSynchronizeField.OBJECTNAME);

		deleteQuery = new SqlDelete(MObjectToSynchronizeDao.TABLE_NAME);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Initializes the private attributes of this DAO: factories and daos use by this dao.
	 */
	@Override
	public void initialize(MContext p_oContext) throws DaoException {
		SqlQuery oSelect = new SqlQuery();
		oSelect.addFunctionToSelect(new SqlFunctionSelectPart(SqlFunction.MIN, MObjectToSynchronizeField.ID, MObjectToSynchronizeDao.ALIAS_NAME));
		oSelect.addToFrom(MObjectToSynchronizeDao.TABLE_NAME, MObjectToSynchronizeDao.ALIAS_NAME);
		this.lastNewId = (Long) this.genericSelect(new DaoQueryImpl(oSelect, this.getEntityName()), p_oContext, new ResultSetReaderCallBack() {
			@Override
			public Object doRead(AndroidSQLiteResultSet p_oResultSet) throws DaoException {
				long r_lMinId = UNSAVED_VALUE;
				if (p_oResultSet.next()) {
					r_lMinId = p_oResultSet.getLong(1);
					if (r_lMinId >= 0L) {
						r_lMinId = UNSAVED_VALUE;
					}
				}
				return r_lMinId;
			}
		});
	}
	
	/** {@inheritDoc} */
	@Override
	public String getTableName() {
		return MObjectToSynchronizeDao.TABLE_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected String getAliasName() {
		return MObjectToSynchronizeDao.ALIAS_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected String getEntityName() {
		return MObjectToSynchronize.ENTITY_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected FieldType[] getPKFields() {
		return MObjectToSynchronizeDao.PK_FIELDS;
	}

	/**
	 * Renvoie un clone de la requête de sélection des entités (pour qu'elle puisse être modifiée).
	 *
	 * @return un clone de la requête de sélection des entités.
	 */
	@Override
	public DaoQuery getSelectDaoQuery() {
		return new DaoQueryImpl(selectQuery.clone(), this.getEntityName());
	}

	/**
	 * Renvoie un clone de la requête de comptage (pour qu'elle puisse être modifiée).
	 *
	 * @return un clone de la requête de comptage.
	 */
	@Override
	public DaoQuery getCountDaoQuery() {
		return new DaoQueryImpl(countQuery.clone(), this.getEntityName());
	}

	/**
	 * Renvoie un clone de la requête d'insertion (pour qu'elle puisse être modifiée).
	 *
	 * @return un clone de la requête d'insertion.
	 */
	@Override
	public SqlInsert getInsertQuery() {
		return insertQuery.clone();
	}

	/**
	 * Renvoie un clone de la requête d'update (pour qu'elle puisse être modifiée).
	 *
	 * @return un clone de la requête d'update.
	 */
	@Override
	public SqlUpdate getUpdateQuery() {
		return updateQuery.clone();
	}

	/**
	 * Renvoie un clone de la requête de suppression (pour qu'elle puisse être modifiée).
	 *
	 * @return renvoie un clone de la requête de suppression.
	 */
	@Override
	public SqlDelete getDeleteQuery() {
		return deleteQuery.clone();
	}

	/**
	 * Retourne une entité MObjectToSynchronize selon la clé primaire
	 *
	 * Les blocs par défaut sont utilisés
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oContext contexte transactionnel
	 * @return une entité MObjectToSynchronize selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, MContext p_oContext) throws DaoException {
		return this.getMObjectToSynchronize(p_lId, this.getSelectDaoQuery(), CascadeSet.NONE, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne une entité MObjectToSynchronize selon la clé primaire
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoQuery requête
	 * @param p_oContext contexte transactionnel
	 * @return une entité MObjectToSynchronize selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException {
		return this.getMObjectToSynchronize(p_lId, p_oDaoQuery, CascadeSet.NONE, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne une entité MObjectToSynchronize selon la clé primaire
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une entité MObjectToSynchronize selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {
		return this.getMObjectToSynchronize(p_lId, this.getSelectDaoQuery(), p_oCascadeSet, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne une entité MObjectToSynchronize selon la clé primaire
	 *
	 * Les blocs par défaut sont utilisés
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une entité MObjectToSynchronize selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, DaoSession p_oDaoSession, MContext p_oContext) throws DaoException {
		return this.getMObjectToSynchronize(p_lId, this.getSelectDaoQuery(), CascadeSet.NONE, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne une entité MObjectToSynchronize selon la clé primaire
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoQuery requête
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une entité MObjectToSynchronize selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		return this.getMObjectToSynchronize(p_lId, p_oDaoQuery, p_oCascadeSet, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne une entité MObjectToSynchronize selon la clé primaire
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoQuery requête
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une entité MObjectToSynchronize selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {
		return this.getMObjectToSynchronize(p_lId, p_oDaoQuery, CascadeSet.NONE, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne une entité MObjectToSynchronize selon la clé primaire
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une entité MObjectToSynchronize selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException {
		return this.getMObjectToSynchronize(p_lId, this.getSelectDaoQuery(), p_oCascadeSet, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne une entité MObjectToSynchronize selon la clé primaire
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoQuery requête
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une entité MObjectToSynchronize selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException {
		MObjectToSynchronize r_oMObjectToSynchronize = null;
		try {
			p_oDaoQuery.getSqlQuery().addEqualsConditionToWhere(MObjectToSynchronizeField.ID, MObjectToSynchronizeDao.ALIAS_NAME, p_lId,
					SqlType.INTEGER);
			AndroidSQLitePreparedStatement oStatement = p_oDaoQuery.prepareStatement(p_oContext);

			try {
				p_oDaoQuery.bindValues(oStatement);
				ResultSetReader oResultSetReader = new ResultSetReader(oStatement.executeQuery());
				try {
					while (oResultSetReader.next()) {
						r_oMObjectToSynchronize = this.valueObject(oResultSetReader, p_oDaoQuery, p_oDaoSession, p_oCascadeSet, p_oContext);
					}
				} finally {
					oResultSetReader.close();
				}
			} finally {
				oStatement.close();
			}
		} catch (IOException e) {
			throw new DaoException(e);
		}
		return r_oMObjectToSynchronize;
	}

	/**
	 * Retourne la liste de toutes les entités MObjectToSynchronize.
	 *
	 * Les blocs par défaut sont utilisés
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(MContext p_oContext) throws DaoException {
		return this.getListMObjectToSynchronize(getSelectDaoQuery(), CascadeSet.NONE, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne la liste des entités MObjectToSynchronize selon la requête.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException {
		return this.getListMObjectToSynchronize(p_oDaoQuery, CascadeSet.NONE, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne la liste de toutes les entités MObjectToSynchronize.
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {
		return this.getListMObjectToSynchronize(getSelectDaoQuery(), p_oCascadeSet, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne la liste de toutes les entités MObjectToSynchronize.
	 *
	 * Les blocs par défaut sont utilisés
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(DaoSession p_oDaoSession, MContext p_oContext) throws DaoException {
		return this.getListMObjectToSynchronize(getSelectDaoQuery(), CascadeSet.NONE, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne la liste des entités MObjectToSynchronize selon la requête.
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		return this.getListMObjectToSynchronize(p_oDaoQuery, p_oCascadeSet, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne la liste des entités MObjectToSynchronize selon la requête.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {
		return this.getListMObjectToSynchronize(p_oDaoQuery, CascadeSet.NONE, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne la liste de toutes les entités MObjectToSynchronize.
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException {
		return this.getListMObjectToSynchronize(getSelectDaoQuery(), p_oCascadeSet, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne la liste des entités MObjectToSynchronize selon la requête.
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException {

		return this.getList(p_oDaoQuery, p_oCascadeSet, p_oDaoSession, p_oContext);
	}

	/**
	 * Sauve ou met à jour l'entité MObjectToSynchronize passée en paramètre selon son existence en base.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, MContext p_oContext)
			throws DaoException {
		this.saveOrUpdateMObjectToSynchronize(p_oMObjectToSynchronize, CascadeSet.NONE, p_oContext);
	}

	/**
	 * Sauve ou met à jour l'entité MObjectToSynchronize passée en paramètre selon son existence en base.
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException {

		this.validateBean(p_oMObjectToSynchronize, p_oContext);
		boolean bHaveErrorMessage = p_oContext.getMessages().hasErrors();

		if (!bHaveErrorMessage) {

			if (this.exist(p_oMObjectToSynchronize, p_oCascadeSet, p_oContext)) {
				this.updateMObjectToSynchronize(p_oMObjectToSynchronize, p_oCascadeSet, p_oContext);
			} else {
				if (p_oMObjectToSynchronize.getId() < 0L) {
					p_oMObjectToSynchronize.setId(this.nextId());
				}
				this.saveMObjectToSynchronize(p_oMObjectToSynchronize, p_oCascadeSet, p_oContext);
			}

		}
	}

	/**
	 * Sauve ou met à jour laliste d'entité MObjectToSynchronize passée en paramètre selon leur existence en base.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_listMObjectToSynchronize une liste d'entité MObjectToSynchronize
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, MContext p_oContext)
			throws DaoException {
		this.saveOrUpdateListMObjectToSynchronize(p_listMObjectToSynchronize, CascadeSet.NONE, p_oContext);
	}

	/**
	 * Sauve ou met à jour laliste d'entité MObjectToSynchronize passée en paramètre selon leur existence en base.
	 *
	 * @param p_listMObjectToSynchronize une liste d'entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException {

		this.validateBeanList(p_listMObjectToSynchronize, p_oContext);
		boolean bHaveErrorMessage = p_oContext.getMessages().hasErrors();

		if (!bHaveErrorMessage) {
			List<MObjectToSynchronize> listSave = new ArrayList<MObjectToSynchronize>();
			List<MObjectToSynchronize> listUpdate = new ArrayList<MObjectToSynchronize>();
			for (MObjectToSynchronize oMObjectToSynchronize : p_listMObjectToSynchronize) {
				if (this.exist(oMObjectToSynchronize, p_oCascadeSet, p_oContext)) {
					listUpdate.add(oMObjectToSynchronize);
				} else {
					if (oMObjectToSynchronize.getId() < 0L) {
						oMObjectToSynchronize.setId(this.nextId());
					}
					listSave.add(oMObjectToSynchronize);
				}
			}

			if (!listSave.isEmpty()) {
				this.saveListMObjectToSynchronize(listSave, p_oCascadeSet, p_oContext);
			}
			if (!listUpdate.isEmpty()) {
				this.updateListMObjectToSynchronize(listUpdate, p_oCascadeSet, p_oContext);
			}
		}
	}

	/**
	 * Supprime l'entité MObjectToSynchronize passée en paramètre de la base de données.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, MContext p_oContext) throws DaoException {
		this.deleteMObjectToSynchronize(p_oMObjectToSynchronize, CascadeSet.NONE, p_oContext);
	}

	/**
	 * Supprime l'entité MObjectToSynchronize passée en paramètre de la base de données.
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		SqlDelete oSqlDelete = getDeleteQuery();
		oSqlDelete.addEqualsConditionToWhere(MObjectToSynchronizeField.ID, p_oMObjectToSynchronize.getId(), SqlType.INTEGER);

		AndroidSQLiteConnection oConnection = ((MContextImpl) p_oContext).getConnection();
		AndroidSQLitePreparedStatement oStatement = oConnection.prepareStatement(oSqlDelete.toSql(p_oContext));
		try {
			oSqlDelete.bindValues(oStatement);
			oStatement.executeUpdate();
		} finally {
			oStatement.close();
		}
	}

	/**
	 * Supprime l'entité MObjectToSynchronize correspondant aux paramètres de la base de données.
	 *
	 * Cette suppression ne supprime pas les entités liés en cascade.
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMObjectToSynchronize(long p_lId, MContext p_oContext) throws DaoException {
		
		SqlDelete oSqlDelete = getDeleteQuery();
		oSqlDelete.addEqualsConditionToWhere(MObjectToSynchronizeField.ID, p_lId, SqlType.INTEGER);
		AndroidSQLiteConnection oConnection = ((MContextImpl) p_oContext).getConnection();
		AndroidSQLitePreparedStatement oStatement = oConnection.prepareStatement(oSqlDelete.toSql(p_oContext));
		try {
			oSqlDelete.bindValues(oStatement);
			oStatement.executeUpdate();
		} finally {
			oStatement.close();
		}
	}

	/**
	 * Supprime de la base de données la liste d'entité MObjectToSynchronize passée en paramètre.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_listMObjectToSynchronize une liste d'entité MObjectToSynchronize
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, MContext p_oContext)
			throws DaoException {
		this.deleteListMObjectToSynchronize(p_listMObjectToSynchronize, CascadeSet.NONE, p_oContext);
	}

	/**
	 * Supprime de la base de données la liste d'entité MObjectToSynchronize passée en paramètre.
	 *
	 * @param p_listMObjectToSynchronize une liste d'entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException {

		if (!p_listMObjectToSynchronize.isEmpty()) {
			SqlDelete oSqlDelete = getDeleteQuery();
			SqlEqualsValueCondition oSqlEqualsValueCondition1 = oSqlDelete.addEqualsConditionToWhere(MObjectToSynchronizeField.ID,
					SqlType.INTEGER);

			AndroidSQLiteConnection oConnection = ((MContextImpl) p_oContext).getConnection();
			AndroidSQLitePreparedStatement oStatement = oConnection.prepareStatement(oSqlDelete.toSql(p_oContext));
			try {
				for (MObjectToSynchronize oMObjectToSynchronize : p_listMObjectToSynchronize) {
					oSqlEqualsValueCondition1.setValue(oMObjectToSynchronize.getId());

					oSqlDelete.bindValues(oStatement);
					oStatement.addBatch();
				}

				oStatement.executeBatch();
			} finally {
				oStatement.close();
			}
		}
	}

	/**
	 * Retourne le nombre d'entité MObjectToSynchronize en base.
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_oContext contexte transactionnel
	 * @return le nombre d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public int getNbMObjectToSynchronize(MContext p_oContext) throws DaoException {
		return this.getNbMObjectToSynchronize(getCountDaoQuery(), p_oContext);
	}

	/**
	 * Retourne le nombre d'entité MObjectToSynchronize selon la requête.
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oContext contexte transactionnel
	 * @return le nombre d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public int getNbMObjectToSynchronize(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException {
		return this.getNb(p_oDaoQuery, p_oContext);
	}

	/**
	 * <p>saveMObjectToSynchronize.</p>
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected void saveMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		if (!p_oContext.getMessages().hasErrors()) {
			SqlInsert oSqlInsert = this.getInsertQuery();
			AndroidSQLiteConnection oConnection = ((MContextImpl) p_oContext).getConnection();
			MDKSQLiteStatement oStatement = oConnection.compileStatement(oSqlInsert.toSql(p_oContext));
			try {
				bindInsert(p_oMObjectToSynchronize, oStatement, p_oContext);
				oStatement.executeUpdate();

			} finally {
				oStatement.close();
			}
		}
	}

	/**
	 * <p>saveListMObjectToSynchronize.</p>
	 *
	 * @param p_listMObjectToSynchronize une liste d'entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected void saveListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException {
		if (!p_oContext.getMessages().hasErrors()) {
			SqlInsert oSqlInsert = this.getInsertQuery();
			AndroidSQLiteConnection oConnection = ((MContextImpl) p_oContext).getConnection();
			MDKSQLiteStatement oStatement = oConnection.compileStatement(oSqlInsert.toSql(p_oContext));
			try {
				for (MObjectToSynchronize oMObjectToSynchronize : p_listMObjectToSynchronize) {
					bindInsert(oMObjectToSynchronize, oStatement, p_oContext);
					oStatement.executeUpdate();
				}

			} finally {
				oStatement.close();
			}
		}
	}

	/**
	 * <p>updateMObjectToSynchronize.</p>
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected void updateMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException {

		if (!p_oContext.getMessages().hasErrors()) {

			SqlUpdate oSqlUpdate = this.getUpdateQuery();
			oSqlUpdate.addEqualsConditionToWhere(MObjectToSynchronizeField.ID, SqlType.INTEGER);
			AndroidSQLiteConnection oConnection = ((MContextImpl) p_oContext).getConnection();
			MDKSQLiteStatement oStatement = oConnection.compileStatement(oSqlUpdate.toSql(p_oContext));
			try {
				bindUpdate(p_oMObjectToSynchronize, oStatement, p_oContext);
				oStatement.executeUpdate();
			} finally {
				oStatement.close();
			}
		}
	}

	/**
	 * <p>updateListMObjectToSynchronize.</p>
	 *
	 * @param p_listMObjectToSynchronize une liste d'entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected void updateListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException {
		
		if (!p_oContext.getMessages().hasErrors()) {

			SqlUpdate oSqlUpdate = this.getUpdateQuery();
			oSqlUpdate.addEqualsConditionToWhere(MObjectToSynchronizeField.ID, SqlType.INTEGER);
			AndroidSQLiteConnection oConnection = ((MContextImpl) p_oContext).getConnection();
			MDKSQLiteStatement oStatement = oConnection.compileStatement(oSqlUpdate.toSql(p_oContext));
			try {
				for (MObjectToSynchronize oMObjectToSynchronize : p_listMObjectToSynchronize) {
					bindUpdate(oMObjectToSynchronize, oStatement, p_oContext);
					oStatement.executeUpdate();
				}
			} finally {
				oStatement.close();
			}
		}
	}

	/**
	 * <p>exist.</p>
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return un boolean indiquant si l'entité existe en base
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected boolean exist(MObjectToSynchronize p_oMObjectToSynchronize, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		boolean r_bExist = false;
		DaoQuery oQuery = this.getSelectDaoQuery();
		oQuery.getSqlQuery().addEqualsConditionToWhere(MObjectToSynchronizeField.ID, MObjectToSynchronizeDao.ALIAS_NAME,
				p_oMObjectToSynchronize.getId(), SqlType.INTEGER);
		AndroidSQLitePreparedStatement oStatement = oQuery.prepareStatement(p_oContext);

		try {
			oQuery.bindValues(oStatement);
			AndroidSQLiteResultSet oResultSet = oStatement.executeQuery();
			try {
				if (oResultSet.next()) {
					r_bExist = true;
				}
			} finally {
				oResultSet.close();
			}
		} finally {
			oStatement.close();
		}
		return r_bExist;
	}

	/** {@inheritDoc} */
	@Override
	protected MObjectToSynchronize valueObject(ResultSetReader p_oResultSetReader, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession,
			CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException, IOException {

		MObjectToSynchronize r_oMObjectToSynchronize = this.MObjectToSynchronizeFactory.createInstance();
		r_oMObjectToSynchronize.setId(p_oResultSetReader.getLong());
		String sEntityId = r_oMObjectToSynchronize.idToString();
		MObjectToSynchronize oCachedMObjectToSynchronize = (MObjectToSynchronize) p_oDaoSession.getFromCache(MObjectToSynchronize.ENTITY_NAME,
				sEntityId);
		if (oCachedMObjectToSynchronize == null) {
			p_oDaoSession.addToCache(MObjectToSynchronize.ENTITY_NAME, sEntityId, r_oMObjectToSynchronize);
			r_oMObjectToSynchronize.setObjectId(p_oResultSetReader.getLong());
			r_oMObjectToSynchronize.setObjectName(p_oResultSetReader.getString());
		} else {
			r_oMObjectToSynchronize = oCachedMObjectToSynchronize;
		}

		p_oDaoQuery.doResultSetCustomRead(r_oMObjectToSynchronize, p_oResultSetReader, p_oDaoSession, p_oCascadeSet);

		return r_oMObjectToSynchronize;
	}

	/** {@inheritDoc} */
	@Override
	protected MObjectToSynchronize valueObject(ResultSetReader p_oResultSetReader, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession,
			CascadeSet p_oCascadeSet, CascadeOptim p_oCascadeOptim, MContext p_oContext) throws DaoException, IOException {

		MObjectToSynchronize r_oMObjectToSynchronize = this.MObjectToSynchronizeFactory.createInstance();

		r_oMObjectToSynchronize.setId(p_oResultSetReader.getLong());

		String sEntityId = r_oMObjectToSynchronize.idToString();
		MObjectToSynchronize oCachedMObjectToSynchronize = (MObjectToSynchronize) p_oDaoSession.getFromCache(MObjectToSynchronize.ENTITY_NAME,
				sEntityId);
		if (oCachedMObjectToSynchronize == null) {
			p_oDaoSession.addToCache(MObjectToSynchronize.ENTITY_NAME, sEntityId, r_oMObjectToSynchronize);
			p_oCascadeOptim.registerEntity(sEntityId, r_oMObjectToSynchronize, r_oMObjectToSynchronize.getId());
			r_oMObjectToSynchronize.setObjectId(p_oResultSetReader.getLong());
			r_oMObjectToSynchronize.setObjectName(p_oResultSetReader.getString());
		} else {
			r_oMObjectToSynchronize = oCachedMObjectToSynchronize;
		}

		p_oDaoQuery.doResultSetCustomRead(r_oMObjectToSynchronize, p_oResultSetReader, p_oDaoSession, p_oCascadeSet);

		return r_oMObjectToSynchronize;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind un prepareStatement d'insertion
	 */
	@Override
	protected void bindInsert(MObjectToSynchronize p_oMObjectToSynchronize, MDKSQLiteStatement p_oPreparedStatement, MContext p_oContext)
			throws DaoException {
		p_oPreparedStatement.bindLong(p_oMObjectToSynchronize.getId());
		p_oPreparedStatement.bindLong(p_oMObjectToSynchronize.getObjectId());
		p_oPreparedStatement.bindString(p_oMObjectToSynchronize.getObjectName());
	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind un preparedStatement de mise à jour
	 */
	@Override
	protected void bindUpdate(MObjectToSynchronize p_oMObjectToSynchronize, MDKSQLiteStatement p_oPreparedStatement, MContext p_oContext)
			throws DaoException {
		p_oPreparedStatement.bindLong(p_oMObjectToSynchronize.getId());
		p_oPreparedStatement.bindLong(p_oMObjectToSynchronize.getObjectId());
		p_oPreparedStatement.bindString(p_oMObjectToSynchronize.getObjectName());
		p_oPreparedStatement.bindLong(p_oMObjectToSynchronize.getId());

	}

	/** {@inheritDoc} */
	@Override
	protected CascadeOptim createCascadeOptim() {
		return this.cascadeOptimDefinition.createCascadeOptim();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Permet de traiter les cascades pour les méthodes 'getList()'
	 */
	@Override
	protected void postTraitementCascade(CascadeSet p_oCascadeSet, CascadeOptim p_oCascadeOptim, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 *
	 * Permet de traiter les cascades pour les méthodes 'fill()'
	 */
	@Override
	protected void postTraitementFillCascade(CascadeSet p_oCascadeSet, CascadeOptim p_oCascadeOptim, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException {
		// nothing to do
	}

}
