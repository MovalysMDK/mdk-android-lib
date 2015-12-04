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
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableExpandedListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * <p>3 levels list component</p>
 * @param <ITEM> level 1 item class
 * @param <SUBITEM> level 2 item class 
 * @param <SUBSUBITEM> level 3 item class
 * @param <SUBSUBITEMVM> level 3 view model class
 * @param <SUBITEMVM> level 2 view model class
 * @param <ITEMVM> level 1 view model class
 */
public class MDKFlipperAdapter<
		ITEM extends MIdentifiable, 
		SUBITEM extends MIdentifiable, 
		SUBSUBITEM extends MIdentifiable, 
		SUBSUBITEMVM extends ItemViewModel<SUBSUBITEM>, 
		SUBITEMVM extends ExpandableViewModel<SUBITEM, SUBSUBITEM, SUBSUBITEMVM>, 
		ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> 
	extends MDKBaseAdapter {

	/** the list model */
	private ListViewModel<ITEM, ITEMVM> masterVM = null;

	/** android id of Title layout (the name of file) for item */
	private int titleLayout = 0;
	
	/** android id of configurable title layout element in the file for item */
	private int configurableTitleLayoutId = 0;

	/** android id of group layout (the name of file) for item */
	private int groupLayout = 0;
	
	/** android id of configurable group layout element in the file for item */
	private int configurableGroupLayoutId = 0;

	/** android id of child layout (the name of file) for item */
	private int childLayout = 0;
	
	/** android id of configurable child layout element in the file for item */
	private int configurableChildLayoutId = 0;
	
	public MDKFlipperAdapter(ListViewModel<ITEM, ITEMVM> p_oMasterVM, int p_iTitleLayout,
			int p_iConfigurableTitleLayoutId, int p_iGroupLayout,int p_iConfigurableGroupLayoutId, int p_iChildLayout,
			int p_iConfigurableChildLayoutId) {
		super();
		this.masterVM = p_oMasterVM;
		this.titleLayout = p_iTitleLayout;
		this.configurableTitleLayoutId = p_iConfigurableTitleLayoutId;
		this.groupLayout = p_iGroupLayout;
		this.configurableGroupLayoutId = p_iConfigurableGroupLayoutId;
		this.childLayout = p_iChildLayout;
		this.configurableChildLayoutId = p_iConfigurableChildLayoutId;
	}

	public int getChildrenCount(int p_iTitlePosition, int p_iGroupPosition) {
		ITEMVM oCurrentItemExtVM = this.getTitle(p_iTitlePosition);
		int r_iCount = 0 ;
		if ( oCurrentItemExtVM.getComposite() != null ) {
			ListViewModel<?, ?> listChildsList = this.getGroup(p_iTitlePosition,p_iGroupPosition).getComposite();
			if (listChildsList != null) {
				r_iCount = listChildsList.getCount();
			}
		}
		return r_iCount ;
	}

	public int getGroupCount(int p_iTitlePosition) {
		return  this.getTitle(p_iTitlePosition).getComposite().getCount();
	}

	public int getTitleCount() {
		return this.masterVM.getCount();
	}

	/**
	 * Get the title model for this position
	 * @param p_iParamTitle the position
	 * @return the ITEMVM model for title
	 */
	public ITEMVM getTitle(int p_iTitlePosition) {
		return this.masterVM.getCacheVMByPosition(p_iTitlePosition);
	}

	/**
	 * Get the Group model for this position
	 * @param p_iTitlePosition The position of the first level of the triple list
	 * @param p_iGroupPosition the position of the group view in the list
	 * @return the SUBITEMVM model for Group
	 */
	public SUBITEMVM getGroup(int p_iTitlePosition, int p_iGroupPosition) {
		ListViewModel<SUBITEM, SUBITEMVM> listGroup = (ListViewModel<SUBITEM, SUBITEMVM>) this
				.getTitle(p_iTitlePosition).getComposite();
		if (listGroup == null) {
			return null;
		} else {
			return listGroup.getCacheVMByPosition(p_iGroupPosition);
		}
	}

	/**
	 * Get the Child model for this position
	 * @param p_iTitlePosition The position of the first level of the triple list
	 * @param p_iGroupPosition the position of the group view in the list
	 * @param p_iChildPosition the position of the current view in the child list
	 * @return the SUBSUBITEMVM model for Child
	 */
	public SUBSUBITEMVM getChild(int p_iTitlePosition, int p_iGroupPosition, int p_iChildPosition) {
		SUBSUBITEMVM r_oItem = null;
		ListViewModel<SUBSUBITEM, SUBSUBITEMVM> listChild = this.getGroup(p_iTitlePosition, p_iGroupPosition).getComposite();
		if (listChild == null) {
			r_oItem = null;
		} else {
			r_oItem = listChild.getCacheVMByPosition(p_iChildPosition);
		}
		return r_oItem;
	}
	
	public long getTitleId(int p_iTitlePosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getTitle(p_iTitlePosition).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getTitle(p_iTitlePosition).getIdVM().hashCode();
		}
		
		return i;
	}
	
	public long getGroupId(int p_iTitlePosition, int p_iGroupPosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getGroup(p_iTitlePosition, p_iGroupPosition).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getGroup(p_iTitlePosition, p_iGroupPosition).getIdVM().hashCode();
		}
		
		return i;
	}
	
	public long getChildId(int p_iTitlePosition, int p_iGroupPosition, int p_iChildPosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getChild(p_iTitlePosition, p_iGroupPosition, p_iChildPosition).getIdVM());
		} catch (NumberFormatException | NullPointerException e ) {
			i = this.getChild(p_iTitlePosition, p_iGroupPosition, p_iChildPosition).getIdVM().hashCode();
		}
		
		return i;
	}
	
	/**
	 * Returns the layout for the title view
	 * @return the layout for the title view
	 */
	public int getTitleLayout() {
		return this.titleLayout;
	}
	
	/**
	 * Returns the layout for the group view
	 * @return the layout for the group view
	 */
	public int getGroupLayout() {
		return this.groupLayout;
	}
	
	/**
	 * Returns the layout for the child view
	 * @return the layout for the child view
	 */
	public int getChildLayout() {
		return this.childLayout;
	}
	
	/**
	 * Returns the configurable layout id for the title view
	 * @return the configurable layout id for the title view
	 */
	public int getConfigurableTitleLayoutId() {
		return this.configurableTitleLayoutId;
	}
	
	/**
	 * Returns the configurable layout id for the group view
	 * @return the configurable layout id for the group view
	 */
	public int getConfigurableGroupLayoutId() {
		return this.configurableGroupLayoutId;
	}
	
	/**
	 * Returns the configurable layout id for the child view
	 * @return the configurable layout id for the child view
	 */
	public int getConfigurableChildLayoutId() {
		return this.configurableChildLayoutId;
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
			SUBSUBITEMVM p_oCurrentViewModel, int p_iParamGroupPosition,
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
			SUBITEMVM p_oCurrentViewModel, int p_iParamGroupPosition, boolean p_bExpanded) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	/**
	 * @param p_oView
	 * @param p_bExpanded
	 */
	public void postInflateTitleView(View p_oView, boolean p_bExpanded) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	/**
	 * @param p_oView
	 * @param p_oCurrentViewModel
	 * @param p_iParamGroupPosition
	 * @param p_bExpanded
	 */
	public void postBindTitleView(View p_oView,
			ITEMVM p_oCurrentViewModel, int p_iParamGroupPosition, boolean p_bExpanded) {
		// nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * Creates and return a ConfigurableListViewHolder.
	 * @return An empty ConfigurableListViewHolder object.
	 */
	public ConfigurableListViewHolder createViewHolderChildItem(View p_oView) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder", 
				new Class[]{ View.class }, 
				new Object[]{ p_oView });
	}
	
	/**
	 * Returns a view holder for the group view
	 * @param p_bExpanded true if the group view is expanded
	 * @return a view holder for the group view
	 */
	public ConfigurableListViewHolder createViewHolderGroupItem(View p_oView, boolean p_bIsExpanded) {
		return (ConfigurableExpandedListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableExpandedListViewHolder", 
				new Class[]{ View.class, Boolean.TYPE }, 
				new Object[]{ p_oView, p_bIsExpanded });
	}
	
	/**
	 * Returns a view holder for the title view
	 * @param p_bExpanded true if the title view is expanded
	 * @return a view holder for the title view
	 */
	public ConfigurableListViewHolder createViewHolderTitleItem(View p_oView, boolean p_bExpanded) {
		return (ConfigurableExpandedListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableExpandedListViewHolder", 
				new Class[]{ View.class, Boolean.TYPE }, 
				new Object[]{ p_oView, p_bExpanded });
	}
	
	@Override
	public void resetSelectedItem() {
		// TODO Auto-generated method stub

	}

}
