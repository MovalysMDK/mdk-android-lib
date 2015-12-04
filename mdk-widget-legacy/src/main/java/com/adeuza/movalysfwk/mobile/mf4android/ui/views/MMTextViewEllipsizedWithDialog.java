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
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMEditTextDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * <p>MMMultiTextViewWithEditDialogGroup widget used in the Movalys Mobile product for Android</p>
 */
public class MMTextViewEllipsizedWithDialog extends MMTextViewEllipsized 
	implements ConfigurableVisualComponent, OnClickListener, OnDismissListener, MMComplexeComponent, ComponentEnable {

	/** result code use with method startActivityForResult */
	public static final int REQUEST_CODE = getUniqueRequestCode();

	/** mask for result code on startActivityForResult */
	public static final int REQUEST_CODE_MASK = 0x0000ffff;

	/** 
	 * method to compute a unique positive integer
	 * @return a unique positive integer based on activity hashCode 
	 */
	private static int getUniqueRequestCode() {
		int hash = MMTextViewEllipsizedWithDialog.class.getSimpleName().hashCode();

		if (hash < 0) {
			hash++;
		}

		// in support-v7, only the last five digits of the result code are read
		// if the result value is greater, an exception will be raised
		return Math.abs(hash) & REQUEST_CODE_MASK;
	}
	
	/** identifier of the input text when passed to the dialog */
	public static final String CONTENT_TEXT = "CONTENT_TEXT" ; 
	
	/** identifier of the label text when passed to the dialog */
	public static final String CONTENT_LABEL = "CONTENT_LABEL" ; 
	
	/** identifier passed to the dialog to indicate whether the content is editable */
	public static final String IS_EDITABLE_CONTENT = "IS_EDITABLE_CONTENT" ; 
	
	/** Flags indicating the input type of the component .*/
	public static final String INPUT_TYPE = "INPUT_TYPE";
	
	/** Max-length. */
	public static final String MAX_LENGTH = "MAX_LENGTH" ; 
	
	/** maximum number of lines to display */
	public static final int MAX_LINES = 4;
	
	/** the key used to retain the value on orientation changes */
	private static final String MULTI_TEXT_VIEW_WITH_EDIT_DIALOG_GROUP_KEY = "multiTextViewWithEditDialogGroupKey";
	
	/** the Location command used to locate the device with network and GPS sensors */
	private MMEditTextDialogFragment oEditTextDialogFragment;
	
	/** tag of the edit text in the fragment */
	private String sEditTextDialogTag;

	/**
	 * Component request code
	 */
	private int requestCode = -1;
	
	/**
	 * Constructs a new MMTextViewEllipsizedWithDialog
	 * 
	 * @param p_oContext the context to use
	 */
	public MMTextViewEllipsizedWithDialog(Context p_oContext) {
		super(p_oContext);
	}
	
	/**
	 * Constructs a new MMTextViewEllipsizedWithDialog
	 * 
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 */
	public MMTextViewEllipsizedWithDialog(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTextViewEllipsized#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(!isInEditMode()) {
			this.setMaxLines(MAX_LINES);
			this.setOnClickListener(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oSourceOfEvent) {
		// création du dialog sur l'activité voir onCreateDialog()
		this.oEditTextDialogFragment = (MMEditTextDialogFragment) createDialogFragment(null);
		this.sEditTextDialogTag = this.oEditTextDialogFragment.getFragmentDialogTag();
		
		Bundle oDialogFragmentArguments = new Bundle();
		prepareDialogFragment(this.oEditTextDialogFragment, oDialogFragmentArguments);
		this.oEditTextDialogFragment.show(getFragmentActivityContext().getSupportFragmentManager(), this.oEditTextDialogFragment.getFragmentDialogTag());
	}

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
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode, Intent p_oIntent) {
		// Si l'utilisateur a fermé la popup de la reconnaissance vocale
		if (p_iRequestCode == this.getRequestCode() && p_oIntent != null) {
			this.oEditTextDialogFragment.onActivityResult(p_oIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
		}
	}	


	///////////////////
	// ZONE DELEGATE //
	///////////////////	
	
	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return MMEditTextDialogFragment.newInstance(this);
	}
	
	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		
		p_oDialog.setCancelable(false);

		p_oDialogFragmentArguments.putCharSequence(MMEditTextDialogFragment.CONTENT_TEXT, this.aivDelegate.configurationGetValue());
		p_oDialogFragmentArguments.putBoolean(MMEditTextDialogFragment.IS_EDITABLE_CONTENT, this.isEnabled());
		p_oDialogFragmentArguments.putInt(MMEditTextDialogFragment.INPUT_TYPE, this.getInputType());
		
		final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration();
		if (oConfiguration != null) {
			AbstractEntityFieldConfiguration oEntityFieldConfiguration = oConfiguration.getEntityFieldConfiguration();
			if (oEntityFieldConfiguration != null && oEntityFieldConfiguration.hasMaxLength()) {
				p_oDialogFragmentArguments.putInt(MMEditTextDialogFragment.MAX_LENGTH, oEntityFieldConfiguration.getMaxLength());
			}
		}

		p_oDialog.setArguments(p_oDialogFragmentArguments);
	}

	@Override
	public void onDismiss(DialogInterface p_oDialog) {
		//si l'utilisateur a validé le texte
		if (oEditTextDialogFragment.isMultiTextValided()) {
			String sNewText=oEditTextDialogFragment.getEditText();
			this.setText(sNewText);
			this.aivDelegate.configurationSetValue( sNewText );
			this.aivFwkDelegate.changed();
			}
		
		oEditTextDialogFragment.dismiss();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {

		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("class", MMTextViewEllipsizedWithDialog.class.getName());
		r_oBundle.putString(MULTI_TEXT_VIEW_WITH_EDIT_DIALOG_GROUP_KEY, this.getText().toString());
		r_oBundle.putString("dialogTag", this.sEditTextDialogTag);
		r_oBundle.putInt("requestCode", this.requestCode);
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);
		
		this.aivDelegate.configurationSetValue(r_oBundle.getString(MULTI_TEXT_VIEW_WITH_EDIT_DIALOG_GROUP_KEY));
		String sDialogTag = r_oBundle.getString("dialogTag");
		if(sDialogTag != null) {
			this.sEditTextDialogTag = sDialogTag;
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			this.oEditTextDialogFragment = (MMEditTextDialogFragment) ((AbstractMMActivity)oApplication.getCurrentActivity()).getSupportFragmentManager().findFragmentByTag(sDialogTag);
		}
		this.requestCode = r_oBundle.getInt("requestCode");
	}

	/********************************************************************************************
	 ***************************** Framework delegate callback **********************************
	 ********************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		if (p_bEnable) {
			this.setOnClickListener(this);
		} else {
			this.setOnClickListener(null);
		}
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
 
}
