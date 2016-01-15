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
package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Locale;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.adeuza.movalysfwk.mobile.mf4android.utils.security.KeyStore.State;

import android.os.IBinder;
import android.os.RemoteException;
import android.security.IKeystoreService;
import android.security.IKeystoreServiceM;
import android.security.KeystoreArguments;

/**
 * Key Store access for encryption key
 *
 */
public class KeyStoreM extends KeyStore {

	   // ResponseCodes
    public static final int NO_ERROR = 1;
    public static final int LOCKED = 2;
    public static final int UNINITIALIZED = 3;
    public static final int SYSTEM_ERROR = 4;
    public static final int PROTOCOL_ERROR = 5;
    public static final int PERMISSION_DENIED = 6;
    public static final int KEY_NOT_FOUND = 7;
    public static final int VALUE_CORRUPTED = 8;
    public static final int UNDEFINED_ACTION = 9;
    public static final int WRONG_PASSWORD = 10;

    // Used for UID field to indicate the calling UID.
    public static final int UID_SELF = -1;

    // Flags for "put" "import" and "generate"
    public static final int FLAG_NONE = 0;
    public static final int FLAG_ENCRYPTED = 1;
    private int mError = NO_ERROR;

    private final IKeystoreServiceM mBinder;

    private KeyStoreM(IKeystoreServiceM binder) {
        mBinder = binder;
    }

    public static KeyStoreM getInstance() {
        //        IKeystoreService keystore = IKeystoreService.Stub.asInterface(ServiceManager
        //                .getService("android.security.keystore"));
        //        return new KeyStore(keystore);
        try {
            Class<?> smClass = Class.forName("android.os.ServiceManager");

            Method getService = smClass.getMethod("getService", String.class);
            IBinder binder = (IBinder) getService.invoke(null,"android.security.keystore");
            IKeystoreServiceM keystore = IKeystoreServiceM.Stub.asInterface(binder);
            return new KeyStoreM(keystore);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static int getKeyTypeForAlgorithm(String keyType) throws IllegalArgumentException {
        if ("RSA".equalsIgnoreCase(keyType)) {
            return NativeCryptoConstants.EVP_PKEY_RSA;
        } else if ("DSA".equalsIgnoreCase(keyType)) {
            return NativeCryptoConstants.EVP_PKEY_DSA;
        } else if ("EC".equalsIgnoreCase(keyType)) {
            return NativeCryptoConstants.EVP_PKEY_EC;
        } else {
            throw new IllegalArgumentException("Unsupported key type: " + keyType);
        }
    }

    public State state() {
        final int ret;
        try {
        	ret = mBinder.getState(0); // FIXME : get actual user id
        } catch (RemoteException e) {
            throw new AssertionError(e);
        }

        State r_oState = null;
		switch (ret) {
		case NO_ERROR: 
			r_oState = State.UNLOCKED;
			break;
		case LOCKED: 
			r_oState = State.LOCKED;
			break;
		case UNINITIALIZED: 
			r_oState = State.UNINITIALIZED;
			break;
		default: 
			throw new AssertionError(mError);
		}
		return r_oState;
     }

    public boolean isUnlocked() {
        return state() == State.UNLOCKED;
    }

    public byte[] get(String key) {
        try {
            return mBinder.get(key);
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public boolean put(String key, byte[] value) {
        return put(key, value, UID_SELF, FLAG_ENCRYPTED);
    }

    public boolean put(String key, byte[] value, int uid, int flags) {
        try {
            return mBinder.insert(key, value, uid, flags) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean delete(String key, int uid) {
        try {
            return mBinder.del(key, uid) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean delete(String key) {
        return delete(key, UID_SELF);
    }

    public boolean contains(String key, int uid) {
        try {
            return mBinder.exist(key, uid) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean contains(String key) {
        return contains(key, UID_SELF);
    }

    public String[] saw(String prefix, int uid) {
        try {
            return mBinder.list(prefix, uid);
        } catch (RemoteException e) {
            return null;
        }
    }

    public String[] saw(String prefix) {
        return saw(prefix, UID_SELF);
    }

    public boolean reset() {
        try {
            return mBinder.reset() == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean password(String password) {
        try {
            return mBinder.unlock(0, password) == NO_ERROR; // FIXME user id
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean lock() {
        try {
            return mBinder.lock(0) == NO_ERROR;  // FIXME user id
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean unlock(String password) {
        try {
            mError = mBinder.unlock(0, password);   // FIXME user id
            return mError == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isEmpty() {
        try {
            return mBinder.isEmpty(0)== KEY_NOT_FOUND;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean generate(String key, int uid, int keyType, int keySize, int flags,
            byte[][] args) {
        try {
        	KeystoreArguments ksa = new KeystoreArguments(args);
            return mBinder.generate(key, uid, keyType, keySize, flags, ksa) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean importKey(String keyName, byte[] key, int uid, int flags) {
        try {
            return mBinder.import_key(keyName, key, uid, flags) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public byte[] getPubkey(String key) {
        try {
            return mBinder.get_pubkey(key);
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean delKey(String key, int uid) {
        try {
            return mBinder.del(key, uid) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean delKey(String key) {
        return delKey(key, UID_SELF);
    }

    public byte[] sign(String key, byte[] data) {
        try {
            return mBinder.sign(key, data);
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean verify(String key, byte[] data, byte[] signature) {
        try {
            return mBinder.verify(key, data, signature) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean grant(String key, int uid) {
        try {
            return mBinder.grant(key, uid) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean ungrant(String key, int uid) {
        try {
            return mBinder.ungrant(key, uid) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * Returns the last modification time of the key in milliseconds since the
     * epoch. Will return -1L if the key could not be found or other error.
     */
    public long getmtime(String key) {
        try {
            final long millis = mBinder.getmtime(key);
            if (millis == -1L) {
                return -1L;
            }

            return millis * 1000L;
        } catch (RemoteException e) {
            return -1L;
        }
    }

    public boolean duplicate(String srcKey, int srcUid, String destKey, int destUid) {
        try {
            return mBinder.duplicate(srcKey, srcUid, destKey, destUid) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    // TODO remove this when it's removed from Settings
    public boolean isHardwareBacked() {
        return isHardwareBacked("RSA");
    }

    public boolean isHardwareBacked(String keyType) {
        try {
            return mBinder.is_hardware_backed(keyType.toUpperCase(Locale.US)) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean clearUid(int uid) {
        try {
            return mBinder.clear_uid(uid) == NO_ERROR;
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getLastError() {
        return mError;
    }
}
