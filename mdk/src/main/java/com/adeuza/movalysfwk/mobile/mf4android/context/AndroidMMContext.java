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
package com.adeuza.movalysfwk.mobile.mf4android.context;

import android.content.Context;
import android.content.SharedPreferences;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Describes Android context</p>
 *
 *
 */
public interface AndroidMMContext extends MContext {

	/**
	 * Returns the shared preferences of an android application. Never null.
	 * 
	 * @return
	 * 		The shared preferences of an android application. Never null.
	 */
	public SharedPreferences getSharedPreferences();

	/**
	 * <p>Return the native android <em>context</em></p>
	 * @return Objet context
	 */
	public Context getAndroidNativeContext();
	
	/**
	 * add a business event based on the first event
	 * 
	 * @param p_bExitMode exit mode
	 */
	public void addBusinessEventFromFirstEvent(boolean p_bExitMode);

}
