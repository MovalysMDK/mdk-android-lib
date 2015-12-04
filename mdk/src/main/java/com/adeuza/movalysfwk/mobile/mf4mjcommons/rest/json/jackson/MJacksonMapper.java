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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Initializable;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson mapper: implementation of object-json mapper using jackson library
 */
@Scope(ScopePolicy.SINGLETON)
public class MJacksonMapper implements MJsonMapper, Initializable {

	private JsonFactory jsonFactory;
		
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mf4jcommons.core.beans.Initializable#initialize()
	 */
	@Override
	public void initialize() {
		initialize(false);
	}
	
	/**
	 * Initialize the GsonBuilder and Gson object.
	 *
	 * @param p_bPretty Configures Gson to output Json that fits in a page for pretty printing.
	 */
	protected void initialize( boolean p_bPretty ) {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("mdk", Version.unknownVersion());
		initializeModule(module);
		mapper.registerModule(module);
		this.jsonFactory = new MappingJsonFactory(mapper);
	}
	
	/**
	 * Initialize
	 * @param p_oModule the module to use
	 */
	protected void initializeModule(SimpleModule p_oModule) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper#fromJson(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T fromJson(String p_sContent, Class<T> p_oRootClass) throws RestException {
		T r_oResult = null;
		try {
			JsonParser oParser = jsonFactory.createParser(p_sContent);
			r_oResult = oParser.readValueAs(p_oRootClass);
		} catch ( IOException oException) {
			throw new RestException("MJacksonMapper.fromJson error", oException);
		}
		return r_oResult;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper#fromJson(java.io.Reader, java.lang.Class)
	 */
	@Override
	public <T> T fromJson(Reader p_oReader, Class<T> p_oRootClass) throws RestException {
		T r_oResult = null;
		try {
			JsonParser oParser = jsonFactory.createParser(p_oReader);
			r_oResult = oParser.readValueAs(p_oRootClass);
		} catch ( IOException oException) {
			throw new RestException("MJacksonMapper.fromJson error", oException);
		}
		return r_oResult;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper#fromJson(java.io.InputStream, java.lang.Class)
	 */
	@Override
	public <T> T fromJson(InputStream p_oIs, Class<T> p_oRootClass) throws RestException {
		T r_oResult = null;
		try {
			JsonParser oParser = jsonFactory.createParser(p_oIs);
			r_oResult = oParser.readValueAs(p_oRootClass);
		} catch (IOException oException) {
			throw new RestException("MJacksonMapper.fromJson error", oException);
		}
		return r_oResult;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper#toJson(java.lang.Object)
	 */
	@Override
	public String toJson(Object p_oObject) throws RestException {
		StringWriter oWriter = new StringWriter();
		toJson(p_oObject, oWriter);
		return oWriter.toString();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper#toJson(java.lang.Object, java.io.Writer)
	 */
	@Override
	public void toJson(Object p_oObject, Writer p_oWriter) throws RestException {
		try {
			JsonGenerator jsonGenerator = jsonFactory.createGenerator(p_oWriter);
			jsonGenerator.writeObject(p_oObject);
		} catch (IOException oException) {
			throw new RestException("MJacksonMapper.toJson error", oException);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper#getSerializedName(java.lang.reflect.Field)
	 */
	@Override
	public String getSerializedName(Field p_oField) {
		JsonProperty oSerializedName = p_oField.getAnnotation(JsonProperty.class);
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
