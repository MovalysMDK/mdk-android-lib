package com.adeuza.movalysfwk.mobile.mf4android.activity.business.generic;

import java.util.Collection;

import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
/**
 * <p>Abstract class</p>
 *
 * <p>Copyright (c) 2014
 *
 * @author Sopra, pole mobilite (Nantes)
 * @param <IN> Action parameter
 * @param <OUT> Action parameter
 *
 */
public abstract class AbstractTreatmentActionImpl<IN extends ActionParameter, OUT extends ActionParameter>
		extends AbstractTaskableAction<IN, OUT, DefaultActionStep, Void> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1734845688650502366L;
	/**
	 * notifier
	 */
	private Notifier notifier = null;

	/**
	 * Construct a new action
	 */
	public AbstractTreatmentActionImpl() {
		this.notifier = new Notifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract IN getEmptyInParameter();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OUT doAction(MContext p_oContext,
			IN p_oParameterIn) throws ActionException {
		OUT r_oResult = null;
		try {
			r_oResult = this.doTreatment(p_oContext, p_oParameterIn);
		} catch (ActionException oException) {
			Log.e(this.getClass().getSimpleName(), "Erreur in doTreatment",
					oException);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}
		return r_oResult;
	}

	/**
	 * do treatment of the action
	 * @param p_oContext context
	 * @param p_oParameterIn input parameter
	 * @return output
	 * @throws ActionException error
	 */
	protected abstract OUT doTreatment(MContext p_oContext, IN p_oParameterIn) throws ActionException ;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPublishProgress(MContext p_oContext,
			DefaultActionStep p_oState, Void... p_oProgressInformations) {
		// Nothing to do;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doPreExecute(MContext p_oContext) throws ActionException {
		// Nothing to do;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPostExecute(MContext p_oContext, IN p_oParameterIn,
			OUT p_oParameterOut) throws ActionException {
		final Collection<BusinessEvent<?>> oEvents = p_oContext.getEvents();
		if (oEvents != null) {
			for (BusinessEvent<?> oEvent : oEvents) {
				Application.getInstance().getController().publishBusinessEvent(p_oContext, oEvent);
			}
			p_oContext.clearEvents();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, IN p_oParameterIn,
			OUT p_oResultOut) throws ActionException {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OUT doOnError(MContext p_oContext, IN p_oParameterIn,
			OUT p_oResultOut) throws ActionException {
		// Nothing to do
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doNotify() {
		// does nothing
	}

	/**
	 * ResetDataBaseAction use always database access. {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return true;
	}

	/**
	 * {@inheritDoc} ResetDataBaseAction use always database access.
	 */
	@Override
	public boolean isWritableDataBaseAccessAction() {
		return true;
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
	public Notifier getNotifier() {
		return this.notifier;
	}
}
