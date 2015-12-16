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
package com.adeuza.movalysfwk.mobile.mf4android.actiontask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask.ActionTaskStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.AbstractResultEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFailEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnProgressActionEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerDelegator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerIdentifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * ActionListenerHandler is responsible of invoking actions listeners on the activity.
 * <p>There are 3 kinds of events : success, failure and progress.</p>
 * <p>ActionListenerHandler is a singleton whose instance is managed by the controller.</p>
 * <p>If an event is published during a configuration change, the event is stored to be replayed on the new instance of the activity.</p>
 */
public class ActionListenerHandler {

	/**
	 * Pending events
	 */
	private Map<String, Queue<EventHolder>> pendingEvents = new HashMap<>();
	
	/**
	 * Invoke finish action listeners (success or error)
	 * @param p_sScreenIdentifier screen identifier
	 * @param p_oActionInterface action interface
	 * @param p_oInParam in parameter
	 * @param p_oOutParam out parameter
	 * @param p_oContext context
	 * @param p_oErrorEvent 
	 */
	public void notifyFinishListener( String p_sScreenIdentifier,
			Class<? extends Action<?,?,?,?>> p_oActionInterface,
			ActionParameter p_oInParam,
			ActionParameter p_oOutParam,
			Object p_oSource,
			MContext p_oContext,
			ListenerOnActionFailEvent<ActionParameter> p_oErrorEvent ) {
		
		if ( p_oContext.getMessages().hasErrors()) {
			this.notifyFailure(p_sScreenIdentifier, p_oActionInterface, p_oInParam, p_oOutParam, p_oSource, p_oContext, p_oErrorEvent);
		}
		else {
			this.notifySuccess(p_sScreenIdentifier, p_oActionInterface, p_oInParam, p_oOutParam, p_oSource, p_oContext);
		}
	}
	
	/**
	 * Invoke progress listeners
	 * @param p_sScreenIdentifier screen identifier
	 * @param p_oActionInterface action interface
	 * @param p_oInParam in parameter of the action
	 * @param p_oInfo progress info
	 * @param p_oContext context
	 */
	public void notifyProgressListeners( String p_sScreenIdentifier,
			Class<? extends Action<?,?,?,?>> p_oActionInterface,
			ActionParameter p_oInParam,
			ActionTaskProgress<?, ?> p_oInfo,
			MContext p_oContext) {

		// Create the event
		ListenerOnProgressActionEvent<Object> oEvent = new ListenerOnProgressActionEvent<Object>(
				p_oContext.getMessages().copy(), p_oInParam, p_oInfo.getValue(), null);
		
		// Get running activity
		AbstractMMActivity oActivity = getRunningActivity(p_oInParam, p_sScreenIdentifier);
		
		if ( oActivity != null ) {
			// If activity is not finishing, we can invoke the listener, else do nothing
			if ( !oActivity.isFinishing()) {
				this.invokeProgressListener(p_oActionInterface, p_oInfo.getStep(), oEvent, oActivity);
			}
		}
		else {
			// Activity is null, should we store events ?
			if ( Application.getInstance().isStoreEventsEnabledForDisplay(p_sScreenIdentifier)) {
				// Store events for next start (configuration changes)	
				addPendingEvent( new EventHolder( oEvent, p_oActionInterface, p_oInfo.getStep()), p_sScreenIdentifier);
			}
		}
	}

	/**
	 * Invoke pending events
	 * @param p_oListenerIdentifier
	 * @param p_oContext
	 */
	public void invokePendingEvents( ListenerIdentifier p_oListenerIdentifier, MContext p_oContext ) {
		Queue<EventHolder> events = this.pendingEvents.get(p_oListenerIdentifier.getUniqueId());
		if ( events != null && !events.isEmpty() ) {
			while( !events.isEmpty()) {
				EventHolder oEventHolder = events.poll();
				// If success event
				if ( ListenerOnActionSuccessEvent.class.isAssignableFrom(oEventHolder.event.getClass())) {
					ListenerOnActionSuccessEvent<ActionParameter> oSuccessEvent = (ListenerOnActionSuccessEvent<ActionParameter>) oEventHolder.event ;
					this.invokeSucessListener(oEventHolder.actionInterface, oSuccessEvent, p_oListenerIdentifier);
				}
				else 
				// If failure event
				if ( ListenerOnActionFailEvent.class.isAssignableFrom(oEventHolder.event.getClass())) {
					ListenerOnActionFailEvent<ActionParameter> oFailEvent = (ListenerOnActionFailEvent<ActionParameter>) oEventHolder.event ;
					this.invokeFailureListener(oEventHolder.actionInterface, oFailEvent, p_oListenerIdentifier, p_oContext);
				} else 
				// If progress event
				if ( ListenerOnProgressActionEvent.class.isAssignableFrom(oEventHolder.event.getClass())) {
					ListenerOnProgressActionEvent<Object> oProgressEvent = (ListenerOnProgressActionEvent<Object>) oEventHolder.event ;
					this.invokeProgressListener(oEventHolder.actionInterface, oEventHolder.step, oProgressEvent, p_oListenerIdentifier);
				}
			}
		}
	}
	
