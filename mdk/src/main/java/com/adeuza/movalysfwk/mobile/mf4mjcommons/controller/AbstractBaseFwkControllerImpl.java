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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.FuncDeny;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.FuncGrant;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.Func2;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.PermissionUtil;
import com.adeuza.movalysfwk.mobile.mf4android.utils.permission.PermissionUtil.PermissionRequestObject;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener.ListenerOnDataLoaderReloadEvent;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionRule;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionRuleContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskIn;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskOut;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoaderError;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ExtBeanType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.log.Logger;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>
 * Defines the controller of the application. The controller can notify listener :
 * <li>when an action with read access database was launched</li>
 * <li>when an action with write access database was launched</li>
 * </p>
 *
 *
 */
public abstract class AbstractBaseFwkControllerImpl implements FwkController {

	/** Log tag. */
	private static final String TAG = "ACTION";

	/** controller request code permission. */
	private static final int CONTROLLER_PERMISSION_CODE = 98;

	/** used to compute unique id */
	protected Long actionId = null;
	
	/** handler for asynchronous action */
	protected Map<Long, AsyncActionHandler> asyncActionHandlers;

	/** indicates that controller is busy : an action non concurent is running */
	protected Map<String, Integer> mapBusyQueue = new HashMap<>();
	
	/** boolean value to know if we must perform fully sync */
	private boolean needFullSynchro = false;

	/** queue of action tasks */
	protected Map<String, Queue<MMActionTask<?,?,?,?>>> mapActionTaskQueue = new HashMap<>();
	
	/** events to be sent to screens when they become active again */
	Map<String, Queue<Object>> pendingEvents = new HashMap<>();
	
	/////////////////////
	// ZONE CONTROLLER //
	/////////////////////
	/** sub controller transverse */
	protected SubControllerTransverse businessTransverse;
	/** sub controller synchronization */
	protected AbstractSubControllerSynchronization businessSynchronization;
	

	/**
	 * Constructs a new Controller
	 */
	public AbstractBaseFwkControllerImpl() {
		this.actionId = new Long(0);
		this.asyncActionHandlers = new HashMap<Long, AsyncActionHandler>();
		this.businessTransverse = createSubControllerTransverse();
		this.businessSynchronization = createSubControllerSynchronization();
	}
	
	/**
	 * <p>createSubControllerSynchronization.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization} object.
	 */
	protected abstract AbstractSubControllerSynchronization createSubControllerSynchronization();

	/**
	 * <p>createSubControllerTransverse.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerTransverse} object.
	 */
	protected abstract SubControllerTransverse createSubControllerTransverse();
	
	// ///////////////////
	// ZONE DEAD/LIVE //
	// ///////////////////

	/** {@inheritDoc} */
	@Override
	public void destroy() {
		// Nothing to do
	}
	
	/**
	 * Launch an action with the management of errors. Throwns when user launch an action before the end of a previous action
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oNotifier
	 *            object launching the action
	 * @param p_oAction
	 *            the action to launch
	 * @param p_oParameterIn
	 *            the parameter to user for launch action
	 * @param p_oLaunchParameter
	 * @return the result of business processing
	 */
	protected ActionParameter launchAction(MContext p_oContext, Notifier p_oNotifier, @SuppressWarnings("rawtypes")// pas moyen de faire autrement pour
			// gérer simplement les problèmes
			// de cast
			Action p_oAction, ActionParameter p_oParameterIn) throws ActionException {
		
		ActionRuleContext oActionRuleContext = null;
		ActionRule oActionRule = null;
		try {
			// Chargement de l'éventuelle règle de gestion centralisée sur les actions
			oActionRule = BeanLoader.getInstance().getBean(ActionRule.class);
			
			// Initialisation du contexte de la règle
			oActionRuleContext = new ActionRuleContext();
			oActionRuleContext.setParameters(new HashMap<String, Object>());
			
		} catch(BeanLoaderError e) {
			// Implémentation de la règle non trouvée
			// NothingToDo
			Application.getInstance().getLog().info(Application.LOG_TAG, "launchAction: The action rule is not implemented.");
		}
		return this.launchAction(p_oContext, p_oNotifier, p_oAction, p_oParameterIn, oActionRule, oActionRuleContext);
	}
	

	/**
	 * Launch an action with the management of errors. Throwns when user launch an action before the end of a previous action
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oNotifier
	 *            object launching the action
	 * @param p_oAction
	 *            the action to launch
	 * @param p_oParameterIn
	 *            the parameter to user for launch action
	 * @param p_oActionRule
	 *            the action rule            
	 * @param p_oActionRuleContext
	 *            the actions' rule context  
	 * @param p_bLaunchPostExecute indicates if the postExecute method is launch                     
	 * @return the result of business processing
	 */
	@SuppressWarnings("unchecked")
	protected abstract ActionParameter launchAction(MContext p_oContext, Notifier p_oNotifier, @SuppressWarnings("rawtypes") 
			Action p_oAction, ActionParameter p_oParameterIn, ActionRule p_oActionRule, ActionRuleContext p_oActionRuleContext) throws ActionException;

