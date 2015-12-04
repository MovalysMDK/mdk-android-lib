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
package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSpinnerCheckedTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * <p>Adapter for a Spinner</p>
 *
 *
 * @param <ITEM> a model for a spinner item
 * @param <LISTVM> a viewmodel for the list
 * @param <ITEMVM> a viewmodel for the items
 *
 */
@Deprecated
public abstract class AbstractConfigurableSpinnerAdapter<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> 
	extends BaseAdapter {
	
	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	@SuppressWarnings("rawtypes")
	private enum Level implements AdapterLevel {
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
	
	/** the list model */
	private LISTVM masterVM = null;
	/** android id of layout (the name of file) for item */
	private int spinnerItemLayoutId = 0;
	/** android id of configurable layout for the drop down list */
	private int spinnerDropDowListLayoutId = 0;
	/** android id of configurable view in the dropdown layout */
	private int configurableMasterDropDowId = 0;
	
	/** the component for selection */
	private int selectedComponent = -1;
	/** the component for error */
	private int errorComponent = -1;
	
	/** <code>true</code> if an empty value must be added by this adapter. */
	private boolean useEmptyValue = true;

	/** the selected position */
	private int selectedPosition = -1;

	/** Inflated components. */
	protected Set<MasterVisualComponent> components;
	
	/** adapter delegate */
	private ListAdapterDelegate delegate = new ListAdapterDelegateImpl<AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM>>(this);
	
	private String errorMessage;

	/**
	 * Constructs a new ConfigurableAdapter
	 * @param p_oMasterVM list's view model
	 * @param p_iSpinnerItemLayoutId id of layout (the file name) of the spinner item
	 * @param p_iConfigurableMasterItemId id of configurable layout (the first component) in this spinner item layout. This must be a masterRelativeLayout
	 * @param p_iSpinnerDropDowListLayoutId id of the layout (the file name) of the spinner drop down list
	 * @param p_iConfigurableMasterDropDowId id of the configurable layout (the first component) in this drop down spinner list. This must be a masterRelativeLayout
	 */
	public AbstractConfigurableSpinnerAdapter(LISTVM p_oMasterVM,
			int p_iSpinnerItemLayoutId, int p_iConfigurableMasterItemId,
			int p_iSpinnerDropDowListLayoutId,
			int p_iConfigurableMasterDropDowId) {

		this(p_oMasterVM, p_iSpinnerItemLayoutId, p_iConfigurableMasterItemId,
				p_iSpinnerDropDowListLayoutId, p_iConfigurableMasterDropDowId,
				-1, -1, true);
	}

	/**
	 * Constructs a new ConfigurableAdapter
	 * @param p_oMasterVM list's view model
	 * @param p_iSpinnerItemLayoutId id of layout (the file name) of the spinner item
	 * @param p_iConfigurableMasterItemId id of configurable layout (the first component) in this spinner item layout. This must be a masterRelativeLayout
	 * @param p_iSpinnerDropDowListLayoutId id of the layout (the file name) of the spinner drop down list
	 * @param p_iConfigurableMasterDropDowId id of the configurable layout (the first component) in this drop down spinner list. This must be a masterRelativeLayout
	 * @param p_iSelectedComponent id of component to set selected
	 * @param p_iErrorComponent id of component to set error
	 * @param p_bUseEmptyValue <code>true</code> if an empty value must be added into the first position.
	 */
	public AbstractConfigurableSpinnerAdapter(LISTVM p_oMasterVM,
			int p_iSpinnerItemLayoutId, int p_iConfigurableMasterItemId,
			int p_iSpinnerDropDowListLayoutId,
			int p_iConfigurableMasterDropDowId, int p_iSelectedComponent,
			int p_iErrorComponent, boolean p_bUseEmptyValue) {

		super();

		this.masterVM = p_oMasterVM;
		this.spinnerItemLayoutId = p_iSpinnerItemLayoutId;
		this.spinnerDropDowListLayoutId = p_iSpinnerDropDowListLayoutId;
		this.configurableMasterDropDowId = p_iConfigurableMasterDropDowId;

		this.selectedComponent = p_iSelectedComponent;
		this.errorComponent = p_iErrorComponent;

		this.useEmptyValue = p_bUseEmptyValue;

		this.components = new HashSet<>();
	}

	/**
	 * Return the object <em>selectedComponent</em>.
	 * @return Objet selectedComponent
	 */
	public int getSelectedComponent() {
		return this.selectedComponent;
	}

	/**
	 * Retourne l'item sélectionné dans l'adapter de liste courant.
	 * 
	 * @return objet sélectionné, null sinon
	 */
	public ITEMVM getSelectedItem() {
		int iRealSelectedPosition = this.selectedPosition;
		if (this.hasEmptyItem()) {
			iRealSelectedPosition--;
		}
		if (iRealSelectedPosition > -1) {
			return this.masterVM.getCacheVMByPosition(iRealSelectedPosition);
		}
		return null;
	}

	/**
	 * Return ItemVM by Id
	 * 
	 * @param p_sId
	 *            id of ItemVM
	 * @return an ItemVM
	 */
	public ITEMVM getItemVMById( String p_sId ) {
		return this.masterVM.getCacheVMById(p_sId);
	}

	/**
	 * Return the position in the spinner of the given ITEMVM
	 * @param p_oITEMVM the ITEMVM object to look for
	 * @return the position found
	 */
	public int getItemVMPos(ITEMVM p_oITEMVM) {
		return this.masterVM.indexOf(p_oITEMVM);
	}

	/**
	 * Return the layout id of the <em>spinnerDropDowListLayoutId</em>.
	 * @return the layout id
	 */
	public int getSpinnerDropDowListLayoutId() {
		return this.spinnerDropDowListLayoutId;
	}

	/**
	 * Return the layout id of the <em>configurableMasterDropDowId</em>.
	 * @return the layout id
	 */
	public int getConfigurableMasterDropDowId() {
		return this.configurableMasterDropDowId;
	}

	/**
	 * Returns true if the spinner displays a selectable empty item
	 * @return true if the spinner has an empty item
	 */
	protected boolean hasEmptyItem() {
		return this.useEmptyValue;
	}

	/**
	 * Sets whether the spinner should display an empty item
	 * @param p_bEmptyValue true if the spinner should display an empty item
	 */
	public void enableEmptyItem(boolean p_bEmptyValue) {
		this.useEmptyValue = p_bEmptyValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		int iIndex = 0;
		if (this.hasEmptyItem()) {
			iIndex = 1;
		}
		return this.masterVM.getCount() + iIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITEMVM getItem(int p_iParamInt) {
		ITEMVM r_oItem = null;

		int iIndex = p_iParamInt;
		if (this.hasEmptyItem()) {
			iIndex--;
		}
		if (iIndex > -1) {
			r_oItem = this.masterVM.getCacheVMByPosition(iIndex);
		}
		return r_oItem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getItemId(int p_iParamInt) {
		return p_iParamInt;
	}

	/**
	 * Returns the position of a given item in the adapter
	 * @param p_oItem the item to look for
	 * @return the position found
	 */
	public int indexOf(ITEMVM p_oItem) {
		int iNdexOf = 0;
		if (this.hasEmptyItem()) {
			iNdexOf = 1;
		}
		return masterVM.indexOf(p_oItem) + iNdexOf;
	}

	/**
	 * This method can be overridden to wrap the components in the MasterVisualCommponent
	 * @param p_oView the current view
	 * @param p_oCurrentViewModel The view Model of this item
	 * @param p_iPosition the position of the current view in the list
	 */
	protected void wrapCurrentView(View p_oView, ITEMVM p_oCurrentViewModel,
			int p_iPosition) {
		// nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * This method can be overridden to wrap the components in the MasterVisualCommponent of the drop down view
	 * @param p_oView the current view
	 * @param p_oCurrentViewModel The view Model of this item
	 * @param p_iPosition the position of the current view in the list
	 * @param p_isSelected true if the current view is the selected one
	 */
	public void wrapCurrentDropDownView(View p_oView, ITEMVM p_oCurrentViewModel, int p_iPosition, boolean p_isSelected) {
		if (this.selectedComponent != -1) {
			View oComponent = p_oView.findViewById(this.selectedComponent);
			if (oComponent == null) {
				oComponent = p_oView.findViewById(((AndroidApplication) Application.getInstance())
						.getAndroidIdByRKey(AndroidApplicationR.component__simple_spinner_dropdown_emptyitem__value));
			}
			if (oComponent instanceof MMSpinnerCheckedTextView) {
				((MMSpinnerCheckedTextView) oComponent).setChecked(p_isSelected);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int p_iParamInt, View p_oView, ViewGroup p_oViewGroup) {
		View r_oView = this.delegate.getLegacyViewByLevel(Level.VIEW, false, p_oView, p_oViewGroup, p_iParamInt);
		
		if (p_iParamInt == getSelectedPosition()) {
			doOnErrorOnSelectedItem(r_oView, this.errorMessage);
		}
		
		return r_oView;
	}

	/**
	 * @return the components
	 */
	public Set<MasterVisualComponent> getComponents() {
		return components;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int p_iParamInt, View p_oView, ViewGroup p_oViewGroup) {
		return this.delegate.getLegacyViewByLevel(Level.DROPDOWN, false, p_oView, p_oViewGroup, p_iParamInt);
	}

	/**
	 * Sets the selected item position in the spinner
	 * @param p_iPosition the position of the selected item
	 */
	public void setSelectedPosition(int p_iPosition) {
		this.selectedPosition = p_iPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemViewType(int p_oPosition) {
		if (p_oPosition == 0) {
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
		if (android.os.Build.VERSION.SDK_INT >= 21) {
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
		return this.getCount() == 0;
	}

	/**
	 * Called to set an error on the spinner
	 * @param p_oView the view holding the spinner
	 * @param p_sMessage the error message to set
	 * 
	 */
	public void doOnErrorOnSelectedItem(View p_oView, String p_sMessage) {
		if (this.errorComponent != -1) {
			View oComponent = p_oView.findViewById(this.errorComponent);
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
		this.errorMessage = p_sMessage;
	}

	/**
	 * Sets the master viewmodel of the spinner
	 * @param p_oMasterVM the master viewmodel to set
	 */
	public void setMasterVM(LISTVM p_oMasterVM) {
		this.masterVM = p_oMasterVM;
		this.selectedPosition = -1;
		this.notifyDataSetChanged();
	}

	/**
	 * Returns the master viewmodel of the spinner
	 * @return the master viewmodel of the spinner
	 */
	public LISTVM getMasterVM() {
		return this.masterVM;
	}

	/**
	 * Creates and return a ConfigurableListViewHolder.
	 * 
	 * @return An empty ConfigurableListViewHolder object.
	 */
	public ConfigurableListViewHolder createViewHolder(View p_oView) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder", 
				new Class[]{ View.class }, 
				new Object[]{ p_oView });
	}
	
	/**
	 * Creates a DropDownViewHolder
	 * @return an empty dropdown view holder
	 */
	public ConfigurableListViewHolder createDropDownViewHolder(View p_oView) {
		return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder", 
				new Class[]{ View.class }, 
				new Object[]{ p_oView });
	}

	/**
	 * Called to uninflate the MDK component
	 */
	public void uninflate() {
		for (MasterVisualComponent oCompositeComponent : this.components) {
			oCompositeComponent.getDescriptor().unInflate(null, oCompositeComponent, null);
		}
		this.resetComponentTags();
		this.components.clear();
	}

	/**
	 * Called to reset the view model tags on the MDK component
	 */
	protected void resetComponentTags() {
		View oView = null;
		
		for (MasterVisualComponent oComponent : this.components) {
			
			if (oComponent instanceof ComponentWrapper) {
				oView = ((ComponentWrapper) oComponent).getComponent();
			} else {
				oView = (View) oComponent;
			}
			
			((ConfigurableListViewHolder) oView.getTag()).setViewModelID(null);
		}
	}

	/**
	 * Returns the master view model interface
	 * @return the master view model interface
	 */
	public Class<ITEMVM> getItemVMInterface() {
		return this.masterVM.getItemVmInterface();
	}

	/**
	 * Returns the item layout for the given position
	 * @param p_iPosition the position to use
	 * @return the item layout 
	 */
	protected int getItemLayout(int p_iPosition) {
		return this.spinnerItemLayoutId;
	}

	/**
	 * Returns the selected position
	 * @return the selected position
	 */
	public int getSelectedPosition() {
		return this.selectedPosition;
	}
	
	/**
	 * Sets the error message on the spinner
	 * @param p_sError the error message to set
	 */
	public void setErrorMessage(String p_sError) {
		this.errorMessage = p_sError;
	}
}
