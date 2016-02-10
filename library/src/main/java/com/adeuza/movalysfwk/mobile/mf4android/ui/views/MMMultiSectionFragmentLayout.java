package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * @author lmichenaud
 *
 */
public class MMMultiSectionFragmentLayout extends LinearLayout implements ConfigurableLayoutComponent<NoneType>, MMIdentifiableView {

	/** name of the MMSectionView */
	public static final String SECTION_CONTAINER_NAME = "SECTION_CONTAINER_NAME";

	/** aivDelegate */
	private AndroidConfigurableLayoutComponentDelegate<NoneType> aivDelegate ;

	/**
	 * Constructs a new MMLinearLayout
	 * 
	 * @param p_oContext the context to use
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public MMMultiSectionFragmentLayout(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this);
		}
	}

	/**
	 * Constructs a new MMLinearLayout.
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public MMMultiSectionFragmentLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	protected void onConfigurationChanged(Configuration p_oNewConfig) {
		super.onConfigurationChanged(p_oNewConfig);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivDelegate.setId(p_oId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}

	/**
	 * {@inheritDoc}
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
	 */
	@Override
	public void removeChild(ConfigurableVisualComponent<?> p_oChildToDelete) {
		this.aivDelegate.removeChild(p_oChildToDelete);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomLabel(final BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomLabel(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
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
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent#configurationAppendCustomPhone(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomPhone(BasicComponentConfiguration p_oConfiguration) {
		return this.aivDelegate.configurationAppendCustomPhone(p_oConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(NoneType p_oNoneType) {
		this.aivDelegate.configurationSetValue(p_oNoneType);
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
		return this.aivDelegate.configurationGetValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return this.aivDelegate.configurationGetCustomValues();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}

	/**
	 * {@inheritDoc}
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
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getModel() {
		return this.aivDelegate.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.aivDelegate.getName();
	}

	/**
	 * {@inheritDoc}
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
	 */
	@Override
	public boolean isNullOrEmptyValue(NoneType p_oObject) {
		return p_oObject == null;
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
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
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
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}

	/**
	 * {@inheritDoc}
	 */
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
