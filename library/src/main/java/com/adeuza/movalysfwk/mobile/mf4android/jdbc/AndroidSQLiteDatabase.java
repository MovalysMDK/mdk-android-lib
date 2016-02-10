package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import android.database.Cursor;

/**
 * <p>SQLite Database</p>
 *
 * <p>Copyright (c) 2014</p>
 * <p>Company: Sopra</p>
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
