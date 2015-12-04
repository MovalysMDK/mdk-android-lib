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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response;

import java.io.InputStream;
import java.io.Reader;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;

/**
 * Reader to read a rest response
 *
 * @param <R>
 */
public interface RestResponseReader<R extends IRestResponse> {

	/**
	 * Read rest response from input stream
	 *
	 * @param p_oNotifier notifier
	 * @param p_oIs inputstream
	 * @param p_oContext context
	 * @return response object
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 */
	public R readResponse(Notifier p_oNotifier, InputStream p_oIs, MContext p_oContext ) throws RestException ;
	
	/**
	 * Read rest response from char stream
	 *
	 * @param p_oCharStream char stream
	 * @param p_oNotifier notifier
	 * @param p_oContext context
	 * @return response object
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 */
	public R readResponse(Reader p_oCharStream, Notifier p_oNotifier, MContext p_oContext) throws RestException;
	
	/**
	 * Create the response object
	 *
	 * @return instance of response object
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 */
	public R createResponse() throws RestException ;
}
