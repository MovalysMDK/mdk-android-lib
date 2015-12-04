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
import java.util.regex.PatternSyntaxException;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ValidableComponent;
/**
 * Validator for any field which type is date.
 */
public class DateFormFieldValidatorImpl extends AbstractDateTimeFieldValidator implements DateFormFieldValidator {
	/**
	 * Default Form Date
	 */
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	/** Constant <code>DEFAULT_DAY_LENGHT=2</code> */
	public static final int DEFAULT_DAY_LENGHT = 2;
	/** Constant <code>DEFAULT_MONTH_LENGHT=2</code> */
	public static final int DEFAULT_MONTH_LENGHT = 2;
	/** Constant <code>DEFAULT_YEAR_LENGHT=4</code> */
	public static final int DEFAULT_YEAR_LENGHT = 4;
	
	/**
	 * Année max du picker de date
	 */
	private static final int MAX_YEAR_OF_ANDROID_PICKER = 2100;
	/**
	 * Année min du picker de date
	 */
	private static final int MIN_YEAR_OF_ANDROID_PICKER = 1900;

	/** {@inheritDoc} */
	@Override
	public boolean validate(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder) {
		boolean r_bIsCorrect = true;
		boolean bValidData = true;
		Calendar oCalendar = Calendar.getInstance();
		SimpleDateFormat oDateFormat = (SimpleDateFormat)this.getDateFormat(p_oConfiguration);
		String sValue = null;
		
		if (p_oComponent instanceof ValidableComponent) {
			sValue = String.valueOf(((ValidableComponent) p_oComponent).getValueToValidate());
			
			// FIXME à améliorer : contrôle du format de la date
			// Une date incohérente ne génère pas d'exception de parsing
			// Il faut valider la chaîne selon le format de date donné dans la configuration.
			if (! this.validateFormat(oDateFormat, sValue)) {
				r_bIsCorrect = false;
				sValue = null;
			}
		} else {
			sValue = String.valueOf( p_oComponent.getComponentDelegate().configurationGetValue() );
		}

		if ( sValue!= null && sValue.length() > 0 && !"null".equals(sValue)) {
			Date oDate = null ;
			try {
				oDate = oDateFormat.parse(sValue);
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

			if (oDateFormat.toPattern().indexOf("yyyy") != -1) {
				int iYear = oCalendar.get(Calendar.YEAR);
				// Année sur 4 + ORA-01841: (full) year must be between -4713 and +9999, and not be 0
				if (iYear > MAX_YEAR_OF_ANDROID_PICKER || iYear < MIN_YEAR_OF_ANDROID_PICKER) {
					r_bIsCorrect = false;
				}
			}
		}
	
		if (!r_bIsCorrect) {
			this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data, oDateFormat.toPattern());
		}
		
		if (r_bIsCorrect) {
			r_bIsCorrect = this.validateMinDateValue(oDateFormat, oCalendar, p_oConfiguration, p_oErrorBuilder);
		}
		if (r_bIsCorrect) {
			r_bIsCorrect = this.validateMaxDateValue(oDateFormat, oCalendar, p_oConfiguration, p_oErrorBuilder);
		}
		
		return r_bIsCorrect;
	}
	
	/**
	 * FIXME à améliorer : contrôle du format de la date Valide la chaîne selon le format de date donné dans la configuration.
	 * 
	 * @param p_oDateFormat
	 *            format de la date
	 * @param p_sValue
	 *            chaîne représentant la date à vérifier
	 * @return true si la date respecte le format donné, false sinon
	 */
	private boolean validateFormat(SimpleDateFormat p_oDateFormat, String p_sValue) {

		boolean bResult = true;
		if (p_sValue != null && p_sValue.length() > 0 && !"null".equals(p_sValue)) {
			if (DEFAULT_DATE_FORMAT.equals(p_oDateFormat.toPattern())) {
				try {
					String[] tDate = p_sValue.split("/");
					Integer iDay = Integer.valueOf(tDate[0]).intValue();
					Integer iMonth = Integer.valueOf(tDate[1]).intValue();
					Integer iYear = Integer.valueOf(tDate[2]).intValue();
					
					bResult = bResult && iDay > 0 && iDay < 32 && tDate[0].length() == DEFAULT_DAY_LENGHT;
					bResult = bResult && iMonth > 0 && iMonth < 13 && tDate[1].length() == DEFAULT_MONTH_LENGHT;
					bResult = bResult && iYear >= MIN_YEAR_OF_ANDROID_PICKER && iYear <= MAX_YEAR_OF_ANDROID_PICKER && tDate[2].length() == DEFAULT_YEAR_LENGHT;
					
					Date oDate;
					try {
						oDate = p_oDateFormat.parse(p_sValue);
						Calendar oCalendar = Calendar.getInstance();
						oCalendar.setTime(oDate);
						oCalendar.setLenient(true);
						if (oCalendar.get(Calendar.YEAR) != iYear || (oCalendar.get(Calendar.MONTH)+1) != iMonth ||  oCalendar.get(Calendar.DAY_OF_MONTH) != iDay)
							bResult = false;
					} catch (ParseException oException3) {
						bResult = false;
					}
					
					
				} catch (PatternSyntaxException oException1) {
					bResult = false;
				} catch (NumberFormatException oException2) {
					bResult = false;
				} catch (ArrayIndexOutOfBoundsException oException3) {
					bResult = false;
				}
			}
		}
		return bResult;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Définit le format de date à utiliser
	 */
	@Override
	public SimpleDateFormat getDateFormat(BasicComponentConfiguration p_oConfiguration) {
		Object sCustomDateFormat = null ;
		if (p_oConfiguration!= null ) {
			Object sDateFormat = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.DATE_FORMAT_ATTRIBUTE.getName());
			if (sDateFormat!= null){
				int iDateFormat = DateFormat.SHORT;
				if (FULL_FORMAT.equals(sDateFormat.toString())){
					iDateFormat = DateFormat.FULL;
				}else if (LONG_FORMAT.equals(sDateFormat.toString())){
					iDateFormat = DateFormat.LONG;
				}else if (MEDIUM_FORMAT.equals(sDateFormat.toString())){
					iDateFormat = DateFormat.MEDIUM;
				}
				sCustomDateFormat = ((SimpleDateFormat)DateFormat.getDateInstance(iDateFormat)).toPattern();
			}
				
			if (sCustomDateFormat == null && p_oConfiguration.getEntityFieldConfiguration()!= null ) {
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
	 * {@inheritDoc}
	 *
	 * Permet de récupérer le nom de la propriété du format de date lié au type complexe.
	 */
	@Override
	public String getDateFormatPropertyName() {
		return "DEFAULT_DATE_FORMAT";
	}
	/**
	 * {@inheritDoc}
	 *
	 * Permet de récupérer le format de date defini par défaut
	 */
	@Override
	public String getDefaultDateFormat() {
		return DEFAULT_DATE_FORMAT;
	}
	/** {@inheritDoc} */
	@Override
	public void addParametersToConfiguration(Map<ConfigurableVisualComponent.Attribute,String>  p_oAtt, BasicComponentConfiguration p_oConfiguration) {
		super.addParametersToConfiguration(p_oAtt,p_oConfiguration);
		if (p_oAtt != null && p_oConfiguration!=null){
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE.getName(),
				p_oAtt.get(ConfigurableVisualComponent.Attribute.FORMAT_ATTRIBUTE));
		}
	}
}
