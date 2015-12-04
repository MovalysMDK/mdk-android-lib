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
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.MMLocationCommandDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl;

/**
 * This view group represents a Position with latitude and longitude Edit views, its label, and a map button that open the map application installed
 * on the device, This component also contain a GPS button that allow to capture the current location using the network and the GPS location provider
 * <ul>
 * 	<li>add xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li> 
 * 	<li>include a com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPositionEditText</li> 
 * 	<li>add label attribute to declare the @string/resource Id to use as label for the group</li> 
 * 	<li>add labelLatitude attribute to declare the @string/resource Id to use as label for the latitude field label</li>
 * 	<li>add labelLongitude attribute to declare the @string/resource Id to use as label for the longitude field label</li>
 * </ul>
 */
public class MMPositionEditText extends AbstractPositionMasterView implements OnDismissListener, MMComplexeComponent, ComponentEnable {


	/** the gps button on the panel */
	private ImageButton oUiGPSButton;
	
	/** the Location command used to locate the device with network and gps sensors */
	private MMLocationCommandDialogFragment oLocationCommand;
	
	/** the key used to retain the value during orientation changes */
	private static final String POSITION_EDIT_TEXT_VALUE_KEY = "positionEditTextValueKey";

	/** true if latitude or longitude are filled */
	private boolean latitudeFilled, longitudeFilled;

	/** Android FragmentDialog TAG */
	private String sLocationDialogTag;

	/**
	 * Component request code
	 */
	private int requestCode = -1;
	
