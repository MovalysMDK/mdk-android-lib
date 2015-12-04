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
import android.app.NotificationManager;
import android.content.Context;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * <p>Helper permettant d'afficher un message en cas de violation d'une règle de gestion centralisée sur les actions.</p>
 * 
 * 
 */
public final class ActionRuleNotificationHelperImpl extends AndroidNotificationHelperImpl implements ActionRuleNotificationHelper {

	private static final int ACTIONRULE_NOTIFICATION_ID = 1;

	/** the notification manager */
	private NotificationManager notificationManager = null;

	/**
	 * Construct an object <em>SynchroNotificationHelper</em>.
	 */
	public ActionRuleNotificationHelperImpl() {
		
		this.notificationManager = 
			(NotificationManager) ((AndroidApplication) Application.getInstance()).getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void killActionRuleNotification() {

		// Suppression de la notification
		this.notificationManager.cancel(ACTIONRULE_NOTIFICATION_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeActionRuleNotification(MContext p_oContext, ApplicationR p_oDrawableData, ApplicationR p_oTrickerText,
			ApplicationR p_oContentTitle, ApplicationR p_oContentText) {

		AndroidNotificationHelper oHelper = BeanLoader.getInstance().getBean(AndroidNotificationHelper.class);

		// Création de la notification
		Notification oNotification = oHelper.computeNotifications(
				p_oContext, 
				p_oDrawableData,
				p_oTrickerText, 
				p_oContentTitle,
				p_oContentText, 
				false,
				Notification.FLAG_INSISTENT);	// FIXME flags ?
		
		// Affichage de la notification
		this.notificationManager.notify(ACTIONRULE_NOTIFICATION_ID, oNotification);
	}
}
