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

//@non-generated-start[imports]
import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
//@non-generated-end

/**
 *
 * <p>Interface : MObjectToSynchronize</p>
 *
 *
 *
 *
 *
 */
public interface MObjectToSynchronize extends MEntity
//@non-generated-start[class-signature]
//@non-generated-end
{

	/**
	 * Constante indiquant le nom de l'entité
	 */
	public static final String ENTITY_NAME = "MObjectToSynchronize";

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
	 * <p>Attribute OBJECTID</p>
	 * <p> type=long mandatory=true</p>
	 *
	 * @return une entité long correspondant à la valeur de l'attribut objectId
	 */
	public long getObjectId();

	/**
	 *
	 *
	 * <p>Attribute OBJECTID</p>
	 * <p> type=long mandatory=true</p>
	 *
	 * @param p_lObjectId la valeur à affecter à l'attribut objectId
	 */
	public void setObjectId(long p_lObjectId);

	/**
	 *
	 *
	 * <p>Attribute OBJECTNAME</p>
	 * <p> type=String mandatory=true</p>
	 *
	 * @return une entité String correspondant à la valeur de l'attribut objectName
	 */
	public String getObjectName();

	/**
	 *
	 *
	 * <p>Attribute OBJECTNAME</p>
	 * <p> type=String mandatory=true</p>
	 *
	 * @param p_sObjectName la valeur à affecter à l'attribut objectName
	 */
	public void setObjectName(String p_sObjectName);

	//@non-generated-start[methods]
	//@non-generated-end
}
