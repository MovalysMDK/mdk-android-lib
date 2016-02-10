package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;

/**
 * This view group represents an email edit view, its label and its button to use this component,
 * <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailEditViewGroup></li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 */
public class MMEmailSimpleViewTextGroup extends AbstractEmailTextView<String> {

	/** regex de validation des email récupéré dans le back-office MI dans filedtype.xml */
	//private final static String EMAIL_VALIDATION_REGEX = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.(\\w{2}|(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum))$" ;
	/**
	 * Construct a MMEmailSimpleViewTextGroup
	 * @param p_oContext          the context
	 * @param p_oAttrs            the xml attributes
	 */
	public MMEmailSimpleViewTextGroup(Context p_oContext) {
		super(p_oContext);
	}	
	/**
	 * Construct a MMEmailSimpleViewTextGroup 
	 * @param p_oContext          the context
	 * @param p_oAttrs            the xml attributes
	 */
	public MMEmailSimpleViewTextGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}
	
	@Override
	protected EMail composeMail(String p_oMail) {
		EMail r_oEMail = new EMail();
		r_oEMail.setTo( p_oMail );
		return r_oEMail;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		oArgType = p_oObjectToSet;
		if (this.isNullOrEmptyValue(p_oObjectToSet)) {
			if(customFormatter() != null) {
				p_oObjectToSet = (String)customFormatter().format(p_oObjectToSet);
			}
			oUiEmail.setText(p_oObjectToSet);
			oUiEmailButton.setOnClickListener(this);
			oUiEmailButton.setVisibility(View.VISIBLE);
		} else {
			oUiEmailButton.setVisibility(View.GONE);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0 || p_oObjectsToSet[0] == null ) {
			this.configurationSetValue(StringUtils.EMPTY);
		} else {
			this.configurationSetValue(p_oObjectsToSet[0]);
		}
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public String configurationGetValue() {
		String oEmailReturnValue =  oUiEmail.getText().toString();
		
		if (oEmailReturnValue.length() == 0) {
			oEmailReturnValue = null;
		}
		
		if(oEmailReturnValue != null && customFormatter() != null) {
			oEmailReturnValue = (String) customFormatter().unformat(oEmailReturnValue);
		}
		return oEmailReturnValue;
	}
	/**
	 * {@inheritDoc}
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
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiEmail.setEnabled(true);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiEmail.setEnabled(false);
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		boolean bReturnValue = false;
		
		if (this.configurationGetValue()!= null && this.configurationGetValue().length() > 0) {
			bReturnValue = true;
		}
		
		return  bReturnValue;
	}
	
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.superOnRestoreInstanceState(p_oState);
	}
	
	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.superOnSaveInstanceState();
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
