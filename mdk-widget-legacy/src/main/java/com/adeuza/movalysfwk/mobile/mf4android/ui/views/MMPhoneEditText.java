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
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * This view group represents a phone number edit text, its label and its button
 * to use this component, <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPhoneTextViewGroup> </li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 * 
 */

public class MMPhoneEditText extends MMPhoneTextView implements TextWatcher, ComponentEnable {
	/**
	 * 
	 * Construct a MMPhoneEditText 
	 * @param p_oContext the context
	 */
	public MMPhoneEditText(Context p_oContext) {
		super(p_oContext, AndroidApplicationR.fwk_component_phone_editview);
		if(!isInEditMode()) {
			this.init();
		}
	}

	/**
	 * 
	 * Construct a MMPhoneEditText 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMPhoneEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, AndroidApplicationR.fwk_component_phone_editview);
		if(!isInEditMode()) {
			this.init();
		}
	}

	/**
	 * Initializes the instance of the class
	 */
	private final void init() {
		((TextView) this.findViewById(((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR
				.component_phonenumber__phonenumber__value))).addTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence p_sText, int p_iStart, int p_iCount, int p_iAfter) {
		// NOTHING TO DO
	}

	@Override
	public void onTextChanged(CharSequence p_sText, int p_iStart, int p_iBefore, int p_iCount) {
		// NOTHING TO DO
	}

	@Override
	public void afterTextChanged(Editable p_oS) {
		this.aivFwkDelegate.changed();
		boolean shouldEnableCallButton = !(p_oS == null || p_oS.toString().length() == 0);
		this.setCallButtonEnabled(shouldEnableCallButton);
	}

	/********************************************************************************************
	 ***************************** Framework delegate callback **********************************
	 ********************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		this.getUiPhoneNumer().setEnabled(p_bEnable);
	}

}
