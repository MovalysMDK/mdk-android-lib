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
 * <p>A2A_DEV - Décrire la classe ExtFwkErrors</p>
 *
 *
 * @since Annapurna
 */
public enum ExtFwkErrors implements ItfMessage {
	
	/**
	 * A2A_DOC
	 */
	ApplicationRError(MessageLevel.ERROR,0,"ApplicationRError" , DefaultApplicationR.error_application ),
	/**
	 * A2A_DOC
	 */
	LaunchActionError(MessageLevel.ERROR,1,"LaunchActionError", DefaultApplicationR.error_launch_action),
	/**
	 * Invalid data typed in a view model 
	 */
	InvalidViewModelDataError(MessageLevel.ERROR,2,"InvalidViewModelDataError", DefaultApplicationR.error_invalid_data),
	/**
	 * A2A_DOC
	 */
	ActionError(MessageLevel.ERROR,3,"ActionError", DefaultApplicationR.error_action);
	
	private MessageLevel level = null;
	/** 
	 * the identifier of the module.
	 */
	private int moduleId = 0;
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
	 * Constructeur simple
	 * @param p_oLevel
	 * @param p_iBodyId
	 * @param p_sSource
	 * @param p_oResource
	 */
	private ExtFwkErrors(MessageLevel p_oLevel, int p_iBodyId, String p_sSource, ApplicationR p_oResource ) {
		this.level = p_oLevel;
		this.bodyId = p_iBodyId;
		this.source = p_sSource;
		this.resource = p_oResource ;
	}
	/**
	 *
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage#getSource()
	 */
	@Override
	public String getSource() {
		return this.source;
	}
	/**
	 *
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage#getBodyId()
	 */
	@Override
	public int getBodyId() {
		return this.bodyId;
	}
	/**
	 *
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage#getModuleId()
	 */
	@Override
	public int getModuleId() {
		return this.moduleId;
	}
	/**
	 *
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage#getLevel()
	 */
	@Override
	public MessageLevel getLevel() {
		return this.level;
	}
	/**
	 * retourne un lien vers la traduction
	 *
	 * @return lien vers la traduction
	 */
	@Override
	public ApplicationR getResource() {
		return this.resource;
	}
}
