package com.adeuza.movalysfwk.mobile.mf4android.actiontask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractWorkspaceAutoBindMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskIn;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskOut;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.AbstractResultEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFailEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnProgressActionEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerDelegator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkControllerImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ActionDialogFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ProgressDialog;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Action Task Delegate</p>
 *
 * <p>Copyright (c) 2014
 *
 * @author Sopra, pole mobilite (Nantes)
 * @param <IN> IN action parameter
 * @param <OUT> OUT action parameter
 * @param <STATE> action step
 * @param <PROGRESS> action progress
 *
 */
public class ActionTaskDelegate<IN extends ActionParameter, OUT extends ActionParameter, STATE extends ActionStep, PROGRESS extends Object> 
		implements MMAndroidActionTask<IN,OUT,STATE,PROGRESS> {

	/** l'action associée de départ */
	private AbstractTaskableAction<IN,OUT,STATE,PROGRESS> action = null;

	
	/** l'interface de l'action de départ*/
	private Class<? extends Action<?,?,?,?>> actionInterface = null; 

	/** l'activité parente */
	private AbstractMMActivity parent = null;
	/** dialog */
	private ProgressDialog dialog = null;

	/** has dialog ? */
	private boolean hasDialog;

	/** permet de synchroniser le dialog : pour éviter les problèmes à la rotation des écrans */
	private Object lock = new Object();
	/** le context courrant */
	private AndroidContextImpl context = null;
	/** in parameter */
	private ActionTaskIn<IN> parameterIn = null;
	/** notifier */
	private Notifier notifier = null;
	/** l'analyse de la class de l'écran sur lequel l'action est lancée */
	private ClassAnalyse analyse = null;

	/** durée de la pause lors de l'exec */
	private static final int SLEEPING_DURATION = 50 ;

	/**
	 * timeout for waiting task notify
	 */
	private static final int TASK_WAIT_TIMEOUT = 60000;
	
	/**
	 * input action parameter
	 */
	private IN actionParameterIn ;

	/**
	 * wakeLock pour empècher le mobile de s'éteindre lors de la synchronisation 
	 */
	private PowerManager.WakeLock wakelock = null;

	/**
	 * android action task
	 */
	private MMAndroidActionTask<IN, OUT, STATE, PROGRESS> delegator;

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
	 * @param p_oParent screen
	 * @param  p_oAction action
	 * @param p_oActionInterface action class
	 * @param p_oParameterIn input parameter
	 * @param p_oAnalyse class analyse
	 */
	@Override
	public void init(Screen p_oParent, 
			Action<IN, OUT, STATE, PROGRESS> p_oAction, 
			Class<? extends Action<IN, OUT, STATE, PROGRESS>> p_oActionInterface, 
			IN p_oParameterIn, ClassAnalyse p_oAnalyse) {
		this.action = (AbstractTaskableAction<IN, OUT, STATE, PROGRESS>) p_oAction;
		this.actionInterface = p_oActionInterface;
		this.parent = (AbstractMMActivity)p_oParent;
		this.analyse = p_oAnalyse;
		this.actionParameterIn = p_oParameterIn ;
	}
	/**
	 * Permet de créer un dialog de progression.
	 */
	@Override
	public void createProgressDialog() {
		synchronized (lock) {
			this.dialog = BeanLoader.getInstance().getBean(ActionDialogFactory.class).createProgressDialog(this.context, this.parent, this);
			this.hasDialog = (this.dialog != null);

		}
	}

	/**
	 * Masque le dialogue de progression
	 */
	public void dismissProgressDialog() {
		synchronized (lock) {
			if (this.dialog!=null) {
				this.dialog.close();
				this.dialog = null;
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
	 * {@inheritDoc}
	 */
	@Override
	public void refresh(Screen p_oScreen) {
		this.parent = (AbstractMMActivity) p_oScreen;
		this.createProgressDialog();
	}

	/**
	 * clear the dialog
	 */
	public void clear() {
		synchronized (lock) {
			if (this.dialog != null) {
				this.dialog.close();
				this.dialog = null;
			}
		}
		this.parent = null;
	}

	/**
	 * Affecte l'objet parent 
	 * @param p_oParent Objet parent
	 */
	public void setParent(AbstractMMActivity p_oParent) {
		if (p_oParent == null && this.dialog != null) {
			this.dialog.close();
			this.dialog = null;
		}
		this.parent = p_oParent;
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
			this.context = (AndroidContextImpl) oCf.createContext();
			((FwkControllerImpl)Application.getInstance().getController()).setCurrentContext(this.context);

			this.context.beginTransaction();
			if (this.action.hasPreExecuteDialog(this.context, this.parameterIn.getIn())) {
				BeanLoader.getInstance().getBean(ActionDialogFactory.class)
						.createPreExecuteDialog(this.context, this.parent, this);

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
	 * {@inheritDoc}
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	protected void onPreExecute() {
		if (SynchronizationAction.class.isAssignableFrom(this.actionInterface)) {
			PowerManager oPowerManage = (PowerManager) this.parent.getSystemService(Context.POWER_SERVICE);
			wakelock = oPowerManage.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
			wakelock.acquire();
		}

		if (this.parent instanceof AbstractWorkspaceAutoBindMMActivity){
			((AbstractWorkspaceAutoBindMMActivity)this.parent).getWlayout().setWillNotDraw(true);
		}
		
		//Affichage de la popup de progression
		this.parent.setProgressBarIndeterminateVisibility(true);
		this.createProgressDialog();

		//Création du context, début de la transaction
		try {
//			MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
//			this.context = oCf.createContext();
//			((FwkControllerImpl)Application.getInstance().getController()).setCurrentContext(this.context);

			//
			//!!! Attention à ne pas créer de connexion dans le thread ui
			//
			
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
			return ((FwkControllerImpl)Application.getInstance().getController()).launchActionByActionTask(this.delegator);
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
	 * on post execute
	 * @param p_oResult action task out
	 */
	protected void onPostExecute(ActionTaskOut<OUT> p_oResult) {
		OUT parameterOut = null;
		if (p_oResult!=null) {
			parameterOut = p_oResult.getOut();
		}
		Object parameterInSrc = this.parameterIn.getSource();


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
			this.context.endTransaction();
			this.context.close();
			Application.getInstance().getController().releaseBusyStatus( this.context , this.action);
		}
		
		// SMA : il peut y avoir un problème lors du traitement des rotations, en effet pendant un laps de temps le dialog peut être null :
		// pour palier une anomalie android présente Android SDK 2.2 le dialog de progression est reconstruit à chaque progression
		// pour éviter que le dialog soit null lors de la fermeture, et/ou de ne pas le fermer
		// les accès au dialog sont synchronisés et on attend que le dialog ne soit pas null pour agir.
		try {
			while(this.hasDialog && this.dialog==null) {
					Thread.sleep(SLEEPING_DURATION);
			}
		} catch (InterruptedException e) {
			//Nothing to do
			Log.d("AndroidActionTask","InterruptedException Thread.sleep(SLEEPING_DURATION) ");
		}
		this.dismissProgressDialog();
		this.parent.setProgressBarIndeterminateVisibility(false);

		AbstractResultEvent<ActionParameter> oEvent;

		//il faut réveiller les méthodes sur l'écran
		if (this.context.getMessages().hasErrors()) {
			oEvent = new ListenerOnActionFailEvent<ActionParameter>(this.context.getMessages().copy(), parameterOut, parameterInSrc);
			Method oMethod = this.analyse.getMethodOfClass(this.actionInterface, MMActionTask.ActionTaskStep.END_FAIL);
			if (oMethod != null) {
				callListeners(MMActionTask.ActionTaskStep.END_FAIL, oEvent, oMethod);
			}
			else {
				//traitement par défaut : affiche un dialog
				this.parent.treatDefaultError(this.context.getMessages());
			}
		}
		else {
			oEvent = new ListenerOnActionSuccessEvent<ActionParameter>(this.context.getMessages().copy(), parameterOut, parameterInSrc);
			Method oMethod = this.analyse.getMethodOfClass(this.actionInterface, MMActionTask.ActionTaskStep.END_SUCCESS);
			callListeners(MMActionTask.ActionTaskStep.END_SUCCESS, oEvent, oMethod);
		}
		

		if (this.parent instanceof AbstractWorkspaceAutoBindMMActivity){
			((AbstractWorkspaceAutoBindMMActivity)this.parent).getWlayout().setWillNotDraw(false);
		}

		if (SynchronizationAction.class.isAssignableFrom(this.actionInterface)) {
			this.wakelock.release();
		}
	}

	/**
	 * @param p_oActionTaskState
	 * @param p_oEvent
	 * @param p_oMethod
	 */
	private void callListeners(ActionTaskStep p_oActionTaskState, AbstractResultEvent<ActionParameter> p_oEvent,
			Method p_oMethod) {
		this.invokeMethod(this.parent,p_oMethod, p_oEvent);
		ClassAnalyse oAnalyse ;
		List<Object> listListener = Application.getInstance().getActionListeners(this.actionInterface) ;
		if ( listListener != null) {
			for ( final Object oListener : listListener ){
				Object oRealListener = oListener;
				if (oListener instanceof String) {
					oRealListener = Application.getInstance().getScreenObjectFromName((String)oListener);
				}
				
				if (oRealListener != null) {
					oAnalyse = Application.getInstance().getClassAnalyser().getClassAnalyse(oRealListener);
					p_oMethod = oAnalyse.getMethodOfClass(this.actionInterface, p_oActionTaskState);
					this.invokeMethod(oRealListener ,p_oMethod, p_oEvent);
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
				//TODO SMA : plus faire tomber en erreur pour afficher la popup + ajouter la classe, et le type de paramètre attendu
				Log.e(Application.LOG_TAG, "méthode " + p_oMethod.getName() + ": invocation impossible with " + p_oParameters.getClass().getName() , e);
			} catch (IllegalAccessException e) {
				Log.e(Application.LOG_TAG, "méthode " + p_oMethod.getName() + ": invocation impossible", e);
			} catch (InvocationTargetException e) {
				Log.e(Application.LOG_TAG, "méthode " + p_oMethod.getName() + ": invocation impossible", e);
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
					Method oMethod = this.analyse.getMethodOfClass(this.actionInterface, oInfo.getStep());
					if (oMethod != null) {
						//on est dans le cas d'une progression
						ListenerOnProgressActionEvent<Object> oEvent = new ListenerOnProgressActionEvent<Object>(oContext.getMessages().copy(), oInfo.getValue());
						this.invokeMethod(this.parent ,oMethod, oEvent);
					}
					
				}
			}
		}
		catch(Exception e) {
			Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			oContext.getMessages().addMessage(ExtFwkErrors.LaunchActionError);
		}
	}
}
