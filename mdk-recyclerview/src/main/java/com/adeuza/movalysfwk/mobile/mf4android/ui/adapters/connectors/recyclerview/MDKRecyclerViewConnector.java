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
package com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.recyclerview;

import java.lang.reflect.InvocationTargetException;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevelTreatment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolderImpl;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ListAdapterChild;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;

public class MDKRecyclerViewConnector extends RecyclerView.Adapter<ConfigurableListViewHolderImpl> implements MDKViewConnector {

	private MDKAdapter<?,?,?> mAdapter ;
	
	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	private enum Level implements AdapterLevel {
		/** Child level */
		CHILD(ListAdapterChild.class);
		
		/** treatments class for the level */
		private AdapterLevelTreatment level;
		
		/** 
		 * Constructor
		 * @param p_oLevelClass the treatment class for the level 
		 */
		Level(Class<? extends AdapterLevelTreatment> p_oLevelClass) {
			try {
				this.level = p_oLevelClass.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException 
					| NoSuchMethodException | SecurityException e) {
				Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			}
		}

		/**
		 * returns the treatment class for the level
		 * @return the treatment class for the level 
		 */
		@Override
		public AdapterLevelTreatment getLevel() {
			return this.level;
		}
	}
	
	private DataSetObserver mObserver = new DataSetObserver() {
		
		@Override
		public void onChanged() {
			MDKRecyclerViewConnector.this.notifyDataSetChanged();
		}
		
		@Override
		public void onInvalidated() {
			notifyItemRangeRemoved(0, getItemCount());
		}
	};
	
	public MDKRecyclerViewConnector( MDKAdapter<?,?,?> p_oAdapter ) {
		this.mAdapter = p_oAdapter;
	}
	
	@Override
	public ConfigurableListViewHolderImpl onCreateViewHolder(ViewGroup p_oViewGroup, int p_iViewType) {
		return (ConfigurableListViewHolderImpl) mAdapter.onCreateViewHolder(Level.CHILD, false, p_oViewGroup, p_iViewType);
	}

	@Override
	public void onBindViewHolder(ConfigurableListViewHolderImpl p_oViewholder, int p_iPositions) {
		mAdapter.onBindViewHolder(Level.CHILD, false, p_oViewholder, p_iPositions);
	}

	@Override
	public int getItemCount() {
		return this.mAdapter.getCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeViewModelChanged() {
		this.mAdapter.getMasterVM().unregisterObserver(this.mObserver);
	}

	@Override
	public void afterViewModelChanged() {
		this.mAdapter.getMasterVM().registerObserver(this.mObserver);
		this.notifyDataSetChanged();
	}

	@Override
	public MDKAdapter<?, ?, ?> getAdapter() {
		return this.mAdapter;
	}
}
