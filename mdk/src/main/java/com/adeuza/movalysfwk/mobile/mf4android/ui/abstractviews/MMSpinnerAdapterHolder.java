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
package com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.spinner.MDKSpinnerConnector;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * Used on spinner views to manipulate their adapter
 * 
 * @param <ITEM> the view describing a row of the spinner
 * @param <ITEMVM> the view model of a row in the spinner
 */
public interface MMSpinnerAdapterHolder<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> {

	/**
	 * Returns the adapter of the view
	 * @return the adapter of the view
	 */
	public MDKSpinnerConnector getAdapter();
	
	/**
	 * Sets the adapter on the view
	 * @param p_oAdapter the adapter to set on the view
	 */
	public void setAdapter(MDKSpinnerConnector p_oAdapter);
}
