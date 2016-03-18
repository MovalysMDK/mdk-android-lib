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

import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.SPACE;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0001;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0001_1_LABEL;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0002_2_LABEL;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.log.AndroidLoggerImpl;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.FuncDeny;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.FuncGrant;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.PermissionUtil;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.PermissionUtil.PermissionRequestObject;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.AbstractEntityHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoaderError;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DumbProgressHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler;

/**
 * Starts the application 
 *
 */
public class Starter {

	/** Starter permission request code */
	public static final int STATER_PERMISSION_REQUEST = 99;
	/** the context to use */
	private Context context = null;
	
	/**
	 * Construct a new Starter
	 * @param p_oContext the context to use
	 */
	public Starter(Context p_oContext) {
		this.context = p_oContext;
	}
	
	/**
	 * Calls this method to start the application without activity (Junit)
	 * Step :
	 * - configureBeanLoader
	 * - preInit
	 * - initVisualcomponent
	 * - runApplication
	 * - initVisualComponentHandler
	 * - loaded
	 */
	public void runStandalone() {
		this.configureBeanLoader();
		this.configureEntityHelper();
		this.preInit();
		this.initApplication( new DumbProgressHandler());
		Application.getInstance().loadSettings();
		this.runApplication();
		this.initVisualComponentHandler();
		
		// check permissions
		this.checkPermissions();
		
		this.loaded();
	}

	private void checkPermissions() {
		
		PackageManager pm = this.context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(this.context.getPackageName(), PackageManager.GET_PERMISSIONS);
			
			if (pi != null && pi.requestedPermissions != null) {
				List<String> requestPermission = new ArrayList<>();
				
				for (String permission : pi.requestedPermissions) {
					Log.d("ABE", "name:" + permission);
					
					if (ContextCompat.checkSelfPermission(this.context, permission) != PackageManager.PERMISSION_GRANTED
							&& !Application.getInstance().isNotMantatoryRequested(permission)
							&& !permission.equals(StringUtils.EMPTY)) {
						requestPermission.add(permission);
					}
				}
				
				if (!requestPermission.isEmpty()) {
					String[] permissions = new String[] {};
					PermissionRequestObject mRequestObject = PermissionUtil.with((AppCompatActivity) this.context)
							.request(requestPermission.toArray(permissions))
							.onAnyDenied(new FuncDeny() {
								
								@Override
								public void call(String[] deniedPermissions) {
									// bug exit 0...
									System.exit(0);
								}
							}).ask(STATER_PERMISSION_REQUEST);
					((AbstractMMActivity)this.context).setPermissionRequestObject(mRequestObject);
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Flags that application is loaded
	 */
	protected void loaded() {
		Application.getInstance().loaded();
	}
	
	/**
	 * Initialize visual component for autobinding
	 */
	protected void initVisualComponentHandler() {
		Application.getInstance().initializeScreensDescriptor();
	}
	
	/**
	 * Runs initializer
	 */
	protected void runApplication() {
		Application.getInstance().run();
	}
	
	/**
	 * Application init
	 * @param p_oProgressHandlerStep1Impl
	 */
	protected void initApplication( ProgressHandler p_oProgressHandler) {
		Application oAppli = Application.getInstance();
		String sLoggerName = oAppli.getStringResource(FwkPropertyName.logger_name);
		AndroidLoggerImpl logger = AndroidLoggerImpl.getInstance(sLoggerName);
		String sDatabaseName = oAppli.getStringResource(FwkPropertyName.database_name);
		int iDatabaseVersion = oAppli.getIntResource(FwkPropertyName.database_version);
		
		oAppli.init(logger, p_oProgressHandler, sDatabaseName, iDatabaseVersion);
	}

	/**
	 * Application init Logger
	 */
	protected void initLogger() {
		Application oAppli = Application.getInstance();
		String sLoggerName = oAppli.getStringResource(FwkPropertyName.logger_name);
		AndroidLoggerImpl logger = AndroidLoggerImpl.getInstance(sLoggerName);
		oAppli.setLogger(logger);
	}
	
	/**
	 * Load bean configuration
	 */
	protected void configureBeanLoader() {
		BeanLoader.getInstance().clear();
		InputStream oIs = null;
		int iIndex = 0;
		boolean load;
		for (String sPropertiesFile : Application.PROPERTIES_FILES) {
			load = true;
			
			if (!Application.MANDATORY_PROPERTIES_FILES[iIndex]) {
				int loadId = context.getResources().getIdentifier(sPropertiesFile, "string", context.getPackageName());
				load = ! ("false".equals(context.getResources().getString(loadId))); // true by default
			}
			
			if (load) {
				oIs = this.openPropertiesInputStream(sPropertiesFile, Application.MANDATORY_PROPERTIES_FILES[iIndex]);
				if (oIs != null) {
					try {
						BeanLoader.getInstance().initialize(oIs);
					}
					catch (BeanLoaderError e) {
						String oMessage = StringUtils.concat(FWK_MOBILE_E_0001_1_LABEL, sPropertiesFile, SPACE, FWK_MOBILE_E_0002_2_LABEL);
						throw new MobileFwkException(FWK_MOBILE_E_0001, oMessage, e);
					}
					finally {
						try {
							oIs.close();
						}
						catch (IOException e) {
							String oMessage = StringUtils.concat(FWK_MOBILE_E_0001_1_LABEL, sPropertiesFile, SPACE, FWK_MOBILE_E_0002_2_LABEL);
							throw new MobileFwkException(FWK_MOBILE_E_0001, oMessage,e);
						}
					}
				}
			}
			iIndex++;
		}
	}
	
	/**
	 * Initialize EntityFactoryUtils
	 */
	protected void configureEntityHelper() {
		for (String sEntityPropertiesFile : Application.ENTITYHELPER_PROPERTIES_FILES) {
			InputStream oIs = this.openPropertiesInputStream(sEntityPropertiesFile, true );
			AbstractEntityHelper.getInstance().initialize(oIs);
		}
	}
	
	/**
	 * Preninit android (log)
	 */
	protected void preInit() {
		AndroidApplication oAppli = (AndroidApplication) Application.getInstance();
		oAppli.androidPreInit(this.context.getPackageName());
	}

	/**
	 * Load stream for analyse properties
	 */
	private InputStream openPropertiesInputStream(String p_sPropertiesName, boolean p_bMandatory) {
		/* Lecture des fichiers dans le répertoire /assets du projet courant */
		InputStream r_oStream = null;
		try {
			
			int rawResource = this.context.getResources().getIdentifier( p_sPropertiesName, "raw", this.context.getPackageName());
			r_oStream = context.getResources().openRawResource(rawResource);
			//r_oStream = this.context.getResources().getAssets().open(p_sPropertiesName);
		}
		catch (Resources.NotFoundException e ) {
			if (p_bMandatory) {
				String oMessage = StringUtils.concat(FWK_MOBILE_E_0001_1_LABEL, p_sPropertiesName, SPACE, FWK_MOBILE_E_0002_2_LABEL);
				throw new MobileFwkException(FWK_MOBILE_E_0001, oMessage,e);
			}
		}
		return r_oStream;
	}
}
