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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * Delegate for framework behavior on date picker component
 *
 */
public class MMDatePickerVisualComponentFwkDelegate extends
		AndroidConfigurableVisualComponentFwkDelegate {
	/**
	 * Create new instance of MMDoublePickerGroupVisualComponentFwkDelegate
	 * @param p_oCurrentView the delegate view
	 */
	public MMDatePickerVisualComponentFwkDelegate(ConfigurableVisualComponent p_oCurrentView) {
		super(p_oCurrentView);
	}
	/**
	 * Create new instance of MMDoublePickerGroupVisualComponentFwkDelegate
	 * @param p_oCurrentView the delegate view
	 * @param p_oAttrs the component attributes
	 */
	public MMDatePickerVisualComponentFwkDelegate(ConfigurableVisualComponent p_oCurrentView,
			AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oAttrs);
	}

	@Override
	public boolean isChanged() {
		this.currentCvComponent.getComponentDelegate().configurationGetValue();
		return super.isChanged();
	}
	
}
