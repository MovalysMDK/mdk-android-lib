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
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * <p>Implements "Driver" of JDBC API for android devices.</p>
 *
 */

public class AndroidSQLiteDriver implements Driver {

	/**
	 * Prefix of url.
	 */
	public static final String URL_PREFIX = "jdbc:sqldroid:";

	/**
	 * {@inheritDoc}
	 * @see java.sql.Driver#acceptsURL(java.lang.String)
	 */
	@Override
	public boolean acceptsURL(String p_sURL) throws SQLException {
		return p_sURL != null && p_sURL.startsWith(AndroidSQLiteDriver.URL_PREFIX);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
	 */
	@Override
	public Connection connect(String p_sURL, Properties p_oProperties) throws SQLException {
		return new AndroidSQLiteConnection(p_sURL, p_oProperties);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Driver#getMajorVersion()
	 */
	@Override
	public int getMajorVersion() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Driver#getMinorVersion()
	 */
	@Override
	public int getMinorVersion() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Driver#getPropertyInfo(java.lang.String, java.util.Properties)
	 */
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String p_sUrl, Properties p_oInfo) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Driver#jdbcCompliant()
	 */
	@Override
	public boolean jdbcCompliant() {
		return false;
	}
	
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
