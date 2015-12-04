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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;

/**
 * <p>Defines view model. Register all visual component for bind data</p>
 *
 *
 * @param <ITEM> the entity type
 */
public abstract class AbstractItemViewModel<ITEM extends MIdentifiable> extends AbstractViewModel implements ItemViewModel<ITEM>{

	/**
	 * default serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel#transformIdFromIdentifiable(java.lang.String)
	 */
	@Override
	public String transformIdFromIdentifiable(String p_sId) {
		return String.valueOf(p_sId);
	}

	/**
	 * Update view model from ITEM data
	 *
	 * @param p_oIdentifiable a ITEM object.
	 */
	@Override
	public void updateFromIdentifiable(ITEM p_oIdentifiable) {
	}
}
