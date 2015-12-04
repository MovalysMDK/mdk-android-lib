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
import android.util.AttributeSet;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;

/**
 * This view group represents an email text view and its button.
 * To use this component:
 * <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailTextViewGroup></li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 */

public class MMEmailTextView extends AbstractEmailTextView<EMailSVMImpl> {

	/**
	 * 
	 * Construct a MMEmailTextViewGroup
	 * 
	 * @param p_oContext the context
	 */
	public MMEmailTextView(Context p_oContext) {
		super(p_oContext, EMailSVMImpl.class);
	}

	/**
	 * 
	 * Construct a MMEmailTextViewGroup
	 * 
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMEmailTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, EMailSVMImpl.class);
	}
	
	/**
	 * <p>
	 * Compose an EMail Value Object to be used with FwkController.doWriteEmail(EMail) based on the {@link EMailSVMImpl} and strings labels
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
	 * @see AbstractEmailTextView#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(EMailSVMImpl p_oObjectToSet) {
		
		this.setMailClientButtonHidden(!((AndroidApplication) Application.getInstance()).isMailAvailable());

		if (this.aivDelegate.isNullOrEmptyValue(p_oObjectToSet)) {
			oUiEmail.setText(null);
			this.setMailClientButtonEnabled(false);
		} else {
			EMailSVMImpl oEmailValueToSet = (EMailSVMImpl) p_oObjectToSet;
			if(this.getComponentFwkDelegate().customFormatter() != null) {
				oEmailValueToSet = (EMailSVMImpl) this.getComponentFwkDelegate().customFormatter().format(p_oObjectToSet); 
			}
			oUiEmail.setText(oEmailValueToSet.to);
			this.setMailClientButtonEnabled(true);
		}
	}

	@Override
	protected EMailSVMImpl stringToValue(String p_sString) {
		EMailSVMImpl r_oEmail = new EMailSVMImpl();
		r_oEmail.to = p_sString;
		return r_oEmail;
	}

	@Override
	protected String valueToString(EMailSVMImpl p_oObject) {
		return p_oObject.to;
	}
}
