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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;

/**
 * AndroidSQLiteStatement
 *
 */
public class AndroidSQLiteStatement {

	/**
	 * Pattern used to detect insert.
	 */
	private static final Pattern INSERT_REGEX = Pattern.compile("^\\s*INSERT\\s+INTO\\s+([a-zA-Z0-9_]+)\\s+.*$", Pattern.CASE_INSENSITIVE);

	/**
	 * Pattern used to deted select.
	 */
	private static final Pattern SELECT_REGEX = Pattern.compile("^\\s*SELECT\\s+.*$", Pattern.CASE_INSENSITIVE);
	
	/**
	 * The connection.
	 */
	private AndroidSQLiteConnection connection;

	/**
	 * The sql instruction.
	 */
	protected String sql;

	/**
	 * The ResultSet.
	 */
	private AndroidSQLiteResultSet resultSet;

	/**
	 * return the table where insert is performed
	 * @param p_sQuery the query
	 * @return the table name
	 */
	protected static String getTableWherePerformInsert(String p_sQuery) {
		String r_sTableName = null;
		if (p_sQuery != null) {
			Matcher oMatch = INSERT_REGEX.matcher(p_sQuery);
			if (oMatch.find()) {
				r_sTableName = oMatch.group(1);
			}
		}
		return r_sTableName;
	}

	/**
	 * return true if the query is a insert query
	 * @param p_sQuery the query to test
	 * @return true if the is a insert query, false otherwise
	 */
	protected static boolean isInsert(String p_sQuery) {
		return INSERT_REGEX.matcher(p_sQuery).find();
	}

	/**
	 * Return <em>true</em> when <em>p_sSql</em> represents a "select" sql instruction.
	 * @param p_sSql A SQL instruction
	 * @return <em>true</em> when <em>p_sSql</em> represents a "select" sql instruction, false otherwise.
	 */
	protected static boolean isSelect(String p_sSql) {
		return p_sSql != null && SELECT_REGEX.matcher(p_sSql).find();
	}
	
	/**
	 * construcor
	 * @param p_oConnection JDBC connection
	 */
	public AndroidSQLiteStatement(AndroidSQLiteConnection p_oConnection) {
		this.connection		= p_oConnection;
		this.resultSet		= null;
		this.sql			= null;
	}

	//FIXME: javadoc
	public AndroidSQLiteResultSet executeQuery(String p_sSql) throws DaoException {
		if (this.resultSet != null && !this.resultSet.isClosed()) {
			throw new DaoException("Previous ResultSet is not closed.");
		}
		this.sql		= p_sSql;
		this.resultSet	= new AndroidSQLiteResultSet(this, this.connection.getAndroidDatabase().rawQuery(p_sSql, null));

		return this.resultSet;
	}

	//FIXME: javadoc
	public int executeUpdate(String p_sSql) throws DaoException {
		this.sql = p_sSql;
		this.connection.getAndroidDatabase().execSQL(p_sSql);
		return 0;
	}

	//FIXME: javadoc
	public void close() throws DaoException {
		if (this.resultSet != null) {
			if (this.resultSet.isClosed()) {
				this.resultSet = null;
			}
			else {
				throw new DaoException("Impossible to close a Statement if a Resulset is not closed.");
			}
		}
		
	}

	//FIXME: javadoc
	public boolean execute(String p_sSql) throws DaoException {
		this.connection.getAndroidDatabase().execSQL(p_sSql);
		return true;
	}

	//FIXME: javadoc
	public AndroidSQLiteResultSet getResultSet() throws DaoException {
		return this.resultSet;
	}

	// FIXME: javadoc
	public AndroidSQLiteConnection getConnection() throws DaoException {
		return this.connection;
	}

	// FIXME: javadoc
	public boolean isClosed() throws DaoException {
		return this.resultSet == null || this.resultSet.isClosed();
	}
}
