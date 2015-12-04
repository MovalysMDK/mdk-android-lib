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
package com.adeuza.movalysfwk.mobile.mf4android.configuration.loader;

import android.content.res.XmlResourceParser;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;

public class MultiSectionFragmentConfigurationLoader extends
		AbstractConfigurationLoader implements ConfigurationLoader {

	private static final String CONF_SEPARATOR = "#";
	
	private static final String CONF_SECTION_ATTRIBUTE = "section";

	private static final String CONF_SECTION_SIZE_ATTRIBUTE = "sectionsize";

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.configuration.loader.ConfigurationLoader#load(android.content.res.XmlResourceParser, java.lang.String)
	 */
	@Override
	public void load(XmlResourceParser p_oParser, String p_sConfigurationKey) {
		final ConfigurationsHandler oConfHandler = ConfigurationsHandler
				.getInstance();

		ManagementConfiguration oDefConfiguration = oConfHandler
				.getManagementConfiguration(p_sConfigurationKey);
		if (oDefConfiguration == null) {
			oDefConfiguration = new ManagementConfiguration(p_sConfigurationKey);
			oConfHandler.addConfiguration(oDefConfiguration);

			// configuration du "detail"
			int iSectionCount = Integer.valueOf(this
					.getStringParameter(CONF_SECTION_SIZE_ATTRIBUTE, p_oParser));
			int i = 1;
			String[] t_sSectionDefinition = null;
			String sGetter = null;
			String sFragment = null;
			String sContainerTarget = null;
			while (i <= iSectionCount) {
				t_sSectionDefinition = this.getStringParameter(
						CONF_SECTION_ATTRIBUTE + String.valueOf(i), p_oParser)
						.split(CONF_SEPARATOR);
				sGetter = t_sSectionDefinition[1];
				sFragment = t_sSectionDefinition[2];
				if ( t_sSectionDefinition.length == 4 ) {
					sContainerTarget = t_sSectionDefinition[3];
				}
				else {
					sContainerTarget = null ;
				}
				oDefConfiguration.addZone("c1", sGetter, sFragment, sContainerTarget);
				i++;
			}

			// Pour toujours avoir à disposition la configuration initiale
			// on la clone
			ManagementConfiguration oClone = oDefConfiguration.clone();
			oClone.setName(oDefConfiguration.getName().concat(".bak"));
			oConfHandler.addConfiguration(oClone);
		}
	}
}
