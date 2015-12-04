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
package com.adeuza.movalysfwk.mf4jcommons.model.configuration.list;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * <p>TODO Décrire la classe ListDescriptor</p>
 *
 *
 */
public class TListDescriptor {
	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of this list.
	 */
	@SerializedName("n")
	public String name;

	/**
	 * Items of this list.
	 * Key-value pairs that defined each element of the list.
	 */
	@SerializedName("it")
	public Map<String, String> items;

	/**
	 * Default constructor. Used only by deserialization.
	 * TODO Décrire le constructeur ListDescriptor
	 */
	protected TListDescriptor() {
		this.items = new HashMap<>();
	}

	/**
	 * Creates a new list using its name.
	 *
	 * @param p_sName
	 * 		Name of this list.
	 */
	public TListDescriptor(final String p_sName) {
		this();
		this.name 	= p_sName;
	}

	/**
	 * Returns the name of this list.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Defines the name of this list. Used only by deserialization.
	 *
	 * @param p_sName
	 * 		The name of this list.
	 */
	protected final void setName(final String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Returns all items of this list.
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public final Map<String, String> getItems() {
		return this.items;
	}
	
	/**
	 * Defines the items of this list. Used only by deserialization.
	 *
	 * @param p_oItems
	 * 		The items of this list.
	 */
	protected final void setItems(Map<String, String> p_oItems) {
		this.items.clear();
		this.items.putAll(p_oItems);
	}

	/**
	 * Adds a new item to this list.
	 *
	 * @param p_sKey
	 * 		Key of the item.
	 * @param p_sValue
	 * 		Value of the item.
	 */
	public void addItem(String p_sKey, String p_sValue) {
		this.items.put(p_sKey, p_sValue);
	}
}
