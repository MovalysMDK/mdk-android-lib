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
package com.adeuza.movalysfwk.mf4jcommons.model.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.TEntityConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.list.TListDescriptor;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.property.TProperty;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.visual.TGraphConfiguration;
import com.google.gson.annotations.SerializedName;

/**
 * <p>Java representation of the mobile configuration.</p>
 *
 *
 */
public class AppConfiguration {
	/**
	 * Properties.
	 */
	@SerializedName("prop")
	private Collection<TProperty> properties;

	/**
	 * Configuration of entities
	 */
	private Map<String, TEntityConfiguration> entitiesByName;

	/**
	 * Configuration of UI Components
	 */
	private Map<String, TGraphConfiguration> graphComponentsByName;

	/**
	 * Lists.
	 */
	private Map<String, TListDescriptor> listsByName;

	/**
	 * Default constructor. Creates empty lists for all attributes.
	 */
	public AppConfiguration() {
		this.entitiesByName			= new HashMap<>();
		this.graphComponentsByName	= new HashMap<String, TGraphConfiguration>();
		this.properties				= new ArrayList<>();
		this.listsByName			= new HashMap<String, TListDescriptor>();
	}

	/**
	 * Returns the configuration of entities.
	 *
	 * @return a {@link java.util.Collection} object : The configuration of Entities.
	 */
	public final Collection<TEntityConfiguration> getEntities() {
		return this.entitiesByName.values();
	}

	/**
	 * Defines the configuration of entities associated to this configuration
	 *
	 * @param p_oEntities
	 * 		The configuration of entities.
	 */
	public final void setEntities(Collection<TEntityConfiguration> p_oEntities) {
		this.entitiesByName.clear();
		this.addEntities(p_oEntities);
	}

	/**
	 * Adds the configuration of an entity to this <em>AppConfiguration</em>.
	 * If this configuration already exists, this methods merges the two versions
	 * of the configuration.
	 *
	 * @param p_oEntityConfiguration
	 * 		The entity configuration to add. Possibly null.
	 */
	public void addEntity(final TEntityConfiguration p_oEntityConfiguration) {
		if (p_oEntityConfiguration != null) {
			final String sEntityName = p_oEntityConfiguration.getName();

			TEntityConfiguration oExistingConfiguration = this.entitiesByName.get(sEntityName);
			if (oExistingConfiguration == null) {
				this.entitiesByName.put(sEntityName, p_oEntityConfiguration);
			}
			else {
				oExistingConfiguration.merge(p_oEntityConfiguration);
			}
		}
	}

	/**
	 * Adds configuration of several entities to this global configuration.
	 *
	 * @param p_oEntityConfigurations
	 * 		The entities configurations to add.
	 */
	public void addEntities(final Collection<TEntityConfiguration> p_oEntityConfigurations) {
		if (p_oEntityConfigurations != null) {
			for (TEntityConfiguration oConfiguration : p_oEntityConfigurations) {
				this.addEntity(oConfiguration);
			}
		}
	}

	/**
	 * Returns the configuration of screens
	 *
	 * @return a {@link java.util.Collection} object : the configuration of screens
	 */
	public final Collection<TGraphConfiguration> getGraphComponents() {
		return this.graphComponentsByName.values();
	}

	/**
	 * Defines the configuration of visual components associated with this configuration.
	 *
	 * @param p_oConfigurations
	 * 		The configuration of visual components associated with this configuration.
	 */
	public final void setGraphComponents(Collection<TGraphConfiguration> p_oConfigurations) {
		this.graphComponentsByName.clear();
		this.addGraphComponents(p_oConfigurations);
	}

	/**
	 * Adds a graphical configurations to this global configuration.
	 *
	 * @param p_oGraphConfiguration
	 * 		The graphical configuration to add.
	 * 		If <code>null</code> no add has been performed.
	 */
	public void addGraph(final TGraphConfiguration p_oGraphConfiguration) {
		if (p_oGraphConfiguration != null) {
			final String sGraphName = p_oGraphConfiguration.getName();

			TGraphConfiguration oExistingConfiguration = this.graphComponentsByName.get(sGraphName);
			if (oExistingConfiguration == null) {
				this.graphComponentsByName.put(sGraphName, p_oGraphConfiguration);
			}
			else {
				oExistingConfiguration.merge(p_oGraphConfiguration);
			}
		}
	}

	/**
	 * Adds several graphical configurations to this glocal configuration.
	 *
	 * @param p_oConfigurations
	 * 		The graphical configurations to add.
	 */
	public void addGraphComponents(final Collection<? extends TGraphConfiguration> p_oConfigurations) {
		if (p_oConfigurations != null) {
			for (TGraphConfiguration oConfiguration : p_oConfigurations) {
				this.addGraph(oConfiguration);
			}
		}
	}

	/**
	 * Returns all defined properties. Never null but possibly an empty collection.
	 *
	 * @return a {@link java.util.Collection} object : All defined properties. Never null but possibly an empty collection.
	 */
	public final Collection<TProperty> getProperties() {
		return this.properties;
	}

	/**
	 * Defines the properties associated with this configuration.
	 *
	 * @param p_oProperties
	 * 		The properties associated with this configuration.
	 */
	public final void setProperties(final Collection<TProperty> p_oProperties) {
		this.properties.clear();
		this.addProperties(p_oProperties);
	}

	/**
	 * Adds a new property to this configuration.
	 *
	 * @param p_oProperty
	 * 		The property to add to this property.
	 */
	public void addProperty(final TProperty p_oProperty) {
		this.properties.add(p_oProperty);
	}

	/**
	 * Adds some properties to this configuration.
	 *
	 * @param p_oProperties
	 * 		The properties to add to this property.
	 */
	public void addProperties(final Collection<TProperty> p_oProperties) {
		if (p_oProperties != null) {
			this.properties.addAll(p_oProperties);
		}
	}

	/**
	 * <p>getLists.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public final Collection<TListDescriptor> getLists() {
		return this.listsByName.values();
	}

	/**
	 * <p>setLists.</p>
	 *
	 * @param p_oListDescriptors a {@link java.util.Collection} object.
	 */
	public final void setLists(final Collection<TListDescriptor> p_oListDescriptors) {
		this.listsByName.clear();
		if (p_oListDescriptors != null) {
			for (TListDescriptor oDescriptor : p_oListDescriptors) {
				this.addList(oDescriptor);
			}
		}
	}

	/**
	 * <p>addList.</p>
	 *
	 * @param p_oDescriptor a {@link com.adeuza.movalysfwk.mf4jcommons.model.configuration.list.TListDescriptor} object.
	 */
	public final void addList(TListDescriptor p_oDescriptor) {
		if (p_oDescriptor != null) {
			this.listsByName.put(p_oDescriptor.getName(), p_oDescriptor);
		}
	}
}
