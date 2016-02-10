package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import android.os.Bundle;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMasterRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>Fragment class to use autobinding</p>
 * @author abelliard
 *
 */
public abstract class AbstractAutoBindMMFragment extends AbstractInflateMMFragment {

	/** identifier */
	protected static final String IDENTIFIER_CACHE_KEY = "id";
	/** argument zone */
	public static final String ARG_ZONE = "zone";
	/** zone */
	protected ManagementZone mZone;
	/** the fragment view model */
	protected ViewModel oVm;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			this.mZone = (ManagementZone) args.getSerializable(ARG_ZONE);
		}

	}
	//XXX CHANGE V3.2
	@Override
	protected void doAfterInflate(ViewGroup p_oRoot) {
		super.doAfterInflate(p_oRoot);
		this.oVm = getFragmentViewModel();
		this.configureAutoBinding(this.mZone, this.oVm);

		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.layout;
		RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
		oDescriptor.inflate(String.valueOf(this.layout.hashCode()), oZoneLayout, ((AbstractAutoBindMMActivity) this.getActivity()).getParameters(), getTag());
	}

	protected abstract ViewModel getFragmentViewModel();

	/**
	 * Configure the link between the view model and the views
	 * @param p_oZone the management zone of the panel
	 * @param p_oVm the view model of the screen
	 * @deprecated use configureAutoBinding(ViewModel p_oVm) instead p_oZone have no use anymore
	 */
	@Deprecated
	protected void configureAutoBinding(ManagementZone p_oZone, ViewModel p_oVm) {
		this.configureAutoBinding(p_oVm);
	}
	
	/**
	 * Configure the link between the view model and the views
	 * @param p_oVm the view model of the screen
	 */
	protected void configureAutoBinding(ViewModel p_oVm) {
		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.layout;
		//XXX CHANGE V3.2
		oZoneLayout.setViewModel(this.oVm);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStart() {
		super.onStart();
		this.doFillAction();

	}
	
	/**
	 * Method to call to populate the panel (this method needs to be override to do a action)
	 */
	protected void doFillAction() {		
		this.oVm.doOnDataLoaded(null);
	}
	
	@Override
	public void onDestroyView() {
		this.localDestroy();
		super.onDestroyView();
	}
	
	/**
	 * destroy the binding betwin views and view model
	 */
	protected void localDestroy() {
		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.layout;

		if (oZoneLayout.getName() != null && this.layout != null) {
			RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
			oDescriptor.unInflate(String.valueOf(this.layout.hashCode()), oZoneLayout, ((AbstractAutoBindMMActivity) this.getActivity()).getParameters());
		}
	}

}
