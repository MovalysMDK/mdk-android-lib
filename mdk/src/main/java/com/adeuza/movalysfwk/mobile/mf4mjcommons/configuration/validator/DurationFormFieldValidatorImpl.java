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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.DurationUtils;
/**
 * Validator for any field which type is date.
 */
public class DurationFormFieldValidatorImpl extends AbstractDateTimeFieldValidator implements DurationFormFieldValidator {
	/**
	 * Default Form Date
	 */
	public static final String DEFAULT_DURATION_FORMAT = "HHH:mm";
	/** {@inheritDoc} */
	@Override
	public boolean validate(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		boolean r_bIsCorrect = true;
		String sValue = String.valueOf( p_oComponent.getComponentDelegate().configurationGetValue() );
		if ( sValue!= null && sValue.length() > 0 && !"null".equals(sValue)) {
			Calendar oCalendar = Calendar.getInstance();
			SimpleDateFormat oDateFormat = (SimpleDateFormat)this.getDateFormat(p_oConfiguration);
			
			if (!sValue.contains(":") && this.isOnlyMinutesEditingAuthorized(p_oConfiguration) ){ 
				// on autorise la saisie d'un nombre seul indiquant le nombre de minutes
				sValue = DurationUtils.convertMinutesToDuration(sValue).toString();
			}
			
			try {
				Date oDate = oDateFormat.parse(sValue);
				oCalendar.setTime(oDate);
			} catch (ParseException oException) {
				r_bIsCorrect = false;
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format, oDateFormat.toPattern());
			}
			if (r_bIsCorrect) {
				r_bIsCorrect = this.validateMinValue(sValue, p_oConfiguration, p_oErrorBuilder);
			} 
			if (r_bIsCorrect) {
				r_bIsCorrect = this.validateMaxValue(sValue, p_oConfiguration, p_oErrorBuilder);
			}
		}
		return r_bIsCorrect;
	}
	
	/**
	 * <p>isOnlyMinutesEditingAuthorized.</p>
	 *
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @return a boolean.
	 */
	public boolean isOnlyMinutesEditingAuthorized(BasicComponentConfiguration p_oConfiguration){
		boolean r_bIsMinutesEditingAuthorized  = true ;
		Object oMinutesEditingAuthorized = null;
		if (p_oConfiguration != null) {
			oMinutesEditingAuthorized = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.ONLY_MINUTES_ATTRIBUTE.getName());
		}
		if (oMinutesEditingAuthorized == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
			oMinutesEditingAuthorized = p_oConfiguration.getEntityFieldConfiguration().getParameter(
					ConfigurableVisualComponent.Attribute.ONLY_MINUTES_ATTRIBUTE.getName());
		}
		if (oMinutesEditingAuthorized == null && this.getOnlyMinutesPropertyName() != null) {
			Property oRegexProp = ConfigurationsHandler.getInstance().getProperty(getOnlyMinutesPropertyName());
			if (oRegexProp != null) {
				oMinutesEditingAuthorized = oRegexProp.getBooleanValue();
			}
		}		
		if (oMinutesEditingAuthorized != null) {
			r_bIsMinutesEditingAuthorized = Boolean.parseBoolean(oMinutesEditingAuthorized.toString()) ;
		}
		return r_bIsMinutesEditingAuthorized ;
	}
	/**
	 * {@inheritDoc}
	 *
	 * Définit le format de date à utiliser
	 */
	@Override
	public SimpleDateFormat getDateFormat(BasicComponentConfiguration p_oConfiguration) {
		Object sCustomDateFormat = null;
		if (p_oConfiguration != null) {
			sCustomDateFormat = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE.getName());
		
			if (sCustomDateFormat == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				sCustomDateFormat = p_oConfiguration.getEntityFieldConfiguration().getParameter(
						ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE.getName());
			}
		}
		if (sCustomDateFormat == null && this.getFormatPropertyName() != null) {
			Property oRegexProp = ConfigurationsHandler.getInstance().getProperty(getFormatPropertyName());
			if (oRegexProp != null) {
				sCustomDateFormat = oRegexProp.getValue();
			}
		}
		if (sCustomDateFormat == null) {
			sCustomDateFormat = this.getDefaultFormat();
		}
		// Create the SimpleDateFormat object.
		SimpleDateFormat oDateFormat = new SimpleDateFormat(sCustomDateFormat.toString());
		oDateFormat.setLenient(true);
		return oDateFormat;
	}
	/**
	 * {@inheritDoc}
	 *
	 * Permet de récupérer le nom de la propriété du format de date lié au type complexe.
	 */
	@Override
	public String getFormatPropertyName() {
		return "DEFAULT_DURATION_FORMAT";
	}
	/**
	 * {@inheritDoc}
	 *
	 * Permet de récupérer le nom de la propriété indiquant si on autorise de saisir que des minutes
	 */
	@Override
	public String getOnlyMinutesPropertyName() {
		return "DEFAULT_DURATION_ONLY_MINUTES_AUTHORIZED";
	}
	/**
	 * {@inheritDoc}
	 *
	 * Permet de récupérer le format de date defini par défaut
	 */
	@Override
	public String getDefaultFormat() {
		return DEFAULT_DURATION_FORMAT;
	}
	/** {@inheritDoc} */
	@Override
	public boolean validateMinValue(String p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder) {
		boolean r_bIsCorrect = true;
		String oMinValue = this.getMinValue(p_oConfiguration);
		
		if (oMinValue != null && oMinValue.contains(":") && p_oDate.contains(":")) {
			String[] sLimitField = oMinValue.split(":");
			String[] sDateFields = p_oDate.split(":");
			if (Integer.parseInt(sLimitField[0]) < Integer.parseInt(sDateFields[0])) {
				r_bIsCorrect = true;
			} else {
				if (Integer.parseInt(sLimitField[0]) > Integer.parseInt(sDateFields[0])) {
					r_bIsCorrect = false;
				} else { // égalite des heures
					if (Integer.parseInt(sLimitField[1]) > Integer.parseInt(sDateFields[1])) {
						r_bIsCorrect = false;
					}
				}
			}
			if (!r_bIsCorrect) {
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_min_value, oMinValue.toString());
			}
		}
		return r_bIsCorrect;
	}
	/** {@inheritDoc} */
	@Override
	public boolean validateMaxValue(String p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder) {
		boolean r_bISCorrect = true;
		Object oMaxValue = this.getMaxValue(p_oConfiguration);
		if (oMaxValue != null && ((String) oMaxValue).contains(":") && p_oDate.contains(":")) {
			String[] sLimitField = ((String) oMaxValue).split(":");
			String[] sDateFields = p_oDate.split(":");
			if (Integer.parseInt(sLimitField[0]) > Integer.parseInt(sDateFields[0])) {
				r_bISCorrect = true;
			} else {
				if (Integer.parseInt(sLimitField[0]) < Integer.parseInt(sDateFields[0])) {
					r_bISCorrect = false;
				} else { // égalite des heures
					if (Integer.parseInt(sLimitField[1]) < Integer.parseInt(sDateFields[1])) {
						r_bISCorrect = false;
					}
				}
			}
			if (!r_bISCorrect) {
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_max_value, oMaxValue.toString());
			}
		}
		return r_bISCorrect;
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
	/** {@inheritDoc} */
	@Override
	public void addParametersToConfiguration(Map<ConfigurableVisualComponent.Attribute, String> p_oAtt,
			BasicComponentConfiguration p_oConfiguration) {
		super.addParametersToConfiguration(p_oAtt, p_oConfiguration);
		if (p_oAtt != null && p_oConfiguration != null) {
			p_oConfiguration.addParameter(getMinValueParameterName(), p_oAtt.get(ConfigurableVisualComponent.Attribute.MIN_VALUE_ATTRIBUTE));
			p_oConfiguration.addParameter(getMaxValueParameterName(), p_oAtt.get(ConfigurableVisualComponent.Attribute.MAX_VALUE_ATTRIBUTE));
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE));
		}
	}
	/** {@inheritDoc} */
	@Override
	public String getMinValue(BasicComponentConfiguration p_oConfiguration){
		Object oMinValue = null;
		if (p_oConfiguration != null) {
			oMinValue = p_oConfiguration.getParameter(getMinValueParameterName());
			if (oMinValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				oMinValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(getMinValueParameterName());
			}
		}
		if (oMinValue != null ) {
			return oMinValue.toString();
		}
		return null ;
	}
	/** {@inheritDoc} */
	@Override
	public String getMaxValue(BasicComponentConfiguration p_oConfiguration){
		Object oMaxValue = null;
		if (p_oConfiguration != null) {
			oMaxValue = p_oConfiguration.getParameter(getMaxValueParameterName());
			if (oMaxValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				oMaxValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(getMaxValueParameterName());
			}
		}
		if (oMaxValue != null ) {
			return oMaxValue.toString();
		}
		return null ;
	}
}
