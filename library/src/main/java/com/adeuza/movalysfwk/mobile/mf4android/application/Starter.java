package com.adeuza.movalysfwk.mobile.mf4android.application;

import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.SPACE;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0001;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0001_1_LABEL;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0002_2_LABEL;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.log.AndroidLoggerImpl;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.descriptor.AndroidVisualComponentDescriptorHelperImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoaderError;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.EntityHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DumbProgressHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler;

/**
 * Starts the application 
 * @author smaitre
 *
 */
public class Starter {

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
		this.initVisualComponent();
		Application.getInstance().loadSettings();
		this.runApplication();
		this.initVisualComponentHandler();
		this.loaded();
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
		VisualComponentDescriptorsHandler.getInstance().init(new AndroidVisualComponentDescriptorHelperImpl());
		Application.getInstance().initializeScreensDescriptor();
	}
	
	/**
	 * Runs initializer
	 */
	protected void runApplication() {
		Application.getInstance().run();
	}
	
	/**
	 * Initialize visual component for autobinding (step2)
	 */
	protected void initVisualComponent() {
		VisualComponentDescriptorsHandler.getInstance().init(new AndroidVisualComponentDescriptorHelperImpl());
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
	 * Load bean configuration
	 */
	protected void configureBeanLoader() {
		BeanLoader.getInstance().clear();
		InputStream oIs = null;
		int iIndex = 0;
		for (String sPropertiesFile : Application.PROPERTIES_FILES) {		
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
			iIndex++;
		}
	}
	
	/**
	 * Initialize EntityFactoryUtils
	 */
	protected void configureEntityHelper() {
		for (String sEntityPropertiesFile : Application.ENTITYHELPER_PROPERTIES_FILES) {
			InputStream oIs = this.openPropertiesInputStream(sEntityPropertiesFile, true );
			EntityHelper.getInstance().initialize(oIs);
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
