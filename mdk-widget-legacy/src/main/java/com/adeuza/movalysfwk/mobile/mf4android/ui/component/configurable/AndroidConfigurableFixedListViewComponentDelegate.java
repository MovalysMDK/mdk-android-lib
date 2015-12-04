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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import java.util.Map;

import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing.GraphicalProperty;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Delegate for FixedList Component
 * 
 * <p>This class centralize behavior for FixedList Components</p>
 *
 * @param <VALUE> the type handled by the fixedlist
 */
public class AndroidConfigurableFixedListViewComponentDelegate<VALUE> extends AndroidConfigurableVisualComponentDelegate<VALUE> {

	/**
	 * Constructor for the fixedlist delegate
	 * @param p_oCurrentView the fixedlist
	 * @param p_oDelegateType the class type handled by component
	 */
	public AndroidConfigurableFixedListViewComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Constructor for the fixedlist Delegate
	 * @param p_oCurrentView the fixedlist
	 * @param p_oDelegateType the class type handled by component
	 * @param p_oAttrs the AttributeSet of the View
	 */
	public AndroidConfigurableFixedListViewComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
	}
	

	@Override
	public void doOnPostAutoBind() {
		// nothing to do
	}
	
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		
		setEditableButtonsVisibility(View.VISIBLE);
	}
	
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		
		setEditableButtonsVisibility(View.GONE);
	}
	
	protected void setEditableButtonsVisibility(int visibility) {
		if (this.currentView instanceof AbstractFixedListView) {
			AbstractFixedListView<?,?> currentFixedList = (AbstractFixedListView<?,?>) this.currentView;
			
			AndroidApplication oApplication = (AndroidApplication) AndroidApplication.getInstance();
			
			if (currentFixedList.getTitleMode()){
				View addButton = currentFixedList.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_addButton__button));
				addButton.setVisibility(visibility);
			}
	
			int iDeleteButton = oApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_item_deleteButton__button);
	
			int iItemCount = currentFixedList.getItemCount();
			for (int i=0; i < iItemCount; i++) {
				View oView = currentFixedList.getItemAt(i).findViewById(iDeleteButton);
				if ( oView != null){
					oView.setVisibility(visibility);
				}
				currentFixedList.getItemAt(i).setClickable(currentFixedList.getDisplayDetail());
			}
		}
	}

	@Override
	public void destroy() {
		int iNbChilds= ((AbstractFixedListView<?, ?>) this.currentView).getItemCount();
		View oChildView;
		for (int i=0; i < iNbChilds; i++) {
			oChildView = ((AbstractFixedListView<?, ?>) this.currentView).getItemAt(i);
			((AbstractFixedListView<?, ?>) this.currentView).destroyView(oChildView);
		}
		((AbstractFixedListView<?, ?>) this.currentView).removeItems();
		super.destroy();
	}

	@SuppressWarnings("unchecked")
	@Override
	public VALUE configurationGetValue() {
		if (this.currentView instanceof AbstractFixedListView) {
			return (VALUE) ((AbstractFixedListView<?, ?>) this.currentView).getAdapter().getMasterVM();
		} else {
			return null;
		}
	}
	
	@Override
	public void configurationSetValue(VALUE p_oObjectToSet) {
		if (this.currentView instanceof AbstractFixedListView) {
			AbstractFixedListView<?, ?> currentFixedList = (AbstractFixedListView<?, ?>) this.currentView;
			currentFixedList.getAdapter().setMasterVM( (ListViewModel) p_oObjectToSet);
			currentFixedList.update();
			if (((ListViewModel<?, ?>) p_oObjectToSet).getCount() > 0) {
				currentFixedList.getFixedListViewListeners().doOnNotification("onAdd", currentFixedList.getNotifier(), this, ((ListViewModel<?, ?>) p_oObjectToSet).getCount());
			}
			if (((ListViewModel<?, ?>) p_oObjectToSet).getParam(GraphicalProperty.BACKGROUND_RESOURCE) != null) {
				currentFixedList.getTitleView().setBackgroundResource( (Integer) ((ListViewModel<?, ?>) p_oObjectToSet).getParam(GraphicalProperty.BACKGROUND_RESOURCE) );
			}
			((AbstractFixedListView<?,?>) this.currentView).updateTitle();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return ((AbstractFixedListView<?,?>) this.currentView).getItemCount() > 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#validate(BasicComponentConfiguration, Map, StringBuilder)
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		if (this.currentView instanceof AbstractFixedListView) {
			boolean bSubVmError = false;
			ListViewModel<?,?> oMasterVM = ((AbstractFixedListView<?,?>) this.currentView).getAdapter().getMasterVM();
			
			for (int i = 0; oMasterVM != null && i < oMasterVM.getCount(); i++) {
				ViewModel oCurrentVM = (ViewModel) oMasterVM.getCacheVMByPosition(i);
				bSubVmError = oCurrentVM.validComponents(null, p_mapParameters) || bSubVmError;
			}
			return !bSubVmError && super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
		} else {
			return true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return ListViewModel.class;
	}
	
}
