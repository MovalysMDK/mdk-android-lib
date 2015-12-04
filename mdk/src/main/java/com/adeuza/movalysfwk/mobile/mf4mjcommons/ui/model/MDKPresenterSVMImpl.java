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

import android.net.Uri;

import java.io.Serializable;

/**
 * The MDKPresenter value object to manage MDKPresenterView between actions.
 */
public class MDKPresenterSVMImpl implements Serializable {

    /**
     * The title.
     */
    private String title;
    /**
     * The uri.
     */
    private Uri uri;


    /**
     * Return the object title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }


    /**
     * Setter of title.
     *
     * @param p_oTitle Title to set
     */
    public void setTitle(String p_oTitle) {
        this.title = p_oTitle;
    }

    /**
     * Return the object Uri.
     *
     * @return the Uri
     */
    public Uri getUri() {
        return this.uri;
    }


    /**
     * Setter of Uri.
     *
     * @param p_oUri Uri to set
     */
    public void setUri(Uri p_oUri) {
        this.uri = p_oUri;
    }
}
