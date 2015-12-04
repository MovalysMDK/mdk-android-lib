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
 * Use it on a component to specify a get behavior on view model.
 * <p>
 * Each component that specifies a implementation of one of the get methods should implement
 * this interface.
 * </p>
 * <p>
 * Components that are only writable (user cannot read from the component) should
 * implement this interface only
 * </p>
 * <p>This implementation has been chosen to limit the methods count in android compiled file</p>
 *
 * @param <VALUE> the type handled by the component
 */
public interface ComponentWritableWrapper<VALUE> {

	/**
	 * Get the component value to put it in the view model
	 * @return the component value
	 */
	public VALUE configurationGetValue();
	
	/**
	 * Check if the component is filled
	 * @return true if the component is filled, false otherwise
	 */
	public boolean isFilled();
	
}
