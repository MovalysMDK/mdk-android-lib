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
 * <p>RadioGroup widget used in the Movalys Mobile product for Android</p>
 */
public class MMSeekBarInteger extends AbstractMMTableLayout<Integer> implements InstanceStatable, ComponentError, ComponentValuedView, ComponentEnable {

	/** Composant dédié à l'affichage de la valeur. */
	private TextView uiValueDisplayComponent;
	
	/** Composant dédié à la modification de la valeur. */
	private SeekBar uiModifierComponent;

	/**
	 * The key used to retain value on orientation changed 
	 */
	private static final String SEEK_BAR_INTEGER_VALUE_KEY = "seekBarIntegerValueKey" ;

	/**
	 * Constructs a new MMRadioGroup
	 * @param p_oContext
	 *            the context to use
	 * @see android.view.View#View(Context)
	 */
	public MMSeekBarInteger(Context p_oContext) {
		this(p_oContext,null);
	}	
	/**
	 * Constructs a new MMRadioGroup
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see android.view.View#View(Context, AttributeSet)
	 */
	public MMSeekBarInteger(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Integer.class);
		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oInflater = LayoutInflater.from(this.getContext());
			oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_integer_seekbar), this, true);
			uiModifierComponent = (SeekBar) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_seekbar__integer__seekbar));
			uiValueDisplayComponent = (TextView) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_seekbar__integer__value));
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		if(!isInEditMode()) {
			uiModifierComponent.setOnSeekBarChangeListener((OnSeekBarChangeListener) this.aivDelegate);
			
			((AndroidConfigurableSeekBarComponentDelegate<Integer>)this.aivDelegate).setMaxValueOnSeekBar();
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {

		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putInt(SEEK_BAR_INTEGER_VALUE_KEY, this.uiModifierComponent.getProgress());

		return r_oBundle;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		Integer retainValue = r_oBundle.getInt(SEEK_BAR_INTEGER_VALUE_KEY);
		this.uiModifierComponent.setProgress(retainValue.intValue());
		this.uiValueDisplayComponent.setText(String.valueOf(retainValue));
	}
	
	@Override
	public void setEnabled(boolean p_bEnabled) {
		super.setEnabled(p_bEnabled);
		this.uiModifierComponent.setEnabled(p_bEnabled);
	}
	
	@Override
	public AndroidConfigurableSeekBarComponentDelegate<Integer> getComponentDelegate() {
		return (AndroidConfigurableSeekBarComponentDelegate<Integer>) this.aivDelegate;
	}
	
	/************************************************************************************************
	 ********************************* Framework delegate callback **********************************
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
