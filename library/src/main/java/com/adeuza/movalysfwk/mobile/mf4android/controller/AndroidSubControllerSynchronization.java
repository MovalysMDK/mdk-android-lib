package com.adeuza.movalysfwk.mobile.mf4android.controller;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaysynchrodialog.SynchronizationDialogParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.ClassicSynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkControllerImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

public class AndroidSubControllerSynchronization extends SubControllerSynchronization {

	protected AndroidSubControllerSynchronization(FwkControllerImpl p_oParent) {
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
		SynchronizationDialogParameterIN oInParameter = new SynchronizationDialogParameterIN();
		oInParameter.synchronisationAction = ClassicSynchronizationAction.class;
		oInParameter.synchronisationParameters = new SynchronizationActionParameterIN();
		oInParameter.synchronisationParameters.firstSynchro = true;
		oInParameter.screen = p_oScreen;

		this.doDisplaySynchronisationDialog(oInParameter);
	}
}
