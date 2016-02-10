package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDateTimeEditText;
import com.adeuza.movalysfwk.mobile.mf4android.utils.MMCalendar;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.TheoriticalVisualComponentDescriptor;

/**
 * The DatePicker DialogFragment to be used in applications based on Movalys Framework.
 *
 * <p>Copyright (c) 2013</p>
 * <p>Company: Sopra Group</p>
 *
 * @since Cotopaxi
 * @author qlagarde
 */
public class MMDatePickerDialogFragment extends MMDialogFragment implements OnClickListener, OnDateChangedListener, OnDismissListener {


	/**
	 * The tag for this dialog
	 */
	public static final String DATE_PICKER_DIALOG_FRAGMENT_TAG = "datePickerDialogFragmentTag";


	/**
	 * The DatePicker displayed in the fragmentDialog
	 */
	private DatePicker uiDatePicker = null;

	/**
	 * The initial date value
	 */
	private Calendar oCurrentDateValue = null;

	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static MMDatePickerDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		MMDatePickerDialogFragment oFragment = new MMDatePickerDialogFragment();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		oFragment.componentId = ((View) p_oDismissListener).getId();
		oFragment.componentFragmentTag = ((ConfigurableVisualComponent<?>) p_oDismissListener).getFragmentTag();
		return oFragment;
	}
	
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		if(getArguments() != null && oCurrentDateValue == null) {
			oCurrentDateValue = Calendar.getInstance();
			
			int iYear = getArguments().getInt(MMCalendar.DATE_TAG_YEAR);
			int iMonth = getArguments().getInt(MMCalendar.DATE_TAG_MONTH);
			int iDay = getArguments().getInt(MMCalendar.DATE_TAG_DAY);
			oCurrentDateValue.set(iYear, iMonth, iDay);
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onClick(DialogInterface p_oDialog, int p_iWhich) {
		switch(p_iWhich) {
			case AlertDialog.BUTTON_POSITIVE:
				oCurrentDateValue.set(
					this.uiDatePicker.getYear(),
					this.uiDatePicker.getMonth(),
					this.uiDatePicker.getDayOfMonth()
				);
				break;
			case AlertDialog.BUTTON_NEGATIVE :
				oCurrentDateValue = null;
				break;
			default:
				throw new MobileFwkException("onClick", "unknown button type");
		}
	}


	@Override
	/**
	 * {@inheritDoc}
	 */
	public Dialog onCreateDialog(Bundle p_oSavedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		//Inflate contentView of the DialogFragment
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_dialog_datepicker), null);
		uiDatePicker = (DatePicker) view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.dialog__datepicker__picker));
		uiDatePicker.init(this.oCurrentDateValue.get(Calendar.YEAR), 
				this.oCurrentDateValue.get(Calendar.MONTH),
				this.oCurrentDateValue.get(Calendar.DAY_OF_MONTH),
				this);			
		builder.setView(view);
		builder.setCancelable(false);
		builder.setTitle(stringDateFormat(oCurrentDateValue));

		//Setting the positive/negative/neutral buttons
		this.setButton(DatePickerDialog.BUTTON_POSITIVE, AndroidApplicationR.component_datetime_dialog__positivebutton__label, this, builder);
		this.setButton(DatePickerDialog.BUTTON_NEGATIVE, AndroidApplicationR.component_datetime_dialog__negativebutton__label, this, builder);
		return builder.create();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onDismiss(DialogInterface p_oDialog) {
		if(oCurrentDateValue != null) {
			int iYear = this.oCurrentDateValue.get(Calendar.YEAR);
			int iMonth = this.oCurrentDateValue.get(Calendar.MONTH);
			int iDate = this.oCurrentDateValue.get(Calendar.DAY_OF_MONTH);

			//Calling the onDismiss of the component after setting the new Date
			((MMDateTimeEditText) getDismissListener()).getTemporaryDialogValue().set(iYear, iMonth, iDate);
			getDismissListener().onDismiss(p_oDialog);
		}
		else {
			getDismissListener().onDismiss(null);
		}
		super.onDismiss(p_oDialog);	
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onDateChanged(DatePicker p_oView, int p_iYear, int p_iMonthOfYear,
			int p_iDayOfMonth) {
		oCurrentDateValue.set(p_iYear, p_iMonthOfYear, p_iDayOfMonth);

		if(this.getDialog() != null) {
			this.getDialog().setTitle(stringDateFormat(oCurrentDateValue));
		}
	}


	/**
	 * Récupère la conf pour définir le texte du bouton à afficher
	 * @param p_oWhichButton id du bouton
	 * @param p_oLabel label à afficher
	 * @param p_oListener callback du bouton
	 */
	private void setButton(int p_oWhichButton, ApplicationR p_oLabel, OnClickListener p_oListener, Builder p_oBuilder) {
		assert p_oLabel.getGroup() == ApplicationRGroup.STRING;

		final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) ConfigurationsHandler.getInstance()
				.getVisualConfiguration(new TheoriticalVisualComponentDescriptor(p_oLabel.getKey(), null)
				.getConfigurationName());

		String sLabel = null;
		if (oConfiguration == null || oConfiguration.getLabel() == null) {
			sLabel = Application.getInstance().getStringResource(p_oLabel);
		}else {
			sLabel = oConfiguration.getLabel();
		}

		switch(p_oWhichButton) {
		case DatePickerDialog.BUTTON_POSITIVE:
			p_oBuilder.setPositiveButton(sLabel, p_oListener);
			break;
		case DatePickerDialog.BUTTON_NEGATIVE:
			p_oBuilder.setNegativeButton(sLabel, p_oListener);
			break;
		case DatePickerDialog.BUTTON_NEUTRAL:
			p_oBuilder.setNeutralButton(sLabel, p_oListener);
			break;
		default:
			throw new MobileFwkException("setButton", "unknown button type");
		}
	}

	/**
	 * Returns a litteral version opf a date
	 * @param p_oDate The date to convert to String
	 * @return A string of the date
	 */
	private String stringDateFormat(Calendar p_oDate) {
		String sTitle = null;
		if(p_oDate != null) {
			SimpleDateFormat postFormater = new SimpleDateFormat("EEEE, dd/MM/yyyy"); 
			sTitle = postFormater.format(p_oDate.getTime());
		}
		return sTitle;

	}

}
