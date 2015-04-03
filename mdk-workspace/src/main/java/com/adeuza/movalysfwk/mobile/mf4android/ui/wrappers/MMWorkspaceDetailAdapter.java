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

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractBaseWorkspaceMMFragmentActivity;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.MMMultiSectionFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;

/**
 * Workspace Adatper class
 *
 */
public class MMWorkspaceDetailAdapter extends MMWorkspaceAdapter {
	
	/**
	 * Constructor
	 * @param p_oFm the fragment manager
	 * @param p_oActivity the framework activity
	 */
	public MMWorkspaceDetailAdapter(FragmentManager p_oFm, AbstractBaseWorkspaceMMFragmentActivity<?> p_oActivity) {
		super(p_oFm, p_oActivity);
	}

	/**
	 * Constructor
	 * @param p_oFm the fragment manager
	 * @param p_oActivity the framework activity
	 * @param p_oWConfig the workspace configuration
	 */
	public MMWorkspaceDetailAdapter(
			FragmentManager p_oFm, AbstractBaseWorkspaceMMFragmentActivity<?> p_oActivity, ManagementConfiguration p_oWConfig) {
		
		super(p_oFm, p_oActivity);
		
		// instance of columns
		for (ManagementGroup oGroup : p_oWConfig.getVisibleGroups()) {
			MMWorkspaceColumn oWorksapceColumn = null;
			
			oWorksapceColumn = this.computeDetailColumn(oGroup);
			
			this.mapColumns.put(oWorksapceColumn.hashCode(), oWorksapceColumn);
			this.columns.add(oWorksapceColumn);
		}
		
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
	
}
