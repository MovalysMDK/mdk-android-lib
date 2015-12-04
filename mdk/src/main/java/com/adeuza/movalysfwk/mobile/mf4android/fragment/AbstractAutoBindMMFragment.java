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
package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import android.os.Bundle;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.wrappers.MMMasterLayoutWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>Fragment class to use autobinding</p>
 *
 */
public abstract class AbstractAutoBindMMFragment extends AbstractInflateMMFragment {

	/** argument zone */
	public static final String ARG_ZONE = "zone";
	/** zone */
	protected ManagementZone mZone;
	/** the fragment view model */
	protected ViewModel oVm;
	/** master component */
	private MMMasterLayoutWrapper masterComponent;
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		this.masterComponent.setRestoringOnRotate(this.isRotated);
	}
	
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
	
	@Override
	public void onResume() {
		super.onResume();
		
		this.masterComponent.setRestoringOnRotate(this.isRotated);
	}
	
	@Override
	protected void doAfterInflate(ViewGroup p_oRoot) {
		super.doAfterInflate(p_oRoot);
		this.oVm = getFragmentViewModel();
		
		this.masterComponent = (MMMasterLayoutWrapper) WidgetWrapperHelper.getInstance().createWrapper(this.layout, "MMMasterLayoutWrapper", this.isRotated);
		
		this.configureAutoBinding(this.mZone, this.oVm);
		
		this.masterComponent.setRestoringOnRotate(this.isRotated);
		
		String rLayout = ((AndroidApplication) Application.getInstance()).getAndroidIdStringByIntKey(getLayoutId());
	
		RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(rLayout);
		oDescriptor.inflate(String.valueOf(this.layout.hashCode()), this.masterComponent, ((AbstractAutoBindMMActivity) this.getActivity()).getParameters(), getTag());
		
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
		this.getMasterComponent().setViewModel(this.oVm);
	}
	
	@Override
	public void onDestroyView() {
		this.localDestroy();
		super.onDestroyView();
	}
	
	/**
	 * destroy the binding between views and view model
	 */
	protected void localDestroy() {
		if (this.layout != null) {
			RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(((AndroidApplication) Application.getInstance()).getAndroidIdStringByIntKey(getLayoutId()));
			oDescriptor.unInflate(String.valueOf(this.layout.hashCode()), getMasterComponent(), ((AbstractAutoBindMMActivity) this.getActivity()).getParameters());
		}
	}

	/**
	 * Returns the MasterVisualComponent linked to the layout of the fragment
	 * @return the MasterVisualComponent
	 */
	private MasterVisualComponent getMasterComponent() {
		return this.masterComponent;
	}
}
