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

import android.app.Activity;
import android.content.Intent;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayexitdialog.DisplayExitDialogAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.resetdatabase.ResetDataBaseAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.FwkController;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerTransverse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

public class AndroidSubControllerTransverse extends SubControllerTransverse {

	protected AndroidSubControllerTransverse(FwkController p_oParent) {
		super(p_oParent);
	}

	/**
	 * Action that reset the database.
	 */
	@Override
	public void doResetDataBase( Screen p_oScreen) {
		this.getController().launchActionByActionTask(
				p_oScreen, ResetDataBaseAction.class, null, p_oScreen);
	}
	
	/**
	 * Launch the actioin to display the exit dialog.
	 */
	@Override	
	public void doDisplayExitApplicationDialog( Screen p_oScreen ) {
		this.getController().launchActionByActionTask(p_oScreen, DisplayExitDialogAction.class, null, p_oScreen);
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
