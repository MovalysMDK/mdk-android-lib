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
 * Validator for any field which type is date.
 */
public interface DurationFormFieldValidator extends IDateFormatValidator {
	/**
	 * Format défini par défaut par l'application
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDefaultFormat();
	/**
	 * Permet de récupérer le nom de la propriété générale du format de la durée
	 *
	 * @return le nom de la propriété générale du format de la durée.
	 */
	public String getFormatPropertyName();
	/**
	 * Permet de récupérer le nom de la propriété générale indiquant si on autorise de saisir que des minutes
	 *
	 * @return le nom de la propriété générale indiquant si on autorise de saisir que des minutes
	 */
	public String getOnlyMinutesPropertyName() ;
	/**
	 * Verify the hour value is bigger than the parameters
	 *
	 * @return true if valid, false if invalid data
	 * @param p_oDate a {@link java.lang.String} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 */
	public boolean validateMinValue(String p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder);
	/**
	 * Verify the hour value is lower than the parameters
	 *
	 * @return true if valid, false if invalid data
	 * @param p_oDate a {@link java.lang.String} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 */
	public boolean validateMaxValue(String p_oDate, BasicComponentConfiguration p_oConfiguration, StringBuilder p_oErrorBuilder);
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
	 * Retourne la valeur minimale à respecter
	 *
	 * @param p_oConfiguration configuration
	 * @return une valeur à décimale ou null si pas de valeur définie
	 */
	public String getMinValue(BasicComponentConfiguration p_oConfiguration);
	/**
	 * Retourne la valeur maximale à respecter
	 *
	 * @param p_oConfiguration configuration
	 * @return une valeur à décimale ou null si pas de valeur définie
	 */
	public String getMaxValue(BasicComponentConfiguration p_oConfiguration);
}
