package com.adeuza.movalysfwk.mobile.mf4android.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;

import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMTabbedFragment;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMWorkspaceListFragment.WorkspaceListHandler;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableExpandedListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPerformItemClickEventData;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceMasterDetailFragmentLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceMasterDetailAdapter;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener.ListenerOnDataLoaderReloadEvent;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;

/**
 * <p>Allows to build an activity with a workspace AbstractWorkspaceAutoBindMMActivity</p>
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 * @author smaitre
 */
public abstract class AbstractWorkspaceMasterDetailMMFragmentActivity extends AbstractBaseWorkspaceMMFragmentActivity<MMWorkspaceMasterDetailFragmentLayout> implements OnTabChangeListener {

	/** Describes the save status of an instance of the class */
	public static enum WorkspaceSaveState {
		/** there is no detail to save */
		NO_DETAIL,
		/** a detail was created / modified and can be saved */
		DETAIL
	}

	/** Used to determine the clicked item */
	protected static final String FROM_LISTITEM = "fromItem";

	/** Page to got to */
	private int goToPage = -1;

	/** Selected item id */
	private String selectedItemId;

	/** Selected item tag */
	private String sTabSelectedTag;

	/** Indicates if the data int the tabs should be restored */
	private boolean restoreTabData;

	/** Workspace status */
	private WorkspaceSaveState status = WorkspaceSaveState.NO_DETAIL;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected MMWorkspaceAdapter createWorkspaceAdapter() {
		return new MMWorkspaceMasterDetailAdapter(
				getSupportFragmentManager(), 
				this,
				ConfigurationsHandler.getInstance().getManagementConfiguration(this.getWlayout().getModel())
				);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnCreate(Bundle p_oSavedInstanceState) {
		//this.getWadapter().resetSelectedItem();
	}

	/**
	 * called when selected item changes 
	 * @param p_oHandler workspace list handler
	 * @param p_oEvent business event sent
	 */
	public void doOnMasterListChangeSelectedItem(final WorkspaceListHandler p_oHandler, final BusinessEvent<MMPerformItemClickEventData> p_oEvent) {

		if ( p_oHandler != null ) {
			// getData() is null in creation mode
			final String sNextItemId;
			if (p_oEvent.getData() != null) {
				p_oEvent.getData().performItemClick();
				if (p_oEvent.getData().getView().getTag() instanceof ConfigurableExpandedListViewHolder) {
					// si on est sur un élément group ou title (list 2d et 3d), on ne doit pas naviguer vers un détail
					return;
				}
				sNextItemId = p_oEvent.getData().getTag();
			} else {
				sNextItemId = null;
			}
			
			// sNextItem is null when create action
			// sNextItem is not null when change selected item (must not be the same)
			if (this.isViewModelModified() && (sNextItemId == null || (sNextItemId != null && !sNextItemId.equals(this.selectedItemId)))) {
				this.doOnViewModelModified(
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p_oDialog, int p_iId) {
								p_oDialog.cancel();
								if ( p_oEvent.getData() != null ) {
									p_oEvent.getData().performItemClick();
								}						
								AbstractWorkspaceMasterDetailMMFragmentActivity.this.selectedItemId = sNextItemId;
								AbstractWorkspaceMasterDetailMMFragmentActivity.this.doOnKeepWorkspaceModifications(
										(View) p_oHandler.getView());
							}
						}, 

						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p_oDialog, int p_iId) {
								p_oDialog.cancel();
								if ( p_oEvent.getData() != null ) {
									p_oEvent.getData().performItemClick();								
								}								
								AbstractWorkspaceMasterDetailMMFragmentActivity.this.selectedItemId = sNextItemId;
								AbstractWorkspaceMasterDetailMMFragmentActivity.this.doDisplayDetail();
							}
						},
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p_oDialog, int p_iId) {
								p_oDialog.cancel();
							}
						});
			} else {
				if(sNextItemId != null && sNextItemId.equals(this.selectedItemId)) {
					//Edit de l'item en cours
					this.goToPage = 1;
					this.doChangePage();
				}
				else {
					//Affichage d'un item différent
					this.selectedItemId = sNextItemId;
					this.doDisplayDetail();
				}
				
				if (p_oEvent.getData() != null ) {
					p_oEvent.getData().performItemClick();
				}
			}
		}
	}

	/**
	 * triggered on detail reload
	 * @param p_oEvent event sent 
	 */
	public void doOnReloadDetail(ListenerOnDataLoaderReloadEvent<?> p_oEvent) {
		//		this.inflateLayout();
		if (this.goToPage != -1) {
			this.getWlayout().unHideDetailColumns(true);
			//on est sur la liste donc on va à la page 1 : il faut changer le workspace de page
			this.doChangePage();
			// [Mantis 21169] solve return to list on rotate
			// don't know why we have set goToPage to -1...
			this.goToPage = -1;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PageTransformer createPageTransformer() {
		// return null by default
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBackPressed() {
		if (this.getWlayout().getCurrentItem() == 0) {
			super.onBackPressed();
		}
		else {
			this.goToPage = 0;
			this.getWlayout().setCurrentItem(this.goToPage);
		}
	}


	/**
	 * Triggered when save action succeeded
	 * @param p_oEvent event sent
	 */
	public void doOnSaveWorkspaceSuccess(ListenerOnActionSuccessEvent p_oEvent) {
		final Object oSource = p_oEvent.getSource();
		if (oSource != null && String.class.isAssignableFrom(oSource.getClass())) {
			if ( FROM_BACK.equals(oSource)) {
				this.exit();
			}
			else if ( FROM_LISTITEM.equals(oSource)) {
				this.doFillAction();
				this.doDisplayDetail();
			}
			else {
				this.goToPage = 0;
				this.doChangePage();
			}
		}			
		else {
			this.goToPage = 0;
			this.doChangePage();
		}
	}

	/**
	 * Performs the display of the detail 
	 */
	protected void doDisplayDetail() {
		this.goToPage = 1;
		this.status = WorkspaceSaveState.DETAIL;
		doRefreshDetail();
	}

	/**
	 * Refreshes the display of the detail
	 */
	protected void doRefreshDetail() {
		this.doDisplayDetail(this.selectedItemId);
	}

	/**
	 * Triggered when a delete was successfully processed on a detail
	 */
	public void doOnDetailDeleteSuccess() {
		this.goToPage = 0;
		this.status = WorkspaceSaveState.NO_DETAIL;
		this.doChangePage();
		this.getWlayout().hideAllColumns(true);
		doRefreshDetail();
	}
	
	/**
	 * Triggered when a save was successfully processed on a detail
	 */
	public void doOnDetailSaveSuccess() {
		this.goToPage = 0;
		this.doChangePage();
	}
	
	/**
	 * Displays the detail of the given item id
	 * @param p_sItemId the item id to display
	 */
	protected abstract void doDisplayDetail(String p_sItemId);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSaveInstanceState(Bundle p_oSavedInstanceState) {
		p_oSavedInstanceState.putInt("goToPage", this.goToPage);
		p_oSavedInstanceState.putString("selectedItemId", this.selectedItemId);
		p_oSavedInstanceState.putString("tabSelectedTag", this.sTabSelectedTag);
		p_oSavedInstanceState.putInt("status", this.status.ordinal());

		super.onSaveInstanceState(p_oSavedInstanceState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onRestoreInstanceState(Bundle p_oSavedInstanceState) {
		super.onRestoreInstanceState(p_oSavedInstanceState);

		this.goToPage = p_oSavedInstanceState.getInt("goToPage");
		this.selectedItemId = p_oSavedInstanceState.getString("selectedItemId");
		this.sTabSelectedTag = p_oSavedInstanceState.getString("tabSelectedTag");
		this.status = WorkspaceSaveState.values()[p_oSavedInstanceState.getInt("status")];
		this.restoreTabData = true;

		if (this.selectedItemId != null) {
			this.getWlayout().unHideAllColumns(false);
			if (this.goToPage != -1) {
				this.doChangePage();
			}
		}

	}

	/**
	 * Called to change the displayed page
	 */
	private void doChangePage() {
		Application app = Application.getInstance();
		if (app != null && this.getWadapter().getCount() / app.getScreenColumnNumber() <= 1 ) {
			this.getWlayout().setCurrentItem(0);
		} else {
			this.getWlayout().setCurrentItem(this.goToPage);
		}
	}

	/**
	 * Triggered when a master element is created
	 * @param p_oFragment the fragment of the master element
	 */
	public void doOnMasterCreated(AbstractMMTabbedFragment p_oFragment) {
		if (this.restoreTabData) {
			p_oFragment.setTabSelected(this.sTabSelectedTag);
			this.restoreTabData = false;
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	@Override
	public void onTabChanged(String p_sTabTag) {
		// Can be overriden
		this.sTabSelectedTag = p_sTabTag;
	}

	/**
	 * Returns the class instance save status
	 * @return the save status
	 */
	public WorkspaceSaveState getSaveStatus() {
		return status;
	}

	/**
	 * Sets the save status of the instance
	 * @param p_oStatus the new save status
	 */
	public void setSaveStatus(WorkspaceSaveState p_oStatus) {
		this.status = p_oStatus;
	}


}
