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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter;

import java.util.HashMap;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;

/**
 * <p>Abstract CustomFormatter class.</p>
 *
 */
@Scope(ScopePolicy.PROTOTYPE)
public abstract class CustomFormatter {
	
	/**
	 * This code is used when an error ocurred during format.
	 */
	public static final int FORMATTER_FORMAT_ERROR_CODE = -1;
	
	/**
	 * This code is used when an error ocurred during format.
	 */
	public static final int FORMATTER_UNFORMAT_ERROR_CODE = -2;
	
	/**
	 * A map containing the parameters of the formatter
	 */
	protected HashMap<String, String> attributes = null;
	
	/**
	 * The list of the attributes for this custom formatter
	 *
	 * @return an array of String that indicates the name of the attributes
	 * use for this custom formatter
	 */
	public abstract String[] getAttributesName();
	
	/**
	 * Format an object
	 *
	 * @param p_oValueToFormat The value to convert.
	 * @return The formatted object
	 */
	public abstract Object format(Object p_oValueToFormat);
	
	/**
	 * Unformat an object
	 *
	 * @param p_oValueToUnformat The value to convert.
	 * @return The unformatted object
	 */
	public abstract Object unformat(Object p_oValueToUnformat);
	
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
