package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * This view group represents a phone number edit text, its label and its button
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

public class MMPhoneEditText extends MMPhoneTextViewGroup implements TextWatcher {
	/**
	 * 
	 * Construct a MMPhoneTextView 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMPhoneEditText(Context p_oContext) {
		super(p_oContext, AndroidApplicationR.fwk_component_phone_editview);
		if(!isInEditMode()) {
			this.init();
		}
	}

	/**
	 * 
	 * Construct a MMPhoneTextView 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMPhoneEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, AndroidApplicationR.fwk_component_phone_editview);
		if(!isInEditMode()) {
			this.init();
		}
	}

	private final void init() {
		((TextView) this.findViewById(((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR
				.component_phonenumber__phonenumber__value))).addTextChangedListener(this);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
		this.getUiPhoneNumer().setFocusable(false);
		this.getUiPhoneNumer().setFocusableInTouchMode(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
		this.getUiPhoneNumer().setFocusable(true);
		this.getUiPhoneNumer().setFocusableInTouchMode(true);
	}

	@Override
	public void beforeTextChanged(CharSequence p_sText, int p_iStart, int p_iCount, int p_iAfter) {
		// NOTHING TO DO

	}

	@Override
	public void onTextChanged(CharSequence p_sText, int p_iStart, int p_iBefore, int p_iCount) {
	}

	@Override
	public void afterTextChanged(Editable p_oS) {
		this.aivDelegate.changed();
		boolean shouldEnableCallButton = !(p_oS == null || p_oS.toString().length() == 0);
		this.setCallButtonEnabled(shouldEnableCallButton);
	}


}