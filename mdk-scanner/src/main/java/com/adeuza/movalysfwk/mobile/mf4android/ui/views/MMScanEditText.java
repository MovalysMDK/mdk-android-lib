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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.ScanBarCodeCommand;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan.BarCodeResult;

/**
 * <p>
 * the MMScanEditTextGroup component is used to manage edit text with BarCode Scanner button. 
 * The available xml attributes are the following:
 * <ul>
 * <li>label : set the label of the group</li>
 * <li>scantype : set the type of the scanned value, ALL_CODE_TYPES is used if null</li>
 * </ul>
 * </p>
 * <p>
 * The Scantype can be one of the following values:
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
 * 
 * 
 */
public class MMScanEditText extends AbstractMMLinearLayout<String> implements MMComplexeComponent, TextWatcher, ComponentEnable, 
ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {
	
	/** the edit text that contain the result*/
	private EditText uiEditText;
	
	/** the format of barcode scanned*/
	private TextView uiTextFormat;
	
	/** local variable for scantype*/
	private String scanType = "";
	
	/** the command used to scan*/
	private static final ScanBarCodeCommand SCAN_COMMAND = ScanBarCodeCommand.getInstance();
	
	/** the key used to retain the value of the bar code during orientation changes */
	private static final String SCAN_CODE_VALUE_KEY = "scanCodeValueKey";
	
	/** the key used to retain the type of the bar code during orientation changes */
	private static final String SCAN_CODE_TYPE_KEY = "scanCodeTypeKey";

	/**
	 * Component request code
	 */
	private int requestCode = -1;
	
	/**
	 * Construct a MMScanEditText component
	 * @param p_oContext the current context
	 */
	public MMScanEditText(Context p_oContext) {
		super(p_oContext, String.class);
	}
	
	/**
	 * Construct a MMScanEditText component
	 * @param p_oContext the current context
	 * @param p_oAttrs xml attributes that contain Label, scan format and other attributes
	 */
	public MMScanEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, String.class);
		if(!isInEditMode()) {
			linkChildrens(p_oAttrs);
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
	 * link the children views with custom attributes and add button onClickListener
	 * 
	 * @param p_oAttrs attributes of XML Component component_phone_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_scanbarcode_edittext), this);

		ImageButton uiScanButton;
		
		this.uiEditText = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_scanbarcode__scanbarcode__edit));
		this.uiTextFormat = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_scanbarcode__scanbarcode__textformat));
		
		uiScanButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_scanbarcode__scanbarcode__button));

		this.scanType = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "scantype");

		uiScanButton.setOnClickListener(oScanButtonOnClickListener);

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
					MMScanEditText.this.getRequestCode(), scanType, MMScanEditText.this);
			if(oDownloadBarCodeScanner != null) {
				oDownloadBarCodeScanner.show(getFragmentActivityContext().getSupportFragmentManager(), oDownloadBarCodeScanner.getFragmentDialogTag());
			}
		}
	};

	/**
	 * Returns the fragmentActivity context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}

	/**
	 * {@inheritDoc}
	 * implementation open a BarCode Scanner activity for result then call DoAfterBaCodeScan action before displaying the bar code value and format
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
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable p_oEditable) {
		if (this.aivDelegate!=null && !this.aivDelegate.isWritingData()) {
			this.aivFwkDelegate.changed();
		}
	}

	/**
	 * does nothing
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence p_sText, int p_iStart, int p_iCount,int p_iAfter) {
		// Nothing to do
	}
	
	/**
	 * does nothing
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(CharSequence p_sText, int p_iStart, int p_iBefore, int p_iCount) {
		// Nothing to do
	}
	
	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return null;
	}
	
	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments) {
		// Nothing to do
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		
		this.aivDelegate.setWritingData(true);
		
		Bundle r_oBundle = (Bundle) p_oState;
		Parcelable oParentParcelable = r_oBundle
				.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		String retainedValue = r_oBundle.getString(SCAN_CODE_TYPE_KEY);
		String retainedType = r_oBundle.getString(SCAN_CODE_VALUE_KEY);

		this.uiEditText.setText(retainedValue);
		this.uiTextFormat.setText(retainedType);
		
		this.requestCode = r_oBundle.getInt("requestCode");
		
		this.aivDelegate.setWritingData(false);
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
		r_oBundle.putInt("requestCode", this.requestCode);

		return r_oBundle;
	}
	
	/************************************************************************************
	 *************************** Framework delegate callback ****************************
	 ************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.uiEditText.toString().length()>0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		String r_ReturnValue = this.uiEditText.getText().toString();
		if (r_ReturnValue.length() == 0) {
			r_ReturnValue = null;
		}
		return r_ReturnValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		 if (p_oObjectToSet != null) {
			int iOldStart = this.uiEditText.getSelectionStart();
			int iOldStop = this.uiEditText.getSelectionEnd();
			iOldStart = Math.min(iOldStart, ((String)p_oObjectToSet).length());
			iOldStop = Math.min(iOldStop, ((String)p_oObjectToSet).length());
			this.uiEditText.setText((String)p_oObjectToSet);
			this.uiEditText.setSelection(iOldStart, iOldStop);
			//le format de code scanné n'est plus affiché actuellement - il était utilisé pour les tests
			this.uiTextFormat.setVisibility(View.GONE);
		}
	}

	@Override
	public void enableComponent(boolean p_bEnable) {
		uiEditText.setEnabled(p_bEnable);
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}
