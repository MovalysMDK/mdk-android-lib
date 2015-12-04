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
 * DONT USE THIS, IT WILL BE REMOVED IN NEXT VERSION ; USE {@link ComponentReadableWrapper} INSTEAD
 * <br/>
 * Use in component to specify set behaviors.
 * <p>
 * Each component that specify a implemention of one of the set methods should implements
 * this interface.
 * </p>
 * <p>
 * Components that are only in writable (user cannot input in the component) should
 * implement this interface only (not ComponentGetSetAndFilled)
 * </p>
 * <p>This implementation has been choose to limit the methods count in android compiled
 * file</p>
 *
 * @param <VALUE> the type handled by component
 */
@Deprecated
public interface ComponentWriteOnly<VALUE> {

	/**
	 * Set the component value from the view model
	 * @param p_oObjectToSet objet to set in component
	 */
	public void configurationSetValue(VALUE p_oObjectToSet);

}
