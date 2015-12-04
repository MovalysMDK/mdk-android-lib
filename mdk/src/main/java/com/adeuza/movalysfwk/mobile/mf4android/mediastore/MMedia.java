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
package com.adeuza.movalysfwk.mobile.mf4android.mediastore;

import java.io.Serializable;

import android.net.Uri;

public class MMedia implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String id ;
	/**
	 * Cannot be serialized
	 */
	private transient Uri localUri ;
	/**
	 * Backup for localUri because Uri type is not serializable
	 */
	private String localUriAsString; 
	
	private String displayName ;
	
	private String contentType ;

	private long size ;

	private String title ;
	
	private long dateModified ;

	public String getId() {
		return id;
	}

	public void setId(String p_sId) {
		this.id = p_sId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String p_sDisplayName) {
		this.displayName = p_sDisplayName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String p_sContentType) {
		this.contentType = p_sContentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long p_lSize) {
		this.size = p_lSize;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String p_sTitle) {
		this.title = p_sTitle;
	}

	public Uri getLocalUri() {
		if ( this.localUri == null && this.localUriAsString != null ) {
			this.localUri = Uri.parse(this.localUriAsString);
		}
		return localUri ;
	}

	public void setLocalUri(Uri p_oUri) {
		this.localUri = p_oUri;
		if ( p_oUri != null ) {
			this.localUriAsString = p_oUri.toString();
		}else {
			this.localUriAsString = null ;
		}
	}

	public long getDateModified() {
		return dateModified;
	}

	public void setDateModified(long p_lDateModified) {
		this.dateModified = p_lDateModified;
	}
}
