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

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaysynchrodialog.SynchronizationDialogParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ClassicSynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ManageSynchronizationParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.ObservableDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Specialized controller for synchronization.</p>
 *
 *
 * @since Barcelone (10 déc. 2010)
 */
public abstract class AbstractSubControllerSynchronization extends SubController {

	/* de 10 à 19 - SYNCHRONIZATION */
	/** action name */
	private static final String SYNCHRO_ACTION_NAME = "EXECUTE_SYNCHRO_ACTION";
	/** role corresponding to Synchronization action. */
	public static final int SC_EXECUTE_SYNCHRO_ACTION = 10;
	/** role corresponding to Synchronization action when the user need a complete one. */
	public static final int SC_NEED_FULL_SYNCHRO_ACTION = 11;
	/** role corresponding to the start of the synchronization */
	public static final int SC_SYNCHRO_START = 12;
	/** role corresponding to the success of synchro */
	public static final int SC_SYNCHRO_SUCCESS = 13;
	/** role corresponding to the crash of synchro */
	public static final int SC_SYNCHRO_CRASH = 14;
	/** role corresponding to the error of synchro */
	public static final int SC_SYNCHRO_ERROR_KO_AUTHENTIFICATION = 15;
	/** role corresponding to the error of synchro (connection error, radio, wifi ...)*/
	public static final int SC_SYNCHRO_ERROR_KO_CONNECTED = 16;
	/** role corresponding to the error (impossible to join server)*/
	public static final int SC_SYNCHRO_ERROR_KO_JOIN_SERVER = 17;
	/** role corresponding to the error of synchro  when the licence is over */
	public static final int SC_SYNCHRO_ERROR_KO_LICENCE = 18;
	/** role corresponding to the crash of synchro because of incompatible time between server and mobile */
	public static final int SC_SYNCHRO_ERROR_KO_INCOMPATIBLE_SERVER_TIME = 19;
	
	/* de 20 à 29 - OBJECT TO SYNCHRONIZE */
	/** role corresponding to ObjectToSynchronize action. */
	public static final int SC_EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION = 20;

	/* de 100 à 199 - DIVERS */
	/** role that defines that we whant to release the synchronization semaphore. */
	public static final int SC_RELEASE_SYNCHRONIZATION_SEMAPHORE = 100;

	/** last synchronisation OK */
	public static final int STATE_SYNC_LAST_OK = 0;
	/** synchronisation ongoing */
	public static final int STATE_SYNC_ONGOING = 2;
	/** synchronisation failed authentication*/
	public static final int STATE_SYNC_KO_AUTHENTIFICATION = 3;
	/** synchronisation failed connexion*/
	public static final int STATE_SYNC_KO_CONNECTED = 4;
	/** synchronisation failed join server*/
	public static final int STATE_SYNC_KO_JOIN_SERVER = 15;
	/** synchronisation failed crash*/
	public static final int STATE_SYNC_KO_CRASH = 6;
	/** synchronisation licence failed*/
	public static final int STATE_SYNC_KO_LICENCE = 7;
	/** synchronisation  failed because of incompatible server time*/
	public static final int STATE_SYNC_KO_INCOMPATIBLE_SERVER_TIME = 8;
	
	
	/* ATTRIBUTES */
	
	/** delegate of PlanningListener */
	private ObservableDelegate<SynchronizationListener> synchroListenerDelegate = null;
	/** semaphore for synchronization actions */
	protected Boolean isOngoingSynchroAction = false;
	/** boolean value to know if an MM to Bo synchro notification is displayed */
	private boolean isMM2BONotif = false;
	/** boolean value to know if an Bo to MM synchro notification is displayed */
	private boolean isBO2MMNotif = false;
	/** state of synchro */
	private int syncState = STATE_SYNC_LAST_OK;
	/** non change */
	private boolean syncStateChanged = true;
	
	/**
	 * Return True if the syncronization's state has changed
	 *
	 * @return true if the sync has changed, false otherwise.
	 */
	public boolean isSyncStateChanged() {
		return this.syncStateChanged;
	}

	/**
	 * Return True if a synchronization is performed
	 *
	 * @return true if a synchronization is performed
	 */
	protected boolean isOngoingSynchroAction() {
		return this.isOngoingSynchroAction;
	}
	
