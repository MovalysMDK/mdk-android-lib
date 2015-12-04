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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.list;

import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.list.TListDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.AbstractConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;

/**
 * <p>Configuration of a list usable by custom fields.</p>
 */
public class CustomListConfiguration extends AbstractConfiguration {

	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * All items of this list. Each item is a key-value pair.
	 */
	private Map<String, String> items;

	/**
	 * Creates a new list using its transfer representation.
	 *
	 * @param p_oTransferObject
	 * 		The object provided by synchronisation that represents the list.
	 */
	public CustomListConfiguration(final TListDescriptor p_oTransferObject) {
		super(p_oTransferObject.getName(), ConfigurationsHandler.TYPE_LIST);
		this.items = p_oTransferObject.getItems();
	}

	/**
	 * Returns all items of this list.
	 *
	 * @return All items of this list.
	 */
	public final Map<String, String> getItems() {
		return this.items;
	}
}
