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

/**
 * <p>Indique si un élément est "actif" ou "non actif"</p>
 *
 *
 * @since 2.5
 */
public enum Living implements Enum {

	/**
	 * Un élément non actif
	 */
	DEAD(Living.DEAD_VALUE),

	/**
	 * Un élément actif
	 */
	LIVING(Living.LIVING_VALUE),

	/**
	 * Un élément indéfini
	 */
	UNDEFINED(Living.UNDEFINED_VALUE);
	/**
	 * La valeur en base de l'énumération Living.dead
	 */
	private static final int DEAD_VALUE = 0;

	/**
	 * La valeur en base de l'énumération Living.living
	 */
	private static final int LIVING_VALUE = 1;
	
	/**
	 * La valeur en base de l'énumération Living.undefined
	 */
	private static final int UNDEFINED_VALUE = -1;
	/**
	 * La valeur en base
	 */
	private int baseId = LIVING_VALUE;

	/**
	 * Constucteur
	 * 
	 * @param p_iBaseId la valeur en base
	 */
	private Living(int p_iBaseId) {
		this.baseId = p_iBaseId;
	}

	/**
	 * Retourne la valeur de l'enumération corresponde à l'entier passé en paramètre
	 *
	 * @param p_iLivingCode la valeur en base de la valeur de l'énumération recherchée
	 * @return la valeur l'énumération correspondante à l'entrée
	 */
	public static Living valueOf(int p_iLivingCode) {
		Living r_oLiving = null;
		switch (p_iLivingCode) {
		case DEAD_VALUE:
			r_oLiving = DEAD;
			break;
		case LIVING_VALUE:
			r_oLiving = LIVING;
			break;
		case UNDEFINED_VALUE:
			r_oLiving = UNDEFINED;
			break;
		default:
			throw new IllegalStateException(" L'entier "+p_iLivingCode+" n'existe pas dans l'énumération");
		}
		return r_oLiving;
	}

	/**
	 * Retourne l'objet baseId
	 *
	 * @return Objet baseId
	 */
	@Override
	public int getBaseId() {
		return this.baseId;
	}

	/** {@inheritDoc} */
	@Override
	public Enum fromBaseId(int p_iBaseId) {
		return valueOf( p_iBaseId);
	}
}
