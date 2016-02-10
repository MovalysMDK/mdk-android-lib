/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2013 Benoit 'BoD' Lubek (BoD@JRAF.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks;

import android.app.Application;

/**
 * Helper for accessing {@link Application#registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks)} and
 * {@link Application#unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks)} introduced in API level 14 in a
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
     * Registers a callback to be called following the life cycle of the application's {@link Activity activities}.
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