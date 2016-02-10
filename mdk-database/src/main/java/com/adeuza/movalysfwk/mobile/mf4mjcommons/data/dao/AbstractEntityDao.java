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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CodableEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.IdentifiableEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlInsert;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlUpdate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInValueCondition;

/**
 * <p>
 * Dao Abstrait pour les MIEntity
 * </p>
 *
 * @param <T>
 *            MIEntity
 */
public abstract class AbstractEntityDao<T extends MEntity> extends AbstractDao {
	
	/**
	 * Constante pour la valeur par défaut de lastNewId
	 */
	protected static final long UNSAVED_VALUE = -1;

	/**
	 * Constante pour l'admCreationKey par défaut
	 */
	private static final String ADM_CREATIONKEY = "dao";

	/**
	 * Méthode de chargement par Ids
	 */
	protected static final String METHOD_GETLIST_BY_IDS = "getListByIds";

	/**
	 * Méthode de chargement par code
	 */
	protected static final String METHOD_GETLIST_BY_CODES = "getListByCodes";

	/**
	 * Méthode de chargement par référence
	 */
	protected static final String METHOD_GETLIST_BY_REFERENCES = "getListByReferences";

	/**
	 * Méthode de chargement par Ids
	 */
	protected static final String METHOD_FILL_BY_IDS = "fillByIds";

	/**
	 * Méthode de chargement par code
	 */
	protected static final String METHOD_FILL_BY_CODES = "fillByCodes";

	/**
	 * Méthode de chargement par référence
	 */
	protected static final String METHOD_FILL_BY_REFERENCES = "fillByReferences";

	/**
	 * Objet utilisé pour bloquer les accès concurrents aux attributs
	 */
	protected final Object lock = new Object();
	
	/**
	 * La valeur de l'id courant.
	 */
	protected Long lastNewId = UNSAVED_VALUE;
	

//	/**
//	 * Helper pour les Dao
//	 */
//	@BeanAutowired
//	protected DaoAccessHelper daoHelper;

	/**
	 * Retourne le nom de l'entity
	 *
	 * @return le nom de l'entity
	 */
	protected abstract String getEntityName();

	/**
	 * Retourne le nom de l'alias
	 *
	 * @return le nom de l'alias
	 */
	protected abstract String getAliasName();

	/**
	 * Retourne les champs de la clé primaire
	 *
	 * @return les champs de la clé primaire
	 */
	protected abstract PairValue<Field, SqlType>[] getPKFields();

	/**
	 * Retourne la requête de sélection
	 *
	 * @return la requête de sélection
	 */
	public abstract DaoQuery getSelectDaoQuery();

	/**
	 * Retourne la requête SELECT de comptage
	 *
	 * @return la requête SELECT de comptage
	 */
	public abstract DaoQuery getCountDaoQuery();

	/**
	 * Retourne la requête d'insertion.
	 *
	 * @return la requête d'insertion.
	 */
	public abstract SqlInsert getInsertQuery();

	/**
	 * Retourne la requête de mise à jour
	 *
	 * @return la requête de mise à jour
	 */
	public abstract SqlUpdate getUpdateQuery();

	/**
	 * Retourne la requête de suppression
	 *
	 * @return la requête de suppression
	 */
	public abstract SqlDelete getDeleteQuery();

	/**
	 * Value un entity à partir du resultset dans le cas d'un chargement optimisé
	 *
	 * @param p_oDaoQuery
	 *            query
	 * @param p_oDaoSession
	 *            session dao
	 * @param p_oCascadeSet
	 *            cascade de récupération
	 * @param p_oCascadeOptim
	 *            gestion des cascades
	 * @return Entity chargé
	 * @throws java.sql.SQLException
	 *             erreur SQL
	 * @throws java.io.IOException
	 *             erreur IO
	 * @param p_oResultSetReader a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader} object.
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 */
	protected abstract T valueObject(ResultSetReader p_oResultSetReader, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, CascadeSet p_oCascadeSet,
			CascadeOptim p_oCascadeOptim, MContext p_oContext) throws SQLException, IOException;

