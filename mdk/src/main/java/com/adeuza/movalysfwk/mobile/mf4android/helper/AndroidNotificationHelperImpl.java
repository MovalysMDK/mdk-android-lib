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
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidMMContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Class used to manage Android notifications.</p>
 *
 *
 * @since Barcelone (6 déc. 2010)
 */
public class AndroidNotificationHelperImpl implements AndroidNotificationHelper {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Notification computeNotifications(
			final MContext p_oContext, final ApplicationR p_oIconId, final ApplicationR p_oTrickerTxt, 
			final ApplicationR p_oContentTitle, final ApplicationR p_oContentTxt, final boolean p_bLaunchSynchroOnClick,
			final int... p_iFlags) {

		final AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		
		final Context oNativeContext = ((AndroidMMContext) p_oContext).getAndroidNativeContext();
		
		final Notification oNotification;
		
		PendingIntent oContentIntent = PendingIntent.getActivity(oNativeContext, 0, new Intent(), 0);
		
		// initialisation de la notification		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(oNativeContext);
		oNotification = builder
				.setSmallIcon(oApplication.getAndroidIdByRKey(p_oIconId))
				.setTicker(oApplication.getStringResource(p_oTrickerTxt))
				.setWhen(System.currentTimeMillis())
				.setContentIntent(oContentIntent)
				.build();
		
		// ajout des flags s'il y a
		for (int iFlag : p_iFlags) {
			oNotification.flags |= iFlag;
		}

		return oNotification;
	}
}
