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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * Classe abstraite permettant de valider les éventuelles expressions régulières définies.
 */
public abstract class AbstractDateTimeFieldValidator extends AbstractFormFieldValidator {

	/**
	 * @param p_oDateFormat a {@link java.text.DateFormat} object.
	 * @param p_oDate a {@link java.util.Calendar} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return a boolean.
	 */
	public boolean validateMinDateValue(SimpleDateFormat p_oDateFormat, Calendar p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder) {
		Object oMinDateValue = null ;
		if (p_oConfiguration != null) {
			oMinDateValue = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.MIN_DATE_ATTRIBUTE.getName());
		}
		if (oMinDateValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
			oMinDateValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.MIN_DATE_ATTRIBUTE.getName());
		}
		if (oMinDateValue != null && oMinDateValue.toString().length() > 0){
			if (p_oDate.getTimeInMillis() < Long.parseLong(oMinDateValue.toString())) {
				Date dMinDate = new Date(Long.parseLong(oMinDateValue.toString()));
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format_min_date, p_oDateFormat.format(dMinDate));					
				return false;
			}

		}
		return true;
	}

	/**
	 * @param p_oDateFormat a {@link java.text.DateFormat} object.
	 * @param p_oDate a {@link java.util.Calendar} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return a boolean.
	 */
	public boolean validateMaxDateValue(SimpleDateFormat p_oDateFormat, Calendar p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder) {
		Object oMaxDateValue = null ;
		if (p_oConfiguration != null) {
			oMaxDateValue = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.MAX_DATE_ATTRIBUTE.getName());
		}
		if (oMaxDateValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
			oMaxDateValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.MAX_DATE_ATTRIBUTE.getName());
		}
		if (oMaxDateValue != null && oMaxDateValue.toString().length() > 0){
			if (p_oDate.getTimeInMillis() > Long.parseLong(oMaxDateValue.toString())) {
				Date dMaxDate = new Date(Long.parseLong(oMaxDateValue.toString()));
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format_max_date, p_oDateFormat.format(dMaxDate));					
				return false;
			}

		}
		return true;
	}
	
	/**
	 * @param p_oDate a {@link java.util.Calendar} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return a boolean.
	 */
	public boolean validateMinTimeValue(Calendar p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder) {
		Object oMinMinValue = null ;
		Object oMinHourValue = null ;
		if (p_oConfiguration != null) {
			oMinHourValue = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.MIN_HOURS_ATTRIBUTE.getName());
			oMinMinValue = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.MIN_MINUTES_ATTRIBUTE.getName());
		}
		if (oMinHourValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
			oMinHourValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.MIN_HOURS_ATTRIBUTE.getName());
			oMinMinValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.MIN_MINUTES_ATTRIBUTE.getName());
		}
		if (oMinHourValue != null && oMinHourValue.toString().length() > 0){
			int iCompareHours = p_oDate.get(Calendar.HOUR_OF_DAY) - Integer.parseInt(oMinHourValue.toString()) ;
			if (iCompareHours < 0 ) {
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format_min_hour, oMinHourValue.toString() +":"+ oMinMinValue.toString());					
				return false;
			}
			else if ( iCompareHours == 0 && (p_oDate.get(Calendar.MINUTE) - Integer.parseInt(oMinMinValue.toString())) < 0 ){
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format_min_hour, oMinHourValue.toString() +":"+ oMinMinValue.toString());					
				return false;
			}
		}
		return true;
	}
	/**
	 * @param p_oDate a {@link java.util.Calendar} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return a boolean.
	 */
	public boolean validateMaxTimeValue(Calendar p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder) {
		Object oMaxMinValue = null ;
		Object oMaxHourValue = null ;
		if (p_oConfiguration != null) {
			oMaxHourValue = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.MAX_HOURS_ATTRIBUTE.getName());
			oMaxMinValue = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.MAX_MINUTES_ATTRIBUTE.getName());
		}
		if (oMaxHourValue == null && p_oConfiguration.getEntityFieldConfiguration() != null) {
			oMaxHourValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.MAX_HOURS_ATTRIBUTE.getName());
			oMaxMinValue = p_oConfiguration.getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.MAX_MINUTES_ATTRIBUTE.getName());
		}
		if (oMaxHourValue != null && oMaxHourValue.toString().length() > 0){
			int iCompareHours = Integer.parseInt(oMaxHourValue.toString()) - p_oDate.get(Calendar.HOUR_OF_DAY) ;
			if ( iCompareHours == 0 && (Integer.parseInt(oMaxMinValue.toString()) - p_oDate.get(Calendar.MINUTE)) < 0 ){
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format_max_hour, oMaxMinValue.toString() +":"+ oMaxHourValue.toString());					
				return false;
			} else if (iCompareHours < 0 ) {
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format_max_hour, oMaxMinValue.toString() +":"+ oMaxHourValue.toString());					
				return false;
			} 
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public void addParametersToConfiguration(Map<ConfigurableVisualComponent.Attribute,String> p_oAtt, BasicComponentConfiguration p_oConfiguration) {
		super.addParametersToConfiguration(p_oAtt,p_oConfiguration);
		if (p_oAtt != null && p_oConfiguration!=null){
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.MIN_DATE_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MIN_DATE_ATTRIBUTE));
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.MAX_DATE_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MAX_DATE_ATTRIBUTE));

			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.MIN_HOURS_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MIN_HOURS_ATTRIBUTE));
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.MAX_HOURS_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MAX_HOURS_ATTRIBUTE));

			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.MIN_MINUTES_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MIN_MINUTES_ATTRIBUTE));
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.MAX_MINUTES_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MAX_MINUTES_ATTRIBUTE));
		}
	}
}
