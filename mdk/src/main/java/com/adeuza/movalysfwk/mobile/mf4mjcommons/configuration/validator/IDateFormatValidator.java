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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;

/**
 * Interface des validateur de champs de type date
 */
public interface IDateFormatValidator extends IFormFieldValidator {
	/** the short format*/
	public static final String SHORT_FORMAT="short";
	/** the medium format*/
	public static final String MEDIUM_FORMAT="medium";
	/** the long format*/
	public static final String LONG_FORMAT="long";
	/** the full format*/
	public static final String FULL_FORMAT="full";
	/**
	 * Définit le format de date à utiliser
	 *
	 * @param p_oConfiguration configuration du composant
	 * @return un format de date à vérifier dans le composant ou à utiliser dans le setValue pour la valeur par défaut
	 */
	public SimpleDateFormat getDateFormat(BasicComponentConfiguration p_oConfiguration);
}
