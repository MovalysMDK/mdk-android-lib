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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequest;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequestWriter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomEntityRequestBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRemoteEntityRequestBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRequestBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRequestWriter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.ResponseProcessor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream.StreamResponseProcessor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream.StreamRestResponseReader;

/**
 * Configuration of a rest invocation
 *
 */
public class RestInvocationConfig<R extends IRestResponse> {

	/**
	 * Request writer 
	 */
	private RestRequestWriter requestWriter ;
	
	/**
	 * Response reader
	 */
	private RestResponseReader<R> responseReader ;
	
	/**
	 * Response processors
	 */
	private List<ResponseProcessor<R>> responseProcessors = new ArrayList<ResponseProcessor<R>>();
	
	/**
	 * Message for invocation
	 */
	private String message ;
	
	/**
	 * <p>Getter for the field <code>responseReader</code>.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader} object.
	 */
	public RestResponseReader<R> getResponseReader() {
		return responseReader;
	}

	/**
	 * <p>Setter for the field <code>responseReader</code>.</p>
	 *
	 * @param p_oResponseReader a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader} object.
	 */
	public void setResponseReader(RestResponseReader<R> p_oResponseReader) {
		this.responseReader = p_oResponseReader;
	}

	/**
	 * <p>Getter for the field <code>requestWriter</code>.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequestWriter} object.
	 */
	public RestRequestWriter getRequestWriter() {
		return requestWriter;
	}

	/**
	 * <p>Setter for the field <code>requestWriter</code>.</p>
	 *
	 * @param p_oRequestWriter a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequestWriter} object.
	 */
	public void setRequestWriter(RestRequestWriter p_oRequestWriter) {
		this.requestWriter = p_oRequestWriter;
	}

	/**
	 * <p>Getter for the field <code>responseProcessors</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ResponseProcessor<R>> getResponseProcessors() {
		return responseProcessors;
	}

	/**
	 * <p>Getter for the field <code>message</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * <p>Setter for the field <code>message</code>.</p>
	 *
	 * @param p_oMessage a {@link java.lang.String} object.
	 */
	public void setMessage(String p_oMessage) {
		this.message = p_oMessage;
	}
	
	/**
	 * Builder to create a rest invocation config in stream mode
	 *
	 * @param <REQ> request class
	 * @param <RESP> response class
	 */
	public static class StreamBuilder<REQ extends RestRequest, RESP extends IRestResponse> {

		/**
		 * Invocation config
		 */
		private RestInvocationConfig<RESP> invocationConfig ;
		
		/**
		 * Constructor
		 */
		public StreamBuilder() {
			this.invocationConfig = new RestInvocationConfig<RESP>();
		}
		
		/**
		 * Define request class
		 * @param p_oRequestClass request class
		 * @return builder instance
		 */
		public StreamBuilder<REQ,RESP> requestClass( Class<REQ> p_oRequestClass ) {
			DomRequestWriter<REQ> oRequestWriter = 
					new DomRequestWriter<REQ>(p_oRequestClass);
			this.invocationConfig.setRequestWriter(oRequestWriter);
			return this;
		}
		
		/**
		 * Define response class
		 * @param p_oResponseClass response class
		 * @return builder instance
		 */
		public StreamBuilder<REQ,RESP> responseClass( Class<RESP> p_oResponseClass ) {
			StreamRestResponseReader<RESP> oResponseReader = 
					new StreamRestResponseReader<RESP>(p_oResponseClass);
			this.invocationConfig.setResponseReader(oResponseReader);
			return this;
		}
		
		/**
		 * Define response class
		 * @param p_oResponseClass response class
		 * @return builder instance
		 */
		public StreamBuilder<REQ,RESP> responseClass( Class<RESP> p_oResponseClass, Type p_oType ) {
			StreamRestResponseReader<RESP> oResponseReader = 
					new StreamRestResponseReader<RESP>(p_oResponseClass, p_oType );
			this.invocationConfig.setResponseReader(oResponseReader);
			return this;
		}
		
		/**
		 * Define request builder
		 * @param p_oRequestBuilder request builder
		 * @return builder instance
		 */
		public StreamBuilder<REQ,RESP> requestBuilder( DomRequestBuilder<REQ> p_oRequestBuilder ) {
			((DomRequestWriter<REQ>) this.invocationConfig.getRequestWriter()).addRequestBuilder(p_oRequestBuilder);
			return this;
		}
		
		/**
		 * Define an entity request builder
		 * @param p_oEntityClass entity class
		 * @param p_oDomRequestBuilder request builder associated with the entity
		 * @return builder instance
		 */
		public StreamBuilder<REQ,RESP> entityRequestBuilder( Class<? extends MEntity> p_oEntityClass, DomEntityRequestBuilder<? extends MEntity, REQ> p_oDomRequestBuilder ) {
			((DomRequestWriter<REQ>) this.invocationConfig.getRequestWriter()).addEntityRequestBuilder(p_oEntityClass, p_oDomRequestBuilder);
			return this;
		}
		
		/**
		 * Define an remote entity request builder
		 * @param p_oDomRemoteRequestBuilder remote entity request builder
		 * @return builder instance
		 */
		public StreamBuilder<REQ,RESP> remoteEntityRequestBuilder( DomRemoteEntityRequestBuilder<REQ> p_oDomRemoteRequestBuilder ) {
			DomRequestWriter<REQ> oDomRequestWriter = (DomRequestWriter<REQ>) this.invocationConfig.getRequestWriter();
			oDomRequestWriter.addRemoteEntityRequestBuilder(p_oDomRemoteRequestBuilder);
			return this;
		}
		
		/**
		 * ADd a stream response processor
		 * @param p_sPath path in json associated with the stream processor
		 * @param p_oStreamResponseProcessor stream processor
		 * @return builder instance
		 */
		private StreamBuilder<REQ, RESP> streamResponseProcessor(
				String p_sPath, StreamResponseProcessor p_oStreamResponseProcessor ) {
			((StreamRestResponseReader) this.invocationConfig.getResponseReader())
				.addStreamResponseProcessor(p_sPath, p_oStreamResponseProcessor);
			return this;
		}
		
		/**
		 * Build and return instance of RestInvocationConfig
		 * @return newly created instance
		 */
		public RestInvocationConfig<RESP> build() {
			return this.invocationConfig;
		}
	}
}
