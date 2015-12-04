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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.model;

//@non-generated-start[imports]
//@non-generated-end

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;

/**
 *
 * <p>Enumération : PhotoState</p>
 *
 */
public enum MPhotoState implements Enum {
	/**
	 * valeur nulle
	 */
	FWK_NONE(0),

	/**
	 * Cascade SELECTED
	 */
	SELECTED(1),
	/**
	 * Cascade DOWNLOADED
	 */
	DOWNLOADED(2),
	/**
	 * Cascade TAKEN
	 */
	TAKEN(3),
	/**
	 * Cascade TODOWNLOAD
	 */
	TODOWNLOAD(4);

	/**
	 * La valeur en base
	 */
	private int baseId;

	/**
	 * Constucteur
	 * 
	 * @param p_iBaseId la valeur en base
	 */
	private MPhotoState(int p_iBaseId) {
		this.baseId = p_iBaseId;
	}

	/**
	 * Retourne la valeur de l'enumération corresponde à l'entier passé en paramètre
	 *
	 * @param p_iPhotoState la valeur en base
	 * @return la valeur l'énumération correspondante à l'entrée
	 */
	public static MPhotoState valueOf(int p_iPhotoState) {
		MPhotoState r_oPhotoState = null;
		switch (p_iPhotoState) {
		case 0:
			r_oPhotoState = FWK_NONE;
			break;

		case 1:
			r_oPhotoState = SELECTED;
			break;

		case 2:
			r_oPhotoState = DOWNLOADED;
			break;

		case 3:
			r_oPhotoState = TAKEN;
			break;

		case 4:
			r_oPhotoState = TODOWNLOAD;
			break;

		default:
			Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_3052, String.valueOf(p_iPhotoState)+ ErrorDefinition.FWK_MOBILE_E_3052_LABEL);
		}
		return r_oPhotoState;
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
	public MPhotoState fromBaseId(int p_iBaseId) {
		return valueOf(p_iBaseId);
	}
}
