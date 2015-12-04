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
 * <p>Interface For View Model representation used in MMExpandableListView.
 * Use this interface for form representation when it includes a collection of items to be prensented as an expandable.</p>
 *
 *
 */
public interface ExpandableIdentifiable<ITEM extends MIdentifiable,SUBITEM extends MIdentifiable,SUBITEMVM extends ItemViewModel<SUBITEM>> extends ItemViewModel<ITEM> {

	/**
	 * when class AVM agregates class BVM avm.getComposite() return a list of BVM
	 * Return the list of linked entities that will be used as sub-list for this entity
	 *
	 * @return the list of agregated entities
	 */
	public ListViewModel<SUBITEM,SUBITEMVM> getComposite();
	
		
}
