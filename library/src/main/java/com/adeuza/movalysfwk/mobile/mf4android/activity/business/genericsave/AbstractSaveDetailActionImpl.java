package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave;

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
 * <p>Contains the generic treatment for saving entity</p>
 * <p>Copyright (c) 2011</p>
 * @param <ITEM> item entity
 * <p>Company: Adeuza</p>
 */
public abstract class AbstractSaveDetailActionImpl<ITEM extends MEntity> 
	extends AbstractTaskableAction<NullActionParameterImpl, EntityActionParameterImpl<ITEM>, DefaultActionStep, Void>
	implements SaveDetailAction<ITEM, DefaultActionStep, Void> {

	/**
	 * notifier
	 */
	private Notifier notifier = null;
	
	/**
	 * Construct a new action
	 */
	public AbstractSaveDetailActionImpl() {
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
		ITEM oItem = null ;
		try {
			
			if ( this.validateData(p_oParameterIn, p_oContext)) {
				oItem = this.preSaveData(p_oParameterIn, p_oContext );
				oItem = this.saveData(oItem, p_oParameterIn, p_oContext);
				this.postSaveData(oItem, p_oParameterIn, p_oContext );
			}
			
		} catch( ActionException oException ) {
			Log.e(this.getClass().getSimpleName(), "Erreur in saveData", oException);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}
		return new EntityActionParameterImpl<>(oItem);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void...p_oProgressInformations) {
		//Nothing to do;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPreExecute(NullActionParameterImpl p_oNullActionParameterImpl, MContext p_oContext) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * {@inheritDoc}
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
	public abstract ITEM saveData(ITEM p_oEntity, NullActionParameterImpl p_oParameterIn, MContext p_oContext ) throws ActionException;

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
		return null;
	}
	
	/**
	 * notify: do nothing
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
	 * GETTER
	 * @return notifier
	 */
	public Notifier getNotifier() {
		return this.notifier;
	}
	
	/**
	 * Method launched before save and after the validation of the view model
	 * @param p_oEntityToSave entity that will be saved
	 * {@inheritDoc}
	 */
	@Override
	public void modifyEntityBeforeSave(ITEM p_oEntityToSave) throws ActionException {
		// does nothing only for overload
	}
}
