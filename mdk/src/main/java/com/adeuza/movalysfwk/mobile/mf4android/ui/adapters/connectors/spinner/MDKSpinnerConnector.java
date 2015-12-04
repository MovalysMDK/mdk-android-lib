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
package com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.spinner;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKSpinnerAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevelTreatment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.SpinnerAdapterDropDown;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.SpinnerAdapterView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

public class MDKSpinnerConnector<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> 
	extends BaseAdapter implements MDKViewConnector {

	private MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> mAdapter ;
	
	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	@SuppressWarnings("rawtypes")
	public enum Level implements AdapterLevel {
		/** View level */
		VIEW(SpinnerAdapterView.class),
		/** Dropdown level */
		DROPDOWN(SpinnerAdapterDropDown.class);
		
		/** treatments class for the level */
		private AdapterLevelTreatment level;
		
		/** 
		 * Constructor
		 * @param p_oLevelClass the treatment class for the level 
		 */
		Level(Class<? extends AdapterLevelTreatment> p_oLevelClass) {
			try {
				this.level = p_oLevelClass.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException 
					| NoSuchMethodException | SecurityException e) {
				Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			}
		}

		/**
		 * returns the treatment class for the level
		 * @return the treatment class for the level
		 */
		@Override
		public AdapterLevelTreatment getLevel() {
			return this.level;
		}
	}
	
	public MDKSpinnerConnector( MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter ) {
		this.mAdapter = p_oAdapter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeViewModelChanged() {
		// nothing to do
	}
	
	public void afterViewModelChanged() {
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mAdapter.getCount();
	}

	@Override
	public ITEMVM getItem(int p_iParamInt) {		
		return mAdapter.getItem(p_iParamInt);
	}

	@Override
	public long getItemId(int position) {
		return mAdapter.getItemId(position);
	}

	@Override
	public View getView(int p_iPosition, View p_oView, ViewGroup p_oViewGroup) {
		View r_oView = this.mAdapter.getLegacyViewByLevel(Level.VIEW, false, p_oView, p_oViewGroup, this.getItemViewType(p_iPosition), p_iPosition);
		
		if (p_iPosition == getSelectedPosition()) {
			doOnErrorOnSelectedItem(r_oView, this.mAdapter.getErrorMessage());
		}
		
		return r_oView;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int p_iParamInt, View p_oView, ViewGroup p_oViewGroup) {
		return this.mAdapter.getLegacyViewByLevel(Level.DROPDOWN, false, p_oView, p_oViewGroup, this.getItemViewType(p_iParamInt), p_iParamInt);
	}
	
	@Override
	public MDKSpinnerAdapter<ITEM, ITEMVM, LISTVM> getAdapter() {
		return this.mAdapter;
	}

	/**
	 * Returns the position of a given item in the adapter
	 * @param p_oItem the item to look for
	 * @return the position found
	 */
	public int indexOf(ITEMVM p_oItem) {
		return this.mAdapter.indexOf(p_oItem);
	}
	
	/**
	 * This method can be overridden to wrap the components in the MasterVisualCommponent
	 * @param p_oView the current view
	 * @param p_oCurrentViewModel The view Model of this item
	 * @param p_iPosition the position of the current view in the list
	 */
	public void postInflateCurrentView(View p_oView) {
		// nothing to do in this implementation, the automatic mapping is used
	}
	
	public void postBindCurrentView(View p_oView, Object p_oCurrentViewModel, int p_iPosition) {
		// nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * This method can be overridden to wrap the components in the MasterVisualCommponent of the drop down view
	 * @param p_oView the current view
	 * @param p_oCurrentViewModel The view Model of this item
	 * @param p_iPosition the position of the current view in the list
	 * @param p_isSelected true if the current view is the selected one
	 */
	public void postInflateDropDownView(View p_oView, Object p_oCurrentViewModel, boolean p_isSelected) {
		// nothing to do
	}
	
	public void postBindDropDownView(View p_oView, Object p_oCurrentViewModel, int p_iPosition, boolean p_isSelected) {
		if (this.mAdapter.getSelectedComponent() != -1) {
			View oComponent = p_oView.findViewById(this.mAdapter.getSelectedComponent());
			if (oComponent == null) {
				oComponent = p_oView.findViewById(((AndroidApplication) Application.getInstance())
						.getAndroidIdByRKey(AndroidApplicationR.component__simple_spinner_dropdown_emptyitem__value));
			}
			if (oComponent instanceof CheckedTextView) {
				((CheckedTextView) oComponent).setChecked(p_isSelected);
			}
		}
	}
	
	/**
	 * Returns true if the spinner displays a selectable empty item
	 * @return true if the spinner has an empty item
	 */
	public boolean hasEmptyItem() {
		return this.mAdapter.hasEmptyItem();
	}

	/**
	 * Sets whether the spinner should display an empty item
	 * @param p_bEmptyValue true if the spinner should display an empty item
	 */
	public void enableEmptyItem(boolean p_bEmptyValue) {
		this.mAdapter.enableEmptyItem(p_bEmptyValue);
	}
	
	/**
	 * Returns the selected position
	 * @return the selected position
	 */
	public int getSelectedPosition() {
		return this.mAdapter.getSelectedPosition();
	}
	
	/**
	 * Sets the selected item position in the spinner
	 * @param p_iPosition the position of the selected item
	 */
	public void setSelectedPosition(int p_iPosition) {
		this.mAdapter.setSelectedPosition(p_iPosition);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemViewType(int p_oPosition) {
		if (this.hasEmptyItem() && p_oPosition == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getViewTypeCount() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.mAdapter.getCount() == 0;
	}
	
	/**
	 * Sets the error message on the spinner
	 * @param p_sError the error message to set
	 */
	public void setErrorMessage(String p_sError) {
		this.mAdapter.setErrorMessage(p_sError);
	}
	
	/** 
	 * sets the error component by its identifier
	 * @param errorComponent the error component identifier
	 */
	public void setErrorComponent(int errorComponent) {
		this.mAdapter.setErrorComponent(errorComponent);
	}
	
	public int getSelectedComponent() {
		return this.mAdapter.getSelectedComponent();
	}

	public void setSelectedComponent(int selectedComponent) {
		this.mAdapter.setSelectedComponent(selectedComponent);
	}

	/**
	 * Returns the master view model interface
	 * @return the master view model interface
	 */
	public Class<?> getItemVMInterface() {
		return this.mAdapter.getMasterVM().getItemVmInterface();
	}
	
	/**
	 * Return ItemVM by Id
	 * @param p_sId id of ItemVM
	 * @return an ItemVM
	 */
	public Object getItemVMById( String p_sId ) {
		return this.mAdapter.getMasterVM().getCacheVMById(p_sId);
	}
	
	/**
	 * Retourne l'item sélectionné dans l'adapter de liste courant.
	 * 
	 * @return objet sélectionné, null sinon
	 */
	public Object getSelectedItem() {
		return this.mAdapter.getSelectedItem();
	}
	
	/**
	 * Return the position in the spinner of the given ITEMVM
	 * @param p_oITEMVM the ITEMVM object to look for
	 * @return the position found
	 */
	public int getItemVMPos(ITEMVM p_oITEMVM) {
		return this.mAdapter.indexOf(p_oITEMVM);
	}
	
	public Object getCacheVMByPosition(int p_iPosition) {
		return this.mAdapter.getMasterVM().getCacheVMByPosition(p_iPosition);
	}

	/**
	 * Called to set an error on the spinner
	 * @param p_oView the view holding the spinner
	 * @param p_sMessage the error message to set
	 * 
	 */
	public void doOnErrorOnSelectedItem(View p_oView, String p_sMessage) {
		if (this.mAdapter.getErrorComponent() != -1) {
			View oComponent = p_oView.findViewById(this.mAdapter.getErrorComponent());
			if (oComponent == null) {
				// on cherche si c'est l'élément null qui est affiché
				oComponent = p_oView
						.findViewById(((AndroidApplication) Application
								.getInstance())
								.getAndroidIdByRKey(AndroidApplicationR.component__simple_spinner_emptyitem__value));
			}
			if (oComponent instanceof TextView) {
				if (p_sMessage == null) {
					((TextView) oComponent).setError(null);
				} else {
					ArrayList<View> oList = new ArrayList<>();
					oList.add(oComponent);
					p_oView.addFocusables(oList, View.FOCUSABLES_ALL);
					((TextView) oComponent).setError(p_sMessage);
				}
			}
		}
		this.mAdapter.setErrorMessage(p_sMessage);
	}
}
