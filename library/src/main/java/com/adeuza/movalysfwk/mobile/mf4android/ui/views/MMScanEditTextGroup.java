package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.ScanBarCodeCommand;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.BarCodeResult;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>
 * the MMScanEditTextGroup component is used to manage edit text with BarCode Scanner button you must use it in xml layouts bay adding the movalys
 * attibutes :
 * <ul>
 * <li>label : set the label of the group</li>
 * <li>scantype : set the scantype, ALL_CODE_TYPES is used if null</li>
 * </ul>
 * </p>
 * <p>
 * The Scantype can be one of the values :
 * <ul>
 * <li>PRODUCT_CODE_TYPES</li>
 * <ul>
 * <li>includes UPC_A,UPC_E,EAN_8,EAN_13</li>
 * </ul>
 * <li>ONE_D_CODE_TYPES</li>
 * <ul>
 * <li>includes PRODUCT_CODE_TYPES +</li>
 * <li>CODE_39,CODE_93,CODE_128</li>
 * </ul>
 * <li>QR_CODE_TYPES</li>
 * <ul>
 * <li>QR_CODE</li>
 * </ul>
 * <li>ALL_CODE_TYPES</li>
 * <ul><li>The default value, includes all codes</li></ul></ul>
 * </p>
 * <p>
 * Copyright (c) 2010
 * <p>
 * Company: Adeuza
 * 
 * @author dmaurange
 * 
 */
public class MMScanEditTextGroup extends AbstractMMTableLayout<String> implements MMComplexeComponent, TextWatcher {

	/** the label above the field*/
	private TextView uiLabel;
	/** the edit text that contain the result*/
	private EditText uiEditText;
	/** the format of barcode scanned*/
	private TextView uiTextFormat;
	/** the scan button*/
	private ImageButton uiScanButton;
	/** local variable for scantype*/
	private String scanType = "";
	/** the command used to scan*/
	private static final ScanBarCodeCommand SCAN_COMMAND = ScanBarCodeCommand.getInstance();
	/** the request code to use */
	private static final int SCAN_CODE_ACTIVITY = Math.abs("SCAN_CODE_ACTIVITY".hashCode()) & NumericConstants.HEXADECIMAL_MASK;
	/** the key used to retain the value of the bar code during orientation changes */
	private static final String SCAN_CODE_VALUE_KEY = "scanCodeValueKey";
	/** the key used to retain the type of the bar code during orientation changes */
	private static final String SCAN_CODE_TYPE_KEY = "scanCodeTypeKey";
	/** Indique si on est en train de modifier le champ */
	private boolean writingData;


