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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader;

/**
 * <p>DomRestResponseReader class.</p>
 *
 * @param <R>
 */
public class DomRestResponseReader<R extends IRestResponse> implements RestResponseReader<R> {

	/** Log Tag */
	private static final String TAG = "DomRestResponseReader";

	/**
	 * Response class
	 */
	private Class<R> responseClass ;
	
	
	/**
	 * 
	 */
	private List<RestResponseUpdater> responseUpdaters = new ArrayList<RestResponseUpdater>();
	
	/**
	 * <p>Constructor for DomRestResponseReader.</p>
	 *
	 * @param p_oResponseClass a {@link java.lang.Class} object.
	 */
	public DomRestResponseReader( Class<R> p_oResponseClass ) {
		this.responseClass = p_oResponseClass ;
	}
	
	/**
	 * <p>registerResponseUpdater.</p>
	 *
	 * @param p_oRestResponseUpdater a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.dom.RestResponseUpdater} object.
	 */
	public void registerResponseUpdater( RestResponseUpdater p_oRestResponseUpdater ) {
		this.responseUpdaters.add(p_oRestResponseUpdater);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader#readResponse(java.io.InputStream, com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public R readResponse(Notifier p_oNotifier, InputStream p_oIs, MContext p_oContext) throws RestException {
		
		R r_oResult = null ;
		try {
			MJsonMapper oMJsonMapper = BeanLoader.getInstance().getBean(MJsonMapper.class);
			
			long lStart = System.currentTimeMillis();

			if ( !this.responseUpdaters.isEmpty()) {
				String sContent = toRestInvokerString(p_oIs);
				for( RestResponseUpdater oRestResponseUpdater : this.responseUpdaters) {
					sContent = oRestResponseUpdater.updateResponse(sContent);
				}
				r_oResult = oMJsonMapper.fromJson(sContent, this.responseClass);
			}
			else {
				r_oResult = oMJsonMapper.fromJson(p_oIs, this.responseClass);
			}
			
			long lTime = System.currentTimeMillis() - lStart;
	
			Application.getInstance().getLog().error(TAG, "Temps de traitement DOM: " + lTime);
		} catch( IOException oIOException ) {
			throw new RestException("DomRestResponseReader.readResponse error", oIOException);
		}
		return r_oResult ;
	}
	
	/** {@inheritDoc} */
	@Override
	public R readResponse(Reader p_oReader, Notifier p_oNotifier,
			MContext p_oContext) throws RestException {
		
		R r_oResult = null ;
		try {
			MJsonMapper oMJsonMapper = BeanLoader.getInstance().getBean(MJsonMapper.class);
			long lStart = System.currentTimeMillis();
			
			if ( !this.responseUpdaters.isEmpty()) {
				String sContent = toRestInvokerString(p_oReader);
				for( RestResponseUpdater oRestResponseUpdater : this.responseUpdaters) {
					sContent = oRestResponseUpdater.updateResponse(sContent);
				}
				r_oResult = oMJsonMapper.fromJson(sContent, this.responseClass);
			}
			else {
				r_oResult = oMJsonMapper.fromJson(p_oReader, this.responseClass);
			}
			
			long lTime = System.currentTimeMillis() - lStart;
	
			Application.getInstance().getLog().error(TAG, "Temps de traitement DOM: " + lTime);
		} catch( IOException oIOException ) {
			throw new RestException("DomRestResponseReader.readResponse error", oIOException);
		}
		return r_oResult ;
	}
	
	

	/**
	 * Méthode permettant la lecture de l'inputStream mis en paramètre.
	 *
	 * @param p_oInputStream a {@link java.io.InputStream} object.
	 * @return String l'inputString mis en paramètre converti
	 * @throws java.io.IOException if any.
	 */
	public static String toRestInvokerString(InputStream p_oInputStream) throws IOException {
	
		String r_sResult = null;
		InputStreamReader oInputStreamReader = new InputStreamReader(p_oInputStream);
		try  {
			r_sResult = toRestInvokerString(oInputStreamReader);
		} 
		finally 
		{
			oInputStreamReader.close();
		} 
		return r_sResult;
	}

	/**
	 * @param oInputStreamReader
	 * @return
	 * @throws IOException
	 */
	private static String toRestInvokerString(
			Reader p_oReader) throws IOException {
		StringWriter oStringWriter = new StringWriter();
		char[] buffer = new char[4096];
		int n = 0;

		while (-1 != (n = p_oReader.read(buffer)))  {
			oStringWriter.write(buffer, 0, n);
		}
		return oStringWriter.toString();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader#createResponse()
	 */
	@Override
	public R createResponse() throws RestException  {
		R r_oResult = null;
		try {
			r_oResult = this.responseClass.newInstance();
		} catch (InstantiationException | IllegalAccessException oException) {
			throw new RestException("DomRestResponse.createResponse error", oException);
		}
		return r_oResult;
	}	
}
