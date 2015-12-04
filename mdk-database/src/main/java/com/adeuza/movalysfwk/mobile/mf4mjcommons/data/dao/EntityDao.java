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

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;

/**
 * <p>Interface du DAO des entités Movalys</p>
 *
 *
 * @param <T> MIEntity
 * @since 2.5
 * @see Dao
 * @see MIEntity
 */
public interface EntityDao<T extends MEntity> extends Dao {
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
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException échec de récupération
	 */
	public Collection<T> getListByIds(Collection<T> p_listEntities, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException ;
	
	/**
	 * Récupére une liste d'entité via leurs codes
	 *
	 * @param p_listEntities liste des entités avec les codes alimentés
	 * @param p_oCascadeSet cascade de récupération
	 * @param p_oDaoSession session dao
	 * @param p_oContext contexte transactionnel
	 * @return la liste des MIEntity chargées
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException échec de récupération des entity par codes
	 */
	public Collection<T> getListByCodes(Collection<T> p_listEntities, CascadeSet p_oCascadeSet, DaoSession p_oDaoSession,
			MContext p_oContext) throws DaoException ;
}
