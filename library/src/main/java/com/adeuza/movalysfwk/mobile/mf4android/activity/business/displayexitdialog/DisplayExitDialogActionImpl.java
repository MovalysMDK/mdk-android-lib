package com.adeuza.movalysfwk.mobile.mf4android.activity.business.displayexitdialog;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.application.MFApplicationHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMCustomDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayexitdialog.DisplayExitDialogAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>This action launch a dialog that ask the user if he really want to quit the application.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (16 déc. 2010)
 */
public class DisplayExitDialogActionImpl extends AbstractTaskableAction<NullActionParameterImpl, NullActionParameterImpl, DefaultActionStep, Void> 
	implements DisplayExitDialogAction {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void doPreExecute(NullActionParameterImpl p_oNullActionParameterImpl, MContext p_oContext) throws ActionException {
		//
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doAction(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, NullActionParameterImpl p_oParam) throws ActionException {
		return new NullActionParameterImpl();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oParameterOut)
			throws ActionException {
		FragmentActivity activity = (FragmentActivity) ((AndroidApplication) Application.getInstance()).getCurrentVisibleActivity();
		// lancement de la fenêtre de dialog pour valider ou non la fermeture de l'application
		MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder(activity);
		oBuilder.setTitle(AndroidApplicationR.close_application_popup_title);
		oBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		oBuilder.setMessage(AndroidApplicationR.close_application_popup_text);
		oBuilder.setPositiveButton(AndroidApplicationR.close_application_popup_OK_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface p_oDialog, int p_iId) {
						// on ferme la popup
						p_oDialog.dismiss();
						DisplayExitDialogActionImpl.this.doOnCloseApplication();
					}
				});
		oBuilder.setNegativeButton(AndroidApplicationR.close_application_popup_KO_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface p_oDialog, int p_iId) {
						// fermeture de la popup
						p_oDialog.cancel();
					}
				});
		MMCustomDialogFragment oAlert = oBuilder.create();
		oAlert.setCancelable(false);
		oAlert.show(activity.getSupportFragmentManager(), MMCustomDialogFragment.CUSTOM_DIALOG_TAG);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doOnSuccess(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) throws ActionException {
		//do nothing
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doOnError(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut)
			throws ActionException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isDataBaseAccessAction()
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isWritableDataBaseAccessAction()
	 */
	@Override
	public boolean isWritableDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isConcurrentAction()
	 */
	@Override
	public boolean isConcurrentAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#getEmptyInParameter()
	 */
	@Override
	public NullActionParameterImpl getEmptyInParameter() {
		return new NullActionParameterImpl();
	}

	/**
	 * 
	 */
	protected void doOnCloseApplication() {
		MFApplicationHolder.getInstance().getApplication().launchStopApplication();
	}
}
