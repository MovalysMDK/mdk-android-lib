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
import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;

/**
 * <p>Main interface of the View Model representation. Use this interface for form representation.</p>
 *
 *
 * @param <ITEM> Soit une entité, soit un value object mais il doit être identifiable
 */
@Scope(ScopePolicy.PROTOTYPE)
public interface ItemViewModel <ITEM extends MIdentifiable> extends ViewModel{
	
	/**
	 * <p>transformIdFromIdentifiable.</p>
	 *
	 * @param p_sId a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String transformIdFromIdentifiable(String p_sId);
	
	/**
	 * <p>modifyToIdentifiable.</p>
	 *
	 * @param p_oIdentifiable a ITEM object.
	 */
	public void modifyToIdentifiable(ITEM p_oIdentifiable);
	
	/**
	 * Update view model from ITEM data
	 *
	 * @param p_oIdentifiable a ITEM object.
	 */
	public void updateFromIdentifiable(ITEM p_oIdentifiable);
	
}
