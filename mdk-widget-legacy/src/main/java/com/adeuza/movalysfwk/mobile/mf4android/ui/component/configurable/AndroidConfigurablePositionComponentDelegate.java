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

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractPositionMasterView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl;

/**
 * Component delegate for Position components
 * <p>
 * This delegate define default behavior for handling errors on position components.
 * </p>
 *
 */
public class AndroidConfigurablePositionComponentDelegate extends
		AndroidConfigurableVisualComponentDelegate<AddressLocationSVMImpl> {

	/**
	 * Constructor
	 * @param p_oCurrentView the component (inherited from TextView)
	 * @param p_oDelegateType the type of the delegate
	 */
	public AndroidConfigurablePositionComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Create new AndroidConfigurablePositionComponentDelegate
	 * @param p_oCurrentView the component view
	 * @param p_oDelegateType the type handle by component
	 * @param p_oAttrs the view attributes
	 */
	public AndroidConfigurablePositionComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
	}

	@Override
	public void configurationSetError(String p_oError) {
		if( ((AbstractPositionMasterView) this.currentView).getLatitudeView().getText().toString().equals(StringUtils.EMPTY) ){
			((AbstractPositionMasterView) this.currentView).getLatitudeView().setError(p_oError);
		}
		if( ((AbstractPositionMasterView) this.currentView).getLongitudeView().getText().toString().equals(StringUtils.EMPTY)){
			((AbstractPositionMasterView) this.currentView).getLongitudeView().setError(p_oError);
		}
	}
	
	@Override
	public void configurationUnsetError() {
		if(!((AbstractPositionMasterView) this.currentView).getLatitudeView().getText().toString().equals(StringUtils.EMPTY) 
				&& ((AbstractPositionMasterView) this.currentView).getLatitudeView().getError() != null) {
			((AbstractPositionMasterView) this.currentView).getLatitudeView().setError(null);
		}
		if(!((AbstractPositionMasterView) this.currentView).getLongitudeView().getText().toString().equals(StringUtils.EMPTY) 
				&& ((AbstractPositionMasterView) this.currentView).getLongitudeView().getError() != null) {
			((AbstractPositionMasterView) this.currentView).getLongitudeView().setError(null);
		}
	}
	
}
