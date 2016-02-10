package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.EmailFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

public abstract class AbstractEmailEditText<T> extends AbstractMMTableLayout<T> implements
MMIdentifiableView, OnClickListener, TextWatcher {

	private String TAG = "AbstractEmail";

	/** the email address field */
	protected EditText oUiEmail;
	/** the label above the email */
	protected TextView oUiLabel;
	/** the email button */
	protected ImageButton oUiEmailButton;
	/** The key to retain the the enable state of the mail client button */
	private static final String MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY = "mailClientButtonEnabledStateKey";
	/** The key to retain the the hidden state of the mail client button */
	private static final String MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY = "mailClientButtonHiddenStateKey";
	/** The key to retain the the hidden state of the mail client button */
	private static final String MAIL_VALUE_KEY = "mailValueKey";	
	/** Indique si on est en train de modifier le champ */
	protected boolean writingData;

	public AbstractEmailEditText(Context  p_oContext) {
		super(p_oContext);	
		if(!isInEditMode()) {
			this.linkLayout();
		}
	}

	public AbstractEmailEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.defineParameters(p_oAttrs);
			this.linkLayout();
			this.linkChildrens(p_oAttrs);
		}
	}

	private void linkLayout(){
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_email_simpletextedit), this);
		oUiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_email__email__label));
		oUiEmail = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_email__email__edit));
		// add input type
		oUiEmail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		// end modification
		oUiEmail.addTextChangedListener(this);
		oUiEmailButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_email__email__button));
		if ( ((AndroidApplication)Application.getInstance()).isMailAvailable()) {
			oUiEmailButton.setOnClickListener(this);
		} else {
			oUiEmailButton.setVisibility(View.GONE);
		}
	}


	/**
	 * link the child views with custom attributes and add button onClickListener
	 * @param p_oAttrs        attributes of XML Component component_phone_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		try {
			oUiLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0)));
		} catch (NotFoundException e) {
			Application.getInstance().getLog().debug(TAG,
					StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0))));
		} 
		setCustomConverter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.CONVERTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
		setCustomFormatter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.FORMATTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
	} 

	/**
	 * Retrieve parameters from layout to put in the local map for validation
	 * @param p_oAttrs XML attribute set
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivDelegate.defineParameters(p_oAttrs);
	}  

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.oUiLabel.setText(oConfiguration.getLabel());
			}

			if (this.oUiLabel.getText() == null || this.oUiLabel.getText().length() == 0) {
				this.oUiLabel.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * On click listener to deal with the click on the Call button
	 * @param p_oArg0    the view that was clicked
	 */
	@Override
	public void onClick(View p_oArg0) {
		String sEmailAddress = oUiEmail.getText().toString();
		if (sEmailAddress != null && sEmailAddress.length() > 0) {
			EMail oEMail = new EMail();
			oEMail.setTo( sEmailAddress );
			Application.getInstance().getController().doWriteEmail(oEMail);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void configurationSetValue(T p_oObjectToSet);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void configurationSetCustomValues(String[] p_t_sValues);

	/**
	 * Définit l'étiquette du champ perso
	 */
	protected void setCustomLabel() {
		String sNewCustomLabel = null  ;
		if ( this.aivDelegate.getAttributes().containsKey(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE) ) {
			sNewCustomLabel = this.aivDelegate.getAttributes().get(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE) ;
		}
		if ( sNewCustomLabel == null && this.getConfiguration() != null ) {
			Object oLabelFromGraph = ((BasicComponentConfiguration)this.getConfiguration()).getParameter(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE.getName());
			if ( oLabelFromGraph != null){
				sNewCustomLabel = oLabelFromGraph.toString() ;
			}
			if ( sNewCustomLabel == null && ((BasicComponentConfiguration)this.getConfiguration()).getEntityFieldConfiguration() != null ) {
				Object oLabelFromEntity = ((BasicComponentConfiguration)this.getConfiguration()).getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE.getName());
				if ( oLabelFromEntity != null){
					sNewCustomLabel = oLabelFromEntity.toString() ;
				}		
			}
		}
		if (sNewCustomLabel!= null && sNewCustomLabel.length() > 0){
			this.oUiLabel.setText(sNewCustomLabel);
		}else{
			this.oUiLabel.setVisibility(GONE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract T configurationGetValue();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String[] configurationGetCustomValues();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		if ( super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder) ) {
			EmailFormFieldValidator r_oValidator  = BeanLoader.getInstance().getBean(EmailFormFieldValidator.class);
			r_oValidator.addParametersToConfiguration(this.aivDelegate.getAttributes(), p_oConfiguration);
			return r_oValidator.validate(this.oUiEmail.getText().toString(), p_oConfiguration, p_oErrorBuilder) ;
		}
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		oUiEmail.setError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		oUiEmail.setError(null);
	}
	/**
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiEmail.setEnabled(true);
		oUiEmailButton.setEnabled(true);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiEmail.setEnabled(false);
		oUiEmailButton.setEnabled(false);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract boolean isNullOrEmptyValue(T p_oObject);

	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable p_oArg0) {
		if (!this.writingData  && this.aivDelegate != null) {
			this.aivDelegate.changed();
			boolean isValid = validate((BasicComponentConfiguration) this.getConfiguration(), null, new StringBuilder());
			this.setMailClientButtonEnabled(isValid);
		}
	}
	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence p_oArg0, int p_oArg1, int p_oArg2, int p_oArg3) {
		//nothing to do 
	}
	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(CharSequence p_oArg0, int p_oArg1, int p_oArg2, int p_oArg3) {
		//nothing to do
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.oUiEmail.getText() != null && this.oUiEmail.getText().length() > 0 ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isVisible() {
		return true;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		if (this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationRemoveMandatoryLabel();
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		if (!this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationSetMandatoryLabel();
		}
	}


	/**
	 * Set the state of the client mail button (enable/disable)
	 * @param p_bIsEnabled The new state of the button
	 */
	protected void setMailClientButtonEnabled(boolean p_bIsEnabled) {
		this.oUiEmailButton.setEnabled(p_bIsEnabled);
	}

	/**
	 * Set the visibility of the client mail button (gone/visible)
	 * @param p_bIsEnabled The new visibility of the button
	 */
	protected void setMailClientButtonHidden(boolean p_bIsHidden) {
		if(p_bIsHidden) {
			this.oUiEmailButton.setVisibility(View.GONE);
		}
		else {
			this.oUiEmailButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(MAIL_VALUE_KEY, this.oUiEmail.getText().toString());
		r_oBundle.putBoolean(MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY, this.oUiEmailButton.isEnabled());
		r_oBundle.putBoolean(MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY, this.oUiEmailButton.getVisibility() == View.GONE);
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		this.writingData = true;
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);


		boolean enabled = r_oBundle.getBoolean(MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY);
		boolean hidden = r_oBundle.getBoolean(MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY);
		String value = r_oBundle.getString(MAIL_VALUE_KEY);
		if(value != null) {
			this.oUiEmail.setText(value);
		}

		setMailClientButtonEnabled(enabled);
		setMailClientButtonHidden(hidden);
		
		this.writingData = false;
		
	}




	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	} 

	public void setCustomConverter(String p_oCustomConverter,
			AttributeSet p_oAttributeSet) {
		this.aivDelegate
		.setCustomConverter(p_oCustomConverter, p_oAttributeSet);
	}

	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}

	public void setCustomFormatter(String p_oCustomFormatter,
			AttributeSet p_oAttributeSet) {
		this.aivDelegate
		.setCustomFormatter(p_oCustomFormatter, p_oAttributeSet);
	}

}
