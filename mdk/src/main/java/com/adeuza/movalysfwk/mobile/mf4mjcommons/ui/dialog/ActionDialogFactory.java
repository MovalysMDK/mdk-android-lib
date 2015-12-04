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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>TODO Décrire la classe ProgressDialogFactory</p>
 *
 *
 */
@Scope(ScopePolicy.SINGLETON)
public interface ActionDialogFactory {

	/**
	 * TODO Décrire la méthode createProgessDialog de la classe ActionDialogFactory
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 * @param p_oActionTask a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ProgressDialog} object.
	 */
	public ProgressDialog createProgressDialog(MContext p_oContext, MMActionTask<?, ?, ?, ?> p_oActionTask);

	/**
	 * TODO Décrire la méthode createPreExecuteDialog de la classe ActionDialogFactory
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_oScreen a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen} object.
	 * @param p_oActionTask a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog} object.
	 */
	public Dialog createPreExecuteDialog(MContext p_oContext, Screen p_oScreen, MMActionTask<?, ?, ?, ?> p_oActionTask);
	
	/**
	 * Destroy progress dialog
	 * @param p_oDialog progress dialog
	 * @param p_oActionTask action task
	 * @param p_sScreenIdentifier screen identifier
	 */
	public void destroyProgressDialog( MMActionTask<?, ?, ?, ?> p_oActionTask );
	
	/**
	 * Return dialog tag for async task
	 * @param p_oActionTask dialog task
	 * @return dialog tag
	 */
	public String getDialogTag( MMActionTask<?, ?, ?, ?> p_oActionTask );
}
