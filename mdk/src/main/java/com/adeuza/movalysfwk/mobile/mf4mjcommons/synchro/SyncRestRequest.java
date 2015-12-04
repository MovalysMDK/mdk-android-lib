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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro;

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RequestTime;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequestImpl;

/**
 * <p>SyncRestRequest</p>
 *
 */
public class SyncRestRequest extends RestRequestImpl implements ISyncRestRequest {

	/** acks list */
	private List<ISyncEntityAck> acks ;
	
	/** full request */
	private boolean fullRequest;
	
	/** request time */
	private RequestTime requestTime ;
	
	/**
	 * Default constructor with definition of the request time of th e mobile
	 */
	public SyncRestRequest() {
		this.requestTime = new RequestTime();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ISyncEntityAck> getAcks() {
		return acks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAcks(List<ISyncEntityAck> p_listAcks) {
		this.acks = p_listAcks;
	}

	/**
	 * Return true if full request mode
	 * @return true if full request mode
	 */
	public boolean isFullRequest() {
		return fullRequest;
	}

	/**
	 * Set true if full request mode
	 * @param p_bFullRequest true if full request mode
	 */
	public void setFullRequest(boolean p_bFullRequest) {
		this.fullRequest = p_bFullRequest;
	}	
		
	/**
	 * Accessor of the definition of the time of the mobile 
	 * @return current time + timezone raw offset + timezone name
	 */
	public RequestTime getTime() {
		return this.requestTime;
	}
}
