package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost.OnTabChangeListener;

import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.GenericUpdateVMForDisplayDetailAction;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMasterRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceLayout;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener.ListenerOnDataLoaderReloadEvent;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccess;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.resetdatabase.ResetDataBaseAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.CustomizableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfigHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>Permet de construire une activité utilisant un workspace AbstractWorkspaceAutoBindMMActivity</p>
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 * @author smaitre
 */
@Deprecated
public abstract class AbstractWorkspaceAutoBindMMActivity extends AbstractAutoBindMMActivity implements OnTabChangeListener {

	/**
	 * Selected tab id
	 */
	//private static final String SELECTED_TAB_PARAMETER = "selected-tab";
	
	/**
	 * Wlayout parameter name
	 */
	private static final String ROOTCOMPONENT_PARAMETER = "wlayout";

	/**
	 * Utilisé lorsque l'utilisateur vient d'effectuer un back.
	 */
	protected static final String FROM_BACK = "fromBack";

	/**
	 * Utilisé lorsque l'utilisateur a cliqué sur l'élément d'une liste.
	 */
	protected static final String FROM_LISTITEM = "fromItem";

	/**
	 * le workspace de la page
	 */
	private MMWorkspaceLayout wlayout = null;
	
	/**
	 * la configuration associée au workspace
	 */
	private ManagementConfiguration wconfiguration = null;
	
	/**
	 * Application
	 */
	private AndroidApplication application=null;

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
	private ListView currentListView ;
	
	/**
	 * Selected item id
	 */
	private String selectedItemId;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#doAfterSetContentView()
	 */
	@Override
	protected void doAfterSetContentView() {
		super.doAfterSetContentView();
				
		this.application = (AndroidApplication)Application.getInstance();
		//récupération du composant workspace		
		if ( this.wlayout == null ) {
			this.wlayout = (MMWorkspaceLayout) this.findViewById(this.getWorkspaceId());
		}
		this.wlayout.doAfterSetContentView();
		this.wlayout.getTabDelegate().setOnTabChangedListener(this);
		
		this.workspaceListHandlers = this.createListHandlers();
		for( WorkspaceListHandler oWorkspaceListHandler : this.workspaceListHandlers) {
			ListView oListView = (ListView) this.findViewById(oWorkspaceListHandler.getListViewId());
			oListView.setAdapter( oWorkspaceListHandler.getAdapter());
			oWorkspaceListHandler.setListView(oListView);
			if ( this.currentListView == null ) {
				this.currentListView = oListView ;
			}
		}

		//récupération de la configuration du workspace
		this.wconfiguration = ConfigurationsHandler.getInstance().getManagementConfiguration(this.wlayout.getModel());
		
		//mise en place de l'auto mapping
		// 1 group = une page
		for(ManagementGroup oGroup : this.wconfiguration.getVisibleGroups()) {
			for(ManagementZone oZone : oGroup.getVisibleZones()) {
				this.configureAutoBinding(oZone, this.getViewModel());
			}
		}
	}
	
	/**
	 * @param p_oZone
	 * @param p_oVm
	 */
	private void configureAutoBinding( ManagementZone p_oZone, ViewModel p_oVm ) {
		int iSectionId = application.getAndroidIdByStringKey(ApplicationRGroup.ID, p_oZone.getSource());
		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iSectionId);
		if ( oZoneLayout == null ) {
			oZoneLayout = this.wlayout.getSectionLayoutByIdInHiddenColumns(iSectionId);
		}
	
		//récupération du view model associé
		try {
			
			if ( !p_oZone.hasChild()) {
				ViewModel oViewModel = null;
				try {
					Field oField = p_oVm.getClass().getField(p_oZone.getName());
					oViewModel = (ViewModel) oField.get(p_oVm);
				} catch (NoSuchFieldException e) {
					Method oMethod = p_oVm.getClass().getMethod(p_oZone.getName());
					oViewModel = (ViewModel) oMethod.invoke(p_oVm);
				}
				oZoneLayout.setViewModel(oViewModel);
			}
			else {
				for(ManagementZone oSubZone : p_oZone.getVisibleZones()) {
					this.configureAutoBinding(oSubZone, this.getViewModel());
				}
			}
			
		} catch (SecurityException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		} catch (IllegalArgumentException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		} catch (IllegalAccessException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		} catch (NoSuchMethodException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		} catch (InvocationTargetException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		}
		
		RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
		oDescriptor.inflate(String.valueOf(this.wlayout.hashCode()), oZoneLayout, this.getParameters());
	}

	/**
	 * Crée les handlers 
	 * @return
	 */
	protected abstract List<WorkspaceListHandler> createListHandlers();
	
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
			((ViewGroup) oRootView.getParent()).removeView(oRootView);
			this.wlayout = (MMWorkspaceLayout) oRootView.findViewById(this.getWorkspaceId());
			
			this.setContentView(oRootView);
			this.doAfterSetContentView();
			
			this.wlayout.init();
		}
		else {
			super.defineContentView();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#inflate(boolean)
	 */
	@Override
	public void inflate(boolean p_bWriteData) {
		Object oConfig = this.getLastCustomNonConfigurationInstance();
		
		// On ne refait pas le inflate dans le cas de la rotation
		if ( oConfig == null ) {
			super.inflate(p_bWriteData);
		}
	}
	
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
		final WorkspaceListHandler oListHandler = getListHandlerByAdapter(p_oEvent.getSource());
		if ( oListHandler != null ) {
			final String sNextItemId = p_oEvent.getData();

			if (this.isViewModelModified()) {
				this.doOnViewModelModified(
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface p_oDialog, int p_iId) {
							p_oDialog.cancel();
							AbstractWorkspaceAutoBindMMActivity.this.selectedItemId = sNextItemId;
							AbstractWorkspaceAutoBindMMActivity.this.doOnKeepWorkspaceModifications(
								oListHandler.getListview());
						}
					}, 

					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface p_oDialog, int p_iId) {
							p_oDialog.cancel();
							AbstractWorkspaceAutoBindMMActivity.this.selectedItemId = sNextItemId;
							AbstractWorkspaceAutoBindMMActivity.this.doDisplayDetail();
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
	public WorkspaceListHandler getListHandlerByAdapter( Object p_oAdapter ) {
		WorkspaceListHandler r_oWorkspaceListHandler = null ;
		for( WorkspaceListHandler oWorkspaceListHandler : this.workspaceListHandlers ) {
			if ( oWorkspaceListHandler.getAdapter() == p_oAdapter ) {
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
	 * Workspace: affichage de la liste si ce n'était pas le cas, sinon appel au super.
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#doOnViewModelNotModified()
	 */
	@Override
	public void exit() {
		if (this.getWlayout().isFirstScreenSelected()) {
			super.exit();
		}
		else {
			this.goToPage = 0;
			this.getWlayout().goToColumn(this.goToPage);
			this.doRefreshDetail();
		}
	}


	/**
	 * Nothing to do
	 * @param p_oSource clicked view
	 */
	protected abstract void doOnKeepWorkspaceModifications(View p_oSource);

	public void doOnSaveWorkspaceSuccess(ListenerOnActionSuccessEvent p_oEvent) {
		final Object oSource = p_oEvent.getSource();
		if (oSource != null && String.class.isAssignableFrom(oSource.getClass())) {
			if (AbstractWorkspaceAutoBindMMActivity.FROM_BACK == oSource) {
				//LBR this.doFillAction();
				this.exit();
			}
			else if (AbstractWorkspaceAutoBindMMActivity.FROM_LISTITEM == oSource) {
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
	protected void doRefreshDetail() {
		this.doDisplayDetail(this.selectedItemId);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle p_oSavedInstanceState) {
		p_oSavedInstanceState.putString("CurrentWorkspaceSelectedItemId", this.selectedItemId);
		p_oSavedInstanceState.putString("selectedTab", this.wlayout.getSelectedTab());
		super.onSaveInstanceState(p_oSavedInstanceState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle p_oSavedInstanceState) {
		if (p_oSavedInstanceState!=null) {
			this.selectedItemId = p_oSavedInstanceState.getString("CurrentWorkspaceSelectedItemId");
			this.wlayout.setSelectedTab(p_oSavedInstanceState.getString("selectedTab"));
		}
		super.onRestoreInstanceState(p_oSavedInstanceState);
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
			this.wlayout.hideAllDetailColumn(true);
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
	public abstract int getWorkspaceId();
 
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#localDestroy()
	 */
	@Override
	public void localDestroy() {
		if (wconfiguration!=null) {	
			for(ManagementGroup oGroup : wconfiguration.getVisibleGroups()) {
				for(ManagementZone oZone : oGroup.getVisibleZones()) {
					unInflateZone( oZone);
				}
			}
		}
		super.localDestroy();
	}
	
	/**
	 * @param p_oZone
	 */
	private void unInflateZone( ManagementZone p_oZone ) {
		int iIdSection=application.getAndroidIdByStringKey(ApplicationRGroup.ID,p_oZone.getSource());
		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iIdSection);
		//récupération du view model associé
		if (oZoneLayout==null) {
			//si elle n'est pas associée à la vue, c'est qu'il s'agit d'une colonne masquée
			oZoneLayout=wlayout.getSectionLayoutByIdInHiddenColumns(iIdSection);
		}
		RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
		oDescriptor.unInflate(String.valueOf(this.wlayout.hashCode()), oZoneLayout, this.getParameters());

		for(ManagementZone oSubZone : p_oZone.getVisibleZones()) {
			this.unInflateZone(oSubZone);
		}
	}

	/**
	 * Retourne l'objet wlayout
	 * @return Objet wlayout
	 */
	public MMWorkspaceLayout getWlayout() {
		return this.wlayout;
	}
	
	/**
	 * return la liste de vue courante
	 * @return la liste de vue courante
	 */
	protected ListView getCurrentListView() {
		return currentListView;
	}
	
	/**
	 * @return
	 */
	protected ListView getListViewByPos( int p_iPos) {
		return this.workspaceListHandlers.get(p_iPos).getListview();
	}
	
	
	/**
	 * @param p_iResId
	 * @return
	 */
	protected ListView getListViewByResId( int p_iResId ) {
		ListView r_oListView = null ;
		for( WorkspaceListHandler oWorkspaceListHandler : this.workspaceListHandlers ) {
			if ( oWorkspaceListHandler.getListViewId() == p_iResId ) {
				r_oListView = oWorkspaceListHandler.getListview();
				break;
			}
		}
		return r_oListView ;
	}

	/**
	 * Mise à jour des onglets
	 * @param p_oEvent paramètre de la mise à jour du détail
	 */
	@ListenerOnActionSuccess(action=GenericUpdateVMForDisplayDetailAction.class)
	public void onUpdateTabConfig(ListenerOnActionSuccessEvent<NullActionParameterImpl> p_oEvent) {
		this.updateTabConfig();
		
	}

	/**
	 * Permet de cacher le détail de l'inter à la fin du reset de la base de données
	 * @param p_oEvent paramètre de la suppression de la base de données
	 */
	@ListenerOnActionSuccess(action=ResetDataBaseAction.class)
	public void onResetDataBaseAction(ListenerOnActionSuccessEvent<NullActionParameterImpl> p_oEvent) {
		this.wlayout.hideAllDetailColumn(true);		
	}
	/**
	 * Met à jour toutes les tabulations
	 */
	protected void updateTabConfig() {
		if ( this.getViewModel() instanceof VMTabConfigHolder ) {
			VMTabConfiguration oTabConfig = ((VMTabConfigHolder) this.getViewModel()).getVMTabConfiguration();
			if ( this.wlayout.getTabDelegate().getTabCount() == oTabConfig.getTabCount()) {
				this.wlayout.configurationSetValue(oTabConfig);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> onRetainCustomNonConfigurationInstance() {
		Map<String,Object> r_oMap =  (Map<String, Object>) super.onRetainCustomNonConfigurationInstance();
		r_oMap.put(ROOTCOMPONENT_PARAMETER, this.wlayout.getParent());
		return r_oMap;
		
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
		private int listViewId ;
		
		/**
		 * Adapter
		 */
		private ListAdapter adapter = null;

		/**
		 * List view
		 */
		private ListView listview = null;
		
		/**
		 * @param p_iListViewId
		 */
		public WorkspaceListHandler( int p_iListViewId, ListAdapter p_oAdapter ) {
			this.listViewId = p_iListViewId ;
			this.adapter = p_oAdapter ;
		}

		/**
		 * @param p_oListView
		 */
		public void setListView(ListView p_oListView) {
			this.listview = p_oListView ;
		}

		/**
		 * @return
		 */
		public int getListViewId() {
			return this.listViewId;
		}

		/**
		 * @return
		 */
		protected ListAdapter getAdapter() {
			return this.adapter;
		}
		
		/**
		 * @return
		 */
		protected ListView getListview() {
			return this.listview;
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#inflateLayout()
	 */
	@Override
	public void inflateLayout() {
		//mise en place de l'auto mapping
		// 1 group = une page
		MMMasterRelativeLayout oZoneLayout = null;
		RealVisualComponentDescriptor oDescriptor = null;
		int iSectionId=0;

		for(ManagementGroup oGroup : this.wconfiguration.getVisibleGroups()) {
			for(ManagementZone oZone : oGroup.getVisibleZones()) {
				iSectionId = this.application.getAndroidIdByStringKey(ApplicationRGroup.ID,oZone.getSource());
				oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iSectionId);
				if (oZoneLayout==null) {
					oZoneLayout=wlayout.getSectionLayoutByIdInHiddenColumns(iSectionId);
				}

				if (oZoneLayout.getViewModel() != null && CustomizableViewModel.class
						.isAssignableFrom(oZoneLayout.getViewModel().getClass())) {

					oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
					oDescriptor.unInflateLayout(String.valueOf(this.wlayout.hashCode()), oZoneLayout, this.getParameters());
					oDescriptor.inflateLayout(String.valueOf(this.wlayout.hashCode()), oZoneLayout, this.getParameters(), null);
				}
			}
		}
	}
}
