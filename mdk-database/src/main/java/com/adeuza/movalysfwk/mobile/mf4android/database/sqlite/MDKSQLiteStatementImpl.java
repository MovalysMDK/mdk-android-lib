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

import android.database.sqlite.SQLiteStatement;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;

/**
 * Statement.
 * <p>Use this statement to bind values for SQL insert/update/query ONLY.</p>
 *
 */
public class MDKSQLiteStatementImpl implements MDKSQLiteStatement {

	/**
	 * Current position.
	 */
	private int position = 1;
	
	/**
	 * SQLite statement.
	 */
	private SQLiteStatement statement;
	
	/**
	 * Constructor.
	 * @param compileStatement SQLiteStatement
	 */
	public MDKSQLiteStatementImpl(SQLiteStatement compileStatement) {
		this.statement = compileStatement;
	}

	@Override
	public void bindLong(long p_lValue) throws DaoException {
		this.statement.bindLong(this.position, p_lValue);
		this.position++;
	}
	
	@Override
	public void bindDouble(double p_dDouble) throws DaoException {
		this.statement.bindDouble(this.position, p_dDouble);
		this.position++;
	}
	
	@Override
	public void bindString(String p_sValue) throws DaoException {
		if ( p_sValue == null ) {
			bindNull();
		}
		else {
			this.statement.bindString(this.position, p_sValue);
			this.position++;
		}
	}
	
	@Override
	public void bindBoolean(Boolean p_bValue) throws DaoException {
		if ( p_bValue == null ) {
			bindNull();
		}
		else {
			this.statement.bindLong(this.position, p_bValue?1:0);
			this.position++;
		}
	}
	
	@Override
	public void bindTimestamp(Timestamp p_oValue) throws DaoException {
		if (p_oValue == null) {
			this.bindNull();
		}
		else {
			this.statement.bindLong(this.position, p_oValue.getTime());
			this.position++;
		}
	}
	
	@Override
	public void bindMEnum(Enum p_oValue) throws DaoException {
		if ( p_oValue == null) {
			this.bindNull();
		}
		else {
			this.statement.bindLong(this.position, p_oValue.getBaseId());
			this.position++;
		}
	}

	@Override
	public void bindInt(Integer p_iValue) throws DaoException {
		if ( p_iValue == null) {
			this.bindNull();
		}
		else {
			this.statement.bindLong(this.position, p_iValue);
			this.position++;
		}
	}

	@Override
	public void bindChar(char p_iValue) throws DaoException {
		this.statement.bindString(this.position, Character.toString(p_iValue));
		this.position++;
	}

	@Override
	public void bindByte(byte p_iValue) throws DaoException {
		this.statement.bindLong(this.position, p_iValue);
		this.position++;
	}

	@Override
	public void bindShort(short p_iValue) throws DaoException {
		this.statement.bindLong(this.position, p_iValue);
		this.position++;
	}
	
	@Override
	public void bindNull() throws DaoException {
		this.statement.bindNull(this.position);
		this.position++;
	}
	
	@Override
	public long executeInsert() {
		long r_lResult = this.statement.executeInsert();
		this.position = 1;
		return r_lResult;
	}
	
	@Override
	public long executeUpdate() {
		long r_lResult = this.statement.executeUpdateDelete();
		this.position = 1;
		return r_lResult;
	}
	
	@Override
	public long executeDelete() {
		long r_lResult = this.statement.executeUpdateDelete();
		this.position = 1;
		return r_lResult;
	}

	@Override
	public void close() {
		this.statement.close();
	}
}
