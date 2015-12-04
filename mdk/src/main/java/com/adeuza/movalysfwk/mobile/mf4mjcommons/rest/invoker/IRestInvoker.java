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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;

/**
 * RestInvoker: invoke rest services
 *
 * @param <R>
 */
public interface IRestInvoker<R extends IRestResponse> {

	/**
	 * Rest invoker initialization
	 *
	 * @param p_oAction action
	 * @param p_oConfig config
	 */
	public void init(Action<?, ?, ?, ?> p_oAction, RestConnectionConfig p_oConfig );
		
	/**
	 * Prepare data for invocation
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 */
	public void prepare() throws RestException;
	
	/**
	 * <p>
	 * 	Invoke service rest.
	 * 	It's necessary to call, before this method, <em>RestInvoker.prepare()</em>.
	 * </p>
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if it's impossible to join server, or if the response is bad.
	 * @param p_oInvocationParams a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 */
	public void invoke( RestInvocationConfig<R> p_oInvocationParams, MContext p_oContext ) throws RestException ;
	
	/**
	 * <p>
	 * 	Threat the result of a service rest.
	 * 	It's necessary to call, before this method, <em>RestInvoker.prepare()</em>
	 * 	and <em>RestInvoker.invoke()</em>
	 * </p>
	 *
	 * @param p_oInvocationConfig invocation config
	 * @param p_oConnectionConfig connection config
	 * @param p_oNotifier notifier notifier
	 * @param p_oContext context
	 * @return a java object
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if it's impossible to join server, or if the response is bad.
	 */
	public R process(RestInvocationConfig<R> p_oInvocationConfig, RestConnectionConfig p_oConnectionConfig, Notifier p_oNotifier, MContext p_oContext) throws RestException ;
	
	/**
	 * Add "get" parameter to url
	 *
	 * @param p_oObject object to add to url
	 */
	public void appendGetParameter(Object p_oObject);
	
	/**
	 * returns true if the Https connection should be used
	 *
	 * @return true if the Https connection should be used
	 */
	public boolean isHttpsConnectionEnabled();
}
