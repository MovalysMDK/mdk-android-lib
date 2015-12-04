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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequest;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequestWriter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 * <p>DomRequestWriter class.</p>
 *
 * @param <R>
 */
public class DomRequestWriter<R extends RestRequest> implements RestRequestWriter {

	/**
	 * Rest request class
	 */
	private Class<R> restRequestClass;

	/**
	 * Rest request
	 */
	private R restRequest;
	
	/**
	 * Rest to object mapper instance
	 */
	private MJsonMapper jsonMapper = null;

	/**
	 * 
	 */
	private Map<Class<? extends MEntity>, DomEntityRequestBuilder<? extends MEntity, R>> entityRequestBuilders 
		= new HashMap<>();

	private List<DomRemoteEntityRequestBuilder<R>> remoteEntityRequestBuilders = new ArrayList<>();
	
	/**
	 * Request Builders
	 */
	private List<DomRequestBuilder<R>> requestBuilders = new ArrayList<>();
	
	/**
	 * <p>Constructor for DomRequestWriter.</p>
	 *
	 * @param p_oRestRequest a {@link java.lang.Class} object.
	 */
	public DomRequestWriter(Class<R> p_oRestRequest) {
		this.restRequestClass = p_oRestRequest;
	}

	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.RestRequestWriter#prepare(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void prepare(boolean p_bfirstGoToServer,
			Map<String, List<MObjectToSynchronize>> p_mapcurrentObjectsToSynchronize,
			Map<String, Long> p_mapSyncTimestamp,
			List<MObjectToSynchronize> p_listAckObjectToSynchronize,
			SynchronizationActionParameterIN p_oSynchronizationActionParameterIN, MContext p_oContext) 
			throws SynchroException {

		try {

			if ( hasData()) {
				this.restRequest = this.restRequestClass.newInstance();
							
				for( DomRequestBuilder<R> oRequestBuilder : this.requestBuilders ) {
					oRequestBuilder.buildRequest(p_bfirstGoToServer, this.restRequest, p_oSynchronizationActionParameterIN, p_mapSyncTimestamp, p_oContext);
				}
				
				// Get living entities to synchronize
				// Treat map
				for (Entry<String, List<MObjectToSynchronize>> oEntry : p_mapcurrentObjectsToSynchronize.entrySet()) {
					Class<? extends MEntity> oEntityClass = (Class<? extends MEntity>) Class.forName(oEntry
							.getKey());
					DomEntityRequestBuilder<? extends MEntity, R> oRequestBuilder = this.entityRequestBuilders.get(oEntityClass);
					if (oRequestBuilder != null) {
						oRequestBuilder.buildRequest(oEntry.getValue(), this.restRequest, 
								p_mapSyncTimestamp, p_listAckObjectToSynchronize, p_oContext);
					} else {
						Application
								.getInstance()
								.getLog()
								.debug("synchro",
										"Can't find requestBuilder for entity class: " + oEntityClass.getName());
					}
				}
			}
		} catch (ClassNotFoundException |InstantiationException | IllegalAccessException oException) {
			throw new SynchroException(oException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void prepareNoSync(MContext p_oContext) throws RestException {
		
		try {
			if ( this.hasData()) {
				this.restRequest = this.restRequestClass.newInstance();
				
				for( DomRemoteEntityRequestBuilder<R> oRequestBuilder : this.remoteEntityRequestBuilders ) {
					oRequestBuilder.buildRequest(this.restRequest, p_oContext);
				}
			}
		} catch (SynchroException | InstantiationException | IllegalAccessException oException) {
			throw new RestException("DomRequestWriter.prepareNoSync error", oException);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void writeJson( Writer p_oWriter ) throws RestException {
		if (jsonMapper == null) {
			jsonMapper = BeanLoader.getInstance().getBean(MJsonMapper.class);
		}
		Application.getInstance().getLog().info("DomRequestWriter", "request: " + this.restRequest);
		String sJson = jsonMapper.toJson(this.restRequest);
		Application.getInstance().getLog().info("DomRequestWriter", "request length: " + sJson.length());
		Application.getInstance().getLog().info("DomRequestWriter", "json request: " + sJson);

        jsonMapper.toJson(this.restRequest, p_oWriter);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasData() {
		return !this.requestBuilders.isEmpty() || !entityRequestBuilders.isEmpty() || !this.remoteEntityRequestBuilders.isEmpty();
	}
	
	/**
	 * <p>Getter for the field <code>entityRequestBuilders</code>.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<Class<? extends MEntity>, DomEntityRequestBuilder<? extends MEntity, R>> getEntityRequestBuilders() {
		return this.entityRequestBuilders;
	}
	
	/**
	 * <p>addEntityRequestBuilder.</p>
	 *
	 * @param p_oEntityClass a {@link java.lang.Class} object.
	 * @param p_oDomRequestBuilder a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomEntityRequestBuilder} object.
	 */
	public void addEntityRequestBuilder( Class<? extends MEntity> p_oEntityClass, DomEntityRequestBuilder<? extends MEntity, R> p_oDomRequestBuilder ) {
		this.entityRequestBuilders.put(p_oEntityClass, p_oDomRequestBuilder);
	}
	
	/**
	 * <p>addRemoteEntityRequestBuilder.</p>
	 *
	 * @param p_oDomRequestBuilder a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRemoteEntityRequestBuilder} object.
	 */
	public void addRemoteEntityRequestBuilder(DomRemoteEntityRequestBuilder<R> p_oDomRequestBuilder ) {
		this.remoteEntityRequestBuilders.add(p_oDomRequestBuilder);
	}
	
	/**
	 * <p>addRequestBuilder.</p>
	 *
	 * @param p_oDomRequestBuilder a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRequestBuilder} object.
	 */
	public void addRequestBuilder( DomRequestBuilder<R> p_oDomRequestBuilder) {
		this.requestBuilders.add(p_oDomRequestBuilder);
	}

	/** {@inheritDoc} */
	@Override
	public String toJson() throws RestException {
		if (jsonMapper == null) {
			jsonMapper = BeanLoader.getInstance().getBean(MJsonMapper.class);
		}
		Application.getInstance().getLog().info("DomRequestWriter", "request: " + this.restRequest);
		String sJson = jsonMapper.toJson(this.restRequest);
		Application.getInstance().getLog().info("DomRequestWriter", "request String length: " + sJson.length());
		Application.getInstance().getLog().info("DomRequestWriter", "json request: " + sJson);

		return sJson;
	}
}
