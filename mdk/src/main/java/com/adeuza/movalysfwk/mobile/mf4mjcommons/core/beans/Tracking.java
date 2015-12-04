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
 * <p>Eumeration destinée au marquage des entités par drapeau. Gère le champ ADMTRACKING des entités</p>
 *
 *
 * @since 2.5
 */
public enum Tracking implements Enum {

	/**
	 * valeur par défaut
	 */
	NOFLAG(Tracking.NO_FLAG),

	/**
	 * drapeau blanc
	 */
	WHITEFLAG(Tracking.WHITE_FLAG),
	/** un élément actif */

	/**
	 * drapeau vert
	 */
	GREENFLAG(Tracking.GREEN_FLAG),

	/**
	 * drapeau jaune
	 */
	YELLOWFLAG(Tracking.YELLOW_FLAG),
	
	/**
	 * drapeau rouge
	 */
	REDFLAG(Tracking.RED_FLAG),
	
	/**
	 * drapeau bleu
	 */
	BLUEFLAG(Tracking.BLUE_FLAG),

	/**
	 * drapeau violet
	 */
	PURPLEFLAG(Tracking.PURPLE_FLAG);

	/**
	 * La valeur en base de l'énumération MITracking.noFlag
	 */
	private static final int NO_FLAG = -1;

	/**
	 * La valeur en base de l'énumération MITracking.whiteFlag
	 */
	private static final int WHITE_FLAG = 0;

	/**
	 * La valeur en base de l'énumération MITracking.greenFlag
	 */
	private static final int GREEN_FLAG = 1;

	/**
	 * La valeur en base de l'énumération MITracking.yellowFlag
	 */
	private static final int YELLOW_FLAG = 2;

	/**
	 * La valeur en base de l'énumération MITracking.redFlag
	 */
	private static final int RED_FLAG = 3;

	/**
	 * La valeur en base de l'énumération MITracking.blueFlag
	 */
	private static final int BLUE_FLAG = 4;

	/**
	 * La valeur en base de l'énumération MITracking.purpleFlag
	 */
	private static final int PURPLE_FLAG = 5;
	
	/**
	 * La valeur en base
	 */
	private int baseId = NO_FLAG;

	/**
	 * Constucteur
	 * 
	 * @param p_iBaseId la valeur en base
	 */
	private Tracking(int p_iBaseId) {
		this.baseId = p_iBaseId;
	}

	/**
	 * Retourne la valeur de l'enumération corresponde à l'entier passé en paramètre
	 *
	 * @param p_iTracking la valeur en base de la valeur de l'énumération recherchée
	 * @return la valeur l'énumération correspondante à l'entrée
	 */
	public static Tracking valueOf(int p_iTracking) {
		Tracking r_oMITracking = null;
		switch (p_iTracking) {
		case NO_FLAG:
			r_oMITracking = NOFLAG;
			break;
		case WHITE_FLAG:
			r_oMITracking = WHITEFLAG;
			break;
		case GREEN_FLAG:
			r_oMITracking = GREENFLAG;
			break;
		case YELLOW_FLAG:
			r_oMITracking = YELLOWFLAG;
			break;
		case RED_FLAG:
			r_oMITracking = REDFLAG;
			break;
		case BLUE_FLAG:
			r_oMITracking = BLUEFLAG;
			break;
		case PURPLE_FLAG:
			r_oMITracking = PURPLEFLAG;
			break;
		default:
			throw new IllegalStateException(" L'entier "+p_iTracking+" n'existe pas dans l'énumération");
		}
		return r_oMITracking;
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
