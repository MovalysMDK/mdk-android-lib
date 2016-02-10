package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.R;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>MMMasterRelativeLayout</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * @author smaitre
 */
public class MMMasterRelativeLayout extends RelativeLayout implements ConfigurableLayoutComponent<NoneType>, MMIdentifiableView, MasterVisualComponent<NoneType>, Checkable {

	public static final boolean PRE_HONEYCOMB = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;
	
	/** configurable view delegate */
	private AndroidConfigurableLayoutComponentDelegate<NoneType> aivDelegate = null;
	/** A delegate for master visual component */
	private MasterVisualComponentDelegate mvcDelegate = null;

	/**the checked value **/
	private boolean checked=false;
	
	/**
	 * Constructs a new MMRelativeLayout
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see RelativeLayout#RelativeLayout(Context, AttributeSet)
	 */
	public MMMasterRelativeLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this);
		}

	}
	/**
	 * called wen the inflator finished the job 
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){
			this.mvcDelegate = new MasterVisualComponentDelegate(this);
		}
	}

	/**
	 * Constructs a new MMRelativeLayout
	 * @param p_oContext the context to use
	 * @see RelativeLayout#RelativeLayout(Context)
	 */
	public MMMasterRelativeLayout(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this);
		}
	}


	/** 
	 * implements checkable interface to style the selected item of a listView (workaroud of activated in android > v11)
	 * @param p_bChecked desired checked state
	 * @see Checkable#setChecked(boolean)
	 * **/
	@Override
	public void setChecked(boolean p_bChecked) {
		if (p_bChecked != checked) {
			checked = p_bChecked;
			refreshDrawableState();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChecked() {
		return checked;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toggle() {
		setChecked(!checked);	
	}

	private static final int[] CHECKED_STATE_SET = {
		R.attr.state_activated
	};
	
	private static final int[] PREHONEYCOMB_CHECKED_STATE_SET = {
		R.attr.state_checked
	};

	@Override
	protected int[] onCreateDrawableState(int p_iExtraSpace) {
		final int[] drawableState = super.onCreateDrawableState(p_iExtraSpace + 1);
		if (isChecked()) {
			if ( PRE_HONEYCOMB ) {
				mergeDrawableStates(drawableState, PREHONEYCOMB_CHECKED_STATE_SET);
			} else {
				mergeDrawableStates(drawableState, CHECKED_STATE_SET);
			}
		}
		return drawableState;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#setId(int)
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivDelegate.setId(p_oId);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfiguration()
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isGroup()
	 */
	@Override
	public boolean isGroup() {
		return this.aivDelegate.isGroup();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationHide(p_bLockModifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationUnHide(p_bLockModifier);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableLayoutComponent#removeChild(com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent)
	 */
	@Override
	public void removeChild(ConfigurableVisualComponent<?> p_oChildToDelete) {
		this.aivDelegate.removeChild(p_oChildToDelete);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableLayoutComponent#configurationAppendCustomLabel(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomLabel(final BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomLabel(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableLayoutComponent#configurationAppendCustomTextField(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomTextField(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomTextField(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomCheckBoxes(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomCheckBoxes(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomRadioButtons(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomRadioButtons(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomComboBox(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomComboBox(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomURL(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomURL(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomEmail(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomEmail(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDate(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomDate(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomTime(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomTime(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDateTime(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomDateTime(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomInteger(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomInteger(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDuration(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomDuration(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomPhone(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomPhone(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(NoneType p_oObject) {
		this.aivDelegate.configurationSetValue(p_oObject);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.aivDelegate.configurationSetCustomValues(p_oObjectsToSet);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NoneType configurationGetValue() {
		return null;
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetLabel(java.lang.String)
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isLabel()
	 */
	@Override
	public boolean isLabel() {
		return this.aivDelegate.isLabel();
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isValue()
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isEdit()
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getLocalisation()
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getModel()
	 */
	@Override
	public String getModel() {
		return this.aivDelegate.getModel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getDescriptor()
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setDescriptor(com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.descriptor.RealVisualComponentDescriptor)
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isPanel()
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isUnknown()
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#getViewModel()
	 */
	@Override
	public ViewModel getViewModel() {
		return this.mvcDelegate.getViewModel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#setViewModel(com.adeuza.movalys.fwk.mobile.javacommons.uimodel.ViewModel)
	 */
	@Override
	public void setViewModel(ViewModel p_oViewModel) {
		this.mvcDelegate.setViewModel(p_oViewModel);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#retrieveConfigurableVisualComponent(java.util.List)
	 */
	@Override
	//A2A_DEV faire un delegate avec MMActivity
	public ConfigurableVisualComponent<?> retrieveConfigurableVisualComponent(List<String> p_oPath) {
		ConfigurableVisualComponent<?> r_oView = this;
		int iId = 0;
		for(String sPath : p_oPath) {
			boolean hasViewBeenRetrievedFromHolder = false;
			if(this.getParameters() != null && this.getParameters().get(ConfigurableListViewHolder.CONFIGURABLE_LIST_VIEW_HOLDER_KEY) != null) {
				ConfigurableListViewHolder oViewHolder = (ConfigurableListViewHolder) this.getParameters().get(ConfigurableListViewHolder.CONFIGURABLE_LIST_VIEW_HOLDER_KEY);
				r_oView = oViewHolder.getComponentByPath(sPath);
				hasViewBeenRetrievedFromHolder = (r_oView != null);
			}
			if(!hasViewBeenRetrievedFromHolder) {
				r_oView = this;
				iId = ((AndroidApplication)Application.getInstance()).getAndroidIdByStringKey(ApplicationRGroup.ID, sPath);
				r_oView = (ConfigurableVisualComponent<?>) ((View)r_oView).findViewById(iId);
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
	public void setTag(Object p_oTag) {
		super.setTag(p_oTag);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#getParameters()
	 */
	@Override
	public Map<String, Object> getParameters() {
		return this.mvcDelegate.getParameters();
	}

	@Override	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#setParameters()
	 */
	public void setParameters(Map<String, Object> p_oParameters) {
		this.mvcDelegate.setParameters(p_oParameters);
	}


	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#inflate()
	 */
	@Override
	public void inflate(boolean p_bWriteData) {
		this.mvcDelegate.inflate(p_bWriteData);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.MasterVisualComponent#inflateLayout()
	 */
	@Override
	public void inflateLayout() {
		this.mvcDelegate.inflateLayout();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getName()
	 */
	@Override
	public String getName() {
		return this.aivDelegate.getName();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(NoneType p_oObject) {
		return p_oObject==null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		return this.aivDelegate.isNullOrEmptyCustomValues(p_oObjects);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#hide()
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#unHide()
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return NoneType.class;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.aivDelegate.isFilled();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.isShown();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}

	/**
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
	public void doOnPostAutoBind() {
		//Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		//Nothing to do
	}

	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}

	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void registerVM(ViewModel p_oViewModel) {
		this.aivDelegate.registerVM(p_oViewModel);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#unregisterVM()
	 */
	@Override
	public void unregisterVM() {
		this.aivDelegate.unregisterVM();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}

	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFragmentTag() {
		return this.aivDelegate.getFragmentTag();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		this.aivDelegate.setFragmentTag(p_sFragmentTag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasRules() {
		return this.aivDelegate.hasRules();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHasRules(boolean p_bHasRules) {
		this.aivDelegate.setHasRules(p_bHasRules);
	}
}
