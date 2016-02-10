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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlInsert;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlUpdate;

/**
 * <p>
 * Interface commune des Dao
 * </p>
 *
 * @since 2.5
 */
public interface Dao {

	/**
	 * Méthode destinée à l'initialisation du DAO.
	 *
	 * @param p_oContext
	 * 				le contexte d'exécution
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 * 				déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	public void initialize(MContext p_oContext) throws DaoException;

	/**
	 * Permet de supprimer les relations 'one-to-many' obsolètes après l'enregistrement des relations de l'entité.
	 *
	 * @param p_oSqlDelete
	 *            requête de suppression
	 * @param p_oContext
	 *            le contexte d'exécution
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	public void genericDelete(SqlDelete p_oSqlDelete, MContext p_oContext) throws DaoException;

	/**
	 * Permet de mettre à jour (mise à 'null') les relations 'one-to-many' obsolètes après l'enregistrement des relations de l'entité.
	 *
	 * @param p_oSqlUpdate
	 *            requête d'update
	 * @param p_oContext
	 *            le contexte d'exécution
	 * @param p_oStatementBinderCallBack
	 *            Interface du binder des valeurs lors de la mise à jour des relations 'one-to-many' après l'enregistrement
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	public void genericUpdate(SqlUpdate p_oSqlUpdate, MContext p_oContext, StatementBinderCallBack p_oStatementBinderCallBack)
			throws DaoException;

	/**
	 * Permet d'éxécuter une requête de sélection et de traiter les données récupérées avec le callback.
	 *
	 * @param p_oDaoQuery
	 *            le requête de selection du DAO
	 * @param p_oContext
	 *            le contexte transactionnel
	 * @param p_oResultSetReader
	 *            le callback pour la lecture du resultset
	 * @return Resultat de la requête
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	public Object genericSelect(DaoQuery p_oDaoQuery, MContext p_oContext, ResultSetReaderCallBack p_oResultSetReader)
			throws DaoException;

	/**
	 * Permet d'éxécuter une requête de sélection et de traiter les données récupérées avec le callback.
	 *
	 * @param p_oDaoQuery
	 *            le requête de selection du DAO
	 * @param p_oContext
	 *            le contexte transactionnel
	 * @param p_oDaoSession
	 *            la session du DAO
	 * @param p_oResultSetReader
	 *            le callback pour la lecture du resultset
	 * @return Resultat de la requête
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	public Object genericSelect(DaoQuery p_oDaoQuery, MContext p_oContext, DaoSession p_oDaoSession,
			ResultSetReaderCallBack p_oResultSetReader) throws DaoException;

	/**
	 * Permet d'exécuter une requête d'insertion générique
	 *
	 * @param p_oSqlInsert
	 *            requête d'insertion
	 * @param p_oContext
	 *            context transactionnel
	 * @param p_oStatementBinderCallBack
	 *            CallBack pour binder les valeurs
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	public void genericInsert(SqlInsert p_oSqlInsert, MContext p_oContext, StatementBinderCallBack p_oStatementBinderCallBack)
			throws DaoException;

	/**
	 * Permet d'exécuter une requête d'insertion générique qui utilisent des valeurs générées
	 *
	 * @param p_oSqlInsert
	 *            requête d'insertion
	 * @param p_oContext
	 *            context transactionnel
	 * @param p_oStatementBinderCallBack
	 *            CallBack pour binder les valeurs
	 * @param p_sGeneratedColumns
	 *            liste des valeurs générées
	 * @param p_oGeneratedValueReaderCallBack
	 *            CallBack pour lire les valeurs générées
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	public void genericInsert(SqlInsert p_oSqlInsert, MContext p_oContext, StatementBinderCallBack p_oStatementBinderCallBack,
			String[] p_sGeneratedColumns, ResultSetReaderCallBack p_oGeneratedValueReaderCallBack) throws DaoException;
}
