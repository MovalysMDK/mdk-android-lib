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

import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.LayoutComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.AutoBindScreen;


public abstract class AbstractMvcMMActivity extends AbstractMMActivity implements AutoBindScreen {

	/** associated descriptor */
	private RealVisualComponentDescriptor descriptor = null;
	/** A2A_DOC description mvcDelegate */
	private MasterVisualComponentDelegate mvcDelegate = null;

	/**
	 * constructor
	 */
	public AbstractMvcMMActivity() {
		super();
		this.mvcDelegate = new MasterVisualComponentDelegate(this);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return ConfigurationsHandler.getInstance().getVisualConfiguration(this.getConfigurationName());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getConfigurationName() {
		return this.getScreenDelegate().getConfigurationName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.descriptor;
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public String getLocalisation() {
//		return this.getScreenDelegate().getLocalisation();
//	}
//
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public String getModel() {
//		return this.getScreenDelegate().getModel();
//	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getParameters() {
		return this.mvcDelegate.getParameters();
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
	public void inflate(boolean p_bWriteData) {
		this.mvcDelegate.inflate(p_bWriteData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.descriptor = p_oDescriptor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameters(Map<String, Object> p_oParameters) {
		this.mvcDelegate.setParameters(p_oParameters);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setViewModel(ViewModel p_oViewModel) {
		this.mvcDelegate.setViewModel(p_oViewModel);
	}


	/**
	 * Unregister configurable visual components
	 * @param p_oLayoutConfiguration configuration
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#unregisterConfigurableVisualComponentsOf(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.LayoutComponentConfiguration)
	 */
	public void unregisterConfigurableVisualComponentsOf(LayoutComponentConfiguration p_oLayoutConfiguration) {
		this.mvcDelegate.unregisterConfigurableVisualComponentsOf(p_oLayoutConfiguration);
	}

	/**
	 * GETTER
	 * @param p_oLayoutConfiguration configuration
	 * @return list of components
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#getConfigurableVisualComponentsOf(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.LayoutComponentConfiguration)
	 */
	public List<ConfigurableVisualComponent> getConfigurableVisualComponentsOf(LayoutComponentConfiguration p_oLayoutConfiguration) {
		return this.mvcDelegate.getConfigurableVisualComponentsOf(p_oLayoutConfiguration);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void localDestroy() {

		// on ne doit pas détruire la référence au screen lorsqu'on pivote
		// l'application
		this.mvcDelegate.unInflate();
		// A2A_INFO _#_FBO_#_ on ne supprime plus les VM du cache car ça fait
		// planter la rotation
		// androidApplication.getViewModelCreator().removeToCache(this.getViewModel().getIdVM(),this.getViewModel().getClass()
		// );

		super.localDestroy();
	}
	
	/**
	 * Register configurable visual components
	 *  @param p_oLayoutConfiguration layout configuration
	 *  @param p_oComponents configurable visual components
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#registerConfigurableVisualComponentsOf(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.LayoutComponentConfiguration,
	 *      java.util.List)
	 */
	public void registerConfigurableVisualComponentsOf(LayoutComponentConfiguration p_oLayoutConfiguration,List<ConfigurableVisualComponent> p_oComponents) {
		this.mvcDelegate.registerConfigurableVisualComponentsOf(p_oLayoutConfiguration, p_oComponents);
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return null;
	}

	@Override
	public VisualComponentDelegate<NoneType> getComponentDelegate() {
		return null;
	}
	
}
