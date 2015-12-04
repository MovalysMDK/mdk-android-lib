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
package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import android.graphics.Bitmap;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * <p>Action's output to retrieve some bitmap data </p>
 *
 *
 *
 */

public class BitmapParameterOUT extends AbstractActionParameter implements ActionParameter {
	
	/**
	 * Generated Serial number 
	 */
	private static final long serialVersionUID = -9138898180345619458L;
	/**
	 * Data to displayed
	 */
	private Bitmap bitmapImage = null ; 
	/**
	 * Default constructor with the minimum
	 * @param p_oBitmapImage bitmap data or null if download is KO 
	 */
	public BitmapParameterOUT(Bitmap p_oBitmapImage ) {
		this.bitmapImage = p_oBitmapImage ;
	}
	/**
	 * Accesseur de l'image
	 * @return le bitmap de l'image
	 */
	public Bitmap getBitmapImage() {
		return this.bitmapImage;
	}
	/**
	 * Modifieur de l'image
	 * @param p_oBitmapImage nouvelle image
	 */
	public void setBitmapImage(Bitmap p_oBitmapImage) {
		this.bitmapImage = p_oBitmapImage;
	}
}
