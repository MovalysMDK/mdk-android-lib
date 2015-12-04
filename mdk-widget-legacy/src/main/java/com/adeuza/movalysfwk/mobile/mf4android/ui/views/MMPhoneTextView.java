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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.PhoneCallCommand;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;

/**
 * This view group represents a phone number text view, its label and its button
 * to use this component, <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPhoneTextViewGroup> </li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 */

public class MMPhoneTextView extends AbstractMMLinearLayout<String> implements OnClickListener, ComponentError, 
ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {

	/** The key for the bundle in savedInstanceState */
	private static final String CALL_BUTTON_ENABLED_STATE_KEY = "callButtonEnabledStateKey";
	
	/** The key for the bundle in savedInstanceState */
	private static final String CALL_BUTTON_HIDDEN_STATE_KEY = "callButtonHiddenStateKey";
	
	/** The key for the bundle in savedInstanceState */
	private static final String CALL_BUTTON_PHONE_VALUE_KEY = "callButtonPhoneValueKey";
	
	/** the command to use*/
	private final PhoneCallCommand oPhoneCommand = PhoneCallCommand.getInstance();
	
	/** the phone number field*/
	private TextView uiPhoneNumber;
	
	/** the call button*/
	private View uiCallButton;

	/**
	 * Construct a MMPhoneTextViewGroup 
	 * @param p_oContext the context to use
	 */
	public MMPhoneTextView(Context p_oContext) {
		this(p_oContext, AndroidApplicationR.fwk_component_phone_textview);
	}

	/**
	 * Construct a MMPhoneTextViewGroup
	 * @param p_oContext the context to use
	 * @param p_oAttrs the xml attributes to set
	 */
	public MMPhoneTextView(Context p_oContext, AttributeSet p_oAttrs) {
		this(p_oContext, p_oAttrs, AndroidApplicationR.fwk_component_phone_textview);
	}

	/**
	 * Construct a MMPhoneTextViewGroup
	 * @param p_oContext the context to use
	 * @param p_oLayout the layout of the component
	 */
	protected MMPhoneTextView(Context p_oContext, ApplicationR p_oLayout) {
		super(p_oContext, String.class);
		if(!isInEditMode()) {
			this.inflate(p_oLayout);
		}
	}

	/**
	 * Construct a MMPhoneTextViewGroup
	 * @param p_oContext the context to use
	 * @param p_oAttrs the xml attributes to set
	 * @param p_oLayout the layout of the component
	 */
	protected MMPhoneTextView(Context p_oContext, AttributeSet p_oAttrs, ApplicationR p_oLayout) {
		super(p_oContext, p_oAttrs, String.class);
		if(!isInEditMode()) {
			this.inflate(p_oLayout);
		}
	}

	/**
	 * inflates the component with the given layout
	 * @param p_oLayout the layout to use to inflate the component
	 */
	private final void inflate(ApplicationR p_oLayout) {

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(p_oLayout), this);

		uiPhoneNumber = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_phonenumber__phonenumber__value));
		uiCallButton = this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_phonenumber__phonenumber__button));

		//Hide the call button if the current device can not use phone service.
		TelephonyManager oTelephonyManager = (TelephonyManager) this.getContext().getSystemService(Activity.TELEPHONY_SERVICE);
		boolean shouldHideButton = (
			oTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE 
			|| oTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT
		);
		this.setCallButtonHidden(shouldHideButton);

	}

	/**
	 * On click listener to deal with the click on the Call button
	 * @param p_oArg0 the view that was clicked
	 */
	@Override
	public void onClick(View p_oArg0) {
		String sNumero = this.uiPhoneNumber.getText().toString();
		if (sNumero != null && sNumero.length() > 0) {
			this.oPhoneCommand.dial(sNumero, this.getContext());
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
	 * Changing visibility of the call button
	 * @param p_bIsCallButtonHidden TRUE if the call button should be hidden, FALSE otherwise
	 */
	public void setCallButtonHidden(boolean p_bIsCallButtonHidden) {
		if(p_bIsCallButtonHidden) {
			this.uiCallButton.setOnClickListener(null);
			this.uiCallButton.setVisibility(View.GONE);
		}
		else {
			this.uiCallButton.setOnClickListener(this);
			this.uiCallButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Changing visibility of the call button
	 * @param p_bIsEnabled TRUE if the call button should be hidden, FALSE otherwise
	 */
	public void setCallButtonEnabled(boolean p_bIsEnabled) {
		this.uiCallButton.setEnabled(p_bIsEnabled);
	}

	/**
	 * Returns the view displaying the phone number
	 * @return the view displaying the phone number
	 */
	public TextView getUiPhoneNumer() {
		return this.uiPhoneNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState;
		Parcelable oParentParcelable = r_oBundle
				.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		boolean enabled = r_oBundle.getBoolean(CALL_BUTTON_ENABLED_STATE_KEY);
		boolean hidden = r_oBundle.getBoolean(CALL_BUTTON_HIDDEN_STATE_KEY);
		String oRetainedValue = r_oBundle.getString(CALL_BUTTON_PHONE_VALUE_KEY);
		if(oRetainedValue != null) {
			this.aivDelegate.configurationSetValue(oRetainedValue);
		}
		this.setCallButtonEnabled(enabled);
		this.setCallButtonHidden(hidden);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);

		boolean enabled = this.uiCallButton.isEnabled();
		boolean hidden = (this.uiCallButton.getVisibility() == View.GONE);

		r_oBundle.putBoolean(CALL_BUTTON_ENABLED_STATE_KEY, enabled);
		r_oBundle.putBoolean(CALL_BUTTON_HIDDEN_STATE_KEY, hidden);

		String oValueToRetain = this.uiPhoneNumber.getText().toString();
		if(this.aivFwkDelegate.customFormatter() != null) {
			oValueToRetain = (String) this.aivFwkDelegate.customFormatter().unformat(oValueToRetain);
		}
		r_oBundle.putString(CALL_BUTTON_PHONE_VALUE_KEY, oValueToRetain);

		return r_oBundle;
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
		return this.uiPhoneNumber;
	}

	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		String oReturnValue = uiPhoneNumber.getText().toString();
		
		if (oReturnValue.length() == 0) {
			oReturnValue = null;
		}
		
		if(oReturnValue != null && this.getComponentFwkDelegate().customFormatter() != null) {
			oReturnValue = (String) this.getComponentFwkDelegate().customFormatter().unformat(oReturnValue);
		} 
		return oReturnValue;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		if (this.aivDelegate.isNullOrEmptyValue(p_oObjectToSet)) {
			this.setCallButtonEnabled(false);
		} else {
			String oObjectToSet = p_oObjectToSet;
			
			if(this.getComponentFwkDelegate().customFormatter() != null) {
				oObjectToSet = (String) this.getComponentFwkDelegate().customFormatter().format(oObjectToSet);
			}

			if (this.uiPhoneNumber instanceof EditText) {
				int selStart = ((EditText) this.uiPhoneNumber).getSelectionStart();
				int selEnd = ((EditText) this.uiPhoneNumber).getSelectionEnd();

				selStart = Math.min(selStart, (oObjectToSet).length());
				selEnd = Math.min(selEnd, (oObjectToSet).length());

				this.uiPhoneNumber.setText(oObjectToSet);
				this.setEditTextSelection(((EditText) this.uiPhoneNumber), selStart, selEnd);
			} else {
				this.uiPhoneNumber.setText(oObjectToSet);
			}

			this.setCallButtonEnabled(true);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		final CharSequence oPhoneNumber = this.uiPhoneNumber.getText();
		return oPhoneNumber != null && oPhoneNumber.length() > 0;
	}
}
