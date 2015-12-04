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

/**
 * List of acks for an entity type.
 *
 */
public interface ISyncEntityAck {

	/**
	 * Entity name
	 * @return entity name
	 */
	public String getEntity();
	
	/**
	 * Set entity name
	 * @param p_sEntity entity name
	 */
	public void setEntity(String p_sEntity);
	
	/**
	 * Acks for insert/update operations
	 * @return acks for insert/update operations
	 */
	public List<ISyncAck> getInsertOrUpdate();
	
	/**
	 * Add an ack for insert/update operation
	 * @param p_oAck ack for insert/update operation
	 */
	public void addInsertOrUpdateAck(ISyncAck p_oAck);
	
	/**
	 * Set ack list for for insert/update operation
	 * @param p_listInsertOrUpdate ack list for for insert/update operation
	 */
	public void setInsertOrUpdate(List<ISyncAck> p_listInsertOrUpdate);
	
	/**
	 * Acks for delete operations
	 * @return acks for delete operations
	 */
	public List<ISyncAck> getDelete();
	
	/**
	 * Set ack list for delete operations
	 * @param p_listDelete the list of acks
	 */
	public void setDelete(List<ISyncAck> p_listDelete);
	
	/**
	 * Add an ack for a delete operation
	 * @param p_oAck ack for a delete operation
	 */
	public void addDeleteAck(ISyncAck p_oAck);
}
