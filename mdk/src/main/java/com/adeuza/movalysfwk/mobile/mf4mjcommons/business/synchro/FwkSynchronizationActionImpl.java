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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask.ActionTaskStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoaderError;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.CredentialException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.IdentResult;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.LocalCredentialService;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.UserErrorMessages;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.network.ConnectionType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRequestWriter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.stream.StreamRequestWriter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.AckDomRequestBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.AckStreamRequestBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.ResponseProcessor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ISyncRestResponse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.ObjectToSynchronizeService;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SyncTimestampService;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 * <p>
 * 	Perform a synchronization.
 * 	The synchronization progress is notified to the user by a dialog box.
 * </p>
 * @param <R> the class to the rest response
 */
public class FwkSynchronizationActionImpl<R extends ISyncRestResponse> extends AbstractTaskableAction<SynchronizationActionParameterIN, SynchronizationActionParameterOUT, ActionTaskStep, Runnable>implements FwkSynchronizationAction {

	/** Entry point for web service */
	public static final String WS_ENTRYPOINT_PARAMETER = "ws_entrypoint";
	
	/** serial version of the class */
	private static final long serialVersionUID = 1920133088347277761L;
	
	/** Message id, provided by server, that indicates the authorization of the ressource failed. */
	public static final int UNAUTHORIZATION_MSG_ID = 3;
	
	/** Message id, provided by server, that indicates the Movalys licence is over.*/
	public static final int LICENCE_MSG_ID = 5;
	
	/** Message id provided by server , that indicates the time difference between the server and the mobile is too large an dblocked the synchro */ 
	public static final int ID_NOT_COMPATIBLE_MOBILE_TIME = 6;

	/** The RESTful service to call. */
	private String command ;
	
	/** Count of synchronisation steps. */
	private int synchroStepsCount = 2 ;

	/** Constant <code>SYNCHRO_MAIN_PROGRESSBAR=0</code> */
	protected static final int SYNCHRO_MAIN_PROGRESSBAR = 0 ;
	
	/** Constant <code>SYNCHRO_SUBSTEP_PROGRESSBAR=1</code> */
	protected static final int SYNCHRO_SUBSTEP_PROGRESSBAR = 1 ;
	
	/** notifier */
	private Notifier notifier = new Notifier();
	
	/** informations */
	private SynchronisationResponseTreatmentInformation informations = new SynchronisationResponseTreatmentInformation();
	
	/**
	 * Constructs a new <em>FwkSynchronizationActionImpl</em> Object.
	 */
	public FwkSynchronizationActionImpl() {
	}
	
	/** {@inheritDoc} */
	@Override
	public final Notifier getNotifier() {
		return this.notifier;
	}
	
	/**
	 * Get informations
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 */
	protected SynchronisationResponseTreatmentInformation getInformations() {
		return this.informations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPreExecuteDialog(MContext p_oContext, SynchronizationActionParameterIN p_oParameterIn) {
		try {
			boolean bWifiConnection = Application.getInstance().getConnectionType() == ConnectionType.WIFI;
			boolean bIsFirstSync = p_oParameterIn.firstSynchro;
			boolean bIsEmptyDB = !this.hasData(p_oContext);

			// Etat
			int iPropertiesState = -1;

			// Chargement et récupération de la propriété appropriée selon la situation
			if (bWifiConnection) {
				if (bIsEmptyDB) {
					iPropertiesState = ConfigurationsHandler.getInstance().getProperty(
							FwkPropertyName.synchronization_emptyDBSync_wifi).getIntValue();
				} else {
					if (bIsFirstSync) {
						iPropertiesState = ConfigurationsHandler.getInstance().getProperty(
								FwkPropertyName.synchronization_firstSync_wifi).getIntValue();
					} else {
						iPropertiesState = ConfigurationsHandler.getInstance().getProperty(
								FwkPropertyName.synchronization_otherSync_wifi).getIntValue();
					}
				}
			} else {
				if (bIsEmptyDB) {
					iPropertiesState = ConfigurationsHandler.getInstance().getProperty(
							FwkPropertyName.synchronization_emptyDBSync_3GGPRS).getIntValue();
				} else {
					if (bIsFirstSync) {
						iPropertiesState = ConfigurationsHandler.getInstance().getProperty(
								FwkPropertyName.synchronization_firstSync_3GGPRS).getIntValue();
					} else {
						iPropertiesState = ConfigurationsHandler.getInstance().getProperty(
								FwkPropertyName.synchronization_otherSync_3GGPRS).getIntValue();
					}
				}
			}
			// Un message Oui/Non conditionne le lancement de la synchro
			return iPropertiesState == 1;
		}
		catch (SynchroException e) {
			throw new MobileFwkException(e);
		}
	}

	/**
	 * Releases the business action. If an error occurs during treatment, error message must be added to the context.
	 *
	 * @param p_oContext the context to use
	 * @param p_oParameterIn the parameter to use
	 * @return result of business processing
	 */
	@Override
	public SynchronizationActionParameterOUT doAction(final MContext p_oContext, final SynchronizationActionParameterIN p_oParameterIn) {
		SynchronizationActionParameterOUT r_oResult = null;

		Application oApplication = Application.getInstance();

		//affichage de la notification annonçant le début de la synchro
		this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationStartNotifier(this.getNotifier()));

		if (oApplication.isConnectionReady()) {
			r_oResult = this.doSynchronization(p_oParameterIn, p_oContext );
		}
		else {

			//tentative d'identification local
			try {
				this.doPublishProgress(p_oContext,  ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationMessageNotifier(AbstractSubControllerSynchronization.SC_SYNCHRO_ERROR_KO_CONNECTED,
						BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));
				r_oResult = this.doLocalProcess(p_oContext, true);
			} catch(ActionException | SynchroException e ) {
				throw new MobileFwkException(e);
			}
		}

		if (r_oResult != null) {
			r_oResult.nextScreen = p_oParameterIn == null ? null : p_oParameterIn.nextScreen;
			r_oResult.informations = this.informations;
		}
		
		return r_oResult;
	}
	
