package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>Component that represents a group of checkBoxes: One field but several values.</p>
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 * @author emalespine
 */
public class MMCheckBoxGroup<VALUE> extends LinearLayout implements ConfigurableVisualComponent<Collection<VALUE>>, InstanceStatable {

	private Map<VALUE, CheckBox> checkBoxByValue;

	private Collection<CheckBox> initialCheckedCheckBoxes;

	/** error container */
	private TextView errorContainer = null;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Collection<VALUE>> aivDelegate = null;

	/**
	 * Constructs a new MMCheckBoxGroup
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 */
	public MMCheckBoxGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.init();
		}
	}
	/**
	 * Constructs a new MMCheckBoxGroup
	 * @param p_oContext the context to use
	 */
	public MMCheckBoxGroup(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
			this.init();
		}
	}
	/**
	 * Initializes this component.
	 */
	private final void init() {
		this.setOrientation(LinearLayout.VERTICAL);

		this.checkBoxByValue			= new HashMap<>();
		this.initialCheckedCheckBoxes	= new ArrayList<>();
	}
	/**
	 * Adds a new checkBox to this group. A checkbox is defined by its label and its value.
	 * 
	 * @param p_oValue Value of the checkBox to add. Possibly a null or empty value.
	 * @param p_sLabel Label of the checkBox to add. Possibly a empty string.
	 */
	public void addCheckBox(VALUE p_oValue, String p_sLabel) {
		CheckBox oCheckBox = new CheckBox(this.getContext());
		oCheckBox.setText(p_sLabel);
		this.addView(oCheckBox);
		this.checkBoxByValue.put(p_oValue, oCheckBox);
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
	public void configurationSetValue(Collection<VALUE> p_oValues) {
		for (Entry<VALUE, CheckBox> oEntry : this.checkBoxByValue.entrySet()) {
			oEntry.getValue().setChecked(false);
		}

		CheckBox oCheckBox = null;
		for (VALUE oValue : p_oValues) {
			oCheckBox = this.checkBoxByValue.get(oValue);
			if (oCheckBox != null) {
				oCheckBox.setChecked(true);
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_t_sValues) {
		//initialisation à vide obligatoire
		this.initialCheckedCheckBoxes.clear();
		for(Entry<VALUE, CheckBox> oEntry : this.checkBoxByValue.entrySet()){
			oEntry.getValue().setChecked(false);
		}

		if (p_t_sValues != null) {
			CheckBox oCheckBox = null;
			for (String sValue : p_t_sValues) {
				oCheckBox = this.checkBoxByValue.get(sValue);
				if (oCheckBox != null) {
					oCheckBox.setChecked(true);
					this.initialCheckedCheckBoxes.add(oCheckBox);
				}
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<VALUE> configurationGetValue() {
		Collection<VALUE> r_oValues = new ArrayList<>();
		for (Entry<VALUE, CheckBox> oEntry : this.checkBoxByValue.entrySet()) {
			if (oEntry.getValue().isChecked()) {
				r_oValues.add(oEntry.getKey());
			}
		}
		return r_oValues;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		Collection<String> oValues = new ArrayList<>();
		for (Entry<VALUE, CheckBox> oEntry : this.checkBoxByValue.entrySet()) {
			if (oEntry.getValue().isChecked()) {
				oValues.add(String.valueOf(oEntry.getKey()));
			}
		}
		return oValues.toArray(new String[oValues.size()]);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return Collection.class;
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
		return this.aivDelegate.isMaster();
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();

		this.initialCheckedCheckBoxes.clear();
		for (CheckBox oCheckBox : this.checkBoxByValue.values()) {
			if (oCheckBox.isChecked()) {
				this.initialCheckedCheckBoxes.add(oCheckBox);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		boolean r_bChanged = false;

		// 1. Une des cases précédemment cochées a-t-elle été décochée?
		Iterator<CheckBox> iterInitialCheckedCheckBoxes = this.initialCheckedCheckBoxes.iterator();
		while (iterInitialCheckedCheckBoxes.hasNext() && !r_bChanged) {
			r_bChanged = !iterInitialCheckedCheckBoxes.next().isChecked();
		}

		if (!r_bChanged) {
			// 2. Une autre case a-t-elle été cochée?
			int iCheckedCount = 0;
			for (CheckBox oCheckBox : this.checkBoxByValue.values()) {
				if (oCheckBox.isChecked()) {
					iCheckedCount++;
				}
			}
			r_bChanged = iCheckedCount != this.initialCheckedCheckBoxes.size();
		}
		return r_bChanged;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(Collection<VALUE> p_oObject) {
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
		for (CheckBox oCheckBox : this.checkBoxByValue.values()) {
			oCheckBox.setEnabled(false);
		}
		this.aivDelegate.configurationDisabledComponent();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		for (CheckBox oCheckBox : this.checkBoxByValue.values()) {
			oCheckBox.setEnabled(true);
		}
		this.aivDelegate.configurationEnabledComponent();
	}
	private void initializeErrorContainer() {
		if (this.errorContainer == null) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			String sErrorContainer = oApplication.getAndroidIdStringByIntKey(this.getId());

			String sSuffix = TYPE_EDIT;
			if (sErrorContainer.endsWith(TYPE_VALUE)) {
				sSuffix = TYPE_VALUE;
			}
			sErrorContainer = sErrorContainer.substring(0, sErrorContainer.length() - sSuffix.length()).concat(TYPE_LABEL);
			this.errorContainer = (TextView) ((View) this.getParent()).findViewById(oApplication.getAndroidIdByStringKey(ApplicationRGroup.ID, sErrorContainer));
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDoOnMandatoryComponentNotFilled()
	 */
	@Override
	public void configurationSetError(String p_sError) {
		if (this.errorContainer == null) {
			this.initializeErrorContainer();
		}
		this.errorContainer.setError(p_sError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		if (this.errorContainer == null) {
			this.initializeErrorContainer();
		}
		if(this.errorContainer.getError() != null) {
			this.errorContainer.setError(null);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		boolean r_bFilled = false;

		Iterator<CheckBox> iterCheckBoxes = this.checkBoxByValue.values().iterator();
		while (iterCheckBoxes.hasNext() && !r_bFilled) {
			r_bFilled = iterCheckBoxes.next().isChecked();
		}
		return r_bFilled;
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// NOTHING TO DO
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isAlwaysEnabled()
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return false;
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
	public void doOnPostAutoBind() {
		// NOTHING TO DO
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#destroy()
	 */
	@Override
	public void destroy() {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#validate(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration, java.lang.StringBuilder)
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

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putSerializable("checkBoxByValue",(Serializable) this.configurationGetValue());
		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		this.configurationSetValue((Collection<VALUE>) r_oBundle.getSerializable("checkBoxByValue"));
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