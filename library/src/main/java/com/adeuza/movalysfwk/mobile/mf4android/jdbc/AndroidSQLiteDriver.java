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
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
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

	/**
	 * {@inheritDoc}
	 */
	//@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
