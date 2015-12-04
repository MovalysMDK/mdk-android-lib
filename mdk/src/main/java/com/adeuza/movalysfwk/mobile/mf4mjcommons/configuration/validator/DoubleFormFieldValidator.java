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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Validator for any field which type is double.
 */
public interface DoubleFormFieldValidator extends IFormFieldValidator {
	
	/**
	 * Verify the value is lower than the parameters
	 *
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return true if valid, false if invalid data	 
	 */
	public boolean validateMaxValue(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder);
	
	/**
	 * Verify the value is bigger than the parameters
	 *
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return true if valid, false if invalid data	 
	 */
	public boolean validateMinValue(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder);
	
	/**
	 * Verify the number of digit is less than the parameter
	 *
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return true if valid, false if invalid data	 
	 */
	public boolean validateDigitNumber(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder);
	
	/**
	 * Définit le nom du paramètre contenant la valeur minimale à respecter
	 *
	 * @return une chaine non vide
	 */
	public String getMinValueParameterName() ;
	
	/**
	 * Définit le nom du paramètre contenant la valeur maximale à respecter
	 *
	 * @return une chaine non vide
	 */
	public String getMaxValueParameterName() ;
	
	/**
	 * Définit le nom du paramètre contenant la valeur maximale du nombre de décimales à respecter
	 *
	 * @return une chaine non vide
	 */
	
	public String getNbDigitParameterName() ;
	/**
	 * Retourne la valeur minimale à respecter
	 *
	 * @param p_oConfiguration configuration
	 * @return une valeur à décimale ou null si pas de valeur définie
	 */
	public Double getMinValue(BasicComponentConfiguration p_oConfiguration);
	
	/**
	 * Retourne la valeur maximale à respecter
	 *
	 * @param p_oConfiguration configuration
	 * @return une valeur à décimale ou null si pas de valeur définie
	 */
	public Double getMaxValue(BasicComponentConfiguration p_oConfiguration);
}
