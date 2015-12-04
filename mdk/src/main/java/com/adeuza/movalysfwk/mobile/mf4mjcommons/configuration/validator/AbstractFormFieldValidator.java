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

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * Classe abstraite permettant de valider les éventuelles expressions régulières définies.
 */
public abstract class AbstractFormFieldValidator {
	/**
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return a boolean.
	 */
	public boolean validateMaxLength(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {

		String sValue = null;
		
		if (p_oComponent.getComponentDelegate().configurationGetValue() != null) {
			sValue = p_oComponent.getComponentDelegate().configurationGetValue().toString();
		}
		
		return this.validateMaxLength(sValue, p_oConfiguration, p_oErrorBuilder);
	}

	
	/**
	 * @param p_sValue a {@link java.lang.String} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return a boolean.
	 */
	public boolean validateMaxLength(String p_sValue, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		Object oMaxLength = null;
		if (p_oConfiguration != null) {
			oMaxLength = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.MAX_LENGTH_ATTRIBUTE.getName());
		}
		if (oMaxLength == null && p_oConfiguration.getEntityFieldConfiguration() != null
				&& p_oConfiguration.getEntityFieldConfiguration().getMaxLength() > 0 ) {
			oMaxLength = p_oConfiguration.getEntityFieldConfiguration().getMaxLength() ;
		}
		if (oMaxLength == null) {
			oMaxLength = this.getDefaultMaxLength();
		}
		if (oMaxLength != null && p_sValue != null && p_sValue.length() > Integer.parseInt(oMaxLength.toString())) {
			this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data_format_max_length, oMaxLength.toString());
			return false;
		}
		return true;
	}

	/**
	 * <p>getDefaultMaxLength.</p>
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getDefaultMaxLength() {
		return 255;
	}
	/**
	 * <p>addErrorMessage.</p>
	 *
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @param p_IdMessage a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 * @param p_sValueToVerify a {@link java.lang.String} object.
	 */
	public void addErrorMessage(StringBuilder p_oErrorBuilder, ApplicationR p_IdMessage, String p_sValueToVerify) {
		if (p_oErrorBuilder.length() > 0) {
			p_oErrorBuilder.append("\n");
		}
		p_oErrorBuilder.append(Application.getInstance().getStringResource(p_IdMessage));
		p_oErrorBuilder.append(" (").append(p_sValueToVerify).append(')');
	}
	/**
	 * <p>addParametersToConfiguration.</p>
	 *
	 * @param p_oAtt a {@link java.util.Map} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 */
	public void addParametersToConfiguration(Map<ConfigurableVisualComponent.Attribute,String>  p_oAtt, BasicComponentConfiguration p_oConfiguration) {
		if (p_oAtt != null && p_oConfiguration!=null) {
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.MAX_LENGTH_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.MAX_LENGTH_ATTRIBUTE));
		}
	}
}
