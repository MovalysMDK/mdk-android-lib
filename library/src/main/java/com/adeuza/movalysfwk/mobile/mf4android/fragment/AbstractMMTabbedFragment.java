package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractWorkspaceMasterDetailMMFragmentActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.GenericUpdateVMForDisplayDetailAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccess;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfigHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * class to show tab in a panel
 * @author abelliard
 *
 */
public abstract class AbstractMMTabbedFragment extends
		AbstractInflateMMFragment {
	
	/** key to get zones from args */
	public static final String ARG_ZONES = "zones";

	private static final String TAG = "AbstractMMTabbedFragment";

	/** key to get tab config from args */
//	public static final String TAB_CONFIGURATION = "tab_config";
	
	/** the tab host */
	private FragmentTabHost mTabHost;

	/** the zone */
	private ManagementZone mZone;

	/**
	 * The current seclected tab tag
	 */
	private String selectedTabTag;

	/** the tab configuration */
//	private VMTabConfiguration mTabConfiguration;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		Bundle args = this.getArguments();
		if (args != null) {
			this.mZone = (ManagementZone) args.getSerializable(ARG_ZONES);
//			this.mTabConfiguration = (VMTabConfiguration) args.getSerializable(TAB_CONFIGURATION);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doAfterInflate(ViewGroup p_oRoot) {
		super.doAfterInflate(p_oRoot);
		
		// create tabs
		mTabHost = (FragmentTabHost) p_oRoot.findViewById(android.R.id.tabhost);
		mTabHost.setup(this.getActivity(), getChildFragmentManager(),
				this.getRealTabContentId());
		
		mTabHost.setOnTouchListener(null);
		Log.v(TAG, "[doAfterInflate] create tabs");
		
		this.createTabs();

		if (OnTabChangeListener.class.isAssignableFrom(this.getActivity().getClass())) {
			mTabHost.setOnTabChangedListener((AbstractWorkspaceMasterDetailMMFragmentActivity) this.getActivity());
		}
		
		//Each time this fragment is instantiated (the inflated), the configuration should be updated
		updateTabConfig();
		
	}

	/**
	 * create the tabs
	 */
	private void createTabs() {
		for( ManagementZone oZone : getTabManagementZones()) {
			
			Class<?> cTabFragmentClass;
			try {
				cTabFragmentClass = (Class<? extends AbstractAutoBindMMFragment>) Class
						.forName(oZone.getSource());
			
				Bundle args = new Bundle();
				args.putSerializable(AbstractAutoBindMMFragment.ARG_ZONE, oZone);
				
				this.mTabHost.addTab(this.createTabSpec(oZone.getTag()), cTabFragmentClass, args);
			
			} catch (ClassNotFoundException e) {
				Log.e(TAG, "Fragment Class not found : "+oZone.getSource());
			}
		}
		
		((AbstractWorkspaceMasterDetailMMFragmentActivity) this.getActivity()).doOnMasterCreated(this);
		
	}
	
	/**
	 * return the list of ManagmentZone for the tabs
	 * @return a List of ManagementZone
	 */
	private List<ManagementZone> getTabManagementZones() {
		return this.mZone.getVisibleZones();
	}

	/**
	 * Create the tab spec
	 * @param p_sTabName
	 * @return the tabSpec
	 */
	private TabSpec createTabSpec(String p_sTabName) {
		TabSpec r_oTabPlannedSpec = this.mTabHost.newTabSpec(p_sTabName);
		r_oTabPlannedSpec.setIndicator(StringUtils.EMPTY);
		return r_oTabPlannedSpec;
	}

	
	
	/**
	 * apply the configuration to the tab widgets
	 * @param p_oVMTabConfiguration
	 */
	public void applyTabConfiguration(VMTabConfiguration p_oVMTabConfiguration) {		
		int iCount = this.mTabHost.getTabWidget().getChildCount();
		
		for( int i = 0 ; i < iCount; i++ ) {
			View oTabView = this.mTabHost.getTabWidget().getChildTabViewAt(i);
			ImageView oImageView = (ImageView) oTabView.findViewById(android.R.id.icon);
			if ( p_oVMTabConfiguration.getTabIcon(i) != null ) {
				int iDrawableResource = this.getActivity().getResources().getIdentifier(
					p_oVMTabConfiguration.getTabIcon(i), "drawable",
					this.getActivity().getPackageName());
				oImageView.setImageResource(iDrawableResource);
			}
			else {
				oImageView.setImageDrawable(null);
			}
			TextView oTextView = (TextView) oTabView.findViewById(android.R.id.title);
			oTextView.setText(p_oVMTabConfiguration.getTabTitle(i));
			
		}
	}
	
	/**
	 * Updating tabs
	 * @param p_oEvent Parameters for detail update
	 */
	@ListenerOnActionSuccess(action=GenericUpdateVMForDisplayDetailAction.class)
	public void onUpdateTabConfig(ListenerOnActionSuccessEvent<NullActionParameterImpl> p_oEvent) {
		this.updateTabConfig();
	}
	
	/**
	 * Update all tabs
	 */
	protected void updateTabConfig() {
		if (this.getActivity() != null) {
			ViewModel oVm = ((AbstractAutoBindMMActivity) this.getActivity()).getViewModel();
			if ( oVm instanceof VMTabConfigHolder ) {
				VMTabConfiguration oTabConfig = ((VMTabConfigHolder) oVm).getVMTabConfiguration();
				this.applyTabConfiguration(oTabConfig);
			}
		}
	}

	/**
	 * get the real tab content id to put the framgents
	 * @return the id of the container view
	 */
	protected abstract int getRealTabContentId();

	
	
	/**
	 * Set the selected tab by a tab tag
	 * @param p_sTabSelectedTag
	 */
	public void setTabSelected(String p_sTabSelectedTag) {
		
		Log.v(TAG, "[setTabSelected] selected :"+p_sTabSelectedTag);
		
		mTabHost.setCurrentTabByTag(p_sTabSelectedTag);
		
	}

	public AbstractMMWorkspaceListFragment getCurrentListFragment() {
		return (AbstractMMWorkspaceListFragment) getChildFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
	}

}
