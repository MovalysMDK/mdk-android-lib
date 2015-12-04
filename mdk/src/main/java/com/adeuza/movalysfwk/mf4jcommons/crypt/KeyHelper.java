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

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.KeyGenerator;

/**
 * <p>KeyHelper class.</p>
 *
 */
public final class KeyHelper {
	
	/**
	 * Empty contructor for helper class
	 * <p>This class cannot have instance<p>
	 */
	private KeyHelper(){
	}
	
	/**
	 * <p>createKey.</p>
	 *
	 * @param p_sAlgo a {@link java.lang.String} object.
	 * @param p_iSize a int.
	 * @throws java.security.NoSuchAlgorithmException if any.
	 * @return a {@link java.security.Key} object.
	 */
	public static Key createKey( String p_sAlgo, int p_iSize) throws NoSuchAlgorithmException {
		KeyGenerator oKeyGenerator = KeyGenerator.getInstance(p_sAlgo);
		oKeyGenerator.init(p_iSize);
		return oKeyGenerator.generateKey();
	}
	
	/**
	 * <p>createKey.</p>
	 *
	 * @param p_sAlgo a {@link java.lang.String} object.
	 * @param p_iSize a int.
	 * @param p_sProvider a {@link java.lang.String} object.
	 * @throws java.security.NoSuchAlgorithmException if any.
	 * @throws java.security.NoSuchProviderException if any.
	 * @return a {@link java.security.Key} object.
	 */
	public static Key createKey( String p_sAlgo, int p_iSize, String p_sProvider ) throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyGenerator oKeyGenerator = KeyGenerator.getInstance(p_sAlgo, p_sProvider );
		oKeyGenerator.init(p_iSize);
		return oKeyGenerator.generateKey();
	}
}
