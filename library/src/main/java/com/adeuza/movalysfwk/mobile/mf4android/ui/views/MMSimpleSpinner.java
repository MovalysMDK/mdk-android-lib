package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;


/**
 * <p>Simple Spinner widget to use in custom code with string arrays</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 */
public class MMSimpleSpinner extends Spinner implements ConfigurableVisualComponent<String>, MMIdentifiableView, InstanceStatable, OnTouchListener {
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;
	/** Doit on ajouter une valeur vide */
	private boolean useEmptyValue = false ;
	/** Es t on en cours d'écriture */
	private boolean writingData;
	/** Key values couples displayed */
	private Map<String,String> keyValues ;
	/**
	 * Constructs a new MMSimpleSpinner
	 * @param p_oContext
	 *            the context to use
	 * @see Spinner#Spinner(Context)
	 */
	public MMSimpleSpinner(Context p_oContext) {
		this(p_oContext,null);
		if (!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
			this.setOnTouchListener(this);
		}
	}
	/**
	 * Constructs a new MMSimpleSpinner
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see Spinner#Spinner(Context, AttributeSet)
	 */
	public MMSimpleSpinner(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs );
		if(!isInEditMode()) {
			this.setOnTouchListener(this);
			this.init(p_oAttrs);
		}
	}
	/**
	 * Constructs a new MMSimpleSpinner
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @param p_oDefStyle
	 *            the style to use
	 * @see Spinner#Spinner(Context, AttributeSet, int)
	 */
	public MMSimpleSpinner(Context p_oContext, AttributeSet p_oAttrs,int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.setOnTouchListener(this);
			this.init(p_oAttrs);
		}
	}
	/**
	 * Initie le delegate et le paramètre use-empty-item
	 * @param p_oAttrs
	 */
	private void init(AttributeSet p_oAttrs){
		this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
		if ( p_oAttrs != null){
			this.useEmptyValue = p_oAttrs.getAttributeBooleanValue(	Application.MOVALYSXMLNS, "use-empty-item", true);
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
	public void configurationSetValue(String p_oObject) {
		this.writingData = true;
		int iPositionSelected = 0; // 0 car si pas de valeur, le premier élément doit être sélectionné.
		if (p_oObject != null) {
			for (int i = 0; i < this.getAdapter().getCount(); i++) {
				if (p_oObject.equals(this.getAdapter().getItem(i))) {
					iPositionSelected = i;
					break;
				}
			}
		}
		//appel du super car on set la valeur initiale, ne pas tagger le champ comme changé
		super.setSelection(iPositionSelected);
		this.requestFocus();
		this.writingData = false;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetValues(VALUE[])
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet!= null && p_oObjectsToSet.length > 0 ){
			this.configurationSetValue( keyValues.get( p_oObjectsToSet[0]) );
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		return (String) this.getSelectedItem() ;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		String sSelectedValue = this.configurationGetValue() ;
		for (Entry<String,String> oLine : keyValues.entrySet() ){
			if (oLine.getValue().equals(sSelectedValue)){
				return  new String[]{ oLine.getKey() };
			}
		}
		return new String[]{};
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
	public void setSelection(int p_oPosition) {
		super.setSelection(p_oPosition);
		if (!this.writingData && this.aivDelegate != null) {
			this.aivDelegate.changed();
		}
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
	public boolean isNullOrEmptyValue(String p_oObject) {
		return p_oObject == null || p_oObject.length() == 0 ;
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
	public void configurationPrepareHide(
			List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
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
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		// Chaine d'une longueur à Zéro est considérer comme vide - Cas ajout d'un élément vide pour champ obligatoire.
		return this.getSelectedItem() != null && ((String) this.getSelectedItem()).length() > 0;
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
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		// Nothing to do
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
		//Nothing to do
	}
	@Override
	public void configurationUnsetError() {
		View oComponent = this.findViewById(((AndroidApplication)Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.component_customfields__edittext__value));
		if (oComponent instanceof TextView) {
			ArrayList<View> oList = new ArrayList<>();
			oList.add(oComponent);
			this.addFocusables(oList, View.FOCUSABLES_ALL);
			((TextView) oComponent).setError(null);
		}
	}
	@Override
	public void configurationSetError(String p_oError) {
		View oComponent = this.findViewById(((AndroidApplication)Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.component_customfields__edittext__value));
		if (oComponent instanceof TextView) {
			ArrayList<View> oList = new ArrayList<>();
			oList.add(oComponent);
			this.addFocusables(oList, View.FOCUSABLES_ALL);
			((TextView) oComponent).setError(p_oError);
		}
	}
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * @see android.widget.AbsSpinner#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}
	/**
	 * {@inheritDoc}
	 * @see android.widget.AbsSpinner#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivDelegate.onRestoreInstanceState(p_oState);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
	}
	/**
	 * Modifieur des couples clé valeurs affichés dans le spinner
	 * et définit l'adapter lié à cet ensemble de valeurs
	 * @param p_mapKeyValues  map de clé valeurs
	 */
	public void setKeyValues(Map<String, String> p_mapKeyValues) {
		this.keyValues = p_mapKeyValues;

		Collection<String> oCollec = p_mapKeyValues.values();		
		List<String> oListValues = new ArrayList<>();
		oListValues.addAll(oCollec);				
		ArrayAdapter<String> oListAdapter = new ArrayAdapter<>( this.getContext(),
				((AndroidApplication)Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.fwk_component__simplespinner_text),
				oListValues); 
		oListAdapter.setDropDownViewResource(((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.fwk_component__simplespinner_spinitem));
		this.setAdapter(oListAdapter);
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