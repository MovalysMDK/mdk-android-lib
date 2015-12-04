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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * <p>Commun rule for named object</p>
 *
 *
 */
public abstract class AbstractNamableObject {

	/** number of key component */
	private static final int NB_KEYS = 3;
	
	/** key localisation position */
	private static final int KEY_LOCALISATION = 0;
	
	/** group localisation position */
	private static final int KEY_MODEL = 1;
	
	/** key separator */
	public static final String KEY_SEPARATOR = "__";
	
	/** unknown element */
	public static final String UNKNOWN = "UNKNOWN";
	
	/** identifiant of component */
	private String name = null;
	
	/** localisation */
	private String localisation = UNKNOWN;
	
	/** model */
	private String model = UNKNOWN;

	/** Nom de la configuration. */
	private String configurationName = UNKNOWN;

	private boolean master = false;
	
	public AbstractNamableObject(boolean p_bIsMaster) {
		this.master = p_bIsMaster;
	}
	
	/**
	 * Returns the name of object
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the localisation for object
	 *
	 * @return the localisation
	 */
	public String getLocalisation() {
		return this.localisation;
	}
	
	/**
	 * Returns the model's name
	 *
	 * @return the model
	 */
	public String getModel() {
		return this.model;
	}

	/**
	 * Returns the configuration's name
	 *
	 * @return the configuration's name
	 */
	public String getConfigurationName() {
		return this.configurationName;
	}
	
	/**
	 * Indicates whether the component is a label.
	 *
	 * @return true whether the component is a label
	 */
	public boolean isLabel() {
		return this.name.endsWith(ConfigurableVisualComponent.TYPE_LABEL);
	}
	
	/**
	 * Indicates whether the component is a value.
	 *
	 * @return true whether the component is a value
	 */
	public boolean isValue() {
		return this.name.endsWith(ConfigurableVisualComponent.TYPE_VALUE);
	}

	/**
	 * Indicates whether the component is editable
	 *
	 * @return true whether the component is editable
	 */
	public boolean isEdit() {
		return this.name.endsWith(ConfigurableVisualComponent.TYPE_EDIT) || this.hasNoValue();
	}
	
	/**
	 * Indicates whether the component is a master component
	 *
	 * @return true whether the component is a panel
	 */
	public boolean isMaster() {
		return this.master;
	}
	
	public void setMaster(boolean p_bIsMaster) {
		this.master = p_bIsMaster;
	}
	
	/**
	 * Indicates whether the component is an unknown component
	 *
	 * @return true whether the component is a unknown
	 */
	public boolean isUnknown() {
		return this.name.contains(UNKNOWN);
	}
	
	/**
	 * Indicates whether the component has no value
	 * If it is, then there is no writing done by the view model on that component
	 * @return true if the component has no value
	 */
	public boolean hasNoValue() {
		return this.name.contains(ConfigurableVisualComponent.TYPE_NOVALUE);
	}
	
	/**
	 * Set id and compute group and localisation. The format of the name is [localisation]__[model]__[type]
	 *
	 * @param p_sStringId the id to set
	 */
	public void setName(String p_sStringId) {
		this.name = p_sStringId;
		String[] oKeys = this.name.split(KEY_SEPARATOR);
		if (oKeys.length == NB_KEYS) {
			this.localisation = oKeys[KEY_LOCALISATION];
			this.model = oKeys[KEY_MODEL];
			this.configurationName = this.localisation + KEY_SEPARATOR + this.model;
		}
	} 
}
