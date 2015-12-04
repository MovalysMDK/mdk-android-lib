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
package com.adeuza.movalysfwk.mobile.mf4android.controller;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaysynchrodialog.SynchronizationDialogParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractFwkControllerImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerTransverse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Android Application's controller implementation</p>
 */
public class AndroidFwkControllerImpl extends AbstractFwkControllerImpl {

	@Override
	protected AbstractSubControllerSynchronization createSubControllerSynchronization() {
		return new AndroidSubControllerSynchronization(this);
	}
	
	@Override
	protected SubControllerTransverse createSubControllerTransverse() {
		return new AndroidSubControllerTransverse(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launchActionByActionTask(Screen p_oParent, Action<?,?,?,?>  p_oAction, Class<? extends Action<?,?,?,?>>  p_oInterfaceClass, ActionParameter p_oParameterIn, Object p_oSource) {
		super.launchActionByActionTask(p_oParent, p_oAction, p_oInterfaceClass, p_oParameterIn, p_oSource);
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

	@Override
	public boolean hasData(MContext p_oContext) throws SynchroException {
		return false;
	}
}
