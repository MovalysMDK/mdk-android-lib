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
package com.adeuza.movalysfwk.mobile.mf4android.database.sqlite;

import java.sql.Timestamp;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;

/**
 * MDK SQLiteStatement.
 */
public interface MDKSQLiteStatement {

	/**
	 * Binding null value.
	 *
	 * @param p_oSqlType sql type
	 * @throws DaoException binding failure
	 */
	void bindNull() throws DaoException;

	/**
	 * Bind a string value.
	 *
	 * @param p_sValue string value for binding.
	 * @throws DaoException binding failure
	 */
	void bindString(String p_sValue) throws DaoException;

	/**
	 * Bind a double value.
	 *
	 * @param p_dDouble double value.
	 * @throws DaoException binding failure
	 */
	void bindDouble(Double p_dDouble) throws DaoException;
	
	/**
	 * Bind a long value.
	 *
	 * @param p_lValue long value to bind
	 * @throws DaoException binding failure
	 */
	void bindLong(Long p_lValue) throws DaoException;

	/**
	 * Bind a boolean value.
	 * @param p_bValue boolean value
	 * @throws DaoException binding failure
	 */
	void bindBoolean(Boolean p_bValue) throws DaoException;
	
	/**
	 * Bind a timestamp value.
	 * @param p_oTimestamp timestamp
	 */
	void bindTimestamp(Timestamp p_oTimestamp) throws DaoException;
	
	/**
	 * Bind a enum value.
	 * @param p_oValue enum value
	 */
	void bindMEnum(Enum p_oValue) throws DaoException;
	
	/**
	 * Bind an Integer value.
	 * @param p_iValue Integer value
	 */
	void bindInt(Integer p_iValue) throws DaoException;
	
	/**
	 * Bind a char value.
	 * @param p_iValue char value.
	 */
	void bindChar(Character p_iValue) throws DaoException;

	/**
	 * Bind a byte value.
	 * @param p_iValue byte value.
	 */
	void bindByte(Byte p_iValue) throws DaoException;

	/**
	 * Bind a short value.
	 * @param p_iValue short value.
	 */
	void bindShort(Short p_iValue) throws DaoException;
	
	/**
	 * Execute delete query.
	 * @return
	 */
	long executeDelete();

	/**
	 * Execute update query.
	 * @return
	 */
	long executeUpdate();

	/**
	 * Execute insert query.
	 * @return
	 */
	long executeInsert();

	/**
	 * Close statement.
	 */
	void close();
}
