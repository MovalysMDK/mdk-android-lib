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

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKSpinnerAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.spinner.MDKSpinnerConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableSpinnerComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentMandatoryLabel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseSpinner;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelListener;

/**
 * <p>
 * Spinner widget used in the Movalys Mobile product for Android
 * </p>
 * 
 * @param <ITEM> a model for item
 * @param <ITEMVM> a view model for item
 * 
 */
@BaseComponent(baseName="MMBaseSpinner", baseClass="android.widget.Spinner", appCompatClass="android.support.v7.widget.AppCompatSpinner")
public class MMSpinner<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>>
extends MMBaseSpinner implements ConfigurableVisualComponent, ListViewModelListener, 
MMSpinnerAdapterHolder<ITEM, ITEMVM>, InstanceStatable, OnTouchListener, ComponentBindDestroy, ComponentMandatoryLabel, 
ComponentReadableWrapper<ITEMVM>, ComponentWritableWrapper<ITEMVM> {
	
	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableSpinnerComponentDelegate<ItemViewModel<?>> aivDelegate = null;
	
	/** component is using empty value */
	private boolean useEmptyValue;
	
	/** field in popup */
	private static Field popupField;
	
	/** is data initialize */
	private boolean initializeData = false;
	
	/** the error set on the component */
	private String error = null;

	/**
	 * Constructs a new MMSpinner
	 * 
	 * @param p_oContext  the context to use
	 * @see Spinner#Spinner(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMSpinner(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableSpinnerComponentDelegate<ItemViewModel<?>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{ItemViewModel.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,  
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
			this.useEmptyValue = true;
			this.setOnTouchListener(this);
		}
	}

	/**
	 * Constructs a new MMSpinner
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see Spinner#Spinner(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMSpinner(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableSpinnerComponentDelegate<ItemViewModel<?>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{ItemViewModel.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			this.useEmptyValue = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "use-empty-item", true);

			int iPromptId = p_oAttrs.getAttributeResourceValue(AndroidApplication.ANDROID_XML_NAMESPACE, "prompt", 0);
			if (iPromptId != 0) {
				this.setPrompt(((AndroidApplication) Application.getInstance()).getStringResource(iPromptId));
			}
			this.setOnTouchListener(this);
		}
	}

	/**
	 * Constructs a new MMSpinner
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see Spinner#Spinner(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMSpinner(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableSpinnerComponentDelegate<ItemViewModel<?>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{ItemViewModel.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			this.useEmptyValue = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "use-empty-item", true);
			this.setOnTouchListener(this);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.AbsSpinner#getCount()
	 */
	@Override
	public int getCount() {
		return this.getAdapter().getCount();
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
	 * 
	 * @see android.widget.AbsSpinner#setSelection(int)
	 */
	@Override
	public void setSelection(int p_iPosition) {
		super.setSelection(p_iPosition, true);
		if ( aivDelegate != null && (!this.aivDelegate.isWritingData() || this.initializeData) ) {
			this.aivFwkDelegate.changed();
		}
		// Si la valeur du spinner change, on supprime l'erreur
		if (this.getSelectedItemPosition() != p_iPosition) {
			this.aivDelegate.configurationUnsetError();
		}
	}

	@Override
	public void doOnListVMChanged() {
		// this.update();
	}

	/**
	 * Returns <code>true</code> if an empty value must be displayed into the comboBox.
	 * @return <code>true</code> if an empty value must be displayed into the comboBox.
	 */
	public boolean hasEmptyValue() {
		return this.useEmptyValue;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder#setAdapter(com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableSpinnerAdapter)
	 */
	@Override
	public void setAdapter(MDKSpinnerConnector p_oAdapter){
		p_oAdapter.enableEmptyItem(hasEmptyValue());
		super.setAdapter(p_oAdapter);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder#getAdapter()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MDKSpinnerConnector<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> getAdapter() {
		return (MDKSpinnerConnector<ITEM, ITEMVM, ListViewModel<ITEM, ITEMVM>>) super.getAdapter();
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		
		String sClass = r_oBundle.getString("class");
		if (!MMSpinner.class.getName().equals(sClass)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}
		
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		
		// we need this information to be able to restore the error displayed by the adapter on rotation
		this.error = r_oBundle.getString("error");
		this.getAdapter().setErrorMessage(this.error);
		this.getAdapter().setSelectedPosition(r_oBundle.getInt("selectedItem"));
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		try {
			if (MMSpinner.popupField == null) {
				MMSpinner.popupField = this.getClass().getSuperclass().getDeclaredField("mPopup");
			}
			//fermeture de la fenêtre avant rotation sur les terminaux 2.2 (et autres versions ...)
			if (MMSpinner.popupField != null) {
				boolean bOldAccess = MMSpinner.popupField.isAccessible();
				MMSpinner.popupField.setAccessible(true);
				Object oPopup=MMSpinner.popupField.get(this);
				if (oPopup!=null && AlertDialog.class.isAssignableFrom(oPopup.getClass())){
					AlertDialog oNativeDialog = (AlertDialog) oPopup;
					oNativeDialog.dismiss();
				}
				MMSpinner.popupField.setAccessible(bOldAccess);
			}
		}catch (NoSuchFieldException e) {
			Log.d("MMSpinner", "NoSuchFieldException");
		}catch (IllegalAccessException e) {
			Log.d("MMSpinner", "IllegalAccessException");
		}
		
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("class", MMSpinner.class.getName());
		
		// the adapter is created again on screen rotation, so we would lose the information about selected item and current error if we did not save them
		r_oBundle.putInt("selectedItem", this.getSelectedItemPosition());
		r_oBundle.putString("error", error);
		
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.AbsSpinner#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.AbsSpinner#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);
	}

	@Override
	public boolean onTouch(View p_oV, MotionEvent p_oEvent) {
		Context oApplicationContext = ((AndroidApplication)AndroidApplication.getInstance()).getApplicationContext();
        InputMethodManager imm=(InputMethodManager)oApplicationContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
		return false;
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public AndroidConfigurableSpinnerComponentDelegate<ItemViewModel<?>> getComponentDelegate() {
		return this.aivDelegate;
	}

	/**
	 * Is data initialized
	 * @return the initializeData
	 */
	public boolean isInitializeData() {
		return initializeData;
	}

	/**
	 * Set whether data is initialized
	 * @param p_bInitializeData the initializeData to set
	 */
	public void setInitializeData(boolean p_bInitializeData) {
		this.initializeData = p_bInitializeData;
	}

	/**
	 * Get the error
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Set the error on component
	 * @param p_sError the error to set
	 */
	public void setError(String p_sError) {
		this.error = p_sError;
	}
	
	/************************************************************************************************
	 ******************************** Framework delegate callback ***********************************
	 ************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#doOnPostAutoBind()
	 */
	@Override
	public void doOnPostAutoBind() {
		// nothing on bind
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#destroy()
	 */
	@Override
	public void destroy() {
		MDKViewConnector oConnector = (MDKViewConnector) this.getAdapter();
		MDKSpinnerAdapter<?,?,?> oAdapter = (MDKSpinnerAdapter<?,?,?>) oConnector.getAdapter();
		if (oAdapter != null) {
			oAdapter.uninflate();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ITEMVM configurationGetValue() {
		MDKViewConnector oConnector = (MDKViewConnector) this.getAdapter();
		MDKSpinnerAdapter<?,?,?> oAdapter = (MDKSpinnerAdapter<?,?,?>) oConnector.getAdapter();
		
		ITEMVM r_oITEMVM = null;
		if (!this.hasEmptyValue() && this.isInitializeData()
				&& oAdapter.getItem(0) != null) {
				r_oITEMVM = (ITEMVM) oAdapter.getItem(0);
		} else {
			r_oITEMVM = (ITEMVM) this.getSelectedItem();
		}
		return r_oITEMVM;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(ITEMVM p_oObjectToSet) {
		MDKSpinnerConnector<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> oConnector = (MDKSpinnerConnector<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>>) this.getAdapter();
		if (p_oObjectToSet != null && !p_oObjectToSet.equals(this.configurationGetValue())
				|| p_oObjectToSet == null && this.configurationGetValue() != null) {

			// Si l'objet est null, il faut se calquer sur le comportement
			// par défaut android
			// et sélectionner le premier élément de la liste déroulante.
			int iPositionSelected = 0;
			if (!this.hasEmptyValue()) {
				this.setInitializeData(true);
			}

			if (p_oObjectToSet != null) {
				this.setInitializeData(false);
				iPositionSelected = oConnector.indexOf(p_oObjectToSet);
				if (iPositionSelected == -1) {
					// L'élément fourni ne correspond à aucune valeur de la
					// liste déroulante,
					// il faut que le spinner reflète cette absence de
					// valeur, ce qui correspond sous android
					// à sélectionner le premier élément de la liste.
					iPositionSelected = 0;
				}
			}
			this.setSelection(iPositionSelected);

			if (this.isInitializeData()) {
				this.getComponentFwkDelegate().resetChanged();
			}
			this.setInitializeData(false);
		} else if (p_oObjectToSet == null && this.getSelectedItemPosition() != 0) {
			// reset on null value
			this.setSelection(0);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		boolean r_bIsFilled = false;
		if (!this.hasEmptyValue() && this.isInitializeData()) {
			if (this.getAdapter() != null && this.getAdapter().getItem(0) != null) {
				r_bIsFilled = true;
			}
		} else {
			r_bIsFilled = this.getSelectedItem() != null;
		}
		return r_bIsFilled;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentMandatoryLabel#setMandatoryLabel(boolean)
	 */
	@Override
	public void setMandatoryLabel(boolean p_bMandatory) {
		String sOriginalLabel = this.getPrompt().toString();
		String sComputedMandatoryLabel = this.aivDelegate.computeMandatoryLabel(sOriginalLabel, p_bMandatory);
		this.setPrompt(sComputedMandatoryLabel);
	}
}
