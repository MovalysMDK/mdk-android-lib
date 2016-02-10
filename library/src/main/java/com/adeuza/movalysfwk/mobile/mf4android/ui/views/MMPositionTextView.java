package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl;

/**
 * This view group represents a Position with latitude and longitude Text views, its label, and a map button that open the map application installed
 * on the device
 * <ul>
 * 	<li>add xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li> 
 * 	<li>include a com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPositionTextView</li> 
 * 	<li>add label attribute to declare the @string/resource Id to use as label for the group</li> 
 * 	<li>add labelLatitude attribute to declare the @string/resource Id to use as label for the latitude field label</li>
 * 	<li>add labelLongitude attribute to declare the @string/resource Id to use as label for the longitude field label</li>
 * </ul>
 * 
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * 
 * @author dmaurange
 * @since Anapurna
 */
public class MMPositionTextView extends AbstractMMRelativeLayout<AddressLocationSVMImpl> implements OnClickListener{

	/** the latitude field on the panel */
	private TextView oUiLatitude;
	/** the longitude field on the panel */
	private TextView oUiLongitude;
	/** the label above fields on the panel */
	private TextView oUiLabel;
	/** the label of Latitude field on the panel */
	private TextView oUiLatitudeLabel;
	/** the label of Longitude field on the panel */
	private TextView oUiLongitudeLabel;
	/** the map button on the panel */
	private ImageButton oUiMapButton;

	/** ValueObject location used to open the map */
	private AddressLocation oValueAddressLocation;
	/** View Model of the location */
	private AddressLocationSVMImpl oValueAddressLocationModel;

	/** the key used to retain the value during orientation changes */
	private static final String POSITION_TEXT_VIEW_VALUE_KEY = "positionTextViewValueKey";

