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
package com.adeuza.movalysfwk.apache.beanutils;


/**
 * <p>A <strong>ConversionException</strong> indicates that a call to
 * <code>Converter.convert()</code> has failed to complete successfully.
 *
 * @author Craig McClanahan
 * @author Paulo Gaspar
 * @since 1.3
 */

public class ConversionException extends RuntimeException {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new exception with the specified message.
     *
     * @param message The message describing this exception
     */
    public ConversionException(String message) {

        super(message);

    }


    /**
     * Construct a new exception with the specified message and root cause.
     *
     * @param message The message describing this exception
     * @param cause The root cause of this exception
     */
    public ConversionException(String message, Throwable cause) {

        super(message);
        this.cause = cause;

    }


    /**
     * Construct a new exception with the specified root cause.
     *
     * @param cause The root cause of this exception
     */
    public ConversionException(Throwable cause) {

        super(cause.getMessage());
        this.cause = cause;

    }


    // ------------------------------------------------------------- Properties


    /**
     * The root cause of this <code>ConversionException</code>, compatible with
     * JDK 1.4's extensions to <code>java.lang.Throwable</code>.
     */
    protected Throwable cause = null;

    /**
     * Return the root cause of this conversion exception.
     * @return the root cause of this conversion exception
     */
    @Override
	public Throwable getCause() {
        return (this.cause);
    }


}
