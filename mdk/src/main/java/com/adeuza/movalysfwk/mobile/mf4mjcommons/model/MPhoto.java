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

import java.sql.Timestamp;

/**
 * Photo component
 *
 */
/**
 * <p>MPhoto class.</p>
 *
 */
public class MPhoto {

	/**
	 * Photo name
	 */
	private String name;
	
	/**
	 * Uri, either remote location or local (content://)
	 */
	private String uri ;
	
	/**
	 * Date of the photo
	 */
	private Timestamp date ;
	
	/**
	 * Comment
	 */
	private String desc ;
	
	/**
	 * Photo state
	 */
	private MPhotoState photoState ;
	
	/**
	 * 
	 */
	private String content ;

	/**
	 * Svg file.
	 */
	private String svg;

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#getName()
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#setName(java.lang.String)
	 * @param p_sName a {@link java.lang.String} object.
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * <p>Getter for the field <code>uri</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#getUri()
	 * @return a {@link java.lang.String} object.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * <p>Setter for the field <code>uri</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#setUri(java.lang.String)
	 * @param p_sUri a {@link java.lang.String} object.
	 */
	public void setUri(String p_sUri) {
		this.uri = p_sUri;
	}

	/**
	 * <p>Getter for the field <code>date</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#getDate()
	 * @return a java$sql$Timestamp object.
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * <p>Setter for the field <code>date</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#setDate(java.sql.Timestamp)
	 * @param p_oDate a java$sql$Timestamp object.
	 */
	public void setDate(Timestamp p_oDate) {
		this.date = p_oDate;
	}

	/**
	 * <p>getDescription.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#getDescription()
	 * @return a {@link java.lang.String} object.
	 */
	public String getDescription() {
		return desc;
	}

	/**
	 * <p>setDescription.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#setDescription(java.lang.String)
	 * @param p_sDescription a {@link java.lang.String} object.
	 */
	public void setDescription(String p_sDescription) {
		this.desc = p_sDescription;
	}

	/**
	 * <p>Getter for the field <code>desc</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#getDescription()
	 * @return a {@link java.lang.String} object.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * <p>Setter for the field <code>desc</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#setDescription(java.lang.String)
	 * @param p_sDescription a {@link java.lang.String} object.
	 */
	public void setDesc(String p_sDescription) {
		this.desc = p_sDescription;
	}
	
	/**
	 * <p>Getter for the field <code>photoState</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#getPhotoState()
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhotoState} object.
	 */
	public MPhotoState getPhotoState() {
		return photoState;
	}

	/**
	 * <p>Setter for the field <code>photoState</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#setPhotoState(com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhotoState)
	 * @param p_oPhotoState a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhotoState} object.
	 */
	public void setPhotoState(MPhotoState p_oPhotoState) {
		this.photoState = p_oPhotoState;
	}

	/**
	 * <p>Getter for the field <code>content</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#getContent()
	 * @return a {@link java.lang.String} object.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * <p>Setter for the field <code>content</code>.</p>
	 *
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto#setContent(java.lang.String)
	 * @param p_sContent a {@link java.lang.String} object.
	 */
	public void setContent(String p_sContent) {
		this.content = p_sContent;
	}

	/**
	 * <p>Getter for the field <code>svg</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSvg() {
		return svg;
	}

	/**
	 * <p>Setter for the field <code>svg</code>.</p>
	 *
	 * @param p_sSvg a {@link java.lang.String} object.
	 */
	public void setSvg(String p_sSvg) {
		this.svg = p_sSvg;
	}
}
