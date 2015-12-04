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
package com.adeuza.movalysfwk.mobile.mf4android.sqlitecipher;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.adeuza.movalysfwk.mobile.mf4android.database.sqlite.MDKSQLiteStatement;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase;

/**
 * This class is used to cipher a database on Android
 *
 */
public class AndroidCipherSQLiteDatabaseImpl implements AndroidSQLiteDatabase<SQLiteDatabase> {
	/**
	 * {@link SQLiteDatabase} object
	 */
	private SQLiteDatabase db = null;
	
	/**
	 * Constructor
	 * @param p_oDb database to initialize the class on
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
	
	@Override
	public SQLiteDatabase getSQLiteDatabase() {
		return this.db;
	}

	@Override
	public MDKSQLiteStatement compileStatement(String p_sQuery) {
		return new MDKCipherSQLiteStatement(this.db.compileStatement(p_sQuery));
	}
}
