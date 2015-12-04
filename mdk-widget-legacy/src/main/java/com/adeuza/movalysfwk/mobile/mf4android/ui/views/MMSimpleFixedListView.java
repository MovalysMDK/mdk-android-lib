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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * <p>Simple fixed list component : there is no detail popup for its items.</p>
 *
 * @param <ITEM> class of an item in list
 * @param <ITEMVM> class of the item view model
 */
public class MMSimpleFixedListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends MMFixedListView<ITEM, ITEMVM>  {

	/**
	 * <p>
	 *  Construct an object <em>MMSimpleFixedListView</em>.
	 * </p>
	 * @param p_oContext The application context
	 * @param p_oAttrs parameter to configure the <em>MMSimpleFixedListView</em> object.
	 */
	public MMSimpleFixedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}
	
	@Override
	protected void wrapCurrentView(View p_oView, ITEMVM p_oCurrentViewModel) {
		super.wrapCurrentView(p_oView, p_oCurrentViewModel);
		p_oView.setClickable(false);
	}
}
