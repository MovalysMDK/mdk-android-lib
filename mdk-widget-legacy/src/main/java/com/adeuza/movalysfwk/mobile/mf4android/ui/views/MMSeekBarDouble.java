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

import java.text.DecimalFormatSymbols;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableSeekBarComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.seekbar.ComponentValuedView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>Seekbar widget used in the MDK</p>
 */
public class MMSeekBarDouble extends AbstractMMTableLayout<Double> implements InstanceStatable, ComponentError, ComponentValuedView, ComponentEnable {

	/** The key used to retain value on orientation changed */
	private static final String SEEK_BAR_DOUBLE_VALUE_KEY = "seekBarDoubleValueKey" ;

	/** Component on which errors will be displayed */
	private TextView uiValueDisplayComponent;
	
	/** Seekbar component used to set the value */
	private SeekBar uiModifierComponent;
	
	/**
	 * Constructs a new MMSeekBar
	 * @param p_oContext the context to use
	 * @see android.view.View#View(Context)
	 */
	public MMSeekBarDouble(Context p_oContext) {
		this(p_oContext,null);
	}
	
	/**
	 * Constructs a new MMSeekBar
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see android.view.View#View(Context, AttributeSet)
	 */
	public MMSeekBarDouble(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Double.class);

		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oInflater = LayoutInflater.from(this.getContext());
			oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_double_seekbar), this, true);
			uiModifierComponent = (SeekBar) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_seekbar__double__seekbar));
			uiValueDisplayComponent = (TextView) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_seekbar__double__value));

			DecimalFormatSymbols oSymbols = new DecimalFormatSymbols();
			oSymbols.setDecimalSeparator('.');
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		if(!isInEditMode()) {
			uiModifierComponent.setOnSeekBarChangeListener((OnSeekBarChangeListener) this.aivDelegate);
			uiModifierComponent.setFocusable(true);
			uiModifierComponent.setFocusableInTouchMode(true);
			
			((AndroidConfigurableSeekBarComponentDelegate<Double>)this.aivDelegate).setMaxValueOnSeekBar();
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
		r_oBundle.putDouble(SEEK_BAR_DOUBLE_VALUE_KEY, this.uiModifierComponent.getProgress());

		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		Double retainValue = r_oBundle.getDouble(SEEK_BAR_DOUBLE_VALUE_KEY);
		this.uiModifierComponent.setProgress(retainValue.intValue());
		this.uiValueDisplayComponent.setText(String.valueOf(retainValue));
	}

	@Override
	public void setEnabled(boolean p_bEnabled) {
		super.setEnabled(p_bEnabled);
		this.uiModifierComponent.setEnabled(p_bEnabled);
	}
	
	@Override
	public AndroidConfigurableSeekBarComponentDelegate<Double> getComponentDelegate() {
		return (AndroidConfigurableSeekBarComponentDelegate<Double>) this.aivDelegate;
	}
	
	/************************************************************************************************
	 ****************************** Framework delegate callback *************************************
	 ************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		return this.uiValueDisplayComponent;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentValuedView#getValuedComponent()
	 */
	@Override
	public TextView getValuedComponent() {
		return this.uiValueDisplayComponent;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentValuedView#getSeekBar()
	 */
	@Override
	public SeekBar getSeekBar() {
		return this.uiModifierComponent;
	}

	@Override
	public void enableComponent(boolean p_bEnable) {
		this.uiModifierComponent.setEnabled(p_bEnable);
	}
}
