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

import java.util.HashMap;

import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;

public interface ConfigurableListViewHolder {
		
	/**
	 * The key used to identify this object as a parameter
	 */
	public static final String CONFIGURABLE_LIST_VIEW_HOLDER_KEY = "CONFIGURABLE_LIST_VIEW_HOLDER_KEY";
	
	public boolean isSelected();
	
	public void setSelected(boolean p_bIsSelected);
	
	/**
	 * The viewModelID
	 */
	public String getViewModelID();
	
	public void setViewModelID(String p_sViewModelId);
	
	public String getViewGroupHashCode();
	
	public void setViewGroupHashCode(String p_sViewGroupHashCode);
	
	/**
	 * Get the map of components indexed by ID
	 * @return a Map of component indexed by ID
	 */
	public HashMap<Integer, ConfigurableVisualComponent> getComponentsById();
	
	/**
	 * Get the map of components indexed by path
	 * @return a Map of component indexed by path
	 */
	public HashMap<String, ConfigurableVisualComponent> getComponentsByPath();
	
	/**
	 * Add a component by id to the holder	
	 * @param p_lId the Id of the component	to add
	 * @param p_oComponent the component to add
	 */
	public void addComponentById(int p_lId, ConfigurableVisualComponent p_oComponent);
	
	/**
	 * Add a component by path to the holder	
	 * @param p_lId the Id of the component	to add
	 * @param p_oComponent the component to add
	 */
	public void addComponentByPath(String p_sPath, ConfigurableVisualComponent p_oComponent);
	
	/**
	 * Removes a component by Id from the holder
	 * @param p_lId the Id of the component to remove
	 */
	public void removeComponentById(int p_lId);
	
	/**
	 * Returns a component hold by the ViewHolder 
	 * @param p_lId The id of the component to get
	 * @return A ConfigurableVisualComponent or null
	 */
	public ConfigurableVisualComponent getComponentById(int p_lId);
	
	/**
	 * Returns a component hold by the ViewHolder 
	 * @param p_sPath The path of the component to get
	 * @return A ConfigurableVisualComponent or null
	 */
	public ConfigurableVisualComponent getComponentByPath(String p_sPath);

	public View getItemView();
	
	/** returns the master component of the row held by the view holder */
	public MasterVisualComponent getMaster();
	
	/** sets the master component of the row held by the view holder */
	public void setMaster(MasterVisualComponent p_oMaster);
}
