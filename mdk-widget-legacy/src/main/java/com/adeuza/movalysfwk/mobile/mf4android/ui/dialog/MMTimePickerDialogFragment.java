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
package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;
 
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

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
 * The TimePicker Dialog to be used in applications based on Movalys Framework.
 * This Dialog specify the native TimePickerDialog by using the labels defined in : 
 * <ul>
 * <li>component_datetime_dialog_positivebutton__label as label for the validation button</li>
 * <li>component_datetime_dialog_negative_button__label as label for the cancel button</li>
 * </ul>
 */
public class MMTimePickerDialogFragment extends MMDialogFragment implements OnClickListener, OnTimeChangedListener, OnDismissListener {

	/**
	 * The DatePicker displayed in the fragmentDialog
	 */
	public static String TIME_PICKER_DIALOG_FRAGMENT_TAG = "timePickerDialogFragmentTag";
	
	/**
	 * The DatePicker displayed in the fragmentDialog
	 */
	private TimePicker uiTimePicker = null;

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
	public static MMTimePickerDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		MMTimePickerDialogFragment oFragment = new MMTimePickerDialogFragment();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		oFragment.componentId = ((View) p_oDismissListener).getId();
		oFragment.componentFragmentTag = ((ConfigurableVisualComponent) p_oDismissListener).getComponentFwkDelegate().getFragmentTag();
		return oFragment;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		
		oCurrentDateValue = Calendar.getInstance();
		int iYear = getArguments().getInt(MMCalendar.DATE_TAG_YEAR);
		int iMonth = getArguments().getInt(MMCalendar.DATE_TAG_MONTH);
		int iDay = getArguments().getInt(MMCalendar.DATE_TAG_DAY);
		int iHour = getArguments().getInt(MMCalendar.DATE_TAG_HOUR);
		int iMinute = getArguments().getInt(MMCalendar.DATE_TAG_MINUTE);
		oCurrentDateValue.set(iYear, iMonth, iDay, iHour, iMinute);
	}



	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onClick(DialogInterface p_oDialog, int p_oWhich) {
		switch(p_oWhich) {
		case AlertDialog.BUTTON_POSITIVE:
			oCurrentDateValue.set(this.oCurrentDateValue.get(Calendar.YEAR), 
				this.oCurrentDateValue.get(Calendar.MONTH), 
				this.oCurrentDateValue.get(Calendar.DAY_OF_MONTH),
				this.uiTimePicker.getCurrentHour(),
				this.uiTimePicker.getCurrentMinute()
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
		View view = inflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_dialog_timepicker), null);
		uiTimePicker = (TimePicker) view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.dialog__timepicker__picker));
		uiTimePicker.setCurrentHour(this.oCurrentDateValue.get(Calendar.HOUR_OF_DAY));
		uiTimePicker.setCurrentMinute(this.oCurrentDateValue.get(Calendar.MINUTE));
		uiTimePicker.setOnTimeChangedListener(this);			
		builder.setView(view);
		builder.setCancelable(false);
		builder.setTitle(stringDateFormat(oCurrentDateValue));

		//Setting the positive/negative/neutral buttons
		this.setButton(TimePickerDialog.BUTTON_POSITIVE, AndroidApplicationR.component_datetime_dialog__positivebutton__label, this, builder);
		this.setButton(TimePickerDialog.BUTTON_NEGATIVE, AndroidApplicationR.component_datetime_dialog__negativebutton__label, this, builder);
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
			int iHour = this.oCurrentDateValue.get(Calendar.HOUR_OF_DAY);
			int iMinute = this.oCurrentDateValue.get(Calendar.MINUTE);

			//Calling the onDismiss of the component after setting the new Date
			((MMDateTimeEditText)getDismissListener()).getTemporaryDialogValue().set(iYear, iMonth, iDate, iHour, iMinute);
			getDismissListener().onDismiss(p_oDialog);
		}
		else {
			getDismissListener().onDismiss(null);
		}
		super.onDismiss(p_oDialog);

	}


	@Override
	public void onTimeChanged(TimePicker p_oView, int p_iHourOfDay, int p_iMinute) {
		oCurrentDateValue.set(
				this.oCurrentDateValue.get(Calendar.YEAR),
				this.oCurrentDateValue.get(Calendar.MONTH),
				this.oCurrentDateValue.get(Calendar.DAY_OF_MONTH),
				p_iHourOfDay,
				p_iMinute);

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
		case TimePickerDialog.BUTTON_POSITIVE:
			p_oBuilder.setPositiveButton(sLabel, p_oListener);
			break;
		case TimePickerDialog.BUTTON_NEGATIVE:
			p_oBuilder.setNegativeButton(sLabel, p_oListener);
			break;
		case TimePickerDialog.BUTTON_NEUTRAL:
			p_oBuilder.setNeutralButton(sLabel, p_oListener);
			break;
		default:
			throw new MobileFwkException("onClick", "unknown button type");
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
			SimpleDateFormat postFormater = new SimpleDateFormat(" dd/MM/yyyy HH:mm"); 
			sTitle = postFormater.format(p_oDate.getTime());
		}
		return sTitle;
	}

}
