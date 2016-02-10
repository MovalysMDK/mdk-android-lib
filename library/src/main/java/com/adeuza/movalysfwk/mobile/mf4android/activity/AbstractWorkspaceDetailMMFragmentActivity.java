package com.adeuza.movalysfwk.mobile.mf4android.activity;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceDetailFragmentLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceDetailAdapter;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener.ListenerOnDataLoaderReloadEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;

/**
 * @author lmichenaud
 *
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
						ConfigurationsHandler.getInstance().getManagementConfiguration(this.getWlayout().getModel())
					);
	}
	
	/**
	 * @param p_oEvent
	 */
	public void doOnReloadDetail(ListenerOnDataLoaderReloadEvent<?> p_oEvent) {
		this.inflateLayout();
	}
		
	/**
	 * @param p_oEvent
	 */
	public void doOnSaveWorkspaceSuccess(ListenerOnActionSuccessEvent p_oEvent) {
		this.exit();
	}
}
