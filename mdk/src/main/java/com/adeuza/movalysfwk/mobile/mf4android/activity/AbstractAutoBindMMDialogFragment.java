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
package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.AutoBindDialog;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.DialogDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>
 * Android implementation of screen.
 * </p>
 * 
 */
public abstract class AbstractAutoBindMMDialogFragment extends MMDialogFragment
implements AutoBindDialog {

	/**
	 * Constant auto bind dialog tag
	 */
	public static String ABSTRACT_AUTO_BIND_DIALOG_TAG = "AbstractAutoBindMMDialogFragmentTag";
	
	/**
	 * constructor
	 */
	public AbstractAutoBindMMDialogFragment(ConfigurableVisualComponent p_oParent) {
		this.componentFragmentTag = p_oParent.getComponentFwkDelegate().getFragmentTag();
		
		this.dialogDelegate = new DialogDelegate();
		this.mvcDelegate = new MasterVisualComponentDelegate(this);
	}
	
	

	/**
	 * parent activity weak reference
	 */
	private WeakReference<AbstractMMActivity> m_oParentActivity = null;

	/** Dialog Delegate */
	private DialogDelegate dialogDelegate;

	/** A2A_DOC description mvcDelegate */
	private MasterVisualComponentDelegate mvcDelegate = null;
	/** associated descriptor */
	private RealVisualComponentDescriptor descriptor = null;
	/** application en cours */
	private AndroidApplication androidApplication = (AndroidApplication) Application
			.getInstance();

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	public final void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);

		this.mvcDelegate.setViewModel(this.createViewModel());
		// this.requestWindowFeature(Window.FEATURE_LEFT_ICON);


		doOnCreate(p_oSavedInstanceState);
	}

	/**
	 * analyse
	 */
	public void analyse() {
		androidApplication.analyzeClassOf(this);

	}

	/** launch the action for fill data : call by on prepare */
	public void doFillAction() {
		// Nothing to do
	}

	@Override
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer,
			Bundle p_oSavedInstanceState) {
		return p_oInflater.inflate(getViewId(), null, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onViewCreated(View p_oView, Bundle p_oSavedInstanceState) {
		doAfterSetContentView();
		if (Application.getInstance().isLoaded()) {
			this.dialogDelegate.setName(this.androidApplication
					.getAndroidIdStringByIntKey(getViewId()));
		}
		this.inflate(writeDataAtConstruction());

		if(getArguments() != null) {
			int iResourceTitleId = getArguments().getInt(DIALOG_FRAGMENT_ARGUMENT_TITLE_KEY);
			int iResourceIconId = getArguments().getInt(DIALOG_FRAGMENT_ARGUMENT_ICON_KEY);

			if(iResourceTitleId != 0) {
				this.getDialog().setTitle(iResourceTitleId);
			}
			if(iResourceIconId != 0) {
				this.getDialog().setFeatureDrawableAlpha(iResourceIconId, 0);
			}
		}

		super.onViewCreated(p_oView, p_oSavedInstanceState);
	}

	/**
	 * this method must be overloaded to wrap manualy the views
	 */
	protected void doAfterSetContentView() {
		// NothingToDo
	}

	/**
	 * create VM
	 * @return viewModel
	 */
	protected abstract ViewModel createViewModel();

	/**
	 * write data at construction
	 * @return false
	 */
	protected boolean writeDataAtConstruction() {
		return false;
	}

	/**
	 * GETTER
	 * @return view ID
	 */
	protected abstract int getViewId();

	/**
	 * do on save
	 * @param p_oSavedInstanceState instance state
	 */
	protected void doOnCreate(Bundle p_oSavedInstanceState) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getViewModel() {
		return this.mvcDelegate.getViewModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setViewModel(ViewModel p_oViewModel) {
		this.mvcDelegate.setViewModel(p_oViewModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inflate(boolean p_bWriteData) {
		this.mvcDelegate.inflate(p_bWriteData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent retrieveConfigurableVisualComponent(List<String> p_oPath, boolean p_bIsInflating) {
		ConfigurableVisualComponent r_oView = this;
		View oTmpView;
		int iId = 0;
		int iCpt = 0;
		// int i = 0;

		for (String sPath : p_oPath) {
			// i++;
			iId = androidApplication.getAndroidIdByStringKey(ApplicationRGroup.ID, sPath);
			if (iCpt == 0) {
				oTmpView = this.getView().findViewById(iId);
			} else {
				oTmpView = ((View) r_oView).findViewById(iId);
			}
			if (oTmpView == null) {
				break;
			} else {
				// get wrapper if it exists, or return view
				String sWrapperClass = WidgetWrapperHelper.getInstance().getWrapperClassForComponent(oTmpView);
				
				if (sWrapperClass != null) {
					if (p_bIsInflating) {
						r_oView = WidgetWrapperHelper.getInstance().createWrapper(oTmpView, sWrapperClass, false);
					} else {
						r_oView = WidgetWrapperHelper.getInstance().getWrapperForComponent(this.mvcDelegate.getViewModel(), sPath);
					}
				} else {
					r_oView = (ConfigurableVisualComponent) oTmpView;
				}
				
				ViewModel oVm = null;
				oVm = this.getViewModel();
				if (oVm == null) {
					oVm = this.createViewModel();
				}
				
				r_oView.getComponentFwkDelegate().registerVM(oVm);
			}
			iCpt++;
		}
		return r_oView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getParameters() {
		return new TreeMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.dialogDelegate.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setDescriptor(com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.descriptor.RealVisualComponentDescriptor)
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.descriptor = p_oDescriptor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getDescriptor()
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.descriptor;
	}

	/**
	 * GETTER
	 * @return configuration name
	 */
	public String getConfigurationName() {
		return this.dialogDelegate.getConfigurationName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.dialogDelegate.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		this.dismiss();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dismiss() {
		Application.getInstance().removeListenerFromDataLoader(this);
		Application.getInstance().removeListenerFromEventManager(this);
		Application.getInstance().removeListenerFromAction(this);
		super.dismiss();
	}

	/**
	 * SETTER
	 * @param p_oParentActivity parent activity
	 */
	public void setParentActivity(AbstractMMActivity p_oParentActivity) {
		this.m_oParentActivity = new WeakReference<>(p_oParentActivity);
	}

	/**
	 * GETTER
	 * @return parent activity
	 */
	public WeakReference<AbstractMMActivity> getParentActivity() {
		return this.m_oParentActivity;
	}

	/**
	 * SETTER
	 * @param p_oCustomConverter custom converter
	 * @param p_oAttributeSet attr set
	 */
	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		//Nothing to do
	}

	/**
	 * SETTER
	 * @param p_oCustomFormatter custom formatter
	 * @param p_oAttributeSet attributes set
	 */
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		//Nothing to do
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameters(Map<String, Object> p_oParameters) {
		// Nothing to do
		
	}
	
	//
	
	@Override
	public VisualComponentDelegate<?> getComponentDelegate() {
		return null;
	}
	
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.dialogDelegate;
	}
	
}


