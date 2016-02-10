package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import android.content.Context;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;

/**
 * <p>SQLite OpenHelper</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
@Scope(ScopePolicy.SINGLETON)
public interface SQLiteOpenHelperFactory {

	/**
	 * Create SQLiteOpenHelper
	 * @param p_oContext android context
	 * @param p_sName database name
	 * @param p_iVersion database version
	 * @return SQLiteOpenHelper instance
	 */
	public AndroidSQLiteOpenHelper<?> createSQLiteOpenHelper(Context p_oContext, String p_sName, int p_iVersion);
}
