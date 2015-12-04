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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.application;

import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.SPACE;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition.FWK_MOBILE_I_1002;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_7;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_8;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

import com.adeuza.movalysfwk.mf4jcommons.crypt.AESUtil;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.AbstractSynchronisableListDataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.CreateConfigurationsHandlerInit;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.PropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkController;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.NotifierLauncher;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoaderError;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ExtBeanType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.user.User;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.log.Logger;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.network.ConnectionType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DefaultViewModelCreator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler;


/**
 * <p>Defines the abstract layer to initialize an application.</p>
 * <p>Manages logs and starting classes.</p>
 */
public abstract class Application  implements NotifierLauncher {

	/** Constant <code>LOG_TAG="MF"</code> */
	public static final String LOG_TAG = "MF" ;

	/** ordered list of properties to load */
	public static final String[] PROPERTIES_FILES = {"beans_fwk", "beans_fwk_database", "beans_model", "beans_viewmodel", "beans_action", "beans_dev", "beans_lollipop" };
	/** which properties files are mandatory */
	public static final boolean[] MANDATORY_PROPERTIES_FILES = {true, false, false, false, false, true, false};
	/** ordered list of properties to load */
	public static final String[] ENTITYHELPER_PROPERTIES_FILES = {"beans_entityhelper", "beans_entityhelperfwk" };
	private static final String ROOT_PATH = "/";

	//////////////////////////////////
	// ZONE DEFINTIION DES SETTINGS //
	//////////////////////////////////
	/** setting label for login (persistent property) */
	public static final String SETTING_LOGIN = "login";
	/** setting label for password (persistent property) */
	public static final String SETTING_PASSWORD = "password";
	/** setting label for url server (persistent property) */
	public static final String SETTING_URLSERVER = "urlserver";
	/** setting int for timeout server connection (persistent property) */
	public static final String SETTING_URLTIMEOUT = "urltimeout";
	/** setting int for timeout server connection (persistent property) */
	public static final String SETTING_URLSOTIMEOUT = "urlsotimeout";
	
	/** setting label for proxy login (persistent property) */
	public static final String SETTING_PROXYLOGIN = "proxylogin";
	/** setting label for proxy password (persistent property) */
	public static final String SETTING_PROXYPASSWORD = "proxypassword";
	/** setting label for proxy url (persistent property) */
	public static final String SETTING_PROXYURLSERVER = "proxyurlserver";	

	/**the setting for number of columns on landscape, -1 for auto */
	public static final String SETTING_LANDSCAPE_COLUMN_NUMBER = "landscapeColumnNumber";
	/**the setting for number of columns on Portrait, -1 for auto */
	public static final String SETTING_PORTRAIT_COLUMN_NUMBER = "portraitColumnNumber";
	/**the setting for the margin to consider on width on landscape orientation*/
	public static final String SETTING_LANDSCAPE_WIDTH_MARGIN = "landscapeWidthMargin";
	/**the setting for the margin to consider on width on portrait orientation*/
	public static final String SETTING_PORTRAIT_WIDTH_MARGIN = "portraitWidthMargin";


	//////////////////////////////////////////
	// ZONE DEFINITION DES DERIVED SETTINGS //
	//////////////////////////////////////////

	/** setting label for host server (volatile property) */
	public static final String SETTING_COMPUTE_URLSERVER_HOST = "urlserverhost";
	/** setting label for port server (volatile property) */
	public static final String SETTING_COMPUTE_URLSERVER_PORT = "urlserverport";
	/** setting label for port server (volatile property) */
	public static final String SETTING_COMPUTE_URLSERVER_TIMEOUT = "urlservertimeout";
	/** setting label for port server (volatile property) */
	public static final String SETTING_COMPUTE_URLSERVER_SOTIMEOUT = "urlserversotimeout";
	/** setting label for path server (volatile property) */
	public static final String SETTING_COMPUTE_URLSERVER_PATH = "urlserverpath";
	/** setting label for proxy host server (volatile property) */
	public static final String SETTING_COMPUTE_PROXYURLSERVER_HOST = "proxyurlserverhost";
	/** setting label for proxy port server (volatile property) */
	public static final String SETTING_COMPUTE_PROXYURLSERVER_PORT = "proxyurlserverport";
	/** setting label for proxy path server (volatile property) */
	public static final String SETTING_COMPUTE_PROXYURLSERVER_PATH = "proxyurlserverpath";

	/** the name space of the Movalys Framework attributes used in the layout files. 
	 * This allow you to use movalys: attributes in conjunction of android: attributes 
	 * see the documentation of th MMViews widgets for details*/
	public static final String MOVALYSXMLNS="http://www.adeuza.com/movalys/mm/android";
	/** setting label for application version. */
	public static final String SETTING_APPVERSION = "appversion";
	/** identifier of the connected resource */
	private long currentUserResource;
	/** the notifier used for currentItemListener*/
	private Notifier notifier = null;

	/** indicates if the application has already been initialized (method {@link #init(Logger, ProgressHandler, String, int)} called. */
	private boolean init = false;
	/** indicated if the initializers has already been executed. */
	private boolean initializersExecuted;
	/** l'analyser de class */
	private ClassAnalyser analyser = null;
	/** contains the classes to start for the initialization sequence */
	private List<RunInit> inits = null;
	/** the handler of a progress Bar object */
	private ProgressHandler progressHandler = null;
	/** true if application is loaded */
	private boolean loaded = false;
	/** controller de l'application*/
	private FwkController controller = null;
	/** instance of logger to use */
	private Logger log = null;
	/** database name */
	private String databaseName = null;
	/** database version */
	private int databaseVersion = 1;
	/** map of current settings */
	protected Map<String, Object> setting = null;
	/** map of older settings */
	protected Map<String, Object> oldSetting = null;
	/** view model cache */
	protected DefaultViewModelCreator vmc = null;
	/** transients entities declared in the UML Model, do not used that for other purpose*/
	private Map<String, Object> cacheTranscientObjects = null;

	/** Configuration Data Cache */
	//private Map<String, Map<String, List<Object>>> cacheConfigurationData = null;
	private Map<Integer, Map<Integer, Object>> cacheConfigurationData = null;

	/** résultat courrant de la synchro */
	private Object currentSynchroResult = null;

	/** ListenerIdentifier counter for dataloaders */
	private Map<String, Integer> dataloaderListenerIdentifierCounter = new HashMap<String, Integer>();
	
	/** ListenerIdentifier counter for business events */
	private Map<String, Integer> businessListenerIdentifierCounter = new HashMap<String, Integer>();
	
	/** ListenerIdentifier counter for actions */
	private Map<String, Integer> actionListenerIdentifierCounter = new HashMap<String, Integer>();
	
	/** Dictionary of dataloader listener */
	private Map<Class<? extends Dataloader<?>>, Set<Object>> dataloadersListeners = new HashMap<Class<? extends Dataloader<?>>, Set<Object>>();

	/** event manager */
	private Map<Class<? extends BusinessEvent<?>>, Map<Class<?> ,List<Object>>> businessEventsListeners = new HashMap<Class<? extends BusinessEvent<?>>, Map<Class<?>, List<Object>>>();

