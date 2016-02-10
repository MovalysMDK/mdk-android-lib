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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoQuery;

/**
 * <p>
 * ResultSet Reader (iterate over columns)
 * </p>
 *
 */
public class ResultSetReader {

	/**
	 * Current column index for reading
	 */
	private int position;

	/**
	 * Inner Resultset
	 */
	private ResultSet resultSet;
	
	/**
	 * Column count
	 */
	private int columnCount = -1 ;

	/**
	 * Constructor
	 *
	 * @param p_oResultSet resultSet to read
	 */
	public ResultSetReader(ResultSet p_oResultSet) {
		this.position = 1;
		this.resultSet = p_oResultSet;
	}

	/**
	 * Read int column and move position to next column
	 *
	 * @return int value
	 * @throws java.sql.SQLException read failure
	 */
	public int getInt() throws SQLException {
		return this.resultSet.getInt(this.position++);
	}
	
	/**
	 * Read int column and move position to next column
	 *
	 * @return int value
	 * @throws java.sql.SQLException read failure
	 */
	public Integer getInteger() throws SQLException {
		Integer r_iValue = null ;
		int iValue = this.getInt();
		if ( !this.resultSet.wasNull()) {
			r_iValue = iValue ;
		}
		return r_iValue;
	}

	/**
	 * Read long column and move position to next column
	 *
	 * @return column value as long
	 * @throws java.sql.SQLException read failure
	 */
	public long getLong() throws SQLException {
		return this.resultSet.getLong(this.position++);
	}
	
	/**
	 * Read long column and move position to next column
	 *
	 * @return column value as long
	 * @throws java.sql.SQLException read failure
	 */
	public Long getLongObject() throws SQLException {
		Long r_lValue = null ;
		long lValue = this.getLong();
		if ( !this.resultSet.wasNull()) {
			r_lValue = lValue ;
		}
		return r_lValue;
	}

	/**
	 *  Read String column and move position to next column
	 *
	 * @return column value as String
	 * @throws java.sql.SQLException read failure
	 */
	public String getString() throws SQLException {
		return this.resultSet.getString(this.position++);
	}

	/**
	 *  Read boolean column and move position to next column
	 *
	 * @return column value as boolean
	 * @throws java.sql.SQLException read failure
	 */
	public boolean getBoolean() throws SQLException {
		return this.resultSet.getBoolean(this.position++);
	}
	
	/**
	 *  Read boolean column and move position to next column
	 *
	 * @return column value as boolean
	 * @throws java.sql.SQLException read failure
	 */
	public Boolean getBooleanObject() throws SQLException {
		Boolean r_bValue = null ;
		boolean bValue = this.getBoolean();
		if ( !this.resultSet.wasNull()) {
			r_bValue = bValue ;
		}
		return r_bValue;
	}

	/**
	 *  Read char column and move position to next column
	 *
	 * @return column value as char
	 * @throws java.sql.SQLException read failure
	 */
	public char getChar() throws SQLException {
		char r_oChar = ' ';
		String sValue = this.resultSet.getString(this.position++);
		if (sValue != null && sValue.length() > 0) {
			r_oChar = sValue.charAt(0);
		}
		return r_oChar;
	}

	/**
	 *  Read Character column and move position to next column
	 *
	 * @return column value as Character
	 * @throws java.sql.SQLException read failure
	 */
	public Character getCharacter() throws SQLException {
		Character r_oValue = null ;
		char bValue = this.getChar();
		if ( !this.resultSet.wasNull()) {
			r_oValue = bValue ;
		}
		return r_oValue;
	}

	/**
	 * Read short column and move position to next column
	 *
	 * @return column value as Short
	 * @throws java.sql.SQLException read failure
	 */
	public short getShort() throws SQLException {
		return this.resultSet.getShort(this.position++);
	}
	
	/**
	 * Read short column and move position to next column
	 *
	 * @return column value as Short
	 * @throws java.sql.SQLException read failure
	 */
	public Short getShortObject() throws SQLException {
		Short r_iValue = null ;
		short iValue = this.getShort();
		if ( !this.resultSet.wasNull()) {
			r_iValue = iValue ;
		}
		return r_iValue;
	}

	/**
	 * Read byte column and move position to next column
	 *
	 * @return column value as byte
	 * @throws java.sql.SQLException read failure
	 */
	public byte getByte() throws SQLException {
		return this.resultSet.getByte(this.position++);
	}
	
	/**
	 * Read byte column and move position to next column
	 *
	 * @return column value as byte
	 * @throws java.sql.SQLException read failure
	 */
	public Byte getByteObject() throws SQLException {
		Byte r_bValue = null ;
		byte bValue = this.getByte();
		if ( !this.resultSet.wasNull()) {
			r_bValue = bValue ;
		}
		return r_bValue;
	}

