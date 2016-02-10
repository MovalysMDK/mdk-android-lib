package com.adeuza.movalysfwk.mobile.mf4android.application;

import android.util.Log;

public class MFApplicationHolder {

	private static MFApplicationHolder applicationHolder = new MFApplicationHolder();
	
	private MFAndroidApplication application ;
	
	public static final synchronized MFApplicationHolder getInstance() {
		return applicationHolder ;
	}

	public MFAndroidApplication getApplication() {
		return application;
	}

	public void setApplication(MFAndroidApplication p_oApplication) {
		Log.d(AndroidApplication.LOG_TAG, "MFApplicationHolder.setApplication to : " + p_oApplication );
		this.application = p_oApplication;
	}
}
