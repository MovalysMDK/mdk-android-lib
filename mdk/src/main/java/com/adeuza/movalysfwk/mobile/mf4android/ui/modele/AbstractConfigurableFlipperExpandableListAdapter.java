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

import java.lang.reflect.InvocationTargetException;

import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.FlipperExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * <p>Adapter for an expandable list</p>
 * @deprecated USE {@link com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKFlipperAdapter} instead
 *<p> you can custimise the layouts component__expandable_list_groupitem__master and component__expandable_list_childitem__master
 * and implement {@link AbstractConfigurableFlipperExpandableListAdapter#wrapCurrentGroupView(android.view.View, com.adeuza.movalys.fwk.mobile.javacommons.uimodel.ExpandableViewModel, int)
 * and {@link AbstractConfigurableFlipperExpandableListAdapter#wrapCurrentChildView(android.view.View, com.adeuza.movalys.fwk.mobile.javacommons.uimodel.ViewModel, int, int)
 * to add custom features to this list </p>
 *
 *
 * @param <LISTVM> a model for list  
 * @param <ITEMVM> a model for item
 */
@Deprecated
public abstract class AbstractConfigurableFlipperExpandableListAdapter<
ITEM extends MIdentifiable, 
SUBITEM extends MIdentifiable, 
SUBSUBITEM extends MIdentifiable, 
SUBSUBITEMVM extends ItemViewModel<SUBSUBITEM>, 
SUBITEMVM extends ExpandableViewModel<SUBITEM, SUBSUBITEM, SUBSUBITEMVM>, 
ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>>
implements FlipperExpandableListAdapter {

	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	private enum Level implements AdapterLevel {
		/** Title level */
		TITLE(FlipperAdapterTitle.class),
		/** Group level */
		GROUP(FlipperAdapterGroup.class),
		/** Child level */
		CHILD(FlipperAdapterChild.class);
		
		/** treatments class for the level */
		private AdapterLevelTreatment level;
		
		/** 
		 * Constructor
		 * @param p_oLevelClass the treatment class for the level 
		 */
		Level(Class<? extends AdapterLevelTreatment> p_oLevelClass) {
			try {
				this.level = p_oLevelClass.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException 
					| NoSuchMethodException | SecurityException e) {
				Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			}
		}

		/**
		 * returns the treatment class for the level
		 * @return the treatment class for the level
		 */
		@Override
		public AdapterLevelTreatment getLevel() {
			return this.level;
		}
	}
	
	/** the list model */
	private ListViewModel<ITEM, ITEMVM> listMasterVM = null;

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

	/** adapter delegate */
	private ListAdapterDelegate delegate = new ListAdapterDelegateImpl(this);
	
	/**
	 * Construc a new ConfigurableAdapter
	 * 
	 * @param p_sName
	 *            the name of layout
	 * @param p_oMasterVM
	 *            list's view model
	 * @param p_iGroupLayout
	 *            id of layout of the Group (the file name)
	 * @param p_iConfigurableGroupLayoutId
	 *            id of configurable layout of the Group (the first component)
	 * @param p_iChildLayout
	 *            id of layout of the child (the file name)
	 * @param p_iConfigurableChildLayoutId
	 *            id of configurable layout of the child (the first component)
	 */
	public AbstractConfigurableFlipperExpandableListAdapter( ListViewModel<ITEM, ITEMVM> p_oMasterVM, int p_iTitleLayout,
			int p_iConfigurableTitleLayoutId, int p_iGroupLayout,int p_iConfigurableGroupLayoutId, int p_iChildLayout,
			int p_iConfigurableChildLayoutId) {
		super();
		this.listMasterVM = p_oMasterVM;
		this.titleLayout = p_iTitleLayout;
		this.configurableTitleLayoutId = p_iConfigurableTitleLayoutId;
		this.groupLayout = p_iGroupLayout;
		this.configurableGroupLayoutId = p_iConfigurableGroupLayoutId;
		this.childLayout = p_iChildLayout;
		this.configurableChildLayoutId = p_iConfigurableChildLayoutId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTitleId(int p_iTitlePosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getTitle(p_iTitlePosition).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getTitle(p_iTitlePosition).getIdVM().hashCode();
		}
		
		return i;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getGroupId(int p_iTitlePosition, int p_iGroupPosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getGroup(p_iTitlePosition, p_iGroupPosition).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getGroup(p_iTitlePosition, p_iGroupPosition).getIdVM().hashCode();
		}
		
		return i;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * {@inheritDoc}
	 */
	@Override
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getGroupCount(int p_iTitlePosition) {
		return  this.getTitle(p_iTitlePosition).getComposite().getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTitleCount() {
		return this.listMasterVM.getCount();
	}

	/**
	 * Get the title model for this position
	 * 
	 * @param p_iParamTitle
	 *            the position
	 * @return the ITEMVM model for title
	 */
	@Override
	public ITEMVM getTitle(int p_iTitlePosition) {
		return this.listMasterVM.getCacheVMByPosition(p_iTitlePosition);
	}

	/**
	 * Get the Group
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            the position of the group view in the list
	 * @return the SUBITEMVM model for Group
	 */	
	@Override
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
	 * Get the Group
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            the position of the group view in the list
	 * @param p_iChildPosition
	 *            the position of the current view in the child list
	 * @return the SUBSUBITEMVM model for Child
	 */	
	@Override
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

	/**
	 * This method can be overriden to wrap the components in the
	 * MasterVisualCommponent of the title
	 * 
	 * @param p_oView
	 *            the current view
	 * @param p_oCurrentViewModel
	 *            The view Model of this item
	 * @param p_iParamTitlePosition
	 *            The position of the first level of the triple list
	 */
	public void wrapTitleView(View p_oCurrentRow, ITEMVM p_oCurrentViewModel, int p_iParamTitlePosition) {
		// nothing to do in this implementation, the automatic binding is used
	}

	/**
	 * 
	 * This method can be overriden to wrap the components in the
	 * MasterVisualCommponent Group item
	 * 
	 * @param p_oView
	 *            the current view
	 * @param p_oCurrentViewModel
	 *            The view Model of this group item
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            the position of the group view in the list
	 */
	protected void wrapCurrentGroupView(View p_oView,SUBITEMVM p_oCurrentViewModel, int p_iTitlePosition, int p_iGroupPosition) {
		// nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * 
	 * This method can be overriden to wrap the components in the
	 * MasterVisualCommponent Child item
	 * 
	 * @param p_oView
	 *            the current view
	 * @param p_oCurrentViewModel
	 *            The view Model of this child item
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            the position of the current view in the grouplist
	 * @param p_iChildPosition
	 *            the position of the current view in the childlist
	 */
	protected void wrapCurrentChildView(View p_oView,SUBSUBITEMVM p_oCurrentViewModel, int p_iTitlePosition, int p_iGroupPosition, int p_iChildPosition) {
		// nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * create the view for the title of the flipper by inflating the sublayout
	 * with autobind capacity of the framework
	 * <p>
	 * To overload this construction and add custom actions, just overload the
	 * {@link AbstractConfigurableFlipperExpandableListAdapter#wrapTitleView(View, Object, int)}
	 * and component__flipper_expandable_list_title__master layout
	 * </p>
	 *
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_oCurrentRow
	 *            The current row
	 * @param p_oParentViewGroup
	 *            The parent
	 * @return the title view
	 */
	@Override
	public View getTitleView(int p_iTitlePosition, boolean p_bExpanded, View p_oCurrentRow, ViewGroup p_oParentViewGroup) {
		return this.delegate.getLegacyViewByLevel(Level.TITLE, p_bExpanded, p_oCurrentRow, p_oParentViewGroup, p_iTitlePosition);
	}

	

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 *      android.view.View, android.view.ViewGroup)
	 *      
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            the position of the current view in the group list
	 * @param p_iChildPosition
	 *            the position of the current view in the child list
	 * @param p_bLastChild
	 *            the last child
	 * @param p_oCurrentRow
	 *            The current row
	 * @param p_oParentViewGroup
	 *            The parent
	 * @return the view created
	 */
	@Override
	public View getChildView(int p_iTitlePosition, int p_iGroupPosition, int p_iChildPosition, boolean p_bLastChild, View p_oCurrentRow, ViewGroup p_oParentViewGroup) {
		return this.delegate.getLegacyViewByLevel(Level.CHILD, false, p_oCurrentRow, p_oParentViewGroup, p_iTitlePosition, p_iGroupPosition, p_iChildPosition);
	}

	/**
	 * Creates and return a ConfigurableListViewHolder.
	 * 
	 * @return An empty ConfigurableListViewHolder object.
	 */
	public ConfigurableListViewHolder createViewHolderChildItem(View p_oView) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder", 
				new Class[]{ View.class }, 
				new Object[]{ p_oView });
	}
	
	public ConfigurableListViewHolder createViewHolderGroupItem(View p_oView, boolean p_bIsExpanded) {
		//return new ConfigurableExpandedListViewHolder(p_oView, p_bIsExpanded);
		return (ConfigurableExpandedListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableExpandedListViewHolder", 
				new Class[]{ View.class, Boolean.TYPE }, 
				new Object[]{ p_oView, p_bIsExpanded });
	} //TODO Rendre Homegène avec le reste des listes
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 *      android.view.View, android.view.ViewGroup)
	 *
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            the position of the current view in the grouplist
	 * @param p_iChildPosition
	 *            the position of the current view in the childlist
	 * @param p_bExpanded
	 *            The Group Expanded
	 * @param p_oCurrentRow
	 *            The current row
	 * @param p_oParentViewGroup
	 *            The parent
	 * @return the view created
	 */
	@Override
	public View getGroupView(int p_iTitlePosition, int p_iGroupPosition, boolean p_bExpanded, View p_oCurrentRow, ViewGroup p_oParentViewGroup) {
		return this.delegate.getLegacyViewByLevel(Level.GROUP, p_bExpanded, p_oCurrentRow, p_oParentViewGroup, p_iTitlePosition, p_iGroupPosition);
	}

	@Override
	public void setSelectedItem(String p_sItemId) {
		////
				////
				//// TODO PAS PROPRE
				////
	}

	@Override
	public void resetSelectedItem() {
		////
				////
				//// TODO PAS PROPRE
				////
	}
	
	@Override
	public void notifyDataSetChanged() {
		////
		////
		//// TODO PAS PROPRE
		////
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver p_oDataSetObserver) {
		this.listMasterVM.registerObserver(p_oDataSetObserver);
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver p_oDataSetObserver) {
		this.listMasterVM.unregisterObserver(p_oDataSetObserver);
		
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public int getGroupType(int p_iGroupPosition) {
		return 0;
	}
	
	@Override
	public int getChildType(int p_iGroupPosition, int p_iChildPosition) {
		return 0;
	}
	
	@Override
	public int getGroupTypeCount() {
		return 1;
	}
	
	@Override
	public int getChildTypeCount() {
		return 1;
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
	 * Returns a view holder for the title view
	 * @param p_bExpanded true if the title view is expanded
	 * @return a view holder for the title view
	 */
	public ConfigurableListViewHolder createViewHolderTitleItem(View p_oView, boolean p_bExpanded) {
		return (ConfigurableExpandedListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableExpandedListViewHolder", 
				new Class[]{ View.class, Boolean.TYPE }, 
				new Object[]{ p_oView, p_bExpanded });
	}

}
