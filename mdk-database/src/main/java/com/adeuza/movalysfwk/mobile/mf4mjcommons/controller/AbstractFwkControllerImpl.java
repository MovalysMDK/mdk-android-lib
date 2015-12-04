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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionRule;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionRuleContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AlertAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AsyncAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AsyncRedirectActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.IgnoreRuleBeforeAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.RedirectActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskOut;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.AlertMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * <p>
 * Defines the controller of the application. The controller can notify listener :
 * <li>when an action with read access database was launched</li>
 * <li>when an action with write access database was launched</li>
 * </p>
 *
 *
 */
public abstract class AbstractFwkControllerImpl extends AbstractBaseFwkControllerImpl {
	
	/** Action task progress value array size */
	private static final int ACTION_TASK_PROGRESS_VALUE_SIZE = 3;

	private static final String TAG = "ACTION";

	/**
	 * Launch an action with the management of errors. 
	 * Throws an exception when the action is launched before the end of a another action
	 * 
	 * @param p_oContext the context to use
	 * @param p_oNotifier object launching the action
	 * @param p_oAction the action to launch
	 * @param p_oParameterIn the parameter to user for launch action
	 * @param p_oActionRule the action rule            
	 * @param p_oActionRuleContext the actions' rule context               
	 * @return the result of business processing
	 * @throws ActionException if any
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected ActionParameter launchAction(MContext p_oContext, Notifier p_oNotifier, @SuppressWarnings("rawtypes") 
			Action p_oAction, ActionParameter p_oParameterIn, ActionRule p_oActionRule, ActionRuleContext p_oActionRuleContext) throws ActionException {

		LaunchParameter oLaunchParameter = null;
		if (p_oContext instanceof AndroidContextImpl) {
			oLaunchParameter = ((AndroidContextImpl) p_oContext).getLaunchParameter();
		}
		
		List<ActionHandler> oAsyncRedirections = new ArrayList<ActionHandler>();
		
		long lStart = System.currentTimeMillis();
		
		synchronized (this.actionId) {
			this.actionId++;
			if (p_oParameterIn != null) {
				p_oParameterIn.setActionId(this.actionId);
			}
		}
		
		boolean bContinueTreatment = executeRuleBeforeAction(p_oContext, p_oAction, p_oActionRule, p_oActionRuleContext);
		
		ActionParameter r_oResult = null;
		
		if (bContinueTreatment) {
			p_oContext.reset();

			//PREEXECUTE : si des threads différents traitements dans le thread ui
			launchActionPreExecute(p_oContext, p_oAction, p_oParameterIn, oLaunchParameter);
			
			//EXECUTE
            Application.getInstance().getLog().debug(TAG, "doAction " + p_oAction.getClass().getSimpleName());
			r_oResult = p_oAction.doAction(p_oContext, p_oParameterIn);
			if (p_oContext.getMessages().hasErrors()) {
                Application.getInstance().getLog().debug(TAG, "doAction error " + p_oAction.getClass().getSimpleName());
				r_oResult = p_oAction.doOnError(p_oContext, p_oParameterIn, r_oResult);
			} else {
                Application.getInstance().getLog().debug(TAG, "doAction success " + p_oAction.getClass().getSimpleName());
				p_oAction.doOnSuccess(p_oContext, p_oParameterIn, r_oResult);
			}
			
			//POSTEXECUTE : si des threads différents traitements dans le thread ui
            Application.getInstance().getLog().debug(TAG, "postExecute " + p_oAction.getClass().getSimpleName());
			lanchActionPostExecute(p_oContext, p_oAction, p_oParameterIn, oLaunchParameter, r_oResult);

			long lEnd = System.currentTimeMillis();
			Application.getInstance().getLog().debug("PERF", p_oAction.getClass().getName() + " : " + (lEnd-lStart) + "ms");
		
			if (p_oAction instanceof AsyncAction && p_oParameterIn != null) {
                Application.getInstance().getLog().debug(TAG, "AsyncAction " + p_oAction.getClass().getSimpleName());
				if (p_oParameterIn instanceof AsyncRedirectActionParameter) {
					oAsyncRedirections.addAll(((AsyncRedirectActionParameter) p_oParameterIn).getAsyncRedirectActions());
				}
				if (r_oResult instanceof AsyncRedirectActionParameter) {
					oAsyncRedirections.addAll(((AsyncRedirectActionParameter) r_oResult).getAsyncRedirectActions());
				}
				this.asyncActionHandlers.put(p_oParameterIn.getActionId(), new AsyncActionHandler(oAsyncRedirections, p_oContext, p_oNotifier,
						p_oParameterIn, r_oResult));
			}

			List<ActionHandler> oRedirections = new ArrayList<ActionHandler>();
			if (p_oParameterIn instanceof RedirectActionParameter) {
				oRedirections.addAll(((RedirectActionParameter) p_oParameterIn).getRedirectActions());
			}
			
			if (r_oResult instanceof RedirectActionParameter) {
				List<ActionHandler> listRedirectActions = ((RedirectActionParameter) r_oResult).getRedirectActions();
				if ( listRedirectActions != null && !listRedirectActions.isEmpty()) {
					oRedirections.addAll(listRedirectActions);
				}
			}
			
			try {
				launchRedirectionsActions(p_oContext, p_oNotifier, p_oParameterIn, p_oActionRule, p_oActionRuleContext, r_oResult, oRedirections);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new MobileFwkException(e);
			}
			
			if (oLaunchParameter!=null) {
				oLaunchParameter.setFirstAction(false);
			}
		}
		return r_oResult;
	}

	/**
	 * Launch actions on redirections
	 * @param p_oContext context to use
	 * @param p_oNotifier notifier 
	 * @param p_oParameterIn param in
	 * @param p_oActionRule action rule
	 * @param p_oActionRuleContext contex on action rule
	 * @param p_oResult result of the action
	 * @param p_oRedirections redirections
	 * @throws ActionException if any
	 * @throws IllegalAccessException if any
	 */
	private void launchRedirectionsActions(MContext p_oContext, Notifier p_oNotifier, ActionParameter p_oParameterIn,
			ActionRule p_oActionRule, ActionRuleContext p_oActionRuleContext, ActionParameter p_oResult, List<ActionHandler> p_oRedirections) 
					throws ActionException, IllegalAccessException {
		ActionParameter oRedirectInParameter;
		Field oSourceField = null;
		Object oSourceData = null;
		for (ActionHandler oRedirection : p_oRedirections) {
			oRedirectInParameter = oRedirection.parameter;
			
			if (oRedirectInParameter != null) {
				continue;
			}
			
			oRedirectInParameter = oRedirection.action.getEmptyInParameter();
			
			for (Field oTargetField : oRedirectInParameter.getClass().getDeclaredFields()) {
				
				if (oTargetField.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				
				try {
                    Application.getInstance().getLog().debug(TAG, "launchRedirectionsActions " + oTargetField.getName());
					// on cherche d'abord si le champ existe dans r_oResult, puis dans p_oParameterIn
					if (p_oResult != null) {
						oSourceField = p_oResult.getClass().getDeclaredField(oTargetField.getName());
						oSourceData = p_oResult;
					}
					if (oSourceField == null && p_oParameterIn != null) {
						oSourceField = p_oParameterIn.getClass().getDeclaredField(oTargetField.getName());
						oSourceData = p_oParameterIn;
					}
				} catch (NoSuchFieldException e) {
					Application.getInstance().getLog().debug("launchRedirectionsActions", e.getMessage());
				}
				
				if (oSourceField != null) {
                    Application.getInstance().getLog().debug(TAG, "launchRedirectionsActions set " + oTargetField.getName() + " " + oSourceField.getName());
					oTargetField.set(oRedirectInParameter, oSourceField.get(oSourceData));
				}
				
				oSourceField = null;
				oSourceData = null;
			}
			
			this.launchAction(p_oContext, p_oNotifier, oRedirection.action, oRedirectInParameter, p_oActionRule, p_oActionRuleContext);
		}
	}

	/**
	 * done after launching the actions
	 * @param p_oContext context to use
	 * @param p_oAction action being launched
	 * @param p_oParameterIn in parameters for the action
	 * @param p_oLaunchParameter launch parameters
	 * @param p_oResult result of the action
	 * @throws ActionException if any
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void lanchActionPostExecute(MContext p_oContext, Action p_oAction, ActionParameter p_oParameterIn,
			LaunchParameter p_oLaunchParameter, ActionParameter p_oResult) throws ActionException {
		if (p_oLaunchParameter==null || !p_oLaunchParameter.isUseActionTask()) {
            Application.getInstance().getLog().debug(TAG, "lanchActionPostExecute launch outside of a task");
			//Lancement de l'action en dehors d'une tâche
			if (p_oLaunchParameter!=null && p_oLaunchParameter.isLaunchPostExecute()) {
				p_oAction.doPostExecute(p_oContext, p_oParameterIn, p_oResult);
			}
		}
		else {
			//Lancement de l'action dans une tâche
			if (!p_oLaunchParameter.isFirstAction()) {
				// à lancer dans le thread ui : ie en faisant un publish
				ActionTaskProgress oAtp = new ActionTaskProgress();
				oAtp.setStep(MMActionTask.ActionTaskStep.POST_EXECUTE);
				Object[] tValue = new Object[ACTION_TASK_PROGRESS_VALUE_SIZE];
				tValue[0] = p_oAction;
				tValue[1] = p_oParameterIn;
				tValue[2] = p_oResult;
				oAtp.setValue(tValue);
				p_oLaunchParameter.getTask().publishActionProgress(oAtp);
			}
			// else {
				// on ne fait le post execute sera lancer dans le post execute de la tâche
			// }
		}
	}

	/**
	 * done before launching the actions
	 * @param p_oContext context to use
	 * @param p_oAction action being launched
	 * @param p_oParameterIn in parameters for the action
	 * @param p_oLaunchParameter launch parameters
	 * @throws ActionException if any
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void launchActionPreExecute(MContext p_oContext, Action p_oAction, ActionParameter p_oParameterIn, LaunchParameter p_oLaunchParameter) 
			throws ActionException {
		if (p_oLaunchParameter==null || !p_oLaunchParameter.isUseActionTask()) {
			//Lancement de l'action en dehors d'une tâche
			p_oAction.doPreExecute(p_oParameterIn, p_oContext);
		}
		else {
			//Lancement de l'action dans une tâche
			if (!p_oLaunchParameter.isFirstAction()) {
				// à lancer dans le thread ui : ie en faisant un publish
				ActionTaskProgress oAtp = new ActionTaskProgress();
				oAtp.setStep(MMActionTask.ActionTaskStep.PRE_EXECUTE);
				Object[] oValue = new Object[1];
				oValue[0] = p_oAction;
				oAtp.setValue(oValue);
				p_oLaunchParameter.getTask().publishActionProgress(oAtp);
			}
			// else {
				// on ne fait rien déjà lancer dans le onPreExecute de la tâche
			// }
		}
	}

	/**
	 * Centralized management rule on actions
	 * It can be discarded by adding the annotation IgnoreRuleBeforeAction to the action
	 * @param p_oContext context to use
	 * @param p_oAction action being launched
	 * @param p_oActionRule rule to apply
	 * @param p_oActionRuleContext context of the rule
	 * @return true if the process should go on afterwards
	 */
	@SuppressWarnings("rawtypes")
	private boolean executeRuleBeforeAction(MContext p_oContext, Action p_oAction, ActionRule p_oActionRule, ActionRuleContext p_oActionRuleContext) {
		boolean r_bContinueTreatment = true;
		if (p_oActionRule != null 
				&& p_oAction.getClass().getAnnotation(IgnoreRuleBeforeAction.class) == null) {
			
			// Exécution de la règle
			p_oActionRule.compute(p_oContext, p_oActionRuleContext);
				
			// Traitement spécifique selon l'état retourné
			switch (p_oActionRuleContext.getState()) {
			case ActionRuleContext.DO_NOTHING_STATE_VALUE:
				p_oActionRule.doOnDoNothingState(p_oContext, p_oActionRuleContext);
				break;
			case ActionRuleContext.CONTINUE_CONTROLLER_STATE_VALUE:
				p_oActionRule.doOnContinueControllerState(p_oContext, p_oActionRuleContext);
				break;
			case ActionRuleContext.INTERRUPT_CONTROLLER_STATE_VALUE:
				p_oActionRule.doOnInterruptControllerState(p_oContext, p_oActionRuleContext);
				r_bContinueTreatment = false;
				break;
			default:
				break;
			}
		}
		return r_bContinueTreatment;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @param p_lActionId a long.
	 * @param p_oActions a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionHandler} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	@Override
	public void doOnAsyncActionEnd(long p_lActionId, ActionHandler... p_oActions) throws ActionException {
		AsyncActionHandler oHandler = this.asyncActionHandlers.get(p_lActionId);
		if (oHandler.getContext() instanceof AndroidContextImpl) {
			LaunchParameter oLp = new LaunchParameter();
			((AndroidContextImpl) oHandler.getContext()).setLaunchParameter(oLp);
		}
		if (p_oActions == null) {
			for (ActionHandler oAction : oHandler.getActions()) {
				this.launchAction(oHandler.getContext(), oHandler.getNotifier(), oAction.action, oAction.parameter);
			}
		} else {
			for (ActionHandler oAction : p_oActions) {
				this.launchAction(oHandler.getContext(), oHandler.getNotifier(), oAction.action, oAction.parameter);
			}
		}
		this.asyncActionHandlers.remove(p_lActionId);
	}
	
	/**
	 * Launch an action
	 *
	 * @param p_oTask  action to launch
	 * @return The action task out
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	@Override
	public ActionTaskOut launchActionByActionTask(MMActionTask p_oTask) throws ActionException {
		ActionTaskOut oAto = new ActionTaskOut();
		LaunchParameter oLp = new LaunchParameter();
		oLp.setLaunchPostExecute(false);
		oLp.setUseActionTask(true);
		oLp.setTask(p_oTask);
		((AndroidContextImpl) p_oTask.getContext()).setLaunchParameter(oLp);
		oAto.setOut(this.launchAction(p_oTask.getContext(), p_oTask.getNotifier(), p_oTask.getAction(), p_oTask.getParameterIn().getIn()));
		return oAto;
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
	@Override
	protected ActionParameter launchActionWithBusyVerification(final Notifier p_oNotifier, @SuppressWarnings("rawtypes")// pas moyen
			// de faire  autrement pour gérer  simplement les problèmes de cast
			final Action p_oAction, final ActionParameter p_oParameterIn) {

		Application.getInstance().getLog().info(TAG, "LaunchActionWithBusyVerification: launch action="+p_oAction.toString());
		ActionParameter r_oResult = null;
		if (canBeginAction(p_oAction) || AlertAction.class.isAssignableFrom(p_oAction.getClass())) {
			MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
			MContext oContext = oCf.createContext();
			try {
				Application.getInstance().getLog().info(TAG, "Transaction: begin " + p_oAction.getClass().getName());
				oContext.beginTransaction();

				if (oContext instanceof AndroidContextImpl) {
					LaunchParameter oLp = new LaunchParameter();
					((AndroidContextImpl) oContext).setLaunchParameter(oLp);
				}
				r_oResult = launchAction(oContext, p_oNotifier, p_oAction, p_oParameterIn);
			}
			catch (Exception e) {
				Application.getInstance().getLog().error(TAG, "FwkControllerImpl: Exception in launchActionWithBusyVerification",e);
				oContext.getMessages().addMessage(ExtFwkErrors.LaunchActionError);
				Application.getInstance().getLog().error(TAG, "Error: " + p_oAction.getClass().getName(), e);
			}
			finally {
				Application.getInstance().getLog().info(TAG, "LaunchActionWithBusyVerification: release action="+p_oAction.toString());
				Application.getInstance().getLog().info(TAG, "Transaction: close " + p_oAction.getClass().getName());
				oContext.endTransaction();
				oContext.close();
				releaseBusyStatus(oContext, p_oAction);
			}
		} else {
			// A2A_DEV SMA : remplacer l'id de resource string par sa clé : alert_application_notiddle
			AlertMessage oMessage = new AlertMessage(DefaultApplicationR.alert_application_notiddle, AlertMessage.SHORT);
			Application.getInstance().getLog().error(TAG, "LaunchActionWithBusyVerification> conflit d'action : "+p_oAction.toString());
			this.doDisplayMessage(oMessage);
		}
		return r_oResult;
	}

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
	@Override
	protected ActionHandler launchActionWithBusyVerificationFromBackgroundThread(final Notifier p_oNotifier, @SuppressWarnings("rawtypes")// pas moyen
			// de faire  autrement pour gérer  simplement les problèmes de cast
			final Action p_oAction, final ActionParameter p_oParameterIn) {

		ActionParameter oResult = null;
		if (this.canBeginAction(p_oAction) || AlertAction.class.isAssignableFrom(p_oAction.getClass())) {
			MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
			MContext oContext = oCf.createContext();
			try {
				Application.getInstance().getLog().info(TAG, "Transaction: begin " + p_oAction.getClass().getName());
				oContext.beginTransaction();
				
				if (oContext instanceof AndroidContextImpl) {
					LaunchParameter oLp = new LaunchParameter();
					oLp.setLaunchPostExecute(false);
					((AndroidContextImpl) oContext).setLaunchParameter(oLp);
				}
				oResult = launchAction(oContext, p_oNotifier, p_oAction, p_oParameterIn);
			}
			catch(Exception e) {
				Application.getInstance().getLog().error(TAG, "FwkControllerImpl: Exception in launchActionWithBusyVerification",e);
				Application.getInstance().getLog().error(TAG, "Error: " + p_oAction.getClass().getName(), e);
				oContext.getMessages().addMessage(ExtFwkErrors.LaunchActionError);
			}
			finally {
				Application.getInstance().getLog().info(TAG, "Transaction: close " + p_oAction.getClass().getName());
				oContext.endTransaction();
				oContext.close();
				Application.getInstance().getLog().info(TAG, "LaunchActionWithBusyVerification: release action="+p_oAction.toString());
				releaseBusyStatus(oContext, p_oAction);
			}
		} else {
			// A2A_DEV SMA : remplacer l'id de resource string par sa clé : alert_application_notiddle
			AlertMessage oMessage = new AlertMessage(DefaultApplicationR.alert_application_notiddle, AlertMessage.SHORT);
			this.doDisplayMessage(oMessage);
		}
		return new ActionHandler(p_oAction, oResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * This method releases the markers set by {@link FwkController#canBeginAction} {@link Action#isConcurrentAction()} and number of active
	 * actions are used this method used synchronized operations on privates attributes busy an running
	 */
	@Override
	public void releaseBusyStatus(MContext p_oContext, Action<?,?,?,?> p_oAction) {
        synchronized (this.mapBusyQueue) {
            if (p_oAction.getConcurrentAction() != Action.NO_QUEUE) {
				if (this.isBusyIntoQueue(p_oAction)) {
					Application.getInstance().getLog().debug(TAG, p_oAction.getClass().getName() + " QUEUE number " +
                            p_oAction.getConcurrentAction() + " QUEUE isBusyIntoQueue");
					this.modifyBusyIntoQueue(p_oAction, false);
				} else {
					Application.getInstance().getLog().debug(TAG, p_oAction.getClass().getName() + " QUEUE number " +
                            p_oAction.getConcurrentAction() + " QUEUE NOT isBusyIntoQueue");
				}
            } else {
				Application.getInstance().getLog().debug(TAG, p_oAction.getClass().getName() + " QUEUE number " +
                        p_oAction.getConcurrentAction() + " NOQUEUE");
			}

            if (p_oContext instanceof AndroidContextImpl) {
                AndroidContextImpl exContext = (AndroidContextImpl) p_oContext;
                if (exContext.getLaunchParameter() != null && exContext.getLaunchParameter().isUseActionTask()) {
                    Queue<MMActionTask<?, ?, ?, ?>> oActionTaskQueue = searchActionTaskIntoQueue(p_oAction);
                    MMActionTask oTask = oActionTaskQueue.peek();
                    if (oTask != null && this.canBeginAction(oTask.getAction())) {
                        oActionTaskQueue.remove();
                        Application.getInstance().getLog().debug(TAG, "Lancement de " + oTask.getAction().getClass().getName());
                        this.modifyBusyIntoQueue(oTask.getAction(), true);
                        oTask.execAction();
                    }
                }
            }
        }
	}
	
	/**
	 * Invokes a method linked to an event on a given listening object
	 * @param p_oContext context to use
	 * @param p_oMethod method to invoke
	 * @param p_oEvent event to give to the method
	 * @param p_oListener listening object
	 */
	@Override
	protected void invokeEventMethod(MContext p_oContext, Method p_oMethod, Object p_oEvent, Object p_oListener) {
		if (p_oContext instanceof AndroidContextImpl && ((AndroidContextImpl)p_oContext).isLaunchedByActionTask()) {
			ActionTaskProgress oProgress = new ActionTaskProgress();
			oProgress.setStep(MMActionTask.ActionTaskStep.PROGRESS_RUNNABLE);
			Object[] oRuns = new Object[1];
			oRuns[0] = new InvokeMethodRun(p_oMethod, p_oListener, p_oEvent);
			oProgress.setValue(oRuns);
			((AndroidContextImpl)p_oContext).getLaunchParameter().getTask().publishActionProgress(oProgress);
		}
		else {
			try {
				p_oMethod.invoke(p_oListener, p_oEvent);
			} catch (IllegalArgumentException e) {
				Application.getInstance().getLog().error(TAG, "FwkControllerImpl: Impossible d'évoquer une méthode", e);
			} catch (IllegalAccessException e) {
				Application.getInstance().getLog().error(TAG, "FwkControllerImpl: Impossible d'évoquer une méthode", e);
			} catch (InvocationTargetException e) {
				Application.getInstance().getLog().error(TAG, "FwkControllerImpl: Impossible d'évoquer une méthode", e);
			}
		}
	}
}
