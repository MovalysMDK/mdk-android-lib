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

public enum AndroidSQLiteParameter {
	/**
	 * Android Context Parameter
	 */
	ANDROID_CONTEXT_PARAMETER("androidContext"),
	
	/**
	 * Database Version Parameter
	 */
	DB_VERSION_PARAMETER("dbVersion"),
	
	/**
	 * Database Version Parameter
	 */
	DB_DISABLE_FK("disableFk");
	
	/**
	 * value
	 */
    public final String value;
    
    /**
     * constructor
     * @param p_sVal value
     */
    AndroidSQLiteParameter(String p_sVal){
    	this.value = p_sVal;
    }
}
