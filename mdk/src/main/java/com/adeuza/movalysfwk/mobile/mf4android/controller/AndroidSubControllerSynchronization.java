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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaysynchrodialog.SynchronizationDialogParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ClassicSynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkController;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

public class AndroidSubControllerSynchronization extends AbstractSubControllerSynchronization {

	protected AndroidSubControllerSynchronization(FwkController p_oParent) {
		super(p_oParent);
	}

	/**
	 * Method that launch a sync.
	 */
	@Override
	protected void doClassicSynchronisationFromMenu( Screen p_oScreen, Class<? extends Screen> p_oNextScreen) {
		SynchronizationDialogParameterIN oSynchroDialogParameter = new SynchronizationDialogParameterIN();
		oSynchroDialogParameter.synchronisationAction = ClassicSynchronizationAction.class;
		oSynchroDialogParameter.synchronisationParameters	= new SynchronizationActionParameterIN();
		oSynchroDialogParameter.synchronisationParameters.nextScreen = p_oNextScreen;
		oSynchroDialogParameter.screen = p_oScreen;

		this.doDisplaySynchronisationDialog(oSynchroDialogParameter);
	}

	/**
	 * <p>
	 * 	A2A_DOC EMA Describe method <em>doFirstSynchronisation</em> of class <em>SubControllerSynchronization</em>.
	 * </p>
	 */
	@Override	
	protected void doFirstSynchronisation( Screen p_oScreen ) {
		doFirstSynchronisation(p_oScreen, true );
	}

	@Override
	protected void doFirstSynchronisation(Screen p_oScreen, boolean p_bAsync) {
		SynchronizationDialogParameterIN oInParameter = new SynchronizationDialogParameterIN();
		oInParameter.synchronisationAction = ClassicSynchronizationAction.class;
		oInParameter.synchronisationParameters = new SynchronizationActionParameterIN();
		oInParameter.synchronisationParameters.firstSynchro = true;
		oInParameter.screen = p_oScreen;
		oInParameter.async = p_bAsync;

		this.doDisplaySynchronisationDialog(oInParameter);
	}
}
