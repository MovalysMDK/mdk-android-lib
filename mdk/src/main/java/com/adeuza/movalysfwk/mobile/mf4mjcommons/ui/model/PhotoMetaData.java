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

import java.io.Serializable;


/**
 * <p>PhotoMetaData interface.</p>
 *
 */
public interface PhotoMetaData extends Serializable {

	/**
	 * Id
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId();

	/**
	 * Photo name
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName();

	/**
	 * <p>setName.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name);

	/**
	 * <p>getDateTaken.</p>
	 *
	 * @return a long.
	 */
	public long getDateTaken();

	/**
	 * <p>setDateTaken.</p>
	 *
	 * @param p_oDate a long.
	 */
	public void setDateTaken(long p_oDate);
	
	/**
	 * <p>getLocalUriAsString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLocalUriAsString();
	
	/**
	 * <p>setLocalUri.</p>
	 *
	 * @param p_sUri a {@link java.lang.String} object.
	 */
	public void setLocalUri( String p_sUri );

	/**
	 * <p>setRemoteUri.</p>
	 *
	 * @param p_sUri a {@link java.lang.String} object.
	 */
	public void setRemoteUri( String p_sUri );
	
	/**
	 * <p>isPhotoShot.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPhotoShot();
	
	/**
	 * <p>isPhotoSelected.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPhotoSelected();
	
	/**
	 * <p>isPhotoDownloaded.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPhotoDownloaded();
	
	/**
	 * <p>setPhotoAsShooted.</p>
	 */
	public void setPhotoAsShooted();
	
	/**
	 * <p>setPhotoAsSelected.</p>
	 */
	public void setPhotoAsSelected();
	
	/**
	 * <p>setPhotoAsDownloaded.</p>
	 */
	public void setPhotoAsDownloaded();
	
	/**
	 * <p>isReadOnly.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isReadOnly();
	
	/**
	 * <p>setReadOnly.</p>
	 *
	 * @param p_bReadOnly a boolean.
	 */
	public void setReadOnly( boolean p_bReadOnly);
	
	/**
	 * <p>toDebug.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toDebug();
}
