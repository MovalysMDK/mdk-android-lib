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
import android.view.View;
import android.widget.ImageView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>ImageView widget used in the Movalys Mobile product for Android</p>
 *
 *
 *
 */

public class MMImageView extends ImageView implements ConfigurableVisualComponent, InstanceStatable, 
ComponentReadableWrapper<Integer>, ComponentWritableWrapper<Integer> {

	/** there is no image */
	public static final int NO_IMAGE = -1;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Integer> aivDelegate = null;

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @see ImageView#ImageView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMImageView(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Integer>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Integer.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see ImageView#ImageView(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMImageView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Integer>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Integer.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use 
	 * @see ImageView#ImageView(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMImageView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Integer>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Integer.class, p_oAttrs});
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
	public VisualComponentDelegate<Integer> getComponentDelegate() {
		return this.aivDelegate;
	}

	/******************************************************************************************
	 ****************************** Framework delegate callback *******************************
	 ******************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Integer configurationGetValue() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Integer p_oObjectToSet) {
		if( p_oObjectToSet != null && !p_oObjectToSet.equals(this.configurationGetValue()) 
				|| p_oObjectToSet == null && this.configurationGetValue() != null ) {
			if (p_oObjectToSet==null || (Integer)p_oObjectToSet == MMImageView.NO_IMAGE ){
				this.setVisibility(View.GONE);
			}else{
				if (this.getVisibility()!= View.VISIBLE){
					this.setVisibility(View.VISIBLE);
				}
				this.setImageResource((Integer)p_oObjectToSet);
			}
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
