package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdelete;

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

/**
 * abstract persistent delete action
 *
 * @param <ITEM> entity
 */ 
public abstract class AbstractPersistentDeleteActionImpl<ITEM extends MEntity> extends AbstractDeleteActionImpl<ITEM> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5575871716073316262L;

	/**
	 * to sync
	 * @param p_oContext context
	 * @param p_oEntity item entity
	 * @throws ActionException error
	 */
	protected abstract void toSynchronize(MContext p_oContext, ITEM p_oEntity) throws ActionException;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdelete.AbstractDeleteActionImpl#doOnSuccess(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl)
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, EntityActionParameterImpl<ITEM> p_oResultOut) throws ActionException {
		try {
			this.toSynchronize(p_oContext, p_oResultOut.getEntity());
			Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION,
					new ManageSynchronizationParameter(BeanLoader.getInstance().getBean(MObjectToSynchronizeDao.class).isThereDataToSynchronize(p_oContext)));

			super.doOnSuccess(p_oContext, p_oParameterIn, p_oResultOut);
		} catch(ActionException oActionException) {
			Log.e(this.getClass().getSimpleName(), "Erreur in toSynchronize", oActionException);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}
		super.doOnSuccess(p_oContext, p_oParameterIn, p_oResultOut);
	}
}
