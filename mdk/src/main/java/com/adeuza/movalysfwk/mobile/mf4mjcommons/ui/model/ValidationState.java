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
 * Component validation state<br/>
 * Gives the following values:<br/>
 * - INIT<br/>
 * - VALID<br/>
 * - EMPTY_MANDATORY<br/>
 * - ERROR<br/>
 */
public enum ValidationState {
	/** Component initialized */
	INIT(true, false),
	/** Component content is valid */
	VALID(true, false),
	/** Component content is mandatory but empty */
	EMPTY_MANDATORY(true, true),
	/** Component has a user error */
	USER_ERROR(true, false),
	/** Component content is in error */
	ERROR(false, true);
	
	/** Should send a notification */
	private boolean mNotify;
	
	/** Is in error */
	private boolean mError;

	/**
	 * Constructor
	 * @param p_bNotify should notify
	 * @param p_bIsError is in error
	 */
	private ValidationState(boolean p_bNotify, boolean p_bIsError) {
		this.mNotify = p_bNotify;
		this.mError = p_bIsError;
	}
	
	/**
	 * Is in error
	 *
	 * @return true when is in error
	 */
	public boolean isError() {
		return this.mError;
	}
	
	/**
	 * Should notify
	 *
	 * @return true if it should notify
	 */
	public boolean doNotify() {
		return this.mNotify;
	}
}
