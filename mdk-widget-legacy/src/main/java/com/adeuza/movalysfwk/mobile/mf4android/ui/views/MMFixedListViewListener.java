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

import android.view.View;

/**
 * Listener on MMFixedListView events
 */
public interface MMFixedListViewListener {
	
	/**
	 * Called when items are added to the list
	 * @param p_oMMFixedListView the list on which the item is added
	 * @param p_iNbElem the number of added items
	 */
	public void onAdd(MMFixedListView<?,?> p_oMMFixedListView, int p_iNbElem);
	
	/**
	 * Called when items are removed from the list
	 * @param p_oMMFixedListView the list on which the item is added
	 * @param p_iNbElem the number of removed items
	 */
	public void onRemove(MMFixedListView<?,?> p_oMMFixedListView, int p_iNbElem);

	/**
	 * Launch an update of view after the writeData
	 * @param p_oMMFixedListView The fixed list
	 * @param p_oView The view to update.
	 */
	public void updateViewDialogAfterWriteData(MMFixedListView<?,?> p_oMMFixedListView, View p_oView);
}
