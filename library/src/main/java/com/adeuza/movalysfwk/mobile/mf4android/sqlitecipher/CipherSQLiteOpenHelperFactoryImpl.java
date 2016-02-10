package com.adeuza.movalysfwk.mobile.mf4android.sqlitecipher;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteOpenHelper;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.SQLiteOpenHelperFactory;


/**
 * <p>SQLiteOpenHelper Factory</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class CipherSQLiteOpenHelperFactoryImpl implements SQLiteOpenHelperFactory {

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.fwk.jdbc.SQLiteOpenHelperFactory#createSQLiteOpenHelper(android.content.Context, java.lang.String, int)
	 */
	@Override
	public AndroidSQLiteOpenHelper<SQLiteDatabase> createSQLiteOpenHelper(Context p_oContext, String p_sName, int p_iVersion) {
		SQLiteDatabase.loadLibs(p_oContext);
		return new AndroidCipherSQLiteOpenHelperImpl(p_oContext, p_sName, p_iVersion);
	}
}
