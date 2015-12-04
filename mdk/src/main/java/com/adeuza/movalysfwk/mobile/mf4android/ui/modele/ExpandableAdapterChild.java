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
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKExpandableAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Processes to execute in the {@link AbstractConfigurableExpandableListAdapter} for the child view
 * @param <ITEM> the class of the group item
 * @param <SUBITEM> the class of the child item
 * @param <SUBITEMVM> the class of the view model of the child
 * @param <ITEMVM> the class of the view model of the group
 */
public class ExpandableAdapterChild<ITEM extends MIdentifiable, SUBITEM extends MIdentifiable, SUBITEMVM extends ItemViewModel<SUBITEM>, ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>>
	extends AbstractAdapterLevelTreatment<MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM>> {


	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getCurrentViewModel(MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getChild(p_oPositions[0], p_oPositions[1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, View p_oView, boolean p_bExpanded) {
		return p_oAdapter.createViewHolder(p_oView);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, int p_iViewType) {
		return p_oAdapter.getChildLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterInflateRow(MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, LayoutInflater p_oLayoutInflater, View p_oCurrentRow, View p_oView) {
		if (p_oAdapter.getChildCoatLayout()!=0 
				&& p_oAdapter.getChildCoatContentLayoutId()!=0){
			View currentCoatRow = p_oLayoutInflater.inflate(p_oAdapter.getChildCoatLayout(), null);
			ViewGroup oContentView = (ViewGroup) currentCoatRow.findViewById(p_oAdapter.getChildCoatContentLayoutId());
			if (p_oCurrentRow.getParent()==null){
				oContentView.addView(p_oCurrentRow);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, View p_oCurrentRow, MasterVisualComponent p_oCompositeComponent) {
		p_oAdapter.getComponents().remove(p_oCompositeComponent);
		
		if (p_oAdapter.getChildCoatLayout()!=0 
				&& p_oAdapter.getChildCoatContentLayoutId()!=0){
			p_oCurrentRow = p_oCurrentRow.findViewById(p_oAdapter.getChildCoatContentLayoutId());
		}
	}
	
	@Override
	public void doAfterSetViewModelProcess(
			MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter,
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
	public View getViewToReturn(MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, View p_oCurrentRow, ViewModel p_oCurrentVM) {
		if (p_oAdapter.getChildCoatLayout() != 0 && p_oAdapter.getChildCoatContentLayoutId() != 0){
			p_oAdapter.wrapCoat(p_oCurrentRow, (SUBITEMVM) p_oCurrentVM);
			return (View) p_oCurrentRow.getParent();
		} else {
			return p_oCurrentRow;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterOnDataLoaded(
			MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, ConfigurableListViewHolder p_oViewholder) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postInflate(
			MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter,
			View p_oCurrentRow, boolean b_isExpanded) {
		p_oAdapter.postInflateChildView(p_oCurrentRow );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postBind(
			MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter,
			View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded,
			int... p_oPositions) {
		p_oAdapter.postBindChildView(p_oCurrentRow, (SUBITEMVM) p_oCurrentVM, p_oPositions[0], p_oPositions[1]);
	}
}
