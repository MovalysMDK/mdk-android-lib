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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessagesList;

/**
 * <p>Value object utilisé pour passer des informations au méthode de "callback" de type "ListenerOnActionX"</p>
 *
 *
 */
public class AbstractResultEvent<RESULT extends ActionParameter> extends AbstractEvent {

	/** résultat de l'action */
	private RESULT actionResult = null;

	/** source object */
	private Object source;

	/** array of denied permissions */
	private String[] deniedPermissions;
	
	/**
	 * Construit un nouveau value object
	 *
	 * @param p_oMessages la liste des messages
	 * @param p_oActionResult le résultat de l'action
	 * @param p_oSource a {@link java.lang.Object} object.
	 * @param p_oDeniedPermissions a list of denied permission
	 */
	public AbstractResultEvent(MessagesList p_oMessages, RESULT p_oActionResult, Object p_oSource, String[] p_oDeniedPermissions) {
		super(p_oMessages);
		this.actionResult = p_oActionResult;
		this.source = p_oSource;
		if (p_oDeniedPermissions != null) {
			this.deniedPermissions = p_oDeniedPermissions.clone();
		}
	}
	
	/**
	 * Donne le résultat de l'action
	 *
	 * @return le résultat de l'action
	 */
	public RESULT getActionResult() {
		return this.actionResult;
	}

	/**
	 * Donne la source de l'évènement.
	 *
	 * @return la source de l'évènement.
	 */
	public Object getSource() {
		return this.source;
	}

	/**
	 * Define source of event
	 * @param p_oSource source
	 */
	public void setSource(Object p_oSource) {
		this.source = p_oSource;
	}
	
	/**
	 * Return the array of denied permissions.
	 * @return the array of denied permissions
	 */
	public String[] getDeniedPermissions() {
		return this.deniedPermissions;
	}
}
