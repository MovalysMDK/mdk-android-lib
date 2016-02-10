package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>
 * MultiAutoCompleteTextView widget used in the Movalys Mobile product for Android
 * </p>
 * 
 * <p>
 * Copyright (c) 2010
 * <p>
 * Company: Adeuza
 * 
 * @author dmaurange
 * 
 */

public class MMMultiAutoCompleteEditTextGroup extends AbstractMMLinearLayout<String> implements ConfigurableVisualComponent<String>, 
MMIdentifiableView, OnClickListener, MMComplexeComponent, TextWatcher {

	/**	 Number of character to launch the completion */
	private static final int DEFAULT_AUTOCOMPLETE_THRESHOLD = 4;
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;
	/** the label above the MultiAutoComplete field*/
	private TextView oUiLabel;
	/** the multiautocpmplete field*/
	private MultiAutoCompleteTextView oUiMMMultiEditText;
	/** button to erase the content of the field*/
	private ImageButton oUiBtnErase;
	/** button to use the voice recognition if available on the device*/
	private ImageButton oUiBtnSpeackNow;
	/** completion threshold */
	private int iCompletionActivated=DEFAULT_AUTOCOMPLETE_THRESHOLD;
	/** Application	 */
	private AndroidApplication application;
	/** Request code of the activity */
	public static final int REQUEST_CODE = Math.abs(MMMultiAutoCompleteEditTextGroup.class.getSimpleName().hashCode()) & NumericConstants.HEXADECIMAL_MASK;
	/** Key to retain visibility state of recognition button */
	private static final String RECOGNITION_BUTTON_HIDDEN_KEY = "recognitionButtonHiddenKey";
	/** Key to retain the activation state (enable/disable) state of erase button */
	private static final String ERASE_BUTTON_STATE_KEY = "eraseButtonStateKey";
	/** Key to retain the current value of the MultiAutoCompletTextView */
	private static final String MULTI_AUTO_COMPLETE_VALUE_KEY = "multiAutoCompleteValueKey";

	private boolean writingData;
	
	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView(
	 */
	public MMMultiAutoCompleteEditTextGroup(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
			this.application = (AndroidApplication)(Application.getInstance());
			this.writingData = false;
		}

	}	
	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView(
	 */
	public MMMultiAutoCompleteEditTextGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.application = (AndroidApplication)(Application.getInstance());
			this.writingData = false;
			this.linkChildrens(p_oAttrs);
		}
	}
	/*
	 * 
	 * link the child views with custom attributes and add button onClickListener
	 * 
	 * @param p_oAttrs
	 *            attributes of XML Component component_phone_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		oInflater.inflate(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_multiautocomplete_edittext), this);

		oUiLabel = (TextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_multiautocomplete__editText__label));
		oUiMMMultiEditText = (MultiAutoCompleteTextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_multiautocomplete__editText__edit));
		oUiBtnErase = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_multiautocomplete__btnErase__button));
		oUiBtnSpeackNow = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_multiautocomplete__btnSpeackNow__button));
		try {
			oUiLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0)));
		} catch (NotFoundException e) {
			this.application.getLog().debug("MMMultiautocompleteEditTextGroup",
					StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0))));
		}
		try {
			iCompletionActivated = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "autocomplete", DEFAULT_AUTOCOMPLETE_THRESHOLD);
		} catch (NotFoundException e) {
			this.application.getLog().debug("MMMultiautocompleteEditTextGroup",
					StringUtils.concat("Parameter not found", String.valueOf(p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "autocomplete", DEFAULT_AUTOCOMPLETE_THRESHOLD))));
		}

		oUiMMMultiEditText.setThreshold(iCompletionActivated);
		oUiMMMultiEditText.addTextChangedListener(this);
		oUiBtnErase.setOnClickListener(this);
		if (this.application.isRecognitionInstalled()) {
			oUiBtnSpeackNow.setOnClickListener(this);
		} else {
			oUiBtnSpeackNow.setVisibility(View.GONE);
		}
	}

	/**
	 * Set the visibility of the recognition button
	 * @param p_bIsHidden Indicates if the button should be hidden
	 */
	private void setRecognitionButtonHidden(boolean p_bIsHidden) {
		if(p_bIsHidden) {
			this.oUiBtnSpeackNow.setVisibility(View.GONE);
		}
		else {
			this.oUiBtnSpeackNow.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Set the activation state for the erase button (enabled/disabled).
	 * @param p_bIsEnabled Indicates if the button should be enabled
	 */
	private void setEraseButtonEnabled(boolean p_bIsEnabled) {
		this.oUiBtnErase.setEnabled(p_bIsEnabled);
	}


	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.oUiLabel.setText(oConfiguration.getLabel());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oArg0) {
		if (p_oArg0.equals(oUiBtnErase)) {
			oUiMMMultiEditText.setText("");
			setEraseButtonEnabled(false);
		}
		if (p_oArg0.equals(oUiBtnSpeackNow)) {
			this.application.startVoiceRecognitionActivity( this.getId() , REQUEST_CODE, this );
		}
	}
	/////////////////////////
	// ZONE VALUEABLE VIEW //
	/////////////////////////	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode, Intent p_oIntent) {
		if (p_oResultCode==Activity.RESULT_OK){
			// Fill the field with the strings the recognizer thought it could have heard
			ArrayList<String> listMatches = p_oIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); 
			this.oUiMMMultiEditText.append(listMatches.get(0));
			this.aivDelegate.changed();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		this.writingData = true ;
		this.oUiMMMultiEditText.setText(p_oObjectToSet);
		this.writingData = false ;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet.length == 1) {
			this.configurationSetValue(p_oObjectsToSet[0]);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		String sReturnValue = this.oUiMMMultiEditText.getText().toString();
		
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
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
	/**
	 * This panel changed when oUiMMMultiEditText changed
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ext.ui.views.movalys.interventionproducts.mm.android.customviews.MMLinearLayout#isChanged()
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#resetChanged()
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}
	///////////////////
	// ZONE DELEGATE //
	///////////////////	
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_oObject) {
		return (null==oUiMMMultiEditText.getText().toString()) || (oUiMMMultiEditText.getText().toString().length() == 0);
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
		this.oUiBtnErase.setEnabled(false);
		this.oUiBtnSpeackNow.setEnabled(false);
		this.oUiMMMultiEditText.setEnabled(false);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.oUiBtnErase.setEnabled(true);
		this.oUiBtnSpeackNow.setEnabled(true);
		this.oUiMMMultiEditText.setEnabled(true);
	}
	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.oUiMMMultiEditText.getText().toString().length()>0;
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
		this.aivDelegate.configurationRemoveMandatoryLabel();
	}	
	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		this.aivDelegate.configurationSetMandatoryLabel();
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
		if(this.oUiMMMultiEditText.getError() != null) {
			this.oUiMMMultiEditText.setError(null);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
		this.oUiMMMultiEditText.setError(p_oError);
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
	 */
	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return null;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		// Nothing to do
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(MULTI_AUTO_COMPLETE_VALUE_KEY, this.oUiMMMultiEditText.getText().toString());
		r_oBundle.putBoolean(ERASE_BUTTON_STATE_KEY, this.oUiBtnErase.isEnabled());
		r_oBundle.putBoolean(RECOGNITION_BUTTON_HIDDEN_KEY, this.oUiBtnSpeackNow.getVisibility() == View.GONE);
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		this.writingData = true;
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);
		configurationSetValue(r_oBundle.getString("urlTextView"));
		// because configurationSetValue set writingData to false
		this.writingData = true;
		boolean eraseEnabled = r_oBundle.getBoolean(ERASE_BUTTON_STATE_KEY);
		boolean recognitionHidden = r_oBundle.getBoolean(RECOGNITION_BUTTON_HIDDEN_KEY);
		String value = r_oBundle.getString(MULTI_AUTO_COMPLETE_VALUE_KEY);

		setEraseButtonEnabled(eraseEnabled);
		setRecognitionButtonHidden(recognitionHidden);
		this.oUiMMMultiEditText.setText(value);
		
		this.writingData = false;
	}

	@Override
	public void beforeTextChanged(CharSequence p_sSeq, int p_iStart, int p_iCount,
			int p_iAfter) {
		// Nothing to do

	}

	@Override
	public void onTextChanged(CharSequence p_oText, int p_oStart, int p_oBefore, int p_oAfter) {
		if (this.aivDelegate!=null) {
			if ( !this.writingData ) {
				this.aivDelegate.changed();
			}
			setEraseButtonEnabled(p_oText != null && p_oText.length() > 0);
		}
	}

	@Override
	public void afterTextChanged(Editable p_oS) {
		// Nothing to do

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
}

