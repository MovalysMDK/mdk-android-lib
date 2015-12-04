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
package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>
 * Download a image with a direct http connection without authorization
 * </p>
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
	 * 	{@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isConcurrentAction()
	 */
	@Override
	public int getConcurrentAction() {
		return Action.NO_QUEUE;
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
