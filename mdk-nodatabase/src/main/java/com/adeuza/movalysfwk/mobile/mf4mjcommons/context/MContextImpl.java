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
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessageLevel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessagesList;


/**
 * <p>Transactional Context Implementation</p>
 *
 *
 */
public class MContextImpl implements MContext {

	protected boolean use = false;

	/**
	 * Messages
	 */
	private MessagesList messages;

	/**
	 * Last modification date
	 */
	private Timestamp currentmodificationdate;

	/**
	 * User's id.
	 */
	private long user;

	/**
	 * Liste des événements à publier
	 */
	private List<BusinessEvent<?>> events;

	/**
	 * Constructor
	 */
	public MContextImpl() {
		this.messages		= BeanLoader.getInstance().getBean(MessagesList.class);
		this.events			= new ArrayList<BusinessEvent<?>>();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext#getMessages()
	 */
	@Override
	public MessagesList getMessages() {
		return this.messages;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext#getCurrentLastModificationDate()
	 */
	@Override
	public Timestamp getCurrentModificationDate() {
		return this.currentmodificationdate;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext#setCurrentLastModificationDate(java.sql.Timestamp)
	 */
	@Override
	public void setCurrentModificationDate(Timestamp p_oCurrentLastModificationDate) {
		this.currentmodificationdate = p_oCurrentLastModificationDate;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext#getUserId()
	 */
	@Override
	public long getUserId() {
		return this.user;
	}
	
	/** {@inheritDoc} */
	@Override
	public void beginTransaction() {
		this.use = true;
	}

	/** {@inheritDoc} */
	@Override
	public void reset() {
		//Nothing To Do
	}
	
	/**
	 * Close the database
	 */
	@Override
	public void close() {
		// Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.MContext.fwk.mobile.javacommons.application.MMContext#endingTransaction()
	 */
	@Override
	public void endTransaction(){
		this.use = false;
	}
	
	/**
	 * <p>isInUse.</p>
	 *
	 * @return a boolean.
	 */
	@Override
	public boolean isInUse() {
		return this.use;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link java.util.List} object.
	 */
	@Override
	public List<BusinessEvent<?>> getEvents() {
		return this.events;
	}

	/** {@inheritDoc} */
	@Override
	public void registerEvent(BusinessEvent<?> p_oEvent) {
		this.events.add(p_oEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearEvents() {
		this.events.clear();
	}

	/** {@inheritDoc} */
	@Override
	public int getNumberOfMessagesByLevel(MessageLevel p_oLevel) {
		if (this.getMessages() != null && this.getMessages().hasMessagesByLevel(p_oLevel)) {
			return this.getMessages().getMessagesContainerByLevel(p_oLevel).size();
		}
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public void setEventsExitMode(boolean p_bExitMode) {
		if ( !this.getEvents().isEmpty()) {
			for(BusinessEvent<?> oEvent : this.getEvents()) {
				oEvent.setExitMode(p_bExitMode);
			}
		}
	}
}
