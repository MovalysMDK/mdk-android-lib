package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.ConnectionFactoryImpl;

/**
 * <p>Factory of "Connection"  .</p>
 *
 * <p>Copyright (c) 2014</p>
 * <p>Company: Sopra</p>
 *
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
