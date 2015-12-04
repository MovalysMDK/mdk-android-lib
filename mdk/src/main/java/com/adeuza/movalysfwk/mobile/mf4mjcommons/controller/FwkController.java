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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.controller;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskOut;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerIdentifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.AlertMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaysynchrodialog.SynchronizationDialogParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.BarCodeResult;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dobeforephonecall.PhoneNumber;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ManageSynchronizationParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Defines the controller of the application. The controller can notify listener :
 * <li>when an action with read access database was launched</li>
 * <li>when an action with write access database was launched</li></p>
 */
public interface FwkController {
		
	/////////////////////
	// ZONE DEAD/LIVE  //
	/////////////////////
	
	/**
	 * Destroys the controller
	 */
	public void destroy();
	
	/**
	 * Action of id p_lActionId call this method to inform controller that it is stopped
	 *
	 * @param p_lActionId the unique id of action
	 * @param p_oActions actions to redirect
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	public void doOnAsyncActionEnd(long p_lActionId, ActionHandler...p_oActions) throws ActionException;
		
	//////////////////
	// ZONE ACTIONS //
	//////////////////
	
	/**
	 * Display Alert Message
	 *
	 * @param p_oAlertMessage the message to display
	 */
	public void doDisplayMessage(AlertMessage p_oAlertMessage);
	
	/**
	 * Create a new context and launch the action send as parameter.
	 * Be careful, this can return NULL !
	 *
	 * @param p_oAction action to launch
	 * @param p_oParameterIn parameters of the action
	 * @return output parameters. If the actino to launch is not define in the BeanLoader, it return a NULL value.
	 */
	public ActionParameter launchAction(Class<? extends Action<?,?,?,?>> p_oAction, ActionParameter p_oParameterIn);
		
	//////////////////////
	// ZONE TRANSVERSE  //
	//////////////////////
	
	
	/**
	 * Stop the application startup process.
	 */
	public void doStopApplicationStartup();
	
	/**
	 * Restart the application startup process.
	 */
	void doRestartApplicationStartup();
	
	/**
	 * Delete the serialized configuration of the application.
	 */
	void doDeleteSerializedConfiguration();
	
	/**
	 * <p>
	 * 	Display the settings.
	 * </p>
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doDisplaySetting( Screen p_oScreen );
	
	/**
	 * <p>
	 * 	Load an activity that displaying an information message and asks the user
	 * 	to configure the Movalys application.
	 * </p>
	 */
	public void doDisplayParameterDialog();
	
	/**
	 * <p>
	 * Reset the settings and displays a dialog that indicates the user
	 * that the application will close.
	 */
	public void doDisplayResetSettingsAndExitDialog();
	
	/**
	 * <p>
	 * 	Reset database.
	 * </p>
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doResetDataBase( Screen p_oScreen );
	
	/**
	 * <p>
	 * 	Reset setting.
	 * </p>
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doResetSetting(Screen p_oScreen);
	
	/**
	 * Send a report on log
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doSendReport(Screen p_oScreen);
	
	/**
	 * <p>
	 * Import database.
	 * </p>
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doImportDatabase( Screen p_oScreen );
	
	/**
	 * Phone Call
	 *
	 * @param p_oPhoneNumber the phone number
	 */
	public void doPhoneCall(PhoneNumber p_oPhoneNumber);

	/**
	 * Write Email
	 *
	 * @param p_oEmail the email
	 */
	public void doWriteEmail(EMail p_oEmail);
	
	/**
	 * Open a map
	 *
	 * @param p_oAddressLocation the addressLocation
	 */
	public void doOpenMap(AddressLocation p_oAddressLocation);

	/**
	 * Bar Code Scanner
	 *
	 * @param p_oBarCodeResult the bar code result
	 * @return an object <em>BarCodeResult</em>
	 */
	public BarCodeResult doAfterBarCodeScan(BarCodeResult p_oBarCodeResult);

	/**
	 * Action that display the Exit dialog
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doDisplayExitApplicationDialog( Screen p_oScreen );
	
	/**
	 * Action that display the Main screen
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doDisplayMain( Screen p_oScreen );
	
	
	//////////////////////////
	// ZONE SYNCHRONISATION //
	//////////////////////////

	/**
	 * Informe si une synchronisation est en cours ou non
	 *
	 * @return a boolean.
	 */
	public boolean isOngoingSynchroAction();
	
	/**
	 * Synchronize all data.
	 */
	public void doClassicSynchronisation();
	
	/**
	 * Synchronize all data.
	 */
	public void doClassicSynchronisationSync();	
	
	/**
	 * Synchronize all data.
	 *
	 * @param p_oScreen
	 * 		The screen used to display the synchro dialog.
	 */
	public void doClassicSynchronisation(Screen p_oScreen);

