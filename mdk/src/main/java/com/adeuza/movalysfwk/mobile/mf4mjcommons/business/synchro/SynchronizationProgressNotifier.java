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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;

/**
 * <p>TODO Décrire la classe SynchronizationProgressNotifier</p>
 *
 *
 */
public class SynchronizationProgressNotifier implements Runnable {
	private static final int NO_STEP = Integer.MIN_VALUE;

	private Notifier notifier;
	
	private int progressBar;

	private int step;

	private String message;

	/**
	 * <p>Constructor for SynchronizationProgressNotifier.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_iProgressBar a int.
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public SynchronizationProgressNotifier(Notifier p_oNotifier, int p_iProgressBar, String p_sMessage) {
		this(p_oNotifier, p_iProgressBar, NO_STEP, p_sMessage);
	}

	/**
	 * <p>Constructor for SynchronizationProgressNotifier.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_iProgressBar a int.
	 * @param p_oMessage a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 */
	public SynchronizationProgressNotifier(Notifier p_oNotifier, int p_iProgressBar, ApplicationR p_oMessage) {
		this(p_oNotifier, p_iProgressBar, NO_STEP, p_oMessage);
	}

	/**
	 * <p>Constructor for SynchronizationProgressNotifier.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_iProgressBar a int.
	 * @param p_iStep a int.
	 * @param p_oMessage a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 */
	public SynchronizationProgressNotifier(Notifier p_oNotifier, int p_iProgressBar, int p_iStep, ApplicationR p_oMessage) {
		this(p_oNotifier, p_iProgressBar, p_iStep, Application.getInstance().getStringResource(p_oMessage));
	}

	/**
	 * <p>Constructor for SynchronizationProgressNotifier.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_iProgressBar a int.
	 * @param p_iStep a int.
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public SynchronizationProgressNotifier(Notifier p_oNotifier, int p_iProgressBar, int p_iStep, String p_sMessage) {
		this.notifier = p_oNotifier;
		this.progressBar = p_iProgressBar;
		this.step = p_iStep;
		this.message = p_sMessage;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (this.step == NO_STEP) {
			Application.getInstance().getController().notifySynchronizationProgressChanged2(this.notifier, this.progressBar, this.message);
		}
		else {
			Application.getInstance().getController().notifySynchronizationProgressChanged2(this.notifier, this.progressBar, this.step, this.message);
		}

	}
}
