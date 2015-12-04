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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * The email value object to manage emails between actions
 * This class does not check any format to allow user modifications in the final mail composition interface
 *
 *
 */
public class EMail extends AbstractActionParameter implements ActionParameter {
	
	/** serial id */
	private static final long serialVersionUID = -8687056919772455983L;
	
	private String to;
	private String cC;
	private String bCC;
	private String object;
	private String body;

	
	/**
	 * Empty contructor for EMail Value object to use with {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.DoWriteMailAction}
	 */
	public EMail() {
		super();
	}
	

	/**
	 * Full contructor for EMail Value object to use with {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.DoWriteMailAction}
	 *
	 * @param p_oTo the recipient (or list of recipients separated by ,) of the mail
	 * @param p_oCc the carbon copy recipient (or list of cc separated by ,) of the mail
	 * @param p_oBcc the hiddent carbon copy recipient (or list of bcc separated by ,) of the mail
	 * @param p_oObject the title of the mail
	 * @param p_oBody the content
	 */
	public EMail(String p_oTo, String p_oCc, String p_oBcc, String p_oObject, String p_oBody) {
		super();
		this.to = p_oTo;
		this.cC = p_oCc;
		this.bCC = p_oBcc;
		this.object = p_oObject;
		this.body = p_oBody;
	}

	/**
	 * get recipient (To)
	 *
	 * @return Objet to
	 */
	public String getTo() {
		return this.to;
	}
	
	/**
	 * set recipient (To)
	 *
	 * @param p_oTo Objet to
	 */
	public void setTo(String p_oTo) {
		this.to = p_oTo;
	}

	/**
	 * get CC
	 *
	 * @return Objet cC
	 */
	public String getCC() {
		return this.cC;
	}

	/**
	 * set CC recipient
	 *
	 * @param p_oCC Objet cC
	 */
	public void setCC(String p_oCC) {
		this.cC = p_oCC;
	}

	/**
	 * get bCC
	 *
	 * @return Objet bCC
	 */
	public String getBCC() {
		return this.bCC;
	}

	/**
	 * set the bCC
	 *
	 * @param p_oBCC Objet bCC
	 */
	public void setBCC(String p_oBCC) {
		this.bCC = p_oBCC;
	}
	
	/**
	 * gets  the email Object
	 *
	 * @return Objet object
	 */
	public String getObject() {
		return this.object;
	}

	/**
	 * Sets the email Object
	 *
	 * @param p_oObject Objet object
	 */
	public void setObject(String p_oObject) {
		this.object = p_oObject;
	}

	/**
	 * get the body
	 *
	 * @return Objet body
	 */
	public String getBody() {
		return this.body;
	}

	/**
	 * stets the body
	 *
	 * @param p_oBody Objet body
	 */
	public void setBody(String p_oBody) {
		this.body = p_oBody;
	}
}