	/**
	 * Returns the synchro state
	 *
	 * @return the synchro state
	 */
	protected int getSyncState() {
		return syncState;
	}
	
	/**
	 * Returns true whether there is data to send
	 *
	 * @return true whether there is data to send
	 */
	protected boolean isMM2BONotif() {
		return this.isMM2BONotif;
	}
	
	/**
	 * Returns true whether there is data to receive
	 *
	 * @return true whether there is data to reveive
	 */
	protected boolean isBO2MMNotif() {
		return this.isBO2MMNotif;
	}
	
	/**
	 * <p>
	 *  Construct an object <em>SubControllerSynchronization</em>.
	 * </p>
	 *
	 * @param p_oParent the parent controller.
	 */
	protected AbstractSubControllerSynchronization(FwkController p_oParent) {
		super(p_oParent);
		this.synchroListenerDelegate = new ObservableDelegate<SynchronizationListener>();
	}

	////////////////////////////
	// Synchro Action Manager //
	////////////////////////////
	
	/**
	 * <p>
	 * 	Synchronized method for manage all the concurrent synchro actions.
	 * 	This method create an <em>MMContext</em> and call the method {@link #manageSynchronizationActions(MMContext, int)}.
	 * </p>
	 *
	 * @param p_iSynchroState the identifier of a sync action.
	 * @param p_oInParameters parameter to use
	 */
	public void manageSynchronizationActions(final int p_iSynchroState, ActionParameter...p_oInParameters) {
		MContext oContext = null;
		try{
			MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
			oContext = oCf.createContext();
			this.manageSynchronizationActions(oContext, p_iSynchroState, p_oInParameters);
		
		}finally{
			if (oContext != null) {
				oContext.endTransaction();
				oContext.close();
			}
		}
		
	}

