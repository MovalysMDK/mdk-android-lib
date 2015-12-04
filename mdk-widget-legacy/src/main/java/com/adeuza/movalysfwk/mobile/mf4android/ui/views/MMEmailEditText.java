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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;

/**
 * This view group represents an email edit view and its button.
 * To use this component: 
 * <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the
 * main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.
 * MMEmailSimpleEditTextGroup></li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 */
public class MMEmailEditText extends AbstractEmailEditText<EMailSVMImpl> {
	
	/**
	 * Constructor
	 * @param p_oContext the context to use
	 */
	public MMEmailEditText(Context p_oContext) {
		super(p_oContext, EMailSVMImpl.class);
	}

	/**
	 * Constructor
	 * @param p_oContext the context to use
	 * @param p_oAttrs the xml attributes for the component
	 */
	public MMEmailEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, EMailSVMImpl.class);
	}

	/**
	 * {@inheritDoc}
	 * @see AbstractEmailMasterView#composeMail(Object)
	 */
	@Override
	protected EMail composeMail(EMailSVMImpl p_oMail) {
		EMail r_oEMail = new EMail();
		r_oEMail.setTo(p_oMail.to);
		r_oEMail.setCC(p_oMail.cC);
		r_oEMail.setBCC(p_oMail.bCC);
		return r_oEMail;
	}

	/**
	 * {@inheritDoc}
	 * @see AbstractEmailMasterView#stringToValue(String)
	 */
	@Override
	protected EMailSVMImpl stringToValue(String p_sString) {
		EMailSVMImpl r_oEMail = new EMailSVMImpl();
		r_oEMail.to = p_sString;
		return r_oEMail;
	}

	/**
	 * {@inheritDoc}
	 * @see AbstractEmailMasterView#valueToString(String)
	 */
	@Override
	protected String valueToString(EMailSVMImpl p_oObject) {
		return p_oObject.to;
	}
	
}