	/** action manager */
	private Map<Class<? extends Action<?,?,?,?>>, List<Object>> actionListeners = new HashMap<Class<? extends Action<?,?,?,?>>, List<Object>>();

	/** active display by name */
	private Map<String, WeakReference<ListenerIdentifier>> mapActiveDisplayByName = new HashMap<>();
	
	/** active display by class */
	private Map<ListenerIdentifier, String> mapActiveDisplayByClass = new WeakHashMap<>();

	/**
	 * Actions in progress for each activity
	 */
	private Map<String, ActiveDisplayActions> mapActionsByActiveDisplay = new HashMap<>();
	
	/**
	 * Actions in progress for each activity
	 */
	private List<String> storeEventsForDisplayList = new ArrayList<>();
	
	/** User interface */
	private User user ;

	/**
	 * Returns instance of application
	 *
	 * @return instance of application
	 */
	public static Application getInstance() {
		// LBR: Test
		BeanLoader beanLoader = null;
		beanLoader = BeanLoader.getInstance();
		if (beanLoader != null && beanLoader.hasDefinition(ExtBeanType.Application)) {
			return (Application) BeanLoader.getInstance().getBean(ExtBeanType.Application);
		} else {
			return null;
		}
	}

	/**
	 * Constructs a new initializer
	 */
	public Application() {
		this.inits = new ArrayList<RunInit>();
		this.oldSetting = new HashMap<String, Object>();
		this.setting = new HashMap<String, Object>();
		this.vmc = (DefaultViewModelCreator) BeanLoader.getInstance().getBean(ExtBeanType.ViewModelCreator);
		this.cacheTranscientObjects = new HashMap<String, Object>();
		this.cacheConfigurationData = new HashMap<Integer, Map<Integer,Object>>();
		this.notifier = new Notifier();
		this.analyser = new ClassAnalyser();
	}

	/**
	 * Permet de savoir si on peut utiliser la connection wifi et gprs.
	 *
	 * @return a boolean.
	 */
	public boolean isConnectionReady() {
		return true;
	}

	/**
	 * <p>putCurrentSynchroResult.</p>
	 *
	 * @param p_oResult a {@link java.lang.Object} object.
	 */	
	public void putCurrentSynchroResult(Object p_oResult) {
		this.currentSynchroResult = p_oResult;

	}

	/**
	 * <p>Getter for the field <code>currentSynchroResult</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getCurrentSynchroResult() {
		return this.currentSynchroResult;
	}

	/**
	 * Ajoute un listener à un dataloader
	 *
	 * @param p_sKey le type de dataloader sur lequel on veut ajouter le listener
	 * @param p_oListener le listener à ajouter
	 */
	public void addDataLoaderListener(Class<? extends Dataloader<?>> p_sKey, Object p_oListener) {
		Set<Object> listeners = this.dataloadersListeners.get(p_sKey);
		if (listeners == null) {
			listeners = new HashSet<Object>();
			this.dataloadersListeners.put(p_sKey, listeners);
		}
		if (p_oListener instanceof ListenerIdentifier) {
			String sUniqueId = p_sKey.getName() + ((ListenerIdentifier)p_oListener).getUniqueId();
			increaseListenerIdentifierCounter(dataloaderListenerIdentifierCounter, sUniqueId);
			listeners.add(((ListenerIdentifier)p_oListener).getUniqueId());
		} else {
			listeners.add(p_oListener);
		}
	}

	/**
	 * Supprimer un listener sur l'ensemble des dataloaders
	 *
	 * @param p_oListener le listener à supprimer
	 */
	public void removeListenerFromDataLoader(Object p_oListener) {
		for(Entry<Class<? extends Dataloader<?>>, Set<Object>> oEntrySet : this.dataloadersListeners.entrySet()) {
			Set<Object> oListeners = oEntrySet.getValue();
			if (p_oListener instanceof ListenerIdentifier) {
				String sUniqueId = ((Class<?>)oEntrySet.getKey()).getName() + ((ListenerIdentifier)p_oListener).getUniqueId();
				int decreaseValue = decreaseListenerIdentifierCounter(dataloaderListenerIdentifierCounter, sUniqueId);
				if (decreaseValue == 0) {
					oListeners.remove(((ListenerIdentifier)p_oListener).getUniqueId());
				}
			} else {
				oListeners.remove(p_oListener);
			}
		}
	}

	/**
	 * Donne l'ensemble des listeners pour un dataloader donné
	 *
	 * @param p_oKey la classe du dataloader
	 * @return la liste des liteners
	 */
	public Collection<Object> getDataLoaderListeners(Class<? extends Dataloader> p_oKey) {
		//il faut recherche l'interface de type Dataloader
		return this.dataloadersListeners.get(p_oKey.getInterfaces()[0]);
	}

	/**
	 * Ajoute un listener à une action
	 *
	 * @param p_sKey le type d'action sur lequel on veut ajouter le listener
	 * @param p_oListener le listener à ajouter
	 */
	public void addActionListener(Class<? extends Action<?,?,?,?>> p_sKey, Object p_oListener) {
		List<Object> listeners = this.actionListeners.get(p_sKey);
		if (listeners == null) {
			listeners = new ArrayList<Object>();
			this.actionListeners.put(p_sKey, listeners);
		}
		if (p_oListener instanceof ListenerIdentifier) {
			String sUniqueId = p_sKey.getName() + ((ListenerIdentifier)p_oListener).getUniqueId();
			increaseListenerIdentifierCounter(actionListenerIdentifierCounter, sUniqueId);
			listeners.add(((ListenerIdentifier)p_oListener).getUniqueId());
		} else {
			listeners.add(p_oListener);
		}
	}

	/**
	 * Supprimer un listener sur l'ensemble des actions
	 *
	 * @param p_oListener le listener à supprimer
	 */
	public void removeListenerFromAction(Object p_oListener) {
		for(Entry<Class<? extends Action<?, ?, ?, ?>>, List<Object>> oEntrySet : this.actionListeners.entrySet()) {
			List<Object> oListeners = oEntrySet.getValue();
			if (p_oListener instanceof ListenerIdentifier) {
				String sUniqueId = ((Class<?>)oEntrySet.getKey()).getName() + ((ListenerIdentifier)p_oListener).getUniqueId();
				int decreaseValue = decreaseListenerIdentifierCounter(dataloaderListenerIdentifierCounter, sUniqueId);
				if (decreaseValue == 0) {
					oListeners.remove(((ListenerIdentifier)p_oListener).getUniqueId());
				}
			} else {
				oListeners.remove(p_oListener);
			}
		}
	}
	
