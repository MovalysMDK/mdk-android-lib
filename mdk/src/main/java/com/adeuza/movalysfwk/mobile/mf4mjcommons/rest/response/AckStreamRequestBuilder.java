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

import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.NotImplementedException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.stream.StreamRequestBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncEntityAck;

/**
 * <p>TODO Décrire la classe AckStreamRequestBuilder</p>
 *
 *
 */
public class AckStreamRequestBuilder implements StreamRequestBuilder {

	/**
	 * Ack list
	 */
	private Map<String, ISyncEntityAck> acks ;
	
	/**
	 * Constructor
	 * @param p_listAcks ack list to transmit to server.
	 */
	public AckStreamRequestBuilder(Map<String, ISyncEntityAck> p_listAcks) {
		this.acks = p_listAcks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeRequest(MJsonWriter p_oJsonWriter, MContext p_oContext) throws RestException {
		throw new NotImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MObjectToSynchronize> getListMObjectToSynchronizeToDelete() throws RestException {
		return null;
	}
}
