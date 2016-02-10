package com.adeuza.movalysfwk.mobile.mf4android.application;

import static com.adeuza.movalysfwk.mobile.mf4android.messages.AndroidErrorDefinition.MM_MOBILE_E_0100;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v4.util.SparseArrayCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabWidget;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.configuration.loader.ConfigurationLoader;
import com.adeuza.movalysfwk.mobile.mf4android.configuration.loader.MultiSectionConfigurationLoader;
import com.adeuza.movalysfwk.mobile.mf4android.configuration.loader.MultiSectionFragmentConfigurationLoader;
import com.adeuza.movalysfwk.mobile.mf4android.configuration.loader.WorkspaceConfigurationLoader;
import com.adeuza.movalysfwk.mobile.mf4android.configuration.loader.WorkspaceFragmentConfigurationLoader;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4android.helper.SynchroNotificationHelper;
import com.adeuza.movalysfwk.mobile.mf4android.network.NetworkHelper;
import com.adeuza.movalysfwk.mobile.mf4android.serialization.SparseArrayIntArrayParcelable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMultiSectionFragmentLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMultiSectionLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceDetailLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceMasterDetailFragmentLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceMasterDetailLayout;
import com.adeuza.movalysfwk.mobile.mf4android.utils.security.LocalAuthHelper;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkController;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.network.ConnectionType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractNamableObject;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.TheoriticalVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;

/**
 * <p>Application Android's implementation</p>
 * 
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * 
 * @author smaitre
 */
public class AndroidApplication extends Application {
	
	/** tv size of the screen */
	private static final int WIDTHPIXEL_TV_1200 = 1200;
	
	/** xLarge size of the screen */
	private static final int WIDTHPIXEL_XHIGHT_1300 = 1300;
	/** xLarge size of the screen */
	private static final int WIDTHPIXEL_XHIGHT_CAP = 1900;

	/** the name space of android. Used to retreive attribute's value. */
	public static final String ANDROID_XML_NAMESPACE = "http://schemas.android.com/apk/res/android";

	// valeurs de références pour le calcul du nombre de colones de l'écran
	// ces valeurs correspondent aux différentes tailles d'écran envisagées
	/** Lower size of the screen */
	private static final int WIDTHPIXEL_LOWER = 480;
	/** Medium size of the screen */
	private static final int WIDTHPIXEL_MEDIUM600 = 600;

	/** Medium size of the screen */
	private static final int WIDTHPIXEL_MEDIUM800 = 800;
	/** Max size of the screen */
	private static final int WIDTHPIXEL_CAP = 1024;

	/** the 1 column result of {@link getScreenColumnNumber} **/
	private static final int SCREEN_ONE_COLUMN=1;
	/** the 2 columns result of {@link getScreenColumnNumber} **/
	private static final int SCREEN_TWO_COLUMN=2;
	/** the 3 columns result of {@link getScreenColumnNumber} **/
	private static final int SCREEN_THREE_COLUMN=3;
	/** the 4 columns result of {@link getScreenColumnNumber} **/
	private static final int SCREEN_FOUR_COLUMN=4;

	/** tag merge */
	private static final String XML_TAG_MERGE = "merge";
	/** tag include */
	private static final String XML_TAG_INCLUDE = "include";
	/** parameter name layout */
	private static final String XML_PARAMETER_LAYOUT = "layout";
	/** parameter name id */
	private static final String XML_PARAMETER_ID = "id";
	/** parameter name entity */
	private static final String XML_PARAMETER_ENTITY = "entity";
	/** parameter name mandatory */
	private static final String XML_PARAMETER_MANDATORY = "mandatory";
	/** parameter minsize */
	private static final String XML_PARAMETER_MINSIZE = "minsize";
	/** parameter group */
	private static final String XML_PARAMETER_GROUP = "group";

	/** default id */
	private static final String XML_DEFAULT_ID = "ANDROIDID";
	/** slash */
	private static final String XML_SLASH = "/";

	/** Name of the application package (cf. manifest) */
	private String mainPackage = null;
	/** link between android id and text id for a visual component */
	private SparseArrayCompat<String> androidIdInt2String = null;
	/** link between android id and text id for a visual component */
	private Map<String, Integer> androidIdString2Int = null;