	/**
	 * Lancement d'une synchronisation depuis l'un des menu de l'application.
	 *
	 * @param p_oNextScreen l'action qui va être lancée à la fin de la synchronisation.
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doClassicSynchronisationFromMenu(Screen p_oScreen, Class<? extends Screen> p_oNextScreen);

	/**
	 * Lancement de la premiere synchronisation.
	 */
	public void doFirstSynchronisation();
	
	/**
	 * Lancement de la première synchronisation au démarrage de l'application.
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 */
	public void doFirstSynchronisation( Screen p_oScreen );
	
	/**
	 * Lancement de la premiere synchronisation en mode synchrone
	 */
	public void doFirstSynchronisationSync();
	
	/**
	 * Register a Synchronization listener.
	 *
	 * @param p_oListener
	 * 				The listener to register
	 */
	public void register(SynchronizationListener p_oListener);
	
	/**
	 * <p>unregister.</p>
	 *
	 * @param p_oListener a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener} object.
	 */
	public void unregister(SynchronizationListener p_oListener);
	
	/**
	 * <p>
	 * 	Display the synchronization popup.
	 * </p>
	 *
	 * @param p_oRedirectSyncParameters
	 * 				Input parameters of the redirect sync. action
	 * @param p_oAction a {@link java.lang.Class} object.
	 */
	public void doDisplaySynchronisationDialog(Class<? extends SynchronizationAction> p_oAction, SynchronizationActionParameterIN p_oRedirectSyncParameters);

	/**
	 * <p>
	 * 	Display the synchronization popup.
	 * </p>
	 *
	 * @param p_oInParameter
	 * 				Input parmeters to the sync. action
	 */
	public void doDisplaySynchronisationDialog(SynchronizationDialogParameterIN p_oInParameter);

	/**
	 * <p>
	 * 	Synchronized method for manage all the concurrent synchro actions.
	 * 	<ul>14 notifications are concerned :
	 * 		<li>SubControllerSynchronization.EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION : when a notification shows the user that it's necessary to sync data from the phone.</li>
	 * 		<li>SubControllerSynchronization.OBJECT_TO_SYNCHRONIZE_SUCCESS_WITH_RESPONSE : notifys when the action has successful and there are a result</li>
	 * 		<li>SubControllerSynchronization.OBJECT_TO_SYNCHRONIZE_SUCCESS_WITHOUT_RESPONSE : notifys when the action has successful but there are no result</li>
	 * 		<li>SubControllerSynchronization.OBJECT_TO_SYNCHRONIZE_CRASH : notifys when the action is crashed</li>
	 *
	 * 		<li>SubControllerSynchronization.EXECUTE_SYNCHRO : when we whant to perform a classical synchronization.</li>
	 * 		<li>SubControllerSynchronization.NEED_FULL_SYNCHRO_ACTION : when it's necessary to perform a complete synchronization.</li>
	 * 		<li>SubControllerSynchronization.SYNCHRO_START : notifys when the action is started</li>
	 * 		<li>SubControllerSynchronization.SYNCHRO_SUCCESS : notifys when the action has successful</li>
	 * 		<li>SubControllerSynchronization.SYNCHRO_CRASH : notifys when the action is crashed</li>
	 * 		<li>SubControllerSynchronization.SYNCHRO_ERROR : notifys when the action has errors</li>
	 * 	</ul>
	 * </p>
	 *
	 * @param p_iSynchroState
	 * 				the identifier of a sync action.
	 */
	public void manageSynchronizationActions(final int p_iSynchroState);

	/**
	 * <p>
	 * 	Synchronized method for manage all the concurrent synchro actions.
	 * 	<ul>14 notifications are concerned :
	 * 		<li>SubControllerSynchronization.EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION : when a notification shows the user that it's necessary to sync data from the phone.</li>
	 * 		<li>SubControllerSynchronization.OBJECT_TO_SYNCHRONIZE_SUCCESS_WITH_RESPONSE : notifys when the action has successful and there are a result</li>
	 * 		<li>SubControllerSynchronization.OBJECT_TO_SYNCHRONIZE_SUCCESS_WITHOUT_RESPONSE : notifys when the action has successful but there are no result</li>
	 * 		<li>SubControllerSynchronization.OBJECT_TO_SYNCHRONIZE_CRASH : notifys when the action is crashed</li>
	 *
	 * 		<li>SubControllerSynchronization.EXECUTE_SYNCHRO : when we whant to perform a classical synchronization.</li>
	 * 		<li>SubControllerSynchronization.NEED_FULL_SYNCHRO_ACTION : when it's necessary to perform a complete synchronization.</li>
	 * 		<li>SubControllerSynchronization.SYNCHRO_START : notifys when the action is started</li>
	 * 		<li>SubControllerSynchronization.SYNCHRO_SUCCESS : notifys when the action has successful</li>
	 * 		<li>SubControllerSynchronization.SYNCHRO_CRASH : notifys when the action is crashed</li>
	 * 		<li>SubControllerSynchronization.SYNCHRO_ERROR : notifys when the action has errors</li>
	 * 	</ul>
	 * </p>
	 *
	 * @param p_iSynchroState
	 * 				the identifier of a sync action.
	 * @param p_oParameters a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ManageSynchronizationParameter} object.
	 */
	public void manageSynchronizationActions(final int p_iSynchroState, final ManageSynchronizationParameter p_oParameters);

