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

/**
 * <p>Defines basic configuration</p>
 *
 *
 */
public class AbstractConfiguration implements Comparable<AbstractConfiguration>, Serializable {

	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;
	
	/** the configuration's name */
	private String name = null;
	/** the configuration's key use in map : type + "." + name */
	private String type = null;

	/**
	 * Default contructor: Creates a new configuration without name.
	 */
	protected AbstractConfiguration() {
		this.name = null;
		this.type = null;
	}

	/**
	 * Constructs a new AbstractConfiguration
	 *
	 * @param p_sName the configuration's name
	 * @param p_sType the type's name
	 */
	public AbstractConfiguration(final String p_sName, String p_sType) {
		this.name = p_sName;
		this.type = p_sType;
	}
	
	/**
	 * Retuns the configuration's name
	 *
	 * @return the configuration's name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Defines the name of this configuration component.
	 *  This method should only be called by deserialization.
	 *
	 * @param p_sName
	 * 		Name of this configuration component. Never null.
	 */
	public final void setName(final String p_sName) {
		this.name = p_sName;
	}
	
	/**
	 * Returns the configuration's key
	 *
	 * @return the key
	 */
	public final String getKey() {
		return this.type == null || this.name == null ? null : this.type + "." + this.name;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Compare two configuration
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AbstractConfiguration p_oConfiguration) {
		return this.getKey().compareTo(p_oConfiguration.getKey());
	}
}
