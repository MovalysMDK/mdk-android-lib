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
package com.adeuza.movalysfwk.mobile.mf4android.helper;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Helper pour la synchronisation coté android.</p>
 *
 *
 */
public interface SynchroNotificationHelper extends AndroidNotificationHelper {

	/**
	 * <p>Return the object <em>notificationManager</em></p>
	 */
	public void killSynchroNotification();
	
	/**
	 * <p>Compute current notifications.</p>
	 * @param p_oNativeContext the context to use
	 * @param p_iSynchroState the synchro state
	 * @param p_bIsMM2BONotif true whether data to send
	 * @param p_bIsBO2MMNotif true whether data to receive
	 */
	public void computeSynchronizationNotification(final MContext p_oContext, 
			int p_iSynchroState, boolean p_bIsMM2BONotif, boolean p_bIsBO2MMNotif, boolean p_bStateSyncChanged);
	
}
