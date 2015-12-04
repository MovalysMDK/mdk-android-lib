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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.converter;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;

/**
 * String to EmailSVMImpl object converter
 */
public class StringToEmailVOConverter extends CustomConverter {

	/** 
	 * Constructor
	 */
	public StringToEmailVOConverter() {
		super();
	}

	@Override
	public String[] getAttributesName() {
		return null;
	}

	@Override
	public Object convertFromComponentToVM(Object p_oValueToConvert, Class<?> p_oReturnClass) {
		if (p_oReturnClass.equals(String.class)) {
			return fromEmailVOToString(p_oValueToConvert);
		} else {
			return p_oValueToConvert;
		}
	}

	@Override
	public Object convertFromVMToComponent(Object p_oValueToConvert) {
		if (p_oValueToConvert instanceof String) {
			return fromStringToEmailVO(p_oValueToConvert);
		} else {
			return p_oValueToConvert;
		}
	}

	/**
	 * Converts an EmailSVMImpl object to a String
	 * @param p_oValueToConvert the value to convert
	 * @return the converted string
	 */
	private String fromEmailVOToString(Object p_oValueToConvert) {
		String r_sTo = null;
		if (p_oValueToConvert != null) {
			r_sTo = ((EMailSVMImpl)p_oValueToConvert).to;
		}
		return r_sTo;
	}
	
	/**
	 * Converts a string to an EmailSVMImpl object
	 * @param p_oValueToConvert the string to convert
	 * @return the converted EmailSVMImpl
	 */
	private EMailSVMImpl fromStringToEmailVO(Object p_oValueToConvert) {
		EMailSVMImpl r_oEmailVO =  new EMailSVMImpl();
		
		if (p_oValueToConvert != null) {
			r_oEmailVO.to = p_oValueToConvert.toString();
		}
		
		return r_oEmailVO;
	}
}