	/**
	 * <p>
	 * 	Synchronized method for manage all the concurrent synchro actions.
	 * 	<ul>14 notifications are concerned :
	 * 		<li>SubControllerSynchronization.SC_EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION : when a notification shows the user that it's necessary to sync data from the phone.</li>
	 * 		<li>SubControllerSynchronization.SC_OBJECT_TO_SYNCHRONIZE_SUCCESS_WITH_RESPONSE : notifys when the action has successful and there are a result</li>
	 * 		<li>SubControllerSynchronization.SC_OBJECT_TO_SYNCHRONIZE_SUCCESS_WITHOUT_RESPONSE : notifys when the action has successful but there are no result</li>
	 * 		<li>SubControllerSynchronization.SC_OBJECT_TO_SYNCHRONIZE_CRASH : notifys when the action is crashed</li>
	 *
	 * 		<li>SubControllerSynchronization.SC_EXECUTE_SYNCHRO_ACTION : when we whant to perform a classical synchronization.</li>
	 * 		<li>SubControllerSynchronization.SC_NEED_FULL_SYNCHRO_ACTION : when it's necessary to perform a complete synchronization.</li>
	 * 		<li>SubControllerSynchronization.SC_SYNCHRO_START : notifys when the action is started</li>
	 * 		<li>SubControllerSynchronization.SC_SYNCHRO_SUCCESS : notifys when the action has successful</li>
	 * 		<li>SubControllerSynchronization.SC_SYNCHRO_CRASH : notifys when the action is crashed</li>
	 * 		<li>SubControllerSynchronization.SC_SYNCHRO_ERROR_KO_AUTHENTIFICATION : notifys when the action has errors due to an authentication problem</li>
	 * 		<li>SubControllerSynchronization.SC_SYNCHRO_ERROR_KO_CONNECTED : notifys when the action has errors due to a connection problem</li>
	 * 		<li>SubControllerSynchronization.SC_SYNCHRO_ERROR_KO_JOIN_SERVER : notifys when the action has errors due to a server problem</li>
	 * 		<li>SubControllerSynchronization.SC_SYNCHRO_ERROR_KO_LICENCE : notifys when the action has errors due to a licence problem</li>
	 * 	</ul>
	 * </p>
	 *
	 * @param p_oContext
	 * 				the application context
	 * @param p_iSynchroState
	 * 				the identifier of a sync action.
	 * @param p_oInParameters parameter to use
	 */
	public void manageSynchronizationActions(final MContext p_oContext, final int p_iSynchroState, ActionParameter...p_oInParameters){
				
		//il ne peut y avoir plusieurs process qui exécute ce bloc en même temps
		synchronized (this.isOngoingSynchroAction) {
			this.syncStateChanged = true;
			//release du sémaphore pour autoriser l'appel à d'autres actions de synchronisation.
			if (p_iSynchroState == SC_RELEASE_SYNCHRONIZATION_SEMAPHORE){
				this.isOngoingSynchroAction = false;
			}
			else {
				// Dans cette méthode, il ne doit pas y avoir d'accès à la base de données
				// La vérification de la présence d'enregistrement dans T_MObjectToSynchronize doit être réalisée dans les actions
				if (p_oInParameters != null && p_oInParameters.length > 0 
						&& ManageSynchronizationParameter.class.isAssignableFrom(p_oInParameters[0].getClass()) ) {
					this.isMM2BONotif = ((ManageSynchronizationParameter) p_oInParameters[0]).isMmToBo();
				}

				switch (p_iSynchroState) {
					//////////////////////////OBJECT TO SYNCHRONIZE//////////////////////////
					case SC_EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION:
						//check si on a des données à synchroniser avec le Back office, dans la table ObjectToSynchronize
						this.syncStateChanged = false;
						this.getController().notifySynchronizeData(p_oContext);
						this.syncStateChanged = false;
						break;
					//////////////////////////SYNCHRONIZATION//////////////////////////
					case SC_EXECUTE_SYNCHRO_ACTION:
						if (!this.isOngoingSynchroAction){
							this.syncState = STATE_SYNC_ONGOING;

							SynchronizationDialogParameterIN oSynchroDialogParameter = (SynchronizationDialogParameterIN) p_oInParameters[0];

							if ( oSynchroDialogParameter.async ) {
								this.getController().launchActionByActionTask(oSynchroDialogParameter.screen, 
									oSynchroDialogParameter.synchronisationAction,
									oSynchroDialogParameter.synchronisationParameters, oSynchroDialogParameter.screen);
							}
							else {
								this.getController().launchSyncAction(oSynchroDialogParameter.screen, oSynchroDialogParameter.synchronisationAction,
										oSynchroDialogParameter.synchronisationParameters, oSynchroDialogParameter.screen);
							}
						}else{
							//lancemnet d'un message d'info
							Application.getInstance().getLog().info(InformationDefinition.FWK_MOBILE_I_0200, StringUtils.concat(
									InformationDefinition.FWK_MOBILE_I_0200_1_LABEL,
									SYNCHRO_ACTION_NAME,
									InformationDefinition.FWK_MOBILE_I_0200_2_LABEL));
						}
						break;
					case SC_NEED_FULL_SYNCHRO_ACTION:
						this.getController().needFullSynchro(true);
						break;
					case SC_SYNCHRO_START:
						this.isOngoingSynchroAction = true;
						this.syncState = STATE_SYNC_ONGOING;
						this.getController().notifySynchroStart(p_oContext);
						break;
					case SC_SYNCHRO_SUCCESS:
						this.isBO2MMNotif = false;
						this.isOngoingSynchroAction = false;
						this.getController().needFullSynchro(false);
						this.syncState = STATE_SYNC_LAST_OK;
						this.getController().notifySynchroSuccess(p_oContext);
						break;
					case SC_SYNCHRO_ERROR_KO_AUTHENTIFICATION:
						this.isOngoingSynchroAction = false;
						this.syncState = STATE_SYNC_KO_AUTHENTIFICATION;
						this.getController().notifyAuthenticationFailure(p_oContext);
						break;
					case SC_SYNCHRO_ERROR_KO_LICENCE:
						this.isOngoingSynchroAction = false;
						this.syncState = STATE_SYNC_KO_LICENCE;
						this.getController().notifyLicenceFailure(p_oContext);
						break;
					case SC_SYNCHRO_ERROR_KO_CONNECTED:
						this.isOngoingSynchroAction = false;
						this.syncState = STATE_SYNC_KO_CONNECTED;
						this.getController().notifySynchroConnectionError(p_oContext);
						break;
					case SC_SYNCHRO_ERROR_KO_JOIN_SERVER:
						this.isOngoingSynchroAction = false;
						this.syncState = STATE_SYNC_KO_JOIN_SERVER;
						this.getController().notifySynchroConnectionError(p_oContext);
						break;
					case SC_SYNCHRO_CRASH:
						this.isOngoingSynchroAction = false;
						this.syncState = STATE_SYNC_KO_CRASH;
						this.getController().notifySynchroCrash(p_oContext);
						break;
					case SC_SYNCHRO_ERROR_KO_INCOMPATIBLE_SERVER_TIME:
						this.isOngoingSynchroAction = false;
						this.syncState = STATE_SYNC_KO_INCOMPATIBLE_SERVER_TIME;
						this.getController().notifyIncompatibleTimeFailure(p_oContext);
						break;
						
					//////////////////////////DEFAULT//////////////////////////
					default:
						break;
				}
			}
		}
	}
	
	
	
	
	/////////////////////////
	// Progress of synchro //
	/////////////////////////
	
