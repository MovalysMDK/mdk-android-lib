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
package com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.fixedlist;

import java.lang.reflect.InvocationTargetException;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevelTreatment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolderImpl;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.FixedListAdapterDetail;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.FixedListAdapterView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

public class MDKFixedListConnector<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>>
        extends RecyclerView.Adapter<ConfigurableListViewHolderImpl> implements MDKViewConnector {

	private MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> mAdapter ;
	
	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	@SuppressWarnings("rawtypes")
	public enum Level implements AdapterLevel {
		/** View level */
		VIEW(FixedListAdapterView.class),
		/** Detail level */
		DETAIL(FixedListAdapterDetail.class);
		
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
	
	@SuppressWarnings("serial")
	private DataSetObserver mObserver = new DataSetObserver() {
		
		@Override
		public void onChanged() {
			MDKFixedListConnector.this.notifyDataSetChanged();
		}
		
		@Override
		public void onInvalidated() {
			notifyItemRangeRemoved(0, getItemCount());
		}
	};
	
    public MDKFixedListConnector(MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter) {
    	this.mAdapter = p_oAdapter;
    	this.mAdapter.getMasterVM().registerObserver(this.mObserver);
    }

	public ITEMVM getSelectedItem() {
		return this.mAdapter.getMasterVM().getCacheVMById(this.mAdapter.getSelectedItem());
	}
    
	public void setSelectedItem(ITEMVM p_oViewModel) {
		this.mAdapter.setSelectedItem(p_oViewModel.getIdVM());
	}
	
    public void resetSelectedItem() {
    	this.mAdapter.resetSelectedItem();
    }

    public int getSelectedPosition() {
    	if (this.mAdapter.getMasterVM().getCacheVMById(this.mAdapter.getSelectedItem()) != null) {
    		return this.mAdapter.getMasterVM().indexOf(this.mAdapter.getMasterVM().getCacheVMById(this.mAdapter.getSelectedItem()));
    	} else {
    		return -1;
    	}
    }
    
    public void setSelectedPosition(int selectedPosition) {
    	if (selectedPosition > -1 && selectedPosition <= this.mAdapter.getMasterVM().getCount()) {
    		this.mAdapter.setSelectedItem(this.mAdapter.getMasterVM().getCacheVMByPosition(selectedPosition).getIdVM());
    	} else {
    		this.mAdapter.setSelectedItem("");
    	}
    }
    
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
    public MDKFixedListAdapter<ITEM, ITEMVM, LISTVM> getAdapter() {
        return this.mAdapter;
    }

    public Class<ITEMVM> getItemVMInterface() {
        return this.mAdapter.getMasterVM().getItemVmInterface();
    }

    public int getConfigurableLayout(ITEMVM p_oViewModel) {
        return this.mAdapter.getConfigurableLayout();
    }

    public void add(ITEMVM p_oItemVM) {
    	this.mAdapter.getMasterVM().add(p_oItemVM);
    }

    public void remove(ITEMVM p_oItemVM) {
    	this.mAdapter.getMasterVM().remove(p_oItemVM);
    }

    public LISTVM getMasterVM() {
    	return this.mAdapter.getMasterVM();
    }
    
    public void setMasterVM(LISTVM p_oListVM) {
    	this.mAdapter.updateAdapter(p_oListVM);
    }

	@Override
	public long getItemId(int p_iPosition) {
		return this.mAdapter.getItemId(p_iPosition);
	}

	@Override
	public ConfigurableListViewHolderImpl onCreateViewHolder(ViewGroup p_oViewGroup, int p_iViewType) {
		return (ConfigurableListViewHolderImpl) mAdapter.onCreateViewHolder(Level.VIEW, false, p_oViewGroup, p_iViewType);
	}

	@Override
	public void onBindViewHolder(ConfigurableListViewHolderImpl p_oViewholder, int p_iPositions) {
		mAdapter.onBindViewHolder(Level.VIEW, false, p_oViewholder, p_iPositions);
	}

	@Override
	public int getItemCount() {
		return this.mAdapter.getCount();
	}
}
