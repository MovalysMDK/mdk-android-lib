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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.context;

import java.sql.Timestamp;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessageLevel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessagesList;


/**
 * <p>Represents the context. A context hold database connection and a list of messages.</p>
 *
 *
 */
@Scope(ScopePolicy.PROTOTYPE)
public interface MContext {
	
	/**
	 * Return the messages list.
	 *
	 * @return The messages list.
	 */
	public MessagesList getMessages();

	/**
	 * Return the current modification date usable by {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.AbstractEntityDao#prepareToWrite}
	 *
	 * @return a {@link java.sql.Timestamp} object.
	 */
	public Timestamp getCurrentModificationDate();

	/**
	 * Define the current modification date usable by {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.AbstractEntityDao#prepareToWrite}
	 *
	 * @param p_oCurrentModificationDate a {@link java.sql.Timestamp} object.
	 */
	public void setCurrentModificationDate(Timestamp p_oCurrentModificationDate);
	
	/**
	 * Return the current user id.
	 *
	 * @return the current user id.
	 */
	public long getUserId();
	
	/**
	 * Start sql transaction
	 */
	public void beginTransaction();
	
	/**
	 * End sql transaction, if the list has an error message
	 * then the transaction is rolledbacked, otherwise transaction is commited
	 */
	public void endTransaction();
	
	/**
	 * Reset error and information sets,  and redirection information
	 */
	public void reset();
	
	/**
	 * Close the context
	 */
	public void close();
	
	/**
	 * <p>isInUse.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isInUse();

	/**
	 * Returns all registered events.
	 *
	 * @return all registered events.
	 */
	public List<BusinessEvent<?>> getEvents();

	/**
	 * SETTER
	 *
	 * @param p_bExitMode exit mode
	 */
	public void setEventsExitMode(boolean p_bExitMode);
	
	
	/**
	 * Register an event
	 *
	 * @param p_oEvent The event to register
	 */
	public void registerEvent(final BusinessEvent<?> p_oEvent);

	/**
	 * Clears the collection of the registered events.
	 */
	public void clearEvents();
	
	/**
	 * Returns the number of messages by a given MessageLevel
	 *
	 * @param p_oLevel a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessageLevel} object.
	 * @return a int.
	 */
	public int getNumberOfMessagesByLevel(MessageLevel p_oLevel);
}
