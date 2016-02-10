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


/**
 * <p>
 * SQLiteOpenHelper for Android
 * </p>
 * 
 * @param <T> type of database
 * @param <T> type of database (eg. SQLiteDatabase)
 */

public interface AndroidSQLiteOpenHelper<T>  {


	/**
	 * {@inheritDoc}
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
	 */
	public void myOnOpen(AndroidSQLiteDatabase<T> p_oDb);

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	public void myOnCreate(AndroidSQLiteDatabase<T> p_oSQLiteDatabase);

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	public void myOnUpgrade(AndroidSQLiteDatabase<T> p_oSQLiteDatabase, int p_iOldVersion, int p_iNewVersion);
	
	/**
	 * to reset db instance
	 * @param p_oSQLiteDatabase database instance
	 */
	public void myResetDatabase( AndroidSQLiteDatabase<T> p_oSQLiteDatabase );

	/**
	 * to get a writable db instance
	 * @return db instance
	 */
	public AndroidSQLiteDatabase<T> myGetWritableDatabase();

	// !! ATTENTION: Android crée une connexion en lecture/écriture même si on demande une connexion uniquement en lecture.
	//public AndroidSQLiteDatabase<T> _getReadableDatabase();

	/**
	 * to close db instance
	 * 
	 */
	public void myClose();
}
