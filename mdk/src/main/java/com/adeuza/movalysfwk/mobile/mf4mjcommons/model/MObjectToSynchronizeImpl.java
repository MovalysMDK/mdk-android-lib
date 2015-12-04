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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.model;

import java.io.Serializable;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.AbstractEntity;

//@non-generated-start[imports]
//@non-generated-end

/**
 * MParametersImpl
 * 
 * Implementation of AbstractEntity with MObjectToSynchronize parameter
 *
 */
@SuppressWarnings("serial")
public class MObjectToSynchronizeImpl extends AbstractEntity<MObjectToSynchronize> implements MObjectToSynchronize, Serializable {

	/**
	 * 
	 * 
	 * <p>Attribute ID</p>
	 * <p> type=long mandatory=true</p>
	 */

	//@non-generated-start[attribute-id]
	//@non-generated-end[attribute-id]
	public long id;

	/**
	 * 
	 * 
	 * <p>Attribute OBJECTID</p>
	 * <p> type=long mandatory=true</p>
	 */

	//@non-generated-start[attribute-objectId]
	//@non-generated-end[attribute-objectId]
	public long objectId;

	/**
	 * 
	 * 
	 * <p>Attribute OBJECTNAME</p>
	 * <p> type=String mandatory=true</p>
	 */

	//@non-generated-start[attribute-objectName]
	//@non-generated-end[attribute-objectName]
	public String objectName;

	//@non-generated-start[attributes]
	//@non-generated-end

	/**
	 * Constructor
	 */
	protected MObjectToSynchronizeImpl() {
		this.id = -1;
		this.objectId = 0;
		this.objectName = null;

		//@non-generated-start[constructor]
		//@non-generated-end
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalysfwk.mf4model.model.MObjectToSynchronize#getId()
	 * @return a long.
	 */
	@Override
	public long getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mf4model.model.MObjectToSynchronize#setId(long)
	 */
	@Override
	public void setId(long p_lId) {

		this.id = p_lId;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalysfwk.mf4model.model.MObjectToSynchronize#getObjectId()
	 * @return a long.
	 */
	@Override
	public long getObjectId() {
		return this.objectId;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mf4model.model.MObjectToSynchronize#setObjectId(long)
	 */
	@Override
	public void setObjectId(long p_lObjectId) {

		this.objectId = p_lObjectId;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalysfwk.mf4model.model.MObjectToSynchronize#getObjectName()
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String getObjectName() {
		return this.objectName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mf4model.model.MObjectToSynchronize#setObjectName(java.lang.String)
	 */
	@Override
	public void setObjectName(String p_sObjectName) {

		this.objectName = p_sObjectName;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalys.fwk.core.beans.MEntity#idToString()
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String idToString() {
		StringBuffer r_sId = new StringBuffer();
		r_sId.append(String.valueOf(this.id));
		return r_sId.toString();
	}

	//@non-generated-start[methods]
	//@non-generated-end
}
