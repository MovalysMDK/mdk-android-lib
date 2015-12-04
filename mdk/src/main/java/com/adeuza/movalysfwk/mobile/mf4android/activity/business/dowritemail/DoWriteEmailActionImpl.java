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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.dowritemail;

import android.content.Intent;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
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
 * 
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
	public int getConcurrentAction() {
		return Action.NO_QUEUE;
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
