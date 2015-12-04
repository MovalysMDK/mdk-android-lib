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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Interface des validateur de champs
 */
public interface IFormFieldValidator {
	/**
	 * Validation a data of a component
	 *
	 * @param p_oComponent the component containing the data
	 * @param p_oConfiguration configuration of the component
	 * @param p_oErrorBuilder errors
	 * @return a boolean.
	 */
	public boolean validate( ConfigurableVisualComponent p_oComponent , BasicComponentConfiguration p_oConfiguration,StringBuilder p_oErrorBuilder) ;
	/**
	 * Verify the value length in character is lower thant the parameters
	 *
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return true if valid, false if invalid data	 
	 */
	public boolean validateMaxLength(ConfigurableVisualComponent p_oComponent, BasicComponentConfiguration p_oConfiguration,
			StringBuilder p_oErrorBuilder);

	/**
	 * Retourne la longueur maximale d'un champ en caractères
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getDefaultMaxLength() ;
	/**
	 * Ajoute les paramètres issues de la configuration XML du composant dans la basique conf
	 *
	 * @param p_oAtt source XML
	 * @param p_oConfiguration configuration modifiée
	 */
	public void addParametersToConfiguration(Map<ConfigurableVisualComponent.Attribute,String> p_oAtt , BasicComponentConfiguration p_oConfiguration ) ;

}
