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
package com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews;

import android.support.v7.widget.RecyclerView;

/**
 * Used on RecyclerView views to manipulate their adapter
 *
 * @param <ITEM> the view describing a row of the fixedlist
 * @param <ITEMVM> the view model of a row in the fixedlist
 */
public interface MMRecyclableList {

    /**
     * Sets the adapter on the view
     * @param p_oAdapter the adapter to set on the view
     */
    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter);

}
