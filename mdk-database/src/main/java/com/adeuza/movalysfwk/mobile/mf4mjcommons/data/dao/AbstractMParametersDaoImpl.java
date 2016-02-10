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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Initializable;
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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsValueCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlFunctionSelectPart;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.factory.MParametersFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MParameters;

/**
 *
 * <p>Classe de DAO : AbstractMParametersDaoImpl</p>
 *
 */
public abstract class AbstractMParametersDaoImpl extends AbstractEntityDao<MParameters> implements Initializable {

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
	 * Factory MParametersFactory
	 */
	protected MParametersFactory MParametersFactory;

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
		this.MParametersFactory = BeanLoader.getInstance().getBean(MParametersFactory.class);

		init();

		cascadeOptimDefinition = new CascadeOptimDefinition(MParametersDao.PK_FIELDS, MParametersDao.ALIAS_NAME);

	}
	
	/**
	 * initializes static objects
	 */
	private static void init() {
		selectQuery = new SqlQuery();
		selectQuery.addFieldToSelect(MParametersDao.ALIAS_NAME, MParametersField.ID, MParametersField.NAME, MParametersField.VALUE);
		selectQuery.addToFrom(MParametersDao.TABLE_NAME, MParametersDao.ALIAS_NAME);

		countQuery = new SqlQuery();
		countQuery.addCountToSelect(MParametersField.ID, MParametersDao.ALIAS_NAME);
		countQuery.addToFrom(MParametersDao.TABLE_NAME, MParametersDao.ALIAS_NAME);

		insertQuery = new SqlInsert(MParametersDao.TABLE_NAME);

		insertQuery.addBindedField(MParametersField.ID);
		insertQuery.addBindedField(MParametersField.NAME);
		insertQuery.addBindedField(MParametersField.VALUE);

		updateQuery = new SqlUpdate(MParametersDao.TABLE_NAME);

		updateQuery.addBindedField(MParametersField.ID);
		updateQuery.addBindedField(MParametersField.NAME);
		updateQuery.addBindedField(MParametersField.VALUE);

		deleteQuery = new SqlDelete(MParametersDao.TABLE_NAME);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Initializes the private attributes of this DAO: factories and daos use by this dao.
	 */
	@Override
	public void initialize(MContext p_oContext) throws DaoException {
		SqlQuery oSelect = new SqlQuery();
		oSelect.addFunctionToSelect(new SqlFunctionSelectPart(SqlFunction.MIN, MParametersField.ID, MParametersDao.ALIAS_NAME));
		oSelect.addToFrom(MParametersDao.TABLE_NAME, MParametersDao.ALIAS_NAME);
		this.lastNewId = (Long) this.genericSelect(new DaoQueryImpl(oSelect, this.getEntityName()), p_oContext, new ResultSetReaderCallBack() {
			@Override
			public Object doRead(ResultSet p_oResultSet) throws SQLException, DaoException {
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
		return MParametersDao.TABLE_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected String getAliasName() {
		return MParametersDao.ALIAS_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected String getEntityName() {
		return MParameters.ENTITY_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected PairValue<Field, SqlType>[] getPKFields() {
		return MParametersDao.PK_FIELDS;
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
	 * Retourne une entité MParameters selon la clé primaire
	 *
	 * Les blocs par défaut sont utilisés
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oContext contexte transactionnel
	 * @return une entité MParameters selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MParameters getMParameters(long p_lId, MContext p_oContext) throws DaoException {
		return this.getMParameters(p_lId, this.getSelectDaoQuery(), CascadeSet.NONE, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne une entité MParameters selon la clé primaire
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoQuery requête
	 * @param p_oContext contexte transactionnel
	 * @return une entité MParameters selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MParameters getMParameters(long p_lId, DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException {
		return this.getMParameters(p_lId, p_oDaoQuery, CascadeSet.NONE, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne une entité MParameters selon la clé primaire
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une entité MParameters selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MParameters getMParameters(long p_lId, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {
		return this.getMParameters(p_lId, this.getSelectDaoQuery(), p_oCascadeSet, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne une entité MParameters selon la clé primaire
	 *
	 * Les blocs par défaut sont utilisés
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une entité MParameters selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MParameters getMParameters(long p_lId, DaoSession p_oDaoSession, MContext p_oContext) throws DaoException {
		return this.getMParameters(p_lId, this.getSelectDaoQuery(), CascadeSet.NONE, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne une entité MParameters selon la clé primaire
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoQuery requête
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une entité MParameters selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MParameters getMParameters(long p_lId, DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		return this.getMParameters(p_lId, p_oDaoQuery, p_oCascadeSet, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne une entité MParameters selon la clé primaire
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoQuery requête
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une entité MParameters selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MParameters getMParameters(long p_lId, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {
		return this.getMParameters(p_lId, p_oDaoQuery, CascadeSet.NONE, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne une entité MParameters selon la clé primaire
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une entité MParameters selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MParameters getMParameters(long p_lId, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {
		return this.getMParameters(p_lId, this.getSelectDaoQuery(), p_oCascadeSet, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne une entité MParameters selon la clé primaire
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oDaoQuery requête
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une entité MParameters selon la clé primaire
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public MParameters getMParameters(long p_lId, DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException {
		MParameters r_oMParameters = null;
		try {
			p_oDaoQuery.getSqlQuery().addEqualsConditionToWhere(MParametersField.ID, MParametersDao.ALIAS_NAME, p_lId, SqlType.INTEGER);
			PreparedStatement oStatement = p_oDaoQuery.prepareStatement(p_oContext);

			try {
				p_oDaoQuery.bindValues(oStatement);
				ResultSetReader oResultSetReader = new ResultSetReader(oStatement.executeQuery());
				try {
					while (oResultSetReader.next()) {
						r_oMParameters = this.valueObject(oResultSetReader, p_oDaoQuery, p_oDaoSession, p_oCascadeSet, p_oContext);
					}
				} finally {
					oResultSetReader.close();
				}
			} finally {
				oStatement.close();
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		} catch (IOException e) {
			throw new DaoException(e);
		}
		return r_oMParameters;
	}

	/**
	 * Retourne la liste de toutes les entités MParameters.
	 *
	 * Les blocs par défaut sont utilisés
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(MContext p_oContext) throws DaoException {
		return this.getListMParameters(getSelectDaoQuery(), CascadeSet.NONE, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne la liste des entités MParameters selon la requête.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException {
		return this.getListMParameters(p_oDaoQuery, CascadeSet.NONE, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne la liste de toutes les entités MParameters.
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {
		return this.getListMParameters(getSelectDaoQuery(), p_oCascadeSet, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne la liste de toutes les entités MParameters.
	 *
	 * Les blocs par défaut sont utilisés
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(DaoSession p_oDaoSession, MContext p_oContext) throws DaoException {
		return this.getListMParameters(getSelectDaoQuery(), CascadeSet.NONE, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne la liste des entités MParameters selon la requête.
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		return this.getListMParameters(p_oDaoQuery, p_oCascadeSet, new DaoSession(), p_oContext);
	}

	/**
	 * Retourne la liste des entités MParameters selon la requête.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {
		return this.getListMParameters(p_oDaoQuery, CascadeSet.NONE, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne la liste de toutes les entités MParameters.
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(CascadeSet p_oCascadeSet, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {
		return this.getListMParameters(getSelectDaoQuery(), p_oCascadeSet, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne la liste des entités MParameters selon la requête.
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException {

		return this.getList(p_oDaoQuery, p_oCascadeSet, p_oDaoSession, p_oContext);
	}

	/**
	 * Sauve ou met à jour l'entité MParameters passée en paramètre selon son existence en base.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateMParameters(MParameters p_oMParameters, MContext p_oContext) throws DaoException {
		this.saveOrUpdateMParameters(p_oMParameters, CascadeSet.NONE, p_oContext);
	}

	/**
	 * Sauve ou met à jour l'entité MParameters passée en paramètre selon son existence en base.
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateMParameters(MParameters p_oMParameters, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {

		this.validateBean(p_oMParameters, p_oContext);
		boolean bHaveErrorMessage = p_oContext.getMessages().hasErrors();

		if (!bHaveErrorMessage) {

			if (this.exist(p_oMParameters, p_oCascadeSet, p_oContext)) {
				this.updateMParameters(p_oMParameters, p_oCascadeSet, p_oContext);
			} else {
				if (p_oMParameters.getId() < 0L) {
					p_oMParameters.setId(this.nextId());
				}
				this.saveMParameters(p_oMParameters, p_oCascadeSet, p_oContext);
			}

		}
	}

	/**
	 * Sauve ou met à jour laliste d'entité MParameters passée en paramètre selon leur existence en base.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateListMParameters(Collection<MParameters> p_listMParameters, MContext p_oContext) throws DaoException {
		this.saveOrUpdateListMParameters(p_listMParameters, CascadeSet.NONE, p_oContext);
	}

	/**
	 * Sauve ou met à jour laliste d'entité MParameters passée en paramètre selon leur existence en base.
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateListMParameters(Collection<MParameters> p_listMParameters, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		
		this.validateBeanList(p_listMParameters, p_oContext);
		boolean bHaveErrorMessage = p_oContext.getMessages().hasErrors();

		if (!bHaveErrorMessage) {
			List<MParameters> listSave = new ArrayList<MParameters>();
			List<MParameters> listUpdate = new ArrayList<MParameters>();
			for (MParameters oMParameters : p_listMParameters) {
				if (this.exist(oMParameters, p_oCascadeSet, p_oContext)) {
					listUpdate.add(oMParameters);
				} else {
					if (oMParameters.getId() < 0L ) {
						oMParameters.setId(this.nextId());
					}
					listSave.add(oMParameters);
				}
			}

			if (!listSave.isEmpty()) {
				this.saveListMParameters(listSave, p_oCascadeSet, p_oContext);
			}
			if (!listUpdate.isEmpty()) {
				this.updateListMParameters(listUpdate, p_oCascadeSet, p_oContext);
			}
		}
	}

	/**
	 * Supprime l'entité MParameters passée en paramètre de la base de données.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMParameters(MParameters p_oMParameters, MContext p_oContext) throws DaoException {
		this.deleteMParameters(p_oMParameters, CascadeSet.NONE, p_oContext);
	}

	/**
	 * Supprime l'entité MParameters passée en paramètre de la base de données.
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMParameters(MParameters p_oMParameters, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {

		try {

			SqlDelete oSqlDelete = getDeleteQuery();
			oSqlDelete.addEqualsConditionToWhere(MParametersField.ID, p_oMParameters.getId(), SqlType.INTEGER);

			Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
			PreparedStatement oStatement = oConnection.prepareStatement(oSqlDelete.toSql(p_oContext));
			try {
				oSqlDelete.bindValues(oStatement);
				oStatement.executeUpdate();
			} finally {
				oStatement.close();
			}

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Supprime l'entité MParameters correspondant aux paramètres de la base de données.
	 *
	 * Cette suppression ne supprime pas les entités liés en cascade.
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMParameters(long p_lId, MContext p_oContext) throws DaoException {

		try {
			SqlDelete oSqlDelete = getDeleteQuery();
			oSqlDelete.addEqualsConditionToWhere(MParametersField.ID, p_lId, SqlType.INTEGER);
			Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
			PreparedStatement oStatement = oConnection.prepareStatement(oSqlDelete.toSql(p_oContext));
			try {
				oSqlDelete.bindValues(oStatement);
				oStatement.executeUpdate();
			} finally {
				oStatement.close();
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Supprime de la base de données la liste d'entité MParameters passée en paramètre.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteListMParameters(Collection<MParameters> p_listMParameters, MContext p_oContext) throws DaoException {
		this.deleteListMParameters(p_listMParameters, CascadeSet.NONE, p_oContext);
	}

	/**
	 * Supprime de la base de données la liste d'entité MParameters passée en paramètre.
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteListMParameters(Collection<MParameters> p_listMParameters, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {

		if (!p_listMParameters.isEmpty()) {
			try {

				SqlDelete oSqlDelete = getDeleteQuery();
				SqlEqualsValueCondition oSqlEqualsValueCondition1 = oSqlDelete.addEqualsConditionToWhere(MParametersField.ID, SqlType.INTEGER);

				Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
				PreparedStatement oStatement = oConnection.prepareStatement(oSqlDelete.toSql(p_oContext));
				try {
					for (MParameters oMParameters : p_listMParameters) {
						oSqlEqualsValueCondition1.setValue(oMParameters.getId());

						oSqlDelete.bindValues(oStatement);
						oStatement.addBatch();
					}

					oStatement.executeBatch();
				} finally {
					oStatement.close();
				}

			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
	}

	/**
	 * Retourne le nombre d'entité MParameters en base.
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_oContext contexte transactionnel
	 * @return le nombre d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public int getNbMParameters(MContext p_oContext) throws DaoException {
		return this.getNbMParameters(getCountDaoQuery(), p_oContext);
	}

	/**
	 * Retourne le nombre d'entité MParameters selon la requête.
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oContext contexte transactionnel
	 * @return le nombre d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public int getNbMParameters(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException {
		return this.getNb(p_oDaoQuery, p_oContext);
	}

	/**
	 * <p>saveMParameters.</p>
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected void saveMParameters(MParameters p_oMParameters, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {

		try {

			if (!p_oContext.getMessages().hasErrors()) {
				SqlInsert oSqlInsert = this.getInsertQuery();
				Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
				String sSql = oSqlInsert.toSql(p_oContext);
				PreparedStatement oStatement = oConnection.prepareStatement(sSql);
				try {
					bindInsert(p_oMParameters, oStatement, p_oContext);
					oStatement.executeUpdate();
				} finally {
					oStatement.close();
				}
			}

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * <p>saveListMParameters.</p>
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected void saveListMParameters(Collection<MParameters> p_listMParameters, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {

		try {

			if (!p_oContext.getMessages().hasErrors()) {
				SqlInsert oSqlInsert = this.getInsertQuery();
				Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
				PreparedStatement oStatement = oConnection.prepareStatement(oSqlInsert.toSql(p_oContext));
				try {
					for (MParameters oMParameters : p_listMParameters) {
						bindInsert(oMParameters, oStatement, p_oContext);
						oStatement.executeUpdate();
					}

				} finally {
					oStatement.close();
				}
			}

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * <p>updateMParameters.</p>
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected void updateMParameters(MParameters p_oMParameters, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {

		try {

			if (!p_oContext.getMessages().hasErrors()) {

				SqlUpdate oSqlUpdate = this.getUpdateQuery();
				oSqlUpdate.addEqualsConditionToWhere(MParametersField.ID, SqlType.INTEGER);
				Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
				PreparedStatement oStatement = oConnection.prepareStatement(oSqlUpdate.toSql(p_oContext));
				try {
					bindUpdate(p_oMParameters, oStatement, p_oContext);
					oStatement.executeUpdate();
				} finally {
					oStatement.close();
				}
			}

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * <p>updateListMParameters.</p>
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected void updateListMParameters(Collection<MParameters> p_listMParameters, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {

		try {

			if (!p_oContext.getMessages().hasErrors()) {

				SqlUpdate oSqlUpdate = this.getUpdateQuery();
				oSqlUpdate.addEqualsConditionToWhere(MParametersField.ID, SqlType.INTEGER);
				Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
				PreparedStatement oStatement = oConnection.prepareStatement(oSqlUpdate.toSql(p_oContext));
				try {
					for (MParameters oMParameters : p_listMParameters) {
						bindUpdate(oMParameters, oStatement, p_oContext);
						oStatement.addBatch();
					}
					oStatement.executeBatch();
				} finally {
					oStatement.close();
				}
			}

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * <p>exist.</p>
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return un boolean indiquant si l'entité existe en base
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	protected boolean exist(MParameters p_oMParameters, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException {
		boolean r_bExist = false;
		try {
			DaoQuery oQuery = this.getSelectDaoQuery();
			oQuery.getSqlQuery().addEqualsConditionToWhere(MParametersField.ID, MParametersDao.ALIAS_NAME, p_oMParameters.getId(), SqlType.INTEGER);
			PreparedStatement oStatement = oQuery.prepareStatement(p_oContext);

			try {
				oQuery.bindValues(oStatement);
				ResultSet oResultSet = oStatement.executeQuery();
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
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return r_bExist;
	}

	/** {@inheritDoc} */
	@Override
	protected MParameters valueObject(ResultSetReader p_oResultSetReader, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws SQLException, DaoException, IOException {

		MParameters r_oMParameters = this.MParametersFactory.createInstance();
		r_oMParameters.setId(p_oResultSetReader.getLong());
		String sEntityId = r_oMParameters.idToString();
		MParameters oCachedMParameters = (MParameters) p_oDaoSession.getFromCache(MParameters.ENTITY_NAME, sEntityId);
		if (oCachedMParameters == null) {
			p_oDaoSession.addToCache(MParameters.ENTITY_NAME, sEntityId, r_oMParameters);
			r_oMParameters.setName(p_oResultSetReader.getString());
			r_oMParameters.setValue(p_oResultSetReader.getString());
		} else {
			r_oMParameters = oCachedMParameters;
		}

		p_oDaoQuery.doResultSetCustomRead(r_oMParameters, p_oResultSetReader, p_oDaoSession, p_oCascadeSet);

		return r_oMParameters;
	}

	/** {@inheritDoc} */
	@Override
	protected MParameters valueObject(ResultSetReader p_oResultSetReader, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, CascadeSet p_oCascadeSet,
			CascadeOptim p_oCascadeOptim, MContext p_oContext) throws SQLException, IOException {

		MParameters r_oMParameters = this.MParametersFactory.createInstance();

		r_oMParameters.setId(p_oResultSetReader.getLong());

		String sEntityId = r_oMParameters.idToString();
		MParameters oCachedMParameters = (MParameters) p_oDaoSession.getFromCache(MParameters.ENTITY_NAME, sEntityId);
		if (oCachedMParameters == null) {
			p_oDaoSession.addToCache(MParameters.ENTITY_NAME, sEntityId, r_oMParameters);
			p_oCascadeOptim.registerEntity(sEntityId, r_oMParameters, r_oMParameters.getId());
			r_oMParameters.setName(p_oResultSetReader.getString());
			r_oMParameters.setValue(p_oResultSetReader.getString());
		} else {
			r_oMParameters = oCachedMParameters;
		}

		p_oDaoQuery.doResultSetCustomRead(r_oMParameters, p_oResultSetReader, p_oDaoSession, p_oCascadeSet);

		return r_oMParameters;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind un prepareStatement d'insertion
	 */
	@Override
	protected void bindInsert(MParameters p_oMParameters, PreparedStatement p_oPreparedStatement, MContext p_oContext)
			throws DaoException, SQLException {

		StatementBinder oStatementBinder = new StatementBinder(p_oPreparedStatement);
		oStatementBinder.bindLong(p_oMParameters.getId());
		oStatementBinder.bindString(p_oMParameters.getName());
		oStatementBinder.bindString(p_oMParameters.getValue());

	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind un preparedStatement de mise à jour
	 */
	@Override
	protected void bindUpdate(MParameters p_oMParameters, PreparedStatement p_oPreparedStatement, MContext p_oContext)
			throws DaoException, SQLException {

		StatementBinder oStatementBinder = new StatementBinder(p_oPreparedStatement);
		oStatementBinder.bindLong(p_oMParameters.getId());
		oStatementBinder.bindString(p_oMParameters.getName());
		oStatementBinder.bindString(p_oMParameters.getValue());
		oStatementBinder.bindLong(p_oMParameters.getId());

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
