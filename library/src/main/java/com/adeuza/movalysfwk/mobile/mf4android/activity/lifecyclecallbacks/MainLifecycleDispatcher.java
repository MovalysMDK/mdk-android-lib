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
 * Copyright (C) 2006 The Android Open Source Project
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