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
 * Use in component to do custom treatments on bind/destroy.
 * <p>
 * Each component can specify a custom implementation of bind/destroy. Implementing
 * this interface give access to custom implementation. This interface methods are 
 * called as callback from component delegate.
 * </p>
 * <p>This implementation has been choose to limit the methods count in android compiled
 * file</p>
 *
 */
public interface ComponentBindDestroy {

	/**
	 * Do specific treatment after view model binding
	 */
	public void doOnPostAutoBind();
	
	/**
	 * Do specifiy treatment on component destroy
	 */
	public void destroy();
	
}
