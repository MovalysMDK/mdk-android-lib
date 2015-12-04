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

import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition.FWK_MOBILE_I_1002;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_10;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_9;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask.ActionTaskStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.FwkSynchronizationActionImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationProgressNotifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.io.IOUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.auth.RestAuth;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;

/**
 * <p>Invoke a service rest</p>
 *
 *
 * @param <R> : result's type
 */
@Scope(ScopePolicy.PROTOTYPE)
public class RestInvoker<R extends IRestResponse> implements IRestInvoker<R> {

	/**
	 * GET parameters
	 */
	private List<Object> getParameters = null;

	/**
	 * the local context
	 */
	private HttpContext localContext = null;
	
	/** représentation d'un noeud root */
	private static final String ROOT_NODE = "/";
	/**
	 * the http client
	 */
	private DefaultHttpClient httpClient = null;

	/**
	 * post parameters
	 */
	private HttpPost post = null;
		
	/**
	 * Connection configuration
	 */
	private RestConnectionConfig connectionConfig ;

	private Action<?, ?, ?, ?> actionSynchro;
	
	/**
	 * Create a new RestInvoker Object.
	 */
	public RestInvoker() {
		/* Nothing to do */
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Init Rest Invoker
	 */
	@Override
	public void init(Action<?, ?, ?, ?> p_oAction, RestConnectionConfig p_oConfig ) {

		if ( p_oConfig.getCommand() == null ) {
			throw new IllegalArgumentException("RestConnectionConfig.getCommand() can not be null");
		}
		
		this.connectionConfig = p_oConfig ;
		this.getParameters = new ArrayList<>();
		this.actionSynchro = p_oAction;
	}

	
	/**
	 * <p>
	 * 	prepare invoke of a service rest.
	 * </p>
	 */
	@Override
	public void prepare() {
		// make sure to use a proxy that supports CONNECT
		HttpHost oProxy = null;
		if (this.connectionConfig.isProxy()) {
			String sScheme = "http";
			if (this.isHttpsConnectionEnabled()) {
				sScheme = "https";
			}
			oProxy = new HttpHost(this.connectionConfig.getProxyHost(), this.connectionConfig.getProxyPort(), sScheme);
		}
		// general setup
		SchemeRegistry oSupportedSchemes = new SchemeRegistry();
		// Register the "http" and "https" protocol schemes, they are
		// required by the default operator to look up socket factories.
		oSupportedSchemes.register(new Scheme("http", this.getPlainSocketFactory(), this.connectionConfig.getPort()));
		if (this.isHttpsConnectionEnabled()) {
			oSupportedSchemes.register(new Scheme("https", this.getSSLSocketFactory(), this.connectionConfig.getSslPort()));
		}

		// prepare parameters
		HttpParams oParams = new BasicHttpParams();
		HttpProtocolParams.setVersion(oParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(oParams, HTTP.UTF_8); //A2A_Q UTF8 ? ou ISO ?
		//HttpProtocolParams.setUseExpectContinue(oParams, true); // Cette ligne provoque des erreurs lors de l'utilisation d'un proxy
		HttpConnectionParams.setConnectionTimeout(oParams, this.connectionConfig.getTimeout()); //Time out d'établissement de connexion
		HttpConnectionParams.setSoTimeout(oParams, this.connectionConfig.getSoTimeout()); //Time out d'attente des données
		
		ClientConnectionManager oCCM = new SingleClientConnManager(oParams, oSupportedSchemes);

		HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		
		this.httpClient = new DefaultHttpClient(oCCM, oParams);
		if (oProxy!=null) {
			this.httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, oProxy);
			if (this.connectionConfig.isProxyAuth()) {
				this.httpClient.getCredentialsProvider().setCredentials(
						new AuthScope(this.connectionConfig.getProxyHost(), this.connectionConfig.getProxyPort()),
						new UsernamePasswordCredentials(this.connectionConfig.getProxyUser(), this.connectionConfig.getProxyPassword()));
			}
		}
	}
	
	
	/**
	 * <p>
	 * 	Invoke service rest.
	 * 	It's necessary to call, before this method, <em>RestInvoker.prepare()</em>.
	 * </p>
 	 *
	 * @param p_oInvocationParams a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oContext context
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if it's impossible to join server, or if the response is bad.
	 */
	@Override
	public void invoke( RestInvocationConfig<R> p_oInvocationParams, MContext p_oContext ) throws RestException {
		
		this.localContext = new BasicHttpContext();
		String sUrl = this.constructUrl();
		this.post = new HttpPost(sUrl);
					
		try {
			RestAuth oRestAuth = BeanLoader.getInstance().getBean(RestAuth.class);
			for( Entry<String,String> oHeader : oRestAuth.getAuthHeaders(this.connectionConfig.getUser(), 
				this.connectionConfig.getPassword(), sUrl, this.connectionConfig.getWsEntryPoint()).entrySet()) {
				this.post.addHeader(oHeader.getKey(), oHeader.getValue());
			}
			
			Application.getInstance().getLog().info(FWK_MOBILE_I_1002, FWK_MOBILE_I_1002_LABEL_9);

			StringWriter oStringWriter = new StringWriter();
			p_oInvocationParams.getRequestWriter().writeJson(oStringWriter);
			StringEntity oStringEntity = new StringEntity(oStringWriter.toString(), HTTP.UTF_8);
			oStringEntity.setContentType("application/json; charset=UTF-8");
			
			Application.getInstance().getLog().info(FWK_MOBILE_I_1002, FWK_MOBILE_I_1002_LABEL_10);
			this.post.setEntity(oStringEntity);
			this.post.setHeader(HTTP.CONTENT_TYPE, "application/json");
		}
		catch (UnsupportedEncodingException oUnsupportedEncodingException) {
			Application.getInstance().getLog().error("RestInvoker", "Erreur", oUnsupportedEncodingException);
			throw new RestException("RestInvoker error", oUnsupportedEncodingException);
		}
	}

	/**
	 * <p>
	 * 	Threat the result of a service rest.
	 * 	It's necessary to call, before this method, <em>RestInvoker.prepare()</em>
	 * 	and <em>RestInvoker.invoke()</em>
	 * </p>
	 * @param p_oInvocationConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oConnectionConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_oContext context
	 * @return a java object
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if it's impossible to join server, or if the response is bad.
	 */
	@Override
	public R process( RestInvocationConfig<R> p_oInvocationConfig, RestConnectionConfig p_oConnectionConfig, Notifier p_oNotifier, MContext p_oContext ) throws RestException {

		Application oApplication=Application.getInstance();
		R r_oResult = null;
		HttpResponse oResponse = null;
		
		try {
			
			HttpHost oTarget = null;
			if (this.isHttpsConnectionEnabled()) {
				oTarget = new HttpHost(this.connectionConfig.getHost(), this.connectionConfig.getSslPort(), "https");
			} else {
				oTarget = new HttpHost(this.connectionConfig.getHost(), this.connectionConfig.getPort(), "http");
			}
			
			oApplication.getLog().debug("synchro", "RestInvoker host: " + oTarget.toHostString());
			oApplication.getLog().debug("synchro", "RestInvoker uri: " + this.post.getURI().toString());
			long tempsRef=System.currentTimeMillis();
			
			//on variabilise le contenu du HttpResponse pour y insérer un Mock test
			int iStatusCode;
			String sReasonPhrase= StringUtils.EMPTY;
			HttpEntity oEntity;
			if ( !p_oConnectionConfig.isMockMode()){
				//utilisation sans le mock
				oResponse = this.httpClient.execute(oTarget, this.post, this.localContext);
				iStatusCode = oResponse.getStatusLine().getStatusCode();
				sReasonPhrase = oResponse.getStatusLine().getReasonPhrase();
				oEntity = oResponse.getEntity();
			}
			else {
				//mock du lancement de la popup
				oResponse = null;
				oEntity = null;
				//selon la conf sync_mock_testid sera égal à HttpStatus.SC_FORBIDDEN ou à HttpStatus.SC_BAD_REQUEST
				iStatusCode = p_oConnectionConfig.getMockStatusCode();
				sReasonPhrase = "MOCK TEST MOCK TEST HttpStatus = "+iStatusCode;
			}
			
			long tempsExe=System.currentTimeMillis()-tempsRef;
			oApplication.getLog().debug("PERF","---duree execute :"+tempsExe+" ms" );

			if (isHttpStatusOk(oResponse, iStatusCode)) {
				r_oResult = trtHttpOk(p_oInvocationConfig, p_oConnectionConfig, p_oNotifier, p_oContext, oResponse, iStatusCode, sReasonPhrase);
			} else {
				r_oResult = trtHttpKo(p_oInvocationConfig, p_oConnectionConfig, p_oNotifier, p_oContext, oResponse, iStatusCode, sReasonPhrase);
			}
			
		}
		catch (IOException oIOException) {
			throw new RestException("RestInvoker.process error", oIOException);
		}
		finally {
			// Fermeture de la connection avant de traiter le résultat
			this.httpClient.getConnectionManager().shutdown();
		}
		return r_oResult;
	}

	/**
	 * Is Http Ok
	 * @param p_oResponse response
	 * @param p_iStatusCode status code
	 * @return true if the status is 200
	 */
	protected boolean isHttpStatusOk(HttpResponse p_oResponse, int p_iStatusCode) {
		return (p_iStatusCode == HttpStatus.SC_OK);
	}
	/**
	 * Treatment Http Ok
	 * 
	 * @param p_oInvocationConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oConnectionConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_oContext context
	 * @param p_oResponse Response invoke Http Request.
	 * @param p_iStatusCode Status invoke Http Request.
	 * @param p_sReasonPhrase Reason Error invoke Http Request.
	 * 
	 * @return an R object.
	 * @throws RestException if any
	 */
	protected R trtHttpOk(RestInvocationConfig<R> p_oInvocationConfig, RestConnectionConfig p_oConnectionConfig, Notifier p_oNotifier, MContext p_oContext,
			HttpResponse p_oResponse, int p_iStatusCode, String p_sReasonPhrase) 
			throws RestException {
		R r_oResult = null;

		try {
			if (p_oResponse != null) {
				Application.getInstance().getLog().info(FWK_MOBILE_I_1002, FWK_MOBILE_I_1002_LABEL_9);
				if (this.actionSynchro != null && FwkSynchronizationActionImpl.class.isAssignableFrom(this.actionSynchro.getClass())) {
					((FwkSynchronizationActionImpl) this.actionSynchro).doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationProgressNotifier(p_oNotifier, 0, 
							Application.getInstance().getStringResource(DefaultApplicationR.synchronisation_treatreturndata)
							+ "(" + p_oInvocationConfig.getMessage() + ")"));
				}
				r_oResult = (R) p_oInvocationConfig.getResponseReader().readResponse(p_oNotifier, p_oResponse.getEntity().getContent(), p_oContext);
				Application.getInstance().getLog().info(FWK_MOBILE_I_1002, FWK_MOBILE_I_1002_LABEL_10);
			}
		} catch (IllegalStateException | IOException oIOException) {
			throw new RestException("RestInvoker trtHttpOk error", oIOException);
		}
		
		return r_oResult;
	}
	
