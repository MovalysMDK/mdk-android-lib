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

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskIn;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskOut;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Action Async Task</p>
 *
 * @param <IN> Action parameter
 * @param <OUT> Action parameter
 * @param <STATE> Action step
 * @param <PROGRESS> progress object
 *
 */
public class ActionASyncTask <IN extends ActionParameter, OUT extends ActionParameter,STATE extends ActionStep,PROGRESS extends Object> 

		extends AsyncTask<ActionTaskIn<IN>, ActionTaskProgress<STATE, PROGRESS>, ActionTaskOut<OUT>>
		implements 
			MMAndroidActionTask<IN,OUT,STATE,PROGRESS>, 
			DialogInterface.OnClickListener {

	/**
	 * action task delegate
	 */
	private ActionTaskDelegate<IN, OUT, STATE, PROGRESS> androidActionTaskDelegate;

	/**
	 * initial priority
	 */
	private int initialPriority;

	/**
	 * Constructor
	 */
	public ActionASyncTask() {
		super();
		this.androidActionTaskDelegate = new ActionTaskDelegate<>(this);
	}

	@Override
	public void onClick(DialogInterface p_oDialog, int p_oParamInt) {
		try {
			p_oDialog.dismiss();
			this.execAsyncTask(this.getParameterIn());
		}
		catch(Exception e) {
			Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			this.getContext().getMessages().addMessage(ExtFwkErrors.LaunchActionError);
		}
	}
	
	@Override
	public void execAction(ActionTaskIn<IN> p_oParameterIn) {
		this.execAsyncTask( p_oParameterIn);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask#publishActionProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress<STATE,PROGRESS>[])
	 */
	@Override
	public void publishActionProgress(ActionTaskProgress<STATE,PROGRESS>[] p_oProgressValues) {
		this.publishProgress(p_oProgressValues);
	}


	/**
	 * Execute la tache asynchrone en parametre avec les parametres en parametre
	 * Cette méthode permet d'avoir la même execution sur les version superieur à 
	 * HONEYCOMB que l'execution à partir de DONUT (execution des taches asynchrones 
	 * sur plusieurs threads)
	 * 
	 * @param p_oParams les parametres de la tache
	 */
	public void execAsyncTask( ActionTaskIn<IN>... p_oParams) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,p_oParams);
		} else {
			this.execute(p_oParams);
		}
	}

	
	// ------------------------------
	// ----- DELEGATED+INHERITED ---------------



	/**
	 * {@inheritDoc}
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(ActionTaskProgress<STATE, PROGRESS>... p_oProgressValues) {
		super.onProgressUpdate(p_oProgressValues);
		this.androidActionTaskDelegate.onProgressUpdate(p_oProgressValues);
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.androidActionTaskDelegate.onPreExecute();
		this.initialPriority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected ActionTaskOut<OUT> doInBackground(ActionTaskIn<IN>... p_oParams) {
		final int iInitialPriority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		ActionTaskOut<OUT> r_oResult = this.androidActionTaskDelegate.doInBackground(p_oParams);

		Thread.currentThread().setPriority(iInitialPriority);
		return r_oResult;
	}


	/**
	 * {@inheritDoc}
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ActionTaskOut<OUT> p_oResult) {
		Thread.currentThread().setPriority(this.initialPriority);
		super.onPostExecute(p_oResult);
		this.androidActionTaskDelegate.onPostExecute(p_oResult);
	}
	
// ------------------------------
// ----- DELEGATED ---------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionTaskIn<IN> getParameterIn() {
		return this.androidActionTaskDelegate.getParameterIn();
	}
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
	public Screen getParent() {
		return this.androidActionTaskDelegate.getParent();
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
	public void init(
			String p_sScreenIdentifier,
			Action<IN, OUT, STATE, PROGRESS> p_oAction,
			Class<? extends Action<IN, OUT, STATE, PROGRESS>> p_oActionInterface,
			IN p_oParameterIn, ClassAnalyse p_oAnalyse) {

		this.androidActionTaskDelegate.init(p_sScreenIdentifier, p_oAction, p_oActionInterface, p_oParameterIn, p_oAnalyse);
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
	public void createProgressDialog() {
		this.androidActionTaskDelegate.createProgressDialog();
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void dismissProgressDialog() {
		this.androidActionTaskDelegate.dismissProgressDialog();
	}

	/**
	 * Publish a runnable progress
	 * @param p_oRunnable runnable to execute in UI thread
	 * @param p_bBlocking if true, wait runnable to finish
	 */
	@Override
	public void publishActionRunnableProgress(Runnable p_oRunnable, boolean p_bBlocking) {
		this.androidActionTaskDelegate.publishActionRunnableProgress(p_oRunnable, p_bBlocking);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractTaskableAction<IN, OUT, STATE, PROGRESS> getAction() {
		return this.androidActionTaskDelegate.getAction();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScreenIdentifier() {
		return this.androidActionTaskDelegate.getScreenIdentifier();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isProgressDialogDisabled() {
		return this.androidActionTaskDelegate.isProgressDialogDisabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execDeniedPermission(String[] p_oDeniedPermission) throws ActionException {
		this.androidActionTaskDelegate.execDeniedPermission(p_oDeniedPermission);
	}
}
