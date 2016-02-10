package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import android.graphics.Bitmap;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * <p>Action's output to retrieve some bitmap data </p>
 *
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 *
 * @author spacreau
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
