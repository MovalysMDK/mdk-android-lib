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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans;

import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;

/**
 * <p>Cache sur un type d'entité</p>
 *
 *
 */
public class EntityCache {
	
	/**
	 * Entrées du cache
	 */
	private Map<String,MEntity> cacheEntries;

	/**
	 * Constructeur
	 *
	 * @param p_sEntityName nom de l'entité(nom du cache)
	 */
	protected EntityCache(String p_sEntityName) {
		this.cacheEntries = new HashMap<String,MEntity>();
	}

	/**
	 * Ajoute une entité dans le cache
	 *
	 * @param p_sKey clé de cache
	 * @param p_oEntity l'entité à ajouter
	 */
	protected void addToCache(String p_sKey, MEntity p_oEntity) {
		this.cacheEntries.put(p_sKey, p_oEntity);
	}

	/**
	 * Récupère un entité via sa clé de cache
	 *
	 * @param p_sKey clé de cache
	 * @return l'entité si trouvée
	 */
	protected Object getFromCache(String p_sKey) {
		return this.cacheEntries.get(p_sKey);
	}
}
