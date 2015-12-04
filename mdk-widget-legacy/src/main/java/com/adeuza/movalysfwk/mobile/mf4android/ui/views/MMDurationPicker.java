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

import java.util.Calendar;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DurationFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.DurationUtils;
/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been made public so its included in
 * the project in this fashion for use with the preferences screen; I have made a few slight modifications to the code here, I simply put a
 * MAX and MIN default in the code but these values can still be set publically by calling code.
 */
public class MMDurationPicker extends AbstractMMLinearLayout<Long> implements ComponentValidator, ComponentEnable, 
ComponentReadableWrapper<Long>, ComponentWritableWrapper<Long> {
	
	/** picker used to modify the hours number */
	private MMNumberPicker oUiPickerHours;
	
	/** picker used to modify the minutes number */
	private MMNumberPicker oUiPickerMinutes;
	
	/** the component validator */
	private DurationFormFieldValidator validator;
	
	/** maximum number of hours */
	private static final int NB_MAX_HOURS = 999 ;

	/**
	 * Create a new MMDurationPickerGroup
	 * @param p_oContext the android context
	 */
	public MMDurationPicker(Context p_oContext) {
		this(p_oContext, null);
		if(!isInEditMode()) {
			validator = BeanLoader.getInstance().getBean( DurationFormFieldValidator.class ) ;
		}
	}
	
	/**
	 * Create a new MMDurationPickerGroup (use by android in XML layouts)
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view attributes (define in XML)
	 */
	public MMDurationPicker(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Long.class);
		if(!isInEditMode()) {
			
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_duration_picker), this, true);
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
			validator = BeanLoader.getInstance().getBean( DurationFormFieldValidator.class ) ;
		}
	}

	/**
	 * Retrieve parameters from layout to put in the local map for validation
	 * @param p_oAttrs XML attribute set
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivFwkDelegate.defineParameters(p_oAttrs);
	}	
	
	/**
	 * called when the inflater finished the job before setting value
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			validator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes() , 
					(BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration() );
		}
	}

	/************************************************************************************************
	 ********************************** Framework delegate callback *********************************
	 ************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentValidator#getValidator()
	 */
	@Override
	public IFormFieldValidator getValidator() {
		return validator;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		if (p_bEnable) {
			oUiPickerHours.getComponentDelegate().configurationEnabledComponent();
			oUiPickerMinutes.getComponentDelegate().configurationEnabledComponent();
		} else {
			oUiPickerHours.getComponentDelegate().configurationDisabledComponent();
			oUiPickerMinutes.getComponentDelegate().configurationDisabledComponent();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Long configurationGetValue() {
		Long r_lValue ;
		try {
			StringBuilder sCompleteTime = new StringBuilder();
			if ( oUiPickerHours.getComponentDelegate().configurationGetValue() != null){
				sCompleteTime.append( oUiPickerHours.getComponentDelegate().configurationGetValue() );
			} else if ( oUiPickerMinutes.getComponentDelegate().configurationGetValue() == null ){
				// rien n'a été saisi
				return null ;
			} else {
				// seules les minutes ont été modifiées
				sCompleteTime.append("00") ;
			}
			sCompleteTime.append(':') ;
			if ( oUiPickerMinutes.getComponentDelegate().configurationGetValue() == null ){
				// seules les heures ont été modifiées
				sCompleteTime.append("00") ;
			} else {
				sCompleteTime.append(oUiPickerMinutes.getComponentDelegate().configurationGetValue());
			}
			r_lValue = DurationUtils.convertTimeToDurationInMinutes(sCompleteTime.toString()) ;
		} catch (NumberFormatException e) {
			r_lValue  = null ;
		}
		return r_lValue ;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Long p_oObjectToSet) {
		if( p_oObjectToSet != null) {
			String[] lCutTime =  DurationUtils.convertMinutesToDuration(p_oObjectToSet).split(":"); 
			this.oUiPickerHours.getComponentDelegate().configurationSetValue( Integer.parseInt(lCutTime[0]));
			this.oUiPickerMinutes.getComponentDelegate().configurationSetValue(Integer.parseInt(lCutTime[1]));
		} else{
			this.oUiPickerHours.getComponentDelegate().configurationSetValue(0);
			this.oUiPickerMinutes.getComponentDelegate().configurationSetValue(0);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.configurationGetValue() != null;
	}


}
