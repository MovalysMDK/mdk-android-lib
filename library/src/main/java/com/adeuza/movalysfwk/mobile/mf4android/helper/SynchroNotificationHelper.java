package com.adeuza.movalysfwk.mobile.mf4android.helper;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Helper pour la synchronisation coté android.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
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
