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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream;

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 * Traitement métier d'une partie d'un flux json (utilisé pendant la lecture en streaming d'un json).
 * Utilisable uniquement sur les attributs de type List
 *
 */
public interface StreamResponseProcessor<T, R extends IRestResponse> {

	/**
	 * <p>processResponsePart.</p>
	 *
	 * @param p_oList a {@link java.util.List} object.
	 * @param p_oContext context
	 * @param p_oRestResponse global restresponse (object is incomplete, depends on previous VDNStreamResponseProcessors)
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 */
	public void processResponsePart( List<T> p_oList, R p_oRestResponse, Notifier p_oNotifier, MContext p_oContext ) throws SynchroException;
	
	/**
	 * Nombre d'élements désirés pour chaque appel du processResponsePart
	 *
	 * @return a int.
	 */
	public int getPartSize();

	/**
	 * Called before looping over the list
	 *
	 * @param p_oContext context
	 * @param p_oRestResponse a R object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public void onStartLoop( R p_oRestResponse, Notifier p_oNotifier, MContext p_oContext ) throws SynchroException;
	
	/**
	 * Called After looping over the list
	 *
	 * @param p_oContext context
	 * @param p_oRestResponse a R object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public void onEndLoop( R p_oRestResponse, Notifier p_oNotifier, MContext p_oContext ) throws SynchroException;
	
	/**
	 * Initialize response processor
	 *
	 * @param p_oContext context
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public void initialize(MContext p_oContext) throws SynchroException;
}
