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
package com.adeuza.movalysfwk.mobile.mf4android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.activity.business.displaymain.MFRootActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.GenericLoadDataForDisplayDetailAction;
import com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.app.LifecycleDispatchActionBarActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.application.ApplicationMainImpl;
import com.adeuza.movalysfwk.mobile.mf4android.application.MFAndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.Starter;
import com.adeuza.movalysfwk.mobile.mf4android.serialization.SparseArrayIntArrayParcelable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMCustomDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.PermissionUtil.PermissionRequestObject;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.AbstractDataloader.ReloadLoaderBusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.listener.ListenerOnBusinessNotification;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.AbstractResultEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccess;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerDelegator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerIdentifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ScreenListenerDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ClassicSynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessageLevel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessagesList;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.ScreenDelegate;
import com.soprasteria.movalysmdk.widget.core.provider.MDKWidgetApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Android implementation of screen
 * </p>
 * 
 * 
 */
public abstract class AbstractMMActivity extends LifecycleDispatchActionBarActivity implements Screen , Comparable<Screen>, ListenerDelegator, ListenerIdentifier {

	/** the mask to match result code */
	private static final int ACTIVITY_RESULT_MASK = 0xffff0000;
	/** the mask to construct screen code */
	private static final int ACTIVITY_SUPPORT_MASK = 0x0000ffff;
	/** mask for result code on startActivityForResult */
	protected static final int REQUEST_CODE_MASK = 0x0000ffff;
	/** constant to create parameter activity */
	public static final int REDIRECT_TO_PARAMETER_ACTIVITY_REQUEST_CODE = 0x000f123;
	/**
	 * Utilisé pour retrouver l'activité de détail précédemment lancée dans un intent.
	 */
	public static final String SCREEN_PARAMETER = "screen";
	/** key to compute id for retreive dialog or activity */
	private static final long KEY_ROOT = 10;

	/** delegate for screen behavior */
	private ScreenDelegate screenDelegate = new ScreenDelegate();
	/** destroy ? */
	private boolean destroyed = false;
	/** android application */
	private AndroidApplication androidApplication = null;
	/** list of dialog */
	private List<HashMap<String, String>> complexeComponents = new ArrayList<>();
	/** Map of activityResults */
	private SparseArray<ActivityResultHolder> activityResults = new SparseArray<>();
	
