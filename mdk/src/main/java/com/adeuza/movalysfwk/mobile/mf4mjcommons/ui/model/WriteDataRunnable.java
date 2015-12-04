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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

import java.lang.ref.WeakReference;

/**
 * This runnable inner class is used when a "writeDataToComponent" should
 * be called in a task out of the ui thread.
 *
 */
public class WriteDataRunnable implements Runnable {

	/**
	 * A Weakreference to the ViewModel used to retrieve the data
	 */
	private WeakReference<ViewModel> oViewModel;

	/**
	 * The key used to identify the component to update
	 */
	private String oKey;

	/**
	 * The public constructor of the runnable
	 *
	 * @param p_oVM view model to use
	 * @param p_sKey key of the component
	 */
	public WriteDataRunnable(ViewModel p_oVM, String p_sKey) {
		this.oViewModel = new WeakReference<ViewModel>(p_oVM);
		this.oKey = p_sKey;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if(oViewModel != null) {
			ViewModel oVm = oViewModel.get();
			if (oVm != null) {
				oVm.writeDataToComponent(oKey);
			}
		}
	}
}
