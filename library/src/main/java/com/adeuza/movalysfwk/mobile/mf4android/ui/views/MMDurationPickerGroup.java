package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Calendar;
import java.util.Map;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DurationFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.DurationUtils;
/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been made public so its included in
 * the project in this fashion for use with the preferences screen; I have made a few slight modifications to the code here, I simply put a
 * MAX and MIN default in the code but these values can still be set publically by calling code.
 */
public class MMDurationPickerGroup extends AbstractMMLinearLayout<Long> implements MMIdentifiableView {

	/** the label above the duration picker */
	private TextView oUiLabel;
	/** picker permettant d'incrémenter le nombre d'heures*/
	private MMNumberPicker oUiPickerHours;
	/** picker permettant d'incrémenter le nombre de minutes */
	private MMNumberPicker oUiPickerMinutes;

	private static DurationFormFieldValidator VALIDATOR;
	private static final int NB_MAX_HOURS = 999 ;

	/**
	 * TODO Décrire le constructeur MMDurationPickerGroup
	 * @param p_oContext
	 */
	public MMDurationPickerGroup(Context p_oContext) {
		this(p_oContext, null);
		if(!isInEditMode()) {
			VALIDATOR = BeanLoader.getInstance().getBean( DurationFormFieldValidator.class ) ;
		}
	}
	/**
	 * TODO Décrire le constructeur MMDurationPickerGroup
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMDurationPickerGroup(Context p_oContext, AttributeSet p_oAttrs) {
		this(p_oContext, p_oAttrs, 0);
		if(!isInEditMode()) {
			VALIDATOR = BeanLoader.getInstance().getBean( DurationFormFieldValidator.class ) ;
		}
	}
	/**
	 * TODO Décrire le constructeur MMDurationPickerGroup
	 * @param p_oContext
	 * @param p_oAttrs
	 * @param p_iDefStyle
	 */
	public MMDurationPickerGroup(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs);

		if(!isInEditMode()) {

			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_duration_picker), this, true);
			oUiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_durationpicker__duration__label));
			oUiPickerHours = (MMNumberPicker) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_durationpicker__hours__picker));
			oUiPickerMinutes = (MMNumberPicker) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_durationpicker__minutes__picker)); 

			oUiPickerHours.setOrientation( VERTICAL );
			oUiPickerHours.setMinimalValue(0);
			oUiPickerHours.setMaximalValue(NB_MAX_HOURS);
			((TextView) oUiPickerHours.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__number__edit))).setInputType(InputType.TYPE_CLASS_NUMBER);

			oUiPickerMinutes.setOrientation( VERTICAL );
			oUiPickerMinutes.setMinimalValue(0);
			oUiPickerMinutes.setMaximalValue(Calendar.getInstance().getActualMaximum(Calendar.MINUTE));
			((TextView) oUiPickerMinutes.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__number__edit))).setInputType(InputType.TYPE_CLASS_NUMBER);

			this.defineParameters(p_oAttrs);	
			VALIDATOR = BeanLoader.getInstance().getBean( DurationFormFieldValidator.class ) ;
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
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this) ;
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
	public void configurationSetValue(Long p_oObjectToSet) {
		super.configurationSetValue(p_oObjectToSet);
		if( p_oObjectToSet != null) {
			String[] lCutTime =  DurationUtils.convertMinutesToDuration(p_oObjectToSet).split(":"); 
			this.oUiPickerHours.configurationSetValue( Integer.parseInt(lCutTime[0]));
			this.oUiPickerMinutes.configurationSetValue(Integer.parseInt(lCutTime[1]));
		} else{
			this.oUiPickerHours.configurationSetValue(0);
			this.oUiPickerMinutes.configurationSetValue(0);
		}
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0) {
			this.configurationSetValue(0L);
		} else {
			Long sFirstValue ;
			try {
				sFirstValue = Long.parseLong(p_oObjectsToSet[0]);
			} catch (NumberFormatException e) {
				sFirstValue = null ; //par défaut si erreur
			}
			if (sFirstValue == null) {
				this.configurationSetValue(0L);
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
	public Long configurationGetValue() {
		Long r_lValue ;
		try {
			StringBuilder sCompleteTime = new StringBuilder();
			if ( oUiPickerHours.configurationGetValue() != null){
				sCompleteTime.append( oUiPickerHours.configurationGetValue() );
			} else if ( oUiPickerMinutes.configurationGetValue() == null ){
				// rien n'a été saisi
				return null ;
			} else {
				// seules les minutes ont été modifiées
				sCompleteTime.append("00") ;
			}
			sCompleteTime.append(':') ;
			if ( oUiPickerMinutes.configurationGetValue() == null ){
				// seules les heures ont été modifiées
				sCompleteTime.append("00") ;
			} else {
				sCompleteTime.append(oUiPickerMinutes.configurationGetValue());
			}
			r_lValue = DurationUtils.convertTimeToDurationInMinutes(sCompleteTime.toString()) ;
		} catch (NumberFormatException e) {
			r_lValue  = null ;
		}
		return r_lValue ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#getValueType()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		String[] oReturnValue = new String[0];
		
		if (this.configurationGetValue() != null) {
			oReturnValue = new String[] { String.valueOf(this.configurationGetValue()) };
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
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters ,StringBuilder p_oErrorBuilder) {
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
		oUiPickerHours.configurationSetError(p_oError);
		oUiPickerMinutes.configurationSetError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		oUiPickerHours.configurationSetError(null);
		oUiPickerMinutes.configurationSetError(null);
	}
	/**
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiPickerHours.configurationEnabledComponent();
		oUiPickerMinutes.configurationEnabledComponent();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiPickerHours.configurationDisabledComponent();
		oUiPickerMinutes.configurationDisabledComponent();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(Long p_oObject) {
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
		this.oUiPickerHours.resetChanged()  ;
		this.oUiPickerMinutes.resetChanged()  ;
		this.aivDelegate.resetChanged();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged() || this.oUiPickerHours.isChanged() || this.oUiPickerMinutes.isChanged() ;
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