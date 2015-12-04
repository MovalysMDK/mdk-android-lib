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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKFlipperAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Processes to execute in the {@link MDKFlipperAdapter} for the child view
 * @param <ITEM> the class of the item
 * @param <SUBITEM> the class of the group item
 * @param <SUBSUBITEM> the class of the child item
 * @param <SUBSUBITEMVM> the class of the view model of the child
 * @param <SUBITEMVM> the class of the view model of the group
 * @param <ITEMVM> the class of the view model of the title
 */
public class FlipperAdapterChild<
		ITEM extends MIdentifiable, 
		SUBITEM extends MIdentifiable, 
		SUBSUBITEM extends MIdentifiable, 
		SUBSUBITEMVM extends ItemViewModel<SUBSUBITEM>, 
		SUBITEMVM extends ExpandableViewModel<SUBITEM, SUBSUBITEM, SUBSUBITEMVM>, 
		ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> 
	extends AbstractAdapterLevelTreatment<MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getCurrentViewModel(
			MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			int... p_oPositions) {
		return p_oAdapter.getChild(p_oPositions[0], p_oPositions[1], p_oPositions[2]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(
			MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			int p_iViewType) {
		return p_oAdapter.getChildLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterInflateRow(MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter, LayoutInflater p_oLayoutInflater, View p_oCurrentRow, View p_oView) {
		if (p_oAdapter.getChildLayout() != 0) {
			ViewGroup oContentView = (ViewGroup) p_oLayoutInflater.inflate(p_oAdapter.getChildLayout(), null);
			if (p_oCurrentRow.getParent()==null){
				oContentView.addView(p_oCurrentRow);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter, View p_oCurrentRow, MasterVisualComponent p_oCompositeComponent) {
		p_oAdapter.getComponents().remove(p_oCompositeComponent);
		
		if (p_oAdapter.getChildLayout() != 0) {
			p_oCurrentRow = p_oCurrentRow.findViewById(p_oAdapter.getChildLayout());
		}
	}
	
	@Override
	public void doAfterSetViewModelProcess(
			MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			boolean p_bExpanded, ConfigurableListViewHolder p_oViewholder, ViewModel p_oCurrentVM,
			MasterVisualComponent p_oCompositeComponent, int... p_oPositions) {
		super.doAfterSetViewModelProcess(p_oAdapter, p_bExpanded, p_oViewholder,
				p_oCurrentVM, p_oCompositeComponent, p_oPositions);
		p_oAdapter.getComponents().add(p_oCompositeComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(
			MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			View p_oView, boolean p_bExpanded) {
		return p_oAdapter.createViewHolderChildItem(p_oView);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterOnDataLoaded(
			MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			ConfigurableListViewHolder p_oViewholder) {
		// Nothing to do
	}

	@Override
	public void postInflate(
			MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			View p_oCurrentRow, boolean b_isExpanded) {
		p_oAdapter.postInflateChildView(p_oCurrentRow);
	}

	@Override
	public void postBind(
			MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded,
			int... p_oPositions) {
		p_oAdapter.postBindChildView(p_oCurrentRow, (SUBSUBITEMVM) p_oCurrentVM, p_oPositions[0], p_oPositions[1]);
	}
}
