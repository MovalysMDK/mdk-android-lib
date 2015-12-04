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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;

/**
 * TextView Delegate
 * <p>Handle behaviors on TextView and all inherited classes</p>
 *
 */
public class AndroidConfigurableEditTextComponentDelegate extends AndroidConfigurableTextViewComponentDelegate {

	/**
	 * Android namespace
	 */
	private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";

	/**
	 * inputType attribute on EditText widget
	 */
	private static final String INPUTTYPE_ATTR = "inputType";

	/**
	 * hint attribute on EditText widget
	 */
	private static final String HINT_ATTR = "hint";
	
	/**
	 * Constructor
	 * @param p_oCurrentView the component (inherited from TextView)
	 * @param p_oDelegateType the type of the delegate
	 */
	public AndroidConfigurableEditTextComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
		
		EditText oEditText = null;
		
		if (p_oCurrentView instanceof ComponentWrapper) {
			oEditText = (EditText) ((ComponentWrapper) p_oCurrentView).getComponent();
		} else {
			oEditText = (EditText) p_oCurrentView;
		}
		
		initView(oEditText, null);
	}
	
	/**
	 * Constructor
	 * @param p_oCurrentView the component (inherited from TextView)
	 * @param p_oDelegateType the type of the delegate
	 * @param p_oAttrs this AttributeSet of the View
	 */
	public AndroidConfigurableEditTextComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
		
		EditText oEditText = null;
		
		if (p_oCurrentView instanceof ComponentWrapper) {
			oEditText = (EditText) ((ComponentWrapper) p_oCurrentView).getComponent();
		} else {
			oEditText = (EditText) p_oCurrentView;
		}
		
		initView(oEditText, p_oAttrs);
	}
	
	/**
	 * Initializes the view
	 * @param p_oCurrentView current view 
	 * @param p_oAttrs xml attributes
	 */
	protected final void initView( EditText p_oCurrentView, AttributeSet p_oAttrs ) {
		
		// Define a default hint to prevent from a memory leak
		if ( (p_oCurrentView.getHint() == null || p_oCurrentView.getHint().toString().isEmpty()) && (p_oAttrs == null || p_oAttrs.getAttributeValue(ANDROID_NAMESPACE, HINT_ATTR) == null) ) {
			p_oCurrentView.setHint(" ");
		}
		
		// Set inputType to "no suggestion" to prevent from a memory leak
		if ( (p_oCurrentView.getInputType() & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT && (p_oAttrs == null || p_oAttrs.getAttributeValue(ANDROID_NAMESPACE, INPUTTYPE_ATTR) == null) ) {
			p_oCurrentView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		EditText text = (EditText) this.currentView;
		
		// This must be done to fix a memory leak of EditText.
		text.setHint(" ");
		text.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

		if (this.currentCvComponent instanceof ComponentWrapper) {
			((ComponentWrapper) this.currentCvComponent).unsetComponent();
		}
		
		super.destroy();
	}
	
	@Override
	public void configurationDisabledComponent() {
		this.currentView.setEnabled(false);
		this.currentView.clearFocus();
		if (this.currentView instanceof ComponentEnable) {
			((ComponentEnable) this.currentView).enableComponent(false);
		}
	}

	@Override
	public void configurationEnabledComponent() {
		this.currentView.setEnabled(true);
		if (this.currentView instanceof ComponentEnable) {
			((ComponentEnable) this.currentView).enableComponent(true);
		}
	}
}
