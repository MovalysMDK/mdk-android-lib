package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>MultiAutoCompleteTextView widget used in the Movalys Mobile product for Android</p>
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author dmaurange
 *
 */

public class MMMultiAutoCompleteEditText extends MultiAutoCompleteTextView 
implements ConfigurableVisualComponent<String>, MMIdentifiableView, InstanceStatable {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;

	private boolean writingData;

	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * @param p_oContext the context to use
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView(Context)
	 */
	public MMMultiAutoCompleteEditText(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
		}
	}

	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView(
	 */
	public MMMultiAutoCompleteEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
		}
	}


	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use 
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView(
	 */
	public MMMultiAutoCompleteEditText(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
		}
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
	public void configurationSetValue(String p_oObject) {
		this.aivDelegate.configurationSetValue(p_oObject);
		this.setError(null);
		this.writingData = true;
		if (p_oObject!=null) {
			this.setText(p_oObject);
		}
		else {
			this.setText(StringUtils.EMPTY);
		}
		this.writingData = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet.length == 1) {
			this.aivDelegate.configurationSetValue(p_oObjectsToSet[0]);
		}
		else {
			this.setText(StringUtils.EMPTY);
		}
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
		return false;
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
	public String getModel() {
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
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		String sReturnValue = this.getText().toString().trim();
		
		if (sReturnValue.length() == 0) {
			sReturnValue = null;
		}
		
		return sReturnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] { this.configurationGetValue() };
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#resetChanged()
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isChanged()
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
	public boolean isNullOrEmptyValue(String p_oObject) {
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
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

	@Override
	public void configurationUnsetError() {
		if(this.getError() != null) {
			this.setError(null);
		}
	}

	@Override
	public void configurationSetError(String p_oError) {
		this.setError(p_oError);
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.getText().toString().length()>0;
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
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivDelegate.onRestoreInstanceState(p_oState);
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

	@Override
	protected void onTextChanged(CharSequence p_oText, int p_oStart,
			int p_oBefore, int p_oAfter) {
		super.onTextChanged(p_oText, p_oStart, p_oBefore, p_oAfter);
		if (!this.writingData && this.aivDelegate!=null) {
			// the group is parent of parent
			if (this.getParent()!= null && this.getParent().getParent() instanceof MMMultiAutoCompleteEditTextGroup) {
				MMMultiAutoCompleteEditTextGroup groupChange = (MMMultiAutoCompleteEditTextGroup) this.getParent().getParent();
			} else {
				this.aivDelegate.changed();
			}
		}
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
