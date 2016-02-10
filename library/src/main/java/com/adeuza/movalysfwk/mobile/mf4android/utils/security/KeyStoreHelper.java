package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.utils.security.KeyStore.State;

/**
 * KeySore Helper
 * <p>Use multiple implementation for Android version</p>
 *
 */
public class KeyStoreHelper {
	/** is jelly bean or superior version */
	private static final boolean IS_JB43 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
	//private static final boolean IS_JB = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	/** is kitkat or superior version */
	private static final boolean IS_KK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	/** old android unlock action */
	public static final String OLD_UNLOCK_ACTION = "android.credentials.UNLOCK";
	/** unlock action */
	public static final String UNLOCK_ACTION = "com.android.credentials.UNLOCK";
	/** keystore */
	private KeyStore ks;
	/** context */
	private Context context;

	/**
	 * Constructor
	 * @param p_oContext the context
	 */
	public KeyStoreHelper(Context p_oContext) {

		this.context = p_oContext;
		if (IS_KK) {
			ks = KeyStoreKk.getInstance();
		} else if (IS_JB43) {
			ks = KeyStoreJb43.getInstance();
		} else {
			ks = KeyStore.getInstance();
		}
	}

	/**
	 * store a key
	 * @param p_sKeyName key name
	 * @param p_oKey key to store
	 */
	public void storeKey(String p_sKeyName, Key p_oKey) {
		boolean success = ks.put(p_sKeyName, p_oKey.getEncoded());
		checkRc(success);
	}

	/**
	 * load a key
	 * @param p_sKeyName key name
	 * @param p_sAlgo algorithm
	 * @return the stored key
	 */
	public Key loadKey(String p_sKeyName, String p_sAlgo ) {
		SecretKeySpec key = null;
		byte[] keyBytes = ks.get(p_sKeyName);
		if ( keyBytes != null ) {
			key = new SecretKeySpec(keyBytes, p_sAlgo);
		}
		return key;
	}

	/**
	 * check RC
	 * @param p_bSuccess false to check
	 */
	private void checkRc(boolean p_bSuccess) {
		if (!p_bSuccess) {
			String errorStr = KeyStore.rcToStr(ks.getLastError());
			Log.d("MDK", "last error = " + errorStr);
			throw new RuntimeException("Keystore error: " + errorStr);
		}
	}

	/**
	 * get the store type
	 * @return the store type
	 */
	public String getStoreType() {
		String r_sStoreType = null;
		if (IS_KK) {
			if (((KeyStoreKk) ks).isHardwareBacked()) {
				r_sStoreType = "HW-backed";
			} else {
				r_sStoreType = "SW only";
			}
		} else if (IS_JB43) {
			if (((KeyStoreJb43) ks).isHardwareBacked()) {
				r_sStoreType = "HW-backed";
			} else {
				r_sStoreType = "SW only";
			}
		}
		return r_sStoreType;
	}

	/**
	 * get the store state
	 * @return the store state
	 */
	public State getStoreState() {
		State storeState = null;
		if (IS_KK) {
			storeState = ((KeyStoreKk) ks).state();
		} else if (IS_JB43) {
			storeState = ((KeyStoreJb43) ks).state();
		}
		return storeState;
	}


	/**
	 * unlock store
	 */
	public void unlock() {
		if (ks.state() == KeyStore.State.UNLOCKED) {
			return;
		}

		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				Log.d("LMI", "unlock keystore");
				context.startActivity(new Intent(OLD_UNLOCK_ACTION));
			} else {
				Log.d("LMI", "unlock keystore");
				context.startActivity(new Intent(UNLOCK_ACTION));
			}
		} catch (ActivityNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
