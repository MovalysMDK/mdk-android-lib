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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MParameters;

/**
 * <p>LocalCredentialServiceImpl class.</p>
 *
 */
public class LocalCredentialServiceImpl implements LocalCredentialService {

	/**
	 * Name of the {@link MParameters} that represents the login of the previous
	 * authenticated user.
	 */
	private static final String PREVIOUS_LOGIN_PARAMETER = "previous-login";

	/**
	 * Name of the {@link MParameters} that represents the resource identifier
	 * of the previous authenticated user.
	 */
	private static final String PREVIOUS_RESOURCE_PARAMETER = "resourceId";

	/**
	 * Name of the {@link MParameter} that represents the last synchronization
	 * date of the user.
	 */
	private static final String PREVIOUS_SYNC_DATE_PARAMETER = "previous-sync-date";

	/**
	 * Milli seconds in one day.
	 */
	private static final int MILLISECONDS_IN_ONE_DAY = 86400000;

	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.LocalCredentialService#storeCredentials(java.lang.String, long, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void storeCredentials(String p_sLogin, long p_lResourceId, MContext p_oContext)
			throws CredentialException {
		// nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.LocalCredentialService#doIdentify(java.lang.String, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public IdentResult doIdentify( String p_sLogin, MContext p_oContext ) throws CredentialException {
		return IdentResult.ok;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.LocalCredentialService#deleteLocalCredentials(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void deleteLocalCredentials( MContext p_oContext ) throws CredentialException {
		// nothing to do
	}
}