	/**
	 * Launch synchronization
	 *
	 * @param p_oSynchronizationActionParameterIN a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN} object.
	 * @param p_oContext context
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT} object.
	 */
	protected SynchronizationActionParameterOUT doSynchronization( SynchronizationActionParameterIN p_oSynchronizationActionParameterIN, MContext p_oContext ) {

		SynchronizationActionParameterOUT r_oResult = null;

		Application oApplication = Application.getInstance();

		try {
			this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationProgressNotifier(this.getNotifier(), SYNCHRO_MAIN_PROGRESSBAR,
					DefaultApplicationR.synchronisation_beforepreparesynchro));

			// Get last synchronisation dates for referential entities
			SyncTimestampService oSyncTimestampService = BeanLoader.getInstance().getBean(SyncTimestampService.class);
			Map<String, Long> mapSyncTimestamp = oSyncTimestampService.getSyncTimestamps(p_oContext);

			// Get objects to synchronized
			ObjectToSynchronizeService oObjectToSynchronizeService =
					(ObjectToSynchronizeService) BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class);
			Map<String, List<MObjectToSynchronize>> mapCurrentObjectsToSynchronize = oObjectToSynchronizeService.getMapObjectToSynchronize(p_oContext);

			// Get Invocations
			List<RestInvocationConfig<R>> listInvocationParams = this.getInvocationConfigs( mapSyncTimestamp, mapCurrentObjectsToSynchronize);
			Application.getInstance().getLog().debug(Application.LOG_TAG, "invocation counts: " + listInvocationParams.size());

