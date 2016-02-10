package com.adeuza.movalysfwk.mobile.mf4android.helper;

import android.app.Notification;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Interface of the class used to manage Android notifications.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
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
