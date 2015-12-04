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
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonReader;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Jackson parser
 *
 */
public class MJacksonReader implements MJsonReader {

	/**
	 * Jackson parser
	 */
	private JsonParser parser ; 
	
	/** {@inheritDoc} */
	@Override
	public void read(Reader p_oReader) throws JsonException {
		JsonFactory oJacksonFactory = new JsonFactory();
		try {
			this.parser = oJacksonFactory.createParser(p_oReader);
			this.parser.nextToken();
		} catch ( IOException oIOException ) {
			throw new JsonException("MJacksonReader: failed to create parser from Reader", oIOException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void close() {
		parser.clearCurrentToken();
	}
	
	/** {@inheritDoc} */
	@Override
	public void beginObject() throws JsonException {
		try {
			if ( !parser.getCurrentToken().equals(JsonToken.START_OBJECT)) {
				throw new JsonException("Json error: current node is not a start object");
			}
			parser.nextToken();
		} catch (IOException oException) {
			throw new JsonException("Json error: current node is not a start object", oException);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endObject() throws JsonException {
		try {
			if ( !parser.getCurrentToken().equals(JsonToken.END_OBJECT)) {
				throw new JsonException("Json error: current node is not an end object");
			}
			parser.nextToken();
		} catch (IOException oException) {
			throw new JsonException("Json error: current node is not an end array", oException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void beginArray() throws JsonException {
		try {
			if ( !parser.getCurrentToken().equals(JsonToken.START_ARRAY)) {
				throw new JsonException("Json error: current node is not a start array");
			}
			parser.nextToken();
		} catch (IOException oException) {
			throw new JsonException("Json error: current node is not a start array", oException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void endArray() throws JsonException {
		try {
			if ( !parser.getCurrentToken().equals(JsonToken.END_ARRAY)) {
				throw new JsonException("Json error: current node is not an end array");
			}
			parser.nextToken();
		} catch (IOException oException) {
			throw new JsonException("Json error: current node is not an end array", oException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public String readPropertyName() throws JsonException {
		String r_sCurrentName = null;
		try {
			r_sCurrentName = parser.getCurrentName();
			parser.nextToken();
		} catch (IOException oException) {
			throw new JsonException("Json error: failure to read next token", oException);
		}
		return r_sCurrentName;
	}
	
	/** {@inheritDoc} */
	@Override
	public String readString() throws JsonException {
		String r_sValue = null;
		try {
			if ( parser.getCurrentToken() != JsonToken.VALUE_NULL) {
				r_sValue = parser.getText();
			}
			parser.nextToken();
		} catch (IOException oIOException) {
			throw new JsonException("Json error: failure to read next token", oIOException);
		}
		return r_sValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public int readInt() throws JsonException {
		int r_iValue = 0;
		try {
			r_iValue = parser.getIntValue();
			parser.nextToken();
		} catch (IOException oIOException) {
			throw new JsonException("Json error: failure to read int value", oIOException);
		}
		return r_iValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean readBoolean() throws JsonException {
		boolean r_bValue = false;
		try {
			r_bValue = parser.getBooleanValue();
			parser.nextToken();
		} catch (IOException oIOException) {
			throw new JsonException("Json error: failure to read boolean value", oIOException);
		}
		return r_bValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public long readLong() throws JsonException {
		long r_lValue = 0;
		try {
			r_lValue = parser.getLongValue();
			parser.nextToken();
		} catch (IOException oIOException) {
			throw new JsonException("Json error: failure to read long value", oIOException);
		}
		return r_lValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public double readDouble() throws JsonException {
		double r_dValue = 0;
		try {
			r_dValue = parser.getDoubleValue();
			parser.nextToken();
		} catch (IOException oIOException) {
			throw new JsonException("Json error: failure to read double value", oIOException);
		}
		return r_dValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public void skipValue() throws JsonException {
		try {
			if (this.parser.getCurrentToken().equals(JsonToken.START_ARRAY)) {
				parser.skipChildren();
				parser.nextToken();
			}
			else if (this.parser.getCurrentToken().equals(JsonToken.START_OBJECT)) {
				parser.skipChildren();
				parser.nextToken();
			}
			else {
				parser.nextToken();
			}
			
		} catch (IOException oException) {
			throw new JsonException("Json error: can't skip value", oException);
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isNullValue() throws JsonException {
		JsonToken oToken = this.parser.getCurrentToken();
		return oToken.equals(JsonToken.VALUE_NULL);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isEndArray() throws JsonException {
		boolean r_bIsEndArray = false;
		r_bIsEndArray = parser.getCurrentToken().equals(JsonToken.END_ARRAY);
		return r_bIsEndArray;
	}

	/** {@inheritDoc} */
	@Override
	public boolean notEndOfArrayOrObject() throws JsonException {
		boolean r_bNextToken = !parser.getCurrentToken().equals(JsonToken.END_ARRAY) &&
				!parser.getCurrentToken().equals(JsonToken.END_OBJECT);
		return r_bNextToken;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Boolean> readListOfBoolean() throws JsonException {
		List<Boolean> r_listValues = new ArrayList<>();
		while( !parser.getCurrentToken().equals(JsonToken.END_ARRAY)) {
			Boolean bValue = this.readBoolean();
			r_listValues.add(bValue);
		}
		return r_listValues;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Double> readListOfDouble() throws JsonException {
		List<Double> r_listValues = new ArrayList<>();
		while( !parser.getCurrentToken().equals(JsonToken.END_ARRAY)) {
			r_listValues.add(Double.parseDouble(readString()));
		}
		return r_listValues;
	}

	/** {@inheritDoc} */
	@Override
	public List<Integer> readListOfInteger() throws JsonException {
		List<Integer> r_listValues = new ArrayList<>();
		while( !parser.getCurrentToken().equals(JsonToken.END_ARRAY)) {
			r_listValues.add(readInt());
		}
		return r_listValues;
	}

	/** {@inheritDoc} */
	@Override
	public List<Long> readListOfLong() throws JsonException {
		List<Long> r_listValues = new ArrayList<>();
		while( !parser.getCurrentToken().equals(JsonToken.END_ARRAY)) {
			r_listValues.add(readLong());
		}
		return r_listValues;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> readListOfString() throws JsonException {
		List<String> r_listValues = new ArrayList<>();
		try {
			while( !parser.getCurrentToken().equals(JsonToken.END_ARRAY)) {
				r_listValues.add(this.parser.nextTextValue());
			}
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
		return r_listValues;
	}


}
