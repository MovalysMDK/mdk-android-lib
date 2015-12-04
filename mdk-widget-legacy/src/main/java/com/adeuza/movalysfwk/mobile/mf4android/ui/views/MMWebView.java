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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>WebView widget used in the Movalys Mobile product for Android</p>
 */
public class MMWebView extends AbstractMMTableLayout<String> implements DialogInterface.OnClickListener,
	ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {

	/** Location settings activity */
	private static final int LOCATION_SETTINGS_ACTIVITY = 12;
	
	/** Tag for log */
	public static final String TAG = "MMWebView";
	
	/** Maximum cache size for the component */
	public static final int CACHE_SIZE_MB = 8;

	/** the default url of the component */
	private String sDefaultURL = "";

	/** the webview used to display the current page **/
	private WebView oUiWebView = null;
	
	/** the progressbar, displayed only while loading the current page **/
	private ProgressBar oUiProgressBar = null;
	
	/** the layout to display when the WebView is loading content */
	private LinearLayout oUiLoadingLayout = null;
	
	/** the text to display when the WebView is loading content */
	private TextView oUiLoadingText = null;

	/**
	 * Constructs a new MMWebView
	 * @param p_oContext the context to use
	 * @see WebView#WebView(Context)
	 */
	public MMWebView(Context p_oContext) {
		super(p_oContext, String.class);
		if(!isInEditMode()) {
			this.init(p_oContext);
		}
	}
	
	/**
	 * Constructs a new MMWebView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see WebView#WebView(Context, AttributeSet)
	 */
	public MMWebView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs, String.class);
		if(!isInEditMode()) {

			this.init(p_oContext);
			// Dans le cas des MMWebView inclut dans un xml ne faisant pas l'objet d'un binding,
			// il faut écraser le libellé définit dans le xml par l'éventuel libellé présent dans le ConfigurationsHandler (propriété)
			String sUrl = p_oAttrs.getAttributeValue(AndroidApplication.MOVALYSXMLNS, "url");
			if (sUrl != null) {
				this.sDefaultURL = sUrl;
				this.oUiWebView.loadUrl(this.sDefaultURL);
			}
		}
	}

	/**
	 * Initializes the webview
	 * @param p_oContext the context to use
	 */
	private void init(Context p_oContext){
		AndroidApplication oApplication = (AndroidApplication)Application.getInstance() ;
		//oActivity.setContentView(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component__webbrowser));

		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component__webbrowser), this);

		oUiWebView = (WebView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__webbrowser_mainwebview));
		oUiProgressBar = (ProgressBar) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__webbrowser_progressbar));
		oUiLoadingLayout = (LinearLayout) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__webbrowser_loading_layout));
		oUiLoadingText = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__webbrowser_loading_text));

		oUiWebView.getSettings().setJavaScriptEnabled(true);
		oUiWebView.getSettings().setDomStorageEnabled(true);

		// Set cache size to 8 mb by default. should be more than enough
		oUiWebView.getSettings().setAppCacheMaxSize(NumericConstants.NB_BYTES_PER_MB * CACHE_SIZE_MB);		

		// This next one is crazy. It's the DEFAULT location for your app's
		// cache
		// But it didn't work for me without this line.
		// UPDATE: no hardcoded path. Thanks to Kevin Hawkins
		String appCachePath = p_oContext.getApplicationContext().getCacheDir().getAbsolutePath();
		oUiWebView.getSettings().setAppCachePath(appCachePath);
		oUiWebView.getSettings().setAllowFileAccess(true);
		oUiWebView.getSettings().setAppCacheEnabled(true);
		oUiWebView.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);

		oUiWebView.getSettings().setBuiltInZoomControls(false);
		// cache de la localisation
		oUiWebView.getSettings().setGeolocationDatabasePath( this.getContext().getDatabasePath("mf").getAbsolutePath());
		// activiation du navigateur Chrome pour localisation (cf inner class
		// ci-dessous)
		oUiWebView.setWebChromeClient(new MyWebChromeClient(this.getContext()));
		oUiWebView.setWebViewClient(new WebViewClient());
		//verification de la disponibilite de la localisation
		if ( !this.checkLocationProviderEnabled()) {
			buildAlertMessageNoLocation();
		}
	}

	/**
	 * Constructs a new MMWebView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see WebView#WebView(Context, AttributeSet, int)

	public MMWebView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		this.autoHide = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "auto-hide", true);
		//completed by the onFinishInflate method
	}
	 */
	
	/**
	 * This allows to use the W3C location which only exists on the {@link WebChromeClient} at the moment
	 */
	class MyWebChromeClient extends WebChromeClient {
		/** logging tag */
		private static final String LOG_TAG = "MyWebChromeClient";
		
		/** context to use */
		private Context mContext;

		/**
		 * Construct a MyWebChromeClient
		 * @param p_oCaller the context to use
		 */
		public MyWebChromeClient(Context p_oCaller) {
			this.mContext = p_oCaller;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onProgressChanged(WebView p_oView, int p_iProgress) {

			if ( p_oView.getOriginalUrl() == null || !p_oView.getOriginalUrl().startsWith("file:///android_asset/")
				|| oUiWebView.getVisibility() == View.GONE) {
				//activity.setProgress(progress * 100);
				setLoadingMessage(p_iProgress, MMWebView.this.aivDelegate.configurationGetValue());
				if (p_iProgress == NumericConstants.PERCENT_100) {
					oUiLoadingLayout.setVisibility(View.GONE);
					oUiProgressBar.setVisibility(View.INVISIBLE);
					oUiWebView.setVisibility(View.VISIBLE);
				} else {
					if (oUiProgressBar.getVisibility() != View.VISIBLE) {
						oUiProgressBar.setVisibility(View.VISIBLE);
					}
					oUiWebView.setVisibility(View.GONE);
					oUiLoadingLayout.setVisibility(View.VISIBLE);
				}
				//Log.d("progress ", "p " + progress);
				oUiProgressBar.setProgress(p_iProgress);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean onJsAlert(WebView p_oView, String p_sUrl, String p_sMessage, JsResult p_oResult) {
			Log.d(LOG_TAG, p_sMessage);
			// This shows the dialog box. This can be commented out for dev
			try {

				if (this.mContext==null){
					this.mContext=p_oView.getContext();
				}
				if (this.mContext!=null){
					AlertDialog.Builder alertBldr = new AlertDialog.Builder(mContext);
					alertBldr.setMessage(p_sMessage);
					AndroidApplication oApplication = (AndroidApplication)Application.getInstance() ;

					alertBldr.setTitle(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_alert_message_title));
					alertBldr.show();
				}
			} catch (BadTokenException e) {
				Log.e(TAG,"impossible de créer le dialog sur un contexte n'existant plus", e);
			}
			p_oResult.confirm();
			return true;
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(final String p_sOrigin, final GeolocationPermissions.Callback p_oCallback) {
			Log.i(TAG, "onGeolocationPermissionsShowPrompt()");
			Activity oActivity = (Activity) ((AndroidApplication) Application.getInstance()).getCurrentVisibleActivity();
			AndroidApplication oApplication = (AndroidApplication)Application.getInstance() ;

			AlertDialog.Builder builder = new AlertDialog.Builder(oActivity);
			builder.setTitle(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_location_msg_title));
			builder.setMessage(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_location_msg_message))
			.setCancelable(true)
			.setPositiveButton(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_location_msg_ok),
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog,int p_iId) {
					// origin, allow, remember
					p_oCallback.invoke(p_sOrigin, true, true);
				}
			})
			.setNeutralButton(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_location_msg_one),
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog,int p_iId) {
					// origin, allow, remember
					p_oCallback.invoke(p_sOrigin, true, false);
				}
			})
			.setNegativeButton(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_location_msg_ko),
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog,int p_iId) {
					// origin, allow, remember
					p_oCallback.invoke(p_sOrigin, false, false);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}

		@Override
		public void onReachedMaxAppCacheSize(long p_lSpaceNeeded, long p_lTotalUsedQuota, WebStorage.QuotaUpdater p_oQuotaUpdater) {
			p_oQuotaUpdater.updateQuota(p_lSpaceNeeded * 2);
		}
	}

	/**
	 * Check if GPS or network are available for geolocation
	 * @return true if a loaction provider is activated
	 */
	private boolean checkLocationProviderEnabled() {
		LocationManager oLocationMgr = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
		return (oLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| oLocationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
	}

	/**
	 * Displays a dialog redirecting to the device location settings
	 */
	private void buildAlertMessageNoLocation() {
		AndroidApplication oApplication = (AndroidApplication)Application.getInstance() ;

		final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		builder.setTitle(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_location_msg_title))
		.setMessage(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_location_msg_sensor))
		.setCancelable(false)
		.setPositiveButton(oApplication.getStringResource(AndroidApplicationR.generic_message_yes), this)
		.setNegativeButton(oApplication.getStringResource(AndroidApplicationR.generic_message_no), this);

		final AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Set the loading message
	 * @param p_sUrlToLoad The url to load
	 */
	public void setLoadingMessage(String p_sUrlToLoad) {
		AndroidApplication oApplication = (AndroidApplication)Application.getInstance() ;
		this.oUiLoadingText.setText(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_loading_msg) + " : "+ p_sUrlToLoad);
	}

	/**
	 * Sets a loading message with a progression percentage
	 * @param p_iProgress the progress percentage
	 * @param p_sUrlToLoad the url to load
	 */
	private void setLoadingMessage(int p_iProgress, String p_sUrlToLoad) {
		AndroidApplication oApplication = (AndroidApplication)Application.getInstance() ;
		this.oUiLoadingText.setText(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_loading_msg) + " ("+p_iProgress+"%) : " + p_sUrlToLoad);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("url", this.oUiWebView.getUrl());
		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);
		this.aivDelegate.configurationSetValue(r_oBundle.getString("url"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(final DialogInterface p_oDialog,final int p_iWhichButton ) {
		if ( p_iWhichButton == DialogInterface.BUTTON_POSITIVE ){
			((AndroidApplication) Application.getInstance()).startActivityForResult(
					new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),LOCATION_SETTINGS_ACTIVITY);
		}else{
			p_oDialog.cancel();
		}
	}

	/**
	 * Allows to enable/disable JavaScript. 
	 * JavaScript could have bad performances and can be disabled here.
	 * JavaScript is enabled by default.
	 * @param p_bJavaScriptEnabled boolean that indicates if JavaScript should be enable on this component.
	 */
	public void setJavaScriptEnabled(boolean p_bJavaScriptEnabled) {
		this.oUiWebView.getSettings().setJavaScriptEnabled(p_bJavaScriptEnabled);
	}
	
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}
	
	@Override
	public VisualComponentDelegate<String> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/************************************************************************************************
	 ******************************** Framework delegate callback ***********************************
	 ************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		return this.oUiWebView.getUrl();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		if (p_oObjectToSet != null) {
			if (!p_oObjectToSet.equals(this.aivDelegate.configurationGetValue())) {
				this.setLoadingMessage((String) p_oObjectToSet);
				this.oUiWebView.loadUrl((String) p_oObjectToSet);
			}
		} else {
			if (sDefaultURL != null) {
				this.setLoadingMessage(sDefaultURL);
				this.oUiWebView.loadUrl(sDefaultURL);
			} else {
				this.oUiWebView.loadUrl(StringUtils.EMPTY);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.oUiWebView.getUrl().length() > 0;
	}
}
