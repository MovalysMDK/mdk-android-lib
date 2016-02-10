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

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;

/**
 * <p>crée un fragment conteneur pour les sections</p>
 * <p>cette class est utilisable uniquement sur les FragmentActivity</p>
 * @author abelliard
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
		oSection.setOrientation(MMLinearLayout.VERTICAL);
		this.root.addView(oSection);
		
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
		
		return root;
	}
	
}
