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

import java.util.Collection;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlInsert;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlUpdate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MParameters;

/**
 * <p>Interface de DAO : MParametersDao</p>
 *
 */
@Scope(ScopePolicy.SINGLETON)
public interface MParametersDao extends EntityDao<MParameters> {
	/**
	 * Table du Dao
	 */
	public static final String TABLE_NAME = "T_MPARAMETERS";

	/**
	 * Alias du DAO
	 */
	public static final String ALIAS_NAME = "MParameters1";

	/**
	 * Tableau de clés primaires
	 */
	@SuppressWarnings("unchecked")
	public static final PairValue<Field, SqlType>[] PK_FIELDS = new PairValue[] { new PairValue<Field, SqlType>(MParametersField.ID, SqlType.INTEGER) };

	/**
	 * Number of fields
	 */
	public static final int NB_FIELDS = 3;

	/**
	 * Renvoie un clone de la requête de sélection des entités (pour qu'elle puisse être modifiée)
	 *
	 * @return un clone de la requête de sélection des entités
	 */
	public DaoQuery getSelectDaoQuery();

	/**
	 * Renvoie un clone de la requête de comptage (pour qu'elle puisse être modifiée)
	 *
	 * @return un clone de la requête de comptage
	 */
	public DaoQuery getCountDaoQuery();

	/**
	 * Renvoie un clone de la requête d'insertion (pour qu'elle puisse être modifiée)
	 *
	 * @return un clone de la requête d'insertion
	 */
	public SqlInsert getInsertQuery();

	/**
	 * Renvoie un clone de la requête d'update (pour qu'elle puisse être modifiée)
	 *
	 * @return un clone de la requête d'update
	 */
	public SqlUpdate getUpdateQuery();

	/**
	 * Renvoie un clone de la requête de suppression (pour qu'elle puisse être modifiée)
	 *
	 * @return renvoie un clone de la requête de suppression
	 */
	public SqlDelete getDeleteQuery();

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
	public MParameters getMParameters(long p_lId, MContext p_oContext) throws DaoException;

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
	public MParameters getMParameters(long p_lId, DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException;

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
	public MParameters getMParameters(long p_lId, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException;

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
	public MParameters getMParameters(long p_lId, DaoSession p_oDaoSession, MContext p_oContext) throws DaoException;

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
			throws DaoException;

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
			throws DaoException;

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
			throws DaoException;

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
			MContext p_oContext) throws DaoException;

	/**
	 * Retourne la liste de toutes les entités MParameters.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(MContext p_oContext) throws DaoException;

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
	public List<MParameters> getListMParameters(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException;

	/**
	 * Retourne la liste de toutes les entités MParameters.
	 *
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException;

	/**
	 * Retourne la liste de toutes les entités MParameters.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(DaoSession p_oDaoSession, MContext p_oContext) throws DaoException;

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
			throws DaoException;

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
			throws DaoException;

	/**
	 * Retourne la liste de toutes les entités MParameters.
	 *
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParameters(CascadeSet p_oCascadeSet, DaoSession p_oDaoSession, MContext p_oContext)
			throws DaoException;

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
			MContext p_oContext) throws DaoException;

	/**
	 * Sauve ou met à jour l'entité MParameters passée en paramètre selon son existence en base.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateMParameters(MParameters p_oMParameters, MContext p_oContext) throws DaoException;

	/**
	 * Sauve ou met à jour l'entité MParameters passée en paramètre selon son existence en base.
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateMParameters(MParameters p_oMParameters, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException;

	/**
	 * Sauve ou met à jour laliste d'entité MParameters passée en paramètre selon leur existence en base.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateListMParameters(Collection<MParameters> p_listMParameters, MContext p_oContext) throws DaoException;

	/**
	 * Sauve ou met à jour la liste d'entité MParameters passée en paramètre selon leur existence en base.
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateListMParameters(Collection<MParameters> p_listMParameters, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException;

	/**
	 * Supprime de la base de données l'entité MParameters passée en paramètre.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMParameters(MParameters p_oMParameters, MContext p_oContext) throws DaoException;

	/**
	 * Supprime de la base de données l'entité MParameters passée en paramètre.
	 *
	 * La cascade doît être correctement indiquée.
	 *
	 * @param p_oMParameters une entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMParameters(MParameters p_oMParameters, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException;

	/**
	 * Supprime de la base de données l'entité MParameters correspondant aux paramètres.
	 *
	 * Cette suppression ne supprime pas les entités liés en cascade.
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMParameters(long p_lId, MContext p_oContext) throws DaoException;

	/**
	 * Supprime de la base de données la liste d'entité MParameters passée en paramètre.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteListMParameters(Collection<MParameters> p_listMParameters, MContext p_oContext) throws DaoException;

	/**
	 * Supprime de la base de données la liste d'entité MParameters passée en paramètre.
	 *
	 * @param p_listMParameters une liste d'entité MParameters
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteListMParameters(Collection<MParameters> p_listMParameters, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException;

	/**
	 * Retourne le nombre d'entité MParameters en base.
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_oContext contexte transactionnel
	 * @return le nombre d'entité MParameters en base
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public int getNbMParameters(MContext p_oContext) throws DaoException;

	/**
	 * Retourne le nombre d'entité MParameters selon la requête.
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oContext contexte transactionnel
	 * @return le nombre d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public int getNbMParameters(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException;
	
	/**
	 * Supprime de la base de données l'entité MParameters correspondant aux paramètres.
	 *
	 * Cette suppression ne supprime pas les entités liés en cascade.
	 *
	 * @param p_oContext contexte transactionnel
	 * @param p_oField champ sur lequel on veux filtrer la suppression
	 * @param p_oType le type du champ
	 * @param p_sValue valeur du champ
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMparametersByName(MContext p_oContext, Field p_oField, SqlType p_oType, String p_sValue) throws DaoException;
	
	/**
	 * Retourne la liste de toutes les entités MParameters correspondants aux paramètres.
	 *
	 * @param p_oContext contexte transactionnel
	 * @param p_oField champ sur lequel on veux filtrer la suppression
	 * @param p_oType le type du champ
	 * @param p_oValue valeur du champ
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @return une liste d'entité MParameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MParameters> getListMParametersByField(MContext p_oContext, Field p_oField, SqlType p_oType, Object p_oValue, CascadeSet p_oCascadeSet) throws DaoException;
	
	/**
	 * Récupération de la valeur d'un paramètre en fonction de son nom.
	 *
	 * @param p_sName Nom du paramètre à récupérer
	 * @param p_oCascadeSet Cascade à appliquer
	 * @param p_oContext Context Transactionnel
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MParameters} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public MParameters getMParametersByName(String p_sName, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException;
}
