package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.UrlFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * This view group represents an url text view, its label and its button
 * to see the url on the web browser :<ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMURLTextViewGroup> </li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 */
public class MMURLEditViewGroup extends AbstractMMTableLayout<String> implements OnClickListener, TextWatcher {
	/** the url field*/
	private EditText oUiURLText;
	/** the label above the url */
	private TextView oUiURLLabel;
	/** the call button*/
	private ImageButton oUiWebBrowserButton;
	/** valeur par défaut dans le champ de saisie */
	private static final String URL_DEFAULT = "http://" ;
	/** Es  on en cours d'écriture */
	private boolean writingData;
	/** The key for the bundle in savedInstanceState */
	private final String WEB_BROWSER_BUTTON_ENABLED_STATE_KEY = "webBrowserButtonEnabledStateKey";
	/** The key for the bundle in savedInstanceState */
	private final String WEB_BROWSER_BUTTON_HIDDEN_STATE_KEY = "webBrowserButtonHiddenStateKey";

	/**
	 * Construct a MMURLTextViewGroup 
	 * @param p_oContext the context
	 */
	public MMURLEditViewGroup(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.writingData = false ;
			this.inflateLayout();
		}
	}	
	/**
	 * Construct a MMURLTextViewGroup 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMURLEditViewGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.writingData = false ;
			this.defineParameters(p_oAttrs);
			this.inflateLayout();
			this.linkChildrens(p_oAttrs);
		}
	}	

	/**
	 * Changing visibility of the call button
	 * @param p_bIsCallButtonHidden TRUE if the call button should be hidden, FALSE otherwhise
	 */
	protected void setWebBrowserButtonHidden(boolean p_bIsCallButtonHidden) {
		if(p_bIsCallButtonHidden) {
			this.oUiWebBrowserButton.setOnClickListener(null);
			this.oUiWebBrowserButton.setVisibility(View.GONE);
		}
		else {
			this.oUiWebBrowserButton.setOnClickListener(this);
			this.oUiWebBrowserButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Changing visibility of the call button
	 * @param p_bIsCallButtonHidden TRUE if the call button should be hidden, FALSE otherwhise
	 */
	protected void setWebBrowserButtonEnabled(boolean p_bIsEnabled) {
		this.oUiWebBrowserButton.setEnabled(p_bIsEnabled);
	}

	/**
	 * Fait le lien avec le layout XML
	 */
	private void inflateLayout() {
		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_url_editview), this);

			oUiURLLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__label));

			oUiURLText = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__value));
			oUiURLText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
			oUiURLText.addTextChangedListener(this);

			oUiWebBrowserButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__button));
			oUiWebBrowserButton.setVisibility(View.VISIBLE);
			oUiWebBrowserButton.setOnClickListener(this);
		}
	}

	/**
	 * link the child views with custom attributes and add button onClickListener
	 * @param p_oAttrs attributes of XML Component component_url_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		int iResourceLabel = p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0) ;
		if (iResourceLabel != 0 ){
			try {
				oUiURLLabel.setText(this.getResources().getString(iResourceLabel));
			} catch (NotFoundException e) {
				Application.getInstance().getLog().debug("MMURLEditViewGroup",
						StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0))));
			}
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

		if(!isInEditMode()) {
			oUiURLText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
			super.onFinishInflate();
			if (!isInEditMode()) {
				BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
				if (oConfiguration != null && oConfiguration.getLabel() != null) {
					this.oUiURLLabel.setText(oConfiguration.getLabel());
				}

				if (this.oUiURLLabel.getText() == null || this.oUiURLLabel.getText().length() == 0) {
					this.oUiURLLabel.setVisibility(GONE);
				}
			}
			oUiURLText.addTextChangedListener(this);
			oUiWebBrowserButton.setOnClickListener(this);
		}
	}

	/**
	 * On click listener to deal with the click on the Call button
	 * @param p_oArg0 the view that was clicked
	 */
	@Override
	public void onClick(View p_oArg0) {
		String sURL = this.configurationGetValue();
		if (sURL!=null && sURL.length() > 0 ){
			((AndroidApplication) Application.getInstance()).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sURL)));
		}
	}	

	/**
	 * {@inheritDoc}
	 */ 
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		this.writingData = true;
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		if( p_oObjectToSet != null && p_oObjectToSet.length() > 0 ) {

			if(customFormatter() != null) {
				p_oObjectToSet = (String) customFormatter().format(p_oObjectToSet);
			}			
			int iOldStart = this.oUiURLText.getSelectionStart();
			int iOldStop = this.oUiURLText.getSelectionEnd();
			iOldStart = Math.min(iOldStart, p_oObjectToSet.length());
			iOldStop = Math.min(iOldStop, p_oObjectToSet.length());
			this.oUiURLText.setText(p_oObjectToSet);
			setEditTextSelection(this.oUiURLText, iOldStart, iOldStop);
		} else{
			this.oUiURLText.setText(URL_DEFAULT);
		}
		this.writingData = false;
	}	
	
	private void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.writingData = true;

		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0) {
			this.configurationSetValue(URL_DEFAULT);
		} else {
			String sFirstValue = p_oObjectsToSet[0];
			if (sFirstValue == null) {
				this.configurationSetValue(URL_DEFAULT);
			}
			else {
				this.configurationSetValue(sFirstValue);
			}
		}
		this.setCustomLabel();
		this.writingData = false;

	}
	/**
	 * Définit l'étiquette du champ perso
	 */
	private void setCustomLabel(){
		String sNewCustomLabel = null  ;
		if ( this.aivDelegate.getAttributes().containsKey(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE) ) {
			sNewCustomLabel = this.aivDelegate.getAttributes().get(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE) ;
		}
		if ( sNewCustomLabel == null && this.getConfiguration() != null ) {
			Object oLabelFromGraph = ((BasicComponentConfiguration)this.getConfiguration()).getParameter(
					ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE.getName());
			if ( oLabelFromGraph != null){
				sNewCustomLabel = oLabelFromGraph.toString() ;
			}
			if ( sNewCustomLabel == null && ((BasicComponentConfiguration)this.getConfiguration()).getEntityFieldConfiguration() != null ) {
				Object oLabelFromEntity = ((BasicComponentConfiguration)this.getConfiguration()).getEntityFieldConfiguration().getParameter(
						ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE.getName());
				if ( oLabelFromEntity != null){
					sNewCustomLabel = oLabelFromEntity.toString() ;
				}		
			}
		}
		if (sNewCustomLabel!= null && sNewCustomLabel.length() > 0){
			this.oUiURLLabel.setText(sNewCustomLabel);
		}else{
			this.oUiURLLabel.setVisibility(GONE);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		if ( URL_DEFAULT.equalsIgnoreCase(oUiURLText.getText().toString() )){
			return StringUtils.EMPTY ;
		}
		String oReturnValue = oUiURLText.getText().toString();
		
		if (oReturnValue.length() == 0) {
			oReturnValue = null;
		}
		
		if(oReturnValue != null && customFormatter() != null) {
			oReturnValue = (String) customFormatter().unformat(oReturnValue);
		} 
		return oReturnValue;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#getValueType()
	 */
	@Override 
	public String[] configurationGetCustomValues() {
		return new String[] { this.configurationGetValue() };
	}
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
			/*
			if ( this.configurationGetValue().length() > 0 &&  !this.configurationGetValue().matches(URL_VALIDATION_REGEX) ){
				if (p_oErrorBuilder.length()>0) {
					p_oErrorBuilder.append("\n");
				}
				p_oErrorBuilder.append(Application.getInstance().getStringResource(DefaultApplicationR.error_invalid_data_format));
				return false ;
			}*/
			UrlFormFieldValidator r_oValidator  = BeanLoader.getInstance().getBean(UrlFormFieldValidator.class);
			r_oValidator.addParametersToConfiguration(this.aivDelegate.getAttributes(), p_oConfiguration);
			return r_oValidator.validate(this, p_oConfiguration, p_oErrorBuilder) ;
		}
		return false ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("urlTextView", this.oUiURLText.getText().toString());
		r_oBundle.putBoolean(WEB_BROWSER_BUTTON_ENABLED_STATE_KEY, this.oUiWebBrowserButton.isEnabled());
		r_oBundle.putBoolean(WEB_BROWSER_BUTTON_HIDDEN_STATE_KEY, this.oUiWebBrowserButton.getVisibility() == View.GONE);
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
		configurationSetValue(r_oBundle.getString("urlTextView"));

		boolean enabled = r_oBundle.getBoolean(WEB_BROWSER_BUTTON_ENABLED_STATE_KEY);
		boolean hidden = r_oBundle.getBoolean(WEB_BROWSER_BUTTON_HIDDEN_STATE_KEY);

		setWebBrowserButtonEnabled(enabled);
		setWebBrowserButtonHidden(hidden);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
			oUiURLText.setError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		if(this.oUiURLText.getError() != null) {
			oUiURLText.setError(null);
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiURLText.setEnabled(true);
		oUiWebBrowserButton.setEnabled(true);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiURLText.setEnabled(false);
		oUiWebBrowserButton.setEnabled(false);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_oObject) {
		return p_oObject == null || p_oObject.length() == 0;
	}
	/**
	 * {@inheritDoc}
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable p_oArg0) {
		if (!this.writingData && this.aivDelegate!=null) {
			this.aivDelegate.changed();
			boolean isValid = this.validate((BasicComponentConfiguration)this.aivDelegate.getConfiguration(), null,new StringBuilder()) && this.isFilled();
			this.setWebBrowserButtonEnabled(isValid); 
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
		return this.configurationGetValue() != null ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		if ( this.aivDelegate.isFlagMandatory()) {
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
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	/**
	 * {@inheritDoc}
	 */
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