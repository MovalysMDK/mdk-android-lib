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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Defines an abstract entity field configuration</p>
 */
public abstract class AbstractTEntityFieldConfiguration {
	
	/** data type String */
	public static final int TYPE_STRING = 0;
	/** data type Integer */
	public static final int TYPE_INTEGER = 1;
	/** data type Float */
	public static final int TYPE_FLOAT = 2;
	/** data type Date */
	public static final int TYPE_DATE = 3;
	/** data type DateTime */
	public static final int TYPE_DATETIME = 4;
	/** data type Duration */
	public static final int TYPE_DURATION = 5;
	/** data type Email */
	public static final int TYPE_EMAIL = 6;
	/** data type Url */
	public static final int TYPE_URL = 7;
	/** data type Phone */
	public static final int TYPE_PHONE = 8;
	/** data type Time */
	public static final int TYPE_TIME = 9;

	/** field's name */
	private String name;
	
	/** field's type */
	private int type;
	
	/** indicates if field is mandatory */
	private boolean mandatory;

	/** Workflow associated to this field. */
	private Collection<?> workflow;
	/** map that contains all defined parameters of this field*/
	private Map<String, String> parameters;

	/**
	 * Creates a new field. By default, this field has not name, is not customisable or mandatory and its type is String.
	 */
	public AbstractTEntityFieldConfiguration() {
		this(null, TYPE_STRING, false);
	}

	/**
	 * Initializes a new AbstractEntityFieldConfiguration
	 *
	 * @param p_sName the field's name
	 * @param p_iType field's type
	 * @param p_bMandatory indicates if field is mandatory
	 */
	@SuppressWarnings("rawtypes")
	public AbstractTEntityFieldConfiguration(String p_sName, int p_iType, boolean p_bMandatory) {
		this.setName(p_sName);
		this.setType(p_iType);
		this.setMandatory(p_bMandatory);
		this.workflow		= new ArrayList();
		this.parameters		= new HashMap<>();

	}

	/**
	 * Returns the name of the field
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Defines the name of the field.
	 *
	 * @param p_sName
	 * 		The name of the field.
	 */
	public final void setName(final String p_sName) {
		this.name = p_sName;
	}
	
	/**
	 * Returs the type of the field
	 *
	 * @return a int.
	 */
	public final int getType() {
		return this.type;
	}

	/**
	 * Defines the type of this field.
	 *
	 * @param p_iType
	 * 		The type of this field.
	 */
	public final void setType(final int p_iType) {
		this.type = p_iType;
	}

	/**
	 * Returns <code>true</code> if the field is mandatory.
	 *
	 * @return a boolean.
	 */
	public final boolean isMandatory() {
		return this.mandatory;
	}

	/**
	 * Defines the value of the "mandatory" flag.
	 *
	 * @param p_bMandatory
	 * 		<code>true</code> if the field is mandatory, <code>false</code> otherwise.
	 */
	public final void setMandatory(final boolean p_bMandatory) {
		this.mandatory = p_bMandatory;
	}

	/**
	 * Returns the workflow of this field or an empty list if no workflow states exist.
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public final Collection<?> getWorkflow() {
		return this.workflow;
	}

	/**
	 * Returns all defined parameters of this field.
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public final Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * <p>
	 * 	Add a parameter to the field.
	 * </p>
	 *
	 * @param p_sName
	 * 			the field's name
	 * @param p_iValue
	 * 			the parameter value
	 */
	public final void addParameter(String p_sName, int p_iValue) {
		this.parameters.put(p_sName, Integer.toString(p_iValue));
	}

	/**
	 * <p>
	 * 	Add a parameter to the field.
	 * </p>
	 *
	 * @param p_sName
	 * 			the field's name
	 * @param p_lValue
	 * 			the parameter value
	 */
	public final void addParameter(String p_sName, long p_lValue) {
		this.parameters.put(p_sName, Long.toString(p_lValue));
	}

	/**
	 * <p>
	 * 	Add a parameter to the field.
	 * </p>
	 *
	 * @param p_sName
	 * 			the field's name
	 * @param p_fValue
	 * 			the parameter value
	 */
	public final void addParameter(String p_sName, float p_fValue) {
		this.parameters.put(p_sName, Float.toString(p_fValue));
	}

	/**
	 * <p>
	 * 	Add a parameter to the field.
	 * </p>
	 *
	 * @param p_sName
	 * 			the field's name
	 * @param p_dValue
	 * 			the parameter value
	 */
	public final void addParameter(String p_sName, double p_dValue) {
		this.parameters.put(p_sName, Double.toString(p_dValue));
	}

	/**
	 * <p>
	 * 	Add a parameter to the field.
	 * </p>
	 *
	 * @param p_sName
	 * 			the field's name
	 * @param p_bValue
	 * 			the parameter value
	 */
	public final void addParameter(String p_sName, boolean p_bValue) {
		this.parameters.put(p_sName, Boolean.toString(p_bValue));
	}

	/**
	 * <p>
	 * 	Add a parameter to the field.
	 * </p>
	 *
	 * @param p_sName
	 * 			the field's name
	 * @param p_sValue
	 * 			the parameter value
	 */
	public final void addParameter(String p_sName, String p_sValue) {
		this.parameters.put(p_sName, p_sValue);
	}

	/**
	 * A2A_DOC Décrire la méthode merge de la classe AbstractEntityFieldConfiguration
	 *
	 * @param p_oConfiguration
	 * 				the field configuration
	 * @return true when merge
	 */
	public boolean merge(final AbstractTEntityFieldConfiguration p_oConfiguration) {

		return p_oConfiguration != null
				&& this.getName().equals(p_oConfiguration.getName())
				&& this.getType() == p_oConfiguration.getType()
				&& this.isMandatory() == p_oConfiguration.isMandatory();
	}
}
