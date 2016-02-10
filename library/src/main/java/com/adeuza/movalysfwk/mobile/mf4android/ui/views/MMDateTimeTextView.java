package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * This view group represents a date text view, its label and its button to use
 * the datePickerDialog,
 * <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the
 * main tag</li>
 * <li>include a
 * <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDateViewGroup></li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * <li>add mode attribute to declare date / datetime / time mode to enable or
 * disable buttons and switch the text representation</li>
 * <li>add date_format attribute to declare the full / long / medium / short format for the date</li>
 * <li>add time_format attribute to declare the full / long / medium / short format for the time</li>
 * </ul>
 * <p>
 * Copyright (c) 2010
 * <p>
 * Company: Adeuza
 * 
 * @author dmaurange
 * @since Annapurna
 * 
 */

public class MMDateTimeTextView extends TextView implements ConfigurableVisualComponent<Long>, MMIdentifiableView, InstanceStatable {
	/** Log TAG */
	private static final String TAG = "MMDateTimeTextView";

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Long> aivDelegate = null;

	/** the application */
	private AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

	/** the Date only mode*/
	public static final String DATE_MODE="date";
	/** the Date Time mode*/
	public static final String DATETIME_MODE="datetime";
	/** the Time only mode*/
	public static final String TIME_MODE="time";
	/** the short format*/
	public static final String SHORT_FORMAT="short";
	/** the medium format*/
	public static final String MEDIUM_FORMAT="medium";
	/** the long format*/
	public static final String LONG_FORMAT="long";
	/** the full format*/
	public static final String FULL_FORMAT="full";
	/** the configuration of date and time display*/
	protected enum Mode {date, datetime, time}

	protected Mode configuration;
	/** the date and time formater*/
	private int iDateFormat=DateFormat.SHORT;
	private int iTimeFormat=DateFormat.SHORT;

	private String format="" ;

	/** the formater for text representation*/
	private DateFormat oDateTimeFormater;
	/** the date used by the component*/
	private Calendar oCalendar = null;

	//private static final int TIME_DIALOG_ID = 0;
	//private static final int DATE_DIALOG_ID = 1;

	private boolean autoHide;

	/**
	 * Construct a MMDateTimeViewGroup
	 * @param p_oContext the context
	 */
	public MMDateTimeTextView(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
			this.autoHide = true;
		}
	}
	/**
	 * Construct a MMDateTimeViewGroup
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMDateTimeTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.autoHide = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "auto-hide", true);
			linkChildrens(p_oAttrs);
		}

	}

	/**
	 * link the child views with custom attributes and add button onClickListener
	 * @param p_oAttrs attributes of XML Component MMDateTimeViewGroup
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		String sMode=p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "mode");

		if (DATE_MODE.equals(sMode)){
			configuration=Mode.date;
			oDateTimeFormater = DateFormat.getDateInstance(iDateFormat);
		}else if (DATETIME_MODE.equals(sMode)){
			configuration=Mode.datetime;
			oDateTimeFormater = DateFormat.getDateTimeInstance(iDateFormat, iTimeFormat);
		}else if (TIME_MODE.equals(sMode)){
			configuration=Mode.time;
			oDateTimeFormater = DateFormat.getTimeInstance(iTimeFormat);
		}else {
			oDateTimeFormater = DateFormat.getDateTimeInstance(iDateFormat, iTimeFormat);
			configuration=Mode.datetime;
		}

		//récupération du format de date à appliquer dans les propriétés du composant
		String sDateFormat=p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "date_format");
		if (FULL_FORMAT.equals(sDateFormat)){
			iDateFormat=DateFormat.FULL;
		}else if (LONG_FORMAT.equals(sDateFormat)){
			iDateFormat=DateFormat.LONG;
		}else if (MEDIUM_FORMAT.equals(sDateFormat)){
			iDateFormat=DateFormat.MEDIUM;
		}else{
			iDateFormat=DateFormat.SHORT;
		}

		//récupération du format d'heure à appliquer dans les propriétés du composant
		String sTimeFormat=p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "time_format");
		if (FULL_FORMAT.equals(sTimeFormat)){
			iTimeFormat=DateFormat.FULL;
		}else if (LONG_FORMAT.equals(sTimeFormat)){
			iTimeFormat=DateFormat.LONG;
		}else if (MEDIUM_FORMAT.equals(sTimeFormat)){
			iTimeFormat=DateFormat.MEDIUM;
		}else{
			iTimeFormat=DateFormat.SHORT;
		}

		this.format=p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "format" );
		if ( this.format != null ) {
			oDateTimeFormater = new SimpleDateFormat(this.format) ;
		}
	}

	/**
	 * set the date to the textView using the mode (date / datetime / time) setted in the xml
	 */
	private void updateDisplay() {

		String oValueToSetInEditText = "";
		if(this.customFormatter() != null) {
			oValueToSetInEditText = (String) customFormatter().format(oCalendar.getTimeInMillis());
		} else {
			oValueToSetInEditText = oDateTimeFormater.format(oCalendar.getTime());
		}
		this.setText(oValueToSetInEditText);
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
	public void configurationSetValue(Long p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		if (p_oObjectToSet==null || p_oObjectToSet.equals(-1L)) {
			this.setText(StringUtils.EMPTY);
			this.oCalendar = null;
		}
		else {
			if (this.oCalendar == null) {
				this.oCalendar = Calendar.getInstance();
			}
			this.oCalendar.setTimeInMillis(p_oObjectToSet);
			this.updateDisplay();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.setVisibility(View.GONE);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long configurationGetValue() {
		Long r_oValue = null;
		if(customFormatter() != null) {
			if (this.getText().toString().length() > 0) {
				r_oValue = (Long) customFormatter().unformat(this.getText().toString());
			}
		}
		else {
			r_oValue = oCalendar.getTimeInMillis();
		}
		
		return r_oValue;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationGetCustomValues()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		String[] r_oValue = new String[0];
		if (this.configurationGetValue() != null) {
			r_oValue = new String[] { Long.toString(this.configurationGetValue()) };
		}
		return r_oValue;
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
	public boolean isNullOrEmptyValue(Long p_oObject) {
		return (p_oObject == null || 0L == p_oObject.longValue() || -1L ==  p_oObject.longValue()) && this.autoHide;
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
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return Long.class;
	}

	/**
	 * {@inheritDoc}
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
		this.setClickable(false);
		this.clearFocus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.getText().length() > 0;
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
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		this.aivDelegate.configurationRemoveMandatoryLabel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		this.aivDelegate.configurationSetMandatoryLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnPostAutoBind() {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#destroy()
	 */
	@Override
	public void destroy() {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();

	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_sError) {
		this.aivDelegate.configurationSetError(p_sError);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#validate(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration, java.util.Map, java.lang.StringBuilder)
	 */
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
	 * @see android.widget.TextView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {

		Parcelable superParcel = this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		if (this.oCalendar != null) {
			r_oBundle.putLong("calendar", this.oCalendar.getTimeInMillis());
		}
		r_oBundle.putSerializable("configuration", this.configuration);
		r_oBundle.putString("format", this.format);
		return r_oBundle;

		//return this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {

		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		this.aivDelegate.onRestoreInstanceState(oParentParcelable);
		this.configuration = (Mode) r_oBundle.getSerializable("configuration");
		this.format = r_oBundle.getString("format");
		if(r_oBundle.containsKey("calendar")){
			this.configurationSetValue(r_oBundle.getLong("calendar") );
		}
		else {
			this.configurationSetValue(null);
		}

		//this.aivDelegate.onRestoreInstanceState(p_oState);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#setName(java.lang.String)
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