	/**
	 * Value un entity à partir du resultset dans le cas d'un chargement simple
	 *
	 * @param p_oDaoQuery
	 *            query
	 * @param p_oDaoSession
	 *            session dao
	 * @param p_oCascadeSet
	 *            cascade de récupération
	 * @param p_oContext
	 *            gestion des cascades
	 * @return Entity chargé
	 * @throws java.sql.SQLException
	 *             erreur SQL
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             erreur com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 * @throws java.io.IOException
	 *             erreur IO
	 * @param p_oResultSetReader a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader} object.
	 */
	protected abstract T valueObject(ResultSetReader p_oResultSetReader, DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws SQLException, DaoException, IOException;

	/**
	 * Bind un prepareStatement d'insertion
	 *
	 * @param p_oEntity
	 *            l'entity à insérer
	 * @param p_oPreparedStatement
	 *            le statement à binder
	 * @param p_oContext
	 *            le context transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             erreur com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 * @throws java.sql.SQLException
	 *             erreur SQL
	 */
	protected abstract void bindInsert(T p_oEntity, PreparedStatement p_oPreparedStatement, MContext p_oContext) throws DaoException,
			SQLException;

	/**
	 * Bind un preparedStatement de mise à jour
	 *
	 * @param p_oEntity
	 *            l'entity à mettre à jour
	 * @param p_oPreparedStatement
	 *            le statement à binder
	 * @param p_oContext
	 *            le context transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             erreur com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 * @throws java.sql.SQLException
	 *             erreur SQL
	 */
	protected abstract void bindUpdate(T p_oEntity, PreparedStatement p_oPreparedStatement, MContext p_oContext) throws DaoException,
			SQLException;

	/**
	 * Réalise le pré-traitement de récupération par liste en mode optimisé
	 *
	 * @return configuration des cascades dans CascadeOptim
	 */
	protected abstract CascadeOptim createCascadeOptim();

	/**
	 * Réalise le post-traitement de récupération par liste en mode optimisé
	 *
	 * @param p_oCascadeSet
	 *            cascade récupération
	 * @param p_oCascadeOptim
	 *            gestion des cascades
	 * @param p_oDaoQuery
	 *            la query
	 * @param p_oDaoSession
	 *            la session dao
	 * @param p_oContext
	 *            le contexte
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             échec du post traitement
	 */
	protected abstract void postTraitementCascade(CascadeSet p_oCascadeSet, CascadeOptim p_oCascadeOptim, DaoQuery p_oDaoQuery,
			DaoSession p_oDaoSession, MContext p_oContext) throws DaoException;

	/**
	 * Réalise le post-traitement de récupération par liste en mode optimisé
	 *
	 * @param p_oCascadeSet
	 *            cascade récupération
	 * @param p_oCascadeOptim
	 *            gestion des cascades
	 * @param p_oDaoQuery
	 *            la query
	 * @param p_oDaoSession
	 *            la session dao
	 * @param p_oContext
	 *            le contexte
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             échec du post traitement
	 */
	protected abstract void postTraitementFillCascade(CascadeSet p_oCascadeSet, CascadeOptim p_oCascadeOptim, DaoQuery p_oDaoQuery,
			DaoSession p_oDaoSession, MContext p_oContext) throws DaoException;

	/**
	 * Récupére une liste d'entité via leurs ids
	 *
	 * @param p_listEntities
	 *            liste des entités à charger avec leurs ids alimentés
	 * @param p_oCascadeSet
	 *            cascade de chargement
	 * @param p_oDaoSession
	 *            session dao
	 * @param p_oContext
	 *            contexte d'intégration
	 * @return la liste des MIEntity chargées
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             échec de récupération
	 */
	public Collection<T> getListByIds(Collection<T> p_listEntities, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {

		DaoQuery oQuery = this.getSelectDaoQuery();

		List<Long> listValues = new ArrayList<Long>();
		for (T oObject : p_listEntities) {
			listValues.add(((IdentifiableEntity) oObject).getId());
		}

		SqlInValueCondition oCondition = new SqlInValueCondition(getPKFields(), getAliasName(), listValues);
		oQuery.getSqlQuery().addToWhere(oCondition);

		return this.getList(oQuery, p_oCascadeSet, p_oDaoSession, p_oContext);
	}

	/**
	 * Récupére une liste d'entité via leurs codes
	 *
	 * @param p_listEntities
	 *            liste des entités avec les codes alimentés
	 * @param p_oCascadeSet
	 *            cascade de récupération
	 * @param p_oDaoSession
	 *            session dao
	 * @param p_oContext
	 *            contexte transactionnel
	 * @return la liste des MIEntity chargées
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             échec de récupération des entity par codes
	 */
	public Collection<T> getListByCodes(Collection<T> p_listEntities, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {
		DaoQuery oQuery = this.getSelectDaoQuery();
		List<String> listValues = new ArrayList<String>();
		for (T oObject : p_listEntities) {
			listValues.add(((CodableEntity) oObject).getCode());
		}

		SqlInValueCondition oCondition = new SqlInValueCondition("CODE", getAliasName(), SqlType.VARCHAR, listValues);
		oQuery.getSqlQuery().addToWhere(oCondition);

		return this.getList(oQuery, p_oCascadeSet, p_oDaoSession, p_oContext);
	}

	/**
	 * Retourne la liste des entités de la requête
	 *
	 * @param p_oDaoQuery
	 *            requête de sélection
	 * @param p_oCascadeSet
	 *            cascade de chargement
	 * @param p_oDaoSession
	 *            session dao
	 * @param p_oContext
	 *            contexte transactionnel
	 * @return Retourne la liste des entités de la requête
	 * @exception DaoException
	 *                échec de chargement
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	protected List<T> getList(DaoQuery p_oDaoQuery, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException {

		List<T> r_listEntities = new ArrayList<T>();

		try {
			boolean bIsComplete = this.completeQueryForList(p_oDaoQuery);
			if (bIsComplete) {
				PreparedStatement oStatement = p_oDaoQuery.prepareStatement(p_oContext);
				CascadeOptim oCascadeOptim = this.createCascadeOptim();
				try {
					p_oDaoQuery.bindValues(oStatement);
					ResultSetReader oResultSetReader = new ResultSetReader(oStatement.executeQuery());
					try {
						T oEntity = null;
						while (oResultSetReader.next()) {
							oEntity = this.valueObject(oResultSetReader, p_oDaoQuery, p_oDaoSession, p_oCascadeSet, oCascadeOptim, p_oContext);
							if (!r_listEntities.contains(oEntity)) {
								r_listEntities.add(oEntity);
							}
						}
					}
					finally {
						oResultSetReader.close();
					}
				}
				finally {
					oStatement.close();
				}

				if (!r_listEntities.isEmpty()) {
					this.postTraitementCascade(p_oCascadeSet, oCascadeOptim, p_oDaoQuery, p_oDaoSession, p_oContext);
				}
			}
		}
		catch (SQLException e) {
			throw new DaoException(e);
		}
		catch (IOException e) {
			throw new DaoException(e);
		}

		return r_listEntities;
	}

	/**
	 * Retourne la liste des identifiants des entités de la requête
	 *
	 * @param p_oDaoQuery
	 *            requête de sélection
	 * @param p_oDaoSession
	 *            session dao
	 * @param p_oContext
	 *            contexte transactionnel
	 * @return la liste des identifiants des entités de la requête
	 * @exception DaoException
	 *                échec de chargement
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	protected List<Long> getListId(DaoQuery p_oDaoQuery, DaoSession p_oDaoSession, MContext p_oContext) throws DaoException {

		List<Long> r_listEntitiesId = new ArrayList<Long>();

		try {
			boolean bIsComplete = this.completeQueryForList(p_oDaoQuery);
			if (bIsComplete) {
				PreparedStatement oStatement = p_oDaoQuery.prepareStatement(p_oContext);
				try {
					p_oDaoQuery.bindValues(oStatement);
					ResultSet oResultSet = oStatement.executeQuery();
					try {
						while (oResultSet.next()) {
							r_listEntitiesId.add(oResultSet.getLong(1));
						}

					}
					finally {
						oResultSet.close();
					}
				}
				finally {
					oStatement.close();
				}
			}
		}
		catch (SQLException oException) {
			throw new DaoException(oException);
		}
		return r_listEntitiesId;
	}

	/**
	 * Valide l'entité passé en paramètre. Les messages d'erreurs résultant de la validation sont ajoutés à la liste des messages du contexte.
	 *
	 * @param p_oMIEntity entité
	 * @param p_oContext contexte de travail
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException Exception liée aux accès à la base
	 */
	protected void validateBean(T p_oMIEntity, MContext p_oContext) throws DaoException {
		// NOTHING
	}

	/**
	 * Valide la liste d'entité passé en paramètre. Les messages d'erreurs résultant de la validation sont ajoutés à la liste des messages du
	 * contexte.
	 *
	 * @param p_oMIEntityList Entité
	 * @param p_oContext Contexte de travail
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException Exception liée aux accès à la base
	 */
	protected void validateBeanList(Collection<T> p_oMIEntityList, MContext p_oContext) throws DaoException {
		// NOTHING
	}

	/**
	 * Retourne le nombre calculé par l'éxécution de la requête
	 *
	 * @param p_oDaoQuery
	 *            requête de sélection
	 * @param p_oContext
	 *            contexte transactionnel
	 * @return le nombre calculé par l'éxécution de la requête
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             échec de requête de sélection
	 */
	public int getNb(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException {
		int r_iResult = 0;
		try {
			boolean bIsComplete = this.completeQueryForList(p_oDaoQuery);
			if (bIsComplete) {
				PreparedStatement oStatement = p_oDaoQuery.prepareStatement(p_oContext);
				try {
					p_oDaoQuery.bindValues(oStatement);
					ResultSet oResultSet = oStatement.executeQuery();
					try {
						while (oResultSet.next()) {
							r_iResult = r_iResult + oResultSet.getInt(1);
						}
					} finally {
						oResultSet.close();
					}
				} finally {
					oStatement.close();
				}
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return r_iResult;
	}
	
	/**
	 * Compute the next identifier
	 *
	 * @return a long.
	 */
	protected long nextId() {
		synchronized (this.lock) {
			return --this.lastNewId;
		}
	}
	
	/**
	 * Get the current identifier
	 *
	 * @return the current value of lastNewId
	 */
	protected long getCurrentId() {
		return this.lastNewId;
	}
	
}
