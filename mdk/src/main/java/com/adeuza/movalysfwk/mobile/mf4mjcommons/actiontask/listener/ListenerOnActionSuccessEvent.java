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
 * <p>Value object utilisé pour passer des informations au méthode de "callback" de type "ListenerOnActionSuccess"</p>
 *
 *
 */
public class ListenerOnActionSuccessEvent<RESULT extends ActionParameter> extends AbstractResultEvent<RESULT> {

	/**
	 * Contruit un nouveau value object
	 *
	 * @param p_oMessages la liste des messages en provenance de l'action
	 * @param p_oActionResult le résultat de l'action (ne peut pas être null)
	 * @param p_oSource a {@link java.lang.Object} object.
	 */
	public ListenerOnActionSuccessEvent(MessagesList p_oMessages, RESULT p_oActionResult, Object p_oSource) {
		super(p_oMessages, p_oActionResult, p_oSource, null);
	}
}
