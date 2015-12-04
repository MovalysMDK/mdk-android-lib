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
package com.adeuza.movalysfwk.mobile.mf4android.database.sqlite;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDatabase;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteOpenHelper;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.SQLiteOpenHelperFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * Holder of the instance of the OpenHelper.
 *
 */
public class MDKSQLiteOpenHelperHolder {
	
    /**
     * Singleton instance.
     */
    private static MDKSQLiteOpenHelperHolder instance;
	
	/**
	 * Native android database representation.
	 */
	private AndroidSQLiteDatabase<?> androidDatabase;
	
	/**
	 * Open helper
	 */
	private AndroidSQLiteOpenHelper<?> openHelper ;

	/**
	 * Current usage count.
	 */
	private AtomicInteger openCounter = new AtomicInteger();
    
    /**
     * Return singleton instance of MDKSQLiteOpenHelperHolder
     * @return singleton instance of MDKSQLiteOpenHelperHolder
     */
    public static synchronized MDKSQLiteOpenHelperHolder getInstance() {
        if (instance == null) {
          	instance = new MDKSQLiteOpenHelperHolder();
        }
        return instance;
    }

    /**
     * Open the database.
     * <p>Return the current database if the database is already opened.</p>
     * @param p_oContext context
     * @param p_sName database name
     * @param p_iVersion database version
     * @return sqlite database.
     */
    public synchronized AndroidSQLiteDatabase<?> openDatabase(Context p_oContext, String p_sName, int p_iVersion) {
        if(openCounter.incrementAndGet() == 1) {
        	this.openHelper = BeanLoader.getInstance().getBean(SQLiteOpenHelperFactory.class)
        			.createSQLiteOpenHelper(p_oContext, p_sName, p_iVersion);

        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        		((SQLiteOpenHelper)this.openHelper).setWriteAheadLoggingEnabled(true);
        	} else {
        		this.androidDatabase.execSQL("PRAGMA journal_mode = wal");
        	}
    		this.androidDatabase = openHelper.myGetWritableDatabase();
        }
        return this.androidDatabase;
    }

    /**
     * Reset the database.
     */
    public synchronized void resetDatabase() {
    	if ( this.openHelper != null && this.androidDatabase != null ) {
			((AndroidSQLiteOpenHelper) this.openHelper).myResetDatabase( this.androidDatabase);
		}
    }
    
    /**
     * Close database.
     * <p>Close only if anybody else is using the database.</p>
     */
    public synchronized void closeDatabase() {
        if ( openCounter.decrementAndGet() == 0 ) {
        	this.openHelper.myClose();
        	this.androidDatabase = null;
        	this.openHelper = null;
        }
    }
}
