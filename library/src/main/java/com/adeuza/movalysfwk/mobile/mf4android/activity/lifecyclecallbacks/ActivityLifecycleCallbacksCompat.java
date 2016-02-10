package com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks;

import android.app.Activity;
import android.os.Bundle;

/**
 * Equivalent of {@link ActivityLifecycleCallbacks} to be used with
 * {@link ApplicationHelper#registerActivityLifecycleCallbacks(Application, ActivityLifecycleCallbacksCompat)} and
 * {@link ApplicationHelper#unregisterActivityLifecycleCallbacks(Application, ActivityLifecycleCallbacksCompat)}.
 */
public interface ActivityLifecycleCallbacksCompat {
	/**
	 * on activity created
	 * @param p_oActivity activity
	 * @param savedInstanceState saved instance state bundle
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
     * @param outState OUT state bundle
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