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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response;

/**
 * <p>Basic class for rest reponse. Indicates if there is an error.</p>
 *
 */
public abstract class RestResponse implements IRestResponse {

	/**
	 * true if service Rest have an error
	 */
	public boolean error = false;

	/**
	 * message identifier
	 */
	public int idMessage;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse#setError(boolean)
	 */
	@Override
	public void setError(boolean p_bError) {
		this.error = p_bError;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse#isError()
	 */
	@Override
	public boolean isError() {
		return this.error;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdMessage(int p_iIdMessage) {
		this.idMessage = p_iIdMessage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIdMessage() {
		return this.idMessage;
	}
}
