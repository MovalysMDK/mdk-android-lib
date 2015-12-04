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

import java.util.Calendar;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;

/**
 * Validator for any field which type is date.
 */
public interface DateTimeFormFieldValidator extends IDateFormatValidator {
	
	/**
	 * <p>Permet de récupérer le nom de la propriété de l'expression régulière lié au type complexe..</p>
	 *
	 * @return le nom de la propriété de l'expression régulière lié au type complexe.
	 */
	public String getDefaultDateFormat();
	
	/**
	 * Permet de récupérer le nom de la propriété de l'expression régulière lié au type complexe.
	 *
	 * @return le nom de la propriété de l'expression régulière lié au type complexe.
	 */
	public String getDateFormatPropertyName();
	
	/**
	 * Verify the time  value is bigger than the parameters
	 *
	 * @param p_oDate a {@link java.util.Calendar} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return true if valid, false if invalid data
	 */
	public boolean validateMinTimeValue(Calendar p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder);
	
	/**
	 * Verify the time value is lower than the parameters
	 *
	 * @param p_oDate a {@link java.util.Calendar} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return true if valid, false if invalid data	 
	 */
	public boolean validateMaxTimeValue(Calendar p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder);
	
}
