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
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
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