	/**
	 * Register a listener
	 *
	 * @param p_oListener the listener to register
	 */
	protected void register(SynchronizationListener p_oListener) {
		this.synchroListenerDelegate.register(p_oListener);
	}
	
	/**
	 * Unregister a listener
	 *
	 * @param p_oListener the listener to unregister
	 */
	protected void unregister(SynchronizationListener p_oListener) {
		this.synchroListenerDelegate.unregister(p_oListener);
	}
	
	/**
	 * <p>
	 * 	Notify listeners that the synchronization start.
	 * 	To declare this notification you must pass 3 parameters.
	 * 	<ul>
	 * 		<li>int : the level of the progress bar widget</li>
	 * 		<li>int : the current step to initialize the progress bar</li>
	 * 		<li>int : the total number of steps that will be represented in the progress bar.</li>
	 * 	</ul>
	 * 	For exemple : this.notifySynchronizationProcessStart(Notifier,0,0,11);<br>
	 * 	This code will initialize the first progress bar at the step 0. This progress bar has 11 steps.
	 * </p>
	 *
	 * @param p_oNotifier
	 * 				the notifier
	 * @param p_oObjects
	 * 				table of parameters. exemple [0;0;11].
	 */
	protected void notifySynchronizationProcessStart(Notifier p_oNotifier, Object...p_oObjects) {
		this.synchroListenerDelegate.doOnNotification(SynchronizationListener.M_DO_ON_START_PROCESS, p_oNotifier,p_oObjects);
	}
	
	/**
	 * <p>
	 * 	Notify listeners that the synchronization has changed.
	 * 	To declare this notification you must pass 1 parameter.
	 * 	<ul>
	 * 		<li>int : the level of the progress bar widget to increase</li>
	 * 	</ul>
	 * 	For exemple : notifySynchronizationProgressChanged(Notifier,0);<br>
	 * 	This code will increse the first progress bar for one step.
	 * </p>
	 *
	 * @param p_oNotifier
	 * 				the notifier
	 * @param p_oObjects
	 * 				table of parameters. exemple [0].
	 */
	protected void notifySynchronizationProgressChanged(Notifier p_oNotifier, Object...p_oObjects) {
		if ( p_oObjects.length == 2 ) {
			this.synchroListenerDelegate.doOnNotification(SynchronizationListener.M_DO_ON_SYNCHRO_CHANGED, p_oNotifier,p_oObjects);
		} else {
			this.synchroListenerDelegate.doOnNotification(SynchronizationListener.M_DO_ON_SYNCHRO_CHANGED2, p_oNotifier,p_oObjects);	
		}
	}
	
	/**
	 * <p>notifySynchronizationResetProgress.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 */
	protected void notifySynchronizationResetProgress(Notifier p_oNotifier) {
		this.synchroListenerDelegate.doOnNotification(SynchronizationListener.M_DO_ON_SYNCHRO_RESETPROGRESS, p_oNotifier);
	}
	
