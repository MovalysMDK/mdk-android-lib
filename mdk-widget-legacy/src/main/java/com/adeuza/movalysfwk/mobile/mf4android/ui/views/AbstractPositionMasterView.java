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
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl;

/**
 * AbstractPositionMasterView
 * 
 * Abstract class to factorize all Position Components
 *
 */
public abstract class AbstractPositionMasterView extends AbstractMMRelativeLayout<AddressLocationSVMImpl> 
	implements OnClickListener, ComponentEnable, ComponentReadableWrapper<AddressLocationSVMImpl>, ComponentWritableWrapper<AddressLocationSVMImpl> {
	
	/** the key used to retain the value during orientation changes */
	private static final String POSITION_TEXT_VIEW_VALUE_KEY = "positionTextViewValueKey";
	
	/** the latitude field on the panel */
	protected TextView oUiLatitude;
	/** the longitude field on the panel */
	protected TextView oUiLongitude;
	/** the label of Latitude field on the panel */
	protected TextView oUiLatitudeLabel;
	/** the label of Longitude field on the panel */
	protected TextView oUiLongitudeLabel;
	/** the map button on the panel */
	protected ImageButton oUiMapButton;

	/** ValueObject location used to open the map */
	protected AddressLocation oValueAddressLocation;
	
	/**
	 * Constructor for AbstractPositionMasterView
	 * @param p_oContext the android context
	 */
	public AbstractPositionMasterView(Context p_oContext) {
		super(p_oContext, AddressLocationSVMImpl.class);
	}
	
	/**
	 * Constructor for AbstractPositionMasterView
	 * @param p_oContext the android context
	 * @param p_oAttrs the view AttributeSet
	 */
	public AbstractPositionMasterView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, AddressLocationSVMImpl.class);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		AndroidApplication oApplication = (AndroidApplication) AndroidApplication.getInstance();
		
		oUiLatitudeLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_latitude__label));
		oUiLongitudeLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_longitude__label));
		oUiLatitude = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_latitude__value));
		oUiLongitude = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_longitude__value));
		oUiMapButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_map__button));
		
		oUiMapButton.setOnClickListener(this);
	}
	
	/**
	 * OnClickListener to deal with GPS and Map buttons {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {
		if (p_oParamView.equals(oUiMapButton) && this.oUiLatitude.length() > 0 && this.oUiLongitude.length() > 0) {
			oValueAddressLocation.setLatitude(Double.parseDouble(this.oUiLatitude.getText().toString()));
			oValueAddressLocation.setLongitude(Double.parseDouble(this.oUiLongitude.getText().toString()));

			Application.getInstance().getController().doOpenMap(oValueAddressLocation);
		}
	}



	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("class", MMPositionTextView.class.getName());
		r_oBundle.putSerializable(POSITION_TEXT_VIEW_VALUE_KEY, this.aivDelegate.configurationGetValue());
		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);
		
		AddressLocationSVMImpl oValueAddressLocationModel = (AddressLocationSVMImpl) r_oBundle.getSerializable(POSITION_TEXT_VIEW_VALUE_KEY);
		this.aivDelegate.configurationSetValue(oValueAddressLocationModel);
	}
	
	/**
	 * Getter of the longitude component
	 * @return the View that represent the longitude text
	 */
	public TextView getLongitudeView() {
		return this.oUiLongitude;
	}

	/**
	 * Getter of the latitude component
	 * @return the View that represent the latitude text
	 */
	public TextView getLatitudeView() {
		return this.oUiLatitude;
	}

	/********************************************************************************************
	 ***************************** Framework delegate callback **********************************
	 ********************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		oUiLongitude.setEnabled(p_bEnable);
		oUiLatitude.setEnabled(p_bEnable);
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(AddressLocationSVMImpl p_oObjectToSet) {
		if (p_oObjectToSet!=null) {
			this.oValueAddressLocation = new AddressLocation(p_oObjectToSet);
			if ( null != p_oObjectToSet.getLatitude() ) {
				oUiLatitude.setText( String.valueOf(p_oObjectToSet.getLatitude()) );
			} else {
				oUiLatitude.setText(StringUtils.EMPTY);
			}
			
			if ( null != p_oObjectToSet.getLongitude() ) {
				oUiLongitude.setText( String.valueOf(p_oObjectToSet.getLongitude()) );
			} else {
				oUiLongitude.setText(StringUtils.EMPTY);
			}
		} else {
		}
		oUiMapButton.setEnabled(isFilled());
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public AddressLocationSVMImpl configurationGetValue() {
		AddressLocationSVMImpl r_Value = null;
		if(oValueAddressLocation != null) {
			r_Value = new AddressLocationSVMImpl();
			r_Value.setGPSPosition(this.oUiLatitude.getText().toString(), this.oUiLongitude.getText().toString());
		}
		return r_Value;
	};
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return (oUiLatitude.getText()!=null && !"".equals(oUiLatitude.getText().toString())&& (oUiLongitude.getText()!=null && !"".equals(oUiLongitude.getText().toString())));
	}
}
