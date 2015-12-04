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
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMRecyclableList;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKBaseAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.recyclerview.MDKRecyclerViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.twowayviews.ItemClickSupport;
import com.adeuza.movalysfwk.twowayviews.ItemSelectionSupport;
import com.adeuza.movalysfwk.twowayviews.ItemSelectionSupport.ChoiceMode;

/**
 * MMRecyclerView component
 *
 * @param <ITEM> entity
 * @param <ITEMVM> item viewmodel
 * @param <LISTVM> list viewmodel
 */
public class MMRecyclerView<
		ITEM extends MIdentifiable, 
		ITEMVM extends ItemViewModel<ITEM>, 
		LISTVM extends ListViewModel<ITEM,ITEMVM>> 
	extends RecyclerView 
	implements ConfigurableVisualComponent, MMAdaptableListView, ComponentBindDestroy,
	ItemClickSupport.OnItemClickListener, MMPerformItemClickView, MMRecyclableList {

	/**
	 * Delegate
	 */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/**
	 * Delegate
	 */
	private AndroidConfigurableVisualComponentDelegate<ListViewModel<ITEM,ITEMVM>> aivDelegate = null;
	
	/**
	 * Listener for perform item click
	 */
	private MMPerformItemClickListener performItemClickListener;
	
	/**
	 * Listener for item click
	 */
	private MMOnItemClickListener mdkItemClickListener;
	
	/**
	 * Support for item selection
	 */
	private ItemSelectionSupport mItemSelection ;
	
	/**
	 * Support for item click
	 */
	private boolean mHasItemClick = false;
	
	/**
	 * Choice mode
	 */
	private ChoiceMode mChoiceMode ;
	
	/**
	 * Constructor
	 * @param p_oContext 
	 */
	public MMRecyclerView(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ListViewModel<ITEM, ITEMVM>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
			init(null);
		}
	}
	
	/**
	 * Constructor
	 * @param p_oContext context
	 * @param p_oAttrs attributes
	 */
	public MMRecyclerView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ListViewModel<ITEM, ITEMVM>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			init(p_oAttrs);
		}
	}
	
	/**
	 * Constructor
	 * @param p_oContext context
	 * @param p_oAttrs attribute
	 * @param p_iDefStyle def style
	 */
	public MMRecyclerView(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs, p_iDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ListViewModel<ITEM, ITEMVM>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			init(p_oAttrs);
		}
	}
	
	/**
	 * Initialize the recycler view with the attributes
	 * @param p_oAttrs attributes
	 */
	protected void init( AttributeSet p_oAttrs ) {
		
		// fixed size
		setHasFixedSize(p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "fixed_size", true));
		
		// item click support
		this.mHasItemClick = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "item_click", false);
		
		// choice mode
		String sChoiceMode = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "choice_mode");
		if ( sChoiceMode == null ) {
			mChoiceMode = ChoiceMode.NONE;
		}
		else {
			switch ( sChoiceMode ) {
				case "SINGLE":
					mChoiceMode = ChoiceMode.SINGLE;
					break;
				case "MULTIPLE":
					mChoiceMode = ChoiceMode.MULTIPLE;
					break;
				case "NONE":
					mChoiceMode = ChoiceMode.NONE;
					break;
				default:
					throw new IllegalArgumentException("choice-mode on RecyclerView not valid. Choose between SINGLE, MULTIPLE, NONE");
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdapter(Adapter p_oAdapter) {
		super.setAdapter(p_oAdapter);
		
		MDKViewConnector oConnector = (MDKViewConnector) p_oAdapter;
		MDKBaseAdapter oBaseAdapter = oConnector.getAdapter();
		
		if ( oBaseAdapter instanceof MMPerformItemClickListener) {
			this.performItemClickListener = (MMPerformItemClickListener) oBaseAdapter;
		}
		
		if ( oBaseAdapter instanceof MMOnItemClickListener ) {
			this.mdkItemClickListener = (MMOnItemClickListener) oBaseAdapter;
		}

		if ( this.mHasItemClick ) {
			final ItemClickSupport itemClick = ItemClickSupport.addTo(this);
			itemClick.setOnItemClickListener(this);
		}
		
		mItemSelection = ItemSelectionSupport.addTo(this);
		mItemSelection.setChoiceMode(mChoiceMode);
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void onItemClick(RecyclerView p_oParent, View p_oView, int p_iPosition, long p_lId) {
    	
    	mItemSelection.setItemChecked(p_iPosition, true);
    	
    	if ( this.mdkItemClickListener != null ) {
    		ConfigurableListViewHolder oHolder = (ConfigurableListViewHolder) this.getChildViewHolder(p_oView);
			this.mdkItemClickListener.onItemClick(p_oView, p_iPosition, p_lId, this, oHolder);
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentDelegate getComponentDelegate() {
		return this.aivDelegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnPostAutoBind() {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setListAdapter(MMListAdapter p_oAdapter) {
		this.setAdapter((Adapter) p_oAdapter);
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
	public boolean superPerformItemClick(View p_oView, int p_iPosition, long p_iId) {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		
		ItemClickSupport.removeFrom(this);
		ItemSelectionSupport.removeFrom(this);
		
		MDKRecyclerViewConnector oConnector = (MDKRecyclerViewConnector) this.getAdapter();
		MDKAdapter<?,?,?> oAdapter = oConnector.getAdapter();
		if (oAdapter != null) {
			oAdapter.uninflate();
		}
	}
}
