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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.wrappers;

import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;

public class MMCheckBoxWrapper extends AbstractComponentWrapper<CheckBox> implements ConfigurableVisualComponent, OnCheckedChangeListener, 
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

	@SuppressWarnings("unchecked")
	@Override
	public void setComponent(View p_oComponent, boolean p_bIsRestoring) {
		super.setComponent(p_oComponent, p_bIsRestoring);
		
		this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Boolean>) DelegateInitialiserHelper.initDelegate(this, 
				new Class[]{Class.class, AttributeSet.class}, 
				new Object[]{Boolean.class, null});
		this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
				new Class[]{AttributeSet.class}, 
				new Object[]{null});
		
		((CheckBox)this.component.get()).setOnCheckedChangeListener(this);
	}

	@Override
	public void unsetComponent() {
		((CheckBox)this.component.get()).setOnCheckedChangeListener(null);
	}

	public void setId(int p_oId) {
		this.component.get().setId(p_oId);
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

	public void setOnCheckedChangeListener(OnCheckedChangeListener p_oListener) {
		this.onCheckedChangeListener = p_oListener;
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
		return this.component.get().isChecked();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Boolean p_oObjectToSet) {
		if (p_oObjectToSet!=null) {
			this.component.get().setChecked(((Boolean) p_oObjectToSet).booleanValue());
		} else {
			this.component.get().setChecked(false);
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
