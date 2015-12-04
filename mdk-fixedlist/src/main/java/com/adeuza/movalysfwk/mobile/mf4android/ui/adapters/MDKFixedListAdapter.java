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
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnectorWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.NotImplementedException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

public class MDKFixedListAdapter<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> extends MDKAdapter<ITEM, ITEMVM, LISTVM> {
	
	/** android id of configurable layout for item. */
	private int configurableLayout = 0;
	
	/** the detail popup layout. */
	private int detailLayoutId;
	
	public MDKFixedListAdapter(LISTVM p_oMasterVM, int p_iLayoutid, int p_iConfigurableLayout, int p_iDetailLayoutId) {
		super(p_oMasterVM, p_iLayoutid, p_iConfigurableLayout);
		
		this.configurableLayout = p_iConfigurableLayout;
		this.detailLayoutId = p_iDetailLayoutId;
	}
	
	/**
	 * Creates and return a ConfigurableListViewHolder for the list item view.
	 * @return An empty ConfigurableListViewHolder object.
	 */
	public ConfigurableListViewHolder createViewHolder(View p_oView) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder", 
				new Class[]{ View.class }, 
				new Object[]{ p_oView });
	}
	
	/**
	 * Creates and return a ConfigurableListViewHolder for the detail view.
	 * @return an empty detail view holder
	 */
	public ConfigurableListViewHolder createDetailViewHolder(View p_oView) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder", 
				new Class[]{ View.class }, 
				new Object[]{ p_oView });
	}

	/**
	 * @return the configurableLayout
	 */
	public int getConfigurableLayout() {
		return configurableLayout;
	}

	/**
	 * @return the detailLayoutId
	 */
	public int getDetailLayoutId() {
		return detailLayoutId;
	}
	
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
	 * 
	 * @param p_oCurrentRow
	 */
	public void postInflateView(View p_oCurrentRow) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	/**
	 * 
	 * @param p_oCurrentRow
	 * @param p_oItem
	 * @param p_iParamInt
	 */
	public void postBindView(View p_oCurrentRow, ITEMVM p_oItem, int p_iParamInt) {
		// nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * 
	 */
	public ITEMVM createEmptyVM() {
		throw new NotImplementedException("The method MDKFixedListAdapter.createEmptyVM must be overidden.");
	}
}
