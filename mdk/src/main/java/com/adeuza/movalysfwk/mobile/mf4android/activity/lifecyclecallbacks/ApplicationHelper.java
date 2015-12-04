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
package com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks;

import android.app.Application;

/**
 * Helper for accessing {@link Application#registerActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks)} and
 * {@link Application#unregisterActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks)} introduced in API level 14 in a
 * backwards compatible fashion.<br/>
 * When running on API level 14 or above, the framework's implementations of these methods will be used.
 */
public final class ApplicationHelper {

	/**
	 * unaccessible constructor
	 */
	private ApplicationHelper(){
	}
	
	/**
     * Registers a callback to be called following the life cycle of the application's {@link android.app.Activity activities}.
     * 
     * @param p_oApplication The application with which to register the callback.
     * @param p_oCallback The callback to register.
     */
    public static void registerActivityLifecycleCallbacks(Application p_oApplication, ActivityLifecycleCallbacksCompat p_oCallback) {
        preIcsRegisterActivityLifecycleCallbacks(p_oCallback);
    }

    /**
     * pre-register activity lifecycle callbacks
     * @param p_oCallback activity lifecycle callback
     */
    private static void preIcsRegisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat p_oCallback) {
        MainLifecycleDispatcher.get().registerActivityLifecycleCallbacks(p_oCallback);
    }

    /**
     * Unregisters a previously registered callback.
     * 
     * @param p_oApplication The application with which to unregister the callback.
     * @param p_oCallback The callback to unregister.
     */
    public static void unregisterActivityLifecycleCallbacks(Application p_oApplication, ActivityLifecycleCallbacksCompat p_oCallback) {
        preIcsUnregisterActivityLifecycleCallbacks(p_oCallback);
    }

    /**
     * unregister activity lifecycle callback
     * @param p_oCallback The callback to unregister.
     */
    private static void preIcsUnregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat p_oCallback) {
        MainLifecycleDispatcher.get().unregisterActivityLifecycleCallbacks(p_oCallback);
    }
}
