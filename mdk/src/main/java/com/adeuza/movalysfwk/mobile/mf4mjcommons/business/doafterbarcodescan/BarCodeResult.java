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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doafterbarcodescan;


import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * <p>Value Object class to manage the barcode resukt between diffents class</p>
 *
 *
 */
public class BarCodeResult extends AbstractActionParameter implements ActionParameter{
	
	/** serial id */
	private static final long serialVersionUID = 2103596897085661118L;
	
	String contents;
	String format;
	
	/**
	 * construct a new barcodeResult
	 */
	public BarCodeResult() {
		//Nothong to do
	}
	
	/**
	 * construct a new barcodeResukt with content and format
	 *
	 * @param p_oContents a {@link java.lang.String} object.
	 * @param p_oFormatName a {@link java.lang.String} object.
	 */
	public BarCodeResult(String p_oContents, String p_oFormatName) {
		contents=p_oContents;
		format=p_oFormatName;
	}

	/**
	 * gets the content
	 *
	 * @return Objet contents
	 */
	public String getContents() {
		return this.contents;
	}

	/**
	 * sets the content
	 *
	 * @param p_oContents Objet contents
	 */
	public void setContents(String p_oContents) {
		this.contents = p_oContents;
	}

	/**
	 * gets the barcode format ie EAN-13...
	 *
	 * @return Objet format
	 */
	public String getFormat() {
		return this.format;
	}

	/**
	 * sets the format
	 *
	 * @param p_oFormat the format
	 */
	public void setFormat(String p_oFormat) {
		this.format = p_oFormat;
	}

}