	/**
	 * {@inheritDoc}
	 *
	 * @param p_lActionId a long.
	 * @param p_oActions a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionHandler} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	@Override
	public abstract void doOnAsyncActionEnd(long p_lActionId, ActionHandler... p_oActions) throws ActionException;
	
	/**
	 * Launch an action in action task (in other thread)
	 *
	 * @param p_oActionClass action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oSource a {@link java.lang.Object} object.
	 */
	@Override
	public void launchActionByActionTask(Class<? extends Action<?,?,?,?>>  p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource) {
		
		AbstractTaskableAction<?, ?, ?, ?> oAction = (AbstractTaskableAction<?, ?, ?, ?>) BeanLoader.getInstance().getBean(p_oActionClass);
		
		this.launchActionByActionTask(null, oAction, p_oActionClass, p_oParameterIn, p_oSource);
	}

	/**
	 * Launch an action in action task (in other thread)
	 *
	 * @param p_oParent screen parent who launched the action
	 * @param p_oActionClass action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oSource a {@link java.lang.Object} object.
	 */
	@Override
	public void launchActionByActionTask(Screen p_oParent, Class<? extends Action<?,?,?,?>>  p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource) {
		
		AbstractTaskableAction<?, ?, ?, ?> oAction = (AbstractTaskableAction<?, ?, ?, ?>) BeanLoader.getInstance().getBean(p_oActionClass);
		
		this.launchActionByActionTask(p_oParent, oAction, p_oActionClass, p_oParameterIn, p_oSource);
	}

	/**
	 * Launch an action in action task (in other thread).
	 *
	 * @param p_oParent screen parent who launched the action
	 * @param p_oAction action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oInterfaceClass a {@link java.lang.Class} object.
	 * @param p_oSource a {@link java.lang.Object} object
	 */
	@Override
	public void launchActionByActionTask(Screen p_oParent, Action<?,?,?,?>  p_oAction, 
			Class<? extends Action<?,?,?,?>> p_oInterfaceClass, 
					ActionParameter p_oParameterIn, Object p_oSource) {
		this.launchAction(p_oParent, p_oAction,  p_oInterfaceClass, p_oParameterIn, p_oSource, ExtBeanType.ASyncAction);
	}

