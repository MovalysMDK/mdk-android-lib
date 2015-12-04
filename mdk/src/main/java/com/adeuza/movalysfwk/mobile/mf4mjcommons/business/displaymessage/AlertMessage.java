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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;

/**
 * <p>Parameter to use to display message</p>
 *
 *
 */
public class AlertMessage extends AbstractActionParameter implements ActionParameter {
	
	/** serial id */
	private static final long serialVersionUID = -727355210373831879L;
		
	/** Show the view or text notification for a long period of time**/
	public static final int LONG=1;
	/**Show the view or text notification for a short period of time**/
	public static final int SHORT=0;
	
	/** the message */
	private ApplicationR  message;
	/** message duration */
	private int duration;
	
	/**
	 * Create a new message to be displayed using {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.DisplayMessageAction}
	 */
	public AlertMessage() {
		super();
	}
	
	/**
	 * Create a new message to be displayed using {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.DisplayMessageAction}
	 *
	 * @param p_sMessage the message ressource id (it could be formated) 
	 * @param p_iDuration duration of the display
	 */
	public AlertMessage(ApplicationR p_sMessage, int p_iDuration) {
		super();
		this.message = p_sMessage;
		this.duration = p_iDuration;
	}
	
	/**
	 * Gets the message resource id
	 *
	 * @return message ressource id
	 */
	public ApplicationR getMessage() {
		return this.message;
	}

	/**
	 * set the message resource Id
	 *
	 * @param p_sMessage message resource Id
	 */
	public void setMessage(ApplicationR p_sMessage) {
		this.message = p_sMessage;
	}

	/**
	 * Gets the message duration
	 *
	 * @return message duration
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * Sets the message duration see {@link SHORT}, {@link LONG}, or user defined
	 *
	 * @param p_oDuration Objet duration
	 */
	public void setDuration(int p_oDuration) {
		this.duration = p_oDuration;
	}

}
