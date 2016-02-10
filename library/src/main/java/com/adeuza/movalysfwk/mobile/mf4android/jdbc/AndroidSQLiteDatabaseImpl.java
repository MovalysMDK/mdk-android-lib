package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * <p>SQLite Database</p>
 *
 * <p>Copyright (c) 2014</p>
 * <p>Company: Sopra</p>
 */
public class AndroidSQLiteDatabaseImpl implements AndroidSQLiteDatabase<SQLiteDatabase> {

	/**
	 * database instance
	 */
	private SQLiteDatabase db = null;
	
	/**
	 * constructor
	 * @param p_oDb db instance
	 */
	public AndroidSQLiteDatabaseImpl(SQLiteDatabase p_oDb) {
		this.db = p_oDb;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTransaction() {
		this.db.beginTransaction();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		this.db.close();
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void endTransaction() {
		this.db.endTransaction();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execSQL(String p_sTring) {
		this.db.execSQL(p_sTring);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean inTransaction() {
		return this.db.inTransaction();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		return this.db.isOpen();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadOnly() {
		return this.db.isReadOnly();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTransactionSuccessful() {
		this.db.setTransactionSuccessful();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cursor rawQuery(String p_sQl, String[] p_aRray) {
		return this.db.rawQuery(p_sQl, p_aRray);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execSQL(String p_sQl, Object[] p_aRray) {
		this.db.execSQL(p_sQl, p_aRray);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLiteDatabase getSQLiteDatabase() {
		return this.db;
	}
}
