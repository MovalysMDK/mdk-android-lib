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
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * Define the abstract class for email component that cannot be edited
 *
 * @param <T> the parameter type used in the email component
 */
public abstract class AbstractEmailTextView<T> extends AbstractEmailMasterView<T> {

	/**
	 * Create a new AbstractEmailTextView
	 * @param p_oContext the android context
	 * @param p_oDelegateType the class of the component type
	 */
	public AbstractEmailTextView(Context p_oContext, Class<?> p_oDelegateType) {
		super(p_oContext, p_oDelegateType);
		if(!isInEditMode()) {
			this.linkLayout();
		}
	}

	/**
	 * 
	 * Create a new AbstractEmailTextView
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 * @param p_oDelegateType the class of the component type
	 */
	public AbstractEmailTextView(Context p_oContext, AttributeSet p_oAttrs, Class<?> p_oDelegateType) {
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

		oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_email_simpletextview), this);
	}
	
	/********************************************************************************************
	 ***************************** Framework delegate callback **********************************
	 ********************************************************************************************/

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void configurationSetValue(T p_oObjectToSet) {
		
		this.setMailClientButtonHidden(!((AndroidApplication) Application .getInstance()).isMailAvailable());

		if (this.aivDelegate.isNullOrEmptyValue(p_oObjectToSet)) {
			oUiEmail.setText(null);
			this.setMailClientButtonEnabled(false);
		} else {
			T affectedValue = p_oObjectToSet;
			if(this.getComponentFwkDelegate().customFormatter() != null) {
				affectedValue = (T) this.getComponentFwkDelegate().customFormatter().format(p_oObjectToSet); 
			}
			
			String sValue = this.valueToString(affectedValue);
			
			oUiEmail.setText(sValue);
			
			this.setMailClientButtonEnabled(true);
		}
	}
	
}
