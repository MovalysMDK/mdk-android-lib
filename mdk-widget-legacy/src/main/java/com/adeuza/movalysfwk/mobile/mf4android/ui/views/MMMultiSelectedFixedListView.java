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

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * <p>
 * 	Simplistic implementation of an <code>FixedList</code>, only to obtain a list of items without action on it (opening popup, delete item, etc.).
 * 	There is no title bar too.
 * </p>
 *
 *
 * @param <ITEM> a model for item  
 * @param <ITEMVM> a view model for item
 * 
 */
public class MMMultiSelectedFixedListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends AbstractFixedListView<ITEM, ITEMVM> {

	/** adapter of the expendableList component of the <code>MMMultiSelectedListView</code> */
	@SuppressWarnings("rawtypes")
	private AbstractConfigurableExpandableListAdapter expandableAdapter;
	
	/**
	 * <p>Construct an object <em>MMMultiSelectedFixedListView</em> with parameters.</p>
	 * @param p_oContext the application context
	 * @param p_oAttrs parameter to configure the <em>MMMultiSelectedFixedListView</em> object.
	 */
	public MMMultiSelectedFixedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean treatCustomButton(View p_oView) {
		// Nothing to do
		return false;
	}
	
	/**
	 * Returns the identifier of the delete button in the fixedlist
	 * @return identifier of the delete button
	 */
	public int getDeleteId(){
		return this.getUiDeleteButtonId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doOnClickDeleteButtonOnItem(View p_oView) {
		super.doOnClickDeleteButtonOnItem(p_oView);
		if (expandableAdapter!=null){
			Log.i(getClass().getSimpleName(),"notify the MMExpendableListView changed");
			expandableAdapter.notifyDataSetChanged();
		}
	}
	
	
	/**
	 * Define the Adapter of the <code>ExpendableListView</code> of the <code>MMMUltiSelectedListView</code> component.
	 * @param p_oAdapter the adapter to set
	 */
	public void setExpandableAdapter(@SuppressWarnings("rawtypes") AbstractConfigurableExpandableListAdapter p_oAdapter){
		expandableAdapter=p_oAdapter;
	}

	@Override
	public void beforeConfigurationChanged(Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}

	@Override
	public void afterConfigurationChanged(Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}
}
