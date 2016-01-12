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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
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
 *
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
	public void init(String p_sScreenIdentifier, Action<IN, OUT, STATE, PROGRESS> p_oAction,
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
	 */
	@Override
	public void dismissProgressDialog() {
		this.androidActionTaskDelegate.dismissProgressDialog();
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
	public Class<? extends Action<? ,? ,?,?>> getActionInterface() {
		return this.androidActionTaskDelegate.getActionInterface();
	}

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
