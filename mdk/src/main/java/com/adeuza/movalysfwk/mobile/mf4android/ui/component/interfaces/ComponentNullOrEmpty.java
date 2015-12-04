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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces;

/**
 * Use in component to declare a custom null or empty behavior.
 * <p>
 * Each component has it's own handled type. If a component need to define a sepcial implémentation
 * of the null or empty checker let this component implements this interface. The interface method is
 * called as a callback from component delegate.
 * </p>
 * <p>This implementation has been choose to limit the methods count in android compiled
 * file</p>
 *
 * @param <VALUE> the type of value to check
 */
public interface ComponentNullOrEmpty<VALUE> {

	/**
	 * Check if the param is null or empty
	 * @param p_oObject the object to check
	 * @return true is the object is null or empty, false otherwise
	 */
	public boolean isNullOrEmptyValue(VALUE p_oObject);
}
