package com.adeuza.movalysfwk.mobile.mf4android.context;

import android.content.Context;
import android.content.SharedPreferences;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Describes Android context</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author smaitre
 */
public interface AndroidMMContext extends MContext {

	/**
	 * Returns the shared preferences of an android application. Never null.
	 * 
	 * @return
	 * 		The shared preferences of an android application. Never null.
	 */
	public SharedPreferences getSharedPreferences();

	/**
	 * <p>Return the native android <em>context</em></p>
	 * @return Objet context
	 */
	public Context getAndroidNativeContext();
	
	/**
	 * add a business event based on the first event
	 * 
	 * @param p_bExitMode exit mode
	 */
	public void addBusinessEventFromFirstEvent(boolean p_bExitMode);

}
