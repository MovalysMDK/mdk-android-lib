package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import android.content.Context;

/**
 * <p>SQLiteOpenHelper Factory</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class SQLiteOpenHelperFactoryImpl implements SQLiteOpenHelperFactory {

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.fwk.jdbc.SQLiteOpenHelperFactory#createSQLiteOpenHelper(android.content.Context, java.lang.String, int)
	 */
	@Override
	public AndroidSQLiteOpenHelper<?> createSQLiteOpenHelper(Context p_oContext, String p_sName, int p_iVersion) {
		return new AndroidSQLiteOpenHelperImpl(p_oContext, p_sName, p_iVersion);
	}
}
