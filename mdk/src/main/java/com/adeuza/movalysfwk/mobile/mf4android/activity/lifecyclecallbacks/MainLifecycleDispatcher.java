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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

/**
 * Keeps a list of {@link ActivityLifecycleCallbacksCompat}s that will be called following the life cycle of the application's {@link Activity activities}.
 * This class is used when the app is running on an older platform version that does not support
 * {@link Application#registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks)} and
 * {@link Application#unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks)}.
 */
public final class MainLifecycleDispatcher implements ActivityLifecycleCallbacksCompat {
    
	/**
	 * main lifecycle dispatcher instance
	 */
	private static final MainLifecycleDispatcher INSTANCE = new MainLifecycleDispatcher();

	/**
	 * GETTER
	 * @return main lifecycle dispatcher instance
	 */
    public static MainLifecycleDispatcher get() {
        return INSTANCE;
    }

    /**
     * hidden constructor
     */
    private MainLifecycleDispatcher() {}

    /**
     * list of activity lifecycle callbacks
     */
    private List<ActivityLifecycleCallbacksCompat> mActivityLifecycleCallbacks = new ArrayList<>();

    /**
     * register activity lifecycle callback
     * @param p_oCallback callback
     */
    void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat p_oCallback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.add(p_oCallback);
        }
    }

    /**
     * unregister activity lifecycle callback
     * @param p_oCallback activity lifecycle callback
     */
    void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat p_oCallback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.remove(p_oCallback);
        }
    }

    /**
     * get activity lifecycle callbacks
     * @return array of callbacks
     */
    private Object[] collectActivityLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (mActivityLifecycleCallbacks) {
            if (!mActivityLifecycleCallbacks.isEmpty()) {
                callbacks = mActivityLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Activity p_oActivity, Bundle p_oSavedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacksCompat) callback).onActivityCreated(p_oActivity, p_oSavedInstanceState);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityStarted(Activity p_oActivity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacksCompat) callback).onActivityStarted(p_oActivity);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityResumed(Activity p_oActivity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacksCompat) callback).onActivityResumed(p_oActivity);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityPaused(Activity p_oActivity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacksCompat) callback).onActivityPaused(p_oActivity);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityStopped(Activity p_oActivity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacksCompat) callback).onActivityStopped(p_oActivity);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivitySaveInstanceState(Activity p_oActivity, Bundle p_oOutState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacksCompat) callback).onActivitySaveInstanceState(p_oActivity, p_oOutState);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityDestroyed(Activity p_oActivity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacksCompat) callback).onActivityDestroyed(p_oActivity);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void onActivityResult(Activity p_oActivity) {
		Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((ActivityLifecycleCallbacksCompat) callback).onActivityResult(p_oActivity);
            }
        }
	}
}
