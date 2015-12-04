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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DoubleFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumberUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;
/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been made public so its included in
 * the project in this fashion for use with the preferences screen; I have made a few slight modifications to the code here, I simply put a
 * MAX and MIN default in the code but these values can still be set publicly by calling code.
 */
public class MMDoublePicker extends AbstractMMLinearLayout<Double> 
	implements ComponentValidator, ComponentEnable, 
	ComponentReadableWrapper<Double>, ComponentWritableWrapper<Double> {

	/** Default component value */
	public static final Double DOUBLE_DEFAULT = 0.0d;
	
	/** component validator */
	private DoubleFormFieldValidator validator;
	
	/** picker used to increment the integer part */
	private MMNumberPicker oUiPickerInt;
	
	/** picker used to increment the decimal part */
	private MMNumberPicker oUiPickerDigit;

	/**
	 * Constructor
	 * @param p_oContext the android context
	 */
	public MMDoublePicker(Context p_oContext) {
		this(p_oContext, null);
	}
	/**
	 * Constructor
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view AttributeSet
	 */
	public MMDoublePicker(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Double.class);
		if(!isInEditMode()) {
			
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_double_picker), this, true);
			oUiPickerInt = (MMNumberPicker) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__int__picker));
			oUiPickerDigit = (MMNumberPicker) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__digit__picker)); 

			oUiPickerInt.setOrientation(VERTICAL);
			oUiPickerDigit.setOrientation(VERTICAL);
			oUiPickerDigit.setMinimalValue(0);

			this.defineParameters(p_oAttrs);
			validator = BeanLoader.getInstance().getBean( DoubleFormFieldValidator.class );
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
			validator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes() , (BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration() );
		}
	}

	/**
	 * Sets the number of digits
	 * @param p_iDecimalCount number of digits
	 */
	public void setDecimalCount(int p_iDecimalCount) {
		this.oUiPickerDigit.setMaximalValue((int) Math.pow(NumericConstants.DECIMAL_UNIT, p_iDecimalCount) - 1);
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
			oUiPickerInt.getComponentDelegate().configurationEnabledComponent();
			oUiPickerDigit.getComponentDelegate().configurationEnabledComponent();
		} else {
			oUiPickerInt.getComponentDelegate().configurationDisabledComponent();
			oUiPickerDigit.getComponentDelegate().configurationDisabledComponent();
		}
	}
	
	@Override
	public Double configurationGetValue() {
		Double dValue = null;
		try {
			Integer oIntPart = oUiPickerInt.getComponentDelegate().configurationGetValue() ;
			StringBuilder sCompleteDouble = new StringBuilder();
			if (oIntPart!= null){
				sCompleteDouble.append(oIntPart);
			}
			Integer oDigitPart = oUiPickerDigit.getComponentDelegate().configurationGetValue() ;
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
			Log.d("MMDoublePicker", "NumberFormatException");
		}
		return dValue;
	}
	
	@Override
	public void configurationSetValue(Double p_oObjectToSet) {
		if( p_oObjectToSet != null) {
			int[] lCutDouble =  NumberUtils.cutDouble((Double) p_oObjectToSet); 
			this.oUiPickerInt.getComponentDelegate().configurationSetValue(lCutDouble[0]);
			this.oUiPickerDigit.getComponentDelegate().configurationSetValue(lCutDouble[1]);
		} else{
			int[] lCutDouble =  NumberUtils.cutDouble(MMDoublePicker.DOUBLE_DEFAULT);
			this.oUiPickerInt.getComponentDelegate().configurationSetValue(lCutDouble[0]);
			this.oUiPickerDigit.getComponentDelegate().configurationSetValue(lCutDouble[1]);
		}
	}
	
	@Override
	public boolean isFilled() {
		return this.oUiPickerInt.isFilled();
	}
}
