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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.AbstractEntityHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * <p>ListSerializer class.</p>
 *
 */
public class ListSerializer implements JsonSerializer<List<?>>, JsonDeserializer<List<?>> {

	/**
	 * Map des interfaces vers les implémentations
	 */
	private Map<Class<?>, Class<?>> mapEntityToImpl = new HashMap<Class<?>, Class<?>>();

	/**
	 * Constructor
	 */
	public ListSerializer() {
		
		AbstractEntityHelper oHelper = AbstractEntityHelper.getInstance();
				
		for (Class<MEntity> oEntityClass : oHelper.getEntityClasses()) {
			Class<?> oImplClass = oHelper.getFactoryForEntity(oEntityClass).createInstance().getClass();
			this.mapEntityToImpl.put(oEntityClass, oImplClass);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(List<?> p_list, Type p_oType, JsonSerializationContext p_oContext) {
		return p_oContext.serialize(p_list, ArrayList.class);
	}

	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public List<?> deserialize(JsonElement p_oElement, Type p_oType, JsonDeserializationContext p_oContext)
			throws JsonParseException {

		ParameterizedType oParameterizedType = (ParameterizedType) p_oType;
		Class<?> oGenericType = (Class<?>) oParameterizedType.getActualTypeArguments()[0];

		List r_list = createList(oGenericType);

		JsonArray oArray = p_oElement.getAsJsonArray();
		Iterator<JsonElement> iterElement = oArray.iterator();

		Class<?> oElemType = mapEntityToImpl.get(oGenericType);
		if (oElemType == null) {
			oElemType = oGenericType;
		}
		while (iterElement.hasNext()) {
			r_list.add(p_oContext.deserialize(iterElement.next(), oElemType));
		}

		return r_list;
	}

	/**
	 * <p>createList.</p>
	 *
	 * @param p_oClass a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a {@link java.util.List} object.
	 */
	public <T> List<T> createList(Class<T> p_oClass) {
		return new ArrayList<T>();
	}
}
