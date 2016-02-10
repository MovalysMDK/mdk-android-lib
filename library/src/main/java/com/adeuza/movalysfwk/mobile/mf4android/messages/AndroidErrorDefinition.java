package com.adeuza.movalysfwk.mobile.mf4android.messages;

/**
 * 
 * <p>Class that defines all the error messages for the Movalys Android Application.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (24 sept. 2010)
 */
public final class AndroidErrorDefinition {
	
	/* BASICS */
	
	/** defines a space used in message */
	public static final String SPACE = " ";
	/** defines a space used in point */
	public static final String POINT = ".";
	
	/* ANDROID ERROR MESSAGE : 100 to 200 */
	
	//A2A_DEV SMA factoriser les messages d'erreur il ne semble pas pour android
	
	/* ACTION INIT ERROR MESSAGE : Properties messages 0100 to 0199 */

	/** This errors happens when a visual component have not an id */
	public static final String MM_MOBILE_E_0100 = "MM_MOBILE_E_0100";
	
	
	/* ACTION CONFIGURATION ERROR MESSAGE : Properties messages 0500 to 0600 */
	/** This error happens when the it's impossible assign a null value to a field  */
	public static final String MM_MOBILE_E_0500 = "MM_MOBILE_E_0500";
	/** Label of error {@link #MM_MOBILE_E_0500} */
	public static final String MM_MOBILE_E_0500_LABEL = "Attempts to assign a null value to the field";
	
	
	/** This error happens when the first column in the configuration is not the planning  */
	public static final String MM_MOBILE_E_0501 = "MM_MOBILE_E_0501";
	/** Label of error {@link #MM_MOBILE_E_0501} */
	public static final String MM_MOBILE_E_0501_LABEL = "The first colum of the list is not the planning -- all columns displayed can be desordered";
	
	
	/**
	 * Construct an object <em>AndroidErrorDefinition</em>
	 */
	private AndroidErrorDefinition() {
		// Nothing to do
	}
	
}
