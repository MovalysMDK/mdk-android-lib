package com.adeuza.movalysfwk.mobile.mf4android.helper;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Helper permettant d'afficher un message en cas de violation d'une règle de gestion centralisée sur les actions.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author ktilhou
 */
public interface ActionRuleNotificationHelper extends AndroidNotificationHelper {

	/**
	 * <p>Return the object <em>notificationManager</em></p>
	 */
	public void killActionRuleNotification();
	
	/**
	 * <p>Compute current notifications.</p>
	 * 
	 * @param p_oContext The context to use
	 * @param p_oDrawableData The icon identifier to use
	 * @param p_oTrickerText The tricker text to use
	 * @param p_oContentTitle The content title id to use
	 * @param p_oContentText The content text id to use
	 */
	public void computeActionRuleNotification(final MContext p_oContext, final ApplicationR p_oDrawableData, final ApplicationR p_oTrickerText,
			final ApplicationR p_oContentTitle, final ApplicationR p_oContentText);
}
