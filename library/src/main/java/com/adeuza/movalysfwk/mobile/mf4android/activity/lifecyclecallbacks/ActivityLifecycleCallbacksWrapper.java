package com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Build;
import android.os.Bundle;

/**
 * Wraps an {@link ActivityLifecycleCallbacksCompat} into an {@link ActivityLifecycleCallbacks}.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class ActivityLifecycleCallbacksWrapper implements ActivityLifecycleCallbacks {
	
	/**
	 * activity lifecycle callback
	 */
    private ActivityLifecycleCallbacksCompat mCallback;

    /**
     * constructor
     * @param p_oCallback activity lifecycle callback
     */
    public ActivityLifecycleCallbacksWrapper(ActivityLifecycleCallbacksCompat p_oCallback) {
        mCallback = p_oCallback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Activity p_oActivity, Bundle p_oSavedInstanceState) {
        mCallback.onActivityCreated(p_oActivity, p_oSavedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityStarted(Activity p_oActivity) {
        mCallback.onActivityStarted(p_oActivity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityResumed(Activity p_oActivity) {
        mCallback.onActivityResumed(p_oActivity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityPaused(Activity p_oActivity) {
        mCallback.onActivityPaused(p_oActivity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityStopped(Activity p_oActivity) {
        mCallback.onActivityStopped(p_oActivity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivitySaveInstanceState(Activity p_oActivity, Bundle p_oOutState) {
        mCallback.onActivitySaveInstanceState(p_oActivity, p_oOutState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityDestroyed(Activity p_oActivity) {
        mCallback.onActivityDestroyed(p_oActivity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object p_oActivity) {
    	return (
			(this == p_oActivity) 
			|| (p_oActivity instanceof ActivityLifecycleCallbacksWrapper && ((ActivityLifecycleCallbacksWrapper) p_oActivity).mCallback == mCallback)
		);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return mCallback.hashCode();
    }
}