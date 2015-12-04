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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncAck;

/**
 * <p>Ack class.</p>
 *
 */
public class Ack implements ISyncAck  {

	private long id = 0;
	
	private int version = 0;

	/**
	 * <p>Constructor for Ack.</p>
	 */
	public Ack() {
		//Nothing to do
	}
	
	/**
	 * <p>Constructor for Ack.</p>
	 *
	 * @param p_lId a long.
	 * @param p_lVersion a int.
	 */
	public Ack(long p_lId, int p_lVersion) {
		this.id = p_lId;
		this.version = p_lVersion;
	}
	
	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * <p>Setter for the field <code>id</code>.</p>
	 *
	 * @param p_lId the id to set
	 */
	public void setId(long p_lId) {
		this.id = p_lId;
	}

	/**
	 * <p>Getter for the field <code>version</code>.</p>
	 *
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * <p>Setter for the field <code>version</code>.</p>
	 *
	 * @param p_iVersion the version to set
	 */
	public void setVersion(int p_iVersion) {
		this.version = p_iVersion;
	}
	
	
}
