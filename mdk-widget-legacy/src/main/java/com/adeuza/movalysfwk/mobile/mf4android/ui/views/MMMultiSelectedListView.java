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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentNullOrEmpty;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MultiSelectedExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MultiSelectedFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.SelectedComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl;

/**
 * <p>
 * 	This component allows multiple selections in a list with two levels.
 * 	In this component we also find a summary of the selected items in a section defined at the top of the screen.
 * 	This summary is defined by using an <code>MMMultiSelectedFixedListView</code> class that extend an <code>AbstractFixedListView</code>.
 * 	The list is defined by using an  <code>MMMultiSelectedExpandableListView</code> class that extends an <code>MMExpandableListView</code>.
 * </p>
 * 
 * @param <ITEM> a model class representing Level1 item
 * @param <SUBITEM> a sub model class representing Level2 item
 * @param <SUBITEMVM> a view model class representing Level2 item
 * @param <ITEMVM> a view model class representing Level1 item
 */
public class MMMultiSelectedListView<ITEM extends MIdentifiable, SUBITEM extends MIdentifiable, SUBITEMVM extends ItemViewModel<SUBITEM>, ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> 
	extends AbstractMMRelativeLayout<List<SUBITEMVM>> implements OnChildClickListener, SelectedComponent, ComponentNullOrEmpty<List<ITEMVM>>, 
	ComponentReadableWrapper<List<ITEMVM>>, ComponentWritableWrapper<List<ITEMVM>> {

	/** the maximum number of selectable item */
	private int maxNumberOfSelectableItem = -1;

	/** the {@link AndroidApplication} object */ 
	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	/** the selected items */
	private MMMultiSelectedFixedListView<SUBITEM, SUBITEMVM> selected;
	
	/** the adapter for the selected items */
	private AbstractConfigurableFixedListAdapter<SUBITEM, SUBITEMVM, ListViewModel<SUBITEM,SUBITEMVM>> selectedAdapter;

	/** the level 2 list */
	private MMExpandableListView<ITEM,SUBITEM, SUBITEMVM, ITEMVM> expandable;
	
	/** the level 2 list adapter */
	private MultiSelectedExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ExpandableViewModel<ITEM,SUBITEM,SUBITEMVM>> expandableAdapter;

	/** the item view model for level 2 */
	private Class<SUBITEMVM> classSubItemVm;

	/**
	 * Construct a new <code>MMMultiSelectedListView</code> component.
	 * @param p_oContext The current context
	 */
	public MMMultiSelectedListView(Context p_oContext) {
		super(p_oContext, List.class);
	}

	/**
	 * Construct a new <code>MMMultiSelectedListView</code> component.
	 * @param p_oContext The current context
	 * @param p_oAttrs xml attributes
	 */
	public MMMultiSelectedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs, List.class);
		if(!isInEditMode()) {
			maxNumberOfSelectableItem=p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "selectableMax",-1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){

			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(application.getAndroidIdByRKey(AndroidApplicationR.fwk_component__multi_selected_expandable_list__panel), this);

			if (maxNumberOfSelectableItem==-1){
				maxNumberOfSelectableItem = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.default_maxNumberSelectableItem).getIntValue();
			}
			expandable = (MMExpandableListView<ITEM,SUBITEM, SUBITEMVM, ITEMVM>) this.findViewById(application.getAndroidIdByRKey(AndroidApplicationR.component__multi_selected_expandable_list_view__panel));
			selected = (MMMultiSelectedFixedListView) this.findViewById(application.getAndroidIdByRKey(AndroidApplicationR.component__multi_selected_expandable_list_selected_item_view__panel));
			expandable.setOnChildClickListener(this);
		}
	}

	/**
	 * returns the adapter of the component
	 * @return the adapter
	 */
	public MultiSelectedExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ExpandableViewModel<ITEM,SUBITEM,SUBITEMVM>> getAdapter() {
		return expandableAdapter;
	}

	/**
	 * Defines the adapters for the <code>MMMultiSelectedListView</code> component.
	 * In that component we must create two adapters based on the p_oAdapter Object.
	 * One for the selected FixedList and an other for the expandable list.
	 * @param p_oAdapter Adapter for data representation.
	 * @param p_oClassSubVm the class of the level 2 items
	 */
	public void setAdapter(MultiSelectedExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ExpandableViewModel<ITEM,SUBITEM,SUBITEMVM>> p_oAdapter, Class<SUBITEMVM> p_oClassSubVm) {
		expandableAdapter=p_oAdapter;
		classSubItemVm = p_oClassSubVm;
		expandable.setAdapter(expandableAdapter);
		selected.setExpandableAdapter(p_oAdapter);
		selectedAdapter = new MultiSelectedFixedListAdapter<>(p_oAdapter.getChildLayoutId(),p_oAdapter.getConfigurableChildLayoutId(), p_oClassSubVm);
		selected.setAdapter(selectedAdapter);
		expandableAdapter.setSelectedAdapter(selectedAdapter);
	}

	/**
	 * Return the adapter of the <code>FixedListView</code> of selected items.
	 * @return the adapter of the fixedlist
	 */
	public AbstractConfigurableFixedListAdapter<SUBITEM, SUBITEMVM, ListViewModel<SUBITEM,SUBITEMVM>> getSelectedAdapter() {
		return selected.getAdapter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onChildClick(ExpandableListView p_oExpandableListView, View p_oClickedView, int p_iGroupPosition, int p_iChildPosition, long p_oParamLong) {
		SUBITEMVM oClickedVM = expandableAdapter.getChild(p_iGroupPosition, p_iChildPosition);
		if (selectedAdapter.getMasterVM()==null){
			selectedAdapter.setMasterVM(new ListViewModelImpl<>(classSubItemVm));
		}
		List<SUBITEMVM> oSelectedList = new ArrayList<>();
		for (int i = 0; i < selectedAdapter.getMasterVM().getCount(); i++) {
			oSelectedList.add(selectedAdapter.getMasterVM().getCacheVMByPosition(i));
		}
//		List<SUBITEMVM> oSelectedList = selectedAdapter.getMasterVM().getList();
		if (oSelectedList.contains(oClickedVM)){
			selectedAdapter.getMasterVM().remove(oClickedVM);
			selected.getComponentDelegate().configurationSetValue(selectedAdapter.getMasterVM());
			expandableAdapter.notifyDataSetChanged();
		}
		else if (oSelectedList.size()<maxNumberOfSelectableItem){
			selectedAdapter.getMasterVM().add(oClickedVM);
			selected.getComponentDelegate().configurationSetValue(selectedAdapter.getMasterVM());
			expandableAdapter.notifyDataSetChanged();
		}
		return false;
	}

	/**************************************************************************************************************
	 ************************************* Framework delegate callback ********************************************
	 **************************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public List<ITEMVM> configurationGetValue() {
		List<ITEMVM> r_oList = new ArrayList<>();
		for (int i = 0; i < this.getSelectedAdapter().getMasterVM().getCount(); i++) {
			r_oList.add((ITEMVM) this.getSelectedAdapter().getMasterVM().getCacheVMByPosition(i));
		}
		return r_oList;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(List<ITEMVM> p_oObjectToSet) {
		this.getSelectedAdapter().getMasterVM().clear();
		this.getSelectedAdapter().getMasterVM().addAll((Collection) p_oObjectToSet);
//		selectedAdapter.getMasterVM().setList(p_oListItem);
		selected.getComponentDelegate().configurationSetValue(this.getSelectedAdapter().getMasterVM());
		this.getAdapter().notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		if (this.selected == null || this.selected.getAdapter() == null || this.selected.getAdapter().getMasterVM() == null) {
			return false;
		}
		return selected.getAdapter().getMasterVM().getCount() > 0;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentNullOrEmpty#isNullOrEmptyValue(Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(List<ITEMVM> p_oObject) {
		return p_oObject==null|| p_oObject.isEmpty();
	}
}
