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
 * Use it on a component to specify a set behavior on view model.
 * <p>
 * Each component that specifies a implementation of one of the set methods should implement
 * this interface.
 * </p>
 * <p>
 * Components that are only readable (user cannot input in the component) should
 * implement this interface only
 * </p>
 * <p>This implementation has been chosen to limit the methods count in android compiled file</p>
 *
 * @param <VALUE> the type handled by the component
 */
public interface ComponentReadableWrapper<VALUE> {

	/**
	 * Sets the component value from the view model
	 * @param p_oObjectToSet objet to set in component
	 */
	public void configurationSetValue(VALUE p_oObjectToSet);
	
}
