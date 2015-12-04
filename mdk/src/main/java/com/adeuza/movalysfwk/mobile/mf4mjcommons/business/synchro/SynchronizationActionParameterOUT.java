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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>Contains the notify action to do</p>
 *
 *
 */
public class SynchronizationActionParameterOUT extends AbstractActionParameter implements ActionParameter {

	/** serial id */
	private static final long serialVersionUID = -3615674818900183022L;
	
	/** no object to synchronized */
	public boolean resetObjectToSynchronise = true;
	
	/** authentification local */
	public boolean localAuthentication = false;
	
	/** empty database synchronization failure */
	public boolean emptyDBSynchronizationFailure = false;
	
	/** no connection to the server failure */
	public boolean noConnectionSynchronizationFailure = false;
	
	/** interruption of the synchronization failure */
	public boolean brokenSynchronizationFailure = false;
	
	/** error in the synchronization failure */
	public boolean errorInSynchronizationFailure = false;
	
	/** error in the synchronization failure */
	public boolean authenticationFailure = false;
	
	/** user waited too long before synchronizing */
	public boolean waitedTooLongBeforeSync = false;

	/** user waited too long before synchronizing */
	public boolean incompatibleServerMobileTimeFailure = false;
	
	public SynchronisationResponseTreatmentInformation informations;
	
	/** Action to launch after the synchronization. */
	public Class<? extends Screen> nextScreen;
}
