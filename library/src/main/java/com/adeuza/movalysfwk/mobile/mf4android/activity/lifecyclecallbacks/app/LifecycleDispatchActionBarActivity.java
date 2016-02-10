package com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.MainLifecycleDispatcher;

/**
 * Extension of {@link FragmentActivity} that dispatches its life cycle calls to registered listeners.
 */
public class LifecycleDispatchActionBarActivity extends ActionBarActivity {

    /**
     * {@inheritDoc}
     */
	@Override
    protected void onCreate(Bundle p_oSavedInstanceState) {
        super.onCreate(p_oSavedInstanceState);
   		MainLifecycleDispatcher.get().onActivityCreated(this, p_oSavedInstanceState);
   		
       	if ( this.isExitApplicationIntent()) {
       		//Log.d(AndroidApplication.LOG_TAG, this.getClass().getSimpleName() + ".onCreate : FINISH");
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
    protected void onPause() {
        super.onPause();
       	MainLifecycleDispatcher.get().onActivityPaused(this);
    }

    /** 
     * {@inheritDoc}
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int p_iRequestCode, int p_iResultCode, Intent p_oData) {
    	super.onActivityResult(p_iRequestCode, p_iResultCode, p_oData);
    	MainLifecycleDispatcher.get().onActivityResult(this);
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
     * Return true if the activity must be stopped.
     * @return isExitApplicationIntent
     */
    public boolean shouldActivityBeStopped() {
    	return this.isExitApplicationIntent();
    }
    
    /**
     * is exit application intent?
     * @return exitApplication
     */
	public boolean isExitApplicationIntent() {
		return (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("exitApplication", false));
	}
}