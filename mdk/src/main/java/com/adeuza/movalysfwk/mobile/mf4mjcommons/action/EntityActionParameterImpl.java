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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.action;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;

/**
 * <p>EntityActionParameterImpl class.</p>
 *
 * @param <E> entity
 */
public class EntityActionParameterImpl<E extends MEntity> extends AbstractActionParameter implements ActionParameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -274764062140772174L;
	
	/**
	 * 
	 */
	private E entity ;

	/**
	 * <p>Constructor for EntityActionParameterImpl.</p>
	 *
	 * @param p_oEntity a E object.
	 */
	public EntityActionParameterImpl( E p_oEntity ) {
		this.entity = p_oEntity ;
	}
	
	/**
	 * <p>Getter for the field <code>entity</code>.</p>
	 *
	 * @return a E object.
	 */
	public E getEntity() {
		return entity;
	}
}
