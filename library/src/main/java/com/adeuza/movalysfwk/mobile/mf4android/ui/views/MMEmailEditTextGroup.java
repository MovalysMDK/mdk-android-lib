package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;

/**
 * This view group represents an email edit view, its label and its button to
 * use this component,
 * <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the
 * main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.
 * MMEmailSimpleEditTextGroup></li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 */
public class MMEmailEditTextGroup extends AbstractEmailEditText<EMailSVMImpl> {
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	public MMEmailEditTextGroup(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	public MMEmailEditTextGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	private void configurationSetValue(String p_sEmail) {
		if(p_sEmail != null && p_sEmail.length() > 0) {
			if(customFormatter() != null) {
				p_sEmail = (String) customFormatter().format(p_sEmail);
			}
			int iOldStart = this.oUiEmail.getSelectionStart();
			int iOldStop = this.oUiEmail.getSelectionEnd();
			iOldStart = Math.min(iOldStart, p_sEmail.length());
			iOldStop = Math.min(iOldStop, p_sEmail.length());
			this.oUiEmail.setText(p_sEmail);
			setEditTextSelection(this.oUiEmail, iOldStart, iOldStop);
			
			if (((AndroidApplication) Application.getInstance()).isMailAvailable()) {
				this.oUiEmailButton.setOnClickListener(this);
				this.oUiEmailButton.setVisibility(View.VISIBLE);
			}
		}


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(EMailSVMImpl p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		this.setMailClientButtonHidden(!((AndroidApplication) Application
				.getInstance()).isMailAvailable());

		this.writingData = true;
		if (this.isNullOrEmptyValue(p_oObjectToSet)) {
			oUiEmail.setText(null);
			this.setMailClientButtonEnabled(false);
		} else {
			EMailSVMImpl oEmailValueToSet = p_oObjectToSet;
			if(customFormatter() != null) {
				oEmailValueToSet = (EMailSVMImpl) customFormatter().format(p_oObjectToSet); 
			}
			int iOldStart = oUiEmail.getSelectionStart();
			int iOldStop = oUiEmail.getSelectionEnd();
			iOldStart = Math.min(iOldStart, oEmailValueToSet.to.length());
			iOldStop = Math.min(iOldStop, oEmailValueToSet.to.length());
			oUiEmail.setText(oEmailValueToSet.to);
			setEditTextSelection(oUiEmail, iOldStart, iOldStop);
			this.setMailClientButtonEnabled(true);
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
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public void configurationSetCustomValues(String[] p_t_sValues) {
		this.aivDelegate.configurationSetCustomValues(p_t_sValues);
		if (p_t_sValues == null || p_t_sValues.length == 0
				|| p_t_sValues[0] == null) {
			this.configurationSetValue(StringUtils.EMPTY);
		} else {
			this.configurationSetValue(p_t_sValues[0]);
		}
		this.setCustomLabel();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public EMailSVMImpl configurationGetValue() {
		EMailSVMImpl r_oResult = new EMailSVMImpl();
		r_oResult.to = oUiEmail.getText().toString();
		EMailSVMImpl oEmailReturnValue = null;
		
		if (r_oResult.to.length() > 0) {
			oEmailReturnValue = r_oResult;
			if(customFormatter() != null) {
				oEmailReturnValue = (EMailSVMImpl) customFormatter().unformat(r_oResult);
			}
		}
		
		return oEmailReturnValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		String[]  oReturnValue = new String[] { this.oUiEmail.getText().toString() };
		if(customFormatter() != null) {
			oReturnValue = new String[] { (String) customFormatter().format(this.oUiEmail.getText().toString()) };
		}
		return oReturnValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isNullOrEmptyValue(EMailSVMImpl p_oObject) {
		return p_oObject == null || p_oObject.to == null
				|| p_oObject.to.length() == 0;
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
