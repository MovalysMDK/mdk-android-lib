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
 * <p>Interface for all redirected action parameters</p>
 *
 *
 */
public interface RedirectActionParameter {

	/**
	 * Add redirections
	 *
	 * @param p_oRedirectActions the actions launched after the end of current action
	 */
	public void redirectTo(Action<?,?,?,?>...p_oRedirectActions);
	
	/**
	 * Add redirections
	 *
	 * @param p_oAction the action launched after the end of current action
	 * @param p_oParameter the parameter to use
	 */
	public void redirectTo(Action<?,?,?,?> p_oAction, ActionParameter p_oParameter);
	
	/**
	 * Add redirections
	 *
	 * @param p_oRedirectActions the actions launched after the end of current action
	 */
	public void redirectTo(List<Action<?,?,?,?>> p_oRedirectActions);

	/**
	 * <p>redirectTo.</p>
	 *
	 * @param p_oActionClass a {@link java.lang.Class} object.
	 */
	public void redirectTo(Class<? extends Action<?,?,?,?>> p_oActionClass);

	/**
	 * Add redirections
	 *
	 * @param p_oParameter the parameter to use
	 * @param p_oActionClass a {@link java.lang.Class} object.
	 */
	public void redirectTo(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameter);

	/**
	 * Returns redirections
	 *
	 * @return the actions to launch after the end of current action
	 */
	public List<ActionHandler> getRedirectActions();
}
