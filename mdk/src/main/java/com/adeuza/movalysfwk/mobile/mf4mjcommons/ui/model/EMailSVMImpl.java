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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

import java.io.Serializable;


/**
 * The email value object to manage emails between actions
 * This class does not check any format to allow user modifications in the final mail composition interface
 *
 *
 * @since Barcelone
 */
public class EMailSVMImpl implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1412991669974789276L;
	/** the email @ to*/
	public String to;
	/** the email @ Carbon Copy */
	public String cC;
	/** the email @ blind carbon copy*/
	public String bCC;
	/** the object of the mail*/
	public String object;
	/** the content of the mail*/ 
	public String body;

	/**
	 * Construct an EMailSVMImpl
	 */
	public EMailSVMImpl() {
		super();
		this.to = "";
		this.cC = "";
		this.bCC = "";
		this.object = "";
		this.body = "";
	}
	

	/**
	 * Constructor using p_oEmail
	 *
	 * @param p_oEmail a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl} object.
	 */
	public EMailSVMImpl(EMailSVMImpl p_oEmail) {
		super();
		this.to = p_oEmail.to;
		this.cC = p_oEmail.cC;
		this.bCC = p_oEmail.bCC;
		this.object = p_oEmail.object;
		this.body = p_oEmail.body;
		
	}


	/**
	 * Retourne l'objet to
	 *
	 * @return Objet to
	 */
	public String getTo() {
		return this.to;
	}


	/**
	 * Affecte l'objet to
	 *
	 * @param p_oTo Objet to
	 */
	public void setTo(String p_oTo) {
		this.to = p_oTo;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof EMailSVMImpl) {
			EMailSVMImpl email = (EMailSVMImpl) obj;
			if ( email.to != null && email.to.equals(this.to) && 
					email.bCC != null && email.bCC.equals(this.bCC) &&
					email.cC != null && email.cC.equals(this.cC) && 
					email.object != null && email.object.equals(this.object) && 
					email.body != null && email.body.equals(this.body) ) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
}
