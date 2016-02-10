package com.adeuza.movalysfwk.mobile.mf4android.controller;

import android.app.Activity;
import android.content.Intent;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayexitdialog.DisplayExitDialogAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.resetdatabase.ResetDataBaseAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkControllerImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerTransverse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

public class AndroidSubControllerTransverse extends SubControllerTransverse {

	protected AndroidSubControllerTransverse(FwkControllerImpl p_oParent) {
		super(p_oParent);
	}

	/**
	 * Action that reset the database.
	 */
	@Override
	public void doResetDataBase( Screen p_oScreen) {
		this.getController().launchActionByActionTask(
				p_oScreen, ResetDataBaseAction.class, null, null);
	}
	
	/**
	 * Launch the actioin to display the exit dialog.
	 */
	@Override	
	public void doDisplayExitApplicationDialog( Screen p_oScreen ) {
		this.getController().launchActionByActionTask(p_oScreen, DisplayExitDialogAction.class, null, null);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerTransverse#doDisplayMain(com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen)
	 */
	@Override
	public void doDisplayMain(Screen p_oScreen) {
		Activity oActivity = ((Activity) p_oScreen);
		String sPackage = oActivity.getPackageName();
		String sAction = sPackage + ".action.PROJECT_MAIN";
		String sCategory = sPackage + ".category.MF4A";
		Intent oIntent = new Intent(sAction);
		oIntent.addCategory( sCategory );	
		oActivity.startActivity(oIntent);
	}
}
