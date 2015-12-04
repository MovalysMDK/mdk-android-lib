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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.action;

import java.io.Serializable;

/**
 * <p>Action's handler, combines the following elements
 * in order to process the return asynchronous action:
 * an action and its parameters.</p>
 */
public class ActionHandler implements Serializable {
	
	/** serial id */
	private static final long serialVersionUID = -5861862753584749360L;

	/** action to associate */
	public Action<?,?,?,?> action = null;
	
	/** parameter to associate */
	public ActionParameter parameter = null;
	
	/**
	 * Constructs a new handler. Associates one action with no parameter.
	 *
	 * @param p_oAction the action to associate
	 */
	public ActionHandler(Action<?,?,?,?> p_oAction) {
		this.action = p_oAction;
	}
	
	/**
	 * Constructs a new handler. Associates one action with a parameter
	 *
	 * @param p_oAction the action to associate
	 * @param p_oParameter the parameter to associate
	 */
	public ActionHandler(Action<?,?,?,?> p_oAction, ActionParameter p_oParameter) {
		this.action = p_oAction;
		this.parameter = p_oParameter;
	}
}
