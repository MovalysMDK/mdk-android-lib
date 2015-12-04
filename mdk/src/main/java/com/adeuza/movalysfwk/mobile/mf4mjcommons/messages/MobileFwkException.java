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

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;

/**
 * <p>
 * 	Exception thrown by the framework application Movalys.
 *  This exception extends RuntimeException object.
 * </p>
 *
 *
 * @since Barcelone
 * @see java.lang.RuntimeException
 */
public class MobileFwkException extends RuntimeException {

	/** <p>serial version</p> */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructs an <em>MobileFwkException</em> with the specified message.</p>
	 *
	 * @param p_sCode
	 * 		The message code.
	 * @param p_sLabel
	 * 		The message label.
	 */
	public MobileFwkException(String p_sCode, String p_sLabel) {
		super(StringUtils.concat(p_sCode, ' ', p_sLabel));
	}

	/**
	 * <p>Constructs an <em>MobileFwkException</em> with the specified message.</p>
	 *
	 * @param p_sCode
	 * 		The message code.
	 * @param p_sLabel
	 * 		The message label.
	 * @param p_oCause
	 * 		The detail exception.
	 */
	public MobileFwkException(String p_sCode, String p_sLabel, Throwable p_oCause) {
		super(StringUtils.concat(p_sCode, ' ', p_sLabel), p_oCause);
	}

	/**
	 * <p>Constructs an <em>MobileFwkException</em>e.</p>
	 *
	 * @param p_oCause
	 * 		The detail exception.
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public MobileFwkException(String p_sMessage, Throwable p_oCause) {
		super(p_sMessage, p_oCause);
	}
	
	/**
	 * <p>Constructs an <em>MobileFwkException</em>e.</p>
	 *
	 * @param p_oCause
	 * 		The detail exception.
	 */
	public MobileFwkException(Throwable p_oCause) {
		super(p_oCause);
	}
}