			if ( !listInvocationParams.isEmpty()) {
				// Configuration for connection to rest service
				RestConnectionConfig oRestConnectionConfig = this.getConnectionConfig(p_oSynchronizationActionParameterIN);

				oRestConnectionConfig.setMockMode(ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_mock_mode).getBooleanValue());
				oRestConnectionConfig.setMockStatusCode(ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_mock_testid).getIntValue());

				// Get rest invoker
				IRestInvoker<R> oRestInvoker = null;

				R oResponse = null ;
				R oOldResponse = null;
				List<MObjectToSynchronize> listSynchronizedObjects = new ArrayList<MObjectToSynchronize>();

				for( RestInvocationConfig<R> oInvocationConfig : listInvocationParams ) {
					oOldResponse = null;
					while(oOldResponse==null || (!oOldResponse.isTerminated()) || (oOldResponse.hasAcks())) {

						// Update the number of steps of the main progress bar (now we know how many invocation)
						this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationProgressNotifier(this.getNotifier(), SYNCHRO_MAIN_PROGRESSBAR, this.synchroStepsCount + 3,
								DefaultApplicationR.synchronisation_beforepreparesynchro));

						oRestInvoker = BeanLoader.getInstance().getBean(IRestInvoker.class);
						oRestInvoker.init(this, oRestConnectionConfig);

						// Add parameters to url
						this.appendGetParameters(p_oSynchronizationActionParameterIN, oRestInvoker);

						oRestInvoker.prepare();
						mapSyncTimestamp = oSyncTimestampService.getSyncTimestamps(p_oContext);
						mapCurrentObjectsToSynchronize = oObjectToSynchronizeService.getMapObjectToSynchronize(p_oContext);
						
						if (oOldResponse!=null && oOldResponse.hasAcks()) {
							if (oInvocationConfig.getRequestWriter() instanceof StreamRequestWriter) {
								((StreamRequestWriter)oInvocationConfig.getRequestWriter()).addRestBuilder(
									new AckStreamRequestBuilder(oOldResponse.getAcks()));
							}
							else if (oInvocationConfig.getRequestWriter() instanceof DomRequestWriter) {
								((DomRequestWriter)oInvocationConfig.getRequestWriter()).addRequestBuilder(
									new AckDomRequestBuilder(oOldResponse.getAcks()));
							}
						}

						Application.getInstance().getLog().debug(Application.LOG_TAG, "synchro: start invocation");

						this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationProgressNotifier(this.getNotifier(), SYNCHRO_MAIN_PROGRESSBAR, 
								Application.getInstance().getStringResource(DefaultApplicationR.synchronisation_beforepreparedata)
								+ "(" + oInvocationConfig.getMessage() + ")" ));

						oInvocationConfig.getRequestWriter().prepare(
								oOldResponse == null, //indique si c'est le premier allez vers le serveur
								mapCurrentObjectsToSynchronize,
								mapSyncTimestamp,
								listSynchronizedObjects,
								p_oSynchronizationActionParameterIN,
								p_oContext);

						oResponse = this.doInvocation(oInvocationConfig, oRestConnectionConfig, oRestInvoker, p_oContext);

						// MAJ des informations pour le refresh des listes
						this.informations.complete(oResponse.getInformations());
						
						oOldResponse = oResponse;
						// Success
						if (!oResponse.isError()) {
							this.doOnSuccessInvocation(oResponse, oInvocationConfig, this.informations, listSynchronizedObjects, p_oContext);
							
							// refresh for next invocation
							mapSyncTimestamp = oSyncTimestampService.getSyncTimestamps(p_oContext);
							if (!oOldResponse.isTerminated() || (oOldResponse.hasAcks())) {
								oResponse = null;
							}
							//else {
								// on va sortir de la boucle on garde la dernière réponse
							//}
							
							Application.getInstance().getLog().debug(Application.LOG_TAG, "synchro: invocation succeeded");
						}
						// Error
						else {
							r_oResult = this.doOnFailedInvocation(oResponse, oInvocationConfig, this.informations, p_oContext);
							Application.getInstance().getLog().debug(Application.LOG_TAG, "synchro: invocation returns error");
							// don't execute next invocations
							break ;
						}
						this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationResetNotifier(this.getNotifier()));
					}
					if (oResponse.isError()) {
						// don't execute next invocations
						break ;
					}
				}

				
				if (!oResponse.isError()) {
					
					doCustomInvocations(p_oSynchronizationActionParameterIN, oResponse, p_oContext);
					
					if (!oResponse.isError()) {
						r_oResult = this.doOnSuccessSynchro( oResponse, oRestConnectionConfig, p_oContext );
					}
				}
			}
			else {
				r_oResult = new SynchronizationActionParameterOUT();
			}
			
		} catch ( RestException oException ) {
			oApplication.getLog().error(Application.LOG_TAG, "synchro: Erreur", oException );

			//A2A_DEV à faire mieux ... on doit avoir une vrai erreur quelque part ...
			// le serveur provoque une erreur on inidque que le serveur n'est pas disponible ou qu'il provoque une erreur
			Throwable oCause = oException.getCause();
			if (oCause != null && oCause instanceof UnknownHostException
					|| oCause.getMessage().indexOf("timed out") != -1
					|| oCause.getMessage().indexOf("refused at") != -1
					|| "Bad address family".equals(oCause.getMessage())
					|| "Bad Request".equals(oCause.getLocalizedMessage())) {

				r_oResult = this.doOnServerNotFound(p_oContext, p_oSynchronizationActionParameterIN);
			}
			else {
				r_oResult = this.doOnSynchroCrash(p_oContext, p_oSynchronizationActionParameterIN);
			}
		}
		catch (Exception e) {
			oApplication.getLog().error(Application.LOG_TAG, "synchro: Erreur", e );
			r_oResult = this.doOnSynchroCrash(p_oContext, p_oSynchronizationActionParameterIN);
		}

		return r_oResult ;
	}
	
	/**
	 * Called on successful invocation
	 * @param p_oSynchronizationActionParameterIN the parameters send in entry
	 * @param oResponse the response from the server
	 * @param p_oContext the context to use
	 */
	protected void doCustomInvocations( SynchronizationActionParameterIN p_oSynchronizationActionParameterIN, R oResponse, MContext p_oContext) {
		
	}
	
	/**
	 * return the connection configuration for this action
	 * @param p_oSynchronizationActionParameterIN the parameters send in entry
	 * 
	 * @return the connection
	 * @throws java.lang.Exception if any.
	 */
	protected RestConnectionConfig getConnectionConfig(SynchronizationActionParameterIN p_oSynchronizationActionParameterIN) throws Exception {
		 return new RestConnectionConfig(
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_URLSERVER_HOST), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_URLSERVER_PORT), 
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_URLSERVER_PATH), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_URLSERVER_TIMEOUT), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_URLSERVER_SOTIMEOUT), 
					Application.getInstance().getWsEntryPoint(), 
					this.command, 
					Application.getInstance().getStringSetting(Application.SETTING_LOGIN), 
					Application.getInstance().getDecriptedStringSetting(Application.SETTING_PASSWORD), 
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_PROXYURLSERVER_HOST), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_PROXYURLSERVER_PORT), 
					Application.getInstance().getStringSetting(Application.SETTING_PROXYLOGIN), 
					Application.getInstance().getStringSetting(Application.SETTING_PROXYPASSWORD));
	}

	/**
	 * Called when all invocations have been passed with success
	 *
	 * @param p_oResponse response
	 * @param p_oRestConnectionConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig} object.
	 * @param p_oContext context
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException if any.
	 */
	protected SynchronizationActionParameterOUT doOnSuccessSynchro(R p_oResponse, 
			RestConnectionConfig p_oRestConnectionConfig, MContext p_oContext) throws SynchroException {
		
		SynchronizationActionParameterOUT r_oSynchronizationActionParameterOUT = new SynchronizationActionParameterOUT();
		
		// Save credential in local storage
		try {
			BeanLoader.getInstance().getBean(LocalCredentialService.class).storeCredentials(
					p_oRestConnectionConfig.getUser(), p_oResponse.getResource(), p_oContext);
		} catch (CredentialException oCredentialException ) {
			throw new SynchroException(oCredentialException);
		}
		
		return r_oSynchronizationActionParameterOUT ;
	}

	/**
	 * <p>getInvocationConfigs.</p>
	 *
	 * @param p_oMapcurrentObjectsToSynchronize a {@link java.util.Map} object.
	 * @param p_mapSyncTimestamp a {@link java.util.Map} object.
	 * @return a {@link java.util.List} object.
	 */
	protected List<RestInvocationConfig<R>> getInvocationConfigs(Map<String, Long> p_mapSyncTimestamp, Map<String, List<MObjectToSynchronize>> p_oMapcurrentObjectsToSynchronize) {
		return new ArrayList<RestInvocationConfig<R>>();
	}

	/**
	 * <p>appendGetParameters.</p>
	 *
	 * @param p_oRestInvoker a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker} object.
	 */
	protected void appendGetParameters(SynchronizationActionParameterIN p_oSynchronizationActionParameterIN, IRestInvoker<R> p_oRestInvoker) {
		String sLogin = Application.getInstance().getStringSetting(Application.SETTING_LOGIN);
		String sMobileId = String.valueOf( Application.getInstance().getUniqueId());
		
		// Case-sensitive login fwk property
		Property oCaseSensitiveLoginProperty = ConfigurationsHandler.getInstance().getProperty(
				FwkPropertyName.case_sensitive_login);
		
		String sUrlLogin = sLogin.toLowerCase();
		if (oCaseSensitiveLoginProperty != null && Property.TRUE.equals(oCaseSensitiveLoginProperty.getValue())) {
			// Case-sensitive login
			sUrlLogin = sLogin;
		}
		p_oRestInvoker.appendGetParameter( (sUrlLogin + "*" + sMobileId).hashCode());
	}

	
	/**
	 * <p>doInvocation.</p>
	 *
	 * @param p_oInvocationParams a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oRestInvoker a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker} object.
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @param p_oConnectionConfig a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 * @return a R object.
	 */
	protected R doInvocation( RestInvocationConfig<R> p_oInvocationParams,
			RestConnectionConfig p_oConnectionConfig,
			IRestInvoker<R> p_oRestInvoker, MContext p_oContext ) throws RestException {

		p_oRestInvoker.invoke(p_oInvocationParams, p_oContext);

		// process the response
		this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationProgressNotifier(this.getNotifier(), SYNCHRO_MAIN_PROGRESSBAR,
				Application.getInstance().getStringResource( DefaultApplicationR.synchronisation_waitserver) + "(" + p_oInvocationParams.getMessage() + ")"));

		return p_oRestInvoker.process(p_oInvocationParams, p_oConnectionConfig, this.getNotifier(), p_oContext );
	}
	
	/**
	 * <p>doOnSuccessInvocation.</p>
	 *
	 * @param p_oResponse a R object.
	 * @param p_oInvocationParams a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oInformation a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 * @param p_listAckSynchronizedObjects a {@link java.util.List} object.
	 * @param p_oContext context
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException if any.
	 */
	protected void doOnSuccessInvocation( R p_oResponse, RestInvocationConfig<R> p_oInvocationParams, 
			SynchronisationResponseTreatmentInformation p_oInformation, 
			List<MObjectToSynchronize> p_listAckSynchronizedObjects, MContext p_oContext ) throws SynchroException {
		
		try {
			SyncTimestampService oSyncTimestampService = BeanLoader.getInstance().getBean(SyncTimestampService.class);

			// initialize progress handler
			this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchroSubStartNotifier(p_oInvocationParams.getResponseProcessors().size()));

			// Synchronisation timestamp for referential entities
			Map<String,Long> mapSyncTimestamp = new HashMap<String, Long>();

			// Launch response processors
			for( ResponseProcessor<R> oResponseProcessor: p_oInvocationParams.getResponseProcessors()) {
				this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationProgressNotifier(this.getNotifier(), SYNCHRO_SUBSTEP_PROGRESSBAR, oResponseProcessor.getMessage()));
				Application.getInstance().getLog().debug(Application.LOG_TAG,"synchro: Launch response processor: " + oResponseProcessor.getClass().getSimpleName());
				oResponseProcessor.processResponse(p_oResponse, mapSyncTimestamp, p_oContext, p_oInformation);
			}

			// Save synchronisation timestamps
			oSyncTimestampService.saveSyncTimestamps(mapSyncTimestamp, p_oContext);

			// Delete ack synchronized objets
			BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).deleteObjectToSynchronize(
					p_listAckSynchronizedObjects, p_oContext);
			p_listAckSynchronizedObjects.clear();

			// Validate transaction
			// TODO : faire autrement
			try {
				Method oGetTransaction = p_oContext.getClass().getMethod("getTransaction");
				Object oTransaction = oGetTransaction.invoke(p_oContext);
				Method oCommit = oTransaction.getClass().getMethod("commit");
				oCommit.invoke(oTransaction);
			} catch (NoSuchMethodException | SecurityException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Application.getInstance().getLog().debug("doOnSuccessInvocation", e.getMessage());
			}

		} catch ( SynchroException oException ) {
			throw new SynchroException(oException);
		}
	}
	
	/**
	 * <p>doOnFailedInvocation.</p>
	 *
	 * @param p_oResponse a R object.
	 * @param p_oInvocationParams a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig} object.
	 * @param p_oInformation a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	protected SynchronizationActionParameterOUT doOnFailedInvocation( R p_oResponse, RestInvocationConfig<R> p_oInvocationParams, 
			SynchronisationResponseTreatmentInformation p_oInformation, MContext p_oContext ) throws ActionException {
		
		SynchronizationActionParameterOUT r_oSynchronizationActionParameterOUT = null ;
		try {
		
			switch (p_oResponse.getIdMessage()) {
				case UNAUTHORIZATION_MSG_ID:
					this.doOnAuthenticationFailed(p_oContext);
					r_oSynchronizationActionParameterOUT = new SynchronizationActionParameterOUT();
					r_oSynchronizationActionParameterOUT.authenticationFailure = true;
					break;

				case LICENCE_MSG_ID:
					this.doOnLicenceAccessFailed(p_oContext);
					break;

				case ID_NOT_COMPATIBLE_MOBILE_TIME:
					this.doOnIncompatibleMobileTime(p_oContext);
					r_oSynchronizationActionParameterOUT = new SynchronizationActionParameterOUT();
					r_oSynchronizationActionParameterOUT.incompatibleServerMobileTimeFailure = true;
					break;

				case AbstractSubControllerSynchronization.STATE_SYNC_KO_JOIN_SERVER:
					r_oSynchronizationActionParameterOUT = this.doOnServerNotFound(p_oContext, null);
					break;

				default:
					//génération d'une notification d'erreur pour indiquer que la synchro ne fonctionne pas
					try {
						this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationMessageNotifier(AbstractSubControllerSynchronization.SC_SYNCHRO_CRASH,
								BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));
					} catch (SynchroException | BeanLoaderError e) {
						Application.getInstance().getLog().error("doOnFailedInvocation", e.getMessage(), e);
					}

					//tentative d'identification local
					r_oSynchronizationActionParameterOUT = this.doLocalProcess(p_oContext, true);
					
					// Flag : erreur dans la synchro
					r_oSynchronizationActionParameterOUT.errorInSynchronizationFailure = true;
					break;
			}
		} catch( CredentialException oException ) {
			throw new ActionException(oException);
		}
		return r_oSynchronizationActionParameterOUT ;
	}

	/**
	 * Action success
	 * Notifies the end of the synchronisation with a success.
	 *
	 * @param p_oContext context
	 * @param p_oParameterIn a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN} object.
	 * @param p_oResultOut a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT} object.
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, SynchronizationActionParameterIN p_oParameterIn, SynchronizationActionParameterOUT p_oResultOut) {
		
		try {
			// Flag indiquant si la base est vide. Peut être nul si pas d'implémentation.
			Boolean oIsEmptyDB = !this.hasData(p_oContext);
			boolean bExitAppOnSynchroFailure = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.synchronization_exitAppOnFirstSynchroFailure).getBooleanValue();

			if (oIsEmptyDB != null && oIsEmptyDB.booleanValue()) {
				Property oSyncEmptyDBfailurePopupProperty = ConfigurationsHandler.getInstance().getProperty(
						FwkPropertyName.synchronization_emptyDB_failure_popup);

				if (bExitAppOnSynchroFailure && oSyncEmptyDBfailurePopupProperty != null 
						&& Property.TRUE.equals(oSyncEmptyDBfailurePopupProperty.getValue())) {
					// Suppression des redirections
					p_oParameterIn.getRedirectActions().clear();

					// Flag : la synchro est terminée (réussite/échec) mais la base est vide
					if (p_oResultOut != null) {
						p_oResultOut.emptyDBSynchronizationFailure = true;
					}
				}
			}

			if ( p_oResultOut != null && !p_oResultOut.authenticationFailure ) {
				if (p_oResultOut.localAuthentication) {
					this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationMessageNotifier(AbstractSubControllerSynchronization.SC_EXECUTE_OBJECT_TO_SYNCHRONIZE_ACTION,
							BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));
				}
				else { // seul cas ou c'est vraiment un succès complet
					this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationMessageNotifier(AbstractSubControllerSynchronization.SC_SYNCHRO_SUCCESS,
							BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));
				}
			}
		} catch (SynchroException oSynchroException ) {
			throw new MobileFwkException(oSynchroException);
		}
	}

	/**
	 * Action error
	 * Notify the end of the synchronisation with an error.
	 *
	 * @param p_oContext the context to use
	 * @param p_oParameterIn the parameter used by doAction
	 * @param p_oResultOut the result of the business processing
	 * @return the modified result of the business processing
	 */
	@Override
	public SynchronizationActionParameterOUT doOnError(MContext p_oContext, SynchronizationActionParameterIN p_oParameterIn, SynchronizationActionParameterOUT p_oResultOut) {

		try {
			// Flag indiquant si la base est vide. Peut être nul si pas d'implémentation.
			Boolean oIsEmptyDB = !this.hasData(p_oContext);
			boolean bExitAppOnSynchroFailure = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.synchronization_exitAppOnFirstSynchroFailure).getBooleanValue();

			if (bExitAppOnSynchroFailure && oIsEmptyDB != null && oIsEmptyDB.booleanValue()) {
				Property oSyncEmptyDBfailurePopupProperty = ConfigurationsHandler.getInstance().getProperty(
						FwkPropertyName.synchronization_emptyDB_failure_popup);

				if (oSyncEmptyDBfailurePopupProperty != null 
						&& Property.TRUE.equals(oSyncEmptyDBfailurePopupProperty.getValue())) {
					// Suppression des redirections
					p_oParameterIn.getRedirectActions().clear();

					if (p_oResultOut == null) {
						p_oResultOut = new SynchronizationActionParameterOUT();
					}
					// Flag : la synchro est terminée (réussite/échec) mais la base est vide
					p_oResultOut.emptyDBSynchronizationFailure = true;
				}
			}
		} catch (SynchroException oSynchroException ) {
			throw new MobileFwkException(oSynchroException);
		}
		return p_oResultOut;
	}

	/**
	 * Internal synchronisation process.
	 *
	 * @param p_oContext context to use
	 * @param p_bFirstFailure a boolean.
	 * @return the object returned by the synchronization action 
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException if any.
	 */
	protected SynchronizationActionParameterOUT doLocalProcess(MContext p_oContext, boolean p_bFirstFailure) throws ActionException {
		SynchronizationActionParameterOUT r_oResult = null;
		try {
		
			String sLogin = Application.getInstance().getStringSetting(Application.SETTING_LOGIN);
			IdentResult oIdentifyResult = BeanLoader.getInstance().getBean(LocalCredentialService.class).doIdentify(sLogin, p_oContext );
			if (IdentResult.ok==oIdentifyResult) {
				r_oResult = new SynchronizationActionParameterOUT();
				r_oResult.resetObjectToSynchronise = !p_bFirstFailure;
				r_oResult.localAuthentication = true;
			}
			else if (IdentResult.koCauseOfDate==oIdentifyResult){
				r_oResult = new SynchronizationActionParameterOUT();
				r_oResult.waitedTooLongBeforeSync=true;
				p_oContext.getMessages().addMessage(UserErrorMessages.FWK_MOBILE_UE_0001);
			}
			else {
				p_oContext.getMessages().addMessage(UserErrorMessages.FWK_MOBILE_UE_0001);
			}
		} catch( CredentialException oException ) {
			throw new ActionException(oException);
		}
		return r_oResult;
	}

	/**
	 * Called when the curent user has not been authenticated.
	 * @param p_oContext The current context. Never null.
	 */
	private void doOnAuthenticationFailed(MContext p_oContext) throws CredentialException {
		BeanLoader.getInstance().getBean(LocalCredentialService.class).deleteLocalCredentials(p_oContext);
		try {
			Application.getInstance().getController().manageSynchronizationActions(AbstractSubControllerSynchronization.SC_SYNCHRO_ERROR_KO_AUTHENTIFICATION,
					new ManageSynchronizationParameter(BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));
		} catch (SynchroException | BeanLoaderError e) {
			Application.getInstance().getLog().error("doOnAuthenticationFailed", e.getMessage(), e);
		}
	}

	/**
	 * <p>doOnSynchroCrash.</p>
	 *
	 * @param p_oContext context
	 * @param p_oParameterIn a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT} object.
	 */
	protected SynchronizationActionParameterOUT doOnSynchroCrash(final MContext p_oContext, final SynchronizationActionParameterIN p_oParameterIn) {
		SynchronizationActionParameterOUT r_oResult = null;
		try {
			//génération d'une notification d'erreur pour indiquer que la synchro ne fonctionne pas
			this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationMessageNotifier(AbstractSubControllerSynchronization.SC_SYNCHRO_CRASH,
					BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));

			//tentative d'identification local
			r_oResult = this.doLocalProcess(p_oContext, true);

			// Flag : interruption de la synchro
			if (r_oResult == null) {
				r_oResult = new SynchronizationActionParameterOUT();
				r_oResult.brokenSynchronizationFailure = true;
			}
		}
		catch (Exception e) {
			Application.getInstance().getLog().error(Application.LOG_TAG, "Error in doOnSynchroCrash", e);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}
		return r_oResult;
	}

	/**
	 * <p>doOnServerNotFound.</p>
	 *
	 * @param p_oContext context
	 * @param p_oParameterIn a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterOUT} object.
	 */
	protected SynchronizationActionParameterOUT doOnServerNotFound(final MContext p_oContext, final SynchronizationActionParameterIN p_oParameterIn) {
		SynchronizationActionParameterOUT r_oResult = null;
		try {
			this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationMessageNotifier(AbstractSubControllerSynchronization.SC_SYNCHRO_ERROR_KO_JOIN_SERVER,
					BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));

			Boolean oIsEmptyDB = !this.hasData(p_oContext);

			if (oIsEmptyDB == null || (oIsEmptyDB != null && oIsEmptyDB.booleanValue())) {
				// Flag : base vide
				r_oResult = new SynchronizationActionParameterOUT();
				r_oResult.emptyDBSynchronizationFailure = true;
			} else {
				//tentative d'identification local
				r_oResult = this.doLocalProcess(p_oContext, true);

				if (r_oResult == null) {
					// Flag : impossibilité de joindre le serveur
					r_oResult = new SynchronizationActionParameterOUT();
					r_oResult.noConnectionSynchronizationFailure = true;
				}
			}
		}
		catch (Exception e) {
			Application.getInstance().getLog().error(Application.LOG_TAG, "Error in doOnServerNotFound", e);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}

		return r_oResult;
	}
	
	/**
	 * Called when the licence is over for Android
	 * @param p_oContext The current application context. Never null.
	 */
	private void doOnLicenceAccessFailed(final MContext p_oContext){
		try {
			this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationMessageNotifier(AbstractSubControllerSynchronization.SC_SYNCHRO_ERROR_KO_LICENCE,
					BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));
		} catch (SynchroException | BeanLoaderError e) {
			Application.getInstance().getLog().error("doOnLicenceAccessFailed", e.getMessage(), e);
		}
	}
	/**
	 * Called when the mobile time is incompatible with the server time
	 * @param p_oContext The current application context. Never null.
	 */
	private void doOnIncompatibleMobileTime(final MContext p_oContext){
		try {
			this.doPublishProgress(p_oContext, ActionTaskStep.PROGRESS_RUNNABLE, new SynchronizationMessageNotifier(AbstractSubControllerSynchronization.SC_SYNCHRO_ERROR_KO_INCOMPATIBLE_SERVER_TIME,
					BeanLoader.getInstance().getBean(ObjectToSynchronizeService.class).isThereDataToSynchronize(p_oContext)));
		} catch (SynchroException | BeanLoaderError e) {
			Application.getInstance().getLog().error("doOnIncompatibleMobileTime", e.getMessage(), e);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void doPostExecute(MContext p_oContext, SynchronizationActionParameterIN p_oParameterIn, SynchronizationActionParameterOUT p_oParameterOut) throws ActionException {
		// LMI Correction DataBase is locked
		SynchronizationStopNotifier oSynchronizationStopNotifier = new SynchronizationStopNotifier(p_oContext, this.getNotifier(), this.informations);
		oSynchronizationStopNotifier.run();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(SynchronizationActionParameterIN p_oParameterIn, MContext p_oContext) throws ActionException {
		//Nothing to do
	}

	/** {@inheritDoc} */
	@Override
	public int getConcurrentAction() {
		return (Application.getInstance().isSyncTransparentEnabled() ?Action.SYNCHRO_QUEUE :Action.DEFAULT_QUEUE);
	}
	
	/** {@inheritDoc} */
	@Override
	public SynchronizationActionParameterIN getEmptyInParameter() {
		return new SynchronizationActionParameterIN();
	}

	/** {@inheritDoc} */
	@Override
	public void setSyncCommand(String p_oRestCommandName) {
		this.command = p_oRestCommandName;
	}

	/**
	 * Checks wether there is data in the database
	 *
	 * @param p_oContext Context to use
	 * @return <code>true</code> if the database is empty ; <code>false</code> otherwise
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException if any.
	 */
	public boolean hasData(MContext p_oContext) throws SynchroException {
		return Application.getInstance().getController().hasData(p_oContext);
	}

	private class SynchroSubStartNotifier implements Runnable {

		private int maxStep;

		public SynchroSubStartNotifier(int p_iMaxStep) {
			this.maxStep = p_iMaxStep;
		}

		/**
		 * {@inheritDoc}
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Application.getInstance().getController().notifySynchronizationProcessStart(FwkSynchronizationActionImpl.this.getNotifier(), SYNCHRO_SUBSTEP_PROGRESSBAR, 0, this.maxStep);
		}
	}
	
	@Override
	public String[] getRequieredPermissions() {
		return new String[] {
				Manifest.permission.WAKE_LOCK,
				Manifest.permission.ACCESS_NETWORK_STATE,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.ACCESS_WIFI_STATE
		};
	}

}
