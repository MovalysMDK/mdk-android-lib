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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Helper permettant d'afficher un message en cas de violation d'une règle de gestion centralisée sur les actions.</p>
 *
 *
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
