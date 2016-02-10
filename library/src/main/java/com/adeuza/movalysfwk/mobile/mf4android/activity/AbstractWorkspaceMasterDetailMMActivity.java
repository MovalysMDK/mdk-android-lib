package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;

import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.GenericUpdateVMForDisplayDetailAction;
import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.InUpdateVMParameter;
import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.OutUpdateVMParameter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMAdaptableListView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceMasterDetailLayout;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener.ListenerOnDataLoaderReloadEvent;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccess;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.resetdatabase.ResetDataBaseAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfigHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;

/**
 * <p>Permet de construire une activité utilisant un workspace AbstractWorkspaceAutoBindMMActivity</p>
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 * @author smaitre
 * @deprecated Replaced by {@link AbstractWorkspaceMasterDetailMMFragmentActivity}
 */
public abstract class AbstractWorkspaceMasterDetailMMActivity extends AbstractBaseWorkspaceMMActivity<MMWorkspaceMasterDetailLayout> implements OnTabChangeListener {

	private static final String TAG = "AbstractWorkspaceMasterDetailMMActivity";

	/**
	 * Utilisé lorsque l'utilisateur a cliqué sur l'élément d'une liste.
	 */
	protected static final String FROM_LISTITEM = "fromItem";

	/**
	 * Page visée
	 */
	private int goToPage = -1;

	/**
	 * List des handlers 
	 */
	private List<WorkspaceListHandler> workspaceListHandlers ;

	/**
	 * Current list view
	 */
	private MMAdaptableListView currentListView ;
	
	/**
	 * Selected item id
	 */
	private String selectedItemId;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#doAfterSetContentView()
	 */
	@Override
	protected void doAfterSetContentView( MMWorkspaceMasterDetailLayout p_oWorkspaceLayout ) {
		p_oWorkspaceLayout.doAfterSetContentView();
		p_oWorkspaceLayout.getTabDelegate().setOnTabChangedListener(this);
		
		this.workspaceListHandlers = this.createListHandlers();
		for( WorkspaceListHandler oWorkspaceListHandler : this.workspaceListHandlers) {
			MMAdaptableListView oListView = (MMAdaptableListView) this.findViewById(oWorkspaceListHandler.getResourceId());
			oWorkspaceListHandler.setView(oListView);
			if ( this.currentListView == null ) {
				this.currentListView = oListView ;
			}
		}
	}
	
	/**
	 * Crée les handlers 
	 * @return
	 */
	protected abstract List<WorkspaceListHandler> createListHandlers();
			
	/**
	 * 
	 */

