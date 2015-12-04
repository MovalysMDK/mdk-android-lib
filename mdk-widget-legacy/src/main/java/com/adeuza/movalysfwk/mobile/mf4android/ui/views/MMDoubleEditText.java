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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDoublePickerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DoubleFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * NE PAS UTILISER COMPOSANT BUGGÉ: -0.5 NON SAISISSABLE AVEC LES PICKER, SAISIR 1 ET 10 DANS LE PICKER DES DECIMALES ABOUTIT AU MEME RÉSULTAT.
 * This view group represents a double text view, its label and its button to use
 * the doublePickerDialog,
 * <ul>
 * <li>inflate layout named "fwk_component_double_edittext"</li>
 * <li>use a DoubleFormFieldValidator validator</li>
 * <li>open a "MMDoublePickerDialog" to choose a float data</li>
 * </ul>
 */
public class MMDoubleEditText extends AbstractMMLinearLayout<Double>
	implements OnClickListener, OnDismissListener, TextWatcher, MMComplexeComponent, 
	ComponentValidator, ComponentError, ComponentEnable, 
	ComponentReadableWrapper<Double>, ComponentWritableWrapper<Double> {

	/** id of the edit date dialog displayed the date picker */
	public static final int DOUBLE_DIALOG_ID = 1;
	
	/** data validator */
	private DoubleFormFieldValidator oValidator = BeanLoader.getInstance().getBean(DoubleFormFieldValidator.class) ;
	
	/** the date field */
	private EditText oUiDoubleEdit;
	
	/** the date button */
	private ImageButton oUiButton;
	
	/** the dialog to edit this component */
	private MMDoublePickerDialogFragment dialog;
	
	/** the key used to retain the value during configuration changes */
	private static final String DOUBLE_EDIT_TEXT_VALUE_KEY = "doubleEditTextValueKey";

	/** Component request code */
	private int requestCode = -1;
	
	/**
	 * Construct a MMDoubleEditText
	 * @param p_oContext  the context
	 */
	public MMDoubleEditText(Context p_oContext) {
		super(p_oContext, Double.class);
		if(!isInEditMode()) {
			this.inflateMyLayout() ;
			this.defineParameters(null);
		}
	}
	
	/**
	 * Construct a MMDoubleEditText
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMDoubleEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Double.class);
		if(!isInEditMode()) {
			this.inflateMyLayout() ;
			this.defineParameters(p_oAttrs);
		}
	}

	/**
	 * Inflate component layout
	 */
	private void inflateMyLayout(){
		AndroidApplication oApplication = AndroidApplication.getInstance();
		
		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_double_edittext),this, true);
		oUiDoubleEdit = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_double__double__edit));
		if (this.oUiDoubleEdit != null) { 
			oUiDoubleEdit.addTextChangedListener(this);
		}
		oUiButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_double__button));
		if (this.oUiButton != null) {
			oUiButton.setOnClickListener(this);
		}
	}
	
	/**
	 * Sets the component from given the xml attributes.
	 * @param p_oAttrs xml attributes to set
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivFwkDelegate.defineParameters(p_oAttrs);
		if (p_oAttrs == null) {
			this.setDecimalCount(DecimalDigitsInputFilter.DEFAULT_DECIMAL_COUNT);
		} else {
			this.setDecimalCount(p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "decimalCount", DecimalDigitsInputFilter.DEFAULT_DECIMAL_COUNT));
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			if (oValidator!= null){
				oValidator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes() , (BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration() );
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDismiss(DialogInterface p_oDimissedDialog) {
		this.requestFocusFromTouch();
		Double newDialogValue = this.dialog.getTemporaryDialogValue();
		if(newDialogValue != null) {
			this.aivDelegate.configurationSetValue(newDialogValue);
			this.aivFwkDelegate.changed();
		}
	}

	/**
	 * after text have been changed
	 */
	@Override
	public void afterTextChanged(Editable p_oText) {
		if (this.aivDelegate != null && !this.aivDelegate.isWritingData()) {
			this.aivFwkDelegate.changed();
			this.aivDelegate.validate((BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration(), null, new StringBuilder());
		}
	}

	/**
	 * before text changed
	 */
	@Override
	public void beforeTextChanged(CharSequence p_oText, int p_iStart, int p_iCount,
			int p_iAfter) {
		// nothing
	}

	/**
	 * each time text changed
	 */
	@Override
	public void onTextChanged(CharSequence p_oText, int p_iStart, int p_iBefore, int p_iCount) {
		// nothing
	}
	
	/**
	 * On click listener to deal with the click on the date and time button
	 * @param p_oClickedView the view that was clicked
	 */
	@Override
	public void onClick(View p_oClickedView) {
		this.dialog = (MMDoublePickerDialogFragment) createDialogFragment(null);
		prepareDialogFragment(this.dialog, null);
		this.dialog.show(getFragmentActivityContext().getSupportFragmentManager(), this.dialog.getFragmentDialogTag());
	}

	/**
	 * Returns the fragmentActivit context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}

	/**
	 * Defines the number of digits to display
	 * @param p_iDecimalCount the number of digits
	 */
	public final void setDecimalCount(final int p_iDecimalCount) {
		this.oUiDoubleEdit.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(p_iDecimalCount) });
	}

	/**
	 * Filters digits on input values 
	 */
	private static class DecimalDigitsInputFilter extends DigitsKeyListener {
		
		/** Default number of digits */
		private static final int DEFAULT_DECIMAL_COUNT = 2;

		/** current number of digits */
		private int decimalCount = 2;

		/**
		 * Constructs a DecimalDigitsInputFilter
		 * @param p_iDecimalCount the number of digits to use
		 */
		public DecimalDigitsInputFilter(int p_iDecimalCount) {
			super(true, true);
			this.decimalCount = p_iDecimalCount;
		}

		/**
		 * Constructs a DecimalDigitsInputFilter
		 */
		@SuppressWarnings("unused")
		public DecimalDigitsInputFilter() {
			this(DEFAULT_DECIMAL_COUNT);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public CharSequence filter(CharSequence p_sSource, int p_iStart, int p_iEnd,
				Spanned p_sDest, int p_iDstart, int p_iDend) {

			CharSequence r_oSource = super.filter(p_sSource, p_iStart, p_iEnd, p_sDest, p_iDstart, p_iDend);

			// if changed, replace the source
			int iStart	= p_iStart;
			int iEnd	= p_iEnd; 
			if (r_oSource == null) {
				r_oSource = p_sSource;
			}
			else {
				iStart	= 0;
				iEnd	= r_oSource.length();
			}

			StringBuilder oIntPart = new StringBuilder();
			int iDecimalCount = 0;
			int iDecimalSeparatorIndex = -1;

			for (int i=0; i < p_iDstart; i++) {
				if (p_sDest.charAt(i) == '.') {
					iDecimalSeparatorIndex = i;
				}
				else if (iDecimalSeparatorIndex >= 0) {
					iDecimalCount++;
				}	
				else {
					oIntPart.append(p_sDest.charAt(i));
				}
			}

			for (int i=iStart; i < iEnd; i++) {
				if (iDecimalSeparatorIndex >= 0) {
					iDecimalCount++;
				}
				else if (r_oSource.charAt(i) == '.') {
					iDecimalSeparatorIndex = i;
				}
				else {
					oIntPart.append(r_oSource.charAt(i));
				}
			}

			for (int i=p_iDend; i < p_sDest.length(); i++) {
				if (iDecimalSeparatorIndex >= 0) {
					iDecimalCount++;
				}
				else if (p_sDest.charAt(i) == '.') {
					iDecimalSeparatorIndex = i;
				}
				else {
					oIntPart.append(p_sDest.charAt(i));
				}
			}

			double dIntPart;
			if( oIntPart.length() == 0 || oIntPart.length() == 1 && oIntPart.charAt(0) == '-'){
				dIntPart = 0D;
			}
			else {
				dIntPart = Double.parseDouble(oIntPart.toString());
			}
			if (iDecimalCount > this.decimalCount || dIntPart > Integer.MAX_VALUE || dIntPart < Integer.MIN_VALUE) {
				r_oSource = StringUtils.EMPTY;
			}

			return r_oSource;
		}
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return MMDoublePickerDialogFragment.newInstance(this);
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments) {
		if (p_oDialog instanceof MMDoublePickerDialogFragment) {
			((MMDoublePickerDialogFragment) p_oDialog).setTemporaryDialogValue(this.aivDelegate.configurationGetValue());
		}
	}

	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode, Intent p_oIntent) {
		// Nothing to do
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(DOUBLE_EDIT_TEXT_VALUE_KEY, this.oUiDoubleEdit.getText().toString());
		r_oBundle.putInt("requestCode", this.requestCode);

		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		String retainValue = r_oBundle.getString(DOUBLE_EDIT_TEXT_VALUE_KEY);
		this.oUiDoubleEdit.setText(retainValue);
		this.requestCode = r_oBundle.getInt("requestCode");
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
		return this.oValidator;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		return this.oUiDoubleEdit;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		oUiDoubleEdit.setEnabled(p_bEnable);
		oUiButton.setEnabled(p_bEnable);
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Double configurationGetValue() {
		String sValue = this.oUiDoubleEdit.getText().toString();
		if (sValue.length() == 0) {
			sValue = null;
		}
		if (sValue != null) {
			sValue = sValue.trim();
			if (sValue.length() > 0) {
				return Double.valueOf(sValue);
			}
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Double p_oObjectToSet) {
		if (p_oObjectToSet==null || p_oObjectToSet.equals(-1D)) {
			oUiDoubleEdit.setText(StringUtils.EMPTY);
			this.aivDelegate.setFilled(false);
		} else {
			DecimalFormatSymbols oSymbols = new DecimalFormatSymbols();
			oSymbols.setDecimalSeparator('.');
			DecimalFormat oFormater = new DecimalFormat("0.0", oSymbols);
			oUiDoubleEdit.setText(oFormater.format(p_oObjectToSet));
			this.aivDelegate.setFilled(true);
			this.getComponentFwkDelegate().resetChanged();
		}
	}
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.oUiDoubleEdit.getText().toString().length() > 0;
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}
