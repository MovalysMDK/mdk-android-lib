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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdelete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidMMContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.RedirectActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * chain delete detail action
 * 
 */
public class ChainDeleteDetailActionImpl
		extends AbstractTaskableAction<ChainDeleteActionDetailParameter, RedirectActionParameterImpl, DefaultActionStep, Void>
		implements ChainDeleteDetailAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5608245764179468600L;

	/**
	 * notifier
	 */
	private Notifier notifier = new Notifier();

	/**
	 * Actions to launch
	 */
	private List<DeleteDetailAction<MEntity, ?, ?>> actions = new ArrayList<>();

	/**
	 * 
	 */
	private Map<DeleteDetailAction, EntityActionParameterImpl> actionResults = new HashMap<>();
	
	/**
	 * do action
	 * @param p_oContext context
	 * @param p_oParameterIn IN chain delete action detail param
	 * @return redirect action param
	 */
	@Override
	public RedirectActionParameterImpl doAction(MContext p_oContext, ChainDeleteActionDetailParameter p_oParameterIn) {
		try {
			if (p_oParameterIn != null) {
				for (DeleteDetailAction<MEntity, ?, ?> oDeleteAction : this.actions) {
					MEntity oEntity = oDeleteAction.deleteData(p_oContext, p_oParameterIn.getDeleteDetailActionsParameterIn());
					this.actionResults.put(oDeleteAction, new EntityActionParameterImpl(oEntity));
				}
				
				// create the events
				p_oContext.setEventsExitMode(p_oParameterIn.isExitMode());
				((AndroidMMContext)p_oContext).addBusinessEventFromFirstEvent(p_oParameterIn.isExitMode());
			}
		} catch (ActionException oException) {
			Log.e(this.getClass().getSimpleName(),"Erreur in ChainDeleteDetailAction", oException);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}

		return new RedirectActionParameterImpl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChainDeleteActionDetailParameter getEmptyInParameter() {
		return new ChainDeleteActionDetailParameter();
	}

	/**
	 * do publish
	 * @param p_oParameterIn IN action parameter
	 * @param p_oContext context
	 * @param p_oState state
	 * @param p_oProgressInformations progress info
	 */
	public void doPublishProgress(ActionParameter p_oParameterIn,
			MContext p_oContext, DefaultActionStep p_oState,
			Void... p_oProgressInformations) {
		//do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPreExecute(ChainDeleteActionDetailParameter p_oParameterIn,
			MContext p_oContext) throws ActionException {

		ChainDeleteActionDetailParameter oParameterIn = (ChainDeleteActionDetailParameter) p_oParameterIn;

		for (Class<? extends DeleteDetailAction<?, ?, ?>> oDeleteActionClass : oParameterIn
				.getDeleteDetailActions()) {
			
			DeleteDetailAction<MEntity, ?, ?> oAction = (DeleteDetailAction<MEntity, ?, ?>) BeanLoader.getInstance().getBean(oDeleteActionClass);
			this.actions.add(oAction);
			try {
				oAction.doPreExecute(p_oParameterIn.getDeleteDetailActionsParameterIn(), p_oContext);
			} catch (ActionException oActionException) {
				throw new MobileFwkException(oActionException);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPostExecute(MContext p_oContext,
			ChainDeleteActionDetailParameter p_oParameterIn,
			RedirectActionParameterImpl p_oParameterOut) throws ActionException {
		
		for (DeleteDetailAction<?, ?, ?> oDeleteAction : this.actions) {
			oDeleteAction.doPostExecute(p_oContext, p_oParameterIn.getDeleteDetailActionsParameterIn(), this.actionResults.get(oDeleteAction));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext,
			ChainDeleteActionDetailParameter p_oParameterIn,
			RedirectActionParameterImpl p_oResultOut) throws ActionException {
		
		for (DeleteDetailAction<?, ?, ?> oDeleteAction : this.actions) {
			oDeleteAction.doOnSuccess(p_oContext, p_oParameterIn.getDeleteDetailActionsParameterIn(), this.actionResults.get(oDeleteAction));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RedirectActionParameterImpl doOnError(MContext p_oContext,
			ChainDeleteActionDetailParameter p_oParameterIn,
			RedirectActionParameterImpl p_oResultOut) throws ActionException {
		
		for (DeleteDetailAction<?, ?, ?> oDeleteAction : this.actions) {
			oDeleteAction.doOnError(p_oContext, p_oParameterIn.getDeleteDetailActionsParameterIn(), this.actionResults.get(oDeleteAction));
		}		
		
		return p_oResultOut;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doNotify() {
		// does nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override	
	public int getConcurrentAction() {
		return Action.DEFAULT_QUEUE;
	}

	/**
	 * GETTER of Notifier
	 * @return notifier
	 */
	public Notifier getNotifier() {
		return this.notifier;
	}
}
