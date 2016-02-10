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

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;

/**
 * <p>TODO Décrire la classe MessagesListImpl</p>
 *
 *
 */
public class MessagesListImpl extends MessagesList {

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessagesList#getMessagesByLevel(com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessageLevel)
	 */
	@Override
	public List<ApplicationR> getMessagesByLevel(MessageLevel p_oLevelOfMessages) {
		List<ApplicationR> r_listMessages = new ArrayList<ApplicationR>();

		List<MessageContainer> oMessages= this.getMessagesContainerByLevel(p_oLevelOfMessages);
		if (oMessages != null) {
			for (MessageContainer oContainer : oMessages) {
				r_listMessages.add(oContainer.getMessage().getResource());
			}
		}
		return r_listMessages;
	}

}
