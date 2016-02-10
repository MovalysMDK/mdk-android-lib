package com.adeuza.movalysfwk.mobile.mf4android.activity;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceDetailLayout;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener.ListenerOnDataLoaderReloadEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;

/**
 * @author lmichenaud
 * @deprecated Since Cotopaxi, replaced by {@link AbstractWorkspaceDetailMMFragmentActivity}
 */
public abstract class AbstractWorkspaceDetailMMActivity extends AbstractBaseWorkspaceMMActivity<MMWorkspaceDetailLayout> {
	 		
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractBaseWorkspaceAutoBindMMActivity#doAfterSetContentView(com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMBaseWorkspaceLayout)
	 */
	@Override
	protected void doAfterSetContentView( MMWorkspaceDetailLayout p_oWorkspaceLayout ) {
		
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
