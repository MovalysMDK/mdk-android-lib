package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
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
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDatePickerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMTimePickerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.utils.MMCalendar;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IDateFormatValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
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
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @since Annapurna
 */
public class MMDateTimeEditText extends AbstractMMTableLayout<Long>

implements OnClickListener, OnDismissListener, ValidableComponent,
MMComplexeComponent, TextWatcher {

	/** the application */
	private AndroidApplication oApplication = (AndroidApplication) Application
			.getInstance();
	/**
	 * Objet de validation de la valeur selon les paramètres liés au type de
	 * données
	 */
	private IDateFormatValidator oValidator;
	/** Attributs définis par XML */
	private Map<ConfigurableVisualComponent.Attribute, String> attributes = new HashMap<>();

	/** The Date only mode */
	public static final String DATE_MODE = "date";
	/** The Date Time mode */
	public static final String DATETIME_MODE = "datetime";
	/** The Time only mode */
	public static final String TIME_MODE = "time";
	/** The tag for the button clicked id */
	public static final String BUTTON_CLICK_ID_TAG = "buttonClickedIdTag";
	/** The tag used to retain the current date */
	public static final String DATE_STRING_TAG = "dateStringTag";

	/** the date field */
	private EditText oUiDate;
	/** the label above the date */
	private TextView oUiLabel;
	/** the date button */
	private ImageButton oUiDateButton;
	/** the time button */
	private ImageButton oUiTimeButton;
	private AbstractEntityFieldConfiguration.DataType dataType;
	/** the formater for text representation */
	private SimpleDateFormat dateTimeFormater;
	/** the date used by the component */
	private Calendar displayedCalendarDate = Calendar.getInstance();
	/** the date for the dialog opened */
	private Calendar temporaryDialogValue = Calendar.getInstance();

	private boolean writingData;
	/** id of the edit time dialog displayed the time picker */
	public static final int TIME_DIALOG_ID = 1000;
	/** id of the edit date dialog displayed the date picker */
	public static final int DATE_DIALOG_ID = 1001;
	/**
	 * Année max du picker de date
	 */
	private static final int MAX_YEAR_OF_ANDROID_PICKER = 2100;
	/**
	 * Année min du picker de date
	 */
	private static final int MIN_YEAR_OF_ANDROID_PICKER = 1900;

	/**
	 * Nom du fragment à instancier pour choisir la date
	 */
	private static final String DATE_DIALOG_PICKER_FRAGMENT_NAME = "MMDatePickerDialogFragment";
	/**
	 * Nom du fragment à instancier pour choisir l'heure
	 */
	private static final String TIME_DIALOG_PICKER_FRAGMENT_NAME = "MMTimePickerDialogFragment";
	/**
	 * Package contenant les DialogFragment à afficher
	 */
	private static final String DATE_TIME_DIALOG_PACKAGE_NAME = "com.adeuza.movalysfwk.mobile.mf4android.ui.dialog";

	/**
	 * Construct a MMDateTimeViewGroup
	 * 
	 * @param p_oContext
	 *            the context
	 */
	public MMDateTimeEditText(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()) {
			this.inflateMyLayout();
			this.defineParameters(null);
		}
	}

	/**
	 * Construct a MMDateTimeViewGroup
	 * 
	 * @param p_oContext
	 *            the context
	 * @param p_oAttrs
	 *            the xml attributes
	 */
	public MMDateTimeEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()) {
			this.inflateMyLayout();
			this.linkChildrens(p_oAttrs);
			this.defineParameters(p_oAttrs);
		}
	}

	private void inflateMyLayout(){
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_datetime_edittext),this);
		oUiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__datetime__label));
		oUiDate = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__datetime__edit));
		oUiDateButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__date__button));
		oUiTimeButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_datetime__time__button));
		oUiDate.addTextChangedListener(this);
	}

	/**
	 * link the child views with custom attributes
	 * 
	 * @param p_oAttrs
	 *            attributes of XML Component MMDateTimeViewGroup
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		try {
			oUiLabel.setText(this.getResources().getString(
					p_oAttrs.getAttributeResourceValue(
							Application.MOVALYSXMLNS, "label", 0)));
		} catch (NotFoundException e) {
			oUiLabel.setVisibility(View.GONE);
			oApplication.getLog().debug(
					this.getClass().getSimpleName(),
					StringUtils.concat(this.getConfiguration().getName(),
							"Ressource not found", String.valueOf(p_oAttrs
									.getAttributeResourceValue(
											Application.MOVALYSXMLNS, "label",
											0))));
		}
		String sMode = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,
				"mode");
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
					StringUtils.concat(this.getConfiguration().getName()
							+ " mode not set, default datetime mode is used"));
		}

		setCustomConverter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.CONVERTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
		setCustomFormatter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.FORMATTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
	}

	/**
	 * Récupère et conserve les données du paramètrage XML du composant Spécifie
	 * le type de données lié et son validateur
	 * 
	 * @param p_oAttrs
	 *            paramétrage XML du composant
	 */
	private void defineParameters(AttributeSet p_oAttrs) {
		this.attributes = new HashMap<>();
		if (p_oAttrs != null) {
			for (ConfigurableVisualComponent.Attribute oAttribute : ConfigurableVisualComponent.Attribute
					.values()) {
				String sValue = p_oAttrs.getAttributeValue(
						Application.MOVALYSXMLNS, oAttribute.getName());
				if (sValue != null) {
					this.attributes.put(oAttribute, sValue);
				}
			}
		}
		if (customFormatter() == null) {
			this.oUiDate
			.setFilters(new InputFilter[] { new DateTimeDigitsInputFilter() });
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
			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this
					.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.oUiLabel.setText(oConfiguration.getLabel());
			}
			if (oValidator != null) {
				oValidator.addParametersToConfiguration(this.attributes,
						(BasicComponentConfiguration) this.getConfiguration());
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
			this.configurationSetValue(temporaryDialogValue.getTimeInMillis());
			if(!this.writingData && this.aivDelegate != null) {
				this.aivDelegate.changed();
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
	 * setted in the xml
	 */
	private void updateDisplay() {
		String oValueToSetInEditText = StringUtils.EMPTY;
		if (this.customFormatter() != null) {
			oValueToSetInEditText = (String) customFormatter().format(displayedCalendarDate.getTimeInMillis());
		} else if (displayedCalendarDate != null) {
			oValueToSetInEditText = dateTimeFormater.format(displayedCalendarDate.getTime());
		}
		oUiDate.setText(oValueToSetInEditText);
		if(!this.writingData && this.aivDelegate != null) {
			this.aivDelegate.changed();
		}

	}

	/**
	 * On click listener to deal with the click on the date and time button
	 * 
	 * @param p_oClickedView
	 *            the view that was clicked
	 */
	@Override
	public void onClick(View p_oClickedView) {
		
		StringBuilder sMessageError = new StringBuilder();
		boolean isValid = this.validate((BasicComponentConfiguration) this.getConfiguration(), null, sMessageError);
		if(isValid) {
			configurationUnsetError();
			this.aivDelegate.changed();
		} else {
			configurationSetError(sMessageError.toString());
		}

		Bundle oParameters = new Bundle();
		oParameters.putInt(BUTTON_CLICK_ID_TAG, p_oClickedView.getId());
		MMDialogFragment oDialog = createDialogFragment(oParameters);

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

		// Setting the data to the dialogFragment
		prepareDialogFragment(oDialog, oDialogFragmentArguments);

		// Showing the dialogFragment
		oDialog.show(getFragmentActivityContext().getSupportFragmentManager(),
				oDialog.getFragmentDialogTag());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(Long p_oObjectToSet) {
		this.writingData = true;

		if(p_oObjectToSet == null || p_oObjectToSet != null && !p_oObjectToSet.equals(configurationGetValue())) {
			this.aivDelegate.configurationSetValue(p_oObjectToSet);
			if (p_oObjectToSet == null || p_oObjectToSet.equals(-1)) {
				oUiDate.setText(StringUtils.EMPTY);
				this.aivDelegate.setFilled(false);
			} else {
				if (displayedCalendarDate == null) {
					displayedCalendarDate = Calendar.getInstance();
				}
				this.aivDelegate.setFilled(true);
				displayedCalendarDate.setTimeInMillis(p_oObjectToSet);
				updateDisplay();
			}
		}
		this.writingData = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.aivDelegate.configurationSetCustomValues(p_oObjectsToSet);
		boolean isFilled = false;
		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0) {
			oUiDate.setText(StringUtils.EMPTY);
		} else {
			String sFirstValue = p_oObjectsToSet[0];
			if (sFirstValue != null && sFirstValue.equalsIgnoreCase("0")
					&& this.dataType.isDateFormatType()) {
				// définir la bonne date qui est relative
				sFirstValue = this
						.computeDefaultDate((BasicComponentConfiguration) this
								.getConfiguration());
			}
			this.updateDisplay();
			if (sFirstValue != null && sFirstValue.length() > 0) {
				try {
					if (displayedCalendarDate == null) {
						displayedCalendarDate = Calendar.getInstance();
					}
					displayedCalendarDate.setTime(dateTimeFormater
							.parse(sFirstValue));
					oUiDate.setText(sFirstValue);
				} catch (ParseException e) {
					Log.e("MMDateTimeEditText.configurationSetCustomValues",
							sFirstValue
							+ " is not a good date with the formatter "
							+ dateTimeFormater.toPattern());
				}
				isFilled = true;
			} else {
				oUiDate.setText(StringUtils.EMPTY);
			}
		}
		this.aivDelegate.setFilled(isFilled);
		this.setCustomLabel();
	}

	/**
	 * Définit l'étiquette du champ perso
	 */
	private void setCustomLabel() {
		String sNewCustomLabel = null;
		if (this.attributes
				.containsKey(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE)) {
			sNewCustomLabel = this.attributes
					.get(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE);
		}
		if (sNewCustomLabel == null && this.getConfiguration() != null) {
			Object oLabelFromGraph = ((BasicComponentConfiguration) this
					.getConfiguration())
					.getParameter(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE
							.getName());
			if (oLabelFromGraph != null) {
				sNewCustomLabel = oLabelFromGraph.toString();
			}
			if (sNewCustomLabel == null
					&& ((BasicComponentConfiguration) this.getConfiguration())
					.getEntityFieldConfiguration() != null) {
				Object oLabelFromEntity = ((BasicComponentConfiguration) this
						.getConfiguration())
						.getEntityFieldConfiguration()
						.getParameter(
								ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE
								.getName());
				if (oLabelFromEntity != null) {
					sNewCustomLabel = oLabelFromEntity.toString();
				}
			}
		}
		if (sNewCustomLabel != null && sNewCustomLabel.length() > 0) {
			this.oUiLabel.setText(sNewCustomLabel);
		} else {
			this.oUiLabel.setVisibility(GONE);
		}
	}

	/**
	 * Définit une date calculée selon la date du jour On incrémente ou
	 * décrémente la date selon le paramétrage et on prend l'heure et les
	 * minutes définies dans le paramétrage
	 * 
	 * @param p_oBasicComponentConfiguration
	 *            configuration du composant
	 */
	private String computeDefaultDate(
			BasicComponentConfiguration p_oBasicComponentConfiguration) {
		Calendar oDefautltDate = Calendar.getInstance();
		// define relative day
		Object oDefaultIncrDay = p_oBasicComponentConfiguration
				.getParameter(ConfigurableVisualComponent.Attribute.DEFAULT_INCR_DAY_ATTRIBUTE
						.getName());
		if (oDefaultIncrDay != null && oDefaultIncrDay.toString().length() > 0) {
			try {
				oDefautltDate.add(Calendar.DATE,
						Integer.parseInt(oDefaultIncrDay.toString()));
			} catch (NumberFormatException oEx) {
				Log.e("MMDateTimeEditText.configurationSetCustomValues.computeDefaultDate",
						oDefaultIncrDay.toString()
						+ " n'est pas un entier : le paramètre du chp perso '"
						+ ConfigurableVisualComponent.Attribute.DEFAULT_INCR_DAY_ATTRIBUTE
						.getName() + "' a une valeur erronée");
			}
		}
		// define hours
		Object oDefaultHours = p_oBasicComponentConfiguration
				.getParameter(ConfigurableVisualComponent.Attribute.DEFAULT_HOURS_ATTRIBUTE
						.getName());
		if (oDefaultHours != null && oDefaultHours.toString().length() > 0) {
			try {
				oDefautltDate.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(oDefaultHours.toString()));
			} catch (NumberFormatException oEx) {
				Log.e("MMDateTimeEditText.configurationSetCustomValues.computeDefaultDate",
						oDefaultHours.toString()
						+ " n'est pas un entier : le paramètre '"
						+ ConfigurableVisualComponent.Attribute.DEFAULT_HOURS_ATTRIBUTE
						.getName() + "' du chp perso '"
						+ this.aivDelegate.getConfigurationName()
						+ "' a une valeur erronée");
			}
		}
		// define minutes
		Object oDefaultMinutes = p_oBasicComponentConfiguration
				.getParameter(ConfigurableVisualComponent.Attribute.DEFAULT_MINUTES_ATTRIBUTE
						.getName());
		if (oDefaultMinutes != null && oDefaultMinutes.toString().length() > 0) {
			try {
				oDefautltDate.set(Calendar.MINUTE,
						Integer.parseInt(oDefaultMinutes.toString()));
			} catch (NumberFormatException oEx) {
				Log.e("MMDateTimeEditText.configurationSetCustomValues.computeDefaultDate",
						oDefaultMinutes.toString()
						+ " n'est pas un entier : le paramètre "
						+ ConfigurableVisualComponent.Attribute.DEFAULT_MINUTES_ATTRIBUTE
						.getName() + "' du chp perso '"
						+ this.aivDelegate.getConfigurationName()
						+ "' a une valeur erronée");
			}
		}
		DateFormat oFormat = ((IDateFormatValidator) oValidator)
				.getDateFormat(p_oBasicComponentConfiguration);
		if (oFormat != null) {
			displayedCalendarDate.setTime(oDefautltDate.getTime());
			return oFormat.format(oDefautltDate.getTime());
		}
		return null;
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
	 * {@inheritDoc}
	 */
	@Override
	public Long configurationGetValue() {

		return retrieveDateFromEditText();
	}


	private Long retrieveDateFromEditText() {
		Long r_oValue = null;

		try {
			// Si la valeur du champ est nulle, le composant ne doit pas
			// renvoyer de valeur.
			String sValue = this.oUiDate.getText().toString();
			if (sValue != null && sValue.trim().length() > 0) {
				if (customFormatter() != null) {
					r_oValue = (Long) customFormatter().unformat(
							this.oUiDate.getText().toString());
				} else {
					displayedCalendarDate.setTime(this.dateTimeFormater
							.parse(sValue.trim()));
					r_oValue = displayedCalendarDate.getTimeInMillis();

				}
			} else {
				displayedCalendarDate = Calendar.getInstance();
			}
			
			
			// Compare Date Saisie et Date Calculée
			String oValueToSetInEditText = StringUtils.EMPTY;
			if (this.customFormatter() != null) {
				oValueToSetInEditText = (String) customFormatter().format(displayedCalendarDate.getTimeInMillis());
			} else if (displayedCalendarDate != null) {
				oValueToSetInEditText = dateTimeFormater.format(displayedCalendarDate.getTime());
			}
			
			if (!sValue.equals(oValueToSetInEditText)) {
				displayedCalendarDate = Calendar.getInstance();
				oApplication.getLog().debug(
						this.getClass().getSimpleName(),
						StringUtils.concat("Error parsing the date field for ",
								this.aivDelegate.getName()));
			}

		} catch (ParseException e) {
			displayedCalendarDate = Calendar.getInstance();
			oApplication.getLog().debug(
					this.getClass().getSimpleName(),
					StringUtils.concat("Error parsing the date field for ",
							this.aivDelegate.getName()));
		}
		return r_oValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiDate.setEnabled(false);
		oUiDateButton.setEnabled(false);
		oUiTimeButton.setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiDate.setEnabled(true);
		oUiDateButton.setEnabled(true);
		oUiTimeButton.setEnabled(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] { oUiDate.getText().toString() };
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
	public boolean isNullOrEmptyValue(Long p_oObject) {
		return p_oObject == null || 0L == p_oObject.longValue();
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
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#resetChanged()
	 */
	@Override
	public void resetChanged() {
		super.resetChanged();
		this.aivDelegate.resetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return aivDelegate.isFilled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		if(this.oUiDate.getError() != null) {
			this.oUiDate.setError(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.oUiDate.setError(p_oError);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, java.util.Map<String,Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		boolean r_bValidate = true;
		
		if (super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder)
				&& customFormatter() == null) {
			r_bValidate = oValidator.validate(this, p_oConfiguration, p_oErrorBuilder);
		}
		return r_bValidate;
	}

	/**
	 * Définit le type de données du champ parmi un champ de type Date Précise
	 * le validateur et stocke le format de date
	 * 
	 * @param p_oType
	 *            nom type de données du champ
	 */
	public void setDataType(AbstractEntityFieldConfiguration.DataType p_oType) {
		this.dataType = p_oType;
		oValidator = (IDateFormatValidator) BeanLoader.getInstance().getBean(
				this.dataType.getValidatorClass());
		oValidator.addParametersToConfiguration(this.attributes,
				(BasicComponentConfiguration) this.getConfiguration());
		dateTimeFormater = oValidator
				.getDateFormat((BasicComponentConfiguration) this
						.getConfiguration());

		switch (this.dataType) {
		case TYPE_TIME:
			oUiDateButton.setVisibility(GONE);
			if (this.isEdit()) {
				oUiTimeButton.setVisibility(VISIBLE);
				oUiTimeButton.setOnClickListener(this);
				oUiDate.setInputType(InputType.TYPE_CLASS_DATETIME
						| InputType.TYPE_DATETIME_VARIATION_TIME);
			} else {
				oUiTimeButton.setVisibility(GONE);
			}
			break;

		case TYPE_DATE:
			if (this.isEdit()) {
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
			if (this.isEdit()) {
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
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		if (this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationRemoveMandatoryLabel();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		if (!this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationSetMandatoryLabel();
		}
	}

	/**
	 * <p>
	 * Filtre sur les date-horaires.
	 * </p>
	 * 
	 * <p>
	 * Copyright (c) 2012
	 * </p>
	 * <p>
	 * Company: Adeuza
	 * </p>
	 * 
	 * @author ktilhou
	 */
	private class DateTimeDigitsInputFilter extends NumberKeyListener {

		/** Nombre de digits pour les heures */
		private int hoursCount = 2;

		/** Nombre de digits pour les minutes */
		private int minutesCount = 2;

		/**
		 * Construit un nouveau filtre.
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

		/**
		 * {@inheritDoc}
		 * 
		 * @see android.text.method.KeyListener#getInputType()
		 */
		@Override
		public int getInputType() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see android.text.method.NumberKeyListener#getAcceptedChars()
		 */
		@Override
		protected char[] getAcceptedChars() {
			return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', '/', ':', ' ' };
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ValidableComponent#getValueToValidate()
	 */
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
		MMDialogFragment oDateTimeDialogFragment = null;
		int iButtonViewClickedId = p_oParameters.getInt(BUTTON_CLICK_ID_TAG);
		Class<MMDialogFragment> oFragmentDialogClass = null;
		int iDialogId = DATE_DIALOG_ID;
		this.configurationGetValue(); // to refresh the displayedCalendarDate
		// with the data
		if (temporaryDialogValue == null) {
			temporaryDialogValue = Calendar.getInstance();
		}
		temporaryDialogValue.setTimeInMillis(displayedCalendarDate
				.getTimeInMillis());
		if (iButtonViewClickedId == oApplication
				.getAndroidIdByRKey(AndroidApplicationR.component_datetime__date__button)) {
			iDialogId = DATE_DIALOG_ID;
			if (displayedCalendarDate != null
					&& (displayedCalendarDate.get(Calendar.YEAR) < MIN_YEAR_OF_ANDROID_PICKER || displayedCalendarDate
							.get(Calendar.YEAR) > MAX_YEAR_OF_ANDROID_PICKER)) {
				Toast.makeText(
						getContext(),
						Application
						.getInstance()
						.getStringResource(
								AndroidApplicationR.error_component_datatime_picker_bad_date),
								Toast.LENGTH_LONG).show();
				return null;
			}
		} else if (iButtonViewClickedId == oApplication
				.getAndroidIdByRKey(AndroidApplicationR.component_datetime__time__button)) {
			iDialogId = TIME_DIALOG_ID;
		}

		// Getting correct parameters for the DialogFragment
		String r_oDialogClassName = null;
		switch (iDialogId) {
		case DATE_DIALOG_ID:
			r_oDialogClassName = DATE_DIALOG_PICKER_FRAGMENT_NAME;
			break;
		case TIME_DIALOG_ID:
			r_oDialogClassName = TIME_DIALOG_PICKER_FRAGMENT_NAME;
			break;
		default:
			throw new MobileFwkException("createDialogFragment", "unknown dialog id");
		}

		r_oDialogClassName = DATE_TIME_DIALOG_PACKAGE_NAME + "."
				+ r_oDialogClassName;

		// Instantiate and show the DialogFragment.
		FragmentActivity oParentActivity = getFragmentActivityContext();
		if (oParentActivity != null
				&& oParentActivity instanceof FragmentActivity) {
			oDateTimeDialogFragment = (MMDialogFragment) oParentActivity
					.getSupportFragmentManager().findFragmentByTag(
							r_oDialogClassName);
			if (oDateTimeDialogFragment == null) {
				try {
					oFragmentDialogClass = (Class<MMDialogFragment>) (Class
							.forName(r_oDialogClassName));
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
					Log.e("MMDateTimeEditText.onClick", "The argument " + this
							+ " is invalid for the method \"invoke\"");
					e.printStackTrace();
				} catch (SecurityException e) {
					Log.e("MMDateTimeEditText.onClick",
							"Calling \"getConstructor()\" on class "
									+ r_oDialogClassName + "is not secure.");
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					Log.e("MMDateTimeEditText.onClick", "The class named "
							+ String.valueOf(oFragmentDialogClass)
							+ "is not found");
					e.printStackTrace();
				}
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
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(DATE_STRING_TAG, this.oUiDate.getText().toString());
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



	private void treatTextChanged(String p_sStringAfterTextChanged) {
		boolean isValid = false;
		if(!this.writingData) {
			if ((p_sStringAfterTextChanged != null && p_sStringAfterTextChanged.length() > 0 && !"null".equals(p_sStringAfterTextChanged))) {
				this.aivDelegate.setFilled(true);

				StringBuilder  sErrorMessage =  new StringBuilder();
				if(customFormatter() != null) {
					Long oFormatterReturnValue = (Long) customFormatter().unformat(p_sStringAfterTextChanged);
					isValid = oFormatterReturnValue == Long.valueOf(CustomFormatter.FORMATTER_UNFORMAT_ERROR_CODE);
				}	
				else {
					isValid = this.validate((BasicComponentConfiguration) this.getConfiguration(), null, sErrorMessage);
				}
				if(isValid) {
					configurationUnsetError();
					this.aivDelegate.changed();
				}
				else {
					configurationSetError(sErrorMessage.toString());
				}
			} else {
				this.aivDelegate.setFilled(false);
			}
		}		
	}

	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	} 

	public void setCustomConverter(String p_oCustomConverter,
			AttributeSet p_oAttributeSet) {
		this.aivDelegate
		.setCustomConverter(p_oCustomConverter, p_oAttributeSet);
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

	public void setCustomFormatter(String p_oCustomFormatter,
			AttributeSet p_oAttributeSet) {
		this.aivDelegate
		.setCustomFormatter(p_oCustomFormatter, p_oAttributeSet);
	}

}
