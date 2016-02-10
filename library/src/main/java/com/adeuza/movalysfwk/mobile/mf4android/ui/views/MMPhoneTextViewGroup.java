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
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.PhoneCallCommand;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;

/**
 * This view group represents a phone number text view, its label and its button
 * to use this componnent, <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPhoneTextViewGroup> </li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 * <p>
 * Copyright (c) 2010
 * <p>
 * Company: Adeuza
 * 
 * @author dmaurange
 * @since Barcelone
 * 
 */

public class MMPhoneTextViewGroup extends AbstractMMTableLayout<String> implements MMIdentifiableView, OnClickListener {

	/** The key for the bundle in savedInstanceState */
	private final String CALL_BUTTON_ENABLED_STATE_KEY = "callButtonEnabledStateKey";
	/** The key for the bundle in savedInstanceState */
	private final String CALL_BUTTON_HIDDEN_STATE_KEY = "callButtonHiddenStateKey";
	/** The key for the bundle in savedInstanceState */
	private final String CALL_BUTTON_PHONE_VALUE_KEY = "callButtonPhoneValueKey";
	/** the command to use*/
	private final PhoneCallCommand oPhoneCommand=PhoneCallCommand.getInstance();
	/** the phone number field*/
	private TextView uiPhoneNumber;
	/** the label above the phone number*/
	private TextView uiLabel;
	/** the call button*/
	private View uiCallButton;

