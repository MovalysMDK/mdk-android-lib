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
package com.adeuza.movalysfwk.mobile.mf4android.application;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationMain;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT;

/**
 * <p>MovalysIntervention temporary activity</p>
 * 
 *
 */
public class ApplicationMainImpl extends AbstractMMActivity implements ApplicationMain { 

	/**
	 * Step nummber init
	 */
	private static final int NUMBER_STEP = 5;
	/**
	 * The number of the first progress step in the first part of the initialisation of the application. 
	 */
	protected static final int FIRST_PART_STARTUP_PROGRESS_STEP = 0;
	/**
	 * The number of the first progress step in the second part of the initialisation of the application. 
	 */
	protected static final int SECOND_PART_STARTUP_PROGRESS_STEP = 3;
	
	/** TExt view containg the version numero */
	private TextView oUiTextVersion;
	/** 
	 * The SplashScreen's ProgressBar Widget.
	 */
	private ProgressBar progressBarWidget;
	/**
	 * The progressBar list to update
	 */
	private List<ProgressBar> progressBarList ;
	/** 
	 * The first async. part of the application startup 
	 */
	private WeakReference<ProgressHandlerStep1> progressHandlerStep1;
	/** 
	 * The second async. part of the application startup 
	 */
	private WeakReference<ProgressHandlerStep2> progressHandlerStep2;
	
	/** Starter */
	private Starter starter ;
	/** Map Rnci */
	private Map<String, Object> customConfigurationInstance;
	
