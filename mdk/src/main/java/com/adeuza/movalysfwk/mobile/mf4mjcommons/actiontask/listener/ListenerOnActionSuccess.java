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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;

/**
 * <p>Annotation à utiliser pour déclarer une méthode "callback" de la fin en succès d'une action. Ne fonctionne que sur un screen ou un fragment.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerOnActionSuccess {

	/**
	 * Indique à quelle action la méthode "callback" est attachée
	 *
	 * @return an array of {@link java.lang.Class} objects.
	 */
	public Class<? extends Action<?,?,?,?>>[] action();
	
	/**
	 * Filter on action triggering classes which will react to this listener <br/>
	 * Add a class to the list and implement the {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ListenerActionClassFilter} interface on it to activate the filter
	 */
	public Class<?>[] classFilters() default {};
}
