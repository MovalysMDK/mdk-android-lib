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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.converter;

import java.util.HashMap;

/**
 * This interface describes a custom converter that can be specified by the developer.
 *
 * @since Cotopaxi
 */
public abstract class CustomConverter {

	/**
	 * A map containing the parameters of the converter
	 */
	protected HashMap<String, String> attributes = null;
	
	/**
	 * The list of the attributes for this custom converter
	 *
	 * @return an array of String that indicates the name of the attributes
	 * use for this custom converter
	 */
	public abstract String[] getAttributesName();
	
	/**
	 * Converts an object value from type of the component to the type
	 * of the associated field in the ViewModel
	 *
	 * @param p_oValueToConvert The value to convert.
	 * @param p_oReturnClass the class of expected returned object.
	 * @return The converted object
	 */
	public abstract Object convertFromComponentToVM(Object p_oValueToConvert, Class<?> p_oReturnClass);
	
	/**
	 * Converts an object value from type of the ViewModel to the type
	 * of the component
	 *
	 * @param p_oValueToConvert The value to convert.
	 * @return The converted object
	 */
	public abstract Object convertFromVMToComponent(Object p_oValueToConvert);
	
	/**
	 * <p>Setter for the field <code>attributes</code>.</p>
	 *
	 * @param p_oAttributesValues an array of {@link java.lang.String} objects.
	 */
	public void setAttributes(String[] p_oAttributesValues) {
		if(p_oAttributesValues.length != this.getAttributesName().length) {
			
			return;
		} 
		else {
			this.attributes = new HashMap<String, String>();
			for(int index = 0 ; index < this.getAttributesName().length; index++) {
				this.attributes.put(this.getAttributesName()[index], p_oAttributesValues[index]);
			}
		}
	}
}
