package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableSpinnerAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelListener;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>
 * Spinner widget used in the Movalys Mobile product for Android
 * </p>
 * 
 * <p>
 * Copyright (c) 2010
 * <p>
 * Company: Adeuza
 * 
 * @author dmaurange
 * 
 * @param <ITEMVM>
 *            a model for item
 * 
 */


public class MMSpinner<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>>
extends Spinner implements ConfigurableVisualComponent<ITEMVM>, MMIdentifiableView, ListViewModelListener, 
MMSpinnerAdapterHolder<ITEM, ITEMVM>, InstanceStatable, OnTouchListener {
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<ITEMVM> aivDelegate = null;

	private boolean useEmptyValue;

	private static Field popupField;

	private boolean writingData;
	
	private boolean initializeData = false;
	
	private String error = null;

	/**
	 * Constructs a new MMSpinner
	 * 
	 * @param p_oContext  the context to use
	 * @see Spinner#Spinner(Context)
	 */
	public MMSpinner(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
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
	public MMSpinner(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
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
	public MMSpinner(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
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
		this.aivDelegate.setId(p_oId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGroup() {
		return this.aivDelegate.isGroup();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationHide(p_bLockModifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationUnHide(p_bLockModifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(ITEMVM p_oObject) {
		if (p_oObject != null && !p_oObject.equals(this.configurationGetValue()) 
			|| p_oObject == null && this.configurationGetValue() != null) {
			this.aivDelegate.configurationSetValue(p_oObject);
	
			// Si l'objet est null, il faut se calquer sur le comportement par défaut android
			// et sélectionner le premier élément de la liste déroulante.
			int iPositionSelected = 0;
			if (!hasEmptyValue()) this.initializeData = true;
	
			if (p_oObject != null) {
				this.initializeData = false;
				iPositionSelected = this.getAdapter().indexOf(p_oObject);
				if (iPositionSelected == -1) {
					// L'élément fourni ne correspond à aucune valeur de la liste déroulante,
					// il faut que le spinner reflète cette absence de valeur, ce qui correspond sous android
					// à sélectionner le premier élément de la liste.
					iPositionSelected = 0;
				}
			}
	
			this.writingData = true;
			this.setSelection(iPositionSelected);
			this.writingData = false;
	
			if (this.initializeData) {
				this.resetChanged();
			}
			this.initializeData = false;
		}
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetValues(VALUE[])
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.aivDelegate.configurationSetCustomValues(p_oObjectsToSet);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITEMVM configurationGetValue() {
		ITEMVM r_oITEMVM = null;

		if (!hasEmptyValue() && this.initializeData) {
			if (this.getAdapter() != null && this.getAdapter().getItem(0) != null)
				r_oITEMVM = this.getAdapter().getItem(0);
		}else {
			r_oITEMVM = (ITEMVM) this.getSelectedItem();
		}
		return r_oITEMVM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return this.aivDelegate.configurationGetCustomValues();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLabel() {
		return this.aivDelegate.isLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModel() {
		return this.aivDelegate.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.aivDelegate.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.AbsSpinner#setSelection(int)
	 */
	@Override
	public void setSelection(int p_iPosition) {
		super.setSelection(p_iPosition, true);
		if ((!this.writingData || this.initializeData) && aivDelegate != null) {
			this.aivDelegate.changed();
		}
		// Si la valeur du spinner change, on supprime l'erreur
		if (this.getSelectedItemPosition() != p_iPosition) {
			this.configurationUnsetError();
		}
		this.requestFocus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(ITEMVM p_oObject) {
		return p_oObject == null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		return this.aivDelegate.isNullOrEmptyCustomValues(p_oObjects);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#hide()
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#unHide()
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return NoneType.class;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
		this.setFocusable(true);
		this.setFocusableInTouchMode(false);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
		this.setFocusable(true);
		this.setFocusableInTouchMode(false);
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		boolean r_bIsFilled = false;
		
		if (!hasEmptyValue() && this.initializeData) {
			if (this.getAdapter() != null && this.getAdapter().getItem(0) != null)
				r_bIsFilled = true;
		} else {
			r_bIsFilled = this.getSelectedItem() != null;			
		}
		
		return r_bIsFilled;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.isShown();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		if (!this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationSetMandatoryLabel();

			StringBuilder oPrompt = new StringBuilder();
			CharSequence sPrompt = this.getPrompt();
			if (sPrompt != null) {
				oPrompt.append(sPrompt);
			}
			if ( !oPrompt.toString().endsWith("(*)") ) {
				oPrompt.append("(*)");
			}

			this.setPrompt(oPrompt.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uimodel.ListViewModelListener#doOnListVMChanged()
	 */
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
	 */
	@Override
	public void doOnPostAutoBind() {
		//Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		if (this.getAdapter() != null) {
			this.getAdapter().uninflate();
		}
	}

	@Override
	public void configurationUnsetError() {
		((AbstractConfigurableSpinnerAdapter<?, ?, ?>) this.getAdapter()).doOnNoErrorOnSelectedItem(this);
		error = null;
	}

	@Override
	public void configurationSetError(String p_oError) {
		((AbstractConfigurableSpinnerAdapter<?, ?, ?>) this.getAdapter()).doOnErrorOnSelectedItem(this, p_oError);
		error = p_oError;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder#setAdapter(com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableSpinnerAdapter)
	 */
	@Override
	public void setAdapter(AbstractConfigurableSpinnerAdapter p_oAdapter){
		p_oAdapter.enableEmptyItem(hasEmptyValue());
		super.setAdapter(p_oAdapter);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder#getAdapter()
	 */
	@Override
	public AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, ListViewModel<ITEM, ITEMVM>> getAdapter() {
		return (AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, ListViewModel<ITEM, ITEMVM>>) super.getAdapter();
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
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
	 * 
	 * @see android.widget.AbsSpinner#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
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
					if (oNativeDialog != null) {
						oNativeDialog.dismiss();
					}
				}
				MMSpinner.popupField.setAccessible(bOldAccess);
			}
		}catch (NoSuchFieldException e) {
			Log.d("MMSpinner", "NoSuchFieldException");
		}catch (IllegalAccessException e) {
			Log.d("MMSpinner", "IllegalAccessException");
		}
		
		Bundle oToSave = new Bundle();
		oToSave.putString("class", MMSpinner.class.getName());
		oToSave.putInt("selectedItem", this.getSelectedItemPosition());
		oToSave.putParcelable("delegate", this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState()));
		oToSave.putString("error", error);
		return oToSave;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.AbsSpinner#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {

		if (!(p_oState instanceof Bundle)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}
		Bundle savedState = (Bundle) p_oState;
		String sClass = savedState.getString("class");
		if (!MMSpinner.class.getName().equals(sClass)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}

		this.setSelection(savedState.getInt("selectedItem"));
		this.error = savedState.getString("error");

		this.aivDelegate.onRestoreInstanceState(savedState.getParcelable("delegate"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void registerVM(ViewModel p_oViewModel) {
		this.aivDelegate.registerVM(p_oViewModel);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#unregisterVM()
	 */
	@Override
	public void unregisterVM() {
		this.aivDelegate.unregisterVM();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}

	@Override
	public boolean onTouch(View p_oV, MotionEvent p_oEvent) {
		Context oApplicationContext = ((AndroidApplication)AndroidApplication.getInstance()).getApplicationContext();
        InputMethodManager imm=(InputMethodManager)oApplicationContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFragmentTag() {
		return this.aivDelegate.getFragmentTag();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		this.aivDelegate.setFragmentTag(p_sFragmentTag);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasRules() {
		return this.aivDelegate.hasRules();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHasRules(boolean p_bHasRules) {
		this.aivDelegate.setHasRules(p_bHasRules);
	}
}