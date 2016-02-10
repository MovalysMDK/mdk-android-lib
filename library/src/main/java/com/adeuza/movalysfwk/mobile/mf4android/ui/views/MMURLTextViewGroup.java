package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;

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
public class MMURLTextViewGroup extends AbstractMMTableLayout<String> implements OnClickListener {

	/** the url field*/
	private TextView oUiURLText;
	/** the label above the url */
	private TextView oUiURLLabel;
	/** the call button*/
	private ImageButton oUiWebBrowserButton;
	/** boolean that indicates if we are writing now */
	private boolean bIsWritingData;
	/** The key for the bundle in savedInstanceState */
	private final String WEB_BROWSER_BUTTON_ENABLED_STATE_KEY = "webBrowserButtonEnabledStateKey";
	/** The key for the bundle in savedInstanceState */
	private final String WEB_BROWSER_BUTTON_HIDDEN_STATE_KEY = "webBrowserButtonHiddenStateKey";

	/**
	 * Construct a MMURLTextViewGroup 
	 * @param p_oContext the context
	 */
	public MMURLTextViewGroup(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_url_textview), this);

			bIsWritingData = false;

			oUiURLLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__label));
			oUiURLText = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__value));
			oUiWebBrowserButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__button));
		}
	}

	/**
	 * Construct a MMURLTextViewGroup 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMURLTextViewGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);		
		if(!isInEditMode()) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
			bIsWritingData = false;

			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_url_textview), this);

			oUiURLLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__label));
			oUiURLText = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__value));
			oUiWebBrowserButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_url__url__button));

			this.linkChildrens(p_oAttrs);
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
				oUiURLLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0)));
			} catch (NotFoundException e) {
				Application.getInstance().getLog().debug("MMURLTextViewGroup",
						StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0))));
			}
		}
		setCustomConverter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.CONVERTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
		setCustomFormatter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS,Attribute.FORMATTER_NAME_ATTRIBUTE.getName()), p_oAttrs);

	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){
			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.oUiURLLabel.setText(oConfiguration.getLabel());
			}
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
	 * 
	 * On click listener to deal with the click on the Call button
	 * @param p_oArg0 the view that was clicked
	 */
	@Override
	public void onClick(View p_oArg0) {
		String sURL = (String) oUiURLText.getText();
		if (sURL!=null && sURL.length() > 0 ){
			((AndroidApplication) Application.getInstance()).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sURL)));
		}
	}	

	/**
	 * {@inheritDoc}
	 */ 
	@Override
	public void configurationSetValue(String p_oObjectToSet) {	
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		this.bIsWritingData = true;

		if( p_oObjectToSet != null && p_oObjectToSet.length() > 0 ) {
			if(customFormatter() != null) {
				p_oObjectToSet = (String) customFormatter().format(p_oObjectToSet);
			}
			oUiURLText.setText(p_oObjectToSet);
			oUiWebBrowserButton.setOnClickListener(this);

			boolean isValid = this.aivDelegate.validate((BasicComponentConfiguration)this.aivDelegate.getConfiguration(), null,new StringBuilder()) && this.isFilled();
			this.setWebBrowserButtonEnabled(isValid);

		} else{
			oUiWebBrowserButton.setVisibility(View.GONE);
		}
		this.bIsWritingData = false; 

		if (this.oUiURLLabel.getText() == null || this.oUiURLLabel.getText().length() == 0) {
			this.oUiURLLabel.setVisibility(GONE);
		}
	}			
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet.length == 1) {
			this.configurationSetValue(p_oObjectsToSet[0]);
		}else {
			this.setVisibility(View.GONE);
		}
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		String oReturnValue = oUiURLText.getText().toString();
		
		if (oReturnValue.length() == 0) {
			oReturnValue = null;
		}
		
		if (oReturnValue != null && customFormatter() != null) {
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.configurationGetValue() != null ;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_oObject) {
		return p_oObject == null || p_oObject.length() == 0;
	}

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
