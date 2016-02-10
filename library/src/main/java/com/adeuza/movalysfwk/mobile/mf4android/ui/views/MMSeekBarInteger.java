package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.text.NumberFormat;

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

/**
 * <p>RadioGroup widget used in the Movalys Mobile product for Android</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 */
public class MMSeekBarInteger extends AbstractMMTableLayout<Integer> implements MMIdentifiableView , OnSeekBarChangeListener, InstanceStatable {

	private static final int INIT_VALUE = -1 ;
	private static final int DEFAULT_MIN_VALUE = 0 ;
	private static final int DEFAULT_MAX_VALUE = 20 ;
	private static final int DEFAULT_STEP = 1 ;

	/** Composant dédié à l'affichage de la valeur. */
	private TextView uiValueDisplayComponent;	
	/** Composant dédié à la modification de la valeur. */
	private SeekBar uiModifierComponent;

	private boolean writingData = false ;

	private int minValue  = DEFAULT_MIN_VALUE ;
	private int maxValue  = DEFAULT_MAX_VALUE ;
	private int step = DEFAULT_STEP ;
	private NumberFormat formater = NumberFormat.getIntegerInstance();  

	/**
	 * The key used to retain value on orientation changed 
	 */
	private static final String SEEK_BAR_INTEGER_VALUE_KEY = "seekBarIntegerValueKey" ;

	/**
	 * Constructs a new MMRadioGroup
	 * @param p_oContext
	 *            the context to use
	 * @see RadioGroup#RadioGroup(Context, AttributeSet)
	 */
	public MMSeekBarInteger(Context p_oContext) {
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
	public MMSeekBarInteger(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oInflater = LayoutInflater.from(this.getContext());
			oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_integer_seekbar), this, true);
			uiModifierComponent = (SeekBar) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_seekbar__integer__seekbar));
			uiValueDisplayComponent = (TextView) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_seekbar__integer__value));

			if (p_oAttrs!= null){
				this.minValue = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "min-value", DEFAULT_MIN_VALUE);
				this.maxValue = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "max-value", DEFAULT_MAX_VALUE);
				this.step = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "step", DEFAULT_STEP);
			}
			uiModifierComponent.setMax(Math.round((this.maxValue - this.minValue)/ this.step));
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		if(!isInEditMode()) {
			uiModifierComponent.setOnSeekBarChangeListener(this);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet[0] != null && p_oObjectsToSet[0].length() > 0) {
			this.configurationSetValue( Integer.valueOf(p_oObjectsToSet[0]) );
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
		return Long.class; 
	}
	/**
	 * p_oObjectToSet id android
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(Integer p_oObjectToSet) {
		if(p_oObjectToSet != null) {
			
			//Correction des valeurs si elles sont hors-bornes, et mise à jour du ViewModel en conséquence
			p_oObjectToSet = Math.min(p_oObjectToSet, DEFAULT_MAX_VALUE);
			p_oObjectToSet = Math.max(p_oObjectToSet, DEFAULT_MIN_VALUE);
			
			this.writingData = true ;

			this.aivDelegate.configurationSetValue(p_oObjectToSet);
			this.uiValueDisplayComponent.setText( formater.format(p_oObjectToSet)) ;
			int iProgress = (p_oObjectToSet - this.minValue )/ this.step ;
			this.uiModifierComponent.setProgress(iProgress);
			this.writingData = false ;
		}

	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(Integer p_oObject) {
		return this.configurationGetValue() == INIT_VALUE;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public Integer configurationGetValue() {
		CharSequence oValue = this.uiValueDisplayComponent.getText() ;
		if (oValue != null ) {
			return Integer.parseInt(oValue.toString());
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
		this.uiValueDisplayComponent.setError(null);
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
		r_oBundle.putInt(SEEK_BAR_INTEGER_VALUE_KEY, this.uiModifierComponent.getProgress());

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

		Integer retainValue = r_oBundle.getInt(SEEK_BAR_INTEGER_VALUE_KEY);
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