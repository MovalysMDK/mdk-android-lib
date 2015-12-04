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
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSignature;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSignatureDrawing;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing.MMPoint;

/**
 * The TimePicker Dialog to be used in applications based on Movalys Framework.
 * This Dialog specify the native TimePickerDialog by using the labels defined in : 
 * <ul>
 * <li>component_datetime_dialog_positivebutton__label as label for the validation button</li>
 * <li>component_datetime_dialog_negative_button__label as label for the cancel button</li>
 * </ul>
 */
public class MMSignatureDialogFragment extends MMDialogFragment implements OnClickListener , OnDismissListener{

	/**
	 * An enumeration for the ids of buttons.
	 */
	private static enum ButtonIds { VALIDATE, CANCEL, ERASE }

	/**
	 * The tag for this DialogFragment
	 */
	public static String SIGNATURE_DIALOG_FRAGMENT_TAG = "MMSignatureDialogFragmentTag";

	/**
	 * The graphic Signature component
	 */
	private MMSignatureDrawing m_oUiSignatureDrawing;

	/**
	 * The temporary value Path for this dialog
	 */
	private Path m_oTemporaryPathValue = null;

	/**
	 * The temporary value Signature for this dialog
	 */
	private List<List<MMPoint>> m_oTemporarySignatureValue = null;

	
	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static MMSignatureDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		MMSignatureDialogFragment oFragment = new MMSignatureDialogFragment();
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
	public void onClick(View p_oButtonView) {
		switch((ButtonIds)p_oButtonView.getTag()) {
		case VALIDATE:
			onValidateButtonClick();
			break;
		case CANCEL:
			onCancelbuttonClick();
			break;
		case ERASE:
			onEraseButtonClick();
			break;
		default:
			throw new MobileFwkException("onClick", "unknown button id");
		}
	}


	@Override
	/**
	 * {@inheritDoc}
	 * @see android.app.Dialog#onCreateonCreateView(LayoutInflater inflater, ViewGroup container,
	 *		Bundle savedInstanceState) 
	 */
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer,
			Bundle p_oSavedInstanceState) {
		super.onCreateView(p_oInflater, p_oContainer, p_oSavedInstanceState);
		final AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		//Inflate contentView of the DialogFragment
		View view = p_oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_signature_dialog), null);
		this.getDialog().setTitle(oApplication.getStringResource(AndroidApplicationR.component_signature__signatureDialog__title));

		this.m_oUiSignatureDrawing = (MMSignatureDrawing) view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_signature__signatureDialog__gesture));

		// If the data are not null (after rotation for example), they are restored, otherwhise, 
		// the data provided by the component are set.
		if(m_oTemporarySignatureValue != null && m_oTemporaryPathValue != null) {
			this.m_oUiSignatureDrawing.setSignatureLines(m_oTemporarySignatureValue);
			this.m_oUiSignatureDrawing.setPath(m_oTemporaryPathValue);
		}
		else {
			this.m_oUiSignatureDrawing.setSignatureLines(((MMSignature)getDismissListener()).getSignature());
			this.m_oUiSignatureDrawing.setPath(new Path(((MMSignature)getDismissListener()).getPath()));
		}

		//Setting the buttons
		Button validateButton = (Button)view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_signature__dialog__button_ok));
		Button cancelButton = (Button)view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_signature__dialog__button_ko));
		Button eraseButton = (Button)view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_signature__dialog__button_erase));

		validateButton.setTag(ButtonIds.VALIDATE);
		cancelButton.setTag(ButtonIds.CANCEL);
		eraseButton.setTag(ButtonIds.ERASE);

		validateButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		eraseButton.setOnClickListener(this);

		return view;
	}


	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onDismiss(DialogInterface p_oDialogInterface) {
		MMSignature dismissListener = (MMSignature)this.m_oDismissListener.get();
		if (dismissListener==null) {
			throw new IllegalStateException("MMSignature dialog should have a MMSignature component");
		}
		dismissListener.setTemporaryDialogPath(this.m_oTemporaryPathValue);
		dismissListener.setTemporaryDialogSignature(this.m_oTemporarySignatureValue);
		dismissListener.onDismiss(p_oDialogInterface);
		super.onDismiss(p_oDialogInterface);


	}

	/**
	 * Setter for temporary path value
	 * @param p_otemporaryPathValue the temporary path value
	 */
	public void setTemporaryPathValue(Path p_otemporaryPathValue) {
		this.m_oTemporaryPathValue = p_otemporaryPathValue;
	}

	/**
	 * Setter for temporary signature value
	 * @param p_oTemporarySignatureValue the temporary signature value
	 */
	public void setTemporarySignatureValue(List<List<MMPoint>> p_oTemporarySignatureValue) {
		this.m_oTemporarySignatureValue = p_oTemporarySignatureValue;
	}


	@Override
	public void onPause() {
		this.m_oTemporaryPathValue = m_oUiSignatureDrawing.getPath();
		this.m_oTemporarySignatureValue = m_oUiSignatureDrawing.getSignatureLines();
		super.onPause();
	}

	/**
	 * Call when valid button clicked.
	 * <p>set the path and signature and dismiss dialog</p>
	 */
	private void onValidateButtonClick() {
		this.m_oTemporaryPathValue = m_oUiSignatureDrawing.getPath();
		this.m_oTemporarySignatureValue = m_oUiSignatureDrawing.getSignatureLines();
		this.dismiss();
	}

	/**
	 * Call when cancel button clicked.
	 * <p>invalidate the path and signature and dismiss dialog</p>
	 */
	private void onCancelbuttonClick() {
		this.m_oTemporaryPathValue = null;
		this.m_oTemporarySignatureValue = null;
		this.dismiss();
	}

	/**
	 * Call when erase button clicked.
	 * <p>clear current signature</p>
	 */
	private void onEraseButtonClick() {
		m_oUiSignatureDrawing.clear();
	}

}
