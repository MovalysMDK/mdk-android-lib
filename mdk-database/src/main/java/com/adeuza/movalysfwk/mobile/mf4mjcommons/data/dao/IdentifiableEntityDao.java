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

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.IdentifiableEntity;

/**
 * <p>Global DAO interface used for identifiable entities persistence</p>
 * 
 * @param <T> class of the entity
 *
 */
public interface IdentifiableEntityDao<T extends IdentifiableEntity> extends EntityDao<T> {

	/**
	 * Met à jour, en base de données, l'identifiant de chaque élément de la liste fournie en paramètre.
	 * Le précédent identifiant est fourni par la méthode getOldId de chaque élément de la liste,
	 * Le nouvelle identifiant est fourni par la méthode getId.
	 *
	 * @param p_listEntities Liste des éléments dont l'identifiant doit être mise à jour en base de données.
	 * @param p_oContext Contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException En cas d'erreur lors de l'exécution de la requête SQL.
	 */
	public void updateIdentifier(List<T> p_listEntities, MContext p_oContext) throws DaoException;
}
