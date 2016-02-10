package com.adeuza.movalysfwk.mobile.mf4android.application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.ActivityLifecycleCallbacksCompat;

public class MFActivityLifecycleListener implements ActivityLifecycleCallbacksCompat {
	
	private int countActivity = 0 ;
	
	private Activity currentActivity ;
	
	private Activity currentVisibleActivity;
	
	private Class<? extends Activity> firstActivityClass ;
	  private static final String TAG = MFActivityLifecycleListener.class.getSimpleName();

    @Override
    public void onActivityCreated(Activity p_oActivity, Bundle p_oSavedInstanceState) {
//        Log.d(LOG_TAG, activity.getClass().getSimpleName() + ".onActivityCreated" );    	
    	if ( !isExitApplicationIntent(p_oActivity)) {
	        this.currentActivity = p_oActivity;
	        this.countActivity++;
//	        Log.d(LOG_TAG, "    set as current activity");
//	        Log.d(LOG_TAG, "    count: " + countActivity);
	        // We consider that activities implementing ApplicationMain are configured with noHistory="true",
	        // so they won't be the firstActivityClass after startup.
	        if ( this.firstActivityClass == null ) {
	        	Class<? extends Activity> firstAct = null;
	        	if ( p_oSavedInstanceState != null ) {
	        		String sFirstAct = p_oSavedInstanceState.getString("firstActivity");
	        		if ( sFirstAct != null ) {
	        			try {
							firstAct = (Class<? extends Activity>) Class.forName(sFirstAct);
						} catch (ClassNotFoundException e) { 
						  	  Log.v(TAG, "[onActivityCreated] "+e);							
						}
	        		}
	        	}
	        	if ( firstAct == null ) {
	        		firstAct = p_oActivity.getClass();
	        	}
//	        	Log.d(LOG_TAG, "    set as first activity : " + firstAct);
				this.firstActivityClass = firstAct;
			}
    	}
    } 
    
    @Override
    public void onActivityStarted(Activity p_oActivity) {
//    	Log.d(LOG_TAG, activity.getClass().getSimpleName() + ".onActivityStarted");
    	if ( !isExitApplicationIntent(p_oActivity)) {
    		this.currentActivity = p_oActivity;
//    		Log.d(LOG_TAG, "    set as current activity");
    	}
    }
    
    @Override
    public void onActivitySaveInstanceState(Activity p_oActivity, Bundle p_oOutState) {
//		Log.d(LOG_TAG, activity.getClass().getSimpleName() + ".onActivitySaveInstanceState : " + this.firstActivityClass.getName());
		p_oOutState.putString("firstActivity", this.firstActivityClass.getName());
    }

    @Override
    public void onActivityResumed(Activity p_oActivity) {
//        Log.d(LOG_TAG, activity.getClass().getSimpleName() + ".onActivityResumed" );    	
    	if ( !isExitApplicationIntent(p_oActivity)) {
	        this.currentVisibleActivity = p_oActivity ;
//	        Log.d(LOG_TAG, "    set as current visible activity");
    	}
    }

    @Override
    public void onActivityPaused(Activity p_oActivity) {
//        Log.d(LOG_TAG, activity.getClass().getSimpleName() + ".onActivityPaused activity" );
    	if ( !isExitApplicationIntent(p_oActivity)) {
	        if ( p_oActivity == this.currentActivity ) {
	        	this.currentVisibleActivity = null ;
//	        	Log.d(LOG_TAG, "    set currentVisibleActivity to null");
	        }
    	}
    }    
    
	@Override
    public void onActivityStopped(Activity p_oActivity) {
//        Log.d(LOG_TAG, activity.getClass().getSimpleName() + ".onActivityStopped");		
		if ( !isExitApplicationIntent(p_oActivity)) {
	        if ( p_oActivity == this.currentActivity ) {
//	        	Log.d(LOG_TAG, "    set current activity to null");
	        	this.currentActivity = null ;
	        }
		}
    }    
    
	@Override
	public void onActivityResult(Activity p_oActivity) {
//		Log.d(LOG_TAG, activity.getClass().getSimpleName() + ".onActivityResult");
		if ( !isExitApplicationIntent(p_oActivity)) {
    		this.currentActivity = p_oActivity;
//    		Log.d(LOG_TAG, "    set as current activity");
    	}
	}
	
    @Override
    public void onActivityDestroyed(Activity p_oActivity) {
//        Log.d(LOG_TAG, activity.getClass().getSimpleName() + ".onActivityDestroyed" );
    	if ( !isExitApplicationIntent(p_oActivity)) {
	        this.countActivity--;
//	        Log.d(LOG_TAG, "    count: " + this.countActivity);
	        
	        MFAndroidApplication app =  MFApplicationHolder.getInstance().getApplication();
			if ( app.isStoppingApplication() && this.countActivity == 0 ) {
				// Application is stopping, all activities have been destroyed,
				// we can now destroy application it self.
//				Log.d(LOG_TAG, "  destroy application");
				app.destroy();
			}
    	}
    }

	/**
	 * Return current activity
	 * @return current activity
	 */
	public Activity getCurrentActivity() {
		return currentActivity;
	}

	/**
	 * Return current and visible activity
	 * @return current and visible activity
	 */
	public Activity getCurrentVisibleActivity() {
		return currentVisibleActivity;
	}

	/**
	 * Return first activity class that has been started.
	 * @return
	 */
	public Class<? extends Activity> getFirstActivityClass() {
		return firstActivityClass;
	}
	
	public boolean isExitApplicationIntent( Activity p_oActivity ) {
		boolean result = false ;
		if ( AbstractMMActivity.class.isAssignableFrom(p_oActivity.getClass())) {
			result = ((AbstractMMActivity) p_oActivity).isExitApplicationIntent();
		}
		
		return result;
	}

	public void clearReferences() {
		this.currentActivity = null;
		this.currentVisibleActivity = null;
	}
}
