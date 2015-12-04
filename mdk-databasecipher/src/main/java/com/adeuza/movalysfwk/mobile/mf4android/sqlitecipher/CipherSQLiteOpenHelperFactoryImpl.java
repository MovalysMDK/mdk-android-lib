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
package com.adeuza.movalysfwk.mobile.mf4android.sqlitecipher;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteOpenHelper;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.SQLiteOpenHelperFactory;


/**
 * <p>SQLiteOpenHelper Factory</p>
 *
 *
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
