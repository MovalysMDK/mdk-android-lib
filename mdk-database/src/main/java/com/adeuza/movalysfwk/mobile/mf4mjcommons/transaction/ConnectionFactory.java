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
import java.sql.SQLException;
import java.util.Properties;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;

/**
 * <p>JDBC Connection Factory</p>
 *
 *
 */
@Scope(ScopePolicy.SINGLETON)
public interface ConnectionFactory {

	/**
	 * Initialize connection factory
	 *
	 * @param p_sJdbcUrl jdbc url
	 * @param p_oInfo a {@link java.util.Properties} object.
	 */
	public void initialize( String p_sJdbcUrl, Properties p_oInfo );
	
	/**
	 * Get a jdbc connection
	 *
	 * @return jdbc connection
	 * @throws java.sql.SQLException if any.
	 */
	public Connection getConnection() throws SQLException;
}
