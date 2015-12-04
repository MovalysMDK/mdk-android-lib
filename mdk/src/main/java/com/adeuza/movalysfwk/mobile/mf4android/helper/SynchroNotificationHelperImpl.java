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
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * <p>
 *  Helper class to compute all synchronization's notification messages.
 * </p>
 * 
 * 
 * @since Barcelone (3 déc. 2010)
 */
public final class SynchroNotificationHelperImpl extends AndroidNotificationHelperImpl implements SynchroNotificationHelper {

	/** Id of the notification content for the sync data */
	private static final int SYNCHRONIZATION_NOTIFICATION_ID_DATA = 2;
	/** id of the notification content for the sync state */
	private static final int SYNCHRONIZATION_NOTIFICATION_ID_SYNC = 1;
	/** the notification manager */
	private NotificationManager notificationManager = null;

	/**
	 * Construct an object <em>SynchroNotificationHelper</em>.
	 */
	public SynchroNotificationHelperImpl() {
		AndroidApplication oAndroidApp = (AndroidApplication) Application.getInstance();
		this.notificationManager = (NotificationManager) oAndroidApp.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void killSynchroNotification() {
		//suppression de la notification de synchronisation
		this.notificationManager.cancel(SYNCHRONIZATION_NOTIFICATION_ID_DATA);
		this.notificationManager.cancel(SYNCHRONIZATION_NOTIFICATION_ID_SYNC);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeSynchronizationNotification(final MContext p_oContext, int p_iSynchroState, boolean p_bIsMM2BONotif, boolean p_bIsBO2MMNotif, boolean p_bStateSyncChanged) {
		boolean bAlert2 = false;
		ApplicationR iDrawableData	= null;
		ApplicationR iTrickerText	= null;
		final ApplicationR iContentTitle = AndroidApplicationR.application_name;
		ApplicationR iContentText	= null;
		boolean bStartSynchroOnNotificationClick = false ;

		AndroidNotificationHelper oHelper = BeanLoader.getInstance().getBean(AndroidNotificationHelper.class);

		if (p_bStateSyncChanged) {
			if (p_iSynchroState == AbstractSubControllerSynchronization.STATE_SYNC_ONGOING) {
				iDrawableData =  AndroidApplicationR.synchronize_green_status;
				iTrickerText = AndroidApplicationR.ongoing_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ongoing_synchro_notification_content_text;
			}
			else if (p_iSynchroState == AbstractSubControllerSynchronization.STATE_SYNC_LAST_OK) {
				iDrawableData = AndroidApplicationR.synchronize_green_status; //A2A_DEV A changer
				iTrickerText = AndroidApplicationR.ok_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ok_synchro_notification_content_text;
			}
			else if (p_iSynchroState == AbstractSubControllerSynchronization.STATE_SYNC_KO_CRASH) {
				iDrawableData = AndroidApplicationR.synchronize_black_status;
				iTrickerText = AndroidApplicationR.crash_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.crash_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == AbstractSubControllerSynchronization.STATE_SYNC_KO_AUTHENTIFICATION) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = AndroidApplicationR.ko_authentification_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ko_authentification_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == AbstractSubControllerSynchronization.STATE_SYNC_KO_LICENCE) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = AndroidApplicationR.ko_licence_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ko_licence_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == AbstractSubControllerSynchronization.STATE_SYNC_KO_CONNECTED) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = AndroidApplicationR.ko_connected_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ko_connected_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == AbstractSubControllerSynchronization.STATE_SYNC_KO_JOIN_SERVER) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = AndroidApplicationR.ko_join_synchro_notification_tricker_text;//A2A_DEV A changer
				iContentText = AndroidApplicationR.ko_join_synchro_notification_content_text;//A2A_DEV A changer
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == AbstractSubControllerSynchronization.STATE_SYNC_KO_INCOMPATIBLE_SERVER_TIME) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = DefaultApplicationR.ko_incompatible_time_synchro_notification_tricker_text;
				iContentText = DefaultApplicationR.ko_incompatible_time_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			//création de la notification
			Notification oNotification2 = oHelper.computeNotifications(
					p_oContext, iDrawableData, iTrickerText, iContentTitle, iContentText, 
					bStartSynchroOnNotificationClick, Notification.FLAG_INSISTENT);

			// Delete old notification
			this.notificationManager.cancel(SYNCHRONIZATION_NOTIFICATION_ID_SYNC);
			
			//affichage de la notification
			this.notificationManager.notify(SYNCHRONIZATION_NOTIFICATION_ID_SYNC, oNotification2);
		}
		
		if (p_bIsMM2BONotif && p_bIsBO2MMNotif) {
			iDrawableData = AndroidApplicationR.synchronize_blue_orange;
			iTrickerText = AndroidApplicationR.launch_synchro_double_notification_tricker_text; 
			iContentText = AndroidApplicationR.launch_synchro_double_notification_content_text;
			bAlert2 = true;
		}
		else if (p_bIsMM2BONotif) {
			iDrawableData = AndroidApplicationR.synchronize_blue;
			iTrickerText = AndroidApplicationR.launch_synchro_mm2bo_notification_tricker_text; 
			iContentText = AndroidApplicationR.launch_synchro_mm2bo_notification_content_text;
			bAlert2 = true;
		}
		else if (p_bIsBO2MMNotif) {
			iDrawableData = AndroidApplicationR.synchronize_orange;
			iTrickerText = AndroidApplicationR.launch_synchro_bo2mm_notification_tricker_text; 
			iContentText = AndroidApplicationR.launch_synchro_bo2mm_notification_content_text;
			bAlert2 = true;
		}
		
		// Delete old notification
		this.notificationManager.cancel(SYNCHRONIZATION_NOTIFICATION_ID_DATA);
		
		if (bAlert2) {
			//création de la notification
			Notification oNotification = oHelper.computeNotifications(
					p_oContext, iDrawableData, iTrickerText, iContentTitle, iContentText, 
					bStartSynchroOnNotificationClick,Notification.FLAG_INSISTENT);

			//affichage de la notification
			this.notificationManager.notify(SYNCHRONIZATION_NOTIFICATION_ID_DATA, oNotification);
		}
	}
}
