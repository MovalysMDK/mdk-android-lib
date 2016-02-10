package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave;

import java.util.Collection;

import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ManageSynchronizationParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MObjectToSynchronizeDao;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;

/**
 * persistent save detail action
 * @param <ITEM> item entity
 */
public abstract class AbstractPersistentSaveDetailActionImpl<ITEM extends MEntity> extends AbstractSaveDetailActionImpl<ITEM> {
	
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -7659582934512458316L;

	/**
	 * to synchronize
	 * @param p_oContext context
	 * @param p_oEntity item entity
	 * @return collection of objects to synchronize
	 * @throws ActionException error
	 */
	protected abstract Collection<MObjectToSynchronize> toSynchronize(MContext p_oContext, ITEM p_oEntity) throws ActionException;
	
	/**
	 * on success
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave.AbstractSaveDetailActionImpl#doOnSuccess(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl)
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, EntityActionParameterImpl<ITEM> p_oResultOut) throws ActionException {
		try {
			Collection<MObjectToSynchronize> listObjectToSync = this.toSynchronize(p_oContext, p_oResultOut.getEntity());
			if (listObjectToSync != null && !listObjectToSync.isEmpty()) {
				BeanLoader.getInstance().getBean(MObjectToSynchronizeDao.class).saveOrUpdateListMObjectToSynchronize(listObjectToSync, p_oContext);
			}

			Application.getInstance().getController().manageSynchronizationActions(
					SubControllerSynchronization.SC_EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION,
					new ManageSynchronizationParameter(listObjectToSync != null && !listObjectToSync.isEmpty()
							|| BeanLoader.getInstance().getBean(MObjectToSynchronizeDao.class).isThereDataToSynchronize(p_oContext)));

			super.doOnSuccess(p_oContext, p_oParameterIn, p_oResultOut);
		} catch( Exception oActionException ) {
			Log.e(this.getClass().getSimpleName(), "Erreur in toSynchronize", oActionException);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}
	}
}
