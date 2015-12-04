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

import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * Generic adapter for list.
 * Can be used with ListView and RecyclerView
 *
 * @param <ITEM> entity
 * @param <ITEMVM> item viewmodel
 * @param <LISTVM> list viewmodel
 */
public class MDKAdapter<
	ITEM extends MIdentifiable, 
	ITEMVM extends ItemViewModel<ITEM>, 
	LISTVM extends ListViewModel<ITEM, ITEMVM>>
	extends MDKBaseAdapter {

	/**
	 * Viewmodel
	 */
	private LISTVM masterVM ;
	
	/**
	 * Item layout
	 */
	private int layoutid = 0;
	
	/**
	 * Selected item
	 */
	private String selectedItem ;
	
	/**
	 * Constructor
	 * @param p_oMasterVM master viewmodel
	 * @param p_iLayoutId item layout
	 * @param p_iConfigurableLayout component id
	 */
	public MDKAdapter(LISTVM p_oMasterVM, int p_iLayoutId, int p_iConfigurableLayout ) {
		super();
		this.masterVM = p_oMasterVM;
		this.layoutid = p_iLayoutId;
	}
	
	/**
	 * Count items in list
	 * @return number of items in viewmodel
	 */
	public int getCount() {
		int r_iResult = 0;
		if (this.masterVM != null) {
			r_iResult = this.masterVM.getCount();
		}
		return r_iResult;
	}
	
	/**
	 * Return viewmodel item at given position
	 * @param p_iPosition
	 * @return
	 */
	public ITEMVM getItem(int p_iPosition) {
		return this.masterVM.getCacheVMByPosition(p_iPosition);
	}
	
	/**
	 * Return item id at given position
	 * @param p_iPosition
	 * @return item id
	 */
	public long getItemId(int p_iPosition) {
		return p_iPosition;
	}
	
	public void postInflate(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			View p_oCurrentRow, boolean b_isExpanded) {
		//nothing to do
	}
	
	public void postBind(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			View p_oCurrentRow, ITEMVM p_oCurrentVM, boolean b_isExpanded,
			int... p_oPositions) {
	}
	
	/**
	 * Replace all data of the adapter with the given viewmodel.
	 * @param p_oMasterVM viewmodel
	 */
	public void updateAdapter(LISTVM p_oMasterVM) {
		this.uninflate();
		this.getViewConnector().beforeViewModelChanged();
		this.masterVM = p_oMasterVM;
		this.getViewConnector().afterViewModelChanged();
	}
	
	/**
	 * Return viewmodel
	 * @return viewmodel
	 */
	public LISTVM getMasterVM() {
		return this.masterVM;
	}
	
	/**
	 * get the selectedItem Id used to highlight the selected item of the
	 * list (this item has the state_checked)
	 * 
	 * @return the <ITEM> Id with state_checked
	 */
	public String getSelectedItem() {
		return selectedItem;
	}
	
	/**
	 * get the selectedItem Id used to highlight the selected item of the
	 * list (this item has the state_checked)
	 * 
	 * @param the
	 *            Id of the Checked <ITEM>
	 * */
	public void setSelectedItem(String p_sSelectedItemId) {
		this.selectedItem = p_sSelectedItemId;
	}

	@Override
	public void resetSelectedItem() {
		this.selectedItem = null;
	}
	
	/**
	 * Returns the position of a given item in the adapter
	 * @param p_oItem the item to look for
	 * @return the position found
	 */
	public int indexOf(ITEMVM p_oItem) {
		return masterVM.indexOf(p_oItem);
	}
	
	/**
	 * Return layout to use.
	 * @param p_iViewType view type
	 * @return layout
	 */
	public int getItemLayout(int p_iViewType) {
		return this.layoutid;
	}

	/**
	 * Create an instance of the view holder
	 * @param p_oView associated view
	 * @return view holder
	 */
	public ConfigurableListViewHolder createViewHolder(View p_oView) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder", 
				new Class[]{ View.class }, 
				new Object[]{ p_oView });
	}
	
}
