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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * <p>Delegate for redirect action parameter</p>
 *
 *
 */
public class RedirectActionParameterDelegate implements Serializable {

	/** serial id */
	private static final long serialVersionUID = -3358536453514604830L;
	
	/** action to use for redirect */
	private List<ActionHandler> actions = null;
	
	/**
	 * Constructs a new delegate
	 */
	public RedirectActionParameterDelegate() {
		this.actions = new ArrayList<ActionHandler>();
	}
	
	/**
	 * Add redirections
	 *
	 * @param p_oRedirectActions the actions launched after the end of current action
	 */
	public void redirectTo(Action<?,?,?,?>...p_oRedirectActions) {
		for(Action<?,?,?,?> oAction : p_oRedirectActions) {
			this.actions.add(new ActionHandler(oAction));
		}
	}
	
	/**
	 * Add redirections
	 *
	 * @param p_oRedirectAction the action launched after the end of current action
	 * @param p_oParameter the parameter to use
	 */
	public void redirectTo(Action<?,?,?,?> p_oRedirectAction, ActionParameter p_oParameter) {
		this.actions.add(new ActionHandler(p_oRedirectAction, p_oParameter));
	}
	
	/**
	 * Add redirections
	 *
	 * @param p_oActionClass a {@link java.lang.Class} object.
	 */
	public void redirectTo(Class<? extends Action<?,?,?,?>> p_oActionClass) {
		Action<?,?,?,?> oAction = BeanLoader.getInstance().getBean(p_oActionClass);
		this.redirectTo(oAction);
	}
	
	/**
	 * Add redirections
	 *
	 * @param p_oParameter the parameter to use
	 * @param p_oActionClass a {@link java.lang.Class} object.
	 */
	public void redirectTo(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameter) {
		Action<?,?,?,?> oAction = BeanLoader.getInstance().getBean(p_oActionClass);
		this.redirectTo(oAction, p_oParameter);
	}
	
	/**
	 * Add redirections
	 *
	 * @param p_oRedirectActions the actions launched after the end of current action
	 */
	public void redirectTo(List<Action<?,?,?,?>> p_oRedirectActions) {
		for(Action<?,?,?,?> oAction : p_oRedirectActions) {
			this.actions.add(new ActionHandler(oAction));
		}
	}
	
	/**
	 * Returns redirections
	 *
	 * @return the actions to launch after the end of current action
	 */
	public List<ActionHandler> getRedirectActions() {
		return this.actions;
	}
}
