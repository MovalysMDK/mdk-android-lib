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

import java.util.HashMap;
import java.util.Map;

/**
 * <p>A2A_DOC LMI - Décrire la classe AESRestAuth</p>
 *
 *
 * @since MF-Annapurna
 */
public class AESRestAuth implements RestAuth {

	/** la precision de l'encodage de la date de cryptage */
	private static final long DATE_PRECISION = 6;
	
	/** {@inheritDoc} */
	@Override
	public Map<String, String> getAuthHeaders(String p_sLogin, String p_sPassword, String p_sUrl,
			String p_sEntryPoint) throws AuthRestException {
		Map<String,String> r_oAuthTokens = new HashMap<String, String>();
		try {
			String sValue = p_sLogin + ':' + AuthHelper.getInstance().cryptPassword(p_sLogin, p_sPassword, DATE_PRECISION);
			r_oAuthTokens.put(HEADER_TOKEN, sValue );
		} catch (Exception oException ) {
			throw new AuthRestException(oException);
		}
		return r_oAuthTokens;
	}
}
