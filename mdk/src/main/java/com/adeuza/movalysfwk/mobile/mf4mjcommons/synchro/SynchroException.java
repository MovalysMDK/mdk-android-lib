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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro;

/**
 * <p>SynchroException class.</p>
 *
 */
public class SynchroException extends Exception {

	/**
	 * <p>Constructor for SynchroException.</p>
	 */
	public SynchroException() {
		super();
	}

	/**
	 * <p>Constructor for SynchroException.</p>
	 *
	 * @param p_sMessage a {@link java.lang.String} object.
	 * @param p_oCause a {@link java.lang.Throwable} object.
	 */
	public SynchroException(String p_sMessage, Throwable p_oCause) {
		super(p_sMessage, p_oCause);
	}

	/**
	 * <p>Constructor for SynchroException.</p>
	 *
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public SynchroException(String p_sMessage) {
		super(p_sMessage);
	}

	/**
	 * <p>Constructor for SynchroException.</p>
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public SynchroException(Throwable cause) {
		super(cause);
	}
}
