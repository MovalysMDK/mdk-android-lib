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

import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition.FWK_MOBILE_I_1002;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import android.content.Context;
import android.os.Environment;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidMMContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask.ActionTaskStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.FwkSynchronizationActionImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationProgressNotifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.NotImplementedException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.auth.RestAuth;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.AllowAllHostnameVerifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;

/**
 * <p>Invoke a service rest</p>
 *
 *
 * @param <R> : result's type
 */
@Scope(ScopePolicy.PROTOTYPE)
public class OkHttpRestInvoker<R extends IRestResponse> implements IRestInvoker<R> {
	
	/**
	 * Json content-type
	 */
	public static final MediaType MEDIATYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	public static final Charset CHARSET_JSON = Util.UTF_8;
	
	/**
	 * GET parameters
	 */
	private List<Object> getParameters ;
	
	/**
	 * Root node representation
	 */
	private static final String ROOT_NODE = "/";
	
	/**
	 * the http client
	 */
	private OkHttpClient httpClient ;
		
	/**
	 * Connection configuration
	 */
	private RestConnectionConfig connectionConfig ;
	
	/**
	 * Request to send
	 */
	private Request request;
	
	/**
	 * Url to invoke
	 */
	private URL url;

	/**
	 * Action to notify
	 */
	private Action<?, ?, ?, ?> actionSynchro;
	
	/**
	 * Create a new RestInvoker Object.
	 */
	public OkHttpRestInvoker() {
		/* Nothing to do */
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker#init(com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action, com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig)
	 */
	@Override
	public void init(Action<?, ?, ?, ?> p_oAction, RestConnectionConfig p_oConfig ) {

		this.httpClient = BeanLoader.getInstance().getBean(IHttpClientHandler.class).getDefault();
		this.connectionConfig = p_oConfig ;
		this.getParameters = new ArrayList<Object>();
		this.actionSynchro = p_oAction;
	}

	/** {@inheritDoc} */
	@Override
	public void prepare() throws RestException {
		
		this.httpClient.setConnectTimeout(this.connectionConfig.getTimeout(), TimeUnit.MILLISECONDS);
		this.httpClient.setReadTimeout(this.connectionConfig.getSoTimeout(), TimeUnit.MILLISECONDS);
		this.httpClient.setWriteTimeout(this.connectionConfig.getWriteTimeout(), TimeUnit.MILLISECONDS);
		
		if ( this.isHttpsConnectionEnabled()) {
			this.httpClient.setSslSocketFactory(this.createSslSocketFactory());
			
			if ( this.connectionConfig.allowAllHostnameVerifierEnabled()) {
				this.httpClient.setHostnameVerifier( new AllowAllHostnameVerifier());
			}
			else {
				this.httpClient.setHostnameVerifier(null);
			}
		}
		
        // proxy
        if ( this.connectionConfig.isProxy()) {
            Proxy oProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
            		this.connectionConfig.getProxyHost(), this.connectionConfig.getProxyPort()));
            this.httpClient.setProxy(oProxy);
            
            // proxy authentication
            if ( this.connectionConfig.isProxyAuth()) {
            	this.httpClient.setAuthenticator( new Authenticator() {
            		
            		/** {@inheritDoc} */
                    @Override
                    public Request authenticate(Proxy p_oProxy, Response p_oResponse) throws IOException {
                        return null;
                    }

                    /** {@inheritDoc} */
                    @Override
                    public Request authenticateProxy(Proxy p_oProxy, Response p_oResponse) throws IOException {
                        String credential = Credentials.basic(OkHttpRestInvoker.this.connectionConfig.getProxyUser(), 
                        		OkHttpRestInvoker.this.connectionConfig.getProxyPassword());
                        return p_oResponse.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                });
            }
        }
	}
	