	/**
	 * Lance une action depuis l'écran courant avec une écoute sur tous les écrans
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 */
	public void launchActionOnApplicationScope(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource) {
		if (p_oParameterIn != null)
			p_oParameterIn.setActionAttachedActivity(false);

		AbstractTaskableAction<?, ?, ?, ?> oAction = (AbstractTaskableAction<?, ?, ?, ?>) BeanLoader.getInstance().getBean(p_oActionClass);
		this.launchActionByActionTask(null, oAction, p_oActionClass, p_oParameterIn, p_oSource);
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launchSyncAction(Screen p_oParent, Class<? extends Action<?,?,?,?>>  p_oActionClass, ActionParameter p_oParameterIn, Object p_oSource) {
		
		AbstractTaskableAction<?, ?, ?, ?> oAction = (AbstractTaskableAction<?, ?, ?, ?>) BeanLoader.getInstance().getBean(p_oActionClass);
		
		this.launchSyncAction(p_oParent, oAction, p_oActionClass, p_oParameterIn, p_oSource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launchSyncAction(Screen p_oParent, Action<?,?,?,?>  p_oAction, 
			Class<? extends Action<?,?,?,?>> p_oInterfaceClass, 
					ActionParameter p_oParameterIn, Object p_oSource) {

		this.launchAction(p_oParent, p_oAction, p_oInterfaceClass, p_oParameterIn, p_oSource, ExtBeanType.SyncAction);
	}

	/**
	 * Launch an action in action task (in other thread)
	 * <p>
	 * This method may return null if an action is currently running and a other 
	 * action need to be launch. The action to be launch is put in queue and a null
	 * value is return
	 * </p>
	 *
	 * @param p_oParent screen parent who launched the action
	 * @param p_oAction action to launch
	 * @param p_oParameterIn in parameter
	 * @param p_oInterfaceClass a {@link java.lang.Class} object.
	 * @param p_oSource a {@link java.lang.Object} object.
	 * @param p_oServiceType a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanType} object.
	 */
	protected void launchAction(final Screen p_oParent, final Action<?,?,?,?>  p_oAction, 
			final Class<? extends Action<?,?,?,?>> p_oInterfaceClass, 
					final ActionParameter p_oParameterIn, final Object p_oSource, final BeanType p_oServiceType) {

		final AbstractTaskableAction<?, ?, ?, ?> oAction = (AbstractTaskableAction<?, ?, ?, ?>) p_oAction;

		ClassAnalyse oAnalyse = null;
		ListenerIdentifier oListenerIdentifier = null;
		String sUniqueId = null;
		Context oContext = null;
		if ( p_oParent != null ) {
			oAnalyse = Application.getInstance().getClassAnalyser().getClassAnalyse(p_oParent);
			oListenerIdentifier = (ListenerIdentifier) p_oParent;
			sUniqueId = oListenerIdentifier.getUniqueId();
		} else  {
			oContext = AndroidApplication.getInstance().getApplicationContext();
		}

		final MMActionTask oTask = BeanLoader.getInstance().getBean(p_oServiceType, MMActionTask.class);
		oAction.setCurrentHandler(oTask);
		oTask.init(sUniqueId, oAction, p_oInterfaceClass, p_oParameterIn, oAnalyse);

		ActionTaskIn oAti = new ActionTaskIn();
		oTask.setParameterIn(oAti);
		oAti.setIn(p_oParameterIn);
		if (p_oSource != null && ListenerIdentifier.class.isAssignableFrom(p_oSource.getClass())) {
			oAti.setSource(((ListenerIdentifier)p_oSource).getUniqueId());
		}
		else {
			oAti.setSource(p_oSource);
		}

		// Register the action with the parent
		Application.getInstance().registerRunningAction((ListenerIdentifier) p_oParent, oTask);
		
		PermissionRequestObject oRequestObject = PermissionUtil.with(( p_oParent == null ?(Context) oContext :(AppCompatActivity) p_oParent))
			.request(oAction.getRequieredPermissions())
			.onAllGranted(new FuncGrant() {
	
				@Override
				protected void call() {
					synchronized (mapBusyQueue) {
						Logger oLog = Application.getInstance().getLog();
						if (canBeginAction(oTask.getAction())) {
							oLog.debug(TAG, p_oAction.getClass().getName() + " QUEUE number " +
									p_oAction.getConcurrentAction() + " FwkController Lancement");
							if (p_oAction.getConcurrentAction() != Action.NO_QUEUE) {
								oLog.debug(TAG, p_oAction.getClass().getName() + " QUEUE number " +
										p_oAction.getConcurrentAction() + " LOCK");
								modifyBusyIntoQueue(p_oAction, true);
							}
							oTask.execAction();
						} else {
							oLog.debug(TAG, p_oAction.getClass().getName() + " QUEUE number " +
									p_oAction.getConcurrentAction() + " mise en queue - NOLOCK");
							Queue<MMActionTask<?, ?, ?, ?>> oActionTaskQueue = searchActionTaskIntoQueue(oTask.getAction());
							oActionTaskQueue.offer(oTask);
	
							// We create the dialog because the action in progress may have been launch from a different screen, the
							// dialog has been closed, so we create the dialog for this new task.
							oTask.createProgressDialog();
						}
					}
				}
			})
			.onAnyDenied(new FuncDeny() {
				
				@Override
				public void call(String[] deniedPermissions) {
					Log.d("PERM_ERROR", "permission denied by user!!!");
					try {
						oTask.execDeniedPermission(deniedPermissions);
					} catch (ActionException e) {
						throw new RuntimeException("ActionPermission error", e);
					}
				}
			})
			.ask(CONTROLLER_PERMISSION_CODE);
		
		if ( p_oParent != null )
			((AbstractMMActivity)p_oParent).setPermissionRequestObject(oRequestObject);
	}
	
	/**
	 * Search ActionTask into Queue ConcurrentAction
	 *
	 * @param p_oTask  action to launch
	 * @return The action task out
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	protected Queue<MMActionTask<?,?,?,?>> searchActionTaskIntoQueue(Action<?,?,?,?> p_oAction) {
		Queue<MMActionTask<?,?,?,?>> oActionTaskQueue = this.mapActionTaskQueue.get(String.valueOf(p_oAction.getConcurrentAction()));
		if (oActionTaskQueue == null) {
			oActionTaskQueue = new LinkedList<MMActionTask<?,?,?,?>>();
			this.mapActionTaskQueue.put(String.valueOf(p_oAction.getConcurrentAction()), oActionTaskQueue);
		}
		
		return oActionTaskQueue;
	}
	
	
	/**
	 * Launch an action
	 *
	 * @param p_oTask  action to launch
	 * @return The action task out
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	@Override
	public abstract ActionTaskOut launchActionByActionTask(MMActionTask p_oTask) throws ActionException;
	
	/**
	 * {@inheritDoc}
	 *
	 * Launch an action with the management of errors.
	 * Throws an exception if the system tries to perform an action when it is busy
	 */
	@Override
	public ActionParameter launchAction(@SuppressWarnings("rawtypes") //pas moyen de faire autrement pour gérer simplement les problèmes de cast 
			Action p_oAction, ActionParameter p_oParameterIn) {

		return this.launchActionWithBusyVerification(null, p_oAction, p_oParameterIn);
	}

	/**
	 * Launch an action with the management of errors.
	 * Throws an exception if the system tries to perform an action when it is busy
	 *
	 * @param p_oAction the action to launch
	 * @param p_oParameterIn the parameter to user for launch action
	 * @return the result of business processing
	 * 	when user launch an action before the end of a previous action
	 */
	public ActionHandler launchActionFromBackgroundThread(@SuppressWarnings("rawtypes") //pas moyen de faire autrement pour gérer simplement les problèmes de cast 
			Action p_oAction, ActionParameter p_oParameterIn) {
		
		return this.launchActionWithBusyVerificationFromBackgroundThread(null, p_oAction, p_oParameterIn);
	}

	/** {@inheritDoc} */
	@Override
	public ActionParameter launchAction(Class<? extends Action<?,?,?,?>> p_oAction, ActionParameter p_oParameterIn) {
		if (BeanLoader.getInstance().hasDefinition(p_oAction.getName())){
			return this.launchAction(BeanLoader.getInstance().getBean(p_oAction), p_oParameterIn);
		}else{
			return null;
		}
	}
	

	/**
	 * Launch an action with the management of errors. Throwns when user launch an action before the end of a previous action
	 *
	 * @param p_oNotifier
	 *            object launching the action
	 * @param p_oAction
	 *            the action to launch
	 * @param p_oParameterIn
	 *            the parameter to user for launch action
	 * @return the result of business processing
	 */
	protected abstract ActionParameter launchActionWithBusyVerification(final Notifier p_oNotifier, @SuppressWarnings("rawtypes")// pas moyen
			// de faire  autrement pour gérer  simplement les problèmes de cast
			final Action p_oAction, final ActionParameter p_oParameterIn);

	/**
	 * Launch an action with the management of errors. Throwns when user launch an action before the end of a previous action
	 * Warning : notification is not launch .... do this after call this method
	 *
	 * @param p_oNotifier
	 *            object launching the action
	 * @param p_oAction
	 *            the action to launch
	 * @param p_oParameterIn
	 *            the parameter to user for launch action
	 * @return the result of business processing
	 */
	protected abstract ActionHandler launchActionWithBusyVerificationFromBackgroundThread(final Notifier p_oNotifier, @SuppressWarnings("rawtypes")// pas moyen
			// de faire  autrement pour gérer  simplement les problèmes de cast
			final Action p_oAction, final ActionParameter p_oParameterIn);

	/**
	 * This method compute {@link Action#isConcurrentAction()} and number of active actions this method used synchronized operations on privates
	 * attributes busy an running
	 * 
	 * @param p_oAction
	 *            The action to run
	 * @return true if the action can be run, false if (another action already runs and the parametered action must be single to run)
	 */
	protected boolean canBeginAction(Action<?,?,?,?> p_oAction) {
		if (p_oAction.getConcurrentAction() != Action.NO_QUEUE) {
            boolean isBusy = this.isBusyIntoQueue(p_oAction);
            Application.getInstance().getLog().debug(TAG, p_oAction.getClass().getName() + " QUEUE number " +
                    p_oAction.getConcurrentAction() + " isBusy: " + isBusy);
			return !isBusy;
		} else {
			Application.getInstance().getLog().debug(Application.LOG_TAG, p_oAction.getClass().getName() + " NO QUEUE");
			return true;
		}
	}
	
	/**
	 * Is Busy into Queue ConcurrentAction
	 *
	 * @param p_oTask  action to launch
	 * @return The action task out
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	protected boolean isBusyIntoQueue(Action<?,?,?,?> p_oAction) {
		Integer oBusyQueue = this.mapBusyQueue.get(String.valueOf(p_oAction.getConcurrentAction()));
		if (oBusyQueue == null) {
            oBusyQueue = 0;
			this.mapBusyQueue.put(String.valueOf(p_oAction.getConcurrentAction()), oBusyQueue);
		}
		return (oBusyQueue != 0);
	}
	
	/**
	 * Modify Busy into Queue ConcurrentAction
	 *
	 * @param p_oAction  action to launch
	 */
	protected void modifyBusyIntoQueue(Action<?,?,?,?> p_oAction, boolean p_bBusy) {
		Application.getInstance().getLog().debug(TAG, p_oAction.getClass().getName() + " QUEUE number " +
				p_oAction.getConcurrentAction() + " LOC ? " + p_bBusy);
		Integer oBusyQueue = this.mapBusyQueue.get(String.valueOf(p_oAction.getConcurrentAction()));
		if (oBusyQueue != null) {
            this.mapBusyQueue.remove(String.valueOf(p_oAction.getConcurrentAction()));
		}
		this.mapBusyQueue.put(String.valueOf(p_oAction.getConcurrentAction()), (p_bBusy ?1 :0));
	}

	/**
	 * {@inheritDoc}
	 *
	 * This method releases the markers set by {@link FwkController#canBeginAction} {@link Action#isConcurrentAction()} and number of active
	 * actions are used this method used synchronized operations on privates attributes busy an running
	 */
	@Override
	public abstract void releaseBusyStatus(MContext p_oContext, Action<?,?,?,?> p_oAction);
	
	//ne pas faire de typage avec des types paramétrés
	/**
	 * <p>getAction.</p>
	 *
	 * @param p_oAction a {@link java.lang.Class} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action} object.
	 */
	@Override
	public Action<?,?,?,?> getAction(Class<? extends Action<?,?,?,?>> p_oAction) {
		return (Action<?,?,?,?>)BeanLoader.getInstance().getBean(p_oAction);
	}

	/**
	 * 
	 * <p>
	 * Handler for asynchronous action
	 * </p>
	 * 
	 * 
	 */
	protected class AsyncActionHandler {

		/** redirect actions associated */
		private List<ActionHandler> actions = null;
		/** in parameter's current action */
		private ActionParameter inParameter = null;
		/** out parameter's current action */
		private ActionParameter outParameter = null;
		/** the context to use */
		private MContext context = null;
		/** the notifier to use */
		private Notifier notifier = null;

		/**
		 * Constructs a new handler
		 * 
		 * @param p_lstActions
		 *            the redirect actions to call
		 * @param p_oContext
		 *            context to use
		 * @param p_oNotifier
		 *            notifier to use
		 * @param p_oParameterIn
		 *            the in parameter's current action
		 * @param p_oParameterOut
		 *            the out parameter's current action
		 */
		protected AsyncActionHandler(List<ActionHandler> p_lstActions, MContext p_oContext, Notifier p_oNotifier, ActionParameter p_oParameterIn,
				ActionParameter p_oParameterOut) {
			this.actions = p_lstActions;
			this.inParameter = p_oParameterIn;
			this.outParameter = p_oParameterOut;
			this.context = p_oContext;
			this.notifier = p_oNotifier;
		}

		/**
		 * Returns the context to use
		 * 
		 * @return a context
		 */
		public MContext getContext() {
			return this.context;
		}

		/**
		 * Returns the notifier to use
		 * 
		 * @return a notifier
		 */
		public Notifier getNotifier() {
			return this.notifier;
		}

		/**
		 * Returns actions to call
		 * 
		 * @return the action to call
		 */
		public List<ActionHandler> getActions() {
			return this.actions;
		}

		/**
		 * Returns the in parameter
		 * 
		 * @return in parameter
		 */
		public ActionParameter getInParameter() {
			return this.inParameter;
		}

		/**
		 * Returns the out parameter
		 * 
		 * @return the out parameter
		 */
		public ActionParameter getOutParameter() {
			return this.outParameter;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void doDisplayMessage(AlertMessage p_oAlertMessage) {
		this.businessTransverse.doDisplayMessage(p_oAlertMessage);
	}

	/** {@inheritDoc} */
	@Override
	public void doStopApplicationStartup() {
		this.businessTransverse.doStopApplicationStartup();
	}

	/** {@inheritDoc} */
	@Override
	public void doRestartApplicationStartup() {
		this.businessTransverse.doRestartApplicationStartup();
	}

	/** {@inheritDoc} */
	@Override
	public void doDeleteSerializedConfiguration() {
		this.businessTransverse.doDeleteSerializedConfiguration();
	}

	/** {@inheritDoc} */
	@Override
	public void doDisplaySetting( Screen p_oScreen ) {
		this.businessTransverse.doDisplaySetting();
	}

	/** {@inheritDoc} */
	@Override
	public void doDisplayParameterDialog() {
		this.businessTransverse.doDisplayParameterDialog();
	}
	
	/** {@inheritDoc} */
	@Override
	public void doDisplayResetSettingsAndExitDialog() {
		this.businessTransverse.doDisplayResetSettingsAndExitDialog();
	}

	/** {@inheritDoc} */
	@Override
	public void doResetDataBase( Screen p_oScreen ) {
		this.businessTransverse.doResetDataBase( p_oScreen );
	}
	
	/** {@inheritDoc} */
	@Override
	public void doImportDatabase( Screen p_oScreen ) {
		this.businessTransverse.doImportDatabase();
	}
	
	/** {@inheritDoc} */
	@Override
	public void doSendReport(Screen p_oScreen) {
		this.businessTransverse.doSendReport(p_oScreen);
	}

	/** {@inheritDoc} */
	@Override
	public void doResetSetting( Screen p_oScreen ) {
		this.businessTransverse.doResetSetting( p_oScreen );
	}

	/** {@inheritDoc} */
	@Override
	public void doPhoneCall(PhoneNumber p_oPhoneNumber) {
		this.businessTransverse.doPhoneCall(p_oPhoneNumber);
	}

	/** {@inheritDoc} */
	@Override
	public void doWriteEmail(EMail p_oEmail) {
		this.businessTransverse.doWriteEmail(p_oEmail);
	}

	/** {@inheritDoc} */
	@Override
	public void doOpenMap(AddressLocation p_oAddressLocation) {
		this.businessTransverse.doOpenMap(p_oAddressLocation);
	}

	/** {@inheritDoc} */
	@Override
	public BarCodeResult doAfterBarCodeScan(BarCodeResult p_oBarCodeResult) {
		return this.businessTransverse.doAfterBarCodeScan(p_oBarCodeResult);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Launch the actioin to display the exit dialog.
	 */
	@Override
	public void doDisplayExitApplicationDialog( Screen screen ) {
		this.businessTransverse.doDisplayExitApplicationDialog( screen );
	}
	
	/** {@inheritDoc} */
	@Override
	public void doDisplayMain( Screen p_oScreen ) {
		this.businessTransverse.doDisplayMain( p_oScreen );
	}
	
	
	//////////////////////////
	// ZONE SYNCHRONISATION //
	//////////////////////////
	
	/** {@inheritDoc} */
	@Override
	public void doClassicSynchronisationFromMenu(Screen p_oScreen, Class<? extends Screen> p_oNextScreen) {
		this.businessSynchronization.doClassicSynchronisationFromMenu(p_oScreen, p_oNextScreen);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doClassicSynchronisation() {
		doClassicSynchronisation(null);
	}
	
	/** {@inheritDoc} */
	@Override
	public void doClassicSynchronisation(Screen p_oScreen) {
		this.businessSynchronization.doClassicSynchronisation(p_oScreen);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doClassicSynchronisationSync() {
		this.businessSynchronization.doClassicSynchronisationSync();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFirstSynchronisation() {
		doFirstSynchronisation(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFirstSynchronisationSync() {
		this.businessSynchronization.doFirstSynchronisationSync();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFirstSynchronisation( Screen p_oScreen ) {
		this.businessSynchronization.doFirstSynchronisation( p_oScreen);
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(SynchronizationListener p_oListener) {
		this.businessSynchronization.register(p_oListener);
	}
	
	/** {@inheritDoc} */
	@Override
	public void unregister(SynchronizationListener p_oListener) {
		this.businessSynchronization.unregister(p_oListener);
	}
	
	/** {@inheritDoc} */
	@Override
	public abstract void doDisplaySynchronisationDialog(Class<? extends SynchronizationAction> p_oAction, 
			SynchronizationActionParameterIN p_oSynchronisationParameters);

	/** {@inheritDoc} */
	@Override
	public void doDisplaySynchronisationDialog(SynchronizationDialogParameterIN p_oInParameter) {
		this.businessSynchronization.doDisplaySynchronisationDialog(p_oInParameter);
	}

	/** {@inheritDoc} */
	@Override
	public void manageSynchronizationActions(final int p_iSynchroState) {
		this.businessSynchronization.manageSynchronizationActions(p_iSynchroState);
	}

	/** {@inheritDoc} */
	@Override
	public void manageSynchronizationActions(final int p_iSynchroState, final ManageSynchronizationParameter p_oParameters) {
		this.businessSynchronization.manageSynchronizationActions(p_iSynchroState, p_oParameters);
	}

	/** {@inheritDoc} */
	@Override
	public void notifySynchronizationProcessStart(Notifier p_oNotifier, Object...p_oObjects) {
		this.businessSynchronization.notifySynchronizationProcessStart(p_oNotifier, p_oObjects);
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifySynchronizationProgressChanged2(Notifier p_oNotifier, int p_iLevel, String p_sMessage) {
		this.businessSynchronization.notifySynchronizationProgressChanged(p_oNotifier, p_iLevel, p_sMessage);
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifySynchronizationProgressChanged2(Notifier p_oNotifier, int p_iLevel, ApplicationR p_oMessage) {
		this.businessSynchronization.notifySynchronizationProgressChanged(p_oNotifier, p_iLevel, 
				Application.getInstance().getStringResource(p_oMessage));
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifySynchronizationProgressChanged2(Notifier p_oNotifier, int p_iLevel, int p_iMaxStep, ApplicationR p_oMessage) {
		this.businessSynchronization.notifySynchronizationProgressChanged(p_oNotifier, p_iLevel, p_iMaxStep, 
				Application.getInstance().getStringResource(p_oMessage));
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifySynchronizationProgressChanged2(Notifier p_oNotifier, int p_iLevel, int p_iMaxStep, String p_sMessage) {
		this.businessSynchronization.notifySynchronizationProgressChanged(p_oNotifier, p_iLevel, p_iMaxStep, p_sMessage);
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifySynchronizationResetProgress(Notifier p_oNotifier) {
		this.businessSynchronization.notifySynchronizationResetProgress(p_oNotifier);
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifySynchronizationProcessStop(Notifier p_oNotifier, MContext p_oContext, SynchronisationResponseTreatmentInformation p_oInformation) {
		this.businessSynchronization.notifySynchronizationProcessStop(p_oNotifier, p_oContext, p_oInformation);
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifySynchroStart(final MContext p_oContext){
		this.notifySynchro(p_oContext);
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifySynchroSuccess(final MContext p_oContext){
		this.notifySynchro(p_oContext);
	}

	/** {@inheritDoc} */
	@Override
	public void notifyLicenceFailure(final MContext p_oContext) {
		this.notifySynchro(p_oContext);
	}
	/** {@inheritDoc} */
	@Override
	public void notifyIncompatibleTimeFailure(final MContext p_oContext){
		this.notifySynchro(p_oContext);
	}	
	/** {@inheritDoc} */
	@Override
	public void notifySynchronizeData(final MContext p_oContext){
		this.notifySynchro(p_oContext);
	}	
	/** {@inheritDoc} */
	@Override
	public boolean isNeedFullSynchro(){
		return this.needFullSynchro;
	}	
	/** {@inheritDoc} */
	@Override
	public void needFullSynchro(boolean p_bValue){
		this.needFullSynchro = p_bValue;
	}
	/** {@inheritDoc} */
	@Override
	public void notifyAuthenticationFailure(final MContext p_oContext) {
		this.notifySynchro(p_oContext);
	}
	/** {@inheritDoc} */
	@Override
	public void notifySynchroConnectionError(final MContext p_oContext){
		this.notifySynchro(p_oContext);
	}
	/** {@inheritDoc} */
	@Override
	public void notifySynchroCrash(final MContext p_oContext){
		this.notifySynchro(p_oContext);
	}
	/**
	 * Notify synchronisation
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 */
	protected void notifySynchro(final MContext p_oContext) {
		// Method to overload to define the functional and graphical rendering of the notification.
	}
	/**
	 * <p>isOngoingSynchroAction.</p>
	 *
	 * @return a boolean.
	 */
	@Override
	public boolean isOngoingSynchroAction() {
		return this.businessSynchronization.isOngoingSynchroAction();
	}	
	/** {@inheritDoc} */
	@Override
	public void releaseSynchroSemaphore(){
		this.businessSynchronization.releaseSynchroSemaphore();
	}	
	/**
	 * Returns the synchro state
	 *
	 * @return the synchro state
	 */
	protected int getSyncState() {
		return this.businessSynchronization.getSyncState();
	}	
	/**
	 * Returns true whether there is data to send
	 *
	 * @return true whether there is data to send
	 */
	protected boolean isMM2BONotif() {
		return this.businessSynchronization.isMM2BONotif();
	}	
	/**
	 * Returns true whether there is data to receive
	 *
	 * @return true whether there is data to reveive
	 */
	protected boolean isBO2MMNotif() {
		return this.businessSynchronization.isBO2MMNotif();
	}	
	/**
	 * Return true if the synchronization state has changed.
	 *
	 * @return boolean indicator
	 */
	public boolean isSyncStateChanged() {
		return this.businessSynchronization.isSyncStateChanged();
	}	
	/**
	 * {@inheritDoc}
	 *
	 * Notifie le controller que le dataloader de type p_oClass a changé
	 */
	//XXX CHANGE V3.2
	@Override
	public void notifyDataLoaderReload(MContext p_oContext, String p_sKey, MMDataloader<?> p_oDataLoader) {
		Collection<Object> oListenersList = Application.getInstance().getDataLoaderListeners(p_oDataLoader.getClass());
		if (oListenersList != null) {
			Method oMethod = null;
			//XXX CHANGE V3.2
			ListenerOnDataLoaderReloadEvent<?> oEvent = new ListenerOnDataLoaderReloadEvent<Dataloader<?>>(p_oDataLoader, p_sKey);
			
			for(final Object oListener : oListenersList) {
				String sListenerName = null;
				Object oRealListener = oListener;
				if (oListener instanceof String) {
					sListenerName = (String)oListener;
					oRealListener = Application.getInstance().getScreenObjectFromName(sListenerName);
				}
				
				if (oListener instanceof String && Application.getInstance().isStoreEventsEnabledForDisplay(sListenerName)) {
					this.addPendingEvent(oEvent, sListenerName);
				} else {
					// oRealListener may have been gc.
					if ( oRealListener != null ) {
						// il faut utiliser l'interface du dataloader
						// fonctionne bien car pour l'instant seul des écrans peuvent s'enregistrer sur les loaders
						oMethod = Application.getInstance().getClassAnalyser().getClassAnalyse(oRealListener).getMethodOfDataLoaderListener(
								(Class<? extends Dataloader>) p_oDataLoader.getClass().getInterfaces()[0], oRealListener.getClass());
	
						invokeEventMethod(p_oContext, oMethod, oEvent, oRealListener);
					}
				}
			}
		}
	}

	/**
	 * @param p_oEvent
	 * @param sListenerName
	 */
	private void addPendingEvent(Object p_oEvent,
			String sListenerName) {
		Queue<Object> oQueue = this.pendingEvents.get(sListenerName);
		if (oQueue == null) {
			oQueue = new LinkedList<>();
			this.pendingEvents.put(sListenerName, oQueue);
		}
		oQueue.add(p_oEvent);
	}	
		
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkController#publishBusinessEvent(com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent)
	 */
	@Override
	public void publishBusinessEvent(MContext p_oContext, BusinessEvent<?> p_oEvent) {
		Map<Class<?>, List<Object>> oMapFilterListeners = Application.getInstance().getBusinessEventListeners(p_oEvent.getClass());
		
		if (oMapFilterListeners != null) {
			this.publishConcreteEvent(oMapFilterListeners, p_oContext, p_oEvent, Object.class);
			this.publishConcreteEvent(oMapFilterListeners, p_oContext, p_oEvent, p_oEvent.getFilterClass());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void publishConcreteEvent(Map<Class<?>, List<Object>> p_oMapFilterListeners, MContext p_oContext, BusinessEvent<?> p_oEvent, Class<?> p_oFilter) {
		List<Object> listeners = p_oMapFilterListeners.get(p_oFilter);
		if (listeners != null) {
			Method oMethod = null;
			for(final Object oListener : listeners) {
				String sListenerName = null;
				Object oRealListener = oListener;
				if (oListener instanceof String) {
					sListenerName = (String)oListener;
					oRealListener = Application.getInstance().getScreenObjectFromName(sListenerName);
				}
				
				if (oListener instanceof String && Application.getInstance().isStoreEventsEnabledForDisplay(sListenerName)) {
					this.addPendingEvent(p_oEvent, sListenerName);
				} else {
					if ( oRealListener != null ) {
						// il faut utiliser l'interface du dataloader
						// fonctionne bien car pour l'instant seul des écrans peuvent s'enregistrer sur les loaders
						oMethod = Application.getInstance().getClassAnalyser().getClassAnalyse(oRealListener).getMethodOfBusinessEventListener((Class<? extends BusinessEvent>) p_oEvent.getClass(), oRealListener.getClass(), p_oFilter);
						invokeEventMethod(p_oContext, oMethod, p_oEvent, oRealListener);
						if (p_oEvent.isConsummed()) {
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void invokeQueuedEvents(MContext p_oContext, ListenerIdentifier p_oListener) {
		String sClassName = p_oListener.getUniqueId();
		if (this.pendingEvents.containsKey(sClassName)) {
			for (Object oEvent : this.pendingEvents.get(sClassName)) {
				Method oMethod = null;
				if (oEvent instanceof BusinessEvent) {
					// invoke with no filter
					oMethod = Application.getInstance().getClassAnalyser().getClassAnalyse(p_oListener).getMethodOfBusinessEventListener((Class<? extends BusinessEvent<?>>) oEvent.getClass(), p_oListener.getClass(), Object.class);
					if (oMethod != null) {
						this.invokeEventMethod(p_oContext, oMethod, oEvent, p_oListener);
					}
					// invoke with source filter
					oMethod = Application.getInstance().getClassAnalyser().getClassAnalyse(p_oListener).getMethodOfBusinessEventListener((Class<? extends BusinessEvent<?>>) oEvent.getClass(), p_oListener.getClass(), ((BusinessEvent<?>) oEvent).getFilterClass());
					if (oMethod != null) {
						this.invokeEventMethod(p_oContext, oMethod, oEvent, p_oListener);
					}
				} else if ( oEvent instanceof ListenerOnDataLoaderReloadEvent<?> ) {
					oMethod = Application.getInstance().getClassAnalyser().getClassAnalyse(p_oListener).getMethodOfDataLoaderListener((Class<? extends Dataloader<?>>) ((ListenerOnDataLoaderReloadEvent<?>) oEvent).getDataLoader().getClass().getInterfaces()[0], p_oListener.getClass());
					if (oMethod != null) {
						this.invokeEventMethod(p_oContext, oMethod, oEvent, p_oListener);
					}
				}
			}
			this.pendingEvents.remove(sClassName);
		}
	}
	
	/**
	 * Invokes a method linked to an event on a given listening object
	 * @param p_oContext context to use
	 * @param p_oMethod method to invoke
	 * @param p_oEvent event to give to the method
	 * @param p_oListener listening object
	 */
	protected abstract void invokeEventMethod(MContext p_oContext, Method p_oMethod, Object p_oEvent, Object p_oListener);
	
	/**
	 * 
	 * <p>Permet d'éxecuter l'appel d'une mérthode dans un thread différent du thread courrant</p>
	 *
	 *
	 *
	 */
	public static class InvokeMethodRun implements Runnable {

		/** la méthode a appeler */
		private Method method = null;
		/** l'objet sur lequel on appel la méthode */
		private Object call = null;
		/** les paramètres a passé à la méthode */
		private Object parameter = null;
		
		/**
		 * Constructeur
		 * @param p_oMethod la méthode a appelé
		 * @param p_oCall l'objet sur lequel appeler la méthode
		 * @param p_oParameter paramètre a passer en paramètre
		 */
		public InvokeMethodRun(Method p_oMethod, Object p_oCall, Object p_oParameter) {
			this.method = p_oMethod;
			this.parameter = p_oParameter;
			this.call = p_oCall;
		}		
		/**
		 * {@inheritDoc}
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				method.invoke(call, parameter);
			} catch (IllegalArgumentException e) {
				Application.getInstance().getLog().error(TAG, "FwkControllerImpl: Impossible d'évoquer une méthode", e);
			} catch (IllegalAccessException e) {
				Application.getInstance().getLog().error(TAG, "FwkControllerImpl: Impossible d'évoquer une méthode", e);
			} catch (InvocationTargetException e) {
				Application.getInstance().getLog().error(TAG, "FwkControllerImpl: Impossible d'évoquer une méthode", e);
			}
		}
	}

	/**
	 * Uniquement utilisé dans le cas de la synchro
	 *
	 * @param p_oAction a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	public void launchActionPostExecute(Action<?, ?, ?, ?> p_oAction) throws ActionException {
		
		if (canBeginAction(p_oAction)) {
			MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
			MContext oContext = oCf.createContext();
			try {
				Application.getInstance().getLog().info(Application.LOG_TAG, "Transaction: begin " + p_oAction.getClass().getName());
				oContext.beginTransaction();
				p_oAction.doPostExecute(oContext, null, null);
			}
			catch(Exception e) {
                Application.getInstance().getLog().debug(TAG, "Exception on Transaction begin");
				throw new ActionException(e);
			}
			finally {
				try {
					Application.getInstance().getLog().info(Application.LOG_TAG, "Transaction: close " + p_oAction.getClass().getName());
					oContext.endTransaction();
					oContext.close();
				}
				finally {
                    Application.getInstance().getLog().debug(TAG, "Transaction release status");
					this.releaseBusyStatus(oContext , p_oAction);
				}
			}
		}
		else {
			throw new ActionException("déjà une action en cours");
		}
	}
}