	/**
	 * Construct a MMScanEditTextGroup component
	 * @param p_oContext the current context
	 */
	public MMScanEditTextGroup(Context p_oContext) {
		super(p_oContext);
	}
	/**
	 * Construct a MMScanEditTextGroup component
	 * @param p_oContext the current context
	 * @param p_oAttrs xml attributes that contain Label, scan format and other attributes
	 */
	public MMScanEditTextGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			linkChildrens(p_oAttrs);
		}
	}
	/**
	 * called wen the inflator finished the job before setting value
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.uiLabel.setText(oConfiguration.getLabel());
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(String p_sObjectToSet) {
		super.configurationSetValue(p_sObjectToSet);
		
		if (p_sObjectToSet != null) {
			int iOldStart = this.uiEditText.getSelectionStart();
			int iOldStop = this.uiEditText.getSelectionEnd();
			iOldStart = Math.min(iOldStart, p_sObjectToSet.length());
			iOldStop = Math.min(iOldStop, p_sObjectToSet.length());
			this.uiEditText.setText(p_sObjectToSet);
			setEditTextSelection(this.uiEditText, iOldStart, iOldStop);
			//le format de code scanné n'est plus affiché actuellement - il était utilisé pour les tests
			this.uiTextFormat.setVisibility(View.GONE);
		}
	}	
	
	private void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetValues(VALUE[])
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet.length == 1) {
			this.configurationSetValue(p_oObjectsToSet[0]);
		}
	}
	/**
	 * <p>
	 * 	return the String value of the field.
	 * </p>
	 * @return the string value of the field.
	 */
	public String getValue(){
		return this.uiEditText.getText().toString();
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMValueableView#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		String rReturnValue = this.uiEditText.getText().toString();
		
		if (rReturnValue.length() == 0) {
			rReturnValue = null;
		}
		
		return rReturnValue;
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return null;
	}
	/**
	 * link the child views with custom attributes and add button onClickListener
	 * 
	 * @param p_oAttrs
	 *            attributes of XML Component component_phone_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_scanbarcode_edittext), this);

		this.uiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_scanbarcode__scanbarcode__label));
		this.uiEditText = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_scanbarcode__scanbarcode__edit));
		this.uiTextFormat = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_scanbarcode__scanbarcode__textformat));
		this.uiScanButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_scanbarcode__scanbarcode__button));

		try {
			uiLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0)));
		} catch (NotFoundException e) {
			// A2A_DEV DMA : mettre les nouveaux logs
			Log.d("MMScanEditTextGroup", StringUtils.concat("Ressource not found",
					String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0))));
			// If no label, visibility set to gone to save space.
			this.uiLabel.setVisibility(View.GONE);
		}

		this.scanType = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "scantype");

		this.uiScanButton.setOnClickListener(oScanButtonOnClickListener);

		this.uiEditText.addTextChangedListener(this);
	}


	/**
	 * The OnClickListener of the scan Button
	 */
	private OnClickListener oScanButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View p_oArg0) {
			MMDialogFragment oDownloadBarCodeScanner = SCAN_COMMAND.initiateScanCommand(
					((AndroidApplication) Application.getInstance()).getCurrentVisibleActivity(),
					SCAN_CODE_ACTIVITY, scanType, MMScanEditTextGroup.this);
			if(oDownloadBarCodeScanner != null) {
				oDownloadBarCodeScanner.show(getFragmentActivityContext().getSupportFragmentManager(), oDownloadBarCodeScanner.getFragmentDialogTag());
			}
		}
	};

	/**
	 * Returns the fragmentActivit context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}



	/**
	 * {@inheritDoc}
	 *      implementation open a BarCode Scanner activity for result then call DoAfterBaCodeScan action before diplaying the bar code value and
	 *      format
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode, Intent p_oIntent) {
		// on récupère le résultat de l'intent
		BarCodeResult oBarCode = SCAN_COMMAND.parseActivityResult(p_oResultCode, p_oIntent);
		// on lance l'action
		oBarCode = Application.getInstance().getController().doAfterBarCodeScan(oBarCode);
		// on affiche le résultat
		uiEditText.setText(oBarCode.getContents());
		uiTextFormat.setText(oBarCode.getFormat());
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationUnsetError()
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationSetError(java.lang.String)
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#validate(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration, java.util.Map, java.lang.StringBuilder)
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}

	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable p_oEditable) {
		if ( !this.writingData && this.aivDelegate!=null) {
			this.aivDelegate.changed();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.uiEditText.toString().length()>0;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_sObject) {
		return p_sObject == null || p_sObject.length() == 0;
	}
	/**
	 * does nothing
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence p_sText, int p_iStart, int p_iCount,int p_iAfter) {		
	}
	/**
	 * does nothing
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(CharSequence p_sText, int p_iStart, int p_iBefore, int p_iCount) {
	}
	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return null;
	}
	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		// Nothing to do

	}


	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		
		this.writingData = true;
		
		Bundle r_oBundle = (Bundle) p_oState;
		Parcelable oParentParcelable = r_oBundle
				.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		String retainedValue = r_oBundle.getString(SCAN_CODE_TYPE_KEY);
		String retainedType = r_oBundle.getString(SCAN_CODE_VALUE_KEY);

		this.uiEditText.setText(retainedValue);
		this.uiTextFormat.setText(retainedType);
		
		this.writingData = false;
	}


	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);

		String retainValue = this.uiEditText.getText().toString();
		String retainType = this.uiTextFormat.getText().toString();

		r_oBundle.putString(SCAN_CODE_TYPE_KEY, retainValue);
		r_oBundle.putString(SCAN_CODE_VALUE_KEY, retainType);

		return r_oBundle;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
}
