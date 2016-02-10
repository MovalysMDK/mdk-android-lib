package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import android.database.Cursor;

/**
 * <p>Wraps a native android cursor into a ResultSet.</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 *
 * *nb of methods: 109 (instead of 45)
 */
public class AndroidSQLiteResultSet extends AbstractResultSet {
	/**
	 * The statement that creates this ResultSet
	 */
	private Statement statement;

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
	public AndroidSQLiteResultSet(Statement p_oStatement, Cursor p_oAndroidCursor) {
		this.statement		= p_oStatement;
		this.androidCursor	= p_oAndroidCursor;
		this.lastReadColumn	= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean next() throws SQLException {
		return this.androidCursor.moveToNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws SQLException {
		this.androidCursor.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean wasNull() throws SQLException {
		return this.androidCursor.isNull(this.lastReadColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(int p_iColumnIndex) throws SQLException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getString(this.lastReadColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(int p_iColumnIndex) throws SQLException {
		return this.getInt(p_iColumnIndex) == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByte(int p_iColumnIndex) throws SQLException {
		return (byte) this.getShort(p_iColumnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShort(int p_iColumnIndex) throws SQLException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getShort(this.lastReadColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(int p_iColumnIndex) throws SQLException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getInt(this.lastReadColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(int p_iColumnIndex) throws SQLException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getLong(this.lastReadColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(int p_iColumnIndex) throws SQLException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getFloat(this.lastReadColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(int p_iColumnIndex) throws SQLException {
		this.lastReadColumn = p_iColumnIndex - 1;
		return this.androidCursor.getDouble(this.lastReadColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(int p_iColumnIndex) throws SQLException {
		return new Date(this.getLong(p_iColumnIndex));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(int p_iColumnIndex) throws SQLException {
		return new Time(this.getLong(p_iColumnIndex));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(int p_iColumnIndex) throws SQLException {
		Timestamp r_oTimestamp = null;

		long lTimeInMillis = this.getLong(p_iColumnIndex);
		if (!this.wasNull()) {
			r_oTimestamp = new Timestamp(lTimeInMillis);
		}
		return r_oTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(String p_sColumnLabel) throws SQLException {
		return this.getString(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(String p_sColumnLabel) throws SQLException {
		return this.getBoolean(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByte(String p_sColumnLabel) throws SQLException {
		return this.getByte(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShort(String p_sColumnLabel) throws SQLException {
		return this.getShort(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(String p_sColumnLabel) throws SQLException {
		return this.getInt(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(String p_sColumnLabel) throws SQLException {
		return this.getLong(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(String p_sColumnLabel) throws SQLException {
		return this.getFloat(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(String p_sColumnLabel) throws SQLException {
		return this.getDouble(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(String p_sColumnLabel, int p_iScale) throws SQLException {
		return this.getBigDecimal(this.findColumn(p_sColumnLabel), p_iScale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBytes(String p_sColumnLabel) throws SQLException {
		return this.getBytes(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(String p_sColumnLabel) throws SQLException {
		return this.getDate(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(String p_sColumnLabel) throws SQLException {
		return this.getTime(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(String p_sColumnLabel) throws SQLException {
		return this.getTimestamp(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getAsciiStream(String p_sColumnLabel) throws SQLException {
		return this.getAsciiStream(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getUnicodeStream(String p_sColumnLabel) throws SQLException {
		return this.getUnicodeStream(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getBinaryStream(String p_sColumnLabel) throws SQLException {
		return this.getBinaryStream(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return new AndroidSQLiteResultSetMetaData(this.androidCursor);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(String p_sColumnLabel) throws SQLException {
		return this.getObject(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int findColumn(String p_sColumnLabel) throws SQLException {
		return this.androidCursor.getColumnIndexOrThrow(p_sColumnLabel) + 1;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getCharacterStream(String p_sColumnLabel) throws SQLException {
		return this.getCharacterStream(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(String p_sColumnLabel) throws SQLException {
		return this.getBigDecimal(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBeforeFirst() throws SQLException {
		return this.androidCursor.isBeforeFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAfterLast() throws SQLException {
		return this.androidCursor.isAfterLast();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFirst() throws SQLException {
		return this.androidCursor.isFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLast() throws SQLException {
		return this.androidCursor.isLast();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeFirst() throws SQLException {
		this.androidCursor.moveToFirst();
		this.androidCursor.moveToPrevious();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterLast() throws SQLException {
		this.androidCursor.moveToLast();
		this.androidCursor.moveToNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean first() throws SQLException {
		return this.androidCursor.moveToFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean last() throws SQLException {
		return this.androidCursor.moveToLast();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRow() throws SQLException {
		// getPosition() renvoie un position à partir de 0, getRow à partir de 1.
		return this.androidCursor.getPosition() + 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean absolute(int p_iRow) throws SQLException {
		// Android : Row à partir de 0. Jdbc : Row à partir de 1
		return this.androidCursor.moveToPosition(p_iRow - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean relative(int p_iRow) throws SQLException {
		return this.androidCursor.moveToPosition(this.androidCursor.getPosition() + p_iRow - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean previous() throws SQLException {
		return this.androidCursor.moveToPrevious();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNull(String p_sColumnLabel) throws SQLException {
		this.updateNull(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBoolean(String p_sColumnLabel, boolean p_bValue) throws SQLException {
		this.updateBoolean(this.findColumn(p_sColumnLabel), p_bValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateByte(String p_sColumnLabel, byte p_bValue) throws SQLException {
		this.updateByte(this.findColumn(p_sColumnLabel), p_bValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateShort(String p_sColumnLabel, short p_oX) throws SQLException {
		this.updateShort(this.findColumn(p_sColumnLabel), p_oX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateInt(String p_sColumnLabel, int p_iValue) throws SQLException {
		this.updateInt(this.findColumn(p_sColumnLabel), p_iValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateLong(String p_sColumnLabel, long p_sValue) throws SQLException {
		this.updateLong(this.findColumn(p_sColumnLabel), p_sValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateFloat(String p_sColumnLabel, float p_fValue) throws SQLException {
		this.updateFloat(this.findColumn(p_sColumnLabel), p_fValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDouble(String p_sColumnLabel, double p_dValue) throws SQLException {
		this.updateDouble(this.findColumn(p_sColumnLabel), p_dValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBigDecimal(String p_sColumnLabel, BigDecimal p_oX) throws SQLException {
		this.updateBigDecimal(this.findColumn(p_sColumnLabel), p_oX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateString(String p_sColumnLabel, String p_sValue) throws SQLException {
		this.updateString(this.findColumn(p_sColumnLabel), p_sValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBytes(String p_sColumnLabel, byte[] p_t_bValue) throws SQLException {
		this.updateBytes(this.findColumn(p_sColumnLabel), p_t_bValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDate(String p_sColumnLabel, Date p_oValue) throws SQLException {
		this.updateDate(this.findColumn(p_sColumnLabel), p_oValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTime(String p_sColumnLabel, Time p_oValue) throws SQLException {
		this.updateTime(this.findColumn(p_sColumnLabel), p_oValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTimestamp(String p_sColumnLabel, Timestamp p_oValue) throws SQLException {
		this.updateTimestamp(this.findColumn(p_sColumnLabel), p_oValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(String p_sColumnLabel, InputStream p_oValue, int p_iLength) throws SQLException {
		this.updateAsciiStream(this.findColumn(p_sColumnLabel), p_oValue, p_iLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(String p_sColumnLabel, InputStream p_oValue, int p_iLength) throws SQLException {
		this.updateBinaryStream(this.findColumn(p_sColumnLabel), p_oValue, p_iLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(String p_sColumnLabel, Reader p_oReader, int p_iLength) throws SQLException {
		this.updateCharacterStream(this.findColumn(p_sColumnLabel), p_oReader, p_iLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateObject(String p_sColumnLabel, Object p_oValue, int p_iScaleOrLength) throws SQLException {
		this.updateObject(this.findColumn(p_sColumnLabel), p_oValue, p_iScaleOrLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateObject(String p_sColumnLabel, Object p_oValue) throws SQLException {
		this.updateObject(this.findColumn(p_sColumnLabel), p_oValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveToCurrentRow() throws SQLException {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statement getStatement() throws SQLException {
		return this.statement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(String p_sColumnLabel, Map<String, Class<?>> p_oMap) throws SQLException {
		return this.getObject(this.findColumn(p_sColumnLabel), p_oMap);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ref getRef(String p_sColumnLabel) throws SQLException {
		return this.getRef(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Blob getBlob(String p_sColumnLabel) throws SQLException {
		return this.getBlob(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Clob getClob(String p_sColumnLabel) throws SQLException {
		return this.getClob(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Array getArray(String p_sColumnLabel) throws SQLException {
		return this.getArray(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(int p_iColumnIndex, Calendar p_oCal) throws SQLException {
		p_oCal.setTimeInMillis(this.getLong(p_iColumnIndex));
		return new Date(p_oCal.getTimeInMillis());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(String p_sColumnLabel, Calendar p_oCal) throws SQLException {
		return this.getDate(this.findColumn(p_sColumnLabel), p_oCal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(int p_iColumnIndex, Calendar p_oCal) throws SQLException {
		p_oCal.setTimeInMillis(this.getLong(p_iColumnIndex));
		return new Time(p_oCal.getTimeInMillis());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(String p_sColumnLabel, Calendar p_oCal) throws SQLException {
		return this.getTime(this.findColumn(p_sColumnLabel), p_oCal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(int p_iColumnIndex, Calendar p_oCal) throws SQLException {
		Timestamp r_oTimestamp = null;

		long lTimeInMillis = this.getLong(p_iColumnIndex);
		if (!this.wasNull()) {
			p_oCal.setTimeInMillis(lTimeInMillis);
			r_oTimestamp = new Timestamp(p_oCal.getTimeInMillis());
		}
		return r_oTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(String p_sColumnLabel, Calendar p_oCal) throws SQLException {
		return this.getTimestamp(this.findColumn(p_sColumnLabel), p_oCal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getURL(int p_iColumnIndex) throws SQLException {
		try {
			return new URL(this.getString(p_iColumnIndex));
		}
		catch (MalformedURLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getURL(String p_sColumnLabel) throws SQLException {
		return this.getURL(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRef(String p_sColumnLabel, Ref p_oX) throws SQLException {
		this.updateRef(this.findColumn(p_sColumnLabel), p_oX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(String p_sColumnLabel, Blob p_oX) throws SQLException {
		this.updateBlob(this.findColumn(p_sColumnLabel), p_oX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(String p_sColumnLabel, Clob p_oX) throws SQLException {
		this.updateClob(this.findColumn(p_sColumnLabel), p_oX);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateArray(String p_sColumnLabel, Array p_oX) throws SQLException {
		this.updateArray(this.findColumn(p_sColumnLabel), p_oX);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowId getRowId(String p_sColumnLabel) throws SQLException {
		return this.getRowId(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRowId(String p_sColumnLabel, RowId p_oX) throws SQLException {
		this.updateRowId(this.findColumn(p_sColumnLabel), p_oX);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return this.androidCursor.isClosed();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNString(String p_sColumnLabel, String p_oNString) throws SQLException {
		this.updateNString(this.findColumn(p_sColumnLabel), p_oNString);
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(String p_sColumnLabel, NClob p_oNClob) throws SQLException {
		this.updateNClob(this.findColumn(p_sColumnLabel), p_oNClob);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NClob getNClob(String p_sColumnLabel) throws SQLException {
		return this.getNClob(this.findColumn(p_sColumnLabel));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLXML getSQLXML(String p_sColumnLabel) throws SQLException {
		return this.getSQLXML(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateSQLXML(String p_sColumnLabel, SQLXML p_oXmlObject) throws SQLException {
		this.updateSQLXML(this.findColumn(p_sColumnLabel), p_oXmlObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNString(String p_sColumnLabel) throws SQLException {
		return this.getNString(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getNCharacterStream(String p_sColumnLabel) throws SQLException {
		return this.getNCharacterStream(this.findColumn(p_sColumnLabel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNCharacterStream(String p_sColumnLabel, Reader p_oReader, long p_oLength) throws SQLException {
		this.updateNCharacterStream(this.findColumn(p_sColumnLabel), p_oReader, p_oLength);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(String p_sColumnLabel, InputStream p_oX, long p_oLength) throws SQLException {
		this.updateAsciiStream(this.findColumn(p_sColumnLabel), p_oX, p_oLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(String p_sColumnLabel, InputStream p_oX, long p_oLength) throws SQLException {
		this.updateBinaryStream(this.findColumn(p_sColumnLabel), p_oX, p_oLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(String p_sColumnLabel, Reader p_oReader, long p_oLength) throws SQLException {
		this.updateCharacterStream(this.findColumn(p_sColumnLabel), p_oReader, p_oLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(String p_sColumnLabel, InputStream p_oInputStream, long p_oLength) throws SQLException {
		this.updateBlob(this.findColumn(p_sColumnLabel), p_oInputStream, p_oLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(String p_sColumnLabel, Reader p_oReader, long p_oLength) throws SQLException {
		this.updateClob(this.findColumn(p_sColumnLabel), p_oReader, p_oLength);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(String p_sColumnLabel, Reader p_oReader, long p_oLength) throws SQLException {
		this.updateNClob(this.findColumn(p_sColumnLabel), p_oReader, p_oLength);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNCharacterStream(String p_sColumnLabel, Reader p_oReader) throws SQLException {
		this.updateNCharacterStream(this.findColumn(p_sColumnLabel), p_oReader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(String p_sColumnLabel, InputStream p_oX) throws SQLException {
		this.updateAsciiStream(this.findColumn(p_sColumnLabel), p_oX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(String p_sColumnLabel, InputStream p_oX) throws SQLException {
		this.updateBinaryStream(this.findColumn(p_sColumnLabel), p_oX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(String p_sColumnLabel, Reader p_oReader) throws SQLException {
		this.updateCharacterStream(this.findColumn(p_sColumnLabel), p_oReader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(String p_sColumnLabel, InputStream p_oInputStream) throws SQLException {
		this.updateBlob(this.findColumn(p_sColumnLabel), p_oInputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(String p_sColumnLabel, Reader p_oReader) throws SQLException {
		this.updateClob(this.findColumn(p_sColumnLabel), p_oReader);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(String p_sColumnLabel, Reader p_oReader) throws SQLException {
		this.updateNClob(this.findColumn(p_sColumnLabel), p_oReader);
	}
}