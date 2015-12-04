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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.EntityConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.list.CustomListConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.serialization.SerializationDao;

/**
 * <p>Handler for configuration</p>
 *
 *
 */
public final class ConfigurationsHandler implements Serializable {

	/** Version number of this class implementation. */
	private static final long serialVersionUID = 1L;
	/** instance of handler */
	private static ConfigurationsHandler instance = null;
	
	/** Key used by serialization. */
	public static final String SERIALIZATION_KEY = "configurations-handler";
	/** type to use for visual configuration */
	public static final String TYPE_VISUAL = "VISUAL";
	/** type to use for entity configuration */
	public static final String TYPE_ENTITY = "ENTITY";
	/** type to use for management configuration */
	public static final String TYPE_MANAGEMENT = "MANAGEMENT";
	/** type to used for property. */
	public static final String TYPE_PROPERTY = "PROPERTY";
	/** type to used for list. */
	public static final String TYPE_LIST = "LIST";
	
	
	
	/**
	 * Creates the single instance of this class by deserialization.
	 *
	 * @param p_oContext
	 * 		The current context
	 */
	protected static void createInstance(MContext p_oContext) {

		// Dans le cas du lancement du nouvelle version de l'application,
		// L'ensemble de la configuration est remise à zéro et tout élément
		// précédemment sauvegardé est supprimé.
		SerializationDao oDao = BeanLoader.getInstance().getBean(SerializationDao.class);
		
		if (Application.getInstance().isNewVersion()) {
			oDao.delete(p_oContext, SERIALIZATION_KEY);
		}
		else {
			try {
				ConfigurationsHandler.instance = (ConfigurationsHandler) oDao.loadObject(p_oContext, SERIALIZATION_KEY);
			}
			catch(Exception e) {
				// si le cache n'est pas lisible
				oDao.delete(p_oContext, SERIALIZATION_KEY);
			}
		}

		if (ConfigurationsHandler.instance == null) {
			ConfigurationsHandler.instance = new ConfigurationsHandler();
		}
	}
	
	/**
	 * <p>isLoaded.</p>
	 *
	 * @return a boolean.
	 */
	public static boolean isLoaded() {
		return ConfigurationsHandler.instance!=null;
	}

	/**
	 * Returns unique instance for handler
	 *
	 * @return unique instance for handler
	 */
	public static ConfigurationsHandler getInstance() {
		if (ConfigurationsHandler.instance == null) {
			throw new MobileFwkException(ErrorDefinition.FWK_MOBILE_E_0003, ErrorDefinition.FWK_MOBILE_E_0003_LABEL);
		}
		return ConfigurationsHandler.instance;
	}
	
	/** map of configurations */
	private Map<String, AbstractConfiguration> configurations = null;
	
	/**
	 * Constructs a new handler
	 */
	private ConfigurationsHandler() {
		this.configurations = new HashMap<String, AbstractConfiguration>();
	}
	
	/**
	 * Initialize the handler
	 */
	public void init() {
		
		// Ajout de la configuration des menus de base
		this.addConfiguration(new Property(FwkPropertyName.menu_base_doStopApplicationStartup$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_doRestartApplicationStartup$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_configuration_during_application_stop$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_configuration_during_application_stop_doDeleteSerializedConfiguration$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_configuration_during_application_stop_doResetSetting$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_configuration_during_application_stop_doResetDataBase$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_configuration$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_configuration_doDisplaySetting$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_configuration_doResetSetting$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_configuration_doResetDataBase$visible));
		this.addConfiguration(new Property(FwkPropertyName.menu_base_doDisplayExitApplicationDialog$visible));
		// Mode debug
		this.addConfiguration(new Property(FwkPropertyName.debug_mode));
		// Mode développement
		this.addConfiguration(new Property(FwkPropertyName.dev_mode));
		this.addConfiguration(new Property(FwkPropertyName.synchronization_exitAppOnFirstSynchroFailure));
		this.addConfiguration(new Property(FwkPropertyName.sync_mock_mode));
		this.addConfiguration(new Property(FwkPropertyName.sync_mock_testid));
		this.addConfiguration(new Property(FwkPropertyName.desactivate_auth));
		// Init of application
		this.addConfiguration(new Property(FwkPropertyName.logger_name));
		this.addConfiguration(new Property(FwkPropertyName.database));
		this.addConfiguration(new Property(FwkPropertyName.database_name));
		this.addConfiguration(new Property(FwkPropertyName.database_version));
		// Synchronization
		this.addConfiguration(new Property(FwkPropertyName.sync_transparent_mode));
		this.addConfiguration(new Property(FwkPropertyName.sync_debug_mode));
		this.addConfiguration(new Property(FwkPropertyName.sync_data_stream_from_file_mode));
		this.addConfiguration(new Property(FwkPropertyName.synchronization_emptyDBSync_3GGPRS));
		this.addConfiguration(new Property(FwkPropertyName.synchronization_emptyDBSync_wifi));
		this.addConfiguration(new Property(FwkPropertyName.synchronization_firstSync_3GGPRS));
		this.addConfiguration(new Property(FwkPropertyName.synchronization_firstSync_wifi));
		this.addConfiguration(new Property(FwkPropertyName.synchronization_otherSync_3GGPRS));
		this.addConfiguration(new Property(FwkPropertyName.synchronization_otherSync_wifi));
		
		this.addConfiguration(new Property(FwkPropertyName.synchronization_emptyDB_failure_popup));
		this.addConfiguration(new Property(FwkPropertyName.synchronization_max_time_without_sync));
		
		this.addConfiguration(new Property(FwkPropertyName.dbcrypt_keystorepass));
		this.addConfiguration(new Property(FwkPropertyName.default_maxNumberSelectableItem));
		
		this.addConfiguration(new Property(FwkPropertyName.case_sensitive_login));
	}
	
