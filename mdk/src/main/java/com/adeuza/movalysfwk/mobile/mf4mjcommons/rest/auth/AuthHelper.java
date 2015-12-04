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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.auth;

import com.adeuza.movalysfwk.mf4jcommons.crypt.AESUtil;

/**
 * AuthHelper to encrypt/decrypt password
 *
 */
public final class AuthHelper {

	/**
	 * Password prefix.
	 * Password will be encrypted with that prefix.
	 */
	private static final String PASSWD_PREFIX = "pass=";
	
	/**
	 * Singleton instance
	 */
	private static AuthHelper instance = new AuthHelper();

	/**
	 * Constructor
	 */
	private AuthHelper() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 *
	 * @return singleton instance
	 */
	public static AuthHelper getInstance() {
		return instance;
	}
	
	/**
	 * Extract password
	 *
	 * @param p_sLogin login
	 * @param p_lDatePrecision date precision for private key
	 * @param p_sEncryptedPassword encrypted password
	 * @return decrypted password or null if failed
	 */
	public String extractPassword(String p_sLogin, long p_lDatePrecision, String p_sEncryptedPassword) {
		String r_sPassword = null ;

		long lDate = System.currentTimeMillis() / (long) Math.pow(10,p_lDatePrecision);

		try {
			String sDecryptedData = null;
			try {
				//précision initiale
				sDecryptedData = AESUtil.decrypt(computeSeed(p_sLogin, lDate), p_sEncryptedPassword);
				if (sDecryptedData.startsWith(PASSWD_PREFIX)) {
					r_sPassword = sDecryptedData.substring(PASSWD_PREFIX.length());
				}
			}
			finally {
				if (r_sPassword == null) {
					try {
						// précision négative
						sDecryptedData = AESUtil.decrypt(computeSeed(p_sLogin, lDate - 1), p_sEncryptedPassword);
						if ( sDecryptedData.startsWith(PASSWD_PREFIX)) {
							r_sPassword = sDecryptedData.substring(PASSWD_PREFIX.length());
						}
					}
					finally {
						if (r_sPassword == null) {
							// précision positive
							sDecryptedData = AESUtil.decrypt(computeSeed(p_sLogin, lDate + 1), p_sEncryptedPassword);
							if ( sDecryptedData.startsWith(PASSWD_PREFIX)) {
								r_sPassword = sDecryptedData.substring(PASSWD_PREFIX.length());
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			// Pour ne pas propager l'exception.
		}
		return r_sPassword;
	}
	
	/**
	 * Crypt password
	 *
	 * @param p_sLogin login
	 * @param p_sPassword password
	 * @param p_lDatePrecision date precision for private key
	 * @return encrypted login/password
	 * @throws java.lang.Exception if any.
	 */
	public String cryptPassword(String p_sLogin, String p_sPassword, long p_lDatePrecision) throws Exception {
		
		long lDate = System.currentTimeMillis() / (long) Math.pow(10,p_lDatePrecision);
		
		String sSeed = computeSeed(p_sLogin, lDate);
		String sPassword = PASSWD_PREFIX + p_sPassword;
		return AESUtil.encrypt(sSeed, sPassword);
	}
	
	/**
	 * Create private key
	 * @param p_sLogin login
	 * @return
	 */
	private String computeSeed( String p_sLogin, long p_lDate) {	
		return "//PP|18.20d" + p_sLogin + "&rt12" + p_lDate + "#%Pom36" + (p_sLogin.length() * 987);
	}
}
