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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.doafterbarcodescan;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.BarCodeResult;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.DoAfterBarCodeScanAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Action lancée après avoir scanner un code dans l'application.</p>
 *
 *
 */
public class DoAfterBarCodeScanActionImpl implements DoAfterBarCodeScanAction {
	
	/** serial id */
	private static final long serialVersionUID = 5070468245144955460L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BarCodeResult getEmptyInParameter() {
		return new BarCodeResult();
	}
	
	/**
	 * {@inheritDoc}
	 * this default implementation does nothing and return the parameter
	 */
	@Override
	public BarCodeResult doAction(MContext p_oContext, BarCodeResult p_oParameterIn) {
		return p_oParameterIn;
	}

	/**
	 * {@inheritDoc}
	 * this default implementation does nothing 
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, BarCodeResult p_oParameterIn, BarCodeResult p_oResultOut) {
		//nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BarCodeResult doOnError(MContext p_oContext, BarCodeResult p_oParameterIn, BarCodeResult p_oResultOut) {
		//nothing
		return p_oResultOut;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getConcurrentAction() {
		return Action.NO_QUEUE;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(BarCodeResult p_oBarCodeResult, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, BarCodeResult p_oParameterIn, BarCodeResult p_oParameterOut)
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
