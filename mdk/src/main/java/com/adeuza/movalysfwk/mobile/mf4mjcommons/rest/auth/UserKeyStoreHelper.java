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

import java.security.KeyStore;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;

/**
 * <p>UserKeyStoreHelper interface.</p>
 *
 */
@Scope(ScopePolicy.SINGLETON)
public interface UserKeyStoreHelper {

	/**
	 * loads a keystore from raw directory
	 *
	 * @param p_sKeyStoreFile the name of the keystore
	 * @param p_sKeyStorePass the password of the keystore
	 * @return the decrypted keystore
	 */
	public KeyStore loadKeyStoreFromRaw(String  p_sKeyStoreFile, String p_sKeyStorePass);
	
	/**
	 * loads a keystore from resources
	 *
	 * @param p_sKeyStoreFile the name of the keystore
	 * @param p_sKeyStorePass the password of the keystore
	 * @return the decrypted keystore
	 */
	public KeyStore loadKeyStore(String p_sKeyStoreFile, String p_sKeyStorePass);
}
