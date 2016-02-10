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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;

/**
 * Workspace Adatper class
 * @author abelliard
 *
 */
public class MMWorkspaceDetailAdapter extends MMWorkspaceAdapter {

	/** serial id */
	private static final long serialVersionUID = -840309277791241554L;
	
	/** the tab configuration */
	private VMTabConfiguration oTabConfiguration;
	
	/**
	 * Constructor
	 * @param fm the fragment manager
	 */
	public MMWorkspaceDetailAdapter(FragmentManager fm, AbstractBaseWorkspaceMMFragmentActivity<?> activity) {
		super(fm, activity);
	}

	/**
	 * Constructor
	 * @param fm the fragment manager
	 * @param wConfig the workspace configuration
	 */
	public MMWorkspaceDetailAdapter(
			FragmentManager fm, AbstractBaseWorkspaceMMFragmentActivity<?> activity, ManagementConfiguration wConfig) {
		
		super(fm, activity);
		
		// instance of columns
		int iColumnNum = 0;
		for (ManagementGroup oGroup : wConfig.getVisibleGroups()) {
			MMWorkspaceColumn oWorksapceColumn = null;
			
			oWorksapceColumn = this.computeDetailColumn(oGroup);
			
			this.mapColumns.put(oWorksapceColumn.hashCode(), oWorksapceColumn);
			this.columns.add(oWorksapceColumn);
			
			iColumnNum++;
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
