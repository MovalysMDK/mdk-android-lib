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
package com.adeuza.movalysfwk.mf4jcommons.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class to encrypt/decrypt with AES
 *
 */
public final class AESUtil {

	/** mask for bytes values */
	private static final int BYTE_MASK = 0x0f;

	/** shift for bytes values */
	private static final int BYTE_SHIFT = 4;

	/** the radix of an hex */
	private static final int HEX_RADIX = 16;
	
	/** the radix of a bit */
	private static final int BIT_RADIX = 2;

	/**
	 * buffer size used to write encrypted files
	 */
	private static final int BUFFER_SIZE = 8196;
	
	/**
	 * AES description constant
	 */
	public static final String AES = "AES";

	/**
	 * Hexadecimal values
	 */
	private static final String HEX = "0123456789ABCDEF";

	/**
	 * Default encryption key
	 */
	private static String DEFAULTPASSKEY="24524MovalysFramework6546384354";

	/**
	 * Constructor
	 */
	private AESUtil() {
		// nothing to do
	}
	
	/**
	 * <p>encrypt.</p>
	 *
	 * @param p_sSeed a {@link java.lang.String} object.
	 * @param p_sCleartext a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encrypt(String p_sSeed, String p_sCleartext)
			throws Exception {
		byte[] oRawKey = getRawKey(p_sSeed.getBytes());
		return encrypt(p_sSeed, p_sCleartext, oRawKey);
	}
	
	/**
	 * <p>encrypt.</p>
	 *
	 * @param p_sSeed a {@link java.lang.String} object.
	 * @param p_sCleartext a {@link java.lang.String} object.
	 * @param p_oKey an array of byte.
	 * @throws java.lang.Exception if any.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encrypt(String p_sSeed, String p_sCleartext, byte[] p_oKey )
			throws Exception {
		byte[] oResult = encrypt(p_oKey, p_sCleartext.getBytes());
		return toHex(oResult);
	}
	
	/**
	 * <p>encrypt.</p>
	 *
	 * @param p_sCleartext a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encrypt(String p_sCleartext)
			throws Exception {
		byte[] oRawKey = getRawKey(DEFAULTPASSKEY.getBytes());
		byte[] oResult = encrypt(oRawKey, p_sCleartext.getBytes());
		return toHex(oResult);
	}
	
	/**
	 * <p>encrypt.</p>
	 *
	 * @param p_oInputFile a {@link java.io.File} object.
	 * @param p_oOutputFile a {@link java.io.File} object.
	 * @throws java.lang.Exception if any.
	 */
	public static void encrypt( File p_oInputFile, File p_oOutputFile)
			throws Exception {
		byte[] oRawKey = getRawKey(DEFAULTPASSKEY.getBytes());
		encrypt(p_oInputFile, p_oOutputFile, oRawKey);
	}

	/**
	 * <p>decrypt.</p>
	 *
	 * @param p_sSeed a {@link java.lang.String} object.
	 * @param p_sEncrypted a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 * @return a {@link java.lang.String} object.
	 */
	public static String decrypt(String p_sSeed, String p_sEncrypted)
			throws Exception {
		byte[] oRawKey = getRawKey(p_sSeed.getBytes());
		byte[] oEnc = toByte(p_sEncrypted);
		byte[] oResult = decrypt(oRawKey, oEnc);
		return new String(oResult);
	}
	
	/**
	 * <p>decrypt.</p>
	 *
	 * @param p_oInputFile a {@link java.io.File} object.
	 * @param p_oOutputFile a {@link java.io.File} object.
	 * @throws java.lang.Exception if any.
	 */
	public static void decrypt(File p_oInputFile, File p_oOutputFile)
			throws Exception {
		byte[] oRawKey = getRawKey(DEFAULTPASSKEY.getBytes());
		decrypt(p_oInputFile, p_oOutputFile, oRawKey);
	}
	
	
	/**
	 * <p>decrypt.</p>
	 *
	 * @param p_sEncrypted a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 * @return a {@link java.lang.String} object.
	 */
	public static String decrypt(String p_sEncrypted)
			throws Exception {
		byte[] oRawKey = getRawKey(DEFAULTPASSKEY.getBytes());
		byte[] oEnc = toByte(p_sEncrypted);
		byte[] oResult = decrypt(oRawKey, oEnc);
		return new String(oResult);
	}

