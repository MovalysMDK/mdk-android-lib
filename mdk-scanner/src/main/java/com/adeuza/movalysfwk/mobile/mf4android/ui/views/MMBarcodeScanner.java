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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.CaptureActivity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;

/**
 * <p></p>
 *
 *
 *
 */

public class MMBarcodeScanner extends CaptureActivity implements ConfigurableVisualComponent ,InstanceStatable, ComponentBindDestroy, 
ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {

	/** Tag for debugging */
	private static final String TAG = MMBarcodeScanner.class.getSimpleName();

	/** format xml attribute for component */
	private static final String FORMAT_ATTRIBUTE = "scan-formats";

	/** mode xml attribute for component */
	private static final String MODE_ATTRIBUTE = "scan-mode";

	/** default barcode formats */
	private static final String DEFAULT_FORMAT;
	
	static {
		DEFAULT_FORMAT = new StringBuilder()
				.append(BarcodeFormat.UPC_A)
				.append(',').append(BarcodeFormat.UPC_E)
				.append(',').append(BarcodeFormat.EAN_13)
				.append(',').append(BarcodeFormat.EAN_8)
				.append(',').append(BarcodeFormat.RSS_14)
				.append(',').append(BarcodeFormat.CODE_39)
				.append(',').append(BarcodeFormat.CODE_93)
				.append(',').append(BarcodeFormat.CODE_128)
				.append(',').append(BarcodeFormat.ITF)
				.append(',').append(BarcodeFormat.CODABAR)
				.append(',').append(BarcodeFormat.AZTEC)
				.append(',').append(BarcodeFormat.DATA_MATRIX)
				.append(',').append(BarcodeFormat.MAXICODE)
				.append(',').append(BarcodeFormat.PDF_417)
				.append(',').append(BarcodeFormat.QR_CODE)
				.append(',').append(BarcodeFormat.RSS_EXPANDED)
				.append(',').append(BarcodeFormat.UPC_EAN_EXTENSION)
				.toString();		
	}
	
	/** Intent of the scanning action for ZXing decoding purpose */
	private Intent intent;
	
	/** Listener who reacts to the value found by the scanner */
	private OnRecognizeBarcodeListener listener;
	
	/** configurable view framework delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;
	
	/** value handled by component */
	private String value;


	/**
	 * Constructs a new MMBarcodeScanner
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 */
	@SuppressWarnings("unchecked")
	public MMBarcodeScanner(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			this.initializeFromAttributes(p_oAttrs);
		}
	}
	
	/**
	 * Constructs a new MMBarcodeScanner
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see CaptureActivity#CaptureActivity(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMBarcodeScanner(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			this.initializeFromAttributes(p_oAttrs);
		}
	}
	
	/**
	 * called when the inflater finished the initialization of the camera 
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.release();
		this.init() ;

	}

	/**
	 * Define the listener who will react to a detected code found by scanner
	 * @param p_oListener new listener
	 */
	public void setOnRecognizeBarcodeListener(OnRecognizeBarcodeListener p_oListener) {
		this.listener = p_oListener;
	}

	/**
	 * Refresh the preview 
	 */
	public void restart() {
		super.restartPreviewAfterDelay(0);
	}

	/**
	 * A valid barcode has been found, so give an indication of success and show the results.
	 *
	 * @param p_oResult The contents of the barcode.
	 * @param p_oBarcode   A greyscale bitmap of the camera data which was decoded.
	 * @param p_oScaleFactor amount by which thumbnail was scaled
	 */
	@Override
	protected void handleDecode(Result p_oResult, Bitmap p_oBarcode, float p_oScaleFactor) {
		super.handleDecode(p_oResult, p_oBarcode, p_oScaleFactor);
		if ( Log.isLoggable(TAG, Log.VERBOSE)) {
			Log.v(TAG, "Found bar code " + p_oResult.getText());
		}
		if (p_oResult != null) {
			this.aivDelegate.configurationSetValue( p_oResult.getText() ) ;
			this.aivFwkDelegate.changed(); // we prevent the view model that we have changed the value
			if (this.listener != null) {
				this.listener.onRecognize(this, p_oResult.getText());
			}
		}
		this.release();
		this.init();
	}

	/**
	 * {@inheritDoc}
	 * Return the intent of the action done by scanning
	 */
	@Override
	protected Intent getIntent() {
		return this.intent;
	}
	
	/**
	 * Customize the surface view : by default, match parent in width and in height
	 * @param p_oSurfaceView the surface view to customize
	 */
	@Override
	protected void customizeUiSurfaceView(SurfaceView p_oSurfaceView) {
		p_oSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
		FrameLayout.LayoutParams.MATCH_PARENT)); 
	}
	
	/**
	 * Initialize the object with the parameters in XML layout
	 * scan-formats define the format to use or default format is DEFAULT_FORMAT
	 * hideLaserZone to hide the transparent zone 
	 * @param p_oAttrs attribute in XML layout
	 */
	private void initializeFromAttributes(AttributeSet p_oAttrs) {
		this.intent = new Intent(Intents.Scan.ACTION);

		String sFormats = p_oAttrs.getAttributeValue(AndroidApplication.MOVALYSXMLNS, FORMAT_ATTRIBUTE);
		if (sFormats == null || sFormats.length() == 0) {
			sFormats = DEFAULT_FORMAT;
		}
		this.intent.putExtra(Intents.Scan.FORMATS, sFormats);
		this.intent.putExtra(Intents.Scan.MODE, p_oAttrs.getAttributeValue(AndroidApplication.MOVALYSXMLNS, MODE_ATTRIBUTE));
		this.intent.putExtra("hideLaserZone", Boolean.FALSE);
	}

	//---- DELEGATE

	
	/**
	 * {@inheritDoc}
	 * Keep the value when state changed
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		if ( this.aivDelegate != null){
			Bundle oToSave = new Bundle();
			oToSave.putParcelable("parent", this.aivFwkDelegate.onSaveInstanceState( super.onSaveInstanceState() ) );
			oToSave.putString("value", this.aivDelegate.configurationGetValue() );
			return oToSave;
		}
		return super.onSaveInstanceState();
	}

	/**
	 * {@inheritDoc}
	 * Reset the value when state changed
     * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		if(!(p_oState instanceof Bundle)){
			super.onRestoreInstanceState(p_oState);
			return;
		}

		Bundle savedState = (Bundle) p_oState;
		this.aivDelegate.configurationSetValue( savedState.getString("value") );
		this.aivFwkDelegate.onRestoreInstanceState(savedState.getParcelable("parent"));
	}
	
	/**
	 *  {@inheritDoc}
	 *  @see InstanceStatable#superOnRestoreInstanceState(Parcelable)
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}
	
	/**
	 * Free the camera to be used by another process or the same after a pause
	 */
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.release();
	}
	
	/**
	 * <p>Listener to receive events of recognition of the scanner</p>
	 */
	public interface OnRecognizeBarcodeListener {
		/**
		 * Method launched when the scanner found a result
		 * @param p_oView bar code scanner who found 
		 * @param p_sResult result found by the scanner 
		 */
		public void onRecognize(MMBarcodeScanner p_oView, String p_sResult);
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<String> getComponentDelegate() {
		return this.aivDelegate;
	}

	/************************************************************************************************************************
	 ********************************************* Framework delegate callback **********************************************
	 ************************************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#doOnPostAutoBind()
	 */
	@Override
	public void doOnPostAutoBind() {
		this.init();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#destroy()
	 */
	@Override
	public void destroy() {
		this.release();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		String oReturnMMBarValue = this.value;
		if (oReturnMMBarValue != null && oReturnMMBarValue.length() == 0) {
			oReturnMMBarValue = null;
		}
		return oReturnMMBarValue;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		this.value = p_oObjectToSet;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.value != null && this.value.length() > 0;
	}
}
