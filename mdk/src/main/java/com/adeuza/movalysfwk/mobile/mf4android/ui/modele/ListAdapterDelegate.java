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

import android.view.View;
import android.view.ViewGroup;

/**
 * Delegate for the list adapters
 */
public interface ListAdapterDelegate {

	/**
	 * returns the view in the given position for the given list level
	 * @param p_oLevel the {@link AdapterLevel} to work on
	 * @param p_bExpanded true if the element is expanded
	 * @param p_oView the view to compute
	 * @param p_oViewGroup the groupview which the view is related to 
	 * @param p_oPositions the position(s) in the list being worked on
	 * @return the view in the given position for the given list level
	 */
	public View getViewByLevel(AdapterLevel p_oLevel, boolean p_bExpanded, View p_oView, ViewGroup p_oViewGroup, int p_iViewType, int... p_iPositions);
	
	public View getLegacyViewByLevel(AdapterLevel p_oLevel, boolean p_bExpanded, View p_oView, ViewGroup p_oViewGroup, int p_iViewType, int ... p_oPositions);
	
	public ConfigurableListViewHolder onCreateViewHolder(AdapterLevel p_oLevel, boolean p_bExpanded, ViewGroup parent, int p_iViewType);
	
	public void onBindViewHolder(AdapterLevel p_oLevel, boolean p_bExpanded, ConfigurableListViewHolder holder, int... position);
}
