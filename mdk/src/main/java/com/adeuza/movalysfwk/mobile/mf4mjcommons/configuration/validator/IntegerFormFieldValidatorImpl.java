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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator;

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * Validator for any field which type is integer.
 */
public class IntegerFormFieldValidatorImpl extends AbstractFormFieldValidator implements IntegerFormFieldValidator {
	/** {@inheritDoc} */
	@Override
	public boolean validate(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		boolean r_bIsCorrect = true;
		String sValue = String.valueOf( p_oComponent.getComponentDelegate().configurationGetValue() );
		if ( sValue!= null && sValue.length() > 0 && !"null".equals(sValue)) {
			// Is the field value an integer?
			try {
				// Try to get an integer from the field value.
				Integer.parseInt(sValue.toString());
			} catch (NumberFormatException oException) {
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_integer_data, "");
				r_bIsCorrect = false;
			}
			if (r_bIsCorrect) {
				r_bIsCorrect = this.validateMaxLength(p_oComponent, p_oConfiguration, p_oErrorBuilder);
			}
			if (r_bIsCorrect) {
				r_bIsCorrect = this.validateMinValue(p_oComponent, p_oConfiguration, p_oErrorBuilder);
			}
			if (r_bIsCorrect) {
				r_bIsCorrect = this.validateMaxValue(p_oComponent, p_oConfiguration, p_oErrorBuilder);
			}
		}
		return r_bIsCorrect;
	}
	/** {@inheritDoc} */
	@Override
	public boolean validateMinValue(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		Object oMinValue = null;
		if (p_oConfiguration != null) {
			oMinValue = p_oConfiguration.getParameter(getMinValueParameterName());
		
			if (oMinValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				oMinValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(getMinValueParameterName());
			}
		}
		if (oMinValue != null
				&& p_oComponent.getComponentDelegate().configurationGetValue() != null
				&& Integer.parseInt(p_oComponent.getComponentDelegate().configurationGetValue().toString()) < Integer.parseInt(oMinValue.toString())) {
			this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_min_value, oMinValue.toString());
			return false;
		}
		return true;
	}
	/** {@inheritDoc} */
	@Override
	public boolean validateMaxValue(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		Object oMaxValue = this.getMaxValue(p_oConfiguration);
		if (oMaxValue != null
				&& p_oComponent.getComponentDelegate().configurationGetValue() != null
				&& Integer.parseInt(p_oComponent.getComponentDelegate().configurationGetValue().toString()) > Integer.parseInt(oMaxValue.toString())) {
			this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_max_value, oMaxValue.toString());
			return false;
		}
		return true;
	}
	/** {@inheritDoc} */
	@Override
	public Integer getMinValue(BasicComponentConfiguration p_oConfiguration) {
		Object oMinValue = null;
		if (p_oConfiguration != null) {
			oMinValue = p_oConfiguration.getParameter(getMinValueParameterName());
			if (oMinValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				oMinValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(getMinValueParameterName());
			}
		}
		if (oMinValue != null ) {
			return Integer.parseInt(oMinValue.toString());
		}
		return null ;
	}
	/** {@inheritDoc} */
	@Override
	public Integer getMaxValue(BasicComponentConfiguration p_oConfiguration) {
		Object oMaxValue = null;
		if (p_oConfiguration != null) {
			oMaxValue = p_oConfiguration.getParameter(getMaxValueParameterName());
			if (oMaxValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				oMaxValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(getMaxValueParameterName());
			}
		}
		if (oMaxValue != null ) {
			return Integer.parseInt(oMaxValue.toString());
		}
		return null ;
	}
	/**
	 * {@inheritDoc}
	 *
	 * Définit le nom du paramètre contenant la valeur minimale à respecter
	 */
	@Override
	public String getMinValueParameterName() {
		return ConfigurableVisualComponent.Attribute.MIN_VALUE_ATTRIBUTE.getName();
	}
	/**
	 * {@inheritDoc}
	 *
	 * Définit le nom du paramètre contenant la valeur maximale à respecter
	 */
	@Override
	public String getMaxValueParameterName() {
		return ConfigurableVisualComponent.Attribute.MAX_VALUE_ATTRIBUTE.getName();
	}
	/** {@inheritDoc} */
	@Override
	public void addParametersToConfiguration(Map<ConfigurableVisualComponent.Attribute, String> p_oAtt,
			BasicComponentConfiguration p_oConfiguration) {
		super.addParametersToConfiguration(p_oAtt, p_oConfiguration);
		if (p_oAtt != null && p_oConfiguration != null) {
			p_oConfiguration.addParameter(getMinValueParameterName(), 
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MIN_VALUE_ATTRIBUTE));
			p_oConfiguration.addParameter(getMaxValueParameterName(), 
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MAX_VALUE_ATTRIBUTE));
		}
	}
}
