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
package com.adeuza.movalysfwk.mobile.mf4android.rest;

import java.io.IOException;
import java.io.InputStream;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.MockRestInvoker;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponse;

/**
 * <p>Mock pour l'invocation de service REST.</p>
 *
 *
 * @param <R> type de l'objet réponse du service
 */
public class AndroidMockRestInvoker<R extends IRestResponse> extends MockRestInvoker<RestResponse>{
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected InputStream getResponseInputStream(String p_sParam) throws IOException {
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		return oApplication.getApplicationContext().getResources().getAssets().open(p_sParam + ".json");
	}
}
