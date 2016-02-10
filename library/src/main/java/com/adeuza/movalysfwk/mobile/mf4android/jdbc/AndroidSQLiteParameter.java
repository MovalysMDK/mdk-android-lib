package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

public enum AndroidSQLiteParameter {
	/**
	 * Android Context Parameter
	 */
	ANDROID_CONTEXT_PARAMETER("androidContext"),
	
	/**
	 * Database Version Parameter
	 */
	DB_VERSION_PARAMETER("dbVersion"),
	
	/**
	 * Database Version Parameter
	 */
	DB_DISABLE_FK("disableFk");
	
	/**
	 * value
	 */
    public final String value;
    
    /**
     * constructor
     * @param p_sVal value
     */
    AndroidSQLiteParameter(String p_sVal){
    	this.value = p_sVal;
    }
}
