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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MMAlwaysEnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * <p>
 * ExpandableListView widget used in the Movalys Mobile product for Android
 * </p>
 * 
 * @param <ITEM> a model class representing Level1 item
 * @param <SUBITEM> a sub model class representing Level2 item
 * @param <SUBITEMVM> a view model class representing Level2 item
 * @param <ITEMVM> a view model class representing Level1 item
 */
@MMAlwaysEnable
public class MMExpandableListView<ITEM extends MIdentifiable, SUBITEM extends MIdentifiable, 
	SUBITEMVM extends ItemViewModel<SUBITEM>, ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>>
		extends ExpandableListView 
		implements ConfigurableVisualComponent, MMAdaptableListView, InstanceStatable, MMPerformItemClickView, ComponentBindDestroy {
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<Object> aivDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;

	/** y position of the first visible element */
	private int firstVisiblePosition = 0;

	/** Top y position of the first visible element */
	private int firstVisibleTop = 0;
	
	/** listener for item click on list (this can intercept event in case of modify data cancel action) */
	private MMPerformItemClickListener performItemClickListener;
	
	private MDKViewConnector connector;
	
	/**
	 * Constructs a new MMListView
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @see ExpandableListView#ExpandableListView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMExpandableListView(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Object>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}

	/**
	 * Constructs a new MMListView
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see ExpandableListView#ExpandableListView(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMExpandableListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Object>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * Constructs a new MMListView
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @param p_oDefStyle
	 *            the style to use
	 * @see ExpandableListView#ExpandableListView(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMExpandableListView(Context p_oContext, AttributeSet p_oAttrs,int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Object>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, p_oAttrs});
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
	protected void onWindowVisibilityChanged(int p_iVisibility) {
		super.onWindowVisibilityChanged(p_iVisibility);
		if (View.VISIBLE == p_iVisibility) {
			this.setSelectionFromTop(this.firstVisiblePosition, this.firstVisibleTop);
		}
		else {
			this.firstVisiblePosition	= this.getFirstVisiblePosition();
			if (this.getChildAt(0) == null ){
				this.firstVisibleTop = 0 ;
			} else {
				this.firstVisibleTop = this.getChildAt(0).getTop();
			}
		}
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
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);
	}

	/**
	 * Set the adapter on Android ExpandableListView
	 * 
	 * @param p_oAdapter the android adapter
	 */
	@Override
	public void setAdapter(ExpandableListAdapter p_oAdapter) {
		super.setAdapter(p_oAdapter);
		
		Object oBaseAdapter = null;
		
		if (p_oAdapter instanceof MDKViewConnector) {
			connector = (MDKViewConnector) p_oAdapter;
			oBaseAdapter = connector.getAdapter();
		} else {
			oBaseAdapter = p_oAdapter;
		}
		
		if ( oBaseAdapter instanceof MMPerformItemClickListener) {
			this.performItemClickListener = (MMPerformItemClickListener) oBaseAdapter;
		}
		
		if (oBaseAdapter instanceof OnChildClickListener) {
			setOnChildClickListener((OnChildClickListener) oBaseAdapter);
		}
	}
	
	@Override
	public void setListAdapter(MMListAdapter p_oAdapter) {
		this.setAdapter((ExpandableListAdapter) p_oAdapter);
		if ( p_oAdapter instanceof MMPerformItemClickListener) {
			this.performItemClickListener = (MMPerformItemClickListener) p_oAdapter;
		}
		setOnChildClickListener((OnChildClickListener) p_oAdapter);
	}

	@Override
	public MMListAdapter getListAdapter() {
		return (MMListAdapter) this.getExpandableListAdapter();
	}
	
	@Override
	public boolean performItemClick( final View p_oView, final int p_iPosition, final long p_iId) {
		boolean doSuper = true ;
		boolean handled = false ;
		if ( performItemClickListener != null ) {
			doSuper = performItemClickListener.onPerformItemClick(p_oView, p_iPosition, p_iId, this);
		}
		if ( doSuper ) {
			handled = super.performItemClick(p_oView, p_iPosition, p_iId);
		}
		
		return handled ;
	}
	
	@Override
	public boolean superPerformItemClick(View p_oView, int p_iPosition, long p_iId) {
		return super.performItemClick(p_oView, p_iPosition, p_iId);
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<Object> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#doOnPostAutoBind()
	 */
	@Override
	public void doOnPostAutoBind() {
		// nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		if (connector != null) {
			connector.getAdapter().uninflate();
		} else {
			AbstractConfigurableExpandableListAdapter oAdapter = (AbstractConfigurableExpandableListAdapter) this.getListAdapter();
			if (oAdapter != null) {
				oAdapter.uninflate();
			}
		}
	}
}
