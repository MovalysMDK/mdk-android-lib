package com.adeuza.movalysfwk.mobile.mf4android.activity.business.generic;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.generic.InParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;

/**
 * <p>Action générique.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since MF-Annapurna
 */
public abstract class GenericActionImpl {

	/** notifier*/
	private Notifier notifier = null;

	/**
	 * Construct a new action
	 */
	public GenericActionImpl() {
		this.notifier = new Notifier();
	}
	
	/**
	 * GETTER
	 * @return IN parameter
	 */
	public InParameter getEmptyInParameter() {
		return new InParameter();
	}

	/**
	 * On success
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @param p_oResultOut OUT null action parameter
	 */
	public void doOnSuccess(MContext p_oContext, InParameter p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
	}

	/**
	 * On error
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @param p_oResultOut OUT result
	 * @return null action OUT parameter
	 */
	public NullActionParameterImpl doOnError(MContext p_oContext, InParameter p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do;
		return p_oResultOut;
	}
	
	/**
	 * publish progress
	 * @param p_oContext context
	 * @param p_oState default action step
	 * @param p_oProgressInformations progress informations
	 */
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void...p_oProgressInformations) {
		//Nothing to do;
	}
	
	/**
	 * pre-execute
	 * @param p_oParameterIn IN parameter
	 * @param p_oContext context
	 * @throws ActionException error
	 */
	public void doPreExecute(InParameter p_oParameterIn, MContext p_oContext) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * post-execute
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @param p_oParameterOut OUT parameter
	 * @throws ActionException error
	 */
	public void doPostExecute(MContext p_oContext, InParameter p_oParameterIn, NullActionParameterImpl p_oParameterOut) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * notify
	 */
	public void doNotify() {
		// nothing to notify
	}

	/**
	 * ResetDataBaseAction use allways database access.
	 * @return true if this action is database access
	 */
	public boolean isDataBaseAccessAction() {
		return false;
	}

	/**
	 * ResetDataBaseAction use allways database access.
	 * @return true if this action is writable database access
	 */
	public boolean isWritableDataBaseAccessAction() {
		return false;
	}

	/**
	 * is concurrent action?
	 * @return true if this action is concurrent
	 */
	public boolean isConcurrentAction() {
		return false;
	}

	/**
	 * GETTER
	 * @return notifier
	 */
	public Notifier getNotifier() {
		return this.notifier;
	}
}
