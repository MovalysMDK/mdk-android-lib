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
import java.sql.Timestamp;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhotoState;

/**
 * <p>MPhotoVO class.</p>
 */
public class MPhotoVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6239629097680875681L;

    /**
     *
     */
    private String id_id;

    /**
     * Photo name
     */
    private String name;

    /**
     *
     */
    private String description;

    /**
     * Uri, either remote location or local (content://)
     */
    private String uri;

    /**
     * Date of the photo
     */
    private Timestamp date;

    /**
     * Photo state
     */
    private MPhotoState photoState;

    /**
     *
     */
    private boolean useComponentForMetadata;

    /**
     * Svg file.
     */
    private String svg;

    /**
     * <p>Constructor for MPhotoVO.</p>
     */
    public MPhotoVO() {
        super();
        this.id_id = null;
        this.name = null;
        this.description = null;
        this.uri = null;
        this.date = null;
        this.photoState = MPhotoState.FWK_NONE;
        this.svg = null;
    }

    /**
     * Constructor using MPhotoVO
     *
     * @param p_oPhoto a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO} object.
     */
    public MPhotoVO(MPhotoVO p_oPhoto) {
        super();
        this.id_id = p_oPhoto.id_id;
        this.name = p_oPhoto.name;
        this.description = p_oPhoto.description;
        this.uri = p_oPhoto.uri;
        this.date = p_oPhoto.date;
        this.photoState = p_oPhoto.photoState;
        this.svg = p_oPhoto.svg;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param p_sName a {@link java.lang.String} object.
     */
    public void setName(String p_sName) {
        this.name = p_sName;
    }

    /**
     * <p>Getter for the field <code>uri</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUri() {
        return uri;
    }

    /**
     * <p>Setter for the field <code>uri</code>.</p>
     *
     * @param p_sUri a {@link java.lang.String} object.
     */
    public void setUri(String p_sUri) {
        this.uri = p_sUri;
    }

    /**
     * <p>Getter for the field <code>date</code>.</p>
     *
     * @return a java$sql$Timestamp object.
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * <p>Setter for the field <code>date</code>.</p>
     *
     * @param p_oDate a java$sql$Timestamp object.
     */
    public void setDate(Timestamp p_oDate) {
        this.date = p_oDate;
    }

    /**
     * <p>Getter for the field <code>photoState</code>.</p>
     *
     * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhotoState} object.
     */
    public MPhotoState getPhotoState() {
        return photoState;
    }

    /**
     * <p>Setter for the field <code>photoState</code>.</p>
     *
     * @param p_oPhotoState a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhotoState} object.
     */
    public void setPhotoState(MPhotoState p_oPhotoState) {
        this.photoState = p_oPhotoState;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param p_sDescription a {@link java.lang.String} object.
     */
    public void setDescription(String p_sDescription) {
        this.description = p_sDescription;
    }

    /**
     * <p>isUseComponentForMetadata.</p>
     *
     * @return a boolean.
     */
    public boolean isUseComponentForMetadata() {
        return useComponentForMetadata;
    }

    /**
     * <p>Setter for the field <code>useComponentForMetadata</code>.</p>
     *
     * @param p_bUseComponentForMetadata a boolean.
     */
    public void setUseComponentForMetadata(boolean p_bUseComponentForMetadata) {
        this.useComponentForMetadata = p_bUseComponentForMetadata;
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

    /**
     * <p>getLocalUri.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLocalUri() {
        return this.uri != null && this.uri.startsWith("content://") ? this.uri : null;
    }

    /**
     * <p>getRemoteUri.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRemoteUri() {
        return this.uri != null && !this.uri.startsWith("content://") ? this.uri : null;
    }

    /**
     * <p>Getter for the field <code>id_id</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getId_id() {
        return id_id;
    }

    /**
     * <p>Setter for the field <code>id_id</code>.</p>
     *
     * @param p_sId_id a {@link java.lang.String} object.
     */
    public void setId_id(String p_sId_id) {
        this.id_id = p_sId_id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MPhotoVO) {
            MPhotoVO photo = (MPhotoVO) obj;
            if (photo.id_id != null && photo.id_id.equals(this.id_id) &&
                    photo.name != null && photo.name.equals(this.name) &&
                    photo.date != null && photo.date.equals(this.date) &&
                    photo.description != null && photo.description.equals(this.description) &&
                    photo.photoState != null && photo.photoState.equals(this.photoState) &&
                    photo.useComponentForMetadata == this.useComponentForMetadata &&
                    photo.uri != null && photo.uri.equals(this.uri) &&
                    ((photo.svg != null && photo.svg.equals(this.svg)) || (photo.svg == null && this.svg == null))
                    ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
}
