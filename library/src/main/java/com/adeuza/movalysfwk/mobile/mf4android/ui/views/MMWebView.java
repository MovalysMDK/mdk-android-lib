package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>WebView widget used in the Movalys Mobile product for Android</p>
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author dmaurange
 *
 */
public class MMWebView extends AbstractMMTableLayout<String> implements DialogInterface.OnClickListener {

	/** Location settings activity */
	private static final int LOCATION_SETTINGS_ACTIVITY = 12;
	/** Tag for log */
	public static final String TAG = "MMWebView";
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;
	public static final int CACHE_SIZE_MB = 8;

	private String sDefaultURL = "";

	/** Hide proprity */
	private boolean autoHide;

	/** la webview affichant les sites **/
	private WebView oUiWebView = null;
	/** la progressBar affichee uniquement pendant les chargements **/
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
		super(p_oContext);
		if(!isInEditMode()) {
			this.init(p_oContext);
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
			this.autoHide = true;	
		}
	}
	/**
	 * Constructs a new MMWebView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see WebView#WebView(Context, AttributeSet)
	 */
	public MMWebView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {

			this.init(p_oContext);
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			// Dans le cas des MMWebView inclut dans un xml ne faisant pas l'objet d'un binding,
			// il faut écraser le libellé définit dans le xml par l'éventuel libellé présent dans le ConfigurationsHandler (propriété)
			String sUrl = p_oAttrs.getAttributeValue(AndroidApplication.MOVALYSXMLNS, "url");
			if (sUrl != null) {
				this.sDefaultURL = sUrl;
				this.oUiWebView.loadUrl(this.sDefaultURL);
			}

			this.autoHide = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "auto-hide", true);
		}
	}

	/**
	 * Initialise the webview
	 * @param p_oContext
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
	 * Cette implementation permet d'utilise la location du W3C qui n'existe
	 * pour l'instant que sur les WebChromeClient et pas dans l'implementation
	 * standard du webView
	 * @author dmaurange
	 */
	class MyWebChromeClient extends WebChromeClient {
		private static final String LOG_TAG = "MyWebChromeClient";
		private Context mContext;

		public MyWebChromeClient(Context p_oCaller) {
			this.mContext = p_oCaller;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onProgressChanged(WebView view, int progress) {

			if ( view.getOriginalUrl() == null || !view.getOriginalUrl().startsWith("file:///android_asset/")
				|| oUiWebView.getVisibility() == View.GONE) {
				//activity.setProgress(progress * 100);
				setLoadingMessage(progress, configurationGetValue());
				if (progress == NumericConstants.PERCENT_100) {
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
				oUiProgressBar.setProgress(progress);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult p_oResult) {
			Log.d(LOG_TAG, message);
			// This shows the dialog box. This can be commented out for dev
			try {

				if (this.mContext==null){
					this.mContext=view.getContext();
				}
				if (this.mContext!=null){
					AlertDialog.Builder alertBldr = new AlertDialog.Builder(mContext);
					alertBldr.setMessage(message);
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

		// Autorisation de la localisation
		@Override
		public void onGeolocationPermissionsShowPrompt(final String p_sOrigin,
				final GeolocationPermissions.Callback p_oCallback) {
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

		/**
		 * augmentation de la taille du cache si necessaire.
		 */
		@Override
		public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
			quotaUpdater.updateQuota(spaceNeeded * 2);
		}
	}

	/**
	 * Verifie si le GPS ou le reseau sont utilisables pour la localisation
	 * 
	 * @return true si un des provider est actif
	 */
	private boolean checkLocationProviderEnabled() {
		LocationManager oLocationMgr = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
		return (oLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| oLocationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
	}

	/**
	 * Affiche le dialogue correspondant au renvoi vers les settings du
	 * terminal pour activer la localisation
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
	private void setLoadingMessage(String p_sUrlToLoad) {
		AndroidApplication oApplication = (AndroidApplication)Application.getInstance() ;
		this.oUiLoadingText.setText(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_loading_msg) + " : "+ p_sUrlToLoad);
	}

	private void setLoadingMessage(int p_iProgress, String p_sUrlToLoad) {
		AndroidApplication oApplication = (AndroidApplication)Application.getInstance() ;
		this.oUiLoadingText.setText(oApplication.getStringResource(AndroidApplicationR.component__webbrowser_loading_msg) + " ("+p_iProgress+"%) : " + p_sUrlToLoad);
	}

//	/**
//	 * called wen the inflator finished the job 
//	 */
//	@Override
//	protected void onFinishInflate() {
//		super.onFinishInflate();
//		if (!isInEditMode()){
//			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<String>(this);
//		}
//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivDelegate.setId(p_oId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGroup() {
		return this.aivDelegate.isGroup();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationHide(p_bLockModifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationUnHide(p_bLockModifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(String p_oObject) {
		this.aivDelegate.configurationSetValue(p_oObject);
		if (p_oObject!=null) {
			if(!p_oObject.equals(configurationGetValue())) {
				setLoadingMessage(p_oObject);
				oUiWebView.loadUrl(p_oObject);
			}
		}
		else {
			if (sDefaultURL != null) {
				setLoadingMessage(sDefaultURL);
				oUiWebView.loadUrl(sDefaultURL);
			} else {
				oUiWebView.loadUrl(StringUtils.EMPTY);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet.length == 1) {
			setLoadingMessage(p_oObjectsToSet[0]);
			this.configurationSetValue(p_oObjectsToSet[0]);
		}
		else {
			oUiWebView.loadUrl(StringUtils.EMPTY);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		return oUiWebView.getUrl() ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] { this.configurationGetValue() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLabel() {
		return this.aivDelegate.isLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModel() {
		return this.aivDelegate.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.aivDelegate.getName();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_oObject) {
		return (p_oObject == null || p_oObject.length() == 0) && this.autoHide;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		return this.aivDelegate.isNullOrEmptyCustomValues(p_oObjects);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#hide()
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#unHide()
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return oUiWebView.getUrl().length()>0;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.isShown();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		if (this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationRemoveMandatoryLabel();
		}
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		if (!this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationSetMandatoryLabel();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		//Nothing to do
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.WebView#onSaveInstanceState()
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("url", this.oUiWebView.getUrl());
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.WebView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);
		configurationSetValue(r_oBundle.getString("url"));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
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
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * Allows to enable/disable JavaScript. 
	 * JavaScript could have bad performances and can be disabled here.
	 * JavaScript is enabled by default.
	 * @param p_bJavaScriptEnabled boolean that indicates if JavaScript should be enable on this component.
	 */
	public void setJavaScriptEnabled(boolean p_bJavaScriptEnabled) {
		this.oUiWebView.getSettings().setJavaScriptEnabled(p_bJavaScriptEnabled);
	}
}
