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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDatePickerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMTimePickerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.utils.MMCalendar;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IDateFormatValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ValidableComponent;

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
 * <li>add date_format attribute to declare the full / long / medium / short
 * format for the date</li>
 * <li>add time_format attribute to declare the full / long / medium / short
 * format for the time</li>
 * </ul>
 */
public class MMDateTimeEditText extends AbstractMMLinearLayout<Long>

implements OnClickListener, OnDismissListener, ValidableComponent,
MMComplexeComponent, TextWatcher, ComponentValidator, ComponentError, ComponentEnable, 
ComponentReadableWrapper<Long>, ComponentWritableWrapper<Long>, ComponentBindDestroy {
	
	/** Validation object for the input values */
	private IDateFormatValidator oValidator;
	
	/** map for the xml attributes */
	private Map<ConfigurableVisualComponent.Attribute, String> attributes = new HashMap<>();

	/** Date only mode */
	public static final String DATE_MODE = "date";
	
	/** Date Time mode */
	public static final String DATETIME_MODE = "datetime";
	
	/** Time only mode */
	public static final String TIME_MODE = "time";
	
	/** tag for the button clicked id */
	public static final String BUTTON_CLICK_ID_TAG = "buttonClickedIdTag";
	
	/** tag used to retain the current date */
	public static final String DATE_STRING_TAG = "dateStringTag";

	/** date field */
	private EditText oUiDate;
	
	/** date button */
	private ImageButton oUiDateButton;
	
	/** time button */
	private ImageButton oUiTimeButton;
	
	/** formatter for text representation */
	private SimpleDateFormat dateTimeFormater;
	
	/** date used by the component */
	private Calendar displayedCalendarDate = Calendar.getInstance();
	
	/** date for the dialog opened */
	private Calendar temporaryDialogValue = Calendar.getInstance();

	/** id of the edit time dialog displayed the time picker */
	public static final int TIME_DIALOG_ID = 1000;
	
	/** id of the edit date dialog displayed the date picker */
	public static final int DATE_DIALOG_ID = 1001;
	
	/** maximum year for the date picker */
	private static final int MAX_YEAR_OF_ANDROID_PICKER = 2100;
	
	/** minimum year for the date picker */
	private static final int MIN_YEAR_OF_ANDROID_PICKER = 1900;

	/** Date picking fragment name */
	private static final String DATE_DIALOG_PICKER_FRAGMENT_NAME = "MMDatePickerDialogFragment";
	
	/** Time picking fragment name */
	private static final String TIME_DIALOG_PICKER_FRAGMENT_NAME = "MMTimePickerDialogFragment";
	
	/** dialog fragments package name */
	private static final String DATE_TIME_DIALOG_PACKAGE_NAME = "com.adeuza.movalysfwk.mobile.mf4android.ui.dialog";

	/** Default Format Date Picker for min-date and max-date  */
	public static final String DEFAULT_DATE_PICKER_FORMAT = "dd/MM/yyyy";

	/** component minimal value */
	private long minValue = 0;

	/** component maximal value */
	private long maxValue = 0;
	
	/** Component request code */
	private int requestCode = -1;
	
	/**
	 * Construct a MMDateTimeEditText
	 * 
	 * @param p_oContext the context
	 */
	public MMDateTimeEditText(Context p_oContext) {
		super(p_oContext, Long.class);
		if (!isInEditMode()) {
			this.inflateMyLayout();
			this.defineParameters(null);
		}
	}

	/**
	 * Construct a MMDateTimeEditText
	 * 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMDateTimeEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Long.class);
		if (!isInEditMode()) {
			this.inflateMyLayout();
			this.linkChildrens(p_oAttrs);
			this.defineParameters(p_oAttrs);
		}
	}

	/**
	 * Inflates the layout in component
	 */
	private final void inflateMyLayout(){
		this.layoutGravity();
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		AndroidApplication oApplication = AndroidApplication.getInstance();
		oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_datetime_edittext),this, true);
		oUiDate = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__datetime__edit));
		oUiDateButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__date__button));
		oUiTimeButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__time__button));
		oUiDate.addTextChangedListener(this);
	}

	private void layoutGravity() {
		this.setGravity(Gravity.CENTER_VERTICAL);
	}

	/**
	 * link the children views with custom attributes
	 * 
	 * @param p_oAttrs attributes of XML Component MMDateTimeViewGroup
	 */
	private final void linkChildrens(AttributeSet p_oAttrs) {
		AndroidApplication oApplication = AndroidApplication.getInstance();

		String sMode = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "mode");
		if (DATE_MODE.equals(sMode)) {
			this.setDataType(AbstractEntityFieldConfiguration.DataType.TYPE_DATE);
		} else if (DATETIME_MODE.equals(sMode)) {
			this.setDataType(AbstractEntityFieldConfiguration.DataType.TYPE_DATETIME);
		} else if (TIME_MODE.equals(sMode)) {
			this.setDataType(AbstractEntityFieldConfiguration.DataType.TYPE_TIME);
		} else {
			this.setDataType(AbstractEntityFieldConfiguration.DataType.TYPE_DATETIME);
			oApplication.getLog().debug(
					this.getClass().getSimpleName(),
					StringUtils.concat(this.aivFwkDelegate.getConfiguration().getName()
							+ " mode not set, default datetime mode is used"));
		}
	}

	/**
	 * sets up the component with the given xml attributes
	 * 
	 * @param p_oAttrs xml attributes for the component
	 */
	private final void defineParameters(AttributeSet p_oAttrs) {
		AndroidApplication oApplication = AndroidApplication.getInstance();
		this.attributes = new HashMap<>();
		SimpleDateFormat sdfDatePicker = new SimpleDateFormat(DEFAULT_DATE_PICKER_FORMAT, Locale.FRENCH);
		
		if (p_oAttrs != null) {
			for (ConfigurableVisualComponent.Attribute oAttribute : ConfigurableVisualComponent.Attribute
					.values()) {
				String sValue = p_oAttrs.getAttributeValue(
						Application.MOVALYSXMLNS, oAttribute.getName());

				if (sValue != null) {
					if (oAttribute.getName().equals(ConfigurableVisualComponent.Attribute.MIN_DATE_ATTRIBUTE.getName())) {
						try {
							this.minValue = sdfDatePicker.parse(sValue).getTime();
							this.attributes.put(oAttribute, Long.toString(this.minValue));
						} catch (ParseException e) {
							this.minValue = 0;
				
							oApplication.getLog().debug(
									this.getClass().getSimpleName(),
									StringUtils.concat("Error parsing the date minValue ",
											this.aivFwkDelegate.getName()));
						}
					} else {
						if (oAttribute.getName().equals(ConfigurableVisualComponent.Attribute.MAX_DATE_ATTRIBUTE.getName())) {
							try {
								this.maxValue = sdfDatePicker.parse(sValue).getTime();
								this.attributes.put(oAttribute, Long.toString(this.maxValue));
							} catch (ParseException e) {
								this.maxValue = 0;
								
								oApplication.getLog().debug(
										this.getClass().getSimpleName(),
										StringUtils.concat("Error parsing the date maxValue ",
												this.aivFwkDelegate.getName()));
							}
						} else {
							this.attributes.put(oAttribute, sValue);
						}
					}
				}
			}
		}
		if (this.aivFwkDelegate.customFormatter() == null) {
			this.oUiDate.setFilters(new InputFilter[] { new DateTimeDigitsInputFilter() });
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			
			if (oValidator != null) {
				oValidator.addParametersToConfiguration(this.attributes, (BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration());
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.content.DialogInterface.OnDismissListener#onDismiss(android.content.DialogInterface)
	 */
	@Override
	public void onDismiss(DialogInterface p_oDialog) {
		// nothing to do
		if (oUiDate.getText().length() == 0) {
			displayedCalendarDate = Calendar.getInstance();
		}
		if (p_oDialog != null) {
			this.aivDelegate.configurationSetValue(temporaryDialogValue.getTimeInMillis());
			if(!this.aivDelegate.isWritingData() && this.aivFwkDelegate != null) {
				this.aivFwkDelegate.changed();
			}
		}
	}

	/**
	 * Getter of temporaryDialogValue
	 * 
	 * @return the value of temporaryDialogValue
	 */
	public Calendar getTemporaryDialogValue() {
		return temporaryDialogValue;
	}

	/**
	 * the Setter of temporaryDialogValue
	 * 
	 * @param p_oTemporaryDialogValue
	 *            the value to set in temporaryDialogValue
	 */
	public void setTemporaryDialogValue(Calendar p_oTemporaryDialogValue) {
		this.temporaryDialogValue = p_oTemporaryDialogValue;
	}

	/**
	 * set the date to the textView using the mode (date / datetime / time)
	 * found in the layout
	 */
	public void updateDisplay() {
		String oValueToSetInEditText = StringUtils.EMPTY;
		if (this.aivFwkDelegate != null) {
			if (this.aivFwkDelegate.customFormatter() != null) {
				oValueToSetInEditText = (String) this.aivFwkDelegate.customFormatter().format(displayedCalendarDate.getTimeInMillis());
			} else if (displayedCalendarDate != null) {
				oValueToSetInEditText = dateTimeFormater.format(displayedCalendarDate.getTime());
			}
			oUiDate.setText(oValueToSetInEditText);
			if(!this.aivDelegate.isWritingData()) {
				this.aivFwkDelegate.changed();
			}
		}
	}

	/**
	 * On click listener to deal with the click on the date and time button
	 * 
	 * @param p_oClickedView the view that was clicked
	 */
	@Override
	public void onClick(View p_oClickedView) {
		
		StringBuilder sMessageError = new StringBuilder();
		boolean isValid = this.aivDelegate.validate((BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration(), null, sMessageError);
		if(isValid) {
			this.aivDelegate.configurationUnsetError();
			this.aivFwkDelegate.changed();
		} else {
			this.aivDelegate.configurationSetError(sMessageError.toString());
		}

		Bundle oParameters = new Bundle();
		oParameters.putInt(BUTTON_CLICK_ID_TAG, p_oClickedView.getId());
		MMDialogFragment oDialog = createDialogFragment(oParameters);

		if (oDialog != null) {
			// Creating data for setting date in the dialogFragment
			Bundle oDialogFragmentArguments = new Bundle();
			oDialogFragmentArguments.putInt(MMCalendar.DATE_TAG_YEAR,
					temporaryDialogValue.get(Calendar.YEAR));
			oDialogFragmentArguments.putInt(MMCalendar.DATE_TAG_MONTH,
					temporaryDialogValue.get(Calendar.MONTH));
			oDialogFragmentArguments.putInt(MMCalendar.DATE_TAG_DAY,
					temporaryDialogValue.get(Calendar.DAY_OF_MONTH));
			oDialogFragmentArguments.putInt(MMCalendar.DATE_TAG_HOUR,
					temporaryDialogValue.get(Calendar.HOUR_OF_DAY));
			oDialogFragmentArguments.putInt(MMCalendar.DATE_TAG_MINUTE,
					temporaryDialogValue.get(Calendar.MINUTE));
			
			oDialogFragmentArguments.putLong(ConfigurableVisualComponent.Attribute.MIN_DATE_ATTRIBUTE.getName(),this.minValue);
			oDialogFragmentArguments.putLong(ConfigurableVisualComponent.Attribute.MAX_DATE_ATTRIBUTE.getName(),this.maxValue);
			
			// Setting the data to the dialogFragment
			prepareDialogFragment(oDialog, oDialogFragmentArguments);
	
			// Showing the dialogFragment
			oDialog.show(getFragmentActivityContext().getSupportFragmentManager(),
					oDialog.getFragmentDialogTag());
		}
	}

	/**
	 * Returns the fragmentActivity context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}


	/**
	 * Create a long from date in edit text
	 * @return the long corresponding to the date text
	 */
	private Long retrieveDateFromEditText() {
		AndroidApplication oApplication = AndroidApplication.getInstance();
		Long r_oValue = null;

		try {
			// Si la valeur du champ est nulle, le composant ne doit pas
			// renvoyer de valeur.
			String sValue = this.oUiDate.getText().toString();
			sValue = sValue.trim();
			if (sValue.length() > 0) {
				if (this.aivFwkDelegate != null && this.aivFwkDelegate.customFormatter() != null) {
					r_oValue = (Long) this.aivFwkDelegate.customFormatter().unformat(sValue);
				} else {
					displayedCalendarDate.setTime(this.dateTimeFormater.parse(sValue));
					r_oValue = displayedCalendarDate.getTimeInMillis();

				}
			} else {
				displayedCalendarDate = Calendar.getInstance();
			}
			
			
			// Compare Date Saisie et Date Calculée
			String oValueToSetInEditText = StringUtils.EMPTY;
			if (this.aivFwkDelegate.customFormatter() != null) {
				oValueToSetInEditText = (String) this.aivFwkDelegate.customFormatter().format(displayedCalendarDate.getTimeInMillis());
			} else if (displayedCalendarDate != null) {
				oValueToSetInEditText = dateTimeFormater.format(displayedCalendarDate.getTime());
			}
			
			if (!sValue.equals(oValueToSetInEditText)) {
				displayedCalendarDate = Calendar.getInstance();
				oApplication.getLog().debug(
						this.getClass().getSimpleName(),
						StringUtils.concat("Error parsing the date field for ",
								this.aivFwkDelegate.getName()));
			}

		} catch (ParseException e) {
			displayedCalendarDate = Calendar.getInstance();
			oApplication.getLog().debug(
					this.getClass().getSimpleName(),
					StringUtils.concat("Error parsing the date field for ",
							this.aivFwkDelegate.getName()));
		}
		return r_oValue;
	}

	/**
	 * Defines the data type of the component, and sets the validator accordingly
	 * 
	 * @param p_oType data type to set
	 */
	public void setDataType(AbstractEntityFieldConfiguration.DataType p_oType) {
		oValidator = (IDateFormatValidator) BeanLoader.getInstance().getBean(p_oType.getValidatorClass());
		oValidator.addParametersToConfiguration(this.attributes, 
				(BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration());
		dateTimeFormater = oValidator.getDateFormat(
				(BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration());
		
		switch (p_oType) {
		case TYPE_TIME:
			oUiDateButton.setVisibility(GONE);
			if (this.aivFwkDelegate.isEdit()) {
				oUiTimeButton.setVisibility(VISIBLE);
				oUiTimeButton.setOnClickListener(this);
				oUiDate.setInputType(InputType.TYPE_CLASS_DATETIME
						| InputType.TYPE_DATETIME_VARIATION_TIME);
			} else {
				oUiTimeButton.setVisibility(GONE);
			}
			break;

		case TYPE_DATE:
			if (this.aivFwkDelegate.isEdit()) {
				oUiDateButton.setVisibility(VISIBLE);
				oUiDateButton.setOnClickListener(this);
				oUiDate.setInputType(InputType.TYPE_CLASS_DATETIME
						| InputType.TYPE_DATETIME_VARIATION_DATE);
			} else {
				oUiDateButton.setVisibility(GONE);
			}
			oUiTimeButton.setVisibility(GONE);
			break;

		default:
			if (this.aivFwkDelegate.isEdit()) {
				oUiDateButton.setVisibility(VISIBLE);
				oUiTimeButton.setVisibility(VISIBLE);
				oUiDateButton.setOnClickListener(this);
				oUiTimeButton.setOnClickListener(this);
				oUiDate.setInputType(InputType.TYPE_CLASS_DATETIME
						| InputType.TYPE_DATETIME_VARIATION_NORMAL);
			} else {
				oUiDateButton.setVisibility(GONE);
				oUiTimeButton.setVisibility(GONE);
			}
			break;
		}
	}

	/**
	 * Filter on the datetime inputs
	 */
	private static class DateTimeDigitsInputFilter extends NumberKeyListener {

		/** digits number on hours */
		private int hoursCount = 2;

		/** digits number on minutes */
		private int minutesCount = 2;

		/**
		 * Constructs a DateTimeDigitsInputFilter
		 */
		public DateTimeDigitsInputFilter() {
			super();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public CharSequence filter(CharSequence p_sSource, int p_iStart,
				int p_iEnd, Spanned p_sDest, int p_iDstart, int p_iDend) {

			CharSequence r_oSource = super.filter(p_sSource, p_iStart, p_iEnd,
					p_sDest, p_iDstart, p_iDend);

			// if changed, replace the source
			int iStart = p_iStart;
			int iEnd = p_iEnd;
			if (r_oSource == null) {
				r_oSource = p_sSource;
			} else {
				iStart = 0;
				iEnd = r_oSource.length();
			}

			int iMinutesCount = 0;
			int iHoursCount = 0;
			int iSpaceSeparatorIndex = -1;
			int iPointsSeparatorIndex = -1;

			for (int i = 0; i < p_iDstart; i++) {
				if (Character.isWhitespace(p_sDest.charAt(i))) {
					iSpaceSeparatorIndex = i;
				} else if (p_sDest.charAt(i) == ':') {
					iPointsSeparatorIndex = i;
				} else if (iSpaceSeparatorIndex >= 0
						&& iPointsSeparatorIndex >= 0) {
					iMinutesCount++;
				} else if (iSpaceSeparatorIndex >= 0
						&& iPointsSeparatorIndex < 0) {
					iHoursCount++;
				}
			}

			for (int i = iStart; i < iEnd; i++) {
				if (Character.isWhitespace(r_oSource.charAt(i))) {
					iSpaceSeparatorIndex = i;
				} else if (r_oSource.charAt(i) == ':') {
					iPointsSeparatorIndex = i;
				} else if (iSpaceSeparatorIndex >= 0
						&& iPointsSeparatorIndex >= 0) {
					iMinutesCount++;
				} else if (iSpaceSeparatorIndex >= 0
						&& iPointsSeparatorIndex < 0) {
					iHoursCount++;
				}
			}

			for (int i = p_iDend; i < p_sDest.length(); i++) {
				if (Character.isWhitespace(p_sDest.charAt(i))) {
					iSpaceSeparatorIndex = i;
				} else if (p_sDest.charAt(i) == ':') {
					iPointsSeparatorIndex = i;
				} else if (iSpaceSeparatorIndex >= 0
						&& iPointsSeparatorIndex >= 0) {
					iMinutesCount++;
				} else if (iSpaceSeparatorIndex >= 0
						&& iPointsSeparatorIndex < 0) {
					iHoursCount++;
				}
			}
			if (iMinutesCount > this.minutesCount
					|| iHoursCount > this.hoursCount) {
				r_oSource = StringUtils.EMPTY;
			}
			return r_oSource;
		}

		@Override
		public int getInputType() {
			return 0;
		}

		@Override
		protected char[] getAcceptedChars() {
			return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', '/', ':', ' ' };
		}
	}

	@Override
	public Object getValueToValidate() {
		return this.oUiDate.getText().toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		AndroidApplication oApplication = AndroidApplication.getInstance();
		MMDialogFragment oDateTimeDialogFragment = null;
		int iButtonViewClickedId = p_oParameters.getInt(BUTTON_CLICK_ID_TAG);
		Class<MMDialogFragment> oFragmentDialogClass = null;
		int iDialogId = DATE_DIALOG_ID;
		this.aivDelegate.configurationGetValue(); // to refresh the displayedCalendarDate
		// with the data
		temporaryDialogValue = Calendar.getInstance();
		
		temporaryDialogValue.setTimeInMillis(displayedCalendarDate.getTimeInMillis());
		if ( iButtonViewClickedId == oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__date__button) ) {
			iDialogId = DATE_DIALOG_ID;
			if (displayedCalendarDate.get(Calendar.YEAR) < MIN_YEAR_OF_ANDROID_PICKER 
							|| displayedCalendarDate.get(Calendar.YEAR) > MAX_YEAR_OF_ANDROID_PICKER) {
				Toast.makeText( getContext(),
						Application.getInstance().getStringResource(AndroidApplicationR.error_component_datatime_picker_bad_date),
								Toast.LENGTH_LONG ).show();
				return null;
			}
		} else if (iButtonViewClickedId == oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__time__button)) {
			iDialogId = TIME_DIALOG_ID;
		}

		// Getting correct parameters for the DialogFragment
		StringBuffer r_oDialogClassName = new StringBuffer();
		r_oDialogClassName.append(DATE_TIME_DIALOG_PACKAGE_NAME);
		r_oDialogClassName.append('.');
		switch (iDialogId) {
		case DATE_DIALOG_ID:
			r_oDialogClassName.append(DATE_DIALOG_PICKER_FRAGMENT_NAME);
			break;
		case TIME_DIALOG_ID:
			r_oDialogClassName.append(TIME_DIALOG_PICKER_FRAGMENT_NAME);
			break;
		default:
			throw new MobileFwkException("createDialogFragment", "unknown dialog id");
		}

		// Instantiate and show the DialogFragment.
		FragmentActivity oParentActivity = getFragmentActivityContext();
		
		oDateTimeDialogFragment = (MMDialogFragment) oParentActivity
				.getSupportFragmentManager().findFragmentByTag(r_oDialogClassName.toString());
		if (oDateTimeDialogFragment == null) {
			try {
				oFragmentDialogClass = (Class<MMDialogFragment>) (Class.forName(r_oDialogClassName.toString()));
				if (oFragmentDialogClass
						.equals(MMDatePickerDialogFragment.class)) {
					oDateTimeDialogFragment = MMDatePickerDialogFragment
							.newInstance(this);
				} else if (oFragmentDialogClass
						.equals(MMTimePickerDialogFragment.class)) {
					oDateTimeDialogFragment = MMTimePickerDialogFragment
							.newInstance(this);
				}
			} catch (IllegalArgumentException e) {
				Log.e("MMDateTimeEditText.onClick", "The argument " + this + " is invalid for the method \"invoke\"");
				Log.e("MMDateTimeEditText.onClick", e.getMessage());
			} catch (SecurityException e) {
				Log.e("MMDateTimeEditText.onClick", "Calling \"getConstructor()\" on class " + r_oDialogClassName + "is not secure.");
				Log.e("MMDateTimeEditText.onClick", e.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e("MMDateTimeEditText.onClick", "The class named " + oFragmentDialogClass + "is not found");
				Log.e("MMDateTimeEditText.onClick", e.getMessage());
			}
		}
		
		return oDateTimeDialogFragment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		if (p_oDialog != null) {
			p_oDialog.setArguments(p_oDialogFragmentArguments);
			p_oDialog.setCancelable(false);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode,
			Intent p_oIntent) {
		// Nothing to do

	}

	/**
	 * Get Min Value DateTime
	 * @return long min value
	 */
	protected long getMinValue() {
		return minValue;
	}

	/**
	 * Set Min Value DateTime
	 * @param p_lMinValue Min Value DateTime
	 */
	public void setMinValue(long p_lMinValue) {
		this.minValue = p_lMinValue;
	}

	/**
	 * Get Max Value DateTime
	 * @return long max value
	 */
	protected long getMaxValue() {
		return maxValue;
	}

	/**
	 * Set Max Value DateTime
	 * @param p_lMaxValue Max Value DateTime
	 */
	public void setMaxValue(long p_lMaxValue) {
		this.maxValue = p_lMaxValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(DATE_STRING_TAG, this.oUiDate.getText().toString());
		r_oBundle.putInt("requestCode", this.requestCode);
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState;
		Parcelable oParentParcelable = r_oBundle
				.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		String textToSet = r_oBundle.getString(DATE_STRING_TAG);
		if(textToSet != null && textToSet.length() > 0 ) {
			this.oUiDate.setText(textToSet);
			this.aivDelegate.setFilled(true);
		}
		else {
			this.aivDelegate.setFilled(false);
		}
		this.requestCode = r_oBundle.getInt("requestCode");
	}

	@Override
	public void beforeTextChanged(CharSequence p_sSeq, int p_iStart, int p_iCount,
			int p_iAfter) {
		// Nothing to do

	}

	@Override
	public void onTextChanged(CharSequence p_sSeq, int p_iStart, int p_iBefore, int p_iCount) {
		// Nothing to do

	}

	@Override
	public void afterTextChanged(Editable p_oSEditable) {
		treatTextChanged(p_oSEditable.toString());
	}


	/**
	 * Call on text change to check change in framework
	 * @param p_sStringAfterTextChanged the string after text change (by user)
	 */
	private void treatTextChanged(String p_sStringAfterTextChanged) {
		boolean isValid = false;
		if(!this.aivDelegate.isWritingData()) {
			if ((p_sStringAfterTextChanged != null && p_sStringAfterTextChanged.length() > 0 && !"null".equals(p_sStringAfterTextChanged))) {
				this.aivDelegate.setFilled(true);

				StringBuilder  sErrorMessage =  new StringBuilder();
				if(this.aivFwkDelegate.customFormatter() != null) {
					Long oFormatterReturnValue = (Long) this.aivFwkDelegate.customFormatter().unformat(p_sStringAfterTextChanged);
					isValid = oFormatterReturnValue == CustomFormatter.FORMATTER_UNFORMAT_ERROR_CODE;
				}
				else {
					isValid = this.aivDelegate.validate((BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration(), null, sErrorMessage);
				}
				if(isValid) {
					this.aivDelegate.configurationUnsetError();
					this.aivFwkDelegate.changed();
				}
				else {
					this.aivDelegate.configurationSetError(sErrorMessage.toString());
				}
			} else {
				this.aivDelegate.setFilled(false);
				this.aivFwkDelegate.changed();
			}
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
		return this.oValidator;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		return this.oUiDate;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		oUiDate.setEnabled(p_bEnable);
		oUiDateButton.setEnabled(p_bEnable);
		oUiTimeButton.setEnabled(p_bEnable);
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Long configurationGetValue() {
		return this.retrieveDateFromEditText();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Long p_oObjectToSet) {

		if(p_oObjectToSet == null || !p_oObjectToSet.equals(this.configurationGetValue())) {
			if (p_oObjectToSet == null || p_oObjectToSet.equals(-1L)) {
				oUiDate.setText(StringUtils.EMPTY);
				this.aivDelegate.setFilled(false);
			} else {
				if (displayedCalendarDate == null) {
					displayedCalendarDate = Calendar.getInstance();
				}
				this.aivDelegate.setFilled(true);
				displayedCalendarDate.setTimeInMillis((Long) p_oObjectToSet);
				this.updateDisplay();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.oUiDate.getText().toString().length() > 0;
	}

	@Override
	public void doOnPostAutoBind() {
		// nothing
	}

	@Override
	public void destroy() {
		this.oUiDate.setHint(" ");
		this.oUiDate.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		this.oUiDateButton.setOnClickListener(null);
		this.oUiTimeButton.setOnClickListener(null);
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}
