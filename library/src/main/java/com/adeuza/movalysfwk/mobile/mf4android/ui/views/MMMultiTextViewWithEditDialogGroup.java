package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMEditTextDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractNamableObject;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * <p>MultiAutoCompleteTextView widget used in the Movalys Mobile product for Android</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * @author spacreau
 */
public class MMMultiTextViewWithEditDialogGroup extends MMTextViewEllipsized implements ConfigurableVisualComponent<String>, MMIdentifiableView, OnClickListener, OnDismissListener, MMComplexeComponent  {

	/** Request code of the activity */
	public static final int REQUEST_CODE = Math.abs(MMMultiTextViewWithEditDialogGroup.class.getSimpleName().hashCode());
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
	/** Nombre de lignes maximales affichées. */
	public static final int MAX_LINES = 4;
	/** the label above the MultiAutoComplete field*/
	private MMTextView oUiLabel;
	/** button to modify the text in the dialog */
	private boolean enabled ;
	/** the key used to retain the value on orientation changes */
	private static final String MULTI_TEXT_VIEW_WITH_EDIT_DIALOG_GROUP_KEY = "multiTextViewWithEditDialogGroupKey";

	/** Background of this component */
	private Drawable background;

	private boolean writingData = false;
	
	/** the Location command used to locate the device with network and gps sensors */
	private MMEditTextDialogFragment oEditTextDialogFragment;
	private String sEditTextDialogTag;
	
	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView(
	 */
	public MMMultiTextViewWithEditDialogGroup(Context p_oContext) {
		super(p_oContext);
		this.enabled = this.isEdit();
	}
	/**
	 * Constructs a new MMMultiAutoCompleteTextView
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see MultiAutoCompleteTextView#MultiAutoCompleteTextView(
	 */
	public MMMultiTextViewWithEditDialogGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		this.enabled = this.isEdit();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTextViewEllipsized#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(!isInEditMode()) {
			this.background = this.getBackground();
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
		if (p_iRequestCode == MMEditTextDialogFragment.REQUEST_CODE && p_oIntent != null) {
				this.oEditTextDialogFragment.onActivityResult(p_oIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
			}
	}	


	///////////////////
	// ZONE DELEGATE //
	///////////////////	

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.setBackgroundDrawable( null );
		this.setOnClickListener(this);
		this.enabled = false;
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		this.setFocusable(true);
		this.setBackgroundDrawable(this.background);
		this.setOnClickListener(this);
		this.enabled = true;

	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		this.aivDelegate.configurationRemoveMandatoryLabel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		this.aivDelegate.configurationSetMandatoryLabel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTextViewEllipsized#configurationSetValue(java.lang.String)
	 */
	@Override
	public void configurationSetValue(String p_oObject) {
		this.writingData = true;
		super.configurationSetValue(p_oObject);
		this.writingData = false;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return this.writingData && this.enabled || !this.writingData && super.isEnabled();
	}
	
	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		MMDialogFragment oDialogFragment = MMEditTextDialogFragment.newInstance(this);
		return oDialogFragment;
	}
	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		
		p_oDialog.setCancelable(false);
		
		if (this.oUiLabel == null) {
			int iLabelId = ((AndroidApplication) Application.getInstance()).getAndroidIdByStringKey(ApplicationRGroup.ID, this.aivDelegate
					.getConfigurationName().concat(AbstractNamableObject.KEY_SEPARATOR).concat(ConfigurableVisualComponent.TYPE_LABEL));
			if (iLabelId > 0) {
				this.oUiLabel = (MMTextView) ((Activity) this.getContext()).findViewById(iLabelId);
			}
		}

		if (this.oUiLabel != null) {
			p_oDialogFragmentArguments.putCharSequence(MMEditTextDialogFragment.CONTENT_LABEL, this.oUiLabel.getText());
		}

		p_oDialogFragmentArguments.putCharSequence(MMEditTextDialogFragment.CONTENT_TEXT, this.configurationGetValue());
		p_oDialogFragmentArguments.putBoolean(MMEditTextDialogFragment.IS_EDITABLE_CONTENT, this.enabled);
		p_oDialogFragmentArguments.putInt(MMEditTextDialogFragment.INPUT_TYPE, this.getInputType());
		
		final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
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
			this.configurationSetValue(	sNewText );
			this.aivDelegate.changed();
			}
		
		oEditTextDialogFragment.dismiss();
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {

		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("class", MMMultiTextViewWithEditDialogGroup.class.getName());
		r_oBundle.putString(MULTI_TEXT_VIEW_WITH_EDIT_DIALOG_GROUP_KEY, this.getText().toString());
		r_oBundle.putString("dialogTag", this.sEditTextDialogTag);
		return r_oBundle;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);
		
		String sClass = r_oBundle.getString("class");
		configurationSetValue(r_oBundle.getString(MULTI_TEXT_VIEW_WITH_EDIT_DIALOG_GROUP_KEY));
		String sDialogTag = r_oBundle.getString("dialogTag");
		if(sDialogTag != null) {
			this.sEditTextDialogTag = sDialogTag;
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			this.oEditTextDialogFragment = (MMEditTextDialogFragment) ((AbstractMMActivity)oApplication.getCurrentActivity()).getSupportFragmentManager().findFragmentByTag(sDialogTag);
		}
	}
 
}
