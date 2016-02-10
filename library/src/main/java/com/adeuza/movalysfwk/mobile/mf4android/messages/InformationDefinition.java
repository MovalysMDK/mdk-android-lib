package com.adeuza.movalysfwk.mobile.mf4android.messages;

/**
 * 
 * <p>Contains information definition</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author smaitre
 */
public final class InformationDefinition {

	/**
	 * This class has only static methods, so it is impossible to instantiate objects.
	 */
	private InformationDefinition() {
		// NOTHING TO DO
	}
	
	/** defines a space used in message */
	public static final String SPACE = " ";
	/** defines a space used in point */
	public static final String POINT = ".";
	
	/* COMMON ERROR MESSAGE : Properties messages 0000 to 0099 */
	
	
	/* COMMON ERROR MESSAGE : Introspection messages 0100 to 0199 */
	
	
	/* COMMON ERROR MESSAGE : FREE 0200 to 0999 */
	/** Start initialize application.*/
	public static final String FWK_MOBILE_I_0200 = "FWK_MOBILE_I_0200";
	/** Label of message {@link #FWK_MOBILE_I_0200} */
	public static final String FWK_MOBILE_I_0200_1_LABEL = "a sync action is already running (sync name=";
	/** Label of message {@link #FWK_MOBILE_I_0200} */
	public static final String FWK_MOBILE_I_0200_2_LABEL = ")";
	
	/* Découpage en phase */
	/* COMMON ERROR MESSAGE : Starting message 1000 to 1099 */
	/** Start initialize application.*/
	public static final String FWK_MOBILE_I_1000 = "FWK_MOBILE_I_1000";
	/** Label of message {@link #FWK_MOBILE_I_1000} */
	public static final String FWK_MOBILE_I_1000_LABEL = "Start of Initialize application.";
	
	/** End initialize application.*/
	public static final String FWK_MOBILE_I_1001 = "FWK_MOBILE_I_1001";
	/** Label of message {@link #FWK_MOBILE_I_1001} */
	public static final String FWK_MOBILE_I_1001_LABEL = "End of Initialize application.";
	
	/** Analyse time of application.*/
	public static final String FWK_MOBILE_I_1002 = "FWK_MOBILE_I_1002";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_1 = "Start Reset And Compute real descriptor.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_2 = "End Reset And Compute real descriptor.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_3 = "Start Run.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_4 = "End Run.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_5 = "Start Initialize screen.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_6 = "End Initialize screen.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_7 = "Start Run.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_8 = "End Run.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_9 = "Start Json.";
	/** Label of message {@link #FWK_MOBILE_I_1002} */
	public static final String FWK_MOBILE_I_1002_LABEL_10 = "End JSon.";
	/*  COMMON ERROR MESSAGE : FREE 1100 to 1999 */
	
	/* Découpage en couche */
	/* COMMON ERROR MESSAGE : SQL Messages 2000 to 2499 */
		
	/* COMMON ERROR MESSAGE : Model Messages 2500 to 3000 */	
	
}