	/** Dictionary to store the dialog reference : 
	 * some component need more than one dialog,
	 * long[0] : the view Id, long[1] the dialog id specified in the MMComponent*/
	private SparseArrayIntArrayParcelable dialogDictionary=new SparseArrayIntArrayParcelable();
	/** Dictionary to store the activity reference : 
	 * some component need more than one activity,
	 * long[0] : the component Id, long[1] the request code*/
	private SparseArrayIntArrayParcelable screenDictionary = new SparseArrayIntArrayParcelable();
	/** Configuration loaders map with keys */
	private Map<String, ConfigurationLoader> configurationLoaders = new HashMap<>();


	/** used to generate id */
	private int genId = 1;

	/** layout */
	private Class<?> androidRlayout = null;

	/** The application's context */
	private MFAndroidApplication application ;

	public AndroidApplication() {
		super();
	}

	/**
	 * return the application's context.
	 * @return context
	 */
	public MFAndroidApplication getApplication() {
		if ( application == null) {
			application = MFApplicationHolder.getInstance().getApplication();
		}
		return application;
	}

	/**
	 * return the application's context.
	 * @return context
	 */
	public Context getApplicationContext() {
		return getApplication().getApplicationContext();
	}

	public Activity getCurrentVisibleActivity() {
		return getApplication().getActivityListener().getCurrentVisibleActivity();
	}

	//	public LayoutInflater getLayoutInflater() {
	//		return this.getCurrentActivity().getLayoutInflater();
	//	}

	/**
	 * Return the object <em>dialogDictionary</em>.
	 * @return Objet dialogDictionary
	 */
	public SparseArrayIntArrayParcelable getDialogDictionary() {
		return this.dialogDictionary;
	}

	/**
	 * Return the object <em>screenDictionary</em>.
	 * @return Objet screenDictionary
	 */	
	public SparseArrayIntArrayParcelable getScreenDictionary() {
		return this.screenDictionary;
	}

	/**
	 * Initializes the android application
	 * @param p_sMainPackage Main package of the application.
	 */
	public void androidPreInit(String p_sMainPackage) {

		if (androidIdInt2String==null) {
			this.mainPackage = p_sMainPackage;
			AndroidRLoader oRLoader = new AndroidRLoader(p_sMainPackage);
			oRLoader.loadAndCheck();

			this.androidIdInt2String	= oRLoader.getAndroidIdInt2String();
			this.androidIdString2Int	= oRLoader.getAndroidIdString2Int();
			this.androidRlayout			= oRLoader.getAndroidRlayout();

			this.configurationLoaders.put(MMWorkspaceLayout.class.getName(), new WorkspaceConfigurationLoader());
			this.configurationLoaders.put(MMWorkspaceMasterDetailLayout.class.getName(), new WorkspaceConfigurationLoader());
			this.configurationLoaders.put(MMWorkspaceMasterDetailFragmentLayout.class.getName(), new WorkspaceFragmentConfigurationLoader());
			this.configurationLoaders.put(MMWorkspaceDetailLayout.class.getName(), new WorkspaceConfigurationLoader());
			this.configurationLoaders.put(MMMultiSectionLayout.class.getName(), new MultiSectionConfigurationLoader());
			this.configurationLoaders.put(MMMultiSectionFragmentLayout.class.getName(), new MultiSectionFragmentConfigurationLoader());
		}
	}

