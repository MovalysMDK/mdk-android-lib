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

import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Executor;

import android.content.Context;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
/**
 * <p>Implements "Connection" of JDBC API for android devices.</p>
 */

public class AndroidSQLiteConnection extends AbstractConnection {

	
	/**
	 * Native android database representation.
	 */
	private AndroidSQLiteDatabase<?> androidDatabase;
	
	/**
	 * Open helper
	 */
	private AndroidSQLiteOpenHelper<?> openHelper ;

	/**
	 * Client info.
	 */
	private Properties info;

	/**
	 * Autocommit?
	 */
	private boolean autoCommit;
	
	
	/**
	 * Initializes a connection from an url and properties.
	 * 
	 * @param p_sURL
	 *            An jdbc url.
	 * @param p_oInfo
	 *            Some properties
	 */
	public AndroidSQLiteConnection(String p_sURL, Properties p_oInfo) {
		
		this.info = p_oInfo;
		this.autoCommit = false;

		Context oAndroidContext = (Context) p_oInfo.get(AndroidSQLiteParameter.ANDROID_CONTEXT_PARAMETER);
		Integer iDbVersion = (Integer) p_oInfo.get(AndroidSQLiteParameter.DB_VERSION_PARAMETER);
		
		Boolean bEnabledFK = p_oInfo.get(AndroidSQLiteParameter.DB_DISABLE_FK) == null || p_oInfo.get(AndroidSQLiteParameter.DB_DISABLE_FK).equals(Boolean.FALSE);

		this.openHelper = BeanLoader.getInstance().getBean(SQLiteOpenHelperFactory.class)
				.createSQLiteOpenHelper(oAndroidContext, p_sURL.replaceFirst(AndroidSQLiteDriver.URL_PREFIX, ""), iDbVersion);
		
		this.androidDatabase = openHelper.myGetWritableDatabase(); 
		
		if ( bEnabledFK ) {
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
		if ( this.openHelper != null && this.androidDatabase != null ) {
			((AndroidSQLiteOpenHelper) this.openHelper).myResetDatabase( this.androidDatabase);
		}
	}

	/**
	 * get database obj
	 * @return database obj
	 */
	public AndroidSQLiteDatabase<?> getAndroidDatabase() {
		return this.androidDatabase;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStatement()
	 */
	@Override
	public Statement createStatement() throws SQLException {
		return new AndroidSQLiteStatement(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	@Override
	public PreparedStatement prepareStatement(String p_sQuery) throws SQLException {
		return new AndroidSQLitePreparedStatement(this, p_sQuery);
	}


	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	@Override
	public void setAutoCommit(boolean p_bAutoCommit) throws SQLException {
		if (this.autoCommit != p_bAutoCommit) {
			this.autoCommit = p_bAutoCommit;
			if (this.autoCommit) {
				this.androidDatabase.setTransactionSuccessful();
				this.androidDatabase.endTransaction();
				this.androidDatabase.beginTransaction();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getAutoCommit()
	 */
	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.autoCommit;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#commit()
	 */
	@Override
	public void commit() throws SQLException {
		if (this.autoCommit) {
			throw new SQLException("Impossible to execute \"commit\" when auto commit is activated");
		}

		try {
			this.androidDatabase.setTransactionSuccessful();
		} finally {
			this.androidDatabase.endTransaction();
			this.androidDatabase.beginTransaction();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#rollback()
	 */
	@Override
	public void rollback() throws SQLException {
		if (this.autoCommit) {
			throw new SQLException("Impossible to execute \"rollback\" when auto commit is activated");
		}
		this.androidDatabase.endTransaction();
		this.androidDatabase.beginTransaction();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#close()
	 */
	@Override
	public void close() throws SQLException {
		
		if ( this.androidDatabase.inTransaction()) {
			this.androidDatabase.endTransaction();
		}
		
		this.androidDatabase.close();			
		this.openHelper.myClose();
		this.androidDatabase = null;
		this.openHelper = null ;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return !this.androidDatabase.isOpen();
	}


	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean p_oReadOnly) throws SQLException {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() throws SQLException {
		return this.androidDatabase.isReadOnly();
	}




	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
	@Override
	public PreparedStatement prepareStatement(String p_sSql, String[] p_t_sColumnNames) throws SQLException {
		return new AndroidSQLitePreparedStatement(this, p_sSql);
	}



	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public void setClientInfo(String p_sName, String p_sValue) throws SQLClientInfoException {
		this.info.put(p_sName, p_sValue);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setClientInfo(java.util.Properties)
	 */
	@Override
	public void setClientInfo(Properties p_oProperties) throws SQLClientInfoException {
		this.info = p_oProperties;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getClientInfo(java.lang.String)
	 */
	@Override
	public String getClientInfo(String p_sName) throws SQLException {
		return this.info.getProperty(p_sName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getClientInfo()
	 */
	@Override
	public Properties getClientInfo() throws SQLException {
		return this.info;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PreparedStatement prepareStatement(String p_sSql, int p_iResultSetType,
			int p_iResultSetConcurrency) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback(Savepoint p_oSavepoint) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statement createStatement(int p_iResultSetType,
			int p_iResultSetConcurrency, int p_iResultSetHoldability)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PreparedStatement prepareStatement(String p_sSql, int p_iResultSetType,
			int p_iResultSetConcurrency, int p_iResultSetHoldability)
			throws SQLException {
		throw new UnsupportedOperationException();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statement createStatement(int p_iResultSetType, int p_iResultSetConcurrency)
			throws SQLException {
		throw new UnsupportedOperationException();
	}
}
