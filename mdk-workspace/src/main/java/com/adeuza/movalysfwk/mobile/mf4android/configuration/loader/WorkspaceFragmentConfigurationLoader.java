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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;

/**
 * Configuration loader for a workspace fragment
 */
public class WorkspaceFragmentConfigurationLoader extends AbstractConfigurationLoader implements ConfigurationLoader {

	/**
	 * separator between the xml configuration elements of the workspace
	 */
	private static final String CONF_SEPARATOR = "#";

	/**
	 * xml attribute base name to get screens lists
	 */
	private static final String CONF_MAIN_ATTRIBUTE = "workspacemain";

	/**
	 * xml attribute base name to get tabs
	 */
	private static final String CONF_TAB_ATTRIBUTE = "workspacetab";

	/**
	 * xml attribute base name to get tabs number
	 */
	private static final String CONF_TAB_SIZE_ATTRIBUTE = "workspacetabsize";

	/**
	 * xml attribute base name to get detail screens
	 */
	private static final String CONF_DETAIL_ATTRIBUTE = "workspacedetail";

	/**
	 * xml attribute base name to get the number of detail columns
	 */
	private static final String CONF_DETAIL_SIZE_ATTRIBUTE = "workspacedetailsize";

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.configuration.loader.ConfigurationLoader#load(android.content.res.XmlResourceParser, java.lang.String)
	 */
	@Override
	public void load(XmlResourceParser p_oParser, String p_sConfigurationKey) {
		final ConfigurationsHandler oConfHandler = ConfigurationsHandler.getInstance();

		ManagementConfiguration oDefConfiguration = oConfHandler.getManagementConfiguration(p_sConfigurationKey);
		if (oDefConfiguration == null) {
			oDefConfiguration = new ManagementConfiguration(p_sConfigurationKey);
			oConfHandler.addConfiguration(oDefConfiguration);

			// Main
			parseMainAttribute(p_oParser, oDefConfiguration);

			// configuration du "detail"
			int iWorkspaceDetailPanelCounts = Integer.valueOf(this.getStringParameter(CONF_DETAIL_SIZE_ATTRIBUTE, p_oParser));
			int i = 1;
			String[] t_sDetailWorkspaceDefinition = null;
			String sGridPosition = null;
			String sGroupId = null;
			while( i <= iWorkspaceDetailPanelCounts ) {
				t_sDetailWorkspaceDefinition = this.getStringParameter(CONF_DETAIL_ATTRIBUTE + i, p_oParser).split(CONF_SEPARATOR);
				sGridPosition = t_sDetailWorkspaceDefinition[0] ;
				sGroupId = sGridPosition.substring(sGridPosition.indexOf('c'), sGridPosition.indexOf('s'));
				
				oDefConfiguration.addZone(sGroupId, t_sDetailWorkspaceDefinition[1], t_sDetailWorkspaceDefinition[2]);
				i++;
			}

			// Pour toujours avoir à disposition la configuration initiale
			// on la clone
			ManagementConfiguration oClone = oDefConfiguration.clone();
			oClone.setName(oDefConfiguration.getName().concat(".bak"));
			oConfHandler.addConfiguration(oClone);
			
		}
	}

	/**
	 * Parses the "workspacemain" xml attribute
	 * @param p_oParser xml parser
	 * @param p_oDefConfiguration management configuration
	 */
	private void parseMainAttribute(XmlResourceParser p_oParser, ManagementConfiguration p_oDefConfiguration) {
		String sMainAttributeValue = this.getStringParameter(CONF_MAIN_ATTRIBUTE, p_oParser);
		if ( sMainAttributeValue != null ) {
			
			final String[] sMainWorkspaceDefinition = sMainAttributeValue.split(CONF_SEPARATOR);
			
			final ManagementZone oMainZone = p_oDefConfiguration.addZone(
					sMainWorkspaceDefinition[0], sMainWorkspaceDefinition[1], sMainWorkspaceDefinition[2]);
			
			// Configuration des onglets
			int iWorkspaceTabSize = 0 ;
			String sTabSize = this.getStringParameter(CONF_TAB_SIZE_ATTRIBUTE, p_oParser);
			if ( sTabSize != null ) {
				iWorkspaceTabSize = Integer.valueOf(sTabSize);
			}

			int i = 1 ;
			String[] sTabWorkspaceDefinition = null;
			while( i <= iWorkspaceTabSize ) {
				sTabWorkspaceDefinition = this.getStringParameter(CONF_TAB_ATTRIBUTE + String.valueOf(i), p_oParser).split(CONF_SEPARATOR);
				oMainZone.addZone(sTabWorkspaceDefinition[1], sTabWorkspaceDefinition[2], sTabWorkspaceDefinition[0]);
				i++;
			}
		}
	}
}
