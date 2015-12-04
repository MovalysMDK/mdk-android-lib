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

/**
 * <p>TODO Décrire la classe SynchroErrorNotifier</p>
 *
 *
 */
public class SynchronizationMessageNotifier implements Runnable {

	private int message;

	private boolean mmToBo;

	/**
	 * <p>Constructor for SynchronizationMessageNotifier.</p>
	 *
	 * @param p_iMessage a int.
	 * @param p_bMmToBo a boolean.
	 */
	public SynchronizationMessageNotifier(int p_iMessage, boolean p_bMmToBo) {
		this.message = p_iMessage;
		this.mmToBo = p_bMmToBo;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Application.getInstance().getController().manageSynchronizationActions(this.message, new ManageSynchronizationParameter(this.mmToBo));
	}
}
