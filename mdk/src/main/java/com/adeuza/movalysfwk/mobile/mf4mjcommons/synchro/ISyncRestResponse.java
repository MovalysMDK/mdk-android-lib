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

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;

/**
 * <p>ISyncRestResponse</p>
 *
 */
public interface ISyncRestResponse extends IRestResponse {

	/**
	 * Return true if response contains elements to ack
	 * @return true if response contains elements to ack
	 */
	public boolean hasAcks();
	
	/**
	 * Return false if data has been received partially and another request needs to be done. 
	 * @return false if data has been received partially and another request needs to be done. 
	 */
	public boolean isTerminated();
	
	/**
	 * Return the map of acks to send to server in the next request 
	 * @return map of acks to send to server in the next request 
	 */
	public Map<String, ISyncEntityAck> getAcks();
	
	/**
	 * Return additional informations about the response
	 * @return additional informations about the response
	 */
	public SynchronisationResponseTreatmentInformation getInformations();

	/**
	 * Set additional informations about the response
	 * @param p_oInformations additional informations
	 */
	public void setInformations(SynchronisationResponseTreatmentInformation p_oInformations);
	
	/**
	 * Resource id to identify user
	 * @return  id to identify user
	 */
	public long getResource();
}
