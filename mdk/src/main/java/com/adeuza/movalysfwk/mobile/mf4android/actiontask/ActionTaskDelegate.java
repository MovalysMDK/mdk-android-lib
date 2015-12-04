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

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.MFApplicationHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskIn;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskOut;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFailEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ActionDialogFactory;

/**
 * <p>Action Task Delegate</p>
 *
 *
 * @param <IN> IN action parameter
 * @param <OUT> OUT action parameter
 * @param <STATE> action step
 * @param <PROGRESS> action progress
 *
 */
public class ActionTaskDelegate<IN extends ActionParameter, OUT extends ActionParameter, STATE extends ActionStep, PROGRESS extends Object> 
		implements MMAndroidActionTask<IN,OUT,STATE,PROGRESS> {

	/** l'action associée de départ */
	private AbstractTaskableAction<IN,OUT,STATE,PROGRESS> action;

	
	/** l'interface de l'action de départ*/
	private Class<? extends Action<?,?,?,?>> actionInterface; 

	/** permet de synchroniser le dialog : pour éviter les problèmes à la rotation des écrans */
	private Object lock = new Object();
	/** le context courrant */
	private MContext context;
	/** in parameter */
	private ActionTaskIn<IN> parameterIn;
	/** notifier */
	private Notifier notifier = null;

	/**
	 * timeout for waiting task notify
	 */
	private static final int TASK_WAIT_TIMEOUT = 60000;


	private static final int WAKE_LOCK_PERMISSION = 990;
	
	/**
	 * input action parameter
	 */
	private IN actionParameterIn ;

	/**
	 * wakeLock pour empècher le mobile de s'éteindre lors de la synchronisation 
	 */
	private PowerManager.WakeLock wakelock;

	/**
	 * android action task
	 */
	private MMAndroidActionTask<IN, OUT, STATE, PROGRESS> delegator;

	/**
	 * Identifier of screen that has started the action (may be null)
	 */
	private String screenIdentifier;

	/**
	 * Denied permission.
	 */
	private String[] mDeniedPermissions;
	
	/**
	 * Construit une tâche
	 * @param p_oActionTask android action task
	 */
	public ActionTaskDelegate(MMAndroidActionTask<IN, OUT, STATE, PROGRESS> p_oActionTask) {
		this.notifier = new Notifier();
		this.delegator = p_oActionTask;
	}

	/**
	 * Retourne l'objet context
	 * @return Objet context
	 */
	@Override
	public MContext getContext() {
		return this.context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractMMActivity getParent() {
		AbstractMMActivity oActivity = (AbstractMMActivity) Application.getInstance().getScreenObjectFromName(this.screenIdentifier);
		return oActivity != null && !oActivity.isFinishing() ? oActivity : null;
	}
	
	/**
	 * Retourne l'objet notifier
	 * @return Objet notifier
	 */
	@Override
	public Notifier getNotifier() {
		return this.notifier;
	}

	/**
	 * Construit une nouvelle tâche asynchrone
	 * {@inheritDoc}
	 * @param p_sScreenIdentifier screen identifier
	 * @param p_oAction action
	 * @param p_oActionInterface action class
	 * @param p_oParameterIn input parameter
	 * @param p_oAnalyse class analyse
	 */
	@Override
	public void init(String p_sScreenIdentifier, 
			Action<IN, OUT, STATE, PROGRESS> p_oAction, 
			Class<? extends Action<IN, OUT, STATE, PROGRESS>> p_oActionInterface, 
			IN p_oParameterIn, ClassAnalyse p_oAnalyse) {
		this.action = (AbstractTaskableAction<IN, OUT, STATE, PROGRESS>) p_oAction;
		this.actionInterface = p_oActionInterface;
		this.screenIdentifier = p_sScreenIdentifier;
		this.actionParameterIn = p_oParameterIn ;
	}
	
	/**
	 * Permet de créer un dialog de progression.
	 */
	@Override
	public void createProgressDialog() {
		if (this.action.getConcurrentAction() != Action.NO_QUEUE) {
			synchronized (lock) {
				BeanLoader.getInstance().getBean(ActionDialogFactory.class).createProgressDialog(
						this.context, this);
			}
		}
	}

	/**
	 * Masque le dialogue de progression
	 */
	@Override
	public void dismissProgressDialog() {
		if (this.action.getConcurrentAction() != Action.NO_QUEUE) {
			synchronized (lock) {
				BeanLoader.getInstance().getBean(ActionDialogFactory.class).destroyProgressDialog(this);
			}
		}
	}

	/**
	 * Retourne l'objet action
	 * @return Objet action
	 */
	@Override
	public AbstractTaskableAction<IN, OUT,STATE, PROGRESS> getAction() {
		return this.action;
	}

	/**
	 * Retourne l'objet parameterIn
	 * @return Objet parameterIn
	 */
	@Override
	public ActionTaskIn<IN> getParameterIn() {
		return this.parameterIn;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask#setParameterIn(com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskIn)
	 */
	@Override
	public void setParameterIn(ActionTaskIn<IN> p_oParameterIn) {
		this.parameterIn = p_oParameterIn;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask#execAction()
	 */

	@Override
	public void execAction() {
		try {
			//Création du context, début de la transaction
			MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
			this.context = (MContext) oCf.createContextFor(this.action);

			this.context.beginTransaction();
			
			AbstractMMActivity oActivity = (AbstractMMActivity) getParent();
			if (oActivity != null && this.action.hasPreExecuteDialog(this.context, this.parameterIn.getIn())) {
				BeanLoader.getInstance().getBean(ActionDialogFactory.class)
						.createPreExecuteDialog(this.context, oActivity, this);

				this.context.endTransaction();
				this.context.close();
			}
			else {
				this.context.endTransaction();
				this.context.close();
				this.delegator.execAction(this.parameterIn);
			}
		}
		catch(Exception e) {
			Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			this.context.getMessages().addMessage(ExtFwkErrors.LaunchActionError);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execAction(ActionTaskIn<IN> p_oParameterIn) {
		this.onPreExecute();
		ActionTaskOut<OUT> r_oResult = this.doInBackground(p_oParameterIn);
		this.onPostExecute(r_oResult);
	}

	/**
	 * <p>This method is called on main thread before the doInBackground method.</p>
	 * <p>The following things are done :</p>
	 * <ul>
	 * 	<li>A wake lock is acquired for synchronization action.</li>
	 *  <li>Show progress dialog depending on the configuration.</li>
	 *  <li>doPreExecuteAction of the method is invoked.</li>
	 * </ul>
	 */
	protected void onPreExecute() {
		if (SynchronizationAction.class.isAssignableFrom(this.actionInterface)) {
			PowerManager oPowerManage = (PowerManager) MFApplicationHolder.getInstance().getApplication().getApplicationContext().getSystemService(Context.POWER_SERVICE);
			//TODO: From API 17, FLAG_KEEP_SCREEN_ON must replace SCREEN_DIM_WAKE_LOCK
			wakelock = oPowerManage.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
			wakelock.acquire();
		}

		AbstractMMActivity oActivity = getParent();
		if ( oActivity != null ) {
			//Affichage de la popup de progression
			oActivity.setProgressBarIndeterminateVisibility(true);
		}
		
		// Show progress dialog
		this.createProgressDialog();

		try {			
			//Exécution du preExecute de l'action
			this.action.doPreExecute(this.actionParameterIn, this.context);
		}
		catch(Exception e) {
			Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			this.context.getMessages().addMessage(ExtFwkErrors.LaunchActionError);
		}
	}

	/**
	 * do in background
	 * @param p_oIns IN action tasks
	 * @return OUT action task
	 */
	protected ActionTaskOut<OUT> doInBackground(ActionTaskIn<IN>... p_oIns) {
		//
		// !!!
		// La transaction SQL doit être commencée est terminée dans le même thread, dans notre cas pas dans le thread background
		// !!!
		//
		try {
			this.context.beginTransaction(); 
			if (p_oIns.length>0) {
				this.parameterIn = p_oIns[0];
			}
			return Application.getInstance().getController().launchActionByActionTask(this.delegator);
		}
		catch(Exception e) {
			Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			this.context.getMessages().addMessage(ExtFwkErrors.LaunchActionError);
		}
		finally {
			this.context.endTransaction();
			this.context.close();
		}

		return null;
	}

	@Override
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask#publishActionProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress<STATE,PROGRESS>[])
	 */
	public void publishActionProgress(ActionTaskProgress<STATE,PROGRESS>[] p_oProgressValues) {
		this.onProgressUpdate(p_oProgressValues);
	}

	/**
	 * Publish a runnable progress
	 * @param p_oRunnable runnable to execute in UI thread
	 * @param p_bBlocking if true, wait runnable to finish
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void publishActionRunnableProgress( Runnable p_oRunnable, boolean p_bBlocking ) {
		ActionTaskProgress<MMActionTask.ActionTaskStep,Object> oProgress 
			= new ActionTaskProgress<>();
		oProgress.setStep(MMActionTask.ActionTaskStep.PROGRESS_RUNNABLE);
		Object[] oRuns = new Object[1];
		oRuns[0] = p_oRunnable;	
		oProgress.setValue(oRuns);

		@SuppressWarnings("rawtypes")
		ActionTaskProgress[] t_listActionProgresses = new ActionTaskProgress[] { oProgress };
		
		if ( p_bBlocking ) {
			synchronized (oRuns[0]) {
				this.delegator.publishActionProgress(t_listActionProgresses);
				try {
					oRuns[0].wait(ActionTaskDelegate.TASK_WAIT_TIMEOUT);
				} catch (InterruptedException e) { 
					Log.d("","InterruptedException");
				}
			}
		}
		else {
			this.delegator.publishActionProgress(t_listActionProgresses);
		}
	}

	/**
	 * <p>This method is invoked on the main thread after the doInBackground method.</p>
	 * <p>The following things are done :</p>
	 * <ul>
	 * 	 <li>doPostExecute of the action is invoked inside a new transaction.</li>
	 *   <li>The busy status on the controller is released. Actions in the pending queue of the controller may be started.</li>
	 *   <li>Action listeners are notified.</li>
	 *   <li>The progress dialog is closed depending on the configuration.</li>
	 *   <li>The acquired wakelock for synchronization action is released.</li>
	 * </ul>
	 * @param p_oResult action task out
	 */
	protected void onPostExecute(ActionTaskOut<OUT> p_oResult) {
		OUT parameterOut = null;
		if (p_oResult!=null) {
			parameterOut = p_oResult.getOut();
		}

		// LMI Correction DataBase is locked
		this.context.beginTransaction();
		try {
			this.action.doPostExecute(this.context, this.parameterIn.getIn(), parameterOut);
		}
		catch(ActionException e) {
			Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			this.context.getMessages().addMessage(ExtFwkErrors.LaunchActionError);
		} 
		finally {
			
			// Release busy status starts actions from the queue, so the following things must be done before :
			// - the context must be closed.
			// - the action must be unregistered.
			
			// Close the context
			this.context.endTransaction();
			this.context.close();
			
			// Unregister the action from the active display
			Application.getInstance().unregisterRunningAction(this.screenIdentifier, ((AbstractTaskableAction)this.getAction()).getHandler());
//			this.parameterIn.setSource(null);
			
			// Release busy status
			Application.getInstance().getController().releaseBusyStatus( this.context , this.action);
		}
		ListenerOnActionFailEvent<ActionParameter> oErrorEvent = null;
		if (mDeniedPermissions != null) {
			oErrorEvent = new ListenerOnActionFailEvent<ActionParameter>(
					this.context.getMessages().copy(), 
					parameterOut, 
					this.parameterIn.getSource(),
					this.mDeniedPermissions);
		}
		// Notify listeners of action (must be done after releaseBusyStatus)
		((AndroidApplication)Application.getInstance()).getActionListenerHandler().notifyFinishListener(
				this.screenIdentifier, this.actionInterface, this.parameterIn.getIn(), parameterOut, this.parameterIn.getSource(), this.context, oErrorEvent );
		
		// Update progress bar
		AbstractMMActivity oActivity = (AbstractMMActivity) getParent();
		if ( oActivity != null ) {
			oActivity.setProgressBarIndeterminateVisibility(false);
		}
		
		// Close dialog
		this.dismissProgressDialog();
		
		// Clean (this.action is needed to dismiss dialog)
		this.action.setCurrentHandler(null);
		this.action = null;
		
		// Release lock for synchronization action
		if (SynchronizationAction.class.isAssignableFrom(this.actionInterface)) {
			if ( this.wakelock != null && this.wakelock.isHeld()) {
				this.wakelock.release();
			}
		}
	}

	/**
	 * Permet d'exécuter du code dans le thread ui et de publier la progression de l'action
	 * @param p_oProgressValues les informations de progression
	 * @param <IN2> action parameter
	 * @param <OUT2> out action parameter
	 * @param <STATE2> action state
	 * @param <PROGRESS2> action progress
	 */
	@SuppressWarnings("unchecked")
	public <IN2 extends ActionParameter, OUT2 extends ActionParameter, STATE2 extends ActionStep, PROGRESS2 extends Object> void onProgressUpdate(ActionTaskProgress<STATE, PROGRESS>... p_oProgressValues) {
		MContext oContext = this.getContext();
		try {
			if(p_oProgressValues.length>0) {
				ActionTaskProgress<STATE, PROGRESS> oInfo = p_oProgressValues[0];
				if (oInfo.getStep().equals(MMActionTask.ActionTaskStep.PRE_EXECUTE)) { //invoker par le FWkCOntrollerImpl
					Action<IN,OUT2,STATE2,PROGRESS2> oAction = (Action<IN,OUT2,STATE2,PROGRESS2>) (oInfo.getValue()[0]); // ce n'est pas forcément l'action courante : ca peut être une action chaînée
					
					oAction.doPreExecute(this.actionParameterIn, oContext);
				}
				else if (oInfo.getStep().equals(MMActionTask.ActionTaskStep.POST_EXECUTE)) { //invoker par le FWkCOntrollerImpl
					Object[] aValues = (Object[]) oInfo.getValue();
					Action<IN2,OUT2,STATE2,PROGRESS2> oAction = (Action<IN2, OUT2, STATE2, PROGRESS2>) aValues[0]; // ce n'est pas forcément l'action courante : ca peut être une action chaînée
					oAction.doPostExecute(oContext, (IN2)aValues[1], (OUT2)aValues[2]);
				}
				else if (oInfo.getStep().equals(MMActionTask.ActionTaskStep.PROGRESS_RUNNABLE)) { //invoker par le FWkCOntrollerImpl
					((Runnable)oInfo.getValue()[0]).run();
				}
				else {
					((AndroidApplication)Application.getInstance()).getActionListenerHandler().notifyProgressListeners(
							this.screenIdentifier, this.actionInterface, this.parameterIn.getIn(), oInfo, this.context);
				}
			}
		}
		catch(Exception e) {
			Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			oContext.getMessages().addMessage(ExtFwkErrors.LaunchActionError);
		}
	}
	
	/**
	 * Get screen identifier
	 * @return screen identifier
	 */
	public String getScreenIdentifier() {
		return screenIdentifier;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isProgressDialogDisabled() {
		return this.actionParameterIn != null && this.actionParameterIn.isProgressDialogDisabled();
	}

	@Override
	public void execDeniedPermission(String[] p_oDeniedPermission) throws ActionException {
		//Création du context, début de la transaction
		MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
		this.context = (MContext) oCf.createContextFor(this.action);
		this.context.getMessages().addMessage(ExtFwkErrors.ActionError, "user denied permission");
		this.mDeniedPermissions = p_oDeniedPermission.clone();
		this.action.doOnError(this.context, null, null);
		this.onPostExecute(null);
	}
}
