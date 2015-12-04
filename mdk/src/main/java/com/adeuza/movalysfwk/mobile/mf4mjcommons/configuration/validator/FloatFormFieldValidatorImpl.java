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
 * Validator for any field which type is double.
 */
public class FloatFormFieldValidatorImpl extends AbstractFormFieldValidator implements FloatFormFieldValidator {
	/** {@inheritDoc} */
	@Override
	public boolean validate(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		boolean r_bIsCorrect = true;
		String sValue = String.valueOf( p_oComponent.getComponentDelegate().configurationGetValue() );
		if ( sValue!= null && sValue.length() > 0 && !"null".equals(sValue)) {
			// Is the field value an double?
			try {
				// Try to get a double from the field value.
				Float.parseFloat(sValue);
			} catch (NumberFormatException exception) {
				r_bIsCorrect = false;
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_decimal_data, "" );
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
			if (r_bIsCorrect) {
				r_bIsCorrect = this.validateDigitNumber(p_oComponent, p_oConfiguration, p_oErrorBuilder);
			}
		}
		return r_bIsCorrect;
	}
	/** {@inheritDoc} */
	@Override
	public boolean validateMinValue(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		Object oMinValue = this.getMinValue(p_oConfiguration);
		if (oMinValue != null
				&& p_oComponent.getComponentDelegate().configurationGetValue() != null
				&& Float.parseFloat(p_oComponent.getComponentDelegate().configurationGetValue().toString()) < Float.parseFloat(oMinValue.toString())) {
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
				&& Float.parseFloat(p_oComponent.getComponentDelegate().configurationGetValue().toString()) > Float.parseFloat(oMaxValue.toString())) {
			this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_max_value, oMaxValue.toString());
			return false;
		}
		return true;
	}
	/** {@inheritDoc} */
	@Override
	public boolean validateDigitNumber(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		Object oMaxNbDigit = null;
		if (p_oConfiguration != null) {
			oMaxNbDigit = p_oConfiguration.getParameter(getNbDigitParameterName());
		
			if (oMaxNbDigit == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				oMaxNbDigit = p_oConfiguration.getEntityFieldConfiguration().getParameter(getNbDigitParameterName());
			}
		}
		if (oMaxNbDigit != null && p_oComponent.getComponentDelegate().configurationGetValue() != null) {
			String sValue = p_oComponent.getComponentDelegate().configurationGetValue().toString();
			String[] sPosDecimal = sValue.split("\\.");
			if (sPosDecimal.length == 2 && sPosDecimal[1].length() > Integer.parseInt(oMaxNbDigit.toString())) {
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format_nb_digit, oMaxNbDigit.toString());
				return false;
			}			
		}
		return true;
	}
	/**
	 * {@inheritDoc}
	 *
	 * Définit le nom du paramètre contenant la valeur minimale à respecter
	 */
	@Override
	public String getMinValueParameterName() {
		return "min-value";
	}
	/**
	 * {@inheritDoc}
	 *
	 * Définit le nom du paramètre contenant la valeur maximale à respecter
	 */
	@Override
	public String getMaxValueParameterName() {
		return "max-value";
	}
	/**
	 * {@inheritDoc}
	 *
	 * Définit le nom du paramètre contenant la valeur maximale à respecter
	 */
	@Override
	public String getNbDigitParameterName() {
		return "nb-digit";
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
			p_oConfiguration.addParameter(getNbDigitParameterName(), 
					p_oAtt.get(ConfigurableVisualComponent.Attribute.NB_DIGITS_ATTRIBUTE));
		}
	}
	/** {@inheritDoc} */
	@Override
	public Float getMinValue(BasicComponentConfiguration p_oConfiguration){
		Object oMinValue = null;
		if (p_oConfiguration != null) {
			oMinValue = p_oConfiguration.getParameter(getMinValueParameterName());
			if (oMinValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				oMinValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(getMinValueParameterName());
			}
		}
		if (oMinValue != null ) {
			return Float.parseFloat(oMinValue.toString());
		}
		return null ;
	}
	/** {@inheritDoc} */
	@Override
	public Float getMaxValue(BasicComponentConfiguration p_oConfiguration){
		Object oMaxValue = null;
		if (p_oConfiguration != null) {
			oMaxValue = p_oConfiguration.getParameter(getMaxValueParameterName());
			if (oMaxValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				oMaxValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(getMaxValueParameterName());
			}
		}
		if (oMaxValue != null ) {
			return Float.parseFloat(oMaxValue.toString());
		}
		return null ;
	}

}
