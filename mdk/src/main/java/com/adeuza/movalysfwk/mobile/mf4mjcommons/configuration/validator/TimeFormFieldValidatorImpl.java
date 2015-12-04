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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * Validator for any field which type is date.
 */
public class TimeFormFieldValidatorImpl extends AbstractDateTimeFieldValidator implements TimeFormFieldValidator {
	/**
	 * Default Form Date
	 */
	public static final String DEFAULT_TIME_FORMAT = "HH:mm";
	/** {@inheritDoc} */
	@Override
	public boolean validate(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		boolean r_bIsCorrect = true;
		Calendar oCalendar = Calendar.getInstance();
		SimpleDateFormat oDateFormat = (SimpleDateFormat)this.getDateFormat(p_oConfiguration);
		String sValue = String.valueOf( p_oComponent.getComponentDelegate().configurationGetValue() );
		if ( sValue!= null && sValue.length() > 0 && !"null".equals(sValue)) {
			try {
				Date oDate = oDateFormat.parse(sValue);
				oCalendar.setTime(oDate);
			}  catch (ParseException oException) {
				if ( p_oComponent.getComponentDelegate().getValueType().equals(Long.class)){
					try {	
						oCalendar.setTimeInMillis(Long.valueOf(sValue));
					}  catch (NumberFormatException oException2) {
						r_bIsCorrect = false;
					}
				}else {
					r_bIsCorrect = false;
				}
			}
		}
		if (r_bIsCorrect) {
			r_bIsCorrect = this.validateMinTimeValue(oCalendar, p_oConfiguration, p_oErrorBuilder);
		}
		if (r_bIsCorrect) {
			r_bIsCorrect = this.validateMaxTimeValue(oCalendar, p_oConfiguration, p_oErrorBuilder);
		}
		return r_bIsCorrect;
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
			Object sTimeFormat = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.TIME_FORMAT_ATTRIBUTE.getName());
			if (sTimeFormat!= null){
				int iTimeFormat = DateFormat.SHORT;
				if (FULL_FORMAT.equals(sTimeFormat.toString())){
					iTimeFormat = DateFormat.FULL;
				}else if (LONG_FORMAT.equals(sTimeFormat.toString())){
					iTimeFormat = DateFormat.LONG;
				}else if (MEDIUM_FORMAT.equals(sTimeFormat.toString())){
					iTimeFormat = DateFormat.MEDIUM;
				}
				sCustomDateFormat = ((SimpleDateFormat)DateFormat.getTimeInstance(iTimeFormat)).toPattern();
			}
		
			if (sCustomDateFormat == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
				sCustomDateFormat = p_oConfiguration.getEntityFieldConfiguration().getParameter(
						ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE.getName());
			}
		}
		if (sCustomDateFormat == null && this.getDateFormatPropertyName() != null) {
			Property oRegexProp = ConfigurationsHandler.getInstance().getProperty(getDateFormatPropertyName());
			if (oRegexProp != null) {
				sCustomDateFormat = oRegexProp.getValue();
			}
		}
		if (sCustomDateFormat == null) {
			sCustomDateFormat = this.getDefaultDateFormat();
		}
		// Create the SimpleDateFormat object.
		SimpleDateFormat oDateFormat = new SimpleDateFormat(sCustomDateFormat.toString());
		oDateFormat.setLenient(true);
		return oDateFormat;
	}
	/**
	 * Permet de récupérer le nom de la propriété du format de date lié au type complexe.
	 *
	 * @return le nom de la propriété du format de date lié au type complexe.
	 */
	@Override
	public String getDateFormatPropertyName() {
		return "DEFAULT_TIME_FORMAT";
	}
	/**
	 * Permet de récupérer le format de date defini par défaut
	 *
	 * @return le format de date defini par défaut si pas de conf définie dans l'entité ou la conf globale
	 */
	@Override
	public String getDefaultDateFormat() {
		return DEFAULT_TIME_FORMAT;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.AbstractTimeFieldValidator#addParametersToConfiguration(java.util.Map, com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public void addParametersToConfiguration(Map<ConfigurableVisualComponent.Attribute, String> p_oAtt,
			BasicComponentConfiguration p_oConfiguration) {
		super.addParametersToConfiguration(p_oAtt, p_oConfiguration);
		if (p_oAtt != null && p_oConfiguration != null) {
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE));
		}
	}
}
