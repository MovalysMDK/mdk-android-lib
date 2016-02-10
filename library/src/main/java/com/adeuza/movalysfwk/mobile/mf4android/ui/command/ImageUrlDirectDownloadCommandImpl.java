package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

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
public class ImageUrlDirectDownloadCommandImpl extends AbstractTaskableAction<ImageDownloadParameterIn, BitmapParameterOUT, DefaultActionStep, Void> implements ImageDownloadCommand {
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -2267569613575033070L;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.command.ImageDownloadCommand#downloadImage(java.lang.String)
	 */
	public Bitmap downloadImage(String p_sImageFileUrl) {
		try {
			if ( p_sImageFileUrl != null) {
				URL oMyFileUrl = new URL(p_sImageFileUrl);
				HttpURLConnection oHttpConnection = (HttpURLConnection) oMyFileUrl.openConnection();
				oHttpConnection.setDoInput(true);
				oHttpConnection.connect();
				return BitmapFactory.decodeStream(oHttpConnection.getInputStream());
			}
		} catch (MalformedURLException e) {
			Log.e(Application.LOG_TAG, "ImageUrlDirectDownloadCommandImpl : downloadImage " + p_sImageFileUrl, e);
		} catch (IOException e) {
			Log.e(Application.LOG_TAG, "ImageUrlDirectDownloadCommandImpl : downloadImage " + p_sImageFileUrl, e);
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doAction(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public BitmapParameterOUT doAction(MContext p_oContext, ImageDownloadParameterIn p_oParameterIn) {
		return new BitmapParameterOUT(this.downloadImage(p_oParameterIn.getId()) );
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doOnSuccess(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, ImageDownloadParameterIn p_oParameterIn, BitmapParameterOUT p_oResultOut) {
	}
	/**
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
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext,
	 *      com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(ImageDownloadParameterIn p_oIn, MContext p_oContext) throws ActionException {
		// Nothing do do
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext,
	 *      com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, ImageDownloadParameterIn p_oParameterIn, BitmapParameterOUT p_oParameterOut) throws ActionException {
		// Nothing to do
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext,
	 *      com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESS[])
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void... p_oProgressInformations) {
		// Nothing to do
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isDataBaseAccessAction()
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return false;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isWritableDataBaseAccessAction()
	 */
	@Override
	public boolean isWritableDataBaseAccessAction() {
		return false;
	}
	/**
	 * 	{@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isConcurrentAction()
	 */
	@Override
	public boolean isConcurrentAction() {
		return true;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#getEmptyInParameter()
	 */
	@Override
	public ImageDownloadParameterIn getEmptyInParameter() {
		return new ImageDownloadParameterIn();
	}
}
