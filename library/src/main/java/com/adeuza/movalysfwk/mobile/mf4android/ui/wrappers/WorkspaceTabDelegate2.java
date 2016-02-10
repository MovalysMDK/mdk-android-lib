package com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceMasterDetailLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;

/**
 * @author lmichenaud
 *
 */
public class WorkspaceTabDelegate2<WLAYOUT extends MMWorkspaceMasterDetailLayout> implements TabContentFactory {
	
	/**
	 * Workspace layout
	 */
	private WLAYOUT workspaceLayout;
	
	/**
	 * 
	 */
	private boolean enabled = false ;
	
	/**
	 * Tab host
	 */
	private TabHost uiTabHost ;
	
	/**
	 * Android application
	 */
	private AndroidApplication application = null ;
	
	/**
	 * List views
	 */
	private Map<String,View> tabViews = new HashMap<>();
	
	/**
	 * @param p_oView
	 */
	public WorkspaceTabDelegate2( WLAYOUT p_oView ) {
		this.workspaceLayout = p_oView ;
		this.application = (AndroidApplication) Application.getInstance();
	}
	
	/**
	 * @param p_oAttrs
	 * @param p_oMainZone
	 */
	public void init(ManagementZone p_oMainZone ) {
		this.enabled = p_oMainZone != null && p_oMainZone.getVisibleZones() != null
				&& !p_oMainZone.getVisibleZones().isEmpty();
	}

	/**
	 * 
	 */
	public void doAfterSetContentView() {		
		// Dans le cas de la rotation, le uiTabHost est déjà valué. On initialise
		// les tabs que quand il est null.
		if ( this.enabled && this.uiTabHost == null ) {
			this.uiTabHost = (TabHost) this.workspaceLayout.findViewById(
					this.application.getAndroidIdByStringKey(ApplicationRGroup.ID, "tabhost"));
			this.uiTabHost.setup();
			this.createTabs();
			
			for( int i = this.getTabManagementZones().size() -1 ; i >= 0 ; i-- ) {
				this.uiTabHost.setCurrentTab(i);
			}
		}
	}
	
	
	/**
	 * 
	 */
	private void createTabs() {
		if ( this.enabled ) {
			for( ManagementZone oZone : getTabManagementZones()) {
				this.uiTabHost.addTab(this.createTabSpec(oZone.getTag()));
			}
		}
	}
	
	
	/**
	 * @return
	 */
	public List<ManagementZone> getTabManagementZones() {
		ManagementConfiguration oManager = ConfigurationsHandler.getInstance().getManagementConfiguration(this.workspaceLayout.getModel());
		return oManager.getVisibleGroups().get(0).getVisibleZones().get(0).getVisibleZones();
	}
	
	/**
	 * @return
	 */
	public ManagementZone getTabManagementZone( String p_sTabId ) {
		ManagementConfiguration oManager = ConfigurationsHandler.getInstance().getManagementConfiguration(this.workspaceLayout.getModel());
		return oManager.getVisibleGroups().get(0).getVisibleZones().get(0).getZoneByTag(p_sTabId);
	}
	
	/**
	 * @param p_sTabName
	 * @param p_iTabPlanningSize
	 * @param p_iImage
	 * @return
	 */
	private TabSpec createTabSpec(String p_sTabName) {
		TabSpec r_oTabPlannedSpec = this.uiTabHost.newTabSpec(p_sTabName);
		r_oTabPlannedSpec.setIndicator(StringUtils.EMPTY);
		r_oTabPlannedSpec.setContent(this);
		return r_oTabPlannedSpec;
	}
	
	/**
	 * Create the tab content
	 * 
	 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
	 * @param p_oParamString
	 *            the label of tab
	 * @return a list view associated to the tab
	 */
	@Override
	public View createTabContent(String p_sParamString) {
		View oCurrentView = this.tabViews.get(p_sParamString);
		if (oCurrentView == null) {
					
			ManagementZone oZone = getTabManagementZone(p_sParamString);
			
			int iTabLayout = this.application.getAndroidIdByStringKey(ApplicationRGroup.LAYOUT, oZone.getSource());
			
			LayoutInflater oLayoutInflater = LayoutInflater.from(this.workspaceLayout.getContext());
			oCurrentView = oLayoutInflater.inflate(iTabLayout, null);
			
			this.uiTabHost.getTabContentView().addView(oCurrentView);

			this.tabViews.put(p_sParamString, oCurrentView);
		}
		return this.tabViews.get(p_sParamString);
	}
	
	/**
	 * @return
	 */
	public boolean isTab() {
		return this.enabled ;
	}
	
	/**
	 * Return number of childs
	 * @return
	 */
	public int getTabCount() {
		return this.uiTabHost.getTabWidget().getChildCount();
	}

	/**
	 * @param p_oVMTabConfiguration
	 */
	public void applyTabConfiguration(VMTabConfiguration p_oVMTabConfiguration) {		
		int iCount = this.uiTabHost.getTabWidget().getChildCount();
		
		for( int i = 0 ; i < iCount; i++ ) {
			View oTabView = this.uiTabHost.getTabWidget().getChildTabViewAt(i);
			ImageView oImageView = (ImageView) oTabView.findViewById(R.id.icon);
			if ( p_oVMTabConfiguration.getTabIcon(i) != null ) {
				int iDrawableResource = this.workspaceLayout.getResources().getIdentifier(
					p_oVMTabConfiguration.getTabIcon(i), "drawable",
					this.workspaceLayout.getContext().getPackageName());
				oImageView.setImageResource(iDrawableResource);
			}
			else {
				oImageView.setImageDrawable(null);
			}
			TextView oTextView = (TextView) oTabView.findViewById(android.R.id.title);
			oTextView.setText( p_oVMTabConfiguration.getTabTitle(i));
			
		}
	}
		
	/**
	 * @return
	 */
	public String getSelectedTab() {
		return this.uiTabHost.getCurrentTabTag();
	}
	
	/**
	 * @param p_sTabId
	 */
	public void setSelectedTab( String p_sTabId ) {
		if ( p_sTabId != null && !p_sTabId.equals(this.uiTabHost.getCurrentTabTag())) {
			this.uiTabHost.setCurrentTabByTag(p_sTabId);
		}
	}
	
	/**
	 * @param p_oOnTabChangedListener
	 */
	public void setOnTabChangedListener( OnTabChangeListener p_oOnTabChangedListener ) {
		if ( this.enabled ) {
			this.uiTabHost.setOnTabChangedListener( p_oOnTabChangedListener );
		}
	}
}
