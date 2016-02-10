package com.adeuza.movalysfwk.mobile.mf4android.activity.business.dobeforephonecall;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dobeforephonecall.DoBeforePhoneCallAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dobeforephonecall.PhoneNumber;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Action exécutée avant de lancer l'action d'appel du téléphone.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author dmaurange
 */
public class DoBeforePhoneCallActionImpl implements DoBeforePhoneCallAction{

	/** serial id */
	private static final long serialVersionUID = 5271589176212573727L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PhoneNumber getEmptyInParameter() {
		return new PhoneNumber();
	}
	
	/**
	 * {@inheritDoc}
	 * Default implementation does nothing and return the number
	 * if "", the number is not dial
	 */
	@Override
	public PhoneNumber doAction(MContext p_oContext, PhoneNumber p_oParameterIn) {
		return p_oParameterIn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, PhoneNumber p_oParameterIn, PhoneNumber p_oResultOut) {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PhoneNumber doOnError(MContext p_oContext, PhoneNumber p_oParameterIn, PhoneNumber p_oResultOut) {
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
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(PhoneNumber p_oPhoneNumber, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, PhoneNumber p_oParameterIn, PhoneNumber p_oParameterOut)
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
