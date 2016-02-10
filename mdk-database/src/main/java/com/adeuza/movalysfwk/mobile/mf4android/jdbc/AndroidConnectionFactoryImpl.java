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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.ConnectionFactoryImpl;

/**
 * <p>Factory of "Connection"  .</p>
 */
public class AndroidConnectionFactoryImpl extends ConnectionFactoryImpl {
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.ConnectionFactory#getConnection()
	 */
	public Connection getConnectionDisableFK() throws SQLException {
		
		Properties oProperties = (Properties) getPropertiesInfo().clone();
		oProperties.put(AndroidSQLiteParameter.DB_DISABLE_FK, Boolean.TRUE);
		return DriverManager.getConnection(getJdbcUrl(), oProperties);
	}
}
