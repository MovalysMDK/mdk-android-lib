package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdelete;

import java.util.Collection;

import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;

/**
 * abstract delete action
 * @author lmichenaud
 *
 * @param <ITEM> entity
 */
public abstract class AbstractDeleteActionImpl <ITEM extends MEntity> 
extends AbstractTaskableAction<NullActionParameterImpl, EntityActionParameterImpl<ITEM>, DefaultActionStep, Void>{

	/**
	 * serial number
	 */
	private static final long serialVersionUID = -3369020749846905408L;
	/** notifier*/
	private Notifier notifier = null;
	
	/**
	 * Construct a new action
	 */
	public AbstractDeleteActionImpl() {
		this.notifier = new Notifier();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl getEmptyInParameter() {
		return new NullActionParameterImpl();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityActionParameterImpl<ITEM> doAction(MContext p_oContext, NullActionParameterImpl p_oParameterIn) throws ActionException {
		ITEM oItem = null;
		try {
			oItem = this.deleteData(p_oContext, p_oParameterIn);
		} catch( ActionException oException ) {
			Log.e(this.getClass().getSimpleName(), "Erreur in deleteData", oException);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}
		return new EntityActionParameterImpl<>(oItem);
	}
	
	/**
	 * delete data
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @return item
	 * @throws ActionException error
	 */
	protected abstract ITEM deleteData(MContext p_oContext, NullActionParameterImpl p_oParameterIn) throws ActionException;
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESSPARAMETER[])
	 * @param p_oContext context
	 * @param p_oState state
	 * @param p_oProgressInformations progress informations
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void...p_oProgressInformations) {
		//Nothing to do;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void doPreExecute(NullActionParameterImpl p_oParameterIn, MContext p_oContext) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, NullActionParameterImpl p_oParameterIn, EntityActionParameterImpl<ITEM> p_oParameterOut) throws ActionException {
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
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, EntityActionParameterImpl<ITEM> p_oResultOut) throws ActionException {
		//Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityActionParameterImpl<ITEM> doOnError(MContext p_oContext, NullActionParameterImpl p_oParameterIn, EntityActionParameterImpl<ITEM> p_oResultOut) throws ActionException {
		//Nothing to do
		return p_oResultOut;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void doNotify() {
		// does nothing
	}
	
	/**
	 * ResetDataBaseAction use always database access.
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * ResetDataBaseAction use always database access.
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
