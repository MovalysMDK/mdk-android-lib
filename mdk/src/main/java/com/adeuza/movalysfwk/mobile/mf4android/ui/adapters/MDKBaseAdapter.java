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
package com.adeuza.movalysfwk.mobile.mf4android.ui.adapters;

import java.util.HashSet;
import java.util.Set;

import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ListAdapterDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;

/**
 * BaseAdapter for collections
 *
 */
public abstract class MDKBaseAdapter {

	/** Binded components */
	private Set<MasterVisualComponent> mComponents = new HashSet<>();
	
	/** Connector for communication with the view. */
	private MDKViewConnector mViewConnector;
	
	/** list delegate of the adapter */
	protected ListAdapterDelegate mAdapterDelegate;
	
	/** referenced adapters for the detail view. */
	protected SparseArrayCompat<MDKBaseAdapter> referencedAdapter;
	
	/** Reset selected item */
	public abstract void resetSelectedItem();
	
	public MDKBaseAdapter() {
		this.referencedAdapter = new SparseArrayCompat<>();
	}
	
	/**
	 * Define adapter delegate
	 * @param p_oAdapterDelegate adapter delegate
	 */
	public void setAdapterDelegate(ListAdapterDelegate p_oAdapterDelegate) {
		this.mAdapterDelegate = p_oAdapterDelegate;
	}
	
	/**
	 * Return view for list item
	 * @param p_oLevel level
	 * @param p_bExpanded expanded or not
	 * @param p_oView view
	 * @param p_oViewGroup parent view
	 * @param p_iViewType view type
	 * @param p_iPositions positions
	 * @return view for list item
	 */
	public View getLegacyViewByLevel(AdapterLevel p_oLevel, boolean p_bExpanded, View p_oView, 
			ViewGroup p_oViewGroup, int p_iViewType, int... p_iPositions) {
		return this.mAdapterDelegate.getViewByLevel(p_oLevel, p_bExpanded, p_oView, p_oViewGroup, p_iViewType, p_iPositions);
	}
	
	/**
	 * Create viewholder for list item
	 * @param p_oLevel level
	 * @param p_bExpanded expanded or not
	 * @param p_oViewGroup parent view
	 * @param p_iViewType view type
	 * @return
	 */
	public ConfigurableListViewHolder onCreateViewHolder(AdapterLevel p_oLevel, boolean p_bExpanded, ViewGroup p_oViewGroup, int p_iViewType) {
		return this.mAdapterDelegate.onCreateViewHolder(p_oLevel, p_bExpanded, p_oViewGroup, p_iViewType);
	}
	
	/**
	 * Bind data in viewholder
	 * @param p_oLevel level
	 * @param p_bExpanded expanded or not
	 * @param p_oViewholder view holder
	 * @param p_iPositions positions
	 */
	public void onBindViewHolder(AdapterLevel p_oLevel, boolean p_bExpanded, ConfigurableListViewHolder p_oViewholder, int... p_iPositions) {
		this.mAdapterDelegate.onBindViewHolder(p_oLevel, p_bExpanded, p_oViewholder, p_iPositions);
	}
	
	/**
	 * Reset tags on components
	 */
	protected void resetComponentTags() {
		for (MasterVisualComponent oComponent : this.mComponents) {
			View oView = null;
			if (oComponent instanceof ComponentWrapper) {
				oView = ((ComponentWrapper) oComponent).getComponent();
			} else {
				oView = (View) oComponent;
			}
			if ( oView.getTag() != null ) {
				ConfigurableListViewHolder oConfigurableListViewHolder = (ConfigurableListViewHolder) oView.getTag();
				oConfigurableListViewHolder.setViewModelID(null);
			}
		}
	}
	
	/**
	 * Uninflate components
	 */
	public void uninflate() {
		for (MasterVisualComponent oCompositeComponent : this.mComponents) {
			oCompositeComponent.getDescriptor().unInflate(null,
					oCompositeComponent, null);
		}
		this.resetComponentTags();
		this.mComponents.clear();
	}
	
	/**
	 * Returns the components list
	 * @return the components
	 */
	public Set<MasterVisualComponent> getComponents() {
		return mComponents;
	}
	
	/**
	 * Return view connector
	 * @return view connector
	 */
	public MDKViewConnector getViewConnector() {
		return mViewConnector;
	}

	/**
	 * Set view connector
	 * @param p_oConnector
	 */
	public void setViewConnector( MDKViewConnector p_oConnector) {
		this.mViewConnector = p_oConnector;
	}

	/**
	 * Add a reference to a adapter in a component (used with combo)
	 * @param p_iComponent the component id
	 * @param p_oAdapter the adapter to link
	 */
	public void addReferenceTo(int p_iComponent, MDKBaseAdapter p_oAdapter) {
		this.referencedAdapter.put(p_iComponent, p_oAdapter);
	}
	
	/**
	 * Notify adapter that dataset has been updated.
	 */
	public void notifyDataSetChanged() {
		this.mViewConnector.notifyDataSetChanged();
		this.resetComponentTags();
	}
}
