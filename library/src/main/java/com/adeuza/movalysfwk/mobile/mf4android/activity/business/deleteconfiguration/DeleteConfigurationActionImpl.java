package com.adeuza.movalysfwk.mobile.mf4android.activity.business.deleteconfiguration;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.deleteconfiguration.DeleteConfigurationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.serialization.SerializationDao;

/**
 * <p>Action used to delete the serialized configuration of the application.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (14 janv. 2011)
 */
public class DeleteConfigurationActionImpl implements DeleteConfigurationAction {

	/**
	 * Serial version for the serialization.
	 */
	private static final long serialVersionUID = 4409352354035843346L;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, NullActionParameterImpl p_oParameterIn) {
		if (Application.getInstance().isNewVersion()) {
			SerializationDao oDao = BeanLoader.getInstance().getBean(SerializationDao.class);
			oDao.delete(p_oContext, ConfigurationsHandler.SERIALIZATION_KEY);
			Application.getInstance().getLog().info(this.getClass().getSimpleName(), "> delete to the serialized configuration of the application");
		}
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
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
	public NullActionParameterImpl getEmptyInParameter() {
		return new NullActionParameterImpl();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(NullActionParameterImpl p_oNullActionParameterImpl, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oParameterOut)
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
