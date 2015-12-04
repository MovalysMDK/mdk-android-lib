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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO;

/**
 * Helper for the component MPhoto
 *
 */
public class BasePhotoHelper {
	
	/**
	 * <p>toComponent.</p>
	 *
	 * @param p_oPhotoVM a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto} object.
	 */
	public static MPhoto toComponent( MPhotoVO p_oPhotoVM ) {
		MPhoto r_oMPhoto = null ;
		if ( p_oPhotoVM != null && (p_oPhotoVM.getName() != null || 
				p_oPhotoVM.getUri() != null || p_oPhotoVM.getDescription() != null ||
						p_oPhotoVM.getDate() != null || p_oPhotoVM.getPhotoState() != MPhotoState.FWK_NONE || p_oPhotoVM.getSvg() != null)) {
			r_oMPhoto = new MPhoto();
			r_oMPhoto.setName(p_oPhotoVM.getName());
			r_oMPhoto.setUri(p_oPhotoVM.getUri());
			r_oMPhoto.setSvg(p_oPhotoVM.getSvg());
			r_oMPhoto.setDate(p_oPhotoVM.getDate());
			r_oMPhoto.setDescription(p_oPhotoVM.getDescription());
			r_oMPhoto.setPhotoState(p_oPhotoVM.getPhotoState());
		}
		return r_oMPhoto ;
	}
	
	/**
	 * <p>toViewModel.</p>
	 *
	 * @param p_oMPhoto a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO} object.
	 */
	public static MPhotoVO toViewModel( MPhoto p_oMPhoto ) {
		MPhotoVO r_oPhotoVM = new MPhotoVO();
		if ( p_oMPhoto != null ) {
			r_oPhotoVM.setName(p_oMPhoto.getName());
			r_oPhotoVM.setUri(p_oMPhoto.getUri());
			r_oPhotoVM.setSvg(p_oMPhoto.getSvg());
			r_oPhotoVM.setDate(p_oMPhoto.getDate());
			r_oPhotoVM.setDescription(p_oMPhoto.getDescription());
			r_oPhotoVM.setPhotoState(p_oMPhoto.getPhotoState());
		}
		return r_oPhotoVM ;
	}
}
