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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.model;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ChronometerParameterVM;

/**
 * <p>Helper for the component ChronometerParameterVM</p>
 *
 *
 */
public class MChronometerHelper {

	/**
	 * Default constructor. Private (Utility class)
	 */
	private MChronometerHelper() {
		// NOTHING TO DO
	}

	/**
	 * <p>toComponent.</p>
	 *
	 * @param p_oChronometerParameterVM a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ChronometerParameterVM} object.
	 * @return a long.
	 */
	public static long toComponent(ChronometerParameterVM p_oChronometerParameterVM) {
		long r_lRealDurationSum = 0;
		if (p_oChronometerParameterVM != null) {
			r_lRealDurationSum = p_oChronometerParameterVM.initValue;
		}
		return r_lRealDurationSum;
	}

	/**
	 * <p>toViewModel.</p>
	 *
	 * @param p_lRealDurationSum a long.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ChronometerParameterVM} object.
	 */
	public static ChronometerParameterVM toViewModel(long p_lRealDurationSum) {
		ChronometerParameterVM r_oChronometerParameterVM = null;
		if (p_lRealDurationSum > -1) {
			r_oChronometerParameterVM = new ChronometerParameterVM(p_lRealDurationSum, 0L, false);
		}
		return r_oChronometerParameterVM;
	}
}
