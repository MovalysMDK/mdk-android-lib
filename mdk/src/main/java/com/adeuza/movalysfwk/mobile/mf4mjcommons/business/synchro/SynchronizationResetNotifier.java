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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;

/**
 * <p>TODO Décrire la classe SynchronizationResetNotifier</p>
 *
 *
 */
public class SynchronizationResetNotifier implements Runnable {
	private Notifier notifier;

	/**
	 * <p>Constructor for SynchronizationResetNotifier.</p>
	 *
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 */
	public SynchronizationResetNotifier(Notifier p_oNotifier) {
		this.notifier = p_oNotifier;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Application.getInstance().getController().notifySynchronizationResetProgress(this.notifier);
	}
}
