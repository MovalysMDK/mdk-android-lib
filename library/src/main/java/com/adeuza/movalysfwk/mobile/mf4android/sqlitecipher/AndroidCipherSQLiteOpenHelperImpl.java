package com.adeuza.movalysfwk.mobile.mf4android.sqlitecipher;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.MCipherOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteOpenHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.DBUpdaterService;

/**
 * <p>
 * SQLiteOpenHelper for Android
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author emalespine
 * 
 */

public class AndroidCipherSQLiteOpenHelperImpl extends MCipherOpenHelper
		implements AndroidSQLiteOpenHelper<SQLiteDatabase> {

	/**
	 * Constructor
	 * 
	 * @param p_oContext
	 *            android context
	 * @param p_oName
	 *            database name
	 * @param p_oVersion
	 *            database version
	 */
	public AndroidCipherSQLiteOpenHelperImpl(Context p_oContext,String p_sName, int p_iVersion) {
		super(p_oContext, p_sName, null, p_iVersion);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void myOnOpen(AndroidSQLiteDatabase<SQLiteDatabase> p_oDb) {
		super.onOpen((SQLiteDatabase) p_oDb);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void myOnCreate(AndroidSQLiteDatabase<SQLiteDatabase> p_oSQLiteDatabase) {
		this.onCreate((SQLiteDatabase) p_oSQLiteDatabase);
	}

	@Override
	public void onCreate(SQLiteDatabase p_oSQLiteDatabase) {
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
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase p_oSQLiteDatabase, int p_iOldVersion,
			int p_iNewVersion) {
		Log.d("AndroidSQLiteOpenHelper", "onUpgrade");
		Log.d("AndroidSQLiteOpenHelper", "  old version : " + p_iOldVersion);
		Log.d("AndroidSQLiteOpenHelper", "  new version : " + p_iNewVersion);

		this.resetDatabase(p_oSQLiteDatabase);

		Log.d("AndroidSQLiteOpenHelper", "onUpgrade end");
	}

	/**
	 * @param p_oSQLiteDatabase
	 */
	public void resetDatabase(SQLiteDatabase p_oSQLiteDatabase) {
		Log.d("AndroidSQLiteOpenHelper", "resetDatabase");
		List<String> listTableNames = new ArrayList<>();

		Cursor oCursor = p_oSQLiteDatabase
				.rawQuery("select name from sqlite_master where type='table' and name <> 'sqlite_sequence' and name <> 'android_metadata'",	null);
		try {
			while (oCursor.moveToNext()) {
				listTableNames.add(oCursor.getString(0));
			}
		} finally {
			oCursor.close();
		}

		for (String sTableName : listTableNames) {
			Log.i("AndroidSQLiteOpenHelper", "  drop de la table " + sTableName);
			p_oSQLiteDatabase.execSQL("drop table if exists " + sTableName);
		}

		this.createSchema(p_oSQLiteDatabase);
	}

	/**
	 * @param p_oSQLiteDatabase
	 */
	private void createSchema(SQLiteDatabase p_oSQLiteDatabase) {
		for (String sSqlInstruction : BeanLoader.getInstance().getBean(DBUpdaterService.class).getCreateInstructions()) {
			p_oSQLiteDatabase.execSQL(sSqlInstruction);
		}
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteOpenHelper#myGetWritableDatabase()
	 */
	@Override
	public AndroidSQLiteDatabase<SQLiteDatabase> myGetWritableDatabase() {
		return new AndroidCipherSQLiteDatabaseImpl(
				this.getWritableDatabase(Application.getInstance().getUniqueId()));
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteOpenHelper#myOnUpgrade(com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase, int, int)
	 */
	@Override
	public void myOnUpgrade(AndroidSQLiteDatabase<SQLiteDatabase> p_oSQLiteDatabase,int p_iOldVersion, int p_iNewVersion) {
		this.onUpgrade((SQLiteDatabase) p_oSQLiteDatabase, p_iOldVersion,p_iNewVersion);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteOpenHelper#myResetDatabase(com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase)
	 */
	@Override
	public void myResetDatabase(AndroidSQLiteDatabase<SQLiteDatabase> p_oSQLiteDatabase) {
		this.resetDatabase((SQLiteDatabase) p_oSQLiteDatabase);
	}
}
