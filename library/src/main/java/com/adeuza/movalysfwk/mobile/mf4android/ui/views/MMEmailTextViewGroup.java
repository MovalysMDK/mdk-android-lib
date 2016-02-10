package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;

/**
 * This view group represents an email text view, its label and its button to use this componnent,
 * <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailTextViewGroup></li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 * <p>
 * Copyright (c) 2010
 * <p>
 * Company: Adeuza
 * 
 * @author dmaurange
 * @since Barcelone
 * 
 */

public class MMEmailTextViewGroup extends AbstractEmailTextView<EMailSVMImpl> {

	/**
	 * 
	 * Construct a MMEmailTextViewGroup
	 * 
	 * @param p_oContext
	 *            the context
	 * @param p_oAttrs
	 *            the xml attributes
	 */
	public MMEmailTextViewGroup(Context p_oContext) {
		super(p_oContext);
		oArgType = null;
	}

	/**
	 * 
	 * Construct a MMEmailTextViewGroup
	 * 
	 * @param p_oContext
	 *            the context
	 * @param p_oAttrs
	 *            the xml attributes
	 */
	public MMEmailTextViewGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		oArgType = null;
	}

	/**
	 * <p>
	 * Compose an EMail Value Object to be used with {@link Controller#doWriteEmail(EMail)} based on the {@link EMailSVMImpl} and strings labels
	 * interventionDetailAddress__email_object__label and interventionDetailAddress__email_content__label
	 * </p>
	 * 
	 * @param p_oEMailSVMImpl
	 *            the ViewModel
	 * @return an EMail value Object
	 */
	@Override
	public EMail composeMail(EMailSVMImpl p_oEMailSVMImpl) {
		EMail r_oEMail = new EMail();
		r_oEMail.setTo(p_oEMailSVMImpl.to);
		r_oEMail.setCC(p_oEMailSVMImpl.cC);
		r_oEMail.setBCC(p_oEMailSVMImpl.bCC);
		return r_oEMail;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(EMailSVMImpl p_oObjectToSet) {
		this.writingData = true;
		if(
			p_oObjectToSet != null && !p_oObjectToSet.equals(this.configurationGetValue()) 
			|| p_oObjectToSet == null && this.configurationGetValue() != null
		) {		
			this.aivDelegate.configurationSetValue(p_oObjectToSet);
			this.setMailClientButtonHidden(!((AndroidApplication)Application.getInstance()).isMailAvailable());

			oArgType = p_oObjectToSet;
			if (this.isNullOrEmptyValue(p_oObjectToSet)) {
				oUiEmail.setText(null);
				this.setMailClientButtonEnabled(false);
			}
			else {
				EMailSVMImpl oEmailValueToSet = p_oObjectToSet;
				if(customFormatter() != null) {
					oEmailValueToSet = (EMailSVMImpl) customFormatter().format(p_oObjectToSet); 
				}
				oUiEmail.setText(oEmailValueToSet.to);
				this.setMailClientButtonEnabled(true);
			}
		}
		this.writingData = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.setVisibility(View.GONE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EMailSVMImpl configurationGetValue() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return EMailSVMImpl.class;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(EMailSVMImpl p_oObject) {
		return p_oObject == null || p_oObject.to == null || p_oObject.to.length() == 0;
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
