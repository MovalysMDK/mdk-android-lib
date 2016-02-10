package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DoubleFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumberUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;
/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been made public so its included in
 * the project in this fashion for use with the preferences screen; I have made a few slight modifications to the code here, I simply put a
 * MAX and MIN default in the code but these values can still be set publically by calling code.
 */
public class MMDoublePickerGroup extends AbstractMMLinearLayout<Double> implements MMIdentifiableView {

	private static final Double DOUBLE_DEFAULT = 0.0d;

	private static DoubleFormFieldValidator VALIDATOR;

	/** the label above the double picker */
	private TextView oUiLabel;
	/** picker permettant d'incrémenter le nombre de partie entière*/
	private MMNumberPicker oUiPickerInt;
	/** picker permettant d'incrémenter le nombre de décimales */
	private MMNumberPicker oUiPickerDigit;

	/**
	 * Constructor
	 * @param p_oContext
	 */
	public MMDoublePickerGroup(Context p_oContext) {
		this(p_oContext, null);

	}
	/**
	 * Constructor
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMDoublePickerGroup(Context p_oContext, AttributeSet p_oAttrs) {
		this(p_oContext, p_oAttrs, 0);

	}
	/**
	 * Constructor
	 * @param p_oContext
	 * @param p_oAttrs
	 * @param p_iDefStyle
	 */
	public MMDoublePickerGroup(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this) ;

			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_double_picker), this, true);
			oUiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__double__label));
			oUiPickerInt = (MMNumberPicker) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__int__picker));
			oUiPickerDigit = (MMNumberPicker) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__digit__picker)); 

			oUiPickerInt.setOrientation(VERTICAL);
			oUiPickerDigit.setOrientation(VERTICAL);
			oUiPickerDigit.setMinimalValue(0);

			this.defineParameters(p_oAttrs);
			VALIDATOR = BeanLoader.getInstance().getBean( DoubleFormFieldValidator.class );
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
	public void configurationSetValue(Double p_oObjectToSet) {
		super.configurationSetValue(p_oObjectToSet);
		if( p_oObjectToSet != null) {
			int[] lCutDouble =  NumberUtils.cutDouble(p_oObjectToSet); 
			this.oUiPickerInt.configurationSetValue(lCutDouble[0]);
			this.oUiPickerDigit.configurationSetValue(lCutDouble[1]);
		} else{
			int[] lCutDouble =  NumberUtils.cutDouble(DOUBLE_DEFAULT);
			this.oUiPickerInt.configurationSetValue(lCutDouble[0]);
			this.oUiPickerDigit.configurationSetValue(lCutDouble[1]);
		}
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0) {
			this.configurationSetValue(DOUBLE_DEFAULT);
		} else {
			Double sFirstValue ;
			try {
				sFirstValue = Double.parseDouble(p_oObjectsToSet[0]);
			} catch (NumberFormatException e) {
				sFirstValue = null ; //par défaut si erreur
			}
			if (sFirstValue == null) {
				this.configurationSetValue(DOUBLE_DEFAULT);
			}
			else {
				this.configurationSetValue(sFirstValue);
			}
		}
		this.setCustomLabel();
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
	public Double configurationGetValue() {
		Double dValue = null;
		try {
			Integer oIntPart = oUiPickerInt.configurationGetValue() ;
			StringBuilder sCompleteDouble = new StringBuilder();
			if (oIntPart!= null){
				sCompleteDouble.append(oIntPart);
			}
			Integer oDigitPart = oUiPickerDigit.configurationGetValue() ;
			if (oDigitPart!= null){
				if (sCompleteDouble.length() == 0){
					sCompleteDouble.append('0');
				}
				sCompleteDouble.append('.').append(oDigitPart);
			}
			if (sCompleteDouble.length() != 0){
				dValue = Double.valueOf( sCompleteDouble.toString());
			}
		} catch (NumberFormatException e) {
			Log.d("MMDoublePickerGroup", "NumberFormatException");
		}
		return dValue ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#getValueType()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		String[] oReturnValue = null;
		
		if (this.configurationGetValue() != null) {
			oReturnValue = new String[] { this.configurationGetValue().toString() };
		}
		
		return oReturnValue;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return Double.class;
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
		oUiPickerInt.configurationSetError(p_oError);
		oUiPickerDigit.configurationSetError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		oUiPickerInt.configurationSetError(null);
		oUiPickerDigit.configurationSetError(null);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiPickerInt.configurationEnabledComponent();
		oUiPickerDigit.configurationEnabledComponent();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiPickerInt.configurationDisabledComponent();
		oUiPickerDigit.configurationDisabledComponent();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(Double p_oObject) {
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
		this.oUiPickerInt.resetChanged()  ;
		this.oUiPickerDigit.resetChanged()  ;
		this.aivDelegate.resetChanged();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged() || this.oUiPickerInt.isChanged() || this.oUiPickerDigit.isChanged() ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		if (this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationRemoveMandatoryLabel();
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
		}
	}

	/**
	 * Définit le nombre de décimales après la virgule.
	 * @param p_iDecimalCount Nombre de décimales après la virguile.
	 */
	public void setDecimalCount(int p_iDecimalCount) {
		this.oUiPickerDigit.setMaximalValue((int) Math.pow(NumericConstants.DECIMAL_UNIT, p_iDecimalCount) - 1);
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