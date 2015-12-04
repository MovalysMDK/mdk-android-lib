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

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * DateTimeTextView Delegate
 * <p>Handle behaviors on DateTimeTextView and all inherited classes</p>
 *
 */
public class AndroidConfigurableDateTimeTextViewComponentDelegate extends AndroidConfigurableVisualComponentDelegate<String> {
	
	/**
	 * Constructor
	 * @param p_oCurrentView the component (inherited from TextView)
	 * @param p_oDelegateType the type of the delegate
	 */
	public AndroidConfigurableDateTimeTextViewComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Constructor
	 * @param p_oCurrentView the component (inherited from TextView)
	 * @param p_oDelegateType the type of the delegate
	 * @param p_oAttrs this AttributeSet of the View
	 */
	public AndroidConfigurableDateTimeTextViewComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
	}
	
	@Override
	public void configurationDisabledComponent() {
		this.currentView.clearFocus();
		if (this.currentView instanceof ComponentEnable) {
			((ComponentEnable) this.currentView).enableComponent(false);
		}
	}

	@Override
	public void configurationEnabledComponent() {
		if (this.currentView instanceof ComponentEnable) {
			((ComponentEnable) this.currentView).enableComponent(true);
		}
	}
}
