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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableTextViewComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentMandatoryLabel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseSpinnerCheckedTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>CheckedTextView widget used in the Movalys Mobile product for Android</p>
 * <p>This widget is used in the default drop down list of the MMSpinner widget</p>
 *
 *
 *
 */
@BaseComponent(baseName="MMBaseSpinnerCheckedTextView", baseClass="android.widget.CheckedTextView", appCompatClass="android.support.v7.widget.AppCompatCheckedTextView")
public class MMSpinnerCheckedTextView extends MMBaseSpinnerCheckedTextView implements ConfigurableVisualComponent, InstanceStatable, OnTouchListener, ComponentMandatoryLabel {

	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableTextViewComponentDelegate aivDelegate = null;

	/**
	 * Constructs a new MMSpinnerCheckedTextView
	 * @param p_oContext the context to use
	 * @see CheckedTextView#CheckedTextView(Context)
	 */
	public MMSpinnerCheckedTextView(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
			this.setOnTouchListener(this);
		}
	}

	/**
	 * Constructs a new MMSpinnerCheckedTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see CheckedTextView#CheckedTextView(Context, AttributeSet)
	 */
	public MMSpinnerCheckedTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			this.setOnTouchListener(this);
		}
		//completed by the onFinishInflate method callback
	}

	/**
	 * Constructs a new MMSpinnerCheckedTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see CheckedTextView#CheckedTextView(Context, AttributeSet, int)
	 */
	public MMSpinnerCheckedTextView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			this.setOnTouchListener(this);
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
	public boolean onTouch(View p_oV, MotionEvent p_oEvent) {
		Context oApplicationContext = ((AndroidApplication)AndroidApplication.getInstance()).getApplicationContext();
        InputMethodManager imm=(InputMethodManager)oApplicationContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
		return false;
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<String> getComponentDelegate() {
		return this.aivDelegate;
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
	 * @see ComponentMandatoryLabel#setMandatoryLabel(boolean)
	 */
	@Override
	public void setMandatoryLabel(boolean p_bMandatory) {
		String sOriginalLabel = this.getText().toString();
		String sComputedMandatoryLabel = this.aivDelegate.computeMandatoryLabel(sOriginalLabel, p_bMandatory);
		this.setText(sComputedMandatoryLabel);
	}
}
