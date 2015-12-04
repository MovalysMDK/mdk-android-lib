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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView.OnChildClickListener;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKBaseAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableFlipperExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.FlipperExpandableListConnector.GroupMetadata;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MMAlwaysEnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelListener;

/**
 * <p>3 levels list component</p>
 * @param <ITEM> level 1 item class
 * @param <SUBITEM> level 2 item class 
 * @param <SUBSUBITEM> level 3 item class
 * @param <SUBSUBITEMVM> level 3 view model class
 * @param <SUBITEMVM> level 2 view model class
 * @param <ITEMVM> level 1 view model class
 */
@MMAlwaysEnable
public class MMFlipperExpandableListView<
ITEM extends MIdentifiable,
SUBITEM extends MIdentifiable,
SUBSUBITEM extends MIdentifiable,
SUBSUBITEMVM extends ItemViewModel<SUBSUBITEM>,
SUBITEMVM extends ExpandableViewModel<SUBITEM, SUBSUBITEM, SUBSUBITEMVM>,
ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> 
extends AbstractMMLinearLayout<ListViewModel<ITEM,ITEMVM>> 
implements ListViewModelListener, OnClickListener, MMAdaptableListView {

	/** the adapter for the component*/
	private FlipperExpandableListAdapter currentAdapter=null;

	/** the next Button on the UI*/
	private Button oUINextButton=null;
	
	/** the previous Button on the UI*/
	private Button oUIPreviousButton=null;
	
	/** the title layout in the center of arrows*/
	private ViewGroup oUiTitleLayout=null;
	
	/** the ViewFlipper component in the layout*/
	private InnerExpandableListView oUIExpListView=null;
	
	/** the application */
	private AndroidApplication oApplication=(AndroidApplication)Application.getInstance();

	/** DataSetObserver */
	private DataSetObserver mDataSetObserver;

	/** Current Item */
	private int currentTitle;

	/** Adapter */
	FlipperExpandableListConnector oFlipperExpandableListConnector = null;

	/**
	 * <p>
	 *  Construct an object <em>MMFlipperExpandableListView</em>.
	 * </p>
	 * @param p_oContext The application context
	 */
	public MMFlipperExpandableListView(Context p_oContext) {
		this(p_oContext,null);
	}

	/**
	 * <p>
	 *  Construct an object <em>MMFlipperExpandableListView</em>.
	 * </p>
	 * @param p_oContext The application context
	 * @param p_oAttrs xml attributes to use
	 */
	public MMFlipperExpandableListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, NoneType.class);
		if(!isInEditMode()) {
			LayoutInflater oInflater = LayoutInflater.from(this.getContext());
			oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component__flipper_expandable_list__panel), this);
			oUINextButton = (Button) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__btn_next__button));
			oUINextButton.setOnClickListener(this);
			oUIPreviousButton = (Button) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__btn_previous__button));
			oUIPreviousButton.setOnClickListener(this);
			oUIExpListView=(InnerExpandableListView)this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list_item__list__master_id));
			oUiTitleLayout=(ViewGroup)this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__title__layout));
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		if (this.oFlipperExpandableListConnector != null
				&& this.oFlipperExpandableListConnector.getTitleExpandableList().containsKey(currentTitle)) {
			List<GroupMetadata> oMetas = this.oFlipperExpandableListConnector.getTitleExpandableList().get(currentTitle);
			
			for (GroupMetadata oMeta : oMetas) {
				if (oMeta.isExpanded()) {
					this.oUIExpListView.expandGroup(oMeta.getCurrentID());
				}
			}
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	/**
	 * Define adapter for the flipper and show current child
	 * @param p_oAdapter new adapter
	 */
	public void setAdapter(FlipperExpandableListAdapter p_oAdapter){
		if (this.currentAdapter != null) {
			this.currentAdapter.unregisterDataSetObserver(this.mDataSetObserver);
		}

		if (p_oAdapter != null) {
			this.mDataSetObserver = new AdapterDataSetObserver();
			p_oAdapter.registerDataSetObserver(this.mDataSetObserver);
		}

		this.currentAdapter = p_oAdapter;
		
		MDKViewConnector oConnector = (MDKViewConnector) p_oAdapter;
		MDKBaseAdapter oBaseAdapter = oConnector.getAdapter();
		
		this.oFlipperExpandableListConnector = new FlipperExpandableListConnector(p_oAdapter, 0);


//		this.oFlipperExpandableListConnector = new FlipperExpandableListConnector(this.currentAdapter ,0);
		oUIExpListView.setAdapter(this.oFlipperExpandableListConnector);
		//oUIExpListView.setListAdapter(this.currentAdapter);
		
		if (oBaseAdapter instanceof OnChildClickListener) {
			oUIExpListView.setOnChildClickListener((OnChildClickListener) oBaseAdapter);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setListAdapter(MMListAdapter p_oAdapter) {
		this.setAdapter((AbstractConfigurableFlipperExpandableListAdapter<ITEM,SUBITEM,SUBSUBITEM,SUBSUBITEMVM, SUBITEMVM, ITEMVM>) p_oAdapter);
	}

	@Override
	public MMListAdapter getListAdapter() {
		return (MMListAdapter) this.currentAdapter;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable oSuperState = this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
		Bundle oState = new Bundle();
		oState.putParcelable("superState", oSuperState);
		oState.putInt("currentIndex", this.currentTitle);

		oState.putSerializable("expandedData", (Serializable) this.oFlipperExpandableListConnector.getTitleExpandableList());
		return oState;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		Bundle oBundle = (Bundle) p_oState;
		Parcelable oSuperState = oBundle.getParcelable("superState");
		super.onRestoreInstanceState(((BaseSavedState) oSuperState).getSuperState());
		this.aivFwkDelegate.onRestoreInstanceState(oSuperState);
		this.currentTitle = (oBundle.getInt("currentIndex"));
		showCurrentChild();	

		this.oFlipperExpandableListConnector.setTitleExpandableList((Map<Integer, ArrayList<GroupMetadata>>) ((Bundle) p_oState).getSerializable("expandedData"));
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View p_oArg0) {
		if(p_oArg0.getId()==oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__btn_next__button)){
			this.showNext();
		}
		else if(p_oArg0.getId()==oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__btn_previous__button)){
			this.showPrevious();
		}

	}

	/**
	 * Shows the next page
	 */
	public void showNext() {
		if (hasNext()) {
			this.currentTitle++;
			showCurrentChild();
			this.restoreGroupPosition();
		}
	}

	/**
	 * Returns true if the component has another page to display
	 * @return true if the component has another page to display
	 */
	private boolean hasNext() {
		return ((this.currentTitle+1) < this.currentAdapter.getTitleCount());
	}

	/**
	 * Shows the previous page
	 */
	public void showPrevious() {
		if (hasPrevious()) {
			this.currentTitle--;
			showCurrentChild();
			this.restoreGroupPosition();
		}
	}

	/**
	 * Returns true if there is a previous page
	 * @return true if there is a previous page
	 */
	private boolean hasPrevious() {
		return ((this.currentTitle-1) >= 0);
	}

	/**
	 * Restores the expanded state of the groups
	 */
	private void restoreGroupPosition(){
		List<GroupMetadata> listGroupPosition = this.oFlipperExpandableListConnector.getGoupExpandableList(this.currentTitle);
		if (listGroupPosition != null && !listGroupPosition.isEmpty()) {
			for(GroupMetadata positionGroup : listGroupPosition) {
				oUIExpListView.expandGroup(positionGroup.getCurrentID());
			}
		}
	}

	/**
	 * to display the current pane 
	 */
	public void showCurrentChild() {
		updateButtonState();
		createTitleView();
		displayCurrentTitle();

	}

	/**
	 * Set the correct state for previous and next button
	 */
	private void updateButtonState() {
		if (this.hasNext()){
			oUINextButton.setVisibility(View.VISIBLE);
		}
		else{
			oUINextButton.setVisibility(View.INVISIBLE);
		}
		if (this.hasPrevious()){
			oUIPreviousButton.setVisibility(View.VISIBLE);
		}
		else{
			oUIPreviousButton.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * <p>
	 * remove the inflated views and create new ones based on the current ViewModel
	 * </p>
	 */
	protected void displayCurrentTitle(){
		this.oFlipperExpandableListConnector.setCurrentTitleID(this.currentTitle, true);
	}

	/**
	 * This method can be overridden to wrap the components in the
	 * MasterVisualCommponent of the master list
	 * 
	 * @param p_oCurrentRow
	 *            the current view
	 * @param p_oCurrentViewModel
	 *            The view Model of this item
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 */
	protected void wrapMasterView(View p_oCurrentRow, ITEMVM p_oCurrentViewModel, int p_iTitlePosition) {
		// nothing to do in this implementation, the automatic binding is used

	}

	/**
	 * create Title View
	 */
	private void createTitleView(){
		//récupère la titleView de l'adapter
		View oTitle=currentAdapter.getTitleView(this.currentTitle, true, oUiTitleLayout.getChildAt(0), oUiTitleLayout);
		if (oTitle.getParent()==null){
			oUiTitleLayout.addView(oTitle);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelListener#doOnListVMChanged()
	 */
	@Override
	public void doOnListVMChanged() {
		this.displayCurrentTitle();
	}

	/**
	 * Custom Adapter DataSetObserver
	 */		
	class AdapterDataSetObserver extends DataSetObserver {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onChanged() {
			super.onChanged();
//			MMFlipperExpandableListView.this.aivDelegate.configurationSetValue(MMFlipperExpandableListView.this.aivDelegate.configurationGetValue());
			MMFlipperExpandableListView.this.showCurrentChild();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onInvalidated() {
			super.onInvalidated();
//			MMFlipperExpandableListView.this.aivDelegate.configurationSetValue(MMFlipperExpandableListView.this.aivDelegate.configurationGetValue());
			MMFlipperExpandableListView.this.showCurrentChild();
		}
	}
}
