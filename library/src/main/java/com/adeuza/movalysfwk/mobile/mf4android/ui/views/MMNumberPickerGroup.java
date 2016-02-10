package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IntegerFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been made public so its included in
 * the project in this fashion for use with the preferences screen; I have made a few slight modifications to the code here, I simply put a
 * MAX and MIN default in the code but these values can still be set publically by calling code.
 */
public class MMNumberPickerGroup extends AbstractMMLinearLayout<Integer> implements MMIdentifiableView {

	/** the label above the number picker */
	private MMTextView oUiLabel;
	/** Bouton permettant d'incrémenter le nombre */
	private MMNumberPicker oUiNumberPicker;

	private static final IntegerFormFieldValidator VALIDATOR = BeanLoader.getInstance().getBean( IntegerFormFieldValidator.class ) ;

	/**
	 * Constructeur sans layout
	 * @param p_oContext contexte de création
	 */
	public MMNumberPickerGroup(Context p_oContext) {
		this(p_oContext, null);
	}
	/**
	 * Constructeur avec layout sans style
	 * @param p_oContext contexte de création
	 * @param p_oAttrs layout XML
	 */
	public MMNumberPickerGroup(Context p_oContext, AttributeSet p_oAttrs) {
		this(p_oContext, p_oAttrs, 0);
	}
	/**
	 * Constructeur complet
	 * @param p_oContext contexte de création
	 * @param p_oAttrs layout XML
	 * @param p_iDefStyle style par défaut
	 */
	public MMNumberPickerGroup(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this) ;

			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oInflater = LayoutInflater.from(this.getContext());
			oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_number_picker), this);
			oUiLabel = (MMTextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__number__label));
			oUiNumberPicker = (MMNumberPicker) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__int__picker));

			this.defineParameters(p_oAttrs);
		}
	}	
	/**
	 * Retrieve parameters from layout to put in the local map for validation
	 * @param p_oAttrs XML attribute set
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivDelegate.defineParameters(p_oAttrs);
	}
	/**
	 * called wen the inflator finished the job before setting value
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			VALIDATOR.addParametersToConfiguration( this.aivDelegate.getAttributes() , (BasicComponentConfiguration)this.getConfiguration() );

			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.oUiLabel.setText(oConfiguration.getLabel());
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */ 
	@Override
	public void configurationSetValue(Integer p_oObjectToSet) {	
		super.configurationSetValue(p_oObjectToSet);
		if( p_oObjectToSet != null) {
			this.oUiNumberPicker.configurationSetValue(p_oObjectToSet);
		} else{
			this.oUiNumberPicker.configurationSetValue(null);
		}
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0) {
			this.configurationSetValue(null);
		} else {
			Integer sFirstValue ;
			try {
				sFirstValue = Integer.parseInt(p_oObjectsToSet[0]);
			} catch (NumberFormatException e) {
				sFirstValue = null ; //par défaut si erreur
			}
			if (sFirstValue == null) {
				this.configurationSetValue(null);
			}else {
				this.configurationSetValue(sFirstValue);
			}
		}
		this.setCustomLabel();

		Integer oMinValue = VALIDATOR.getMinValue((BasicComponentConfiguration)this.getConfiguration());
		if (oMinValue != null) {
			this.oUiNumberPicker.setMinimalValue(oMinValue.intValue());
		}
		Integer oMaxValue = VALIDATOR.getMaxValue((BasicComponentConfiguration)this.getConfiguration());
		if (oMaxValue != null) {
			this.oUiNumberPicker.setMaximalValue(oMaxValue.intValue());
		}
	}	
	/**
	 * Définit l'étiquette du champ perso
	 */
	private void setCustomLabel(){
		String sNewCustomLabel = null  ;
		if ( this.aivDelegate.getAttributes().containsKey(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE) ) {
			sNewCustomLabel = this.aivDelegate.getAttributes().get(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE) ;
		}
		if ( sNewCustomLabel == null && this.getConfiguration() != null ) {
			Object oLabelFromGraph = ((BasicComponentConfiguration)this.getConfiguration()).getParameter(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE.getName());
			if ( oLabelFromGraph != null){
				sNewCustomLabel = oLabelFromGraph.toString() ;
			}
			if ( sNewCustomLabel == null && ((BasicComponentConfiguration)this.getConfiguration()).getEntityFieldConfiguration() != null ) {
				Object oLabelFromEntity = ((BasicComponentConfiguration)this.getConfiguration()).getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE.getName());
				if ( oLabelFromEntity != null){
					sNewCustomLabel = oLabelFromEntity.toString() ;
				}		
			}
		}
		if (sNewCustomLabel!= null && sNewCustomLabel.length() > 0){
			this.oUiLabel.setText(sNewCustomLabel);
		}else{
			this.oUiLabel.setVisibility(GONE);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer configurationGetValue() {
		return  oUiNumberPicker.configurationGetValue();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#getValueType()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] { String.valueOf(this.configurationGetValue()) };
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return Integer.class;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		if ( super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder) ) {
			return VALIDATOR.validate(this, p_oConfiguration, p_oErrorBuilder) ;
		}
		return false ;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		oUiNumberPicker.configurationSetError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		oUiNumberPicker.configurationSetError(null);
	}
	/**
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiNumberPicker.configurationEnabledComponent();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiNumberPicker.configurationDisabledComponent();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(Integer p_oObject) {
		return configurationGetValue() == null ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.configurationGetValue() != null ;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.oUiNumberPicker.resetChanged()  ;
		this.aivDelegate.resetChanged();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged() || this.oUiNumberPicker.isChanged() ;
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		if (this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationRemoveMandatoryLabel();
			oUiLabel.configurationRemoveMandatoryLabel();
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		if (!this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationSetMandatoryLabel();
			oUiLabel.configurationSetMandatoryLabel();
		}
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
}