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
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableTextViewComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseMultiAutoCompleteEditText;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>MultiAutoCompleteTextView widget used in the Movalys Mobile product for Android</p>
 *
 *
 *
 */
@BaseComponent(baseName="MMBaseMultiAutoCompleteEditText", baseClass="android.widget.MultiAutoCompleteTextView", appCompatClass="android.support.v7.widget.AppCompatMultiAutoCompleteTextView")
public class MMMultiAutoCompleteEditText extends MMBaseMultiAutoCompleteEditText implements ConfigurableVisualComponent, InstanceStatable {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableTextViewComponentDelegate aivDelegate = null;

	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * @param p_oContext the context to use
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView(Context)
	 */
	public MMMultiAutoCompleteEditText(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}

	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView
	 */
	public MMMultiAutoCompleteEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}


	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use 
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView
	 */
	public MMMultiAutoCompleteEditText(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);
	}

	@Override
	protected void onTextChanged(CharSequence p_oText, int p_oStart,
			int p_oBefore, int p_oAfter) {
		super.onTextChanged(p_oText, p_oStart, p_oBefore, p_oAfter);
		if (this.aivDelegate!=null && !this.aivDelegate.isWritingData() && this.aivFwkDelegate!=null
				&& !(this.getParent()!= null && this.getParent().getParent() instanceof MMMultiAutoCompleteEditTextWithDialog)) {
			this.aivFwkDelegate.changed();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see ConfigurableVisualComponent#getComponentFwkDelegate()
	 */
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	/**
	 * {@inheritDoc}
	 * @see ConfigurableVisualComponent#getComponentDelegate()
	 */
	@Override
	public VisualComponentDelegate<String> getComponentDelegate() {
		return this.aivDelegate;
	}
}
