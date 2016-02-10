/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sqlcipher.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * A helper class to manage database creation and version management.
 * You create a subclass implementing {@link #onCreate}, {@link #onUpgrade} and
 * optionally {@link #onOpen}, and this class takes care of opening the database
 * if it exists, creating it if it does not, and upgrading it as necessary.
 * Transactions are used to make sure the database is always in a sensible state.
 * <p>For an example, see the NotePadProvider class in the NotePad sample application,
 * in the <em>samples/</em> directory of the SDK.</p>
 */
public abstract class MCipherOpenHelper {
    private static final String TAG = MCipherOpenHelper.class.getSimpleName();

    private final Context mContext;
    private final String mName;
    private final CursorFactory mFactory;
    private final int mNewVersion;

    private SQLiteDatabase mDatabaseRO, mDatabaseRW = null;
    private boolean mIsInitializing = false;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * The database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     *
     * @param p_oContext to use to open or create the database
     * @param p_sName of the database file, or null for an in-memory database
     * @param p_oFactory to use for creating cursor objects, or null for the default
     * @param p_iVersion number of the database (starting at 1); if the database is older,
     *     {@link #onUpgrade} will be used to upgrade the database
     */
    public MCipherOpenHelper(Context p_oContext, String p_sName, CursorFactory p_oFactory, int p_iVersion) {
        if (p_iVersion < 1) throw new IllegalArgumentException("Version must be >= 1, was " + p_iVersion);

        mContext = p_oContext;
        mName = p_sName;
        mFactory = p_oFactory;
        mNewVersion = p_iVersion;
    }

    /**
     * Create and/or open a database that will be used for reading and writing.
     * Once opened successfully, the database is cached, so you can call this
     * method every time you need to write to the database.  Make sure to call
     * {@link #close} when you no longer need it.
     *
     * <p>Errors such as bad permissions or a full disk may cause this operation
     * to fail, but future attempts may succeed if the problem is fixed.</p>
     *
     * @throws SQLiteException if the database cannot be opened for writing
     * @return a read/write database object valid until {@link #close} is called
     */
    public synchronized SQLiteDatabase getWritableDatabase(String p_sPassword) {
        if (mDatabaseRW != null && mDatabaseRW.isOpen()) {
            return mDatabaseRW;  // The database is already open for business
        }

        if (mIsInitializing) {
            throw new IllegalStateException("getWritableDatabase called recursively");
        }

        // If we have a read-only database open, someone could be using it
        // (though they shouldn't), which would cause a lock to be held on
        // the file, and our attempts to open the database read-write would
        // fail waiting for the file lock.  To prevent that, we acquire the
        // lock on the read-only database, which shuts out other users.

        boolean bSuccess = false;
        SQLiteDatabase oDatabase = null;
        if (mDatabaseRW != null) mDatabaseRW.lock();
        try {
            mIsInitializing = true;
            if (mName == null) {
                oDatabase = SQLiteDatabase.create(null, p_sPassword);
            } else {
                String sPath = mContext.getDatabasePath(mName).getPath();
                
                File oDbPathFile = new File (sPath);
                if (!oDbPathFile.exists()){
                	oDbPathFile.getParentFile().mkdirs();
                }
                	
                oDatabase = SQLiteDatabase.openOrCreateDatabase(sPath, p_sPassword, mFactory);
                
             //   db = SQLiteDatabase.openDatabase(path,mFactory , SQLiteDatabase.OPEN_READWRITE);
                //db = mContext.openOrCreateDatabase(mName, 0, mFactory);
            }           

            int iVersion = oDatabase.getVersion();
            if (iVersion != mNewVersion) {
            	
                if (iVersion == 0) {
                    onCreate(oDatabase);
                } else {
                    onUpgrade(oDatabase, iVersion, mNewVersion);
                }
            	
                oDatabase.beginTransaction();
                try {
                    oDatabase.setVersion(mNewVersion);
                    oDatabase.setTransactionSuccessful();
                } finally {
                    oDatabase.endTransaction();
                }
            }
            
            oDatabase.close();
            
            copyDatabase();
            
            String sPath = mContext.getDatabasePath(mName).getPath();
            oDatabase = SQLiteDatabase.openOrCreateDatabase(sPath, p_sPassword, mFactory);

            onOpen(oDatabase);
            bSuccess = true;
            return oDatabase;
        } finally {
            mIsInitializing = false;
            if (bSuccess) {
                if (mDatabaseRW != null) {
                    try { 
                    	mDatabaseRW.close(); 
                    } 
                    catch (Exception e) { 
                    	Log.d(TAG,"exception close databases", e);
                    }
                    mDatabaseRW.unlock();
                }
                mDatabaseRW = oDatabase;
            } else {
                if (mDatabaseRW != null) {
                	mDatabaseRW.unlock();
                }
                if (oDatabase != null) {
                	oDatabase.close();
                }
            }
        }
    }

    private void copyDatabase() {
    	try {
    		File oDbFile = new File(mName);
	
    		File oExportDir = new File(Environment.getExternalStorageDirectory(), "");
    		if (!oExportDir.exists()) {
    			oExportDir.mkdirs();
    		}
    		File oFile = new File(oExportDir, oDbFile.getName());
	        oFile.createNewFile();
	        this.copyFile(oDbFile, oFile);
    	} catch( Exception e ) {
    		Log.e("error","error copy databases", e);
    	}
	}
    
    void copyFile(File p_oSrc, File p_oFileDst) throws IOException {
        FileChannel oInChannel = new FileInputStream(p_oSrc).getChannel();
        FileChannel oOutChannel = new FileOutputStream(p_oFileDst).getChannel();
        try {
           oInChannel.transferTo(0, oInChannel.size(), oOutChannel);
        } finally {
           if (oInChannel != null){
              oInChannel.close();
           }
           if (oOutChannel != null){
              oOutChannel.close();
           }
        }
     }


	/**
     * Create and/or open a database.  This will be the same object returned by
     * {@link #getWritableDatabase} unless some problem, such as a full disk,
     * requires the database to be opened read-only.  In that case, a read-only
     * database object will be returned.  If the problem is fixed, a future call
     * to {@link #getWritableDatabase} may succeed, in which case the read-only
     * database object will be closed and the read/write object will be returned
     * in the future.
     *
     * @throws SQLiteException if the database cannot be opened
     * @return a database object valid until {@link #getWritableDatabase}
     *     or {@link #close} is called.
     */
    public synchronized SQLiteDatabase getReadableDatabase(String p_sPassword) {
        if (mDatabaseRO != null && mDatabaseRO.isOpen()) {        	
        		return mDatabaseRO;  // The database is already open for business        	
        }

        if (mIsInitializing) {
            throw new IllegalStateException("getReadableDatabase called recursively");
        }

        SQLiteDatabase oDatabase = null;
        try {
            mIsInitializing = true;
            String sPath = mContext.getDatabasePath(mName).getPath();
            File oDatabasePath = new File(sPath);
            File oDatabasesDirectory = new File(mContext.getDatabasePath(mName).getParent());
            
            if(!oDatabasesDirectory.exists()){
                oDatabasesDirectory.mkdirs();
            }
            if(!oDatabasePath.exists()){
                mIsInitializing = false;
                oDatabase = getWritableDatabase(p_sPassword);
                mIsInitializing = true;
                oDatabase.close();
            }
            oDatabase = SQLiteDatabase.openDatabase(sPath, p_sPassword, mFactory, SQLiteDatabase.OPEN_READONLY);
            if (oDatabase.getVersion() != mNewVersion) {
                throw new SQLiteException("Can't upgrade read-only database from version " 
                        + oDatabase.getVersion() + " to " + mNewVersion + ": " + sPath);
            }

            onOpen(oDatabase);
            Log.w(TAG, "Opened " + mName + " in read-only mode");
            mDatabaseRO = oDatabase;
            return mDatabaseRO;
        } finally {
            mIsInitializing = false;
            if (oDatabase != null && oDatabase != mDatabaseRO) {
            	oDatabase.close();
            }
        }
    }

    /**
     * Close any open database object.
     */
    public synchronized void myClose() {
        if (mIsInitializing) throw new IllegalStateException("Closed during initialization");

        if (mDatabaseRO != null && mDatabaseRO.isOpen()) {
        	mDatabaseRO.close();
        	mDatabaseRO = null;
        }
        
        if (mDatabaseRW != null && mDatabaseRW.isOpen()) {
        	mDatabaseRW.close();
        	mDatabaseRW = null;
        }
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param p_oDatabase The database.
     */
    public abstract void onCreate(SQLiteDatabase p_oDatabase);

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     *
     * @param p_oDatabase The database.
     * @param p_iOldVersion The old database version.
     * @param p_iNnewVersion The new database version.
     */
    public abstract void onUpgrade(SQLiteDatabase p_oDatabase, int p_iOldVersion, int p_iNnewVersion);

    /**
     * Called when the database has been opened.
     * Override method should check {@link SQLiteDatabase#isReadOnly} before
     * updating the database.
     *
     * @param p_oDatabase The database.
     */
    public void onOpen(SQLiteDatabase p_oDatabase) {}
}
