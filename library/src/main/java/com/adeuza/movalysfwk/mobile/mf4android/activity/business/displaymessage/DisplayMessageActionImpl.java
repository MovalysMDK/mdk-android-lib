package com.adeuza.movalysfwk.mobile.mf4android.activity.business.displaymessage;

import android.widget.Toast;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.AlertMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.DisplayMessageAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Action d'affichage d'un nouveau message à l'écran.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author dmaurange
 */
public class DisplayMessageActionImpl implements DisplayMessageAction {

	/** serial id */
	private static final long serialVersionUID = 2243149632791649492L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AlertMessage getEmptyInParameter() {
		return new AlertMessage();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, AlertMessage p_oParameterIn) {
		AbstractMMActivity oCurrentActivity = (AbstractMMActivity)
				((AndroidApplication) Application.getInstance()).getCurrentVisibleActivity();
		int iDuration;
		switch (p_oParameterIn.getDuration()){
		case AlertMessage.LONG :
			iDuration=Toast.LENGTH_LONG; 
			break;
		case AlertMessage.SHORT :
			iDuration=Toast.LENGTH_SHORT;
			break;
		default :
			iDuration=p_oParameterIn.getDuration();
			break;
		}
		try {
			Toast.makeText(oCurrentActivity,Application.getInstance().getStringResource(p_oParameterIn.getMessage()), iDuration).show();
		} catch (RuntimeException e) {
			Application.getInstance().getLog().debug("DisplayMessageActionImpl", "Can't display toast from a background thread");
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, AlertMessage p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, AlertMessage p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
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
	public void doPreExecute(AlertMessage p_oAlertMessage, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, AlertMessage p_oParameterIn, NullActionParameterImpl p_oParameterOut)
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
