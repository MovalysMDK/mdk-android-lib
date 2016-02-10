package com.adeuza.movalysfwk.mobile.mf4android.activity.business.displayquestion;

import android.content.Intent;

import com.adeuza.movalysfwk.mobile.mf4android.activity.QuestionActivity;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AsyncRedirectActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion.DisplayQuestionAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion.InDisplayQuestionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Parameter to display a question</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author smaitre
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
		Intent oIntent = new Intent(((AndroidContextImpl)p_oContext).getAndroidNativeContext(),
				QuestionActivity.class);
		oIntent.putExtra("parameterIn", p_oParameterIn);
		
		((AndroidContextImpl)p_oContext).getAndroidNativeContext().startActivity(oIntent);
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
	public boolean isDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritableDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConcurrentAction() {
		return false;
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
