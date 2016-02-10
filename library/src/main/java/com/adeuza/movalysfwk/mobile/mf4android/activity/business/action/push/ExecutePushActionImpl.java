package com.adeuza.movalysfwk.mobile.mf4android.activity.business.action.push;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.PushParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.push.ExecutePushAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.FwkSynchronizationActionImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.UserErrorMessages;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvoker;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRequestWriter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.dom.DomRestResponseReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.PushRestRequest;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.PushRestResponse;

/**
 * <p>Implements the action to execute a push on Movalys.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (1 déc. 2010)
 */
public class ExecutePushActionImpl implements ExecutePushAction {

	/** serial id */
	private static final long serialVersionUID = -8936879959625843840L;
	
	/** the name of service rest to use */
	private static final String COMMAND = "pushsrv/getpush";
	
	/**
	 * response of synchro
	 */
	private SynchronisationResponseTreatmentInformation information = new SynchronisationResponseTreatmentInformation();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl getEmptyInParameter() {
		return new NullActionParameterImpl();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public PushParameter doAction(MContext p_oContext, NullActionParameterImpl p_oParameterIn) {
		//objet de paramétrage à retourner si l'action réussis
		PushParameter r_oParameter = new PushParameter();
		RestConnectionConfig oRestConnectionConfig = null ;

		try {
			//on test si le mobile arrive à capter le réseaux 
			if (Application.getInstance().isConnectionReady()) {

				oRestConnectionConfig = new RestConnectionConfig(
						Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_URLSERVER_HOST), 
						Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_URLSERVER_PORT), 
						Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_URLSERVER_PATH), 
						Application.getInstance().getWsEntryPoint(), 
						COMMAND, 
						Application.getInstance().getStringSetting(Application.SETTING_LOGIN), 
						Application.getInstance().getDecriptedStringSetting(Application.SETTING_PASSWORD), 
						Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_PROXYURLSERVER_HOST), 
						Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_PROXYURLSERVER_PORT), 
						Application.getInstance().getStringSetting(Application.SETTING_PROXYLOGIN), 
						Application.getInstance().getStringSetting(Application.SETTING_PROXYPASSWORD));
				
				//invocation du service rest
				RestInvoker<PushRestResponse> oRestInvoker = new RestInvoker<>();
				oRestInvoker.init(this, oRestConnectionConfig);

				RestInvocationConfig<PushRestResponse> oInvocationParameters = this.getInvocationConfigs();
				if (oInvocationParameters != null) {
					//récupération du résultatDomRequestBuilder
					oRestInvoker.prepare();
					oRestInvoker.invoke(oInvocationParameters, p_oContext);
					this.information.clear();
					PushRestResponse oResponse = oRestInvoker.process(oInvocationParameters, oRestConnectionConfig, new Notifier(), p_oContext );
					if (oResponse.isError()) {
						if (FwkSynchronizationActionImpl.UNAUTHORIZATION_MSG_ID == oResponse.getIdMessage()) {
							Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_PUSH_ERROR_KO_AUTHENTIFICATION);
						}
						else if (FwkSynchronizationActionImpl.LICENCE_MSG_ID == oResponse.getIdMessage()) {
							Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_PUSH_ERROR_KO_AUTHENTIFICATION);
						}
						else {
							Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_PUSH_CRASH);
						}
						p_oContext.getMessages().addMessage(UserErrorMessages.FWK_MOBILE_UE_0002); 
						Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_3501, 
								StringUtils.concat(ErrorDefinition.FWK_MOBILE_E_3501_LABEL, ',', oRestConnectionConfig.getHost(), Long.toString(oResponse.getIdMessage())));
					}
					else{
						r_oParameter.setDataToSynchronize(oResponse.push.state);
					}
				}
			}
		} catch (RestException oException) {
			p_oContext.getMessages().addMessage(UserErrorMessages.FWK_MOBILE_UE_0001); 
			String hostName = "";
			if(oRestConnectionConfig != null && oRestConnectionConfig.getHost() !=null){
				hostName = oRestConnectionConfig.getHost();
			}
			Application.getInstance().getLog().error(
				ErrorDefinition.FWK_MOBILE_E_3500, 
				StringUtils.concat(ErrorDefinition.FWK_MOBILE_E_3500_LABEL, ',',hostName )
			);
			Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_PUSH_ERROR_KO_JOIN_SERVER);
		} catch (Exception oException) {
			Application.getInstance().getLog().error("ExecutePushActionImpl", "Erreur", oException );
			Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_PUSH_ERROR_KO_JOIN_SERVER);
		}
		

		return r_oParameter;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, PushParameter p_oResultOut) {
		//lancement de la notification adaptée
		if(p_oResultOut.isDataToSynchronize()){
			Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_PUSH_SUCCESS_WITH_RESPONSE);
		}else{
			Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_PUSH_SUCCESS_WITHOUT_RESPONSE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PushParameter doOnError(MContext p_oContext, NullActionParameterImpl p_oParameterIn, PushParameter p_oResultOut) {
		// Nothing to do
		return p_oResultOut;
	}

	/**
	 * @return invocation config
	 */
	protected RestInvocationConfig<PushRestResponse> getInvocationConfigs() {
		RestInvocationConfig<PushRestResponse> r_oPushInvocationConfig = new RestInvocationConfig<>();

		r_oPushInvocationConfig.setRequestWriter(new DomRequestWriter<>(PushRestRequest.class));

		r_oPushInvocationConfig.setResponseReader(new DomRestResponseReader<>(PushRestResponse.class));

		return r_oPushInvocationConfig;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritableDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConcurrentAction() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(NullActionParameterImpl p_oIn, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, NullActionParameterImpl p_oParameterIn, PushParameter p_oParameterOut)
			throws ActionException {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESS[])
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void... p_oProgressInformations) {
		//Nothing to do
	}
}
