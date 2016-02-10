package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.AutoBindDialog;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.DialogDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>
 * Android implementation of screen.
 * </p>
 * 
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author smaitre
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
	public AbstractAutoBindMMDialogFragment() {
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
	public void inflateLayout() {
		this.mvcDelegate.inflateLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> retrieveConfigurableVisualComponent(
			List<String> p_oPath) {
		ConfigurableVisualComponent<?> r_oView = this;
		int iId = 0;
		int iCpt = 0;
		// int i = 0;

		for (String sPath : p_oPath) {
			// i++;
			iId = androidApplication.getAndroidIdByStringKey(
					ApplicationRGroup.ID, sPath);
			if (iCpt == 0) {
				r_oView = (ConfigurableVisualComponent<?>) this.getView()
						.findViewById(iId);
			} else {
				r_oView = (ConfigurableVisualComponent<?>) ((View) r_oView)
						.findViewById(iId);
			}
			if (r_oView == null) {
				break;
			} else {
				ViewModel oVm = null;
				oVm = this.getViewModel();
				if (oVm == null) {
					oVm = this.createViewModel();
				}
				r_oView.registerVM(oVm);
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
		return ConfigurationsHandler.getInstance().getVisualConfiguration(
				this.getConfigurationName());
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
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(NoneType p_oObject) {
		// Nothing To Do
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
	public NoneType configurationGetValue() {
		// Nothing To Do
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetLabel(java.lang.String)
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		// Nothing To Do : an activity as no label
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationHide()
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		// Nothing To Do : an activity can not be hide
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationUnHide()
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		// Nothing To Do : an activity can not be hide
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isLabel()
	 */
	@Override
	public boolean isLabel() {
		return this.dialogDelegate.isLabel();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isAlwaysEnable()
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isValue()
	 */
	@Override
	public boolean isValue() {
		return this.dialogDelegate.isValue();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isEdit()
	 */
	@Override
	public boolean isEdit() {
		return this.dialogDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isGroup()
	 */
	@Override
	public boolean isGroup() {
		return this.dialogDelegate.isGroup();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getLocalisation()
	 */
	@Override
	public String getLocalisation() {
		return this.dialogDelegate.getLocalisation();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getModel()
	 */
	@Override
	public String getModel() {
		return this.dialogDelegate.getModel();
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
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isPanel()
	 */
	@Override
	public boolean isPanel() {
		return this.dialogDelegate.isPanel();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isUnknown()
	 */
	@Override
	public boolean isUnknown() {
		return this.dialogDelegate.isUnknown();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isChanged()
	 */
	@Override
	public boolean isChanged() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#resetChanged()
	 */
	@Override
	public void resetChanged() {
		// Nothing To do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(NoneType p_oObject) {
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
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#hide()
	 */
	@Override
	public void hide() {
		// Nothing To Do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#unHide()
	 */
	@Override
	public void unHide() {
		// Nothing To Do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return NoneType.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(
			List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		// NothingToDo
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		// NothingToDo
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		// NothingToDo
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		// Nothing to do
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
	 * {@inheritDoc}
	 */
	@Override
	public void doOnPostAutoBind() {
		// Nothing  to do
		
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
	public void configurationUnsetError() {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration,
			Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#setName(java.lang.String)
	 */
	@Override
	public void setName(String p_sNewName) {
		this.dialogDelegate.setName(p_sNewName);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void registerVM(ViewModel p_oViewModel) {
		//do nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#unregisterVM()
	 */
	@Override
	public void unregisterVM() {
		//do nothing
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
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return null;
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
	 * {@inheritDoc}
	 */
	@Override
	public CustomFormatter customFormatter() {
		return null;
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
	public void setFragmentTag(String p_sFragmentTag) {
		// Nothing to do
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
	public void setHasRules(boolean p_bHasRules) {
		// Nothing to do
	}
	


	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		// Nothing to do
	}

	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		// Nothing to do
	}
}


