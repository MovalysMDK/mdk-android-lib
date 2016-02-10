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

import android.database.Cursor;

/**
 * <p>SQLite Database</p>
 *
 *	@param <T> type of database (eg. SQLiteDatabase)
 */
public interface AndroidSQLiteDatabase<T> {

	/**
	 * execute SQL
	 * @param p_sTring query to execute
	 */
	public void execSQL(String p_sTring);

	/**
	 * Begin transaction
	 */
	public void beginTransaction();

	/**
	 * set transaction successful
	 */
	public void setTransactionSuccessful();

	/**
	 * end the transaction
	 */
	public void endTransaction();

	/**
	 * check if transaction opened
	 * @return boolean
	 */
	public boolean inTransaction();


	/**
	 * close database
	 */
	public void close();

	/**
	 * check if database is opened
	 * @return boolean
	 */
	public boolean isOpen();

	/**
	 * check if database access is in read-only mode
	 * @return boolean
	 */
	public boolean isReadOnly();

	/**
	 * get cursor on the result
	 * @param p_sQl query SQL
	 * @param p_aRray parameters
	 * @return result
	 */
	public Cursor rawQuery(String p_sQl, String[] p_aRray);

	/**
	 * execute query
	 * @param p_sQl SQL query
	 * @param p_aRray query parameters
	 */
	public void execSQL(String p_sQl, Object[] p_aRray);
	
	/**
	 * get database instance
	 * @return db instance
	 */
	public T getSQLiteDatabase();

}
