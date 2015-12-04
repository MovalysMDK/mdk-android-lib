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
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableEnumComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * Component that display a text from a Enum
 *
 */
@BaseComponent(baseName="MMBaseTextView", baseClass="android.widget.TextView", appCompatClass="android.support.v7.widget.AppCompatTextView")
public class MMEnumTextView  extends MMBaseTextView implements ConfigurableVisualComponent, InstanceStatable, 
ComponentReadableWrapper<Enum<?>>, ComponentWritableWrapper<Enum<?>> {
	
	/** Default empty value */
	private static final String DEFAULT_EMPTY_VALUE = "";

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Enum<?>> aivDelegate;
	
	/** Empty value */
	private String emptyValue = DEFAULT_EMPTY_VALUE;

	/**
	 * Constructs a new MMEnumTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see TextView#TextView(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMEnumTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Enum<?>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Enum.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * Constructs a new MMEnumTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use 
	 * @see TextView#TextView(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMEnumTextView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Enum<?>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Enum.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,  
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
		//completed by th onFinishInflate method callback
	}

	/**
	 * compute text to display
	 */
	public void computeDisplay() {
		
		int stringId = ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getRessourceId(ApplicationRGroup.STRING);
		
		if(stringId == -1) {
			this.setText(this.emptyValue);
		}
		else {
			this.setText(stringId);
		}
	}

	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	/**
	 *  {@inheritDoc}
	 *  @see InstanceStatable#superOnRestoreInstanceState(Parcelable)
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
	 * {@inheritDoc}
	 * Keep the value when state changed
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		if ( this.aivDelegate != null){
			Bundle oToSave = new Bundle();
			oToSave.putParcelable("parent", this.aivFwkDelegate.onSaveInstanceState( super.onSaveInstanceState() ) );
			oToSave.putString("imageNamePrefix", ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getEnumRessourceNamePrefix());
			oToSave.putString("imageNameSuffix", ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getEnumValue());
			return oToSave;
		}
		return super.onSaveInstanceState();
	}

	/**
	 * {@inheritDoc}
	 * Reset the value when state changed
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		if(!(p_oState instanceof Bundle)){
			super.onRestoreInstanceState(p_oState);
			return;
		}

		Bundle savedState = (Bundle) p_oState;
		this.aivFwkDelegate.onRestoreInstanceState(savedState.getParcelable("parent"));
		((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).setEnumRessourceNamePrefix(savedState.getString("imageNamePrefix"));
		((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).setEnumValue(savedState.getString("imageNameSuffix"));
		this.aivDelegate.setFilled(true);

		this.computeDisplay();
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<Enum<?>> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/************************************************************************************************
	 ******************************** Framework delegate callback ***********************************
	 ************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Enum<?> configurationGetValue() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Enum<?> p_oObjectToSet) {
		computeDisplay();
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return true;
	}
}
