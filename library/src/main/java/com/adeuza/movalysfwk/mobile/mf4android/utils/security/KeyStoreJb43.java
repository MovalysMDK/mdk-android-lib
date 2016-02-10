package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.IBinder;
import android.os.RemoteException;
import android.security.IKeystoreService;

/**
 * JellyBean keystore implementation
 *
 */
public class KeyStoreJb43 extends KeyStore {

	// ResponseCodes
	/** no error */
	public static final int NO_ERROR = 1;
	/** locked */
	public static final int LOCKED = 2;
	/** uninitialized */
	public static final int UNINITIALIZED = 3;
	/** system error */
	public static final int SYSTEM_ERROR = 4;
	/** protocol error */
	public static final int PROTOCOL_ERROR = 5;
	/** permission denied */
	public static final int PERMISSION_DENIED = 6;
	/** key not found */
	public static final int KEY_NOT_FOUND = 7;
	/** value corrupted */
	public static final int VALUE_CORRUPTED = 8;
	/** undefined action */
	public static final int UNDEFINED_ACTION = 9;
	/** wrong password */
	public static final int WRONG_PASSWORD = 10;

	/** binder */
	private final IKeystoreService mBinder;

	/** Used for UID field to indicate the calling UID. */
	public static final int UID_SELF = -1;

	// Flags for "put" "import" and "generate"
	/** flag none */
	public static final int FLAG_NONE = 0;
	/** flag encypted */
	public static final int FLAG_ENCRYPTED = 1;
	/** error */
	private int mError = NO_ERROR;

	/**
	 * private contructor
	 * @param p_oBinder binder
	 */
	private KeyStoreJb43(IKeystoreService p_oBinder) {
		mBinder = p_oBinder;
	}

	/**
	 * Singleton get instance
	 * @return the keystore instance
	 */
	public static KeyStoreJb43 getInstance() {
		try {
			Class<?> smClass = Class.forName("android.os.ServiceManager");

			Method getService = smClass.getMethod("getService", String.class);
			IBinder binder = (IBinder) getService.invoke(null, "android.security.keystore");
			IKeystoreService keystore = IKeystoreService.AbstractStub.asInterface(binder);
			return new KeyStoreJb43(keystore);
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

	/**
	 * is hardware backed
	 * @return true if hardware backed
	 */
	public boolean isHardwareBacked() {
		try {
			return mBinder.isHardwareBacked() == NO_ERROR;
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * is unlocked
	 * @return true if keystore unlocked
	 */
	public boolean isUnlocked() {
		return state() == State.UNLOCKED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State state() {
		final int ret;
		try {
			ret = mBinder.test();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] get(String p_sKey) {
		try {
			return mBinder.get(p_sKey);
		} catch (RemoteException e) {
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean put(String p_sKey, byte[] p_oValue) {
		return put(p_sKey, p_oValue, UID_SELF, FLAG_ENCRYPTED);
	}

	/**
	 * put
	 * @param p_sKey key
	 * @param p_oValue value
	 * @param p_iUid id
	 * @param p_iFlags flags
	 * @return true if success, false otherwise
	 */
	public boolean put(String p_sKey, byte[] p_oValue, int p_iUid, int p_iFlags) {
		try {
			return mBinder.insert(p_sKey, p_oValue, p_iUid, p_iFlags) == NO_ERROR;
		} catch (RemoteException e) {
			return false;
		}
	}
}
