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
import java.sql.Timestamp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * <p>TimestampSerializer class.</p>
 *
 */
public class TimestampSerializer implements JsonDeserializer<Timestamp>, JsonSerializer<Timestamp> {

	/** {@inheritDoc} */
	@Override
	public Timestamp deserialize(JsonElement p_oJsonElement, Type p_oType,
			JsonDeserializationContext p_oJsonDeserializationContext) throws JsonParseException {
		return new Timestamp(p_oJsonElement.getAsLong());
	}
	
	/** {@inheritDoc} */
	@Override
	public JsonElement serialize(Timestamp p_oTimestamp, Type p_oType, JsonSerializationContext context) {
	    return new JsonPrimitive(p_oTimestamp.getTime());
	  }
}
