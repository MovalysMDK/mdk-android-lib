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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

/**
 * <p>Wraps a native android cursor into a ResultSet.</p>
 *
 *nb of methods: 80 (instead of 45)
 */
public abstract class AbstractResultSet extends AbstractWrapper implements ResultSet {

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	@Override
	public InputStream getAsciiStream(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getUnicodeStream(int)
	 */
	@Override
	public InputStream getUnicodeStream(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	@Override
	public InputStream getBinaryStream(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getCursorName()
	 */
	@Override
	public String getCursorName() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(int p_oDirection) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(int p_oRows) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getType()
	 */
	@Override
	public int getType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	@Override
	public int getConcurrency() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#rowUpdated()
	 */
	@Override
	public boolean rowUpdated() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#rowInserted()
	 */
	@Override
	public boolean rowInserted() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#rowDeleted()
	 */
	@Override
	public boolean rowDeleted() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateNull(int)
	 */
	@Override
	public void updateNull(int p_iColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBoolean(int, boolean)
	 */
	@Override
	public void updateBoolean(int p_iColumnIndex, boolean p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateByte(int, byte)
	 */
	@Override
	public void updateByte(int p_oColumnIndex, byte p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateShort(int, short)
	 */
	@Override
	public void updateShort(int p_oColumnIndex, short p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateInt(int, int)
	 */
	@Override
	public void updateInt(int p_oColumnIndex, int p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateLong(int, long)
	 */
	@Override
	public void updateLong(int p_oColumnIndex, long p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateFloat(int, float)
	 */
	@Override
	public void updateFloat(int p_oColumnIndex, float p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateDouble(int, double)
	 */
	@Override
	public void updateDouble(int p_oColumnIndex, double p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
	 */
	@Override
	public void updateBigDecimal(int p_oColumnIndex, BigDecimal p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateString(int, java.lang.String)
	 */
	@Override
	public void updateString(int p_oColumnIndex, String p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBytes(int, byte[])
	 */
	@Override
	public void updateBytes(int p_oColumnIndex, byte[] p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
	 */
	@Override
	public void updateDate(int p_oColumnIndex, Date p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
	 */
	@Override
	public void updateTime(int p_oColumnIndex, Time p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
	 */
	@Override
	public void updateTimestamp(int p_oColumnIndex, Timestamp p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
	 */
	@Override
	public void updateAsciiStream(int p_oColumnIndex, InputStream p_oX, int p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
	 */
	@Override
	public void updateBinaryStream(int p_oColumnIndex, InputStream p_oX, int p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
	 */
	@Override
	public void updateCharacterStream(int p_oColumnIndex, Reader p_oX, int p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
	 */
	@Override
	public void updateObject(int p_oColumnIndex, Object p_oX, int p_oScaleOrLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
	 */
	@Override
	public void updateObject(int p_oColumnIndex, Object p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	@Override
	public Object getObject(int p_oColumnIndex, Map<String, Class<?>> p_oMap) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getRef(int)
	 */
	@Override
	public Ref getRef(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	@Override
	public Blob getBlob(int p_iColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getClob(int)
	 */
	@Override
	public Clob getClob(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getArray(int)
	 */
	@Override
	public Array getArray(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	@Override
	public void updateRef(int p_oColumnIndex, Ref p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	@Override
	public void updateBlob(int p_oColumnIndex, Blob p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	@Override
	public void updateClob(int p_oColumnIndex, Clob p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	@Override
	public void updateArray(int p_oColumnIndex, Array p_oX) throws SQLException {
		throw new UnsupportedOperationException();
		
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getRowId(int)
	 */
	@Override
	public RowId getRowId(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateRowId(int, java.sql.RowId)
	 */
	@Override
	public void updateRowId(int p_oColumnIndex, RowId p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}


	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateNString(int, java.lang.String)
	 */
	@Override
	public void updateNString(int p_oColumnIndex, String p_oNString) throws SQLException {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getNClob(int)
	 */
	@Override
	public NClob getNClob(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getSQLXML(int)
	 */
	@Override
	public SQLXML getSQLXML(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateSQLXML(int, java.sql.SQLXML)
	 */
	@Override
	public void updateSQLXML(int p_oColumnIndex, SQLXML p_oXmlObject) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getNString(int)
	 */
	@Override
	public String getNString(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}


	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getNCharacterStream(int)
	 */
	@Override
	public Reader getNCharacterStream(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void updateNCharacterStream(int p_oColumnIndex, Reader p_oX, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, long)
	 */
	@Override
	public void updateAsciiStream(int p_oColumnIndex, InputStream p_oX, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, long)
	 */
	@Override
	public void updateBinaryStream(int p_oColumnIndex, InputStream p_oX, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void updateCharacterStream(int p_oColumnIndex, Reader p_oX, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream, long)
	 */
	@Override
	public void updateBlob(int p_oColumnIndex, InputStream p_oInputStream, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}


	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader, long)
	 */
	@Override
	public void updateClob(int p_oColumnIndex, Reader p_oReader, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader, long)
	 */
	@Override
	public void updateNClob(int p_oColumnIndex, Reader p_oReader, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void updateNCharacterStream(int p_oColumnIndex, Reader p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream)
	 */
	@Override
	public void updateAsciiStream(int p_oColumnIndex, InputStream p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream)
	 */
	@Override
	public void updateBinaryStream(int p_oColumnIndex, InputStream p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void updateCharacterStream(int p_oColumnIndex, Reader p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream)
	 */
	@Override
	public void updateBlob(int p_oColumnIndex, InputStream p_oInputStream) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader)
	 */
	@Override
	public void updateClob(int p_oColumnIndex, Reader p_oReader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader)
	 */
	@Override
	public void updateNClob(int p_oColumnIndex, Reader p_oReader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getObject(int)
	 */
	@Override
	public Object getObject(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	@Override
	public Reader getCharacterStream(int p_oColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#insertRow()
	 */
	@Override
	public void insertRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateRow()
	 */
	@Override
	public void updateRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#deleteRow()
	 */
	@Override
	public void deleteRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#refreshRow()
	 */
	@Override
	public void refreshRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#cancelRowUpdates()
	 */
	@Override
	public void cancelRowUpdates() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#moveToInsertRow()
	 */
	@Override
	public void moveToInsertRow() throws SQLException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSet#updateNClob(int, java.sql.NClob)
	 */
	@Override
	public void updateNClob(int p_oColumnIndex, NClob p_oNClob) throws SQLException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(int p_iColumnIndex, int p_iScale)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBytes(int p_iColumnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T getObject(int p_iColumnIndex, Class<T> p_oType) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T getObject(String p_sColumnLabel, Class<T> p_oType)
			throws SQLException {
		throw new UnsupportedOperationException();
	}
}
