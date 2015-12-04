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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import java.text.NumberFormat;

import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.seekbar.ComponentValuedView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.AbstractConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * A delegate of a SeekBar component.<br/>
 * 
 * this class contains all default behaviors of the SeekBar component in the framework.
 *
 * @param <VALUE> the type handled by the SeekBar component
 * @since 4.3.0
 */
public class AndroidConfigurableSeekBarComponentDelegate<VALUE> extends AndroidConfigurableVisualComponentDelegate<VALUE> implements OnSeekBarChangeListener {

	/** log tag */
	private static final String TAG = "AndroidConfigurableSeekBarComponentDelegate";
	/** initial value of the component */
	private static final Double INIT_VALUE = -1d;
	/** default minimal value */
	private static final float DEFAULT_MIN_VALUE = 0f;
	/** default maximal value */
	private static final float DEFAULT_MAX_VALUE = 10f;
	/** default step value */
	private static final float DEFAULT_STEP = 1f;
	/** number formatter for this component */
	private NumberFormat formater = NumberFormat.getIntegerInstance();
	/** component minimal value */
	private double minValue = DEFAULT_MIN_VALUE;
	/** component maximal value */
	private double maxValue = DEFAULT_MAX_VALUE;
	/** component step value */
	private double step = DEFAULT_STEP;
	
