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

import java.util.ArrayList;

import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsSpinner;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.spinner.MDKSpinnerConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSearchSpinner;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSimpleSpinner;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSpinner;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Delegate for spinner components
 * 
 * <p>Contains default behavior of Spinner components</p>
 *
 * @param <VALUE> the type handled by component
 */
public class AndroidConfigurableSpinnerComponentDelegate<VALUE>  extends
		AndroidConfigurableVisualComponentDelegate<VALUE> {

	/**
	 * Create new AndroidConfigurableSpinnerComponentDelegate
	 * @param p_oCurrentView the component view
	 * @param p_oDelegateType the component handled class type
	 */
	public AndroidConfigurableSpinnerComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Create new AndroidConfigurableSpinnerComponentDelegate
	 * @param p_oCurrentView the component view
	 * @param p_oDelegateType the component handled class type
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 */
	public AndroidConfigurableSpinnerComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
	}
	
	@Override
	public void configurationUnsetError() {
		super.configurationUnsetError();
		
		if (!this.isWritingData()) { 
			if (this.currentView instanceof AbsSpinner || this.currentView instanceof MMSearchSpinner) {

				this.getSpinnerViewAdapter().doOnErrorOnSelectedItem(this.currentView, null);
				
				View oComponent = this.currentView.findViewById(((AndroidApplication)Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.component_customfields__edittext__value));
				if (oComponent instanceof TextView) {
					ArrayList<View> oList = new ArrayList<>();
					oList.add(oComponent);
					((MMSimpleSpinner)this.currentView).addFocusables(oList, View.FOCUSABLES_ALL);
					((TextView) oComponent).setError(null);
				}
			}
			
			if (this.currentView instanceof MMSpinner<?, ?>) {
				((MMSpinner<?, ?>) this.currentView).setError(null);
			}
		}

	}

	@Override
	public void configurationSetError(String p_oError) {
		super.configurationSetError(p_oError);
		
		if (this.currentView instanceof AbsSpinner || this.currentView instanceof MMSearchSpinner) {
			this.getSpinnerViewAdapter().doOnErrorOnSelectedItem(this.currentView, p_oError);
			
			View oComponent = this.currentView.findViewById(((AndroidApplication)Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.component_customfields__edittext__value));
			if (oComponent instanceof TextView) {
				ArrayList<View> oList = new ArrayList<>();
				oList.add(oComponent);
				((MMSimpleSpinner)this.currentView).addFocusables(oList, View.FOCUSABLES_ALL);
				((TextView) oComponent).setError(p_oError);
			}
			
			if (this.currentView instanceof MMSpinner<?, ?>) {
				((MMSpinner<?, ?>) this.currentView).setError(p_oError);
			}
		}
		
	}
	
	/**
	 * returns the adapter for the spinner
	 * @return adapter of the spinner
	 */
	private MDKSpinnerConnector getSpinnerViewAdapter() {
		if (this.currentView instanceof AbsSpinner) {
			return ((MDKSpinnerConnector) ((AbsSpinner) this.currentView).getAdapter());
		} else {
			return ((MDKSpinnerConnector) ((MMSearchSpinner<?, ?>) this.currentView).getAdapter());
		}
	}
}
