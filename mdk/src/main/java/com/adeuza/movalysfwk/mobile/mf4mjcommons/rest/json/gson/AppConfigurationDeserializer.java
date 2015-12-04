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
import java.util.Collection;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.AppConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.AbstractTEntityFieldConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.TEntityConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.list.TListDescriptor;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.property.TProperty;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.visual.TGraphConfiguration;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * <p>TODO Décrire la classe AppConfigurationDeserializer</p>
 */
public class AppConfigurationDeserializer implements JsonDeserializer<AppConfiguration> {

	/**
	 * {@inheritDoc}
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public AppConfiguration deserialize(JsonElement p_oJsonElement, Type p_oType, JsonDeserializationContext p_oContext) {

		AppConfiguration r_oConfiguration = null;

		SynchronizedConfiguration oTmpConfiguration = (SynchronizedConfiguration) p_oContext.deserialize(p_oJsonElement, SynchronizedConfiguration.class);
		if (oTmpConfiguration != null) {
			r_oConfiguration = new AppConfiguration();
			r_oConfiguration.addProperties(oTmpConfiguration.properties);

			if (oTmpConfiguration.entities != null) {
				TEntityConfiguration oEntityConfiguration = null;
				for (SynchronizedEntityConfiguration oTmpEntity : oTmpConfiguration.entities) {
					oEntityConfiguration = new TEntityConfiguration(oTmpEntity.name);
					r_oConfiguration.addEntity(oEntityConfiguration);
					for (AbstractTEntityFieldConfiguration oTmpField : oTmpEntity.fields) {
						oEntityConfiguration.addFieldConfiguration(oTmpField);
					}
				}
			}

			r_oConfiguration.addGraphComponents(oTmpConfiguration.graphComponents);

			if (oTmpConfiguration.lists != null) {
				for (TListDescriptor oDescriptor : oTmpConfiguration.lists) {
					r_oConfiguration.addList(oDescriptor);
				}
			}
		}

		return r_oConfiguration;
	}

	/**
	 * Private inner class
	 * Store synchronization configuration
	 *
	 */
	private class SynchronizedConfiguration {
		/**
		 * Propriétés
		 */
		@SerializedName("prop")
		private Collection<TProperty> properties;

		/**
		 * Configuration of entities
		 */
		@SerializedName("ent")
		private Collection<SynchronizedEntityConfiguration> entities;

		/**
		 * Configuration graphique
		 */
		@SerializedName("graph")
		private Collection<TGraphConfiguration> graphComponents;

		/**
		 * Lists.
		 */
		@SerializedName("list")
		private Collection<TListDescriptor> lists;
	}

	/** Synchronisation entity configuration */
	private static class SynchronizedEntityConfiguration {
		/** name */
		private String name;
		/** fields */
		private Collection<AbstractTEntityFieldConfiguration> fields;
	}
}


