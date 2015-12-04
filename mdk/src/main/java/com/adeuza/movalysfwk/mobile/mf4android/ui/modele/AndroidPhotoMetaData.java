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
package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import java.io.Serializable;

import android.net.Uri;

import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MImage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData;

/**
 * Meta data de photo
 *
 */
public class AndroidPhotoMetaData implements PhotoMetaData, Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 2705369219165004370L;

	/**
	 * <p>
	 * Photometadata ID
	 * </p>
	 */
	private String id;
	
	/**
	 * remote uri
	 */
	private String remoteUri ;
	
	/**
	 * Mediastore image
	 */
	private MImage image = new MImage();

	/**
	 * Geolocalisation
	 */
	private AddressLocationSVMImpl position;

	/**
	 * android.net.Uri class is not serializable, this attribut is to store uri when serializing
	 */
	private String localUriPersistent ;
	
	/**
	 * <p>
	 * True if the photo was taken in the app, false if the photo was choosen
	 * </p>
	 * this allow further computings to decide if the photo can be clear or not
	 */
	private boolean photoShot;
	/**
	 * Is a Choosen photo
	 */
	private boolean photoSelected;
	/**
	 * Is a Downloaded photo
	 */
	private boolean photoDownloaded;

	/**
	 * Photo can be modified
	 */
	private boolean readOnly = false ;
	
	/**
	 * Get photo id
	 * @return
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/**
	 * Set photo id
	 * @param p_lId
	 */
	public void setId(String p_sId) {
		this.id = p_sId;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#getName()
	 */
	@Override
	public String getName() {
		return this.image.getTitle();
	}

	/**
	 * {@inheritDoc}
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#setName(java.lang.String)
	 */
	@Override
	public void setName(String p_sName) {
		this.image.setTitle(p_sName);
	}

	/**
	 * Get photo description
	 * @return
	 */
	public String getDescription() {
		return this.image.getDescription();
	}
	
	/**
	 * Set photo description
	 * @param p_sDescription
	 */
	public void setDescription(String p_sDescription) {
		this.image.setDescription(p_sDescription);
	}

	/**
	 * Photo taken date
	 * @return
	 */
	@Override
	public long getDateTaken() {
		return this.image.getDateTaken();
	}

	/**
	 * Photo taken date
	 * @param p_oDate
	 */
	@Override
	public void setDateTaken(long p_oDate) {
		this.image.setDateTaken(p_oDate);
	}

	/**
	 * Position viewmodel
	 * @return
	 */
	public AddressLocationSVMImpl getPosition() {
		return position;
	}

	/**
	 * Position viewmodel
	 * @param p_oPosition
	 */
	public void setPosition(AddressLocationSVMImpl p_oPosition) {
		this.position = p_oPosition;
	}

	/**
	 * @param p_oLatitude
	 * @param p_oLongitude
	 */
	public void setPosition(Double p_oLatitude, Double p_oLongitude) {
		this.image.setLatitude(p_oLatitude);
		this.image.setLongitude(p_oLongitude);
		if (this.position == null) {
			this.position = new AddressLocationSVMImpl(p_oLatitude, p_oLongitude);
		} else {
			this.position.setGPSPosition(p_oLatitude, p_oLongitude);
		}
	}

	/**
	 * @return
	 */
	public boolean isPrivate() {
		return this.image.isPriv();
	}

	/**
	 * @param p_bPrivate
	 */
	public void setPrivate(boolean p_bPrivate) {
		this.image.setPriv(p_bPrivate);
	}

	/**
	 * @return
	 */
	public String getRemoteUri() {
		return this.remoteUri;
	}

	/**
	 * @param p_sRemoteUri
	 */
	@Override
	public void setRemoteUri(String p_sRemoteUri) {
		this.remoteUri = p_sRemoteUri;
	}
	
	/**
	 * @return
	 */
	public Uri getLocalUri() {
		if ( this.image.getLocalUri() == null && this.localUriPersistent != null ) {
			this.image.setLocalUri( Uri.parse(this.localUriPersistent));
		}
		return this.image.getLocalUri();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#getLocalUriAsString()
	 */
	@Override
	public String getLocalUriAsString() {
		if ( this.image.getLocalUri() == null && this.localUriPersistent != null ) {
			this.image.setLocalUri( Uri.parse(this.localUriPersistent));
		}
		String sUri = null ;
		if (this.getLocalUri() != null) {
			sUri = this.getLocalUri().toString() ;
		}
		return sUri ;
	}

	/**
	 * Modifieur de  localUriPersistent
	 * @param p_sLocalUri nouvel uri
	 */
	public void setLocalUri(Uri p_oLocalUri) {
		if ( p_oLocalUri != null ) {
			this.localUriPersistent = p_oLocalUri.toString();
		}
		else {
			this.localUriPersistent = null ;
		}
		this.image.setLocalUri( p_oLocalUri );
	}
	
	/**
	 * Modifieur de  localUriPersistent
	 * @param p_sLocalUri nouvel uri
	 */
	@Override
	public void setLocalUri( String p_sLocalUri) {
		this.localUriPersistent = p_sLocalUri;
		if ( p_sLocalUri != null ) {
			this.image.setLocalUri( Uri.parse(p_sLocalUri));
		}
		else {
			this.image.setLocalUri(null);
		}
	}

	/**
	 * return image
	 * @return image
	 */
	public MImage getImage() {
		return image;
	}
	
	/**
	 * clear the object
	 */
	public void clear() {
		this.clearImage();
		this.localUriPersistent = null ;
		this.photoDownloaded = false ;
		this.photoSelected = false ;
		this.photoShot = false ;
		this.remoteUri = null ;
		this.position.clear();
	}
	
	/**
	 * clear the image
	 */
	public void clearImage() {
		this.image.setBitmapImage(null);
		this.image.setDateTaken(0);
		this.setPosition(0d,0d);
		this.image.setLocalUri(null);
	}

	/**
	 * @return
	 */
	@Override
	public String toDebug() {
		StringBuilder sDebug = new StringBuilder();
		sDebug.append("Photometadata - id: ");
		sDebug.append(this.id);
		sDebug.append(", local uri:");
		sDebug.append(this.getLocalUri());
		sDebug.append(", remote uri:");
		sDebug.append(this.getRemoteUri());
		sDebug.append("\n  name:");
		sDebug.append(this.getName());
		sDebug.append(", description:");
		sDebug.append(this.getDescription());
		sDebug.append("\n  date taken:");
		sDebug.append(this.getDateTaken());
		sDebug.append(", private:");
		sDebug.append(this.isPrivate());
		sDebug.append(", is photo taken:");
		sDebug.append(this.isPhotoShot());
		sDebug.append(", is photo downloaded:");
		sDebug.append(this.isPhotoDownloaded());
		sDebug.append(", is photo selected:");
		sDebug.append(this.isPhotoSelected());
		
		return sDebug.toString();
	}
	
	/** (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	/** (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean p_bReadOnly) {
		this.readOnly = p_bReadOnly;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.core.beans.MEntity#idToString()
	 */
	public String idToString() {
		StringBuilder r_sId = new StringBuilder();
		r_sId.append(String.valueOf(this.id));
		return r_sId.toString();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#isPhotoShot()
	 */
	@Override
	public boolean isPhotoShot() {
		return this.photoShot;
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#isPhotoSelected()
	 */
	@Override
	public boolean isPhotoSelected() {
		return this.photoSelected;
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#isPhotoDownloaded()
	 */
	@Override
	public boolean isPhotoDownloaded() {
		return this.photoDownloaded;
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#setPhotoAsShooted()
	 */
	@Override
	public void setPhotoAsShooted() {
		this.photoShot = true ;
		this.photoSelected = false ;
		this.photoDownloaded = false ;
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#setPhotoAsSelected()
	 */
	@Override
	public void setPhotoAsSelected() {
		this.photoSelected = true ;
		this.photoShot = false ;
		this.photoDownloaded = false ;
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData#setPhotoAsDownloaded()
	 */
	@Override
	public void setPhotoAsDownloaded() {
		this.photoDownloaded = true;
		this.photoShot = true ;
		this.photoSelected = false ;
	}
}
