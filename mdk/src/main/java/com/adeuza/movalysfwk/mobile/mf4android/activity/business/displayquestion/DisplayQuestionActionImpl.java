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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.displayquestion;

import android.content.Intent;

import com.adeuza.movalysfwk.mobile.mf4android.activity.QuestionActivity;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidMMContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AsyncRedirectActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion.DisplayQuestionAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion.InDisplayQuestionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Parameter to display a question</p>
 *
 *
 *
 */
public class DisplayQuestionActionImpl implements DisplayQuestionAction {
	
	/** serial id */
	private static final long serialVersionUID = -9023983938509433300L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AsyncRedirectActionParameterImpl doAction(MContext p_oContext, InDisplayQuestionParameter p_oParameterIn) {
		Intent oIntent = new Intent(((AndroidMMContext)p_oContext).getAndroidNativeContext(),
				QuestionActivity.class);
		oIntent.putExtra("parameterIn", p_oParameterIn);
		
		((AndroidMMContext)p_oContext).getAndroidNativeContext().startActivity(oIntent);
		//((AbstractMMActivity) Application.getInstance().getMainScreen()).startActivity(oIntent);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, InDisplayQuestionParameter p_oParameterIn, AsyncRedirectActionParameterImpl p_oResultOut) {
		//NothingToDo
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AsyncRedirectActionParameterImpl doOnError(MContext p_oContext, InDisplayQuestionParameter p_oParameterIn,
			AsyncRedirectActionParameterImpl p_oResultOut) {
		// Nothing to do
		return p_oResultOut;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getConcurrentAction() {
		return Action.DEFAULT_QUEUE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InDisplayQuestionParameter getEmptyInParameter() {
		return new InDisplayQuestionParameter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyControllerOfActionEnd(long p_lActionId) {
		// Nothing to do
		// dans ce cas le comportement de cette méthode est implémentée dans l'activité correspondande
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(InDisplayQuestionParameter p_oInDisplayQuestionParameter, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, InDisplayQuestionParameter p_oParameterIn, AsyncRedirectActionParameterImpl p_oParameterOut)
			throws ActionException {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESS[])
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void... p_oProgressInformations) {
		//Nothing to do
	}
}
