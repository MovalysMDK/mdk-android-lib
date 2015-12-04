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

import android.content.Context;

import com.adeuza.movalysfwk.mobile.mf4android.database.sqlite.MDKSQLiteOpenHelperHolder;
import com.adeuza.movalysfwk.mobile.mf4android.database.sqlite.MDKSQLiteStatement;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;

/**
 * <p>Implements "Connection" of JDBC API for android devices.</p>
 */
public class AndroidSQLiteConnection {

	/**
	 * Native android database representation.
	 */
	private AndroidSQLiteDatabase<?> androidDatabase;
	
	/**
	 * Initializes a connection from an url and properties.
	 * 
	 * @param p_sName database name
	 * @param p_iDatabaseVersion database version
	 * @param p_bForeignKeyEnabled foreign key enabled
	 * @param p_oAndroidContext android context
	 */
	public AndroidSQLiteConnection(String p_sDatabaseName, int p_iDatabaseVersion, boolean p_bForeignKeyEnabled, Context p_oAndroidContext ) {
		
		this.androidDatabase = MDKSQLiteOpenHelperHolder.getInstance().openDatabase(p_oAndroidContext, p_sDatabaseName, p_iDatabaseVersion);
		
		if ( p_bForeignKeyEnabled ) {
			this.androidDatabase.execSQL("PRAGMA foreign_keys = ON");
		}
		else {
			this.androidDatabase.execSQL("PRAGMA foreign_keys = OFF");
		}
		
		this.androidDatabase.beginTransaction();
	}
	
	/**
	 * Removes all objects in databases
	 */
	public void resetDatabase() {
		MDKSQLiteOpenHelperHolder.getInstance().resetDatabase();
	}

	/**
	 * get database obj
	 * @return database obj
	 */
	public AndroidSQLiteDatabase<?> getAndroidDatabase() {
		return this.androidDatabase;
	}

	//FIXME: javadoc
	public AndroidSQLiteStatement createStatement() throws DaoException {
		return new AndroidSQLiteStatement(this);
	}

	//FIXME: javadoc
	public AndroidSQLitePreparedStatement prepareStatement(String p_sQuery) throws DaoException {
		AndroidSQLitePreparedStatement r_oPreparedStatement = new AndroidSQLitePreparedStatement(this, p_sQuery);
		if ( !r_oPreparedStatement.isSelect()) {
			beginTransactionConnection();
		}
		return r_oPreparedStatement;
	}
	
	/**
	 * Compile statement.
	 * @param p_sQuery MDKSQLiteStatement
	 * @return compiled statement.
	 */
	public MDKSQLiteStatement compileStatement(String p_sQuery) {
		return this.androidDatabase.compileStatement(p_sQuery);
	}

	//FIXME: javadoc
	//public void setAutoCommit(boolean p_bAutoCommit) throws DaoException {
	//}

	//FIXME: javadoc
	//public boolean getAutoCommit() throws DaoException {
	//	return this.autoCommit;
	//}

	//FIXME: javadoc
	public void commit() throws DaoException {
		try {
			this.androidDatabase.setTransactionSuccessful();
		} finally {
			this.androidDatabase.endTransaction();
		}
	}

	//FIXME: javadoc
	public void rollback() throws DaoException {
		if (this.androidDatabase.inTransaction()) {
			this.androidDatabase.endTransaction();
		}
	}

	//FIXME: javadoc
	public void close() throws DaoException {
		
		if ( this.androidDatabase.inTransaction()) {
			this.androidDatabase.endTransaction();
		}
				
		MDKSQLiteOpenHelperHolder.getInstance().closeDatabase();
		
		this.androidDatabase = null;
	}

	//FIXME: javadoc
	public boolean isClosed() throws DaoException {
		return !this.androidDatabase.isOpen();
	}

	//FIXME: javadoc
	public boolean isReadOnly() throws DaoException {
		return this.androidDatabase.isReadOnly();
	}
	
	public void beginTransactionConnection() {
		if (!this.androidDatabase.inTransaction()) {
			this.androidDatabase.beginTransaction();
		}
	}
}
