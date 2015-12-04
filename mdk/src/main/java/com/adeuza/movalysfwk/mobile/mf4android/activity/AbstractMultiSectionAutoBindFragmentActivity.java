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
package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractAutoBindMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMultiSectionFragmentLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;

public abstract class AbstractMultiSectionAutoBindFragmentActivity extends AbstractAutoBindMMActivity {

	/**
	 * Wlayout parameter name
	 */
	private static final String ROOTCOMPONENT_PARAMETER = "wlayout";

	/**
	 * Log tag
	 */
	private static final String TAG = "AbstractMultiSectionAutoBindFragmentActivity";

	/**
	 * le workspace de la page
	 */
	private MMMultiSectionFragmentLayout sectionContainer ;

	/**
	 * la configuration associée au multi section container
	 */
	private ManagementConfiguration wconfiguration ;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#doAfterSetContentView()
	 */
	@Override
	protected void doAfterSetContentView() {
		super.doAfterSetContentView();

		//		this.application = (AndroidApplication)Application.getInstance();
		if ( this.sectionContainer == null ) {
			this.sectionContainer = (MMMultiSectionFragmentLayout) this.findViewById(this.getMultiSectionId());
		}

		//récupération de la configuration du workspace
		this.wconfiguration = ConfigurationsHandler.getInstance().getManagementConfiguration(this.sectionContainer.getComponentFwkDelegate().getModel());

		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction trx = fm.beginTransaction();

		for (ManagementGroup oGroup : this.wconfiguration.getVisibleGroups()) {

			for (ManagementZone oZone : oGroup.getVisibleZones()) {
				// add new fragment
				try {

					if (!TextUtils.isEmpty(oZone.getTag())) {
					
						// oZone.getSource() as to be the complete Name (package+className)
						@SuppressWarnings("unchecked")
						Class<? extends AbstractAutoBindMMFragment> oFragmentClass = (Class<? extends AbstractAutoBindMMFragment>) Class.forName(oZone.getSource());
	
						Fragment instanceFrag = fm.findFragmentByTag(String.valueOf(oZone.hashCode()));
						Bundle args = new Bundle();
						args.putSerializable(AbstractAutoBindMMFragment.ARG_ZONE, oZone);
	
						// if the fragment is hold by the fragment manager replace it
						int iContainerViewId = ((AndroidApplication) Application.getInstance()).getAndroidIdByStringKey(ApplicationRGroup.ID, oZone.getTag());
						// le replace entraine des problèmes de récupération des états dans list 2 et list 3
						if (instanceFrag == null) {
							trx.add(iContainerViewId, AbstractAutoBindMMFragment.instantiate(this, oFragmentClass.getName(), args), String.valueOf(oZone.hashCode()));
						}
					}
				} catch (ClassNotFoundException e) {
					Log.e(TAG, "Fragment Class not found : "+oZone.getSource());
				}
			}
		}

		trx.commitAllowingStateLoss();
	}

	public abstract int getMultiSectionId();

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#defineContentView()
	 */
	@Override
	protected void defineContentView() {
		Object oConfig = this.getLastCustomNonConfigurationInstance();
		if ( oConfig != null ) {
			// if configuration changed, use the saved wlayout component
			ViewGroup oRootView = (ViewGroup) ((Map<String, Object>) oConfig).get(ROOTCOMPONENT_PARAMETER);
			if(oRootView != null) {
				((ViewGroup) oRootView.getParent()).removeView(oRootView);
				this.sectionContainer = (MMMultiSectionFragmentLayout) oRootView.findViewById(this.getMultiSectionId());

				this.setContentView(oRootView);

				this.doAfterSetContentView();
			}
			else {
				super.defineContentView();
			}
		}
		else {
			super.defineContentView();
		}
	}
	
	/**
	 * Returns management configuration
	 * @return management configuration
	 */
	public ManagementConfiguration getManagementConfiguration() {
        return this.wconfiguration;
    }
    
	/**
	 * Returns section container
	 * @return section container
	 */
    public MMMultiSectionFragmentLayout getSectionContainer() {
        return this.sectionContainer;
    }
}
