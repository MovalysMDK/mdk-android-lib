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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 * <p>RestRequestWriter interface.</p>
 *
 */
public interface RestRequestWriter {

	/**
	 * <p>prepare.</p>
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_bfirstGoToServer a boolean.
	 * @param p_mapCurrentObjectsToSynchronize a {@link java.util.Map} object.
	 * @param p_mapSyncTimestamp a {@link java.util.Map} object.
	 * @param p_listAckObjectToSynchronize a {@link java.util.List} object.
	 * @param p_oSynchronizationActionParameterIN a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException if any.
	 */
	public void prepare(
			boolean p_bfirstGoToServer, 
			Map<String, List<MObjectToSynchronize>> p_mapCurrentObjectsToSynchronize,
			Map<String, Long> p_mapSyncTimestamp,
			List<MObjectToSynchronize> p_listAckObjectToSynchronize,
			SynchronizationActionParameterIN p_oSynchronizationActionParameterIN, MContext p_oContext) 
			throws SynchroException ;

	/**
	 * <p>prepareNoSync.</p>
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 * @since 3.1.1
	 */
	public void prepareNoSync( MContext p_oContext) throws RestException ;
	
	/**
	 * True if request writer has data to send
	 *
	 * @return a boolean.
	 * @since 3.1.1
	 */
	public boolean hasData();
	
	/**
	 * Write json into writer
	 *
	 * @param p_oWriter writer
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException exception
	 * @since 3.1.1
	 */
	public void writeJson( Writer p_oWriter ) throws RestException;
	
	/**
	 * Extract Json
	 *
	 * @return Rest request To Json String.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException exception
	 * @since 4.3.0
	 */
	public String toJson() throws RestException;
}
