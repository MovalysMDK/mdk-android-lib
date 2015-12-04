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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * Describes a view holding a {@link AbstractConfigurableFixedListAdapter}<br/>
 *
 * @param <ITEM> An entity class
 * @param <ITEMVM> A viewmodel classe
 */
public interface MMAdaptableFixedListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> {

	/**
	 * Setter adapter
	 * @param p_oAdapter the adapter to set
	 */
	public void setAdapter(AbstractConfigurableFixedListAdapter<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> p_oAdapter);

	/**
	 * Getter adapter
	 * @return the adapter
	 */
	public AbstractConfigurableFixedListAdapter<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> getAdapter();
}
