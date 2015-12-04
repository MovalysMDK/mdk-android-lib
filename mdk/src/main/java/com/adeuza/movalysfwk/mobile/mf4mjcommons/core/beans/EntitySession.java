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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;

/**
 * <p>Représente une session de travail sur les entity</p>
 * <p>Permet notamment de gérer un cache sur différents types d'entités</p>
 *
 *
 */
public class EntitySession {

	/**
	 * Cache : type d'entité => Cache d'entités
	 */
	private Map<String,EntityCache> sessionCache;

	/**
	 * Entities already saved in database (no need to resave )
	 */
	private Map<String,List<String>> savedEntities = new HashMap<String,List<String>>();
	
	/**
	 * Constructeur
	 */
	public EntitySession() {
		this.sessionCache = new HashMap<String,EntityCache>();
	}

	/**
	 * Ajoute une entité dans le cache
	 *
	 * @param p_sEntityName type d'entité
	 * @param p_sKey clé de l'entité
	 * @param p_oEntity l'entité à ajouter dans le cache
	 */
	public void addToCache(String p_sEntityName, String p_sKey, MEntity p_oEntity) {
		EntityCache oEntityCache = this.sessionCache.get(p_sEntityName);

		// Crée le cache s'il n'existe pas
		if (oEntityCache == null) {
			oEntityCache = new EntityCache(p_sEntityName);
			this.sessionCache.put(p_sEntityName, oEntityCache);
		}

		// Ajoute l'élément au cache
		oEntityCache.addToCache(p_sKey, p_oEntity);
	}

	/**
	 * Récupère dans le cache
	 *
	 * @param p_sEntityName type d'entité
	 * @param p_sKey clé de cache
	 * @return l'entité cachée correspondante
	 */
	public Object getFromCache(String p_sEntityName, String p_sKey) {
		Object r_oResult = null;
		EntityCache oEntityCache = this.sessionCache.get(p_sEntityName);

		if (oEntityCache != null) {
			r_oResult = oEntityCache.getFromCache(p_sKey);
		}
		return r_oResult;
	}
	
	/**
	 * <p>markAsSaved.</p>
	 *
	 * @param p_sEntityName a {@link java.lang.String} object.
	 * @param p_oEntity a {@link com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity} object.
	 */
	public void markAsSaved( String p_sEntityName, MEntity p_oEntity ) {
		List<String> listKeys = this.savedEntities.get(p_sEntityName);
		if ( listKeys == null ) {
			listKeys = new ArrayList<String>();
			this.savedEntities.put(p_sEntityName, listKeys);
		}
		listKeys.add(p_oEntity.idToString());
	}
	
	/**
	 * <p>isAlreadySaved.</p>
	 *
	 * @param p_sEntityName a {@link java.lang.String} object.
	 * @param p_oEntity a {@link com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity} object.
	 * @return a boolean.
	 */
	public boolean isAlreadySaved( String p_sEntityName, MEntity p_oEntity ) {
		boolean r_bSaved = false ;
		List<String> listKeys = this.savedEntities.get(p_sEntityName);
		if ( listKeys != null ) {
			r_bSaved = listKeys.contains(p_oEntity.idToString());
		}
		return r_bSaved ;
	}
	
	/**
	 * <p>getEntitiesToPersist.</p>
	 *
	 * @param p_listEntities a {@link java.util.Collection} object.
	 * @param p_bMarkAsPersisted a boolean.
	 * @param p_sEntityName a {@link java.lang.String} object.
	 * @return a {@link java.util.Collection} object.
	 * @param <T> a T object.
	 */
	public <T extends MEntity> Collection<T> getEntitiesToPersist( String p_sEntityName, Collection<T> p_listEntities, boolean p_bMarkAsPersisted ) {
		Collection<T> r_listEntitiesToPersist = new ArrayList<T>();
		for ( T oEntity : p_listEntities) {
			if ( !isAlreadySaved( p_sEntityName, oEntity )) {
				r_listEntitiesToPersist.add(oEntity);
				if ( p_bMarkAsPersisted ) {
					markAsSaved(p_sEntityName, oEntity);
				}
			}
		}
		return r_listEntitiesToPersist ;
	}
	
	/**
	 * Clear
	 */
	public void clear() {
		this.savedEntities.clear();
		this.sessionCache.clear();
	}
}
