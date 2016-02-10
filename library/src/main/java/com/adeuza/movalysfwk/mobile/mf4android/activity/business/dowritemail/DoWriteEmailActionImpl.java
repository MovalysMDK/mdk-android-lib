package com.adeuza.movalysfwk.mobile.mf4android.activity.business.dowritemail;

import android.content.Intent;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.DoWriteMailAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>
 * 	This implementation open a chooser to allow users to use the mail composer of their choice<br/>
 * 	This action use {@link EMail} fields To, CC, BCC, Object and Body to populate the mail <br/>
 * 	the From field is not set and depends on email client of the user
 * </p>
 * 
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * 
 * @author dmaurange
 */
public class DoWriteEmailActionImpl implements DoWriteMailAction {

	/** serial id */
	private static final long serialVersionUID = 1734180586147761337L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EMail getEmptyInParameter() {
		return new EMail();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, EMail p_oParameterIn) {
		Intent oEmailIntent = new Intent(android.content.Intent.ACTION_SEND);

		if (p_oParameterIn.getTo() == null) {
			p_oParameterIn.setTo("");
		}
		if (p_oParameterIn.getCC() == null) {
			p_oParameterIn.setCC("");
		}
		if (p_oParameterIn.getBCC() == null) {
			p_oParameterIn.setBCC("");
		}
		if (p_oParameterIn.getObject() == null) {
			p_oParameterIn.setObject("");
		}
		if (p_oParameterIn.getBody() == null) {
			p_oParameterIn.setBody("");
		}
		
		String[] t_sEmailList = { p_oParameterIn.getTo() };
		String[] t_sEmailCCList = { p_oParameterIn.getCC() };
		String[] t_sEmailBCCList = { p_oParameterIn.getBCC() };

		oEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, t_sEmailList);
		oEmailIntent.putExtra(android.content.Intent.EXTRA_CC, t_sEmailCCList);
		oEmailIntent.putExtra(android.content.Intent.EXTRA_BCC, t_sEmailBCCList);

		oEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, p_oParameterIn.getObject());

		oEmailIntent.setType("plain/text");
		oEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, p_oParameterIn.getBody());

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		
		// titre de la fenêtre de choix du client de messagerie
		Intent oIntent = Intent.createChooser(oEmailIntent,
				oApplication.getStringResource(AndroidApplicationR.actiondowriteemail_chooser));
		((AndroidApplication) Application.getInstance()).startActivity(oIntent);

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, EMail p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		// nothing to do

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, EMail p_oParameterIn, NullActionParameterImpl p_oResultOut) {
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
	public void doPreExecute(EMail p_oEMail, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, EMail p_oParameterIn, NullActionParameterImpl p_oParameterOut)
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
