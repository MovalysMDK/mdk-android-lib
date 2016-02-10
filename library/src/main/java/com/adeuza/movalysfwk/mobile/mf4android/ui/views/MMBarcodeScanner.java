package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.CaptureActivity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;

/**
 * <p></p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class MMBarcodeScanner extends CaptureActivity implements ConfigurableVisualComponent<String>, MMIdentifiableView ,InstanceStatable {

	private static final String TAG = MMBarcodeScanner.class.getSimpleName();

	private static final String FORMAT_ATTRIBUTE = "scan-formats";

	private static final String MODE_ATTRIBUTE = "scan-mode";

	//private static final Pattern COMMA_PATTERN = Pattern.compile("\\s*,\\s*");

	private static final String DEFAULT_FORMAT;

	//private static final long DELAY_BETWEEN_2_SCANS = 0 ;
	
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
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;
	/** Value scanned */
	private String value;

	/**
	 * Constructs a new MMBarcodeScanner
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 */
	public MMBarcodeScanner(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
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
	public MMBarcodeScanner(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.initializeFromAttributes(p_oAttrs);
		}
	}
	
	/**
	 * called when the inflator finished the job to init the camera 
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
		this.configurationSetValue( p_oResult.getText() ) ;
		this.aivDelegate.changed(); // we prevent the view model that we have changed the value
		if (this.listener != null && p_oResult != null) {
			this.listener.onRecognize(this, p_oResult.getText());
		}
		this.release();
		this.init();
	}

	/**
	 * Return the intent of the action done by scanning
	 */
	@Override
	protected Intent getIntent() {
		return this.intent;
	}
	
	/**
	 * Customize the surface view : by default, match parent in width and in height
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
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getName()
	 */
	@Override
	public String getName() {
		return this.aivDelegate.getName();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getConfiguration()
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#setDescriptor(com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor)
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getDescriptor()
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(String p_sValue) {
		this.aivDelegate.configurationSetValue(p_sValue);
		this.value = p_sValue;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetCustomValues(java.lang.String[])
	 */
	@Override
	public void configurationSetCustomValues(String[] p_t_sValues) {
		this.aivDelegate.configurationSetCustomValues(p_t_sValues);
		if (p_t_sValues == null || p_t_sValues.length == 0) {
			this.value = null;
		}
		else {
			this.value = p_t_sValues[0];
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		String oReturnValue = this.value;
		if (oReturnValue.length() == 0) {
			oReturnValue = null;
		}
		return oReturnValue;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationGetCustomValues()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		if(this.value == null || this.value.length() == 0){
			return null;
		}
		else {
			return new String[] { value };
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetLabel(java.lang.String)
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationHide(boolean)
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationHide(p_bLockModifier);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationUnHide(boolean)
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationUnHide(p_bLockModifier);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isLabel()
	 */
	@Override
	public boolean isLabel() {
		return this.aivDelegate.isLabel();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isAlwaysEnabled()
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return this.aivDelegate.isAlwaysEnabled();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isValue()
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isEdit()
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isGroup()
	 */
	@Override
	public boolean isGroup() {
		return this.aivDelegate.isGroup();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isPanel()
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isUnknown()
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getLocalisation()
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getModel()
	 */
	@Override
	public String getModel() {
		return this.aivDelegate.getModel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#resetChanged()
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isChanged()
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_sObject) {
		return p_sObject == null || p_sObject.length() == 0;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isNullOrEmptyCustomValues(java.lang.String[])
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		return p_oObjects == null || p_oObjects.length == 0 || this.isNullOrEmptyValue(p_oObjects[0]);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#hide()
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#unHide()
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnPostAutoBind() {
		this.init();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		this.release();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationUnsetError()
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetError(java.lang.String)
	 */
	@Override
	public void configurationSetError(String p_sError) {
		this.aivDelegate.configurationSetError(p_sError);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.value != null && this.value.length() > 0;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.aivDelegate.isVisible();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		this.aivDelegate.configurationSetMandatoryLabel();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		this.aivDelegate.configurationRemoveMandatoryLabel();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#validate(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration, java.util.Map, java.lang.StringBuilder)
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_oMapParameter, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_oMapParameter, p_oErrorBuilder);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#setName(java.lang.String)
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#registerVM(ViewModel)
	 */
	@Override
	public void registerVM(ViewModel p_oViewModel) {
		this.aivDelegate.registerVM(p_oViewModel);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#unregisterVM()
	 */
	@Override
	public void unregisterVM() {
		this.aivDelegate.unregisterVM();
	}
	
	/**
	 * {@inheritDoc}
	 * Keep the value when state changed
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		if ( this.aivDelegate != null){
			Bundle oToSave = new Bundle();
			oToSave.putParcelable("parent", this.aivDelegate.onSaveInstanceState( super.onSaveInstanceState() ) );
			oToSave.putString("value", this.value );
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
		this.value = savedState.getString("value");
		this.aivDelegate.onRestoreInstanceState(savedState.getParcelable("parent"));
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
	 *  @see InstanceStatable#superOnSaveInstanceState(Parcelable)
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
	
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
	
	/**
	 * <p>Listener to receive events of recognition of the scanner</p>
	 *
	 * <p>Copyright (c) 2012</p>
	 * <p>Company: Adeuza</p>
	 *
	 * @author emalespine
	 *
	 */
	public interface OnRecognizeBarcodeListener {
		/**
		 * Method launched when the scanner found a result
		 * @param p_oView bar code scanner who found 
		 * @param p_sResult result found by the scanner 
		 */
		public void onRecognize(MMBarcodeScanner p_oView, String p_sResult);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFragmentTag() {
		return this.aivDelegate.getFragmentTag();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		this.aivDelegate.setFragmentTag(p_sFragmentTag);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasRules() {
		return this.aivDelegate.hasRules();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHasRules(boolean p_bHasRules) {
		this.aivDelegate.setHasRules(p_bHasRules);
	}

}
