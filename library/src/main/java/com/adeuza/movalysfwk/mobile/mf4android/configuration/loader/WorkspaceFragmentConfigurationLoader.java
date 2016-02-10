package com.adeuza.movalysfwk.mobile.mf4android.configuration.loader;

import android.content.res.XmlResourceParser;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;

/**
 * <p>TODO Décrire la classe WorkspaceConfigurationLoader</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class WorkspaceFragmentConfigurationLoader extends AbstractConfigurationLoader implements ConfigurationLoader {

	/**
	 * le séparateur entre les éléments dans la conf xml du workspace
	 */
	private static final String CONF_SEPARATOR = "#";

	/**
	 * la base du nom de l'attribut pour récupérer les écrans de listes
	 */
	private static final String CONF_MAIN_ATTRIBUTE = "workspacemain";

	/**
	 * la base du nom de l'attribut pour récupérer les onglets
	 */
	private static final String CONF_TAB_ATTRIBUTE = "workspacetab";

	/**
	 * la base du nom de l'attribut pour récupérer le nombre d'onglets
	 */
	private static final String CONF_TAB_SIZE_ATTRIBUTE = "workspacetabsize";

	/**
	 * le base du nom de l'attribut pour récupérer les differents écrans details
	 */
	private static final String CONF_DETAIL_ATTRIBUTE = "workspacedetail";

	/**
	 * le nom de l'attribut pour connaitre le nombre de colonnes détail
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
			String sMainAttributeValue = this.getStringParameter(CONF_MAIN_ATTRIBUTE, p_oParser);
			if ( sMainAttributeValue != null ) {
				
				final String[] sMainWorkspaceDefinition = sMainAttributeValue.split(CONF_SEPARATOR);
				
				final ManagementZone oMainZone = oDefConfiguration.addZone(
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

			// configuration du "detail"
			int iWorkspaceDetailPanelCounts = Integer.valueOf(this.getStringParameter(CONF_DETAIL_SIZE_ATTRIBUTE, p_oParser));
			int i = 1;
			String[] t_sDetailWorkspaceDefinition = null;
			String sGridPosition = null;
			String sGroupId = null;
			while( i <= iWorkspaceDetailPanelCounts ) {
				t_sDetailWorkspaceDefinition = this.getStringParameter(CONF_DETAIL_ATTRIBUTE + String.valueOf(i), p_oParser).split(CONF_SEPARATOR);
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
}
