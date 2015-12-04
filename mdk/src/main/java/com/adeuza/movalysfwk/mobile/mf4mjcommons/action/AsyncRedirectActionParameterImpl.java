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
 * <p>Uses this class when a asynchrone action needs redirection</p>
 *
 *
 */
public class AsyncRedirectActionParameterImpl extends AbstractActionParameter implements ActionParameter, AsyncRedirectActionParameter {

	/** Serial id */
	private static final long serialVersionUID = -5647356233275066682L;
	
	/** delegate */
	private RedirectActionParameterDelegate rapDelegate = null;
	
	/**
	 * Constructs a new action parameter
	 */
	public AsyncRedirectActionParameterImpl() {
		this.rapDelegate = new RedirectActionParameterDelegate();
	}

	/** {@inheritDoc} */
	@Override
	public void asyncRedirectTo(Action<?,?,?,?>... p_oRedirectActions) {
		this.rapDelegate.redirectTo(p_oRedirectActions);
	}
	
	/** {@inheritDoc} */
	@Override
	public void asyncRedirectTo(List<Action<?,?,?,?>> p_oRedirectActions) {
		this.rapDelegate.redirectTo(p_oRedirectActions);		
	}

	/** {@inheritDoc} */
	@Override
	public List<ActionHandler> getAsyncRedirectActions() {
		return this.rapDelegate.getRedirectActions();
	}
}
