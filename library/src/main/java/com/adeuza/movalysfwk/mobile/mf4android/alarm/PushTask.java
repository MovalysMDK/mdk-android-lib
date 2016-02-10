package com.adeuza.movalysfwk.mobile.mf4android.alarm;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>
 * 	Class to perform a push.
 * 	The class implements the <em>TimerTask</em> interface.
 * </p>
 * 
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (30 nov. 2010)
 */
public final class PushTask implements Runnable {

	/** 
	 * Construct an object <em>PushTask</em>.
	 */
	public PushTask() {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		int iInitialPriority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		Application.getInstance().getController().executePush();
		Thread.currentThread().setPriority(iInitialPriority);
	}

}
