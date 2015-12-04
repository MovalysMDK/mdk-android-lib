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
package com.adeuza.movalysfwk.mobile.mf4android.application;

import android.content.Intent;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.ApplicationHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

public class MFAndroidApplication extends android.app.Application {

	private MFActivityLifecycleListener activityListener ;
	
	/** is on stopping process */
	private boolean stopping = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		MFApplicationHolder.getInstance().setApplication(this);
		this.activityListener = new MFActivityLifecycleListener();
		ApplicationHelper.registerActivityLifecycleCallbacks(this, activityListener);
	}

	/**
	 * Call this method to initiate application stop
	 */
	public void launchStopApplication() {
		
		Log.d(Application.LOG_TAG, "AndroidApplication.launchStopApplication, already stopping: " + isStoppingApplication());
		if ( !isStoppingApplication()) {
			this.stoppingApplication();
			Log.d(Application.LOG_TAG, "  launch intent to stop application : " + activityListener.getFirstActivityClass().getSimpleName());
			Intent intent = new Intent(activityListener.getCurrentActivity(), activityListener.getFirstActivityClass());
			//LMI: NEW_TASK is not a good solution because it creates a new task but this
			// is the only way i found.  CLEAR_TOP doesnot work when the first activity is configured with noHistory="true"
			// For later Level11: See FLAG_ACTIVITY_CLEAR_TASK
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("exitApplication", Boolean.TRUE);
			this.startActivity(intent);
		}
	}
	
	/**
	 * Indicates to controller that application is in stopping process
	 */
	protected void stoppingApplication() {
		this.stopping = true;
	}
	
	/**
	 * Return true whether application is stopping
	 * @return true whether application is stopping
	 */
	public boolean isStoppingApplication() {
		return this.stopping;
	}
	
	public MFActivityLifecycleListener getActivityListener() {
		return this.activityListener;
	}
	
	public void destroy() {
		try {
			if ( Application.getInstance() != null ) {
				Application.getInstance().destroy();
			}
		} finally {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(NumericConstants.EXIT_STATUS_10);
		}
	}
}
