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

import java.util.List;

/**
 * Interface implemented on classes when a filter is set on action listeners
 * 
 * @param <OUT> the action result type
 */
public interface ListenerActionClassFilter <OUT extends ActionParameter> {

	/**
	 * called on the class when a filter is set on the listener to check whether it should react
	 * @param p_oClassesList the list of classes applying to the filter
	 * @param p_oActionResult the result of the action
	 * @return true if the class should react to the listener, false otherwise
	 */
	public boolean filterListenerOnActionClass(List<Class<?>> p_oClassesList, OUT p_oActionResult);
	
}
