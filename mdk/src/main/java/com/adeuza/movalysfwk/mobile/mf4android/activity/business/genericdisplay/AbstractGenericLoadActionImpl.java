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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;

/**
 *
 * @param <IN>
 * @param <OUT>
 * @param <PROGRESS>
 * @param <PROGRESSPARAMETER>
 */
public abstract class AbstractGenericLoadActionImpl<IN extends ActionParameter, OUT extends ActionParameter, PROGRESS extends ActionStep, PROGRESSPARAMETER extends Object> extends AbstractGenericActionImpl<IN, OUT, PROGRESS, PROGRESSPARAMETER> {


	/**
	 * @param p_oContext
	 * @param p_oParam
	 * @return
	 */
	protected MMDataloader<?> reloadDataLoader(MContext p_oContext, InDisplayParameter p_oParam){
		MMDataloader<?> r_oDtl = null;
		try {
			r_oDtl = BeanLoader.getInstance().getBean(p_oParam.getDataLoader());
			long iId = -1L ;
			if (p_oParam.getId()!=null){
				iId = Long.parseLong(p_oParam.getId() ) ;
			}
			r_oDtl.addItemId(p_oParam.getKey(),  iId);
			r_oDtl.addOptions(p_oParam.getKey(), p_oParam.getOptions());
			r_oDtl.reload(p_oContext, p_oParam.getKey(), doNotify(), p_oParam.getReload());
		} catch (DataloaderException e) {
			Log.e(this.getClass().getSimpleName(), "Erreur in loadData", e);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);

		}
		
		return r_oDtl;
		
	}

	/**
	 * return true if the dataloader need to notify context
	 * @return true if the dataloader need to notify context false otherwise
	 */
	protected abstract boolean doNotify();

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isConcurrentAction()
	 */
	@Override
	public int getConcurrentAction() {
		return Action.DEFAULT_QUEUE;
	}
}
