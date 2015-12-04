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

/**
 * <p>Exception thrown by actions.</p>
 *
 *
 */
public class ActionException extends Exception {

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 7342304772526458175L;

	/**
	 * Constructs a new action exception with <code>null</code> as its
	 * detail message.  The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 */
	public ActionException() {
		super();
	}

	/**
	 * Constructs a new action exception with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 *
	 * @param p_sMessage
	 * 		The detail message. The detail message is saved for
	 *		later retrieval by the {@link #getMessage()} method.
	 */
	public ActionException(String p_sMessage) {
		super(p_sMessage);
	}

	/**
	 * Constructs a new action exception with the specified detail message and
	 * cause. <p>Note that the detail message associated with
	 * <code>cause</code> is <i>not</i> automatically incorporated in
	 * this runtime exception's detail message.
	 *
	 * @param p_sMessage the detail message (which is saved for later retrieval
	 *		by the {@link #getMessage()} method).
	 * @param p_oCause the cause (which is saved for later retrieval by the
	 *		 {@link #getCause()} method).  (A <tt>null</tt> value is
	 *		 permitted, and indicates that the cause is nonexistent or
	 *		 unknown.)
	 * @since  1.4
	 */
	public ActionException(String p_sMessage, Throwable p_oCause) {
		super(p_sMessage, p_oCause);
	}

	/**
	 * Constructs a new action exception with the specified cause and a
	 * detail message of <tt>(cause==null ? null : cause.toString())</tt>
	 * (which typically contains the class and detail message of
	 * <tt>cause</tt>).  This constructor is useful for runtime exceptions
	 * that are little more than wrappers for other throwables.
	 *
	 * @param  cause the cause (which is saved for later retrieval by the
	 *		 {@link #getCause()} method).  (A <tt>null</tt> value is
	 *		 permitted, and indicates that the cause is nonexistent or
	 *		 unknown.)
	 * @since  1.4
	 */
	public ActionException(Throwable cause) {
		super(cause);
	}
}
