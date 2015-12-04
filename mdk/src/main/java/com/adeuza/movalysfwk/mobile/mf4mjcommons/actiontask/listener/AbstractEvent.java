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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessagesList;

/**
 * <p>Value object utilisé pour passer des informations au méthode de "callback"</p>
 *
 *
 */
public abstract class AbstractEvent {

	/** messages d'erreur ou d'information en provenance de l'action */
	private MessagesList messages = null;

	/**
	 * Construction d'un value object de type "event"
	 *
	 * @param p_oMessages la liste des messages
	 * @param p_oActionIN In Action Parameter 
	 */
	public AbstractEvent(MessagesList p_oMessages) {
		this.messages = p_oMessages;
	}
	
	/**
	 * Donne la liste des messages
	 *
	 * @return la liste des messages en provenance de l'action
	 */
	public MessagesList getMessagesList() {
		return this.messages;
	}
}
