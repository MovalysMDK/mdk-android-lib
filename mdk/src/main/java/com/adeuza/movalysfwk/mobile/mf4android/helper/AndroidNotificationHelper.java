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

import android.app.Notification;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Interface of the class used to manage Android notifications.</p>
 *
 *
 * @since fwk-Annapurna
 */
public interface AndroidNotificationHelper {

	/**
	 * <p>
	 * Private method that create a notification.
	 * </p>
	 * 
	 * @param p_oNativeContext
	 *            The Android context
	 * 
	 * @param p_iIconId
	 *            The icon identifier to use
	 * 
	 * @param p_iTrickerTxtId
	 *            The tricker text to use
	 * 
	 * @param p_iContentTitleId
	 *            The content title to use
	 * 
	 * @param p_iContentTxtId
	 *            The content text to use
	 * 
	 * @param p_oNotificationIntent
	 *            The intent dispatch when you clic on the notification
	 * 
	 * @param p_iFlags
	 *            The flags to add to the notification
	 * 
	 * @return a new <em>Notification</em> object
	 */
	public Notification computeNotifications(
			final MContext p_oContext, final ApplicationR p_oIcon, final ApplicationR p_oTrickerTxt, 
			final ApplicationR p_oContentTitle, final ApplicationR p_oContentTxt, final boolean p_bLaunchSynchroOnClick,
			final int... p_iFlags);

}
