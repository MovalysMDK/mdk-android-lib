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

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;

/**
 * This is a classic ViewHolder implementation used to retain some references
 * of the components of the items of a ListView
 */
public class ConfigurableListViewHolderImpl extends RecyclerView.ViewHolder implements ConfigurableListViewHolder {
	
	public boolean isSelected;
	/**
	 * The viewModelID
	 */
	public String viewModelID;
	
	public String viewGroupHashCode;
	
	/**
	 * A map that hold components by path
	 */
	private  HashMap<String, ConfigurableVisualComponent> componentsByPath = new HashMap<>();
	
	/**
	 * A map that hold components by Id
	 */
	private  HashMap<Integer, ConfigurableVisualComponent> componentsById = new HashMap<>();
	
	/** the master component of the row held by the view holder */ 
	private MasterVisualComponent master;
	
	/**
	 * Constructeur
	 */
	public ConfigurableListViewHolderImpl( View p_oItemView ) {
		super( p_oItemView );
		this.componentsById = new HashMap<>();
		this.componentsByPath = new HashMap<>();
		this.isSelected = false;
	}
	
	
	
	/**
	 * Get the map of components indexed by ID
	 * @return a Map of component indexed by ID
	 */
	public HashMap<Integer, ConfigurableVisualComponent> getComponentsById() {
		return componentsById;
	}
	
	/**
	 * Get the map of components indexed by path
	 * @return a Map of component indexed by path
	 */
	public HashMap<String, ConfigurableVisualComponent> getComponentsByPath() {
		return componentsByPath;
	}
	
	/**
	 * Add a component by id to the holder	
	 * @param p_lId the Id of the component	to add
	 * @param p_oComponent the component to add
	 */
	public void addComponentById(int p_lId, ConfigurableVisualComponent p_oComponent) {
		this.componentsById.put(p_lId, p_oComponent);
	}
	
	/**
	 * Add a component by path to the holder	
	 * @param p_lId the Id of the component	to add
	 * @param p_oComponent the component to add
	 */
	public void addComponentByPath(String p_sPath, ConfigurableVisualComponent p_oComponent) {
		this.componentsByPath.put(p_sPath, p_oComponent);
	}
	
	/**
	 * Removes a component by Id from the holder
	 * @param p_lId the Id of the component to remove
	 */
	public void removeComponentById(int p_lId) {
		this.componentsById.remove(p_lId);
	}
	
	/**
	 * Returns a component hold by the ViewHolder 
	 * @param p_lId The id of the component to get
	 * @return A ConfigurableVisualComponent or null
	 */
	public ConfigurableVisualComponent getComponentById(int p_lId) {
		return this.componentsById.get(p_lId);
	}
	
	/**
	 * Returns a component hold by the ViewHolder 
	 * @param p_sPath The path of the component to get
	 * @return A ConfigurableVisualComponent or null
	 */
	public ConfigurableVisualComponent getComponentByPath(String p_sPath) {
		return this.componentsByPath.get(p_sPath);
	}



	@Override
	public boolean isSelected() {
		return this.isSelected;
	}



	@Override
	public void setSelected(boolean p_bIsSelected) {
		this.isSelected = p_bIsSelected;
	}



	@Override
	public String getViewModelID() {
		return this.viewModelID;
	}



	@Override
	public void setViewModelID(String p_sViewModelId) {
		this.viewModelID = p_sViewModelId;
	}



	@Override
	public String getViewGroupHashCode() {
		return this.viewGroupHashCode;
	}

	@Override
	public void setViewGroupHashCode(String p_sViewGroupHashCode) {
		this.viewGroupHashCode = p_sViewGroupHashCode;
	}

	@Override
	public View getItemView() {
		return this.itemView;
	}

	@Override
	public MasterVisualComponent getMaster() {
		return this.master;
	}

	@Override
	public void setMaster(MasterVisualComponent p_oMaster) {
		this.master = p_oMaster;
	}
}
