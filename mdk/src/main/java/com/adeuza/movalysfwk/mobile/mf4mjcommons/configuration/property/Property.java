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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.property.TProperty;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.AbstractConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.PropertyName;


/**
 * <p>
 * 	Represents the "property" concept: a key-value pair used to variabilize an application.
 * 	Here, this key-value pair is associated to an integer that represents the version of the property:
 * 	If thow properties, with the same name but different value are defined, only the property with the greater version number
 * 	is used by the application.
 * </p>
 *
 *
 */
public class Property extends AbstractConfiguration {

	/**
	 * The string constant "true".
	 */
	public static final String TRUE = "true";
	/** 
	 * The string constant "false".
	 */
	public static final String FALSE = "false";

	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;

	/** The value of this property. */
	private String value;

	/**
	 * Default constructor. Creates a new property based to its server representation.
	 *
	 * @param p_oTransferObject
	 * 		The server representation of a property.
	 */
	public Property(TProperty p_oTransferObject) {
		super(p_oTransferObject.name, ConfigurationsHandler.TYPE_PROPERTY);
		this.value = p_oTransferObject.value;
	}

	/**
	 * <p>Constructor for Property.</p>
	 *
	 * @param p_oName a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.PropertyName} object.
	 * @param p_sValue a {@link java.lang.String} object.
	 */
	public Property(PropertyName p_oName, String p_sValue) {
		super(p_oName.name(), ConfigurationsHandler.TYPE_PROPERTY);
		this.value = p_sValue;
	}

	/**
	 * <p>Constructor for Property.</p>
	 *
	 * @param p_oName a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.PropertyName} object.
	 * @param p_bValue a boolean.
	 */
	public Property(PropertyName p_oName, boolean p_bValue) {
		this(p_oName, p_bValue ? TRUE : FALSE);
	}
	
	/**
	 * <p>Constructor for Property.</p>
	 *
	 * @param p_oName a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.PropertyName} object.
	 */
	public Property(PropertyName p_oName) {
		this(p_oName, Application.getInstance().getStringResource(p_oName));
	}
	
	/**
	 * Returns the value of this property.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public final String getValue() {
		return this.value;
	}

	/**
	 * Returns <code>true</code> if the value of this property is equals to {@link TRUE}
	 *
	 * @return <code>true</code> if the value of this property is equals to {@link TRUE}
	 */
	public final boolean getBooleanValue() {
		return TRUE.equalsIgnoreCase(this.value);
	}
	/**
	 * Returns the value of the property in a double
	 * It replace a , by a point
	 *
	 * @return the value of the property in a double
	 */
	public final double getDoubleValue() {
		if (this.value.contains(",")){
			this.value.replace(',', '.');
		}
		return Double.valueOf(this.value);
	}
	/**
	 * Returns the value of the property in a integer
	 * It replace a , by a point
	 *
	 * @return the value of the property in a integer
	 */
	public final int getIntValue() {
		return Integer.valueOf(this.value);
	}	
	
	/**
	 * Returns the value of the property in a long
	 * It replace a , by a point
	 *
	 * @return the value of the property in a long
	 */
	public final long getLongValue() {
		return Long.valueOf(this.value);
	}
}
