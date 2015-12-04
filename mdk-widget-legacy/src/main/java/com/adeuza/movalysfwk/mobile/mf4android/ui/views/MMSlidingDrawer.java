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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>SlidingDrawer layout used in the Movalys Mobile product for Android</p>
 *
 *
 *
 */

public class MMSlidingDrawer extends SlidingDrawer implements ConfigurableLayoutComponent, OnDrawerCloseListener, OnDrawerOpenListener {

	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<NoneType> aivDelegate = null;

	/**
	 * Constructs a new MMSlidingDrawer
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see SlidingDrawer#SlidingDrawer(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMSlidingDrawer(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<NoneType>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			this.setOnDrawerCloseListener(this);
			this.setOnDrawerOpenListener(this);
			this.setDrawerStateResource();		
		}
	}

	/**change the drawer image when opened */
	@Override
	public void onDrawerOpened() {
		setDrawerStateResource();
		// Allow to disable buttons above the drawer
		this.getContent().setClickable(true);
	}

	/**change the drawer image when closed */
	@Override
	public void onDrawerClosed() {
		setDrawerStateResource();
		// Allow to disable buttons above the drawer
		this.getContent().setClickable(false);
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
	 * Allows to change the edit mode for this view
	 * @param p_bEdit true if the slidingDrawer should be editable
	 */
	public void setEdit(boolean p_bEdit) {
		setEdit(p_bEdit, this);
	}

	/**
	 * Allows to change the edit mode of the subviews of this view
	 * @param p_bEdit true if the slidingDrawer should be editable
	 * @param p_oUiChildView A childView of this view
	 */
	private void setEdit(boolean p_bEdit, ViewGroup p_oUiChildView) {
		for (int i = 0; i < p_oUiChildView.getChildCount(); i++) {
			Object uiSubview = p_oUiChildView.getChildAt(i);
			if( uiSubview instanceof ViewGroup ) {
				setEdit(p_bEdit, (ViewGroup)uiSubview);
			}
			else {
				if(uiSubview instanceof Button) {
					((View)uiSubview).setEnabled(true);
				}
				else {
					((View)uiSubview).setEnabled(p_bEdit);
				}
			}
		}
	}

	/**
	 * Set the resource that indicates if the drawer is closed or opened.
	 */
	private void setDrawerStateResource() {
		if(this.getHandle() != null) {
			if(this.isOpened()) {
				this.getHandle().setBackgroundResource(((AndroidApplication)Application.getInstance())
						.getAndroidIdByRKey(AndroidApplicationR.component_sliding_drawer__sliding_drawer_down__button));
			}
			else {
				this.getHandle().setBackgroundResource(((AndroidApplication)Application.getInstance())
						.getAndroidIdByRKey(AndroidApplicationR.component_sliding_drawer__sliding_drawer_up__button));
			}
		}
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
