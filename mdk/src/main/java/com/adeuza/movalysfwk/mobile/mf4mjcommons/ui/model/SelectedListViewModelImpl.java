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
 * <p>Implementation permettant de définir un view model qui permet la sélection d'item de liste.</p>
 *
 *
 * @since Baltoro
 */
public class SelectedListViewModelImpl<ITEM extends MIdentifiable,ITEMVM extends ItemViewModel<ITEM>> extends ListViewModelImpl<ITEM, ITEMVM> implements SelectedListViewModel<ITEM,ITEMVM> {

	/**
	 * <p>Constructor for SelectedListViewModelImpl.</p>
	 *
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public SelectedListViewModelImpl(Class<ITEMVM> p_oClass) {
		super(p_oClass);
	}

}
