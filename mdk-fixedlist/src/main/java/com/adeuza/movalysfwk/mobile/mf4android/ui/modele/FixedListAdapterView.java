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
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

public class FixedListAdapterView <ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>>
	extends AbstractAdapterLevelTreatment<MDKFixedListAdapter<ITEM, ITEMVM, LISTVM>> {

	@Override
	public ViewModel getCurrentViewModel(MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getItem(p_oPositions[0]);
	}

	@Override
	public int getItemLayout(MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int p_iViewType) {
		return p_oAdapter.getItemLayout(p_iViewType);
	}

	@Override
	public ConfigurableListViewHolder getViewHolder(MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oView, boolean p_bExpanded) {
		return p_oAdapter.createViewHolder(p_oView);
	}

	@Override
	public void postInflate(MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oCurrentRow, boolean b_isExpanded) {
		p_oAdapter.postInflateView(p_oCurrentRow);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postBind(MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded, int... p_oPositions) {
		p_oAdapter.postBindView(p_oCurrentRow, (ITEMVM) p_oCurrentVM, p_oPositions[0]);
	}

	@Override
	public void doAfterOnDataLoaded(MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, ConfigurableListViewHolder p_oViewholder) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oCurrentRow, MasterVisualComponent p_oCompositeComponent) {
		p_oAdapter.getComponents().remove(p_oCompositeComponent);
	}
	
	@Override
	public void doAfterSetViewModelProcess(
			MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			boolean p_bExpanded, ConfigurableListViewHolder p_oViewholder, ViewModel p_oCurrentVM,
			MasterVisualComponent p_oCompositeComponent, int... p_oPositions) {
		super.doAfterSetViewModelProcess(p_oAdapter, p_bExpanded, p_oViewholder,
				p_oCurrentVM, p_oCompositeComponent, p_oPositions);
		p_oAdapter.getComponents().add(p_oCompositeComponent);
	}

}
