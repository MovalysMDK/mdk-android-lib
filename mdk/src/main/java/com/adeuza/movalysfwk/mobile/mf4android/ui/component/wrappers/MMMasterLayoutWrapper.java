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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.wrappers;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

public class MMMasterLayoutWrapper extends AbstractComponentWrapper<View> implements ConfigurableLayoutComponent, MasterVisualComponent {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<NoneType> aivDelegate = null;
	
	/** A delegate for master visual component */
	private MasterVisualComponentDelegate mvcDelegate = null;
	
	/** true if the data is being restored after a rotation */
	private boolean restoringOnRotate = false;
	
	@Override
	public AndroidConfigurableVisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public AndroidConfigurableVisualComponentDelegate<NoneType> getComponentDelegate() {
		return this.aivDelegate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setComponent(View p_oComponent, boolean p_bIsRestoring) {
		super.setComponent(p_oComponent, p_bIsRestoring);
		
		this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<NoneType>) DelegateInitialiserHelper.initDelegate(this, 
				new Class[]{Class.class, AttributeSet.class}, 
				new Object[]{NoneType.class, null});
		this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
				new Class[]{AttributeSet.class}, 
				new Object[]{null});
		
		this.mvcDelegate = new MasterVisualComponentDelegate(this);
	}

	@Override
	public void unsetComponent() {
		// nothing to do?
	}

	public void setId(int p_oId) {
		this.component.get().setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivFwkDelegate.getDescriptor();
	}

	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivFwkDelegate.setDescriptor(p_oDescriptor);
	}

	@Override
	public ViewModel getViewModel() {
		return this.mvcDelegate.getViewModel();
	}

	@Override
	public void setViewModel(ViewModel p_oViewModel) {
		this.mvcDelegate.setViewModel(p_oViewModel);
	}

	@Override
	public ConfigurableVisualComponent retrieveConfigurableVisualComponent(List<String> p_oPath, boolean p_bIsInflating) {
		ConfigurableVisualComponent r_oView = this;
		View oTmpView;
		int iId = 0;
		for(String sPath : p_oPath) {
			boolean hasViewBeenRetrievedFromHolder = false;
			if(this.getParameters() != null && this.getParameters().get(ConfigurableListViewHolder.CONFIGURABLE_LIST_VIEW_HOLDER_KEY) != null) {
				ConfigurableListViewHolder oViewHolder = (ConfigurableListViewHolder) this.getParameters().get(ConfigurableListViewHolder.CONFIGURABLE_LIST_VIEW_HOLDER_KEY);
				r_oView = oViewHolder.getComponentByPath(sPath);
				hasViewBeenRetrievedFromHolder = (r_oView != null);
			}
			if(!hasViewBeenRetrievedFromHolder) {
				iId = ((AndroidApplication)Application.getInstance()).getAndroidIdByStringKey(ApplicationRGroup.ID, sPath);
				oTmpView = ((View)this.component.get()).findViewById(iId);
				
				// get wrapper if it exists, or return view
				String sWrapperClass = WidgetWrapperHelper.getInstance().getWrapperClassForComponent(oTmpView);
				
				if (sWrapperClass != null) {
					if (p_bIsInflating) {
						r_oView = WidgetWrapperHelper.getInstance().createWrapper(oTmpView, sWrapperClass, this.isRestoringOnRotate());
					} else {
						r_oView = WidgetWrapperHelper.getInstance().getWrapperForComponent(this.mvcDelegate.getViewModel(), sPath);
					}
				} else {
					r_oView = (ConfigurableVisualComponent) oTmpView;
				}
				if(this.getParameters() != null) {
					ConfigurableListViewHolder oViewHolder = (ConfigurableListViewHolder) this.getParameters().get(ConfigurableListViewHolder.CONFIGURABLE_LIST_VIEW_HOLDER_KEY);
					if(oViewHolder != null) {
						oViewHolder.addComponentByPath(sPath, r_oView);
						oViewHolder.addComponentById(iId, r_oView);
					} 
				}
			}
		}

		return r_oView;
	}

	@Override
	public Map<String, Object> getParameters() {
		return this.mvcDelegate.getParameters();
	}

	@Override
	public void setParameters(Map<String, Object> p_oParameters) {
		this.mvcDelegate.setParameters(p_oParameters);
	}


	@Override
	public void inflate(boolean p_bWriteData) {
		this.mvcDelegate.inflate(p_bWriteData);
	}
	
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivFwkDelegate.getConfiguration();
	}

	/**
	 * Whether the application is restoring the view after a rotation. 
	 * @return true if the application is restoring the view after a rotation
	 */
	public boolean isRestoringOnRotate() {
		return restoringOnRotate;
	}

	/**
	 * Sets the restoring data attribute.
	 * @param restoringOnRotate true if the activity is being restored after a rotation
	 */
	public void setRestoringOnRotate(boolean restoringOnRotate) {
		for (Entry<String, List<AbstractComponentWrapper<?>>> oEntry : this.getViewModel().getWrappers()) {
			for (AbstractComponentWrapper<?> oWrapper : oEntry.getValue()) {
				oWrapper.setWritingData(restoringOnRotate);
			}
		}
		this.restoringOnRotate = restoringOnRotate;
	}
	
}
