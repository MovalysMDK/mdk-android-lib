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
import android.text.method.NumberKeyListener;
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
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDurationPickerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DurationFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.DurationUtils;

/**
 * This view group represents a date text view, its label and its button to use
 * the datePickerDialog,
 * <ul>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * <li>use "fwk_component_duration_edittext" xml layout</li>
 * <li>Use parameterized "DurationFormFieldValidator" validator</li>
 * <li>Open "MMDurationPickerDialog" to edit a duration</li>
 * </ul>
 */
public class MMDurationEditText extends AbstractMMLinearLayout<Long> implements MMComplexeComponent, OnClickListener, OnDismissListener, TextWatcher,
	ComponentValidator, ComponentError, ComponentEnable, 
	ComponentReadableWrapper<Long>, ComponentWritableWrapper<Long> {
	/** Objet de validation de la valeur selon les paramètres liés au type de données */
	private DurationFormFieldValidator validator = BeanLoader.getInstance().getBean(DurationFormFieldValidator.class) ;
	/** the date field */
	private EditText oUiDurationEdit;
	/** the date button */
	private ImageButton oUiButton;
	/** The dialog to show, to edit the value */
	private MMDurationPickerDialogFragment dialog = null;

	/** The key used to retain the current value of the component during orientation changes */
	private static final String DURATION_PICKER_GROUP_VALUE_KEY = "durationPickerGroupValueKey";

	/**
	 * Component request code
	 */
	private int requestCode = -1;
	
	/**
	 * Construct a MMDurationEditText
	 * @param p_oContext           the context
	 */
	public MMDurationEditText(Context p_oContext) {
		super(p_oContext, Long.class);
		this.inflateMyLayout() ;
		this.defineParameters(null);
	}
	/**
	 * Construct a MMDurationEditText
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMDurationEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Long.class);
		if(!isInEditMode()) {
			this.inflateMyLayout();
			this.defineParameters(p_oAttrs);
		}
	}

	/**
	 * Inflate component layout
	 */
	private void inflateMyLayout(){
		AndroidApplication app = AndroidApplication.getInstance();
		
		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(app.getAndroidIdByRKey(AndroidApplicationR.fwk_component_duration_edittext),this, true);
		oUiDurationEdit = (EditText) this.findViewById(app.getAndroidIdByRKey(AndroidApplicationR.component_duration__duration__edit));
		oUiDurationEdit.addTextChangedListener(this);
		oUiButton = (ImageButton) this.findViewById(app.getAndroidIdByRKey(AndroidApplicationR.component_duration__button));
		oUiButton.setOnClickListener(this);

	}

	/**
	 * Récupère et conserve les données du paramètrage XML du composant
	 * Spécifie le type de données lié et son validateur
	 * @param p_oAttrs paramétrage XML du composant
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivFwkDelegate.defineParameters(p_oAttrs);
		if (p_oAttrs == null) {
			this.setHoursDigitsCount(DurationDigitsInputFilter.DEFAULT_HOURS_COUNT);
		} else {
			this.setHoursDigitsCount(p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "hoursCount", DurationDigitsInputFilter.DEFAULT_HOURS_COUNT));
		}
	}
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			if (validator!= null){
				validator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes() , (BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration() );
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDismiss(DialogInterface p_oDimissedDialog) {
		this.requestFocusFromTouch();
		Long newDialogValue = this.dialog.getTemporaryDialogValue();
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
		this.aivFwkDelegate.changed();
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
	 * @param p_oClickedView
	 *            the view that was clicked
	 */
	@Override
	public void onClick(View p_oClickedView) {

		this.dialog = (MMDurationPickerDialogFragment) createDialogFragment(null);
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
	 * Définit le nombre de digits des heures.
	 * @param p_iHoursCount Nombre de digits des heures.
	 */
	public final void setHoursDigitsCount(final int p_iHoursCount) {
		this.oUiDurationEdit.setFilters(new InputFilter[] { new DurationDigitsInputFilter(p_iHoursCount) });
	}

	/**
	 * <p>Filtre sur les durées.</p>
	 *
	 *
	 */
	private static class DurationDigitsInputFilter extends NumberKeyListener {
		/**
		 * Nombre 0 in ASCII
		 */
		private static final int ASCII_0 = 48;
		/**
		 * Nombre 5 in ASCII
		 */
		private static final int ASCII_5 = 53;
		
		/**
		 * Nombre de digits pour les heures
		 */
		private static final int DEFAULT_HOURS_COUNT = 3;
		/**
		 * Nombre de digits pour les minutes
		 */
		private static final int DEFAULT_MINUTES_COUNT = 2;

		/** Nombre de digits pour les heures */
		private int hoursCount = DEFAULT_HOURS_COUNT;

		/** Nombre de digits pour les minutes */
		private int minutesCount = DEFAULT_MINUTES_COUNT;

		/**
		 * Construit un nouveau filtre.
		 * @param p_iHoursCount le nombre de digits des heures
		 */
		public DurationDigitsInputFilter(int p_iHoursCount) {
			super();
			this.hoursCount = p_iHoursCount;
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

			int iMinutesCount = 0;
			int iHoursCount = 0;
			int iSeparatorIndex = -1;
			boolean bValidMinutes = true;

			for (int i=0; i < p_iDstart; i++) {
				if (p_sDest.charAt(i) == ':') {
					iSeparatorIndex = i;
				}
				else if (iSeparatorIndex >= 0) {
					iMinutesCount++;
				} else {
					iHoursCount++;
				}
			}

			for (int i=iStart; i < iEnd; i++) {
				if (iSeparatorIndex >= 0) {
					iMinutesCount++;
					if (iMinutesCount == 1) {
						// dizaine des minutes
						bValidMinutes = r_oSource.charAt(i) >= ASCII_0 && r_oSource.charAt(i) <= ASCII_5;
					}
				}
				else if (r_oSource.charAt(i) == ':') {
					iSeparatorIndex = i;
				} else {
					iHoursCount++;
				}
			}

			for (int i=p_iDend; i < p_sDest.length(); i++) {
				if (iSeparatorIndex >= 0) {
					iMinutesCount++;
				}
				else if (p_sDest.charAt(i) == ':') {
					iSeparatorIndex = i;
				} else {
					iHoursCount++;
				}
			}
			if (iMinutesCount > this.minutesCount || iHoursCount > this.hoursCount || !bValidMinutes) {
				r_oSource = StringUtils.EMPTY;
			}
			return r_oSource;
		}

		/**
		 * {@inheritDoc}
		 * @see android.text.method.KeyListener#getInputType()
		 */
		@Override
		public int getInputType() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 * @see android.text.method.NumberKeyListener#getAcceptedChars()
		 */
		@Override
		protected char[] getAcceptedChars() {
			return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':' };
		}
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return MMDurationPickerDialogFragment.newInstance(this);
	}
	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments) {
		if (p_oDialog instanceof MMDurationPickerDialogFragment) {
			((MMDurationPickerDialogFragment) p_oDialog).setTemporaryDialogValue(this.aivDelegate.configurationGetValue());
		}
	}

	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode,
			Intent p_oIntent) {
		// Nothing to do

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {

		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(DURATION_PICKER_GROUP_VALUE_KEY, this.oUiDurationEdit.getText().toString());
		r_oBundle.putInt("requestCode", this.requestCode);

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

		String retainValue = r_oBundle.getString(DURATION_PICKER_GROUP_VALUE_KEY);
		this.oUiDurationEdit.setText(retainValue);
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
		return this.validator;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		return this.oUiDurationEdit;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		oUiDurationEdit.setEnabled(p_bEnable);
		oUiButton.setEnabled(p_bEnable);
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Long configurationGetValue() {
		String sValue = this.oUiDurationEdit.getText().toString();
		if (sValue != null) {
			sValue = sValue.trim();
			if (sValue.length() == 0) {
				sValue = null;
			} else {
				return DurationUtils.convertTimeToDurationInMinutes(sValue);
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Long p_oObjectToSet) {
		if (p_oObjectToSet==null || p_oObjectToSet.equals(-1L)) {
			this.oUiDurationEdit.setText(StringUtils.EMPTY);
			this.aivDelegate.setFilled(false);
		} else {
			this.oUiDurationEdit.setText(DurationUtils.convertMinutesToDuration((Long) p_oObjectToSet));
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
		return this.oUiDurationEdit.getText().toString().length() >0;
	}
	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
	
}
