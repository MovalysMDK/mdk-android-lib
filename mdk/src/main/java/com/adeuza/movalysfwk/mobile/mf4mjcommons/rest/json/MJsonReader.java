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

import java.io.Reader;
import java.util.List;

/**
 * Json reader (used for streaming)
 */
public interface MJsonReader {

	/**
	 * Read the json using a Reader
	 *
	 * @param p_oReader reader
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public void read(Reader p_oReader) throws JsonException;
	
	/**
	 * Terminates read
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public void close() throws JsonException ;

	/**
	 * Read the start marker of a json object
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public void beginObject() throws JsonException;

	/**
	 * Read the stop marker of a json object
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public void endObject()  throws JsonException;
	
	/**
	 * Read the start marker of a json array
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public void beginArray() throws JsonException;

	/**
	 * Read the end marker of a json array
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public void endArray()  throws JsonException;
	
	/**
	 * Return true if the value is null
	 *
	 * @return true if the value is null
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public boolean isNullValue() throws JsonException;
	
	/**
	 * Skip the value of a json property
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public void skipValue() throws JsonException;
	
	/**
	 * Return if the current element is an end array
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 * @return a boolean.
	 */
	public boolean isEndArray() throws JsonException;
	
	/**
	 * Read the property name
	 *
	 * @return property name
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public String readPropertyName() throws JsonException;
	
	/**
	 * Return true if current element is neither an end array nor a end object
	 *
	 * @return true if current element is neither an end array nor a end object
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public boolean notEndOfArrayOrObject() throws JsonException;
	
	/**
	 * Read a long value
	 *
	 * @return long value
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public long readLong() throws JsonException;

	/**
	 * Read a double value
	 *
	 * @return double value
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public double readDouble() throws JsonException;

	/**
	 * Read a string value
	 *
	 * @return string value
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public String readString() throws JsonException;

	/**
	 * Read a boolean value
	 *
	 * @return boolean value
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public boolean readBoolean() throws JsonException;

	/**
	 * Read an int value
	 *
	 * @return int value
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public int readInt() throws JsonException;
	
	/**
	 * Read an array of booleans
	 *
	 * @return list of booleans
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public List<Boolean> readListOfBoolean() throws JsonException;
	
	/**
	 * Read an array of double
	 *
	 * @return list of double
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public List<Double> readListOfDouble() throws JsonException;
	
	/**
	 * Read an array of integers
	 *
	 * @return list of integers
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public List<Integer> readListOfInteger() throws JsonException;
	
	/**
	 * Read an array of long
	 *
	 * @return list of long
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public List<Long> readListOfLong() throws JsonException;
	
	/**
	 * Read an list of string
	 *
	 * @return list of string
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	public List<String> readListOfString() throws JsonException;
}
