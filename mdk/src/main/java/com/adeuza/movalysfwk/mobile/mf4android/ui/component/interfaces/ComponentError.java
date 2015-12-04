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

import android.widget.TextView;

/**
 * Use in component to set error.
 * <p>
 * Each component can specify a error component that hold the error (only {@link TextView} can hold
 * errors in Android).
 * </p>
 * <p>This implementation has been choose to limit the methods count in android compiled
 * file</p>
 *
 */
public interface ComponentError {

	/**
	 * Return a component to set an error on
	 * @return a {@link TextView} to set error on
	 */
	public TextView getErrorView();
	
}
