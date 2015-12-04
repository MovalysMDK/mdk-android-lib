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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom;

import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequest;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 * <p>DomEntityRequestBuilder interface.</p>
 *
 * @param <E>
 */
public interface DomEntityRequestBuilder<E extends MEntity,R extends RestRequest> {

	/**
	 * <p>buildRequest.</p>
	 *
	 * @param listSyncObjets a {@link java.util.List} object.
	 * @param p_oSyncRequest a R object.
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_mapSyncTimestamp a {@link java.util.Map} object.
	 * @param listAckSyncObjets a {@link java.util.List} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException if any.
	 */
	public void buildRequest( List<MObjectToSynchronize> listSyncObjets, 
			R p_oSyncRequest, 
			Map<String, Long> p_mapSyncTimestamp,
			List<MObjectToSynchronize> listAckSyncObjets,
			MContext p_oContext ) throws SynchroException;
}