	/**
	 * Retourne la liste des listeners pour une action
	 *
	 * @param p_oKey a {@link java.lang.Class} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<Object> getActionListeners(Class<? extends Action<?,?,?,?>> p_oKey) {
		return this.actionListeners.get(p_oKey);
	}

	/**
	 * <p>addBusinessEventListener.</p>
	 *
	 * @param p_oKey a {@link java.lang.Class} object.
	 * @param p_oListener a {@link java.lang.Object} object.
	 * @param p_oClassFilter 
	 */	
	public void addBusinessEventListener(Class<? extends BusinessEvent<?>> p_oKey, Object p_oListener, Class<?> p_oClassFilter) {
		Map<Class<?>, List<Object>> oMapListenerByFilter = this.businessEventsListeners.get(p_oKey);
		// get map of listener by filter
		if (oMapListenerByFilter == null) {
			oMapListenerByFilter = new HashMap<Class<?>, List<Object>>();
			this.businessEventsListeners.put(p_oKey, oMapListenerByFilter);
		}
		// get list of listener
		List<Object> listeners = null;
		Class<?> oTmpFilterClass = p_oClassFilter;
		if (oTmpFilterClass == null) {
			oTmpFilterClass = Object.class;
		}
		listeners = oMapListenerByFilter.get(oTmpFilterClass);
		if (listeners == null) {
			listeners = new ArrayList<Object>();
			oMapListenerByFilter.put(p_oClassFilter, listeners);
		}
		if (p_oListener instanceof ListenerIdentifier) {
			String sUniqueId = p_oKey.getName() + ((ListenerIdentifier)p_oListener).getUniqueId();
			increaseListenerIdentifierCounter(businessListenerIdentifierCounter, sUniqueId);
			listeners.add(((ListenerIdentifier)p_oListener).getUniqueId());
		} else {
			listeners.add(p_oListener);
		}
	}

	/**
	 * <p>removeListenerFromEventManager.</p>
	 *
	 * @param p_oListener a {@link java.lang.Object} object.
	 */	
	public void removeListenerFromEventManager(Object p_oListener) {
		for(Entry<Class<? extends BusinessEvent<?>>, Map<Class<?>, List<Object>>> oEntrySet : this.businessEventsListeners.entrySet()) {
			Map<Class<?>, List<Object>> oMapListeners = oEntrySet.getValue();
			for (Entry<Class<?>, List<Object>> oEntryToCheck : oMapListeners.entrySet()) {
				List<Object> oListeners = oEntryToCheck.getValue();
				if (p_oListener instanceof ListenerIdentifier) {
					String sUniqueId = ((Class<?>)oEntryToCheck.getKey()).getName() + ((ListenerIdentifier)p_oListener).getUniqueId();
					int decreaseValue = decreaseListenerIdentifierCounter(dataloaderListenerIdentifierCounter, sUniqueId);
					if (decreaseValue == 0) {
						oListeners.remove(((ListenerIdentifier)p_oListener).getUniqueId());
					}
				} else {
					oListeners.remove(p_oListener);
				}
			}
		}
	}

	/**
	 * <p>getBusinessEventListeners.</p>
	 *
	 * @param p_oKey a {@link java.lang.Class} object.
	 * @return a {@link java.util.List} object.
	 */	
	public Map<Class<?>, List<Object>> getBusinessEventListeners(Class<? extends BusinessEvent> p_oKey) {
		return this.businessEventsListeners.get(p_oKey);
	}

	/**
	 * Adds a screen object (activity or fragment) to the active list
	 * @param p_oListenerIdentifier the object to add
	 */
	public void addActiveDisplayList(ListenerIdentifier p_oListenerIdentifier) {
		String sName = p_oListenerIdentifier.getUniqueId();
		
		this.mapActiveDisplayByClass.put(p_oListenerIdentifier, sName);
		this.mapActiveDisplayByName.put(sName, new WeakReference<ListenerIdentifier>(p_oListenerIdentifier));
		disableStoreEventsForDisplay(p_oListenerIdentifier);
		
		// Invoke queue events (dataloader events and business events)
		this.getController().invokeQueuedEvents(
			BeanLoader.getInstance().getBean(MContextFactory.class).createContext(), p_oListenerIdentifier);
	}
	
	/**
	 * Removes a screen object (activity or fragment) from the active list
	 * @param p_oObject the object to add
	 */
	public void removeActiveDisplayFromList(ListenerIdentifier p_oObject, boolean p_isFinishing) {
		this.mapActiveDisplayByClass.remove(p_oObject);
		
		if (p_oObject == this.mapActiveDisplayByName.get(p_oObject.getUniqueId())) {
			this.mapActiveDisplayByName.remove(p_oObject.getUniqueId());
		}
		
		if ( !p_isFinishing ) {
			enableStoreEventsForDisplay(p_oObject);
		}
	}

	/**
	 * Check if screen has a dialog of type tag
	 * @param p_sTag tag
	 * @param p_sScreenIdentifier screen identifier
	 * @return true if screen has a dialog of type tag
	 */
	public boolean hasDialogOfType( String p_sTag, String p_sScreenIdentifier) {
		ActiveDisplayActions oActiveDisplayActions = this.mapActionsByActiveDisplay.get(p_sScreenIdentifier);
		boolean r_bResult = oActiveDisplayActions.hasDialogOfType(p_sTag);
		return r_bResult;
	}
	
	/**
	 * Register an opened progress dialog 
	 * @param p_sDialogTag dialog tag
	 * @param p_sScreenIdentifier screen identifier
	 */
	public void registerProgressDialog(String p_sDialogTag, String p_sScreenIdentifier) {
		ActiveDisplayActions oActiveDisplayActions = this.mapActionsByActiveDisplay.get(p_sScreenIdentifier);
		oActiveDisplayActions.registerProgressDialog(p_sDialogTag);
	}
	
	/**
	 * Unregister a progress dialog
	 * @param p_sDialogTag dialog tag
	 * @param p_sScreenIdentifier screen identifier
	 */
	public void unregisterProgressDialog(String p_sDialogTag, String p_sScreenIdentifier) {
		ActiveDisplayActions oActiveDisplayActions = this.mapActionsByActiveDisplay.get(p_sScreenIdentifier);
		oActiveDisplayActions.unregisterProgressDialog(p_sDialogTag);
	}
	
	/**
	 * Return
	 * @param sDialogTag dialog tag
	 * @param p_sScreenIdentifier screen identifier
	 * @return true if 
	 */
	public boolean hasRunningOrPendingActionWithDialogType(String sDialogTag, String p_sScreenIdentifier) {
		ActiveDisplayActions oActiveDisplayActions = this.mapActionsByActiveDisplay.get(p_sScreenIdentifier);
		return oActiveDisplayActions.hasRunningOrPendingAction(sDialogTag);
	}
	
	/**
	 * @param p_oObject
	 */
	public void disableStoreEventsForDisplay(ListenerIdentifier p_oObject) {
		this.storeEventsForDisplayList.remove(p_oObject.getUniqueId());
	}
	
	/**
	 * Enable event storage for display
	 * @param p_oObject
	 */
	public void enableStoreEventsForDisplay(ListenerIdentifier p_oListenerIdentifier) {
		this.storeEventsForDisplayList.add(p_oListenerIdentifier.getUniqueId());
	}	
	
	/**
	 * Return true if event storage is enabled for listener
	 * @param p_sListenerName listener name
	 * @return true if event storage is enabled for listener
	 */
	public boolean isStoreEventsEnabledForDisplay(String p_sListenerName) {
		if (p_sListenerName != null) 
			return this.storeEventsForDisplayList.contains(p_sListenerName);
		else
			return false;
	}
	
