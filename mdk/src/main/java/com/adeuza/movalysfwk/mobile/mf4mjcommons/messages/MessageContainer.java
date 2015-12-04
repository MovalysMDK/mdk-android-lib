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


/**
 * <p>TODO Décrire la classe MessageContainer</p>
 *
 *
 */
public class MessageContainer {
	protected ItfMessage message;

	protected String source;

	protected String[] arguments;

	/**
	 * <p>Constructor for MessageContainer.</p>
	 *
	 * @param p_oMessage a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage} object.
	 * @param p_sSource a {@link java.lang.String} object.
	 * @param p_t_sArguments a {@link java.lang.String} object.
	 */
	protected MessageContainer(ItfMessage p_oMessage, String p_sSource, String ...p_t_sArguments) {
		this.message	= p_oMessage;
		this.source 	= p_sSource ;
		this.arguments	= p_t_sArguments;
	}
	/**
	 * Retourne l'objet arguments
	 *
	 * @return Objet arguments
	 */
	public String[] getArguments() {
		return this.arguments;
	}
	/**
	 * Retourne l'objet message
	 *
	 * @return Objet message
	 */
	public ItfMessage getMessage() {
		return this.message;
	}
	/**
	 * Retourne l'objet source
	 *
	 * @return Objet source
	 */
	public String getSource() {
		return this.source;
	}
}