	/**
	 * Construct a new MMPositionEditText component
	 * @param p_oContext the current context
	 * @param p_oAttrs xml attributes
	 */
	public MMPositionEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!this.isInEditMode()) {
			this.inflateLayout();
		}
	}
	
	/**
	 * inflates the components's view
	 */
	private final void inflateLayout() {
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_position_edittext), this);

		oUiGPSButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_gps__button));
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		//seulement lorsqu'une valeur non vides est settée
		//oUiMapButton.setOnClickListener(this);
		oUiGPSButton.setOnClickListener(this);
		oUiLatitude.addTextChangedListener(this.latitudeChanged);
		oUiLongitude.addTextChangedListener(this.longitudeChanged);

		latitudeFilled = false;
		longitudeFilled = false;
		
	}

	/**
	 * Set a selection on the parameter EditText
	 * @param p_oEditText the EditText on witch put selection
	 * @param p_iStart the selection start
	 * @param p_iStop the selection end
	 */
	private void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean p_bEnabled) {
		super.setEnabled(p_bEnabled);
		oUiGPSButton.setEnabled(p_bEnabled);
		oUiMapButton.setEnabled(this.aivDelegate.isFilled());
		oUiLatitude.setEnabled(p_bEnabled);
		oUiLatitude.setFocusable(p_bEnabled);
		oUiLatitude.setFocusableInTouchMode(p_bEnabled);
		oUiLongitude.setEnabled(p_bEnabled);
		oUiLongitude.setFocusable(p_bEnabled);
		oUiLongitude.setFocusableInTouchMode(p_bEnabled);
	}

	/**
	 * Dismiss listener allow to set the location if OK Button pressed of accuracy conform to requested Cancel the location Modification if Cancel
	 * button pressed {@inheritDoc}
	 * 
	 */
	@Override
	public void onDismiss(DialogInterface p_oParamDialogInterface) {
		//si l'utilisateur a accepté la nouvelle position (ok et pas cancel)
		if (oLocationCommand.isCurrentLocationAvaillable()) {
			Location oLocationResult = oLocationCommand.getCurrentLocation();
			if (oLocationResult != null){
				oUiLatitude.setText(String.valueOf(oLocationResult.getLatitude()));
				oUiLongitude.setText(String.valueOf(oLocationResult.getLongitude()));
			}
		} 
		oLocationCommand.dismiss();
	}

	/**
	 * OnClickListener to deal with GPS and Map buttons {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {
		if (p_oParamView.equals(oUiGPSButton)) {
			// création du dialog sur l'activité voir onCreateDialog()
			this.oLocationCommand = (MMLocationCommandDialogFragment) createDialogFragment(null);
			this.sLocationDialogTag = this.oLocationCommand.getFragmentDialogTag();
			prepareDialogFragment(this.oLocationCommand, null);
			this.oLocationCommand.show(getFragmentActivityContext().getSupportFragmentManager(), oLocationCommand.getFragmentDialogTag());
		} else {
			super.onClick(p_oParamView);
		}
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
	 * Checks if the component is filled
	 */
	public void checkIsFilled() {
		this.aivDelegate.setFilled(latitudeFilled && longitudeFilled);
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return MMLocationCommandDialogFragment.newInstance(this);
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments) {
		p_oDialog.setCancelable(false);
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
		r_oBundle.putString("class", MMPositionTextView.class.getName());
		r_oBundle.putSerializable(POSITION_EDIT_TEXT_VALUE_KEY, this.aivDelegate.configurationGetValue());
		r_oBundle.putString("dialogTag", this.sLocationDialogTag);
		r_oBundle.putInt("requestCode", this.requestCode);
		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		AddressLocationSVMImpl oValueAddressLocationModel = (AddressLocationSVMImpl) r_oBundle.getSerializable(POSITION_EDIT_TEXT_VALUE_KEY);
		this.aivDelegate.configurationSetValue(oValueAddressLocationModel);
		String dialogTag = r_oBundle.getString("dialogTag");
		if(dialogTag != null) {
			this.sLocationDialogTag = dialogTag;
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			this.oLocationCommand = (MMLocationCommandDialogFragment) ((AbstractMMActivity)oApplication.getCurrentActivity()).getSupportFragmentManager().findFragmentByTag(dialogTag);
		}
		this.requestCode = r_oBundle.getInt("requestCode");
	}

	/**
	 * TextWatcher on latitude EditText
	 */
	private TextWatcher latitudeChanged = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence p_sS, int p_iStart, int p_iBefore, int p_iCount) {
			if (!MMPositionEditText.this.aivDelegate.isWritingData() ) {
				latitudeFilled = (p_sS.toString() != null && !p_sS.toString().equals(StringUtils.EMPTY));
				MMPositionEditText.this.checkIsFilled();
				MMPositionEditText.this.aivFwkDelegate.changed();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence p_sS, int p_iStart, int p_iCount, int p_iAfter) {
			// Nothing to do
		}

		@Override
		public void afterTextChanged(Editable p_oS) {
			// Nothing to do
		}
	};

	/**
	 * TextWatcher on longitude EditText
	 */
	private TextWatcher longitudeChanged = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence p_sS, int p_iStart, int p_iBefore, int p_iCount) {
			if (!MMPositionEditText.this.aivDelegate.isWritingData() ) {
				longitudeFilled = (p_sS.toString() != null && !p_sS.toString().equals(StringUtils.EMPTY));
				MMPositionEditText.this.checkIsFilled();
				MMPositionEditText.this.aivFwkDelegate.changed();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence p_sS, int p_iStart, int p_iCount, int p_iAfter) {
			// Nothing to do
		}

		@Override
		public void afterTextChanged(Editable p_oS) {
			// Nothing to do
		}
	};
	
	/********************************************************************************************
	 ***************************** Framework delegate callback **********************************
	 ********************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(AddressLocationSVMImpl p_oObjectToSet) {
		int iLatOldStart = 0;
		int iLatOldStop = 0;
		int iLonOldStart = 0;
		int iLonOldStop = 0;
		
		if (p_oObjectToSet!=null) {
			if (null != p_oObjectToSet.getLatitude() ) {
				iLatOldStart = this.oUiLatitude.getSelectionStart();
				iLatOldStop = this.oUiLatitude.getSelectionEnd();
				iLatOldStart = Math.min(iLatOldStart, this.oUiLatitude.length());
				iLatOldStop = Math.min(iLatOldStop, this.oUiLatitude.length());
				this.latitudeFilled = true;
			} else {
				this.latitudeFilled = false;
			}
			
			if (null != p_oObjectToSet.getLongitude() ) {
				iLonOldStart= oUiLongitude.getSelectionStart();
				iLonOldStop = oUiLongitude.getSelectionEnd();
				iLonOldStart = Math.min(iLonOldStart, oUiLongitude.length());
				iLonOldStop = Math.min(iLonOldStop, oUiLongitude.length());
			}
		}
		super.configurationSetValue(p_oObjectToSet);
		
		if (p_oObjectToSet!=null) {
			if (null != p_oObjectToSet.getLatitude() ) {
				this.setEditTextSelection((EditText) this.oUiLatitude, iLatOldStart, iLatOldStop);
				this.latitudeFilled = true;
			} else {
				this.latitudeFilled = false;
			}
			
			if (null != p_oObjectToSet.getLongitude()) {
				this.setEditTextSelection((EditText) this.oUiLongitude, iLonOldStart, iLonOldStop);
				this.longitudeFilled = true;
			} else {
				this.longitudeFilled = false;
			}
			
			if(this.oValueAddressLocation.isAcurate()) {
				oUiMapButton.setEnabled(true);
				oUiMapButton.setOnClickListener(this);
			}
		} else {
			this.latitudeFilled = false;
			this.longitudeFilled = false;
			oUiMapButton.setEnabled(false);
		}
		this.checkIsFilled();
		
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		super.enableComponent(p_bEnable);
		oUiGPSButton.setEnabled(p_bEnable);
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}

}
