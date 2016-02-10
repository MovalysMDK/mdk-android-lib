package com.adeuza.movalysfwk.mobile.mf4android.helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidMMContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Class used to manage Android notifications.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
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
		
		// récupération du contenu
//		int iIcon = p_iIconId;
//		String sTickerText = p_oNativeContext.getString(p_iTrickerTxtId);
//		long lWhen = System.currentTimeMillis();
//		String sContentTitle = p_oNativeContext.getString(p_iContentTitleId);
//		String sContentText = p_oNativeContext.getString(p_iContentTxtId);

		// initialisation de la notification
		final Notification oNotification = new Notification(oApplication.getAndroidIdByRKey(p_oIconId),
				oApplication.getStringResource(p_oTrickerTxt), System.currentTimeMillis());

		final Context oNativeContext = ((AndroidMMContext) p_oContext).getAndroidNativeContext();

		// lors du clic sur la notif on lance l'activité de synchronisation
		PendingIntent oContentIntent = PendingIntent.getActivity(oNativeContext, 0, new Intent(), 0);

		// the next two lines initialize the Notification, using the configurations above
		oNotification.setLatestEventInfo(oNativeContext, oApplication.getStringResource(p_oContentTitle),
				oApplication.getStringResource(p_oContentTxt), oContentIntent);

		// ajout des flags s'il y a
		for (int iFlag : p_iFlags) {
			oNotification.flags |= iFlag;
		}

		return oNotification;
	}
}
