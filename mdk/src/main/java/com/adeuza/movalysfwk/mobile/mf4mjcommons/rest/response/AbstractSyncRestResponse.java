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

import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.gson.Unexposed;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncAck;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncEntityAck;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncRestResponse;

/**
 * <p>Extending class for rest reponse. Contains some extra informations.</p>
 *
 *
 */
public abstract class AbstractSyncRestResponse extends RestResponse implements ISyncRestResponse {

	/**
	 * informations
	 */
	@Unexposed
	private SynchronisationResponseTreatmentInformation informations = new SynchronisationResponseTreatmentInformation();

	/**
	 * Ack to transmit to server in the next request
	 */
	@Unexposed
	private Map<String, ISyncEntityAck> acks = new HashMap<>();

	/**
	 * Return true if ack to transmit
	 */
	@Unexposed
	private boolean hasAcks = false;
	
	/**
	 * resource's identifier.
	 */
	private long resource;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SynchronisationResponseTreatmentInformation getInformations() {
		return this.informations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInformations(SynchronisationResponseTreatmentInformation p_oInformations) {
		this.informations = p_oInformations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, ISyncEntityAck> getAcks() {
		return this.acks;
	}
	
	/**
	 * Set if has acks
	 * @param p_bHasAcks true if response has acks to transmit to the next request
	 */
	protected void setHasAcks(boolean p_bHasAcks) {
		this.hasAcks = p_bHasAcks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract boolean isTerminated();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAcks() {
		return hasAcks;
	}
	
	/**
	 * Getter for resource
	 * @return the resource
	 */
	@Override
	public long getResource() {
		return this.resource;
	}
	
	/**
	 * Setter for resource
	 * @param p_lResource the value to set
	 */
	public void setResource(long p_lResource) {
		this.resource = p_lResource;
	}
	
	/**
	 * <p>addInsertOrUpdateAck.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param p_lAckToAddId a long.
	 * @param p_lVersion a int.
	 */
	public void addInsertOrUpdateAck(String p_sKey, long p_lAckToAddId, int p_lVersion) {
		hasAcks = true;
		ISyncEntityAck oAcks = this.acks.get(p_sKey);
		ISyncAck oAckToAdd = new Ack(p_lAckToAddId, p_lVersion);
		if (oAcks == null) {
			oAcks = new EntityAcks();
			oAcks.setEntity(p_sKey);
			this.acks.put(p_sKey, oAcks);
		}
		oAcks.addInsertOrUpdateAck(oAckToAdd);
	}
	
	/**
	 * <p>addDeleteAck.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param p_lAckToDeleteId a long.
	 */
	public void addDeleteAck(String p_sKey, long p_lAckToDeleteId) {
		hasAcks = true;
		ISyncEntityAck oAcks = this.acks.get(p_sKey);
		ISyncAck oAckToDelete = new Ack(p_lAckToDeleteId, 0);
		if (oAcks == null) {
			oAcks = new EntityAcks();
			oAcks.setEntity(p_sKey);
			this.acks.put(p_sKey, oAcks);
		}
		oAcks.addDeleteAck(oAckToDelete);
	}
}
