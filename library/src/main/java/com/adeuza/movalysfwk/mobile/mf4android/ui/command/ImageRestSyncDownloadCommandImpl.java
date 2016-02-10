package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import org.apache.commons.codec.binary.Base64;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MMediaStoreImageService;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.UserErrorMessages;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.IRestInvoker;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestConnectionConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.RestInvocationConfig;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequest;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom.DomRequestWriter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.dom.DomRestResponseReader;

/**
 * <p>
 * Download a image with a direct http connection without authorization
 * </p>
 * 
 * <p>
 * Copyright (c) 2012
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author spacreau
 * 
 */
public class ImageRestSyncDownloadCommandImpl extends AbstractTaskableAction<ImageDownloadParameterIn, BitmapParameterOUT, DefaultActionStep, Void> implements ImageDownloadCommand {
	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = 9187336149611300279L;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doAction(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public BitmapParameterOUT doAction(MContext p_oContext, ImageDownloadParameterIn p_oParameterIn) {
		
		Log.d("image", "ImageRestSyncDownloadCommandImpl : downloadImage " + p_oParameterIn.getId());
		try {
			// InvocationConfig pour la synchro classic
			RestInvocationConfig<RestImageResponse> oPhotoRestInvocationConfig = new RestInvocationConfig<>();
			oPhotoRestInvocationConfig.setMessage("photo");

			DomRequestWriter<RestRequest> oClassicRequestWriter = new DomRequestWriter<>(RestRequest.class);
			DomRestResponseReader<RestImageResponse> oVDNRestResponseReader = new DomRestResponseReader<>(RestImageResponse.class);
			
			oPhotoRestInvocationConfig.setRequestWriter(oClassicRequestWriter);
			oPhotoRestInvocationConfig.setResponseReader(oVDNRestResponseReader);
			
			RestConnectionConfig oRestConnectionConfig = new RestConnectionConfig(
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_URLSERVER_HOST), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_URLSERVER_PORT),
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_URLSERVER_PATH), 
					Application.getInstance().getWsEntryPoint(), 
					Application.getInstance().getStringResource( FwkPropertyName.url_downloadImage ) , 
					Application.getInstance().getStringSetting(Application.SETTING_LOGIN),
					Application.getInstance().getDecriptedStringSetting(Application.SETTING_PASSWORD), 
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_PROXYURLSERVER_HOST), 
					Application.getInstance().getIntSetting( Application.SETTING_COMPUTE_PROXYURLSERVER_PORT), 
					Application.getInstance().getStringSetting( Application.SETTING_PROXYLOGIN), Application.getInstance().getStringSetting(Application.SETTING_PROXYPASSWORD));
			oRestConnectionConfig.setMockMode(ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_mock_mode).getBooleanValue());
			oRestConnectionConfig.setMockStatusCode(ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_mock_testid).getIntValue());
			
			// Get rest invoker
			IRestInvoker<RestImageResponse> oRestInvoker = BeanLoader.getInstance().getBean(IRestInvoker.class);
			oRestInvoker.init(this, oRestConnectionConfig);

			oRestInvoker.appendGetParameter( p_oParameterIn.getId().substring("remote://".length()));
			// Add parameters to url
			oRestInvoker.prepare();

			oRestInvoker.invoke(oPhotoRestInvocationConfig, p_oContext);
			RestImageResponse oRestResponse = oRestInvoker.process( oPhotoRestInvocationConfig, oRestConnectionConfig,  
					Application.getInstance().getNotifier(),p_oContext);
			Log.e(Application.LOG_TAG, "ImageRestSyncDownloadCommandImpl : error: " + oRestResponse.isError() );
			if (oRestResponse.isError()) {
				p_oContext.getMessages().addMessage(UserErrorMessages.FWK_MOBILE_UE_0002);
				Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_3501,
						StringUtils.concat(ErrorDefinition.FWK_MOBILE_E_3501_LABEL, ',', oRestConnectionConfig.getHost(),
						Long.toString(oRestResponse.getIdMessage())));
			} else {				
				//byte[] oDecodedPhoto = Base64.decode(oRestResponse.content, Base64.DEFAULT);
				Base64 oBase64 = new Base64();
				byte[] oDecodedPhoto = oBase64.decode(oRestResponse.getContent().getBytes());

				MMediaStoreImageService oMediaStoreService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
				
				// See API: Return the decoded bitmap, or null if the image could not be decode. 
				BitmapFactory.Options oBitmapOptions = new BitmapFactory.Options();
				if ( p_oParameterIn.getMaxWidth() != -1 ) {
					oBitmapOptions.inSampleSize = oMediaStoreService.computeSampleSize(oDecodedPhoto, p_oParameterIn.getMaxWidth() );
				}
				Bitmap oBitmap = BitmapFactory.decodeByteArray(oDecodedPhoto, 0, oDecodedPhoto.length, oBitmapOptions );
				
				if ( oBitmap == null ) {
					throw new Exception("Failure decoding bitmap Bitmap");
				}
				return new BitmapParameterOUT(oBitmap);
			}
		} catch (Exception oException) {
			Log.e(Application.LOG_TAG, "ImageRestSyncDownloadCommandImpl : downloadImage " + p_oParameterIn.getId(), oException);
			p_oContext.getMessages().addMessage(UserErrorMessages.FWK_MOBILE_UE_0002);
			
		}
		return null;
	}
	/**
	 * Nothing to do
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doOnSuccess(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, ImageDownloadParameterIn p_oParameterIn, BitmapParameterOUT p_oResultOut) {
		
	}
	/**
	 * Nothing to do
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doOnError(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public BitmapParameterOUT doOnError(MContext p_oContext, ImageDownloadParameterIn p_oParameterIn, BitmapParameterOUT p_oResultOut) {
		// nothing to do
		return p_oResultOut;
	}

	/**
	 * {@inheritDoc}
	 * Nothing to do
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext,
	 *      com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(ImageDownloadParameterIn p_oIn, MContext p_oContext) throws ActionException {
		// Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * Nothing to do
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext,
	 *      com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, ImageDownloadParameterIn p_oParameterIn, BitmapParameterOUT p_oParameterOut) throws ActionException {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * Nothing to do
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext,
	 *      com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESS[])
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void... p_oProgressInformations) {
		// Nothing to do
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
	 */
	@Override
	public ImageDownloadParameterIn getEmptyInParameter() {
		return new ImageDownloadParameterIn();
	}

}
