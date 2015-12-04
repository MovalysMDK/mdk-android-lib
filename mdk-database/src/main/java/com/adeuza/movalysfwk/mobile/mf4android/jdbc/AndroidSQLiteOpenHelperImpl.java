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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.DBUpdaterService;

/**
 * <p>
 * SQLiteOpenHelper for Android
 * </p>
 * 
 */
public class AndroidSQLiteOpenHelperImpl extends SQLiteOpenHelper implements
		AndroidSQLiteOpenHelper<SQLiteDatabase> {

	/**
	 * Class name
	 */
	private static final String TAG = "AndroidSQLiteOpenHelper";
	/**
	 * Constructor
	 * 
	 * @param p_oContext  android context
	 * @param p_sName   database name
	 * @param p_iVersion database version
	 */
	public AndroidSQLiteOpenHelperImpl(Context p_oContext, String p_sName,int p_iVersion) {
		super(p_oContext, p_sName, null, p_iVersion);
	}
	/**
	 * {@inheritDoc}
	 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void myOnOpen(AndroidSQLiteDatabase<SQLiteDatabase> p_oDb) {
		super.onOpen(p_oDb.getSQLiteDatabase());
	}
	/**
	 * {@inheritDoc}
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void myOnCreate( AndroidSQLiteDatabase<SQLiteDatabase> p_oSQLiteDatabase) {
		this.onCreate(p_oSQLiteDatabase.getSQLiteDatabase());
	}
	
	/**
	 * to create the schema
	 * @param p_oSQLiteDatabase db instance
	 */
	@Override
	public void onCreate(SQLiteDatabase p_oSQLiteDatabase) {
		Log.d(TAG, "onCreate: " + p_oSQLiteDatabase.inTransaction());
		this.createSchema(p_oSQLiteDatabase);
	}
	/**
	 * {@inheritDoc}
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase p_oSQLiteDatabase, int p_iOldVersion,
			int p_iNewVersion) {
		Log.d(TAG, "onUpgrade");
		Log.d(TAG, "  old version : " + p_iOldVersion);
		Log.d(TAG, "  new version : " + p_iNewVersion);

		this.resetDatabase(p_oSQLiteDatabase);

		Log.d(TAG, "onUpgrade end");
	}

	/**
	 * to reset db instance
	 * @param p_oSQLiteDatabase db instance
	 */
	public void resetDatabase(SQLiteDatabase p_oSQLiteDatabase) {
		Log.d(TAG, "resetDatabase");
		List<String> listTableNames = new ArrayList<>();

		Cursor oCursor = p_oSQLiteDatabase.rawQuery(
						"select name from sqlite_master where type='table' and name <> 'sqlite_sequence' and name <> 'android_metadata'",
						null);
		try {
			while (oCursor.moveToNext()) {
				listTableNames.add(oCursor.getString(0));
			}
		} finally {
			oCursor.close();
		}

		for (String sTableName : listTableNames) {
			Log.i(TAG, "  drop de la table " + sTableName);
			p_oSQLiteDatabase.execSQL("drop table if exists " + sTableName);
		}

		this.createSchema(p_oSQLiteDatabase);
	}

	/**
	 * @param p_oSQLiteDatabase db instance
	 */
	private void createSchema(SQLiteDatabase p_oSQLiteDatabase) {
		
		// Create database structure
		for (String sSqlInstruction : BeanLoader.getInstance().getBean(DBUpdaterService.class).getCreateInstructions()) {
			p_oSQLiteDatabase.execSQL(sSqlInstruction);
		}

		// Insert initial data
		p_oSQLiteDatabase.beginTransaction();
		try {
			for (String sSqlInstruction : BeanLoader.getInstance().getBean(DBUpdaterService.class).getCreateDataInstructions()) {
				p_oSQLiteDatabase.execSQL(sSqlInstruction);
			}
			p_oSQLiteDatabase.setTransactionSuccessful();
		} finally {
			p_oSQLiteDatabase.endTransaction();
		}
	}
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public AndroidSQLiteDatabase myGetWritableDatabase() {
		return new AndroidSQLiteDatabaseImpl(this.getWritableDatabase());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void myOnUpgrade(AndroidSQLiteDatabase p_oSQLiteDatabase,	int p_iOldVersion, int p_iNewVersion) {
		this.onUpgrade((SQLiteDatabase) p_oSQLiteDatabase.getSQLiteDatabase(), p_iOldVersion,
				p_iNewVersion);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void myResetDatabase(AndroidSQLiteDatabase p_oSQLiteDatabase) {
		this.resetDatabase((SQLiteDatabase)p_oSQLiteDatabase.getSQLiteDatabase());
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteOpenHelper#myClose()
	 */
	@Override
	public void myClose() {
		this.close();
	}
}