	/**
	 * Create a new AndroidConfigurableSeekBarComponentDelegate
	 * @param p_oCurrentView the android view
	 * @param p_oDelegateType the class of the type handled by the component
	 */
	public AndroidConfigurableSeekBarComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		this(p_oCurrentView, p_oDelegateType, null);
	}
	
	/**
	 * Create a new AndroidConfigurableSeekBarComponentDelegate
	 * @param p_oCurrentView the android view
	 * @param p_oDelegateType the class of the type handled by the component
	 * @param p_oAttrs android view AttributeSet (in layout XML)
	 */
	public AndroidConfigurableSeekBarComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
		if (p_oDelegateType == Double.class) {
			this.formater = NumberFormat.getNumberInstance();
		}
		if (p_oAttrs != null) {
			String sMin = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "min-value");
			String sMax = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "max-value");
			String sStep = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "step");
			
			if (sMax == null) {
				this.maxValue = DEFAULT_MAX_VALUE;
			} else {
				try {
					this.maxValue = Double.parseDouble(sMax);
				} catch (NumberFormatException e) {
					Log.e(TAG, "Wrong format : use default value "+DEFAULT_MAX_VALUE+" insted of "+sMax);
					this.maxValue = DEFAULT_MAX_VALUE;
				}
			}
			
			if (sMin == null) {
				this.minValue = DEFAULT_MIN_VALUE;
			} else {
				try {
					this.minValue = Double.parseDouble(sMin);
				} catch (NumberFormatException e) {
					Log.e(TAG, "Wrong format for min-value attribute : use default value "+DEFAULT_MIN_VALUE+" insted of "+sMin);
					this.minValue = DEFAULT_MIN_VALUE;
				}
			}
			
			if (sStep == null) {
				this.step = DEFAULT_STEP;
			} else {
				try {
					this.step = Double.parseDouble(sStep);
				} catch (NumberFormatException e) {
					Log.e(TAG, "Wrong format for step attribute : use default value "+DEFAULT_STEP+" insted of "+sStep);
					this.step = DEFAULT_STEP;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public VALUE configurationGetValue() {
		VALUE r_nReturnValue = null;
		if (this.currentView instanceof ComponentValuedView) {
			TextView valuedView = ((ComponentValuedView) this.currentView).getValuedComponent();
			CharSequence oValue = valuedView.getText() ;
			if (oValue != null ) {
				VALUE valueToGet;
				if (this.currentCvComponent.getComponentFwkDelegate().customFormatter() == null) {
					// format with default formatter
					valueToGet = (VALUE) new Double(Double.parseDouble(oValue.toString()));
				} else {
					// format with user formatter
					valueToGet = this.getUnFormattedValue((VALUE) oValue);
				}
				r_nReturnValue  = (VALUE) valueToGet;
			} else {
				r_nReturnValue = (VALUE) new Double(INIT_VALUE);
			}
		} else {
			r_nReturnValue = (VALUE) new Double(INIT_VALUE);
		}
		return r_nReturnValue;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void configurationSetValue(VALUE p_oObjectToSet) {
		if(p_oObjectToSet != null) {
			
			//Correction des valeurs si elles sont hors-bornes, et mise à jour du ViewModel en conséquence
			Double iObject = (Double) Math.min((Double) p_oObjectToSet, this.maxValue);
			iObject = Math.max((Double) p_oObjectToSet, this.minValue);
			
			super.configurationSetValue((VALUE) iObject);
			
			this.writingData = true;
			if (this.currentView instanceof ComponentValuedView) {
				String textToSet = "";
				if (this.currentCvComponent.getComponentFwkDelegate().customFormatter() == null) {
					// format with default formatter
					textToSet = formater.format(p_oObjectToSet);
				} else {
					// format with user formatter
					textToSet = (String) this.getFormattedValue(p_oObjectToSet);
				}
					
				((ComponentValuedView) this.currentView).getValuedComponent().setText( textToSet) ;
				int iProgress = (int) ( ((Double)p_oObjectToSet - this.minValue) / this.step) ;
				((ComponentValuedView) this.currentView).getSeekBar().setProgress(iProgress);
			}
			
			this.writingData = false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isFilled() {
		return this.configurationGetValue() != (VALUE)INIT_VALUE;
	}

	@Override
	public boolean isNullOrEmptyValue(VALUE p_oObject) {
		return !this.isFilled();
	}
	
	/**
	 * Getter for minimal value
	 * @return the minimal value
	 */
	public double getMinValue() {
		return minValue;
	}
	
	/**
	 * Setter for minimal value
	 * @param p_dMinValue the minimal value to set
	 */
	public void setMinValue(double p_dMinValue) {
		this.minValue = p_dMinValue;
	}
	
	/**
	 * Getter for maximal value
	 * @return the maximal value
	 */
	public double getMaxValue() {
		return maxValue;
	}
	
	/**
	 * Setter for the component maximal value
	 * @param p_dMaxValue the maximal value to set
	 */
	public void setMaxValue(double p_dMaxValue) {
		this.maxValue = p_dMaxValue;
	}
	
	/**
	 * Getter for the step value
	 * @return the step value
	 */
	public double getStep() {
		return step;
	}
	
	/**
	 * Setter for the step value of the component
	 * @param p_dStep the new step value
	 */
	public void setStep(double p_dStep) {
		this.step = p_dStep;
	}

	@Override
	public void onProgressChanged(SeekBar p_oSeekBar, int p_oProgress, boolean p_bFromUser) {
		double dNewValue = (double)( p_oProgress * this.step + this.minValue );

		if (this.currentView instanceof ComponentValuedView) {
			((ComponentValuedView) this.currentView).getValuedComponent().setText(formater.format(dNewValue));
		}

		if (p_bFromUser) {
			p_oSeekBar.requestFocusFromTouch();
		}
		
		if ( !this.isWritingData() ) {
			((AbstractConfigurableVisualComponentFwkDelegate) this.currentCvComponent.getComponentFwkDelegate()).changed();
		}
	
		this.configurationUnsetError();
	}

	@Override
	public void onStartTrackingTouch(SeekBar p_oSeekBar) {
		p_oSeekBar.requestFocusFromTouch();
	}

	@Override
	public void onStopTrackingTouch(SeekBar p_oSeekBar) {
		p_oSeekBar.requestFocusFromTouch();
	}

	/**
	 * Sets the maximum read value on the seekbar view
	 */
	public void setMaxValueOnSeekBar() {
		if (this.currentView instanceof ComponentValuedView) {
			((ComponentValuedView) this.currentView).getSeekBar().setMax( Math.round( ((float) ((this.maxValue - this.minValue) / this.step)) ) ); 
		}
	}
}
