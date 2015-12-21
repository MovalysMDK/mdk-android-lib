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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.displayparameterdialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayparameterdialog.DisplayParameterDialogAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;
import com.sopragroup.mobility.mdk.R;

/**
 * <p>
 * 	Action to display the activity that asks the user to configure the Movalys application.
 * </p>
 *
 *
 * @since Barcelone (22 nov. 2010)
 */
public class DisplayParameterDialogActionImpl implements DisplayParameterDialogAction {

	/** serial id */
	private static final long serialVersionUID = 2678703814790871832L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl getEmptyInParameter() {
		return new NullActionParameterImpl();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doAction(final MContext p_oContext, NullActionParameterImpl p_oParameterIn) {

		final Activity activity = AndroidApplication.getInstance().getCurrentActivity();


		AlertDialog alert = new AlertDialog.Builder(activity)
				.setMessage(R.string.screen_redirect_to_parameter_text)
				.setTitle(R.string.activityname_parameter)
				.setPositiveButton(R.string.screen_redirect_to_parameter_param_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Application.getInstance().getController().doDisplaySetting((Screen) activity);
					}
				})
				.setNegativeButton(R.string.screen_redirect_to_parameter_stop_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AndroidApplication.getInstance().getApplication().launchStopApplication();
					}
				})
				.show();
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing To Do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
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
	public void doPreExecute(NullActionParameterImpl p_oNullActionParameterImpl, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oParameterOut)
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
