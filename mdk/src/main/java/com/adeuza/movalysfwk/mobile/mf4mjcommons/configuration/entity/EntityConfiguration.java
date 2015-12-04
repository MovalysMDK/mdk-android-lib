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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.AbstractConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;

/**
 * <p>Entity configuration. Defines customisable fields and basic fields for an entity.</p>
 *
 *
 */
public class EntityConfiguration extends AbstractConfiguration {

	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;

	/** list of customisable fields */
	private Map<String, AbstractEntityFieldConfiguration> fields = null;

	/**
	 * Constructs a new entity configuration.
	 *
	 * @param p_sName the entity's name
	 */
	public EntityConfiguration(String p_sName) {
		super(p_sName, ConfigurationsHandler.TYPE_ENTITY);
		this.fields = new HashMap<String, AbstractEntityFieldConfiguration>(128);
	}

	/**
	 * Add a new field configuration
	 *
	 * @param p_oConfiguration the field configuration to add
	 */
	public void addFieldConfiguration(AbstractEntityFieldConfiguration p_oConfiguration) {
		this.fields.put(p_oConfiguration.getName(), p_oConfiguration);
	}
	
	/**
	 * Add a configuration for a basic field
	 *
	 * @param p_sName name of basic field
	 * @param p_iType type of basic field
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.BasicEntityFieldConfiguration} object.
	 */
	public BasicEntityFieldConfiguration addBasicFieldConfiguration(String p_sName, int p_iType) {
		BasicEntityFieldConfiguration oConf = new BasicEntityFieldConfiguration(p_sName, p_iType);
		this.fields.put(p_sName, oConf);
		return oConf;
	}
	
	/**
	 * Returns all field configurations
	 *
	 * @return all field configurations
	 */
	public Collection<AbstractEntityFieldConfiguration> getEntityFieldConfigurations() {
		return this.fields.values();
	}

	/**
	 * Retreives a field using its name.
	 *
	 * @param p_sName The name of the searched field.
	 * @return The searched field or null if no field has the given name.
	 */
	public AbstractEntityFieldConfiguration getEntityFieldConfiguration(final String p_sName) {
		return this.fields.get(p_sName);
	}
}