	/**
	 * Returns true if the given object is in the active screens list
	 * @param p_sObject object to look for 
	 * @return true if the object is found
	 */
	public boolean isInActiveList(String p_sObject) {
		return this.mapActiveDisplayByName.containsKey(p_sObject);
	}
	
	/**
	 * Returns the screen object from its name;
	 * @param p_sName the name to look for
	 * @return the screen object
	 */
	public ListenerIdentifier getScreenObjectFromName(String p_sName) {
		ListenerIdentifier r_oListenerIdentifier = null;
		if (p_sName != null) {
			WeakReference<ListenerIdentifier> oWeakRef = this.mapActiveDisplayByName.get(p_sName);
			if ( oWeakRef != null ) {
				r_oListenerIdentifier = oWeakRef.get();
			}
		}
		return r_oListenerIdentifier ;
	}

	/**
	 * Returns the screen object from its name;
	 * @param p_sName the name to look for
	 * @return the screen object
	 */
	public ListenerIdentifier getScreenObjecthasWindowFocus() {
		
		for (Map.Entry<String, WeakReference<ListenerIdentifier>> ActiveDisplay : this.mapActiveDisplayByName.entrySet())
		{
			WeakReference<ListenerIdentifier> oWeakRef = ActiveDisplay.getValue();
			ListenerIdentifier oListenerIdentifier = oWeakRef.get();
			if (oListenerIdentifier != null && AbstractMMActivity.class.isAssignableFrom(oListenerIdentifier.getClass()) &&
					((AbstractMMActivity)oListenerIdentifier).hasWindowFocus())
				return oListenerIdentifier;
		}
		
		return null;
	}
	
	/**
	 * Returns the screen name from its instance;
	 * @param p_oObject the object to look for
	 * @return the screen name
	 */
	public String getScreenNameFromObject(ListenerIdentifier p_oObject) {
		return this.mapActiveDisplayByClass.get(p_oObject);
	}
	
	/**
	 * Register a running action for an active display
	 * @param p_oListenerIdentifier active display
	 * @param p_oAction running action
	 */
	public void registerRunningAction( ListenerIdentifier p_oListenerIdentifier, MMActionTask<?,?,?,?> p_oAction) {
		if (p_oListenerIdentifier != null) {
			ActiveDisplayActions oRunningActions = this.mapActionsByActiveDisplay.get( p_oListenerIdentifier.getUniqueId());
			if ( oRunningActions == null ) {
				oRunningActions = new ActiveDisplayActions();
				this.mapActionsByActiveDisplay.put(p_oListenerIdentifier.getUniqueId(), oRunningActions);
			}
			oRunningActions.registerAction(p_oAction);
		}
	}
	
	/**
	 * Register a running action for an active display
	 * @param p_sUniqueId unique id of active display
	 * @param p_oAction running action
	 */
	public void unregisterRunningAction( String p_sUniqueId, MMActionTask<?, ?, ?, ?> p_oAction) {
		if (p_sUniqueId != null) {
			ActiveDisplayActions oRunningActions = this.mapActionsByActiveDisplay.get( p_sUniqueId);
			// oRunningActions can be null when an action finishes and the activity has been destroyed. In that case,
			// the running actions have already been unregistered by the activity.
			if ( oRunningActions != null ) {
				oRunningActions.unregisterAction(p_oAction);
			}
		}
	}
	
	/**
	 * Register a running action for an active display
	 * @param p_oListenerIdentifier active display
	 * @param p_oAction running action
	 */
	public void unregisterAllRunningActions( ListenerIdentifier p_oListenerIdentifier) {
		this.mapActionsByActiveDisplay.remove(p_oListenerIdentifier.getUniqueId());
	}
	
	/**
	 * Return running actions for active display
	 * @param p_oListenerIdentifier active display
	 * @return running actions for active display
	 */
	public List<MMActionTask<?, ?, ?, ?>> getRunningActionsForActiveDisplay( ListenerIdentifier p_oListenerIdentifier ) {
		List<MMActionTask<?, ?, ?, ?>> r_listActions = null;
		ActiveDisplayActions oRunningActions = this.mapActionsByActiveDisplay.get(p_oListenerIdentifier.getUniqueId());
		if ( oRunningActions != null ) {
			r_listActions = oRunningActions.getAll();
		}
		else {
			r_listActions = new ArrayList<>();
		}
		
		return r_listActions;
	}
	
	/**
	 * <p>getViewModelCreator.</p>
	 *
	 * @return the view model creator
	 */
	public DefaultViewModelCreator getViewModelCreator() {
		return this.vmc;
	}

	/**
	 * Retourne le logger de l'application.
	 *
	 * @return logger
	 */
	public Logger getLog() {
		return this.log;
	}

	/**
	 * Initialize the initializer :
	 * <li>set the logger</li>
	 * <li>set the superfactory properties file</li>
	 * <li>add framework initializers</li>
	 * <li>add other initializers</li>
	 *
	 * @param p_oLogger a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.log.Logger} object.
	 * @param p_oProgressHandler a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler} object.
	 * @param p_sDatabaseName a {@link java.lang.String} object.
	 * @param p_sDatabaseVersion a int.
	 */
	public final void init(Logger p_oLogger, ProgressHandler p_oProgressHandler,
			String p_sDatabaseName, int p_sDatabaseVersion) {

		this.databaseName = p_sDatabaseName;
		this.databaseVersion = p_sDatabaseVersion;
		this.log = p_oLogger;
		this.progressHandler = p_oProgressHandler;
		this.getLog().info(InformationDefinition.FWK_MOBILE_I_1000, InformationDefinition.FWK_MOBILE_I_1000_LABEL);
		this.init = true;
		this.initializersExecuted = false;

		// pas dans le constructeur car les méthodes suivantes peuvent être surchargées.
		this.prepareFwkInitialization();

		this.prepareCustomInitialization();

		this.getLog().info(InformationDefinition.FWK_MOBILE_I_1001, InformationDefinition.FWK_MOBILE_I_1001_LABEL);
	}

	/**
	 * Définis le Handler de progression.
	 *
	 * @param p_oProgressHandler ProgressHandler
	 */
	public final void setProgressHandler(ProgressHandler p_oProgressHandler) {
		this.progressHandler = p_oProgressHandler;
	}

	/**
	 * Prepares the framework initialisation
	 */
	protected void prepareFwkInitialization() {
		this.addInit(new CreateConfigurationsHandlerInit());
		CustomApplicationInit oCustomApplicationInit = BeanLoader.getInstance().getBean(CustomApplicationInit.class);

		//TODO : a faire plus proprement
		try {
			this.addInit((RunInit) BeanLoader.getInstance().getBeanByType("createDatabaseInit"));
		} catch ( BeanLoaderError | Exception e) {
			this.getLog().debug("prepareFwkInitialization", e.toString());
		}
		try {
			Class<RunInit> oDaoInit = (Class<RunInit>) Class.forName("com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoInit");
			this.addInit( oDaoInit.newInstance());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoClassDefFoundError e) {
			this.getLog().debug("prepareFwkInitialization", e.toString());
		}
		
		this.addInit(oCustomApplicationInit.getCustomInit());
	}

