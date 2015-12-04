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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * <p>EnumSerializer class.</p>
 *
 */
public class EnumSerializer implements JsonDeserializer<Enum>, JsonSerializer<Enum>{

	private Enum mEnum ;
	
	/**
	 * <p>Constructor for EnumSerializer.</p>
	 *
	 * @param p_oEnum a {@link java.lang.Class} object.
	 */
	public EnumSerializer( Class<? extends Enum> p_oEnum ) {
		this.mEnum = (Enum) p_oEnum.getEnumConstants()[0];
	}
	
	/** {@inheritDoc} */
	@Override
	public JsonElement serialize(Enum p_oEnum, Type p_oEnumype, JsonSerializationContext context) {
		return new JsonPrimitive(p_oEnum.getBaseId());
	}

	/** {@inheritDoc} */
	@Override
	public Enum deserialize(JsonElement p_oJsonElement, Type p_oEnumype,
			JsonDeserializationContext p_oJsonDeserializationContext)
			throws JsonParseException {
		return (Enum) mEnum.fromBaseId(p_oJsonElement.getAsInt());
	}
}
