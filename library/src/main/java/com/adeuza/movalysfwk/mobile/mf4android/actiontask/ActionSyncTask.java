package com.adeuza.movalysfwk.mobile.mf4android.actiontask;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskIn;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;
/**
 * <p>Action Sync Task</p>
 *
 * <p>Copyright (c) 2014
 *
 * @author Sopra, pole mobilite (Nantes)
 * @param <IN> Action parameter
 * @param <OUT> Action parameter
 * @param <STATE> Action step
 * @param <PROGRESS> progress object
 *
 */
public class ActionSyncTask<IN extends ActionParameter, OUT extends ActionParameter, STATE extends ActionStep,PROGRESS extends Object> 
implements MMAndroidActionTask<IN,OUT,STATE,PROGRESS>  {

	/**
	 * action task delegate
	 */
	private ActionTaskDelegate<IN, OUT, STATE, PROGRESS> androidActionTaskDelegate;

	/**
	 * Construit une tâche
	 */
	public ActionSyncTask() {
		this.androidActionTaskDelegate = new ActionTaskDelegate<>(this);
	}


	// ------------------------------
	// ----- DELEGATED ---------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameterIn(ActionTaskIn<IN> p_oParameterIn) {
		this.androidActionTaskDelegate.setParameterIn(p_oParameterIn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MContext getContext() {
		return this.androidActionTaskDelegate.getContext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Notifier getNotifier() {
		return this.androidActionTaskDelegate.getNotifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(Screen p_oParent, Action<IN, OUT, STATE, PROGRESS> p_oAction,
			Class<? extends Action<IN, OUT, STATE, PROGRESS>> p_oActionInterface,
			IN p_oParameterIn, ClassAnalyse p_oAnalyse) {

		this.androidActionTaskDelegate.init(p_oParent, p_oAction, p_oActionInterface, p_oParameterIn, p_oAnalyse);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execAction() {
		this.androidActionTaskDelegate.execAction();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execAction(ActionTaskIn<IN> p_oParameterIn) {
		this.androidActionTaskDelegate.execAction(p_oParameterIn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void publishActionProgress(ActionTaskProgress<STATE, PROGRESS>... p_oProgressValues) {
		this.androidActionTaskDelegate.publishActionProgress(p_oProgressValues);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void publishActionRunnableProgress(Runnable p_oRunnable, boolean p_bBlocking) {
		this.androidActionTaskDelegate.publishActionRunnableProgress(p_oRunnable, p_bBlocking);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createProgressDialog() {
		this.androidActionTaskDelegate.createProgressDialog();
	}

	/**
	 * {@inheritDoc}
	 * Do nothing
	 */
	@Override
	public void refresh(Screen p_oScreen) {
		this.androidActionTaskDelegate.refresh(p_oScreen);
	}


	@Override
	public AbstractTaskableAction<IN, OUT, STATE, PROGRESS> getAction() {
		return this.androidActionTaskDelegate.getAction();
	}


	@Override
	public ActionTaskIn<IN> getParameterIn() {
		// TODO Auto-generated method stub
		return this.androidActionTaskDelegate.getParameterIn();
	}
}