	/**
	 * Returns all configuration managed by this handler.
	 *
	 * @return All configuration managed by this handler. Never null but possibly an empty collection.
	 */
	public Collection<AbstractConfiguration> getConfigurations() {
		return this.configurations.values();
	}

	/**
	 * Returns the visual configuration for the key p_sName
	 *
	 * @param p_sName the configuration's name
	 * @return the configuration
	 */
	public VisualComponentConfiguration getVisualConfiguration(String p_sName) {
		return (VisualComponentConfiguration) this.configurations.get(StringUtils.concat(TYPE_VISUAL, ".", p_sName));
	}
	
	/**
	 * Returns the management configuration for the key p_sName
	 *
	 * @param p_sName the configuration's name
	 * @return the configuration
	 */
	public ManagementConfiguration getManagementConfiguration(String p_sName) {
		return (ManagementConfiguration) this.configurations.get(StringUtils.concat(TYPE_MANAGEMENT, ".", p_sName));
	}

	/**
	 * Returns the management group for the keys p_sConfigName and p_sGroupName
	 *
	 * @param p_sConfigName the configuration's name
	 * @param p_sGroupName the group's name
	 * @return the configuration
	 */
	public ManagementGroup getManagementGroup(String p_sConfigName, String p_sGroupName) {
		ManagementGroup r_oGroup = null;

		ManagementConfiguration oConfiguration = (ManagementConfiguration) this.configurations
				.get(StringUtils.concat(TYPE_MANAGEMENT, ".", p_sConfigName));

		if (oConfiguration != null) {
			for (ManagementGroup oGroup : oConfiguration.getVisibleGroups()) {
				if (oGroup.getName().equals(p_sGroupName)) {
					r_oGroup = oGroup;
					break;
				}
			}
		}
		return r_oGroup;
	}

	/**
	 * <p>getManagementZone.</p>
	 *
	 * @param p_sConfigName a {@link java.lang.String} object.
	 * @param p_sGroupName a {@link java.lang.String} object.
	 * @param p_sZoneName a {@link java.lang.String} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone} object.
	 */
	public ManagementZone getManagementZone(String p_sConfigName, String p_sGroupName, String p_sZoneName) {
		ManagementZone r_oZone = null;

		ManagementGroup oGroup = this.getManagementGroup(p_sConfigName, p_sGroupName);
		if (oGroup != null) {
			for (ManagementZone oZone : oGroup.getVisibleZones()) {
				if (oZone.getName().equals(p_sZoneName)) {
					r_oZone = oZone;
				}
			}
		}
		return r_oZone;
	}
	
	/**
	 * Returns the entity configuration for the key p_sName
	 *
	 * @param p_sName the configuration's name
	 * @return the configuration
	 */
	public EntityConfiguration getEntityConfiguration(String p_sName)  {
		return (EntityConfiguration) this.configurations.get(StringUtils.concat(TYPE_ENTITY, ".", p_sName));
	}
	
	/**
	 * Returns the list, usable by custom fields, for the key <em>p_sName</em>.
	 *
	 * @param p_sName The configuration-s name. Mandatory.
	 * @return The list or null if there is not list named <em>p_sName</em>.
	 */
	public CustomListConfiguration getCustomListConfiguration(String p_sName) {
		return (CustomListConfiguration) this.configurations.get(StringUtils.concat(TYPE_LIST, ".", p_sName));
	}

	/**
	 * Returns a property using its name.
	 *
	 * @param p_sName
	 * 		The property's name
	 * @return The property named <em>p_sName</em> or null if it is not exist.
	 */
	public Property getProperty(String p_sName) {
		return (Property) this.configurations.get(StringUtils.concat(TYPE_PROPERTY, ".", p_sName));
	}

	/**
	 * Returns a property using its name.
	 *
	 * @param p_oName
	 * 		The property's name
	 * @return The property named <em>p_oName</em> or null if it is not exist.
	 */
	public Property getProperty(PropertyName p_oName) {
		return this.getProperty(p_oName.name());
	}
	
	/**
	 * Defines the configuration managed by this handler.
	 *
	 * @param p_oConfigurations
	 * 		The configuration managed by this handler.
	 */
	public void setConfigurations(Collection<AbstractConfiguration> p_oConfigurations) {
		if (p_oConfigurations != null) {
			for (AbstractConfiguration oConfiguration : p_oConfigurations) {
				this.addConfiguration(oConfiguration);
			}
		}
	}

	/**
	 * Adds a configuration, the name of it is used as key
	 *
	 * @param p_oConfiguration the configuration to add
	 */
	public void addConfiguration(AbstractConfiguration p_oConfiguration) {
		this.configurations.put(p_oConfiguration.getKey(), p_oConfiguration);
	}

	/**
	 * Removes a configuration.
	 *
	 * @param p_oConfiguration
	 * 		The configuration to remove.
	 */
	public void removeConfiguration(AbstractConfiguration p_oConfiguration) {
		this.configurations.remove(p_oConfiguration.getKey());
	}

	/**
	 * Removes a property.
	 *
	 * @param p_sPropertyName
	 * 		The property's name.
	 */
	public void removeProperty(final String p_sPropertyName) {
		this.configurations.remove(StringUtils.concat(TYPE_PROPERTY, ".", p_sPropertyName));
	}

	/**
	 * Removes a management configuration.
	 *
	 * @param p_sConfigurationName a {@link java.lang.String} object.
	 */
	public void removeManagementConfiguration(final String p_sConfigurationName) {
		this.configurations.remove(StringUtils.concat(TYPE_MANAGEMENT, ".", p_sConfigurationName));
	}
}
