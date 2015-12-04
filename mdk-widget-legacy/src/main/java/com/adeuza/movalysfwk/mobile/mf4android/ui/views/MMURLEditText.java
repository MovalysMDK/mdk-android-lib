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

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.UrlFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * This view group represents an url text view, its label and its button
 * to see the url on the web browser :<ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMURLTextViewGroup> </li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 */
public class MMURLEditText extends AbstractMMLinearLayout<String> implements OnClickListener, TextWatcher, 
	ComponentError, ComponentValidator, ComponentEnable, ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {
	
	/** the url field*/
	private EditText oUiURLText;
	
	/** the call button*/
	private ImageButton oUiWebBrowserButton;
	
	/** default value in the input */
	public static final String URL_DEFAULT = "http://" ;
	
	/** The key for the bundle in savedInstanceState */
	private static final String WEB_BROWSER_BUTTON_ENABLED_STATE_KEY = "webBrowserButtonEnabledStateKey";
	
	/** The key for the bundle in savedInstanceState */
	private static final String WEB_BROWSER_BUTTON_HIDDEN_STATE_KEY = "webBrowserButtonHiddenStateKey";
	
	/** the url validator */
	private UrlFormFieldValidator oValidator;
	
	/**
	 * Construct a MMURLTextViewGroup 
	 * @param p_oContext the context
	 */
	public MMURLEditText(Context p_oContext) {
		super(p_oContext, String.class);
		if(!isInEditMode()) {
			this.inflateLayout();
		}
	}
	
	/**
	 * Construct a MMURLTextViewGroup 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMURLEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, String.class);
		if(!isInEditMode()) {
			this.aivFwkDelegate.defineParameters(p_oAttrs);
			this.inflateLayout();
			this.linkChildrens(p_oAttrs);
		}
	}	

	/**
	 * Changing visibility of the call button
	 * @param p_bIsCallButtonHidden TRUE if the call button should be hidden, FALSE otherwise
	 */
	protected void setWebBrowserButtonHidden(boolean p_bIsCallButtonHidden) {
		if(p_bIsCallButtonHidden) {
			this.oUiWebBrowserButton.setOnClickListener(null);
			this.oUiWebBrowserButton.setVisibility(View.GONE);
		}
		else {
			this.oUiWebBrowserButton.setOnClickListener(this);
			this.oUiWebBrowserButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Changing visibility of the call button
	 * @param p_bIsEnabled TRUE if the call button should be hidden, FALSE otherwise
	 */
	protected void setWebBrowserButtonEnabled(boolean p_bIsEnabled) {
		this.oUiWebBrowserButton.setEnabled(p_bIsEnabled);
	}

	/**
	 * inflates the view and finds the inner components
	 */
	private void inflateLayout() {
		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_url_editview), this, true);

			oUiURLText = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__value));
			oUiURLText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
			oUiURLText.addTextChangedListener(this);

			oUiWebBrowserButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__button));
			oUiWebBrowserButton.setVisibility(View.VISIBLE);
			oUiWebBrowserButton.setOnClickListener(this);
			
			this.oValidator = BeanLoader.getInstance().getBean(UrlFormFieldValidator.class);
		}
	}

	/**
	 * link the child views with custom attributes and add button onClickListener
	 * @param p_oAttrs attributes of XML Component component_url_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		this.aivFwkDelegate.setCustomConverter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.CONVERTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
		this.aivFwkDelegate.setCustomFormatter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.FORMATTER_NAME_ATTRIBUTE.getName()), p_oAttrs);

		this.oValidator.addParametersToConfiguration(this.aivFwkDelegate.getAttributes(), (BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration());
		
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {

		if(!isInEditMode()) {
			oUiURLText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
			super.onFinishInflate();
			oUiURLText.addTextChangedListener(this);
			oUiWebBrowserButton.setOnClickListener(this);
		}
	}

	/**
	 * On click listener to deal with the click on the Call button
	 * @param p_oArg0 the view that was clicked
	 */
	@Override
	public void onClick(View p_oArg0) {
		String sURL = this.aivDelegate.configurationGetValue();
		if (sURL!=null && sURL.length() > 0 ){
			((AndroidApplication) Application.getInstance()).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sURL)));
		}
	}
	
	/**
	 * Sets some selected text on the component 
	 * @param p_oEditText the EditText view on which the text will be made selected
	 * @param p_iStart the start index of the selection
	 * @param p_iStop the stop index of the selection
	 */
	public void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}
	
	/**
	 * Returns the component attributes
	 * @return the component attributes
	 */
	public Map<Attribute, String> getAttributes() {
		return this.aivFwkDelegate.getAttributes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("urlTextView", this.oUiURLText.getText().toString());
		r_oBundle.putBoolean(WEB_BROWSER_BUTTON_ENABLED_STATE_KEY, this.oUiWebBrowserButton.isEnabled());
		r_oBundle.putBoolean(WEB_BROWSER_BUTTON_HIDDEN_STATE_KEY, this.oUiWebBrowserButton.getVisibility() == View.GONE);
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);
		this.aivDelegate.configurationSetValue(r_oBundle.getString("urlTextView"));

		boolean enabled = r_oBundle.getBoolean(WEB_BROWSER_BUTTON_ENABLED_STATE_KEY);
		boolean hidden = r_oBundle.getBoolean(WEB_BROWSER_BUTTON_HIDDEN_STATE_KEY);

		setWebBrowserButtonEnabled(enabled);
		setWebBrowserButtonHidden(hidden);
	}

	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable p_oArg0) {
		if (this.aivDelegate != null && !this.aivDelegate.isWritingData()) {
			this.aivFwkDelegate.changed();
			boolean isValid = this.aivDelegate.validate((BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration(), null,new StringBuilder()) && this.aivDelegate.isFilled();
			this.setWebBrowserButtonEnabled(isValid); 
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence p_oArg0, int p_oArg1, int p_oArg2, int p_oArg3) {
		//nothing to do 
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(CharSequence p_oArg0, int p_oArg1, int p_oArg2, int p_oArg3) {
		//nothing to do
	}
	
	/***********************************************************************************************
	 ************************************** Framework Interface ************************************
	 ***********************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextView getErrorView() {
		return this.oUiURLText;
	}
	
	@Override
	public IFormFieldValidator getValidator() {
		return this.oValidator;
	}
	
	@Override
	public void enableComponent(boolean p_bEnable) {
		if (this.oUiURLText != null) {
			this.oUiURLText.setEnabled(p_bEnable);
		}
	}
	
	@Override
	public String configurationGetValue() {
		if (URL_DEFAULT.equalsIgnoreCase(this.oUiURLText.getText().toString())) {
			return StringUtils.EMPTY;
		}
		String oReturnValue = this.oUiURLText.getText().toString();
		if (oReturnValue.length() == 0) {
			oReturnValue = null;
		}
		oReturnValue = this.aivDelegate.getFormattedValue(oReturnValue);
		return oReturnValue;
	}
	
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		if (this.aivDelegate.isNullOrEmptyValue(p_oObjectToSet)) {
			this.oUiURLText.setText(URL_DEFAULT);
			this.setWebBrowserButtonEnabled(false); 
		} else {
			String oString = (String) this.aivDelegate.getFormattedValue(p_oObjectToSet);
			int iOldStart = this.oUiURLText.getSelectionStart();
			int iOldStop = this.oUiURLText.getSelectionEnd();
			iOldStart = Math.min(iOldStart, (oString).length());
			iOldStop = Math.min(iOldStop, (oString).length());
			this.oUiURLText.setText(oString);
			this.setEditTextSelection(this.oUiURLText, iOldStart, iOldStop);
		}
	}
	
	@Override
	public boolean isFilled() {
		return this.configurationGetValue() != null && this.configurationGetValue().length() > 0;
	}

}
