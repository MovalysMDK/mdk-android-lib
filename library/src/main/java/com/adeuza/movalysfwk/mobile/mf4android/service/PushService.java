package com.adeuza.movalysfwk.mobile.mf4android.service;


/**
 * <p>The push simulation service for synchronizing Movalys Intervention data.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Annapurna
 */
public interface PushService {

	/**
	 * Request Code for alarm
	 */
	public static final int ALARM_REQUEST_CODE = 2405 ;

	/**
     * Stop the timer
     */
    public void stop();
	
}