	/** {@inheritDoc} */
	@Override
	public void invoke( RestInvocationConfig<R> p_oInvocationConfig, MContext p_oContext ) throws RestException {
		
		Application.getInstance().getLog().debug("synchro", "OkHttpRestInvoker.invoke");
		
		try {
			Request.Builder oRequestBuilder = new Request.Builder();
			
			// Construct url
			this.url = this.buildURL();
			oRequestBuilder.url(this.url);
			
			// Add authentication header using RestAuth service
			if ( this.connectionConfig.isRestAuthServiceEnabled()) {
				RestAuth oRestAuth = BeanLoader.getInstance().getBean(RestAuth.class);
				for( Entry<String,String> oHeader : oRestAuth.getAuthHeaders(
						this.connectionConfig.getUser(), 
						this.connectionConfig.getPassword(), 
						this.url.toString(),
						this.connectionConfig.getWsEntryPoint()).entrySet()) {
					oRequestBuilder.header(oHeader.getKey(), oHeader.getValue());
				}
			}
			
			// Add headers
			for( Entry<String,String> oHeader : this.connectionConfig.getHeaders().entrySet()) {
				oRequestBuilder.header(oHeader.getKey(), oHeader.getValue());
			}
			
			// Create request body
			defineHttpMethod(p_oInvocationConfig, oRequestBuilder);
			
			// Create request
			this.request = oRequestBuilder.build();
			
		} catch (MalformedURLException oMalformedURLException) {
			throw new RestException("OkHttpRestInvoker.invoke error", oMalformedURLException);
		}
	}

