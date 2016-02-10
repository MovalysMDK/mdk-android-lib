package com.adeuza.movalysfwk.mobile.mf4android.helper;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * <p>
 *  Helper class to compute all synchronization's notification messages.
 * </p>
 * 
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * 
 * @author fbourlieux
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
		boolean bPush = false;
		ApplicationR iDrawableData	= null;
		ApplicationR iTrickerText	= null;
		final ApplicationR iContentTitle = AndroidApplicationR.application_name;
		ApplicationR iContentText	= null;
		boolean bStartSynchroOnNotificationClick = false ;

		AndroidNotificationHelper oHelper = BeanLoader.getInstance().getBean(AndroidNotificationHelper.class);

		if (p_bStateSyncChanged) {
			if (p_iSynchroState == SubControllerSynchronization.STATE_SYNC_ONGOING) {
				iDrawableData =  AndroidApplicationR.synchronize_green_status;
				iTrickerText = AndroidApplicationR.ongoing_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ongoing_synchro_notification_content_text;
			}
			else if (p_iSynchroState == SubControllerSynchronization.STATE_SYNC_LAST_OK) {
				iDrawableData = AndroidApplicationR.synchronize_green_status; //A2A_DEV A changer
				iTrickerText = AndroidApplicationR.ok_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ok_synchro_notification_content_text;
			}
			else if (p_iSynchroState == SubControllerSynchronization.STATE_SYNC_KO_CRASH) {
				iDrawableData = AndroidApplicationR.synchronize_black_status;
				iTrickerText = AndroidApplicationR.crash_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.crash_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == SubControllerSynchronization.STATE_SYNC_KO_AUTHENTIFICATION) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = AndroidApplicationR.ko_authentification_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ko_authentification_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == SubControllerSynchronization.STATE_SYNC_KO_LICENCE) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = AndroidApplicationR.ko_licence_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ko_licence_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == SubControllerSynchronization.STATE_SYNC_KO_CONNECTED) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = AndroidApplicationR.ko_connected_synchro_notification_tricker_text;
				iContentText = AndroidApplicationR.ko_connected_synchro_notification_content_text;
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == SubControllerSynchronization.STATE_SYNC_KO_JOIN_SERVER) {
				iDrawableData = AndroidApplicationR.synchronize_red_status;
				iTrickerText = AndroidApplicationR.ko_join_synchro_notification_tricker_text;//A2A_DEV A changer
				iContentText = AndroidApplicationR.ko_join_synchro_notification_content_text;//A2A_DEV A changer
				bStartSynchroOnNotificationClick = false ;
			}
			else if (p_iSynchroState == SubControllerSynchronization.STATE_SYNC_KO_INCOMPATIBLE_SERVER_TIME) {
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
			bPush = true;
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
			bPush = true;
		}
		
		// Delete old notification
		this.notificationManager.cancel(SYNCHRONIZATION_NOTIFICATION_ID_DATA);
		
		if (bAlert2) {
			//création de la notification
			Notification oNotification = oHelper.computeNotifications(
					p_oContext, iDrawableData, iTrickerText, iContentTitle, iContentText, 
					bStartSynchroOnNotificationClick,Notification.FLAG_INSISTENT);
			
			if (bPush) {
				// Push Ringtone
				boolean bPushActivation = false ;
				if ( Application.getInstance().getBooleanSetting(Application.SETTING_PUSH) != null ){
					bPushActivation = Application.getInstance().getBooleanSetting(Application.SETTING_PUSH).booleanValue() ;  
				}
				
				String sPushRingtone = Application.getInstance().getStringSetting(Application.SETTING_PUSH_RINGTONE);
				
				if (bPushActivation && sPushRingtone != null ) {
					oNotification.sound = Uri.parse(sPushRingtone);
					oNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
				}
			}
			
			//affichage de la notification
			this.notificationManager.notify(SYNCHRONIZATION_NOTIFICATION_ID_DATA, oNotification);
		}
	}
}
