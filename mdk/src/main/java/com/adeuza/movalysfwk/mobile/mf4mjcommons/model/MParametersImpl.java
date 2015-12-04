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
 * Implementation of AbstractEntity with MParameters parameter
 *
 */
@SuppressWarnings("serial")
public class MParametersImpl extends AbstractEntity<MParameters> implements MParameters, Serializable {

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
	 * <p>Attribute NAME</p>
	 * <p> type=String mandatory=true</p>
	 */

	//@non-generated-start[attribute-name]
	//@non-generated-end[attribute-name]
	public String name;

	/**
	 * 
	 * 
	 * <p>Attribute VALUE</p>
	 * <p> type=String mandatory=true</p>
	 */

	//@non-generated-start[attribute-value]
	//@non-generated-end[attribute-value]
	public String value;

	//@non-generated-start[attributes]
	//@non-generated-end

	/**
	 * Constructor
	 */
	protected MParametersImpl() {
		this.id = -1;
		this.name = null;
		this.value = null;

		//@non-generated-start[constructor]
		//@non-generated-end
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalysfwk.mf4model.model.MParameters#getId()
	 * @return a long.
	 */
	@Override
	public long getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mf4model.model.MParameters#setId(long)
	 */
	@Override
	public void setId(long p_lId) {

		this.id = p_lId;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalysfwk.mf4model.model.MParameters#getName()
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mf4model.model.MParameters#setName(java.lang.String)
	 */
	@Override
	public void setName(String p_sName) {

		this.name = p_sName;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalysfwk.mf4model.model.MParameters#getValue()
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mf4model.model.MParameters#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String p_sValue) {

		this.value = p_sValue;

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
