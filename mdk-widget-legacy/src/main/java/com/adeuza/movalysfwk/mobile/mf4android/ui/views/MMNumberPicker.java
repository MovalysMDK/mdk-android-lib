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

import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IntegerFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been made public so its included in
 * the project in this fashion for use with the preferences screen; I have made a few slight modifications to the code here, I simply put a
 * MAX and MIN default in the code but these values can still be set publically by calling code.
 */
public class MMNumberPicker extends AbstractMMLinearLayout<Integer> implements OnClickListener, OnLongClickListener, 
	ComponentValidator, ComponentError, ComponentEnable, TextWatcher,
	ComponentReadableWrapper<Integer>, ComponentWritableWrapper<Integer> {
	
	/** Default minimum value */
	private static final int DEFAULT_MIN = Integer.MIN_VALUE;
	
	/** Default maximum value */
	private static final int DEFAULT_MAX = Integer.MAX_VALUE;
	
	/** default long click variation on the button */
	private static final int DEFAULT_DELTA_ON_LONG_CLICK = 10 ;
	
	/** default click variation on the button */
	private static final int DEFAULT_DELTA_ON_CLICK = 1 ;
	
	/** default scrolling speed */
	private static final int DEFAULT_SPEED = 300 ;
	
	/** click variation on the button */
	private int smallDelta = DEFAULT_DELTA_ON_CLICK;
	
	/** long click variation on the button */
	private int bigDelta = DEFAULT_DELTA_ON_LONG_CLICK;	
	
	/** validator on the input data */
	private IntegerFormFieldValidator validator;
	
	/** handler */
	private Handler mHandler;
	
	/** runnable to delay changes */
	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			if (bCanIncrement) {
				changeCurrent(mCurrent + bigDelta);
				mHandler.postDelayed(this, mSpeed);
			} else if (bCanDecrement) {
				changeCurrent(mCurrent - bigDelta);
				mHandler.postDelayed(this, mSpeed);
			}
		}
	};
	
	/** Edit Text containing value */
	private EditText oUiText;
	
	/** true if a pending change exists on oUiText */
	private boolean oUiTextChanged = false;
	
	/** number input filter */
	private InputFilter oNumberInputFilter;
	
	/** value displayed in the picker */
	private String[] mDisplayedValues;
	
	/** picker minimum value */ 
	private int mStart = DEFAULT_MIN;
	
	/** picker maximum value */
	private int mEnd   = DEFAULT_MAX;
	
	/** currently displayed value */
	private int mCurrent;
	
	/** current scrolling speed */
	private long mSpeed = DEFAULT_SPEED;
	
	/** true if the picker value can be incremented */
	private boolean bCanIncrement;
	
	/** true if the picker value can be decremented */
	private boolean bCanDecrement;
	
	/** authorized characters array */
	protected static final char[] DIGIT_CHARACTERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	
	/** button used to increment the value */
	private MMNumberPickerButton oUiIncrementButton;
	
	/** button used to decrement the value */
	private MMNumberPickerButton oUiDecrementButton;
	
	/**
	 * Constructs a MMNumberPicker
	 * @param p_oContext context used
	 */
	public MMNumberPicker(Context p_oContext) {
		this(p_oContext, null);

	}
	
	/**
	 * Construct a MMNumberPicker
	 * @param p_oContext context to use
	 * @param p_oAttrs xml attributes
	 */
	public MMNumberPicker(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Integer.class);
		if(!isInEditMode()) {

			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			LayoutInflater oInflater = LayoutInflater.from(this.getContext());

			// selon l'orientation désirée on inflate le XML différent
			String sOrientation = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "orientation") ;
			ApplicationR oXmlLayout = AndroidApplicationR.fwk_number_picker ;
			if ( sOrientation != null && sOrientation.equalsIgnoreCase("horizontal")){
				oXmlLayout = AndroidApplicationR.fwk_numberhorizontal_picker ; 
			}
			oInflater.inflate(oApplication.getAndroidIdByRKey(oXmlLayout), this);
			mHandler = new Handler(Looper.getMainLooper()); // sur thread principal

			oNumberInputFilter = new NumberRangeKeyListener();
			oUiIncrementButton = (MMNumberPickerButton) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__increment__button));
			oUiIncrementButton.setNumberPicker(this);
			oUiIncrementButton.setOnClickListener(this);
			oUiIncrementButton.setOnLongClickListener(this);
			oUiDecrementButton = (MMNumberPickerButton) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__decrement__button));
			oUiDecrementButton.setNumberPicker(this);
			oUiDecrementButton.setOnClickListener(this);
			oUiDecrementButton.setOnLongClickListener(this);

			//oUiLabel = (MMTextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__number__label));
			oUiText = (EditText) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__number__edit));
			InputFilter oInputFilter = new NumberPickerInputFilter();
			oUiText.setFilters(new InputFilter[] { oInputFilter });
			oUiText.addTextChangedListener(this);
			
			if (!isEnabled()) {
				setEnabled(false);
			}
			this.defineParameters(p_oAttrs);
			validator = BeanLoader.getInstance().getBean( IntegerFormFieldValidator.class );
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
	 * called when the inflater has finished the job before setting value
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode() && validator != null) {
			validator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes() , (BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration() );
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.view.View#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean p_bEnabled) {
		super.setEnabled(p_bEnabled);
		oUiIncrementButton.setEnabled(p_bEnabled);
		oUiDecrementButton.setEnabled(p_bEnabled);
		oUiText.setEnabled(p_bEnabled);
		if (oUiText.isEnabled()) {
			oUiText.setTextColor(Color.BLACK);
		} else {
			oUiText.setTextColor(Color.WHITE);
		}
	}

	/**
	 * Get view for text
	 * @return the edit text view
	 */
	public EditText getUiText() {
		return oUiText;
	}
	
	/**
	 * Called on click on a view
	 * @param p_oView the clicked view
	 */
	@Override
	public void onClick(View p_oView){
		if (!oUiText.hasFocus()){
			oUiText.requestFocus();
		}
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		if ( oUiTextChanged ){
			//mPrevious = mCurrent;
			String sValue = String.valueOf(oUiText.getText()).trim().replace(" ", "");
			if (sValue.length() > 0 ){
				try {
					mCurrent = Integer.valueOf(sValue);
				} catch (NumberFormatException oOException) {
					mCurrent = 0 ;
				}
			} else {
				mCurrent = 0 ;
			}
			oUiTextChanged = false;
		}
		// now perform the increment/decrement
		if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__increment__button) == p_oView.getId()) {
			this.changeCurrent(mCurrent + smallDelta);
		} else if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__decrement__button) == p_oView.getId()) {
			this.changeCurrent(mCurrent - smallDelta);
		}
		// on change la valeur
		if (aivDelegate!=null) {
			this.aivFwkDelegate.changed();
		}
	}
	
	/**
	 * Changes the current value
	 * @param p_iNewCurrent the new value
	 */
	public void changeCurrent(int p_iNewCurrent) {
		int iNewCurrent = p_iNewCurrent ;
		// Wrap around the values if we go past the start or end
		if (p_iNewCurrent > mEnd) {
			iNewCurrent = mStart;
		} else if (p_iNewCurrent < mStart) {
			iNewCurrent = mEnd;
		}
		//mPrevious = mCurrent;
		mCurrent = iNewCurrent;
		//notifyChange();
		updateView();
	}
	
	/**
	 * update the view of the component
	 */
	protected void updateView() {
		/*
		 * If we don't have displayed values then use the current number else find the correct value in the displayed values for the current
		 * number.
		 */
		
		if (mDisplayedValues == null) {
			if (!this.aivDelegate.isWritingData()) {
				this.aivDelegate.configurationSetValue(Integer.valueOf(mCurrent));
			}
		} else {
			oUiText.setText(mDisplayedValues[mCurrent - mStart]);
		}
		oUiText.setSelection(oUiText.getText().length());
		this.aivFwkDelegate.changed();
	}
	
	/**
	 * We start the long click here but rely on the NumberPickerButton to inform us when the long click has ended.
	 * @param p_oView the long clicked view
	 * @return true
	 */
	@Override
	public boolean onLongClick(View p_oView) {
		/*
		 * The text view may still have focus so clear it's focus which will trigger the on focus changed and any typed values to be pulled.
		 */
		oUiText.clearFocus();
		
		if ( oUiTextChanged ){
			//mPrevious = mCurrent;
			mCurrent = Integer.valueOf(String.valueOf(oUiText.getText()));
			oUiTextChanged = false;
		}
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__increment__button) == p_oView.getId()) {
			bCanIncrement = true;
			mHandler.post(mRunnable);
		} else if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__decrement__button) == p_oView.getId()) {
			bCanDecrement = true;
			mHandler.post(mRunnable);
		}
		return true;
	}
	
	/**
	 * Cancels the increment
	 */
	public void cancelIncrement() {
		bCanIncrement = false;
	}
	
	/**
	 * Cancels the decrement
	 */
	public void cancelDecrement() {
		bCanDecrement = false;
	}

	/**
	 * Defines the number picker input filter
	 */
	private class NumberPickerInputFilter implements InputFilter {
		@Override
		public CharSequence filter(CharSequence p_sSource, int p_iStart, int p_iEnd, Spanned p_oDest, int p_oDstart, int p_oDend) {
			if (mDisplayedValues == null) {
				return oNumberInputFilter.filter(p_sSource, p_iStart, p_iEnd, p_oDest, p_oDstart, p_oDend);
			}
			CharSequence oFiltered = p_sSource.subSequence(p_iStart, p_iEnd);
			String sResult = new StringBuilder( p_oDest.subSequence(0, p_oDstart) ).append(oFiltered)
					.append(p_oDest.subSequence(p_oDend, p_oDest.length())).toString().toLowerCase(Locale.getDefault());
			CharSequence r_sResult = "" ;
			for (String sValue : mDisplayedValues) {
				sValue = sValue.toLowerCase(Locale.getDefault());
				if (sValue.startsWith(sResult)) {
					r_sResult = oFiltered;
					break ;
				}
			}
			return r_sResult;
		}
	}
	
	/**
	 * Key listener to cancel key on out of range values
	 *
	 */
	private class NumberRangeKeyListener extends NumberKeyListener {
		@Override
		public int getInputType() {
			return InputType.TYPE_CLASS_NUMBER;
		}
		
		@Override
		protected char[] getAcceptedChars() {
			return DIGIT_CHARACTERS;
		}
		
		@Override
		public CharSequence filter(CharSequence p_oSource, int p_oStart, int p_oEnd, Spanned p_oDest, int p_iStart, int p_iEnd) {
			CharSequence sFiltered = super.filter(p_oSource, p_oStart, p_oEnd, p_oDest, p_iStart, p_iEnd);
			if (sFiltered == null) {
				sFiltered = p_oSource.subSequence(p_oStart, p_oEnd);
			}
			String sResult = new StringBuilder( p_oDest.subSequence(0, p_iStart) ).append( sFiltered )
					.append( p_oDest.subSequence(p_iEnd, p_oDest.length())).toString().toLowerCase(Locale.getDefault());

			if ("".equals(sResult)) {
				return sResult;
			}
			int iVal = getSelectedPos(sResult);
			/*
			 * Ensure the user can't type in a value greater than the max allowed. We have to allow less than min as the user might want to
			 * delete some numbers and then type a new number.
			 */
			CharSequence r_sResult = ""; 
			if (iVal <= mEnd) {
				r_sResult = sFiltered ;
			}
			return r_sResult ;
		}
	}
	
	/**
	 * Get the selected position from parameter text
	 * @param p_sText the text to find position
	 * @return the position of parameter text
	 */
	private int getSelectedPos(String p_sText) {
		int r_iPos ;
		if (mDisplayedValues == null) {
			r_iPos = Integer.parseInt(p_sText);
		} else {
			for (int iNdex = 0; iNdex < mDisplayedValues.length; iNdex++) {
				/* Don't force the user to type in jan when ja will do */
				if (mDisplayedValues[iNdex].toLowerCase(Locale.getDefault()).startsWith(p_sText)) {
					return mStart + iNdex;
				}
			}
			/*
			 * The user might have typed in a number into the month field i.e. 10 instead of OCT so support that too.
			 */
			try {
				r_iPos = Integer.parseInt(p_sText);
			} catch (NumberFormatException e) {
				/* Ignore as if it's not a number we don't care */
				Log.d("MMNumberPicker", p_sText+" pas un entier");
				r_iPos = mStart ; 
			}
		}		
		return r_iPos;
	}
	
	/**
	 * Get current selected value
	 * @return the current value.
	 */
	public int getCurrent() {
		return mCurrent;
	}

	/**
	 * Set the maximal and minimal value for this picker
	 * <p>
	 * Those value are store in component configuration
	 * </p>
	 */
	public void retrieveMinMaxValues(){
		Integer oMinValue = validator.getMinValue((BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration()) ;
		if ( oMinValue != null){
			mStart  = oMinValue.intValue() ;
		} 
		Integer oMaxValue = validator.getMaxValue((BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration()) ;
		if ( oMaxValue != null){
			mEnd  = oMaxValue.intValue() ;
		}
	}

	/**
	 * sets the maximum authorized value
	 * @param p_iEnd maximum value 
	 */
	public void setMaximalValue(int p_iEnd) {
		this.mEnd = p_iEnd;
	}
	
	/**
	 * sets the minimum authorized value
	 * @param p_iStart minimum value 
	 */	
	public void setMinimalValue(int p_iStart) {
		this.mStart = p_iStart;
	}
	
	/**
	 * sets the variation on click
	 * @param p_iSmallDelta variation 
	 */
	public void setSmallDelta(int p_iSmallDelta) {
		this.smallDelta = p_iSmallDelta;
	}
	
	/**
	 * sets the variation on long click
	 * @param p_iBigDelta variation 
	 */
	public void setBigDelta(int p_iBigDelta) {
		this.bigDelta = p_iBigDelta;
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
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		return this.oUiText;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		if (p_bEnable) {
			oUiText.setEnabled(true);
			oUiIncrementButton.getComponentDelegate().configurationEnabledComponent();
			oUiDecrementButton.getComponentDelegate().configurationEnabledComponent();
			oUiIncrementButton.setOnClickListener(this);
			oUiIncrementButton.setOnLongClickListener(this);
			oUiIncrementButton.setNumberPicker(this);
			oUiDecrementButton.setOnClickListener(this);
			oUiDecrementButton.setOnLongClickListener(this);
			oUiDecrementButton.setNumberPicker(this);
		} else {
			oUiText.setEnabled(false);
			oUiIncrementButton.getComponentDelegate().configurationDisabledComponent();
			oUiDecrementButton.getComponentDelegate().configurationDisabledComponent();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Integer configurationGetValue() {
		Integer lValue ;
		try {
			lValue = Integer.valueOf(oUiText.getText().toString().trim() );
		} catch (NumberFormatException e) {
			lValue = null ; //par défaut si erreur
		}
		return lValue;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Integer p_oObjectToSet) {
		this.retrieveMinMaxValues();
		if( p_oObjectToSet != null) {
			oUiText.setText(String.valueOf(p_oObjectToSet));
			this.changeCurrent(((Integer) p_oObjectToSet).intValue());
		} else{
			oUiText.setText("");
		}
		// définit le nombre max de caractères
		oUiText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(String.valueOf(mStart).length()) });
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		try {
			Integer.valueOf(oUiText.getText().toString().trim() );
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		String sValue = String.valueOf(oUiText.getText()).trim().replace(" ", "");
		if (sValue.length() > 0 ){
			try {
				mCurrent = Integer.valueOf(sValue);
			} catch (NumberFormatException oOException) {
				mCurrent = 0 ;
			}
		} else {
			mCurrent = 0 ;
		}
		r_oBundle.putInt("edittext", mCurrent);

		return r_oBundle;
	}
	
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		changeCurrent(r_oBundle.getInt("edittext"));
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// nothing to do
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// nothing to do
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		this.oUiTextChanged = true;
	}
}
