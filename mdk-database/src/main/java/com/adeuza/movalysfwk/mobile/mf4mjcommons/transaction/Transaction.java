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

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;

/**
 * <p>Transaction</p>
 *
 *
 */
@Scope(ScopePolicy.PROTOTYPE)
public interface Transaction {

	/**
	 * Get the jdbc connection
	 *
	 * @return jdbc connection
	 * @throws java.sql.SQLException sql exception
	 */
	public Connection getConnection() throws SQLException;
	
	/**
	 * Commit the transaction
	 *
	 * @throws java.sql.SQLException sql exception
	 */
	public void commit() throws SQLException;
	
	/**
	 * Rollback the transaction
	 *
	 * @throws java.sql.SQLException sql exception
	 */
	public void rollback() throws SQLException;
	
	/**
	 * Release the connection
	 *
	 * @throws java.sql.SQLException sql exception
	 */
	public void release() throws SQLException;
	
	/**
	 * True if transaction has a connection
	 *
	 * @return true if transaction has a connection
	 */
	public boolean hasConnection();
	
}
