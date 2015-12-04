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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * Classe abstraite permettant de valider les éventuelles expressions régulières définies.
 */
public abstract class AbstractRegexFormFieldValidator extends AbstractFormFieldValidator {


	private Pattern patternMatcher = null;
	private String errorMessage = null;
	
	/**
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return a boolean.
	 */
	public boolean validate(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		String sValue = "";
		if (p_oComponent.getComponentDelegate().configurationGetValue() != null) {
			sValue = p_oComponent.getComponentDelegate().configurationGetValue().toString();
		}
		return this.validate(sValue, p_oConfiguration, p_oErrorBuilder);
	}

	/**
	 * @param p_sValue a {@link java.lang.String} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return a boolean.
	 */
	public boolean validate(String p_sValue, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder) {
		boolean r_bIsValid = false;
		if (this.validateMaxLength(p_sValue, p_oConfiguration, p_oErrorBuilder)) {
			if ( p_sValue!= null && p_sValue.length() > 0 && !"null".equals(p_sValue)) {
				String sRegex = this.getRegex(p_oConfiguration);
				String sRegexError = this.getRegexErrorMessage(p_oConfiguration);
				r_bIsValid = this.performParameterRegexValidation(p_sValue, sRegex, sRegexError, p_oErrorBuilder);
			} else {
				r_bIsValid = true ;
			}
		}
		return r_bIsValid;
	}


	/**
	 * Valide la valeur du champ avec l'expression régulière définie dans le paramètrage du champs.
	 * @param p_sRegex The regex to match
	 * @param p_sValue The field value to validate.
	 * @return boolean true if the value is correct
	 */
	private boolean performParameterRegexValidation(String p_sValue, String p_sRegex, String p_sRegexError, StringBuilder p_oErrorBuilder) {
		if(patternMatcher == null && p_sRegex != null) {
			patternMatcher = Pattern.compile(p_sRegex);
		}

		boolean r_bIsCorrect = true;

		if(patternMatcher != null) {
			Matcher matcher = patternMatcher.matcher(p_sValue);

			long beforeMatch = System.currentTimeMillis();
			boolean match = matcher.find();
			long afterMatch = System.currentTimeMillis();

			if(afterMatch-beforeMatch > 500) {
				System.out.println("WARNING : The time to match \""+ p_sRegex +"\" seems to be long. You should not try" +
						" a longer sentence");
			}

			if (p_sRegex != null && p_sRegex.length() > 0 && p_sValue != null && p_sValue.length() > 0 && !match) {
				r_bIsCorrect = false;
				String sErrorMessage = p_sRegexError;
				if (sErrorMessage == null)
					sErrorMessage = this.getHumanReadableRegexp();
				this.addErrorMessage(p_oErrorBuilder, DefaultApplicationR.error_invalid_data, sErrorMessage);
			}
		}
		return r_bIsCorrect; 
	}
	/**
	 * Search a regex : - 1 in entity configuration - 2 in property defined in the global configuration - 3 get the default regex in
	 * validator
	 *
	 * @param p_oConfiguration configuration of the component
	 * @return a string
	 */
	public String getRegex(BasicComponentConfiguration p_oConfiguration) {
		if (p_oConfiguration != null) {
			Object sRegex = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.REGEX_ATTRIBUTE.getName());
			if (sRegex != null) {
				return sRegex.toString();
			}

			if (p_oConfiguration.getEntityFieldConfiguration() != null) {
				Object sRegex2 = p_oConfiguration.getEntityFieldConfiguration().getParameter(
						ConfigurableVisualComponent.Attribute.REGEX_ATTRIBUTE.getName());
				if (sRegex2 != null) {
					return sRegex2.toString();
				}
			}
		}
		if (this.getStringRegexPropertyName() != null) {
			Property oRegexProp = ConfigurationsHandler.getInstance().getProperty(getStringRegexPropertyName());
			if (oRegexProp != null) {
				return oRegexProp.getValue();
			}
		}
		return this.getDefaultRegex();
	}
	
	/**
	 * Revoie le message d'erreur définit pour la validation de la regex
	 *
	 * @param p_oConfiguration configuration of the component
	 * @return le message d'erreur
	 */
	public String getRegexErrorMessage(BasicComponentConfiguration p_oConfiguration) {
		if (p_oConfiguration != null) {
			Object sRegexError = p_oConfiguration.getParameter(ConfigurableVisualComponent.Attribute.REGEX_ERROR_ATTRIBUTE.getName());
			if (sRegexError != null) {
				return sRegexError.toString();
			}

			if (p_oConfiguration.getEntityFieldConfiguration() != null) {
				Object sRegexError2 = p_oConfiguration.getEntityFieldConfiguration().getParameter(
						ConfigurableVisualComponent.Attribute.REGEX_ERROR_ATTRIBUTE.getName());
				if (sRegexError2 != null) {
					return sRegexError2.toString();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Permet de récupérer le nom de la propriété de l'expression régulière lié au type complexe.
	 *
	 * @return le nom de la propriété de l'expression régulière lié au type complexe.
	 */
	public abstract String getStringRegexPropertyName();
	
	/**
	 * Permet de récupérer l'expression régulière definie par défaut
	 *
	 * @return l'expression régulière definie par défaut si pas de conf définie dans l'entité ou la conf globale
	 */
	public abstract String getDefaultRegex();
	
	/**
	 * Permet de récupérer une chaine lisible décrivant l'expression régulière régissant le composant
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String getHumanReadableRegexp();
	
	/** {@inheritDoc} */
	@Override
	public void addParametersToConfiguration(Map<ConfigurableVisualComponent.Attribute, String> p_oAtt,
			BasicComponentConfiguration p_oConfiguration) {
		super.addParametersToConfiguration(p_oAtt, p_oConfiguration);
		if (p_oAtt != null && p_oConfiguration!=null) {
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.REGEX_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.REGEX_ATTRIBUTE));
			p_oConfiguration.addParameter(ConfigurableVisualComponent.Attribute.REGEX_ERROR_ATTRIBUTE.getName(),
					p_oAtt.get(ConfigurableVisualComponent.Attribute.REGEX_ERROR_ATTRIBUTE));
		}
	}
}
