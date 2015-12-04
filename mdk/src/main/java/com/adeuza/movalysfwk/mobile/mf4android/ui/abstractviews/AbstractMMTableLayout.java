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
package com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TableLayout;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.InstanceStatable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>TableLayout widget used in the Movalys Mobile product for Android</p>
 *
 *
 * @param <VALUE> the type of value to set in component
 */
public abstract class AbstractMMTableLayout<VALUE> extends TableLayout implements ConfigurableLayoutComponent, InstanceStatable {

	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<VALUE> aivDelegate = null;

	/**
	 * Constructs a new MMTableLayout
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDelegateType the handled component class type
	 * @see android.widget.TableLayout#TableLayout(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public AbstractMMTableLayout(Context p_oContext, AttributeSet p_oAttrs, Class<?> p_oDelegateType) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<VALUE>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{p_oDelegateType, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * Constructs a new MMTableLayout
	 * @param p_oContext the context to use
	 * @param p_oDelegateType the handled component class type
	 * @see android.widget.TableLayout#TableLayout(Context)
	 */
	@SuppressWarnings("unchecked")
	public AbstractMMTableLayout(Context p_oContext, Class<?> p_oDelegateType) {
		super(p_oContext);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<VALUE>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{p_oDelegateType, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
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
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
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
	public VisualComponentDelegate<VALUE> getComponentDelegate() {
		return this.aivDelegate;
	}
}
