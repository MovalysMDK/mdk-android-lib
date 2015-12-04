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

/**
 * <p>Description d'un message</p>
 *
 *
 */
public interface ItfMessage {

	/**
	 * Donne la source du message
	 *
	 * @return la source du message
	 */
	public String getSource();

	/**
	 * identifiant du message.
	 *
	 * @return Donne l'identifiant du message
	 */
	public int getBodyId();

	/**
	 * identifiant du module qui a engendre le message
	 *
	 * @return l'identifiant du module
	 */
	public int getModuleId();

	/**
	 * niveau du message: ADEUZA, OK, INFO, WARNING, ERROR
	 *
	 * @return le niveau du message
	 */
	public MessageLevel getLevel();

	/**
	 * <p>getResource.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 */
	public ApplicationR getResource();
}
