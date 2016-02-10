package com.adeuza.movalysfwk.mobile.mf4android.activity.business.resetsettings;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.RedirectActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.resetsettings.ResetSettingAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.NotifierLauncher;

/**
 * <p>ResetSettingAction implementation</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author smaitre
 */
public class ResetSettingActionImpl implements ResetSettingAction, NotifierLauncher {

	/** serial id */
	private static final long serialVersionUID = 2007858446410534405L;
	/** notifier*/
	private Notifier notifier = null;
	
	/** 
	 * Construct an object <em>ResetSettingActionImpl</em>.
	 */
	public ResetSettingActionImpl() {
		this.notifier = new Notifier();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RedirectActionParameterImpl getEmptyInParameter() {
		return new RedirectActionParameterImpl();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, RedirectActionParameterImpl p_oParameterIn) {
		Application.getInstance().removeSettings();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, RedirectActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing To do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, RedirectActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		// Nothing to do
		return p_oResultOut;
	}

	/**
	 * ResetSettingAction has never access to database
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return false;
	}

	/**
	 * ResetSettingAction has never access to database
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
	 */
	@Override
	public Notifier getNotifier() {
		return this.notifier;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(RedirectActionParameterImpl p_oParameterIn, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, RedirectActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oParameterOut)
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
