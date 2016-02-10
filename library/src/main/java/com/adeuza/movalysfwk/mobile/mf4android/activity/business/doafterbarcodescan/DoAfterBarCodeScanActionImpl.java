package com.adeuza.movalysfwk.mobile.mf4android.activity.business.doafterbarcodescan;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.BarCodeResult;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.DoAfterBarCodeScanAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Action lancée après avoir scanner un code dans l'application.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author dmaurange
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
		return true;
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