	/**
	 * <p>
	 * 	Notify listeners that the synchronization stop
	 * </p>
	 *
	 * @param p_oNotifier the notifier
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @param p_oInformation a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 */
	protected void notifySynchronizationProcessStop(Notifier p_oNotifier, MContext p_oContext, SynchronisationResponseTreatmentInformation p_oInformation) {
		this.synchroListenerDelegate.doOnNotification(SynchronizationListener.M_DO_ON_STOP_PROCESS, p_oNotifier, p_oContext, p_oInformation);
	}

	
	//////////////////////
	// Synchro Activity //
	//////////////////////
	
//	/**
//	 * Method that launch a sync.
//	 */
//	protected void doClassicSynchronisation() {
//		this.getController().doDisplaySynchronisationDialog(
//				BeanLoader.BEANTYPE_PREFIX + ExtBeanType.SynchronizationCall.getName(),
//				new SynchronizationActionParameterINImpl());
//	}

	/**
	 * Do a classic synchronisation
	 * @param p_oScreen the screen that launch synchronisation
	 */
	protected void doClassicSynchronisation(Screen p_oScreen) {
		doClassicSynchronisation(p_oScreen, true );
	}
	
	/**
	 * Do a classic synchronisation
	 */
	protected void doClassicSynchronisationSync() {
		doClassicSynchronisation(null, false );
	}
	
	/**
	 * Do classic synchronisation in sync mode
	 * @param p_oScreen sreen tha launch sync
	 * @param p_bAsync is asynchrone
	 */
	protected void doClassicSynchronisation( Screen p_oScreen, boolean p_bAsync ) {
		SynchronizationDialogParameterIN oSynchroDialogParameter = new SynchronizationDialogParameterIN();
		oSynchroDialogParameter.synchronisationAction = ClassicSynchronizationAction.class;
		oSynchroDialogParameter.synchronisationParameters	= new SynchronizationActionParameterIN();
		oSynchroDialogParameter.screen = p_oScreen;
		oSynchroDialogParameter.async = p_bAsync;

		if (Application.getInstance().isSyncTransparentEnabled()) {
			oSynchroDialogParameter.synchronisationParameters.setActionAttachedActivity(false);
			oSynchroDialogParameter.synchronisationParameters.disableProgressDialog();
		}

		this.doDisplaySynchronisationDialog(oSynchroDialogParameter);
	}

	/**
	 * Method that launch a sync.
	 *
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 * @param p_oNextScreen a {@link java.lang.Class} object.
	 */
	protected abstract void doClassicSynchronisationFromMenu( Screen p_oScreen, Class<? extends Screen> p_oNextScreen);

	/**
	 * Do first synchronisation (on the main thread)
	 */
	public void doFirstSynchronisationSync() {
		doFirstSynchronisation(null, false );
	}
	
	/**
	 * Do first synchronisation
	 * @param p_oScreen screen that launch synchronisation.
	 */
	protected abstract void doFirstSynchronisation( Screen p_oScreen );
	
	/**
	 * Do first synchronisation
	 * @param p_oScreen the screen that launch synchronisation
	 * @param p_bAsync is async (thread)
	 */
	protected abstract void doFirstSynchronisation( Screen p_oScreen, boolean p_bAsync );
	
	/**
	 * Method that launch the action to display the synchronisation dialog.
	 *
	 * @param p_oInParameter the action parameters
	 */
	protected void doDisplaySynchronisationDialog(SynchronizationDialogParameterIN p_oInParameter) {
		this.manageSynchronizationActions(SC_EXECUTE_SYNCHRO_ACTION, p_oInParameter);
	}
	
	/**
	 * <p>
	 * 	Method that release the synchronization semaphore.
	 * </p>
	 */
	public void releaseSynchroSemaphore(){
		this.manageSynchronizationActions(SC_RELEASE_SYNCHRONIZATION_SEMAPHORE);
	}
	
	///////////////////////////
	// object to synchronize //
	///////////////////////////

	/**
	 * <p>Set the object <em>isMM2BONotif</em></p>.
	 *
	 * @param p_oIsMM2BONotif Objet isMM2BONotif
	 */
	public void setMM2BONotif(boolean p_oIsMM2BONotif) {
		this.isMM2BONotif = p_oIsMM2BONotif;
	}

	/**
	 * <p>Set the object <em>isBO2MMNotif</em></p>.
	 *
	 * @param p_oIsBO2MMNotif Objet isBO2MMNotif
	 */
	public void setBO2MMNotif(boolean p_oIsBO2MMNotif) {
		this.isBO2MMNotif = p_oIsBO2MMNotif;
	}
}