	/**
	 * Remove pending events of a particular listener
	 * @param p_sListenerIdentifier listener identifier
	 */
	public void removePendingEvents( String p_sListenerIdentifier ) {
		this.pendingEvents.remove(p_sListenerIdentifier);
	}
	
	/**
	 * Remove all pending events
	 */
	public void clearPendingEvents() {
		this.pendingEvents.clear();
	}
	
	/**
	 * Invoke success listeners
	 * @param p_sScreenIdentifier screen identifier
	 * @param p_oActionInterface action interface
	 * @param p_oInParam in parameter of the action
	 * @param p_oOutParam out parameter of the action
	 * @param p_oContext context
	 */
	protected void notifySuccess( String p_sScreenIdentifier,
			Class<? extends Action<?,?,?,?>> p_oActionInterface, 
			ActionParameter p_oInParam,
			ActionParameter p_oOutParam,
			Object p_oSource,
			MContext p_oContext ) {
		
		// Create event
		ListenerOnActionSuccessEvent<ActionParameter> oEvent = new ListenerOnActionSuccessEvent<ActionParameter>(
				p_oContext.getMessages().copy(), p_oOutParam, p_oSource);
		
		// Get running activity
		AbstractMMActivity oActivity = getRunningActivity(p_oInParam, p_sScreenIdentifier);
		
		if ( oActivity != null ) {
			// If activity is not finishing, we can invoke the listener, else do nothing
			if ( !oActivity.isFinishing()) {
				invokeSucessListener(p_oActionInterface, oEvent, oActivity);
			}
		}
		else {
			// Activity is null, should we store events ?
			if ( Application.getInstance().isStoreEventsEnabledForDisplay(p_sScreenIdentifier)) {
				// Store events for next start (configuration changes)	
				addPendingEvent(new EventHolder(oEvent, p_oActionInterface), p_sScreenIdentifier);
			}
		}
	}

	private void invokeSucessListener(
			Class<? extends Action<?,?,?,?>> p_oActionInterface,
			ListenerOnActionSuccessEvent<ActionParameter> p_oEvent,
			ListenerIdentifier p_oListenerIdentifier) {
		ClassAnalyse oClassAnalyse = Application.getInstance().getClassAnalyser().getClassAnalyse(p_oListenerIdentifier);
		Method oMethod = oClassAnalyse.getMethodOfClass(p_oActionInterface, MMActionTask.ActionTaskStep.END_SUCCESS, p_oEvent.getActionResult());
		if ( oMethod != null ) {
			this.invokeMethod(p_oListenerIdentifier, oMethod, p_oEvent);
		}
		// SMA A voir Pourquoi deux appels ???
		this.callListeners(p_oListenerIdentifier, p_oActionInterface, MMActionTask.ActionTaskStep.END_SUCCESS, p_oEvent);
	}

