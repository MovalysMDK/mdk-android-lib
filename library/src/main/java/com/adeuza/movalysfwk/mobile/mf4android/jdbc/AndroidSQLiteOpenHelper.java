package com.adeuza.movalysfwk.mobile.mf4android.jdbc;


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
 * @param <T> type of database
 * @author emalespine
 *	@param <T> type of database (eg. SQLiteDatabase)
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
