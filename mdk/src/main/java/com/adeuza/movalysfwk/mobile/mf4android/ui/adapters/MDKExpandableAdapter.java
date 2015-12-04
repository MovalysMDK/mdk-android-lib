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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * Adapter for list with 2 levels
 * @param <ITEM> entity for level 2
 * @param <SUBITEM> entity for level 1
 * @param <SUBITEMVM> viewmodel for level 1
 * @param <ITEMVM> viewmodel for level 2
 */
public class MDKExpandableAdapter
	<ITEM extends MIdentifiable, 
	SUBITEM extends MIdentifiable, 
	SUBITEMVM extends ItemViewModel<SUBITEM>, 
	ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>>
	extends MDKBaseAdapter {

	private ListViewModel<ITEM, ITEMVM> masterVM ;

	/**
	 * android id of group layout (the name of file) for item
	 */
	private int groupLayout = 0;

	/**
	 * android id of child layout (the name of file) for item
	 */
	private int childLayout = 0;
	
	/**
	 * android id of configurable group layout element in the file for item
	 */
	private int configurableGroupLayoutId = 0;
	
	/**
	 * coat for the child layout that add a checkbox on it
	 */
	private int childCoatLayout = 0;
	
	/**
	 * android id of coat for the child layout
	 */
	private int childCoatContentLayoutId = 0;
	
	/** 
	 * Construct a new MDKExpandableAdapter
	 * 
	 * @param p_oMasterVM list's view model
	 * @param p_iGroupLayout id of layout of the Group (the file name)
	 * @param p_iConfigurableGroupLayoutId id of configurable layout of the Group (the first component)
	 * @param p_iChildLayout id of layout of the child (the file name)
	 * @param p_iConfigurableChildLayoutId id of configurable layout of the child (the first component)
	 */
	public MDKExpandableAdapter( ListViewModel<ITEM, ITEMVM> p_oMasterVM, 
			int p_iGroupLayout, int p_iConfigurableGroupLayoutId, int p_iChildLayout, int p_iConfigurableChildLayoutId ) {
		super();
		this.masterVM = p_oMasterVM;
		this.groupLayout = p_iGroupLayout;
		this.childLayout = p_iChildLayout;
	}
	
	/**
	 * Get child count for given group
	 * @param p_iPosition
	 * @return
	 */
	public int getChildrenCount(int p_iPosition) {
		ITEMVM oCurrentItemExtVM = this.getGroup(p_iPosition);
		ListViewModel<?, ?> listChild = oCurrentItemExtVM.getComposite();
		if (listChild == null) {
			return 0;
		} else {
			return listChild.getCount();
		}
	}

	/**
	 * Get group count
	 * @return group count
	 */
	public int getGroupCount() {
		return this.masterVM.getCount();
	}
	
	/**
	 * Get child item at given position in given group
	 * @param p_iGroupPosition group position
	 * @param p_iChildPosition child position
	 * @return
	 */
	public SUBITEMVM getChild(int p_iGroupPosition, int p_iChildPosition) {
		ListViewModel<SUBITEM, SUBITEMVM> listChild = this.getGroup(p_iGroupPosition).getComposite();
		if (listChild == null) {
			return null;
		} else {
			return listChild.getCacheVMByPosition(p_iChildPosition);
		}
	}
	
	/**
	 * Get group at given position
	 * @param p_iGroupPosition group position
	 * @return group item
	 */
	public ITEMVM getGroup(int p_iGroupPosition) {
		return this.masterVM.getCacheVMByPosition(p_iGroupPosition);
	}
	
	/**
	 * Get child id for given group and child positions
	 * @param p_iGroupPosition group position
	 * @param p_iChildPosition child position
	 * @return child id
	 */
	public long getChildId(int p_iGroupPosition, int p_iChildPosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getChild(p_iGroupPosition, p_iChildPosition).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getChild(p_iGroupPosition, p_iChildPosition).getIdVM().hashCode();
		}

		return i;
	}

	/**
	 * Get groupd id at given position
	 * @param p_iGroupPosition group position
	 * @return group id
	 */
	public long getGroupId(int p_iGroupPosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getGroup(p_iGroupPosition).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getGroup(p_iGroupPosition).getIdVM().hashCode();
		}

		return i;
	}

	/**
	 * Get child layout
	 * 
	 * @return child layout
	 */
	public int getChildLayout() {
		return this.childLayout;
	}
	
	/**
	 * Returns the layout for the group view
	 * @return the layout for the group view
	 */
	public int getGroupLayout() {
		return this.groupLayout;
	}
	
	/**
	 * @return
	 */
	public int getConfigurableGroupLayoutId() {
		return this.configurableGroupLayoutId;
	}
	
	/**
	 * Return viewmodel
	 * @return viewmodel
	 */
	public ListViewModel<ITEM, ITEMVM> getMasterVM() {
		return this.masterVM;
	}

	/**
	 * This method can be overriden to wrap the components in the
	 * MasterVisualCommponent Child item
	 * 
	 * @param p_oView
	 *            the current view
	 * @param p_oCurrentViewModel
	 *            The view Model of this child item
	 * @param p_iParamGroupPosition
	 *            the position of the current view in the list
	 * @param p_iParamChildPosition
	 *            the position of the current child view in the list
	 */
	public void postInflateChildView(View p_oView) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	/**
	 * @param p_oView
	 * @param p_oCurrentViewModel
	 * @param p_iParamGroupPosition
	 * @param p_iParamChildPosition
	 */
	public void postBindChildView(View p_oView,
			SUBITEMVM p_oCurrentViewModel, int p_iParamGroupPosition,
			int p_iParamChildPosition) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	/**
	 * @param p_oView
	 * @param p_bExpanded
	 */
	public void postInflateGroupView(View p_oView, boolean p_bExpanded) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	/**
	 * @param p_oView
	 * @param p_oCurrentViewModel
	 * @param p_iParamGroupPosition
	 * @param p_bExpanded
	 */
	public void postBindGroupView(View p_oView,
			ITEMVM p_oCurrentViewModel, int p_iParamGroupPosition, boolean p_bExpanded) {
		// nothing to do in this implementation, the automatic mapping is used
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
	
	/**
	 * Returns a view holder for a group view
	 * @param p_oView 
	 * @return a view holder for a group view
	 */
	public ConfigurableListViewHolder createGroupViewHolder(View p_oView, boolean p_bExpanded) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableExpandedListViewHolder", 
				new Class[]{ View.class, Boolean.TYPE }, 
				new Object[]{ p_oView, p_bExpanded });
	}
	
	/**
	 * Définis un Layout qui va englober l'item enfant d'un expandableListView.
	 * 
	 * @param p_iChildCoatLayout
	 *            identifiant du layout qui va englober l'item
	 * @param p_iChildCoatContentId
	 *            identifiant de la View qui va contenir l'item
	 */
	public void setChildCoat(int p_iChildCoatLayout, int p_iChildCoatContentId) {
		this.childCoatLayout = p_iChildCoatLayout;
		this.childCoatContentLayoutId = p_iChildCoatContentId;
	}
	
	/**
	 * returns the coat layout for the child view
	 * @return the coat layout for the child view
	 */
	public int getChildCoatLayout() {
		return this.childCoatLayout;
	}

	/**
	 * returns the layout identifier of the coat layout for the child view
	 * @return the layout identifier of the coat layout for the child view
	 */
	public int getChildCoatContentLayoutId() {
		return this.childCoatContentLayoutId;
	}
	
	/**
	 * Coat an item view
	 * @param p_oView the current view to coat
	 * @param p_oCurrentViewModel the current SUBITEMVM to coat
	 */
	public void wrapCoat(View p_oView, SUBITEMVM p_oCurrentViewModel) {
		/* Nothing to do here. Just for override */
	}

	public void setSelectedItem(String p_sItemId) {
		// NOTHING TO DO
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetSelectedItem() {
		// Nothing to do
	}
}
