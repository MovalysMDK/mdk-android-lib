/**
 * 
 */
package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.LayoutComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.AutoBindScreen;

/**
 * @author pedubreuil
 *
 */
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
	public void doOnPostAutoBind() {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		// NothingToDo
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		// NothingToDo
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public NoneType configurationGetValue() {
		// Nothing To Do
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		// Nothing To Do : an activity can not be hide
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationPrepareHide(
			List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		// NothingToDo
	}

	/**
	 * Remove the mandatory label
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// NothingToDo
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_sError) {
		// NothingToDo
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		// Nothing To Do : an activity as no label
	}

	/**
	 * Set the mandatory label
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		// NothingToDo
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(NoneType p_oObject) {
		// Nothing To Do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		// Nothing To Do : an activity can not be hide
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		// NothingToDo
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return null;
	}

	/**
	 *  get custom formatter
	 *  @return null !!
	 */
	@Override
	public CustomFormatter customFormatter() {
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		// Nothing to do
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
	public Method getConfigurationSetValueMethod() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.descriptor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFragmentTag() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalisation() {
		return this.getScreenDelegate().getLocalisation();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModel() {
		return this.getScreenDelegate().getModel();
	}



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
	public Class<?> getValueType() {
		return NoneType.class;
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
	public boolean hasRules() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hide() {
		// Nothing To Do
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
	public void inflateLayout() {
		this.mvcDelegate.inflateLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEdit() {
		return this.getScreenDelegate().isEdit();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGroup() {
		return this.getScreenDelegate().isGroup();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLabel() {
		return this.getScreenDelegate().isLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaster() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(NoneType p_oObject) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPanel() {
		return this.getScreenDelegate().isPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUnknown() {
		return this.getScreenDelegate().isUnknown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValue() {
		return this.getScreenDelegate().isValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void registerVM(ViewModel p_oViewModel) {
		//do nothing
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		// Nothing To do
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
	public void setFragmentTag(String p_sFragmentTag) {
		//do nothing
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHasRules(boolean p_bHasRules) {
		//do nothing
	}

	/**
	 * {@inheritDoc} NothingToDo
	 */
	@Override
	public void setName(String p_sNewName) {
		// NothingToDo
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
	 * {@inheritDoc}
	 */
	@Override
	public void unHide() {
		// Nothing To Do
	}
	

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void unregisterVM() {
		//do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oError) {
		return true;
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
	public List<ConfigurableVisualComponent<?>> getConfigurableVisualComponentsOf(LayoutComponentConfiguration p_oLayoutConfiguration) {
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
	public void registerConfigurableVisualComponentsOf(LayoutComponentConfiguration p_oLayoutConfiguration,List<ConfigurableVisualComponent<?>> p_oComponents) {
		this.mvcDelegate.registerConfigurableVisualComponentsOf(p_oLayoutConfiguration, p_oComponents);
	}

}
