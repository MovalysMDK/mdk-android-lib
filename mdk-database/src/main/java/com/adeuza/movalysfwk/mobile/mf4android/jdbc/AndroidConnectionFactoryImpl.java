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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.ConnectionFactory;

/**
 * <p>Factory of "Connection"  .</p>
 */
public class AndroidConnectionFactoryImpl implements ConnectionFactory {
	
	/**
	 * Database name
	 */
	private String databaseName ;
	
	/**
	 * Database version
	 */
	private int databaseVersion;

	@Override
	public void initialize(String p_sDatabaseName, int p_iDatabaseVersion ) {
		this.databaseName = p_sDatabaseName;
		this.databaseVersion = p_iDatabaseVersion ;
	}

	@Override
	public AndroidSQLiteConnection getConnection( Context p_oAndroidContext ) throws DaoException {
		return new AndroidSQLiteConnection(databaseName, databaseVersion, true, p_oAndroidContext);
	}
	
	@Override
	public AndroidSQLiteConnection getConnectionDisableFK( Context p_oAndroidContext ) throws DaoException {
		return new AndroidSQLiteConnection(databaseName, databaseVersion, false, p_oAndroidContext);
	}
}
