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
 * <p>This abstract implementation manages an identifier named "identifier" in the model</p>
 * @param <ITEM> the entity type
 */
public abstract class AbstractItemViewModelIdentifier<ITEM extends MIdentifiable> extends AbstractItemViewModel<ITEM> {

	/**
	 * default serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Key used to identify the id_identifier attribute
	 */
	protected static final String KEY_ID_IDENTIFIER = "id_identifier";
	
	/**
	 * <p>Attribute </p>
	 * <p> type=long mandatory=false</p>
	 */
	protected long id_identifier;
	
	/** 
	 * Get identifier
	 * @return view model identifier
	 */
	public long getId_identifier() {
		return this.id_identifier;
	}

	/**
	 * Set identifier
	 * @param p_lId_identifier new identifier value
	 */
	public void setId_identifier(long p_lId_identifier) {
		this.affectLongAndNotify(this.id_identifier, p_lId_identifier, KEY_ID_IDENTIFIER);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIdVM() {
		return String.valueOf(this.getId_identifier());
	}
}
