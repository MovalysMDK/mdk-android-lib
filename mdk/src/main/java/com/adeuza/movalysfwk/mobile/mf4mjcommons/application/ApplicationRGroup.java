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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.application;

/**
 * <p>ApplicationRGroup class.</p>
 */
public enum ApplicationRGroup {

	/** group 'id' in R class : replace R.id */
	ID("Id","id"),
	/** group 'layout' in R class : replace R.layout */
	LAYOUT("Layout","layout"),
	/** group 'string' in R class : replace R.string */
	STRING("String","string"),
	/** group 'menu' in R class : replace R.menu */
	MENU("Menu","menu"),
	/** group 'xml' in R class : replace R.xml */
	XML("XML","xml"),
	/** group 'style' in R class : replace R.style */
	STYLE("Style","style"),
	/** group 'drawable' in R class : replace R.drawable */
	DRAWABLE("Drawable","drawable"),
	/** group 'color' in R class : replace R.color */
	COLOR("Color","color"),
	/** group 'raw' in R class : replace R.raw */
	RAW("Raw", "raw"),
	/** group 'anim' in R class : replace R.anim */
	ANIM("Anim", "anim"),
	/** group 'array' in R class : replace R.array */
	ARRAY("Array", "array");
	/** the key identifier */
	private String key = null;
	/** the r path */
	private String r = null;
	
	/**
	 * Private contructor for enum
	 * @param p_sKey the key to find group
	 * @param p_sR the name of group in R class
	 */
	private ApplicationRGroup(String p_sKey, String p_sR) {
		this.key = p_sKey;
		this.r = p_sR;
	}
	
	/**
	 * <p>Getter for the field <code>key</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey() {
		return this.key;
	}	
	
	/**
	 * <p>Getter for the field <code>r</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getR() {
		return this.r;
	}
}
