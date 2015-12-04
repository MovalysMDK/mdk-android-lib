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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;

/**
 * Email type field validator
 */
public class EmailFormFieldValidatorImpl extends AbstractRegexFormFieldValidator implements EmailFormFieldValidator {
	
	/** Regular expression to verify */
	//Regex actuelle tr��s permissive mais qui obtient de bon r��sultats : 
	public static final String EMAIL_REGEX = "[a-z0-9!#$%&\\'*+/=?^_\\`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
			"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPropertyName() {
		return "DEFAULT_EMAIL_REGEX";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultFormat() {
		return EMAIL_REGEX;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStringRegexPropertyName() {
		return getPropertyName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultRegex() {
		return getDefaultFormat();
	}
	
	/** {@inheritDoc} */
	@Override
	public String getHumanReadableRegexp() {
		return Application.getInstance().getStringResource(DefaultApplicationR.component_email__bad_entry);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		if (p_oComponent.getComponentDelegate().configurationGetValue() instanceof EMailSVMImpl) {
			String sValidateValue = ((EMailSVMImpl)p_oComponent.getComponentDelegate().configurationGetValue()).to;
			return super.validate(sValidateValue, p_oConfiguration, p_oErrorBuilder);
		}
		return super.validate(p_oComponent, p_oConfiguration, p_oErrorBuilder);
	}
}
