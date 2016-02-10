package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.MMLocationCommandDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
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
 * 
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * 
 * @author dmaurange
 * @since Barcelone
 */
public class MMPositionEditText extends AbstractMMRelativeLayout<AddressLocationSVMImpl> 
implements OnClickListener, OnDismissListener, MMComplexeComponent{

	/** the latitude field on the panel
	 *  Note: Don't replace the type by the native one else u will have some bugs on save/restore instance
	 */
	private EditText oUiLatitude;
	/** the longitude field on the panel 
	 * 	Note: Don't replace the type by the native one else u will have some bugs on save/restore instance 
	 */
	private EditText oUiLongitude;
	/** the label above fields on the panel */
	private TextView oUiLabel;
	/** the label of Latitude field on the panel */
	private TextView oUiLatitudeLabel;
	/** the label of Longitude field on the panel */
	private TextView oUiLongitudeLabel;
	/** the map button on the panel */
	private ImageButton oUiMapButton;
	/** the gps button on the panel */
	private ImageButton oUiGPSButton;
	/** ValueObject location used to open the map */
	private AddressLocation oValueAddressLocation = new AddressLocation();
	/** View Model of the location */
	private AddressLocationSVMImpl oValueAddressLocationModel;
	/** the Location command used to locate the device with network and gps sensors */
	private MMLocationCommandDialogFragment oLocationCommand;
	/** the key used to retain the value during orientation changes */
	private static final String POSITION_EDIT_TEXT_VALUE_KEY = "positionEditTextValueKey";

	private boolean writingData;

	private boolean latitudeFilled, longitudeFilled;

	private String sLocationDialogTag;


	/**
	 * Construct a new MMPositionEditText component
	 * @param p_oContext
	 *            the current context
	 */
	public MMPositionEditText(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {

			this.writingData = false;
		}
	}	
	/**
	 * Construct a new MMPositionEditText component
	 * @param p_oContext
	 *            the current context
	 * @param p_oAttrs
	 *            xml attributes
	 */
	public MMPositionEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			linkChildrens(p_oAttrs);
			this.writingData = false;
		}
	}
	/**
	 * link the child views with custom attributes and add button onClickListener
	 * @param p_oAttrs
	 *            attributes of XML Component component_phone_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		if(!isInEditMode()) {
			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_position_edittext), this);

			oUiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position__label));
			oUiLatitudeLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_latitude__label));
			oUiLongitudeLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_longitude__label));
			oUiLatitude = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_latitude__value));
			oUiLongitude = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_longitude__value));
			oUiMapButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_map__button));
			oUiGPSButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_position__position_gps__button));

			try {
				oUiLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0)));
			} catch (NotFoundException e) {
				Application.getInstance().getLog().debug("MMPositionEditText",
						StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0))));
			}
			try {
				oUiLatitudeLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "labelLatitude", 0)));
			} catch (NotFoundException e) {
				Application.getInstance().getLog().debug("MMPositionEditText",
						StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "labelLatitude", 0))));
			}
			try {
				oUiLongitudeLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "labelLongitude", 0)));
			} catch (NotFoundException e) {
				Application.getInstance().getLog()
				.debug("MMPositionEditText",
						StringUtils.concat("Ressource not found",
								String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "labelLongitude", 0))));
			}
			//seulement lorsqu'une valeur non vides est settée
			//oUiMapButton.setOnClickListener(this);
			oUiGPSButton.setOnClickListener(this);
			oUiLatitude.addTextChangedListener(this.latitudeChanged);
			oUiLongitude.addTextChangedListener(this.longitudeChanged);

			latitudeFilled = false;
			longitudeFilled = false;
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

		this.writingData = true;

		if (p_oAddressLocation!=null) {
			oValueAddressLocationModel= new AddressLocationSVMImpl(p_oAddressLocation);

			if (null != p_oAddressLocation.getLatitude()) {
				oUiLatitude.setText(String.valueOf(p_oAddressLocation.getLatitude()));
				
				int iOldStart = oUiLatitude.getSelectionStart();
				int iOldStop = oUiLatitude.getSelectionEnd();
				iOldStart = Math.min(iOldStart, oUiLatitude.length());
				iOldStop = Math.min(iOldStop, oUiLatitude.length());
				setEditTextSelection(oUiLatitude, iOldStart, iOldStop);
				latitudeFilled = true;
			}else{
				oUiLatitude.setText(StringUtils.EMPTY);
				oUiMapButton.setEnabled(false);
				latitudeFilled = false;
			}

			if (null != p_oAddressLocation.getLongitude()) {
				oUiLongitude.setText(String.valueOf(p_oAddressLocation.getLongitude()));
				
				int iOldStart = oUiLongitude.getSelectionStart();
				int iOldStop = oUiLongitude.getSelectionEnd();
				iOldStart = Math.min(iOldStart, oUiLongitude.length());
				iOldStop = Math.min(iOldStop, oUiLongitude.length());
				setEditTextSelection(oUiLongitude, iOldStart, iOldStop);
				longitudeFilled = true;
			}else{
				oUiLongitude.setText(StringUtils.EMPTY);
				oUiMapButton.setEnabled(false);
				longitudeFilled = false;
			}

			if(oValueAddressLocationModel.isAcurate())
			{
				oUiMapButton.setEnabled(true);
				oUiMapButton.setOnClickListener(this);
			}
		}
		else{
			latitudeFilled = false;
			longitudeFilled = false;
			oUiMapButton.setEnabled(false);
		}

		checkIsFilled();
		this.writingData = false;
	}

	private void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
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
			oValueAddressLocationModel.setGPSPosition(this.oUiLatitude.getText().toString(),
					this.oUiLongitude.getText().toString());
		}
		return new AddressLocationSVMImpl(oValueAddressLocationModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean p_bEnabled) {
		super.setEnabled(p_bEnabled);
		oUiGPSButton.setEnabled(p_bEnabled);
		oUiMapButton.setEnabled(isFilled());
		oUiLatitude.setEnabled(p_bEnabled);
		oUiLatitude.setFocusable(p_bEnabled);
		oUiLatitude.setFocusableInTouchMode(p_bEnabled);
		oUiLongitude.setEnabled(p_bEnabled);
		oUiLongitude.setFocusable(p_bEnabled);
		oUiLongitude.setFocusableInTouchMode(p_bEnabled);
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
		if (p_oParamView.equals(oUiMapButton)) {
			if (this.oUiLatitude.length() > 0 && this.oUiLongitude.length() > 0) {
				oValueAddressLocation.setLatitude(Double.parseDouble(this.oUiLatitude.getText().toString()));
				oValueAddressLocation.setLongitude(Double.parseDouble(this.oUiLongitude.getText().toString()));

				Application.getInstance().getController().doOpenMap(oValueAddressLocation);
			}
		} else if (p_oParamView.equals(oUiGPSButton)) {
			// création du dialog sur l'activité voir onCreateDialog()
			this.oLocationCommand = (MMLocationCommandDialogFragment) createDialogFragment(null);
			this.sLocationDialogTag = this.oLocationCommand.getFragmentDialogTag();
			prepareDialogFragment(this.oLocationCommand, null);
			this.oLocationCommand.show(getFragmentActivityContext().getSupportFragmentManager(), oLocationCommand.getFragmentDialogTag());
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
		return (p_oObject.getLatitude()==null || p_oObject.getLongitude()==null);
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled(java.lang.Object)
	@Override
	public boolean isFilled() {
		return this.aivDelegate.isFilled();
	}


	/**
	 * Checks if the component is filled
	 * @return
	 */
	private void checkIsFilled() {
		this.aivDelegate.setFilled(latitudeFilled && longitudeFilled);		
	}

	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}



	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		MMDialogFragment oDialogFragment = MMLocationCommandDialogFragment.newInstance(this);
		return oDialogFragment;
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		p_oDialog.setCancelable(false);
	}

	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode,
			Intent p_oIntent) {
		// Nothing to do
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("class", MMPositionTextView.class.getName());
		r_oBundle.putSerializable(POSITION_EDIT_TEXT_VALUE_KEY, configurationGetValue());
		r_oBundle.putString("dialogTag", this.sLocationDialogTag);
		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		String sClass = r_oBundle.getString("class");
		oValueAddressLocationModel = (AddressLocationSVMImpl) r_oBundle.getSerializable(POSITION_EDIT_TEXT_VALUE_KEY);
		configurationSetValue(oValueAddressLocationModel);
		String dialogTag = r_oBundle.getString("dialogTag");
		if(dialogTag != null) {
			this.sLocationDialogTag = dialogTag;
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			this.oLocationCommand = (MMLocationCommandDialogFragment) ((AbstractMMActivity)oApplication.getCurrentActivity()).getSupportFragmentManager().findFragmentByTag(dialogTag);
		}
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

	private TextWatcher latitudeChanged = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence p_sS, int p_iStart, int p_iBefore, int p_iCount) {
			if (!MMPositionEditText.this.writingData ) {
				latitudeFilled = (p_sS.toString() != null && !p_sS.toString().equals(StringUtils.EMPTY));
				MMPositionEditText.this.checkIsFilled();
				MMPositionEditText.this.aivDelegate.changed();
			}

		}

		@Override
		public void beforeTextChanged(CharSequence p_sS, int p_iStart, int p_iCount,
				int p_iAfter) {
			// Nothing to do

		}

		@Override
		public void afterTextChanged(Editable p_oS) {
			// Nothing to do

		}
	};

	private TextWatcher longitudeChanged = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence p_sS, int p_iStart, int p_iBefore, int p_iCount) {
			if (!MMPositionEditText.this.writingData ) {
				longitudeFilled = (p_sS.toString() != null && !p_sS.toString().equals(StringUtils.EMPTY));
				MMPositionEditText.this.checkIsFilled();
				MMPositionEditText.this.aivDelegate.changed();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence p_sS, int p_iStart, int p_iCount,
				int p_iAfter) {
			// Nothing to do

		}

		@Override
		public void afterTextChanged(Editable p_oS) {
			// Nothing to do

		}
	};

}
