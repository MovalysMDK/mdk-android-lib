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

/**
 * This view group represents an email edit view and its button.
 * To use this component:
 * <ul>
 * <li>define xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li>
 * <li>include a <com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailEditViewGroup></li>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * </ul>
 */
public class MMEmailSimpleViewText extends AbstractEmailTextView<String> {

	///** regex de validation des email récupéré dans le back-office MI dans filedtype.xml */
	//private final static String EMAIL_VALIDATION_REGEX = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.(\\w{2}|(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum))$" ;
	
	/**
	 * Construct a MMEmailSimpleViewTextGroup
	 * @param p_oContext the android context
	 * @see AbstractEmailTextView
	 */
	public MMEmailSimpleViewText(Context p_oContext) {
		super(p_oContext, String.class);
	}	
	
	/**
	 * Construct a MMEmailSimpleViewTextGroup
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view xml attributes
	 * @see AbstractEmailTextView
	 */
	public MMEmailSimpleViewText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, String.class);
	}
	
	@Override
	protected EMail composeMail(String p_oMail) {
		EMail r_oEMail = new EMail();
		r_oEMail.setTo( p_oMail );
		return r_oEMail;
	}
	
	@Override
	protected String stringToValue(String p_sString) {
		return p_sString;
	}
	
	@Override
	protected String valueToString(String p_oObject) {
		return p_oObject;
	}
}
