package com.adeuza.movalysfwk.mobile.mf4android.messages;

/**
 * <p>
 * 	Exception thrown by the application Movalys Android.
 *  This exception extends RuntimeException object.
 * </p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (24 sept. 2010)
 * @see java.lang.RuntimeException
 */
public class AndroidException extends RuntimeException{

	/** <p>serial version</p> */
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Construct an <em>AndroidException</em> with the specified detail message.</p>
	 * @param p_sMessage the detail message.
	 */
	public AndroidException(final String p_sMessage) {
		super(p_sMessage);
	}
	
	/**
	 * <p>Construct an <em>AndroidException</em> with the specified detail message.</p>
	 * @param p_sMessage the detail message.
	 * @param p_oException the detail exception.
	 */
	public AndroidException(final String p_sMessage, final Throwable p_oException) {
		super(p_sMessage, p_oException);
	}

}