	/**
	 * Returns an MD5 key for the given bytes array
	 * @param p_bSeed MD5 seed to apply
	 * @return the MD5 digest
	 * @throws Exception if any
	 */
	private static byte[] getRawKey(byte[] p_bSeed) throws Exception {
		MessageDigest oMD5 = MessageDigest.getInstance("MD5");
		oMD5.update(p_bSeed);
		return oMD5.digest();
	}

	/**
	 * Returns the given bytes array encrypted in AES with the given key
	 * @param p_oRawKey key to use for encryption
	 * @param p_bBytes byte array to encrypt
	 * @return the encrypted bytes array
	 * @throws Exception if any
	 */
	private static byte[] encrypt(byte[] p_oRawKey, byte[] p_bBytes) throws Exception {
		Cipher oCipher = initCipher(p_oRawKey, Cipher.ENCRYPT_MODE, AES);
		return oCipher.doFinal(p_bBytes);
	}
	
	/**
	 * <p>encrypt.</p>
	 *
	 * @param p_oInputFile a {@link java.io.File} object.
	 * @param p_oOutputFile a {@link java.io.File} object.
	 * @param p_oKey an array of byte.
	 * @throws java.lang.Exception if any.
	 */
	public static void encrypt( File p_oInputFile, File p_oOutputFile, byte[] p_oKey ) throws Exception {
		Cipher oCipher = initCipher(p_oKey, Cipher.ENCRYPT_MODE, AES );
		crypt(p_oInputFile, p_oOutputFile, oCipher);
	}
	
	/**
	 * <p>encrypt.</p>
	 *
	 * @param p_oInputFile a {@link java.io.File} object.
	 * @param p_oOutputFile a {@link java.io.File} object.
	 * @param p_oKey an array of byte.
	 * @param p_sProviderName a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	public static void encrypt( File p_oInputFile, File p_oOutputFile, byte[] p_oKey, String p_sProviderName ) throws Exception {
		Cipher oCipher = initCipher(p_oKey, Cipher.ENCRYPT_MODE, AES, p_sProviderName );
		crypt(p_oInputFile, p_oOutputFile, oCipher);
	}

	/**
	 * <p>encrypt and decrypt common code.</p>
	 *
	 * @param p_oInputFile a {@link java.io.File} object.
	 * @param p_oOutputFile a {@link java.io.File} object.
	 * @param p_oCipher a {@link javax.crypto.Cipher} object.
	 * @throws java.lang.Exception if any.
	 */
	private static void crypt( File p_oInputFile, File p_oOutputFile, Cipher p_oCipher ) throws Exception {
		InputStream oIs = new FileInputStream(p_oInputFile);
		try {
			FileOutputStream oOutputStream = new FileOutputStream(p_oOutputFile);
			try {
				CipherOutputStream oCipherOutputStream = new CipherOutputStream( oOutputStream, p_oCipher );				
				try {
					
					byte [] oBuffer = new byte [ BUFFER_SIZE ];
					int iBytesRead = oIs.read( oBuffer );
					while ( iBytesRead != -1 ) {
						oCipherOutputStream.write( oBuffer, 0, iBytesRead );
						iBytesRead = oIs.read( oBuffer );
					}
					oCipherOutputStream.flush();
				} finally {
					oCipherOutputStream.close();
				}
			} finally {
				oOutputStream.close();
			}
		} finally {
			oIs.close();
		}
	}
	
	/**
	 * Initializes a cipher object
	 * @param p_oKey the key to use for encryption
	 * @param p_iMode operation mode of the cipher
	 * @param p_sAlgo the algorithm to use
	 * @return the Cipher object
	 * @throws NoSuchAlgorithmException if any
	 * @throws NoSuchPaddingException if any
	 * @throws InvalidKeyException if any
	 * @throws NoSuchProviderException if any
	 */
	private static Cipher initCipher(byte[] p_oKey, int p_iMode, String p_sAlgo) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, NoSuchProviderException {
		return initCipher(p_oKey, p_iMode, p_sAlgo, null);
	}
	
