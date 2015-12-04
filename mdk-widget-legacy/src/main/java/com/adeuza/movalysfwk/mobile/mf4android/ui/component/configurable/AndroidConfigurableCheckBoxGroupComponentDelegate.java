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
import android.widget.CheckBox;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMCheckBoxGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Delegate for {@link MMCheckBoxGroup} component
 *
 * @param <VALUE> the type handled by component
 */
public class AndroidConfigurableCheckBoxGroupComponentDelegate<VALUE> extends
		AndroidConfigurableVisualComponentDelegate<VALUE> {
	/**
	 * Create instance of AndroidConfigurableCheckBoxGroupComponentDelegate
	 * @param p_oCurrentView the component view
	 * @param p_oDelegateType the class type handled by component
	 */
	public AndroidConfigurableCheckBoxGroupComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Create instance of AndroidConfigurableCheckBoxGroupComponentDelegate
	 * @param p_oCurrentView the component view
	 * @param p_oDelegateType the class type handled by component
	 * @param p_oAttrs the view attributes
	 */
	public AndroidConfigurableCheckBoxGroupComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		for (CheckBox oCheckBox : ((MMCheckBoxGroup<?>) this.currentView).getCheckBoxByValue().values()) {
			oCheckBox.setEnabled(false);
		}
		super.configurationDisabledComponent();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		for (CheckBox oCheckBox : ((MMCheckBoxGroup<?>) this.currentView).getCheckBoxByValue().values()) {
			oCheckBox.setEnabled(true);
		}
		super.configurationEnabledComponent();
	}
	
}
