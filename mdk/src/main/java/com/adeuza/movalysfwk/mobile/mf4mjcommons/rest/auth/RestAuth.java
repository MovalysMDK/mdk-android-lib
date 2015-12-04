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

import java.util.Map;

/**
 * <p>A2A_DOC LMI - Décrire la classe RestAuth</p>
 *
 *
 * @since MF-Annapurna
 */
public interface RestAuth {

	/**
	 * header token identifiant
	 */
	public static final String HEADER_TOKEN = "Authorization";
	
	/**
	 * A2A_DOC LMI - Décrire la méthode getAuthHeaders de la classe RestAuth
	 *
	 * @param p_sLogin a {@link java.lang.String} object.
	 * @param p_sPassword a {@link java.lang.String} object.
	 * @param p_sUrl a {@link java.lang.String} object.
	 * @param p_sEntryPoint a {@link java.lang.String} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.auth.AuthRestException if any.
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String,String> getAuthHeaders( String p_sLogin, String p_sPassword, String p_sUrl, String p_sEntryPoint ) throws AuthRestException ;
}
