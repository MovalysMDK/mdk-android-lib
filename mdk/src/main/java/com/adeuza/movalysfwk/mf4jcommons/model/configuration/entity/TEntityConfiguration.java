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
package com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Entity configuration. Defines customisable fields and the other fields.</p>
 *
 *
 */
public class TEntityConfiguration {

	/** entity's name */
	private String name;

	/** list of customisable fields */
	private Map<String, AbstractTEntityFieldConfiguration> fields;

	/**
	 * Default constructor used to deserialize an EntityConfiguration from JSON.
	 */
	public TEntityConfiguration() {
		this.name = null;
		this.fields = new HashMap<String, AbstractTEntityFieldConfiguration>();
	}

	/**
	 * Constructs a new entity configuration.
	 *
	 * @param p_sName
	 * 		The entity's name
	 */
	public TEntityConfiguration(final String p_sName) {
		this();
		this.setName(p_sName);
	}

	/**
	 * Returns the name of entity or null if it is not define.
	 *
	 * @return a {@link java.lang.String} object : The name of entity or null if it is not define.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Defines the name of the entity
	 *
	 * @param p_sName Entity's name.
	 */
	public final void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Returns all field configurations
	 *
	 * @return all field configurations
	 */
	public Collection<AbstractTEntityFieldConfiguration> getFields() {
		return this.fields.values();
	}

	/**
	 * Adds all fields in a collection to this entity.
	 * This methods is used to deserialize an entity from JSON or XML
	 *
	 * @param p_oFields Fields to add. Possibly a null value.
	 */
	public void setFields(Collection<AbstractTEntityFieldConfiguration> p_oFields) {
		if (p_oFields != null) {
			for (AbstractTEntityFieldConfiguration oField : p_oFields) {
				this.addFieldConfiguration(oField);
			}
		}
	}

	/**
	 * Add a new field configuration
	 *
	 * @param p_oConfiguration the field configuration to add
	 */
	public void addFieldConfiguration(AbstractTEntityFieldConfiguration p_oConfiguration) {
		final String sFieldName = p_oConfiguration.getName();

		final AbstractTEntityFieldConfiguration oExistingField = this.fields.get(sFieldName);
		if (oExistingField == null || !oExistingField.merge(p_oConfiguration)) {
			this.fields.put(sFieldName, p_oConfiguration);	
		}
	}

	/**
	 * Add a configuration for a basic field
	 *
	 * @param p_sName name of basic field
	 * @param p_iType type of basic field
	 * @param p_bMandatory defines if field is mandatory
	 */
	public void addBasicFieldConfiguration(String p_sName, int p_iType, boolean p_bMandatory) {
		this.addFieldConfiguration(new TBasicEntityFieldConfiguration(p_sName, p_iType, p_bMandatory));
	}


	/**
	 * Returs the configuration of a field using its name or null if no field exits with this name.
	 *
	 * @param p_sFieldName The name of the searched field.
	 * @return a {@link com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.AbstractTEntityFieldConfiguration} object.
	 */
	public AbstractTEntityFieldConfiguration getFieldByName(final String p_sFieldName) {
		return this.fields.get(p_sFieldName);
	}

	/**
	 * Merges two configuration of the same entity.
	 *
	 * @param p_oConfiguration
	 * 		An another configuration to merge with the current configuration.
	 * 		If <code>null</code> no merge has been performed.
	 * @return The name of entity or null if it is not define.
	 */
	public boolean merge(TEntityConfiguration p_oConfiguration) {
		final boolean bMerge = p_oConfiguration != null
				&& this.getName().equals(p_oConfiguration.getName());

		if (bMerge) {
			AbstractTEntityFieldConfiguration oExistingField = null;
			for (AbstractTEntityFieldConfiguration oField : p_oConfiguration.getFields()) {
				oExistingField = this.fields.get(oField.getName());
				if (oExistingField == null) {
					this.addFieldConfiguration(oField);
				}
				else {
					oExistingField.merge(oField);
				}
			}
		}

		return bMerge;
	}
}
