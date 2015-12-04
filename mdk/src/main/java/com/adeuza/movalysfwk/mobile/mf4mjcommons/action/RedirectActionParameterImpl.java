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
 * <p>Uses this class when action needs parameter for redirection</p>
 *
 *
 */
public class RedirectActionParameterImpl extends AbstractActionParameter implements ActionParameter, RedirectActionParameter {

	/** serial id */
	private static final long serialVersionUID = 8195091666239823221L;
	
	/** delegate */
	private RedirectActionParameterDelegate rapDelegate = null;
	
	/**
	 * Construct new redirect action parameter
	 */
	public RedirectActionParameterImpl() {
		this.rapDelegate = new RedirectActionParameterDelegate();
	}

	/** {@inheritDoc} */
	@Override
	public void redirectTo(Action<?,?,?,?>... p_oRedirectActions) {
		this.rapDelegate.redirectTo(p_oRedirectActions);
	}
	
	/** {@inheritDoc} */
	@Override
	public void redirectTo(Action<?,?,?,?> p_oAction, ActionParameter p_oParameter) {
		this.rapDelegate.redirectTo(p_oAction, p_oParameter);
	}
	
	/** {@inheritDoc} */
	@Override
	public void redirectTo(List<Action<?,?,?,?>> p_oRedirectActions) {
		this.rapDelegate.redirectTo(p_oRedirectActions);
	}

	/** {@inheritDoc} */
	@Override
	public void redirectTo(Class<? extends Action<?,?,?,?>> p_oActionClass) {
		this.rapDelegate.redirectTo(p_oActionClass);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Add redirections
	 */
	@Override
	public void redirectTo(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameter) {
		this.rapDelegate.redirectTo(p_oActionClass, p_oParameter);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ActionHandler> getRedirectActions() {
		return this.rapDelegate.getRedirectActions();
	}
}
