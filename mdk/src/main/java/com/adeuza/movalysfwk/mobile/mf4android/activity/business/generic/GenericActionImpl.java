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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.generic;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.generic.InParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;

/**
 * <p>Action générique.</p>
 *
 *
 * @since MF-Annapurna
 */
public abstract class GenericActionImpl {

	/** notifier*/
	private Notifier notifier = null;

	/**
	 * Construct a new action
	 */
	public GenericActionImpl() {
		this.notifier = new Notifier();
	}
	
	/**
	 * GETTER
	 * @return IN parameter
	 */
	public InParameter getEmptyInParameter() {
		return new InParameter();
	}

	/**
	 * On success
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @param p_oResultOut OUT null action parameter
	 */
	public void doOnSuccess(MContext p_oContext, InParameter p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
	}

	/**
	 * On error
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @param p_oResultOut OUT result
	 * @return null action OUT parameter
	 */
	public NullActionParameterImpl doOnError(MContext p_oContext, InParameter p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do;
		return p_oResultOut;
	}
	
	/**
	 * publish progress
	 * @param p_oContext context
	 * @param p_oState default action step
	 * @param p_oProgressInformations progress informations
	 */
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void...p_oProgressInformations) {
		//Nothing to do;
	}
	
	/**
	 * pre-execute
	 * @param p_oParameterIn IN parameter
	 * @param p_oContext context
	 * @throws ActionException error
	 */
	public void doPreExecute(InParameter p_oParameterIn, MContext p_oContext) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * post-execute
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @param p_oParameterOut OUT parameter
	 * @throws ActionException error
	 */
	public void doPostExecute(MContext p_oContext, InParameter p_oParameterIn, NullActionParameterImpl p_oParameterOut) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * notify
	 */
	public void doNotify() {
		// nothing to notify
	}

	/**
	 * is concurrent action?
	 * @return true if this action is concurrent
	 */
	public int getConcurrentAction() {
		return Action.DEFAULT_QUEUE;
	}

	/**
	 * GETTER
	 * @return notifier
	 */
	public Notifier getNotifier() {
		return this.notifier;
	}
}
