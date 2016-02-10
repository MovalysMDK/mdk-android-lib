package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 * AES crypto utils
 *
 */
public class AESUtil2 {

	/** key lenght */
	private static int KEY_LENGTH = 256;
	/** key generation algo */
	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	/** key delimiter */
	private static String DELIMITER = "]";

	/**
	 * generate a AES key
	 * @return the key
	 */
	public static Key generateKey() {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(KEY_LENGTH);
			return kg.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ENcrypt aes
	 * @param p_sPlaintext plain text to encrypt
	 * @param p_oSecretKey encryption key
	 * @param p_sSecureRandomAlgo secure algorithm
	 * @return the encrypted text
	 */
	public static String encryptAesCbc(String p_sPlaintext, Key p_oSecretKey, String p_sSecureRandomAlgo) {
		String encryptedText = null ;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			byte[] iv = generateIv(cipher.getBlockSize(), p_sSecureRandomAlgo);
			
			// initialization vector (IV)
			IvParameterSpec ivParams = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, p_oSecretKey, ivParams);
			byte[] cipherText = cipher.doFinal(p_sPlaintext.getBytes("UTF-8"));
			encryptedText = String.format("%s%s%s", toBase64(iv), DELIMITER, toBase64(cipherText));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return encryptedText;
	}
	
	/**
	 * decrypt the text
	 * @param p_sCiphertext encrypted text
	 * @param p_oKey decriptionkey
	 * @return the decrypted text
	 */
	public static String decryptAesCbc(String p_sCiphertext, Key p_oKey) {
		String plainrStr = null ;
        try {
            String[] fields = p_sCiphertext.split(DELIMITER);
            if (fields.length != 2) {
                throw new IllegalArgumentException(
                        "Invalid encypted text format");
            }

            byte[] iv = fromBase64(fields[0]);
            byte[] cipherBytes = fromBase64(fields[1]);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, p_oKey, ivParams);

            byte[] plaintext = cipher.doFinal(cipherBytes);
            plainrStr = new String(plaintext, "UTF-8");
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return plainrStr;
    }

	/**
	 * generate IV
	 * @param p_iLength the length
	 * @param p_sAlgo the algorithm to use
	 * @return a byte array (IV)
	 * @throws NoSuchAlgorithmException algorithm do not exist
	 */
	public static byte[] generateIv(int p_iLength, String p_sAlgo)
			throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance(p_sAlgo);
		byte[] b = new byte[p_iLength];
		sr.nextBytes(b);
		return b;
	}
	
	/**
	 * convert a byte array to string base64
	 * @param p_oBytes the byte array to convert
	 * @return the text converted
	 */
    public static String toBase64(byte[] p_oBytes) {
        return Base64.encodeToString(p_oBytes, Base64.NO_WRAP);
    }

    /**
     * convert base64 string to byte array
     * @param p_sBase64 text base64
     * @return the byte array
     */
    public static byte[] fromBase64(String p_sBase64) {
        return Base64.decode(p_sBase64, Base64.NO_WRAP);
    }
}