	/**
	 * <p>
	 * 	Method for notify that current sync is start.
	 * </p>
	 *
	 * @param p_oNotifier
	 * 				Notifiers of the event
	 * @param p_oObjects
	 * 				Parameters
	 */
	public void notifySynchronizationProcessStart(Notifier p_oNotifier, Object...p_oObjects);
	
	/**
	 * <p>
	 * 	Method for notify that current sync has stopped.
	 * </p>
	 *
	 * @param p_oNotifier
	 * 				Notifiers of the event
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_oInformations a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 */
	public void notifySynchronizationProcessStop(Notifier p_oNotifier, MContext p_oContext, SynchronisationResponseTreatmentInformation p_oInformations);
	
	/**
	 * <p>
	 * 	Method for notify that current sync has changed.
	 * </p>
	 *
	 * @param p_oNotifier
	 * 				Notifiers of the eventpackage com.adeuza.movalys.fwk.mobile.javacommons.business.displaymessage;
	 * @param p_iLevel a int.
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public void notifySynchronizationProgressChanged2(Notifier p_oNotifier, int p_iLevel, String p_sMessage);
	
	/**
	 * <p>notifySynchronizationProgressChanged2.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_iLevel a int.
	 * @param p_oMessage a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 */
	public void notifySynchronizationProgressChanged2(Notifier p_oNotifier, int p_iLevel, ApplicationR p_oMessage);
	
	/**
	 * <p>notifySynchronizationProgressChanged2.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_iLevel a int.
	 * @param p_iMaxStep a int.
	 * @param p_oMessage a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 */
	public void notifySynchronizationProgressChanged2(Notifier p_oNotifier, int p_iLevel, int p_iMaxStep, ApplicationR p_oMessage);
	
	/**
	 * <p>notifySynchronizationProgressChanged2.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_iLevel a int.
	 * @param p_iMaxStep a int.
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public void notifySynchronizationProgressChanged2(Notifier p_oNotifier, int p_iLevel, int p_iMaxStep, String p_sMessage);
	
	/**
	 * <p>notifySynchronizationResetProgress.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 */
	public void notifySynchronizationResetProgress(Notifier p_oNotifier);
	
	
	/**
	 * <p>
	 *  Notify that the global synchronization start.
	 * </p>
	 *
	 * @param p_oContext the application context.
	 */
	public void notifySynchroStart(final MContext p_oContext);
	
	/**
	 * <p>
	 * Notifys the user that the synchronization has successful.
	 * </p>
	 *
	 * @param p_oContext
	 *            The application context
	 */
	public void notifySynchroSuccess(final MContext p_oContext);
	
	/**
	 * <p>
	 * 	Notifys the user that the licence is over for the smartphone.
	 * </p>
	 *
	 * @param p_oContext
	 * 		the application context
	 */
	public void notifyLicenceFailure(final MContext p_oContext);
	/**
	 * <p>
	 * 	Notifys the user that the time of the mobile is incompatible with the server time
	 * </p>
	 *
	 * @param p_oContext
	 * 		the application context
	 */
	public void notifyIncompatibleTimeFailure(final MContext p_oContext);
	
	/**
	 * <p>
	 * Notifys the user that he must synchronize the system because of Movalys Mobile AND Back office updates.
	 * </p>
	 *
	 * @param p_oContext
	 *            the application Context
	 */
	public void notifySynchronizeData(final MContext p_oContext);
	
	/**
	 * <p>
	 * 	Return a boolean value to know if we must perform full sync.
	 * </p>
	 *
	 * @return true when we need to perform full synchronization, false otherwise.
	 */
	public boolean isNeedFullSynchro();
	
	/**
	 * returns true if the database exists and has data in it
	 * @param p_oContext the context to use
	 * @return true if the database exists and has data in it
	 */
	public boolean hasData(MContext p_oContext) throws SynchroException;
	
	/**
	 * <p>
	 * 	Configures the application to perform a fully sync if the parameter is 'true'.
	 * </p>
	 *
	 * @param p_bValue true : synchronization mus be fully perform.
	 */
	public void needFullSynchro(boolean p_bValue);
	