	/**
	 * Invoke error listeners
	 * @param p_sScreenIdentifier screen identifier
	 * @param p_oActionInterface action interface
	 * @param p_oInParam in parameter of the action
	 * @param p_oOutParam out parameter of the action
	 * @param p_oContext context
	 */
	protected void notifyFailure( String p_sScreenIdentifier,
			Class<? extends Action<?,?,?,?>> p_oActionInterface, 
			ActionParameter p_oInParam,
			ActionParameter p_oOutParam,
			Object p_oSource,
			MContext p_oContext, 
			ListenerOnActionFailEvent<ActionParameter> p_oErrorEvent) {
		
		ListenerOnActionFailEvent<ActionParameter> oEvent = null;
		if (p_oErrorEvent == null) {
			// Create event
			oEvent = new ListenerOnActionFailEvent<ActionParameter>(
				p_oContext.getMessages().copy(), p_oOutParam, p_oSource, null);
		} else {
			oEvent = p_oErrorEvent;
		}
		// Get running activity
		AbstractMMActivity oActivity = getRunningActivity(p_oInParam, p_sScreenIdentifier);
		
		if ( oActivity != null ) {
			// If activity is not finishing, we can invoke the listener, else do nothing
			if ( !oActivity.isFinishing()) {
				invokeFailureListener(p_oActionInterface, oEvent, oActivity, p_oContext);
			}
		}
		else {
			// Activity is null, should we store events ?
			if ( Application.getInstance().isStoreEventsEnabledForDisplay(p_sScreenIdentifier)) {
				// Store events for next start (configuration changes)	
				addPendingEvent(new EventHolder(oEvent, p_oActionInterface), p_sScreenIdentifier);
			}
		}
	}

	/**
	 * @param p_oActionInterface
	 * @param p_oEvent
	 * @param p_oListenerIdentifier
	 * @param p_oContext
	 */
	private void invokeFailureListener(
			Class<? extends Action<?,?,?,?>> p_oActionInterface,
			ListenerOnActionFailEvent<ActionParameter> p_oEvent,
			ListenerIdentifier p_oListenerIdentifier,
			MContext p_oContext ) {
		ClassAnalyse oClassAnalyse = Application.getInstance().getClassAnalyser().getClassAnalyse(p_oListenerIdentifier);
		//Cherche si l'activité est listener ...
		Method oMethod = oClassAnalyse.getMethodOfClass(p_oActionInterface, MMActionTask.ActionTaskStep.END_FAIL, p_oEvent.getActionResult());
		if ( oMethod != null ) {
			this.invokeMethod(p_oListenerIdentifier, oMethod, p_oEvent);
		}
		else if ( AbstractMMActivity.class.isAssignableFrom(p_oListenerIdentifier.getClass())) {
			((AbstractMMActivity) p_oListenerIdentifier).treatDefaultError(p_oContext.getMessages());
		}	
		//Cherche si d'autres objets (fragment par exemple) sont listeners
		this.callListeners(p_oListenerIdentifier, p_oActionInterface, MMActionTask.ActionTaskStep.END_FAIL, p_oEvent );
	}
	
	/**
	 * @param p_oActionInterface
	 * @param p_oStep
	 * @param p_oEvent
	 * @param p_oListenerIdentifier
	 */
	private <IN extends ActionParameter> void invokeProgressListener(
			Class<? extends Action<?,?,?,?>> p_oActionInterface,
			ActionStep p_oStep,
			ListenerOnProgressActionEvent<Object> p_oEvent,
			ListenerIdentifier p_oListenerIdentifier) {
		ClassAnalyse oClassAnalyse = Application.getInstance().getClassAnalyser().getClassAnalyse(p_oListenerIdentifier);
		Method oMethod = oClassAnalyse.getMethodOfClass(p_oActionInterface, p_oStep);
		if (oMethod != null ) {
			this.invokeMethod(p_oListenerIdentifier, oMethod, p_oEvent);
		}

		//Cherche si d'autres objets (fragment par exemple) sont listeners
		List<Object> listListener = Application.getInstance().getActionListeners(p_oActionInterface) ;
		if ( listListener != null) {
			ClassAnalyse oAnalyse ;
			for ( final Object oListener : listListener ){
				Object oRealListener = oListener;
				if (oListener instanceof String) {
					oRealListener = Application.getInstance().getScreenObjectFromName((String)oListener);
				}

				if (oRealListener != null) {
					oAnalyse = Application.getInstance().getClassAnalyser().getClassAnalyse(oRealListener);
					oMethod = oAnalyse.getMethodOfClass(p_oActionInterface, p_oStep);
					this.invokeMethod(oRealListener, oMethod, p_oEvent);
				}
			}
		}
	}
	
