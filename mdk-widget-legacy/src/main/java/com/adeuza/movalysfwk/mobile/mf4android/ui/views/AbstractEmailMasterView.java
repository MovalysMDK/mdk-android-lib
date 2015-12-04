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
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.EmailFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * Define the hightest level of abstraction for Email Components
 *
 * @param <VALUE> the parameter type used in the email component
 */
public abstract class AbstractEmailMasterView<VALUE> extends
		AbstractMMLinearLayout<VALUE> implements OnClickListener, ComponentValidator, ComponentReadableWrapper<VALUE>, ComponentWritableWrapper<VALUE>, ComponentEnable, ComponentError {
	
	/** The key to retain the the enable state of the mail client button */
	private static final String MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY = "mailClientButtonEnabledStateKey";
	/** The key to retain the the hidden state of the mail client button */
	private static final String MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY = "mailClientButtonHiddenStateKey";
	/** The key to retain the the hidden state of the mail client button */
	private static final String MAIL_VALUE_KEY = "mailValueKey";
	
	/** the email address field */
	protected TextView oUiEmail;
	/** the email button */
	protected View oUiEmailButton;
	
	/**
	 * Create new AbstractEmailMasterView
	 * @param p_oContext the android context
	 * @param p_oDelegateType the class of the component type
	 */
	public AbstractEmailMasterView(Context p_oContext, Class<?> p_oDelegateType) {
		super(p_oContext, p_oDelegateType);
	}
	
	/**
	 * Create new AbstractEmailMasterView
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 * @param p_oDelegateType the class of the component type
	 */
	public AbstractEmailMasterView(Context p_oContext, AttributeSet p_oAttrs, Class<?> p_oDelegateType) {
		super(p_oContext, p_oAttrs, p_oDelegateType);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		
		oUiEmail = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_email__email__value));
		oUiEmailButton = this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_email__email__button));
		
		if (this.oUiEmailButton != null) {
			if ( ((AndroidApplication)Application.getInstance()).isMailAvailable() ) {
				oUiEmailButton.setOnClickListener(this);
			} else {
				oUiEmailButton.setVisibility(View.GONE);
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void onClick(View p_oView) {
		String sEmailAddress = oUiEmail.getText().toString();
		if (sEmailAddress != null && sEmailAddress.length() > 0) {
			Application.getInstance().getController().doWriteEmail(this.composeMail(this.configurationGetValue()));
		}
	}

	/**
	 * Specify the action to realize
	 * @param p_oMail the mail value
	 * @return the EMail to send to android application
	 */
	protected abstract EMail composeMail(VALUE p_oMail);

	/**
	 * Set the state of the client mail button (enable/disable)
	 * @param p_bIsEnabled The new state of the button
	 */
	protected void setMailClientButtonEnabled(boolean p_bIsEnabled) {
		if (this.oUiEmailButton != null) {
			this.oUiEmailButton.setEnabled(p_bIsEnabled);
		}
	}

	/**
	 * Set the visibility of the client mail button (gone/visible)
	 * @param p_bIsHidden The new visibility of the button
	 */
	protected void setMailClientButtonHidden(boolean p_bIsHidden) {
		if (this.oUiEmailButton != null) {
			if(p_bIsHidden) {
				this.oUiEmailButton.setVisibility(View.GONE);
			}
			else {
				this.oUiEmailButton.setVisibility(View.VISIBLE);
			}
		}
	}
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(MAIL_VALUE_KEY, this.oUiEmail.getText().toString());
		r_oBundle.putBoolean(MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY, this.oUiEmailButton.isEnabled());
		r_oBundle.putBoolean(MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY, this.oUiEmailButton.getVisibility() == View.GONE);
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


		boolean enabled = r_oBundle.getBoolean(MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY);
		boolean hidden = r_oBundle.getBoolean(MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY);
		String value = r_oBundle.getString(MAIL_VALUE_KEY);
		if(value != null) {
			this.oUiEmail.setText(value);
		}

		setMailClientButtonEnabled(enabled);
		setMailClientButtonHidden(hidden);
		this.aivDelegate.setWritingData(false);
	}
	
	/**
	 * Convert string to the handled type
	 * @param p_sString the string from android component
	 * @return the value handled by the component
	 */
	protected abstract VALUE stringToValue(String p_sString);

	/**
	 * Convert the handled value type to string value
	 * @param p_oObject the component value type
	 * @return a string representing the parameter value
	 */
	protected abstract String valueToString(VALUE p_oObject);
	
	/********************************************************************************************
	 ***************************** Framework delegate callback **********************************
	 ********************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public VALUE configurationGetValue() {
		String emailText = this.oUiEmail.getText().toString();
		VALUE r_oEmail = stringToValue(emailText);
		if (emailText.length() > 0 && this.getComponentFwkDelegate().customFormatter() != null) {
			r_oEmail = (VALUE) this.getComponentFwkDelegate().customFormatter().unformat(r_oEmail);
		}
		return r_oEmail;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentValidator#getValidator()
	 */
	@Override
	public IFormFieldValidator getValidator() {
		EmailFormFieldValidator r_oValidator  = BeanLoader.getInstance().getBean(EmailFormFieldValidator.class);
		r_oValidator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes(), (BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration());
		return r_oValidator;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.oUiEmail.getText() != null && this.oUiEmail.getText().length() > 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		oUiEmail.setEnabled(p_bEnable);
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		return this.oUiEmail;
	}
}
