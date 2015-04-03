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

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceDetailFragmentLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceDetailAdapter;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener.ListenerOnDataLoaderReloadEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;

/**
 * Detail fragment for a workspace activity
 */
public abstract class AbstractWorkspaceDetailMMFragmentActivity extends AbstractBaseWorkspaceMMFragmentActivity<MMWorkspaceDetailFragmentLayout> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected MMWorkspaceAdapter createWorkspaceAdapter() {
		return new MMWorkspaceDetailAdapter(
						getSupportFragmentManager(), 
						this,
						ConfigurationsHandler.getInstance().getManagementConfiguration(this.getWlayout().getComponentFwkDelegate().getModel())
					);
	}
	
	/**
	 * Triggered when details are being reloaded
	 * @param p_oEvent the event received
	 */
	public void doOnReloadDetail(ListenerOnDataLoaderReloadEvent<?> p_oEvent) {
		this.inflateLayout();
	}
		
	/**
	 * Triggered when the workspace was successfully saved
	 * @param p_oEvent the event received
	 */
	@SuppressWarnings("rawtypes")
	public void doOnSaveWorkspaceSuccess(ListenerOnActionSuccessEvent p_oEvent) {
		this.exit();
	}
}
