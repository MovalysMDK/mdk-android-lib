package com.adeuza.movalysfwk.mobile.mf4android.log;

import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.log.Logger;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.NotImplementedException;

/**
 * <p>Implementation of an Android Logger.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (24 sept. 2010)
 */
public final class AndroidLoggerImpl implements Logger {
	
	//information sur le fonctionnement des logs sous Android
	//http://developer.android.com/guide/developing/tools/adb.html (06/10/2010)
	
	/**
	 * <p>
	 * 	The tag of a log message is a short string indicating the system component from 
	 * 	which the message originates. It often represent the name of the class to log or 
	 * 	the name of the method running.
	 * </p>
	 */
	private transient String tag;
	
	/**
	 * <p>
	 * 	Construct an object <em>AndroidLoggerImpl</em>.<br>
	 * 	It takes a string parameter that corresponds of a short string indicating the system component from 
	 * 	which the message originates.
	 * </p>
	 * @param p_sTag The tag of a log message.
	 */
    private AndroidLoggerImpl(final String p_sTag){
        if(p_sTag == null || "".equals(p_sTag)){
            this.tag = this.getClass().getName();
        }else{
        	this.tag = p_sTag;
        }
    }

    /**
     * Return an instance of an <em>AndroidLoggerImpl</em> object.
     * @param p_sTag The tag of a log message.
     * @return an object <em>AndroidLoggerImpl</em>.
     * @see #AndroidLoggerImpl(String)
     */
    public static AndroidLoggerImpl getInstance(final String p_sTag){
    	return new AndroidLoggerImpl(p_sTag);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String p_sId, final String p_sMessage) {
		Log.e(p_sId, p_sMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String p_sId,final String p_sMessage, final Throwable p_oException) {
		Log.e(p_sId, p_sMessage, p_oException);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String p_sId,final String p_sMessage) {
		Log.i(p_sId, p_sMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String p_sId,final String p_sMessage) {
		Log.d(p_sId, p_sMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInErrorLevel() {
		return Log.isLoggable(this.tag, Log.ERROR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInfoLevel() {
		return Log.isLoggable(this.tag, Log.INFO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugLevel() {
		return Log.isLoggable(this.tag, Log.DEBUG);
	}

	/**
	 * <em><q>
	 * 	Please note, this method is not implemented on Android. 
	 * 	Indeed, it is not possible in android, to change in use the log level as in Java.
	 * </q></em><br><br>
	 * {@inheritDoc}
	 * <br><br><em><q>
	 * 	Please note, this method is not implemented on Android. 
	 * 	Indeed, it is not possible in android, to change in use the log level as in Java.
	 * </q></em><br><br>
	 */
	@Override
	public void setLevel(int p_iLevel) {
		throw new NotImplementedException();
	}

}