	/**
	 * 
	 * Construct a new MMPositionEditText component
	 * 
	 * @param p_oContext
	 *            the current context
	 */
	public MMPositionTextView(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * 
	 * Construct a new MMPositionTextView component
	 * 
	 * @param p_oContext
	 *            the current context
	 * @param p_oAttrs
	 *            xml attributes
	 */
	public MMPositionTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			linkChildrens(p_oAttrs);
		}
	}


	/**
	 * 
	 * link the child views with custom attributes and add button onClickListener
	 * 
	 * @param p_oAttrs
	 *            attributes of XML Component component_phone_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		if(!isInEditMode()) {
			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_position_textview), this);

			oUiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position__label));
			oUiLatitudeLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_latitude__label));
			oUiLongitudeLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_longitude__label));
			oUiLatitude = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_latitude__value));
			oUiLongitude = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_longitude__value));
			oUiMapButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_map__button));

			try {
				oUiLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0)));
			} catch (NotFoundException e) {
				Application.getInstance().getLog().debug("MMPositionTextView",
						StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0))));
			}
			try {
				oUiLatitudeLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "labelLatitude", 0)));
			} catch (NotFoundException e) {
				Application.getInstance().getLog().debug("MMPositionTextView",
						StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "labelLatitude", 0))));
			}
			try {
				oUiLongitudeLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "labelLongitude", 0)));
			} catch (NotFoundException e) {
				Application.getInstance().getLog()
				.debug("MMPositionTextView",
						StringUtils.concat("Ressource not found",
								String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "labelLongitude", 0))));
			}
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
			// Ecrasement des libellés par défaut avec l'éventuelle configuration fournie par le serveur.
			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.oUiLabel.setText(oConfiguration.getLabel());
			}

			if (ConfigurationsHandler.isLoaded()) {
				oConfiguration = (BasicComponentConfiguration) ConfigurationsHandler.getInstance()
						.getVisualConfiguration(this.aivDelegate.getConfigurationName().concat("Latitude"));

				if (oConfiguration != null && oConfiguration.getLabel() != null) {
					this.oUiLatitudeLabel.setText(oConfiguration.getLabel());
				}

				oConfiguration = (BasicComponentConfiguration) ConfigurationsHandler.getInstance()
						.getVisualConfiguration(this.aivDelegate.getConfigurationName().concat("Longitude"));

				if (oConfiguration != null && oConfiguration.getLabel() != null) {
					this.oUiLongitudeLabel.setText(oConfiguration.getLabel());
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(AddressLocationSVMImpl p_oAddressLocation) {
		super.configurationSetValue(p_oAddressLocation);
		if (p_oAddressLocation!=null) {
			oValueAddressLocationModel=p_oAddressLocation;
			oValueAddressLocation = new AddressLocation(p_oAddressLocation);
			//lors de la récupération d'une position nulle, les valeurs peuvent etre 0.0 0.0 (résultat de cursor.getDouble(null))
			if (p_oAddressLocation.isAcurate() && ((p_oAddressLocation.getLatitude()!= null && p_oAddressLocation.getLatitude()!=0.0) || (p_oAddressLocation.getLongitude()!=null && p_oAddressLocation.getLongitude()!=0.0))) {
				oUiLatitude.setText(String.valueOf(p_oAddressLocation.getLatitude()));
				oUiLongitude.setText(String.valueOf(p_oAddressLocation.getLongitude()));
				oUiMapButton.setEnabled(true);
				oUiMapButton.setOnClickListener(this);

			}else{
				oUiLatitude.setText(StringUtils.EMPTY);
				oUiLongitude.setText(StringUtils.EMPTY);
				oUiMapButton.setEnabled(false);
			}

		}
		else{
			oUiMapButton.setEnabled(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public AddressLocationSVMImpl configurationGetValue() {
		if(oValueAddressLocationModel != null) {
			oValueAddressLocationModel.setGPSPosition(oUiLatitude.getText().toString(), oUiLongitude.getText().toString());
		}
		return oValueAddressLocationModel;
	}

	/**
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMRelativeLayout#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.setEnabled(true);
		oUiMapButton.setEnabled(true);
		oUiLatitude.setEnabled(true);
		oUiLongitude.setEnabled(true);
	}



	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMRelativeLayout#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.setEnabled(false);
		oUiLatitude.setEnabled(false);
		oUiLongitude.setEnabled(false);
		oUiMapButton.setEnabled(false);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return AddressLocationSVMImpl.class;
	}

	/**
	 * OnClickListener to deal with GPS and Map buttons {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {
		if (p_oParamView.equals(oUiMapButton)) {
			Application.getInstance().getController().doOpenMap(oValueAddressLocation);
		} 
	}

	@Override
	public void configurationUnsetError() {
		if(!this.oUiLatitude.getText().toString().equals(StringUtils.EMPTY) && this.oUiLatitude.getError() != null) {
			this.oUiLatitude.setError(null);
		}
		if(!this.oUiLongitude.getText().toString().equals(StringUtils.EMPTY) && this.oUiLongitude.getError() != null) {
			this.oUiLongitude.setError(null);
		}	
	}

	@Override
	public void configurationSetError(String p_oError) {
		if(this.oUiLatitude.getText().toString().equals(StringUtils.EMPTY)){
			this.oUiLatitude.setError(p_oError);
		}
		if(this.oUiLongitude.getText().toString().equals(StringUtils.EMPTY)){
			this.oUiLongitude.setError(p_oError);
		}
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(AddressLocationSVMImpl p_oObject) {
		return (p_oObject == null || p_oObject.getLatitude()== null || p_oObject.getLatitude()== 0.0 || p_oObject.getLongitude()== null || p_oObject.getLongitude()== 0.0);
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled(java.lang.Object)
	 */
	@Override
	public boolean isFilled() {
		return (oUiLatitude.getText()!=null && !"".equals(oUiLatitude.getText().toString())&& (oUiLongitude.getText()!=null && !"".equals(oUiLongitude.getText().toString())));
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("class", MMPositionTextView.class.getName());
		r_oBundle.putSerializable(POSITION_TEXT_VIEW_VALUE_KEY, configurationGetValue());
		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		String sClass = r_oBundle.getString("class");
		oValueAddressLocationModel = (AddressLocationSVMImpl) r_oBundle.getSerializable(POSITION_TEXT_VIEW_VALUE_KEY);
		configurationSetValue(oValueAddressLocationModel);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
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
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
}
