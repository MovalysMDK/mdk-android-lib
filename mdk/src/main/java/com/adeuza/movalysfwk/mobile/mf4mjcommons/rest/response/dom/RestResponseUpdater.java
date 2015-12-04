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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.dom;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;

/**
 * Allow to modify serialized response before unserialization
 *
 */
public interface RestResponseUpdater {

	/**
	 * <p>updateResponse.</p>
	 *
	 * @param p_sResponse a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 */
	public String updateResponse( String p_sResponse ) throws RestException ;
}
