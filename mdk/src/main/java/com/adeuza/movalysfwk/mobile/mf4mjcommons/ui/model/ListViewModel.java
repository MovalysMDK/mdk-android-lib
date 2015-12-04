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

import java.util.Collection;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;

/**
 * <p>Main interface for view model, use this interface for list representation</p>
 *
 *
 * @param <ITEMVM> view model type for item
 */
public interface ListViewModel<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends ViewModel {

	/**
	 * Set the list of object
	 *
	 * @param p_oList a {@link java.util.Collection} object.
	 */
	public void setItems(Collection<ITEM> p_oList);
	/**
	 * Add a new <code>ViewModel</code> object in the list at first position.
	 *
	 * @param p_oItem the <code>ViewModel</code> to add
	 */
	public void addFirst(ITEMVM p_oItem);
	
	/**
	 * Add a new <code>ViewModel</code> object in the list.
	 *
	 * @param p_oItem the <code>ViewModel</code> to add
	 */
	public void add(ITEMVM p_oItem);
	
	/**
	 * Add a list of <code>ViewModel</code> object in the list.
	 *
	 * @param p_oItem the list of <code>ViewModel</code> to add in the list.
	 */
	public void addAll(Collection<ITEMVM> p_oItem);
	
	/**
	 * Add a new <code>ViewModel</code> object in the list at first position.
	 *
	 * @param p_oItem the <code>ViewModel</code> to add
	 * @param p_bAddToFilteredList Indicates if an item should be add to the filtered list
	 */
	public void addFirst(ITEMVM p_oItem, boolean p_bAddToFilteredList);
	
	/**
	 * Add a new <code>ViewModel</code> object in the list.
	 *
	 * @param p_oItem the <code>ViewModel</code> to add
	 * @param p_bAddToFilteredList Indicates if an item should be add to the filtered list
	 */
	public void add(ITEMVM p_oItem, boolean p_bAddToFilteredList);
	
	/**
	 * Add a list of <code>ViewModel</code> object in the list.
	 *
	 * @param p_oItem the list of <code>ViewModel</code> to add in the list.
	 * @param p_bAddToFilteredList Indicates if items should be add to the filtered list
	 */
	public void addAll(Collection<ITEMVM> p_oItem, boolean p_bAddToFilteredList);
	
	/**
	 * Return the list of the selected items.
	 *
	 * @return a list of items. It can be null.
	 */
	public List<? extends ItemViewModel<?>> getSelectedItemList();
	
	/**
	 * Define the list of item that will be selected.
	 *
	 * @param p_oSelectedItemList list of items
	 */
	public void setSelectedItemList(List<? extends ItemViewModel<?>> p_oSelectedItemList);
	
	/**
	 * Update a <code>ViewModel</code> object in the list.
	 * The item can be null.
	 *
	 * @param p_oItem the <code>ViewModel</code> to update
	 */
	public void update(ITEMVM p_oItem);
	
	/**
	 * Update a <code>ViewModel</code> object in the list.
	 * The item can be null.
	 *
	 * @param p_iIndexOfItem the new index of the item
	 * @param p_oItem the <code>ViewModel</code> to update
	 */
	public void update(int p_iIndexOfItem, ITEMVM p_oItem);
	
	/**
	 * Remove a <code>ViewModel</code> object in the list.
	 * The item can be null.
	 *
	 * @param p_oItem the <code>ViewModel</code> to remove
	 */
	public void remove(ITEMVM p_oItem);	
	
	/**
	 * Clear the list.
	 */
	@Override
	public void clear();
	
	/**
	 * Returns the item view model in cache. If view model is not in cache returns null.
	 *
	 * @param p_iPosition the position of object to find
	 * @return an item view model
	 */
	public ITEMVM getCacheVMByPosition(int p_iPosition);
	
	/**
	 * Returns the item view model in cache. If view model is not in cache returns null.
	 *
	 * @param p_sId the id of object to find
	 * @return an item view model
	 */
	public ITEMVM getCacheVMById(String p_sId);
	
	/**
	 * Returns the item view model in cache. If view model is not in cache returns null.
	 *
	 * @param p_oViewModel the position of object to find
	 * @return an item view model
	 */
	public ITEMVM getCacheVMById(ITEM p_oViewModel);
	
	/**
	 * Count number of items
	 *
	 * @return number of items
	 */
	public int getCount();
	

	/**
	 * Get the position of the provided item in the list
	 *
	 * @param p_oItem the item to search for
	 * @return the position
	 */
	public int indexOf(ITEMVM p_oItem);
	

	/**
	 * <p>modifyToIdentifiable.</p>
	 *
	 * @param p_oIdentifiables a {@link java.util.List} object.
	 */
	public void modifyToIdentifiable(List<ITEM> p_oIdentifiables);
	
	/**
	 * <p>updateFromIdentifiable.</p>
	 *
	 * @param p_oIdentifiable a ITEM object.
	 */
	public void updateFromIdentifiable(ITEM p_oIdentifiable);
	
	/**
	 * Add a parameter to the optionnal parameters map
	 *
	 * @param p_oKey Parameter key to add to the map
	 * @param p_oParamValue Parameter value to add to the map
	 */
	public void addParam(Object p_oKey, Object p_oParamValue);
	
	/**
	 * Get the given key's parameter value
	 *
	 * @param p_oKey The key
	 * @return The parameter value
	 */
	public Object getParam(Object p_oKey);
	
	/**
	 * Register a new observer
	 *
	 * @param p_oObserver the observer to register
	 */
	public void registerObserver(DataSetObserver p_oObserver);
	
	/**
	 * Unregister an observer
	 *
	 * @param p_oObserver the observer to unregister
	 */
	public void unregisterObserver(DataSetObserver p_oObserver);
	
	/**
	 * return the class of the ITEMVM interface
	 *
	 * @return the ITEMVM interface
	 */
	Class<ITEMVM> getItemVmInterface();
	
	/**
	 * filter the List view model with the filter
	 *
	 * @param p_oFilter a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelFilter} object.
	 * @param p_oParam a PARAM object.
	 * @param <PARAM> a PARAM object.
	 */
	public <PARAM> void filter(ListViewModelFilter<ITEM, ITEMVM, PARAM> p_oFilter, PARAM... p_oParam);
	
	/**
	 * Call this to trigger the collection changed listener of the list
	 */
	public void notifyCollectionChanged();

}
