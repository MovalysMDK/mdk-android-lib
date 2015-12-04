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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.stream;

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.MJsonWriter;

/**
 * <p>StreamRequestBuilder interface.</p>
 *
 */
public interface StreamRequestBuilder {

	/**
	 * <p>writeRequest.</p>
	 *
	 * @param p_oJsonWriter a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.MJsonWriter} object.
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 */
	public void writeRequest( MJsonWriter p_oJsonWriter, MContext p_oContext ) throws RestException ;
	
	/**
	 * Return list of MObjectToSynchronizeToDelete after successfull synchronization
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 * @return a {@link java.util.List} object.
	 */
	public List<MObjectToSynchronize> getListMObjectToSynchronizeToDelete() throws RestException ;
}
