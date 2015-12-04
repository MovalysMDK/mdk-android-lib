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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.assembler.TransferConfigurationAssembler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DateFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DateTimeFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DoubleFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DurationFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.EmailFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.FloatFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IntegerFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.LongFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.PhoneFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.StringFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.TimeFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.UrlFormFieldValidator;

/**
 * <p>Defines an abstract entity field configuration.
 * Sets the configuration of an entity field : mandatory, custom, data type ...</p>
 *
 *
 */
public abstract class AbstractEntityFieldConfiguration implements Serializable {
	
	/** Value of an undefined max length. */
	public static final int UNDEFINED_MAX_LENGTH = -1;
	
	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Defines the data type of the field, giving its related validator
	 */
	public enum DataType {
		/** data type String */
		TYPE_STRING(StringFormFieldValidator.class),
		/** data type Integer */
		TYPE_INTEGER(IntegerFormFieldValidator.class),
		/** data type Double */
		TYPE_DOUBLE(DoubleFormFieldValidator.class),
		/** data type Date */
		TYPE_DATE(DateFormFieldValidator.class),
		/** data type DateTime */
		TYPE_DATETIME(DateTimeFormFieldValidator.class),
		/** data type Duration */
		TYPE_DURATION(DurationFormFieldValidator.class),
		/** data type Email */
		TYPE_EMAIL(EmailFormFieldValidator.class),
		/** data type Url */
		TYPE_URL(UrlFormFieldValidator.class),
		/** data type Phone */
		TYPE_PHONE(PhoneFormFieldValidator.class),
		/** data type Phone */
		TYPE_LONG(LongFormFieldValidator.class),
		/** data type Phone */
		TYPE_FLOAT(FloatFormFieldValidator.class),
		/** data type Time */
		TYPE_TIME(TimeFormFieldValidator.class);
		
		private Class<? extends IFormFieldValidator> validatorClass ;
		
		private DataType( Class<? extends IFormFieldValidator> p_oValidatorClass){
			this.validatorClass = p_oValidatorClass ;
		}
		
		/**
		 * Return the class of the validator linked to the given data type
		 * @return class of the validator linked to the given data type
		 */
		public Class<? extends IFormFieldValidator> getValidatorClass() {
			return this.validatorClass;
		}
		
	}
	/** field's name */
	private String name = null;
	
	/** Max length of the value of this field. */
	private int maxLength;
	
	/** Paramètre spécifiques de l'entité */
	private Map<String, Object> parameters;
	
	/**
	 * Initialises a new AbstractEntityFieldConfiguration
	 *
	 * @param p_sName the field's name
	 * @param p_iType field's type
	 */
	public AbstractEntityFieldConfiguration(String p_sName, int p_iType) {
		this.name = p_sName;
		this.maxLength		= -1;
		this.parameters		= new HashMap<String, Object>();
	}
	
	/**
	 * Returns the field name
	 *
	 * @return the field name
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Defines the max length of the value(s) of this field.
	 *
	 * @param p_iMaxLength The max length of the value of the field.
	 */
	public final void setMaxLength(final int p_iMaxLength) {
		this.maxLength = p_iMaxLength;
	}
	
	/**
	 * Returns the max length of the value(s) of this field.
	 *
	 * @return The max length of the value(s) of this field or {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration#UNDEFINED_MAX_LENGTH} if this field has not max length.
	 */
	public final int getMaxLength() {
		return this.maxLength;
	}
	
	/**
	 * Returns <code>true</code> if the max length has been defined (if its value is different of {@link #UNDEFINED_MAX_LENGTH}
	 *
	 * @return <code>true</code> if the max length has been defined.
	 */
	public final boolean hasMaxLength() {
		return this.maxLength != UNDEFINED_MAX_LENGTH;
	}
	
	/**
	 * Gets the value of parameter named p_sKey
	 *
	 * @param p_sKey the key to find
	 * @return the value for associated key
	 */
	public Object getParameter(String p_sKey) {
		return this.parameters.get(p_sKey);
	}	
	
	/**
	 * Add a parameter
	 *
	 * @param p_sKey the name of parameter
	 * @param p_oValue the value of parameter
	 */
	public void addParameter(String p_sKey, Object p_oValue) {
		this.parameters.put(p_sKey, p_oValue);
	}
	
	/**
	 * <p>putAllParameters.</p>
	 *
	 * @param p_oMap a {@link java.util.Map} object.
	 */
	public void putAllParameters(Map<? extends String,?extends Object> p_oMap){
		this.parameters.putAll(p_oMap);
		this.parameters.remove(TransferConfigurationAssembler.MAX_LENGTH_PARAMETER);
	}
}
