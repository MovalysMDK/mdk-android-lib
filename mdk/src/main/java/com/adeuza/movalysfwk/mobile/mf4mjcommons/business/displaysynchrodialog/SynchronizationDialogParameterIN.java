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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaysynchrodialog;

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;


/**
 * <p>Parameter of DisplaySynchronisationDialog</p>
 *
 * <p>This class will be serialized so attributs should be simple types.</p>
 *
 */
public class SynchronizationDialogParameterIN extends AbstractActionParameter implements ActionParameter {

	/** serial id */
	private static final long serialVersionUID = -4878165802872910165L;

	/** actions to redirect */
	public List<String> actionsToRedirectAfterSynchronisation = null;

	/** action used to perform the sync. */
	public Class<? extends SynchronizationAction> synchronisationAction = null;
	
	/** parameter of the sync action */
	public SynchronizationActionParameterIN synchronisationParameters = null;
	
	/** redirection d'action dans le cas de l'appel d'une action affichant une activité apèrs un clic dans le menu */
	public Class<? extends Screen> actionAfterSynchronisationFromMenu = null; 
	
	/** Is empty database */
	public boolean isEmptyDB = false;

	/** L'écran sur lequel l'affichage de la popup de synchro doit être opéré. */
	public Screen screen;
	
	/**
	 * Asynchronous
	 */
	public boolean async = true ;
}
