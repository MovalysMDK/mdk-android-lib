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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;

/**
 * Validateur pour les champs de type "url".
 */
public class UrlFormFieldValidatorImpl extends AbstractRegexFormFieldValidator implements UrlFormFieldValidator {
	/**
	 * Regex to verify
	 * @see http://mathiasbynens.be/demo/url-regex
	 */
	//public static final String URL_REGEX = "^(ht|f)tp(s?)\\:\\/\\/([\\w]+:\\w+@)?([a-zA-Z]{1}([\\w\\-]+\\.)+([\\w]{2,5}))(:[\\d]{1,5})?((/?\\w+/)+|/?)(\\w+\\.[\\w]{3,4})?((\\?\\w+=\\w+)?(&\\w+=\\w+)*)?";
	public static final String URL_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"; 
	/** {@inheritDoc} */
	@Override
	public String getStringRegexPropertyName() {
		return getPropertyName();
	}
	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String getDefaultRegex() {
		return getDefaultFormat();
	}
	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String getDefaultFormat() {
		return URL_REGEX;
	}
	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String getPropertyName() {
		return "DEFAULT_URL_REGEX";
	}

	/** {@inheritDoc} */
	@Override
	public String getHumanReadableRegexp() {
		return Application.getInstance().getStringResource(DefaultApplicationR.component_url__bad_entry);
	}
	
	/**
	 * Surcharge de la méthode par défaut pour ne pas afficher la regex.
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.AbstractFormFieldValidator#addErrorMessage(java.lang.StringBuilder, com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR, java.lang.String)
	 */
	@Override
	public void addErrorMessage(StringBuilder p_oErrorBuilder, ApplicationR p_IdMessage, String p_sValueToVerify) {
		if (p_oErrorBuilder.length() > 0) {
			p_oErrorBuilder.append("\n");
		}
		p_oErrorBuilder.append(Application.getInstance().getStringResource(p_IdMessage));
	}
}
