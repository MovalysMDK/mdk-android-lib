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
import java.util.Map;
import java.util.TreeMap;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.AbstractTEntityFieldConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.TBasicEntityFieldConfiguration;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * <p>TODO Décrire la classe EntityFieldConfigurationDeserializer</p>
 *
 *
 */
public class EntityFieldConfigurationDeserializer implements JsonDeserializer<AbstractTEntityFieldConfiguration> {

	Map<String, Class<? extends AbstractTEntityFieldConfiguration>> classByName;
	
	/**
	 * <p>Constructor for EntityFieldConfigurationDeserializer.</p>
	 */
	public EntityFieldConfigurationDeserializer() {
		this.classByName = new TreeMap<String, Class<? extends AbstractTEntityFieldConfiguration>>();

		this.classByName.put("com.adeuza.movalys.fwk.mobile.model.configuration.entity.BasicEntityFieldConfiguration", TBasicEntityFieldConfiguration.class);
	}
	/**
	 * {@inheritDoc}
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public AbstractTEntityFieldConfiguration deserialize(JsonElement p_oElement, Type p_oType,
			JsonDeserializationContext p_oContext) throws JsonParseException {

		AbstractTEntityFieldConfiguration r_oField = null;

		if (p_oElement.isJsonObject()) {
			JsonElement oJsonClass = ((JsonObject) p_oElement).get("@class");
			if (oJsonClass != null) {
				Class<? extends AbstractTEntityFieldConfiguration> oClass = this.classByName.get(oJsonClass.getAsString());
				if (oClass != null) {
					r_oField = p_oContext.deserialize(p_oElement, oClass);
				}
			}
		}

		return r_oField;
	}
}
