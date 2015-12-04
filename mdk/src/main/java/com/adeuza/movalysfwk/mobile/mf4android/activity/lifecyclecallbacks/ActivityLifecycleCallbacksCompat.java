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

import android.app.Activity;
import android.os.Bundle;

/**
 * Equivalent of {@link android.app.Application.ActivityLifecycleCallbacks} to be used with
 * {@link ApplicationHelper#registerActivityLifecycleCallbacks(Application, ActivityLifecycleCallbacksCompat)} and
 * {@link ApplicationHelper#unregisterActivityLifecycleCallbacks(Application, ActivityLifecycleCallbacksCompat)}.
 */
public interface ActivityLifecycleCallbacksCompat {
	/**
	 * on activity created
	 * @param p_oActivity activity
	 * @param p_oSavedInstanceState saved instance state bundle
	 */
    void onActivityCreated(Activity p_oActivity, Bundle p_oSavedInstanceState);

    /**
     * on activity started
     * @param p_oActivity activity
     */
    void onActivityStarted(Activity p_oActivity);

    /**
     * on activity resumed
     * @param p_oActivity activity
     */
    void onActivityResumed(Activity p_oActivity);

    /**
     * on activity paused
     * @param p_oActivity activity
     */
    void onActivityPaused(Activity p_oActivity);

    /**
     * on activity stopped
     * @param p_oActivity activity
     */
    void onActivityStopped(Activity p_oActivity);

    /**
     * on activity save instance state
     * @param p_oActivity activity
     * @param p_oOutState OUT state bundle
     */
    void onActivitySaveInstanceState(Activity p_oActivity, Bundle p_oOutState);

    /**
     * on activity destroyed
     * @param p_oActivity activity
     */
    void onActivityDestroyed(Activity p_oActivity);

    /**
     * on activity result
     * @param p_oActivity activity
     */
	void onActivityResult(Activity p_oActivity);
}
