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
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * TextView Delegate
 * <p>Handle behaviors on TextView and all inherited classes</p>
 *
 */
public class AndroidConfigurableTextViewComponentDelegate extends
		AndroidConfigurableVisualComponentDelegate<String> {
	
	/**
	 * Constructor
	 * @param p_oCurrentView the component (inherited from TextView)
	 * @param p_oDelegateType the type of the delegate
	 */
	public AndroidConfigurableTextViewComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Constructor
	 * @param p_oCurrentView the component (inherited from TextView)
	 * @param p_oDelegateType the type of the delegate
	 * @param p_oAttrs this AttributeSet of the View
	 */
	public AndroidConfigurableTextViewComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
	}

	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		try {
			TextView textView = (TextView) this.currentView;
			String oReturnValue = textView.getText().toString();
			if (oReturnValue.length() == 0) {
				oReturnValue = null;
			}
			oReturnValue = (String) getUnFormattedValue((String) oReturnValue);
			return oReturnValue;
		} catch (ClassCastException e) {
			throw new RuntimeException("component : "+this.currentView+" is not an instance of textview!", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		this.writingData = true;
		String r_sString = p_oObjectToSet;
		try {
			TextView textView = (TextView) this.currentView;
			if (r_sString != null) {
				r_sString = getFormattedValue(p_oObjectToSet);
			}
			textView.setText(r_sString);
		} catch (ClassCastException e) {
			throw new RuntimeException("component : "+this.currentView+" is not an instance of textview!", e);
		}
		super.configurationSetValue(r_sString);
	}
	
	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#isFilled()
	 */
	@Override
	public boolean isFilled() {
		try {
			TextView textView = (TextView) this.currentView;
			return textView.getText().toString().length() > 0;
		} catch (ClassCastException e) {
			throw new RuntimeException("component : "+this.currentView+" is not an instance of textview!", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#validate(BasicComponentConfiguration, Map, StringBuilder)
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		if (this.currentCvComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().getMinsize()!=0 
				&& this.isFilled()
				&& this.configurationGetValue() != null
				&& ((String) this.configurationGetValue()).length()<this.currentCvComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().getMinsize() ) {
			if (p_oErrorBuilder.length()>0) {
				p_oErrorBuilder.append('\n');
			}
			p_oErrorBuilder.append(Application.getInstance().getStringResource(DefaultApplicationR.error_minsize1)).append(' ');
			p_oErrorBuilder.append(this.currentCvComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().getMinsize()).append(' ');
			p_oErrorBuilder.append(Application.getInstance().getStringResource(DefaultApplicationR.error_minsize2));
			return false;
		}
		
		boolean r_valide = true;
		if (this.currentView instanceof ComponentValidator) {
			r_valide = ((ComponentValidator) this.currentView).getValidator().validate(this.currentCvComponent, p_oConfiguration, p_oErrorBuilder);
		}
		return r_valide;
	}
	
	/**
	 * {@inheritDoc}
	 * @see AndroidConfigurableVisualComponentDelegate#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
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
