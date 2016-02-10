package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumberUtils;

/**
 * <p>RadioGroup widget used in the Movalys Mobile product for Android</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 */
public class MMSeekBarDouble extends AbstractMMTableLayout<Double> implements MMIdentifiableView , OnSeekBarChangeListener, InstanceStatable {

	private static final double INIT_VALUE = -1d ;
	private static final double DEFAULT_MIN_VALUE = 0d ;
	private static final double DEFAULT_MAX_VALUE = 10d ;
	private static final double DEFAULT_STEP = 1d ;

	/**
	 * The key used to retain value on orientation changed 
	 */
	private static final String SEEK_BAR_DOUBLE_VALUE_KEY = "seekBarDoubleValueKey" ;

	/** Composant dédié à l'affichage de la valeur. */
	private TextView uiValueDisplayComponent;	
	/** Composant dédié à la modification de la valeur. */
	private SeekBar uiModifierComponent;

	private boolean writingData = false ;

	private double minValue  = DEFAULT_MIN_VALUE ;
	private double maxValue  = DEFAULT_MAX_VALUE ;
	private double step = DEFAULT_STEP ;
	private DecimalFormat formater ;  
	/**
	 * Constructs a new MMRadioGroup
	 * @param p_oContext
	 *            the context to use
	 * @see RadioGroup#RadioGroup(Context, AttributeSet)
	 */
	public MMSeekBarDouble(Context p_oContext) {
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
	public MMSeekBarDouble(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);

		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oInflater = LayoutInflater.from(this.getContext());
			oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_double_seekbar), this, true);
			uiModifierComponent = (SeekBar) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_seekbar__double__seekbar));
			uiValueDisplayComponent = (TextView) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_seekbar__double__value));

			DecimalFormatSymbols oSymbols = new DecimalFormatSymbols();
			oSymbols.setDecimalSeparator('.');
			formater = new DecimalFormat("#", oSymbols);

			if (p_oAttrs!= null){
				this.minValue = p_oAttrs.getAttributeFloatValue(Application.MOVALYSXMLNS, "min-value", (float)DEFAULT_MIN_VALUE);
				this.maxValue = p_oAttrs.getAttributeFloatValue(Application.MOVALYSXMLNS, "max-value", (float)DEFAULT_MAX_VALUE);
				this.step = p_oAttrs.getAttributeFloatValue(Application.MOVALYSXMLNS, "step", (float)DEFAULT_STEP);
			}
			uiModifierComponent.setMax(NumberUtils.safeLongToInt(Math.round((this.maxValue - this.minValue)/ this.step)));
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		if(!isInEditMode()) {
			uiModifierComponent.setOnSeekBarChangeListener(this);
			uiModifierComponent.setFocusable(true);
			uiModifierComponent.setFocusableInTouchMode(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet[0] != null && p_oObjectsToSet[0].length() > 0) {
			this.configurationSetValue( Double.valueOf(p_oObjectsToSet[0]) );
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[]{ formater.format(this.configurationGetValue()) };
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return Double.class; 
	}
	/**
	 * p_oObjectToSet id android
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(Double p_oObjectToSet) {
		if(p_oObjectToSet != null) {
			
			//Correction des valeurs si elles sont hors-bornes, et mise à jour du ViewModel en conséquence
			p_oObjectToSet = Math.min(p_oObjectToSet, DEFAULT_MAX_VALUE);
			p_oObjectToSet = Math.max(p_oObjectToSet, DEFAULT_MIN_VALUE);
			
			this.writingData = true ;

			this.aivDelegate.configurationSetValue(p_oObjectToSet);
			this.uiValueDisplayComponent.setText( formater.format(p_oObjectToSet)) ;
			int iProgress = NumberUtils.safeLongToInt( (long)((p_oObjectToSet - this.minValue )/ this.step) );
			this.uiModifierComponent.setProgress(iProgress);
			this.writingData = false ;
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(Double p_oObject) {
		return this.configurationGetValue() == INIT_VALUE;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public Double configurationGetValue() {
		CharSequence oValue = this.uiValueDisplayComponent.getText() ;
		if (oValue != null && !oValue.equals("")) {
			return Double.parseDouble(oValue.toString());
		}
		return INIT_VALUE ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.configurationGetValue() != INIT_VALUE ;
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
	public void destroy() {
		//Nothing to do
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationUnsetError()
	 */
	@Override
	public void configurationUnsetError() {
		if(this.uiValueDisplayComponent.getError() != null) {
			this.uiValueDisplayComponent.setError(null);
		}
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetError(java.lang.String)
	 */
	@Override
	public void configurationSetError(String p_sError) {
		this.uiValueDisplayComponent.setError(p_sError);
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
	 */
	@Override
	public void onStartTrackingTouch(SeekBar p_oArg0) {
		this.uiModifierComponent.requestFocusFromTouch();
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
	 */
	@Override
	public void onStopTrackingTouch(SeekBar p_oArg0) {
		this.uiModifierComponent.requestFocusFromTouch();
	}
	/**
	 * {@inheritDoc}
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
	 */
	@Override
	public void onProgressChanged(SeekBar p_oSeekBar, int p_oProgress, boolean p_oFromUser) {
		double dNewValue = (double)( p_oProgress * this.step + this.minValue );

		this.uiValueDisplayComponent.setText(formater.format(dNewValue));

		if ( p_oFromUser) {
			this.uiModifierComponent.requestFocusFromTouch();
		}
		
		if ( !this.writingData && this.aivDelegate != null ) {
			this.aivDelegate.changed();
		}
		this.configurationUnsetError();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {

		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putDouble(SEEK_BAR_DOUBLE_VALUE_KEY, new Double(this.uiModifierComponent.getProgress()));

		return r_oBundle;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		Double retainValue = r_oBundle.getDouble(SEEK_BAR_DOUBLE_VALUE_KEY);
		this.uiModifierComponent.setProgress(retainValue.intValue());
		this.uiValueDisplayComponent.setText(String.valueOf(retainValue));
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
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.uiModifierComponent.setEnabled(enabled);
	}
}