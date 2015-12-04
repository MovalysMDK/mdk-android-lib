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

import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKBaseAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnectorWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * Adapter for the fixed list
 * <p>Use to get view binded to view model items in a fixed list component</p>
 * 
 * @param <ITEM> a model class representing item
 * @param <ITEMVM> a view model class representing item (binded to view)
 * @param <LISTVM> the ListViewModel type
 *
 */

public abstract class AbstractConfigurableFixedListAdapter<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> {

	/** tag to identify selected item */
	public static final String SELECTED_VM = "SELECTED_VM";
	/**
	 * the master view model
	 * <p>this list is used to store ViewModels of the fixed list</p>
	 */
	private LISTVM masterVM;

	/** android id of layout (the name of file) for item */
	private int layoutid = 0;
	/** android id of configurable layout for item */
	private int configurableLayout = 0;
	/** */
	private int detailLayoutId;
	/** */
	private SparseArrayCompat<MDKBaseAdapter> referencedAdapter;

	/**
	 * Create a new Adapter
	 * @param p_iLayoutId the layout id (view container)
	 * @param p_iConfigurableLayoutId the configurable layout id (view of item)
	 * @param p_iDetailLayoutId the detail layout id (view in dialog)
	 */
	public AbstractConfigurableFixedListAdapter(int p_iLayoutId, int p_iConfigurableLayoutId, int p_iDetailLayoutId) {
		this.layoutid = p_iLayoutId;
		this.configurableLayout = p_iConfigurableLayoutId;
		this.detailLayoutId = p_iDetailLayoutId;
		this.referencedAdapter = new SparseArrayCompat<>();
	}

	/**
	 * Create a new Adapter
	 * @param p_oMasterVM the ListViewModel to use in adapter
	 * @param p_iLayoutId the layout id (view container)
	 * @param p_iConfigurableLayoutId the configurable layout id (view of item)
	 * @param p_iDetailLayoutId the detail layout id (view in dialog)
	 */
	public AbstractConfigurableFixedListAdapter(LISTVM p_oMasterVM, int p_iLayoutId, int p_iConfigurableLayoutId, int p_iDetailLayoutId) {
		this(p_iLayoutId, p_iConfigurableLayoutId, p_iDetailLayoutId);
		this.masterVM = p_oMasterVM;
	}

	/**
	 * Getter for the layout id
	 * @param p_oViewModel not used
	 * @return the layout id
	 */
	public int getLayout(ITEMVM p_oViewModel) {
		return this.layoutid;
	}

	/**
	 * Getter for configurable layout id
	 * @param p_oViewModel not used
	 * @return the configurable layout id
	 */
	public int getConfigurableLayout(ITEMVM p_oViewModel) {
		return this.configurableLayout;
	}

	/**
	 * Getter for detail layout id
	 * @return the detail layout id
	 */
	public int getDetailLayout() {
		return this.detailLayoutId;
	}

	/**
	 * Getter for master view model
	 * @return the {@link ListViewModel} used in adapter
	 */
	public LISTVM getMasterVM() {
		return this.masterVM;
	}
	
	/**
	 * Setter of {@link ListViewModel}
	 * @param p_oMasterVM the new {@link ListViewModel} to use
	 */
	public void setMasterVM(LISTVM p_oMasterVM) {
		this.masterVM = p_oMasterVM;
	}

	/**
	 * Add a new {@link ItemViewModel} in adapter
	 * @param p_oViewModel the {@link ItemViewModel} to add
	 */
	public void add(ITEMVM p_oViewModel) {
		this.masterVM.add(p_oViewModel, true);
	}
	/**
	 * Update a existing {@link ItemViewModel} in adapter
	 * @param p_oViewModel the {@link ItemViewModel} to add
	 */
	public void update(ITEMVM p_oViewModel) {
		this.masterVM.update(p_oViewModel);
	}

	/**
	 * Remove a {@link ItemViewModel} from adapter
	 * @param p_oViewModel the {@link ItemViewModel} to remove
	 */
	public void remove(ITEMVM p_oViewModel) {
		this.masterVM.remove(p_oViewModel);
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
	 * Create a empty view model
	 * @return a new instance of {@link ItemViewModel} used in adapter
	 */
	public abstract ITEMVM createEmptyVM();

	/**
	 * Wrap detail view (instantiate android and link instance to views)
	 * @param p_oDetailComponent the component to wrap
	 */
	public void wrapDetailView(MasterVisualComponent p_oDetailComponent) {
		View oViewGroup = ((ComponentWrapper) p_oDetailComponent).getComponent();
		
		if (oViewGroup instanceof ViewGroup) {
			ViewGroup oDetailView = (ViewGroup) oViewGroup;
			for( int i = 0 ; i < this.referencedAdapter.size(); i++ ) {
				View oView = oDetailView.findViewById(this.referencedAdapter.keyAt(i));
				
				MDKViewConnectorWrapper mConnectorWrapper = WidgetWrapperHelper.getInstance().getConnectorWrapper(oView.getClass());
				mConnectorWrapper.configure(this.referencedAdapter.valueAt(i), oView);
			}
		}
	}

	/**
	 * Getter for the {@link ItemViewModel} interface
	 * <p>Use to instantiate new {@link ItemViewModel} with the Framework {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader}
	 * @return the class of the interface of the {@link ItemViewModel} used in adapter
	 */
	public Class<ITEMVM> getItemVMInterface() {
		return this.masterVM.getItemVmInterface();
	}
	
}
