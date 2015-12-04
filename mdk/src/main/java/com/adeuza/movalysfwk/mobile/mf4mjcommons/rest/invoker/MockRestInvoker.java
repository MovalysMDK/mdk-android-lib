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

import java.io.IOException;
import java.io.InputStream;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;

/**
 * MockRestInvoker
 *
 * @param <R>
 */
public class MockRestInvoker<R extends IRestResponse> implements IRestInvoker<R>{


	/**
	 * File to send as a response
	 */
	private String file ;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker#init(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader)
	 */
	@Override
	public void init(Action<?, ?, ?, ?> p_oAction,  RestConnectionConfig p_oConfig ) {
		this.file = p_oConfig.getCommand();
	}

	/** {@inheritDoc} */
	@Override
	public void prepare() {
		// nothing to do
	}

	/** {@inheritDoc} */
	@Override
	public void invoke( RestInvocationConfig<R> p_oInvocationParams, MContext p_oContext) throws RestException {
		try {			
			// nothing to do
		}
		catch (Exception oException) {
			Application.getInstance().getLog().error("RestInvoker", "Erreur", oException);
			throw new RestException("MockRestInvoker exception", oException);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker#process(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public R process( RestInvocationConfig<R> p_oInvocationParams, RestConnectionConfig p_oConnectionConfig, Notifier p_oNotifier, MContext p_oContext) throws RestException {
		R r_oResult = null ;
		try {
			InputStream oIs = this.getResponseInputStream(this.file);
			try {
				r_oResult = p_oInvocationParams.getResponseReader().readResponse(p_oNotifier, oIs, p_oContext);
			} finally {
				oIs.close();
			}
		} catch( Exception oException ) {
			throw new RestException("MockRestInvoker exception", oException);
		}
		return r_oResult;
	}

	/**
	 * <p>getResponseInputStream.</p>
	 *
	 * @param p_sParam a {@link java.lang.String} object.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.io.IOException if any.
	 */
	protected InputStream getResponseInputStream( String p_sParam ) throws IOException {
		return this.getClass().getResourceAsStream( "/" + p_sParam);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker#appendGetParameter(java.lang.Object)
	 */
	@Override
	public void appendGetParameter(Object p_oObject) {
		
	}

	/** {@inheritDoc} */
	@Override
	public boolean isHttpsConnectionEnabled() {
		return false;
	}
}
