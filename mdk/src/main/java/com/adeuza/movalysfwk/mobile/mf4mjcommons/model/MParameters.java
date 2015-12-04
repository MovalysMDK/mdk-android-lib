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

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;


//@non-generated-start[imports]
//@non-generated-end

/**
 *
 * <p>Interface : MParameters</p>
 *
 *
 *
 *
 *
 */
public interface MParameters extends MEntity
//@non-generated-start[class-signature]
//@non-generated-end
{

	/**
	 * Constante indiquant le nom de l'entité
	 */
	public static final String ENTITY_NAME = "MParameters";

	/**
	 *
	 *
	 * <p>Attribute ID</p>
	 * <p> type=long mandatory=true</p>
	 *
	 * @return une entité long correspondant à la valeur de l'attribut id
	 */
	public long getId();

	/**
	 *
	 *
	 * <p>Attribute ID</p>
	 * <p> type=long mandatory=true</p>
	 *
	 * @param p_lId la valeur à affecter à l'attribut id
	 */
	public void setId(long p_lId);

	/**
	 *
	 *
	 * <p>Attribute NAME</p>
	 * <p> type=String mandatory=true</p>
	 *
	 * @return une entité String correspondant à la valeur de l'attribut name
	 */
	public String getName();

	/**
	 *
	 *
	 * <p>Attribute NAME</p>
	 * <p> type=String mandatory=true</p>
	 *
	 * @param p_sName la valeur à affecter à l'attribut name
	 */
	public void setName(String p_sName);

	/**
	 *
	 *
	 * <p>Attribute VALUE</p>
	 * <p> type=String mandatory=true</p>
	 *
	 * @return une entité String correspondant à la valeur de l'attribut value
	 */
	public String getValue();

	/**
	 *
	 *
	 * <p>Attribute VALUE</p>
	 * <p> type=String mandatory=true</p>
	 *
	 * @param p_sValue la valeur à affecter à l'attribut value
	 */
	public void setValue(String p_sValue);

	//@non-generated-start[methods]
	//@non-generated-end
}
