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
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMNumberPicker;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Component Delegate for PickerGroup
 * <p>
 * This class is used in the Group Picker components to factor gestion of errors in component.
 * </p>
 *
 * @param <VALUE> the type handle by the final component
 */
public class AndroidConfigurablePickerComponentDelegate<VALUE> extends
		AndroidConfigurableVisualComponentDelegate<VALUE> {

	/**
	 * Construct new AndroidConfigurablePickerGroupComponentDelegate
	 * <p>Call super</p>
	 * @param p_oCurrentView the component view
	 * @param p_oDelegateType the class type to handle
	 * @see AndroidConfigurableVisualComponentDelegate
	 */
	public AndroidConfigurablePickerComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Construct new AndroidConfigurablePickerGroupComponentDelegate
	 * <p>Call super</p>
	 * @param p_oCurrentView the component view
	 * @param p_oDelegateType the class type to handle
	 * @param p_oAttrs the view {@link AttributeSet}
	 * @see AndroidConfigurableVisualComponentDelegate
	 */
	public AndroidConfigurablePickerComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
	}

	@Override
	public void configurationSetError(String p_oError) {
		super.configurationSetError(p_oError);
		for (int i = 0; i < ((ViewGroup) this.currentView).getChildCount(); i++) {
			if ( ((ViewGroup) this.currentView).getChildAt(i) instanceof MMNumberPicker) {
				((MMNumberPicker) ((ViewGroup) this.currentView).getChildAt(i)).getComponentDelegate().configurationSetError(p_oError);
			}
		}
	}
	
	@Override
	public void configurationUnsetError() {
		super.configurationUnsetError();
		for (int i = 0; i < ((ViewGroup) this.currentView).getChildCount(); i++) {
			if ( ((ViewGroup) this.currentView).getChildAt(i) instanceof MMNumberPicker) {
				((MMNumberPicker) ((ViewGroup) this.currentView).getChildAt(i)).getComponentDelegate().configurationUnsetError();
			}
		}
	}
	
}
