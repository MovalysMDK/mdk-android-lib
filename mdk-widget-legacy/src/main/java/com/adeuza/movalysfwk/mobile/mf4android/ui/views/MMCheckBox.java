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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseCheckBox;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>Checkbox widget used in the Movalys Mobile product for Android</p>
 *
 *
 *
 */
@BaseComponent(baseName="MMBaseCheckBox", baseClass="android.widget.CheckBox", appCompatClass="android.support.v7.widget.AppCompatCheckBox")
public class MMCheckBox extends MMBaseCheckBox implements ConfigurableVisualComponent, OnCheckedChangeListener, InstanceStatable, 
	ComponentReadableWrapper<Boolean>, ComponentWritableWrapper<Boolean> {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Boolean> aivDelegate = null;

	/**
	 * Le composant lui-même est un OnCheckedChangeListener.
	 * Pour permettre en intégration de définir un autre listener
	 * et surtout empêcher l'écrasement du composant en tant que listener sur lui-même,
	 * il faut ajouter un attribut et surcharger la méthode setOnCheckedChangeListener.
	 */
	private OnCheckedChangeListener onCheckedChangeListener;


	/**
	 * Constructs a new MMCheckBox
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see CheckBox#CheckBox(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMCheckBox(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Boolean>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Boolean.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}	
	}

	/**
	 * Constructs a new MMCheckBox
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see CheckBox#CheckBox(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMCheckBox(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Boolean>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Boolean.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * called when the inflater finished the job 
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		super.setOnCheckedChangeListener(this);
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#setId(int)
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton p_oButtonView, boolean p_bIsChecked) {
		if (this.aivDelegate != null && !this.aivDelegate.isWritingData()) {
			this.aivFwkDelegate.changed();
			if (this.onCheckedChangeListener != null) {
				this.onCheckedChangeListener.onCheckedChanged(p_oButtonView, p_bIsChecked);
			}
		}
	}

	@Override
	public void setOnCheckedChangeListener(OnCheckedChangeListener p_oListener) {
		this.onCheckedChangeListener = p_oListener;
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
	 * @see android.widget.CompoundButton#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.CompoundButton#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<Boolean> getComponentDelegate() {
		return this.aivDelegate;
	}

	/***************************************************************************
	 ************************ Framework delegate callback **********************
	 ***************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Boolean configurationGetValue() {
		return this.isChecked();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Boolean p_oObjectToSet) {
		if (p_oObjectToSet!=null) {
			this.setChecked(((Boolean) p_oObjectToSet).booleanValue());
		} else {
			this.setChecked(false);
		}
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
