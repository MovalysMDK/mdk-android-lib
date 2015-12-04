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
package com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.MainLifecycleDispatcher;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;

/**
 * Extension of {@link FragmentActivity} that dispatches its life cycle calls to registered listeners.
 */
public class LifecycleDispatchFragmentActivity extends FragmentActivity {
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void onCreate(Bundle p_oSavedInstanceState) {
        super.onCreate(p_oSavedInstanceState);
   		MainLifecycleDispatcher.get().onActivityCreated(this, p_oSavedInstanceState);
       	
       	if ( this.isExitApplicationIntent()) {
       		Log.d(AndroidApplication.LOG_TAG, this.getClass().getSimpleName() + ".onCreate : FINISH");
       		this.finish();
       	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void onStart() {
        super.onStart();
       	MainLifecycleDispatcher.get().onActivityStarted(this);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void onResume() {
        super.onResume();
       	MainLifecycleDispatcher.get().onActivityResumed(this);
    }
    
	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void onActivityResult(int p_iRequestCode, int p_iResultCode, Intent p_oData) {
    	super.onActivityResult(p_iRequestCode, p_iRequestCode, p_oData);
    	MainLifecycleDispatcher.get().onActivityResult(this);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void onPause() {
        super.onPause();
       	MainLifecycleDispatcher.get().onActivityPaused(this);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void onStop() {
        super.onStop();
       	MainLifecycleDispatcher.get().onActivityStopped(this);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void onSaveInstanceState(Bundle p_oOutState) {
        super.onSaveInstanceState(p_oOutState);
       	MainLifecycleDispatcher.get().onActivitySaveInstanceState(this, p_oOutState);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void onDestroy() {
        super.onDestroy();
       	MainLifecycleDispatcher.get().onActivityDestroyed(this);
    }
    
	/**
	 * exit application intend?
	 * @return exitApplication
	 */
	public boolean isExitApplicationIntent() {
		return getIntent().getExtras() != null && getIntent().getExtras().getBoolean("exitApplication", false);
	}
}
