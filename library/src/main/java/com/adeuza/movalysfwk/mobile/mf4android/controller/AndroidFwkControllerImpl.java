package com.adeuza.movalysfwk.mobile.mf4android.controller;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.helper.SynchroNotificationHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaysynchrodialog.SynchronizationDialogParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkControllerImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerTransverse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Android Application's controller implementation</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Annapurna
 */
public class AndroidFwkControllerImpl extends FwkControllerImpl {

	@Override
	protected SubControllerSynchronization createSubControllerSynchronization() {
		return new AndroidSubControllerSynchronization(this);
	}
	
	@Override
	protected SubControllerTransverse createSubControllerTransverse() {
		return new AndroidSubControllerTransverse(this);
	}	
	
	/**
	 * Notify an Android sync.
	 */
	@Override
	protected void notifySynchro(final MContext p_oContext) {
		//affichage de la notification
		SynchroNotificationHelper oHelper = BeanLoader.getInstance().getBean(SynchroNotificationHelper.class);
		oHelper.computeSynchronizationNotification(p_oContext, this.getSyncState(), this.isMM2BONotif(), this.isBO2MMNotif(),
				this.isSyncStateChanged());
	}

	/**
	 * Launch an action in action task (in other thread)
	 * @param p_oParent screen parent who launched the action
	 * @param p_oAction action to launch
	 * @param p_oParameterIn in parameter
	 * @return the task launched
	 */
	@Override
	public MMActionTask launchActionByActionTask(Screen p_oParent, Action<?,?,?,?>  p_oAction, Class<? extends Action<?,?,?,?>>  p_oInterfaceClass, ActionParameter p_oParameterIn, Object p_oSource) {
		MMActionTask r_oActionTask = super.launchActionByActionTask(p_oParent, p_oAction, p_oInterfaceClass, p_oParameterIn, p_oSource);
		((AbstractMMActivity) p_oParent).setCurrentActionTask(r_oActionTask);
		return r_oActionTask;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doDisplaySynchronisationDialog(Class<? extends SynchronizationAction> p_oAction, SynchronizationActionParameterIN p_oSynchronisationParameters) {
		
		SynchronizationDialogParameterIN oSynchroDialogParameter = new SynchronizationDialogParameterIN();
		oSynchroDialogParameter.synchronisationAction = p_oAction;
		oSynchroDialogParameter.synchronisationParameters = p_oSynchronisationParameters;
		oSynchroDialogParameter.screen = (Screen)((AndroidApplication) Application.getInstance()).getCurrentActivity();
		this.doDisplaySynchronisationDialog(oSynchroDialogParameter);
	}
}
