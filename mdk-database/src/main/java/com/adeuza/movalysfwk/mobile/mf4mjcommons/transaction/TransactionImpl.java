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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * <p>Transaction Implementation</p>
 *
 *
 */
public class TransactionImpl implements Transaction {

	/**
	 * Sql Connection
	 */
	private Connection connection ;

	
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.Transaction#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		if ( this.connection == null ) {
			this.connection = BeanLoader.getInstance().getBean(ConnectionFactory.class).getConnection();
			this.connection.setAutoCommit(false);
		}
		return connection;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.Transaction#commit()
	 */
	@Override
	public void commit() throws SQLException {
		if ( this.connection != null ) {
			this.connection.commit();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.Transaction#rollback()
	 */
	@Override
	public void rollback() throws SQLException {
		if ( this.connection != null ) {
			this.connection.rollback();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.Transaction#release()
	 */
	@Override
	public void release() throws SQLException {
		if ( this.connection != null ) {
			this.connection.close();
		}
		this.connection = null ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.Transaction#hasConnection()
	 */
	@Override
	public boolean hasConnection() {
		return this.connection!=null;
	}
}
