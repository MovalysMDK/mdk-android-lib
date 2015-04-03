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
package com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractBaseWorkspaceMMFragmentActivity;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractAutoBindMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractInflateMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMTabbedFragment;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.MMMultiSectionFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;

/**
 * Workspace Adatper class
 *
 */
public class MMWorkspaceMasterDetailAdapter extends MMWorkspaceAdapter {

	/** logging tag */
	private static final String TAG = "MMWorkspaceMasterDetailAdapter";
	
	/**
	 * Constructor
	 * @param p_oFm the fragment manager
	 * @param p_oActivity the framework activity
	 */
	public MMWorkspaceMasterDetailAdapter(FragmentManager p_oFm, AbstractBaseWorkspaceMMFragmentActivity<?> p_oActivity) {
		super(p_oFm, p_oActivity);
	}

	/**
	 * Constructor
	 * @param p_oFm the fragment manager
	 * @param p_oActivity the framework activity
	 * @param p_oWConfig the workspace configuration
	 */
	public MMWorkspaceMasterDetailAdapter(FragmentManager p_oFm, AbstractBaseWorkspaceMMFragmentActivity<?> p_oActivity, ManagementConfiguration p_oWConfig) {
		
		super(p_oFm, p_oActivity);
		
		// instance of columns
		int iColumnNum = 0;
		for (ManagementGroup oGroup : p_oWConfig.getVisibleGroups()) {
			MMWorkspaceColumn oWorksapceColumn = null;
			if (iColumnNum == 0) {
				oWorksapceColumn = this.computeMasterColumn(oGroup);
			} else {
				oWorksapceColumn = this.computeDetailColumn(oGroup);
			}
			
			this.addColumns(oGroup, oWorksapceColumn);
			
			iColumnNum++;
		}
		
	}

	/**
	 * Method to compute the master column
	 * @param p_oGroup the ManagementGroup to use for this column
	 * @return the Workspace Column
	 */
	private MMWorkspaceColumn computeMasterColumn(ManagementGroup p_oGroup) {
		MMWorkspaceColumn r_oMMWorkspaceColumn = this.getColumnByGroup(p_oGroup);
		if (r_oMMWorkspaceColumn == null) {
			
			r_oMMWorkspaceColumn = new MMWorkspaceColumn();
			r_oMMWorkspaceColumn.setList(true);
			
			
			if (p_oGroup.getVisibleZones().get(0).getVisibleZones().size() == 0) {
				ManagementZone oMainWorkspaceZone = p_oGroup.getVisibleZones().get(0);
				
				try {
					Class<? extends AbstractInflateMMFragment> cFragmentClass = (Class<? extends AbstractInflateMMFragment>) Class.forName(oMainWorkspaceZone.getSource());
					Bundle args = new Bundle();
					args.putSerializable(AbstractAutoBindMMFragment.ARG_ZONE, oMainWorkspaceZone);
					r_oMMWorkspaceColumn.setFragmentClass(cFragmentClass, args );
				} catch (ClassNotFoundException e) {
					Log.e(TAG, "Fragment Class not found : "+oMainWorkspaceZone.getSource());
				}
				
			} else {
				// there are tabs in master
				ManagementZone oMainWorkspaceZone = p_oGroup.getVisibleZones().get(0);
				
				try {
					Class<? extends AbstractInflateMMFragment> cFragmentClass = (Class<? extends AbstractInflateMMFragment>) Class.forName(oMainWorkspaceZone.getSource());
					Bundle args = new Bundle();
					args.putSerializable(AbstractMMTabbedFragment.ARG_ZONES, oMainWorkspaceZone);
//					args.putSerializable(AbstractMMTabbedFragment.TAB_CONFIGURATION, this.oTabConfiguration);
					r_oMMWorkspaceColumn.setFragmentClass(cFragmentClass, args );
				} catch (ClassNotFoundException e) {
					Log.e(TAG, "Fragment Class not found : "+oMainWorkspaceZone.getSource());
				}
			}
			
		}
		
		return r_oMMWorkspaceColumn;
	}
	
	/**
	 * set the tab configuration if needed
	 * @param p_oTC the tab configuration
	 */
	public void setTabConfiguration(VMTabConfiguration p_oTC) {
		AbstractMMTabbedFragment tabbedFragment = this.getDefaultTabbedFragment();
		if (tabbedFragment != null) {
			tabbedFragment.applyTabConfiguration(p_oTC);
		}
	}
	
	/**
	 * get the default tabbed fragment
	 * @return the default tabbed fragment if exist or null
	 */
	private AbstractMMTabbedFragment getDefaultTabbedFragment() {
		for( int i = 0 ; i < this.mActiveFragments.size(); i++ ) {
			if ( this.mActiveFragments.valueAt(i) instanceof AbstractMMTabbedFragment) {
				return (AbstractMMTabbedFragment) this.mActiveFragments.valueAt(i);
			}
		}
		return null;
	}
	
	/**
	 * Compute detail column
	 * @param p_oGroup the ManagementGroup to use for this column
	 * @return the Workspace Column
	 */
	private MMWorkspaceColumn computeDetailColumn(ManagementGroup p_oGroup) {
		MMWorkspaceColumn r_oMMWorkspaceColumn = this.getColumnByGroup(p_oGroup);
		if (r_oMMWorkspaceColumn == null) {
			
			// Change implementation
			r_oMMWorkspaceColumn = new MMWorkspaceColumn();
			r_oMMWorkspaceColumn.setList(false);
			
			List<ManagementZone> oMainWorkspaceZones = p_oGroup.getVisibleZones();
			MMMultiSectionFragment oFragmentColumn = new MMMultiSectionFragment();
			Bundle args = new Bundle();
			args.putSerializable(MMMultiSectionFragment.ARG_ZONES, new ArrayList<>(oMainWorkspaceZones));
			oFragmentColumn.setArguments(args);
			r_oMMWorkspaceColumn.setFragmentClass(MMMultiSectionFragment.class, args);
			
			
		}
		
		return r_oMMWorkspaceColumn;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hideAllColumns() {
		this.shownColumns.removeAll(this.columns.subList(1, this.columns.size()));
		this.bWidthChanged = didWidthChange();
		this.notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unHideAllColumns() {
		this.shownColumns.removeAll(this.columns.subList(1, this.columns.size()));
		this.shownColumns.addAll(this.columns.subList(1, this.columns.size()));
		this.bWidthChanged = didWidthChange();
		this.notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hideColumn(int p_iColumnIndex) {
		if (p_iColumnIndex != 0) {
			super.hideColumn(p_iColumnIndex);
			this.bWidthChanged = didWidthChange();
			this.notifyDataSetChanged();
		} else {
			Log.w(TAG, "cannot hide master column on Workspace Master/Detail");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unhideColumn(int p_iColumnIndex) {
		if (p_iColumnIndex != 0) {
			super.unhideColumn(p_iColumnIndex);
			this.bWidthChanged = didWidthChange();
			this.notifyDataSetChanged();
		} else {
			Log.w(TAG, "cannot unhide master column on Workspace Master/Detail");
		}
	}
	
}
