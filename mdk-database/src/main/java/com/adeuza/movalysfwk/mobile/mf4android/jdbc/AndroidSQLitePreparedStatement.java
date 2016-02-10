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
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.util.Log;

/**
 * <p>Implements "PreparedStatement" of JDBC API for android devices.</p>
 */
public class AndroidSQLitePreparedStatement extends AndroidSQLiteStatement implements PreparedStatement {

	/**
	 * All binded values (Object format)
	 */
	private List<Object> bindedObjects;

	/**
	 * All binded values (String format)
	 */
	private List<String> bindedStrings;

	/**
	 * Previous binded values stored is this object when {@link #addBatch()} is called.
	 */
	private Queue<List<Object>> batchBindedObjects;

	/**
	 * True if the sql instructions is an insert.
	 */
	private boolean select;

	/**
	 * Creates a new instance of this class using a connection and a sql instruction.
	 * @param p_oConnection A database connection
	 * @param p_sQuery a sql instruction
	 */
	public AndroidSQLitePreparedStatement(AndroidSQLiteConnection p_oConnection, String p_sQuery) {
		super(p_oConnection);

		this.sql = p_sQuery;
		this.select			= AndroidSQLiteStatement.isSelect(this.sql);
		this.bindedObjects	= new ArrayList<>();
		this.bindedStrings	= new ArrayList<>();

		this.batchBindedObjects = new LinkedList<>();
	}


	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	@Override
	public ResultSet executeQuery() throws SQLException {
		if(!this.select) {
			throw new SQLException("impossible to call \"executeQuery\" if the sql instruction is not a \"select\".");
		}

//		final long lStart = System.currentTimeMillis();
//		Log.d("SQL", "executeQuery, sql: "  + this.sql + ", binded values: " + this.bindedStrings.toString());

		ResultSet r_oResultSet = new AndroidSQLiteResultSet(this, this.getConnection().getAndroidDatabase().rawQuery(this.sql,
				this.bindedStrings.toArray(new String[this.bindedStrings.size()])));

//		Log.d("SQL", "executeQuery, tps: " + (System.currentTimeMillis() - lStart) + " ms");

		return r_oResultSet; 
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	@Override
	public int executeUpdate() throws SQLException {
		if (this.select) {
			throw new SQLException("Impossible to call \"executeUpdate\" if the sql instruction is not an update intruction.");
		}
		try {
			Log.d("SQL", "executeUpdate, sql: "  + this.sql + ", binded values: " + this.bindedObjects.toString());
			this.getConnection().getAndroidDatabase().execSQL(this.sql, this.bindedObjects.toArray());
		} catch(android.database.SQLException oSqlException) {
			Log.e("SQL", "executeUpdate failed ! sql: "  + this.sql + ", binded values: " + this.bindedObjects.toString());
			throw oSqlException ;
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	@Override
	public void setNull(int p_iParameterIndex, int p_oSqlType) throws SQLException {
		this.setBindedValue(p_iParameterIndex, null, null);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	@Override
	public void setBoolean(int p_iParameterIndex, boolean p_bValue) throws SQLException {
		if (p_bValue){
			this.setInt(p_iParameterIndex, 1 );
		}else{
			this.setInt(p_iParameterIndex, 0 );
		}
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	@Override
	public void setByte(int p_iParameterIndex, byte p_bValue) throws SQLException {
		this.setBindedValue(p_iParameterIndex, p_bValue, Short.toString(p_bValue));
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	@Override
	public void setShort(int p_iParameterIndex, short p_sValue) throws SQLException {
		this.setBindedValue(p_iParameterIndex, p_sValue, Short.toString(p_sValue));
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	@Override
	public void setInt(int p_iParameterIndex, int p_iValue) throws SQLException {
		this.setBindedValue(p_iParameterIndex, p_iValue, Integer.toString(p_iValue));
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	@Override
	public void setLong(int p_iParameterIndex, long p_lValue) throws SQLException {
		this.setBindedValue(p_iParameterIndex, p_lValue, Long.toString(p_lValue));
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	@Override
	public void setFloat(int p_iParameterIndex, float p_fValue) throws SQLException {
		this.setBindedValue(p_iParameterIndex, p_fValue, Float.toString(p_fValue));
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	@Override
	public void setDouble(int p_iParameterIndex, double p_dValue) throws SQLException {
		this.setBindedValue(p_iParameterIndex, p_dValue, Double.toString(p_dValue));
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(int p_iParameterIndex, BigDecimal p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	@Override
	public void setString(int p_iParameterIndex, String p_sValue) throws SQLException {
		this.setBindedValue(p_iParameterIndex, p_sValue, p_sValue);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	@Override
	public void setBytes(int p_iParameterIndex, byte[] p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	@Override
	public void setDate(int p_iParameterIndex, Date p_oValue) throws SQLException {
		if (p_oValue == null) {
			this.setNull(p_iParameterIndex, Types.DATE);
		}
		else {
			this.setLong(p_iParameterIndex, p_oValue.getTime());
		}
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	@Override
	public void setTime(int p_iParameterIndex, Time p_oValue) throws SQLException {
		if (p_oValue == null) {
			this.setNull(p_iParameterIndex, Types.TIME);
		}
		else {
			this.setLong(p_iParameterIndex, p_oValue.getTime());
		}
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(int p_iParameterIndex, Timestamp p_oValue) throws SQLException {
		if (p_oValue == null) {
			this.setNull(p_iParameterIndex, Types.TIMESTAMP);
		}
		else {
			this.setLong(p_iParameterIndex, p_oValue.getTime());
		}
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
	 */
	@Override
	public void setAsciiStream(int p_iParameterIndex, InputStream p_oX, int p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
	 */
	@Override
	public void setUnicodeStream(int p_iParameterIndex, InputStream p_oX, int p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
	 */
	@Override
	public void setBinaryStream(int p_iParameterIndex, InputStream p_oX, int p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	@Override
	public void clearParameters() throws SQLException {
		this.bindedObjects.clear();
		this.bindedStrings.clear();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	@Override
	public void setObject(int p_iParameterIndex, Object p_oValue, int p_iTargetSqlType) throws SQLException {
		
		int v_iTargetSqlType = p_iTargetSqlType;
		if(p_iTargetSqlType == Types.NUMERIC && !Long.class.isAssignableFrom(p_oValue.getClass())){
			v_iTargetSqlType = Types.INTEGER; 
		}
		
		if(p_iTargetSqlType == Types.DECIMAL){
			v_iTargetSqlType = Types.FLOAT;
		}
		
		switch (v_iTargetSqlType) {
			case Types.NUMERIC:
				this.setLong(p_iParameterIndex, (Long) p_oValue);
				break;
			case Types.FLOAT:
				this.setFloat(p_iParameterIndex, (Float) p_oValue);
				break;
			case Types.DOUBLE:
				this.setDouble(p_iParameterIndex, (Double) p_oValue);
				break;
			case Types.VARCHAR:
				this.setString(p_iParameterIndex, (String) p_oValue);
				break;
			case Types.DATE:
				this.setDate(p_iParameterIndex, (Date) p_oValue);
				break;
			case Types.TIME:
				this.setTime(p_iParameterIndex, (Time) p_oValue);
				break;
			case Types.TIMESTAMP:
				this.setTimestamp(p_iParameterIndex, (Timestamp) p_oValue);
				break;
			case Types.BOOLEAN:
				this.setBoolean(p_iParameterIndex, (Boolean) p_oValue);
				break;
			default:
				this.setObject(p_iParameterIndex, p_oValue);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	@Override
	public void setObject(int p_iParameterIndex, Object p_oValue) throws SQLException {
		String sValue = null;
		if (p_oValue != null) {
			sValue = p_oValue.toString();
		}
		this.setBindedValue(p_iParameterIndex, p_oValue, sValue);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#execute()
	 */
	@Override
	public boolean execute() throws SQLException {
		try { 
			//Log.d("AndroidSQLitePreparedStatment.execute()", this.sql);
			this.getConnection().getAndroidDatabase().execSQL(this.sql, this.bindedObjects.toArray());
		} catch( android.database.SQLException oSqlException ) {
			Log.e("SQL", "execute failed ! sql: "  + this.sql + ", binded values: " + this.bindedObjects.toString());
			throw oSqlException ;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	@Override
	public void addBatch() throws SQLException {
		if (this.select) {
			throw new SQLException("Impossible to call \"addBatch\" method for a select.");
		}

		this.batchBindedObjects.offer(this.bindedObjects);

		this.bindedObjects = new ArrayList<>();
		this.bindedStrings.clear();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#executeBatch()
	 */
	@Override
	public int[] executeBatch() throws SQLException {
		if (this.select) {
			throw new SQLException("Impossible to call \"executeBatch\" method for a select.");
		}

		int[] r_t_iUpdates = new int[this.batchBindedObjects.size()];

		int iIndex = 0;
		for (List<Object> listBindedObjects : this.batchBindedObjects) {
			this.bindedObjects = listBindedObjects;
			r_t_iUpdates[iIndex] = this.executeUpdate();
		}
		return r_t_iUpdates;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#clearBatch()
	 */
	@Override
	public void clearBatch() throws SQLException {
		this.batchBindedObjects.clear();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
	 */
	@Override
	public void setCharacterStream(int p_iParameterIndex, Reader p_oReader, int p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	@Override
	public void setRef(int p_iParameterIndex, Ref p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	@Override
	public void setBlob(int p_iParameterIndex, Blob p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	@Override
	public void setClob(int p_iParameterIndex, Clob p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	@Override
	public void setArray(int p_iParameterIndex, Array p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
	 */
	@Override
	public void setDate(int p_iParameterIndex, Date p_oValue, Calendar p_oCal) throws SQLException {
		this.setDate(p_iParameterIndex, p_oValue);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
	 */
	@Override
	public void setTime(int p_iParameterIndex, Time p_oValue, Calendar p_oCal) throws SQLException {
		this.setTime(p_iParameterIndex, p_oValue);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
	 */
	@Override
	public void setTimestamp(int p_iParameterIndex, Timestamp p_oValue, Calendar p_oCal) throws SQLException {
		this.setTimestamp(p_iParameterIndex, p_oValue);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	@Override
	public void setNull(int p_iParameterIndex, int p_oSqlType, String p_oTypeName) throws SQLException {
		this.setBindedValue(p_iParameterIndex, null, null);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	@Override
	public void setURL(int p_iParameterIndex, URL p_oValue) throws SQLException {
		if (p_oValue == null) {
			this.setNull(p_iParameterIndex, Types.VARCHAR);
		}
		else {
			this.setString(p_iParameterIndex, p_oValue.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 */
	@Override
	public void setRowId(int p_iParameterIndex, RowId p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 */
	@Override
	public void setNString(int p_iParameterIndex, String p_sValue) throws SQLException {
		this.setString(p_iParameterIndex, p_sValue);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void setNCharacterStream(int p_iParameterIndex, Reader p_oValue, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 */
	@Override
	public void setNClob(int p_iParameterIndex, NClob p_oValue) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
	 */
	@Override
	public void setClob(int p_iParameterIndex, Reader p_oReader, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
	 */
	@Override
	public void setBlob(int p_iParameterIndex, InputStream p_oInputStream, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
	 */
	@Override
	public void setNClob(int p_iParameterIndex, Reader p_oReader, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 */
	@Override
	public void setSQLXML(int p_iParameterIndex, SQLXML p_oXmlObject) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
	 */
	@Override
	public void setObject(int p_iParameterIndex, Object p_oX, int p_oTargetSqlType, int p_oScaleOrLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
	 */
	@Override
	public void setAsciiStream(int p_iParameterIndex, InputStream p_oX, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
	 */
	@Override
	public void setBinaryStream(int p_iParameterIndex, InputStream p_oX, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void setCharacterStream(int p_iParameterIndex, Reader p_oReader, long p_oLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 */
	@Override
	public void setAsciiStream(int p_iParameterIndex, InputStream p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 */
	@Override
	public void setBinaryStream(int p_iParameterIndex, InputStream p_oX) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void setCharacterStream(int p_iParameterIndex, Reader p_oReader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void setNCharacterStream(int p_iParameterIndex, Reader p_oValue) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 */
	@Override
	public void setClob(int p_iParameterIndex, Reader p_oReader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 */
	@Override
	public void setBlob(int p_iParameterIndex, InputStream p_oInputStream) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 */
	@Override
	public void setNClob(int p_iParameterIndex, Reader p_oReader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO Décrire la méthode setBindedValue de la classe AndroidSQLitePreparedStatement
	 * @param p_iParameterIndex index of the parameter
	 * @param p_oValue object to bind
	 * @param p_sValue string to bind
	 */
	private void setBindedValue(int p_iParameterIndex, Object p_oValue, String p_sValue) {
		while (this.bindedObjects.size() < p_iParameterIndex) {
			this.bindedObjects.add(null);
			this.bindedStrings.add(null);
		}

		int iAndroidIndex = p_iParameterIndex - 1;

		this.bindedObjects.set(iAndroidIndex, p_oValue);
		this.bindedStrings.set(iAndroidIndex, p_sValue);
	}
}
