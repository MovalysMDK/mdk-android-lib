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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericcreate;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>TODO Décrire la classe AbstractCreateActionImpl</p>
 *
 * @param <IN> Null action parameter
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
	 * {@inheritDoc}
	 */
	@Override
	public int getConcurrentAction() {
		return Action.DEFAULT_QUEUE;
	}	
}
