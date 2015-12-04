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
package com.adeuza.movalysfwk.mobile.mf4android.application;

import java.lang.ref.WeakReference;

import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractProgressHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler;

/**
 * <p>
 * 	Inner class to represent the first step of the application startup.
 * 	This object extends AsyncTask and will perform asynchronously, the application initialization.
 * 	At each step, it increments a progress bar, positioned on the splash screen of the application.
 * </p>
 * <ul>Under initialization steps:
 * 	<li>recovery of properties for defining the database and business actions.v</li>
 * 	<li>initializing the database</li>
 * 	<li>android context generation</li>
 * 	<li>checking the Movalys Mobile configuration</li>
 * </ul>
 *
 */
public class ProgressHandlerStep1 extends AbstractProgressHandler<Void> implements ProgressHandler {

	private WeakReference<ApplicationMainImpl> appMain ;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Void doInBackground(Void... p_oInutilise){
		final int iInitialPriority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		//initialisation de la progression de la barre du splash screen
		this.initProgress(ApplicationMainImpl.FIRST_PART_STARTUP_PROGRESS_STEP, this.appMain.get().getStepCount());
		if (!this.isCancel()) {
			this.increaseProgress(null); //+1
			if (!this.isCancel()) {
				// vérification du chargement du fichier de properties
				this.appMain.get().loadSettings();
				this.increaseProgress(null); //+2
			}
		}

		Thread.currentThread().setPriority(iInitialPriority);
		return null;
	}
	
	public void attach(ApplicationMainImpl p_oApplicationMainImpl) {
		this.appMain = new WeakReference<>(p_oApplicationMainImpl);
	}
}
