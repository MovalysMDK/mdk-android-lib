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
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncAck;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncEntityAck;

/**
 * <p>EntityAcks class.</p>
 *
 */
public class EntityAcks implements ISyncEntityAck  {

	private String entity ;
	
	private List<ISyncAck> insertOrUpdate = new ArrayList<ISyncAck>();
	
	private List<ISyncAck> delete = new ArrayList<ISyncAck>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEntity() {
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEntity(String p_sEntity) {
		this.entity = p_sEntity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ISyncAck> getInsertOrUpdate() {
		return insertOrUpdate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInsertOrUpdate(List<ISyncAck> p_listInsertOrUpdate) {
		this.insertOrUpdate = p_listInsertOrUpdate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addInsertOrUpdateAck(ISyncAck p_oAck) {
		this.insertOrUpdate.add(p_oAck);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ISyncAck> getDelete() {
		return delete;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDelete(List<ISyncAck> delete) {
		this.delete = delete;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDeleteAck(ISyncAck p_oAck) {
		this.delete.add(p_oAck);
	}
}
