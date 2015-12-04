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

import java.util.HashMap;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Abstract {@link AdapterLevelTreatment} class
 * @param <ADAPTER> the class of the linked adapter
 */
public abstract class AbstractAdapterLevelTreatment<ADAPTER> implements AdapterLevelTreatment<ADAPTER> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBeforeSetViewModelProcess(ADAPTER p_oAdapter, boolean p_bExpanded, View p_oView, ViewModel p_oCurrentVM, int... p_oPositions) {
		// Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean needToInflateView(View p_oCurrentRow, int p_iViewType) {
		return p_oCurrentRow == null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterInflateRow(ADAPTER p_oAdapter, LayoutInflater p_oLayoutInflater, View p_oCurrentRow, View p_oView) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(ADAPTER p_oAdapter, View p_oCurrentRow, MasterVisualComponent p_oCompositeComponent) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getInflateParameters(ConfigurableListViewHolder p_oHolder) {
		Map<String, Object> r_oInflateParameters = new HashMap<>();
		r_oInflateParameters.put(ConfigurableListViewHolder.CONFIGURABLE_LIST_VIEW_HOLDER_KEY, p_oHolder);
		return r_oInflateParameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterSetViewModelProcess(ADAPTER p_oAdapter, boolean p_bExpanded, ConfigurableListViewHolder p_oViewholder, 
			ViewModel p_oCurrentVM, MasterVisualComponent p_oCompositeComponent, int... p_oPositions) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View inflateOnNullViewModel(ADAPTER p_oAdapter, LayoutInflater p_oLayoutInflater, View p_oCurrentRow, ConfigurableListViewHolder p_oHolder, int... p_oPositions) {
		throw new MobileFwkException("ConfigurableExpandableListAdapterDelegate", "View model must not be null");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getViewToReturn(ADAPTER p_oAdapter, View p_oCurrentRow, ViewModel p_oCurrentVM) {
		return p_oCurrentRow;
	}
	
}
