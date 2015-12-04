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

import java.util.ArrayList;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRequestBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncEntityAck;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncRestRequest;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 * <p>TODO Décrire la classe AckDomRequestBuilder</p>
 *
 *
 */
public class AckDomRequestBuilder implements DomRequestBuilder<ISyncRestRequest> {

	private Map<String, ISyncEntityAck> acks ;
	
	/**
	 * Constructor
	 * @param p_oAcks map of ack to transmit to server
	 */
	public AckDomRequestBuilder(Map<String, ISyncEntityAck> p_mapAcks) {
		this.acks = p_mapAcks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buildRequest(boolean p_bfirstGoToServer, ISyncRestRequest p_oSyncRequest, SynchronizationActionParameterIN p_oSynchronizationActionParameterIN,
			Map<String, Long> p_oP_mapSyncTimestamp, MContext p_oContext) throws SynchroException {
		p_oSyncRequest.setAcks(new ArrayList<ISyncEntityAck>(this.acks.values()));
	}

}
