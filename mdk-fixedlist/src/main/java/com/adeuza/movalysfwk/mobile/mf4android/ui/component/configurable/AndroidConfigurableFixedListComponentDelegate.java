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

import android.util.AttributeSet;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMFixedList;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

import java.util.Map;

/**
 * Delegate for FixedList Component
 * 
 * <p>This class centralize behavior for FixedList Components</p>
 *
 * @param <VALUE> the type handled by the fixedlist
 */
@SuppressWarnings("rawtypes")
public class AndroidConfigurableFixedListComponentDelegate<VALUE extends ListViewModel> extends AndroidConfigurableVisualComponentDelegate<VALUE> {

	/** Attr display detail */
	public static final String ATTR_DISPLAY_DETAIL = "displayDetail";

	/** Default display detail */
	public static final boolean DEFAULT_DISPLAY_DETAIL = true;
	
	/** used to load the movalys:titlemode attribute that disable the title and + button**/
	private boolean bTitleMode=true;

	/** Max items in the fixed list, -1 for unlimited */
	private int maxItems = Integer.MAX_VALUE ;

	/** boolean display detail */
	protected boolean displayDetail;
	
	/**
	 * Constructor for the fixedlist delegate
	 * @param p_oCurrentView the fixedlist
	 * @param p_oDelegateType the class type handled by component
	 */
	public AndroidConfigurableFixedListComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Constructor for the fixedlist Delegate
	 * @param p_oCurrentView the fixedlist
	 * @param p_oDelegateType the class type handled by component
	 * @param p_oAttrs the AttributeSet of the View
	 */
	public AndroidConfigurableFixedListComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
		if (p_oAttrs != null) {
			this.bTitleMode = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "titleMode", true);
			this.maxItems = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "maxItems", Integer.MAX_VALUE);
			this.displayDetail = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, ATTR_DISPLAY_DETAIL, DEFAULT_DISPLAY_DETAIL);
		}
	}
	

	@Override
	public void doOnPostAutoBind() {
		// nothing to do
	}
	
//	@Override
//	public void configurationEnabledComponent() {
//		super.configurationEnabledComponent();
//		
//		setEditableButtonsVisibility(View.VISIBLE);
//	}
//	
//	@Override
//	public void configurationDisabledComponent() {
//		super.configurationDisabledComponent();
//		
//		setEditableButtonsVisibility(View.GONE);
//	}
	
//	protected void setEditableButtonsVisibility(int visibility) {
//		if (this.currentView instanceof MMFixedList) {
//			MMFixedList currentFixedList = (MMFixedList) this.currentView;
//			
//			AndroidApplication oApplication = (AndroidApplication) AndroidApplication.getInstance();
//			
//			if (currentFixedList.getTitleMode()){
//				View addButton = currentFixedList.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_addButton__button));
//				addButton.setVisibility(visibility);
//			}
//	
//			int iDeleteButton = oApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_item_deleteButton__button);
//	
//			int iItemCount = currentFixedList.getItemCount();
//			for (int i=0; i < iItemCount; i++) {
//				View oView = currentFixedList.getItemAt(i).findViewById(iDeleteButton);
//				if ( oView != null){
//					oView.setVisibility(visibility);
//				}
//				currentFixedList.getItemAt(i).setClickable(currentFixedList.getDisplayDetail());
//			}
//		}
//	}

	@Override
	public void destroy() {
		((MMFixedList) this.currentView).removeItems();
		super.destroy();
	}

	@SuppressWarnings("unchecked")
	@Override
	public VALUE configurationGetValue() {
		if (this.currentView instanceof MMFixedList) {
			return (VALUE) ((MMFixedList) this.currentView).getAdapter().getMasterVM();
		} else {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public void configurationSetValue(VALUE p_oObjectToSet) {
		super.configurationSetValue(p_oObjectToSet);

		if (this.currentView instanceof MMFixedList) {
			MMFixedList currentFixedList = (MMFixedList) this.currentView;
			currentFixedList.getAdapter().setMasterVM(p_oObjectToSet);
			//if (((ListViewModel<?, ?>) p_oObjectToSet).getCount() > 0) {
			//	currentFixedList.getAdapter().notifyDataSetChanged();
			//}
			// TODO
//			if (((ListViewModel<?, ?>) p_oObjectToSet).getParam(GraphicalProperty.BACKGROUND_RESOURCE) != null) {
//				currentFixedList.getTitleView().setBackgroundResource( (Integer) ((ListViewModel<?, ?>) p_oObjectToSet).getParam(GraphicalProperty.BACKGROUND_RESOURCE) );
//			}
			((MMFixedList) this.currentView).updateTitle();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return ((MMFixedList) this.currentView).getItemCount() > 0;
	}

	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#isNullOrEmptyValue(Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(VALUE p_oObject) {
		return (p_oObject == null || p_oObject.getCount() == 0);
	}

	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#validate(BasicComponentConfiguration, Map, StringBuilder)
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		if (this.currentView instanceof MMFixedList) {
			boolean bSubVmError = false;
			ListViewModel<?,?> oMasterVM = ((MMFixedList) this.currentView).getAdapter().getMasterVM();
			
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

	/**
	 * @return the bTitleMode
	 */
	public boolean isTitleMode() {
		return bTitleMode;
	}

	/**
	 * @return the maxItems
	 */
	public int getMaxItems() {
		return maxItems;
	}

	/**
	 * @return the displayDetail
	 */
	public boolean isDisplayDetail() {
		return displayDetail;
	}
}
