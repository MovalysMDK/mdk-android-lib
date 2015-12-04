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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dobeforephonecall;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * <p>
 * Value Object to manage a phone Number across actions
 * </p>
 */
public class PhoneNumber extends AbstractActionParameter implements ActionParameter {
	
	/** serial id */
	private static final long serialVersionUID = -8815335867461432620L;
	
	/** the phone number as String **/
	String phoneNumber;

	/**
	 *
	 * Construct a Phone number value object
	 */
	public PhoneNumber() {
		//Nothing To Do
	}
	
	/**
	 *
	 * Construct a Phone number value object
	 *
	 * @param p_sPhoneNumber a {@link java.lang.String} object.
	 */
	public PhoneNumber(String p_sPhoneNumber) {
		phoneNumber = p_sPhoneNumber;
	}

	/**
	 * get the phone number of this value object
	 *
	 * @return phone number as string
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * get the phone number of this value object
	 *
	 * @param p_sPhoneNumber phone number as string
	 */
	public void setPhoneNumber(String p_sPhoneNumber) {
		this.phoneNumber=p_sPhoneNumber;
	}

}
