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

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;

/**
 * Define the abstract class for email component that can be edited
 *
 * @param <T> the parameter type used in the email component
 */
public abstract class AbstractEmailEditText<T> extends AbstractEmailMasterView<T> implements TextWatcher {

	/**
	 * Create a new AbstractEmailTextView
	 * @param p_oContext the android context
	 * @param p_oDelegateType the class of the component type
	 */
	public AbstractEmailEditText(Context p_oContext, Class<?> p_oDelegateType) {
		super(p_oContext, p_oDelegateType);	
		if(!isInEditMode()) {
			this.linkLayout();
		}
	}

	/**
	 * Create a new AbstractEmailTextView
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 * @param p_oDelegateType the class of the component type
	 */
	public AbstractEmailEditText(Context p_oContext, AttributeSet p_oAttrs, Class<?> p_oDelegateType) {
		super(p_oContext, p_oAttrs, p_oDelegateType);
		if(!isInEditMode()) {
			this.linkLayout();
		}
	}
	
	/**
	 * Inflate the component layout
	 */
	private void linkLayout(){
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_email_simpletextedit), this, true);
		
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		((EditText) this.oUiEmail).addTextChangedListener(this);
	}

	/**
	 * Set the selection on the EditText
	 * @param p_oEditText the edit text to set seletion
	 * @param p_iStart the start index of the selection
	 * @param p_iStop the stop index of the selection
	 */
	public void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable p_oArg0) {
		if (this.aivDelegate != null && !this.aivDelegate.isWritingData()) {
			this.aivFwkDelegate.changed();
			boolean isValid = this.aivDelegate.validate((BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration(), null, new StringBuilder());
			this.setMailClientButtonEnabled(isValid);
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
	
	/********************************************************************************************
	 ***************************** Framework delegate callback **********************************
	 ********************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(T p_oObjectToSet) {
		this.setMailClientButtonHidden(!((AndroidApplication) Application .getInstance()).isMailAvailable());

		if (this.aivDelegate.isNullOrEmptyValue(p_oObjectToSet)) {
			oUiEmail.setText(null);
			this.setMailClientButtonEnabled(false);
		} else {
			T valuedObject = p_oObjectToSet;
			if(this.getComponentFwkDelegate().customFormatter() != null) {
				valuedObject = (T) this.getComponentFwkDelegate().customFormatter().format(p_oObjectToSet); 
			}
			
			String sValue = this.valueToString(valuedObject);
			
			int iOldStart = oUiEmail.getSelectionStart();
			int iOldStop = oUiEmail.getSelectionEnd();
			iOldStart = Math.min(iOldStart, sValue.length());
			iOldStop = Math.min(iOldStop, sValue.length());
			oUiEmail.setText(sValue);
			this.setEditTextSelection((EditText) oUiEmail, iOldStart, iOldStop);
			this.setMailClientButtonEnabled(true);
		}
	}
	
}
