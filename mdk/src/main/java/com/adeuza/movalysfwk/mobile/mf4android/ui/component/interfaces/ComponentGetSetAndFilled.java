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
 * Use in component to specify get, set and is filled behaviors.
 * <p>
 * Each component that specify a implemention of one of those methods should implements
 * this interface.
 * </p>
 * <p>This implementation has been choose to limit the methods count in android compiled
 * file</p>
 *
 * @param <VALUE> the type handled by component
 */
public interface ComponentGetSetAndFilled<VALUE> extends ComponentWritableWrapper<VALUE>, ComponentReadableWrapper<VALUE> {
	
}
