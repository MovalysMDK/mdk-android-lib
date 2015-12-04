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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.ArrayList;

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
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * <p>
 * MultiAutoCompleteTextView widget used in the Movalys Mobile product for Android
 * </p>
 * 
 */

public class MMMultiAutoCompleteEditTextWithDialog extends AbstractMMLinearLayout<String> implements ConfigurableVisualComponent, OnClickListener, MMComplexeComponent, TextWatcher,
	ComponentError, ComponentEnable, ComponentBindDestroy, 
	ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {

	/**	 Number of character to launch the completion */
	private static final int DEFAULT_AUTOCOMPLETE_THRESHOLD = 4;
	
	/** the multiautocomplete field*/
	private MultiAutoCompleteTextView oUiMMMultiEditText;
	
	/** button to erase the content of the field*/
	private ImageButton oUiBtnErase;
	
	/** button to use the voice recognition if available on the device*/
	private ImageButton oUiBtnSpeackNow;
	
	/** Application	 */
	private AndroidApplication application;
	
	/** Key to retain visibility state of recognition button */
	private static final String RECOGNITION_BUTTON_HIDDEN_KEY = "recognitionButtonHiddenKey";
	
	/** Key to retain the activation state (enable/disable) state of erase button */
	private static final String ERASE_BUTTON_STATE_KEY = "eraseButtonStateKey";
	
	/** Key to retain the current value of the MultiAutoCompletTextView */
	private static final String MULTI_AUTO_COMPLETE_VALUE_KEY = "multiAutoCompleteValueKey";
	
	/**
	 * Component request code
	 */
	private int requestCode = -1;
	
	/**
	 * Constructs a new MMMultiAutoCompleteEditTextWithDialog
	 * @param p_oContext the context to use
	 * @see MMMultiAutoCompleteEditTextWithDialog#MMMultiAutoCompleteEditTextWithDialog
	 */
	public MMMultiAutoCompleteEditTextWithDialog(Context p_oContext) {
		super(p_oContext, String.class);
		if(!isInEditMode()) {
			this.application = (AndroidApplication)(Application.getInstance());
		}

	}	
	/**
	 * Constructs a new MMMultiAutoCompleteEditTextWithDialog
	 * 
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see MMMultiAutoCompleteEditTextWithDialog#MMMultiAutoCompleteEditTextWithDialog
	 */
	public MMMultiAutoCompleteEditTextWithDialog(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, String.class);
		if(!isInEditMode()) {
			this.application = (AndroidApplication)(Application.getInstance());
			this.linkChildrens(p_oAttrs);
		}
	}
	
	/**
	 * link the child views with custom attributes and add button onClickListener
	 * 
	 * @param p_oAttrs attributes of XML Component component_phone_textview
	 */
	private final void linkChildrens(AttributeSet p_oAttrs) {
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		oInflater.inflate(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_multiautocomplete_edittext), this);

		oUiMMMultiEditText = (MultiAutoCompleteTextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_multiautocomplete__editText__edit));
		oUiBtnErase = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_multiautocomplete__btnErase__button));
		oUiBtnSpeackNow = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_multiautocomplete__btnSpeackNow__button));
		
		int iCompletionActivated=DEFAULT_AUTOCOMPLETE_THRESHOLD;
		
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
	 */
	@Override
	public void onClick(View p_oArg0) {
		if (p_oArg0.equals(oUiBtnErase)) {
			oUiMMMultiEditText.setText("");
			setEraseButtonEnabled(false);
		}
		if (p_oArg0.equals(oUiBtnSpeackNow)) {
			this.application.startVoiceRecognitionActivity( this.getId() , this.getRequestCode(), this );
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
			this.aivFwkDelegate.changed();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
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
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments) {
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
		r_oBundle.putInt("requestCode", this.requestCode);
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		this.aivDelegate.setWritingData(true);
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);
		this.aivDelegate.configurationSetValue(r_oBundle.getString("urlTextView"));
		// because configurationSetValue set writingData to false
		this.aivDelegate.setWritingData(true);
		boolean eraseEnabled = r_oBundle.getBoolean(ERASE_BUTTON_STATE_KEY);
		boolean recognitionHidden = r_oBundle.getBoolean(RECOGNITION_BUTTON_HIDDEN_KEY);
		String value = r_oBundle.getString(MULTI_AUTO_COMPLETE_VALUE_KEY);

		setEraseButtonEnabled(eraseEnabled);
		setRecognitionButtonHidden(recognitionHidden);
		this.oUiMMMultiEditText.setText(value);
		
		this.requestCode = r_oBundle.getInt("requestCode");
		
		this.aivDelegate.setWritingData(false);
	}

	@Override
	public void beforeTextChanged(CharSequence p_sSeq, int p_iStart, int p_iCount, int p_iAfter) {
		// Nothing to do
	}

	@Override
	public void onTextChanged(CharSequence p_oText, int p_oStart, int p_oBefore, int p_oAfter) {
		if (this.aivFwkDelegate!=null) {
			if ( !this.aivDelegate.isWritingData() ) {
				this.aivFwkDelegate.changed();
			}
			setEraseButtonEnabled(p_oText != null && p_oText.length() > 0);
		}
	}

	@Override
	public void afterTextChanged(Editable p_oS) {
		// Nothing to do

	}
	
	/************************************************************************************************
	 ********************************** Framework delegate callback *********************************
	 ************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		return this.oUiMMMultiEditText;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		this.oUiBtnErase.setEnabled(p_bEnable);
		this.oUiBtnSpeackNow.setEnabled(p_bEnable);
		this.oUiMMMultiEditText.setEnabled(p_bEnable);
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
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
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		this.oUiMMMultiEditText.setText(p_oObjectToSet);
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.oUiMMMultiEditText.getText().toString().length()>0;
	}

	@Override
	public void doOnPostAutoBind() {
		// fix android memory leak
		if (this.oUiMMMultiEditText.getHint() == null || this.oUiMMMultiEditText.getHint().equals("") ) {
			this.oUiMMMultiEditText.setHint(" ");
		}
	}

	@Override
	public void destroy() {
		// nothing to do
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}

