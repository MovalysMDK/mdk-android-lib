package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * <p>
 * A2A_DEV Décrire la classe LocationCommand
 * </p>
 * use it by :{@code LocationCommand myLocation = new LocationCommand(); private void locationClick() myLocation.getLocation(this, locationResult)); }
 * 
 * public LocationResult locationResult = new LocationResult(){
 * 
 * public void gotLocation(final Location location){ //Got the location! }); } }; }
 * <p>
 * Copyright (c) 2010
 * <p>
 * Company: Adeuza
 * 
 * @author dmaurange
 * 
 */
// A2A_DEV DMA : finaliser l'aquisition et les cas de chagement des capteurs ou absence de capteur
public class MMLocationCommandDialogFragment extends MMDialogFragment implements OnClickListener {

	/**
	 * TODO Décrire le champs LocationCommand.java
	 */
	private static final int UPDATE_TIMER = 500;
	/**
	 * TODO Décrire le champs LocationCommand.java
	 */
	private static final int COARSE_ACCURACY_LEVEL = 1000;
	/**
	 * TODO Décrire le champs LocationCommand.java
	 */
	private static final int FINE_ACCURACY_LEVEL = 50;
	
	public static final String LOCATION_COMMAND_DIALOG_FRAGMENT_TAG = "MMLocationCommandDialogFragmentTag";
	
	private AndroidApplication application = null;
	
	
	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static MMLocationCommandDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		MMLocationCommandDialogFragment oFragment = new MMLocationCommandDialogFragment();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		oFragment.componentId = ((View) p_oDismissListener).getId();
		oFragment.componentFragmentTag = ((ConfigurableVisualComponent<?>) p_oDismissListener).getFragmentTag();
		oFragment.application = (AndroidApplication)(Application.getInstance());

		return oFragment;
	}

	/** the location manager */
	private LocationManager oLocationManager;
	/** Holds the most up to date location */
	private Location oCurrentLocation;
	/** Set to false when location services are unavailable. */
	private boolean bLocationAvailable = true;
	/** Set if the used validate the location or of accuracy conforms woth specified */
	private boolean bLocationAccepted = false;
	/** the Two listeners */
	private LocationListener oListenerFine, oListenerCoarse;

	/** the latitude field in the dialog */
	private TextView oUiLatitude;
	/** the longitude field in the dialog */
	private TextView oUiLongitude;
	/** the accuracy field in the dialog */
	private TextView oUiAccuracy;

	/** The ok button */
	private Button oUiOKButton;
	/** The cancel button */
	private Button oUiCancelButton;

	@Override
	public void onStart() {
		super.onStart();

		if (oLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Criteria oFine = new Criteria();
			oFine.setAccuracy(Criteria.ACCURACY_FINE);

			// Will keep updating about every 500 ms until
			// accuracy is about 50 meters to get accurate fix.
			oLocationManager.requestLocationUpdates(oLocationManager.getBestProvider(oFine, true), UPDATE_TIMER, FINE_ACCURACY_LEVEL, oListenerFine);
		}

		if (oLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Criteria oCoarse = new Criteria();
			oCoarse.setAccuracy(Criteria.ACCURACY_COARSE);

			// Will keep updating about every 500 ms until
			// accuracy is about 1000 meters to get accurate fix.
			// Replace oLocationManager.getBestProvider(oCoarse, true) to 
			oLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_TIMER, COARSE_ACCURACY_LEVEL,
					oListenerCoarse);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStop() {
		if (oLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			oLocationManager.removeUpdates(oListenerFine);
		}
		if (oLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			oLocationManager.removeUpdates(oListenerCoarse);
		}
		super.onStop();
	}

	/**
	 * Gets the current location, use this method on dismiss to to get the result see {@link MMLocationCommandDialogFragment#isCurrentLocationAvaillable()} before
	 * 
	 * @return Objet CurrentLocation {@link Location}
	 */
	public Location getCurrentLocation() {
		return this.oCurrentLocation;
	}

	/**
	 * Check if the current location is usable or if the user pressed the cancel button, use this method before
	 * {@link MMLocationCommandDialogFragment#getCurrentLocation()}
	 * 
	 * @return true if the user valid or accuracy conforms to requested, fals if cancelled
	 */
	public boolean isCurrentLocationAvaillable() {
		return this.bLocationAccepted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer, Bundle p_oSavedInstanceState) {
	super.onCreateView(p_oInflater, p_oContainer, p_oSavedInstanceState);
		View oGlobalView = p_oInflater.inflate(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_position_dialog), null);

//		this.setTitle(this.application.getStringResource(AndroidApplicationR.component_position__positionDialog__title));
		oUiLatitude = (TextView) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_position__positionDialog_latitude__value));
		oUiLongitude = (TextView) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_position__positionDialog_longitude__value));
		oUiAccuracy = (TextView) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_position__positionDialog_accuracy__value));
		oUiOKButton = (Button) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_position__positionDialog_ok__button));
		oUiCancelButton = (Button) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_position__positionDialog_cancel__button));

		oUiOKButton.setOnClickListener(this);
		oUiCancelButton.setOnClickListener(this);
		registerLocationListeners();
		return oGlobalView;
	}

	/**
	 * 
	 * Update the dialog ui to use the last currentLocation
	 */
	protected void onCurrentLocationChange() {
		if (oCurrentLocation != null) {
			oUiLatitude.setText(String.valueOf(oCurrentLocation.getLatitude()));
			oUiLongitude.setText(String.valueOf(oCurrentLocation.getLongitude()));
			oUiAccuracy.setText(String.valueOf(oCurrentLocation.getAccuracy()));
		}
	}

	/**
	 * 
	 * register the 2 listener on Coarse and Fine location
	 */
	private void registerLocationListeners() {
		oLocationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

		// Initialize criteria for location providers
		Criteria oFine = new Criteria();
		oFine.setAccuracy(Criteria.ACCURACY_FINE);
		Criteria oCoarse = new Criteria();
		oCoarse.setAccuracy(Criteria.ACCURACY_COARSE);

		// Get at least something from the device,
		// could be very inaccurate though
		if (oLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			oCurrentLocation = oLocationManager.getLastKnownLocation(oLocationManager.getBestProvider(oFine, true));
			this.onCurrentLocationChange();
		} else {
			Toast.makeText(getActivity(), this.application.getStringResource(AndroidApplicationR.alert_gps_disabled), Toast.LENGTH_LONG).show();
		}
		
		if (oLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			// Replace oLocationManager.getBestProvider(oCoarse, true) to LocationManager.NETWORK_PROVIDER
			oCurrentLocation = oLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			this.onCurrentLocationChange();
		} else{
			Toast.makeText(getActivity(), this.application.getStringResource(AndroidApplicationR.alert_connection_disable), Toast.LENGTH_LONG).show();
		}

		if (oListenerFine == null || oListenerCoarse == null) {
			createLocationListeners();
		}
	}

	/**
	 * Creates LocationListeners
	 */
	private void createLocationListeners() {
		oListenerCoarse = new LocationListener() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onStatusChanged(String p_sProvider, int p_iStatus, Bundle p_oExtras) {
				switch (p_iStatus) {
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					MMLocationCommandDialogFragment.this.bLocationAvailable = false;
					break;
				case LocationProvider.AVAILABLE:
					MMLocationCommandDialogFragment.this.bLocationAvailable = true;
					break;
				default:
					MMLocationCommandDialogFragment.this.bLocationAvailable = false;
				}
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onProviderEnabled(String p_sProvider) {
				// nothing to do
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onProviderDisabled(String p_sProvider) {
				// nothing to do
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onLocationChanged(Location p_oLocation) {
				oCurrentLocation = p_oLocation;
				MMLocationCommandDialogFragment.this.onCurrentLocationChange();
				if (p_oLocation!=null && p_oLocation.getAccuracy() < COARSE_ACCURACY_LEVEL && p_oLocation.hasAccuracy()) {
					oLocationManager.removeUpdates(this);
					MMLocationCommandDialogFragment.this.locationCoarseOk();
				}
			}
		};
		oListenerFine = new LocationListener() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onStatusChanged(String p_sProvider, int p_iStatus, Bundle p_oExtras) {
				switch (p_iStatus) {
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					MMLocationCommandDialogFragment.this.bLocationAvailable = false;
					break;
				case LocationProvider.AVAILABLE:
					MMLocationCommandDialogFragment.this.bLocationAvailable = true;
					break;
				default:
					MMLocationCommandDialogFragment.this.bLocationAvailable = false;
				}
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onProviderEnabled(String p_sProvider) {
				// nothing to do
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onProviderDisabled(String p_sProvider) {
				// nothing to do
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onLocationChanged(Location p_oLocation) {
				oCurrentLocation = p_oLocation;
				MMLocationCommandDialogFragment.this.onCurrentLocationChange();
				if (p_oLocation!=null && p_oLocation.getAccuracy() < FINE_ACCURACY_LEVEL && p_oLocation.hasAccuracy()) {
					oLocationManager.removeUpdates(this);
					MMLocationCommandDialogFragment.this.locationFineOk();
				}
			}
		};
	}

	/**
	 * 
	 * called when the coarse location get the defined accuracy {@link MMLocationCommandDialogFragment#COARSE_ACCURACY_LEVEL}
	 */
	public void locationCoarseOk() {
		Application.getInstance().getLog().debug("LocationCommand", StringUtils.concat("CoarseLocartion ok", String.valueOf(oCurrentLocation.getAccuracy())));
		Application.getInstance().getLog().debug("LocationCommand",
				StringUtils.concat("CoarseLocartion ok-bLocationAvailable :", String.valueOf(bLocationAvailable)));

		// A2A_DEV traiter le cas d'un arret de la dispo des providers avec le bLocationAvailable
		// A2A_DEV faire disparaitre la fenêtre qd coarse ok et que pas de gps
	}

	/**
	 * 
	 * called when the fine location get the defined accuracy {@link MMLocationCommandDialogFragment#FINE_ACCURACY_LEVEL}
	 */
	public void locationFineOk() {
		Application.getInstance().getLog().debug("LocationCommand", StringUtils.concat("FineLocartion ok", String.valueOf(oCurrentLocation.getAccuracy())));
		Application.getInstance().getLog().debug("LocationCommand",
				StringUtils.concat("FineLocartion ok-bLocationAvailable :", String.valueOf(bLocationAvailable)));
		bLocationAccepted = true;
		this.dismiss();
	}

	/**
	 * onClick listeners on OK and Cancel button of the dialog {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {
		if (p_oParamView.equals(oUiOKButton)) {
			bLocationAccepted = true;
			getDismissListener().onDismiss(null);
		} else if (p_oParamView.equals(oUiCancelButton)) {
			bLocationAccepted = false;
			getDismissListener().onDismiss(null);
		}
	}
	
	@Override
	public void onDismiss(DialogInterface p_oDialog) {
		getDismissListener().onDismiss(p_oDialog);
		super.onDismiss(p_oDialog);
	}

	@Override
	public String getFragmentDialogTag() {
		return this.m_sFragmentTag;
	}
}