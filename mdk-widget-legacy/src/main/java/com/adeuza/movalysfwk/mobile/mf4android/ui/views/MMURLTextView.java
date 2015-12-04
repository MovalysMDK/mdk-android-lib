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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;

/**
 * This view group represents an url text view, its label and its button
 * to see the url on the web browser :<ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMURLTextViewGroup> </li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 */
public class MMURLTextView extends AbstractMMTableLayout<String> implements OnClickListener, ComponentError, 
	ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {

	/** the url field*/
	private TextView oUiURLText;
	
	/** the call button*/
	private ImageButton oUiWebBrowserButton;
	
	/** The key for the bundle in savedInstanceState */
	private static final String WEB_BROWSER_BUTTON_ENABLED_STATE_KEY = "webBrowserButtonEnabledStateKey";
	
	/** The key for the bundle in savedInstanceState */
	private static final String WEB_BROWSER_BUTTON_HIDDEN_STATE_KEY = "webBrowserButtonHiddenStateKey";
	
	/**
	 * Construct a MMURLTextViewGroup 
	 * @param p_oContext the context
	 */
	public MMURLTextView(Context p_oContext) {
		super(p_oContext, String.class);
		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_url_textview), this);

			oUiURLText = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__value));
			oUiWebBrowserButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__button));
		}
	}

	/**
	 * Construct a MMURLTextViewGroup 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMURLTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, String.class);
		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_url_textview), this);

			oUiURLText = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__value));
			oUiWebBrowserButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__button));

			this.linkChildrens(p_oAttrs);
		}
	}
	
	/**
	 * link the children views with custom attributes
	 * @param p_oAttrs attributes of XML Component component_url_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		this.aivFwkDelegate.setCustomConverter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.CONVERTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
		this.aivFwkDelegate.setCustomFormatter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.FORMATTER_NAME_ATTRIBUTE.getName()), p_oAttrs);

	}

	/**
	 * Changes the visibility of the button launching the web browser
	 * @param p_bIsbrowserButtonHidden TRUE if the call button should be hidden, FALSE otherwise
	 */
	protected void setWebBrowserButtonHidden(boolean p_bIsbrowserButtonHidden) {
		if(p_bIsbrowserButtonHidden) {
			this.oUiWebBrowserButton.setOnClickListener(null);
			this.oUiWebBrowserButton.setVisibility(View.GONE);
		}
		else {
			this.oUiWebBrowserButton.setOnClickListener(this);
			this.oUiWebBrowserButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Changing the enabled status of the button launching the web browser
	 * @param p_bIsEnabled TRUE if the button should be enabled, FALSE otherwise
	 */
	public void setWebBrowserButtonEnabled(boolean p_bIsEnabled) {
		this.oUiWebBrowserButton.setEnabled(p_bIsEnabled);
	}

	/**
	 * On click listener to deal with the click on the Call button
	 * @param p_oArg0 the view that was clicked
	 */
	@Override
	public void onClick(View p_oArg0) {
		String sURL = (String) oUiURLText.getText();
		if (sURL!=null && sURL.length() > 0 ){
			((AndroidApplication) Application.getInstance()).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sURL)));
		}
	}

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

	/*****************************************************************************************
	 ******************************* Framework delegate callback *****************************
	 *****************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextView getErrorView() {
		return this.oUiURLText;
	}

	@Override
	public String configurationGetValue() {
		String r_oReturnValue = this.oUiURLText.getText().toString();
		if (r_oReturnValue.length() == 0) {
			r_oReturnValue = null;
		}
		r_oReturnValue = (String) this.aivDelegate.getFormattedValue(r_oReturnValue);
		return r_oReturnValue;
	}

	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		if (p_oObjectToSet != null && ((String) p_oObjectToSet).length() > 0) {
			String oUrlText = (String) this.aivDelegate.getFormattedValue(p_oObjectToSet);
			this.oUiURLText.setText(oUrlText);
			this.oUiWebBrowserButton.setOnClickListener(this);
			boolean isValid = this.aivDelegate.validate((BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration(), null, new StringBuilder())&& this.isFilled();
			this.setWebBrowserButtonEnabled(isValid);
		} else {
			this.oUiWebBrowserButton.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean isFilled() {
		return this.configurationGetValue() == null;
	}
}
