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

import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

public class ListAdapterChild<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>>
	extends AbstractAdapterLevelTreatment<MDKAdapter<ITEM, ITEMVM, LISTVM>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getCurrentViewModel(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getItem(p_oPositions[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBeforeSetViewModelProcess(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, 
			boolean p_bExpanded, View p_oView, ViewModel p_oCurrentVM, int... p_oPositions) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int p_iViewType) {
		return p_oAdapter.getItemLayout(p_iViewType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oView, boolean p_bExpanded) {
		return p_oAdapter.createViewHolder(p_oView);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, 
			View p_oCurrentRow, MasterVisualComponent p_oCompositeComponent) {
		p_oAdapter.getComponents().remove(p_oCompositeComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterSetViewModelProcess(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			boolean p_bExpanded, ConfigurableListViewHolder p_oViewHolder, 
			ViewModel p_oCurrentVM, MasterVisualComponent p_oCompositeComponent, int... p_oPositions) {
		p_oAdapter.getComponents().add(p_oCompositeComponent);
		
		boolean bSelected = p_oCurrentVM.getIdVM().equals(p_oAdapter.getSelectedItem());
		p_oViewHolder.setSelected(bSelected);
		bSelected = bSelected || p_oViewHolder.isSelected();
		p_oViewHolder.getItemView().setSelected(bSelected);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterOnDataLoaded(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, ConfigurableListViewHolder p_oViewholder) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postInflate(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			View p_oCurrentRow, boolean b_isExpanded) {
		p_oAdapter.postInflate(p_oAdapter, p_oCurrentRow, b_isExpanded);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postBind(MDKAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded,
			int... p_oPositions) {
		p_oAdapter.postBind(p_oAdapter, p_oCurrentRow, (ITEMVM) p_oCurrentVM, b_isExpanded, p_oPositions);
	}
}
