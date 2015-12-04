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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule;

/**
 * Exception thrown on bad BusinessRule definition processing
 */
public class BusinessRuleException extends Exception {

	/**
	 * serial Id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Construct an <em>BusinessRuleException</em> with the specified detail message.</p>
	 * @param p_sMessage the detail message.
	 */
	public BusinessRuleException(final String p_sMessage) {
		super(p_sMessage);
	}
	
	/**
	 * <p>Construct an <em>BusinessRuleException</em> with the specified detail message.</p>
	 * @param p_sMessage the detail message.
	 * @param p_oException the detail exception.
	 */
	public BusinessRuleException(final String p_sMessage, final Throwable p_oException) {
		super(p_sMessage, p_oException);
	}
	
}
