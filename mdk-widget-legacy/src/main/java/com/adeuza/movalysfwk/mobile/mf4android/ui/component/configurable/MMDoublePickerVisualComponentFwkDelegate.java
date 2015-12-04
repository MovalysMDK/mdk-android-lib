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

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMNumberPicker;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * Delegate for framework behavior on double picker component
 *
 */
public class MMDoublePickerVisualComponentFwkDelegate extends
AndroidConfigurableVisualComponentFwkDelegate {
	/**
	 * Create new instance of MMDoublePickerGroupVisualComponentFwkDelegate
	 * @param p_oCurrentView the delegate view
	 */
	public MMDoublePickerVisualComponentFwkDelegate(ConfigurableVisualComponent p_oCurrentView) {
		super(p_oCurrentView);
	}
	/**
	 * Create new instance of MMDoublePickerGroupVisualComponentFwkDelegate
	 * @param p_oCurrentView the delegate view
	 * @param p_oAttrs the component attributes
	 */
	public MMDoublePickerVisualComponentFwkDelegate(ConfigurableVisualComponent p_oCurrentView, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oAttrs);
	}

	@Override
	public void resetChanged() {
		MMNumberPicker oUiPickerInt = (MMNumberPicker) this.currentView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__int__picker));
		MMNumberPicker oUiPickerDigit = (MMNumberPicker) this.currentView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__digit__picker));
		oUiPickerInt.getComponentFwkDelegate().resetChanged()  ;
		oUiPickerDigit.getComponentFwkDelegate().resetChanged()  ;
		super.resetChanged();
	}
	
	@Override
	public boolean isChanged() {
		MMNumberPicker oUiPickerInt = (MMNumberPicker) this.currentView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__int__picker));
		MMNumberPicker oUiPickerDigit = (MMNumberPicker) this.currentView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__digit__picker));
		return super.isChanged() || oUiPickerInt.getComponentFwkDelegate().isChanged() || oUiPickerDigit.getComponentFwkDelegate().isChanged() ;
	}
	
}
