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

/**
 * This is a classic ViewHolder implementation used to retain some references
 * of the components of the items of a ListView
 */
public class ConfigurableExpandedListViewHolderImpl extends ConfigurableListViewHolderImpl implements ConfigurableExpandedListViewHolder {
	
	
	private boolean isExpanded = false;
	
	
	/**
	 * Constructeur
	 */
	public ConfigurableExpandedListViewHolderImpl(View p_oView, boolean p_bIsExpanded) {
		super(p_oView);
		this.isExpanded = p_bIsExpanded;
	}


	public boolean isExpanded() {
		return isExpanded;
	}


	public void setExpanded(boolean p_bIsExpanded) {
		this.isExpanded = p_bIsExpanded;
	}
	
	
}
