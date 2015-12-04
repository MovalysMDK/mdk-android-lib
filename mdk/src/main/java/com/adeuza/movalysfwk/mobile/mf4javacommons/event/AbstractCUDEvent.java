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
package com.adeuza.movalysfwk.mobile.mf4javacommons.event;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;

/**
 * <p>AbstractCUDEvent class.</p>
 *
 * @param <DATA> entity
 */
public class AbstractCUDEvent<DATA extends MEntity> extends AbstractBusinessEvent<DATA> implements CUDEvent<DATA> {

	/**
	 * 
	 */
	private CUDType type ;
	
	/**
	 * <p>Constructor for AbstractCUDEvent.</p>
	 *
	 * @param p_oSource a {@link java.lang.Object} object.
	 * @param p_oData a DATA object.
	 * @param p_oEnum a {@link com.adeuza.movalysfwk.mobile.mf4javacommons.event.CUDType} object.
	 */
	public AbstractCUDEvent(Object p_oSource, DATA p_oData, CUDType p_oEnum) {
		super(p_oSource, p_oData);
		this.type = p_oEnum ;
	}
	
	/* (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4javacommons.event.CUDEvent#getType()
	 */
	/** {@inheritDoc} */
	@Override
	public CUDType getType() {
		return this.type ;
	}
}
