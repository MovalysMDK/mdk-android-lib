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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.okhttp;

import java.net.CookieManager;
import java.net.CookiePolicy;

import com.squareup.okhttp.OkHttpClient;

/**
 * Handle sinle instance of http client
 *
 */
public class OkHttpClientHandler implements IHttpClientHandler {

    /**
     * Http client
     */
    private OkHttpClient httpClient;

    /**
     * Constructor
     */
    public OkHttpClientHandler() {
        this.httpClient = createHttpClient();
        configureHttpClient();
    }

    /** {@inheritDoc} */
    @Override
	public OkHttpClient getDefault() {
        return httpClient;
    }

    /** {@inheritDoc} */
    @Override
	public OkHttpClient cloneDefault() {
        return httpClient.clone();
    }

    /**
     * Create an new instance of OkHttpClient
     *
     * @return a {@link com.squareup.okhttp.OkHttpClient} object.
     */
    protected OkHttpClient createHttpClient() {
    	return new OkHttpClient();
    }
    
    /**
     * Initialize http client
     */
    protected void configureHttpClient() {

    	this.httpClient.setFollowSslRedirects(true);

        // Enable cookies
        enableCookies();

        // to disable gzip compression
        // oUrlConnection.setRequestProperty("Accept-Encoding", "identity");
    }
    
    /**
     * Enable cookies
     */
    protected void enableCookies() {
        // - CookieManager accepts cookies from the origin server only
        // - Two other policies are included: ACCEPT_ALL and ACCEPT_NONE
        // - will forget these cookies when the VM exits
        // - Implement CookieStore to define a custom cookie store
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.httpClient.setCookieHandler(cookieManager);
    }

	/**
	 * Return httpClient instance
	 *
	 * @return a {@link com.squareup.okhttp.OkHttpClient} object.
	 */
	protected OkHttpClient getHttpClient() {
		return this.httpClient;
	}
}
