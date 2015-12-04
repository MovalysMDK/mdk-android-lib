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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;

/**
 * Validateur pour les champs de type "email".
 */
public interface EmailFormFieldValidator extends IFormFieldValidator  {
	
	/**
	 * Return the default format to verify the email
	 *
	 * @return an string containing a regex
	 */
	public String getDefaultFormat();
	
	/**
	 * return the name of the global property toretrieve the format regex
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPropertyName() ;

	/**
	 *
	 * Validation a data of a component
	 * @param p_sValue 
	 * @param p_oConfiguration 
	 * @param p_oErrorBuilder 
	 * @return true if valide value, false otherwise
	 */
	public boolean validate( String p_sValue , BasicComponentConfiguration p_oConfiguration,StringBuilder p_oErrorBuilder);
	
	/**
	 *
	 * Verify the value length in character is lower thant the parameters
	 * @param p_sValue 
	 * @param p_oConfiguration 
	 * @param p_oErrorBuilder 
	 * @return true if < max size, false otherwise
	 */
	public boolean validateMaxLength(String p_sValue, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder);
}