	/**
	 * <p>
	 * 	Notifys the user that he is not authorized to execute MM.
	 * </p>
	 *
	 * @param p_oContext
	 * 		the application context
	 */
	public void notifyAuthenticationFailure(final MContext p_oContext);
	
	/**
	 * <p>
	 * Notifys the user that the synchronization had a connection error.
	 * </p>
	 *
	 * @param p_oContext
	 *            The application context
	 */
	public void notifySynchroConnectionError(final MContext p_oContext);
	
	/**
	 * <p>
	 * Notifys the user that the synchronization crashed.
	 * </p>
	 *
	 * @param p_oContext
	 *            The application context
	 */
	public void notifySynchroCrash(final MContext p_oContext);
	
	/**
	 * <p>releaseBusyStatus.</p>
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_oAction a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action} object.
	 */
	public void releaseBusyStatus(MContext p_oContext ,  Action<?,?,?,?> p_oAction);

	/**
	 * <p>
	 * 	Method that release the synchronization semaphore.
	 * </p>
	 */
	public void releaseSynchroSemaphore();

	/**
	 * Notifie le controller que le dataloader de type p_oClass a changé
	 *
	 * @param p_oContext le context d'exécution
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param p_oDataLoader a {@link com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader} object.
	 */
	//XXX CHANGE V3.2
	public void notifyDataLoaderReload(MContext p_oContext, String p_sKey, MMDataloader<?> p_oDataLoader);

	
	/**
	 * <p>publishBusinessEvent.</p>
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_oEvent a {@link com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent} object.
	 */
	public void publishBusinessEvent(MContext p_oContext, BusinessEvent<?> p_oEvent);
	
	/**
	 * Invokes the queued events on a given listener
	 * @param p_oContext context to use
	 * @param p_oListener listener on which events should be dequeued
	 */
	public void invokeQueuedEvents(MContext p_oContext, ListenerIdentifier p_oListener);
	
	/**
	 * Launch an action in action task (in other thread)
	 *
	 * @param p_oActionClass action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oSource a {@link java.lang.Object} object.
	 */
	public void launchActionByActionTask(Class<? extends Action<?,?,?,?>>  p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource);
	
	/**
	 * Launch an action in action task (in other thread)
	 *
	 * @param p_oParent screen parent who launched the action
	 * @param p_oActionClass action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oSource a {@link java.lang.Object} object.
	 */
	public void launchActionByActionTask(Screen p_oParent, Class<? extends Action<?,?,?,?>>  p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource);
	
	/**
	 * Launch an action in action task (in other thread)
	 *
	 * @param p_oParent screen parent who launched the action
	 * @param p_oAction action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oInterfaceClass a {@link java.lang.Class} object.
	 * @param p_oSource a {@link java.lang.Object} object.
	 */
	public void launchActionByActionTask(Screen p_oParent, Action<?,?,?,?>  p_oAction, 
			Class<? extends Action<?,?,?,?>> p_oInterfaceClass, 
					ActionParameter p_oParameterIn, Object p_oSource);
	
	/**
	 * Lance une action depuis l'écran courant avec une écoute sur tous les écrans
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 */
	public void launchActionOnApplicationScope(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource);

	/**
	 * Launch an action
	 *
	 * @param p_oTask  action to launch
	 * @return The action task out
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	public ActionTaskOut launchActionByActionTask(MMActionTask p_oTask) throws ActionException;
	
	//ne pas faire de typage avec des types paramétrés
	/**
	 * <p>getAction.</p>
	 *
	 * @param p_oAction a {@link java.lang.Class} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action} object.
	 */
	public Action<?,?,?,?> getAction(Class<? extends Action<?,?,?,?>> p_oAction);
	
	/**
	 * {@inheritDoc}
	 *
	 * Launch an action with the management of errors.
	 * Throws an exception if the system tries to perform an action when it is busy
	 */
	public ActionParameter launchAction(@SuppressWarnings("rawtypes") //pas moyen de faire autrement pour gérer simplement les problèmes de cast 
	Action p_oAction, ActionParameter p_oParameterIn);
	
	/**
	 * Launch an action in action task (in other thread)
	 *
	 * @param p_oParent screen parent who launched the action
	 * @param p_oActionClass action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oSource a {@link java.lang.Object} object.
	 */
	public void launchSyncAction(Screen p_oParent, Class<? extends Action<?,?,?,?>>  p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource);

	/**
	 * Launch an action in action task (in other thread)
	 *
	 * @param p_oParent screen parent who launched the action
	 * @param p_oAction action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oInterfaceClass a {@link java.lang.Class} object.
	 * @param p_oSource a {@link java.lang.Object} object.
	 */
	public void launchSyncAction(Screen p_oParent, Action<?,?,?,?>  p_oAction, 
			Class<? extends Action<?,?,?,?>> p_oInterfaceClass, 
					ActionParameter p_oParameterIn, Object p_oSource);
}