	/**
	 * Permet de savoir si on peut utiliser la connexion wifi et gprs.
	 * @return true if the wifi or gprs connection is ready
	 */
	@Override
	public boolean isConnectionReady() {
		boolean r_bResult = false;
		ConnectivityManager oCM = ((ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
		if (oCM.getActiveNetworkInfo() == null){
			this.getLog().info(ConnectivityManager.class.getSimpleName(), "Data connection state: Not Connected.");
			this.getLog().info(ConnectivityManager.class.getSimpleName(), ">>> no active network");
			r_bResult = false;
		}else if (oCM.getActiveNetworkInfo().isConnectedOrConnecting()){
			this.getLog().info(ConnectivityManager.class.getSimpleName(), "Data connection state: Connected.");
			r_bResult = true;

		}else if (oCM.getActiveNetworkInfo().isFailover()){
			this.getLog().info(ConnectivityManager.class.getSimpleName(), "Data connection state: Not Connected.");
			this.getLog().info(ConnectivityManager.class.getSimpleName(), ">>> data state = FailOver");
			r_bResult = false;

		}else if (oCM.getActiveNetworkInfo().isRoaming()){
			this.getLog().info(ConnectivityManager.class.getSimpleName(), "Data connection state: Not Connected.");
			this.getLog().info(ConnectivityManager.class.getSimpleName(), ">>> data state = Roaming");
			//A2A_DEV _#_FBO_#_ - dispatch un toast coté android, la connexion est ok, il faut lui indiquer qu'il est en roaming mais que ca marche 
			r_bResult = true;
		}
		return r_bResult;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewVersion() {
		return !this.getApplicationVersion().equals(this.getStringSetting(Application.SETTING_APPVERSION));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getApplicationVersion() {
		String r_sVersion = null;
		try {
			r_sVersion = getApplication().getPackageManager()
					.getPackageInfo(this.mainPackage, PackageManager.GET_CONFIGURATIONS).versionName;
		}
		catch (PackageManager.NameNotFoundException e) {
			throw new MobileFwkException(e);
		}

		return r_sVersion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueId() {
		String r_sDeviceId = null ;

		TelephonyManager oTM = ((TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE));
		r_sDeviceId = oTM.getDeviceId();

		if ( r_sDeviceId == null ) {
			r_sDeviceId = BeanLoader.getInstance().getBean(NetworkHelper.class).getMacAddress(getApplication());
		}

		return r_sDeviceId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseExist() {
		File oFile = this.getApplicationContext().getDatabasePath(this.getDatabaseName());
		return oFile.exists();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getScreenColumnNumber() {
		int r_iResult = -1;

		// est ce que les préférences sont != automatic
		Configuration oConfig = getApplication().getResources().getConfiguration();
		if(oConfig.orientation == Configuration.ORIENTATION_PORTRAIT ){
			r_iResult=this.getIntSetting(SETTING_PORTRAIT_COLUMN_NUMBER);
		}
		else{
			r_iResult=this.getIntSetting(SETTING_LANDSCAPE_COLUMN_NUMBER);
		}
		//les settings sont en automatic, on calcule en fonction de la taille d'écran
		if (r_iResult==-1 || r_iResult==0){ 
			
			int oConstScreenOrientation = oConfig.orientation ;
			if (oConstScreenOrientation == Configuration.ORIENTATION_PORTRAIT) {
				r_iResult = SCREEN_ONE_COLUMN;
			} else {
				int oConstScreenSize = this.getApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
				
				if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && oConstScreenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
					r_iResult = SCREEN_FOUR_COLUMN;
				} else if (oConstScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
					r_iResult = SCREEN_THREE_COLUMN;
				} else if (oConstScreenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
					r_iResult = SCREEN_TWO_COLUMN;
				}  else {
					r_iResult = SCREEN_ONE_COLUMN;
				}
			}
		}
		return r_iResult;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getScreenWidth() {
		AbstractMMActivity oCurrentActivity=(AbstractMMActivity) getApplication().getActivityListener().getCurrentVisibleActivity();
		DisplayMetrics oMetrics = new DisplayMetrics();
		oCurrentActivity.getWindowManager().getDefaultDisplay().getMetrics(oMetrics);

		//calcul de la largeur utile de l'écran
		int iScreenWidth = oMetrics.widthPixels;
		Configuration oConfig = oCurrentActivity.getResources().getConfiguration();
		if(oConfig.orientation == Configuration.ORIENTATION_PORTRAIT ){
			iScreenWidth=oMetrics.widthPixels -  this.getIntSetting(SETTING_PORTRAIT_WIDTH_MARGIN);
		}
		else{
			iScreenWidth=oMetrics.widthPixels -  this.getIntSetting(SETTING_LANDSCAPE_WIDTH_MARGIN);
		}
		return iScreenWidth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getScreenColumnWidth(int p_oNbColumnToDisplay,int p_oNbColumnOnTheScreen, int p_iStdPadding, int p_iPaddingLeft) {
		int r_iComputedWidth=0;
		AbstractMMActivity oCurrentActivity=(AbstractMMActivity) getApplication().getActivityListener().getCurrentVisibleActivity();

		// largeur de l'écran
		int iScreenWidth=this.getScreenWidth();

		// la largeur des colonnes dépend de la largeur de l'écran pour gérer les petites variations ansi que du nombre de colonnes
		r_iComputedWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (iScreenWidth / p_oNbColumnOnTheScreen)-(p_iPaddingLeft / p_oNbColumnOnTheScreen) -p_iStdPadding,
				oCurrentActivity.getResources().getDisplayMetrics());
		return r_iComputedWidth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeScreensDescriptor() {
		long lTime = System.currentTimeMillis();
		this.getLog().info("INFO", "Start initialise screens descriptor");
		try {
			Resources oResources = getApplication().getResources();
			String sKey = null; // nom du champ dans le fichier R
			String sName = null; // nom du composant lu
			String sParameter = null; // nom du paramètre à charger
			String sTag = null; // tag en cours de lecture
			String sEntity = null; //paramètre entity
			String sMandatory = null; //paramètre mandatory
			String sGroup = null;
			String sMinsize = null;
			int iMinsize = 0;
			int iValue = 0; // valeur du champ dans le fichier R
			int iEventType = 0; // valeur de l'évènenemt revoyé par le parser
			XmlResourceParser oParser = null;
			TheoriticalVisualComponentDescriptor oMasterDescriptor = null;
			LinkedList<TheoriticalVisualComponentDescriptor> oCurrentDescriptors = null;
			TheoriticalVisualComponentDescriptor oCurrentDescriptor1 = null;
			TheoriticalVisualComponentDescriptor oCurrentDescriptor2 = null;
			for (Field oField : androidRlayout.getFields()) {
				// chargement de l'arborescence des composants (tous les layouts sont chargés)
				sKey = oField.getName();
				iValue = oField.getInt(null);
				try {
					oParser = oResources.getXml(iValue);
				} catch( NotFoundException oNotFoundException ) {
					throw new MobileFwkException(
							"Can't find resource " + iValue + " of Field " + oField.getName(), oNotFoundException);
				}
				oParser.next();
				iEventType = oParser.getEventType();
				oMasterDescriptor = new TheoriticalVisualComponentDescriptor(sKey, ConfigurableVisualComponent.TYPE_MASTER);
				oCurrentDescriptors = new LinkedList<>();
				sName = null;
				sParameter = null;
				sTag = null;
				while (iEventType != XmlPullParser.END_DOCUMENT) {
					iEventType = oParser.next();
					sTag = oParser.getName();
					if (iEventType == XmlPullParser.START_TAG) {
						if (!XML_TAG_MERGE.equals(sTag)) {
							if (XML_TAG_INCLUDE.equals(sTag)) {
								sParameter = XML_PARAMETER_LAYOUT;
							} else {
								sParameter = XML_PARAMETER_ID;
							}
							sName = getStringParameter(sParameter, oParser);
							if (sTag.equals(TabWidget.class.getSimpleName()) || sTag.equals(FrameLayout.class.getSimpleName())) { 
								// les TabWidget ont un id spécifique android
								sName = XML_DEFAULT_ID;
							}
							if (sName == null) {
								sName = AbstractNamableObject.UNKNOWN;
								this.getLog().error(MM_MOBILE_E_0100,
										StringUtils.concat("The component ", oParser.getName(), " in ", sKey, " has no ", sParameter));
							}
							if (sName.contains(XML_SLASH)) {
								sName = sName.substring(sName.indexOf(XML_SLASH) + 1);
							}
							//récupération de l'entité associée, util si configuration particulière
							sEntity = getStringParameter(XML_PARAMETER_ENTITY, oParser);
							sMandatory = getStringParameter(XML_PARAMETER_MANDATORY, oParser);
							sMinsize = getStringParameter(XML_PARAMETER_MINSIZE, oParser);
							sGroup = getStringParameter(XML_PARAMETER_GROUP, oParser);
							iMinsize = 0;
							if (sMinsize!=null && sMinsize.length()>0) {
								iMinsize = Integer.valueOf(sMinsize);
							}
							oCurrentDescriptors.add(new TheoriticalVisualComponentDescriptor(sName, sTag, sEntity,"true".equalsIgnoreCase(sMandatory), iMinsize, sGroup));
							if (XML_TAG_INCLUDE.equals(sTag)) {
								oCurrentDescriptors.getLast().linkTo(sName);// suppression de @layout/
							}
							else {
								ConfigurationLoader oLoader = this.configurationLoaders.get(sTag);
								if (oLoader != null) {
									oLoader.load(oParser, oCurrentDescriptors.getLast().getModel());
								}
							}
						}
					} 
					else if (iEventType == XmlPullParser.END_TAG && !XML_TAG_MERGE.equals(sTag)) {
						oCurrentDescriptor1 = oCurrentDescriptors.removeLast();
						if (oCurrentDescriptors.isEmpty()) {
							oCurrentDescriptor2 = oMasterDescriptor;
						} 
						else {
							oCurrentDescriptor2 = oCurrentDescriptors.getLast();
						}
						oCurrentDescriptor2.addComponentDescriptor(oCurrentDescriptor1);
					}
				}
				// suppression des layouts intermédiaires
				oMasterDescriptor.reduce();
				// si le descriptor possède un seul fils et que celui ci porte le même nom que son père le père est supprimé
				// ce cas arrive dans le cas des "masters" pour les listes
				if (oMasterDescriptor.getComponents().size() == 1
						&& oMasterDescriptor.getComponents().get(0).getName().equals(oMasterDescriptor.getName())) {
					oMasterDescriptor = oMasterDescriptor.getComponents().get(0);
				}
				VisualComponentDescriptorsHandler.getInstance().addTheoriticalDescriptor(sKey, oMasterDescriptor);
			}

			VisualComponentDescriptorsHandler.getInstance().unlink();
		} catch (IllegalArgumentException e) {
			throw new MobileFwkException(e);
		} catch (IllegalAccessException e) {
			throw new MobileFwkException(e);
		} catch (XmlPullParserException e) {
			throw new MobileFwkException(e);
		} catch (IOException e) {
			throw new MobileFwkException(e);
		}
		VisualComponentDescriptorsHandler.getInstance().resetAndComputeRealDescriptors();
		this.getLog().info(LOG_TAG, "Stop initialise screens descriptor " + (System.currentTimeMillis()-lTime));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadSettings() {
		// Récupération des informations relatives aux users
		// Chargement des settings (login,mdp,url serveur, etc.)
		SharedPreferences oPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		Map<String, Object> defaultValues = getDefaultSettingsValues();
		for(String sKey : this.getStringSettingsDefinition()) {
			String defVal = null;
			if(defaultValues.get(sKey)!=null){
				defVal = defaultValues.get(sKey).toString() ;
			}
			this.setStringSetting(sKey, oPreferences.getString(sKey,defVal));
		}
		for(String sKey : this.getIntegerSettingsDefinition()) {
			if (defaultValues.get(sKey)!=null && Integer.class.isAssignableFrom(defaultValues.get(sKey).getClass())) {
				this.setIntSetting(sKey, oPreferences.getInt(sKey, (Integer)defaultValues.get(sKey) ));
			} else {
				this.setIntSetting(sKey, oPreferences.getInt(sKey, 0 ));
			}
		}
		for(String sKey : this.getBooleanSettingsDefinition()) {
			if (defaultValues.get(sKey)!=null && Boolean.class.isAssignableFrom(defaultValues.get(sKey).getClass())) {
				this.setBooleanSetting(sKey, oPreferences.getBoolean(sKey, (Boolean)defaultValues.get(sKey)));
			} else {
				this.setBooleanSetting(sKey, oPreferences.getBoolean(sKey, false));
			}
		}
		// Mise à jour des setting calculers
		this.computeSettings();
	}

	@Override
	protected Map<String, Object> getDefaultSettingsValues() {
		return new HashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSettings() {
		super.removeSettings();
		SharedPreferences oPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
		oPreferences.edit().clear().commit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openInputStream(MContext p_oContext, ApplicationR p_oResourceKey) {
		return ((AndroidContextImpl) p_oContext).getAndroidNativeContext().getResources()
				.openRawResource(((AndroidApplication) Application.getInstance())
						.getAndroidIdByRKey(p_oResourceKey));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openInputStream(MContext p_oContext, ApplicationR p_oResourceKey, int p_iIndex) {
		try {
			return ((AndroidContextImpl) p_oContext).getAndroidNativeContext().getResources()
					.openRawResource(this.getAndroidIdByStringKey(p_oResourceKey.getGroup(), p_oResourceKey.getKey() + '_' + p_iIndex));
		}
		catch (Resources.NotFoundException e) {
			return null;
		}
	}

	/**
	 * Read a parameter in xml parser
	 * @param p_sId the parameter name
	 * @param p_oParser the parser to use
	 * @return the parameter value, if parameter is an android id then it's converted in literal parameter
	 */
	private String getStringParameter(String p_sId, XmlResourceParser p_oParser) {
		String sName = null;
		String r_sValue = null;
		// la méthode du framework android p_oParser.getAttributeValue("android", p_sId)
		// semble ne pas fonctionner, on garde le code pas optimum pour le moment ???
		// A2A_DEV à retester problème de lié à la chaine pour android c'est pas une chaîne ???
		for (int a = 0; a < p_oParser.getAttributeCount(); a++) {
			sName = p_oParser.getAttributeName(a);
			if (sName.equals(p_sId) || sName.equals("android:" + p_sId) || sName.equals("movalys:" + p_sId)) {
				r_sValue = p_oParser.getAttributeValue(a);
				if (r_sValue.contains("@")) {
					r_sValue = r_sValue.substring(r_sValue.indexOf('@') + 1);
					try {
						int iValue = Integer.valueOf(r_sValue);
						r_sValue = this.getAndroidIdStringByIntKey(iValue);
					}
					catch(NumberFormatException e) {
						// Nothing To Do
						this.getLog().error(NumberFormatException.class.getSimpleName(), e.toString());
					}
				}
				break;
			}
		}
		return r_sValue;
	}

	////////////////////
	// ZONE DEAD/LIVE //
	////////////////////

	/**
	 * Returns the text id of a visual component for a int Android id
	 * @param p_iIntToFind
	 *            the int id to find
	 * @return the string id associated
	 */
	public String getAndroidIdStringByIntKey(int p_iIntToFind) {
		return this.androidIdInt2String.get(p_iIntToFind);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getAndroidIdByStringKey(ApplicationRGroup p_sKey, String p_sElementKey) {
		int r_iAndroidId = -1;
		String sKey = p_sKey.getKey() + p_sElementKey;
		if (this.androidIdString2Int.containsKey(sKey)) {
			r_iAndroidId = this.androidIdString2Int.get(sKey);
		}
		return r_iAndroidId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getAndroidIdByRKey(ApplicationR p_sRKey) {
		int r_iAndroidId = -1;
		String sKey = p_sRKey.getGroup().getKey() + p_sRKey.getKey();
		if (this.androidIdString2Int.containsKey(sKey)) {
			r_iAndroidId = this.androidIdString2Int.get(sKey);
		}
		return r_iAndroidId;
	}

	//	/**
	//	 * Get the currentColumnId, it's used to get the intervention detail column displayed on the screen when orientation change
	//	 * @return Objet currentColumnId
	//	 */
	//	public int getCurrentColumnId() {
	//		return this.currentColumnId;
	//	}
	//
	//	/**
	//	 * set the currentColumnId, it's used to get the intervention detail column displayed on the screen when orientation change
	//	 * @param p_oCurrentColumnId Objet currentColumnId
	//	 */
	//	public void setCurrentColumnId(int p_oCurrentColumnId) {
	//		this.currentColumnId = p_oCurrentColumnId;
	//	}
	//	
	//	/**
	//	 * reset the currentColumnId to the default value, it's used to get the intervention detail column displayed on the screen when orientation change
	//	 */
	//	public void resetCurrentColumnId() {
	//		this.currentColumnId = 0;
	//	}

	/**
	 * Compute an unique id and put them is component
	 * 
	 * @param p_oComponent
	 *            the component requested id
	 * @param p_sName
	 *            the name of component
	 */
	// A2A_Q synchronize ou pas ?
	public void computeId(View p_oComponent, String p_sName) {
		String sName = ApplicationRGroup.ID.getKey()+p_sName;
		Integer oId = this.androidIdString2Int.get(sName);
		if (oId == null) {
			// l'id n'existe pas, on le créer
			genId++;
			this.androidIdInt2String.put(genId, p_sName);
			this.androidIdString2Int.put(sName, genId);
			p_oComponent.setId(genId);
		} else {
			p_oComponent.setId(oId);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStringResource(final ApplicationR p_oKey) {
		String r_sResource = super.getStringResource(p_oKey);
		if (r_sResource == null) {
			r_sResource = getApplication().getString(this.getAndroidIdByStringKey(p_oKey.getGroup(),p_oKey.getKey().replaceAll("\\.", "_")));
		}

		return r_sResource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStringResource(final String p_sKey) {
		String r_sResource = super.getStringResource(p_sKey);
		if (r_sResource == null) {
			int iRes = this.getAndroidIdByStringKey(ApplicationRGroup.STRING, p_sKey);
			if (iRes != -1) {
				r_sResource = getApplication().getString(iRes);
			}
		}
		return r_sResource;
	}

	/**
	 * Récupère une chaine de caractères à partir d'une clé.
	 * @param p_iKey La clé
	 * @return la chaine de caractères associée à la clé ou null si aucune chaîne ne correspond à la clé.
	 */
	public String getStringResource(final int p_iKey) {
		String r_sResource = super.getStringResource(this.getAndroidIdStringByIntKey(p_iKey));
		if (r_sResource == null) {
			r_sResource = getApplication().getString(p_iKey);
		}
		return r_sResource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDecriptedStringSetting(final String p_sKey) {
		String encryptedString = (String) this.setting.get(p_sKey);
		return LocalAuthHelper.getInstance().decrypt(this.getApplicationContext(), encryptedString);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getWsEntryPoint() {
		return getStringResource(AndroidApplicationR.ws_entrypoint);
	}

	/**
	 * Return the connection type
	 * @return object <em>ConnectionType</em>
	 */
	@Override
	public ConnectionType getConnectionType() {
		return BeanLoader.getInstance().getBean(NetworkHelper.class).getConnectionType(getApplication());
	}

	/**
	 * Permet d'analyser les listeners sur l'objet fourni
	 * @param p_oObject  objet à étudier pour stocker les listener dans l'application
	 */
	public void analyzeClassOf(Object p_oObject ){ 
		ClassAnalyse oAnalyse = getClassAnalyser().analyzeClass(p_oObject);
		List<Class<? extends Dataloader<?>>> oDataLoaders = oAnalyse.getDataLoaderForObject(p_oObject.getClass());
		if (oDataLoaders!=null) {
			for(Class<? extends Dataloader<?>> oDataLoaderClass : oDataLoaders) {
				addDataLoaderListener(oDataLoaderClass, p_oObject);
			}
		}
		List<Class<? extends BusinessEvent<?>>> oBusinessEvents = oAnalyse.getBusinessEventForObject(p_oObject.getClass());
		if (oBusinessEvents!=null) {
			for(Class<? extends BusinessEvent<?>> oBusinessEventClass : oBusinessEvents) {
				addBusinessEventListener(oBusinessEventClass, p_oObject);
			}
		}
		if ( !(p_oObject instanceof Activity) ) {
			Set<Class<? extends Action<?,?,?,?>>> oActionEvents = oAnalyse.getActions();
			if (oActionEvents!=null) {
				for(Class<? extends Action<?,?,?,?>> oActionClass : oActionEvents) {
					addActionListener(oActionClass, p_oObject );
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application#getImageByName(java.lang.String)
	 */
	@Override
	public Integer getImageByName(String p_sName) {
		return this.getAndroidIdByStringKey(ApplicationRGroup.DRAWABLE, p_sName);
	}	
	/**
	 * 
	 * Check if a voce recognition application is installed on the device by searching an activity that match with {@link RecognizerIntent#ACTION_RECOGNIZE_SPEECH}
	 * @return true if this kind of application is available on the device
	 */
	public boolean isRecognitionInstalled() {
		boolean r_bSpeechReconitionExist = false;
		// Check to see if a recognition activity is present
		PackageManager oPackageManager = getApplication().getPackageManager();
		List<ResolveInfo> listActivities = oPackageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (!listActivities.isEmpty()) {
			r_bSpeechReconitionExist = true;
		}
		return r_bSpeechReconitionExist;
	}
	/**
	 * Fire an intent to start the speech recognition activity.
	 * @param p_iOpenerId opener id
	 * @param p_iRequestCode code of the request 
	 */
	public void startVoiceRecognitionActivity( int p_iOpenerId , int p_iRequestCode , MMComplexeComponent p_oTargetComponent) {
		Intent oIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		oIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		oIntent.putExtra(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS , RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE);
		//oIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
		((AbstractMMActivity)getApplication().getActivityListener().getCurrentVisibleActivity()).
		startActivityForResult(oIntent, p_oTargetComponent, p_iRequestCode);
	}
	/**
	 * Check if an email application is installed on the device
	 * @return true if this kind of application is available on the device
	 */
	public boolean isMailAvailable() {
		boolean r_bMailAvailable = false;
		PackageManager oPackageManager = getApplication().getPackageManager();
		// [mantis 0020184] ajout de la précision sur le contenu à envoyer sinon ça marche pas (email fictive)  
		Intent oIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","test@sopra.com", null));
		List<ResolveInfo> listActivities = oPackageManager.queryIntentActivities( oIntent , PackageManager.MATCH_DEFAULT_ONLY);	
		if (!listActivities.isEmpty()) {
			r_bMailAvailable = true;
		}
		return r_bMailAvailable;
	}

	/**
	 * Execute la tache asynchrone en parametre avec les parametres en parametre
	 * Cette méthode permet d'avoir la même execution sur les version superieur à 
	 * HONEYCOMB que l'execution à partir de DONUT (execution des taches asynchrones 
	 * sur plusieurs threads)
	 * 
	 * @param p_oTask la tache à executer
	 * @param p_oParams les parametres de la tache
	 */
	public <T> void execAsyncTask(AsyncTask<T, ?, ?> p_oTask, T... p_oParams) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			p_oTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					p_oParams);
		} else {
			p_oTask.execute(p_oParams);
		}
	}

	public void setScreenDictionary(SparseArrayIntArrayParcelable p_oScreenDictionary) {
		this.screenDictionary = p_oScreenDictionary;
	}


	/**
	 * @param p_oIntent
	 * @param p_iRequestCode
	 */
	public void startActivityForResult( Intent p_oIntent, int p_iRequestCode ) {
		Activity activity = getCurrentActivity();
		Log.d(LOG_TAG, "startActivityForResult using Activity " + activity.getClass().getName());
		activity.startActivityForResult(p_oIntent, p_iRequestCode );
	}

	/**
	 * @param p_oIntent
	 * @param p_iRequestCode
	 */
	public void startActivity( Intent p_oIntent ) {
		Activity activity = getCurrentActivity();
		Log.d(LOG_TAG, "startActivity using Activity " + activity.getClass().getName());
		activity.startActivity(p_oIntent);
	}

	/**
	 * Return current activity
	 * If one is visible, return the visible activity.
	 * Else if in creation state, return the activity in creation state
	 * Else if in onActivityResult state, return the activity of the current running onActivityResult
	 * @return
	 */
	public Activity getCurrentActivity() {
		Activity activity = getCurrentVisibleActivity();
		if ( activity == null ) {
			activity = getApplication().getActivityListener().getCurrentActivity();
		}
		return activity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOnUIThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	/**
	 * Destroy application : this method clears application caches, unregisters listeners, remove notifications,
	 * disable push and finally kill application process.
	 * This method must not be called directly. Use launchStopApplication instead. It must be called when all
	 * activities are destroyed.
	 */
	@Override
	public void destroy() {

		Log.d(LOG_TAG, "AndroidApplication.destroy");

		if (ConfigurationsHandler.isLoaded()) {

			// suppression des alertes
			BeanLoader.getInstance().getBean(SynchroNotificationHelper.class).killSynchroNotification();

			//désactivation du push si il est activé
			FwkController oControl = Application.getInstance().getController();
			if (oControl.isPushActivated()){
				oControl.desactivatePush();
			}
		}

		super.destroy();
	}

	@Override
	public void runOnUiThread(Runnable p_oRunnable) {
		Activity oActivity = ((AndroidApplication) Application.getInstance()).getCurrentActivity();
		if(oActivity != null) {
			oActivity.runOnUiThread(p_oRunnable);	
		}
	}
}
