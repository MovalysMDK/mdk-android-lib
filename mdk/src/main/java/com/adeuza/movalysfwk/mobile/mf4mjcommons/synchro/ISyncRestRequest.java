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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequest;

/**
 * RestRequest for synchronisation
 *
 */
public interface ISyncRestRequest extends RestRequest {

	/**
	 * Return list of acks to transmit to server
	 * @return the acks list
	 */
	public List<ISyncEntityAck> getAcks();
	
	/**
	 * Set list of acks to transmit to server
	 * @param p_listAcks list of ack
	 */
	public void setAcks(List<ISyncEntityAck> p_listAcks);
}
