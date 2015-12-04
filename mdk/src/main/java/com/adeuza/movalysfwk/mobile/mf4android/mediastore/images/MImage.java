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
package com.adeuza.movalysfwk.mobile.mf4android.mediastore.images;

import java.io.Serializable;

import android.graphics.Bitmap;

import com.adeuza.movalysfwk.mobile.mf4android.mediastore.MMedia;

public class MImage extends MMedia implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2240135283982415181L;

	/**
	 * Image bitmap
	 */
	private Bitmap bitmapImage ;  
	
	/**
	 * Taken date of the photo
	 */
	private long dateTaken ;
	
	/**
	 * Description
	 */
	private String description ;
	
	/**
	 * Private photo
	 */
	private boolean priv ;
	
	/**
	 * Latitude
	 */
	private double latitude ;
	
	/**
	 * Longitude
	 */
	private double longitude ;
	
	/**
	 * Orientation
	 */
	private double orientation ;

	/**
	 * Retourne l'objet bitmapImage
	 * @return Objet bitmapImage
	 */
	public Bitmap getBitmapImage() {
		return this.bitmapImage;
	}

	/**
	 * Affecte l'objet bitmapImage 
	 * @param p_oBitmapImage Objet bitmapImage
	 */
	public void setBitmapImage(Bitmap p_oBitmapImage) {
		this.bitmapImage = p_oBitmapImage;
	}

	/**
	 * Retourne l'objet dateTaken
	 * @return Objet dateTaken
	 */
	public long getDateTaken() {
		return this.dateTaken;
	}

	/**
	 * Affecte l'objet dateTaken 
	 * @param p_oDateTaken Objet dateTaken
	 */
	public void setDateTaken(long p_oDateTaken) {
		this.dateTaken = p_oDateTaken;
	}

	/**
	 * Retourne l'objet description
	 * @return Objet description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Affecte l'objet description 
	 * @param p_oDescription Objet description
	 */
	public void setDescription(String p_oDescription) {
		this.description = p_oDescription;
	}

	/**
	 * Retourne l'objet priv
	 * @return Objet priv
	 */
	public boolean isPriv() {
		return this.priv;
	}

	/**
	 * Affecte l'objet priv 
	 * @param p_oPriv Objet priv
	 */
	public void setPriv(boolean p_oPriv) {
		this.priv = p_oPriv;
	}

	/**
	 * Retourne l'objet latitude
	 * @return Objet latitude
	 */
	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * Affecte l'objet latitude 
	 * @param p_oLatitude Objet latitude
	 */
	public void setLatitude(double p_oLatitude) {
		this.latitude = p_oLatitude;
	}

	/**
	 * Retourne l'objet longitude
	 * @return Objet longitude
	 */
	public double getLongitude() {
		return this.longitude;
	}

	/**
	 * Affecte l'objet longitude 
	 * @param p_oLongitude Objet longitude
	 */
	public void setLongitude(double p_oLongitude) {
		this.longitude = p_oLongitude;
	}

	/**
	 * Retourne l'objet orientation
	 * @return Objet orientation
	 */
	public double getOrientation() {
		return this.orientation;
	}

	/**
	 * Affecte l'objet orientation 
	 * @param p_oOrientation Objet orientation
	 */
	public void setOrientation(double p_oOrientation) {
		this.orientation = p_oOrientation;
	}	
}