	/** Runtime permission request */
	private PermissionRequestObject mRequestObject;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);

		// Test if we have to stopped activity
		if ( this.shouldActivityBeStopped()) {
			return;
		}

		// Redémarrage Application suite arret process
		if(!this.getClass().getName().contains("ApplicationMainImpl") && !this.getClass().getName().contains("ParameterDialogActivity")
			&& (Application.getInstance() == null || ConfigurationsHandler.getInstance() == null || BeanLoader.getInstance() == null) ) {
			Log.v(Application.LOG_TAG,"Application restart !");

			Starter oStarter = new Starter(this);
			oStarter.runStandalone();
			
		}

		this.androidApplication = (AndroidApplication) Application.getInstance();
		
		try {
			int iTitleId = this.getPackageManager().getActivityInfo(this.getComponentName(), 0).labelRes;
			if (iTitleId > 0) {
				this.setTitle(this.getAndroidApplication().getStringResource(iTitleId));
			}
		}catch (NameNotFoundException e) {
			throw new MobileFwkException(e);
		}

		this.androidApplication.analyzeClassOf(this);

		if (p_oSavedInstanceState != null) {
			Application.getInstance().setCurrentUserResource(p_oSavedInstanceState.getLong("CurrentUserResource"));
			this.getAndroidApplication().setScreenDictionary((SparseArrayIntArrayParcelable)p_oSavedInstanceState.getParcelable("screenDictionary"));
			this.complexeComponents = (ArrayList<HashMap<String, String>>) p_oSavedInstanceState.getSerializable("complexeComponents");
		}


	}

	@Override
	protected void onStart() {
		super.onStart();
		this.getAndroidApplication().addActiveDisplayList(this);
		
		// Show dialog if an action is running
		List<MMActionTask<?, ?, ?, ?>> listActions = this.getAndroidApplication().getRunningActionsForActiveDisplay(this);
		if ( !listActions.isEmpty()) {
			for ( MMActionTask<?,?,?,?>oAction : listActions) {
				if (oAction.getAction().getConcurrentAction() != Action.NO_QUEUE) {	
					oAction.createProgressDialog();
					break;
				}
			}
		}

		// if no splash is defined and we should show settings
		if (this.getAndroidApplication().displayMandatorySettingWindow()) {
			this.loadSettings();
			if (this.getAndroidApplication().hasUndefinedMandatorySetting()) {
				this.getAndroidApplication().getController().doDisplayParameterDialog();
			}
		}
	}
	
	@Override
	protected void onStop() {
		
		// Dismiss dialog is an action is running
		List<MMActionTask<?, ?, ?, ?>> listActions = this.getAndroidApplication().getRunningActionsForActiveDisplay(this);
		if ( !listActions.isEmpty()) {
			listActions.get(0).dismissProgressDialog();
		}
		
		// Unregister active display
		this.getAndroidApplication().removeActiveDisplayFromList(this, isFinishing());

		// If finishing (not called on orientation changed or going background), unregister all running actions
		if ( this.isFinishing()) {
			Application.getInstance().unregisterAllRunningActions(this);
			
			// Disable event storage for this display
			Application.getInstance().disableStoreEventsForDisplay(this);
		}
		
		super.onStop();
	}

	@Override
	public void onRequestPermissionsResult(int p_iRequestCode, String[] p_oPremissions, int[] p_oGrantResults) {
		
		if (mRequestObject != null) {
			mRequestObject.onRequestPermissionsResult(p_iRequestCode, p_oPremissions, p_oGrantResults);
		}
		
		super.onRequestPermissionsResult(p_iRequestCode, p_oPremissions, p_oGrantResults);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle p_oSavedInstanceState) {

		p_oSavedInstanceState.putLong("CurrentUserResource", Application.getInstance().getCurrentUserResource());

		// Cast to HashMap because Map is not serializable, but the used implementation (hashMap) is serializable
		p_oSavedInstanceState.putParcelable("screenDictionary", androidApplication.getScreenDictionary());

		//Save the complexe components
		p_oSavedInstanceState.putSerializable("complexeComponents", new ArrayList<>(this.complexeComponents) );

		super.onSaveInstanceState(p_oSavedInstanceState);
	}

	/**
	 * Retourne l'instance de l'application Android.
	 * @return objet de type <em>AndroidApplication</em>
	 */
	protected AndroidApplication getAndroidApplication() {
		return this.androidApplication;
	}

	/**
	 * Accesseur du screen delegate
	 * @return screen delegate lié
	 */
	public ScreenDelegate getScreenDelegate() {
		return this.screenDelegate;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu p_oMenu) {
		if (!Application.getInstance().displayMandatorySettingWindow() ||
				!Application.getInstance().hasUndefinedMandatorySetting()) {
			MenuInflater oInflater = this.getMenuInflater();

			for( Integer iMenuId : getOptionMenuIds()) {
				oInflater.inflate(iMenuId, p_oMenu);
			}
			this.inflateMenu(p_oMenu);
		}
		return super.onCreateOptionsMenu(p_oMenu);
	}

	/**
	 * Retourne la liste des identifiants des menus à afficher.
	 * @return liste d'entier
	 */
	public List<Integer> getOptionMenuIds() {
		List<Integer> r_listMenuIds = new ArrayList<>();
		r_listMenuIds.add(((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.menu_base));
		return r_listMenuIds ;
	}

	/**
	 * Rend visible l'item du menu envoyé paramètre.
	 * @param p_oMenu le menu dont on veut rendre visible un item
	 * @param p_oR l'item que l'on souhaite rendre visible
	 */
	protected void setVisible(Menu p_oMenu, ApplicationR p_oR) {
		p_oMenu.findItem(androidApplication.getAndroidIdByRKey(p_oR)).setVisible(this.androidApplication.getVisibleProperty(p_oR.getKey()));
	}

	/**
	 * Sets the literal name of activity
	 * 
	 * @see android.app.Activity#setContentView(int)
	 * @param p_iLayoutResID
	 *            content view id
	 */
	@Override
	public void setContentView(int p_iLayoutResID) {	
		super.setContentView(p_iLayoutResID);
		if (Application.getInstance().isLoaded()) {
			this.screenDelegate.setName(((AndroidApplication) Application
					.getInstance()).getAndroidIdStringByIntKey(p_iLayoutResID));
		}
		doAfterSetContentView();
	}

	/**
	 * Do after set content view
	 */
	protected void doAfterSetContentView(){
		//NothingToDo
	}

	/**
	 * Add a listener on all menu
	 * 
	 * @param p_oMenu
	 *            the menu to inflate
	 */
	protected void inflateMenu(Menu p_oMenu) {
		MenuItem oItem = null;

		String sTitle = null;
		for (int i = 0; i < p_oMenu.size(); i++) {
			oItem = p_oMenu.getItem(i);

			try {
				sTitle = Application.getInstance().getStringResource(((AndroidApplication)Application.getInstance())
						.getAndroidIdStringByIntKey(oItem.getItemId()).concat("__title"));
				if (sTitle != null) {
					oItem.setTitle(sTitle);
				}
			} catch( Resources.NotFoundException oException ) { 
				// No title override
				this.getAndroidApplication().getLog().debug("AbstractMMActivity", "No title override");
			}

			if (oItem.hasSubMenu()) {
				this.inflateMenu(oItem.getSubMenu());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.screenDelegate.getName();
	}

	/**
	 * Launch an activity for which a component would like a result when it finished. When this activity exits, your onActivityResult() method will be called with the given requestCode.
	 * AbstractMMActivity dispatch the result to the component p_oComponent
	 * @param p_oIntent the intent to use
	 * @param p_oComponent the component
	 * @param p_iRequestCode the request code to use
	 */
	public void startActivityForResult(Intent p_oIntent, MMComplexeComponent p_oComponent, int p_iRequestCode) {
		int[] iValues={((View)p_oComponent).getId(), p_iRequestCode};

		registerComplexeComponent(p_oComponent);

		int iKey=Math.abs(computeKey(((View)p_oComponent).getId(), p_iRequestCode)) & ACTIVITY_SUPPORT_MASK;

		this.androidApplication.getScreenDictionary().put(iKey, iValues);
		this.startActivityForResult(p_oIntent, iKey);
	}

	/**
	 * Modifies the standard behavior to allow results to be delivered to fragments.
	 * This imposes a restriction that requestCode be <= 0xffff.
	 * @param p_oIntent the child result intent
	 * @param p_iRequestCode the child result code
	 */
	@Override
	public void startActivityForResult(Intent p_oIntent, int p_iRequestCode) {
		if (p_iRequestCode != -1 && (p_iRequestCode&ACTIVITY_RESULT_MASK) != 0) {
			throw new MobileFwkException("AbstractActivity", "request code to large");
		}
		super.startActivityForResult(p_oIntent, p_iRequestCode);
	}

	/**
	 * Compute a key to retreive component and dispatche messages
	 * @param p_oComponentId the component id
	 * @param p_iDialogId the dialog id
	 * @return the key for the component and dialog
	 */
	private int computeKey(int p_oComponentId, int p_iDialogId){
		return (int)(p_oComponentId*KEY_ROOT+p_iDialogId);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem p_oMenuItem) {
		boolean r_bHandled = true;
		if (p_oMenuItem.hasSubMenu()) {
			this.inflateMenu(p_oMenuItem.getSubMenu());
			r_bHandled = true;
		}
		try {
			ClassAnalyse oClassAnalyze = Application.getInstance().getClassAnalyser().getClassAnalyse(this);
			Method oListenerMethod = oClassAnalyze.getMethodOfMenuItemEventListener(p_oMenuItem.getItemId());
			if ( oListenerMethod != null ) {
				oListenerMethod.invoke(this) ;
			}
			else {
				// récupération de l'id du menu
				// le nom de la méthode à appeler est située après le dernier  "_"
				String sMenuId = ((AndroidApplication) Application.getInstance()).getAndroidIdStringByIntKey(p_oMenuItem.getItemId());
				// A2A_DEV mettre en cache ....

				String sMenu = sMenuId.substring(sMenuId.lastIndexOf('_') + 1);
				Method oMethod = Application.getInstance().getController().getClass().getMethod(sMenu, 
						new Class<?>[] { Screen.class} );
				oMethod.invoke(Application.getInstance().getController(), AbstractMMActivity.this);
				r_bHandled = true;
			}
		} catch (SecurityException e) {
			throw new MobileFwkException(e);
		} catch (NoSuchMethodException e) {
			// call super
			r_bHandled = super.onOptionsItemSelected(p_oMenuItem);
		} catch (IllegalArgumentException e) {
			throw new MobileFwkException(e);
		} catch (IllegalAccessException e) {
			throw new MobileFwkException(e);
		} catch (InvocationTargetException e) {
			throw new MobileFwkException(e);
		}
		return r_bHandled;
	}

	/**
	 * Replay on activity results
	 */
	public void replayOnActivityResults() {
		for( int i = 0 ; i < this.activityResults.size(); i++ ) {
			ActivityResultHolder oHolder = this.activityResults.valueAt(i);
			onActivityResult(oHolder.getRequestCode(), oHolder.getResultCode(), oHolder.getIntent());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onActivityResult(int p_oRequestCode, int p_oResultCode, Intent p_oData) {
		super.onActivityResult(p_oRequestCode, p_oResultCode, p_oData);
		MMComplexeComponent oMMComplexeComponent = null;

		// Cas d'un startActivityForResult invoqué par une MMValuableViewMM
		// on renvoie vers le onActivityResult du composant qui a généré
		// l'activityforresult
		int[] iValuesInDictionnary = this.androidApplication.getScreenDictionary().get(p_oRequestCode);

		if (iValuesInDictionnary!=null) {
			 int idxToRemove = -1;
			for(int i = 0; i < this.complexeComponents.size(); i++) {
				HashMap<String, String> mapComplexeComponentDescriptor  = this.complexeComponents.get(i);
				int iComplexecomponentId = Integer.parseInt(mapComplexeComponentDescriptor.get(MMComplexeComponent.COMPLEXE_COMPONENT_ID_KEY));
				String sComplexeComponentFragmentTag = mapComplexeComponentDescriptor.get(MMComplexeComponent.COMPLEXE_COMPONENT_FRAGMENT_TAG_KEY);
				
				if (mapComplexeComponentDescriptor.get(MMComplexeComponent.COMPLEXE_COMPONENT_REQUEST_CODE) != null) {
					int iComplexeComponentRequestCode = Integer.parseInt(mapComplexeComponentDescriptor.get(MMComplexeComponent.COMPLEXE_COMPONENT_REQUEST_CODE));
					if (iComplexecomponentId == iValuesInDictionnary[0]
						&& iComplexeComponentRequestCode == iValuesInDictionnary[1]) {
						oMMComplexeComponent = findComplexeComponentWithRequestCode(iComplexecomponentId, sComplexeComponentFragmentTag, iComplexeComponentRequestCode);
						idxToRemove = i;
					}
				} else {
					if (iComplexecomponentId == iValuesInDictionnary[0]) {
						oMMComplexeComponent = (MMComplexeComponent) findComponent(iComplexecomponentId, sComplexeComponentFragmentTag);
						idxToRemove = i;
					}
				}
			}

			if ( oMMComplexeComponent != null ) { 			
				oMMComplexeComponent.onActivityResult(iValuesInDictionnary[1], p_oResultCode, p_oData);
				this.activityResults.remove(iValuesInDictionnary[0]);
				this.complexeComponents.remove(idxToRemove);
			}
			else {
				// On restoreState, dialog may be not reconstructed, so we save the results for later treatment.				
				this.activityResults.put(iValuesInDictionnary[0], new ActivityResultHolder(p_oRequestCode, p_oResultCode, p_oData));
			}
		}

		// cas d'un onActivityForResult exécuté par une autre activité
		else {

			// if go back from Preference activity, we reload settings
			if (p_oRequestCode == ParameterActivity.PREFERENCE_ACTIVITY_REQUEST_CODE) {
				this.loadSettings();
			}

			// cas du clic sur le bouton Paramétrer de la popup du splash screen
			else if (p_oRequestCode == REDIRECT_TO_PARAMETER_ACTIVITY_REQUEST_CODE
					&& p_oResultCode == RESULT_OK) {

				// lancement de l'action pour l'affichage de la page de paramétrage
				Application.getInstance().getController().doDisplaySetting(this);
			}
			// cas du clic sur le bouton quiter de la popup du splash screen
			else if (p_oRequestCode == REDIRECT_TO_PARAMETER_ACTIVITY_REQUEST_CODE
					&& p_oResultCode == RESULT_CANCELED) {
				// fermeture de l'application
				((MFAndroidApplication) getApplication()).launchStopApplication();
			}
		}
		
		if (getApplication() instanceof MDKWidgetApplication) {
            ((MDKWidgetApplication) getApplication()).getMDKWidgetComponentActionHelper().handleActivityResult(p_oRequestCode, p_oResultCode, p_oData);
        }
	}

	/**
	 * NE DEVRAIT PAS ETRE ICI NE PAS SURCHARGER, NE PAS UTILISER AUTRE QUE LE FWK
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationMain#loadSettings()
	 */
	public void loadSettings() {
		// Récupération des informations relatives aux users
		// Chargement des settings (login,mdp,url serveur, etc.)
		Application.getInstance().loadSettings();
		this.analyseSettings();
	}

	/**
	 * NE DEVRAIT PAS ETRE ICI NE PAS SURCHARGER, NE PAS UTILISER AUTRE QUE LE FWK
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationMain#analyseSettings()
	 */
	public void analyseSettings() {

		//!!! ne peut pas être fait au démarrage tout n'est pas initialiser pour lancer les actions
		if ( !ApplicationMainImpl.class.isAssignableFrom(this.getClass())) { 
			Application oApplication = Application.getInstance();
			if(!oApplication.hasUndefinedMandatorySetting() && oApplication.isLoginAndUrlSettingChanged()){
				oApplication.getController().doResetDataBase(this);
				oApplication.updateOldSetting();
				oApplication.getController().doClassicSynchronisation(this);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object p_oObj) {
		boolean r_bResult = false;
		if(p_oObj == null){
			return false;
		}
		if (p_oObj instanceof Screen){
			Screen oScreen = (Screen) p_oObj;
			r_bResult = this.compareTo(oScreen) == 0;
		}
		else if (p_oObj instanceof AbstractMMActivity){
			AbstractMMActivity act = (AbstractMMActivity)p_oObj;
			r_bResult = this.toString().equals(act.toString());
		}		
		return r_bResult;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDestroy() {
		
		if (!destroyed) { 
			this.localDestroy();
		}
		super.onDestroy();
	}

	/**
	 * Détruit la référence du screen courant dans l'application.
	 */
	public void localDestroy() {
		
		if ( this.isFinishing()) {
			// If finishing (not called on orientation changed or going background), unregister all running actions
			Application.getInstance().unregisterAllRunningActions(this);
			
			// Disable event storage for this display
			Application.getInstance().disableStoreEventsForDisplay(this);
		}
		
		if (this.getAndroidApplication() != null) {
			this.getAndroidApplication().removeListenerFromAction(this);
			this.getAndroidApplication().removeListenerFromDataLoader(this);
			this.getAndroidApplication().removeListenerFromEventManager(this);
		}

		this.destroyed = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Screen p_oScreen) {
		return p_oScreen.toString().compareTo(this.toString() ); 
	}

	/**
	 * Lance une action depuis l'écran courant
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 */
	public void launchAction(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameterIn) {
		this.launchAction(p_oActionClass, p_oParameterIn, this);
	}

	/**
	 * Launch an action from the current screen
	 * @param p_oAction instance de l'action à lancer
	 * @param p_oInterfaceClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 */
	public void launchAction(Action<?,?,?,?> p_oAction, Class<? extends Action<?,?,?,?>> p_oInterfaceClass, ActionParameter p_oParameterIn) {
		this.launchAction(p_oAction, p_oInterfaceClass, p_oParameterIn, this);
	}

	/**
	 * Launch an action from the current screen
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 * @param p_oSource the source object
	 */
	public void launchAction(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource) {
		Application.getInstance().getController().launchActionByActionTask(this, p_oActionClass, p_oParameterIn, p_oSource);
	}

	/**
	 * Lance une action depuis Ml'écran courant
	 * @param p_oAction instance de l'action à lancer
	 * @param p_oInterfaceClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 * @param p_oSource the source object
	 */
	public void launchAction(Action<?,?,?,?> p_oAction, Class<? extends Action<?,?,?,?>> p_oInterfaceClass, 
			ActionParameter p_oParameterIn, Object p_oSource) {
		Application.getInstance().getController().launchActionByActionTask(
				this, p_oAction, p_oInterfaceClass, p_oParameterIn, p_oSource);
	}

	/**
	 * Lance une action depuis l'écran courant
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 */
	public void launchSyncAction(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameterIn) {
		this.launchSyncAction(p_oActionClass, p_oParameterIn, this);
	}

	/**
	 * Lance une action depuis l'écran courant
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 * @param p_oSource the source object
	 */
	public void launchSyncAction(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource) {
		Application.getInstance().getController().launchActionByActionTask(this, p_oActionClass, p_oParameterIn, p_oSource);
	}

	
	/**
	 * Lance une action depuis l'écran courant avec une écoute sur tous les écrans
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 */
	public void launchActionOnApplicationScope(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameterIn) {
		if (p_oParameterIn != null)
			p_oParameterIn.setActionAttachedActivity(true);
		
		this.launchAction(p_oActionClass, p_oParameterIn, this);
	}
	
	/**
	 * Traitement des erreurs par défaut
	 * @param p_oMessages issus de l'action.
	 */
	public void treatDefaultError(MessagesList p_oMessages) {
		MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder(this);

		oBuilder.setTitle(DefaultApplicationR.error);
		oBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		List<ApplicationR> listMessages = p_oMessages.getMessagesByLevel(MessageLevel.ERROR);
		if (listMessages == null || listMessages.isEmpty()) {
			oBuilder.setMessage(DefaultApplicationR.error_inform);
		}
		else {
			oBuilder.setMessages(listMessages);
		}

		oBuilder.setPositiveButton(AndroidApplicationR.generic_message_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p_oDialog, int p_iId) {
				p_oDialog.cancel();
			}
		});

		MMCustomDialogFragment oAlert = oBuilder.create();
		oAlert.setCancelable(false);
		oAlert.show(getSupportFragmentManager(), MMCustomDialogFragment.CUSTOM_DIALOG_TAG);
	}

	/**
	 * Do on synchro finish
	 * @param p_oEvent Success event of action
	 */
	@ListenerOnActionFail(action = ClassicSynchronizationAction.class)
	@ListenerOnActionSuccess(action = ClassicSynchronizationAction.class)
	public void doOnSynchroFinish(AbstractResultEvent<SynchronizationActionParameterOUT> p_oEvent) {
		SynchronizationActionParameterOUT oResult = null ;
		if ( p_oEvent != null) {
			oResult = p_oEvent.getActionResult() ;
		}
		this.doOnSynchroFinish( oResult );
	}

	/**
	 * Do on synchro finished
	 * @param p_oResult the synchro out parameter
	 */
	protected void doOnSynchroFinish(SynchronizationActionParameterOUT p_oResult) {
		if (p_oResult == null) {
			return;
		}
		
		final boolean bExitAppOnFirstSynchroFailure = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.synchronization_exitAppOnFirstSynchroFailure).getBooleanValue();

		if(bExitAppOnFirstSynchroFailure){
			if (p_oResult.emptyDBSynchronizationFailure ){
				if(p_oResult.noConnectionSynchronizationFailure) {
					// Message informant de la fermeture de l'application
					this.computeErrorCustomDialog(AndroidApplicationR.screen_synchro_activity_name, AndroidApplicationR.screen_initFailed_noConnectionFailure, true );
				}
				else if (p_oResult.brokenSynchronizationFailure) {
					// Message informant de la fermeture de l'application
					this.computeErrorCustomDialog(AndroidApplicationR.screen_synchro_activity_name, AndroidApplicationR.screen_initFailed_brokenSyncFailure, true );
				}
				else if (p_oResult.incompatibleServerMobileTimeFailure ) {
					this.computeErrorCustomDialog(AndroidApplicationR.screen_synchronisation_incompatibles_times_failure_title, AndroidApplicationR.screen_synchronisation_incompatibles_times_failure_force_close_application, true );
				}
				else {
					// Message informant de la fermeture de l'application
					this.computeErrorCustomDialog(AndroidApplicationR.screen_synchro_activity_name, AndroidApplicationR.screen_initFailed_errorInSyncFailure, true );
				}
				this.resetPasswordSetting();
			}
			else if (p_oResult.incompatibleServerMobileTimeFailure) {
				this.computeErrorCustomDialog(AndroidApplicationR.screen_synchronisation_incompatibles_times_failure_title, AndroidApplicationR.screen_synchronisation_incompatibles_times_failure, false );
			}
			else if (p_oResult.waitedTooLongBeforeSync) {
				//Message informant que l'utilisateur n'a pas synchronisé depuis trop longtemps et qu'il doit redémarrer l'application
				this.computeErrorCustomDialog(AndroidApplicationR.screen_synchronisation_delayedsync_failure_title, AndroidApplicationR.screen_synchronisation_delayedsync_failure, true );
			}
			else if ( p_oResult.authenticationFailure ) {
				this.computeErrorCustomDialog(AndroidApplicationR.screen_synchronisation_authentication_failure_title, AndroidApplicationR.screen_synchronisation_authentication_failure, true );
				this.resetPasswordSetting();
			}
			else  {
				this.computeErrorCustomDialog(AndroidApplicationR.screen_synchro_activity_name, AndroidApplicationR.screen_initFailed_errorInSyncFailure, true );
				this.resetPasswordSetting();
			}
		}
		else if (p_oResult.nextScreen != null) {
			try {
				final Intent oIntent = new Intent(this, p_oResult.nextScreen);
				Field oRequestField = p_oResult.nextScreen.getDeclaredField("REQUEST_CODE");
				this.startActivityForResult(oIntent, (Integer) oRequestField.get(null) );
			} catch (SecurityException|NoSuchFieldException|IllegalArgumentException|IllegalAccessException oException) {
				throw new MobileFwkException(oException);
			}
		}
	}

	/**
	 * Supprime le mot de passe de l'application des préférences.
	 */
	private void resetPasswordSetting(){
		SharedPreferences oPreferences = PreferenceManager.getDefaultSharedPreferences(((AndroidApplication)Application.getInstance()).getApplicationContext());
		oPreferences.edit().remove(Application.SETTING_PASSWORD).commit();
		Application.getInstance().removeSetting(Application.SETTING_PASSWORD);
	}

	/**
	 * Create and display a dialog to show an error to the user
	 * @param p_oTitle the title ressource reference in android
	 * @param p_oMessage the message ressource reference in android
	 * @param p_bCloseApp the boolean if the app will close
	 */
	protected void computeErrorCustomDialog(final ApplicationR p_oTitle, final ApplicationR p_oMessage, final boolean p_bCloseApp) {

		final MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder(this);

		oBuilder.setTitle(p_oTitle);
		oBuilder.setIcon(android.R.drawable.ic_popup_sync);
		oBuilder.setMessage(p_oMessage);

		oBuilder.setPositiveButton(AndroidApplicationR.screen_synchronisation_failure_button,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p_oDialog, int p_iId) {

				if (p_bCloseApp) {
					// fermeture de la popup
					p_oDialog.dismiss();
					((MFAndroidApplication) getApplication()).launchStopApplication();
				} else {
					p_oDialog.dismiss();
				}
			}
		});
		MMCustomDialogFragment oAlert = oBuilder.create();
		oAlert.setCancelable(false);
		oAlert.show(getSupportFragmentManager(), MMCustomDialogFragment.CUSTOM_DIALOG_TAG);
	}

	/**
	 * Getter listener delegate
	 * @return the ScreenListenerDelegate instance
	 */
	@Override
	public ListenerDelegate getListenerDelegate() {
		return  BeanLoader.getInstance().getBean(ScreenListenerDelegate.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String,Object> onRetainCustomNonConfigurationInstance() {
		return new HashMap<>();
	}
	
	/**
	 * On back pressed
	 */
	@Override
	public void onBackPressed() {
		if ( MFRootActivity.class.isAssignableFrom(this.getClass())) {
			moveTaskToBack(true);
		}
		else {
			super.onBackPressed();
		}
	}

	/**
	 * Exit current screen
	 */
	public void exit() {
		if ( MFRootActivity.class.isAssignableFrom(this.getClass())) {
			moveTaskToBack(true);
		}
		else {
			super.finish();
		}
	}

	/**
	 * Result Holder class
	 * Store the result of the activity
	 */
	public static class ActivityResultHolder {
		/** the request code */
		private int requestCode ;
		/** the result code */
		private int resultCode ;
		/** the intent */
		private Intent intent ;

		/**
		 * Constructor
		 * @param p_iRequestCode the request code
		 * @param p_iResultCode the result code
		 * @param p_oIntent the intent
		 */
		public ActivityResultHolder(int p_iRequestCode, int p_iResultCode, Intent p_oIntent) {
			super();
			this.requestCode = p_iRequestCode;
			this.resultCode = p_iResultCode;
			this.intent = p_oIntent;
		}

		/**
		 * Getter request code
		 * @return the request code
		 */
		public int getRequestCode() {
			return requestCode;
		}

		/**
		 * Getter result code
		 * @return the result code
		 */
		public int getResultCode() {
			return resultCode;
		}

		/**
		 * Getter intent
		 * @return the intent
		 */
		public Intent getIntent() {
			return intent;
		}
	}

	/**
	 * Update Display
	 */
	public void updateDisplay() {
		// Nothing to do
	}

	/**
	 * This method is called to update the component that launched this dialogFragment
	 * when its holder is restored (after configuration changed for example).
	 * @param p_iComponentId the componant id
	 * @param p_sComponentFragmentTag the componant fragment tag
	 * @return the component corresponding to id and tag or null
	 */
	public ConfigurableVisualComponent findComponent(int p_iComponentId, String p_sComponentFragmentTag) {

		ConfigurableVisualComponent oComponent = null;

		//Retrieving the fragment holder pf the component if it exists.
		Fragment oFragmentHolder = getSupportFragmentManager().findFragmentByTag(p_sComponentFragmentTag);

		if(oFragmentHolder != null)
		{
			//The component is in a fragment. Now, checking if the view for this fragment has
			//been already created, otherwise, there is nothing to update.
			if(oFragmentHolder.getView() != null) {
				oComponent = (ConfigurableVisualComponent) oFragmentHolder.getView().findViewById(p_iComponentId);
			}
			else {
				Log.e("AbstractMMActivity", "Impossible to retrieve component with id:"+ p_iComponentId + " in fragment with tag:"+ p_sComponentFragmentTag);
			}
		}
		else {
			//The component that opened this dialog belongs not to a fragment but an activity.
			//Trying to update the component by its id
			oComponent = (ConfigurableVisualComponent) findViewById(p_iComponentId);
		}

		return oComponent;
	}
	
	/**
	 * This method is called to update the component that launched this dialogFragment
	 * when its holder is restored (after configuration changed for example).
	 * @param p_iComponentId the componant id
	 * @param p_sComponentFragmentTag the componant fragment tag
	 * @param p_iRequestCode the request code
	 * @return the component corresponding to id and tag or null
	 */
	private MMComplexeComponent findComplexeComponentWithRequestCode(int p_iComponentId, String p_sComponentFragmentTag, int p_iRequestCode) {

		MMComplexeComponent oComponent = null;

		//Retrieving the fragment holder pf the component if it exists.
		Fragment oFragmentHolder = getSupportFragmentManager().findFragmentByTag(p_sComponentFragmentTag);

		if(oFragmentHolder != null)
		{
			//The component is in a fragment. Now, checking if the view for this fragment has
			//been already created, otherwise, there is nothing to update.
			if(oFragmentHolder.getView() != null) {
				for (View oView : getAllViewsById((ViewGroup) oFragmentHolder.getView(), p_iComponentId)) {
					if (oView instanceof MMComplexeComponent && ((MMComplexeComponent)oView).getRequestCode() == p_iRequestCode) {
						oComponent = (MMComplexeComponent) oView;
					}
				}
			}
			else {
				Log.e("AbstractMMActivity", "Impossible to retrieve component with id:"+ p_iComponentId + " in fragment with tag:"+ p_sComponentFragmentTag);
			}
		}
		else {
			//The component that opened this dialog belongs not to a fragment but an activity.
			//Trying to update the component by its id
			for (View oView : getAllViewsById((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content), p_iComponentId)) {
				if (oView instanceof MMComplexeComponent && ((MMComplexeComponent)oView).getRequestCode() == p_iRequestCode) {
					oComponent = (MMComplexeComponent) oView;
				}
			}
		}

		return oComponent;
	}
	
	/**
	 * Returns all the views with a given identifier found in a ViewGroup.
	 * There may be more than one should we have a list for example.
	 * @param p_oRoot the root ViewGroup to look into
	 * @param p_iId the view identifier to find
	 * @return the list of views found
	 */
	private ArrayList<View> getAllViewsById(ViewGroup p_oRoot, int p_iId){
	    ArrayList<View> views = new ArrayList<View>();
	    final int childCount = p_oRoot.getChildCount();
	    for (int i = 0; i < childCount; i++) {
	        final View child = p_oRoot.getChildAt(i);
	        if (child instanceof ViewGroup) {
	            views.addAll(getAllViewsById((ViewGroup) child, p_iId));
	        } 

	        final int iId = child.getId();
	        if (iId == p_iId) {
	            views.add(child);
	        }

	    }
	    return views;
	}
	
	/**
	 * Add or update a complexe component. The list of registered complexe components is used to perform onActivityResult appropriatly on each component.
	 * @param p_oComplexeComponent The component to add or update
	 */
	private void registerComplexeComponent(MMComplexeComponent p_oComplexeComponent) {
		boolean bIsComplexeComponentAlreadyRegistered = false;
		int iIndexOfAlreadyRegisteredComplexeComponent = -1;
		HashMap<String, String> oComplexeComponentDescriptor = new HashMap<>();

		for(int iIndex = 0 ; iIndex < this.complexeComponents.size(); iIndex++)  {
			if(this.complexeComponents.get(iIndex).get(MMComplexeComponent.COMPLEXE_COMPONENT_ID_KEY).equals(String.valueOf(((View)p_oComplexeComponent).getId()))) {
				if (this.complexeComponents.get(iIndex).get(MMComplexeComponent.COMPLEXE_COMPONENT_REQUEST_CODE) != null) {
					if (this.complexeComponents.get(iIndex).get(MMComplexeComponent.COMPLEXE_COMPONENT_REQUEST_CODE).equals(String.valueOf(p_oComplexeComponent.getRequestCode()))) {
						bIsComplexeComponentAlreadyRegistered = true;
						iIndexOfAlreadyRegisteredComplexeComponent = iIndex;
						break;
					}
				} else {
					bIsComplexeComponentAlreadyRegistered = true;
					iIndexOfAlreadyRegisteredComplexeComponent = iIndex;
					break;
				}
			}
		}
		
		//On inscrit un descripteur du composant dans la liste des complexeComponents
		if(!bIsComplexeComponentAlreadyRegistered) {
			//AJOUT
			oComplexeComponentDescriptor.put(MMComplexeComponent.COMPLEXE_COMPONENT_ID_KEY, String.valueOf(((View)p_oComplexeComponent).getId()));
			oComplexeComponentDescriptor.put(MMComplexeComponent.COMPLEXE_COMPONENT_FRAGMENT_TAG_KEY, ((ConfigurableVisualComponent)p_oComplexeComponent).getComponentFwkDelegate().getFragmentTag());
			oComplexeComponentDescriptor.put(MMComplexeComponent.COMPLEXE_COMPONENT_REQUEST_CODE, String.valueOf(p_oComplexeComponent.getRequestCode()));
			this.complexeComponents.add(oComplexeComponentDescriptor);
		}
		else {
			//MISE A JOUR
			oComplexeComponentDescriptor = this.complexeComponents.remove(iIndexOfAlreadyRegisteredComplexeComponent);
			oComplexeComponentDescriptor.put(MMComplexeComponent.COMPLEXE_COMPONENT_ID_KEY, String.valueOf(((View)p_oComplexeComponent).getId()));
			oComplexeComponentDescriptor.put(MMComplexeComponent.COMPLEXE_COMPONENT_FRAGMENT_TAG_KEY, ((ConfigurableVisualComponent)p_oComplexeComponent).getComponentFwkDelegate().getFragmentTag());		
			oComplexeComponentDescriptor.put(MMComplexeComponent.COMPLEXE_COMPONENT_REQUEST_CODE, String.valueOf(p_oComplexeComponent.getRequestCode()));
			this.complexeComponents.add(oComplexeComponentDescriptor);
		}
	}
	
	/**
	 * Listener on reload event
	 * @param p_oEvent the reload event with the reload datas
	 */
	@ListenerOnBusinessNotification(ReloadLoaderBusinessEvent.class)
	public void dataLoaderReload(final ReloadLoaderBusinessEvent p_oEvent) {
		
		List<Class<? extends Dataloader<?>>> oDataLoadersList = this.androidApplication.getClassAnalyser().getClassAnalyse(this).getDataLoaderForObject(this.getClass());
		
		if (oDataLoadersList.contains(p_oEvent.getSource().getClass().getInterfaces()[0])) {
			InDisplayParameter oInDisplayParameter = new InDisplayParameter();
			oInDisplayParameter.setDataLoader(p_oEvent.getDataLoaderClass());
			oInDisplayParameter.setReload(p_oEvent.getData());
			this.launchAction(GenericLoadDataForDisplayDetailAction.class, oInDisplayParameter);
			p_oEvent.setConsummed(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueId() {
		return this.getClass().getSimpleName();
	}

	public void setPermissionRequestObject(PermissionRequestObject p_oRequestObject) {
		mRequestObject = p_oRequestObject;
	}
}
