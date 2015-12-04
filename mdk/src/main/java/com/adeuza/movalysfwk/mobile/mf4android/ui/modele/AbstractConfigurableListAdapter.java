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
import android.widget.BaseAdapter;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * <p>
 * Adapter for list.
 * </p>
 * @deprecated USE {@link com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKAdapter} instead
 * 
 * @param <LISTVM>
 *            a model for list
 * @param <ITEMVM>
 *            a model for item
 */
@Deprecated
public abstract class AbstractConfigurableListAdapter<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>>
extends BaseAdapter implements MMListAdapter {

	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	private enum Level implements AdapterLevel {
		/** Child level */
		CHILD(ListAdapterChild.class);
		
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
	private LISTVM masterVM = null;
	/** android id of layout (the name of file) for item */
	private int layoutid = 0;
	/** android id of configurable layout for item */
	private int configurableLayout = 0;

	/** the ViewModel selected */
	private String selectedItem = null;

	/** Inflated components. */
	protected Set<MasterVisualComponent> components = new HashSet<>();
	
	/** adapter delegate */
	protected ListAdapterDelegate delegate = new ListAdapterDelegateImpl(this);

	/** Data observer */
	private DataSetObserver mObserver = new DataSetObserver() {
		
		@Override
		public void onChanged() {
			AbstractConfigurableListAdapter.this.notifyDataSetChanged();
		}
		
		@Override
		public void onInvalidated() {
			AbstractConfigurableListAdapter.this.notifyDataSetInvalidated();
		}

	};
	
	/**
	 * Constructs a new ConfigurableAdapter
	 * 
	 * @param p_oMasterVM
	 *            list's view model
	 * @param p_iLayoutId
	 *            id of layout (the file name)
	 * @param p_iConfigurableLayout
	 *            id of configurable layout (the first component)
	 */
	public AbstractConfigurableListAdapter(LISTVM p_oMasterVM, int p_iLayoutId,
			int p_iConfigurableLayout) {
		super();
		this.masterVM = p_oMasterVM;
		this.masterVM.registerObserver(this.mObserver);
		this.layoutid = p_iLayoutId;
		this.configurableLayout = p_iConfigurableLayout;
	}

	/**
	 * A2A_DOC - Décrire la méthode updateAdapter de la classe
	 * AbstractConfigurableListAdapter
	 * 
	 * @param p_oMasterVM
	 */
	protected void updateAdapter(LISTVM p_oMasterVM) {
		this.masterVM = p_oMasterVM;
		this.masterVM.registerObserver(this.mObserver);
		this.notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		int r_iResult = 0;
		if (this.masterVM != null) {
			r_iResult = this.masterVM.getCount();
		}
		return r_iResult;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITEMVM getItem(int p_iParamInt) {
		return this.masterVM.getCacheVMByPosition(p_iParamInt);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getItemId(int p_iParamInt) {
		return p_iParamInt;
	}

	public LISTVM getMasterVM() {
		return this.masterVM;
	}

	/**
	 * get the selectedItem Id used to highlight the selectected item of the
	 * list (this item has the state_checked)
	 * 
	 * @return the <ITEM> Id with state_checked
	 */
	public String getSelectedItem() {
		return selectedItem;
	}

	/**
	 * get the selectedItem Id used to highlight the selectected item of the
	 * list (this item has the state_checked)
	 * 
	 * @param the
	 *            Id of the Checked <ITEM>
	 * */
	@Override
	public void setSelectedItem(String p_sSelectedItemId) {
		this.selectedItem = p_sSelectedItemId;
	}

	@Override
	public void resetSelectedItem() {
		this.selectedItem = null;
	}

	/**
	 * This method can be overriden to wrap the components in the
	 * MasterVisualCommponent
	 * 
	 * @param p_oView
	 *            the current view
	 * @param p_oCurrentViewModel
	 *            The view Model of this item
	 * @param p_iPosition
	 *            the position of the current view in the list
	 */
	protected void wrapCurrentView(View p_oView, ITEMVM p_oCurrentViewModel, int p_iPosition) {
		// nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int p_iPosition, View p_oView, ViewGroup p_oViewGroup) {
		return this.delegate.getViewByLevel(Level.CHILD, false, p_oView, p_oViewGroup, p_iPosition);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		this.resetComponentTags();
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
			((ConfigurableListViewHolder) ((View) oComponent).getTag()).setViewModelID(null);
		}
	}

	protected int getItemLayout(int p_iPosition) {
		return this.layoutid;
	}

	protected int getItemMasterId(int p_iPosition) {
		return this.configurableLayout;
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
	 * Returns the components list
	 * @return the components
	 */
	public Set<MasterVisualComponent> getComponents() {
		return components;
	}
	
}
