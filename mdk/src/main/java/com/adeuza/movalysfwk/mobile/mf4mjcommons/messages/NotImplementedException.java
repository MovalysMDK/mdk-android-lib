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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.messages;

/**
 * <p>
 * 	This exception is thrown when a method is not implemented on Movalys.<br>
 *  <em>Extends RuntimeException</em>.
 * </p>
 *
 *
 * @since Barcelone (24 sept. 2010)
 * @see java.lang.RuntimeException
 */
public class NotImplementedException extends RuntimeException {

	/** <p>serial version</p> */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construct an object <em>NotImplementedException</em>.
	 */
	public NotImplementedException() {
		super();
	}
	
	/**
	 * <p>Construct an <em>NotImplementedException</em> with the specified detail message.</p>
	 *
	 * @param p_sMessage the detail message.
	 */
	public NotImplementedException(String p_sMessage) {
		super(p_sMessage);
	}
}
