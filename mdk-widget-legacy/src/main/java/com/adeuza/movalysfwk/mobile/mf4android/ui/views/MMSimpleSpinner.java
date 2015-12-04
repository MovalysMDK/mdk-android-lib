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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseSpinner;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;


/**
 * <p>Simple Spinner widget to use in custom code with string arrays</p>
 */
@BaseComponent(baseName="MMBaseSpinner", baseClass="android.widget.Spinner", appCompatClass="android.support.v7.widget.AppCompatSpinner")
public class MMSimpleSpinner extends MMBaseSpinner implements ConfigurableVisualComponent, InstanceStatable, OnTouchListener, 
ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {
	
	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;
	
	/** true if an empty value is added to the spinner */
	private boolean useEmptyValue = false ;
	
	/**
	 * Constructs a new MMSimpleSpinner
	 * @param p_oContext the context to use
	 * @see Spinner#Spinner(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMSimpleSpinner(Context p_oContext) {
		this(p_oContext,null);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
			this.setOnTouchListener(this);
		}
	}
	/**
	 * Constructs a new MMSimpleSpinner
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see Spinner#Spinner(Context, AttributeSet)
	 */
	public MMSimpleSpinner(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs );
		if(!isInEditMode()) {
			this.setOnTouchListener(this);
			this.init(p_oAttrs);
		}
	}
	/**
	 * Constructs a new MMSimpleSpinner
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see Spinner#Spinner(Context, AttributeSet, int)
	 */
	public MMSimpleSpinner(Context p_oContext, AttributeSet p_oAttrs,int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.setOnTouchListener(this);
			this.init(p_oAttrs);
		}
	}
	
	/**
	 * Initializes the delegates and the use-empty-item parameter
	 * @param p_oAttrs xml attributes to use
	 */
	@SuppressWarnings("unchecked")
	private final void init(AttributeSet p_oAttrs){
		this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this, 
				new Class[]{Class.class, AttributeSet.class}, 
				new Object[]{String.class, p_oAttrs});
		this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
				new Class[]{AttributeSet.class}, 
				new Object[]{p_oAttrs});
		if ( p_oAttrs != null){
			this.useEmptyValue = p_oAttrs.getAttributeBooleanValue(	Application.MOVALYSXMLNS, "use-empty-item", true);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.AbsSpinner#getCount()
	 */
	@Override
	public int getCount() {
		return this.getAdapter().getCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.AbsSpinner#setSelection(int)
	 */
	@Override
	public void setSelection(int p_oPosition) {
		super.setSelection(p_oPosition);
		if (this.aivDelegate != null && !this.aivDelegate.isWritingData()) {
			this.aivFwkDelegate.changed();
		}
	}
	
	/**
	 * Returns <code>true</code> if an empty value must be displayed into the comboBox.
	 * @return <code>true</code> if an empty value must be displayed into the comboBox.
	 */
	public boolean hasEmptyValue() {
		return this.useEmptyValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	/**
	 * Sets the key / values map on the spinner and sets the adapter
	 * @param p_mapKeyValues key / values map to set
	 */
	public void setKeyValues(Map<String, String> p_mapKeyValues) {
		Collection<String> oCollec = p_mapKeyValues.values();		
		List<String> oListValues = new ArrayList<>();
		oListValues.addAll(oCollec);				
		ArrayAdapter<String> oListAdapter = new ArrayAdapter<>( this.getContext(),
				((AndroidApplication)Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.fwk_component__simplespinner_text),
				oListValues); 
		oListAdapter.setDropDownViewResource(((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.fwk_component__simplespinner_spinitem));
		this.setAdapter(oListAdapter);
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
	
	/************************************************************************************
	 ************************** Framework delegate callback *****************************
	 ************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		return (String) this.getSelectedItem();
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		int iPositionSelected = 0; // 0 car si pas de valeur, le premier élément doit être sélectionné.
		if (p_oObjectToSet != null) {
			for (int i = 0; i < this.getAdapter().getCount(); i++) {
				if (p_oObjectToSet.equals(this.getAdapter().getItem(i))) {
					iPositionSelected = i;
					break;
				}
			}
		}
		//appel du super car on set la valeur initiale, ne pas tagger le champ comme changé
		this.setSelection(iPositionSelected);
		this.requestFocus();
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.getSelectedItem() != null && ((String) this.getSelectedItem()).length() > 0;
	}
}
