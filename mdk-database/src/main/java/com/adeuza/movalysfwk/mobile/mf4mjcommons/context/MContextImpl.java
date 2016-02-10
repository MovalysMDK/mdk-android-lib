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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.context;

import java.sql.SQLException;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.Transaction;

/**
 * <p>Transactional Context Implementation</p>
 *
 *
 */
public class MContextImpl extends BaseContextImpl implements ItfTransactionalContext {

	/**
	 * Transaction
	 */
	private Transaction transaction ;

	/**
	 * Constructor
	 */
	public MContextImpl() {
		super();
		this.transaction	= BeanLoader.getInstance().getBean(Transaction.class);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext#getTransaction()
	 */
	@Override
	public Transaction getTransaction() {
		return this.transaction;
	}
	
	/** {@inheritDoc} */
	@Override
	public void beginTransaction() {
		this.use = true;
		//lorsque l'on commence un transaction, il ne doit pas y avoir encore de connexion sql
		if (this.transaction.hasConnection()) {
			throw new RuntimeException("Transaction has already a connection before to be started");
		}
	}
	
	/**
	 * Close the database
	 */
	@Override
	public void close() {
		try {
			this.getTransaction().release();
		}
		catch (SQLException e) {
			throw new RuntimeException("Transaction cannot release the connection");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.MContext.fwk.mobile.javacommons.application.MMContext#endingTransaction()
	 */
	@Override
	public void endTransaction(){
		try {
			if (!this.getMessages().hasErrors()) {
				this.getTransaction().commit();
			}
			else {
				this.getTransaction().rollback();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Transaction cannot commit");
		}
		this.use = false;
	}

}
