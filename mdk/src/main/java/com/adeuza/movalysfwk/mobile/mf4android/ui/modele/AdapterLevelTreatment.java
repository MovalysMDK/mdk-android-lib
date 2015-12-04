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

import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Describes the processes to execute for a given view level of an adapter
 * @param <ADAPTER> the class of the linked adapter 
 */
public interface AdapterLevelTreatment<ADAPTER> {
	
	/**
	 * Return the view model for the processed view level
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oPositions the positions of the processed view through all levels
	 * @return the view model of the view
	 */
	public ViewModel getCurrentViewModel(ADAPTER p_oAdapter, int ... p_oPositions);
	
	/**
	 * Done before the view model gets processed
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_bExpanded true if the view is expanded
	 * @param p_oView the view being processed
	 * @param p_oViewGroup the view group containing the current view
	 * @param p_oCurrentVM the view model of the current view
	 * @param p_oPositions the positions of the processed view through all levels
	 */
	public void doBeforeSetViewModelProcess(ADAPTER p_oAdapter, boolean p_bExpanded, View p_oView, ViewModel p_oCurrentVM, int ... p_oPositions);
	
	/**
	 * Returns true if the current view needs to be inflated
	 * @param p_oCurrentRow the row associated to the current view
	 * @param p_iViewType type of the view
	 * @return true if the current view needs to be inflated
	 */
	public boolean needToInflateView(View p_oCurrentRow, int p_iViewType);
	
	/**
	 * Returns the id of the layout to use for the view
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oPositions the positions of the processed view through all levels
	 * @return the id of the layout to use for the view
	 */
	public int getItemLayout(ADAPTER p_oAdapter, int p_iViewType);
	
	/**
	 * Return the view holder for the current view
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oView the view to bind holder to
	 * @param p_bExpanded true if the view is expanded
	 * @return the view holder for the current view
	 */
	public ConfigurableListViewHolder getViewHolder(ADAPTER p_oAdapter, View p_oView, boolean p_bExpanded);
	
	/**
	 * Done after the row has been inflated
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oLayoutInflater the inflater used on the view
	 * @param p_oCurrentRow the row associated to the current view
	 * @param p_oView the view being processed
	 */
	public void doAfterInflateRow(ADAPTER p_oAdapter, LayoutInflater p_oLayoutInflater, View p_oCurrentRow, View p_oView);
	
	/**
	 * Done after the view was restored from a view holder
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oCurrentRow the row associated to the current view
	 * @param p_oCompositeComponent the composite component linked to the view
	 */
	public void doAfterRestoreRow(ADAPTER p_oAdapter, View p_oCurrentRow, MasterVisualComponent p_oCompositeComponent);
	
	/**
	 * Return the parameters used to inflate the view
	 * @param p_oHolder the holder linked to the view
	 * @return the parameters used to inflate the view
	 */
	public Map<String, Object> getInflateParameters(ConfigurableListViewHolder p_oHolder);
	
	/**
	 * Done after the view model was processed
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_bExpanded true if the view is expanded
	 * @param p_oCurrentRow the row associated to the current view
	 * @param p_oViewGroup the view group containing the current view
	 * @param p_oCurrentVM the view model of the current view
	 * @param p_oCompositeComponent the composite component linked to the view
	 * @param p_oPositions the positions of the processed view through all levels
	 */
	public void doAfterSetViewModelProcess(ADAPTER p_oAdapter, boolean p_bExpanded, ConfigurableListViewHolder p_oViewholder, ViewModel p_oCurrentVM, MasterVisualComponent p_oCompositeComponent, int ... p_oPositions);
	
	/**
	 * This method is called after inflate has been called. Can be use to add/modify/remove views.
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oCurrentRow the row associated to the current view
	 * @param p_oCurrentVM the view model of the current view
	 * @param b_isExpanded true if the view is expanded
	 * @param p_oPositions the positions of the processed view through all levels
	 */
	public void postInflate(ADAPTER p_oAdapter, View p_oCurrentRow, boolean b_isExpanded);
	
	/**
	 * This method is called after data in view has been binded. Can be use to customize data in view.
	 * @param p_oAdapter
	 * @param p_oCurrentRow
	 * @param p_oCurrentVM
	 * @param b_isExpanded
	 * @param p_oPositions
	 */
	public void postBind(ADAPTER p_oAdapter, View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded, int ... p_oPositions);
	
	/**
	 * Called when the view has no linked view model
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oLayoutInflater the inflater used on the view
	 * @param p_oCurrentRow the row associated to the current view
	 * @param p_oHolder the holder linked to the view
	 * @param p_oPositions the positions of the processed view through all levels
	 * @return the actual view to return from the adapter
	 */
	public View inflateOnNullViewModel(ADAPTER p_oAdapter, LayoutInflater p_oLayoutInflater, View p_oCurrentRow, ConfigurableListViewHolder p_oHolder, int ... p_oPositions);
	
	/**
	 * Returns the actual view to return from the adapter
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oCurrentRow the row associated to the current view
	 * @param p_oCurrentVM the view model of the current view
	 * @return the actual view to return from the adapter
	 */
	public View getViewToReturn(ADAPTER p_oAdapter, View p_oCurrentRow, ViewModel p_oCurrentVM);
	
	/**
	 * Done after the Data Loaded
	 * @param p_oAdapter the instance of the linked adapter
	 * @param p_oCurrentRow the row associated to the current view
	 * @param p_oViewGroup the view group containing the current view
	 */
	public void doAfterOnDataLoaded(ADAPTER p_oAdapter, ConfigurableListViewHolder p_oViewholder);
}