	/**
	 * Prepares the application initialisation
	 */
	protected void prepareCustomInitialization() {
		//Nothing To Do
	}

	/**
	 * Add an initializer
	 *
	 * @param p_oInit the initializer to add
	 */
	public void addInit(RunInit p_oInit) {
		this.inits.add(p_oInit);
	}

	/**
	 * Insert an initializer
	 *
	 * @param p_oInit the initializer to insert
	 * @param p_iPosition the position of insertion
	 */
	public void insertInit(RunInit p_oInit, int p_iPosition) {
		this.inits.add(p_iPosition, p_oInit);
	}

	/**
	 * Remove the initializer at position p_iPosition
	 *
	 * @param p_iPosition the position to use
	 */
	public void removeInit(int p_iPosition) {
		this.inits.remove(p_iPosition);
	}

	/**
	 * Return the numbers of initialisers
	 *
	 * @return a number
	 */
	public int getInitsNumber() {
		return this.inits.size();
	}

	/**
	 * Launch each initializer. If this class is not correctly inialized a MobileFwkException was thrown.
	 */
	public void run() {
		if (!this.init) {
			// impossible de logger : le logger n'est pas encore mis en place
			throw new MobileFwkException(ErrorDefinition.FWK_MOBILE_E_1000, ErrorDefinition.FWK_MOBILE_E_1000_LABEL);
		}
		MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
		MContext oContext = oCf.createContext();
		oContext.beginTransaction();
		try {
			long lTime = 0;
			for(RunInit oInit : inits) {
				lTime = System.currentTimeMillis();
				this.getLog().info(FWK_MOBILE_I_1002, StringUtils.concat(FWK_MOBILE_I_1002_LABEL_7, SPACE, oInit.getClass().getSimpleName()));
				oInit.run(oContext);
				this.progressHandler.increaseProgress(null);
				this.getLog().info(FWK_MOBILE_I_1002, StringUtils.concat(FWK_MOBILE_I_1002_LABEL_8, SPACE, String.valueOf(System.currentTimeMillis()-lTime)));
			}
		}
		catch(UnsatisfiedLinkError e) {
			this.getLog().error(FWK_MOBILE_I_1002, "", e);
		}
		finally {
			oContext.endTransaction();
			oContext.close();
		}
		this.initializersExecuted = true;
	}

	/**
	 * Returns application controller
	 *
	 * @return controller
	 */
	public FwkController getController() {
		//attention à ne pas faire dans le contructeur,
		//le bean loader n'est pas encore chargé !!!
		if (this.controller == null) {
			this.controller = (FwkController) BeanLoader.getInstance().getBean(ExtBeanType.Controller);
		}
		return this.controller;
	}

	/**
	 * Méthode de remise à zéro des caches de l'application
	 */
	public void reset() {
		this.cacheTranscientObjects.clear();
		this.cacheConfigurationData.clear();
		this.vmc.destroy();
	}

	/**
	 * Destroys the application
	 */
	public void destroy() {
		this.getController().destroy();

		this.reset();

		//il faut vider les listeners des loaders
		for(Dataloader<?> oLoader : BeanLoader.getInstance().getSingletons(Dataloader.class)) {
			if (oLoader instanceof AbstractSynchronisableListDataloader) {
				Application.getInstance().getController().unregister((AbstractSynchronisableListDataloader)oLoader);
			}
		}
		for(Set<Object> listeners : this.dataloadersListeners.values()) {
			listeners.clear();
		}
	}

	/**
	 * Indicates if the application is loaded
	 *
	 * @return true if application is loaded
	 */
	public boolean isLoaded() {
		return this.loaded;
	}

	/**
	 * Indictates that application is loaded
	 */
	public void loaded() {
		this.loaded = true;
	}

	/**
	 * Indicated that initializers have been executed.
	 *
	 * @return <code>true</code> if the initializers have been executed.
	 */
	public boolean isInitializersExecuted() {
		return this.initializersExecuted;
	}

	/**
	 * <p>
	 * 	Accessors that return the database name
	 * </p>
	 *
	 * @return String representation of the database name
	 */
	public String getDatabaseName() {
		return this.databaseName;
	}

	/**
	 * <p>
	 * 	Accessors that return the database version
	 * </p>
	 *
	 * @return the version number
	 */
	public int getDataBaseVersion() {
		return this.databaseVersion;
	}

	/**
	 * Opens a input stream on a resource specified by its key.
	 *
	 * @param p_oContext The current context. Never null.
	 * @param p_oResourceKey A key that identifies the resource to open.
	 * @return a {@link java.io.InputStream} object.
	 */
	public abstract InputStream openInputStream(MContext p_oContext, ApplicationR p_oResourceKey);
	
	/**
	 * Opens a input stream on a resource specified by its key.
	 *
	 * @param p_oContext The current context. Never null.
	 * @param p_oResourceKey A key that identifies the resource to open.
	 * @param p_iIndex : to retreive p_oResourceKey.getKey() + "." p_iIndex
	 * @return a {@link java.io.InputStream} object.
	 */
	public abstract InputStream openInputStream(MContext p_oContext, ApplicationR p_oResourceKey, int p_iIndex);
	
	/**
	 * Returns <code>true</code> if the a new version has been installed, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if the a new version has been installed, <code>false</code> otherwise.
	 */
	public abstract boolean isNewVersion();
	
	/**
	 * Returns the current version of the application. Never null.
	 *
	 * @return The current version of the application. Never null.
	 */
	public abstract String getApplicationVersion();
	
	/**
	 * Returns a single id (sample IMEI id for pda)
	 *
	 * @return a single id
	 */
	public abstract String getUniqueId();
	
	/**
	 * returns true whether database exists
	 *
	 * @return true whether database exists
	 */
	public abstract boolean isDataBaseExist();
	
