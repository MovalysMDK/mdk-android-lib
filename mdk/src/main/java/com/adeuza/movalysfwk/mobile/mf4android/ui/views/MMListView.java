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
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.listview.MDKListViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MMAlwaysEnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;


/**
 * <p>ListView widget used in the Movalys Mobile product for Android</p>
 *
 * 
 * @param <ITEM> ITEM in list
 * @param <ITEMVM> ITEMVM in list
 * @param <LISTVM> LISTVM the listviewmodel class
 *
 *
 */
@MMAlwaysEnable
public class MMListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM,ITEMVM>> extends ListView 
	implements ConfigurableVisualComponent, MMAdaptableListView, MMPerformItemClickView, ComponentBindDestroy,
		android.widget.AdapterView.OnItemClickListener {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<ListViewModel<ITEM,ITEMVM>> aivDelegate = null;
	
	/** itemclick listener */
	private MMPerformItemClickListener performItemClickListener;

	private MMOnItemClickListener mdkItemClickListener;
	
	private boolean oldAdapter = false;
	
	/** 
	 * Constructs a new MMListView
	 * @param p_oContext the context to use
	 * @see ListView#ListView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMListView(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ListViewModel<ITEM, ITEMVM>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}

	/**
	 * Constructs a new MMListView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see ListView#ListView(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ListViewModel<ITEM, ITEMVM>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * Constructs a new MMListView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use 
	 * @see ListView#ListView(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMListView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ListViewModel<ITEM, ITEMVM>>) DelegateInitialiserHelper.initDelegate(this, 
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdapter(ListAdapter p_oAdapter) {
		super.setAdapter(p_oAdapter);
		
		Object oBaseAdapter = null;
		
		if (p_oAdapter instanceof MDKViewConnector) {
			MDKViewConnector oConnector = (MDKViewConnector) p_oAdapter;
			oBaseAdapter = oConnector.getAdapter();
		} else {
			oBaseAdapter = p_oAdapter;
			oldAdapter = true;
		}
		
		if ( oBaseAdapter instanceof MMPerformItemClickListener) {
			this.performItemClickListener = (MMPerformItemClickListener) oBaseAdapter;
		}
		
		if ( oBaseAdapter instanceof MMOnItemClickListener ) {
			this.mdkItemClickListener = (MMOnItemClickListener) oBaseAdapter;
		}
		
		this.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> p_oParent, View p_oView, int p_iPosition, long p_lId) {
		if ( this.mdkItemClickListener != null ) {
			ConfigurableListViewHolder oHolder = (ConfigurableListViewHolder) p_oView.getTag();
			this.mdkItemClickListener.onItemClick(p_oView, p_iPosition, p_lId, this, oHolder);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setListAdapter(MMListAdapter p_oAdapter) {
		this.setAdapter((ListAdapter) p_oAdapter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MMListAdapter getListAdapter() {
		return (MMListAdapter) this.getAdapter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performItemClick( final View p_oView, final int p_iPosition, final long p_lId) {
		boolean doSuper = true ;
		boolean handled = false ;
		if ( performItemClickListener != null ) {
			doSuper = performItemClickListener.onPerformItemClick(p_oView, p_iPosition, p_lId, this);
		}
		if ( doSuper ) {
			handled = super.performItemClick(p_oView, p_iPosition, p_lId);
		}

		return handled ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean superPerformItemClick(View p_oView, int p_iPosition, long p_lId) {
		return super.performItemClick(p_oView, p_iPosition, p_lId);
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<ListViewModel<ITEM,ITEMVM>> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/************************************************************************************************************
	 ************************************ Framework delegate callback *******************************************
	 ************************************************************************************************************/
	
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
	 * @see ComponentBindDestroy#destroy()
	 */
	@Override
	public void destroy() {
		if (!oldAdapter) {
			MDKListViewConnector oConnector = null;
			if (this.getAdapter() instanceof HeaderViewListAdapter) {
				oConnector = (MDKListViewConnector) ((HeaderViewListAdapter)this.getAdapter()).getWrappedAdapter();
			} else {
				oConnector = (MDKListViewConnector) this.getAdapter();
			}
			if (oConnector != null) {
				oConnector.getAdapter().uninflate();
			}
		} else {
			AbstractConfigurableListAdapter oAdapter = null;
			if (this.getAdapter() instanceof HeaderViewListAdapter) {
				oAdapter = (AbstractConfigurableListAdapter) ((HeaderViewListAdapter)this.getAdapter()).getWrappedAdapter();
			} else {
				oAdapter = (AbstractConfigurableListAdapter) this.getAdapter();
			}
			if (oAdapter != null) {
				oAdapter.uninflate();
			}
		}
	}
}
