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
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * Gson reader
 *
 */
public class MGsonReader implements MJsonReader {

	/**
	 * JsonReader
	 */
	private JsonReader jsonReader ;
	
	/** {@inheritDoc} */
	@Override
	public void read(Reader p_oReader) throws JsonException {
		this.jsonReader = new JsonReader(p_oReader);
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws JsonException {
		try {
			jsonReader.close();
		} catch (IOException oIOException ) {
			throw new JsonException(oIOException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void skipValue() throws JsonException {
		try {
			this.jsonReader.skipValue();
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void beginObject() throws JsonException {
		try {
			this.jsonReader.beginObject();
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endObject() throws JsonException {
		try {
			this.jsonReader.endObject();
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void beginArray() throws JsonException {
		try {
			this.jsonReader.beginArray();
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void endArray() throws JsonException {
		try {
			this.jsonReader.endArray();
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean notEndOfArrayOrObject() throws JsonException {
		boolean r_bNextToken = false ;
		try {
			r_bNextToken = this.jsonReader.hasNext();
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
		return r_bNextToken;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEndArray() throws JsonException {
		boolean r_bIsEndArray = false ;
		try {
			r_bIsEndArray = this.jsonReader.peek().equals(JsonToken.END_ARRAY);
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
		return r_bIsEndArray;	
	}

	/** {@inheritDoc} */
	@Override
	public String readPropertyName() throws JsonException {
		String r_sName = null;
		try {
			r_sName = this.jsonReader.nextName();
		} catch (IOException oIOException) {
			throw new JsonException(oIOException);
		}
		return r_sName;
	}
	
	/** {@inheritDoc} */
	@Override
	public String readString() throws JsonException{
		String r_sValue = null;
		try {
			if ( this.jsonReader.peek() != JsonToken.NULL ) {
				r_sValue = this.jsonReader.nextString();
			}
			else {
				this.jsonReader.nextNull();
			}
		} catch (IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_sValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public int readInt() throws JsonException{
		int r_iValue = 0;
		try {
			if ( this.jsonReader.peek() != JsonToken.NULL ) {
				r_iValue = this.jsonReader.nextInt();
			}
			else {
				this.jsonReader.nextNull();
			}
		} catch (IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_iValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean readBoolean() throws JsonException{
		boolean r_bValue = false;
		try {
			if ( this.jsonReader.peek() != JsonToken.NULL ) {
				r_bValue = this.jsonReader.nextBoolean();
			}
			else {
				this.jsonReader.nextNull();
			}
		} catch (IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_bValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public long readLong() throws JsonException {
		long r_lValue = 0;
		try {
			if ( this.jsonReader.peek() != JsonToken.NULL ) {
				r_lValue = this.jsonReader.nextLong();
			}
			else {
				this.jsonReader.nextNull();
			}
		} catch (IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_lValue;
	}

	/** {@inheritDoc} */
	@Override
	public double readDouble() throws JsonException {
		double r_dValue = 0;
		try {
			if ( this.jsonReader.peek() != JsonToken.NULL ) {
				r_dValue = this.jsonReader.nextDouble();
			}
			else {
				this.jsonReader.nextNull();
			}
		} catch (IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_dValue;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isNullValue() throws JsonException {
		boolean r_bIsNull = false;
		try {
			r_bIsNull = this.jsonReader.peek() == JsonToken.NULL;
		} catch (IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_bIsNull;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Boolean> readListOfBoolean() throws JsonException {
		List<Boolean> r_listValues = new ArrayList<>();
		try {
			while( this.jsonReader.peek() != JsonToken.END_ARRAY) {
				r_listValues.add(this.jsonReader.nextBoolean());
			}
		} catch( IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_listValues;
	}

	/** {@inheritDoc} */
	@Override
	public List<Double> readListOfDouble() throws JsonException {
		List<Double> r_listValues = new ArrayList<>();
		try {
			while( this.jsonReader.peek() != JsonToken.END_ARRAY) {
				r_listValues.add(this.jsonReader.nextDouble());
			}
		} catch( IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_listValues;
	}

	/** {@inheritDoc} */
	@Override
	public List<Integer> readListOfInteger() throws JsonException {
		List<Integer> r_listValues = new ArrayList<>();
		try {
			while( this.jsonReader.peek() != JsonToken.END_ARRAY) {
				r_listValues.add(this.jsonReader.nextInt());
			}
		} catch( IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_listValues;
	}

	/** {@inheritDoc} */
	@Override
	public List<Long> readListOfLong() throws JsonException {
		List<Long> r_listValues = new ArrayList<>();
		try {
			while( this.jsonReader.peek() != JsonToken.END_ARRAY) {
				r_listValues.add(this.jsonReader.nextLong());
			}
		} catch( IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_listValues;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> readListOfString() throws JsonException {
		List<String> r_listValues = new ArrayList<String>();
		try {
			while( this.jsonReader.peek() != JsonToken.END_ARRAY) {
				r_listValues.add(this.jsonReader.nextString());
			}
		} catch( IOException oIOException ) {
			throw new JsonException(oIOException);
		}
		return r_listValues;
	}
}
