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
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKSpinnerAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Processes to execute in the {@link MDKSpinnerAdapter} for the drop down view
 * @param <ITEM> the class of the item
 * @param <ITEMVM> the class of the item view model
 * @param <LISTVM> the class of the list view model
 */
public class SpinnerAdapterDropDown<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> 
	extends AbstractAdapterLevelTreatment<MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getCurrentViewModel(MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getItem(p_oPositions[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBeforeSetViewModelProcess(MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			boolean p_bExpanded, View p_oView, ViewModel p_oCurrentVM, int... p_oPositions) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean needToInflateView(View p_oCurrentRow, int p_iViewType) {
		return p_oCurrentRow == null || p_iViewType == 0 
				|| p_oCurrentRow.getId() == ((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.component__simple_spinner_dropdown_emptyitem__master);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int p_iViewType) {
		if (p_iViewType == 0) {
			// empty item
			return ((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.fwk_component__simple_spinner_dropdown_emptyitem__master);
		} else {
			return p_oAdapter.getSpinnerDropDowListLayoutId(p_iViewType);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oView, boolean p_bExpanded) {
		return p_oAdapter.createDropDownViewHolder(p_oView);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			View p_oCurrentRow, MasterVisualComponent p_oCompositeComponent) {
		p_oAdapter.getComponents().remove(p_oCompositeComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterOnDataLoaded(MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			ConfigurableListViewHolder p_oViewholder) {
		MasterVisualComponent oCompositeComponent = p_oViewholder.getMaster();
		oCompositeComponent.getDescriptor().unInflate(p_oViewholder.getViewGroupHashCode(), oCompositeComponent, null);
		p_oAdapter.getComponents().remove(oCompositeComponent);
	}

	@Override
	public void postInflate(MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oCurrentRow, boolean b_isExpanded) {
		p_oAdapter.postInflateDropDownView(p_oCurrentRow);
	}

	@Override
	public void postBind(MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded, int... p_oPositions) {
		p_oAdapter.postBindDropDownView(p_oCurrentRow, (ITEMVM)p_oCurrentVM, p_oPositions[0]);
	}
}
