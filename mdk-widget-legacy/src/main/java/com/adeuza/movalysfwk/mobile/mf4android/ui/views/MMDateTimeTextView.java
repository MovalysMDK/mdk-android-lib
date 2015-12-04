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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

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
 */
@BaseComponent(baseName="MMBaseTextView", baseClass="android.widget.TextView", appCompatClass="android.support.v7.widget.AppCompatTextView")
public class MMDateTimeTextView extends MMBaseTextView implements ConfigurableVisualComponent, InstanceStatable, 
ComponentReadableWrapper<Long>, ComponentWritableWrapper<Long>{

	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<Long> aivDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;

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
	protected enum Mode {
		/** displays only the date */
		date, 
		/** displays the date and the time */
		datetime, 
		/** displays only the time */
		time
	}

	/** mode configuration */
	protected Mode configuration;
	
	/** the date formatter*/
	private int iDateFormat=DateFormat.SHORT;
	
	/** time formatter */
	private int iTimeFormat=DateFormat.SHORT;
	
	/** custom format applied */
	private String format="" ;
	
	/** the current selected date */
	private Calendar calendar = null;
	
	/** the formatter for text representation*/
	private DateFormat oDateTimeFormater;

	/**
	 * Construct a MMDateTimeViewGroup
	 * @param p_oContext the context
	 */
	@SuppressWarnings("unchecked")
	public MMDateTimeTextView(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Long>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Long.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}
	/**
	 * Construct a MMDateTimeViewGroup
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	@SuppressWarnings("unchecked")
	public MMDateTimeTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Long>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Long.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
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
			oDateTimeFormater = new SimpleDateFormat(this.format, this.getResources().getConfiguration().locale) ;
		}
	}

	/**
	 * set the date to the textView using the mode (date / datetime / time) setted in the xml
	 */
	public void updateDisplay() {

		String oValueToSetInEditText = "";
		if(this.aivFwkDelegate.customFormatter() != null) {
			oValueToSetInEditText = (String) this.aivFwkDelegate.customFormatter().format( this.calendar.getTimeInMillis() );
		} else {
			oValueToSetInEditText = oDateTimeFormater.format(this.calendar.getTime());
		}
		this.setText(oValueToSetInEditText);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
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

		Parcelable superParcel = this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		if ( this.calendar != null) {
			r_oBundle.putLong("calendar", this.calendar.getTimeInMillis());
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
		this.aivFwkDelegate.onRestoreInstanceState(oParentParcelable);
		this.configuration = (Mode) r_oBundle.getSerializable("configuration");
		this.format = r_oBundle.getString("format");
		if(r_oBundle.containsKey("calendar")){
			this.aivDelegate.configurationSetValue(r_oBundle.getLong("calendar") );
		}
		else {
			this.aivDelegate.configurationSetValue(null);
		}

	}
	
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}
	
	@Override
	public VisualComponentDelegate<Long> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/****************************************************************************************************************
	 ************************************ Framework delegate callback ***********************************************
	 ****************************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Long configurationGetValue() {
		Long r_oValue = null;
		if(this.getComponentFwkDelegate().customFormatter() != null) {
			if (this.isFilled()) {
				r_oValue = (Long) this.getComponentFwkDelegate().customFormatter().unformat(this.getText().toString());
			}
		}
		else {
			r_oValue = this.calendar.getTimeInMillis();
		}
		
		return r_oValue;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Long p_oObjectToSet) {
		if (p_oObjectToSet==null || p_oObjectToSet.equals(-1L)) {
			this.setText(StringUtils.EMPTY);
			this.calendar = null;
		}
		else {
			if (this.calendar == null) {
				this.calendar = Calendar.getInstance();
			}
			this.calendar.setTimeInMillis((Long) p_oObjectToSet);
			this.updateDisplay();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.getText().toString().length() > 0;
	}
}
