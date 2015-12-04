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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteConnection;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLitePreparedStatement;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.io.IOUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ServiceException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;

/**
 * <p>
 * Database Updater Service Implementation
 * </p>
 *
 */
public class DBUpdaterServiceImpl implements DBUpdaterService {

	/**
	 * DDL Sql Instruction to create the database
	 * Structure only (table, index, constraints)
	 */
	private List<String> createInstructions;

	/**
	 * Data Sql Instruction to create the database
	 * Data only (insert, update, ...)
	 */
	private List<String> createDataInstructions;
	
	/**
	 * Sql Instruction to drop the database
	 */
	private List<String> dropInstructions;

	/**
	 * Sql Instruction to drop the database
	 */
	private List<String> dropDataInstructions;
	
	/**
	 * Initialize DBUpdaterService
	 *
	 * @param p_oCreateIs creation sql script inputStream
	 * @param p_oDropIs drop sql script inputStream
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ServiceException if any.
	 */
	public void addCreateDropScript(InputStream p_oCreateIs, InputStream p_oDropIs) throws ServiceException {
		try {
			if (this.createInstructions == null){
				this.createInstructions = new ArrayList<String>();
			}
			String sCreateScript = IOUtils.toString(p_oCreateIs);
			for (String sInstruction : extractSqlInstructions(sCreateScript)) {
				this.createInstructions.add(sInstruction);
			}

			if (this.dropInstructions == null){
				this.dropInstructions = new ArrayList<String>();
			}
			if (p_oDropIs != null) {
				String sDropScript = IOUtils.toString(p_oDropIs);
				for (String sInstruction : extractSqlInstructions(sDropScript)) {
					this.dropInstructions.add(sInstruction);
				}
			}
		} catch (IOException oIOException) {
			throw new ServiceException("DBUpdaterService initialisation failed.", oIOException);
		}
	}
	
	/**
	 * <p>addDataScript.</p>
	 *
	 * @param p_oCreateIs a {@link java.io.InputStream} object.
	 * @param p_oDropIs a {@link java.io.InputStream} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ServiceException if any.
	 */
	public void addDataScript(InputStream p_oCreateIs, InputStream p_oDropIs) throws ServiceException {
		try {
			if (this.createDataInstructions == null){
				this.createDataInstructions = new ArrayList<String>();
			}
			
			String sCreateScript = IOUtils.toString(p_oCreateIs);
			for (String sInstruction : extractSqlInstructions(sCreateScript)) {
				this.createDataInstructions.add(sInstruction);
			}
			
			if (this.dropDataInstructions == null){
				this.dropDataInstructions = new ArrayList<String>();
			}
			
			if (p_oDropIs != null) {
				String sDropScript = IOUtils.toString(p_oDropIs);
				for (String sInstruction : extractSqlInstructions(sDropScript)) {
					this.dropDataInstructions.add(sInstruction);
				}
			}
			
		} catch (IOException oIOException) {
			throw new ServiceException("DBUpdaterService initialisation failed.", oIOException);
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	//Attention Android ne passe pas par là pour créer la base ... => voir le code dans AndroidSQLiteOpenHelper
	public void createDatabase(MContext p_oContext) throws ServiceException {
		executeInstructions(p_oContext, this.createInstructions);
	}

	/**
	 * Executes a list of instructions
	 * @param p_oContext context to use
	 * @param p_oInstructions instructions list
	 * @throws ServiceException if any
	 */
	private void executeInstructions(MContext p_oContext, List<String> p_oInstructions) throws ServiceException {
		ItfTransactionalContext oTransactionalContext = ((ItfTransactionalContext)p_oContext);
		try {
			AndroidSQLiteConnection oConnection = oTransactionalContext.getConnection();
			AndroidSQLitePreparedStatement oStatement = null;
			for (String sSqlInstruction : p_oInstructions) {
				oStatement = oConnection.prepareStatement(sSqlInstruction);
				try {
					oStatement.execute();
				} finally {
					oStatement.close();
				}
			}
			oTransactionalContext.commit();
		} catch (DaoException oDaoException) {
			try {
				oTransactionalContext.rollback();
			} catch (DaoException oRollbackException) {
				throw new ServiceException("Database creation failed and rollback failed", oRollbackException);
			}
			throw new ServiceException("Database creation failed", oDaoException);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Retourne l'objet createInstructions
	 */
	@Override
	public List<String> getCreateInstructions() {
		return this.createInstructions;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Retourne l'objet dropInstructions
	 */
	@Override	
	public List<String> getDropInstructions() {
		return this.dropInstructions;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.DBUpdaterService#dropDatabase()
	 */
	@Override
	public void dropDatabase(MContext p_oContext) throws ServiceException {
		executeInstructions(p_oContext, this.dropInstructions);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.DBUpdaterService#upgradeDatabase()
	 */
	@Override
	public void upgradeDatabase(MContext p_oContext) throws ServiceException {
		dropDatabase(p_oContext);
		createDatabase(p_oContext);
	}

	/**
	 * Extraction sql instructions from sql script
	 * 
	 * @param p_sSqlScript sql script
	 * @return list of sql instructions
	 */
	private List<String> extractSqlInstructions(String p_sSqlScript) {
		List<String> r_listSqlInstructions = new ArrayList<String>();
		StringBuilder sCurrentRequest = new StringBuilder();
		boolean bInQuote = false;
		if (p_sSqlScript != null && p_sSqlScript.length() > 0) {

			int iScriptLength = p_sSqlScript.length();

			for (int iIndex = 0; iIndex < iScriptLength; iIndex++) {

				char cCurrentChar = p_sSqlScript.charAt(iIndex);
				if (cCurrentChar == ';') {
					if (bInQuote) {
						sCurrentRequest.append(cCurrentChar);
					} else {
						// fin de la requete en cours
						r_listSqlInstructions.add(sCurrentRequest.toString());
						sCurrentRequest = new StringBuilder();
						bInQuote = false;
					}
				} else {
					if (cCurrentChar == '\'') {
						// double quote case: nothing todo
						bInQuote = !bInQuote;
						sCurrentRequest.append(cCurrentChar);
					} else {
						// cariage return case: ignore
						if (cCurrentChar == '\r' && iIndex + 1 < iScriptLength && p_sSqlScript.charAt(iIndex + 1) == '\n') {
							iIndex++;
						} else {
							sCurrentRequest.append(cCurrentChar);
						}
					}
				}
			}
		}
		return r_listSqlInstructions;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getCreateDataInstructions() {
		return this.createDataInstructions;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getDropDataInstructions() {
		return this.dropDataInstructions;
	}
}
