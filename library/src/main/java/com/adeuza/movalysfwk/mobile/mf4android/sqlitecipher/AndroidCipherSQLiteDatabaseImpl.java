package com.adeuza.movalysfwk.mobile.mf4android.sqlitecipher;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase;
/**
 * 
 *
 */
public class AndroidCipherSQLiteDatabaseImpl implements AndroidSQLiteDatabase<SQLiteDatabase> {
	/**
	 * 
	 */
	private SQLiteDatabase db = null;
	/**
	 * 
	 * @param p_oDb
	 */
	public AndroidCipherSQLiteDatabaseImpl(SQLiteDatabase p_oDb) {
		this.db = p_oDb;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#beginTransaction()
	 */
	@Override
	public void beginTransaction() {
		this.db.beginTransaction();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#close()
	 */
	@Override
	public void close() {
		this.db.close();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#endTransaction()
	 */
	@Override
	public void endTransaction() {
		this.db.endTransaction();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#execSQL(java.lang.String)
	 */
	@Override
	public void execSQL(String p_sString) {
		this.db.execSQL(p_sString);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#inTransaction()
	 */
	@Override
	public boolean inTransaction() {
		return this.db.inTransaction();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return this.db.isOpen();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return this.db.isReadOnly();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#setTransactionSuccessful()
	 */
	@Override
	public void setTransactionSuccessful() {
		this.db.setTransactionSuccessful();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#rawQuery(java.lang.String, java.lang.String[])
	 */
	@Override
	public Cursor rawQuery(String p_sSql, String[] p_aRray) {
		return this.db.rawQuery(p_sSql, p_aRray);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#execSQL(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void execSQL(String p_sSql, Object[] p_aRray) {
		this.db.execSQL(p_sSql, p_aRray);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase#getSQLiteDatabase()
	 */
	@Override
	public SQLiteDatabase getSQLiteDatabase() {
		return this.db;
	}
}
