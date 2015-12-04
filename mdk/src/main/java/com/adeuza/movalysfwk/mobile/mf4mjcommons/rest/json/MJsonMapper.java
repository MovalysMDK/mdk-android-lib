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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;

/**
 * MJsonMapper: mapper between json and objects
 */
public interface MJsonMapper {
	
	/**
	 * Convert json string to object
	 *
	 * @param p_sContent json string
	 * @param p_oRootClass object class
	 * @return object valued with json
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException exception
	 * @param <T> a T object.
	 */
	public <T> T fromJson( String p_sContent, Class<T> p_oRootClass ) throws RestException;
	
	/**
	 * Convert json inputstream to object
	 *
	 * @param p_oIs inputstream
	 * @param p_oRootClass object class
	 * @return object valued with json
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException exception
	 * @param <T> a T object.
	 */
	public <T> T fromJson(InputStream p_oIs, Class<T> p_oRootClass) throws RestException;
	
	/**
	 * Convert json reader to object
	 *
	 * @param p_oReader reader
	 * @param p_oRootClass object class
	 * @return object valued with json
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 * @param <T> a T object.
	 */
	public <T> T fromJson(Reader p_oReader, Class<T> p_oRootClass) throws RestException;
	
	/**
	 * Convert object to json
	 *
	 * @param p_oObject object to convert
	 * @return json representation
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException exception
	 */
	public String toJson( Object p_oObject ) throws RestException;

	/**
	 * Convert object to json and write the json to the writer.
	 *
	 * @param p_oObject object to convert
	 * @param p_oWriter writer
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException exception
	 * @since 3.1.1
	 */
	public void toJson( Object p_oObject, Writer p_oWriter ) throws RestException;
	
	/**
	 * Return the json property name of a class field
	 *
	 * @param p_oField class field
	 * @return json property name
	 * @since 3.1.1
	 */
	public String getSerializedName(Field p_oField);
}
