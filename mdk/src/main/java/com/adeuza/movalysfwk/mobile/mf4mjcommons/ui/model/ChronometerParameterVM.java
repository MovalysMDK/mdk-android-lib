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


/**
 * <p>View model for chronometer</p>
 *
 *
 */
public class ChronometerParameterVM {
	
	/** initial value */
	public long initValue = 0;
	/** start chronometer ? */
	public boolean start = false;
	/** last start date */	
	public long lastStart = 0;
		
	/**
	 * Construct a new chronometer value
	 *
	 * @param p_lInitValue a long.
	 * @param p_lLastStart a long.
	 * @param p_bStart a boolean.
	 */
	public ChronometerParameterVM(long p_lInitValue, long p_lLastStart, boolean p_bStart) {
		this.initValue = p_lInitValue;
		this.start = p_bStart;
		this.lastStart = p_lLastStart;
	}
	
	/**
	 * Construct a new chronometer value
	 */
	public ChronometerParameterVM() {
		this.initValue = 0L;
		this.start = false;
		this.lastStart = 0L;
	}
	
	/**
	 * <p>Constructor for ChronometerParameterVM.</p>
	 *
	 * @param p_oChrono a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ChronometerParameterVM} object.
	 */
	public ChronometerParameterVM(ChronometerParameterVM p_oChrono) {
		this.initValue = p_oChrono.initValue;
		this.start = p_oChrono.start;
		this.lastStart = p_oChrono.lastStart;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ChronometerParameterVM) {
			ChronometerParameterVM chrono = (ChronometerParameterVM) obj;
			if ( chrono.initValue == this.initValue &&
					chrono.start == this.start &&
					chrono.lastStart == this.lastStart ) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
