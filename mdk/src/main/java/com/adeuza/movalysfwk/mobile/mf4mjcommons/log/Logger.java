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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.log;

/**
 *
 * <p>Interface of Logger. Android have its own implementation of logger, to have a common foundation,
 * we need to encapsulate the different implementations.</p>
 *
 *
 */
public interface Logger {

	/** ERROR level */
	public static int ERROR = 0;
	/** INFO level */
	public static int INFO = 1;
	/** DEBUG level */
	public static int DEBUG = 2;
	
	/**
	 * Set the log level.
	 * The possible value is :
	 * <li>ERROR</li>
	 * <li>INFO</li>
	 * <li>DEBUG</li>
	 *
	 * @param p_iLevel the log level to set.
	 */
	public void setLevel(int p_iLevel);
	
	/**
	 * Log an error message.
	 *
	 * @param p_sId error id
	 * @param p_sMessage error message
	 */
	public void error(String p_sId, String p_sMessage);
	
	/**
	 * Log an error message.
	 *
	 * @param p_sId error id
	 * @param p_sMessage error message
	 * @param p_oException exception that we want to log
	 */
	public void error(String p_sId, String p_sMessage, Throwable p_oException);
	
	/**
	 * Log an information message.
	 *
	 * @param p_sId error id
	 * @param p_sMessage error message
	 */
	public void info(String p_sId, String p_sMessage);
	
	/**
	 * Log an information message.
	 *
	 * @param p_sId error id
	 * @param p_sMessage error message
	 */
	public void debug(String p_sId, String p_sMessage);
	
	/**
	 * indicates if the logger is in error mode
	 *
	 * @return true if the logger is in error mode
	 */
	public boolean isInErrorLevel();
	
	/**
	 * indicates if the logger is in information mode
	 *
	 * @return true if the logger is in information mode
	 */
	public boolean isInfoLevel();
	
	/**
	 * indicates if the logger is in debug mode
	 *
	 * @return true if the logger is in debug mode
	 */
	public boolean isDebugLevel();
}
