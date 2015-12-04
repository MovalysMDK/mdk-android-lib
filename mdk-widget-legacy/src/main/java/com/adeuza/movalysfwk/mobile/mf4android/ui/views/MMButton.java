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
import android.widget.Button;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseButton;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>Button widget used in the Movalys Mobile product for Android</p>
 *
 *
 *
 */
@BaseComponent(baseName="MMBaseButton", baseClass="android.widget.Button", appCompatClass="android.support.v7.widget.AppCompatButton")
public class MMButton extends MMBaseButton implements ConfigurableVisualComponent, InstanceStatable {

	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<NoneType> aivDelegate = null;

	/**
	 * Constructs a new MMButton
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see Button#Button(Context, AttributeSet)
	 */
	public MMButton(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}

	/**
	 * Constructs a new MMButton
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see Button#Button(Context, AttributeSet, int)
	 */
	public MMButton(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}

	/**
	 * Initialize component with current attribute set
	 * @param p_oAttrs the component attribute set
	 */
	@SuppressWarnings("unchecked")
	protected final void init (final AttributeSet p_oAttrs) {

		this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<NoneType>) DelegateInitialiserHelper.initDelegate(this, 
				new Class[]{Class.class, AttributeSet.class}, 
				new Object[]{NoneType.class, p_oAttrs});
		this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
				new Class[]{AttributeSet.class}, 
				new Object[]{p_oAttrs});
		
		// Dans le cas des MMButton inclut dans un xml ne faisant pas l'objet d'un binding (pas de VM associé),
		// il faut écraser le libellé définit dans le xml par l'éventuel libellé présent dans le ConfigurationsHandler (propriété)
		final int iTextId = p_oAttrs.getAttributeResourceValue(AndroidApplication.ANDROID_XML_NAMESPACE, "text", 0);
		if (iTextId != 0 && !isInEditMode()) {
			this.setText(((AndroidApplication)Application.getInstance()).getStringResource(iTextId));
		}
	}

	/**
	 * called wen the inflator finished the job 
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){
			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.setText(oConfiguration.getLabel());
			}
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
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<NoneType> getComponentDelegate() {
		return this.aivDelegate;
	}
}
