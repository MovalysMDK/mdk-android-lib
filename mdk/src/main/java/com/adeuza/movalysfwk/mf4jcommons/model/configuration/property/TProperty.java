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
package com.adeuza.movalysfwk.mf4jcommons.model.configuration.property;

import com.google.gson.annotations.SerializedName;

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
public class TProperty {

	/**
	 * Property's identifier
	 */
	@SerializedName("i")
	public long id;

	/**
	 * Property's name
	 */
	@SerializedName("n")
	public String name;

	/**
	 * Property's value
	 */
	@SerializedName("v")
	public String value;

	/**
	 * Property's version
	 */
	@SerializedName("ver")
	public long version;

	/**
	 * <code>true</code> if the propery must be deleted.
	 */
	@SerializedName("del")
	public boolean deleted;
}
