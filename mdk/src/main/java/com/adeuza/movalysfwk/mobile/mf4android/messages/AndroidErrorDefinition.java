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
package com.adeuza.movalysfwk.mobile.mf4android.messages;

/**
 * 
 * <p>Class that defines all the error messages for the Movalys Android Application.</p>
 *
 *
 * @since Barcelone (24 sept. 2010)
 */
public final class AndroidErrorDefinition {
	
	/* BASICS */
	
	/** defines a space used in message */
	public static final String SPACE = " ";
	/** defines a space used in point */
	public static final String POINT = ".";
	
	/* ANDROID ERROR MESSAGE : 100 to 200 */
	
	//A2A_DEV SMA factoriser les messages d'erreur il ne semble pas pour android
	
	/* ACTION INIT ERROR MESSAGE : Properties messages 0100 to 0199 */

	/** This errors happens when a visual component have not an id */
	public static final String MM_MOBILE_E_0100 = "MM_MOBILE_E_0100";
	
	
	/* ACTION CONFIGURATION ERROR MESSAGE : Properties messages 0500 to 0600 */
	/** This error happens when the it's impossible assign a null value to a field  */
	public static final String MM_MOBILE_E_0500 = "MM_MOBILE_E_0500";
	/** Label of error {@link #MM_MOBILE_E_0500} */
	public static final String MM_MOBILE_E_0500_LABEL = "Attempts to assign a null value to the field";
	
	
	/** This error happens when the first column in the configuration is not the planning  */
	public static final String MM_MOBILE_E_0501 = "MM_MOBILE_E_0501";
	/** Label of error {@link #MM_MOBILE_E_0501} */
	public static final String MM_MOBILE_E_0501_LABEL = "The first colum of the list is not the planning -- all columns displayed can be desordered";
	
	
	/**
	 * Construct an object <em>AndroidErrorDefinition</em>
	 */
	private AndroidErrorDefinition() {
		// Nothing to do
	}
	
}
