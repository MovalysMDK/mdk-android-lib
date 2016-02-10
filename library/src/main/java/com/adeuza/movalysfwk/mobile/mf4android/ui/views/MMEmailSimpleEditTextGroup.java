package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.EmailFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;

/**
 * This view group represents an email edit view, its label and its button to use this component,
 * <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailSimpleEditTextGroup></li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 */
public class MMEmailSimpleEditTextGroup extends AbstractEmailEditText<String> {
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	public MMEmailSimpleEditTextGroup(Context p_oContext) {
		super(p_oContext);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	public MMEmailSimpleEditTextGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		if(customFormatter() != null) {
			p_oObjectToSet = (String)customFormatter().format(p_oObjectToSet);
		}
		int iOldStart = this.oUiEmail.getSelectionStart();
		int iOldStop = this.oUiEmail.getSelectionEnd();
		iOldStart = Math.min(iOldStart, p_oObjectToSet.length());
		iOldStop = Math.min(iOldStop, p_oObjectToSet.length());
		this.oUiEmail.setText(p_oObjectToSet);
		setEditTextSelection(this.oUiEmail, iOldStart, iOldStop);
		
	}
	
	private void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public void configurationSetCustomValues(String[] p_t_sValues) {
		if (p_t_sValues == null || p_t_sValues.length == 0 || p_t_sValues[0] == null ) {
			this.configurationSetValue(StringUtils.EMPTY);
		} else {
			this.configurationSetValue(p_t_sValues[0]);
		}
		this.setCustomLabel();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public String configurationGetValue() {
		String oEmailReturnValue = oUiEmail.getText().toString();
		
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] { this.configurationGetValue() };
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		boolean bIsValide = false ; 
		if ( super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder) ) {
			if ( this.configurationGetValue() != null && this.configurationGetValue().length() > 0 ) {
				EmailFormFieldValidator r_oValidator  = BeanLoader.getInstance().getBean(EmailFormFieldValidator.class);
				r_oValidator.addParametersToConfiguration(this.aivDelegate.getAttributes(), p_oConfiguration);
				bIsValide = r_oValidator.validate(this, p_oConfiguration, p_oErrorBuilder) ;
			}else {
				bIsValide = true ;
			}
		}
		return bIsValide ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
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
		
		if (this.configurationGetValue() != null && this.configurationGetValue().length() > 0) {
			bReturnValue = true;
		}
		
		return bReturnValue ;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	@Override
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
	
	@Override
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
}
