package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

/**
 * Key Store access for encryption key
 *
 */
public class KeyStore {

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

	/** local socket adress */
	private static final LocalSocketAddress sAddress = new LocalSocketAddress("keystore", LocalSocketAddress.Namespace.RESERVED);

	/** error */
	private int mError = NO_ERROR;

	/** States */
	public enum State {
		/** unlocked */
		UNLOCKED,
		/** locked */
		LOCKED,
		/** uninitialized */
		UNINITIALIZED
	}

	/**
	 * instance getter
	 * @return the keystore instance
	 */
	public static KeyStore getInstance() {
		return new KeyStore();
	}

	/**
	 * get the current state
	 * @return the current state
	 */
	public State state() {
		execute('t');
		State r_oState = null;
		switch (mError) {
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
	 * get the byte array corresponding to key
	 * @param p_oKey the key
	 * @return the byte array corresponding
	 */
	private byte[] get(byte[] p_oKey) {
		ArrayList<byte[]> values = execute('g', p_oKey);
		if (values == null || values.isEmpty()) {
			return null;
		} else {
			return values.get(0);
		}
	}

	/**
	 * get the byte array corresponding to key
	 * @param p_sKey the key
	 * @return the byte array corresponding
	 */
	public byte[] get(String p_sKey) {
		return get(getBytes(p_sKey));
	}

	/**
	 * put value in keystore
	 * @param p_sKey the key
	 * @param p_oValue the value
	 * @return true if put success, false otherwise
	 */
	public boolean put(String p_sKey, byte[] p_oValue) {
		return put(getBytes(p_sKey), p_oValue);
	}

	/**
	 * put value in keystore
	 * @param p_sKey the key
	 * @param p_oValue the value
	 * @return true if put success, false otherwise
	 */
	private boolean put(byte[] p_sKey, byte[] p_oValue) {
		execute('i', p_sKey, p_oValue);
		return mError == NO_ERROR;
	}

	/**
	 * get bytes from string
	 * @param p_sString the string
	 * @return the byte array from string
	 */
	private static byte[] getBytes(String p_sString) {
		try {
			return p_sString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * execute
	 * @param p_iCode code
	 * @param p_oParameters parameters
	 * @return array list of byte
	 */
	private ArrayList<byte[]> execute(int p_iCode, byte[]... p_oParameters) {
		mError = PROTOCOL_ERROR;

		for (byte[] parameter : p_oParameters) {
			if (parameter == null || parameter.length > 65535) {
				return null;
			}
		}

		LocalSocket socket = new LocalSocket();
		try {
			socket.connect(sAddress);

			OutputStream out = socket.getOutputStream();
			out.write(p_iCode);
			for (byte[] parameter : p_oParameters) {
				out.write(parameter.length >> 8);
				out.write(parameter.length);
				out.write(parameter);
			}
			out.flush();
			socket.shutdownOutput();

			InputStream in = socket.getInputStream();
			if ((p_iCode = in.read()) != NO_ERROR) {
				if (p_iCode != -1) {
					mError = p_iCode;
				}
				return null;
			}

			ArrayList<byte[]> values = new ArrayList<>();
			while (true) {
				int i, j;
				if ((i = in.read()) == -1) {
					break;
				}
				if ((j = in.read()) == -1) {
					return null;
				}
				byte[] value = new byte[i << 8 | j];
				for (i = 0; i < value.length; i += j) {
					if ((j = in.read(value, i, value.length - i)) == -1) {
						return null;
					}
				}
				values.add(value);
			}
			mError = NO_ERROR;
			return values;
		} catch (IOException e) {
			// ignore
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	/**
	 * get the last error
	 * @return error code
	 */
	public int getLastError() {
		return mError;
	}

	/**
	 * convert rc to String
	 * @param p_iRc rc
	 * @return string from rc
	 */
	public static final String rcToStr(int p_iRc) {
		String r_sResult = null;
		switch (p_iRc) {
		case KeyStore.NO_ERROR:
			r_sResult = "NO_ERROR";
			break;
		case KeyStore.LOCKED:
			r_sResult = "LOCKED";
			break;
		case KeyStore.UNINITIALIZED:
			r_sResult = "UNINITIALIZED";
			break;
		case KeyStore.SYSTEM_ERROR:
			r_sResult = "SYSTEM_ERROR";
			break;
		case KeyStore.PROTOCOL_ERROR:
			r_sResult = "PROTOCOL_ERROR";
			break;
		case KeyStore.PERMISSION_DENIED:
			r_sResult = "PERMISSION_DENIED";
			break;
		case KeyStore.KEY_NOT_FOUND:
			r_sResult = "KEY_NOT_FOUND";
			break;
		case KeyStore.VALUE_CORRUPTED:
			r_sResult = "VALUE_CORRUPTED";
			break;
		case KeyStore.UNDEFINED_ACTION:
			r_sResult = "UNDEFINED_ACTION";
			break;
		case KeyStore.WRONG_PASSWORD:
			r_sResult = "WRONG_PASSWORD";
			break;
		default:
			r_sResult = "Unknown RC";
			break;
		}
		return r_sResult;
	}
}
