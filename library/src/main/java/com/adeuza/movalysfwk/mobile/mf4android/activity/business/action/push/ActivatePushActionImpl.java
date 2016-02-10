package com.adeuza.movalysfwk.mobile.mf4android.activity.business.action.push;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4android.service.PushService;
import com.adeuza.movalysfwk.mobile.mf4android.service.PushServiceImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.push.ActivatePushAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Implements the action to activate the push function on Movalys.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (30 nov. 2010)
 */
public class ActivatePushActionImpl extends AbstractTaskableAction<NullActionParameterImpl, NullActionParameterImpl, DefaultActionStep, Void> implements ActivatePushAction {

	/** serial id */
	private static final long serialVersionUID = 2565392417874269974L;

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
	public NullActionParameterImpl doAction(MContext p_oContext, NullActionParameterImpl p_oParameterIn) {
		//démarrage du service de Push
		Context context = ((AndroidContextImpl) p_oContext).getAndroidNativeContext();
		
		// get the alarm manager
		AlarmManager am = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
		
		// create the pending intent 
		Intent oIntent= new Intent(context.getApplicationContext(), PushServiceImpl.class);
		am.setRepeating(AlarmManager.RTC_WAKEUP, 
				Application.getInstance().getController().getAutoSyncTime(), 
				Application.getInstance().getController().getAutoSyncTime(), 
				PendingIntent.getService(
						context.getApplicationContext(), 
						PushService.ALARM_REQUEST_CODE, 
						oIntent, 
						PendingIntent.FLAG_UPDATE_CURRENT
						)
						
				);
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
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
	public void doPreExecute(NullActionParameterImpl p_oParameterIn, MContext p_oContext) throws ActionException {
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
