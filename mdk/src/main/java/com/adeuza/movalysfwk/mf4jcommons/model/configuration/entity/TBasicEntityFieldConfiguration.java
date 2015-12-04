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

/**
 * <p>Defines a basic entity field configuration</p>
 *
 *
 */
public class TBasicEntityFieldConfiguration extends AbstractTEntityFieldConfiguration {
	/**
	 * Creates a new basic field. By default this field has not name, is not mandatory and its value is a string.
	 */
	public TBasicEntityFieldConfiguration() {
		this(null, TYPE_STRING, false);
	}

	/**
	 * Construct a new basic entity field configuration
	 *
	 * @param p_sName the field's name
	 * @param p_iType field's type
	 * @param p_bMandatory indicates if field is mandatory
	 */
	public TBasicEntityFieldConfiguration(String p_sName, int p_iType, boolean p_bMandatory) {
		super(p_sName, p_iType, p_bMandatory);
	}
}