	/**
	 * Returns <code>true</code> when the debug mode is activated.
	 *
	 * @return <code>true</code> when the debug mode is activated.
	 */
	public boolean isDebugEnabled() {
		Property oProperty = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.debug_mode);
		return oProperty != null && Property.TRUE.equals(oProperty.getValue());
	}

	/**
	 * Returns <code>true</code> when the dev mode is activated.
	 *
	 * @return <code>true</code> when the dev mode is activated.
	 */
	public boolean isDevModeEnabled() {
		Property oProperty = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.dev_mode);
		return oProperty != null && Property.TRUE.equals(oProperty.getValue());
	}

	/**
	 * Returns <code>true</code> when the synchro debug mode is activated.
	 *
	 * @return <code>true</code> when the synchro debug mode is activated.
	 */
	public boolean isSyncDebugEnabled() {
		Property oProperty = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_debug_mode);
		return oProperty != null && Property.TRUE.equals(oProperty.getValue());
	}

	/**
	 * Returns <code>true</code> when the synchro DataStream From File mode is activated.
	 *
	 * @return <code>true</code> when the synchro DataStream From File mode is activated.
	 */
	public boolean isSyncDataStreamFromFileEnabled() {
		Property oProperty = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_data_stream_from_file_mode);
		return oProperty != null && Property.TRUE.equals(oProperty.getValue());
	}

	/**
	 * Returns <code>true</code> when the synchro Transparent mode is activated.
	 *
	 * @return <code>true</code> when the synchro Transparent mode is activated.
	 */
	public boolean isSyncTransparentEnabled() {
		Property oProperty = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_transparent_mode);
		return oProperty != null && Property.TRUE.equals(oProperty.getValue());
	}

	// ////////////////////////////
	// ZONE CurrentItem Observer //
	// ////////////////////////////	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Notifier getNotifier() {
		return this.notifier;
	}


	////////////////////
	// ZONE Caches ////
	///////////////////
	/**
	 * Gets the number of column that can be displayed on the mobile screen
	 * for exemple a PDA with 480*800 in portrait mode use 1 column,
	 * a tablet or netbook with 1024*600 in landscape mode can use 4 columns
	 *
	 * @return the int number of columns
	 */
	public abstract int getScreenColumnNumber();
	
	/**
	 *
	 * Compute the width of the screen, based on the width return by android minus the margin set in shared preferences
	 *
	 * @return the screen width in pixels
	 */
	public abstract int getScreenWidth();
	
	/**
	 * Compute the width of a column based on device width and sharedPreferences
	 *
	 * @param p_oNbColumnToDisplay number of column to display
	 * @param p_oNbColumnOnTheScreen the numbre of columns in a screen
	 * @param p_iPaddingLeft The padding between planning and details columns
	 * @param p_iStdPadding the standard padding between columns
	 * @return a int.
	 */
	public abstract int getScreenColumnWidth(int p_oNbColumnToDisplay, int p_oNbColumnOnTheScreen, int p_iStdPadding, int p_iPaddingLeft);
	
	/**
	 * Reading the configuration to build the theoretical descriptors screen.
	 * A descriptor screen lets you know the feature tree.
	 * This reading is done only once at application startup.
	 * No update takes place during the life of the application.
	 */
	public abstract void initializeScreensDescriptor();
	// /////////////////
	// ZONE SETTINGS //
	// /////////////////

	/**
	 * Loads persistent settings
	 */
	public abstract void loadSettings();
	
	/**
	 * Get the default settings values associate with the keys
	 *
	 * @return a {@link java.util.Map} object.
	 */
	protected abstract Map<String, Object> getDefaultSettingsValues();
	
	/**
	 * Returns the setting names mandatory
	 *
	 * @return a list of setting names
	 */
	public String[] getMandatorySettingsDefinition() {
		return new String[] {SETTING_LOGIN, SETTING_PASSWORD, SETTING_URLSERVER};
	}

	/**
	 * Returns the setting names of string type
	 * don't delete the SETTING_APPVERSION because the startup verify that the app is not new with it
	 *
	 * @return a list of setting names
	 */
	public String[] getStringSettingsDefinition() {
		return new String[] {SETTING_LOGIN, SETTING_PASSWORD, SETTING_URLSERVER,
				SETTING_LANDSCAPE_WIDTH_MARGIN, SETTING_PORTRAIT_WIDTH_MARGIN, 
				SETTING_LANDSCAPE_COLUMN_NUMBER, SETTING_PORTRAIT_COLUMN_NUMBER, 
				SETTING_APPVERSION, SETTING_PROXYLOGIN, SETTING_PROXYPASSWORD, SETTING_PROXYURLSERVER};
	}

	/**
	 * <p>getIntegerSettingsDefinition.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */	
	public String[] getIntegerSettingsDefinition() {
		return new String[] {SETTING_URLTIMEOUT, SETTING_URLSOTIMEOUT};
	}
	
	/**
	 * Returns the setting names of boolean type
	 *
	 * @return a list of setting names
	 */
	public String[] getBooleanSettingsDefinition() {
		return new String[] {};
	}

	/**
	 * This method indicates if the configuration was changed or not.
	 *
	 * @return true if the configuration changed, false otherwise.
	 */
	public boolean isLoginAndUrlSettingChanged(){
		boolean r_bChanged = false;
		if (this.oldSetting.size() != this.setting.size()){
			r_bChanged = true;
		}else{
			String sPastValue = String.valueOf(this.oldSetting.get(SETTING_LOGIN));

			// Case-sensitive login fwk property
			Property oCaseSensitiveLoginProperty = ConfigurationsHandler.getInstance().getProperty(
					FwkPropertyName.case_sensitive_login);

			boolean bSameLogin = sPastValue.equalsIgnoreCase(String.valueOf(this.setting.get(SETTING_LOGIN)));
			if (oCaseSensitiveLoginProperty != null && Property.TRUE.equals(oCaseSensitiveLoginProperty.getValue())) {
				// Case-sensitive login
				bSameLogin = sPastValue.equals(String.valueOf(this.setting.get(SETTING_LOGIN)));
			}

			if (!bSameLogin) {
				r_bChanged = true;
			}
			sPastValue = String.valueOf(this.oldSetting.get(SETTING_URLSERVER));
			if (!sPastValue.equals(String.valueOf(this.setting.get(SETTING_URLSERVER)))){
				r_bChanged = true;
			}
		}
		return r_bChanged;
	}

	/**
	 * It updates the old setting with the current setting.
	 */
	public void updateOldSetting(){
		this.oldSetting.clear();
		this.oldSetting.putAll(this.setting);
	}

	/**
	 * Indicates if a mandatory setting is null.
	 * For string type an empty value is a null value.
	 *
	 * @return true if a setting is null
	 */
	public boolean hasUndefinedMandatorySetting() {
		boolean r_bUndefined = false;
		for (String sKey : this.getMandatorySettingsDefinition()) {
			if (!(this.setting.containsKey(sKey) && this.setting.get(sKey) != null && ((String) this.setting.get(sKey)).length() > 0)) {
				r_bUndefined = true;
				break;
			}
		}
		return r_bUndefined;
	}

	/**
	 * Returns the string setting for key.
	 *
	 * @param p_sKey the key to find
	 * @return value for key
	 */
	public String getStringSetting(String p_sKey) {
		return (String) this.setting.get(p_sKey);
	}
	
	/**
	 * Getter for decipted String setting
	 *
	 * @param p_sKey the key of the value to decrypt
	 * @return the decripted value
	 * @throws java.lang.Exception throw exception if decrypt fail
	 */
	public String getDecriptedStringSetting(final String p_sKey) throws Exception {
		return AESUtil.decrypt( (String) this.setting.get(p_sKey) );
	}

	/**
	 * Sets the string setting for a key
	 *
	 * @param p_sKey the key to use
	 * @param p_sValue the value to set
	 */
	public void setStringSetting(String p_sKey, String p_sValue) {
		this.setting.put(p_sKey, p_sValue);
	}

	/**
	 * Returns the boolean setting for key.
	 *
	 * @param p_sKey the key to find
	 * @return value for key
	 */
	public Boolean getBooleanSetting(String p_sKey) {
		return (Boolean) this.setting.get(p_sKey);
	}

	/**
	 * Sets the boolean setting for a key
	 *
	 * @param p_sKey the key to use
	 * @param p_bValue the value to set
	 */
	public void setBooleanSetting(String p_sKey, boolean p_bValue) {
		this.setting.put(p_sKey, p_bValue);
	}

	/**
	 * Returns the int setting for key.
	 *
	 * @param p_sKey the key to find
	 * @return value for key
	 */
	public int getIntSetting(String p_sKey) {
		int r_iresult = 0;
		if (this.setting.containsKey(p_sKey) && this.setting.get(p_sKey) != null) {
			if (Integer.class.equals(this.setting.get(p_sKey).getClass())) {
				r_iresult = (Integer) this.setting.get(p_sKey);
			} else {
				if (String.class.equals(this.setting.get(p_sKey).getClass())) {
					r_iresult = Integer.valueOf((String) this.setting.get(p_sKey));
				} else {
					this.getLog().debug("AbstarctFwkControllerImpl", StringUtils.concat("Malformed preference", p_sKey));
				}
			}
		} else {
			this.getLog().debug("AbstarctFwkControllerImpl", StringUtils.concat("Missing preference", p_sKey));
		}
		return r_iresult;
	}

	/**
	 * Sets the int setting for a key
	 *
	 * @param p_sKey the key to use
	 * @param p_sValue the value to set
	 */
	public void setIntSetting(String p_sKey, int p_sValue) {
		this.setting.put(p_sKey, p_sValue);
	}
	
	/**
	 * Returns the int setting for key.
	 *
	 * @param p_sKey the key to find
	 * @return value for key
	 */
	public long getLongSetting(String p_sKey) {
		long r_iresult = 0;
		if (this.setting.containsKey(p_sKey) && this.setting.get(p_sKey) != null) {
			if (Long.class.equals(this.setting.get(p_sKey).getClass())) {
				r_iresult = (Long) this.setting.get(p_sKey);
			} else {
				if (String.class.equals(this.setting.get(p_sKey).getClass())) {
					r_iresult = Long.valueOf((String) this.setting.get(p_sKey));
				} else {
					this.getLog().debug("AbstarctFwkControllerImpl", StringUtils.concat("Malformed preference", p_sKey));
				}
			}
		} else {
			this.getLog().debug("AbstarctFwkControllerImpl", StringUtils.concat("Missing preference", p_sKey));
		}
		return r_iresult;
	}
	
	/**
	 * Sets the int setting for a key
	 *
	 * @param p_sKey the key to use
	 * @param p_sValue the value to set
	 */
	public void setLongSetting(String p_sKey, long p_sValue) {
		this.setting.put(p_sKey, p_sValue);
	}

	/**
	 * Computes volatiles settings
	 */
	public void computeSettings() {
		String sUrl = this.getStringSetting(SETTING_URLSERVER);
		URL oUrl = null;
		if (sUrl != null) {
			try {
				oUrl = new URL(sUrl);
				this.setStringSetting(SETTING_COMPUTE_URLSERVER_HOST, oUrl.getHost());
				int iPort = oUrl.getPort();
				if (iPort==-1) {
					iPort = RestConnectionConfig.DEFAULT_PORT;
				}
				this.setIntSetting(SETTING_COMPUTE_URLSERVER_PORT, iPort);

				int timeout = this.getIntSetting(SETTING_URLTIMEOUT);
				if (timeout <= 0) {
					timeout = RestConnectionConfig.TIME_OUT;
				}
				this.setIntSetting(SETTING_COMPUTE_URLSERVER_TIMEOUT, timeout);
				
				int soTimeout = this.getIntSetting(SETTING_URLSOTIMEOUT);
				if (soTimeout <= 0) {
					soTimeout = RestConnectionConfig.SO_TIME_OUT;
				}
				this.setIntSetting(SETTING_COMPUTE_URLSERVER_SOTIMEOUT, soTimeout);
				
				// If sPath is null or empty, the synchronization does not work.
				// In this case, sPath is valued at "/".
				String sPath = oUrl.getPath();
				if (sPath == null || sPath.length() == 0) {
					sPath = ROOT_PATH;
				}

				this.setStringSetting(SETTING_COMPUTE_URLSERVER_PATH, sPath);
			} catch (MalformedURLException e) {
				// A2A_DEV Rediriger vers un message d'erreur.
			}
		}
		sUrl = this.getStringSetting(SETTING_PROXYURLSERVER);
		if (sUrl != null) {
			try {
				oUrl = new URL(sUrl);
				this.setStringSetting(SETTING_COMPUTE_PROXYURLSERVER_HOST, oUrl.getHost());
				this.setIntSetting(SETTING_COMPUTE_PROXYURLSERVER_PORT, oUrl.getPort());
				this.setStringSetting(SETTING_COMPUTE_PROXYURLSERVER_PATH, oUrl.getPath());
			} catch (MalformedURLException e) {
				// A2A_DEV Rediriger vers un message d'erreur.
			}
		}
	}

	/**
	 * Supprime l'une des propriété contenue dans le cache de l'application.
	 *
	 * @param p_sKey la clé de la propriété à supprimer
	 * @return true si la suppression s'est faite, false sinon.
	 */
	public boolean removeSetting(String p_sKey){
		return this.setting.remove(p_sKey) != null;
	}

	/**
	 * Supprime l'ensemble des propriétés.
	 */
	public void removeSettings() {
		this.setting.clear();
		this.oldSetting.clear();
	}

	/**
	 * Récupère une chaine de caractères à partir d'une clé.
	 *
	 * @param p_oKey La clé
	 * @return la chaine de caractères associée à la clé ou null si aucune chaîne ne correspond à la clé.
	 */
	public String getStringResource(final ApplicationR p_oKey) {
		String r_sResource = null;

		if (ApplicationRGroup.STRING == p_oKey.getGroup()) {
			final Property oProperty = ConfigurationsHandler.isLoaded() ? ConfigurationsHandler.getInstance().getProperty(p_oKey.getKey()) : null;
			if (oProperty != null) {
				r_sResource = oProperty.getValue();
			}
		}

		return r_sResource;
	}

	/**
	 * Récupère une chaine de caractères à partir d'une clé.
	 *
	 * @param p_sKey La clé
	 * @return la chaine de caractères associée à la clé ou null si aucune chaîne ne correspond à la clé.
	 */
	public String getStringResource(final String p_sKey) {
		String r_sResource = null;

		final Property oProperty = ConfigurationsHandler.isLoaded() ? ConfigurationsHandler.getInstance().getProperty(p_sKey) : null;
		if (oProperty != null) {
			r_sResource = oProperty.getValue();
		}

		return r_sResource;
	}

	/**
	 * Récupère une chaine de caractères à partir d'une clé.
	 *
	 * @param p_sKey La clé
	 * @return la chaine de caractères associée à la clé ou null si aucune chaîne ne correspond à la clé.
	 */
	public final String getStringResource(final PropertyName p_sKey) {
		return this.getStringResource(p_sKey.name());
	}

	/**
	 * TODO Décrire la méthode getIntResource de la classe Application
	 *
	 * @param p_sKey a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.PropertyName} object.
	 * @return a int.
	 */
	public int getIntResource(final PropertyName p_sKey) {
		final String s = getStringResource(p_sKey);
		int i = Integer.parseInt(s);
		return i;
	}

	/**
	 * <p>getWsEntryPoint.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */	
	public abstract String getWsEntryPoint();

	/**
	 * <p>getVisibleProperty.</p>
	 *
	 * @param p_sName a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean getVisibleProperty(String p_sName) {
		return ConfigurationsHandler.getInstance().getProperty(p_sName + "$visible").getBooleanValue();
	}

	/**
	 * Return the identifier of the connected resource.
	 *
	 * @return a long.
	 */
	public long getCurrentUserResource() {
		return this.currentUserResource;
	}

	/**
	 * Defines the identifier of the connected resource.
	 *
	 * @param p_lCurrentUserResource The identifier of the connected resource.
	 */
	public void setCurrentUserResource(final long p_lCurrentUserResource) {
		this.currentUserResource = p_lCurrentUserResource;
	}

	/**
	 * <p>getTranscientObjectFromCache.</p>
	 *
	 * @param p_sId a {@link java.lang.String} object.
	 * @param p_oType a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */	
	public <T> T getTranscientObjectFromCache(String p_sId, Class<? extends T> p_oType) {
		return (T) this.cacheTranscientObjects.get(p_sId + "_" + p_oType.getName());
	}

	/**
	 * <p>addTranscientObjectToCache.</p>
	 *
	 * @param p_sId a {@link java.lang.String} object.
	 * @param p_oType a {@link java.lang.Class} object.
	 * @param p_oObject a {@link java.lang.Object} object.
	 */
	public void addTranscientObjectToCache(String p_sId, Class<?> p_oType, Object p_oObject) {
		this.cacheTranscientObjects.put(p_sId + "_" + p_oType.getName(), p_oObject);
	}

	 /**
	  * <p>getConfigurationDataFromCache.</p>
	  *
	  * @param p_sIdContext a {@link java.lang.Integer} object.
	  * @param p_sIdDialog a {@link java.lang.Integer} object.
	  * @param <T> a T object.
	  * @return a T object.
	  */	
	public <T> T getConfigurationDataFromCache(Integer p_sIdContext, Integer p_sIdDialog) {
		if(p_sIdContext != null && p_sIdDialog != null) {
			 Map<Integer, Object> map =  this.cacheConfigurationData.get(p_sIdContext);
			 if(map != null) {
				 return (T) map.get(p_sIdDialog);
			 }
		}
		return null;
	}
	
	/**
	 * <p>putConfigurationDataToCache.</p>
	 *
	 * @param p_sContextId a {@link java.lang.Integer} object.
	 * @param p_sComponentId a {@link java.lang.Integer} object.
	 * @param p_oObject a T object.
	 * @param <T> a T object.
	 */	
	public <T> void putConfigurationDataToCache(Integer p_sContextId, Integer p_sComponentId, T p_oObject) {
		Map<Integer,Object> mapObjectsToSave = this.cacheConfigurationData.get(p_sContextId);
		if (mapObjectsToSave == null) {
			mapObjectsToSave = new TreeMap<Integer, Object>();
			this.cacheConfigurationData.put(p_sContextId, mapObjectsToSave);
		}
		mapObjectsToSave.put(p_sComponentId, p_oObject);
	}

	/**
	 * <p>cleanCacheContextConfigurationData.</p>
	 *
	 * @param p_sIdContext a {@link java.lang.Integer} object.
	 */	
	public void cleanCacheContextConfigurationData(Integer p_sIdContext) {
		this.cacheConfigurationData.get(p_sIdContext);

	}
	
	/**
	 * <p>cleanAllCacheConfigurationData.</p>
	 */	
	public void cleanAllCacheConfigurationData() {
		this.cacheConfigurationData.clear();

	}

	/**
	 * Affecte une nouvelle instance du logger.
	 *
	 * @param p_oLogger le logger à affecter.
	 */
	public void setLogger( Logger p_oLogger ) {
		this.log = p_oLogger ;
	}

	/**
	 * <p>Getter for the field <code>user</code>.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.user.User} object.
	 */
	public User getUser() {
		return this.user ;
	}

	/**
	 * <p>Setter for the field <code>user</code>.</p>
	 *
	 * @param p_oUser a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.user.User} object.
	 */	
	public void setUser( User p_oUser ) {
		this.user = p_oUser ;
	}

	/**
	 * Donne l'analyse d'une classe
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyser} object.
	 */
	public ClassAnalyser getClassAnalyser() {
		return this.analyser;
	}

	/**
	 * TODO Décrire la méthode putConfigurationData de la classe Application
	 *
	 * @param p_oUid a int.
	 * @param p_oListToSave a {@link java.util.Map} object.
	 */
	public void putConfigurationData(int p_oUid, Map<String, Object> p_oListToSave) {
		// nothing to do
	}

	/**
	 * Increases the number of known instances of a listener when it is of type {@link ListenerIdentifier}.
	 * The reference is done by its unique id.
	 * @param p_oListenersMap the map to modify
	 * @param p_sUniqueId the unique id of the listener
	 * @return the number of known instances
	 */
	private int increaseListenerIdentifierCounter(Map<String, Integer> p_oListenersMap, String p_sUniqueId) {
		int iNum = 1;
		
		if (p_oListenersMap.containsKey(p_sUniqueId)) {
			iNum = p_oListenersMap.get(p_sUniqueId);
			iNum++;
		}
		
		p_oListenersMap.put(p_sUniqueId, iNum);
		
		return iNum;
	}
	
	/**
	 * Decreases the number of known instances of a listener when it is of type {@link ListenerIdentifier}.
	 * The reference is done by its unique id.
	 * Returns the number of known instances
	 * @param p_oListenersMap the map to modify
	 * @param p_sUniqueId the unique id of the listener
	 * @return the number of known instances
	 */
	private int decreaseListenerIdentifierCounter(Map<String, Integer> p_oListenersMap, String p_sUniqueId) {
		int iNum = 0;
		
		if (p_oListenersMap.containsKey(p_sUniqueId)) {
			iNum = p_oListenersMap.get(p_sUniqueId);
			iNum--;
		}
		
		if (iNum == 0) {
			p_oListenersMap.remove(p_sUniqueId);
		} else {
			p_oListenersMap.put(p_sUniqueId, iNum);
		}
		
		return iNum;
	}

	/**
	 * <p>getConnectionType.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.network.ConnectionType} object.
	 */
	public abstract ConnectionType getConnectionType();

	/**
	 * <p>getImageByName.</p>
	 *
	 * @param p_sName a {@link java.lang.String} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public abstract <T> T getImageByName(String p_sName);


	/**
	 * return if the execution is on main thread (UI)
	 *
	 * @return true if this is the main thread false overwise
	 */
	public abstract boolean isOnUIThread();

	/**
	 * <p>runOnUiThread.</p>
	 *
	 * @param p_oRunnable a {@link java.lang.Runnable} object.
	 */
	public abstract void runOnUiThread(Runnable p_oRunnable);

	/**
	 * Return true to show the setting popup.
	 * <p>This propertie is link to the ressource R.string.display_setting_window</p>
	 */
	public abstract boolean displayMandatorySettingWindow();

	/**
	 * Return true if the permission is requested for application running.
	 * @param permission the permission string
	 * @return true if the permission is mandatory requested, false otherwise
	 */
	public abstract boolean isNotMantatoryRequested(String p_sPermission);
}
