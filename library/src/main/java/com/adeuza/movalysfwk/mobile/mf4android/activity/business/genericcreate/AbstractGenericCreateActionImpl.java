package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericcreate;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>TODO Décrire la classe AbstractCreateActionImpl</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 * @param <IN> Null action parameter
 * @author emalespine
 */
public abstract class AbstractGenericCreateActionImpl<IN extends ActionParameter> extends AbstractTaskableAction<IN, NullActionParameterImpl, DefaultActionStep, Void>{

	/**
	 * Numéro de version
	 */
	private static final long serialVersionUID = -37316718289792552L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract IN getEmptyInParameter();

	/**
	 * {@inheritDoc}
	 * @throws ActionException 
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, IN p_oParameterIn) throws ActionException {
		this.getDataLoader().setItemId(-1);
		try {
			this.getDataLoader().reload(p_oContext);
		} catch (DataloaderException e) {
			throw new ActionException(e);
		}
		return new NullActionParameterImpl();
	}
	
	/**
	 * GETTER
	 * @return data loader
	 */
	protected abstract Dataloader<?> getDataLoader();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, IN p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, IN p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do;
		return p_oResultOut;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void...p_oProgressInformations) {
		//Nothing to do;
	}
	
	/**
	 * Pre execute
	 * @param p_oContext context
	 * @throws error
	 */
	public void doPreExecute(MContext p_oContext) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPostExecute(MContext p_oContext, IN p_oParameterIn, NullActionParameterImpl p_oParameterOut) throws ActionException {
		//Nothing to do;
	}

	/**
	 * ResetDataBaseAction use allways database access.
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * ResetDataBaseAction use allways database access.
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
}
