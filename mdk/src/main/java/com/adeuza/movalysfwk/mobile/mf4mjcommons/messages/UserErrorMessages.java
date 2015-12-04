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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.messages;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;

/**
 * <p>Enumération that describe all the messages for the push service.</p>
 *
 *
 * @since Annapurna
 */
public enum UserErrorMessages implements ItfMessage {
	
	/** Impossible to join server */
	FWK_MOBILE_UE_0001(MessageLevel.ERROR,0,"UserErrorMessage"  , DefaultApplicationR.UserErrorMessage_0001),
	
	/** Server in error */
	FWK_MOBILE_UE_0002(MessageLevel.ERROR,1,"UserErrorMessages" , DefaultApplicationR.UserErrorMessage_0002),
	
	/** This error happends when mobile is not connected */
	FWK_MOBILE_UE_0003(MessageLevel.ERROR,2,"UserErrorMessages" , DefaultApplicationR.UserErrorMessage_0003)
	
	;

	/** 
	 * the identifier of the module.
	 */
	private int moduleId = 2;

	/** 
	 * the level of the message (info, warning, error, etc.)
	 */
	private MessageLevel level = null;

	/** 
	 * the identifier of the body message.
	 */
	private int bodyId = 0;

	/** 
	 * the message source
	 */
	private String source = null;
	/**
	 * Lien vers la traduction
	 */
	private ApplicationR resource = null;
	/**
	 * Construct an enumeration corresponding to a push message.
	 * @param p_oLevel
	 * 		the level of the message (info, warning, error, etc.)
	 * @param p_iBodyId
	 * 		the identifier of the body message
	 * @param p_sSource
	 * 		the message source
	 */
	private UserErrorMessages(MessageLevel p_oLevel, int p_iBodyId, String p_sSource, ApplicationR p_oResource ) {
		this.level = p_oLevel;
		this.bodyId = p_iBodyId;
		this.source = p_sSource;
		this.resource = p_oResource ;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getSource() {
		return this.source;
	}

	/** {@inheritDoc} */
	@Override
	public int getBodyId() {
		return this.bodyId;
	}

	/** {@inheritDoc} */
	@Override
	public int getModuleId() {
		return this.moduleId;
	}

	/** {@inheritDoc} */
	@Override
	public MessageLevel getLevel() {
		return this.level;
	}

	/**
	 * <p>Getter for the field <code>resource</code>.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 */
	@Override
	public ApplicationR getResource() {
		return this.resource;
	}
}
