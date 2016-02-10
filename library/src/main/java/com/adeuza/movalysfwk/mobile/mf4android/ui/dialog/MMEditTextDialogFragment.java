package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * This photo command is an Activity that can :
 * <ul>
 * <li>Take a photo using an intent to a camera activity</li>
 * <li>Choose an existing image using galery or other activities that can handle
 * an ACTION_GET for images/* data</li>
 * <li>View and Update image metadata</li>
 * </ul>
 * 
 * this activity wait for 2 informations in intent :
 * <ul>
 * <li>idPhoto for the photo id as string</li>
 * <li>photoMetadata the {@link PhotoMetaData} containing existing photo data</li>
 * </ul>
 * 
 * @author dmaurange
 * 
 */

public class  MMEditTextDialogFragment extends MMDialogFragment implements OnClickListener {

	/** Identifiant passé au dialog contenant le texte contenu dans le champ */
	public static final String CONTENT_TEXT = "CONTENT_TEXT" ; 
	/** Identifiant passé au dialog contenant l'étiquette contenue dans le champ */
	public static final String CONTENT_LABEL = "CONTENT_LABEL" ; 
	/** Identifiant passé au dialog contenant un booléen indiquant si le champ est éditable  */
	public static final String IS_EDITABLE_CONTENT = "IS_EDITABLE_CONTENT" ; 
	/** Flags indiquant l'inputtype du composant.*/
	public static final String INPUT_TYPE = "INPUT_TYPE";
	/** Max-length. */
	public static final String MAX_LENGTH = "MAX_LENGTH" ; 
	
	
	/** Texte contenu dans le champ */
	private String contentText = null; 
	
	/** Etiquette contenue dans le champ */
	private String contentLabel = null; 
	
	/** Booléen indiquant si le champ est éditable  */
	private boolean isEditableContent = false;
	
	/** Flags indiquant l'inputtype du composant.*/
	private int inputType = 0;

	/** Max-length. */
	private int maxLength = 0; 
	
	/** Application Android */
	private AndroidApplication application = null;
	
	/** UI Components */
	private ImageButton uiOkButton;

	private ImageButton uiCancelButton;
	private ImageButton uiEraseButton;
	private ImageButton uiSpeakButton;
	private MultiAutoCompleteTextView uiEditText ;
	private TextView uiLabel ;
	
	/**  Is the text modifiable */
	private boolean isEditable= true ;
	
	/**  Is the text valide */
	private boolean isMultiTextValided = false;
	
	public static final int REQUEST_CODE = Math.abs(MMEditTextDialogFragment.class.getSimpleName().hashCode());

	
	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static MMEditTextDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		MMEditTextDialogFragment oFragment = new MMEditTextDialogFragment();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		oFragment.componentId = ((View) p_oDismissListener).getId();
		oFragment.componentFragmentTag = ((ConfigurableVisualComponent<?>) p_oDismissListener).getFragmentTag();
		return oFragment;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);

		Bundle oArguments = getArguments();
		if(oArguments != null) {
			this.contentText = oArguments.getString(this.CONTENT_TEXT); 
			this.contentLabel = oArguments.getString(this.CONTENT_LABEL);
			this.isEditableContent = oArguments.getBoolean(this.IS_EDITABLE_CONTENT);
			this.inputType = oArguments.getInt(this.INPUT_TYPE);
			this.maxLength = oArguments.getInt(this.MAX_LENGTH);
		}
		
		//setStyle(STYLE_NO_FRAME, THEME_DEFAULT);
		setStyle(STYLE_NO_TITLE, THEME_DEFAULT);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer, Bundle p_oSavedInstanceState) {
		super.onCreateView(p_oInflater, p_oContainer, p_oSavedInstanceState);
		
		this.application = (AndroidApplication)(Application.getInstance());
		View oGlobalView = p_oInflater.inflate(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_edittextdialog_edittextcommand_layout), null);

		this.uiOkButton = (ImageButton) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__ok__button));
		this.uiEraseButton = (ImageButton) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__erase__button));
		this.uiSpeakButton = (ImageButton) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__speaknow__button));
		this.uiCancelButton = (ImageButton) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__cancel__button));
		
		this.uiCancelButton.setOnClickListener(this);

		this.uiEditText = (MultiAutoCompleteTextView) oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__textview__value));
		this.uiEditText.setBackgroundDrawable(null) ;

		this.uiLabel = (TextView)oGlobalView.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__textview__label));

		this.uiEditText.setText(this.contentText);
		final int iInputType = this.inputType;
		if (iInputType > 0) {
			this.uiEditText.setRawInputType(iInputType);
		}

		final int iMaxLength = this.maxLength;
		if (iMaxLength > 0) {
			InputFilter[] aFilterArray= new InputFilter[1];
			aFilterArray[0] = new InputFilter.LengthFilter(iMaxLength);
			this.uiEditText.setFilters(aFilterArray);
		}

		this.uiLabel.setText(this.contentLabel);
		this.isEditable = this.isEditableContent;

		if (this.isEditable ){
			if (this.application.isRecognitionInstalled()) {
				this.uiSpeakButton.setOnClickListener(this);
				this.uiSpeakButton.setEnabled(true);
			} else {
				this.uiSpeakButton.setVisibility(View.GONE);
			}
			this.uiEraseButton.setOnClickListener(this);
			this.uiOkButton.setOnClickListener(this);
			this.uiEditText.setEnabled(true);
		} else {
			this.uiSpeakButton.setVisibility(View.GONE);
			this.uiEraseButton.setVisibility(View.GONE);
			this.uiOkButton.setVisibility(View.GONE);
			this.uiEditText.setEnabled(false);
			this.uiEditText.setFocusable(false);
			
			RelativeLayout.LayoutParams oParams = (RelativeLayout.LayoutParams)this.uiCancelButton.getLayoutParams();
			oParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			this.uiCancelButton.setLayoutParams(oParams);
		}

		return oGlobalView;
	}
	
	/**
	 * onClick listeners on OK and Cancel button of the dialog {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {
		if (this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__speaknow__button) == p_oParamView.getId() ) {
			Intent oIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			oIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			oIntent.putExtra(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS , RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE);
		    ((AbstractMMActivity)this.getActivity()).startActivityForResult(oIntent, this.componentId, this.componentFragmentTag, REQUEST_CODE);
		} else if (this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__ok__button) == p_oParamView.getId() ) {
			// Validation
			this.isMultiTextValided = true;
			getDismissListener().onDismiss(null);
			onDestroyView();
		} else if (this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__erase__button) == p_oParamView.getId() ) {
			// Je fais le vide
			this.uiEditText.setText("");
		} else if (this.application.getAndroidIdByRKey(AndroidApplicationR.component_edittextdialog__cancel__button) == p_oParamView.getId() ) {
			// Annulation
			this.isMultiTextValided = false;
			getDismissListener().onDismiss(null);
			onDestroyView();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onActivityResult(ArrayList<String> p_oResult) {
		// Fill the field with the strings the recognizer thought it could have heard
		if ( this.uiEditText.getText().length() > 0 ) {
			this.uiEditText.append(" ");
		}
		this.uiEditText.append(p_oResult.get(0));
	}

	/**
	 * Getter uiEditText
	 */
	public String getEditText() {
		return this.uiEditText.getText().toString();
	}

	/**
	 * Check MultiText Valided
	 * {@link MMEditTextDialogFragment#isMultiTextValided()}
	 * 
	 * @return true if the user valid, false if cancelled
	 */
	public boolean isMultiTextValided() {
		return isMultiTextValided;
	}
}