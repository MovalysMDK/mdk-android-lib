package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentSavedState;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractNamableObject;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>RadioGroup widget used in the Movalys Mobile product for Android</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 */
public class MMRadioGroup extends RadioGroup implements ConfigurableVisualComponent<Enum<?>>, MMIdentifiableView, OnCheckedChangeListener, InstanceStatable {

	private static final String FWK_NONE = "FWK_NONE";
	private static final int INIT_VALUE = -1 ;

	/** local storage of a FWK_NONE if initialized with no radioButton selected*/
	private Enum<?> noneValue=null;

	private int errorComponentId;

	/** Composant dédié à l'affichage d'une erreur. */
	private TextView errorComponent;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Enum<?>> aivDelegate = null;
	/** Tous les radios par valeur de clé */
	private HashMap<String, MMRadioButton> radioByKey ;
	/** Writing data */
	private boolean writingData = false ;

	private OnCheckedChangeListener onCheckedChangeListener;
	
	/**
	 * Constructs a new MMRadioGroup
	 * @param p_oContext
	 *            the context to use
	 * @see RadioGroup#RadioGroup(Context, AttributeSet)
	 */
	public MMRadioGroup(Context p_oContext) {
		this(p_oContext,null);
	}	
	/**
	 * Constructs a new MMRadioGroup
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see RadioGroup#RadioGroup(Context, AttributeSet)
	 */
	public MMRadioGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.radioByKey = new HashMap<>();
			super.setOnCheckedChangeListener(this);
			if( p_oAttrs != null ){
				this.errorComponentId =  p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "error-component", 0);
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		if(!isInEditMode()) {
			super.setOnCheckedChangeListener(this);
		}
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
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.clearCheck(); // nécessaire pour changer le check
		if (p_oObjectsToSet != null) {
			MMRadioButton oRadioButton = null;
			for (String sKeyValue : p_oObjectsToSet) {
				oRadioButton = this.radioByKey.get(sKeyValue);
				if (oRadioButton != null) {
					boolean r_bWasChanged = oRadioButton.isChanged();
					oRadioButton.setChecked(true);
					if ( !r_bWasChanged ) {
						oRadioButton.resetChanged();
					}
					break;
				}
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		Collection<String> oKeyValues = new ArrayList<>();
		for (Entry<String, MMRadioButton> oEntry : this.radioByKey.entrySet()) {
			if (oEntry.getValue().isChecked()) {
				oKeyValues.add(String.valueOf(oEntry.getKey()));
			}
		}
		return oKeyValues.toArray(new String[oKeyValues.size()]);
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
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
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
		return Integer.class; 
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
		MMRadioButton oCurrentRadioButton;
		for (int iNdexRadio=0;iNdexRadio<this.getChildCount();iNdexRadio++){
			try{
				oCurrentRadioButton=(MMRadioButton)this.getChildAt(iNdexRadio);
				oCurrentRadioButton.configurationDisabledComponent();
			}
			catch (ClassCastException oE) {
				Application.getInstance().getLog().error("MMRadioGroup", StringUtils.concat("The radiogroup ",this.getName(),"have forbiden childs (Not MMRadioButton) at position ",String.valueOf(iNdexRadio)));
			}
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
		MMRadioButton oCurrentRadioButton;
		for (int iNdexRadio=0;iNdexRadio<this.getChildCount();iNdexRadio++){
			try{
				oCurrentRadioButton=(MMRadioButton)this.getChildAt(iNdexRadio);
				oCurrentRadioButton.configurationEnabledComponent();
			}
			catch (ClassCastException oE) {
				Application.getInstance().getLog().error("MMRadioGroup", StringUtils.concat("The radiogroup ",this.getName(),"have forbiden childs (Not MMRadioButton) at position ",String.valueOf(iNdexRadio)));
			}
		}
	}	

	/**
	 * p_oObjectToSet id android
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(Enum<?> p_oObjectToSet) {
		this.writingData = true ;

		// le clear check provoque un onCheckChanged()
		this.clearCheck();
		//affectation des values aux radioBouttons fils
		Enum<?>[] oEnums;
		if (p_oObjectToSet!=null){
			if ( FWK_NONE.equals(p_oObjectToSet.name())){
				this.noneValue=p_oObjectToSet;
			}
			oEnums=p_oObjectToSet.getClass().getEnumConstants();

			MMRadioButton oCurrentRadio = null;
			for(int iCompteurEnum = 0;iCompteurEnum<oEnums.length; iCompteurEnum++) {
				for(int iCompteurRadio = 0; iCompteurRadio<this.getChildCount(); iCompteurRadio++) {
					oCurrentRadio=(MMRadioButton)this.getChildAt(iCompteurRadio);
					if ((oCurrentRadio).getName().endsWith("_"+oEnums[iCompteurEnum].name())){
						oCurrentRadio.configurationSetValue(oEnums[iCompteurEnum]);
						if (p_oObjectToSet.equals(oCurrentRadio.configurationGetValue())){
							oCurrentRadio.setChecked(true);
						}						
					}
				}
			}
		}

		this.writingData = false ;
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(Enum<?> p_oObject) {
		return this.getCheckedRadioButtonId()== INIT_VALUE;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public Enum<?> configurationGetValue() {
		MMRadioButton oCheckedRadioButton = (MMRadioButton) this.findViewById(this.getCheckedRadioButtonId());
		if (oCheckedRadioButton!=null){
			return oCheckedRadioButton.configurationGetValue();
		} else {
			//aucun radio n'est coché, cela n'est possible que si init à FWK_NONE
			return this.noneValue;
		}
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup p_oRadioGroup, int p_ibCheckedId) {
		if ( !this.writingData && this.aivDelegate != null ) {
			this.aivDelegate.changed();
			if (this.onCheckedChangeListener != null) {
				this.onCheckedChangeListener.onCheckedChanged(p_oRadioGroup, p_ibCheckedId);
			}
		}
		this.configurationUnsetError();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.getCheckedRadioButtonId() > 0;
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
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationUnsetError()
	 */
	@Override
	public void configurationUnsetError() {
		if (this.errorComponent == null) {
			this.aivDelegate.configurationUnsetError();
		}
		else if(this.errorComponent.getError() != null){
			this.errorComponent.setError(null);
		}
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetError(java.lang.String)
	 */
	@Override
	public void configurationSetError(String p_sError) {
		if (this.errorComponent == null && this.errorComponentId > 0) {
			this.errorComponent = (TextView) ((View) this.getParent()).findViewById(this.errorComponentId);
		}

		if (this.errorComponent == null) {
			this.aivDelegate.configurationSetError(p_sError);
		}
		else {
			this.errorComponent.setError(p_sError);
		}
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#validate(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration, java.util.Map, java.lang.StringBuilder)
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
	}	
	/**
	 * Adds a new radio button  to this group. A radio button is defined by its label and its value.
	 * @param p_oKeyValue Value of the radio button to add. Possibly a null or empty value.
	 * @param p_sLabel Label of the radio button to add. Possibly a empty string.
	 */
	public void addRadioButton(String p_oKeyValue, String p_sLabel) {
		MMRadioButton oRadioButton = new MMRadioButton(this.getContext());
		((AndroidApplication) Application.getInstance()).computeId(oRadioButton, this.getName() + AbstractNamableObject.KEY_SEPARATOR + this.getChildCount());
		oRadioButton.setText(p_sLabel);
		this.addView(oRadioButton);
		this.radioByKey.put(p_oKeyValue, oRadioButton);
	}
	/**
	 * @param p_bWritingData
	 */
	public void setWritingData(boolean p_bWritingData ) {
		this.writingData = p_bWritingData ;
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#validate(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration, java.util.Map, java.lang.StringBuilder)
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState=superOnSaveInstanceState();
		MMRadioGroupSavedState oState=new MMRadioGroupSavedState(superState);
		oState.setChanged(this.aivDelegate.isChanged());
		oState.setSelectedRadioButtonId(this.getCheckedRadioButtonId());
		oState.setSelectedRadioButtonValue(this.configurationGetValue());

		return oState;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		try{
			if(! (p_oState instanceof MMRadioGroupSavedState)){
				superOnRestoreInstanceState(p_oState);
				return;
			}
	
			this.writingData = true;
			
			MMRadioGroupSavedState savedState = (MMRadioGroupSavedState) p_oState;
			// Etat sauvegardé par la classe mère
			superOnRestoreInstanceState(savedState.getSuperState());
			
			//on sélectionne le bouton qui a été sauvegardé
			this.check(savedState.getSelectedRadioButtonId());
			
			//on repose l'état
			if (savedState.isChanged()){
				MMRadioButton oSelectedRadioButton = (MMRadioButton) this.findViewById(savedState.getSelectedRadioButtonId());
				oSelectedRadioButton.configurationSetValue(savedState.getSelectedRadioButtonValue());
				this.aivDelegate.changed();
			}
			else {
				this.aivDelegate.resetChanged();
			}	

	
			this.writingData = false;
			
		}
		catch (Exception e){
			Log.e("MMRadioGroup","Exception dans le onRestore",e);
		}
	
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

	


	///debut DMA

	protected static class MMRadioGroupSavedState extends  AndroidConfigurableVisualComponentSavedState{
		private int selectedRadioButtonId;
		private Enum<?> selectedRadioButtonValue;

		public MMRadioGroupSavedState(Parcelable p_oSuperState) {
			super(p_oSuperState);
		}

		public MMRadioGroupSavedState(Parcel p_oInParcel) {
			super(p_oInParcel);
			this.selectedRadioButtonId = p_oInParcel.readInt();
			this.selectedRadioButtonValue = (Enum<?>) p_oInParcel.readSerializable();
		}



		public Enum<?> getSelectedRadioButtonValue() {
			return selectedRadioButtonValue;
		}

		public void setSelectedRadioButtonValue(Enum<?> p_oSelectedRadioButtonValue) {
			this.selectedRadioButtonValue = p_oSelectedRadioButtonValue;
		}

		/**
		 * @return the selectedRadioButtonId
		 */
		public int getSelectedRadioButtonId() {
			return selectedRadioButtonId;
		}

		/**
		 * @param p_iSelectedRadioButtonId the selectedRadioButtonId to set
		 */
		public void setSelectedRadioButtonId(int p_iSelectedRadioButtonId) {
			this.selectedRadioButtonId = p_iSelectedRadioButtonId;
		}

		@Override
		public void writeToParcel(Parcel p_oOutParcel, int p_iFlags) {
			super.writeToParcel(p_oOutParcel, p_iFlags);
			p_oOutParcel.writeInt(selectedRadioButtonId);
			p_oOutParcel.writeSerializable(selectedRadioButtonValue);
		} 

		public static final Parcelable.Creator<MMRadioGroupSavedState> CREATOR
		= new Parcelable.Creator<MMRadioGroupSavedState>() {
			@Override
			public MMRadioGroupSavedState createFromParcel(Parcel p_oInParcel) {
				return new MMRadioGroupSavedState(p_oInParcel);
			}

			@Override
			public MMRadioGroupSavedState[] newArray(int p_iSize) {
				return new MMRadioGroupSavedState[p_iSize];
			}
		};
	}
	
	@Override
	public void setOnCheckedChangeListener(OnCheckedChangeListener p_oListener) {
		this.onCheckedChangeListener = p_oListener;
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

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
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
