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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Initializable;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.AppConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.AbstractTEntityFieldConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.visual.TGraphConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.AbstractEntityHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhotoState;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Create the mapping based on Gson librairy.
 *
 */
@Scope(ScopePolicy.SINGLETON)
public class MGsonMapper implements MJsonMapper, Initializable {
	
	/**
	 * <p>
	 *  Gson is a Java library that can be used to convert Java Objects into their JSON representation. 
	 *  It can also be used to convert a JSON string to an equivalent Java object. 
	 *  Gson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of.
	 * </p>
	 */
	private Gson gson ;
	
	/** {@inheritDoc} */
	@Override
	public void initialize() {
		initialize(false);
	}
	
	/**
	 * Initialize the GsonBuilder and Gson object.
	 *
	 * @param p_bPretty Configures Gson to output Json that fits in a page for pretty printing.
	 */
	public final void initialize( boolean p_bPretty ) {
		
		//oGsonBuilder.serializeNulls();
		this.gson = initGsonBuilder(p_bPretty).create();
	}
	
	/**
	 * TODO Décrire la méthode initGsonBuilder de la classe MGsonMapper
	 *
	 * @param p_bPretty a boolean.
	 * @return a {@link com.google.gson.GsonBuilder} object.
	 */
	protected GsonBuilder initGsonBuilder( boolean p_bPretty ) {
		
		GsonBuilder r_oBuilder = new GsonBuilder();
		JsonEntityInstanceCreator oJsonEntityInstanceCreator = new JsonEntityInstanceCreator();
		for( Class<MEntity> oEntityClass : AbstractEntityHelper.getInstance().getEntityClasses()) {
			r_oBuilder.registerTypeAdapter(oEntityClass, oJsonEntityInstanceCreator);	
		}
		r_oBuilder.registerTypeAdapter(MPhotoState.class, new EnumSerializer( MPhotoState.class));

		r_oBuilder.registerTypeAdapter(List.class, new ListSerializer());
		r_oBuilder.registerTypeAdapter(Timestamp.class, new TimestampSerializer());
		r_oBuilder.registerTypeAdapter(AppConfiguration.class, new AppConfigurationDeserializer());
		r_oBuilder.registerTypeAdapter(AbstractTEntityFieldConfiguration.class, new EntityFieldConfigurationDeserializer());
		r_oBuilder.registerTypeAdapter(TGraphConfiguration.class, new GraphConfigurationDeserializer());

		r_oBuilder.addSerializationExclusionStrategy(new MGsonExclusionStrategy());
		r_oBuilder.addDeserializationExclusionStrategy(new MGsonExclusionStrategy());
		
		if ( p_bPretty ) {
			r_oBuilder.setPrettyPrinting();
		}
		
		return r_oBuilder ;
	}
	
	/** {@inheritDoc} */
	@Override
	public <T> T fromJson(String p_sContent, Class<T> p_oRootClass) {
		return gson.fromJson(p_sContent, p_oRootClass);
	}
	
	/** {@inheritDoc} */
	@Override
	public <T> T fromJson(Reader p_oReader, Class<T> p_oRootClass) {
		return gson.fromJson(p_oReader, p_oRootClass);
	}

	/** {@inheritDoc} */
	@Override
	public <T> T fromJson(InputStream p_oIs, Class<T> p_oRootClass) throws RestException {
		T r_oResult = null ;
		InputStreamReader oReader = new InputStreamReader(p_oIs);
		try {
			r_oResult = this.fromJson(oReader, p_oRootClass);
		} finally {
			try {
				oReader.close();
			} catch (IOException oIOException) {
				throw new RestException("MGsonMapper.fromJson error", oIOException);
			}
		}
		return r_oResult ;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toJson(Object p_oObject) throws RestException {
		return this.gson.toJson(p_oObject);
	}
	
	/** {@inheritDoc} */
	@Override
	public void toJson(Object p_oObject, Writer p_oWriter) throws RestException {
		JsonWriter oWriter = new JsonWriter(p_oWriter);
		try {
			oWriter.setIndent("");
	        this.gson.toJson(p_oObject, p_oObject.getClass(), oWriter);
	        oWriter.flush();
		} catch( IOException oIOException) {
			throw new RestException("MGsonMapper.toJson error", oIOException);
		} finally {
			try {
				oWriter.close();
			} catch (IOException oIOException) {
				throw new RestException("MGsonMapper.toJson error", oIOException);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * TODO Décrire la méthode fromJson de la classe MGsonMapper
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromJson( JsonReader p_oJsonReader, Class<T> p_oType) {
		return (T) this.gson.fromJson(p_oJsonReader, p_oType);
	}
	
	
	/**
	 * TODO Décrire la méthode fromJson de la classe MGsonMapper
	 *
	 * @param p_oJsonReader a {@link com.google.gson.stream.JsonReader} object.
	 * @param p_oType a {@link java.lang.reflect.Type} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public Object fromJson( JsonReader p_oJsonReader, Type p_oType) {
		return this.gson.fromJson(p_oJsonReader, p_oType);
	}

	/** {@inheritDoc} */
	@Override
	public String getSerializedName(Field p_oField) {
		SerializedName oSerializedName = p_oField.getAnnotation(SerializedName.class);
		String sSerializationName = null ;
		if ( oSerializedName != null ) {
			sSerializationName = oSerializedName.value();
		}
		else {
			sSerializationName = p_oField.getName();
		}
		return sSerializationName;
	}
}
