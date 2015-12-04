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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Permet de définir une tâche (généralement un autre thread) ayant la capacité de lancée une action</p>
 *
 *
 */
@Scope(ScopePolicy.PROTOTYPE)
public interface MMActionTask<IN extends ActionParameter, OUT extends ActionParameter,STATE extends ActionStep, PROGRESS extends Object> {

	/** Etats utilisés pour réaliser les actions de callback */
	public static enum ActionTaskStep implements ActionStep {
		/** état de pré execute (exécuté dans le thread UI) */
		PRE_EXECUTE,
		/** état de post execute (exécuté dans le thread UI) */
		POST_EXECUTE,
		/** état de fin d'action en succès (exécuté dans le thread UI)*/
		END_SUCCESS,
		/** état de fin d'action en erreur (exéctué dans le thread UI)*/
		END_FAIL,
		/** état de lancement de code intermédiaire (exécuté dans le thread UI) */
		PROGRESS_RUNNABLE
	}
	
	/**
	 * Retourne l'objet action
	 *
	 * @return Objet action
	 */
	public AbstractTaskableAction<IN, OUT, STATE, PROGRESS> getAction();

	/**
	 * Retourne l'objet parameterIn
	 *
	 * @return Objet parameterIn
	 */
	public ActionTaskIn<IN> getParameterIn();

	/**
	 * Définit les paramètres en entrée
	 *
	 * @param p_oParameterIn Les paramètres en entrée.
	 */
	public void setParameterIn(ActionTaskIn<IN> p_oParameterIn);

	/**
	 * Retourne l'objet context
	 *
	 * @return Objet context
	 */
	public MContext getContext();

	/**
	 * Retourne l'objet notifier
	 *
	 * @return Objet notifier
	 */
	public Notifier getNotifier();
	
	/**
	 * Init the task with screen parent and the action to execute
	 *
	 * @param p_sScreenIdentifier screen identifier who launched the action
	 * @param p_oAction the action to execute
	 * @param p_oActionInterface a {@link java.lang.Class} object.
	 * @param p_oParameterIn a IN object.
	 * @param p_oAnalyse a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse} object.
	 */
	public void init(String p_sScreenIdentifier, Action<IN,OUT,STATE,PROGRESS> p_oAction, 
			Class<? extends Action<IN, OUT, STATE, PROGRESS>> p_oActionInterface,
			IN p_oParameterIn, 
			ClassAnalyse p_oAnalyse);
	
	/**
	 * Execute the task
	 */
	public void execAction();
	
	/**
	 * Permet de publier l'avancement d'une ActionTask
	 *
	 * @param p_oProgressValues les informations à utiliser lors de la publication
	 */
	public void publishActionProgress(ActionTaskProgress<STATE, PROGRESS>...p_oProgressValues);

	/**
	 * Publish a runnable progress
	 *
	 * @param p_oRunnable runnable to execute in UI thread
	 * @param p_bBlocking if true, wait runnable to finish
	 */
	public void publishActionRunnableProgress( Runnable p_oRunnable, boolean p_bBlocking );

	/**
	 * Create waiting dialog for current action
	 */
	public void createProgressDialog();
	
	/**
	 * Create waiting dialog for current action
	 */
	public void dismissProgressDialog();

	/**
	 * Get parent
	 *
	 * @return parent
	 */
	public Screen getParent();
	
	/**
	 * Get screen identifier
	 *
	 * @return screen identifier
	 */
	public String getScreenIdentifier();
	
	/**
	 * Return true if progress dialog is disabled
	 * @return true if progress dialog is disabled
	 */
	public boolean isProgressDialogDisabled();

	/**
	 * Execute fail process only.
	 * @param p_oDeniedPermissions 
	 * @throws ActionException 
	 */
	public void execDeniedPermission(String[] p_oDeniedPermissions) throws ActionException;
}
