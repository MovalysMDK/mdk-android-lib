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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>LocalCredentialService interface.</p>
 *
 */
public interface LocalCredentialService {

	/**
	 * Called when the current user has been authenticated. Stores the login and
	 * the resource identifier.
	 *
	 * @param p_oContext The current context. Never null.
	 * @param p_sLogin The login of the current user.
	 * @param p_lResourceId The resource identifier of the current user.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.CredentialException if any.
	 */
	public void storeCredentials(String p_sLogin, long p_lResourceId, MContext p_oContext)
			throws CredentialException;

	/**
	 * Tries to authentify the user using datas in database.
	 *
	 * @param p_sLogin The login of the current user.
	 * @param p_oContext The current context.
	 * @return is identify
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.CredentialException if any.
	 */
	public IdentResult doIdentify(String p_sLogin, MContext p_oContext) throws CredentialException;

	/**
	 * Delete credentials store on mobile
	 *
	 * @param p_oContext context
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.CredentialException if any.
	 */
	public void deleteLocalCredentials(MContext p_oContext) throws CredentialException;

}
