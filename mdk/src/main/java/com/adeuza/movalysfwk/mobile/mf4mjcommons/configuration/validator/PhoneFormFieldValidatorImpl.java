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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;

/**
 * Validateur pour les champs de type "phone".
 *
 */
public class PhoneFormFieldValidatorImpl extends AbstractRegexFormFieldValidator implements PhoneFormFieldValidator {
	/**
	 * Regex to verify
	 */
	public static final String PHONE_REGEX = "^(\\+[-. ]?\\d{1,3}[-. ]?(\\(\\d{1}\\)[-. ]?)?|\\d{1})\\d{1}([-. ]?\\d{2}){4}$";
	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String getPropertyName() {
		return "DEFAULT_PHONE_REGEX";
	}
	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String getDefaultFormat() {
		return PHONE_REGEX;
	}
	/**
	 * {@inheritDoc}
	 *
	 * Permet de récupérer le nom de la propriété de l'expression régulière lié au type complexe.
	 */
	@Override
	public String getStringRegexPropertyName() {
		return getPropertyName();
	}
	/**
	 * {@inheritDoc}
	 *
	 * Permet de récupérer l'expression régulière definie par défaut
	 */
	@Override
	public String getDefaultRegex() {
		return getDefaultFormat();
	}
	
	/** {@inheritDoc} */
	@Override
	public String getHumanReadableRegexp() {
		return Application.getInstance().getStringResource(DefaultApplicationR.component_phone__bad_entry);
	}
}
