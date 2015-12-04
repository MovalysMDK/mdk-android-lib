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

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequestWriter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 * <p>StreamRequestWriter class.</p>
 *
 */
public class StreamRequestWriter implements RestRequestWriter {

	/**
	 * Request Builders
	 */
	private List<StreamRequestBuilder> requestBuilders = new ArrayList<StreamRequestBuilder>();
	
	/** {@inheritDoc} */
	@Override
	public void prepare(boolean p_bfirstGoToServer, Map<String, List<MObjectToSynchronize>> mapcurrentObjectsToSynchronize,
			Map<String, Long> p_mapSyncTimestamp, List<MObjectToSynchronize> p_listAckObjectToSynchronize,
			SynchronizationActionParameterIN p_oSynchronizationActionParameterIN, MContext p_oContext)
			throws SynchroException {
		// Nothing to do
	}
	
	/** {@inheritDoc} */
	@Override
	public void prepareNoSync(MContext p_oContext) throws RestException {
		// Nothing to do
	}
	
	/**
	 * <p>addRestBuilder.</p>
	 *
	 * @param p_oRestRequestWriter a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.stream.StreamRequestBuilder} object.
	 */
	public void addRestBuilder( StreamRequestBuilder p_oRestRequestWriter ) {
		this.requestBuilders.add(p_oRestRequestWriter);
	}

	/** {@inheritDoc} */
	@Override
	public void writeJson(Writer p_oWriter) throws RestException {
		
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasData() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public String toJson() throws RestException {
		return null;
	}
}
