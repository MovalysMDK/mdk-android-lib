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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.gson;

import java.lang.reflect.Type;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.EntityFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.AbstractEntityHelper;
import com.google.gson.InstanceCreator;

/**
 * <p>JsonEntityInstanceCreator class.</p>
 *
 */
public class JsonEntityInstanceCreator implements InstanceCreator<Object> {

	/** {@inheritDoc} */
	@Override
	public Object createInstance(Type p_oType) {
		Object r_oResult = null ;
		if ( p_oType instanceof Class) {
			Class<MEntity> oEntityClass = (Class<MEntity>) p_oType ;
			
			AbstractEntityHelper oHelper = AbstractEntityHelper.getInstance();
			
			EntityFactory<MEntity> oEntityFactory = oHelper.getFactoryForEntity(oEntityClass);
			r_oResult = oEntityFactory.createInstance();
		}
		return r_oResult;
	}
}
