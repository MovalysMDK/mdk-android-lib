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
package com.adeuza.movalysfwk.mf4jcommons.core.beans;

/**
 * <p>TODO Décrire la classe ScopePolicy</p>
 *
 */
public enum ScopePolicy {
	/**
	 * When a bean is a singleton, only one shared instance of the bean will be managed,
	 * and all requests for beans with an id or ids matching that bean definition will result
	 * in that one specific bean instance being returned by the BeanLoader.
	 */
	SINGLETON,

	/**
	 * The non-singleton, prototype scope of bean deployment results in the creation of a new bean instance
	 * every time a request for that specific bean is made.
	 */
	PROTOTYPE
}
