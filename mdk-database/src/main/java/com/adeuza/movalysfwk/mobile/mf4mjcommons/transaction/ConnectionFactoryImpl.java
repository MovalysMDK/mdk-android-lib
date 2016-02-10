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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <p>Connection Factory Implementation</p>
 *
 *
 */
public class ConnectionFactoryImpl implements ConnectionFactory {

	/**
	 * JDBC Url 
	 */
	private String jdbcUrl ;
	
	/**
	 * Properties Info
	 */
	private Properties propertiesInfo ;
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.ConnectionFactory#initialize(java.lang.String)
	 */
	@Override
	public void initialize(String p_sJdbcUrl, Properties p_oPropertiesInfo ) {
		this.jdbcUrl = p_sJdbcUrl;
		this.propertiesInfo = p_oPropertiesInfo ;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.ConnectionFactory#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(this.jdbcUrl, this.propertiesInfo);
	}

	/**
	 * <p>Getter for the field <code>propertiesInfo</code>.</p>
	 *
	 * @return a {@link java.util.Properties} object.
	 */
	protected Properties getPropertiesInfo() {
		return propertiesInfo;
	}

	/**
	 * <p>Getter for the field <code>jdbcUrl</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}
}
