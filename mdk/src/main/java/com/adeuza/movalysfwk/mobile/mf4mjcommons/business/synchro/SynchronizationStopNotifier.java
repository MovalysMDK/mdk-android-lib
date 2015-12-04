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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;

/**
 * <p>TODO Décrire la classe SynchroStopNotifier</p>
 *
 *
 */
public class SynchronizationStopNotifier implements Runnable {
	private MContext context;
	private Notifier notifier;
	private SynchronisationResponseTreatmentInformation informations;

	/**
	 * <p>Constructor for SynchronizationStopNotifier.</p>
	 *
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_oInformations a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 */
	public SynchronizationStopNotifier(MContext p_oContext, Notifier p_oNotifier, SynchronisationResponseTreatmentInformation p_oInformations) {
		this.context = p_oContext;
		this.notifier = p_oNotifier;
		this.informations = p_oInformations;
	}

	/** {@inheritDoc} */
	@Override
	public void run() {
		Application.getInstance().getController().notifySynchronizationProcessStop(this.notifier, this.context, this.informations);
	}
}
