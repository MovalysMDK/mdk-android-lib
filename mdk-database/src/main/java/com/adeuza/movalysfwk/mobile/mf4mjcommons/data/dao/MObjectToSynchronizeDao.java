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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;

//@non-generated-start[imports]
//@non-generated-end

/**
 * <p>Interface de DAO : MObjectToSynchronizeDao</p>
 *
 */
@Scope(ScopePolicy.SINGLETON)
public interface MObjectToSynchronizeDao extends EntityDao<MObjectToSynchronize> {
	/**
	 * Table du Dao
	 */
	public static final String TABLE_NAME = "T_MOBJECTTOSYNCHRONIZE";

	/**
	 * Alias du DAO
	 */
	public static final String ALIAS_NAME = "MObjectToSynchronize1";

	/**
	 * Tableau de clés primaires
	 */
	public static final FieldType[] PK_FIELDS = new FieldType[] { new FieldType(MObjectToSynchronizeField.ID,
			SqlType.INTEGER) };

	/**
	 * Number of fields
	 */
	public static final int NB_FIELDS = 5;

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
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, MContext p_oContext) throws DaoException;

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
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException;

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
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException;

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
	public MObjectToSynchronize getMObjectToSynchronize(long p_lId, DaoSession p_oDaoSession, MContext p_oContext) throws DaoException;

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
			throws DaoException;

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
			throws DaoException;

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
			MContext p_oContext) throws DaoException;

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
			MContext p_oContext) throws DaoException;

	/**
	 * Retourne la liste de toutes les entités MObjectToSynchronize.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(MContext p_oContext) throws DaoException;

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
	public List<MObjectToSynchronize> getListMObjectToSynchronize(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException;

	/**
	 * Retourne la liste de toutes les entités MObjectToSynchronize.
	 *
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(CascadeSet p_oCascadeSet, MContext p_oContext) throws DaoException;

	/**
	 * Retourne la liste de toutes les entités MObjectToSynchronize.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(DaoSession p_oDaoSession, MContext p_oContext) throws DaoException;

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
			throws DaoException;

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
			throws DaoException;

	/**
	 * Retourne la liste de toutes les entités MObjectToSynchronize.
	 *
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oDaoSession session Dao
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronize(CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException;

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
			MContext p_oContext) throws DaoException;

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
			throws DaoException;

	/**
	 * Sauve ou met à jour l'entité MObjectToSynchronize passée en paramètre selon son existence en base.
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException;

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
			throws DaoException;

	/**
	 * Sauve ou met à jour la liste d'entité MObjectToSynchronize passée en paramètre selon leur existence en base.
	 *
	 * @param p_listMObjectToSynchronize une liste d'entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void saveOrUpdateListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException;

	/**
	 * Supprime de la base de données l'entité MObjectToSynchronize passée en paramètre.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, MContext p_oContext) throws DaoException;

	/**
	 * Supprime de la base de données l'entité MObjectToSynchronize passée en paramètre.
	 *
	 * La cascade doît être correctement indiquée.
	 *
	 * @param p_oMObjectToSynchronize une entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMObjectToSynchronize(MObjectToSynchronize p_oMObjectToSynchronize, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException;

	/**
	 * Supprime de la base de données l'entité MObjectToSynchronize correspondant aux paramètres.
	 *
	 * Cette suppression ne supprime pas les entités liés en cascade.
	 *
	 * @param p_lId un paramètre de la clé primaire de type long
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteMObjectToSynchronize(long p_lId, MContext p_oContext) throws DaoException;

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
			throws DaoException;

	/**
	 * Supprime de la base de données la liste d'entité MObjectToSynchronize passée en paramètre.
	 *
	 * @param p_listMObjectToSynchronize une liste d'entité MObjectToSynchronize
	 * @param p_oCascadeSet ensemble de Cascades sur les entités
	 * @param p_oContext contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, CascadeSet p_oCascadeSet,
			MContext p_oContext) throws DaoException;

	/**
	 * Retourne le nombre d'entité MObjectToSynchronize en base.
	 *
	 * Les blocs par défaut sont utilisés
	 *
	 * @param p_oContext contexte transactionnel
	 * @return le nombre d'entité MObjectToSynchronize en base
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public int getNbMObjectToSynchronize(MContext p_oContext) throws DaoException;

	/**
	 * Retourne le nombre d'entité MObjectToSynchronize selon la requête.
	 *
	 * @param p_oDaoQuery requête
	 * @param p_oContext contexte transactionnel
	 * @return le nombre d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public int getNbMObjectToSynchronize(DaoQuery p_oDaoQuery, MContext p_oContext) throws DaoException;

	//@non-generated-start[methods]

	/**
	 * Supprime des éléments de la table T_MOBJECTTOSYNCHRONIZE, pas par identifiant mais en utilisant
	 * l'objectid et l'objectname des objets fournis en paramètre.
	 *
	 * @param p_listObjectToSynchronize La liste des objets à supprimer.
	 * @param p_oContext Le contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public void deleteListMObjectToSynchronizeByObjectIdAndObjectName(List<MObjectToSynchronize> p_listObjectToSynchronize,
			MContext p_oContext) throws DaoException;

	/**
	 * <p>
	 * 	Method that checks if there are changes to be synchronized with the Back Office.
	 * </p>
	 *
	 * @param p_oContext
	 * 				The application context.
	 * @return true if we must synchronize, false otherwise.
	 */
	public boolean isThereDataToSynchronize(MContext p_oContext);
	
	/**
	 * Retourne la liste des entités MObjectToSynchronize en fonction de l'objectId envoyé en paramètre.
	 *
	 * @param p_lObjectId identifiant de l'objet à synchroniser
	 * @param p_oContext contexte transactionnel
	 * @return une liste d'entité MObjectToSynchronize
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si une exception technique survient
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronizeByObjectId(long p_lObjectId, MContext p_oContext) throws DaoException;
	
	//@non-generated-end
}
