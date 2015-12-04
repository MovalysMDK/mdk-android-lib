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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction;

import android.content.Context;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteConnection;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;

/**
 * <p>JDBC Connection Factory.</p>
 */
@Scope(ScopePolicy.SINGLETON)
public interface ConnectionFactory {

	/**
	 * Initialize connection factory
	 *
	 * @param p_sDatabaseName database name
	 * @param p_iDatabaseVersion database version.
	 */
	public void initialize(String p_sDatabaseName, int p_iDatabaseVersion );
	
	/**
	 * Get a jdbc connection
	 *
	 * @return AndroidSQLiteConnection.
	 * @throws DaoException if any.
	 */
	public AndroidSQLiteConnection getConnection( Context p_oAndroidContext ) throws DaoException;
	
	/**
	 * Get a jdbc connection with foreign key disabled.
	 * @param p_oAndroidContext android context
	 * @return AndroidSQLiteConnection
	 * @throws DaoException if any.
	 */
	public AndroidSQLiteConnection getConnectionDisableFK( Context p_oAndroidContext ) throws DaoException;
}