	protected void defineHttpMethod(RestInvocationConfig<R> p_oInvocationConfig, Request.Builder p_oRequestBuilder) throws RestException {

		// Create request body
		if ( p_oInvocationConfig.getRequestWriter() != null && p_oInvocationConfig.getRequestWriter().hasData()) {
			// Do POST if data should be send
			RequestBody oRequestBody = this.buildRequest(p_oInvocationConfig);
			Application.getInstance().getLog().debug("synchro", "OkHttpRestInvoker POST method");
			p_oRequestBuilder.post(oRequestBody);
		}
		else {
			Application.getInstance().getLog().debug("synchro", "OkHttpRestInvoker GET method");
			p_oRequestBuilder.get();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param p_oInvocationConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oConnectionConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @return an R object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 */
	@Override
	public R process( RestInvocationConfig<R> p_oInvocationConfig, RestConnectionConfig p_oConnectionConfig, Notifier p_oNotifier, MContext p_oContext ) throws RestException {
		R r_oResult = null;

		Application oApplication = Application.getInstance();
		Response oResponse = null;

		try {
						
			oApplication.getLog().debug("synchro", "RestInvoker uri: " + this.url.toString());
			long lRefTime=System.currentTimeMillis();
			
			//on variabilise le contenu du HttpResponse pour y insérer un Mock test
			int iStatusCode;
			String sReasonPhrase = StringUtils.EMPTY;
			if ( !p_oConnectionConfig.isMockMode()){
				//utilisation sans le mock
				oResponse = this.invokeHttpRequest();
	        	iStatusCode = oResponse.code();
	        	sReasonPhrase = oResponse.message();
			}
			else {
				//mock du lancement de la popup
				oResponse = this.invokeMockHttpRequest();
				//selon la conf sync_mock_testid sera égal à HttpStatus.SC_FORBIDDEN ou à HttpStatus.SC_BAD_REQUEST
				if ( oResponse == null ) {
					iStatusCode = p_oConnectionConfig.getMockStatusCode();
					sReasonPhrase = "MOCK TEST MOCK TEST HttpStatus = "+iStatusCode;
				}
				else {
					iStatusCode = oResponse.code();
		        	sReasonPhrase = oResponse.message();
				}
			}
			
			long lExecTime=System.currentTimeMillis()-lRefTime;
			oApplication.getLog().debug("PERF","---execute duration :"+lExecTime+" ms" );
		
			if (isHttpStatusOk(oResponse, iStatusCode)) {
				r_oResult = trtHttpOk(p_oInvocationConfig, p_oConnectionConfig, p_oNotifier, p_oContext, oResponse, iStatusCode, sReasonPhrase);
			} else {
				r_oResult = trtHttpKo(p_oInvocationConfig, p_oConnectionConfig, p_oNotifier, p_oContext, oResponse, iStatusCode, sReasonPhrase);
			}
		}
		catch (IOException oException ) {
			throw new RestException("OkHttpRestInvoker process error", oException);
		}

		return r_oResult;
	}
	
	/**
	 * Is Http Ok
	 * @return response response
	 * @throws IOException if any
	 */
	protected boolean isHttpStatusOk(Response p_oResponse, int p_iStatusCode) {
		return (p_iStatusCode == HttpURLConnection.HTTP_OK);
	}
	
	/**
	 * Treatment Http Ok
	 * 
	 * @param p_oInvocationConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oConnectionConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @param p_oResponse Response invoke Http Request.
	 * @param p_iStatusCode Status invoke Http Request.
	 * @param p_sReasonPhrase Reason Error invoke Http Request.
	 * 
	 * @return an R object.
	 */
	protected R trtHttpOk(RestInvocationConfig<R> p_oInvocationConfig, RestConnectionConfig p_oConnectionConfig, Notifier p_oNotifier, MContext p_oContext,	
			Response p_oResponse, int p_iStatusCode, String p_sReasonPhrase) 
			throws RestException {
		R r_oResult = null;
		
		if (p_oResponse.isSuccessful()) {
			if ( this.actionSynchro != null && 
				FwkSynchronizationActionImpl.class.isAssignableFrom(this.actionSynchro.getClass())) {
				((FwkSynchronizationActionImpl) this.actionSynchro).doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationProgressNotifier(p_oNotifier, 0, 
						Application.getInstance().getStringResource(DefaultApplicationR.synchronisation_treatreturndata)
						+ "(" + p_oInvocationConfig.getMessage() + ")"));
			}
			r_oResult = readResponse(p_oResponse, p_oInvocationConfig, p_oNotifier, p_oContext );
		}
		return r_oResult;
	}

	/**
	 * Treatment Http Ko
	 * 
	 * @param p_oInvocationConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oConnectionConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @param p_oResponse Response invoke Http Request.
	 * @param p_iStatusCode Status invoke Http Request.
	 * @param p_sReasonPhrase Reason Error invoke Http Request.
	 * 
	 * @return an R object.
	 */
	protected R trtHttpKo(RestInvocationConfig<R> p_oInvocationConfig, RestConnectionConfig p_oConnectionConfig, Notifier p_oNotifier, MContext p_oContext,
			Response p_oResponse, int p_iStatusCode, String p_sReasonPhrase) 
			throws RestException {
		R r_oResult = null;
		
		try {
			switch(p_iStatusCode) {
				case HttpURLConnection.HTTP_FORBIDDEN :
					//pas d'autorisation du server CAS
					r_oResult = (R) p_oInvocationConfig.getResponseReader().createResponse();
					r_oResult.setIdMessage(AbstractSubControllerSynchronization.STATE_SYNC_KO_AUTHENTIFICATION);
					r_oResult.setError(true);
					break;
	
				case HttpURLConnection.HTTP_NOT_FOUND :
					// Serveur existant mais contexte erroné
					r_oResult = (R) p_oInvocationConfig.getResponseReader().createResponse();
					r_oResult.setIdMessage(AbstractSubControllerSynchronization.STATE_SYNC_KO_JOIN_SERVER);
					r_oResult.setError(true);
					break;
				
				default:
					//génération d'un message d'erreur
					StringBuilder sErrorBuilder = new StringBuilder();
					sErrorBuilder.append(p_sReasonPhrase);
					if ( p_oResponse != null ) {
						sErrorBuilder.append("\n");
						sErrorBuilder.append(readErrorStream(p_oResponse.body().byteStream()));
					}
					throw new RestException(sErrorBuilder.toString());
				}
		} catch (IOException oException ) {
			throw new RestException("OkHttpRestInvoker trtHttpKo error", oException);
		}
		
		return r_oResult;
	}
	
	/**
	 * Invokes http request
	 * @return response response
	 * @throws IOException if any
	 */
	protected Response invokeHttpRequest() throws IOException {
		return this.httpClient.newCall(this.request).execute();
	}
	
	/**
	 * Invokes http request
	 * @return response response
	 * @throws IOException if any
	 */
	protected Response invokeMockHttpRequest() throws IOException {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void appendGetParameter(Object p_oObject) {
		this.getParameters.add(p_oObject);
	}

   /**
    * Build full url
    * @return the url
    * @throws MalformedURLException if any
    */
   protected URL buildURL() throws MalformedURLException {
       StringBuilder oUrl = new StringBuilder();
       if ( this.connectionConfig.getProtocol() != null ) {
    	   oUrl.append(this.connectionConfig.getProtocol().name().toLowerCase());
           oUrl.append("://");
       }
       else {
    	   if  (this.isHttpsConnectionEnabled()) {
    	       oUrl.append("https://");
    	   }
    	   else {
    		   oUrl.append("http://");
    	   }   
       }

       oUrl.append(this.connectionConfig.getHost());
       if  (!this.isHttpsConnectionEnabled()) {
	       if ( this.connectionConfig.getPort() != 80 ) {
	           oUrl.append(':');
	           oUrl.append(this.connectionConfig.getPort());
	       }
       }
       else {
	       if ( this.connectionConfig.getSslPort() != 443 ) {
	           oUrl.append(':');
	           oUrl.append(this.connectionConfig.getSslPort());
	       }
       }

       oUrl.append(buildURLPath(this.connectionConfig));
       return new URL(oUrl.toString());
   }

   /**
    * Builds the URL path
    * @param p_oRestInvocationConfig invocation configuration object
    * @return the path computed
    */
   private String buildURLPath( RestConnectionConfig p_oRestConnectionConfig ) {
       StringBuilder oUrl = new StringBuilder();
       oUrl.append(p_oRestConnectionConfig.getPath());
       concatenatePath(oUrl, p_oRestConnectionConfig.getWsEntryPoint());
       concatenatePath(oUrl, p_oRestConnectionConfig.getCommand());

       for(Object oObject : this.getParameters) {
           oUrl.append('/');
           oUrl.append(oObject);
       }

       if ( !p_oRestConnectionConfig.getQueryParameters().isEmpty()) {
           oUrl.append('?');
           boolean bFirst = true;
           for (String sQueryParameter : p_oRestConnectionConfig.getQueryParameters()) {
               if ( bFirst ) {
                   bFirst = false;
               }
               else {
                   oUrl.append('&');
               }
               oUrl.append(sQueryParameter);
           }
       }

       return oUrl.toString();
   }
	
	/**
	 * <p>
	 * Merges a string builder to an other one, taking in account the use of the character / between the two.
	 * ie: should there be a / character at the end of the first string builder, and another one at the beginning of the
	 * second string builder, we keep only one of those / characters.
	 * The method will also process the case where no / is found.
	 * </p> 
	 * @param p_oBuilderToAppend the source builder
	 * @param p_sNextPath the path to add to the builder
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
	

	/**
	 * Build request content
	 * @param p_oRestInvocationConfig rest invocation configuration
	 * @return RequestBody the request content
	 * @throws RestException if any
	 */
	protected RequestBody buildRequest( final RestInvocationConfig<R> p_oRestInvocationConfig) throws RestException {
	   
		String sJson = p_oRestInvocationConfig.getRequestWriter().toJson();
		
		byte[] bJson = sJson.getBytes(CHARSET_JSON);
		Application.getInstance().getLog().info("DomRequestWriter", "request Byte length: " + bJson.length);
		
		return RequestBody.create(MEDIATYPE_JSON, sJson);		
   }

   /**
    * Reads the response sent by the server
    * @param p_oResponse the response received
    * @param p_oInvocationConfig the invocation configuration
    * @param p_oNotifier the notifier object
    * @param p_oContext the context to use
    * @return the response read
    * @throws IOException if any
    */
   private R readResponse( Response p_oResponse, RestInvocationConfig<R> p_oInvocationConfig, Notifier p_oNotifier, MContext p_oContext ) throws RestException {
       
	   Reader oReader = null;
	   File oFile = null;
	   
	   if (Application.getInstance().isSyncDataStreamFromFileEnabled()) {
		   // Save InputStreamReader To File
		   oFile = inputStreamReaderToFile(p_oResponse.body().charStream(), p_oContext);
	   
		   // Read File to InputStreamReader
		   try {
			oReader = new FileReader(oFile);
		   } catch (FileNotFoundException e) {
			   throw new RestException(e.toString());
		   }
	   } else {
		   // Read the response sent by the server
		   oReader = p_oResponse.body().charStream();
	   }
	   
	   R r_oResponse = (R) p_oInvocationConfig.getResponseReader().readResponse(
	   			oReader, p_oNotifier, p_oContext);
	   Application.getInstance().getLog().info(FWK_MOBILE_I_1002, FWK_MOBILE_I_1002_LABEL_10);
	
	   
	   // Delete Temp File
	   if (Application.getInstance().isSyncDataStreamFromFileEnabled() && !Application.getInstance().isSyncDebugEnabled())
		   oFile.deleteOnExit();
	   
       return r_oResponse;
   }	
  

   /**
    * Save to file the stream data 
    * @param p_oReader read the response received
    * @return r_oFile the file to save
    * @throws IOException if any
    */
   private File inputStreamReaderToFile(Reader p_oReader, MContext p_oContext) throws RestException {
	   final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	   final int EOF = -1;

	   File r_oFile = null;
	   FileWriter outputStream = null;

	   try {
		   r_oFile = getFile("sync",".response", p_oContext);
		   outputStream = new FileWriter(r_oFile);
    
	       char[] cBuffer = new char[DEFAULT_BUFFER_SIZE];
	       int iCount = 0;
	       while (EOF != (iCount = p_oReader.read(cBuffer))) {
	    	   outputStream.write(cBuffer, 0, iCount);
	       }
	       outputStream.flush();
	       outputStream.close();
	    } catch (Exception oException) {
	    	throw new RestException("OkHttpRestInvoker inputStreamReaderToFile error", oException);
	    }
	
   	   return r_oFile;
   }
 
   /**
    * Get the file to save 
    * @param p_oPrefixeFile prefixe file
    * @param p_oPrefixeFile sufixe file
    * @return r_oFile the file to save
    * @throws IOException if any
    */
   public File getFile(String p_sFilePrefix, String p_sFileSuffix, MContext p_oContext) throws RestException {
 
	   File r_oFile = null;
		
	   // Create an image file name
	   String sDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	   StringBuilder sFileName= new StringBuilder();
	   sFileName.append(p_sFilePrefix).append("_").append(sDate).append(p_sFileSuffix);

	   // Directory File
	   File oDir = null;
	   String state = Environment.getExternalStorageState();
	   if (Application.getInstance().isSyncDebugEnabled() && Environment.MEDIA_MOUNTED.equals(state)) {
		   oDir = Environment.getExternalStorageDirectory();
	   }
	   else {
		   Context oContext = ((AndroidMMContext) p_oContext).getAndroidNativeContext();
		   oDir = oContext.getFilesDir();
	   }

	   // Create Temp File
	   r_oFile = new File(oDir, sFileName.toString());
	   
	   return r_oFile;
	}
  
   /**
    * Reads the error stream
    * @param p_oErrorStream the stream to read
    * @return the error found
    * @throws IOException if any
    */
   private String readErrorStream(InputStream p_oErrorStream ) throws IOException {
       BufferedReader br = new BufferedReader(new InputStreamReader(p_oErrorStream));
       StringBuilder sResponse = new StringBuilder();
       String sLine = null;
       while ((sLine = br.readLine()) != null){
           sResponse.append(sLine);
       }
       return sResponse.toString();
   }
	
	
	/** {@inheritDoc} */
	@Override
	public boolean isHttpsConnectionEnabled() {
		return false;
	}
	
	/**
	 * <p>createSslSocketFactory.</p>
	 *
	 * @return a {@link javax.net.ssl.SSLSocketFactory} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 */
	public SSLSocketFactory createSslSocketFactory() throws RestException {
		throw new NotImplementedException();
	}

	/**
	 * Return http client
	 * @return http client
	 */
	protected OkHttpClient getHttpClient() {
		return this.httpClient;
	}

	/**
	 * Return connection config
	 * @return connection config
	 */
	public RestConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}
	
	
}
