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
package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import java.io.Serializable;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;

/**
 * <p>crée un fragment conteneur pour les sections</p>
 * <p>cette class est utilisable uniquement sur les FragmentActivity</p>
 *
 */
public class MMMultiSectionFragment extends AbstractMMFragment implements Serializable {


	
	/**
	 * class serial id
	 */
	private static final long serialVersionUID = -4859406586656993551L;
	
	/**
	 * Log tag
	 */
	private static final String TAG = "MMMultiSectionFragment";
	
	/** multisection scroll container */
	private static final String MULTI_SECTION_FRAGMENT_SCROLL = "multi_section_fragment_scroll";
	/** multisection container */
	private static final String MULTI_SECTION_FRAGMENT_CONTAINER = "multi_section_fragment_container";

	/** argument to retriv zones */
	public static final String ARG_ZONES = "zones";
	
	/**
	 * the root view (ScrollView)
	 */
	private ScrollView root;
	/**
	 * List of zones
	 */
	private List<ManagementZone> zones;

	/** true if the view was rotated. */
	private boolean isRotated = false;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		
		Bundle args = getArguments();
		if (args!=null) {
			this.zones = (List<ManagementZone>) args.getSerializable(ARG_ZONES);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer,
			Bundle p_oSavedInstanceState) {
		
		this.isRotated = (p_oSavedInstanceState != null && p_oSavedInstanceState.getBoolean("ROTATED"));
		
		if (this.root != null) {
			p_oContainer.removeAllViews();
		}
		
		// init the scroll view
		this.root = new ScrollView(this.getActivity());
		this.application.computeId(this.root, MULTI_SECTION_FRAGMENT_CONTAINER + this.root.hashCode());
		// init the container view
		LinearLayout oSection = new LinearLayout(this.getActivity());
		this.application.computeId(oSection, MULTI_SECTION_FRAGMENT_SCROLL + this.zones.hashCode());
		
		LinearLayout.LayoutParams oParamsLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		oSection.setLayoutParams(oParamsLayout);
		oSection.setOrientation(LinearLayout.VERTICAL);
		this.root.addView(oSection);
		
		if (!this.isRotated) {
			// begin the fragment transaction
			FragmentTransaction ftx = this.getChildFragmentManager().beginTransaction();
			for (ManagementZone oZone : this.zones) {
				// for each zone create the fragment
				try {
					@SuppressWarnings("unchecked")
					Class<? extends AbstractMMFragment> fragClass = (Class<? extends AbstractMMFragment>) Class.forName(oZone.getSource());
					
					Bundle args = new Bundle();
					args.putSerializable(AbstractAutoBindMMFragment.ARG_ZONE, oZone);
					AbstractMMFragment oSectionFragment = (AbstractMMFragment) AbstractMMFragment.instantiate(getActivity(), fragClass.getName(), args);
					
					// if the fragment is already in the manager replace it
					if( this.getChildFragmentManager().findFragmentByTag( String.valueOf(oZone.hashCode()) ) != null ) {
						ftx.replace(oSection.getId(), oSectionFragment,String.valueOf(oZone.hashCode()));
					} else {
						ftx.add(oSection.getId(), oSectionFragment, String.valueOf(oZone.hashCode()));
					}
					
				} catch (ClassNotFoundException e) {
					Log.e(TAG, "Fragment Class not found : "+oZone.getSource());
				}
			}
			ftx.commitAllowingStateLoss();
		}
		
		return root;
	}
	
	@Override
	public void onSaveInstanceState(Bundle p_oOutState) {
		p_oOutState.putBoolean("ROTATED", true);
	}
	
}