	/**
	 * Read double column and move position to next column
	 *
	 * @return column value as double
	 * @throws java.sql.SQLException read failure
	 */
	public double getDouble() throws SQLException {
		return this.resultSet.getDouble(this.position++);
	}
	
	/**
	 * Read double column and move position to next column
	 *
	 * @return column value as double
	 * @throws java.sql.SQLException read failure
	 */
	public Double getDoubleObject() throws SQLException {
		Double r_dValue = null ;
		double dValue = this.getDouble();
		if ( !this.resultSet.wasNull()) {
			r_dValue = dValue ;
		}
		return r_dValue;
	}

	/**
	 * Read float column and move position to next column
	 *
	 * @return column value as float
	 * @throws java.sql.SQLException read failure
	 */
	public float getFloat() throws SQLException {
		return this.resultSet.getFloat(this.position++);
	}
	
	/**
	 * Read float column and move position to next column
	 *
	 * @return column value as float
	 * @throws java.sql.SQLException read failure
	 */
	public Float getFloatObject() throws SQLException {
		Float r_fValue = null ;
		float fValue = this.getFloat();
		if ( !this.resultSet.wasNull()) {
			r_fValue = fValue ;
		}
		return r_fValue;
	}

	/**
	 * Read date column and move position to next column
	 *
	 * @return column value as Date
	 * @throws java.sql.SQLException read failure
	 */
	public Date getDate() throws SQLException {
		return this.resultSet.getDate(this.position++);
	}

	/**
	 * Read timestamp column and move position to next column
	 *
	 * @return column value as Timestamp
	 * @throws java.sql.SQLException read failure
	 */
	public Timestamp getTimestamp() throws SQLException {
		return this.resultSet.getTimestamp(this.position++);
	}

	/**
	 * Read clob column and move position to next column
	 *
	 * @return column value as String
	 * @throws java.sql.SQLException read failure
	 */
	public String getClobAsString() throws SQLException {
		Clob oClob = this.resultSet.getClob(this.position++); 
        if (oClob != null) { 
        	return oClob.getSubString(1, (int) oClob.length()); 
        }
        return null; 
	}

	/**
	 * Read blob column and move position to next column
	 *
	 * @return column value as byte
	 * @throws java.sql.SQLException read failure
	 */
	public byte[] getBlobAsByteArray() throws SQLException {
		Blob oBlob = this.resultSet.getBlob(this.position++);
		if (oBlob != null) {
			return oBlob.getBytes(1, (int) oBlob.length());
		}
		return null;
	}

	/**
	 * Return true if last column was null
	 *
	 * @return true if last column was null
	 * @throws java.sql.SQLException read failure
	 */
	public boolean wasNull() throws SQLException {
		return this.resultSet.wasNull();
	}
	
	/**
	 * Read next line in resultset
	 *
	 * @return false if not next line
	 * @throws java.sql.SQLException read failure
	 */
	public boolean next() throws SQLException {
		this.position = 1 ;
		return this.resultSet.next();
	}

	/**
	 * For MIEntity Query, set position to the custom fields
	 *
	 * @param p_oDaoQuery Dao query
	 * @param p_iNbFields number of fields in entity
	 * @param p_iNbI18nFields number of i18n field in entity
	 */
	public void setPositionToCustomFields( DaoQuery p_oDaoQuery, int p_iNbFields, int p_iNbI18nFields ) {
		this.position = p_iNbFields + 1 ;
	}

	/**
	 * Retourne sur le champs précédents
	 */
	public void back() {
		if ( this.position > 1 ) {
			this.position-- ;
		}
	}
	
	/**
	 * Advance position to the next column
	 */
	public void incrPosition() {
		this.position++;
	}
	
	/**
	 * Return the current column position
	 *
	 * @return current column position
	 */
	public int getPosition() {
		return this.position;
	}

	/**
	 * Return the entity histo level
	 * <p>This method doesnot touch the current column position</p>
	 *
	 * @return entity histo level
	 * @throws java.sql.SQLException get histolevel failure
	 */
	public String getHistoLevel() throws SQLException {
		if ( this.columnCount == -1 ) {
			this.columnCount = this.resultSet.getMetaData().getColumnCount();
		}
		return this.resultSet.getString(this.columnCount);
	}
	
	/**
	 * Return the inner resultSet
	 *
	 * @return inner resultset
	 */
	public ResultSet getResultSet() {
		return this.resultSet ;
	}
	
	/**
	 * Close the result set
	 *
	 * @throws java.sql.SQLException resultset close failure
	 */
	public void close() throws SQLException {
		this.resultSet.close();
	}
}