	/**
	 * 
	 * Construct a MMPhoneTextView 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMPhoneTextViewGroup(Context p_oContext) {
		this(p_oContext, AndroidApplicationR.fwk_component_phone_textview);
	}

	/**
	 * 
	 * Construct a MMPhoneTextView 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMPhoneTextViewGroup(Context p_oContext, AttributeSet p_oAttrs) {
		this(p_oContext, p_oAttrs, AndroidApplicationR.fwk_component_phone_textview);
	}

	/**
	 * @param p_oContext
	 * @param p_oLayout
	 */
	protected MMPhoneTextViewGroup(Context p_oContext, ApplicationR p_oLayout) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.inflate(p_oLayout);
		}
	}

	/**
	 * @param p_oContext
	 * @param p_oAttrs
	 * @param p_oLayout
	 */
	protected MMPhoneTextViewGroup(Context p_oContext, AttributeSet p_oAttrs, ApplicationR p_oLayout) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.inflate(p_oLayout);
			this.linkChildrens(p_oAttrs);
		}
	}

	/**
	 * @param p_oLayout
	 */
	private void inflate(ApplicationR p_oLayout) {

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(p_oLayout), this);

		uiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_phonenumber__phonenumber__label));
		uiPhoneNumber = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_phonenumber__phonenumber__value));
		uiCallButton = this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_phonenumber__phonenumber__button));

	}

	/**
	 * 
	 * link the child views with custom attributes and add button onClickListener
	 * @param p_oAttrs attributes of XML Component component_phone_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		if(!isInEditMode()) {
			int iValue = p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0);
			if ( iValue != 0 ) {
				this.uiLabel.setText(this.getResources().getString(iValue));
			}
		}
		setCustomConverter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.CONVERTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
		setCustomFormatter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.FORMATTER_NAME_ATTRIBUTE.getName()), p_oAttrs);

	}

	/**
	 * called wen the inflator finished the job before setting value
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.uiLabel.setText(oConfiguration.getLabel());
			}
		}
	}

	/**
	 * 
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
	 * {@inheritDoc}
	 */ 
	@Override
	public void configurationSetValue(String p_oObjectToSet) {	
		if(
				p_oObjectToSet != null && !p_oObjectToSet.equals(this.configurationGetValue()) 
				|| p_oObjectToSet == null && this.configurationGetValue() != null
		) {
			super.configurationSetValue(p_oObjectToSet);

			//Hide the call button if the current device can not use phone service.
			TelephonyManager oTelephonyManager = (TelephonyManager) this.getContext().getSystemService(Activity.TELEPHONY_SERVICE);
			boolean shouldHideButton = (
				oTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE 
				|| oTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT
			);
			setCallButtonHidden(shouldHideButton);

			if(p_oObjectToSet != null && p_oObjectToSet.length() > 0) {

				if(customFormatter() != null) {
					p_oObjectToSet = (String) customFormatter().format(p_oObjectToSet);
				}

				if (this.uiPhoneNumber instanceof EditText) {
					int selStart = ((EditText) this.uiPhoneNumber).getSelectionStart();
					int selEnd = ((EditText) this.uiPhoneNumber).getSelectionEnd();

					selStart = Math.min(selStart, p_oObjectToSet.length());
					selEnd = Math.min(selEnd, p_oObjectToSet.length());

					this.uiPhoneNumber.setText(p_oObjectToSet);
					setEditTextSelection(((EditText) this.uiPhoneNumber), selStart, selEnd);
				} else {
					this.uiPhoneNumber.setText(p_oObjectToSet);
				}

				this.setCallButtonEnabled(true);
			}
			else {
				this.setCallButtonEnabled(false);
			}
		}
	}

	private void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}
	
	/**
	 * Changing visibility of the call button
	 * @param p_bIsCallButtonHidden TRUE if the call button should be hidden, FALSE otherwhise
	 */
	protected void setCallButtonHidden(boolean p_bIsCallButtonHidden) {
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
	 * @param p_bIsCallButtonHidden TRUE if the call button should be hidden, FALSE otherwhise
	 */
	protected void setCallButtonEnabled(boolean p_bIsEnabled) {
		this.uiCallButton.setEnabled(p_bIsEnabled);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet.length == 1) {
			this.configurationSetValue(p_oObjectsToSet[0]);
		}
		else {
			this.aivDelegate.configurationSetCustomValues(p_oObjectsToSet);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		String oReturnValue = uiPhoneNumber.getText().toString();
		
		if (oReturnValue.length() == 0) {
			oReturnValue = null;
		}
		
		if(oReturnValue != null && customFormatter() != null) {
			oReturnValue = (String) customFormatter().unformat(oReturnValue);
		} 
		return oReturnValue;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#getValueType()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		String[] r_t_sPhoneNumber = null;

		String sPhoneNumber = this.configurationGetValue();
		if (sPhoneNumber != null) {
			if(customFormatter() != null) {
				sPhoneNumber = (String) customFormatter().unformat(sPhoneNumber);
			} 
			r_t_sPhoneNumber = new String[] { sPhoneNumber };
		}

		return r_t_sPhoneNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_sPhoneNumber) {
		return p_sPhoneNumber == null || p_sPhoneNumber.length() == 0;
	}

	@Override
	public boolean isFilled() {
		final CharSequence oPhoneNumber = this.uiPhoneNumber.getText();
		return oPhoneNumber != null && oPhoneNumber.length() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.uiPhoneNumber.setError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		if(this.uiPhoneNumber.getError() != null) {
			this.uiPhoneNumber.setError(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}

	protected TextView getUiPhoneNumer() {
		return this.uiPhoneNumber;
	}


	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState;
		Parcelable oParentParcelable = r_oBundle
				.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		boolean enabled = r_oBundle.getBoolean(CALL_BUTTON_ENABLED_STATE_KEY);
		boolean hidden = r_oBundle.getBoolean(CALL_BUTTON_HIDDEN_STATE_KEY);
		String oRetainedValue = r_oBundle.getString(CALL_BUTTON_PHONE_VALUE_KEY);
		if(oRetainedValue != null) {
			this.configurationSetValue(oRetainedValue);
		}
		this.setCallButtonEnabled(enabled);
		this.setCallButtonHidden(hidden);
	}


	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);

		boolean enabled = this.uiCallButton.isEnabled();
		boolean hidden = (this.uiCallButton.getVisibility() == View.GONE);

		r_oBundle.putBoolean(CALL_BUTTON_ENABLED_STATE_KEY, enabled);
		r_oBundle.putBoolean(CALL_BUTTON_HIDDEN_STATE_KEY, hidden);

		String oValueToRetain = this.uiPhoneNumber.getText().toString();
		if(this.customFormatter() != null) {
			oValueToRetain = (String) this.customFormatter().unformat(oValueToRetain);
		}
		r_oBundle.putString(CALL_BUTTON_PHONE_VALUE_KEY, oValueToRetain);

		return r_oBundle;
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