	/**
	 * Treatment Http Ko
	 * 
	 * @param p_oInvocationConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oConnectionConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_oContext context
	 * @param p_oResponse Response invoke Http Request.
	 * @param p_iStatusCode Status invoke Http Request.
	 * @param p_sReasonPhrase Reason Error invoke Http Request.
	 * 
	 * @return an R object.
	 * @throws RestException if any
	 */
	protected R trtHttpKo(RestInvocationConfig<R> p_oInvocationConfig, RestConnectionConfig p_oConnectionConfig, Notifier p_oNotifier, MContext p_oContext,
			HttpResponse p_oResponse, int p_iStatusCode, String p_sReasonPhrase) 
			throws RestException {
		R r_oResult = null;
		
		try {
			switch(p_iStatusCode) {
				case HttpStatus.SC_FORBIDDEN :
					//pas d'autorisation du server CAS
					r_oResult = (R) p_oInvocationConfig.getResponseReader().createResponse();
					r_oResult.setIdMessage(AbstractSubControllerSynchronization.STATE_SYNC_KO_AUTHENTIFICATION);
					r_oResult.setError(true);
					break;
				case HttpStatus.SC_NOT_FOUND :
					// Serveur existant mais contexte erroné
					r_oResult = (R) p_oInvocationConfig.getResponseReader().createResponse();
					r_oResult.setIdMessage(AbstractSubControllerSynchronization.STATE_SYNC_KO_JOIN_SERVER);
					r_oResult.setError(true);
					break;
				default:
					//génération d'un message d'erreur
					StringBuilder sErrorBuilder = new StringBuilder();
					sErrorBuilder.append(p_sReasonPhrase);
					if (p_oResponse != null) {
						sErrorBuilder.append("\n");
						sErrorBuilder.append(IOUtils.toString(p_oResponse.getEntity().getContent()));
					}
					throw new RestException(sErrorBuilder.toString());
			}
		} catch (IOException oException ) {
			throw new RestException("OkHttpRestInvoker trtHttpKo error", oException);
		}
		
		return r_oResult;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Append GET parameter
	 */
	@Override
	public void appendGetParameter(Object p_oObject) {
		this.getParameters.add(p_oObject);
	}

	/**
	 * Constructs the url to call by adding GET parameters
	 * @return the url to call
	 */
	private String constructUrl() {
		StringBuilder oUrl = new StringBuilder();
		oUrl.append(this.connectionConfig.getPath());
		concatenatePath(oUrl, this.connectionConfig.getWsEntryPoint());
		concatenatePath(oUrl, this.connectionConfig.getCommand());
		
		for(Object oObject : this.getParameters) {
			oUrl.append('/');
			oUrl.append(oObject);
		}
		return oUrl.toString();
	}
	
	/**
	 * <p>
	 * 	Permet de concatainer à un path builder un nouveau path en prenant en compte la gestion du "/" entre les deux chaînes. 
	 * 	C'est à dire que le cas ou on obtiens un "/" à la fin du builder et un "/" en début de l'autre chaine à ajouter, 
	 * 	est géré de façon à ce qu'on obtienne pas deux "/" à la suite. Le cas ou aucun "/" est présent est géré aussi.
	 * </p> 
	 * @param p_oBuilderToAppend le builder source
	 * @param p_sNextPath le path à ajouter au builder
	 */
	private void concatenatePath(StringBuilder p_oBuilderToAppend, String p_sNextPath){
		String sFrom = p_oBuilderToAppend.toString();
		
		//si il y a deux "/", j'en supprime un
		if (sFrom.endsWith(ROOT_NODE) && p_sNextPath.startsWith(ROOT_NODE)){
			p_oBuilderToAppend.append(p_sNextPath.substring(1));
		}
		//si il n'y a pas de "/", j'en ajoute un
		else if (!sFrom.endsWith(ROOT_NODE) && !p_sNextPath.startsWith(ROOT_NODE)){
			p_oBuilderToAppend.append(ROOT_NODE).append(p_sNextPath);
		}
		//sinon il y a bien un "/" entre les deux chaînes à concatainer
		else{
			p_oBuilderToAppend.append(p_sNextPath);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isHttpsConnectionEnabled() {
		return false;
	}

	/**
	 * Return plain socket factory
	 *
	 * @return a {@link org.apache.http.conn.scheme.SocketFactory} object.
	 */
	public SocketFactory getPlainSocketFactory() {
		return PlainSocketFactory.getSocketFactory();
	}

	/**
	 * Return SSLSocketFactory
	 *
	 * @return a {@link org.apache.http.conn.scheme.SocketFactory} object.
	 */
	public SocketFactory getSSLSocketFactory() {
		return SSLSocketFactory.getSocketFactory();
	}
	
}
