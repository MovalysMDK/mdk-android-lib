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

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;


/**
 * <p>Interface qui implémente à la fois Identifiable et MIEntity</p>
 * Elle est applicable à toutes les entités qui possèdent une clef primaire simple (ID).
 *
 *
 * @param <T>
 * 		Une entité de movalys identifiable
 */
public interface IdentifiableEntity extends MEntity {
	/**
	 *
	 * Méthode getId de la classe Identifiable
	 *
	 * @return l'id de l'objet identifiable
	 */
	public long getId();
	
	/**
	 * Affecte l'identifiant de l'objet
	 *
	 * @param p_lId l'identifiant de l'objet
	 */
	public void setId(long p_lId);

	/**
	 * Récupère la précédente valeur de l'identifiant
	 *
	 * @return La précédente valeur de l'identifiant
	 */
	public long getOldId();

	/**
	 * Définit la précédente valeur de l'identifiant
	 *
	 * @param p_lOldId La précédente valeur de l'identifiant
	 */
	public void setOldId(long p_lOldId);
}
