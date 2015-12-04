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
import java.util.HashSet;
import java.util.Set;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * <p>Adapter for an expandable list</p>
 * @deprecated USE {@link com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKExpandableAdapter} instead
 * 
 *<p> you can custimise the layouts component__expandable_list_groupitem__master and component__expandable_list_childitem__master
 * and implement {@link AbstractConfigurableExpandableListAdapter#wrapCurrentGroupView(android.view.View, com.adeuza.movalys.fwk.mobile.javacommons.uimodel.ExpandableViewModel, int)
 * and {@link AbstractConfigurableExpandableListAdapter#wrapCurrentChildView(android.view.View, com.adeuza.movalys.fwk.mobile.javacommons.uimodel.ViewModel, int, int)
 * to add custom features to this list </p>
 *
 *
 * @param <ITEM> a item model
 * @param <SUBITEM> a subitem model
 * @param <ITEMVM> a VM for item
 * @param <SUBITEMVM> a VM for subitem  
 */
@Deprecated
public abstract class AbstractConfigurableExpandableListAdapter<ITEM extends MIdentifiable, SUBITEM extends MIdentifiable, SUBITEMVM extends ItemViewModel<SUBITEM>, ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>>
		extends BaseExpandableListAdapter implements MMListAdapter {
	
	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	private enum Level implements AdapterLevel {
		/** Group level */
		GROUP(ExpandableAdapterGroup.class),
		/** Child level */
		CHILD(ExpandableAdapterChild.class);
		
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
	private ListViewModel<ITEM, ITEMVM> masterVM = null;
	/** android id of group layout (the name of file) for item */
	private int groupLayout = 0;
	/** android id of configurable group layout element in the file for item */
	private int configurableGroupLayoutId = 0;

	/** android id of child layout (the name of file) for item */
	private int childLayout = 0;
	/** coat for the child layout that add a checkbox on it */
	private int childCoatLayout = 0;
	/** android id of configurable child layout element in the file for item */
	private int configurableChildLayoutId = 0;
	/** ndroid id of coat for the child layout */
	private int childCoatContentLayoutId = 0;

	/** Inflated components. */
	protected Set<MasterVisualComponent> components = new HashSet<>();
	
	/** adapter delegate */
	private ListAdapterDelegate delegate = new ListAdapterDelegateImpl(this);
	
	/**
	 * Construct a new ConfigurableAdapter
	 * 
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
	public AbstractConfigurableExpandableListAdapter(
			ListViewModel<ITEM, ITEMVM> p_oMasterVM, int p_iGroupLayout,
			int p_iConfigurableGroupLayoutId, int p_iChildLayout,
			int p_iConfigurableChildLayoutId) {

		super();
		this.masterVM = p_oMasterVM;
		this.groupLayout = p_iGroupLayout;
		this.configurableGroupLayoutId = p_iConfigurableGroupLayoutId;
		childLayout = p_iChildLayout;
		configurableChildLayoutId = p_iConfigurableChildLayoutId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount(int p_iParamGroup) {
		ITEMVM oCurrentItemExtVM = this.getGroup(p_iParamGroup);
		ListViewModel<?, ?> listChild = oCurrentItemExtVM.getComposite();
		if (listChild == null) {
			return 0;
		} else {
			return listChild.getCount();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getGroupCount() {
		return this.masterVM.getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITEMVM getGroup(int p_iParamGroup) {
		return this.masterVM.getCacheVMByPosition(p_iParamGroup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SUBITEMVM getChild(int p_iParamGroup, int p_iParamChild) {
		ListViewModel<SUBITEM, SUBITEMVM> listChild = this.getGroup(
				p_iParamGroup).getComposite();
		if (listChild == null) {
			return null;
		} else {
			return listChild.getCacheVMByPosition(p_iParamChild);
		}
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
	protected void postInflateChildView(View p_oView) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	protected void postBindChildView(View p_oView,
			SUBITEMVM p_oCurrentViewModel, int p_iParamGroupPosition,
			int p_iParamChildPosition) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	protected void postInflateGroupView(View p_oView, boolean p_bExpanded) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	protected void postBindGroupView(View p_oView,
			ITEMVM p_oCurrentViewModel, int p_iParamGroupPosition, boolean p_bExpanded) {
		// nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getChildView(int p_iParamGroup, int p_iParamChild,
			boolean p_oParamBoolean, View p_oParamView,
			ViewGroup p_oParentViewGroup) {
		return this.delegate.getLegacyViewByLevel(Level.CHILD, false, p_oParamView, p_oParentViewGroup, p_iParamGroup, p_iParamChild);
	}

	/**
	 * Coat an item view
	 * @param p_oView the current view to coat
	 * @param p_oCurrentViewModel the current SUBITEMVM to coat
	 */
	protected void wrapCoat(View p_oView, SUBITEMVM p_oCurrentViewModel) {
		/* Nothing to do here. Just for override */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getGroupView(int p_iParamGroup, boolean p_oParamBoolean, View p_oParamView, ViewGroup p_oParentViewGroup) {
		return this.delegate.getLegacyViewByLevel(Level.GROUP, p_oParamBoolean, p_oParamView, p_oParentViewGroup, p_iParamGroup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getChildId(int p_iInt1, int p_iInt2) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getChild(p_iInt1, p_iInt2).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getChild(p_iInt1, p_iInt2).getIdVM().hashCode();
		}

		return i;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getGroupId(int p_iInt1) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getGroup(p_iInt1).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getGroup(p_iInt1).getIdVM().hashCode();
		}

		return i;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChildSelectable(int p_iInt1, int p_iInt2) {
		return true;
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
		childCoatLayout = p_iChildCoatLayout;
		childCoatContentLayoutId = p_iChildCoatContentId;
	}

	/**
	 * Retourne l'objet childLayout.
	 * 
	 * @return Objet childLayout
	 */
	public int getChildLayout() {
		return this.childLayout;
	}

	/**
	 * Retourne l'objet configurableChildLayoutId.
	 * 
	 * @return Objet configurableChildLayoutId
	 */
	public int getConfigurableChildLayoutId() {
		return this.configurableChildLayoutId;
	}
	
	/**
	 * Retourne l'objet configurableGroupLayoutId.
	 * 
	 * @return Objet configurableGroupLayoutId
	 */
	public int getConfigurableGroupLayoutId() {
		return this.configurableGroupLayoutId;
	}

	@Override
	public void setSelectedItem(String p_sItemId) {
		// NOTHING TO DO
	}

	/**
	 * Reset selected item
	 */
	@Override
	public void resetSelectedItem() {
		// NOTHING TO DO
	}
	
	/**
	 * Creates and return a ConfigurableListViewHolder.
	 * 
	 * @return An empty ConfigurableListViewHolder object.
	 */
	public ConfigurableListViewHolder createViewHolder(View p_oView) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder", 
				new Class[]{ View.class }, 
				new Object[]{ p_oView });
	}
	
	/**
	 * Returns the layout for the group view
	 * @return the layout for the group view
	 */
	public int getGroupLayout() {
		return this.groupLayout;
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
	 * Returns a view holder for a group view
	 * @param p_oView 
	 * @return a view holder for a group view
	 */
	public ConfigurableExpandedListViewHolder createGroupViewHolder(View p_oView, boolean p_bExpanded) {
		return (ConfigurableExpandedListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableExpandedListViewHolder", 
				new Class[]{ View.class, Boolean.TYPE }, 
				new Object[]{ p_oView, p_bExpanded });
	}
	
	/**
	 * Returns the components list
	 * @return the components
	 */
	public Set<MasterVisualComponent> getComponents() {
		return components;
	}
	
	/**
	 * Uninflate components
	 */
	public void uninflate() {
		for (MasterVisualComponent oCompositeComponent : this.components) {
			oCompositeComponent.getDescriptor().unInflate(null,
					oCompositeComponent, null);
		}
		this.resetComponentTags();
		this.components.clear();
	}
	
	protected void resetComponentTags() {
		for (MasterVisualComponent oComponent : this.components) {
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
	
	public ListViewModel<ITEM, ITEMVM> getMasterVM() {
		return this.masterVM;
	}
}
