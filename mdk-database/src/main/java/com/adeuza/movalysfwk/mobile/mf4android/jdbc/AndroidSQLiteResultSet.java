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
package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import java.sql.Date;
import java.sql.Timestamp;

import android.database.Cursor;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;

/**
 * <p>Wraps a native android cursor into a ResultSet.</p>
 */
public class AndroidSQLiteResultSet  {

	/**
	 * Native android cursor
	 */
	private Cursor androidCursor;

	/**
	 * last Read column
	 */
	private int lastReadColumn;

	/**
	 * Creates a ResultSet from a native android cursor.
	 * @param p_oStatement statement
	 * @param p_oAndroidCursor Native android cursor.
	 */
	public AndroidSQLiteResultSet(AndroidSQLiteStatement p_oStatement, Cursor p_oAndroidCursor) {
		this.androidCursor	= p_oAndroidCursor;
		this.lastReadColumn	= 0;
	}
	
	//FIXME: javadoc
	public boolean isClosed() throws DaoException {
		return this.androidCursor.isClosed();
	}
	
	//FIXME: javadoc
	public void close() throws DaoException {
		this.androidCursor.close();
	}

	//FIXME: javadoc
	public boolean wasNull() throws DaoException {
		return this.androidCursor.isNull(this.lastReadColumn);
	}
	
	//FIXME: javadoc
	public boolean next() throws DaoException {
		return this.androidCursor.moveToNext();
	}
	
	//FIXME: javadoc
	public int getInt(int p_iColumnIndex) throws DaoException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getInt(this.lastReadColumn);
	}
	
	//FIXME: javadoc
	public long getLong(int p_iColumnIndex) throws DaoException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getLong(this.lastReadColumn);
	}
	
	//FIXME: javadoc
	public String getString(int p_iColumnIndex) throws DaoException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getString(this.lastReadColumn);
	}
	
	//FIXME: javadoc
	public boolean getBoolean(int p_iColumnIndex) throws DaoException {
		return this.getInt(p_iColumnIndex) == 1;
	}
	
	//FIXME: javadoc
	public short getShort(int p_iColumnIndex) throws DaoException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getShort(this.lastReadColumn);
	}
	
	//FIXME: javadoc
	public byte getByte(int p_iColumnIndex) throws DaoException {
		return (byte) this.getShort(p_iColumnIndex);
	}
	
	//FIXME: javadoc
	public double getDouble(int p_iColumnIndex) throws DaoException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getDouble(this.lastReadColumn);
	}
	
	//FIXME: javadoc
	public float getFloat(int p_iColumnIndex) throws DaoException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getFloat(this.lastReadColumn);
	}
	
	//FIXME: javadoc
	public Date getDate(int p_iColumnIndex) throws DaoException {
		return new Date(this.getLong(p_iColumnIndex));
	}
	
	//FIXME: javadoc
	public Timestamp getTimestamp(int p_iColumnIndex) throws DaoException {
		Timestamp r_oTimestamp = null;

		long lTimeInMillis = this.getLong(p_iColumnIndex);
		if (!this.wasNull()) {
			r_oTimestamp = new Timestamp(lTimeInMillis);
		}
		return r_oTimestamp;
	}
}
