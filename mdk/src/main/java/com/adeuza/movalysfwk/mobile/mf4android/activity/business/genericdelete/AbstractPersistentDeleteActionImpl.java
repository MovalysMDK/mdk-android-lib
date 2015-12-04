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

import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ManageSynchronizationParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoaderError;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ObjectToSynchronizeService;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

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
			Application.getInstance().getController().manageSynchronizationActions(AbstractSubControllerSynchronization.SC_EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION,
					new ManageSynchronizationParameter(BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));

			super.doOnSuccess(p_oContext, p_oParameterIn, p_oResultOut);
		} catch(ActionException | SynchroException | BeanLoaderError oActionException) {
			Log.e(this.getClass().getSimpleName(), "Erreur in toSynchronize", oActionException);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}
		super.doOnSuccess(p_oContext, p_oParameterIn, p_oResultOut);
	}
}