	/**
	 * call the listeners given as parameter
	 * @param p_oListenerIdentifier
	 * @param p_oActionInterface
	 * @param p_oActionTaskState
	 * @param p_oEvent
	 */
	private void callListeners(
			ListenerIdentifier p_oListenerIdentifier,
			Class<? extends Action<?,?,?,?>> p_oActionInterface,
			ActionTaskStep p_oActionTaskState,
			AbstractResultEvent<ActionParameter> p_oEvent) {
		List<Object> listListener = Application.getInstance().getActionListeners(p_oActionInterface) ;
		if ( listListener != null) {
			ClassAnalyse oAnalyse ;
			Method oMethod = null;
			for ( final Object oListener : listListener ){
				Object oRealListener = oListener;
				if (oListener instanceof String) {
					oRealListener = Application.getInstance().getScreenObjectFromName((String)oListener);
				}
				
				if (oRealListener != null) {
					oAnalyse = Application.getInstance().getClassAnalyser().getClassAnalyse(oRealListener);
					oMethod = oAnalyse.getMethodOfClass(p_oActionInterface, p_oActionTaskState, p_oEvent.getActionResult());
					this.invokeMethod(oRealListener, oMethod, p_oEvent);
				}
			}
		}
	}
	
	/**
	 * Invoke une méthode de l'écran avec le paramètre p_oParameters
	 * @param p_oObject l'objet contenant la méthode à invoquer
	 * @param p_oMethod la méthode à invoker
	 * @param p_oParameters le paramètre à passer
	 */
	private void invokeMethod(Object p_oObject , Method p_oMethod, Object p_oParameters) {
		if (p_oMethod!=null) {
			try {
				Object oReference = p_oObject;
				if (ListenerDelegate.class.isAssignableFrom(p_oMethod.getDeclaringClass())) {
					oReference = ((ListenerDelegator) p_oObject).getListenerDelegate();
				}
				
				p_oMethod.invoke(oReference, p_oParameters);
			} catch (IllegalArgumentException e) {
				Application.getInstance().getLog().error(Application.LOG_TAG, "méthode " + p_oMethod.getName() + ": invocation impossible with " + p_oParameters.getClass().getName() , e);
			} catch (IllegalAccessException | InvocationTargetException e) {
				Application.getInstance().getLog().error(Application.LOG_TAG, "méthode " + p_oMethod.getName() + ": invocation impossible", e);
			}
		}
	}
	
	/**
	 * Add a event to be replayed on activity restart
	 * @param p_oEventHolder event
	 * @param p_sListenerName listener name
	 */
	private void addPendingEvent( EventHolder p_oEventHolder, String p_sListenerName) {
		if (p_sListenerName != null) {
			Queue<EventHolder> oQueue = this.pendingEvents.get(p_sListenerName);
			if (oQueue == null) {
				oQueue = new LinkedList<>();
				this.pendingEvents.put(p_sListenerName, oQueue);
			}
			oQueue.add(p_oEventHolder);
		}
	}
	
	
	/**
	 * Add a event to be replayed on activity restart
	 * @param p_oInParam
	 * @param p_sScreenIdentifier
	 */
	private AbstractMMActivity getRunningActivity(ActionParameter p_oInParam, String p_sScreenIdentifier) {
		// Get running activity
		AbstractMMActivity oActivity = (AbstractMMActivity) Application.getInstance().getScreenObjectFromName(p_sScreenIdentifier);
	
		// Search Activity Active
		if (p_oInParam != null && p_oInParam.isActionAttachedActivity()) {
			ListenerIdentifier oListenerIdentifier = Application.getInstance().getScreenObjecthasWindowFocus();
			if (oListenerIdentifier !=  null) {
				oActivity = (AbstractMMActivity)oListenerIdentifier;
			}
		}
		
		return oActivity;
	}

	/**
	 * Holder to hold event in the pending queue
	 *
	 */
	private static class EventHolder {
		
		/**
		 * Event
		 */
		private Object event;
		
		/**
		 * Action interface
		 */
		private Class<? extends Action<?,?,?,?>> actionInterface;
		
		/**
		 * Step (for progress event only)
		 */
		private ActionStep step;
		
		/**
		 * Constructor
		 * @param event event
		 * @param actionInterface action interface
		 */
		public EventHolder(Object event, Class<? extends Action<?, ?, ?, ?>> actionInterface) {
			super();
			this.event = event;
			this.actionInterface = actionInterface;
		}
		
		/**
		 * Constructor
		 * @param p_oEvent event
		 * @param p_oActionInterface action interface
		 * @param p_oStep step
		 */
		public EventHolder(Object p_oEvent, Class<? extends Action<?, ?, ?, ?>> p_oActionInterface, ActionStep p_oStep) {
			super();
			this.event = p_oEvent;
			this.actionInterface = p_oActionInterface;
			this.step = p_oStep;
		}	
	}
}