	/**
	 * Initializes a cipher object
	 * @param p_oKey the key to use for encryption
	 * @param p_iMode operation mode of the cipher
	 * @param p_sAlgo the algorithm to use
	 * @param p_sProvider the name of the provider
	 * @return the Cipher object
	 * @throws NoSuchAlgorithmException if any
	 * @throws NoSuchPaddingException if any
	 * @throws InvalidKeyException if any
	 * @throws NoSuchProviderException if any
	 */
	private static Cipher initCipher(byte[] p_oKey, int p_iMode, String p_sAlgo, String p_sProvider) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, NoSuchProviderException {
		SecretKeySpec skeySpec = new SecretKeySpec(p_oKey, p_sAlgo);
		Cipher oCipher ;
		if ( p_sProvider != null ) {
			oCipher = Cipher.getInstance(p_sAlgo, p_sProvider);
		}
		else {
			oCipher = Cipher.getInstance(p_sAlgo);
		}
		oCipher.init(p_iMode, skeySpec);
		return oCipher;
	}

	/**
	 * decrypts the given input in AES mode with given key
	 * @param p_oKey the key to use for encryption
	 * @param p_bInput the bytes array to encrypt
	 * @return the encrypted bytes array
	 * @throws Exception if any
	 */
	private static byte[] decrypt(byte[] p_oKey, byte[] p_bInput)
			throws Exception {
		Cipher oCipher = initCipher(p_oKey, Cipher.DECRYPT_MODE, AES);
		return oCipher.doFinal(p_bInput);
	}

	/**
	 * <p>decrypt.</p>
	 *
	 * @param p_oInputFile a {@link java.io.File} object.
	 * @param p_oOutputFile a {@link java.io.File} object.
	 * @param p_oKey an array of byte.
	 * @param p_sProvider a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	public static void decrypt( File p_oInputFile, File p_oOutputFile, byte[] p_oKey, String p_sProvider ) throws Exception {
		Cipher oCipher = initCipher(p_oKey, Cipher.DECRYPT_MODE, AES, p_sProvider );
		crypt( p_oInputFile, p_oOutputFile, oCipher);
	}
	
	/**
	 * <p>decrypt.</p>
	 *
	 * @param p_oInputFile a {@link java.io.File} object.
	 * @param p_oOutputFile a {@link java.io.File} object.
	 * @param p_oKey an array of byte.
	 * @throws java.lang.Exception if any.
	 */
	public static void decrypt( File p_oInputFile, File p_oOutputFile, byte[] p_oKey ) throws Exception {
		Cipher oCipher = initCipher(p_oKey, Cipher.DECRYPT_MODE, AES );
		crypt( p_oInputFile, p_oOutputFile, oCipher);
	}
	
	/**
	 * <p>toHex.</p>
	 *
	 * @param p_sTxt a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toHex(String p_sTxt) {
		return toHex(p_sTxt.getBytes());
	}

	/**
	 * <p>fromHex.</p>
	 *
	 * @param p_sHex a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String fromHex(String p_sHex) {
		return new String(toByte(p_sHex));
	}

	/**
	 * <p>toByte.</p>
	 *
	 * @param p_sHex a {@link java.lang.String} object.
	 * @return an array of byte.
	 */
	public static byte[] toByte(String p_sHex) {
		int len = p_sHex.length() / 2;
		byte[] oResult = new byte[len];
		for (int i = 0; i < len; i++) {
			Integer oValue = Integer.valueOf(p_sHex.substring(BIT_RADIX * i, BIT_RADIX * i + 2), HEX_RADIX);
			oResult[i] = oValue.byteValue();
		}
		return oResult;
	}

	/**
	 * <p>toHex.</p>
	 *
	 * @param p_oBuf an array of byte.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toHex(byte[] p_oBuf) {
		if (p_oBuf == null) {
			return "";
		}
		StringBuffer oResult = new StringBuffer(2 * p_oBuf.length);
		for (int i = 0; i < p_oBuf.length; i++) {
			appendHex(oResult, p_oBuf[i]);
		}
		return oResult.toString();
	}

	/**
	 * <p>appendHex.</p>
	 * @param p_sStringBuffer the StringBuffer on which we append byte
	 * @param p_bByte the byte to append
	 */
	private static void appendHex(StringBuffer p_sStringBuffer, byte p_bByte) {
		p_sStringBuffer.append(HEX.charAt((p_bByte >> BYTE_SHIFT) & BYTE_MASK)).append(HEX.charAt(p_bByte & BYTE_MASK));
	}
}
