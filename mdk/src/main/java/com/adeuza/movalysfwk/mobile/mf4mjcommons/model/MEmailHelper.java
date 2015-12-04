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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.model;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;

/**
 * <p>Helper for the component Email</p>
 *
 *
 */
public class MEmailHelper {
	
	/**
	 * <p>toComponent.</p>
	 *
	 * @param p_oEMailSVM a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toComponent(EMailSVMImpl p_oEMailSVM) {
		String r_sEmail = null;
		if (p_oEMailSVM != null && p_oEMailSVM.getTo() != null && !"".equals(p_oEMailSVM.getTo())) {
			r_sEmail = p_oEMailSVM.getTo();
		}
		return r_sEmail;
	}

	/**
	 * <p>toViewModel.</p>
	 *
	 * @param p_sEmail a {@link java.lang.String} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl} object.
	 */
	public static EMailSVMImpl toViewModel(String p_sEmail) {
		EMailSVMImpl r_oEMailSVM = new EMailSVMImpl();
		if (p_sEmail != null && !"".equals(p_sEmail)) {
			r_oEMailSVM.setTo(p_sEmail);
		}
		return r_oEMailSVM;
	}
}
