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
 * <p>Value object utilisé pour passer des informations au méthode de "callback" de type "ListenerOnProgressAction"</p>
 */
public class ListenerOnProgressActionEvent<PROGRESS extends Object> extends AbstractEvent {

	/** les informations de progressions */
	private PROGRESS[] progressInformations = null;
	
	/**
	 * Construit un nouveau value object
	 *
	 * @param p_oMessages la liste des messages de l'action
	 * @param p_oProgress les éléments d'information de la progression
	 */
	public ListenerOnProgressActionEvent(MessagesList p_oMessages, PROGRESS...p_oProgress) {
		super(p_oMessages);
		this.progressInformations = p_oProgress;
	}
	
	/**
	 * Donne les informations de progression
	 *
	 * @return les informations de progression
	 */
	public PROGRESS[] getProgressInformations() {
		return this.progressInformations;
	}
}
