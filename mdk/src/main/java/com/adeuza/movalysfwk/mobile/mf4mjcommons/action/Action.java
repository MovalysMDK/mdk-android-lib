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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.action;

import java.io.Serializable;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Interface for business processing.</p>
 *
 * @param <IN> type of parameter
 * @param <OUT> type of result
 */
@Scope(ScopePolicy.PROTOTYPE)
public interface Action<IN extends ActionParameter, OUT extends ActionParameter, STATE extends ActionStep, PROGRESS extends Object> extends Serializable {
	
	static final int NO_QUEUE = 0;
	static final int DEFAULT_QUEUE = 1;
	static final int SYNCHRO_QUEUE = 2;
	
	/**
	 * Realises the pre business action. If an error occurs during treatment, error message must be added to the context (ui thread if it exists).
	 *
	 * @param p_oContext the context to use
	 * @param p_oParameterIn the parameter to use
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	public void doPreExecute(IN p_oParameterIn, MContext p_oContext) throws ActionException;
	
	/**
	 * Realises the business action. If an error occurs during treatment, error message must be added to the context.
	 *
	 * @param p_oContext the context to use
	 * @param p_oParameterIn the parameter to use
	 * @return result of business processing
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	public OUT doAction(MContext p_oContext, IN p_oParameterIn) throws ActionException;
	
	/**
	 * This method is called after doOnSuccess or doOnError. (ui thread if it exists)
	 *
	 * @param p_oContext the context to use
	 * @param p_oParameterIn the parameter used by doAction
	 * @param p_oParameterOut a OUT object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	public void doPostExecute(MContext p_oContext, IN p_oParameterIn, OUT p_oParameterOut) throws ActionException;
	
	/**
	 * This method is called when the methode doAction is executed without error.
	 *
	 * @param p_oContext the context to use
	 * @param p_oParameterIn the parameter used by doAction
	 * @param p_oResultOut the result given by doAction
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	public void doOnSuccess(MContext p_oContext, IN p_oParameterIn, OUT p_oResultOut) throws ActionException;
	
	/**
	 * This method is called when the methode doAction is executed with some error.
	 *
	 * @param p_oContext the context to use
	 * @param p_oParameterIn the parameter used by doAction
	 * @param p_oResultOut the result given by doAction
	 * @return the modified result of business processing
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	public OUT doOnError(MContext p_oContext, IN p_oParameterIn, OUT p_oResultOut) throws ActionException;
	
	/**
	 * Publish action progression (if there are two threads, this method is call in ui thread).
	 *
	 * @param p_oContext the context to use
	 * @param p_oState the current state of action
	 * @param p_oProgressInformations a PROGRESS object.
	 */
	public void doPublishProgress(MContext p_oContext, STATE p_oState, PROGRESS...p_oProgressInformations);
	
	/**
	 * Indicates if the action can be launch in paralell of others actions
	 *
	 * @return 0 it the action can be run in parallel (No Queue), 1 if it must be runned alone (Default Queue)
	 */
	public int getConcurrentAction();
	
	/**
	 * Return empty in parameter
	 *
	 * @return empty in parameter
	 */
	public IN getEmptyInParameter();
}
