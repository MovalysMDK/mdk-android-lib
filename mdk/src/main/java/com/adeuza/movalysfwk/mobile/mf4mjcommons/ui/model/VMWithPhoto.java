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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;


/**
 * View model that contains a photo.
 * This view model can be used with the component MMPhotoFixedListView
 *
 */
public interface VMWithPhoto {

	/**
	 * <p>getPhoto.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO} object.
	 */
	public MPhotoVO getPhoto();
	
	/**
	 * <p>setPhoto.</p>
	 *
	 * @param p_oPhoto a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO} object.
	 */
	public void setPhoto( MPhotoVO p_oPhoto );
}