	@Override
	public void doOnCreate(Bundle p_oSavedInstanceState) {
		
		for( final WorkspaceListHandler oListHandler : this.workspaceListHandlers) {
			((AbstractConfigurableListAdapter) oListHandler.getAdapter()).resetSelectedItem();
			this.doOnMasterListChangeSelectedItem(new BusinessEvent<String>() {
				/**
				 * {@inheritDoc}
				 */
				@Override
				public Object getSource() {
					return oListHandler.getAdapter();
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public String getData() {
					return null;
				}
				@Override
				public boolean isExitMode() {
					return false;
				}

				@Override
				public void setExitMode(boolean p_bExitMode) {
					//do nothing
				}

				@Override
				public boolean isConsummed() {
					return false;
				}

				@Override
				public void setConsummed(boolean p_bConsummed) {
					//do nothing
				}
			});
		}
	}
	
	/**
	 * @param p_oEvent
	 */
	public void doOnMasterListChangeSelectedItem(BusinessEvent<String> p_oEvent) {
		final WorkspaceListHandler oListHandler = this.getListHandlerByAdapter(p_oEvent.getSource());
		if ( oListHandler != null ) {
			final String sNextItemId = p_oEvent.getData();

			if (this.isViewModelModified()) {
				this.doOnViewModelModified(
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface p_oDialog, int p_iId) {
							p_oDialog.cancel();
							AbstractWorkspaceMasterDetailMMActivity.this.selectedItemId = sNextItemId;
							AbstractWorkspaceMasterDetailMMActivity.this.doOnKeepWorkspaceModifications(
								(View) oListHandler.getView());
						}
					}, 

					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface p_oDialog, int p_iId) {
							p_oDialog.cancel();
							AbstractWorkspaceMasterDetailMMActivity.this.selectedItemId = sNextItemId;
							AbstractWorkspaceMasterDetailMMActivity.this.doDisplayDetail();
						}
					},
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface p_oDialog, int p_iId) {
							p_oDialog.cancel();
						}
					});
			}
			else {
				this.selectedItemId = p_oEvent.getData();
				this.doDisplayDetail();
			}
		}
	}

	/**
	 * @return
	 */
	public WorkspaceListHandler getListHandlerByAdapter(Object p_oAdapter) {
		WorkspaceListHandler r_oWorkspaceListHandler = null ;
		for( WorkspaceListHandler oWorkspaceListHandler : this.workspaceListHandlers) {
			if (oWorkspaceListHandler.getAdapter() == p_oAdapter) {
				r_oWorkspaceListHandler = oWorkspaceListHandler ;
				break ;
			}
		}
		return r_oWorkspaceListHandler ;
	}

	/**
	 * @param p_oEvent
	 */
	public void doOnReloadDetail(ListenerOnDataLoaderReloadEvent<?> p_oEvent) {
		this.inflateLayout();
		if (this.goToPage != -1) {
			//on n'est sur la liste donc on va à la page 1 : il faut changer le workspace de page
			this.getWlayout().goToColumn(this.goToPage);
			this.goToPage = -1;
		}
	}

	/**
	 * Workspace: affichage de la liste si ce n'�tait pas le cas, sinon appel au super.
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#doOnViewModelNotModified()
	 */
	@Override
	public void exit() {
		if (this.getWlayout().isFirstScreenSelected() ) {
			super.exit();
		}
		else {
			// this.doFillAction();
			this.goToPage = 0;
			this.getWlayout().goToColumn(this.goToPage);
			this.doRefreshDetail();
		}
	}


	/**
	 * Nothing to do
	 * @param p_oSource clicked view
	 */
	@Override
	protected abstract void doOnKeepWorkspaceModifications(View p_oSource);

	/**
	 * @param p_oEvent
	 */
	public void doOnSaveWorkspaceSuccess(ListenerOnActionSuccessEvent p_oEvent) {
		final Object oSource = p_oEvent.getSource();
		if (oSource != null && String.class.isAssignableFrom(oSource.getClass())) {
			if ( FROM_BACK.equals(oSource)) {
//				this.doFillAction();
				this.exit();
			}
			else if ( FROM_LISTITEM.equals(oSource)) {
				this.doFillAction();
				this.doRefreshDetail();
			}
		}
	}

	/**
	 * Lance l'affiche du détail et positionne l'utilisateur sur celui-ci.
	 */
	protected void doDisplayDetail() {
		this.goToPage = 1;
		doRefreshDetail();
	}

	/**
	 * Lance le rafraîchissement du détail du workspace (pas de changement de page).
	 */
	public void doRefreshDetail() {
		this.doDisplayDetail(this.selectedItemId);
	}

	/**
	 * @param p_sItemId
	 */
	public final void displayDetail(String p_sItemId) {
		this.goToPage = 1;
		this.selectedItemId = p_sItemId;
		this.doDisplayDetail(p_sItemId);
	}

	/**
	 * @param p_sItemId
	 */
	protected abstract void doDisplayDetail(String p_sItemId);

	/**
	 * {@inheritDoc}
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle p_oSavedInstanceState) {
		super.onPostCreate(p_oSavedInstanceState);
		Object oConfig = this.getLastCustomNonConfigurationInstance();
		if (oConfig==null) {
			//le boolean true est nrcessaire pour construire le premier Workspace
			this.getWlayout().hideAllDetailColumn(true);
		}
		else {
			//Cas d'une rotation :
			//ce n'est pas la peine d'appeler l'action de remplissage des donnees
			// mais on doit remettre à jour la configuration des onglets
			this.updateTabConfig();
		}
	}

	/**
	 * return l'identifiant android du composant workspace
	 * @return l'identifiant android du composant workspace
	 */
	@Override
	public abstract int getWorkspaceId();
 		
	/**
	 * return la liste de vue courante
	 * @return la liste de vue courante
	 */
	protected MMAdaptableListView getCurrentListView() {
		return this.currentListView;
	}
	
	/**
	 * @return
	 */
	protected MMAdaptableListView getListViewByPos( int p_iPos) {
		return this.workspaceListHandlers.get(p_iPos).getView();
	}
	
	
	/**
	 * @param p_iResId
	 * @return
	 */
	protected MMAdaptableListView getListViewByResId( int p_iResId ) {
		MMAdaptableListView r_oView = null ;
		for( WorkspaceListHandler oWorkspaceListHandler : this.workspaceListHandlers ) {
			if ( oWorkspaceListHandler.getResourceId() == p_iResId ) {
				r_oView = oWorkspaceListHandler.getView();
				break;
			}
		}
		return r_oView;
	}

	/**
	 * Mise à jour des onglets
	 * @param p_oEvent paramètre de la mise à jour du détail
	 */
	@Override
	@ListenerOnActionSuccess(action=GenericUpdateVMForDisplayDetailAction.class)
	public void reloadDetail(ListenerOnActionSuccessEvent<OutUpdateVMParameter> p_oEvent) {
		
		if (this.saveInstanceState!=null) {
			
			InUpdateVMParameter oActionParameter = (InUpdateVMParameter) p_oEvent.getActionResult().getRuleParameters().get("in");

			if (this.ClassNameDataLoader.equals(oActionParameter.getDataLoader().getName())){
				
				// fin de la mise à jour du détail
				this.superOnRestoreInstanceState();
				this.saveInstanceState = null;
				
				if (this.goToPage != -1) {
					this.doRefreshDetail();
				}
			}
		}
		
		this.updateTabConfig();
	}

	@Override
	protected void onSaveInstanceState(Bundle p_oSavedInstanceState) {
		p_oSavedInstanceState.putInt("goToPage", this.goToPage);
		p_oSavedInstanceState.putString("selectedItemId", this.selectedItemId);
		p_oSavedInstanceState.putString("SelectedTab", this.getWlayout().getTabDelegate().getSelectedTab());

		Log.v(TAG, "[onSaveInstanceState] goToPage [" + this.goToPage  + "] selectedItemId [" + this.selectedItemId + "] SelectedTab [" + this.getWlayout().getTabDelegate().getSelectedTab() + "]");
		super.onSaveInstanceState(p_oSavedInstanceState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle p_oSavedInstanceState) {
		super.onRestoreInstanceState(p_oSavedInstanceState);

		this.goToPage = p_oSavedInstanceState.getInt("goToPage");
		this.selectedItemId = p_oSavedInstanceState.getString("selectedItemId");
		this.getWlayout().getTabDelegate().setSelectedTab(p_oSavedInstanceState.getString("SelectedTab"));

		Log.v(TAG, "[onRestoreInstanceState] goToPage [" + this.goToPage  + "] selectedItemId [" + this.selectedItemId + "] SelectedTab [" + this.getWlayout().getTabDelegate().getSelectedTab() + "]");
	}
	
	
	/**
	 * Permet de cacher les colonnes détails à la fin du reset de la base de données
	 * @param p_oEvent paramètre de la suppression de la base de données
	 */
	@ListenerOnActionSuccess(action=ResetDataBaseAction.class)
	public void onResetDataBaseAction(ListenerOnActionSuccessEvent<NullActionParameterImpl> p_oEvent) {
		this.getWlayout().hideAllDetailColumn(true);		
	}
	
	/**
	 * Met à jour toutes les tabulations
	 */
	protected void updateTabConfig() {
		if ( this.getViewModel() instanceof VMTabConfigHolder ) {
			VMTabConfiguration oTabConfig = ((VMTabConfigHolder) this.getViewModel()).getVMTabConfiguration();
			if ( this.getWlayout().getTabDelegate().getTabCount() == oTabConfig.getTabCount()) {
				this.getWlayout().configurationSetValue(oTabConfig);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	@Override
	public void onTabChanged(String p_sTabTag) {
		// Can be override
	}
	
	/**
	 * @author lmichenaud
	 *
	 */
	public class WorkspaceListHandler {

		/**
		 * List view id
		 */
		private int resourceId ;

		/**
		 * Adapter
		 */
		private MMListAdapter adapter = null;

		/**
		 * List view
		 */
		private MMAdaptableListView view = null;

		/**
		 * @param p_iResourceId
		 */
		public WorkspaceListHandler(int p_iResourceId, MMListAdapter p_oAdapter) {
			this.resourceId = p_iResourceId ;
			this.adapter = p_oAdapter ;
		}

		/**
		 * @param p_oListView
		 */
		protected void setView(MMAdaptableListView p_oView) {
			this.view = p_oView;
			this.view.setListAdapter(this.adapter);
		}

		/**
		 * @return
		 */
		protected int getResourceId() {
			return this.resourceId;
		}

		/**
		 * @return
		 */
		protected MMListAdapter getAdapter() {
			return this.adapter;
		}
		
		/**
		 * @return
		 */
		protected MMAdaptableListView getView() {
			return this.view;
		}
	}
}