	/** 
	 * <p>Called when the activity is first created.</p> 
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(final Bundle p_oSavedInstState) {
		
		// Test if this is the intent to exit application
		if ( this.shouldActivityBeStopped()) {
			super.onCreate(p_oSavedInstState);
			return;
		}
		
		//@SuppressWarnings("unchecked")
		this.customConfigurationInstance = (Map<String, Object>) this.getLastCustomNonConfigurationInstance();
		if (this.customConfigurationInstance != null) {
			this.starter = (Starter) this.customConfigurationInstance.get("starter");
		}
		else {
			this.starter = new Starter(this);
		}
		
		//Step 1 : Configuration of class BeanLoader
		if (this.customConfigurationInstance == null) {
			this.starter.configureBeanLoader();
			this.starter.configureEntityHelper();
			this.starter.preInit();
		}
		
		//Step 2 : in onCreate instanciate application object
		super.onCreate(p_oSavedInstState);
		// on cache l'actionbar
		ActionBar oActionBar = this.getSupportActionBar();
		if (oActionBar != null) {
			oActionBar.hide();
		}

		// uniquement dans le cas de l'activité principale, à faire avant le run pour pouvoir initialiser le context android
		Application.getInstance().reset();
		
		this.setContentView(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.fwk_screen_splash));

		this.initUIVersionNumber();

		//initialisation de la progress bar 
		this.progressBarWidget=(ProgressBar)this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.screen_splash_progress));
		this.progressBarWidget.setProgress(FIRST_PART_STARTUP_PROGRESS_STEP);
		
		//initialisation de la liste des progress bar à incrémenter
		this.progressBarList = new ArrayList<>();
		this.progressBarList.add(this.progressBarWidget);
	
		// Show/Hide splashscreen logo		
		this.initUILogoCopyright();
	}

	/**
	 * Init logo and copyright
	 */
	protected void initUILogoCopyright() {
		if ( this.getAndroidApplication().getStringResource(AndroidApplicationR.splashScreen_logoCopyright_hidden)
				.equalsIgnoreCase("true")) {

			View oSplashCopyright = this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.screen_splash_copyright));
			oSplashCopyright.setVisibility(View.GONE);
			View oSplashLogo = this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.screen_splash_logo));
			oSplashLogo.setVisibility(View.GONE);
		}
	}

	/**
	 * Initialisation du numéro de version
	 */
	protected void initUIVersionNumber() {
		oUiTextVersion=(TextView)this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.screen_splash_label));
		String sVersion;
		try {
			PackageInfo oPinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			sVersion=oPinfo.versionName;
		} catch (NameNotFoundException e) {
			sVersion=StringUtils.EMPTY;
		}
		oUiTextVersion.setText(this.getResources().getString(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.screen_splash_version), sVersion));
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle p_oSavedInstanceState) {
		super.onPostCreate(p_oSavedInstanceState);

		if ( customConfigurationInstance == null ) {
			// pré-intialisation de l'application 
			ProgressHandlerStep1 oProgressHandlerStep1 = new ProgressHandlerStep1();
			this.initProgressHandlerStep1(oProgressHandlerStep1);
			this.starter.initApplication(oProgressHandlerStep1);
			
			// lancement du thread qui initialise l'application et incrémente la progressBar
			((AndroidApplication) Application.getInstance()).execAsyncTask(oProgressHandlerStep1);
		}
		else {
			// cas d'une rotation
			Map<String,Object> oConfig = (Map<String, Object>) customConfigurationInstance.get("config");
			if ( oConfig.get("initStep1") != null ) {
				this.initProgressHandlerStep1((ProgressHandlerStep1) oConfig.get("initStep1"));
			}
			if ( oConfig.get("initStep2") !=null ) {
				this.initProgressHandlerStep2((ProgressHandlerStep2) oConfig.get("initStep2"));
			}
		}
		
		this.initUILogoCopyright();
	}
	
	protected void initProgressHandlerStep1( ProgressHandlerStep1 p_oProgressHandlerStep1 ) {
		this.progressHandlerStep1 = new WeakReference<>(p_oProgressHandlerStep1);
		p_oProgressHandlerStep1.attach(this);
		p_oProgressHandlerStep1.init( this.progressBarList, null);
	}
	
	protected void initProgressHandlerStep2( ProgressHandlerStep2 p_oProgressHandlerStep2 ) {
		this.progressHandlerStep2 = new WeakReference<>(p_oProgressHandlerStep2);
		p_oProgressHandlerStep2.attach(this);
		p_oProgressHandlerStep2.init( this.progressBarList, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> onRetainCustomNonConfigurationInstance() {
		Map<String, Object> r_oMap = super.onRetainCustomNonConfigurationInstance();
		r_oMap.put("config", initConfigurationChanged(String.valueOf(this.hashCode())));
		r_oMap.put("starter", this.starter);
		return r_oMap;
	}
	
	/**
	 * Init de la configuration à sauvegarder même après rotation
	 * @param p_sKey la clé de la conf
	 * @return une map de paramètres à sauvegarder
	 */
	protected Map<String,Object> initConfigurationChanged(String p_sKey){
		Map<String, Object> r_oResult = new HashMap<>();
		if (this.progressHandlerStep1 != null && this.progressHandlerStep1.get() != null ) {
			r_oResult.put("initStep1", this.progressHandlerStep1.get());
		}
		if (this.progressHandlerStep2 != null && this.progressHandlerStep2.get() != null) {
			r_oResult.put("initStep2", this.progressHandlerStep2.get());
		}
		return r_oResult;
	}
	
	/**
	 * During the application startup, a special menu must be displayed.
	 * Each button of this menu can be displayed or not using a property.
	 * The properties are store by the ConfigurationsHandler.
	 * This handler must be loaded before the read of properties.
	 * This methods inflates the menu, but all buttons are not visible.
	 * This is the {@link #onPrepareOptionsMenu(Menu)} whose read the properties after the initializers execution.
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu p_oMenu) {
		new MenuInflater(this.getApplication()).inflate(this.getAndroidApplication()
				.getAndroidIdByRKey(AndroidApplicationR.menu_startup), p_oMenu);

		this.inflateMenu(p_oMenu);

		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu p_oMenu) {
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		if (oApplication.isInitializersExecuted()) {
			if (this.progressHandlerStep1 != null && this.progressHandlerStep1.get() != null && this.progressHandlerStep1.get().isCancel() 
					|| this.progressHandlerStep2 != null &&  this.progressHandlerStep2.get() != null && this.progressHandlerStep2.get().isCancel()){

				this.setVisible(p_oMenu, AndroidApplicationR.menu_base_doRestartApplicationStartup);
				this.setVisible(p_oMenu, AndroidApplicationR.menu_base_configuration_during_application_stop);
				this.setVisible(p_oMenu, AndroidApplicationR.menu_base_doDisplayExitApplicationDialog);
			}
			else {
				this.setVisible(p_oMenu, AndroidApplicationR.menu_base_doStopApplicationStartup);
			}
		}
		return super.onPrepareOptionsMenu(p_oMenu);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void analyseSettings() {
		super.analyseSettings();
		if (Application.getInstance().displayMandatorySettingWindow() 
				&& Application.getInstance().hasUndefinedMandatorySetting()) {
			this.doOnSettingProblems();
		}else{
			//execution du deuxième Thread qui va terminer l'initialisation de l'application
			ProgressHandlerStep2 oProgressHandlerStep2 = new ProgressHandlerStep2();
			initProgressHandlerStep2(oProgressHandlerStep2);
			Application.getInstance().setProgressHandler(oProgressHandlerStep2);
			((AndroidApplication) Application.getInstance()).execAsyncTask(oProgressHandlerStep2);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSettingProblems() {
		//affichage de l'activité qui redirige l'utilisateur soit hors de l'application, soit sur la page de paramétage.
		Application.getInstance().getController().doDisplayParameterDialog();
	}
		
	/**
	 * This method stop the application startup.
	 */
	public void stopApplicationStartup() {
		if (this.progressHandlerStep1.get() != null ) {
			this.progressHandlerStep1.get().cancel(true);
		}
		if (this.progressHandlerStep2.get()!=null) {
			this.progressHandlerStep2.get().cancel(true);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.progressHandlerStep1 != null && this.progressHandlerStep1.get() != null ) {
			this.progressHandlerStep1.get().init( null, null);
		}
		if (this.progressHandlerStep2 != null && this.progressHandlerStep2.get()!=null) {
			this.progressHandlerStep2.get().init( null, null);
		}
	}
	
	/**
	 * @return
	 */
	protected int getStepCount() {
		return NUMBER_STEP + Application.getInstance().getInitsNumber();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		//we disabled the back key
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#doOnSynchroFinish(com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT)
	 */
	@Override
	protected void doOnSynchroFinish(SynchronizationActionParameterOUT p_oResult) {
		super.doOnSynchroFinish(p_oResult);

		if (p_oResult == null 
				|| !p_oResult.authenticationFailure && !p_oResult.brokenSynchronizationFailure
				&& !p_oResult.emptyDBSynchronizationFailure && !p_oResult.errorInSynchronizationFailure
				&& !p_oResult.noConnectionSynchronizationFailure && !p_oResult.waitedTooLongBeforeSync) {

			if (!Application.getInstance().isSyncTransparentEnabled()) {
				startMainScreen();
			}
		}
	}

	/**
	 * Start main screen of application (the one after the splashscreen)
	 */
	protected void startMainScreen() {
		Application.getInstance().getController().doDisplayMain(this);
	}

	protected Starter getStarter() {
		return this.starter;
	}	
}
