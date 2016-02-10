package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;
 
import java.lang.ref.WeakReference;
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
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDoublePickerGroup;
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
public class MMDoublePickerDialogFragment extends MMDialogFragment implements OnClickListener, OnDateChangedListener, OnDismissListener {

	/**
	 * The tag for this dialog
	 */
	public static final String DOUBLE_PICKER_DIALOG_FRAGMENT_TAG = "doublePickerDialogFragmentTag";

	/**
	 * The temporary value for this dialog
	 */
	public Double m_dTemporaryValue = null;
	
	/**
	 * The temporary value for this dialog
	 */
	public MMDoublePickerGroup m_uiDoublePicker = null;

	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static MMDoublePickerDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		MMDoublePickerDialogFragment oFragment = new MMDoublePickerDialogFragment();
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
	public void onClick(DialogInterface p_oDialog, int p_iWhich) {
		switch(p_iWhich) {
		case AlertDialog.BUTTON_POSITIVE:
			onValidateClick();
			break;
		case AlertDialog.BUTTON_NEGATIVE:
			onCancelClick();
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
		View o_uiContentView = inflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_double_dialog), null);
		builder.setTitle(oApplication.getStringResource(AndroidApplicationR.component_double__doubleDialog__title));
		
		m_uiDoublePicker = (MMDoublePickerGroup) o_uiContentView.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_doublepicker__picker));
		m_uiDoublePicker.configurationSetValue(m_dTemporaryValue);
		m_uiDoublePicker.configurationEnabledComponent() ;
		
		//Setting the positive/negative/neutral buttons
		this.setButton(DatePickerDialog.BUTTON_POSITIVE, AndroidApplicationR.component_dialog_fragment__positivebutton__label, this, builder);
		this.setButton(DatePickerDialog.BUTTON_NEGATIVE, AndroidApplicationR.component_dialog_fragment__negativebutton__label, this, builder);

		builder.setView(o_uiContentView);

		return builder.create();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onDismiss(DialogInterface p_oDialogInterface) {
		getDismissListener().onDismiss(p_oDialogInterface);
		super.onDismiss(p_oDialogInterface);	
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onDateChanged(DatePicker p_oView, int p_iYear, int p_iMonthOfYear,
			int p_iDayOfMonth) {
		Calendar oCurrentDate = Calendar.getInstance();
		oCurrentDate.set(p_iYear, p_iMonthOfYear, p_iDayOfMonth);
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
	 * Get the current temporary value of this dialog
	 * @return the current value of this dialog
	 */
	public Double getTemporaryDialogValue() {
		return this.m_dTemporaryValue;
	}

	/**
	 * Set the temporary value for this dialog
	 * @param p_dTemporaryValue the value to set in this dialog
	 */
	public void setTemporaryDialogValue(Double p_dTemporaryValue) {
		this.m_dTemporaryValue = p_dTemporaryValue;
	}
	
	/**
	 * Validate the current value
	 */
	private void onValidateClick() {
		this.m_dTemporaryValue = m_uiDoublePicker.configurationGetValue();
	}

	/**
	 * Cancel the current value
	 */
	private void onCancelClick() {
		this.m_dTemporaryValue = null;
	}

}
