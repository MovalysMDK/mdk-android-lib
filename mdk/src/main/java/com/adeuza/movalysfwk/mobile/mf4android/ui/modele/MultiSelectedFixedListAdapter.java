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
package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl;

/**
 * <p>Generic adapter for a simple <code>FixedList</code> that only display some items, not initiate actions.</p>
 *
 *
 * @param <SUBITEM> model entity
 * @param <SUBITEMVM> a view model for list  
 * 
 * @since MF-Baltoro
 */
public class MultiSelectedFixedListAdapter<SUBITEM extends MIdentifiable, SUBITEMVM extends ItemViewModel<SUBITEM>> 
				extends AbstractConfigurableFixedListAdapter<SUBITEM,SUBITEMVM,ListViewModel<SUBITEM, SUBITEMVM>> {

	/**
	 * Construct a new Adapter for all selected items in <code>MMMultiSelectedExpandableListView</code> component.
	 * @param p_iLayoutId the layout id
	 * @param p_iComponentId the layout component id
	 * @param p_oClass the second item view model class
	 */
	public MultiSelectedFixedListAdapter(int p_iLayoutId, int p_iComponentId, Class<SUBITEMVM> p_oClass) {
		super(new ListViewModelImpl<>(p_oClass, false),p_iLayoutId,p_iComponentId,-1);//pour désactiver l'action lors du clic sur l'item
	}
	
	/**
	 * {@inheritDoc}
	 * <p>DONT USE THIS METHOD, IN OUR CASE THIS METHOD WILL ALLWAYS RETURN NULL !!</p>
	 */
	@Override
	public SUBITEMVM createEmptyVM() {
		/* Nothing to do */
		return null;
	}

}
