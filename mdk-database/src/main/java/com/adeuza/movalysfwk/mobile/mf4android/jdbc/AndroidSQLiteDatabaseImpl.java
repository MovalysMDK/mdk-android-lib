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
import android.database.sqlite.SQLiteDatabase;

/**
 * <p>SQLite Database</p>
 *
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
